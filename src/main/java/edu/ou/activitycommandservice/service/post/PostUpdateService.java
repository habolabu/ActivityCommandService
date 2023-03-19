package edu.ou.activitycommandservice.service.post;


import edu.ou.activitycommandservice.common.constant.CodeStatus;
import edu.ou.activitycommandservice.common.mapper.PostMapper;
import edu.ou.activitycommandservice.data.entity.PostEntity;
import edu.ou.activitycommandservice.data.pojo.request.post.PostUpdateRequest;
import edu.ou.coreservice.common.constant.Message;
import edu.ou.coreservice.common.exception.BusinessException;
import edu.ou.coreservice.common.validate.ValidValidation;
import edu.ou.coreservice.data.pojo.request.base.IBaseRequest;
import edu.ou.coreservice.data.pojo.response.base.IBaseResponse;
import edu.ou.coreservice.data.pojo.response.impl.SuccessPojo;
import edu.ou.coreservice.data.pojo.response.impl.SuccessResponse;
import edu.ou.coreservice.queue.activity.internal.post.PostUpdateQueueI;
import edu.ou.coreservice.repository.base.IBaseRepository;
import edu.ou.coreservice.service.base.BaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class PostUpdateService extends BaseService<IBaseRequest, IBaseResponse> {
    private final IBaseRepository<PostEntity, Integer> postUpdateRepository;
    private final IBaseRepository<Integer, Boolean> postCheckExistByIdRepository;
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
        if (validValidation.isInValid(request, PostUpdateRequest.class)) {
            throw new BusinessException(
                    CodeStatus.INVALID_INPUT,
                    Message.Error.INVALID_INPUT,
                    "post"
            );
        }
    }

    /**
     * Update exist post
     *
     * @param request request from client
     * @return response to client
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected IBaseResponse doExecute(IBaseRequest request) {
        final PostEntity postEntity = PostMapper.INSTANCE
                .fromPostUpdateRequest((PostUpdateRequest) request);

        if (!postCheckExistByIdRepository.execute(postEntity.getId())) {
            throw new BusinessException(
                    CodeStatus.NOT_FOUND,
                    Message.Error.NOT_FOUND,
                    "post",
                    "post identity",
                    postEntity.getId()
            );
        }

        postUpdateRepository.execute(postEntity);

        rabbitTemplate.convertSendAndReceive(
                PostUpdateQueueI.EXCHANGE,
                PostUpdateQueueI.ROUTING_KEY,
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
    protected void postExecute(IBaseRequest input) {
        // do nothing
    }
}
