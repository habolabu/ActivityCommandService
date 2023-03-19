package edu.ou.activitycommandservice.repository.postEmotion;


import edu.ou.activitycommandservice.common.constant.CodeStatus;
import edu.ou.activitycommandservice.data.entity.PostEmotionEntity;
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
public class PostEmotionAddRepository extends BaseRepository<PostEmotionEntity, PostEmotionEntityPK> {
    private final ValidValidation validValidation;
    private final EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;

    /**
     * Validate postEmotionEntity entity
     *
     * @param postEmotionEntity postEmotionEntity entity
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected void preExecute(PostEmotionEntity postEmotionEntity) {
        if (validValidation.isInValid(postEmotionEntity, PostEmotionEntity.class)) {
            throw new BusinessException(
                    CodeStatus.INVALID_INPUT,
                    Message.Error.INVALID_INPUT,
                    "post emotion"
            );
        }
        entityManager = entityManagerFactory.createEntityManager();
    }

    /**
     * Add new post emotion entity
     *
     * @param postEmotionEntity input of task
     * @return id of post emotion
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected PostEmotionEntityPK doExecute(PostEmotionEntity postEmotionEntity) {
        try {
            return (PostEmotionEntityPK)
                    entityManager
                            .unwrap(Session.class)
                            .save(postEmotionEntity);

        } catch (Exception e) {
            throw new BusinessException(
                    CodeStatus.CONFLICT,
                    Message.Error.ADD_FAIL,
                    "post emotion"
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
    protected void postExecute(PostEmotionEntity input) {
        if(Objects.nonNull(entityManager)){
            entityManager.close();
        }
    }
}
