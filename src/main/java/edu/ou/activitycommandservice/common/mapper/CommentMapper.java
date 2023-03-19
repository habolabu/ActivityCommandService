package edu.ou.activitycommandservice.common.mapper;

import edu.ou.activitycommandservice.data.entity.CommentEntity;
import edu.ou.activitycommandservice.data.pojo.request.comment.CommentAddRequest;
import edu.ou.activitycommandservice.data.pojo.request.comment.CommentUpdateRequest;
import edu.ou.coreservice.common.util.SecurityUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Mapper(componentModel = "spring")
@Component
public abstract class CommentMapper {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * Map CommentAddRequest object to CommentEntity object
     *
     * @param commentAddRequest represents for CommentAddRequest object
     * @return CommentEntity object
     * @author Nguyen Trung Kien - OU
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "content", source = "content")
    @Mapping(target = "userId", ignore = true, qualifiedByName = "currentUserToUserId")
    @Mapping(target = "postId", source = "postId")
    @Mapping(
            target = "commentId",
            source = "commentId",
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL
    )
    @Mapping(target = "totalEmotion", ignore = true)
    @Mapping(target = "isDeleted", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    public abstract CommentEntity fromCommentAddRequest(CommentAddRequest commentAddRequest);

    /**
     * Map CommentUpdateRequest object to CommentEntity object
     *
     * @param commentUpdateRequest represents for CommentUpdateRequest object
     * @return CommentEntity object
     * @author Nguyen Trung Kien - OU
     */
    @Mapping(target = "content", source = "content")
    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "totalEmotion", ignore = true)
    @Mapping(target = "postId", ignore = true)
    @Mapping(target = "commentId", ignore = true)
    @Mapping(target = "isDeleted", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    public abstract CommentEntity fromCommentUpdateRequest(CommentUpdateRequest commentUpdateRequest);

    /**
     * Convert current user id
     *
     * @return slug of feedback
     * @author Nguyen Trung Kien - OU
     */
    @Named("currentUserToUserId")
    int getCurrentUserId() {
        Map<String, String> accountInfo = SecurityUtils.getCurrentAccount(rabbitTemplate);
        return Integer.parseInt(accountInfo.get("userId"));
    }
}
