package edu.ou.activitycommandservice.service.emotion;

import edu.ou.activitycommandservice.common.constant.CodeStatus;
import edu.ou.activitycommandservice.data.pojo.request.emotion.EmotionDeleteRequest;
import edu.ou.coreservice.common.constant.Message;
import edu.ou.coreservice.common.exception.BusinessException;
import edu.ou.coreservice.common.validate.ValidValidation;
import edu.ou.coreservice.data.pojo.request.base.IBaseRequest;
import edu.ou.coreservice.data.pojo.response.base.IBaseResponse;
import edu.ou.coreservice.data.pojo.response.impl.SuccessPojo;
import edu.ou.coreservice.data.pojo.response.impl.SuccessResponse;
import edu.ou.coreservice.queue.activity.internal.emotion.EmotionDeleteQueueI;
import edu.ou.coreservice.repository.base.IBaseRepository;
import edu.ou.coreservice.service.base.BaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class EmotionDeleteService extends BaseService<IBaseRequest, IBaseResponse> {
    private final IBaseRepository<Integer, Integer> emotionDeleteRepository;
    private final IBaseRepository<Integer, Boolean> emotionCheckExistByIdRepository;
    private final IBaseRepository<Integer, Boolean> emotionHasReferencesRepository;
    private final RabbitTemplate rabbitTemplate;
    private final ValidValidation validValidation;

    /**
     * Validate request
     *
     * @param request request
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected void preExecute(IBaseRequest request) {
        if (validValidation.isInValid(request, EmotionDeleteRequest.class)) {
            throw new BusinessException(
                    CodeStatus.INVALID_INPUT,
                    Message.Error.INVALID_INPUT,
                    "emotion"

            );
        }
    }

    /**
     * Delete exist emotion
     *
     * @param request request from client
     * @return response to client
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected IBaseResponse doExecute(IBaseRequest request) {
        final EmotionDeleteRequest emotionDeleteRequest = (EmotionDeleteRequest) request;

        if (!emotionCheckExistByIdRepository.execute(emotionDeleteRequest.getId())) {
            throw new BusinessException(
                    CodeStatus.NOT_FOUND,
                    Message.Error.NOT_FOUND,
                    "emotion",
                    "emotion identity",
                    emotionDeleteRequest.getId()
            );
        }

        if (emotionHasReferencesRepository.execute(emotionDeleteRequest.getId())) {
            throw new BusinessException(
                    CodeStatus.NOT_EMPTY,
                    Message.Error.NOT_EMPTY,
                    "emotion"
            );
        }

        final int resultSlug = emotionDeleteRepository.execute(emotionDeleteRequest.getId());

        rabbitTemplate.convertSendAndReceive(
                EmotionDeleteQueueI.EXCHANGE,
                EmotionDeleteQueueI.ROUTING_KEY,
                resultSlug
        );

        return new SuccessResponse<>(
                new SuccessPojo<>(
                        resultSlug,
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
