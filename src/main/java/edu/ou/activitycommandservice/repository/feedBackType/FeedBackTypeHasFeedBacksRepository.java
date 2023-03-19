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
import java.util.Objects;


@Repository
@RequiredArgsConstructor
public class FeedBackTypeHasFeedBacksRepository extends BaseRepository<String, Boolean> {
    private final ValidValidation validValidation;
    private final EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;

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
    }

    /**
     * Check feedbacks in feedback type
     *
     * @param feedBackTypeSlug feedback type slug
     * @return feedback type has feedbacks or not
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected Boolean doExecute(String feedBackTypeSlug) {
        final String hqlQuery = "SELECT FBT.id " +
                "FROM FeedBackTypeEntity FBT " +
                "JOIN FeedBackEntity FB ON FBT.id = FB.feedBackTypeId " +
                "WHERE FBT.slug = :feedBackTypeSlug AND FBT.isDeleted IS NULL AND FB.isDeleted IS NULL";

        try {
            return !entityManager
                    .unwrap(Session.class)
                    .createQuery(hqlQuery)
                    .setParameter(
                            "feedBackTypeSlug",
                            feedBackTypeSlug
                    )
                    .getResultList()
                    .isEmpty();

        } catch (Exception e) {
            throw new BusinessException(
                    CodeStatus.NOT_FOUND,
                    Message.Error.NOT_FOUND,
                    "feedback type",
                    "feedback type slug",
                    feedBackTypeSlug
            );
        }
    }

    /**
     * Close connection
     *
     * @param feedBackTypeSlug input of task
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected void postExecute(String feedBackTypeSlug) {
        if(Objects.nonNull(entityManager)){
            entityManager.close();
        }
    }
}
