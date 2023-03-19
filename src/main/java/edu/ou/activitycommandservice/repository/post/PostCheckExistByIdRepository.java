package edu.ou.activitycommandservice.repository.post;

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
public class PostCheckExistByIdRepository extends BaseRepository<Integer, Boolean> {
    private final ValidValidation validValidation;
    private final EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;

    /**
     * Validate post id
     *
     * @param postId post id
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
     * Check exist status of post
     *
     * @param postId id of post
     * @return exist status
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected Boolean doExecute(Integer postId) {
        final String hqlQuery = "FROM PostEntity P WHERE P.id = :postId AND P.isDeleted IS NULL";

        try {
            entityManager
                    .unwrap(Session.class)
                    .createQuery(hqlQuery)
                    .setParameter(
                            "postId",
                            postId
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
     * @param areaId input of task
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected void postExecute(Integer areaId) {
        if(Objects.nonNull(entityManager)){
            entityManager.close();
        }
    }
}
