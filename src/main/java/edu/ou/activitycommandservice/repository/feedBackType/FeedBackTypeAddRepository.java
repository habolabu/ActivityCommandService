package edu.ou.activitycommandservice.repository.feedBackType;


import edu.ou.activitycommandservice.common.constant.CodeStatus;
import edu.ou.activitycommandservice.data.entity.FeedBackTypeEntity;
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
public class FeedBackTypeAddRepository extends BaseRepository<FeedBackTypeEntity, Integer> {
    private final ValidValidation validValidation;
    private final EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;

    /**
     * Validate feedBackType
     *
     * @param feedBackType feedBackType
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected void preExecute(FeedBackTypeEntity feedBackType) {
        if (validValidation.isInValid(feedBackType, FeedBackTypeEntity.class)) {
            throw new BusinessException(
                    CodeStatus.INVALID_INPUT,
                    Message.Error.INVALID_INPUT,
                    "feedback type"
            );
        }
        entityManager = entityManagerFactory.createEntityManager();
    }

    /**
     * Insert new feedback type
     *
     * @param feedBackType new feedback type
     * @return id of new feedback type
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected Integer doExecute(FeedBackTypeEntity feedBackType) {
        try {
            return (Integer)
                    entityManager
                            .unwrap(Session.class)
                            .save(feedBackType);

        } catch (Exception e) {
            throw new BusinessException(
                    CodeStatus.CONFLICT,
                    Message.Error.ADD_FAIL,
                    "feedback type"
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
    protected void postExecute(FeedBackTypeEntity input) {
        if(Objects.nonNull(entityManager)){
            entityManager.close();
        }
    }
}
