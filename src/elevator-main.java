import java.util.*;

// ---------------- ENUMS ----------------
enum Direction {
    UP,
    DOWN
}

enum Status {
    IDLE,
    WORKING
}

// ---------------- DISPLAY ----------------
class Display {
    Direction direction;
    int floor;

    public void show() {
        System.out.println("Display: Floor " + floor + " | Direction: " + direction);
    }
}

// ---------------- INTERNAL BUTTONS ----------------
class InternalButtons {
    InternalButtonDispatcher dispatcher;

    public InternalButtons(InternalButtonDispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }

    public void pressButton(int destination, Elevator elevator) {
        System.out.println("Internal button pressed for floor " + destination + " in Elevator " + elevator.id);
        dispatcher.submitRequest(destination, elevator);
    }
}

// ---------------- INTERNAL BUTTON DISPATCHER ----------------
class InternalButtonDispatcher {
    public void submitRequest(int destination, Elevator elevator) {
        elevator.controller.addInternalRequest(destination);
    }
}

// ---------------- ELEVATOR CLASS ----------------
class Elevator {
    int id;
    Display display;
    int currentFloor;
    Status status;
    InternalButtons buttons;
    ElevatorController controller;

    public Elevator(int id) {
        this.id = id;
        this.currentFloor = 0;
        this.status = Status.IDLE;
        this.display = new Display();
        this.display.direction = Direction.UP;
        this.display.floor = 0;
        InternalButtonDispatcher dispatcher = new InternalButtonDispatcher();
        this.buttons = new InternalButtons(dispatcher);
        this.controller = new ElevatorController(this);
    }

    public void move(int destination, Direction direction) {
        System.out.println("Elevator " + id + " moving " + direction + " from floor " + currentFloor + " to " + destination);
        status = Status.WORKING;
        display.direction = direction;
        display.floor = destination;
        currentFloor = destination;
        System.out.println("Elevator " + id + " reached floor " + destination);
        status = Status.IDLE;
    }
}

// ---------------- ELEVATOR CONTROLLER ----------------
class ElevatorController {
    Elevator elevator;

    // Requests
    PriorityQueue<Integer> minHeap = new PriorityQueue<>(); // upward requests
    PriorityQueue<Integer> maxHeap = new PriorityQueue<>(Collections.reverseOrder()); // downward requests
    Direction direction = Direction.UP;

    public ElevatorController(Elevator elevator) {
        this.elevator = elevator;
    }

    // Add internal request (from inside the elevator) // jaha tak jaana hai 
    public void addInternalRequest(int floor) {
        if (floor > elevator.currentFloor) minHeap.add(floor);
        else if (floor < elevator.currentFloor) maxHeap.add(floor);
        processRequests();
    }

    // Add external request (from building) // jaha se jaana hai 
    public void addExternalRequest(int floor, Direction dir) {
        if (dir == Direction.UP) minHeap.add(floor);
        else maxHeap.add(floor);
        processRequests();
    }

    // Process requests
    public void processRequests() {
        while (!minHeap.isEmpty() || !maxHeap.isEmpty()) {
            if (direction == Direction.UP) {
                while (!minHeap.isEmpty()) {
                    int nextFloor = minHeap.poll();
                    elevator.move(nextFloor, Direction.UP);
                }
                direction = Direction.DOWN;
            } else {
                while (!maxHeap.isEmpty()) {
                    int nextFloor = maxHeap.poll();
                    elevator.move(nextFloor, Direction.DOWN);
                }
                direction = Direction.UP;
            }
        }
    }
}

// ---------------- FLOOR ----------------
class Floor {
    int floorNumber;
    ExternalButton externalButton;

    public Floor(int floorNumber, ExternalButtonDispatcher dispatcher) {
        this.floorNumber = floorNumber;
        this.externalButton = new ExternalButton(floorNumber, dispatcher);
    }
}

// ---------------- EXTERNAL BUTTON ----------------
class ExternalButton {
    int floor;
    ExternalButtonDispatcher dispatcher;

    public ExternalButton(int floor, ExternalButtonDispatcher dispatcher) {
        this.floor = floor;
        this.dispatcher = dispatcher;
    }

    public void pressButton(Direction direction) {
        System.out.println("External button pressed at floor " + floor + " for direction " + direction);
        dispatcher.submitExternalRequest(floor, direction);
    }
}

// ---------------- EXTERNAL BUTTON DISPATCHER ----------------
class ExternalButtonDispatcher {
    List<Elevator> elevators;

    public ExternalButtonDispatcher(List<Elevator> elevators) {
        this.elevators = elevators;
    }

    // Simple nearest-elevator dispatch logic
    public void submitExternalRequest(int floor, Direction direction) {
        Elevator chosen = null;
        int minDistance = Integer.MAX_VALUE;

        for (Elevator e : elevators) {
            int distance = Math.abs(e.currentFloor - floor);
            if (distance < minDistance && e.status == Status.IDLE) {
                minDistance = distance;
                chosen = e;
            }
        }

        if (chosen == null) {
            chosen = elevators.get(0); // fallback
        }

        System.out.println("Dispatching Elevator " + chosen.id + " to floor " + floor);
        chosen.controller.addExternalRequest(floor, direction);
    }
}

// ---------------- BUILDING ----------------
class Building {
    List<Floor> floors;
    List<Elevator> elevators;
    ExternalButtonDispatcher dispatcher;

    public Building(int numFloors, int numElevators) {
        elevators = new ArrayList<>();
        for (int i = 0; i < numElevators; i++) {
            elevators.add(new Elevator(i + 1));
        }

        dispatcher = new ExternalButtonDispatcher(elevators);
        floors = new ArrayList<>();

        for (int i = 0; i < numFloors; i++) {
            floors.add(new Floor(i, dispatcher));
        }
    }

    public void pressExternalButton(int floor, Direction direction) {
        floors.get(floor).externalButton.pressButton(direction);
    }

    public Elevator getElevator(int id) {
        return elevators.get(id - 1);
    }
}

// ---------------- MAIN ----------------
public class Main {
    public static void main(String[] args) {
        Building building = new Building(10, 2); // 10 floors, 2 elevators

        // Simulate external button presses
        building.pressExternalButton(3, Direction.UP);
        building.pressExternalButton(7, Direction.DOWN);

        // Simulate internal button press inside elevator 1
        building.getElevator(1).buttons.pressButton(9, building.getElevator(1));
        building.getElevator(1).buttons.pressButton(1, building.getElevator(1));
    }
}


------------------------------------------------
