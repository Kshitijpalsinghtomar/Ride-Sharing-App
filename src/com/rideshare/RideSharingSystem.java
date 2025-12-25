package com.rideshare;
import java.util.Scanner;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * Custom exception for invalid ride type.
 */
class InvalidRideTypeException extends Exception {
    public InvalidRideTypeException(String message) {
        super(message);
    }
}

/**
 * Abstract Ride class representing a generic ride.
 */
abstract class Ride {
    private final String driverName;
    private final String vehicleNumber;
    private final double distance;

    /**
     * Constructs a Ride object.
     * @param driverName Name of the driver
     * @param vehicleNumber Vehicle number
     * @param distance Distance in kilometers (must be > 0)
     */
    public Ride(String driverName, String vehicleNumber, double distance) {
        if (driverName == null || driverName.isBlank()) {
            throw new IllegalArgumentException("Driver name cannot be empty.");
        }
        if (vehicleNumber == null || vehicleNumber.isBlank()) {
            throw new IllegalArgumentException("Vehicle number cannot be empty.");
        }
        if (distance <= 0) {
            throw new IllegalArgumentException("Distance must be greater than 0.");
        }
        this.driverName = driverName;
        this.vehicleNumber = vehicleNumber;
        this.distance = distance;
    }

    /**
     * Gets the driver name.
     */
    public String getDriverName() {
        return driverName;
    }

    /**
     * Gets the vehicle number.
     */
    public String getVehicleNumber() {
        return vehicleNumber;
    }

    /**
     * Gets the distance in kilometers.
     */
    public double getDistance() {
        return distance;
    }

    /**
     * Calculates the fare for the ride.
     */
    public abstract double calculateFare();
}

/**
 * BikeRide class for bike rides.
 */
class BikeRide extends Ride {
    private static final double RATE_PER_KM = 10.0;

    public BikeRide(String driverName, String vehicleNumber, double distance) {
        super(driverName, vehicleNumber, distance);
    }

    @Override
    public double calculateFare() {
        return getDistance() * RATE_PER_KM;
    }
}

/**
 * CarRide class for car rides.
 */
class CarRide extends Ride {
    private static final double RATE_PER_KM = 20.0;

    public CarRide(String driverName, String vehicleNumber, double distance) {
        super(driverName, vehicleNumber, distance);
    }

    @Override
    public double calculateFare() {
        return getDistance() * RATE_PER_KM;
    }
}

/**
 * Main class for the Ride Sharing System.
 * Designed for international business standards and extensibility.
 */
public class RideSharingSystem {
    /**
     * Simulates fetching driver name from a database/service.
     * In a real-world app, this would query a database or external API.
     */
    private static String getDriverName(String rideType) {
        return rideType.equalsIgnoreCase("bike") ? "Amit Sharma" : "Priya Singh";
    }

    /**
     * Simulates fetching vehicle number from a database/service.
     */
    private static String getVehicleNumber(String rideType) {
        return rideType.equalsIgnoreCase("bike") ? "BK1234" : "CR5678";
    }

    /**
     * Formats currency for internationalization.
     * @param amount Amount to format
     * @param locale Locale for currency
     * @return Formatted currency string
     */
    private static String formatCurrency(double amount, Locale locale) {
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(locale);
        return currencyFormatter.format(amount);
    }

    /**
     * Entry point for the Ride Sharing System.
     * Human-like prompts and extensible design.
     */
    public static void main(String[] args) {
        Locale userLocale = Locale.getDefault(); // Could be set by user input for internationalization
    System.out.println("\nWelcome to Ride Sharing!");
    System.out.println("Book your ride quickly and travel comfortably.\n");
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.print("Choose your ride (bike or car): ");
            String rideType = scanner.nextLine().trim();
            System.out.print("How many kilometers do you want to travel? ");
            double distance = Double.parseDouble(scanner.nextLine().trim());

            Ride ride;
            switch (rideType.toLowerCase()) {
                case "bike":
                    ride = new BikeRide(getDriverName(rideType), getVehicleNumber(rideType), distance);
                    break;
                case "car":
                    ride = new CarRide(getDriverName(rideType), getVehicleNumber(rideType), distance);
                    break;
                default:
                    throw new InvalidRideTypeException("We only offer bike and car rides at the moment. Please enter a valid option.");
            }

            System.out.println("\n--- Your Ride Details ---");
            System.out.println("Driver: " + ride.getDriverName());
            System.out.println("Vehicle Number: " + ride.getVehicleNumber());
            System.out.printf("Distance: %.2f km\n", ride.getDistance());
            System.out.println("Estimated Fare: " + formatCurrency(ride.calculateFare(), userLocale));
            System.out.println("Enjoy your ride!\n");
        } catch (InvalidRideTypeException e) {
            System.out.println(e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Unexpected error: " + e.getMessage());
        }
    }
}
