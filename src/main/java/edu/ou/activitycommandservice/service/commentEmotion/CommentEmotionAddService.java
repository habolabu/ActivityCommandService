package edu.ou.activitycommandservice.service.commentEmotion;

import edu.ou.activitycommandservice.common.constant.CodeStatus;
import edu.ou.activitycommandservice.common.mapper.CommentEmotionMapper;
import edu.ou.activitycommandservice.data.entity.CommentEmotionEntity;
import edu.ou.activitycommandservice.data.entity.CommentEmotionEntityPK;
import edu.ou.activitycommandservice.data.entity.CommentEntity;
import edu.ou.activitycommandservice.data.pojo.request.commentEmotion.CommentEmotionAddRequest;
import edu.ou.coreservice.common.constant.Message;
import edu.ou.coreservice.common.exception.BusinessException;
import edu.ou.coreservice.common.validate.ValidValidation;
import edu.ou.coreservice.data.pojo.request.base.IBaseRequest;
import edu.ou.coreservice.data.pojo.response.base.IBaseResponse;
import edu.ou.coreservice.data.pojo.response.impl.SuccessPojo;
import edu.ou.coreservice.data.pojo.response.impl.SuccessResponse;
import edu.ou.coreservice.queue.activity.internal.comment.CommentUpdateQueueI;
import edu.ou.coreservice.queue.activity.internal.commentEmotion.CommentEmotionAddQueueI;
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
public class CommentEmotionAddService extends BaseService<IBaseRequest, IBaseResponse> {
    private final IBaseRepository<CommentEmotionEntity, CommentEmotionEntityPK> commentEmotionAddRepository;
    private final IBaseRepository<CommentEmotionEntityPK, CommentEmotionEntityPK> commentEmotionDeleteRepository;
    private final IBaseRepository<CommentEmotionEntityPK, Boolean> commentEmotionCheckExistByIdRepository;
    private final IBaseRepository<Integer, Boolean> commentCheckExistByIdRepository;
    private final IBaseRepository<Integer, Boolean> emotionCheckExistByIdRepository;
    private final IBaseRepository<Integer, CommentEntity> commentFindByIdRepository;
    private final IBaseRepository<CommentEntity, Integer> commentUpdateRepository;
    private final CommentEmotionMapper commentEmotionMapper;
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
        if (validValidation.isInValid(request, CommentEmotionAddRequest.class)) {
            throw new BusinessException(
                    CodeStatus.INVALID_INPUT,
                    Message.Error.INVALID_INPUT,
                    "comment emotion"

            );
        }
    }

    /**
     * Add new comment emotion
     *
     * @param request request from client
     * @return response to client
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected IBaseResponse doExecute(IBaseRequest request) {
        final CommentEmotionEntity commentEmotionEntity = commentEmotionMapper
                .fromCommentEmotionAddRequest((CommentEmotionAddRequest) request);
        final CommentEmotionEntityPK commentEmotionEntityPK = new CommentEmotionEntityPK()
                .setCommentId(commentEmotionEntity.getCommentId())
                .setUserId(commentEmotionEntity.getUserId());

        if (commentCheckExistByIdRepository.execute(commentEmotionEntityPK.getCommentId())) {
            throw new BusinessException(
                    CodeStatus.NOT_FOUND,
                    Message.Error.NOT_FOUND,
                    "comment",
                    "comment identity",
                    commentEmotionEntityPK.getCommentId()
            );
        }

        if (emotionCheckExistByIdRepository.execute(commentEmotionEntityPK.getEmotionId())) {
            throw new BusinessException(
                    CodeStatus.NOT_FOUND,
                    Message.Error.NOT_FOUND,
                    "emotion",
                    "emotion identity",
                    commentEmotionEntityPK.getEmotionId()
            );
        }

        final CommentEntity commentEntity = commentFindByIdRepository.execute(commentEmotionEntityPK.getCommentId());

        if (commentEmotionCheckExistByIdRepository.execute(commentEmotionEntityPK)) {
            final CommentEmotionEntityPK updateResult = commentEmotionDeleteRepository.execute(commentEmotionEntityPK);

            rabbitTemplate.convertSendAndReceive(
                    CommentEmotionDeleteQueueI.EXCHANGE,
                    CommentEmotionDeleteQueueI.ROUTING_KEY,
                    updateResult
            );

            commentEntity.setTotalEmotion(commentEntity.getTotalEmotion() - 1);
        }

        final CommentEmotionEntityPK addResult = commentEmotionAddRepository.execute(commentEmotionEntity);

        rabbitTemplate.convertSendAndReceive(
                CommentEmotionAddQueueI.EXCHANGE,
                CommentEmotionAddQueueI.ROUTING_KEY,
                commentEmotionEntity
        );

        commentEntity.setTotalEmotion(commentEntity.getTotalEmotion() + 1);
        commentUpdateRepository.execute(commentEntity);

        rabbitTemplate.convertSendAndReceive(
                CommentUpdateQueueI.EXCHANGE,
                CommentUpdateQueueI.ROUTING_KEY,
                commentEntity
        );

        return new SuccessResponse<>(
                new SuccessPojo<>(
                        addResult,
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
