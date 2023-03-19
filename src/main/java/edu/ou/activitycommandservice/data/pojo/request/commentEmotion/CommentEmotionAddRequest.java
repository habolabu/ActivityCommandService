package edu.ou.activitycommandservice.data.pojo.request.commentEmotion;

import edu.ou.coreservice.data.pojo.request.base.IBaseRequest;
import lombok.Data;

import javax.validation.constraints.Min;

@Data
public class CommentEmotionAddRequest implements IBaseRequest {
    @Min(
            value = 1,
            message = "The value must be greater or equals than 1"
    )
    private int commentId;
    @Min(
            value = 1,
            message = "The value must be greater or equals than 1"
    )
    private int emotionId;
}
