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
import java.util.Objects;

@Repository
@RequiredArgsConstructor
public class CommentEmotionCheckExistByIdRepository extends BaseRepository<CommentEmotionEntityPK, Boolean> {
    private final ValidValidation validValidation;
    private final EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;

    /**
     * Validate comment emotion id
     *
     * @param commentEmotionId id of comment emotion
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected void preExecute(CommentEmotionEntityPK commentEmotionId) {
        if (validValidation.isInValid(commentEmotionId)) {
            throw new BusinessException(
                    CodeStatus.INVALID_INPUT,
                    Message.Error.INVALID_INPUT,
                    "comment emotion identity"
            );
        }
        entityManager = entityManagerFactory.createEntityManager();
    }

    /**
     * Check comment emotion exist or not by id
     *
     * @param commentEmotionEntityPK id of comment emotion
     * @return exist or not
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected Boolean doExecute(CommentEmotionEntityPK commentEmotionEntityPK) {
        final String hqlQuery = "FROM CommentEmotionEntity CME " +
                "WHERE CME.commentId = :commentId AND CME.userId = :userId";

        try {
            return !entityManager
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
     * @param feedBackId input of task
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected void postExecute(CommentEmotionEntityPK feedBackId) {
        if(Objects.nonNull(entityManager)){
            entityManager.close();
        }
    }
}
