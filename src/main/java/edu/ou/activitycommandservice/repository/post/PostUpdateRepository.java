package edu.ou.activitycommandservice.repository.post;

import edu.ou.activitycommandservice.common.constant.CodeStatus;
import edu.ou.activitycommandservice.data.entity.PostEntity;
import edu.ou.coreservice.common.constant.Message;
import edu.ou.coreservice.common.exception.BusinessException;
import edu.ou.coreservice.common.validate.ValidValidation;
import edu.ou.coreservice.repository.base.BaseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import java.util.Objects;

@Repository
@RequiredArgsConstructor
public class PostUpdateRepository extends BaseRepository<PostEntity, Integer> {
    private final ValidValidation validValidation;
    private final EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;
    private EntityTransaction entityTransaction;

    /**
     * validate post
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
        entityTransaction = entityManager.getTransaction();
    }

    /**
     * Update exist post
     *
     * @param postEntity exist post
     * @return id of exist post
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected Integer doExecute(PostEntity postEntity) {
        try {
            entityTransaction.begin();
            entityManager
                    .find(
                            PostEntity.class,
                            postEntity.getId()
                    )
                    .setTitle(postEntity.getTitle())
                    .setSlug(postEntity.getSlug())
                    .setTotalComment(postEntity.getTotalComment())
                    .setTotalEmotion(postEntity.getTotalEmotion())
                    .setEdited(postEntity.isEdited())
                    .setContent(postEntity.getContent())
                    .setIsDeleted(postEntity.getIsDeleted());
            entityTransaction.commit();

            return postEntity.getId();

        } catch (Exception e) {
            entityTransaction.rollback();

            throw new BusinessException(
                    CodeStatus.CONFLICT,
                    Message.Error.UPDATE_FAIL,
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
