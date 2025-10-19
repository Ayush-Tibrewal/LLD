interface PaymentStratergy{
    void makepayment(int amount);
}

class Creditcard implements PaymentStratergy{
    
    @Override
    public void makepayment(int amount){
        System.out.println( amount + "credited from your system ");
    }
}

class Wallet implements PaymentStratergy{
      @Override
    public void makepayment(int amount){
        System.out.println("amount :"+ amount +"DEBITED from your WALLET ");
    }

} 

class PaymentFactory{
    public static PaymentStratergy getpaymentstatergy(String type){
        if(type.equals("WALLET")) return new Wallet();
        else return new Creditcard();
    }
}



class User{
    String name; 
    User(String name){
        this.name = name;
    }
     void userpayment(String type , int amount){
        PaymentStratergy payment = PaymentFactory.getpaymentstatergy(type);
        payment.makepayment(amount);
        System.out.println(amount);
    }
    
}

class Main{
    public static void main(String[] args){
       User ayush = new User("ayush");
       ayush.userpayment("WALLET"  , 10);
       
    }
}







// ----- correct one=-----


// ------------------ Strategy Pattern ------------------
interface PaymentStrategy {
    boolean pay(double amount);
}

class CreditCardPayment implements PaymentStrategy {
    public boolean pay(double amount) {
        System.out.println("Paid $" + amount + " using Credit Card");
        return true;
    }
}

class UpiPayment implements PaymentStrategy {
    public boolean pay(double amount) {
        System.out.println("Paid $" + amount + " using UPI");
        return true;
    }
}

class WalletPayment implements PaymentStrategy {
    public boolean pay(double amount) {
        System.out.println("Paid $" + amount + " using Wallet");
        return true;
    }
}

// ------------------ User ------------------
class User {
    String name;
    User(String name) { this.name = name; }

    public void makePayment(PaymentStrategy paymentMethod, double amount) {
        paymentMethod.pay(amount);
    }
}

// ------------------ Demo ------------------
public class PaymentDemo {
    public static void main(String[] args) {
        User user = new User("Ayush");
        user.makePayment(new CreditCardPayment(), 500);
        user.makePayment(new UpiPayment(), 200);
        user.makePayment(new WalletPayment(), 100);
    }
}
