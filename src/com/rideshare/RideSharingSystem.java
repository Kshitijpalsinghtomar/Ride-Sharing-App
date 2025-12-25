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
    private static final double BASE_SURGE = 1.0;
    private static final double DEMAND_STEP = 0.15;
    private static final int LONG_TRIP_THRESHOLD_KM = 12;
    private static final double LONG_TRIP_SURGE_BUMP = 0.05;
    private final String driverName;
    private final String vehicleNumber;
    private final double distance;
    private final int demandLevel;
    private FareDetails cachedFareDetails;

    /**
     * Constructs a Ride object.
     * @param driverName Name of the driver
     * @param vehicleNumber Vehicle number
     * @param distance Distance in kilometers (must be > 0)
     */
    public Ride(String driverName, String vehicleNumber, double distance) {
        this(driverName, vehicleNumber, distance, 1);
    }

    /**
     * Constructs a Ride object with demand awareness.
     * @param driverName Name of the driver
     * @param vehicleNumber Vehicle number
     * @param distance Distance in kilometers (must be > 0)
     * @param demandLevel Demand/traffic level (1-5)
     */
    public Ride(String driverName, String vehicleNumber, double distance, int demandLevel) {
        if (driverName == null || driverName.isBlank()) {
            throw new IllegalArgumentException("Driver name cannot be empty.");
        }
        if (vehicleNumber == null || vehicleNumber.isBlank()) {
            throw new IllegalArgumentException("Vehicle number cannot be empty.");
        }
        if (distance <= 0) {
            throw new IllegalArgumentException("Distance must be greater than 0.");
        }
        if (demandLevel < 1 || demandLevel > 5) {
            throw new IllegalArgumentException("Demand level must be between 1 and 5. Received: " + demandLevel);
        }
        this.driverName = driverName;
        this.vehicleNumber = vehicleNumber;
        this.distance = distance;
        this.demandLevel = demandLevel;
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
     * Gets the current demand level.
     */
    public int getDemandLevel() {
        return demandLevel;
    }

    /**
     * Calculates surge multiplier using demand and distance signals.
     */
    protected double calculateSurgeMultiplier() {
        double surge = BASE_SURGE + (DEMAND_STEP * (getDemandLevel() - 1)); // Demand-only up to 1.6x (1.65x with long-trip bump)
        if (getDistance() > LONG_TRIP_THRESHOLD_KM) {
            surge += LONG_TRIP_SURGE_BUMP; // Slight bump for long trips
        }
        return surge;
    }

    /**
     * Shared fare computation with detailed breakdown.
     */
    protected FareDetails buildFareDetails(double baseFare, double ratePerKm, double bookingFee, double minimumFare) {
        double surgeMultiplier = calculateSurgeMultiplier();
        double distanceFare = getDistance() * ratePerKm * surgeMultiplier;
        double subtotal = baseFare + distanceFare + bookingFee;
        double totalFare = Math.max(subtotal, minimumFare);
        return new FareDetails(baseFare, distanceFare, bookingFee, surgeMultiplier, minimumFare, subtotal, totalFare);
    }

    /**
     * Calculates the fare for the ride.
     */
    public double calculateFare() {
        return estimateFare().getTotalFare();
    }

    /**
     * Calculates fare and provides a breakdown.
     */
    public final FareDetails estimateFare() {
        if (cachedFareDetails == null) {
            cachedFareDetails = buildFare();
        }
        return cachedFareDetails;
    }

    /**
     * Template method implemented by ride types to build fare details.
     */
    protected abstract FareDetails buildFare();
}

/**
 * BikeRide class for bike rides.
 */
class BikeRide extends Ride {
    private static final double BASE_FARE = 8.0;
    private static final double RATE_PER_KM = 7.0;
    private static final double BOOKING_FEE = 2.0;
    private static final double MINIMUM_FARE = 25.0;

    public BikeRide(String driverName, String vehicleNumber, double distance) {
        super(driverName, vehicleNumber, distance);
    }

    public BikeRide(String driverName, String vehicleNumber, double distance, int demandLevel) {
        super(driverName, vehicleNumber, distance, demandLevel);
    }

    @Override
    public FareDetails buildFare() {
        return buildFareDetails(BASE_FARE, RATE_PER_KM, BOOKING_FEE, MINIMUM_FARE);
    }
}

/**
 * CarRide class for car rides.
 */
class CarRide extends Ride {
    private static final double BASE_FARE = 25.0;
    private static final double RATE_PER_KM = 10.0;
    private static final double BOOKING_FEE = 6.0;
    private static final double MINIMUM_FARE = 50.0;

    public CarRide(String driverName, String vehicleNumber, double distance) {
        super(driverName, vehicleNumber, distance);
    }

    public CarRide(String driverName, String vehicleNumber, double distance, int demandLevel) {
        super(driverName, vehicleNumber, distance, demandLevel);
    }

    @Override
    public FareDetails buildFare() {
        return buildFareDetails(BASE_FARE, RATE_PER_KM, BOOKING_FEE, MINIMUM_FARE);
    }
}

/**
 * Main class for the Ride Sharing System.
 * Designed for international business standards and extensibility.
 */
public class RideSharingSystem {
    private static final int DEFAULT_DEMAND_LEVEL = 3;
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
     * Safely parses the demand level with defaulting and validation messaging.
     */
    private static int parseDemandLevel(String demandInput) {
        if (demandInput == null || demandInput.isBlank()) {
            return DEFAULT_DEMAND_LEVEL;
        }
        try {
            int parsed = Integer.parseInt(demandInput);
            if (parsed < 1 || parsed > 5) {
                throw new IllegalArgumentException("Demand level must be between 1 and 5. Received: " + parsed);
            }
            return parsed;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Demand level must be a number between 1 and 5. Received: " + demandInput);
        }
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
            System.out.print("Rate the current demand/traffic (1-5, 1 = calm, 5 = peak) [press Enter for default 3]: ");
            String demandInput = scanner.nextLine().trim();
            int demandLevel = parseDemandLevel(demandInput);

            Ride ride;
            switch (rideType.toLowerCase()) {
                case "bike":
                    ride = new BikeRide(getDriverName(rideType), getVehicleNumber(rideType), distance, demandLevel);
                    break;
                case "car":
                    ride = new CarRide(getDriverName(rideType), getVehicleNumber(rideType), distance, demandLevel);
                    break;
                default:
                    throw new InvalidRideTypeException("We only offer bike and car rides at the moment. Please enter a valid option.");
            }

            System.out.println("\n--- Your Ride Details ---");
            System.out.println("Driver: " + ride.getDriverName());
            System.out.println("Vehicle Number: " + ride.getVehicleNumber());
            System.out.printf("Distance: %.2f km\n", ride.getDistance());
            FareDetails fareDetails = ride.estimateFare();
            System.out.printf("Demand Level: %d (surge %.2fx)\n", ride.getDemandLevel(), fareDetails.getSurgeMultiplier());
            System.out.println("Base Fare: " + formatCurrency(fareDetails.getBaseFare(), userLocale));
            System.out.println("Distance Fare: " + formatCurrency(fareDetails.getDistanceFare(), userLocale));
            System.out.println("Booking Fee: " + formatCurrency(fareDetails.getBookingFee(), userLocale));
            if (fareDetails.isMinimumFareApplied()) {
                System.out.println("Minimum Fare Applied: " + formatCurrency(fareDetails.getMinimumFare(), userLocale));
            }
            System.out.println("Estimated Fare: " + formatCurrency(fareDetails.getTotalFare(), userLocale));
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

/**
 * Represents a fare breakdown.
 */
class FareDetails {
    private final double baseFare;
    private final double distanceFare;
    private final double bookingFee;
    private final double surgeMultiplier;
    private final double minimumFare;
    private final double subtotal;
    private final double totalFare;

    public FareDetails(double baseFare, double distanceFare, double bookingFee, double surgeMultiplier, double minimumFare, double subtotal, double totalFare) {
        this.baseFare = baseFare;
        this.distanceFare = distanceFare;
        this.bookingFee = bookingFee;
        this.surgeMultiplier = surgeMultiplier;
        this.minimumFare = minimumFare;
        this.subtotal = subtotal;
        this.totalFare = totalFare;
    }

    public double getBaseFare() {
        return baseFare;
    }

    public double getDistanceFare() {
        return distanceFare;
    }

    public double getBookingFee() {
        return bookingFee;
    }

    public double getSurgeMultiplier() {
        return surgeMultiplier;
    }

    public double getMinimumFare() {
        return minimumFare;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public double getTotalFare() {
        return totalFare;
    }

    public boolean isMinimumFareApplied() {
        return subtotal < minimumFare;
    }
}
