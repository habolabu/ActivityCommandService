package edu.ou.activitycommandservice.service.feedBackType;


import edu.ou.activitycommandservice.common.constant.CodeStatus;
import edu.ou.activitycommandservice.common.mapper.FeedBackTypeMapper;
import edu.ou.activitycommandservice.data.entity.FeedBackTypeEntity;
import edu.ou.activitycommandservice.data.pojo.request.feedBackType.FeedBackTypeUpdateRequest;
import edu.ou.coreservice.common.constant.Message;
import edu.ou.coreservice.common.exception.BusinessException;
import edu.ou.coreservice.common.validate.ValidValidation;
import edu.ou.coreservice.data.pojo.request.base.IBaseRequest;
import edu.ou.coreservice.data.pojo.response.base.IBaseResponse;
import edu.ou.coreservice.data.pojo.response.impl.SuccessPojo;
import edu.ou.coreservice.data.pojo.response.impl.SuccessResponse;
import edu.ou.coreservice.queue.activity.internal.feedBackType.FeedBackTypeUpdateQueueI;
import edu.ou.coreservice.repository.base.IBaseRepository;
import edu.ou.coreservice.service.base.BaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class FeedBackTypeUpdateService extends BaseService<IBaseRequest, IBaseResponse> {
    private final IBaseRepository<FeedBackTypeEntity, Integer> feedBackTypeUpdateRepository;
    private final IBaseRepository<Integer, Boolean> feedBackTypeCheckExistByIdRepository;
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
        if (validValidation.isInValid(request, FeedBackTypeUpdateRequest.class)) {
            throw new BusinessException(
                    CodeStatus.INVALID_INPUT,
                    Message.Error.INVALID_INPUT,
                    "feedback type"
            );
        }
    }

    /**
     * Update exist feedback type
     *
     * @param request request from client
     * @return response to client
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected IBaseResponse doExecute(IBaseRequest request) {
        final FeedBackTypeEntity feedBackTypeEntity = FeedBackTypeMapper.INSTANCE
                .fromFeedBackTypeUpdateRequest((FeedBackTypeUpdateRequest) request);

        if (!feedBackTypeCheckExistByIdRepository.execute(feedBackTypeEntity.getId())) {
            throw new BusinessException(
                    CodeStatus.NOT_FOUND,
                    Message.Error.NOT_FOUND,
                    "feedback type",
                    "feedback type identity",
                    feedBackTypeEntity.getId()
            );
        }

        final int feedBackTypeId = feedBackTypeUpdateRepository.execute(feedBackTypeEntity);

        rabbitTemplate.convertSendAndReceive(
                FeedBackTypeUpdateQueueI.EXCHANGE,
                FeedBackTypeUpdateQueueI.ROUTING_KEY,
                feedBackTypeEntity
        );

        return new SuccessResponse<>(
                new SuccessPojo<>(
                        feedBackTypeId,
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
