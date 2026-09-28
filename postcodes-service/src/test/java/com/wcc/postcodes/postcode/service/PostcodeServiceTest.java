package com.wcc.postcodes.postcode.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.wcc.postcodes.postcode.dto.DistanceResponseDto;
import com.wcc.postcodes.postcode.dto.PostcodeDto;
import com.wcc.postcodes.postcode.exception.PostcodeNotFoundException;
import com.wcc.postcodes.postcode.model.Postcode;
import com.wcc.postcodes.postcode.repository.PostcodeRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;

class PostcodeServiceTest {

    private final PostcodeRepository repository = mock(PostcodeRepository.class);
    private final PostcodeService service = new PostcodeService(repository);

    private final Postcode b34 = new Postcode("id-b34", "B34", 52.4964133, -1.7817039);
    private final Postcode sw1a = new Postcode("id-sw1a", "SW1A", 51.5044592, -0.1321624);

    @Test
    void getPostcodeLooksUpTheUppercasePostcode() {
        when(repository.findByPostcode("B34")).thenReturn(Optional.of(b34));

        PostcodeDto postcode = service.getPostcode("b34");

        assertEquals("B34", postcode.postcode());
        assertEquals(52.4964133, postcode.latitude());
        assertEquals(-1.7817039, postcode.longitude());
    }

    @Test
    void getPostcodeThrowsNotFoundForAnUnknownPostcode() {
        when(repository.findByPostcode("ZZ99")).thenReturn(Optional.empty());

        assertThrows(PostcodeNotFoundException.class, () -> service.getPostcode("ZZ99"));
    }

    @Test
    void calculatePostcodesDistanceInKmReturnsBothLocationsAndTheDistance() {
        when(repository.findByPostcode("B34")).thenReturn(Optional.of(b34));
        when(repository.findByPostcode("SW1A")).thenReturn(Optional.of(sw1a));

        DistanceResponseDto response = service.calculatePostcodesDistanceInKm("B34", "SW1A");

        assertEquals("B34", response.from().postcode());
        assertEquals("SW1A", response.to().postcode());
        assertEquals(157.84653946737518, response.distance());
        assertEquals("km", response.unit());
    }

    @Test
    void updatePostcodeCoordinatesSavesNewCoordinatesUnderTheSameId() {
        Double updatedLatitude = 52.5;
        Double updatedLongitude = -1.78;

        Postcode updatedPostcode = new Postcode("id-b34", "B34", updatedLatitude, updatedLongitude);

        when(repository.findByPostcode("B34")).thenReturn(Optional.of(b34));
        when(repository.save(any(Postcode.class))).thenReturn(updatedPostcode);

        PostcodeDto updated = service.updatePostcodeCoordinates("B34", updatedLatitude, updatedLongitude);

        assertEquals(updatedLatitude, updated.latitude());
        assertEquals(updatedLongitude, updated.longitude());
        verify(repository).save(new Postcode("id-b34", "B34", 52.5, -1.78));
    }

}
