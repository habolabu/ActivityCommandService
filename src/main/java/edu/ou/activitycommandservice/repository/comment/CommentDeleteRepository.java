package edu.ou.activitycommandservice.repository.comment;

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
public class CommentDeleteRepository extends BaseRepository<Integer, Integer> {
    private final ValidValidation validValidation;
    private final EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;
    private EntityTransaction entityTransaction;

    /**
     * Validate comment identity
     *
     * @param commentId comment identity
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected void preExecute(Integer commentId) {
        if (validValidation.isInValid(commentId)) {
            throw new BusinessException(
                    CodeStatus.INVALID_INPUT,
                    Message.Error.INVALID_INPUT,
                    "comment identity"
            );
        }
        entityManager = entityManagerFactory.createEntityManager();
        entityTransaction = entityManager.getTransaction();
    }

    /**
     * Delete exist comment
     *
     * @param commentId id of exist comment
     * @return id of exist comment
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected Integer doExecute(Integer commentId) {
        final String hqlQuery =
                "UPDATE CommentEntity CM " +
                        "SET CM.isDeleted = CURRENT_TIMESTAMP " +
                        "WHERE CM.id = :commentId";

        try {
            entityTransaction.begin();
            final int rowChangeNumber =
                    entityManager
                            .unwrap(Session.class)
                            .createQuery(hqlQuery)
                            .setParameter(
                                    "commentId",
                                    commentId
                            )
                            .executeUpdate();
            entityTransaction.commit();

            if (rowChangeNumber == 1) {
                return commentId;
            }
            return null;

        } catch (Exception e) {
            throw new BusinessException(
                    CodeStatus.CONFLICT,
                    Message.Error.DELETE_FAIL,
                    "comment",
                    "comment identity",
                    commentId
            );

        }
    }

    /**
     * Close connection
     *
     * @param commentId input of task
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected void postExecute(Integer commentId) {
        if(Objects.nonNull(entityManager)){
            entityManager.close();
        }
    }
}
