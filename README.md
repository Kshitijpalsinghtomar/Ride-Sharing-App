# Ride Sharing System

## Overview

Ride Sharing System is a Java mini-project inspired by real-world platforms like Ola and Uber. It is designed for clarity, reliability, and future growth, with a focus on practical business needs and user experience.

- Friendly, conversational prompts and clear error messages
- Currency formatting that matches your system locale
- Clean, modular code using OOP principles for easy maintenance
- Ready for new features like driver ratings, payment options, and more
- Well-tested with JUnit to ensure reliability

This project is ideal for students, professionals, and anyone interested in building scalable, user-friendly business applications.

## Features
- Abstract class `Ride` for all ride types
- Encapsulated driver and vehicle details
- Subclasses `BikeRide` and `CarRide` with dynamic, demand-aware fare calculation
- Custom exception for invalid ride types
- Input validation for distance and ride type
- Currency formatting for global use and fare breakdown output
- Modular structure for future business features
- JUnit tests for reliability

## Usage
Run the main class and follow the prompts:
1. Enter ride type (`bike` or `car`)
2. Enter distance in kilometers
3. Rate current demand/traffic (1-5, press Enter to accept the smart default)

Sample output:
```
Welcome to Ride Sharing!

Please select your ride type (bike or car): car
Enter distance to travel in kilometers: 10
Rate the current demand/traffic (1-5, 1 = calm, 5 = peak) [press Enter for default 3]: 4

--- Ride Details ---
Driver: Priya Singh
Vehicle No: CR5678
Distance: 10.00 km
Demand Level: 4 (surge 1.45x)
Base Fare: ₹25.00
Distance Fare: ₹145.00
Booking Fee: ₹6.00
Estimated Fare: ₹176.00
Enjoy your ride!
```

## Testing
JUnit tests are included in `RideSharingSystemTest.java` and cover:
- Fare calculation for bike and car rides
- Input validation and error handling
- Edge cases and future extensibility

## Business Context & Evaluation Criteria
- **Code Quality:** Clean, modular, and well-documented code
- **Functionality:** Meets all specifications and handles errors gracefully
- **Testing:** Comprehensive unit tests for all major components
- **Originality:** Practical design, custom exception, and simulated data fetching
- **Internationalization:** Ready for global deployment and business expansion
- **Scalability:** Easily extendable for new features and business needs
- **Documentation:** Clear instructions for users and developers

## Author
Kshitij Pal Singh Tomar
