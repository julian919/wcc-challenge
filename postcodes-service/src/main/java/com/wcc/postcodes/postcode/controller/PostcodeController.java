package com.wcc.postcodes.postcode.controller;

import com.wcc.postcodes.postcode.dto.DistanceResponseDto;
import com.wcc.postcodes.postcode.dto.PostcodeDto;
import com.wcc.postcodes.postcode.service.PostcodeService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
    public DistanceResponseDto calculatePostcodesDistanceInKm(@RequestParam String from, @RequestParam String to) {
        return service.calculatePostcodesDistanceInKm(from, to);
    }

    @GetMapping("/{postcode}")
    public PostcodeDto getPostcode(@PathVariable String postcode) {
        return service.getPostcode(postcode);
    }
}
