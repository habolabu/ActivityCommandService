package edu.ou.activitycommandservice.data.pojo.request.emotion;

import edu.ou.coreservice.data.pojo.request.base.IBaseRequest;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class EmotionAddRequest implements IBaseRequest {
    @NotBlank
    @Size(
            min = 1,
            max = 50,
            message = "The value must be greater than or equals to 1 and less than or equals to 50 characters"
    )
    private String name;

    @NotNull
    private MultipartFile icon;
}
