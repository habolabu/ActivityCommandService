package edu.ou.activitycommandservice.data.pojo.request.feedBack;

import edu.ou.coreservice.data.pojo.request.base.IBaseRequest;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class FeedBackDeleteRequest implements IBaseRequest {
    @NotBlank
    @Size(
            min = 1,
            max = 255,
            message = "The value must be greater than or equals to 1 and less than or equals to 255 characters"
    )
    private String slug;
}
