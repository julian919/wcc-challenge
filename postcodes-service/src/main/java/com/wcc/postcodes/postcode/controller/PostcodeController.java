package com.wcc.postcodes.postcode.controller;

import com.wcc.postcodes.postcode.dto.DistanceResponseDto;
import com.wcc.postcodes.postcode.dto.PostcodeDto;
import com.wcc.postcodes.postcode.dto.UpdatePostcodeRequestDto;
import com.wcc.postcodes.postcode.service.PostcodeService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/postcodes")
public class PostcodeController {

    private final PostcodeService service;

    public PostcodeController(PostcodeService service) {
        this.service = service;
    }

    @GetMapping("/distance")
    @PreAuthorize("hasAuthority('CALCULATE_DISTANCE')")
    public DistanceResponseDto calculatePostcodesDistanceInKm(@RequestParam String from, @RequestParam String to) {
        return service.calculatePostcodesDistanceInKm(from, to);
    }

    @GetMapping("/{postcode}")
    @PreAuthorize("hasAuthority('READ_POSTCODE')")
    public PostcodeDto getPostcode(@PathVariable String postcode) {
        return service.getPostcode(postcode);
    }

    @PutMapping("/{postcode}")
    @PreAuthorize("hasAuthority('UPDATE_POSTCODE')")
    public PostcodeDto updatePostcodeCoordinates(@PathVariable String postcode,
                                                 @Valid @RequestBody UpdatePostcodeRequestDto request) {
        return service.updatePostcodeCoordinates(postcode, request.latitude(), request.longitude());
    }
}
