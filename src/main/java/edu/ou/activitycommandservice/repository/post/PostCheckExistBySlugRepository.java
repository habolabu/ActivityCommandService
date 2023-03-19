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
public class PostCheckExistBySlugRepository extends BaseRepository<String, Boolean> {
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
     * Check exist status of post
     *
     * @param postSlug slug of post
     * @return exist status
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected Boolean doExecute(String postSlug) {
        final String hqlQuery = "FROM PostEntity P WHERE P.slug = :postSlug AND P.isDeleted IS NULL";

        try {
            entityManager
                    .unwrap(Session.class)
                    .createQuery(hqlQuery)
                    .setParameter(
                            "postSlug",
                            postSlug
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
    protected void postExecute(String areaId) {
        if(Objects.nonNull(entityManager)){
            entityManager.close();
        }
    }
}
