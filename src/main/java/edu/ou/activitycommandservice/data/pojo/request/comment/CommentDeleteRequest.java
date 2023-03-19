package edu.ou.activitycommandservice.data.pojo.request.comment;

import edu.ou.coreservice.data.pojo.request.base.IBaseRequest;
import lombok.Data;

import javax.validation.constraints.Min;

@Data
public class CommentDeleteRequest implements IBaseRequest {
    @Min(
            value = 1,
            message = "The value must be greater or equals than 1"
    )
    private int id;
}
