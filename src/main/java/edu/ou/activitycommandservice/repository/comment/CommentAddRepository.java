package edu.ou.activitycommandservice.repository.comment;


import edu.ou.activitycommandservice.common.constant.CodeStatus;
import edu.ou.activitycommandservice.data.entity.CommentEntity;
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
public class CommentAddRepository extends BaseRepository<CommentEntity, Integer> {
    private final ValidValidation validValidation;
    private final EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;

    /**
     * Validate comment entity
     *
     * @param comment comment entity
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected void preExecute(CommentEntity comment) {
        if (validValidation.isInValid(comment, CommentEntity.class)) {
            throw new BusinessException(
                    CodeStatus.INVALID_INPUT,
                    Message.Error.INVALID_INPUT,
                    "comment"
            );
        }
        entityManager = entityManagerFactory.createEntityManager();
    }

    /**
     * Add new comment entity
     *
     * @param commentEntity input of task
     * @return id of comment
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected Integer doExecute(CommentEntity commentEntity) {
        try {
            return (Integer)
                    entityManager
                            .unwrap(Session.class)
                            .save(commentEntity);

        } catch (Exception e) {
            throw new BusinessException(
                    CodeStatus.CONFLICT,
                    Message.Error.ADD_FAIL,
                    "comment"
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
    protected void postExecute(CommentEntity input) {
        if(Objects.nonNull(entityManager)){
            entityManager.close();
        }
    }
}
