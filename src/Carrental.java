enum VehicleType {
    CAR, BIKE, TRUCK, BUS
}
 
enum Status {
    Active,
    Inactive
}
 
class Vehicle {
    int id;
    String vehicleNo;
    VehicleType vtype;
    Status status;
 
    Vehicle(int id, String vehicleNo, VehicleType vtype, Status status) {
        this.id = id;
        this.vehicleNo = vehicleNo;
        this.vtype = vtype;
        this.status = status;
    }
}
 
class Car extends Vehicle {
    int seatingCapacity;
    String model;
 
    Car(int id, String vehicleNo, String model, int seatingCapacity) {
        super(id, vehicleNo, VehicleType.CAR, Status.Active);
        this.model = model;
        this.seatingCapacity = seatingCapacity;
    }
}
 
class Location {
    String city;
    String state;
    String pincode;
 
    Location(String city, String state, String pincode) {
        this.city = city;
        this.state = state;
        this.pincode = pincode;
    }
}
 
class VehicleInventoryManagement {
    List<Vehicle> list;
 
    VehicleInventoryManagement() {
        this.list = new ArrayList<>();
    }
 
    void addVehicle(Vehicle v) {
        list.add(v);
    }
}
 
class User {
    int id;
    String name;
 
    User(int id, String name) {
        this.id = id;
        this.name = name;
    }
}
 
class Reservation {
    int id;
    User user;
    Vehicle vehicle;
    String bookFrom;
    String bookTill;
 
    Reservation(int id, User user, Vehicle vehicle, String bookFrom, String bookTill) {
        this.id = id;
        this.user = user;
        this.vehicle = vehicle;
        this.bookFrom = bookFrom;
        this.bookTill = bookTill;
    }
}
 
class Store {
    int storeId;
    Location location;
    VehicleInventoryManagement management;
    List<Reservation> reservations;
 
    Store(int storeId, Location location) {
        this.storeId = storeId;
        this.location = location;
        this.management = new VehicleInventoryManagement();
        this.reservations = new ArrayList<>();
    }
    
    List<Vechile> getmevechile(VehicleType vech){
        for(List<vechile>)
    }
    
}
 
 
class VechileRebtalSystem {
    List<User> users;
    List<Store> stores;
    VechileRebtalSystem(List<User> users, List<Store> stores) {
        this.users = new ArrayList<>();
        this.stores = new ArrayList<>();
    }
    void addUser(User user) {
        users.add(user);
    }
    void addStore(Store store) {
        stores.add(store);
    }
    
    List<Store> getmestores(Location location){
        return stores;
    }
}
 
class Bill {
    Reservation reservation;
    double totalBillPayment;
    boolean isBillPaid;
 
    Bill(Reservation reservation) {
        this.reservation = reservation;
        this.totalBillPayment = 0;
        this.isBillPaid = false;
    }
 
    double computeBill() {
        totalBillPayment = 100; 
        return totalBillPayment;
    }
}
 
 
class Payment {
    Bill bill;
    Payment(Bill bill) {
        this.bill = bill;
    }
    void makePayment() {
        double amount = bill.computeBill();
        System.out.println("Processing payment of " + amount);
        bill.payBill();
    }
}
 
 
public class Main {
    public static void main(String[] args) {
        Car c1 = new Car(1, "MH12AB1234", "Honda City", 5);
        Car c2 = new Car(2, "MH12XY5678", "Hyundai i20", 5);
        User u1 = new User(1, "Ayush");
        Location loc = new Location("Pune", "Maharashtra", "411001");
 
        Store store1 = new Store(101, loc);
        store1.management.addVehicle(c1);
 
        Store store2 = new Store(102, loc);
        store2.management.addVehicle(c2);
 
        VehicleRentalSystem rental = new VehicleRentalSystem(new ArrayList<>(), new ArrayList<>());
        rental.addUser(u1);
        rental.addStore(store1);
        rental.addStore(store2);
 
        List<Store> storesAtLocation = rental.getStoresByLocation(loc);
        System.out.println("Stores at location: " + storesAtLocation.size());
 
        for (Store s : storesAtLocation) {
            List<Vehicle> availableCars = s.management.getVehiclesByType(VehicleType.CAR);
            System.out.println("Store ID: " + s.storeId + ", Available cars: " + availableCars.size());
            for (Vehicle v : availableCars) {
                System.out.println(" - " + ((Car)v).model + " (" + v.vehicleNo + ")");
            }
        }
 
        if (!storesAtLocation.isEmpty() && !storesAtLocation.get(0).management.vehicles.isEmpty()) {
            Vehicle bookedCar = storesAtLocation.get(0).management.vehicles.get(0);
            Reservation r1 = new Reservation(1, u1, bookedCar, "2025-10-20", "2025-10-25");
            storesAtLocation.get(0).reservations.add(r1);
 
            Bill bill = new Bill(r1);
            Payment payment = new Payment(bill);
            payment.makePayment();
        }
    }
}
