package edu.ou.activitycommandservice.repository.post;

import edu.ou.activitycommandservice.common.constant.CodeStatus;
import edu.ou.activitycommandservice.data.entity.PostEntity;
import edu.ou.coreservice.common.constant.Message;
import edu.ou.coreservice.common.exception.BusinessException;
import edu.ou.coreservice.common.validate.ValidValidation;
import edu.ou.coreservice.repository.base.BaseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PostDeActiveRepository extends BaseRepository<PostEntity, Integer> {
    private final BaseRepository<PostEntity, Integer> postUpdateRepository;
    private final ValidValidation validValidation;

    /**
     * Validate feedBackType
     *
     * @param feedBackType feedBackType
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected void preExecute(PostEntity feedBackType) {
        if (validValidation.isInValid(feedBackType, PostEntity.class)) {
            throw new BusinessException(
                    CodeStatus.INVALID_INPUT,
                    Message.Error.INVALID_INPUT,
                    "post"
            );
        }
    }

    /**
     * de-active exist postEntity
     *
     * @param postEntity postEntity
     * @return id of postEntity
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected Integer doExecute(PostEntity postEntity) {
        postEntity.setIsDeleted(null);

        return postUpdateRepository.execute(postEntity);
    }

    @Override
    protected void postExecute(PostEntity input) {
        // do nothing
    }
}
