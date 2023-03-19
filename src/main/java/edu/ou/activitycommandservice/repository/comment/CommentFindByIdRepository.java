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
public class CommentFindByIdRepository extends BaseRepository<Integer, CommentEntity> {
    private final ValidValidation validValidation;
    private final EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;

    /**
     * Validate input
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
    }

    /**
     * Find comment by id
     *
     * @param commentId comment identity
     * @return comment
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected CommentEntity doExecute(Integer commentId) {
        final String hqlQuery = "FROM CommentEntity CM WHERE CM.id = :commentId AND CM.isDeleted IS NULL";

        try {
            return (CommentEntity)
                    entityManager
                            .unwrap(Session.class)
                            .createQuery(hqlQuery)
                            .setParameter(
                                    "commentId",
                                    commentId
                            )
                            .getSingleResult();

        } catch (Exception e) {
            throw new BusinessException(
                    CodeStatus.NOT_FOUND,
                    Message.Error.NOT_FOUND,
                    "comment",
                    "comment identity",
                    commentId
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
    protected void postExecute(Integer input) {
        if(Objects.nonNull(entityManager)){
            entityManager.close();
        }
    }
}
