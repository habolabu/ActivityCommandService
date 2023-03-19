package edu.ou.activitycommandservice.common.mapper;

import edu.ou.activitycommandservice.data.entity.FeedBackTypeEntity;
import edu.ou.activitycommandservice.data.pojo.request.feedBackType.FeedBackTypeAddRequest;
import edu.ou.activitycommandservice.data.pojo.request.feedBackType.FeedBackTypeUpdateRequest;
import edu.ou.coreservice.common.util.SlugUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper
public interface FeedBackTypeMapper {
    FeedBackTypeMapper INSTANCE = Mappers.getMapper(FeedBackTypeMapper.class);

    /**
     * Map FeedBackTypeAddRequest object to FeedBackTypeEntity object
     *
     * @param feedBackTypeAddRequest represents for FeedBackTypeAddRequest object
     * @return FeedBackTypeEntity object
     * @author Nguyen Trung Kien - OU
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "name", source = "name")
    @Mapping(target = "slug", source = "name", qualifiedByName = "nameToSlug")
    @Mapping(target = "isDeleted", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    FeedBackTypeEntity fromFeedBackTypeAddRequest(FeedBackTypeAddRequest feedBackTypeAddRequest);

    /**
     * Map FeedBackTypeUpdateRequest object to FeedBackTypeEntity object
     *
     * @param feedBackTypeUpdateRequest represents for FeedBackTypeUpdateRequest object
     * @return FeedBackTypeEntity object
     * @author Nguyen Trung Kien - OU
     */
    @Mapping(target = "name", source = "name")
    @Mapping(target = "slug", source = "name", qualifiedByName = "nameToSlug")
    @Mapping(target = "isDeleted", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    FeedBackTypeEntity fromFeedBackTypeUpdateRequest(FeedBackTypeUpdateRequest feedBackTypeUpdateRequest);

    /**
     * Convert feedback type name to feedback type slug
     *
     * @param name name of feedback type
     * @return slug of feedback type
     * @author Nguyen Trung Kien - OU
     */
    @Named("nameToSlug")
    static String toSlug(String name) {
        return SlugUtils.createSlug(name);
    }
}
