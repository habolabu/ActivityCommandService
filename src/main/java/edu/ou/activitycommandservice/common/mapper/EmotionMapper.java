package edu.ou.activitycommandservice.common.mapper;

import com.cloudinary.Cloudinary;
import edu.ou.activitycommandservice.common.util.CloudinaryUtils;
import edu.ou.activitycommandservice.data.entity.EmotionEntity;
import edu.ou.activitycommandservice.data.pojo.request.emotion.EmotionAddRequest;
import edu.ou.activitycommandservice.data.pojo.request.emotion.EmotionUpdateRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Mapper(componentModel = "spring")
@Component
public abstract class EmotionMapper {
    @Autowired
    private Cloudinary cloudinary;

    /**
     * Map EmotionAddRequest object to EmotionEntity object
     *
     * @param emotionAddRequest represents for EmotionAddRequest object
     * @return EmotionEntity object
     * @author Nguyen Trung Kien
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "name", source = "name")
    @Mapping(target = "icon", source = "icon", qualifiedByName = "fileToUrl")
    @Mapping(target = "isDeleted", ignore = true)
    public abstract EmotionEntity fromEmotionAddRequest(EmotionAddRequest emotionAddRequest);

    /**
     * Map EmotionUpdateRequest object to EmotionEntity object
     *
     * @param emotionUpdateRequest represents for EmotionUpdateRequest object
     * @return EmotionEntity object
     * @author Nguyen Trung Kien - OU
     */
    @Mapping(target = "name", source = "name")
    @Mapping(target = "icon", source = "icon", qualifiedByName = "fileToUrl")
    @Mapping(target = "isDeleted", ignore = true)
    public abstract EmotionEntity fromEmotionUpdateRequest(EmotionUpdateRequest emotionUpdateRequest);

    /**
     * Upload icon file image to cloud
     *
     * @param icon icon image file
     * @return url
     * @author Nguyen Trung Kien - OU
     */
    @Named("fileToUrl")
    String uploadFile(MultipartFile icon) throws IOException {
        return CloudinaryUtils.uploadImage(
                cloudinary,
                icon,
                "auto",
                "emoji"
        );
    }
}
