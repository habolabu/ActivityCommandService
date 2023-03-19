package edu.ou.activitycommandservice.repository.comment;

import edu.ou.activitycommandservice.common.constant.CodeStatus;
import edu.ou.activitycommandservice.data.entity.CommentEntity;
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
public class CommentUpdateRepository extends BaseRepository<CommentEntity, Integer> {
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
    protected void preExecute(CommentEntity emotion) {
        if (validValidation.isInValid(emotion, CommentEntity.class)) {
            throw new BusinessException(
                    CodeStatus.INVALID_INPUT,
                    Message.Error.INVALID_INPUT,
                    "comment"
            );
        }
        entityManager = entityManagerFactory.createEntityManager();
        entityTransaction = entityManager.getTransaction();
    }

    /**
     * Update exist comment
     *
     * @param comment comment information
     * @return id of updated comment
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected Integer doExecute(CommentEntity comment) {
        try {
            entityTransaction.begin();
            entityManager
                    .find(
                            CommentEntity.class,
                            comment.getId()
                    )
                    .setContent(comment.getContent());
            entityTransaction.commit();

            return comment.getId();

        } catch (Exception e) {
            entityTransaction.rollback();

            throw new BusinessException(
                    CodeStatus.CONFLICT,
                    Message.Error.UPDATE_FAIL,
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
