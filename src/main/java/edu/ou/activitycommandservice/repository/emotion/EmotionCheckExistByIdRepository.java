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
import javax.persistence.NoResultException;
import java.util.Objects;

@Repository
@RequiredArgsConstructor
public class EmotionCheckExistByIdRepository extends BaseRepository<Integer, Boolean> {
    private final ValidValidation validValidation;
    private final EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;

    /**
     * Validate emotion id
     *
     * @param emotionId id of emotion
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
    }

    /**
     * Check emotion exist or not by id
     *
     * @param emotionId id of emotion
     * @return exist or not
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected Boolean doExecute(Integer emotionId) {
        final String hqlQuery = "FROM EmotionEntity E WHERE E.id = :emotionId AND E.isDeleted IS NULL";

        try {
            entityManager
                    .unwrap(Session.class)
                    .createQuery(hqlQuery)
                    .setParameter(
                            "emotionId",
                            emotionId
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
     * @param feedBackId input of task
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected void postExecute(Integer feedBackId) {
        if(Objects.nonNull(entityManager)){
            entityManager.close();
        }
    }
}
