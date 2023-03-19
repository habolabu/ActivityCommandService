package edu.ou.activitycommandservice.repository.emotion;


import edu.ou.activitycommandservice.common.constant.CodeStatus;
import edu.ou.activitycommandservice.data.entity.EmotionEntity;
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
public class EmotionAddRepository extends BaseRepository<EmotionEntity, Integer> {
    private final ValidValidation validValidation;
    private final EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;

    /**
     * Validate emotion entity
     *
     * @param emotion emotion entity
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
    }

    /**
     * Add new emotion entity
     *
     * @param emotionEntity input of task
     * @return id of emotion
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected Integer doExecute(EmotionEntity emotionEntity) {
        try {
            return (Integer)
                    entityManager
                            .unwrap(Session.class)
                            .save(emotionEntity);

        } catch (Exception e) {
            throw new BusinessException(
                    CodeStatus.CONFLICT,
                    Message.Error.ADD_FAIL,
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
