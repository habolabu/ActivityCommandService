package edu.ou.activitycommandservice.controller.feedBack;

import edu.ou.activitycommandservice.common.constant.Endpoint;
import edu.ou.activitycommandservice.data.pojo.request.feedBack.FeedBackAddRequest;
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
@RequestMapping(Endpoint.FeedBack.BASE)
public class FeedBackAddController {
    private final IBaseService<IBaseRequest, IBaseResponse> feedBackAddService;

    /**
     * Add new feedback
     *
     * @param feedBackAddRequest new feedback information
     * @return slug of new feedback
     * @author Nguyen Trung Kien - OU
     */
    @PreAuthorize(SecurityPermission.ADD_NEW_FEEDBACK)
    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<IBaseResponse> addNewFeedBack(
            @Validated
            @RequestBody
            FeedBackAddRequest feedBackAddRequest
    ) {
        return new ResponseEntity<>(
                feedBackAddService.execute(feedBackAddRequest),
                HttpStatus.OK
        );
    }
}
