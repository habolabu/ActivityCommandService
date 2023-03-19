package edu.ou.activitycommandservice.controller.feedBackType;


import edu.ou.activitycommandservice.common.constant.Endpoint;
import edu.ou.activitycommandservice.data.pojo.request.feedBackType.FeedBackTypeAddRequest;
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
@RequestMapping(Endpoint.FeedBackType.BASE)
public class FeedBackTypeAddController {
    private final IBaseService<IBaseRequest, IBaseResponse> feedBackTypeAddService;

    /**
     * Add new feedback type
     *
     * @param feedBackTypeAddRequest new feedback type information
     * @return slug of new feedback type
     * @author Nguyen Trung Kien - OU
     */
    @PreAuthorize(SecurityPermission.ADD_NEW_FEEDBACK_TYPE)
    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<IBaseResponse> addNewFeedBackType(
            @Validated
            @RequestBody
            FeedBackTypeAddRequest feedBackTypeAddRequest
    ) {
        return new ResponseEntity<>(
                feedBackTypeAddService.execute(feedBackTypeAddRequest),
                HttpStatus.OK
        );
    }
}
