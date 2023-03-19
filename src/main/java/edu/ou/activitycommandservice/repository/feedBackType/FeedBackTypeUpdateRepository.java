package edu.ou.activitycommandservice.repository.feedBackType;

import edu.ou.activitycommandservice.common.constant.CodeStatus;
import edu.ou.activitycommandservice.data.entity.FeedBackTypeEntity;
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
public class FeedBackTypeUpdateRepository extends BaseRepository<FeedBackTypeEntity, Integer> {
    private final ValidValidation validValidation;
    private final EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;
    private EntityTransaction entityTransaction;

    /**
     * validate feedBackType
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
        entityManager = entityManagerFactory.createEntityManager();
        entityTransaction = entityManager.getTransaction();
    }

    /**
     * Update exist feedback type
     *
     * @param feedBackType exist feedback type
     * @return id of exist feedback type
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected Integer doExecute(FeedBackTypeEntity feedBackType) {
        try {
            entityTransaction.begin();
            entityManager
                    .find(
                            FeedBackTypeEntity.class,
                            feedBackType.getId()
                    )
                    .setName(feedBackType.getName())
                    .setSlug(feedBackType.getSlug())
                    .setIsDeleted(feedBackType.getIsDeleted());
            entityTransaction.commit();

            return feedBackType.getId();

        } catch (Exception e) {
            entityTransaction.rollback();

            throw new BusinessException(
                    CodeStatus.CONFLICT,
                    Message.Error.UPDATE_FAIL,
                    "feedback type"
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
    protected void postExecute(FeedBackTypeEntity input) {
        if(Objects.nonNull(entityManager)){
            entityManager.close();
        }
    }
}
