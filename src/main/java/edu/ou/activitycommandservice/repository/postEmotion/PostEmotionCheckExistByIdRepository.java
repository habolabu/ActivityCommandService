package edu.ou.activitycommandservice.repository.postEmotion;

import edu.ou.activitycommandservice.common.constant.CodeStatus;
import edu.ou.activitycommandservice.data.entity.PostEmotionEntityPK;
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
public class PostEmotionCheckExistByIdRepository extends BaseRepository<PostEmotionEntityPK, Boolean> {
    private final ValidValidation validValidation;
    private final EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;

    /**
     * Validate post emotion id
     *
     * @param postEmotionId id of post emotion
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected void preExecute(PostEmotionEntityPK postEmotionId) {
        if (validValidation.isInValid(postEmotionId)) {
            throw new BusinessException(
                    CodeStatus.INVALID_INPUT,
                    Message.Error.INVALID_INPUT,
                    "post emotion identity"
            );
        }
        entityManager = entityManagerFactory.createEntityManager();
    }

    /**
     * Check post emotion exist or not by id
     *
     * @param postEmotionEntityPK id of post emotion
     * @return exist or not
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected Boolean doExecute(PostEmotionEntityPK postEmotionEntityPK) {
        final String hqlQuery = "FROM PostEmotionEntity PE " +
                "WHERE PE.postId = :postId AND PE.userId = :userId";

        try {
            return !entityManager
                    .unwrap(Session.class)
                    .createQuery(hqlQuery)
                    .setParameter(
                            "postId",
                            postEmotionEntityPK.getPostId()
                    )
                    .setParameter(
                            "userId",
                            postEmotionEntityPK.getUserId()
                    )
                    .getResultList()
                    .isEmpty();
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
    protected void postExecute(PostEmotionEntityPK input) {
        if(Objects.nonNull(entityManager)){
            entityManager.close();
        }
    }
}
