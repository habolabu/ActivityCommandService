package edu.ou.activitycommandservice.service.postEmotion;

import edu.ou.activitycommandservice.common.constant.CodeStatus;
import edu.ou.activitycommandservice.common.mapper.PostEmotionMapper;
import edu.ou.activitycommandservice.data.entity.PostEmotionEntityPK;
import edu.ou.activitycommandservice.data.entity.PostEntity;
import edu.ou.activitycommandservice.data.pojo.request.postEmotion.PostEmotionDeleteRequest;
import edu.ou.coreservice.common.constant.Message;
import edu.ou.coreservice.common.exception.BusinessException;
import edu.ou.coreservice.common.validate.ValidValidation;
import edu.ou.coreservice.data.pojo.request.base.IBaseRequest;
import edu.ou.coreservice.data.pojo.response.base.IBaseResponse;
import edu.ou.coreservice.data.pojo.response.impl.SuccessPojo;
import edu.ou.coreservice.data.pojo.response.impl.SuccessResponse;
import edu.ou.coreservice.queue.activity.internal.post.PostUpdateQueueI;
import edu.ou.coreservice.queue.activity.internal.postEmotion.PostEmotionDeleteQueueI;
import edu.ou.coreservice.repository.base.IBaseRepository;
import edu.ou.coreservice.service.base.BaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class PostEmotionDeleteService extends BaseService<IBaseRequest, IBaseResponse> {
    private final IBaseRepository<PostEmotionEntityPK, PostEmotionEntityPK> postEmotionDeleteRepository;
    private final IBaseRepository<PostEmotionEntityPK, Boolean> postEmotionCheckExistByIdRepository;
    private final IBaseRepository<PostEntity, Integer> postUpdateRepository;
    private final IBaseRepository<Integer, PostEntity> postFindByIdRepository;
    private final PostEmotionMapper postEmotionMapper;
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
        if (validValidation.isInValid(request, PostEmotionDeleteRequest.class)) {
            throw new BusinessException(
                    CodeStatus.INVALID_INPUT,
                    Message.Error.INVALID_INPUT,
                    "post emotion identity"

            );
        }
    }

    /**
     * Delete exist post emotion
     *
     * @param request request from client
     * @return response to client
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected IBaseResponse doExecute(IBaseRequest request) {
        final PostEmotionDeleteRequest postEmotionDeleteRequest = (PostEmotionDeleteRequest) request;
        final PostEmotionEntityPK postEmotionEntityPK = postEmotionMapper
                .fromPostEmotionDeleteRequest(postEmotionDeleteRequest);

        if (!postEmotionCheckExistByIdRepository.execute(postEmotionEntityPK)) {
            throw new BusinessException(
                    CodeStatus.NOT_FOUND,
                    Message.Error.NOT_FOUND,
                    "post emotion",
                    "post emotion identity",
                    postEmotionEntityPK.getPostId()
            );
        }

        final PostEmotionEntityPK deleteResult = postEmotionDeleteRepository.execute(postEmotionEntityPK);

        rabbitTemplate.convertSendAndReceive(
                PostEmotionDeleteQueueI.EXCHANGE,
                PostEmotionDeleteQueueI.ROUTING_KEY,
                deleteResult
        );

        final PostEntity postEntity = postFindByIdRepository.execute(postEmotionEntityPK.getPostId());
        postEntity.setTotalEmotion(postEntity.getTotalEmotion() - 1);
        postUpdateRepository.execute(postEntity);

        rabbitTemplate.convertSendAndReceive(
                PostUpdateQueueI.EXCHANGE,
                PostUpdateQueueI.ROUTING_KEY,
                postEntity
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
