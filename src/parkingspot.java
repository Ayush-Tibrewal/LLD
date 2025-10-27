import java.util.*;

// ===== Enums =====
enum VehicleType { 
    TwoWheeler, 
    FourWheeler 
}

// ===== Vehicle Class =====
class Vehicle {
    int vehicleNo;
    VehicleType vehicleType;

    Vehicle(int vehicleNo, VehicleType vehicleType) {
        this.vehicleNo = vehicleNo;
        this.vehicleType = vehicleType;
    }
}

// ===== Base ParkingSpot =====
class ParkingSpot {
    int id;
    boolean isEmpty;
    Vehicle vehicle;
    int price;

    ParkingSpot(int id, int price) {
        this.id = id;
        this.isEmpty = true;
        this.vehicle = null;
        this.price = price;
    }

    void parkVehicle(Vehicle v) {
        this.vehicle = v;
        this.isEmpty = false;
    }

    void removeVehicle() {
        this.vehicle = null;
        this.isEmpty = true;
    }
}

// ===== Extended Spot Types =====
class TwoWheelerSpot extends ParkingSpot {
    TwoWheelerSpot(int id, int price) {
        super(id, price);
    }
}

class FourWheelerSpot extends ParkingSpot {
    FourWheelerSpot(int id, int price) {
        super(id, price);
    }
}

// ===== Abstract Manager =====
abstract class ParkingSpotManager {
    List<ParkingSpot> spots;

    ParkingSpotManager(List<ParkingSpot> spots) {
        this.spots = spots;
    }

    abstract ParkingSpot findParkingSpace();

    void parkVehicle(Vehicle v) {
        ParkingSpot spot = findParkingSpace();
        if (spot != null) {
            spot.parkVehicle(v);
            System.out.println("Vehicle " + v.vehicleNo + " parked at spot " + spot.id);
        } else {
            System.out.println("No empty spot available!");
        }
    }

    void removeVehicle(Vehicle v) {
        for (ParkingSpot spot : spots) {
            if (spot.vehicle != null && spot.vehicle.equals(v)) {
                spot.removeVehicle();
                System.out.println("Vehicle " + v.vehicleNo + " removed from spot " + spot.id);
                break;
            }
        }
    }
}

// ===== Managers =====
class TwoWheelerManager extends ParkingSpotManager {
    TwoWheelerManager(List<ParkingSpot> spots) {
        super(spots);
    }

    @Override
    ParkingSpot findParkingSpace() {
        for (ParkingSpot spot : spots) {
            if (spot instanceof TwoWheelerSpot && spot.isEmpty) return spot;
        }
        return null;
    }
}

class FourWheelerManager extends ParkingSpotManager {
    FourWheelerManager(List<ParkingSpot> spots) {
        super(spots);
    }

    @Override
    ParkingSpot findParkingSpace() {
        for (ParkingSpot spot : spots) {
            if (spot instanceof FourWheelerSpot && spot.isEmpty) return spot;
        }
        return null;
    }
}

// ===== Factory =====
class ParkingSpotManagerFactory {
    ParkingSpotManager getParkingSpotManager(VehicleType type, List<ParkingSpot> spots) {
        if (type == VehicleType.TwoWheeler)
            return new TwoWheelerManager(spots);
        else if (type == VehicleType.FourWheeler)
            return new FourWheelerManager(spots);
        else
            return null;
    }
}

// ===== Ticket =====
class Ticket {
    long entryTime;
    ParkingSpot parkingSpot;
    Vehicle vehicle;

    Ticket(long entryTime, ParkingSpot parkingSpot, Vehicle vehicle) {
        this.entryTime = entryTime;
        this.parkingSpot = parkingSpot;
        this.vehicle = vehicle;
    }
}

// ===== EntranceGate =====
class EntranceGate {
    ParkingSpotManagerFactory factory;

    EntranceGate(ParkingSpotManagerFactory factory) {
        this.factory = factory;
    }

    Ticket parkVehicle(Vehicle vehicle, List<ParkingSpot> spots) {
        ParkingSpotManager manager = factory.getParkingSpotManager(vehicle.vehicleType, spots);
        ParkingSpot spot = manager.findParkingSpace();

        if (spot == null) {
            System.out.println("No parking space available!");
            return null;
        }

        spot.parkVehicle(vehicle);
        long entryTime = System.currentTimeMillis();
        System.out.println("Vehicle " + vehicle.vehicleNo + " parked at spot " + spot.id);
        return new Ticket(entryTime, spot, vehicle);
    }
}

// ===== ExitGate =====
class ExitGate {
    ParkingSpotManagerFactory factory;

    ExitGate(ParkingSpotManagerFactory factory) {
        this.factory = factory;
    }

    void exitVehicle(Ticket ticket, List<ParkingSpot> spots) {
        long exitTime = System.currentTimeMillis();
        long duration = (exitTime - ticket.entryTime) / 1000; // seconds

        ParkingSpotManager manager = factory.getParkingSpotManager(ticket.vehicle.vehicleType, spots);
        manager.removeVehicle(ticket.vehicle);

        int totalCost = (int) (duration * ticket.parkingSpot.price);
        System.out.println("Vehicle " + ticket.vehicle.vehicleNo + " exited.");
        System.out.println("Duration: " + duration + " seconds | Total Cost: ₹" + totalCost);
    }
}

// ===== Main =====
public class Main {
    public static void main(String[] args) {
        List<ParkingSpot> spots = new ArrayList<>();

        for (int i = 1; i <= 50; i++) spots.add(new TwoWheelerSpot(i, 10));
        for (int i = 51; i <= 100; i++) spots.add(new FourWheelerSpot(i, 20));

        ParkingSpotManagerFactory factory = new ParkingSpotManagerFactory();
        EntranceGate entrance = new EntranceGate(factory);
        ExitGate exit = new ExitGate(factory);

        Vehicle bike = new Vehicle(101, VehicleType.TwoWheeler);
        Ticket ticket = entrance.parkVehicle(bike, spots);

        try { Thread.sleep(3000); } catch (Exception e) {}

        exit.exitVehicle(ticket, spots);
    }
}
