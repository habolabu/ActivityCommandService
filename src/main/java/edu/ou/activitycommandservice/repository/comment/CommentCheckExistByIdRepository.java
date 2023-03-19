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
import javax.persistence.NoResultException;
import java.util.Objects;

@Repository
@RequiredArgsConstructor
public class CommentCheckExistByIdRepository extends BaseRepository<Integer, Boolean> {
    private final ValidValidation validValidation;
    private final EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;

    /**
     * Validate comment id
     *
     * @param commentId id of comment
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
    }

    /**
     * Check comment exist or not by id
     *
     * @param commentId id of comment
     * @return exist or not
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected Boolean doExecute(Integer commentId) {
        final String hqlQuery = "FROM CommentEntity CM WHERE CM.id = :commentId AND CM.isDeleted IS NULL";

        try {
            entityManager
                    .unwrap(Session.class)
                    .createQuery(hqlQuery)
                    .setParameter(
                            "commentId",
                            commentId
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
