package edu.ou.activitycommandservice.repository.feedBackType;

import edu.ou.activitycommandservice.common.constant.CodeStatus;
import edu.ou.activitycommandservice.data.entity.FeedBackTypeEntity;
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
public class FeedBackTypeFindByFeedBackSlugRepository extends BaseRepository<String, FeedBackTypeEntity> {
    private final ValidValidation validValidation;
    private final EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;

    /**
     * Validate feedback type slug
     *
     * @param feedBackType feedback type slug
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected void preExecute(String feedBackType) {
        if (validValidation.isInValid(feedBackType)) {
            throw new BusinessException(
                    CodeStatus.INVALID_INPUT,
                    Message.Error.INVALID_INPUT,
                    "feedback type slug"
            );
        }
        entityManager = entityManagerFactory.createEntityManager();
    }

    /**
     * Find feedback type by feedback slug
     *
     * @param feedBackSlug feedback slug
     * @return feedback type
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected FeedBackTypeEntity doExecute(String feedBackSlug) {
        final String hqlQuery = "FROM FeedBackEntity FBT " +
                "WHERE FBT.isDeleted IS NULL AND FBT.id = " +
                "( " +
                "SELECT FBT.feedBackTypeId " +
                "FROM FeedBackEntity FB " +
                "WHERE FB.isDeleted IS NULL AND FB.slug = :feedBackSlug" +
                " )";

        try {
            return (FeedBackTypeEntity)
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
                    "feedback type",
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
