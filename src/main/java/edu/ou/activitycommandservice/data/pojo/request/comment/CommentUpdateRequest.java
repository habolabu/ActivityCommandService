package edu.ou.activitycommandservice.data.pojo.request.comment;

import edu.ou.coreservice.data.pojo.request.base.IBaseRequest;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Data
public class CommentUpdateRequest implements IBaseRequest {
    @Min(
            value = 1,
            message = "The value must be greater or equals than 1"
    )
    private int id;
    @NotBlank(message = "The value must not be blank")
    private String content;
}
