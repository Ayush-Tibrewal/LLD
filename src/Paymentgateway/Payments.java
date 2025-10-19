// ------------------ Observer Pattern ------------------
interface PaymentObserver {
    void update(String status, double amount);
}

class EmailNotifier implements PaymentObserver {
    public void update(String status, double amount) {
        System.out.println("Email: Payment " + status + " for amount $" + amount);
    }
}

class SmsNotifier implements PaymentObserver {
    public void update(String status, double amount) {
        System.out.println("SMS: Payment " + status + " for amount $" + amount);
    }
}

// ------------------ Strategy Pattern ------------------
interface PaymentStrategy {
    boolean pay(double amount);
}

class CreditCardPayment implements PaymentStrategy {
    public boolean pay(double amount) {
        System.out.println("Processing Credit Card Payment: $" + amount);
        return true;
    }
}

class UpiPayment implements PaymentStrategy {
    public boolean pay(double amount) {
        System.out.println("Processing UPI Payment: $" + amount);
        return true;
    }
}

class WalletPayment implements PaymentStrategy {
    public boolean pay(double amount) {
        System.out.println("Processing Wallet Payment: $" + amount);
        return true;
    }
}

// ------------------ Factory Pattern ------------------
class PaymentFactory {
    public static PaymentStrategy getPaymentMethod(String type) {
        if (type.equalsIgnoreCase("CARD")) return new CreditCardPayment();
        else if (type.equalsIgnoreCase("UPI")) return new UpiPayment();
        else if (type.equalsIgnoreCase("WALLET")) return new WalletPayment();
        throw new IllegalArgumentException("Invalid Payment Type: " + type);
    }
}

// ------------------ Singleton Pattern ------------------
class PaymentGateway {
    private static PaymentGateway instance;
    private List<PaymentObserver> observers = new ArrayList<>();

    private PaymentGateway() {}

    public static PaymentGateway getInstance() {
        if (instance == null) {
            instance = new PaymentGateway();
        }
        return instance;
    }

    public void addObserver(PaymentObserver observer) {
        observers.add(observer);
    }

    private void notifyObservers(String status, double amount) {
        for (PaymentObserver observer : observers) {
            observer.update(status, amount);
        }
    }

    public void processPayment(PaymentStrategy paymentMethod, double amount) {
        boolean success = paymentMethod.pay(amount);
        if (success) {
            System.out.println("Payment Successful!");
            notifyObservers("SUCCESS", amount);
        } else {
            System.out.println("Payment Failed!");
            notifyObservers("FAILED", amount);
        }
    }
}

// ------------------ User ------------------
class User {
    String name;
    User(String name) { this.name = name; }

    public void makePayment(String paymentType, double amount) {
        PaymentStrategy method = PaymentFactory.getPaymentMethod(paymentType);
        PaymentGateway gateway = PaymentGateway.getInstance();
        gateway.processPayment(method, amount);
    }
}

// ------------------ Demo ------------------
public class Main {
    public static void main(String[] args) {
        PaymentGateway gateway = PaymentGateway.getInstance();
        gateway.addObserver(new EmailNotifier());
        gateway.addObserver(new SmsNotifier());

        User user1 = new User("Ayush");
        user1.makePayment("CARD", 500);
        user1.makePayment("UPI", 200);
        user1.makePayment("WALLET", 100);
    }
}
