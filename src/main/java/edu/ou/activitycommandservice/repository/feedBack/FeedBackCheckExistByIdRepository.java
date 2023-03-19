package edu.ou.activitycommandservice.repository.feedBack;

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
public class FeedBackCheckExistByIdRepository extends BaseRepository<Integer, Boolean> {
    private final ValidValidation validValidation;
    private final EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;

    /**
     * Validate feedback id
     *
     * @param feedBackId id of feedback
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected void preExecute(Integer feedBackId) {
        if (validValidation.isInValid(feedBackId)) {
            throw new BusinessException(
                    CodeStatus.INVALID_INPUT,
                    Message.Error.INVALID_INPUT,
                    "feedback identity"
            );
        }
        entityManager = entityManagerFactory.createEntityManager();
    }

    /**
     * Check feedback exist or not by id
     *
     * @param feedBackId id of feedback
     * @return exist or not
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected Boolean doExecute(Integer feedBackId) {
        final String hqlQuery = "FROM FeedBackEntity FB WHERE FB.id = :feedBackId AND FB.isDeleted IS NULL";

        try {
            entityManager
                    .unwrap(Session.class)
                    .createQuery(hqlQuery)
                    .setParameter(
                            "feedBackId",
                            feedBackId
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
