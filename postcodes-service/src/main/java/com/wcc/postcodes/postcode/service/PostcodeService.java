package com.wcc.postcodes.postcode.service;

import com.wcc.postcodes.postcode.dto.DistanceResponseDto;
import com.wcc.postcodes.postcode.dto.PostcodeDto;
import com.wcc.postcodes.postcode.model.Postcode;
import com.wcc.postcodes.postcode.repository.PostcodeRepository;
import com.wcc.postcodes.postcode.utility.DistanceCalculator;
import java.util.Locale;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class PostcodeService {

    private final PostcodeRepository repository;

    public PostcodeService(PostcodeRepository repository) {
        this.repository = repository;
    }

    public PostcodeDto getPostcode(String postcode) {
        return PostcodeDto.from(getPostcodeDocument(postcode));
    }

    public DistanceResponseDto calculatePostcodesDistanceInKm(String fromPostcode, String toPostcode) {
        Postcode postcode1 = getPostcodeDocument(fromPostcode);
        Postcode postcode2 = getPostcodeDocument(toPostcode);
        double distanceInKm = DistanceCalculator.calculateDistance(
                postcode1.latitude(), postcode1.longitude(), postcode2.latitude(), postcode2.longitude());
        return new DistanceResponseDto(PostcodeDto.from(postcode1), PostcodeDto.from(postcode2), distanceInKm, "km");
    }

    private Postcode getPostcodeDocument(String postcode) {
        String upperCased = postcode.toUpperCase(Locale.ROOT);
        return repository.findByPostcode(upperCased)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Postcode not found: " + upperCased));
    }
}
