package edu.ou.activitycommandservice.service.feedBackType;

import edu.ou.activitycommandservice.common.constant.CodeStatus;
import edu.ou.activitycommandservice.common.mapper.FeedBackTypeMapper;
import edu.ou.activitycommandservice.data.entity.FeedBackTypeEntity;
import edu.ou.activitycommandservice.data.pojo.request.feedBackType.FeedBackTypeAddRequest;
import edu.ou.coreservice.common.constant.Message;
import edu.ou.coreservice.common.exception.BusinessException;
import edu.ou.coreservice.common.validate.ValidValidation;
import edu.ou.coreservice.data.pojo.request.base.IBaseRequest;
import edu.ou.coreservice.data.pojo.response.base.IBaseResponse;
import edu.ou.coreservice.data.pojo.response.impl.SuccessPojo;
import edu.ou.coreservice.data.pojo.response.impl.SuccessResponse;
import edu.ou.coreservice.queue.activity.internal.feedBackType.FeedBackTypeAddQueueI;
import edu.ou.coreservice.repository.base.IBaseRepository;
import edu.ou.coreservice.service.base.BaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class FeedBackTypeAddService extends BaseService<IBaseRequest, IBaseResponse> {
    private final IBaseRepository<FeedBackTypeEntity, Integer> feedBackTypeAddRepository;
    private final IBaseRepository<String, FeedBackTypeEntity> feedBackTypeFindBySlugWithDeletedRepository;
    private final IBaseRepository<String, Boolean> feedBackTypeCheckDeleteRepository;
    private final IBaseRepository<FeedBackTypeEntity, Integer> feedBackTypeDeActiveRepository;
    private final RabbitTemplate rabbitTemplate;
    private final ValidValidation validValidation;

    /**
     * Validate feedback type add request
     *
     * @param request feedback type add request
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected void preExecute(IBaseRequest request) {
        if (validValidation.isInValid(request, FeedBackTypeAddRequest.class)) {
            throw new BusinessException(
                    CodeStatus.INVALID_INPUT,
                    Message.Error.INVALID_INPUT,
                    "feedback type"
            );
        }
    }

    /**
     * Add new feedback type
     *
     * @param request feedback type add request
     * @return id of new feedback type
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected IBaseResponse doExecute(IBaseRequest request) {
        final FeedBackTypeEntity feedBackTypeEntity = FeedBackTypeMapper.INSTANCE
                .fromFeedBackTypeAddRequest((FeedBackTypeAddRequest) request);

        int feedBackTypeId;

        if (feedBackTypeCheckDeleteRepository.execute(feedBackTypeEntity.getSlug())) {
            final FeedBackTypeEntity existDeletedFeedBackTypeEntity =
                    feedBackTypeFindBySlugWithDeletedRepository.execute(feedBackTypeEntity.getSlug());
            feedBackTypeEntity.setId(existDeletedFeedBackTypeEntity.getId());
            feedBackTypeId = feedBackTypeDeActiveRepository.execute(feedBackTypeEntity);

        } else {
            feedBackTypeId = feedBackTypeAddRepository.execute(feedBackTypeEntity);
        }

        feedBackTypeEntity.setId(feedBackTypeId);

        rabbitTemplate.convertSendAndReceive(
                FeedBackTypeAddQueueI.EXCHANGE,
                FeedBackTypeAddQueueI.ROUTING_KEY,
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
    protected void postExecute(IBaseRequest request) {
        // do nothing
    }
}
