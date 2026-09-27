package com.wcc.postcodes.postcode.service;

import com.wcc.postcodes.postcode.dto.DistanceResponseDto;
import com.wcc.postcodes.postcode.dto.PostcodeDto;
import com.wcc.postcodes.postcode.exception.PostcodeNotFoundException;
import com.wcc.postcodes.postcode.model.Postcode;
import com.wcc.postcodes.postcode.repository.PostcodeRepository;
import com.wcc.postcodes.postcode.utility.DistanceCalculator;
import java.util.Locale;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class PostcodeService {

    private static final Logger log = LoggerFactory.getLogger(PostcodeService.class);

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
        log.atInfo().setMessage("Distance calculated")
                .addKeyValue("fromPostcode", postcode1.postcode())
                .addKeyValue("toPostcode", postcode2.postcode())
                .addKeyValue("distanceKm", distanceInKm)
                .log();
        return new DistanceResponseDto(PostcodeDto.from(postcode1), PostcodeDto.from(postcode2), distanceInKm, "km");
    }

    public PostcodeDto updatePostcodeCoordinates(String postcode, double latitude, double longitude) {
        Postcode existing = getPostcodeDocument(postcode);
        Postcode updatedPostcode = new Postcode(existing.id(), existing.postcode(), latitude, longitude);
        Postcode updated = repository.save(updatedPostcode);
        return PostcodeDto.from(updated);
    }

    private Postcode getPostcodeDocument(String postcode) {
        String upperCased = postcode.toUpperCase(Locale.ROOT);
        return repository.findByPostcode(upperCased)
                .orElseThrow(() -> new PostcodeNotFoundException(upperCased));
    }
}
