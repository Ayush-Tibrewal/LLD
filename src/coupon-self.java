import java.util.ArrayList;
import java.util.List;

class Pair {
    int amount;
    Product product;

    public Pair(int amount, Product product) {
        this.amount = amount;
        this.product = product;
    }
}

enum Status {
    NOTAVAILABLE, 
    AVAILABLE
}

class User {
    int id; 
    String name;
    Cart cart;

    public User(int id, String name) {
        this.id = id;
        this.name = name;
        this.cart = new Cart();
    }

    public void addToCart(Pair pair) {
        cart.addProduct(pair);
    }

    public void checkout(PaymentStrategy payment) {
        double total = cart.calculate_amount();
        System.out.println("Total amount: " + total);

        Bill bill = new Bill(payment, (int) total);
        bill.pay();

        cart.amount_product.clear(); // clear cart
        cart.amount = 0;
    }
}

class Product {
    int id; 
    String name; 
    Status status;
    int limit;
    int amount;

    public Product(int id, String name, Status status, int limit, int amount) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.limit = limit;
        this.amount = amount;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public Status getStatus() { return status; }
    public int getLimit() { return limit; }
    public int getAmount() { return amount; }
}

class ProductManagement {
    List<Product> prod_list = new ArrayList<>();
    
    void add(Product prod){
        prod_list.add(prod);
    }

    public Product findProductByName(String name) {
        for (Product p : prod_list) {
            if (p.name.equals(name)) {
                return p;
            }
        }
        return null;
    }

    public Product findProductById(int id) {
        for (Product p : prod_list) {
            if (p.id == id) {
                return p;
            }
        }
        return null;
    }

    public boolean isProductAvailable(int id) {
        Product p = findProductById(id);
        return p != null && p.status == Status.AVAILABLE && p.amount > 0;
    }
}

class Cart {
    List<Pair> amount_product; 
    int amount;

    Cart() {
       this.amount_product = new ArrayList<>();
       this.amount = 0;
    }

    int calculate_amount() {
        int total_amount = 0; 
        for(Pair p : amount_product){
            total_amount += p.amount;
        }
        return total_amount; 
    }

    void addProduct(Pair p) {
        amount_product.add(p);
        amount = calculate_amount();
    }
}

interface CouponStrategy {
    int calDiscount(int amount);
}

class TenPercentageOff implements CouponStrategy {
    public int calDiscount(int amount) {
        return amount - (amount * 10) / 100;
    }
}

class FiftyRupeesOff implements CouponStrategy {
    public int calDiscount(int amount) {
        return amount - 50;
    }
}

// --------- Bill and Payment Integration ---------
class Bill {
    PaymentStrategy payment;  
    int amount;
    boolean isPaid = false;

    Bill(PaymentStrategy payment, int amount) {
        this.payment = payment;
        this.amount = amount;
    }

    public void pay() {
        if (!isPaid) {
            payment.pay(amount);
            isPaid = true;
            System.out.println("Payment successful for amount: " + amount);
        } else {
            System.out.println("Bill is already paid.");
        }
    }
}

interface PaymentStrategy {
    void pay(int amount);
}

class CreditCardPayment implements PaymentStrategy {
    public void pay(int amount) {
        System.out.println("Paid " + amount + " using Credit Card.");
    }
}

class PaytmPayment implements PaymentStrategy {
    public void pay(int amount) {
        System.out.println("Paid " + amount + " using Paytm.");
    }
}

// -------- Main Function --------
class Main {
    public static void main(String[] args) {
        ProductManagement prodManagement = new ProductManagement();

        Product p1 = new Product(1, "Laptop", Status.NOTAVAILABLE, 10, 200);
        Product p2 = new Product(2, "Mouse", Status.AVAILABLE, 50, 20);
        Product p3 = new Product(3, "Keyboard", Status.AVAILABLE, 30, 0);

        prodManagement.add(p1);
        prodManagement.add(p2);
        prodManagement.add(p3);

        User u1 = new User(1, "Ayush");

        if (prodManagement.isProductAvailable(p2.getId())) {
            u1.addToCart(new Pair(2, p2));
        }

        if (prodManagement.isProductAvailable(p3.getId())) {
            u1.addToCart(new Pair(1, p3));
        }

        System.out.println("Cart total before coupon: " + u1.cart.calculate_amount());

        CouponStrategy coupon = new TenPercentageOff();
        int discountedAmount = coupon.calDiscount(u1.cart.calculate_amount());
        System.out.println("Cart total after 10% off: " + discountedAmount);

        // --------- Create Bill and Pay ---------
        PaymentStrategy paymentMethod = new CreditCardPayment();  // or new PaytmPayment()
        Bill bill = new Bill(paymentMethod, discountedAmount);   // use discounted amount
        bill.pay();

        // Clear cart after payment
        u1.cart.amount_product.clear();
        u1.cart.amount = 0;
        System.out.println("Cart cleared. Current total: " + u1.cart.calculate_amount());
    }
}
