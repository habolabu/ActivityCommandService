package edu.ou.activitycommandservice.service.emotion;

import edu.ou.activitycommandservice.common.constant.CodeStatus;
import edu.ou.activitycommandservice.common.mapper.EmotionMapper;
import edu.ou.activitycommandservice.data.entity.EmotionEntity;
import edu.ou.activitycommandservice.data.pojo.request.emotion.EmotionAddRequest;
import edu.ou.coreservice.common.constant.Message;
import edu.ou.coreservice.common.exception.BusinessException;
import edu.ou.coreservice.common.validate.ValidValidation;
import edu.ou.coreservice.data.pojo.request.base.IBaseRequest;
import edu.ou.coreservice.data.pojo.response.base.IBaseResponse;
import edu.ou.coreservice.data.pojo.response.impl.SuccessPojo;
import edu.ou.coreservice.data.pojo.response.impl.SuccessResponse;
import edu.ou.coreservice.queue.activity.internal.emotion.EmotionAddQueueI;
import edu.ou.coreservice.repository.base.IBaseRepository;
import edu.ou.coreservice.service.base.BaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class EmotionAddService extends BaseService<IBaseRequest, IBaseResponse> {
    private final IBaseRepository<EmotionEntity, Integer> emotionAddRepository;
    private final EmotionMapper emotionMapper;
    private final RabbitTemplate rabbitTemplate;
    private final ValidValidation validValidation;


    /**
     * Validate request
     *
     * @param request input of task
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected void preExecute(IBaseRequest request) {
        if (validValidation.isInValid(request, EmotionAddRequest.class)) {
            throw new BusinessException(
                    CodeStatus.INVALID_INPUT,
                    Message.Error.INVALID_INPUT,
                    "emotion"

            );
        }
    }

    /**
     * Add new emotion
     *
     * @param request request from client
     * @return response to client
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected IBaseResponse doExecute(IBaseRequest request) {
        final EmotionEntity emotionEntity = emotionMapper.fromEmotionAddRequest((EmotionAddRequest) request);
        final int emotionId = emotionAddRepository.execute(emotionEntity);
        emotionEntity.setId(emotionId);

        rabbitTemplate.convertSendAndReceive(
                EmotionAddQueueI.EXCHANGE,
                EmotionAddQueueI.ROUTING_KEY,
                emotionEntity
        );

        return new SuccessResponse<>(
                new SuccessPojo<>(
                        emotionId,
                        CodeStatus.SUCCESS,
                        Message.Success.SUCCESSFUL
                )
        );
    }

    @Override
    protected void postExecute(IBaseRequest input) {
        // do nothing
    }
}
