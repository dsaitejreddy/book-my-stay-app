import java.util.*;

public class UC7 {

    // Add-On Service
    static class AddOnService {
        private String serviceName;
        private double cost;

        public AddOnService(String serviceName, double cost) {
            this.serviceName = serviceName;
            this.cost = cost;
        }

        public String getServiceName() {
            return serviceName;
        }

        public double getCost() {
            return cost;
        }
    }

    // Manager
    static class AddOnServiceManager {

        private Map<String, List<AddOnService>> servicesByReservation;

        public AddOnServiceManager() {
            servicesByReservation = new HashMap<>();
        }

        public void addService(String reservationId, AddOnService service) {

            servicesByReservation
                    .computeIfAbsent(reservationId, k -> new ArrayList<>())
                    .add(service);
        }

        public double calculateTotalServiceCost(String reservationId) {

            double total = 0;

            List<AddOnService> services = servicesByReservation.get(reservationId);

            if (services != null) {
                for (AddOnService s : services) {
                    total += s.getCost();
                }
            }

            return total;
        }
    }

    // MAIN
    public static void main(String[] args) {

        System.out.println("Add-On Service Selection");

        AddOnServiceManager manager = new AddOnServiceManager();

        String reservationId = "Single-1";

        // Add services
        manager.addService(reservationId, new AddOnService("Breakfast", 500));
        manager.addService(reservationId, new AddOnService("Spa", 1000));

        double total = manager.calculateTotalServiceCost(reservationId);

        System.out.println("Reservation ID: " + reservationId);
        System.out.println("Total Add-On Cost: " + total);
    }
}