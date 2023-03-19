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
import javax.persistence.EntityTransaction;
import java.util.Objects;

@Repository
@RequiredArgsConstructor
public class EmotionDeleteRepository extends BaseRepository<Integer, Integer> {
    private final ValidValidation validValidation;
    private final EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;
    private EntityTransaction entityTransaction;

    /**
     * Validate emotion identity
     *
     * @param emotionId emotion identity
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected void preExecute(Integer emotionId) {
        if (validValidation.isInValid(emotionId)) {
            throw new BusinessException(
                    CodeStatus.INVALID_INPUT,
                    Message.Error.INVALID_INPUT,
                    "emotion identity"
            );
        }
        entityManager = entityManagerFactory.createEntityManager();
        entityTransaction = entityManager.getTransaction();
    }

    /**
     * Delete exist emotion
     *
     * @param emotionId id of exist emotion
     * @return id of exist emotion
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected Integer doExecute(Integer emotionId) {
        final String hqlQuery =
                "UPDATE EmotionEntity E " +
                        "SET E.isDeleted = CURRENT_TIMESTAMP " +
                        "WHERE E.id = :emotionId";

        try {
            entityTransaction.begin();
            final int rowChangeNumber =
                    entityManager
                            .unwrap(Session.class)
                            .createQuery(hqlQuery)
                            .setParameter(
                                    "emotionId",
                                    emotionId
                            )
                            .executeUpdate();
            entityTransaction.commit();

            if (rowChangeNumber == 1) {
                return emotionId;
            }
            return null;

        } catch (Exception e) {
            throw new BusinessException(
                    CodeStatus.CONFLICT,
                    Message.Error.DELETE_FAIL,
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
