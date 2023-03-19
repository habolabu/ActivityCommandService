package edu.ou.activitycommandservice.service.post;

import edu.ou.activitycommandservice.common.constant.CodeStatus;
import edu.ou.activitycommandservice.data.pojo.request.post.PostDeleteRequest;
import edu.ou.coreservice.common.constant.Message;
import edu.ou.coreservice.common.exception.BusinessException;
import edu.ou.coreservice.common.validate.ValidValidation;
import edu.ou.coreservice.data.pojo.request.base.IBaseRequest;
import edu.ou.coreservice.data.pojo.response.base.IBaseResponse;
import edu.ou.coreservice.data.pojo.response.impl.SuccessPojo;
import edu.ou.coreservice.data.pojo.response.impl.SuccessResponse;
import edu.ou.coreservice.queue.activity.internal.post.PostDeleteQueueI;
import edu.ou.coreservice.repository.base.IBaseRepository;
import edu.ou.coreservice.service.base.BaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class PostDeleteService extends BaseService<IBaseRequest, IBaseResponse> {
    private final IBaseRepository<String, String> postDeleteRepository;
    private final IBaseRepository<String, Boolean> postCheckExistBySlugRepository;
    private final RabbitTemplate rabbitTemplate;
    private final ValidValidation validValidation;

    /**
     * Validate post delete request
     *
     * @param request post delete request
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected void preExecute(IBaseRequest request) {
        if (validValidation.isInValid(request, PostDeleteRequest.class)) {
            throw new BusinessException(
                    CodeStatus.INVALID_INPUT,
                    Message.Error.INVALID_INPUT,
                    "post"
            );
        }
    }

    /**
     * Delete exist post
     *
     * @param request post delete request
     * @return slug of exist post
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected IBaseResponse doExecute(IBaseRequest request) {
        final PostDeleteRequest postDeleteRequest = (PostDeleteRequest) request;

        if (!postCheckExistBySlugRepository.execute(postDeleteRequest.getSlug())) {
            throw new BusinessException(
                    CodeStatus.NOT_FOUND,
                    Message.Error.NOT_FOUND,
                    "post",
                    "post slug",
                    postDeleteRequest.getSlug()
            );
        }

        final String resultSlug = postDeleteRepository.execute(postDeleteRequest.getSlug());

        rabbitTemplate.convertSendAndReceive(
                PostDeleteQueueI.EXCHANGE,
                PostDeleteQueueI.ROUTING_KEY,
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
