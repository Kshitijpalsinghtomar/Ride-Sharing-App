package com.rideshare;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RideSharingSystemTest {
    @Test
    void testBikeRideFare() {
        Ride ride = new BikeRide("TestDriver", "BK0001", 5.0);
        assertEquals(50.0, ride.calculateFare());
    }

    @Test
    void testCarRideFare() {
        Ride ride = new CarRide("TestDriver", "CR0001", 3.5);
        assertEquals(70.0, ride.calculateFare());
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
            new BikeRide("TestDriver", "BK0001", -1.0);
        });
        assertEquals("Distance must be greater than 0.", exception.getMessage());
    }

    @Test
    void testNegativeDistanceCar() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new CarRide("TestDriver", "CR0001", 0.0);
        });
        assertEquals("Distance must be greater than 0.", exception.getMessage());
    }

    @Test
    void testEmptyDriverName() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new BikeRide("", "BK0001", 2.0);
        });
        assertEquals("Driver name cannot be empty.", exception.getMessage());
    }

    @Test
    void testEmptyVehicleNumber() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new CarRide("TestDriver", "", 2.0);
        });
        assertEquals("Vehicle number cannot be empty.", exception.getMessage());
    }

    @Test
    void testCaseInsensitiveRideType() {
        assertDoesNotThrow(() -> {
            Ride ride = new BikeRide("TestDriver", "BK0001", 1.0);
            assertEquals(10.0, ride.calculateFare());
        });
        assertDoesNotThrow(() -> {
            Ride ride = new CarRide("TestDriver", "CR0001", 1.0);
            assertEquals(20.0, ride.calculateFare());
        });
    }
}
