package edu.ou.activitycommandservice.service.feedBack;

import edu.ou.activitycommandservice.common.constant.CodeStatus;
import edu.ou.activitycommandservice.common.mapper.FeedBackMapper;
import edu.ou.activitycommandservice.data.entity.FeedBackEntity;
import edu.ou.activitycommandservice.data.pojo.request.feedBack.FeedBackAddRequest;
import edu.ou.coreservice.common.constant.Message;
import edu.ou.coreservice.common.exception.BusinessException;
import edu.ou.coreservice.common.validate.ValidValidation;
import edu.ou.coreservice.data.pojo.request.base.IBaseRequest;
import edu.ou.coreservice.data.pojo.response.base.IBaseResponse;
import edu.ou.coreservice.data.pojo.response.impl.SuccessPojo;
import edu.ou.coreservice.data.pojo.response.impl.SuccessResponse;
import edu.ou.coreservice.queue.activity.internal.feedBack.FeedBackAddQueueI;
import edu.ou.coreservice.repository.base.IBaseRepository;
import edu.ou.coreservice.service.base.BaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class FeedBackAddService extends BaseService<IBaseRequest, IBaseResponse> {
    private final IBaseRepository<FeedBackEntity, Integer> feedBackAddRepository;
    private final IBaseRepository<String, FeedBackEntity> feedBackFindBySlugWithDeletedRepository;
    private final IBaseRepository<String, Boolean> feedBackCheckDeleteRepository;
    private final IBaseRepository<FeedBackEntity, Integer> feedBackDeActiveRepository;
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
        if (validValidation.isInValid(request, FeedBackAddRequest.class)) {
            throw new BusinessException(
                    CodeStatus.INVALID_INPUT,
                    Message.Error.INVALID_INPUT,
                    "apartment"

            );
        }
    }

    /**
     * Add new apartment
     *
     * @param request request from client
     * @return response to client
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected IBaseResponse doExecute(IBaseRequest request) {
        final FeedBackEntity feedBackEntity = FeedBackMapper.INSTANCE
                .fromFeedBackAddRequest((FeedBackAddRequest) request);

        if (!feedBackTypeCheckExistByIdRepository.execute(feedBackEntity.getFeedBackTypeId())) {
            throw new BusinessException(
                    CodeStatus.NOT_FOUND,
                    Message.Error.NOT_FOUND,
                    "feedback type",
                    "feedback type identity",
                    feedBackEntity.getFeedBackTypeId()
            );
        }

        int feedBackId;

        if (feedBackCheckDeleteRepository.execute(feedBackEntity.getSlug())) {
            final FeedBackEntity existDeletedFeedBackEntity =
                    feedBackFindBySlugWithDeletedRepository.execute(feedBackEntity.getSlug());

            feedBackEntity.setId(existDeletedFeedBackEntity.getId());
            feedBackId = feedBackDeActiveRepository.execute(feedBackEntity);

        } else {
            feedBackId = feedBackAddRepository.execute(feedBackEntity);
        }

        feedBackEntity.setId(feedBackId);

        rabbitTemplate.convertSendAndReceive(
                FeedBackAddQueueI.EXCHANGE,
                FeedBackAddQueueI.ROUTING_KEY,
                feedBackEntity
        );

        return new SuccessResponse<>(
                new SuccessPojo<>(
                        feedBackId,
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
