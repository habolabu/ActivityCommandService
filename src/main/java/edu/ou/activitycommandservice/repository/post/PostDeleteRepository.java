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
import javax.persistence.EntityTransaction;
import java.util.Objects;

@Repository
@RequiredArgsConstructor
public class PostDeleteRepository extends BaseRepository<String, String> {
    private final ValidValidation validValidation;
    private final EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;
    private EntityTransaction entityTransaction;

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
        entityTransaction = entityManager.getTransaction();
    }

    /**
     * Delete exist post
     *
     * @param postSlug slug of post which want to delete
     * @return slug of deleted post
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected String doExecute(String postSlug) {
        final String hqlQuery =
                "UPDATE PostEntity P " +
                        "SET P.isDeleted = CURRENT_TIMESTAMP " +
                        "WHERE P.slug = :postSlug";

        try {
            entityTransaction.begin();
            final int rowChangeNumber =
                    entityManager
                            .unwrap(Session.class)
                            .createQuery(hqlQuery)
                            .setParameter(
                                    "postSlug",
                                    postSlug
                            )
                            .executeUpdate();
            entityTransaction.commit();

            if (rowChangeNumber == 1) {
                return postSlug;
            }
            return null;

        } catch (Exception e) {
            throw new BusinessException(
                    CodeStatus.CONFLICT,
                    Message.Error.DELETE_FAIL,
                    "post",
                    "post slug",
                    postSlug
            );

        }
    }

    /**
     * Close connection
     *
     * @param postSlug input of task
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected void postExecute(String postSlug) {
        if(Objects.nonNull(entityManager)){
            entityManager.close();
        }
    }
}
