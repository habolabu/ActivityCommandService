package edu.ou.activitycommandservice.repository.commentEmotion;


import edu.ou.activitycommandservice.common.constant.CodeStatus;
import edu.ou.activitycommandservice.data.entity.CommentEmotionEntity;
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
public class CommentEmotionAddRepository extends BaseRepository<CommentEmotionEntity, CommentEmotionEntityPK> {
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
    protected void preExecute(CommentEmotionEntity emotion) {
        if (validValidation.isInValid(emotion, CommentEmotionEntity.class)) {
            throw new BusinessException(
                    CodeStatus.INVALID_INPUT,
                    Message.Error.INVALID_INPUT,
                    "comment emotion"
            );
        }
        entityManager = entityManagerFactory.createEntityManager();
    }

    /**
     * Add new comment emotion entity
     *
     * @param commentEmotionEntity input of task
     * @return id of emotion
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected CommentEmotionEntityPK doExecute(CommentEmotionEntity commentEmotionEntity) {
        try {
            return (CommentEmotionEntityPK)
                    entityManager
                            .unwrap(Session.class)
                            .save(commentEmotionEntity);

        } catch (Exception e) {
            throw new BusinessException(
                    CodeStatus.CONFLICT,
                    Message.Error.ADD_FAIL,
                    "comment emotion"
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
    protected void postExecute(CommentEmotionEntity input) {
        if(Objects.nonNull(entityManager)){
            entityManager.close();
        }
    }
}
