
// ================= Movie =================
class Movie {
    private String id;
    private String name;
    private int duration;

    public Movie(String id, String name, int duration) {
        this.id = id;
        this.name = name;
        this.duration = duration;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getDuration() { return duration; }
    public void setDuration(int duration) { this.duration = duration; }
}

class MovieController {
    private List<Movie> movies;

    public MovieController() {
        this.movies = new ArrayList<>();
    }

    public void addMovie(Movie movie) {
        movies.add(movie);
    }

    public List<Movie> getAllMovies() {
        return movies;
    }
}

// ================= Seat =================
enum Category {
    PREMIUM,
    LOWER_PREMIUM,
    AVG,
    GOOD
}

class Seat {
    private int id;
    private int row;
    private Category category;

    public Seat(int id, int row, Category category) {
        this.id = id;
        this.row = row;
        this.category = category;
    }

    public int getId() { return id; }
    public int getRow() { return row; }
    public Category getCategory() { return category; }
}

// ================= Screen =================
class Screen {
    private String id;
    private List<Seat> seats;

    public Screen(String id, List<Seat> seats) {
        this.id = id;
        this.seats = seats != null ? seats : new ArrayList<>();
    }

    public String getId() { return id; }
    public List<Seat> getSeats() { return seats; }
}

// ================= Show =================
class Show {
    private String id;
    private Movie movie;
    private Screen screen;
    private int showtime; 
    private List<Seat> bookedSeats;

    public Show(String id, Movie movie, Screen screen, int showtime) {
        this.id = id;
        this.movie = movie;
        this.screen = screen;
        this.showtime = showtime;
        this.bookedSeats = new ArrayList<>();
    }

    public String getId() { return id; }
    public Movie getMovie() { return movie; }
    public Screen getScreen() { return screen; }
    public int getShowtime() { return showtime; }
    public List<Seat> getBookedSeats() { return bookedSeats; }

    public void bookSeat(Seat seat) { bookedSeats.add(seat); }
}

// ================= Theater =================
class Theater {
    private String id;
    private String address;
    private City city;
    private List<Screen> screens;
    private List<Show> shows;

    public Theater(String id, String address, City city, List<Screen> screens, List<Show> shows) {
        this.id = id;
        this.address = address;
        this.city = city;
        this.screens = screens != null ? screens : new ArrayList<>();
        this.shows = shows != null ? shows : new ArrayList<>();
    }

    public String getId() { return id; }
    public String getAddress() { return address; }
    public City getCity() { return city; }
    public List<Screen> getScreens() { return screens; }
    public List<Show> getShows() { return shows; }
}

// ================= Theater Controller =================
class TheaterController {
    private Map<City, List<Theater>> cityTheaterMap;
    private List<Theater> theaters;

    public TheaterController() {
        this.cityTheaterMap = new HashMap<>();
        this.theaters = new ArrayList<>();
    }

    public void addTheater(Theater theater) {
        theaters.add(theater);
        cityTheaterMap.computeIfAbsent(theater.getCity(), k -> new ArrayList<>()).add(theater);
    }

    public List<Theater> getAllTheaters() { return theaters; }
}

// ================= Booking & Payment =================
class Payment {
    private int id;
    private boolean status;

    public Payment(int id, boolean status) {
        this.id = id;
        this.status = status;
    }

    public boolean isStatus() { return status; }
}

class Booking {
    private Show show;
    private List<Seat> seats;
    private Payment payment;

    public Booking(Show show, List<Seat> seats, Payment payment) {
        this.show = show;
        this.seats = seats != null ? seats : new ArrayList<>();
        this.payment = payment;
    }

    public List<Seat> getSeats() { return seats; }
    public void addSeat(Seat seat) { seats.add(seat); }
}

// ================= City =================
class City {
    private String name;

    public City(String name) { this.name = name; }
    public String getName() { return name; }
}

// ================= Main =================
public class Main {
    public static void main(String[] args) {
        // Cities
        City delhi = new City("Delhi");
        City mumbai = new City("Mumbai");

        // Movies
        Movie movie1 = new Movie("M1", "Inception", 150);
        Movie movie2 = new Movie("M2", "Interstellar", 170);
        MovieController movieController = new MovieController();
        movieController.addMovie(movie1);
        movieController.addMovie(movie2);

        // Seats
        List<Seat> seats = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            seats.add(new Seat(i, 1, Category.PREMIUM));
        }

        // Screens
        Screen screen1 = new Screen("S1", seats);
        List<Screen> screens = Arrays.asList(screen1);

        // Shows
        Show show1 = new Show("SH1", movie1, screen1, 10);
        Show show2 = new Show("SH2", movie2, screen1, 14);
        List<Show> shows = Arrays.asList(show1, show2);

        // Theater
        Theater theater1 = new Theater("T1", "Connaught Place", delhi, screens, shows);
        TheaterController theaterController = new TheaterController();
        theaterController.addTheater(theater1);

        // Booking
        List<Seat> bookedSeats = new ArrayList<>();
        bookedSeats.add(seats.get(0));
        Booking booking = new Booking(show1, bookedSeats, new Payment(1, true));

        // Output
        System.out.println("Movies in system:");
        for (Movie m : movieController.getAllMovies()) {
            System.out.println(m.getName() + " (" + m.getDuration() + " mins)");
        }

        System.out.println("\nTheaters in Delhi:");
        for (Theater t : theaterController.getAllTheaters()) {
            if (t.getCity().equals(delhi)) {
                System.out.println(t.getAddress());
            }
        }

        System.out.println("\nBooked Seats for show " + show1.getMovie().getName() + ":");
        for (Seat s : booking.getSeats()) {
            System.out.println("Seat ID: " + s.getId() + ", Row: " + s.getRow());
        }
    }
}
