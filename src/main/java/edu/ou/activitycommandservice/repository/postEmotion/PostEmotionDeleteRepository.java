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
import javax.persistence.EntityTransaction;
import java.util.Objects;

@Repository
@RequiredArgsConstructor
public class PostEmotionDeleteRepository extends BaseRepository<PostEmotionEntityPK, PostEmotionEntityPK> {
    private final ValidValidation validValidation;
    private final EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;
    private EntityTransaction entityTransaction;

    /**
     * Validate post emotion identity
     *
     * @param postEmotionEntityPK post emotion identity
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected void preExecute(PostEmotionEntityPK postEmotionEntityPK) {
        if (validValidation.isInValid(postEmotionEntityPK)) {
            throw new BusinessException(
                    CodeStatus.INVALID_INPUT,
                    Message.Error.INVALID_INPUT,
                    "post emotion identity"
            );
        }
        entityManager = entityManagerFactory.createEntityManager();
        entityTransaction = entityManager.getTransaction();
    }

    /**
     * Delete exist post emotion
     *
     * @param postEmotionEntityPK id of exist post emotion
     * @return id of exist post emotion
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected PostEmotionEntityPK doExecute(PostEmotionEntityPK postEmotionEntityPK) {
        final String hqlQuery =
                "DELETE FROM PostEmotionEntity PE " +
                        "WHERE PE.postId = :postId AND PE.userId = :userId";

        try {
            entityTransaction.begin();
            final int rowChangeNumber =
                    entityManager
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
                            .executeUpdate();
            entityTransaction.commit();

            if (rowChangeNumber == 1) {
                return postEmotionEntityPK;
            }
            return null;

        } catch (Exception e) {
            throw new BusinessException(
                    CodeStatus.CONFLICT,
                    Message.Error.DELETE_FAIL,
                    "comment emotion",
                    "comment emotion identity",
                    postEmotionEntityPK
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
