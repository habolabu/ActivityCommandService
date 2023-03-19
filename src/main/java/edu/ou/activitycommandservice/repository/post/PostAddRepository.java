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
public class PostAddRepository extends BaseRepository<PostEntity, Integer> {
    private final ValidValidation validValidation;
    private final EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;

    /**
     * Validate post
     *
     * @param postEntity post
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected void preExecute(PostEntity postEntity) {
        if (validValidation.isInValid(postEntity, PostEntity.class)) {
            throw new BusinessException(
                    CodeStatus.INVALID_INPUT,
                    Message.Error.INVALID_INPUT,
                    "post"
            );
        }
        entityManager = entityManagerFactory.createEntityManager();
    }

    /**
     * Insert new post
     *
     * @param postEntity new post
     * @return id of new post
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected Integer doExecute(PostEntity postEntity) {
        try {
            return (Integer)
                    entityManager
                            .unwrap(Session.class)
                            .save(postEntity);

        } catch (Exception e) {
            throw new BusinessException(
                    CodeStatus.CONFLICT,
                    Message.Error.ADD_FAIL,
                    "post"
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
    protected void postExecute(PostEntity input) {
        if(Objects.nonNull(entityManager)){
            entityManager.close();
        }
    }
}
