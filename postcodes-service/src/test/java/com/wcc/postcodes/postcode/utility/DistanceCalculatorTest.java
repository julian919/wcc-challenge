package com.wcc.postcodes.postcode.utility;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

public class DistanceCalculatorTest {

    @Test
    public void testCalculateDistanceBetweenTwoPoints() {
        double latitude1 = 52.5200;
        double longitude1 = 13.4050;
        double latitude2 = 48.8566;
        double longitude2 = 2.3522;

        double actualDistance = DistanceCalculator.calculateDistance(latitude1, longitude1, latitude2, longitude2);

        assertEquals(877.4633259175432, actualDistance);
    }

    @Test
    public void testCalculateDistanceBetweenSamePoints() {
        double latitude = 0.0;
        double longitude = 0.0;

        double actualDistance = DistanceCalculator.calculateDistance(latitude, longitude, latitude, longitude);

        assertEquals(0.0, actualDistance);
    }
}
