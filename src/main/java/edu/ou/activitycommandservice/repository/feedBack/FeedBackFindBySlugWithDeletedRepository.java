package edu.ou.activitycommandservice.repository.feedBack;

import edu.ou.activitycommandservice.common.constant.CodeStatus;
import edu.ou.activitycommandservice.data.entity.FeedBackEntity;
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
public class FeedBackFindBySlugWithDeletedRepository extends BaseRepository<String, FeedBackEntity> {
    private final ValidValidation validValidation;
    private final EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;

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
    }

    /**
     * Find feedback entity by slug (contains deleted feedback)
     *
     * @param feedBackSlug slug of feedback
     * @return feedback
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected FeedBackEntity doExecute(String feedBackSlug) {
        final String hqlQuery = "FROM FeedBackEntity FB WHERE FB.slug = :feedBackSlug";

        try {
            return (FeedBackEntity)
                    entityManager
                            .unwrap(Session.class)
                            .createQuery(hqlQuery)
                            .setParameter(
                                    "feedBackSlug",
                                    feedBackSlug
                            )
                            .getSingleResult();

        } catch (Exception e) {
            throw new BusinessException(
                    CodeStatus.NOT_FOUND,
                    Message.Error.NOT_FOUND,
                    "feedback",
                    "feedback slug",
                    feedBackSlug
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
