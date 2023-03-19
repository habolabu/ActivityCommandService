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
import javax.persistence.NoResultException;
import java.util.Objects;

@Repository
@RequiredArgsConstructor
public class FeedBackCheckDeleteRepository extends BaseRepository<String, Boolean> {
    private final ValidValidation validValidation;
    private final EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;

    /**
     * Validate feedback slug
     *
     * @param feedBackSlug slug of feedback
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
    }

    /**
     * Check deleted status of feedback
     *
     * @param feedBackSlug slug of feedback
     * @return deleted status
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected Boolean doExecute(String feedBackSlug) {
        final String hqlQuery = "FROM FeedBackEntity FB WHERE FB.slug = :feedBackSlug AND FB.isDeleted IS NOT NULL";

        try {
            entityManager
                    .unwrap(Session.class)
                    .createQuery(hqlQuery)
                    .setParameter(
                            "feedBackSlug",
                            feedBackSlug
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
