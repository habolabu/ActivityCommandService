package edu.ou.activitycommandservice.common.mapper;

import edu.ou.activitycommandservice.data.entity.CommentEmotionEntity;
import edu.ou.activitycommandservice.data.entity.CommentEmotionEntityPK;
import edu.ou.activitycommandservice.data.pojo.request.commentEmotion.CommentEmotionAddRequest;
import edu.ou.activitycommandservice.data.pojo.request.commentEmotion.CommentEmotionDeleteRequest;
import edu.ou.coreservice.common.util.SecurityUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Mapper(componentModel = "spring")
@Component
public abstract class CommentEmotionMapper {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * Map CommentEmotionAddRequest object to CommentEmotionEntity object
     *
     * @param commentEmotionAddRequest represents for CommentEmotionAddRequest object
     * @return CommentEmotionEntity object
     * @author Nguyen Trung Kien
     */
    @Mapping(target = "commentId", source = "commentId")
    @Mapping(target = "emotionId", source = "emotionId")
    @Mapping(target = "userId", ignore = true, qualifiedByName = "currentUserToUserId")
    public abstract CommentEmotionEntity fromCommentEmotionAddRequest(CommentEmotionAddRequest commentEmotionAddRequest);

    /**
     * Map CommentEmotionDeleteRequest object to CommentEmotionEntityPK object
     *
     * @param commentEmotionDeleteRequest represents for CommentEmotionDeleteRequest object
     * @return CommentEmotionEntityPK object
     * @author Nguyen Trung Kien - OU
     */
    @Mapping(target = "commentId", source = "commentId")
    @Mapping(target = "userId", ignore = true, qualifiedByName = "currentUserToUserId")
    @Mapping(target = "emotionId", ignore = true)
    public abstract CommentEmotionEntityPK fromCommentEmotionDeleteRequest(
            CommentEmotionDeleteRequest commentEmotionDeleteRequest);

    /**
     * Convert to current user id
     *
     * @return id of current user
     * @author Nguyen Trung Kien - OU
     */
    @Named("currentUserToUserId")
    int getCurrentUserId() {
        Map<String, String> accountInfo = SecurityUtils.getCurrentAccount(rabbitTemplate);
        return Integer.parseInt(accountInfo.get("userId"));
    }
}
