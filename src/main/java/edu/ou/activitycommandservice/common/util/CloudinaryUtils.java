package edu.ou.activitycommandservice.common.util;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public class CloudinaryUtils {
    /**
     * Upload image to cloudinary
     *
     * @param cloudinary cloudinary
     * @param image image file
     * @param resourceType resource type
     * @param folder folder name in cloudinary
     * @return url of uploaded image
     * @throws IOException exception
     * @author Nguyen Trung Kien - OU
     */
    public static String uploadImage(
            Cloudinary cloudinary,
            MultipartFile image,
            String resourceType,
            String folder
    ) throws IOException {
        return (String)
                cloudinary
                        .uploader()
                        .upload(
                                image.getBytes(),
                                ObjectUtils.asMap(
                                        "resource_type", resourceType,
                                        "folder", folder
                                )
                        )
                        .get("secure_url");
    }
}
