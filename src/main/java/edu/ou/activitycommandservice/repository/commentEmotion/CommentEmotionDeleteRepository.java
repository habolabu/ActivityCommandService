package edu.ou.activitycommandservice.repository.commentEmotion;

import edu.ou.activitycommandservice.common.constant.CodeStatus;
import edu.ou.activitycommandservice.data.entity.CommentEmotionEntityPK;
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
public class CommentEmotionDeleteRepository extends BaseRepository<CommentEmotionEntityPK, CommentEmotionEntityPK> {
    private final ValidValidation validValidation;
    private final EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;
    private EntityTransaction entityTransaction;

    /**
     * Validate comment emotion identity
     *
     * @param commentEmotionEntityPK comment emotion identity
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected void preExecute(CommentEmotionEntityPK commentEmotionEntityPK) {
        if (validValidation.isInValid(commentEmotionEntityPK)) {
            throw new BusinessException(
                    CodeStatus.INVALID_INPUT,
                    Message.Error.INVALID_INPUT,
                    "comment emotion identity"
            );
        }
        entityManager = entityManagerFactory.createEntityManager();
        entityTransaction = entityManager.getTransaction();
    }

    /**
     * Delete exist comment emotion
     *
     * @param commentEmotionEntityPK id of exist comment emotion
     * @return id of exist comment emotion
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected CommentEmotionEntityPK doExecute(CommentEmotionEntityPK commentEmotionEntityPK) {
        final String hqlQuery =
                "DELETE FROM CommentEmotionEntity CME " +
                        "WHERE CME.commentId = :commentId AND CME.userId = :userId";

        try {
            entityTransaction.begin();
            final int rowChangeNumber =
                    entityManager
                            .unwrap(Session.class)
                            .createQuery(hqlQuery)
                            .setParameter(
                                    "commentId",
                                    commentEmotionEntityPK.getCommentId()
                            )
                            .setParameter(
                                    "userId",
                                    commentEmotionEntityPK.getUserId()
                            )
                            .executeUpdate();
            entityTransaction.commit();

            if (rowChangeNumber == 1) {
                return commentEmotionEntityPK;
            }
            return null;

        } catch (Exception e) {
            throw new BusinessException(
                    CodeStatus.CONFLICT,
                    Message.Error.DELETE_FAIL,
                    "comment emotion",
                    "comment emotion identity",
                    commentEmotionEntityPK
            );

        }
    }

    /**
     * Close connection
     *
     * @param commentEmotionEntityPK input of task
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected void postExecute(CommentEmotionEntityPK commentEmotionEntityPK) {
        if(Objects.nonNull(entityManager)){
            entityManager.close();
        }
    }
}
