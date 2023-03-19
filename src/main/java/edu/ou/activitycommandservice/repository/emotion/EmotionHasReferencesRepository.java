package edu.ou.activitycommandservice.repository.emotion;

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
public class EmotionHasReferencesRepository extends BaseRepository<Integer, Boolean> {
    private final ValidValidation validValidation;
    private final EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;

    /**
     * Validate emotion id
     *
     * @param emotionId emotion id
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected void preExecute(Integer emotionId) {
        if (validValidation.isInValid(emotionId)) {
            throw new BusinessException(
                    CodeStatus.INVALID_INPUT,
                    Message.Error.INVALID_INPUT,
                    "emotion id"
            );
        }
        entityManager = entityManagerFactory.createEntityManager();
    }


    /**
     * Check references in emotion
     *
     * @param emotionId emotion id
     * @return emotion has references or not
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected Boolean doExecute(Integer emotionId) {
        final String nativeQuery = "SELECT (SELECT COUNT(*) FROM PostEmotion PE WHERE PE.emotionId = E.id) " +
                " + (SELECT COUNT(*) FROM CommentEmotion CE WHERE CE.emotionId = E.id) " +
                "FROM Emotion E " +
                "WHERE E.id = :emotionId AND E.isDeleted IS NULL";

        try {
            return !entityManager
                    .unwrap(Session.class)
                    .createSQLQuery(nativeQuery)
                    .setParameter(
                            "emotionId",
                            emotionId
                    )
                    .getResultList()
                    .isEmpty();

        } catch (Exception e) {
            throw new BusinessException(
                    CodeStatus.NOT_FOUND,
                    Message.Error.NOT_FOUND,
                    "emotion",
                    "emotion identity",
                    emotionId
            );
        }
    }

    /**
     * Close connection
     *
     * @param emotionId input of task
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected void postExecute(Integer emotionId) {
        if(Objects.nonNull(entityManager)){
            entityManager.close();
        }
    }
}
