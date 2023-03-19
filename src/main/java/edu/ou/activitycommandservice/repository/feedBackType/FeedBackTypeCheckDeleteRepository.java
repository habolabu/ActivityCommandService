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
import javax.persistence.NoResultException;
import java.util.Objects;

@Repository
@RequiredArgsConstructor
public class FeedBackTypeCheckDeleteRepository extends BaseRepository<String, Boolean> {
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
     * Check deleted status of feedback type
     *
     * @param feedBackTypeSlug slug of feedback type
     * @return deleted status
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected Boolean doExecute(String feedBackTypeSlug) {
        final String hqlQuery = "FROM FeedBackEntity FBT " +
                "WHERE FBT.slug = :feedBackTypeSlug AND FBT.isDeleted IS NOT NULL";

        try {
            entityManager
                    .unwrap(Session.class)
                    .createQuery(hqlQuery)
                    .setParameter(
                            "feedBackTypeSlug",
                            feedBackTypeSlug
                    )
                    .getSingleResult();
            return true;

        } catch (NoResultException e) {
            return false;

        } catch (Exception e) {
            throw new BusinessException(
                    CodeStatus.SERVER_ERROR,
                    Message.Error.UN_KNOWN
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
    protected void postExecute(String input) {
        if(Objects.nonNull(entityManager)){
            entityManager.close();
        }
    }
}
