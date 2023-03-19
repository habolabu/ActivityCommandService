package edu.ou.activitycommandservice.data.pojo.request.post;

import edu.ou.coreservice.data.pojo.request.base.IBaseRequest;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class PostAddRequest implements IBaseRequest {
    @NotBlank
    @Size(
            min = 1,
            max = 255,
            message = "The value must be greater than or equals to 1 and less than or equals to 255 characters"
    )
    private String title;
    @NotBlank
    private String content;
}
