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
public class PostFindBySlugWithDeletedRepository extends BaseRepository<String, PostEntity> {
    private final ValidValidation validValidation;
    private final EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;

    /**
     * Validate post slug
     *
     * @param postSlug post slug
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected void preExecute(String postSlug) {
        if (validValidation.isInValid(postSlug)) {
            throw new BusinessException(
                    CodeStatus.INVALID_INPUT,
                    Message.Error.INVALID_INPUT,
                    "post slug"
            );
        }
        entityManager = entityManagerFactory.createEntityManager();
    }

    /**
     * Find post by slug with deleted
     *
     * @param postSlug post slug
     * @return post
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected PostEntity doExecute(String postSlug) {
        final String hqlQuery = "FROM PostEntity P WHERE P.slug = :postSlug";

        try {
            return (PostEntity)
                    entityManager
                            .unwrap(Session.class)
                            .createQuery(hqlQuery)
                            .setParameter(
                                    "postSlug",
                                    postSlug
                            )
                            .getSingleResult();

        } catch (Exception e) {
            throw new BusinessException(
                    CodeStatus.NOT_FOUND,
                    Message.Error.NOT_FOUND,
                    "post",
                    "post slug",
                    postSlug
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
    protected void postExecute(String input) {
        if(Objects.nonNull(entityManager)){
            entityManager.close();
        }
    }
}
