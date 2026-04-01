import java.util.HashMap;
import java.util.Map;

// Custom Exception for Invalid Booking
class InvalidBookingException extends Exception {
    public InvalidBookingException(String message) {
        super(message);
    }
}

// Validator Class
class InvalidBookingValidator {

    public static void validate(String roomType, int roomsRequested, Map<String, Integer> inventory)
            throws InvalidBookingException {

        // Check if room type exists
        if (!inventory.containsKey(roomType)) {
            throw new InvalidBookingException("Error: Invalid room type selected.");
        }

        // Check for valid room count
        if (roomsRequested <= 0) {
            throw new InvalidBookingException("Error: Number of rooms must be greater than zero.");
        }

        // Check availability
        int available = inventory.get(roomType);
        if (roomsRequested > available) {
            throw new InvalidBookingException("Error: Not enough rooms available. Only " + available + " left.");
        }
    }
}

// Main Class
public class UseCase9ErrorHandlingValidation{

    public static void main(String[] args) {

        // Inventory Setup
        Map<String, Integer> inventory = new HashMap<>();
        inventory.put("Single", 5);
        inventory.put("Double", 3);
        inventory.put("Suite", 2);

        // Sample Inputs (You can modify these to test)
        String roomType = "Double";
        int roomsRequested = 2;

        try {
            // Step 1: Validate input
            InvalidBookingValidator.validate(roomType, roomsRequested, inventory);

            // Step 2: Process booking (only if valid)
            int remaining = inventory.get(roomType) - roomsRequested;
            inventory.put(roomType, remaining);

            System.out.println("Booking Successful!");
            System.out.println("Room Type: " + roomType);
            System.out.println("Rooms Booked: " + roomsRequested);
            System.out.println("Remaining Rooms: " + remaining);

        } catch (InvalidBookingException e) {
            // Graceful failure handling
            System.out.println(e.getMessage());
        }

        // System continues running safely
        System.out.println("\nSystem is still running...");
    }
}