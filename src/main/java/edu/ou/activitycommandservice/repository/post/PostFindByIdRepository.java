package edu.ou.activitycommandservice.repository.post;

import edu.ou.activitycommandservice.common.constant.CodeStatus;
import edu.ou.activitycommandservice.data.entity.PostEntity;
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
public class PostFindByIdRepository extends BaseRepository<Integer, PostEntity> {
    private final ValidValidation validValidation;
    private final EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;

    /**
     * Validate input
     *
     * @param postId post identity
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected void preExecute(Integer postId) {
        if (validValidation.isInValid(postId)) {
            throw new BusinessException(
                    CodeStatus.INVALID_INPUT,
                    Message.Error.INVALID_INPUT,
                    "post identity"
            );
        }
        entityManager = entityManagerFactory.createEntityManager();
    }

    /**
     * Find post by id
     *
     * @param postId post identity
     * @return post
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected PostEntity doExecute(Integer postId) {
        final String hqlQuery = "FROM PostEntity P WHERE P.id = :postId AND P.isDeleted IS NULL";

        try {
            return (PostEntity)
                    entityManager
                            .unwrap(Session.class)
                            .createQuery(hqlQuery)
                            .setParameter(
                                    "postId",
                                    postId
                            )
                            .getSingleResult();

        } catch (Exception e) {
            throw new BusinessException(
                    CodeStatus.NOT_FOUND,
                    Message.Error.NOT_FOUND,
                    "post",
                    "post identity",
                    postId
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
