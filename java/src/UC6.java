import java.util.*;

public class UC6 {

    // Reservation
    static class Reservation {
        private String guestName;
        private String roomType;

        public Reservation(String guestName, String roomType) {
            this.guestName = guestName;
            this.roomType = roomType;
        }

        public String getGuestName() {
            return guestName;
        }

        public String getRoomType() {
            return roomType;
        }
    }

    // Queue (UC5)
    static class BookingRequestQueue {
        private Queue<Reservation> queue = new LinkedList<>();

        public void addRequest(Reservation r) {
            queue.offer(r);
        }

        public Reservation getNextRequest() {
            return queue.poll();
        }

        public boolean hasPendingRequests() {
            return !queue.isEmpty();
        }
    }

    // Inventory (UC3)
    static class RoomInventory {
        private Map<String, Integer> availability = new HashMap<>();

        public RoomInventory() {
            availability.put("Single", 2);
            availability.put("Double", 2);
            availability.put("Suite", 1);
        }

        public Map<String, Integer> getAvailability() {
            return availability;
        }
    }

    // UC6 MAIN LOGIC
    static class RoomAllocationService {

        private Set<String> allocatedRoomIds = new HashSet<>();
        private Map<String, Integer> roomCounters = new HashMap<>();

        public RoomAllocationService() {
            roomCounters.put("Single", 0);
            roomCounters.put("Double", 0);
            roomCounters.put("Suite", 0);
        }

        public void allocateRoom(Reservation r, RoomInventory inventory) {

            String type = r.getRoomType();
            Map<String, Integer> avail = inventory.getAvailability();

            if (avail.get(type) > 0) {

                String roomId = generateRoomId(type);

                allocatedRoomIds.add(roomId);

                // decrease availability
                avail.put(type, avail.get(type) - 1);

                System.out.println("Booking confirmed for Guest: "
                        + r.getGuestName()
                        + ", Room ID: "
                        + roomId);

            } else {
                System.out.println("No rooms available for " + type);
            }
        }

        private String generateRoomId(String type) {
            int count = roomCounters.get(type) + 1;
            roomCounters.put(type, count);
            return type + "-" + count;
        }
    }

    // MAIN
    public static void main(String[] args) {

        System.out.println("Room Allocation Processing");

        BookingRequestQueue queue = new BookingRequestQueue();
        RoomInventory inventory = new RoomInventory();
        RoomAllocationService service = new RoomAllocationService();

        // Requests
        queue.addRequest(new Reservation("Abhi", "Single"));
        queue.addRequest(new Reservation("Subha", "Single"));
        queue.addRequest(new Reservation("Vanmathi", "Suite"));

        // Process FIFO
        while (queue.hasPendingRequests()) {
            Reservation r = queue.getNextRequest();
            service.allocateRoom(r, inventory);
        }
    }
}