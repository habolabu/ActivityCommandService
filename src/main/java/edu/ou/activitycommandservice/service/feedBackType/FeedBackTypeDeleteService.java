package edu.ou.activitycommandservice.service.feedBackType;

import edu.ou.activitycommandservice.common.constant.CodeStatus;
import edu.ou.activitycommandservice.data.pojo.request.feedBackType.FeedBackTypeDeleteRequest;
import edu.ou.coreservice.common.constant.Message;
import edu.ou.coreservice.common.exception.BusinessException;
import edu.ou.coreservice.common.validate.ValidValidation;
import edu.ou.coreservice.data.pojo.request.base.IBaseRequest;
import edu.ou.coreservice.data.pojo.response.base.IBaseResponse;
import edu.ou.coreservice.data.pojo.response.impl.SuccessPojo;
import edu.ou.coreservice.data.pojo.response.impl.SuccessResponse;
import edu.ou.coreservice.queue.activity.internal.feedBackType.FeedBackTypeDeleteQueueI;
import edu.ou.coreservice.repository.base.IBaseRepository;
import edu.ou.coreservice.service.base.BaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class FeedBackTypeDeleteService extends BaseService<IBaseRequest, IBaseResponse> {
    private final IBaseRepository<String, String> feedBackTypeDeleteRepository;
    private final IBaseRepository<String, Boolean> feedBackTypeCheckExistBySlugRepository;
    private final IBaseRepository<String, Boolean> feedBackTypeHasFeedBacksRepository;
    private final RabbitTemplate rabbitTemplate;
    private final ValidValidation validValidation;

    /**
     * Validate feedback type delete request
     *
     * @param request feedback type delete request
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected void preExecute(IBaseRequest request) {
        if (validValidation.isInValid(request, FeedBackTypeDeleteRequest.class)) {
            throw new BusinessException(
                    CodeStatus.INVALID_INPUT,
                    Message.Error.INVALID_INPUT,
                    "feedback type"
            );
        }
    }

    /**
     * Delete exist feedback type
     *
     * @param request feedback type delete request
     * @return slug of exist feedback type
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected IBaseResponse doExecute(IBaseRequest request) {
        final FeedBackTypeDeleteRequest feedBackTypeDeleteRequest = (FeedBackTypeDeleteRequest) request;

        if (!feedBackTypeCheckExistBySlugRepository.execute(feedBackTypeDeleteRequest.getSlug())) {
            throw new BusinessException(
                    CodeStatus.NOT_FOUND,
                    Message.Error.NOT_FOUND,
                    "feedback type",
                    "feedback type slug",
                    feedBackTypeDeleteRequest.getSlug()
            );
        }

        if (feedBackTypeHasFeedBacksRepository.execute(feedBackTypeDeleteRequest.getSlug())) {
            throw new BusinessException(
                    CodeStatus.NOT_EMPTY,
                    Message.Error.NOT_EMPTY,
                    "feedback type"
            );
        }

        final String resultSlug = feedBackTypeDeleteRepository.execute(feedBackTypeDeleteRequest.getSlug());

        rabbitTemplate.convertSendAndReceive(
                FeedBackTypeDeleteQueueI.EXCHANGE,
                FeedBackTypeDeleteQueueI.ROUTING_KEY,
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
    protected void postExecute(IBaseRequest request) {
        // do nothing
    }
}
