package edu.ou.activitycommandservice.controller.post;

import edu.ou.activitycommandservice.common.constant.Endpoint;
import edu.ou.activitycommandservice.data.pojo.request.post.PostUpdateRequest;
import edu.ou.coreservice.common.constant.SecurityPermission;
import edu.ou.coreservice.data.pojo.request.base.IBaseRequest;
import edu.ou.coreservice.data.pojo.response.base.IBaseResponse;
import edu.ou.coreservice.service.base.IBaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(Endpoint.Post.BASE)
public class PostUpdateController {
    private final IBaseService<IBaseRequest, IBaseResponse> postUpdateService;

    /**
     * Update exist post
     *
     * @param postUpdateRequest new post information
     * @return slug of exist post
     * @author Nguyen Trung Kien - OU
     */
    @PreAuthorize(SecurityPermission.MODIFY_EXIST_POST)
    @PutMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<IBaseResponse> updateExistPost(
            @Validated
            @RequestBody
            PostUpdateRequest postUpdateRequest
    ) {
        return new ResponseEntity<>(
                postUpdateService.execute(postUpdateRequest),
                HttpStatus.OK
        );
    }
}
