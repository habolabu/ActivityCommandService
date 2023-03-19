package edu.ou.activitycommandservice.repository.emotion;

import edu.ou.activitycommandservice.common.constant.CodeStatus;
import edu.ou.activitycommandservice.data.entity.EmotionEntity;
import edu.ou.coreservice.common.constant.Message;
import edu.ou.coreservice.common.exception.BusinessException;
import edu.ou.coreservice.common.validate.ValidValidation;
import edu.ou.coreservice.repository.base.BaseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import java.util.Objects;

@Repository
@RequiredArgsConstructor
public class EmotionUpdateRepository extends BaseRepository<EmotionEntity, Integer> {
    private final ValidValidation validValidation;
    private final EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;
    private EntityTransaction entityTransaction;

    /**
     * Validate emotion
     *
     * @param emotion emotion
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected void preExecute(EmotionEntity emotion) {
        if (validValidation.isInValid(emotion, EmotionEntity.class)) {
            throw new BusinessException(
                    CodeStatus.INVALID_INPUT,
                    Message.Error.INVALID_INPUT,
                    "emotion"
            );
        }
        entityManager = entityManagerFactory.createEntityManager();
        entityTransaction = entityManager.getTransaction();
    }

    /**
     * Update exist emotion
     *
     * @param emotion emotion information
     * @return id of updated emotion
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected Integer doExecute(EmotionEntity emotion) {
        try {
            entityTransaction.begin();
            entityManager
                    .find(
                            EmotionEntity.class,
                            emotion.getId()
                    )
                    .setIcon(emotion.getIcon())
                    .setName(emotion.getName());
            entityTransaction.commit();

            return emotion.getId();

        } catch (Exception e) {
            entityTransaction.rollback();

            throw new BusinessException(
                    CodeStatus.CONFLICT,
                    Message.Error.UPDATE_FAIL,
                    "emotion"
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
    protected void postExecute(EmotionEntity input) {
        if(Objects.nonNull(entityManager)){
            entityManager.close();
        }
    }
}
