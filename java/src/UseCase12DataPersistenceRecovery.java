import java.io.*;
import java.util.*;

// Serializable Booking System State
class SystemState implements Serializable {
    private static final long serialVersionUID = 1L;

    Map<String, Integer> inventory;
    Map<Integer, String> bookings;

    public SystemState(Map<String, Integer> inventory, Map<Integer, String> bookings) {
        this.inventory = inventory;
        this.bookings = bookings;
    }
}

// Persistence Service
class PersistenceService {

    private static final String FILE_NAME = "system_state.ser";

    // Save state to file
    public static void save(SystemState state) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(state);
            System.out.println("✅ System state saved successfully.");
        } catch (IOException e) {
            System.out.println("❌ Error saving system state.");
        }
    }

    // Load state from file
    public static SystemState load() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            System.out.println("✅ System state loaded successfully.");
            return (SystemState) ois.readObject();
        } catch (Exception e) {
            System.out.println("⚠ No previous data found. Starting fresh.");
            return null;
        }
    }
}

// Booking System
class BookingSystem {

    Map<String, Integer> inventory = new HashMap<>();
    Map<Integer, String> bookings = new HashMap<>();
    int bookingCounter = 1;

    public BookingSystem() {
        // Try loading previous state
        SystemState state = PersistenceService.load();

        if (state != null) {
            this.inventory = state.inventory;
            this.bookings = state.bookings;
        } else {
            // Initialize fresh inventory
            inventory.put("Single", 5);
            inventory.put("Double", 3);
            inventory.put("Suite", 2);
        }
    }

    // Book Room
    public void bookRoom(String roomType) {
        if (!inventory.containsKey(roomType)) {
            System.out.println("Invalid room type.");
            return;
        }

        if (inventory.get(roomType) <= 0) {
            System.out.println("No rooms available.");
            return;
        }

        bookings.put(bookingCounter, roomType);
        inventory.put(roomType, inventory.get(roomType) - 1);

        System.out.println("Booking successful! ID: " + bookingCounter);
        bookingCounter++;
    }

    // Show inventory
    public void showInventory() {
        System.out.println("\nInventory:");
        for (String type : inventory.keySet()) {
            System.out.println(type + " -> " + inventory.get(type));
        }
    }

    // Save state before shutdown
    public void shutdown() {
        SystemState state = new SystemState(inventory, bookings);
        PersistenceService.save(state);
    }
}

// Main Class
public class UseCase12DataPersistenceRecovery {

    public static void main(String[] args) {

        BookingSystem system = new BookingSystem();

        system.showInventory();

        // Perform some bookings
        system.bookRoom("Single");
        system.bookRoom("Double");

        system.showInventory();

        // Simulate shutdown (save state)
        system.shutdown();

        System.out.println("\n--- Restart the program to see recovery ---");
    }
}