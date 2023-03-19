package edu.ou.activitycommandservice.service.comment;

import edu.ou.activitycommandservice.common.constant.CodeStatus;
import edu.ou.activitycommandservice.data.entity.CommentEntity;
import edu.ou.activitycommandservice.data.entity.PostEntity;
import edu.ou.activitycommandservice.data.pojo.request.comment.CommentDeleteRequest;
import edu.ou.activitycommandservice.data.pojo.request.comment.CommentOwnerCheckRequest;
import edu.ou.coreservice.common.constant.Message;
import edu.ou.coreservice.common.exception.BusinessException;
import edu.ou.coreservice.common.util.SecurityUtils;
import edu.ou.coreservice.common.validate.ValidValidation;
import edu.ou.coreservice.data.pojo.request.base.IBaseRequest;
import edu.ou.coreservice.data.pojo.response.base.IBaseResponse;
import edu.ou.coreservice.data.pojo.response.impl.SuccessPojo;
import edu.ou.coreservice.data.pojo.response.impl.SuccessResponse;
import edu.ou.coreservice.queue.activity.internal.comment.CommentDeleteQueueI;
import edu.ou.coreservice.queue.activity.internal.post.PostUpdateQueueI;
import edu.ou.coreservice.repository.base.IBaseRepository;
import edu.ou.coreservice.service.base.BaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentDeleteService extends BaseService<IBaseRequest, IBaseResponse> {
    private final IBaseRepository<Integer, Integer> commentDeleteRepository;
    private final IBaseRepository<CommentOwnerCheckRequest, Boolean> commentCheckOwnerRepository;
    private final IBaseRepository<Integer, Boolean> commentCheckExistByIdRepository;
    private final IBaseRepository<Integer, CommentEntity> commentFindByIdRepository;
    private final IBaseRepository<PostEntity, Integer> postUpdateRepository;
    private final IBaseRepository<Integer, PostEntity> postFindByIdRepository;
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
        if (validValidation.isInValid(request, CommentDeleteRequest.class)) {
            throw new BusinessException(
                    CodeStatus.INVALID_INPUT,
                    Message.Error.INVALID_INPUT,
                    "comment"

            );
        }
    }

    /**
     * Delete exist comment
     *
     * @param request request from client
     * @return response to client
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected IBaseResponse doExecute(IBaseRequest request) {
        final CommentDeleteRequest commentDeleteRequest = (CommentDeleteRequest) request;

        if (!commentCheckExistByIdRepository.execute(commentDeleteRequest.getId())) {
            throw new BusinessException(
                    CodeStatus.NOT_FOUND,
                    Message.Error.NOT_FOUND,
                    "comment",
                    "emotion identity",
                    commentDeleteRequest.getId()
            );
        }

        final Map<String, String> currentAccountInfo = SecurityUtils.getCurrentAccount(rabbitTemplate);
        final int userId = Integer.parseInt(currentAccountInfo.get("userId"));
        final CommentOwnerCheckRequest commentOwnerCheckRequest = new CommentOwnerCheckRequest()
                .setCommentId(commentDeleteRequest.getId())
                .setUserId(userId);

        if (!commentCheckOwnerRepository.execute(commentOwnerCheckRequest)) {
            throw new BusinessException(
                    CodeStatus.INVALID_INPUT,
                    Message.Error.DELETE_FAIL,
                    "comment",
                    "comment identity",
                    commentDeleteRequest.getId()
            );
        }

        final CommentEntity commentEntity = commentFindByIdRepository.execute(commentDeleteRequest.getId());
        final int commentId = commentDeleteRepository.execute(commentDeleteRequest.getId());

        rabbitTemplate.convertSendAndReceive(
                CommentDeleteQueueI.EXCHANGE,
                CommentDeleteQueueI.ROUTING_KEY,
                commentId
        );

        final PostEntity postEntity = postFindByIdRepository.execute(commentEntity.getPostId());
        postEntity.setTotalComment(postEntity.getTotalComment() - 1);
        postUpdateRepository.execute(postEntity);

        rabbitTemplate.convertSendAndReceive(
                PostUpdateQueueI.EXCHANGE,
                PostUpdateQueueI.ROUTING_KEY,
                postEntity
        );

        return new SuccessResponse<>(
                new SuccessPojo<>(
                        commentId,
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
