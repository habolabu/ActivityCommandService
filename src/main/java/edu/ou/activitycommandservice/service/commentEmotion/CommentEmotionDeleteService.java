package edu.ou.activitycommandservice.service.commentEmotion;

import edu.ou.activitycommandservice.common.constant.CodeStatus;
import edu.ou.activitycommandservice.common.mapper.CommentEmotionMapper;
import edu.ou.activitycommandservice.data.entity.CommentEmotionEntityPK;
import edu.ou.activitycommandservice.data.entity.CommentEntity;
import edu.ou.activitycommandservice.data.pojo.request.commentEmotion.CommentEmotionDeleteRequest;
import edu.ou.coreservice.common.constant.Message;
import edu.ou.coreservice.common.exception.BusinessException;
import edu.ou.coreservice.common.validate.ValidValidation;
import edu.ou.coreservice.data.pojo.request.base.IBaseRequest;
import edu.ou.coreservice.data.pojo.response.base.IBaseResponse;
import edu.ou.coreservice.data.pojo.response.impl.SuccessPojo;
import edu.ou.coreservice.data.pojo.response.impl.SuccessResponse;
import edu.ou.coreservice.queue.activity.internal.comment.CommentUpdateQueueI;
import edu.ou.coreservice.queue.activity.internal.commentEmotion.CommentEmotionDeleteQueueI;
import edu.ou.coreservice.repository.base.IBaseRepository;
import edu.ou.coreservice.service.base.BaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentEmotionDeleteService extends BaseService<IBaseRequest, IBaseResponse> {
    private final IBaseRepository<CommentEmotionEntityPK, CommentEmotionEntityPK> commentEmotionDeleteRepository;
    private final IBaseRepository<CommentEmotionEntityPK, Boolean> commentEmotionCheckExistByIdRepository;
    private final IBaseRepository<Integer, CommentEntity> commentFindByIdRepository;
    private final IBaseRepository<CommentEntity, Integer> commentUpdateRepository;
    private final CommentEmotionMapper commentEmotionMapper;
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
        if (validValidation.isInValid(request, CommentEmotionDeleteRequest.class)) {
            throw new BusinessException(
                    CodeStatus.INVALID_INPUT,
                    Message.Error.INVALID_INPUT,
                    "comment emotion identity"

            );
        }
    }

    /**
     * Delete exist comment emotion
     *
     * @param request request from client
     * @return response to client
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected IBaseResponse doExecute(IBaseRequest request) {
        final CommentEmotionDeleteRequest commentEmotionDeleteRequest = (CommentEmotionDeleteRequest) request;
        final CommentEmotionEntityPK commentEmotionEntityPK = commentEmotionMapper
                .fromCommentEmotionDeleteRequest(commentEmotionDeleteRequest);

        if (!commentEmotionCheckExistByIdRepository.execute(commentEmotionEntityPK)) {
            throw new BusinessException(
                    CodeStatus.NOT_FOUND,
                    Message.Error.NOT_FOUND,
                    "comment emotion",
                    "comment emotion identity",
                    commentEmotionEntityPK.getCommentId()
            );
        }


        final CommentEmotionEntityPK deleteResult = commentEmotionDeleteRepository.execute(commentEmotionEntityPK);

        rabbitTemplate.convertSendAndReceive(
                CommentEmotionDeleteQueueI.EXCHANGE,
                CommentEmotionDeleteQueueI.ROUTING_KEY,
                deleteResult
        );

        final CommentEntity commentEntity = commentFindByIdRepository.execute(commentEmotionEntityPK.getCommentId());
        commentEntity.setTotalEmotion(commentEntity.getTotalEmotion() - 1);
        commentUpdateRepository.execute(commentEntity);

        rabbitTemplate.convertSendAndReceive(
                CommentUpdateQueueI.EXCHANGE,
                CommentUpdateQueueI.ROUTING_KEY,
                commentEntity
        );

        return new SuccessResponse<>(
                new SuccessPojo<>(
                        deleteResult,
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
