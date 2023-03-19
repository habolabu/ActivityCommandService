package edu.ou.activitycommandservice.common.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Endpoint {
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class FeedBackType {
        public static final String BASE = "/feedback-type";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class FeedBack {
        public static final String BASE = "/feedback";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class Emotion {
        public static final String BASE = "/emotion";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class Comment {
        public static final String BASE = "/comment";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class CommentEmotion {
        public static final String BASE = "/comment-emotion";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class Post {
        public static final String BASE = "/post";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class PostEmotion {
        public static final String BASE = "/post-emotion";
    }
}
