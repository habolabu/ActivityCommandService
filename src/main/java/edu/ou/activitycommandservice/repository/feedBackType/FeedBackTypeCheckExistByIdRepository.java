package edu.ou.activitycommandservice.repository.feedBackType;

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
public class FeedBackTypeCheckExistByIdRepository extends BaseRepository<Integer, Boolean> {
    private final ValidValidation validValidation;
    private final EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;

    /**
     * Validate feedback type id
     *
     * @param feedBackTypeId feedback type id
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected void preExecute(Integer feedBackTypeId) {
        if (validValidation.isInValid(feedBackTypeId)) {
            throw new BusinessException(
                    CodeStatus.INVALID_INPUT,
                    Message.Error.INVALID_INPUT,
                    "feedback type identity"
            );
        }
        entityManager = entityManagerFactory.createEntityManager();
    }

    /**
     * Check exist status of feedback type
     *
     * @param feedBackTypeId id of feedback type
     * @return exist status
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected Boolean doExecute(Integer feedBackTypeId) {
        final String hqlQuery = "FROM FeedBackEntity FBT WHERE FBT.id = :feedBackTypeId AND FBT.isDeleted IS NULL";

        try {
            entityManager
                    .unwrap(Session.class)
                    .createQuery(hqlQuery)
                    .setParameter(
                            "feedBackTypeId",
                            feedBackTypeId
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
