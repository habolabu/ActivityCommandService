package edu.ou.activitycommandservice.service.post;

import edu.ou.activitycommandservice.common.constant.CodeStatus;
import edu.ou.activitycommandservice.common.mapper.PostMapper;
import edu.ou.activitycommandservice.data.entity.PostEntity;
import edu.ou.activitycommandservice.data.pojo.request.post.PostAddRequest;
import edu.ou.coreservice.common.constant.Message;
import edu.ou.coreservice.common.exception.BusinessException;
import edu.ou.coreservice.common.validate.ValidValidation;
import edu.ou.coreservice.data.pojo.request.base.IBaseRequest;
import edu.ou.coreservice.data.pojo.response.base.IBaseResponse;
import edu.ou.coreservice.data.pojo.response.impl.SuccessPojo;
import edu.ou.coreservice.data.pojo.response.impl.SuccessResponse;
import edu.ou.coreservice.queue.activity.internal.post.PostAddQueueI;
import edu.ou.coreservice.repository.base.IBaseRepository;
import edu.ou.coreservice.service.base.BaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class PostAddService extends BaseService<IBaseRequest, IBaseResponse> {
    private final IBaseRepository<PostEntity, Integer> postAddRepository;
    private final IBaseRepository<String, PostEntity> postFindBySlugWithDeletedRepository;
    private final IBaseRepository<String, Boolean> postCheckDeleteRepository;
    private final IBaseRepository<PostEntity, Integer> postDeActiveRepository;
    private final RabbitTemplate rabbitTemplate;
    private final ValidValidation validValidation;

    /**
     * Validate post add request
     *
     * @param request post add request
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected void preExecute(IBaseRequest request) {
        if (validValidation.isInValid(request, PostAddRequest.class)) {
            throw new BusinessException(
                    CodeStatus.INVALID_INPUT,
                    Message.Error.INVALID_INPUT,
                    "post"
            );
        }
    }

    /**
     * Add new post
     *
     * @param request post add request
     * @return id of new post
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected IBaseResponse doExecute(IBaseRequest request) {
        final PostEntity postEntity = PostMapper.INSTANCE.fromPostAddRequest((PostAddRequest) request);
        int postId;

        if (postCheckDeleteRepository.execute(postEntity.getSlug())) {
            final PostEntity existDeletedPostEntity =
                    postFindBySlugWithDeletedRepository.execute(postEntity.getSlug());
            postEntity.setId(existDeletedPostEntity.getId());
            postId = postDeActiveRepository.execute(postEntity);

        } else {
            postId = postAddRepository.execute(postEntity);
        }

        postEntity.setId(postId);

        rabbitTemplate.convertSendAndReceive(
                PostAddQueueI.EXCHANGE,
                PostAddQueueI.ROUTING_KEY,
                postEntity
        );

        return new SuccessResponse<>(
                new SuccessPojo<>(
                        postEntity.getSlug(),
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
