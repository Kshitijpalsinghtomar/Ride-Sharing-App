package com.rideshare;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RideSharingSystemTest {
    @Test
    void testBikeRideFareWithCalmDemand() {
        Ride ride = new BikeRide("TestDriver", "BK0001", 5.0, 1);
        assertEquals(45.0, ride.calculateFare(), 0.0001);
    }

    @Test
    void testCarRideFareWithPeakDemand() {
        Ride ride = new CarRide("TestDriver", "CR0001", 3.5, 3);
        assertEquals(76.5, ride.calculateFare(), 0.0001);
    }

    @Test
    void testMinimumFareAppliedForShortBikeRide() {
        BikeRide ride = new BikeRide("TestDriver", "BK0001", 0.5, 1);
        FareDetails details = ride.estimateFare();
        assertEquals(25.0, details.getTotalFare(), 0.0001);
        assertTrue(details.isMinimumFareApplied());
    }

    @Test
    void testInvalidRideTypeException() {
        Exception exception = assertThrows(InvalidRideTypeException.class, () -> {
            throw new InvalidRideTypeException("Invalid ride type: bus");
        });
        assertEquals("Invalid ride type: bus", exception.getMessage());
    }

    @Test
    void testNegativeDistanceBike() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new BikeRide("TestDriver", "BK0001", -1.0, 1);
        });
        assertEquals("Distance must be greater than 0.", exception.getMessage());
    }

    @Test
    void testNegativeDistanceCar() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new CarRide("TestDriver", "CR0001", 0.0, 1);
        });
        assertEquals("Distance must be greater than 0.", exception.getMessage());
    }

    @Test
    void testEmptyDriverName() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new BikeRide("", "BK0001", 2.0, 1);
        });
        assertEquals("Driver name cannot be empty.", exception.getMessage());
    }

    @Test
    void testEmptyVehicleNumber() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new CarRide("TestDriver", "", 2.0, 1);
        });
        assertEquals("Vehicle number cannot be empty.", exception.getMessage());
    }

    @Test
    void testDemandLevelValidation() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new CarRide("TestDriver", "CR0001", 1.0, 7);
        });
        assertEquals("Demand level must be between 1 and 5. Received: 7", exception.getMessage());
    }
}
