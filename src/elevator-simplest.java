import java.util.*;

enum Direction {
    UP, DOWN, IDLE
}


 

class Elevator {
    private PriorityQueue<Integer> minHeap; // for upward requests
    private PriorityQueue<Integer> maxHeap; // for downward requests
    private int currentFloor;
    private Direction direction;

    public Elevator(int startFloor) {
        currentFloor = startFloor;
        direction = Direction.IDLE;

        // minHeap for upward requests (smallest floor first)
        minHeap = new PriorityQueue<>();

        // maxHeap for downward requests (largest floor first)
        maxHeap = new PriorityQueue<>(Collections.reverseOrder());
    }

    // Add new request
    public void addRequest(int floor) {
        if (floor > currentFloor) {
            minHeap.offer(floor);
        } else if (floor < currentFloor) {
            maxHeap.offer(floor);
        }
        // If floor == currentFloor, already there, ignore
    }

    // Move elevator based on direction
    public void processRequests() {
        if (direction == Direction.IDLE) {
            // Decide initial direction
            if (!minHeap.isEmpty()) direction = Direction.UP;
            else if (!maxHeap.isEmpty()) direction = Direction.DOWN;
            else return; // No requests
        }

        while (!minHeap.isEmpty() || !maxHeap.isEmpty()) {
            if (direction == Direction.UP) {
                while (!minHeap.isEmpty()) {
                    int nextFloor = minHeap.poll();
                    moveTo(nextFloor);
                    currentFloor = nextFloor;
                }
                // When up requests done, reverse direction if down pending
                if (!maxHeap.isEmpty()) direction = Direction.DOWN;
                else direction = Direction.IDLE;
            }

            else if (direction == Direction.DOWN) {
                while (!maxHeap.isEmpty()) {
                    int nextFloor = maxHeap.poll();
                    moveTo(nextFloor);
                    currentFloor = nextFloor;
                }
                // When down requests done, reverse if up pending
                if (!minHeap.isEmpty()) direction = Direction.UP;
                else direction = Direction.IDLE;
            }
        }
        System.out.println("✅ All requests processed. Elevator idle at floor " + currentFloor);
    }

    private void moveTo(int floor) {
        System.out.println("Moving from floor " + currentFloor + " → " + floor);
        // Simulate time or door open/close if needed
    }
}

public class Main {
    public static void main(String[] args) {
        Elevator elevator = new Elevator(4); // starting at floor 4

        elevator.addRequest(6);
        elevator.addRequest(8);
        elevator.addRequest(3);
        elevator.addRequest(2);
        elevator.addRequest(10);

        elevator.processRequests();

        System.out.println("\n🚪 New requests arrived while elevator idle...");
        elevator.addRequest(1);
        elevator.addRequest(9);

        elevator.processRequests();
    }
}
