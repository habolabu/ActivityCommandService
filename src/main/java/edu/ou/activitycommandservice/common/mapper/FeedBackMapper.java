package edu.ou.activitycommandservice.common.mapper;

import edu.ou.activitycommandservice.data.entity.FeedBackEntity;
import edu.ou.activitycommandservice.data.pojo.request.feedBack.FeedBackAddRequest;
import edu.ou.activitycommandservice.data.pojo.request.feedBack.FeedBackUpdateRequest;
import edu.ou.coreservice.common.util.SlugUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper
public interface FeedBackMapper {
    FeedBackMapper INSTANCE = Mappers.getMapper(FeedBackMapper.class);

    /**
     * Map FeedBackAddRequest object to FeedBackEntity object
     *
     * @param feedBackAddRequest represents for FeedBackAddRequest object
     * @return FeedBackEntity object
     * @author Nguyen Trung Kien - OU
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "title", source = "title")
    @Mapping(target = "slug", source = "title", qualifiedByName = "titleToSlug")
    @Mapping(target = "content", source = "content")
    @Mapping(target = "userId", source = "userId")
    @Mapping(target = "feedBackTypeId", source = "feedBackTypeId")
    @Mapping(target = "isDeleted", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    FeedBackEntity fromFeedBackAddRequest(FeedBackAddRequest feedBackAddRequest);

    /**
     * Map FeedBackUpdateRequest object to FeedBackEntity object
     *
     * @param feedBackUpdateRequest represents for FeedBackUpdateRequest object
     * @return FeedBackEntity object
     * @author Nguyen Trung Kien - OU
     */
    @Mapping(target = "title", source = "title")
    @Mapping(target = "slug", source = "title", qualifiedByName = "titleToSlug")
    @Mapping(target = "content", source = "content")
    @Mapping(target = "userId", source = "userId")
    @Mapping(target = "feedBackTypeId", source = "feedBackTypeId")
    @Mapping(target = "isDeleted", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    FeedBackEntity fromFeedBackUpdateRequest(FeedBackUpdateRequest feedBackUpdateRequest);

    /**
     * Convert feedback title to feedback type slug
     *
     * @param title title of feedback
     * @return slug of feedback
     * @author Nguyen Trung Kien - OU
     */
    @Named("titleToSlug")
    static String toSlug(String title) {
        return SlugUtils.createSlug(title);
    }
}
