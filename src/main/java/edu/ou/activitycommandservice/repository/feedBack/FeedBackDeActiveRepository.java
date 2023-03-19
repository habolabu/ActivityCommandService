package edu.ou.activitycommandservice.repository.feedBack;

import edu.ou.activitycommandservice.common.constant.CodeStatus;
import edu.ou.activitycommandservice.data.entity.FeedBackEntity;
import edu.ou.coreservice.common.constant.Message;
import edu.ou.coreservice.common.exception.BusinessException;
import edu.ou.coreservice.common.validate.ValidValidation;
import edu.ou.coreservice.repository.base.BaseRepository;
import edu.ou.coreservice.repository.base.IBaseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class FeedBackDeActiveRepository extends BaseRepository<FeedBackEntity, Integer> {
    private final IBaseRepository<FeedBackEntity, Integer> feedBackUpdateRepository;
    private final ValidValidation validValidation;

    /**
     * Validate feedback
     *
     * @param feedback feedback
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected void preExecute(FeedBackEntity feedback) {
        if (validValidation.isInValid(feedback, FeedBackEntity.class)) {
            throw new BusinessException(
                    CodeStatus.INVALID_INPUT,
                    Message.Error.INVALID_INPUT,
                    "feedback"
            );
        }

    }

    /**
     * de-active exist feedback
     *
     * @param feedBack feedback
     * @return id of feedback
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected Integer doExecute(FeedBackEntity feedBack) {
        feedBack.setIsDeleted(null);

        return feedBackUpdateRepository.execute(feedBack);
    }

    @Override
    protected void postExecute(FeedBackEntity input) {
        // do nothing
    }
}
