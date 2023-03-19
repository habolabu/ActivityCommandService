package edu.ou.activitycommandservice.repository.feedBack;

import edu.ou.activitycommandservice.common.constant.CodeStatus;
import edu.ou.activitycommandservice.data.entity.FeedBackEntity;
import edu.ou.coreservice.common.constant.Message;
import edu.ou.coreservice.common.exception.BusinessException;
import edu.ou.coreservice.common.validate.ValidValidation;
import edu.ou.coreservice.repository.base.BaseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import java.util.Objects;

@Repository
@RequiredArgsConstructor
public class FeedBackUpdateRepository extends BaseRepository<FeedBackEntity, Integer> {
    private final ValidValidation validValidation;
    private final EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;
    private EntityTransaction entityTransaction;

    /**
     * Validate feedback
     *
     * @param feedBack feedback
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected void preExecute(FeedBackEntity feedBack) {
        if (validValidation.isInValid(feedBack, FeedBackEntity.class)) {
            throw new BusinessException(
                    CodeStatus.INVALID_INPUT,
                    Message.Error.INVALID_INPUT,
                    "feedback"
            );
        }
        entityManager = entityManagerFactory.createEntityManager();
        entityTransaction = entityManager.getTransaction();
    }

    /**
     * Update exist feedback
     *
     * @param feedBack feedback information
     * @return id of updated feedback
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected Integer doExecute(FeedBackEntity feedBack) {
        try {
            entityTransaction.begin();
            entityManager
                    .find(
                            FeedBackEntity.class,
                            feedBack.getId()
                    )
                    .setTitle(feedBack.getTitle())
                    .setSlug(feedBack.getSlug())
                    .setContent(feedBack.getContent())
                    .setIsDeleted(feedBack.getIsDeleted())
                    .setUserId(feedBack.getUserId())
                    .setFeedBackTypeId(feedBack.getFeedBackTypeId());
            entityTransaction.commit();

            return feedBack.getId();

        } catch (Exception e) {
            entityTransaction.rollback();

            throw new BusinessException(
                    CodeStatus.CONFLICT,
                    Message.Error.UPDATE_FAIL,
                    "feedback"
            );

        }
    }

    /**
     * Close connection
     *
     * @param input input of task
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected void postExecute(FeedBackEntity input) {
        if(Objects.nonNull(entityManager)){
            entityManager.close();
        }
    }
}
