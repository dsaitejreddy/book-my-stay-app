import java.util.*;

// Booking Request Class
class BookingRequest {
    String roomType;
    int guestId;

    public BookingRequest(int guestId, String roomType) {
        this.guestId = guestId;
        this.roomType = roomType;
    }
}

// Shared Booking System (Thread-Safe)
class ConcurrentBookingSystem {

    private Map<String, Integer> inventory = new HashMap<>();

    public ConcurrentBookingSystem() {
        inventory.put("Single", 2);
        inventory.put("Double", 2);
        inventory.put("Suite", 1);
    }

    // Synchronized booking method (critical section)
    public synchronized void bookRoom(BookingRequest request) {

        String roomType = request.roomType;

        System.out.println("Guest " + request.guestId + " trying to book " + roomType);

        if (!inventory.containsKey(roomType)) {
            System.out.println("Guest " + request.guestId + ": Invalid room type");
            return;
        }

        int available = inventory.get(roomType);

        if (available > 0) {
            // Simulate delay (to show race condition if not synchronized)
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            inventory.put(roomType, available - 1);

            System.out.println("✅ Guest " + request.guestId + " booked " + roomType +
                    " | Remaining: " + inventory.get(roomType));

        } else {
            System.out.println("❌ Guest " + request.guestId + ": No " + roomType + " rooms available");
        }
    }

    public void showInventory() {
        System.out.println("\nFinal Inventory:");
        for (String type : inventory.keySet()) {
            System.out.println(type + " -> " + inventory.get(type));
        }
    }
}

// Thread Class
class GuestThread extends Thread {

    private ConcurrentBookingSystem system;
    private BookingRequest request;

    public GuestThread(ConcurrentBookingSystem system, BookingRequest request) {
        this.system = system;
        this.request = request;
    }

    @Override
    public void run() {
        system.bookRoom(request);
    }
}

// Main Class
public class UseCase11ConcurrentBookingSimulation {

    public static void main(String[] args) {

        ConcurrentBookingSystem system = new ConcurrentBookingSystem();

        // Simulate multiple guests
        List<Thread> threads = new ArrayList<>();

        threads.add(new GuestThread(system, new BookingRequest(1, "Single")));
        threads.add(new GuestThread(system, new BookingRequest(2, "Single")));
        threads.add(new GuestThread(system, new BookingRequest(3, "Single"))); // extra request

        threads.add(new GuestThread(system, new BookingRequest(4, "Double")));
        threads.add(new GuestThread(system, new BookingRequest(5, "Double")));

        threads.add(new GuestThread(system, new BookingRequest(6, "Suite")));
        threads.add(new GuestThread(system, new BookingRequest(7, "Suite"))); // extra

        // Start all threads
        for (Thread t : threads) {
            t.start();
        }

        // Wait for all threads to finish
        for (Thread t : threads) {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // Final inventory
        system.showInventory();

        System.out.println("\nSystem executed safely with thread synchronization.");
    }
}