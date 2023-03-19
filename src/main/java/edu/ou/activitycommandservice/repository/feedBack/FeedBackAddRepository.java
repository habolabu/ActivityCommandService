package edu.ou.activitycommandservice.repository.feedBack;


import edu.ou.activitycommandservice.common.constant.CodeStatus;
import edu.ou.activitycommandservice.data.entity.FeedBackEntity;
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
public class FeedBackAddRepository extends BaseRepository<FeedBackEntity, Integer> {
    private final ValidValidation validValidation;
    private final EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;

    /**
     * Validate feedback entity
     *
     * @param feedBack feedback entity
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected void preExecute(FeedBackEntity feedBack) {
        if (validValidation.isInValid(feedBack, FeedBackEntity.class)) {
            throw new BusinessException(
                    CodeStatus.INVALID_INPUT,
                    Message.Error.INVALID_INPUT,
                    "feedback"
            );
        }
        entityManager = entityManagerFactory.createEntityManager();
    }

    /**
     * Add new feedback entity
     *
     * @param feedBackEntity input of task
     * @return id of feedback
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected Integer doExecute(FeedBackEntity feedBackEntity) {
        try {
            return (Integer)
                    entityManager
                            .unwrap(Session.class)
                            .save(feedBackEntity);

        } catch (Exception e) {
            throw new BusinessException(
                    CodeStatus.CONFLICT,
                    Message.Error.ADD_FAIL,
                    "feedback"
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
    protected void postExecute(FeedBackEntity input) {
        if(Objects.nonNull(entityManager)){
            entityManager.close();
        }
    }
}
