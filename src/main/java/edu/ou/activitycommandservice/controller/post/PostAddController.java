package edu.ou.activitycommandservice.controller.post;

import edu.ou.activitycommandservice.common.constant.Endpoint;
import edu.ou.activitycommandservice.data.pojo.request.post.PostAddRequest;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(Endpoint.Post.BASE)
public class PostAddController {
    private final IBaseService<IBaseRequest, IBaseResponse> postAddService;

    /**
     * Add new post
     *
     * @param postAddRequest new post information
     * @return slug of new post
     * @author Nguyen Trung Kien - OU
     */
    @PreAuthorize(SecurityPermission.ADD_NEW_POST)
    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<IBaseResponse> addNewPost(
            @Validated
            @RequestBody
            PostAddRequest postAddRequest
    ) {
        return new ResponseEntity<>(
                postAddService.execute(postAddRequest),
                HttpStatus.OK
        );
    }
}
