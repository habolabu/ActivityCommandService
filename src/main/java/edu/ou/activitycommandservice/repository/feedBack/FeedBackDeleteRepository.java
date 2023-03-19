package edu.ou.activitycommandservice.repository.feedBack;

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
public class FeedBackDeleteRepository extends BaseRepository<String, String> {
    private final ValidValidation validValidation;
    private final EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;
    private EntityTransaction entityTransaction;

    /**
     * Validate feedback slug
     *
     * @param feedBackSlug feedback slug
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected void preExecute(String feedBackSlug) {
        if (validValidation.isInValid(feedBackSlug)) {
            throw new BusinessException(
                    CodeStatus.INVALID_INPUT,
                    Message.Error.INVALID_INPUT,
                    "feedback slug"
            );
        }
        entityManager = entityManagerFactory.createEntityManager();
        entityTransaction = entityManager.getTransaction();
    }

    /**
     * Delete exist feedback
     *
     * @param feedBackSlug slug of exist feedback
     * @return slug of exist feedback
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected String doExecute(String feedBackSlug) {
        final String hqlQuery =
                "UPDATE FeedBackEntity FB " +
                        "SET FB.isDeleted = CURRENT_TIMESTAMP " +
                        "WHERE FB.slug = :feedBackSlug";

        try {
            entityTransaction.begin();
            final int rowChangeNumber =
                    entityManager
                            .unwrap(Session.class)
                            .createQuery(hqlQuery)
                            .setParameter(
                                    "feedBackSlug",
                                    feedBackSlug
                            )
                            .executeUpdate();
            entityTransaction.commit();

            if (rowChangeNumber == 1) {
                return feedBackSlug;
            }
            return null;

        } catch (Exception e) {
            throw new BusinessException(
                    CodeStatus.CONFLICT,
                    Message.Error.DELETE_FAIL,
                    "feedback",
                    "feedback slug",
                    feedBackSlug
            );

        }
    }

    /**
     * Close connection
     *
     * @param feedBackSlug input of task
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected void postExecute(String feedBackSlug) {
        if(Objects.nonNull(entityManager)){
            entityManager.close();
        }
    }
}
