package edu.ou.activitycommandservice.service.comment;

import edu.ou.activitycommandservice.common.constant.CodeStatus;
import edu.ou.activitycommandservice.common.mapper.CommentMapper;
import edu.ou.activitycommandservice.data.entity.CommentEntity;
import edu.ou.activitycommandservice.data.pojo.request.comment.CommentOwnerCheckRequest;
import edu.ou.activitycommandservice.data.pojo.request.comment.CommentUpdateRequest;
import edu.ou.coreservice.common.constant.Message;
import edu.ou.coreservice.common.exception.BusinessException;
import edu.ou.coreservice.common.util.SecurityUtils;
import edu.ou.coreservice.common.validate.ValidValidation;
import edu.ou.coreservice.data.pojo.request.base.IBaseRequest;
import edu.ou.coreservice.data.pojo.response.base.IBaseResponse;
import edu.ou.coreservice.data.pojo.response.impl.SuccessPojo;
import edu.ou.coreservice.data.pojo.response.impl.SuccessResponse;
import edu.ou.coreservice.queue.activity.internal.comment.CommentUpdateQueueI;
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
public class CommentUpdateService extends BaseService<IBaseRequest, IBaseResponse> {
    private final IBaseRepository<CommentEntity, Integer> commentUpdateRepository;
    private final IBaseRepository<Integer, Boolean> commentCheckExistByIdRepository;
    private final IBaseRepository<CommentOwnerCheckRequest, Boolean> commentCheckOwnerRepository;
    private final CommentMapper commentMapper;
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
        if (validValidation.isInValid(request, CommentUpdateRequest.class)) {
            throw new BusinessException(
                    CodeStatus.INVALID_INPUT,
                    Message.Error.INVALID_INPUT,
                    "comment"
            );
        }
    }

    /**
     * Update exist comment
     *
     * @param request request from client
     * @return response to client
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected IBaseResponse doExecute(IBaseRequest request) {
        final CommentEntity commentEntity = commentMapper.fromCommentUpdateRequest((CommentUpdateRequest) request);

        if (!commentCheckExistByIdRepository.execute(commentEntity.getId())) {
            throw new BusinessException(
                    CodeStatus.NOT_FOUND,
                    Message.Error.NOT_FOUND,
                    "comment",
                    "comment identity",
                    commentEntity.getId()
            );
        }

        final Map<String, String> currentAccountInfo = SecurityUtils.getCurrentAccount(rabbitTemplate);
        final int userId = Integer.parseInt(currentAccountInfo.get("userId"));
        final CommentOwnerCheckRequest commentOwnerCheckRequest = new CommentOwnerCheckRequest()
                .setCommentId(commentEntity.getId())
                .setUserId(userId);

        if (!commentCheckOwnerRepository.execute(commentOwnerCheckRequest)) {
            throw new BusinessException(
                    CodeStatus.INVALID_INPUT,
                    Message.Error.UPDATE_FAIL,
                    "comment"
            );
        }

        commentEntity.setUserId(userId);
        final int commentId = commentUpdateRepository.execute(commentEntity);

        rabbitTemplate.convertSendAndReceive(
                CommentUpdateQueueI.EXCHANGE,
                CommentUpdateQueueI.ROUTING_KEY,
                commentEntity
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
