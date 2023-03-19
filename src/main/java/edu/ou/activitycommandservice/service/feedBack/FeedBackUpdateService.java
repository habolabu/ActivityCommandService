package edu.ou.activitycommandservice.service.feedBack;

import edu.ou.activitycommandservice.common.constant.CodeStatus;
import edu.ou.activitycommandservice.common.mapper.FeedBackMapper;
import edu.ou.activitycommandservice.data.entity.FeedBackEntity;
import edu.ou.activitycommandservice.data.pojo.request.feedBack.FeedBackUpdateRequest;
import edu.ou.coreservice.common.constant.Message;
import edu.ou.coreservice.common.exception.BusinessException;
import edu.ou.coreservice.common.validate.ValidValidation;
import edu.ou.coreservice.data.pojo.request.base.IBaseRequest;
import edu.ou.coreservice.data.pojo.response.base.IBaseResponse;
import edu.ou.coreservice.data.pojo.response.impl.SuccessPojo;
import edu.ou.coreservice.data.pojo.response.impl.SuccessResponse;
import edu.ou.coreservice.queue.activity.internal.feedBack.FeedBackUpdateQueueI;
import edu.ou.coreservice.queue.building.internal.apartment.ApartmentUpdateQueueI;
import edu.ou.coreservice.queue.human.external.user.UserCheckExistQueueE;
import edu.ou.coreservice.repository.base.IBaseRepository;
import edu.ou.coreservice.service.base.BaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@Transactional
@RequiredArgsConstructor
public class FeedBackUpdateService extends BaseService<IBaseRequest, IBaseResponse> {
    private final IBaseRepository<FeedBackEntity, Integer> feedBackUpdateRepository;
    private final IBaseRepository<Integer, Boolean> feedBackCheckExistByIdRepository;
    private final IBaseRepository<Integer, Boolean> feedBackTypeCheckExistByIdRepository;
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
        if (validValidation.isInValid(request, FeedBackUpdateRequest.class)) {
            throw new BusinessException(
                    CodeStatus.INVALID_INPUT,
                    Message.Error.INVALID_INPUT,
                    "feedback"
            );
        }
    }

    /**
     * Update exist feedback
     *
     * @param request request from client
     * @return response to client
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected IBaseResponse doExecute(IBaseRequest request) {
        final FeedBackEntity feedBackEntity = FeedBackMapper.INSTANCE
                .fromFeedBackUpdateRequest((FeedBackUpdateRequest) request);

        if (!feedBackTypeCheckExistByIdRepository.execute(feedBackEntity.getFeedBackTypeId())) {
            throw new BusinessException(
                    CodeStatus.NOT_FOUND,
                    Message.Error.NOT_FOUND,
                    "feedback type",
                    "feedback type identity",
                    feedBackEntity.getFeedBackTypeId()
            );
        }

        final Boolean userExist = (Boolean) rabbitTemplate.convertSendAndReceive(
                UserCheckExistQueueE.EXCHANGE,
                UserCheckExistQueueE.ROUTING_KEY,
                feedBackEntity.getUserId()
        );
        if (Objects.isNull(userExist)) {
            throw new BusinessException(
                    CodeStatus.NOT_FOUND,
                    Message.Error.NOT_FOUND,
                    "user",
                    "user identity",
                    feedBackEntity.getUserId()
            );
        }

        if (!feedBackCheckExistByIdRepository.execute(feedBackEntity.getId())) {
            throw new BusinessException(
                    CodeStatus.NOT_FOUND,
                    Message.Error.NOT_FOUND,
                    "feedback",
                    "feedback identity",
                    feedBackEntity.getId()
            );
        }


        final int feedBackId = feedBackUpdateRepository.execute(feedBackEntity);

        rabbitTemplate.convertSendAndReceive(
                FeedBackUpdateQueueI.EXCHANGE,
                FeedBackUpdateQueueI.ROUTING_KEY,
                feedBackEntity
        );

        return new SuccessResponse<>(
                new SuccessPojo<>(
                        feedBackId,
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
