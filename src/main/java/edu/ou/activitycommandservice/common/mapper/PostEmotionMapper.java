package edu.ou.activitycommandservice.common.mapper;

import edu.ou.activitycommandservice.data.entity.PostEmotionEntity;
import edu.ou.activitycommandservice.data.entity.PostEmotionEntityPK;
import edu.ou.activitycommandservice.data.pojo.request.postEmotion.PostEmotionAddRequest;
import edu.ou.activitycommandservice.data.pojo.request.postEmotion.PostEmotionDeleteRequest;
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
public abstract class PostEmotionMapper {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * Map PostEmotionAddRequest object to PostEmotionEntity object
     *
     * @param postEmotionAddRequest represents for PostEmotionAddRequest object
     * @return PostEmotionEntity object
     * @author Nguyen Trung Kien - OU
     */
    @Mapping(target = "postId", source = "postId")
    @Mapping(target = "emotionId", source = "emotionId")
    @Mapping(target = "userId", ignore = true, qualifiedByName = "currentUserToUserId")
    public abstract PostEmotionEntity fromPostEmotionAddRequest(PostEmotionAddRequest postEmotionAddRequest);

    /**
     * Map PostEmotionDeleteRequest object to PostEmotionEntityPK object
     *
     * @param postEmotionDeleteRequest represents for PostEmotionDeleteRequest object
     * @return PostEmotionEntityPK object
     * @author Nguyen Trung Kien - OU
     */
    @Mapping(target = "postId", source = "postId")
    @Mapping(target = "userId", ignore = true, qualifiedByName = "currentUserToUserId")
    @Mapping(target = "emotionId", ignore = true)
    public abstract PostEmotionEntityPK fromPostEmotionDeleteRequest(PostEmotionDeleteRequest postEmotionDeleteRequest);

    /**
     * Convert current user id
     *
     * @return id of user
     * @author Nguyen Trung Kien - OU
     */
    @Named("currentUserToUserId")
    int getCurrentUserId() {
        Map<String, String> accountInfo = SecurityUtils.getCurrentAccount(rabbitTemplate);
        return Integer.parseInt(accountInfo.get("userId"));
    }
}
