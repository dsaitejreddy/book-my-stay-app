import java.util.*;

// Custom Exception
class CancellationException extends Exception {
    public CancellationException(String message) {
        super(message);
    }
}

// Booking Service
class BookingService {

    // Inventory
    private Map<String, Integer> inventory = new HashMap<>();

    // Booking records (bookingId -> roomType)
    private Map<Integer, String> bookings = new HashMap<>();

    // Stack for rollback (released room IDs)
    private Stack<Integer> rollbackStack = new Stack<>();

    private int bookingCounter = 1;

    // Constructor
    public BookingService() {
        inventory.put("Single", 5);
        inventory.put("Double", 3);
        inventory.put("Suite", 2);
    }

    // Book Room
    public int bookRoom(String roomType) throws Exception {

        if (!inventory.containsKey(roomType)) {
            throw new Exception("Invalid room type.");
        }

        if (inventory.get(roomType) <= 0) {
            throw new Exception("No rooms available.");
        }

        // Allocate booking
        int bookingId = bookingCounter++;
        bookings.put(bookingId, roomType);

        // Reduce inventory
        inventory.put(roomType, inventory.get(roomType) - 1);

        System.out.println("Booking confirmed! ID: " + bookingId);

        return bookingId;
    }

    // Cancel Booking
    public void cancelBooking(int bookingId) throws CancellationException {

        // Validate booking existence
        if (!bookings.containsKey(bookingId)) {
            throw new CancellationException("Error: Booking does not exist.");
        }

        String roomType = bookings.get(bookingId);

        // Rollback: push to stack
        rollbackStack.push(bookingId);

        // Restore inventory
        inventory.put(roomType, inventory.get(roomType) + 1);

        // Remove booking
        bookings.remove(bookingId);

        System.out.println("Booking cancelled successfully. ID: " + bookingId);
    }

    // Display Inventory
    public void showInventory() {
        System.out.println("\nCurrent Inventory:");
        for (String type : inventory.keySet()) {
            System.out.println(type + " -> " + inventory.get(type));
        }
    }

    // Display Rollback Stack
    public void showRollbackStack() {
        System.out.println("\nRollback Stack (Recent cancellations): " + rollbackStack);
    }
}

// Main Class
public class UseCase10BookingCancellation {

    public static void main(String[] args) {

        BookingService service = new BookingService();

        try {
            // Booking
            int id1 = service.bookRoom("Double");
            int id2 = service.bookRoom("Single");

            service.showInventory();

            // Cancellation
            service.cancelBooking(id1);

            service.showInventory();
            service.showRollbackStack();

            // Invalid cancellation (test case)
            service.cancelBooking(999);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        System.out.println("\nSystem running safely...");
    }
}