package edu.ou.activitycommandservice.repository.feedBackType;

import edu.ou.activitycommandservice.common.constant.CodeStatus;
import edu.ou.activitycommandservice.data.entity.FeedBackTypeEntity;
import edu.ou.coreservice.common.constant.Message;
import edu.ou.coreservice.common.exception.BusinessException;
import edu.ou.coreservice.common.validate.ValidValidation;
import edu.ou.coreservice.repository.base.BaseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class FeedBackTypeDeActiveRepository extends BaseRepository<FeedBackTypeEntity, Integer> {
    private final BaseRepository<FeedBackTypeEntity, Integer> feedBackTypeUpdateRepository;
    private final ValidValidation validValidation;

    /**
     * Validate feedBackType
     *
     * @param feedBackType feedBackType
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected void preExecute(FeedBackTypeEntity feedBackType) {
        if (validValidation.isInValid(feedBackType, FeedBackTypeEntity.class)) {
            throw new BusinessException(
                    CodeStatus.INVALID_INPUT,
                    Message.Error.INVALID_INPUT,
                    "feedback type"
            );
        }
    }

    /**
     * de-active exist feedBackType
     *
     * @param feedBackType feedBackType
     * @return id of feedBackType
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected Integer doExecute(FeedBackTypeEntity feedBackType) {
        feedBackType.setIsDeleted(null);

        return feedBackTypeUpdateRepository.execute(feedBackType);
    }

    @Override
    protected void postExecute(FeedBackTypeEntity input) {
        // do nothing
    }
}
