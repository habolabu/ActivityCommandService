package edu.ou.activitycommandservice.repository.feedBackType;

import edu.ou.activitycommandservice.common.constant.CodeStatus;
import edu.ou.coreservice.common.constant.Message;
import edu.ou.coreservice.common.exception.BusinessException;
import edu.ou.coreservice.common.validate.ValidValidation;
import edu.ou.coreservice.repository.base.BaseRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import java.util.Objects;

@Repository
@RequiredArgsConstructor
public class FeedBackTypeDeleteRepository extends BaseRepository<String, String> {
    private final ValidValidation validValidation;
    private final EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;
    private EntityTransaction entityTransaction;

    /**
     * Validate feedback type slug
     *
     * @param feedBackTypeSlug feedback type slug
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected void preExecute(String feedBackTypeSlug) {
        if (validValidation.isInValid(feedBackTypeSlug)) {
            throw new BusinessException(
                    CodeStatus.INVALID_INPUT,
                    Message.Error.INVALID_INPUT,
                    "feedback type slug"
            );
        }
        entityManager = entityManagerFactory.createEntityManager();
        entityTransaction = entityManager.getTransaction();
    }

    /**
     * Delete exist feedback type
     *
     * @param feedBackTypeSlug slug of feedback type which want to delete
     * @return slug of deleted feedback
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected String doExecute(String feedBackTypeSlug) {
        final String hqlQuery =
                "UPDATE FeedBackTypeEntity FBT " +
                        "SET FBT.isDeleted = CURRENT_TIMESTAMP " +
                        "WHERE FBT.slug = :feedBackTypeSlug";

        try {
            entityTransaction.begin();
            final int rowChangeNumber =
                    entityManager
                            .unwrap(Session.class)
                            .createQuery(hqlQuery)
                            .setParameter(
                                    "feedBackTypeSlug",
                                    feedBackTypeSlug
                            )
                            .executeUpdate();
            entityTransaction.commit();

            if (rowChangeNumber == 1) {
                return feedBackTypeSlug;
            }
            return null;

        } catch (Exception e) {
            throw new BusinessException(
                    CodeStatus.CONFLICT,
                    Message.Error.DELETE_FAIL,
                    "feedback type",
                    "feedback type slug",
                    feedBackTypeSlug
            );

        }
    }

    /**
     * Close connection
     *
     * @param areaSlug input of task
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected void postExecute(String areaSlug) {
        if(Objects.nonNull(entityManager)){
            entityManager.close();
        }
    }
}
