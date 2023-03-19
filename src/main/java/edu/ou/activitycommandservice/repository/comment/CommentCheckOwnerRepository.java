package edu.ou.activitycommandservice.repository.comment;

import edu.ou.activitycommandservice.common.constant.CodeStatus;
import edu.ou.activitycommandservice.data.pojo.request.comment.CommentOwnerCheckRequest;
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
public class CommentCheckOwnerRepository extends BaseRepository<CommentOwnerCheckRequest, Boolean> {
    private final ValidValidation validValidation;
    private final EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;

    /**
     * Validate ownerCheckRequest
     *
     * @param ownerCheckRequest ownerCheckRequest
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected void preExecute(CommentOwnerCheckRequest ownerCheckRequest) {
        if (validValidation.isInValid(ownerCheckRequest)) {
            throw new BusinessException(
                    CodeStatus.INVALID_INPUT,
                    Message.Error.INVALID_INPUT,
                    "comment owner identity"
            );
        }
        entityManager = entityManagerFactory.createEntityManager();
    }

    /**
     * Check user is owner of comment or not
     *
     * @param ownerCheckRequest comment owner id
     * @return check result
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected Boolean doExecute(CommentOwnerCheckRequest ownerCheckRequest) {
        final String hqlQuery = "FROM CommentEntity CM " +
                "WHERE CM.id = :commentId AND CM.userId = :userId AND CM.isDeleted IS NULL";

        try {
            entityManager
                    .unwrap(Session.class)
                    .createQuery(hqlQuery)
                    .setParameter(
                            "commentId",
                            ownerCheckRequest.getCommentId()
                    )
                    .setParameter(
                            "userId",
                            ownerCheckRequest.getUserId()
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
    protected void postExecute(CommentOwnerCheckRequest feedBackId) {
        if(Objects.nonNull(entityManager)){
            entityManager.close();
        }
    }
}
