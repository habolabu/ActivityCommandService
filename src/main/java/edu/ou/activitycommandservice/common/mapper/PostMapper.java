package edu.ou.activitycommandservice.common.mapper;

import edu.ou.activitycommandservice.data.entity.PostEntity;
import edu.ou.activitycommandservice.data.pojo.request.post.PostAddRequest;
import edu.ou.activitycommandservice.data.pojo.request.post.PostUpdateRequest;
import edu.ou.coreservice.common.util.SlugUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.UUID;

@Mapper
public interface PostMapper {
    PostMapper INSTANCE = Mappers.getMapper(PostMapper.class);

    /**
     * Map PostAddRequest object to PostEntity object
     *
     * @param postAddRequest represents for PostAddRequest object
     * @return PostEntity object
     * @author Nguyen Trung Kien - OU
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "title", source = "title")
    @Mapping(target = "slug", source = "title", qualifiedByName = "titleToSlug")
    @Mapping(target = "totalComment", ignore = true)
    @Mapping(target = "totalEmotion", ignore = true)
    @Mapping(target = "edited", ignore = true)
    @Mapping(target = "isDeleted", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    PostEntity fromPostAddRequest(PostAddRequest postAddRequest);

    /**
     * Map PostUpdateRequest object to PostEntity object
     *
     * @param postUpdateRequest represents for PostUpdateRequest object
     * @return PostEntity object
     * @author Nguyen Trung Kien - OU
     */
    @Mapping(target = "title", source = "title")
    @Mapping(target = "slug", source = "title", qualifiedByName = "titleToSlug")
    @Mapping(target = "totalComment", ignore = true)
    @Mapping(target = "totalEmotion", ignore = true)
    @Mapping(target = "edited", ignore = true, qualifiedByName = "setEditedStatus")
    @Mapping(target = "isDeleted", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    PostEntity fromPostUpdateRequest(PostUpdateRequest postUpdateRequest);

    /**
     * Convert post title to post slug
     *
     * @param title title of post
     * @author Nguyen Trung Kien - OU
     */
    @Named("titleToSlug")
    static String toSlug(String title) {
        return String.format("%s-%s", SlugUtils.createSlug(title), UUID.randomUUID());
    }

    /**
     * Set edited status
     *
     * @return edited status of post
     * @author Nguyen Trung Kien - OU
     */
    @Named("setEditedStatus")
    static boolean setEditedStatus() {
        return true;
    }
}
