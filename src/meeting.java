
import java.util.*;

// ==================== Location ====================
class Location {
    int building;
    int room;
    int floor;

    Location(int building, int room, int floor) {
        this.building = building;
        this.room = room;
        this.floor = floor;
    }

    @Override
    public String toString() {
        return "Building " + building + ", Floor " + floor + ", Room " + room;
    }
}

// ==================== MeetingRoom ====================
class MeetingRoom {
    int id;
    int capacity;
    Location location;

    MeetingRoom(int id, int capacity, Location location) {
        this.id = id;
        this.capacity = capacity;
        this.location = location;
    }

    @Override
    public String toString() {
        return "Room ID: " + id + ", Capacity: " + capacity + ", Location: [" + location + "]";
    }
}

// ==================== Day Enum ====================
enum Day {
    Monday, Tuesday, Wednesday, Thursday, Friday, Saturday, Sunday
}

// ==================== TimeInterval ====================
class TimeInterval {
    int start; // start time in 24-hour format (e.g., 1300 for 1 PM)
    int end;   // end time in 24-hour format
    Day day;

    TimeInterval(int start, int end, Day day) {
        this.start = start;
        this.end = end;
        this.day = day;
    }

    // Check if two intervals overlap
    boolean overlaps(TimeInterval other) {
        return this.day == other.day && !(this.end <= other.start || this.start >= other.end);
    }

    @Override
    public String toString() {
        return day + " [" + start + "-" + end + "]";
    }
}

// ==================== Booking ====================
class Booking {
    MeetingRoom room;
    TimeInterval interval;

    Booking(MeetingRoom room, TimeInterval interval) {
        this.room = room;
        this.interval = interval;
    }

    @Override
    public String toString() {
        return "Booking: Room " + room.id + " at " + interval;
    }
}

// ==================== Calendar ====================
class Calendar {
    List<Booking> bookings;

    Calendar() {
        bookings = new ArrayList<>();
    }

    void add(Booking book) {
        bookings.add(book);
        System.out.println("Booking added: " + book);
    }

    List<Booking> getAllBookings() {
        return bookings;
    }
}

// ==================== MeetingManager ====================
class MeetingManager {
    Calendar calendar;
    List<MeetingRoom> meetingRooms;

    MeetingManager() {
        calendar = new Calendar();
        meetingRooms = new ArrayList<>();
    }

    void addRoom(MeetingRoom room) {
        meetingRooms.add(room);
        System.out.println("Added room: " + room);
    }

    List<MeetingRoom> getAllRooms() {
        return meetingRooms;
    }

    void addMeeting(MeetingRoom room, TimeInterval interval) {
        for (Booking book : calendar.getAllBookings()) {
            if (book.room.id == room.id && book.interval.overlaps(interval)) {
                System.out.println("Room " + room.id + " is already booked for " + interval);
                return;
            }
        }
        calendar.add(new Booking(room, interval));
        System.out.println("Booking done for room " + room.id + " at " + interval);
    }
}

// ==================== Main ====================
public class Main {
    public static void main(String[] args) {
        Location location = new Location(2, 14, 1);
        MeetingManager meetingManager = new MeetingManager();

        for (int i = 0; i < 10; i++) {
            meetingManager.addRoom(new MeetingRoom(i, 10, location));
        }

        System.out.println("\nAll Rooms:");
        for (MeetingRoom room : meetingManager.getAllRooms()) {
            System.out.println(room);
        }

        // Example booking
        TimeInterval interval1 = new TimeInterval(1000, 1130, Day.Monday);
        TimeInterval interval2 = new TimeInterval(1100, 1230, Day.Monday);

        meetingManager.addMeeting(meetingManager.getAllRooms().get(0), interval1);
        meetingManager.addMeeting(meetingManager.getAllRooms().get(0), interval2); // Overlapping
    }
}
