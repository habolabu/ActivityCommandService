package edu.ou.activitycommandservice.data.pojo.request.feedBackType;

import edu.ou.coreservice.data.pojo.request.base.IBaseRequest;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class FeedBackTypeUpdateRequest implements IBaseRequest {
    @Min(
            value = 1,
            message = "The value must be greater or equals than 1"
    )
    private int id;
    @NotBlank
    @Size(
            min = 1,
            max = 255,
            message = "The value must be greater than or equals to 1 and less than or equals to 255 characters"
    )
    private String name;
}
