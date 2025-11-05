import java.util.*;

class ATM {
    int id;
    double availableCash;
    Bank bank;

    ATM(int id, double availableCash, Bank bank) {
        this.id = id;
        this.availableCash = availableCash;
        this.bank = bank;
    }

    boolean authenticate(Card card, int pin) {
        if (card == null) return false;
        return bank.validateCard(card, pin);
    }

    boolean executeTransaction(Customer customer, Transaction txn) {
        if (customer.status != CustomerStatus.ACTIVE) {
            System.out.println("Account not active.");
            return false;
        }

        Account acc = customer.account;
        switch (txn.type) {
            case BALANCE_INQUIRY:
                System.out.println("Balance: " + acc.balance);
                return true;

            case DEPOSIT:
                acc.balance += txn.amount;
                System.out.println("Deposited: " + txn.amount);
                return true;

            case WITHDRAW:
                if (txn.amount > acc.balance || txn.amount > availableCash) {
                    System.out.println("Insufficient funds.");
                    return false;
                }
                acc.balance -= txn.amount;
                availableCash -= txn.amount;
                System.out.println("Withdrawn: " + txn.amount);
                return true;

            case TRANSFER:
                Account to = bank.getAccount(txn.destAccount);
                if (to == null || txn.amount > acc.balance) {
                    System.out.println("Transfer failed.");
                    return false;
                }
                acc.balance -= txn.amount;
                to.balance += txn.amount;
                System.out.println("Transferred " + txn.amount + " to " + to.accNo);
                return true;

            default:
                return false;
        }
    }
}

class Bank {
    String name;
    Map<Integer, Account> accounts = new HashMap<>();

    Bank(String name) { this.name = name; }

    void addAccount(Account a) { accounts.put(a.accNo, a); }

    Account getAccount(int no) { return accounts.get(no); }

    boolean validateCard(Card c, int pin) {
        return c.pin == pin && c.expiry.after(new Date());
    }
}

class Account {
    int accNo;
    double balance;
    Account(int accNo, double balance) {
        this.accNo = accNo; this.balance = balance;
    }
}

class Card {
    String number;
    Date expiry;
    int pin;
    Card(String number, Date expiry, int pin) {
        this.number = number; this.expiry = expiry; this.pin = pin;
    }
}

class Customer {
    String name;
    Account account;
    Card card;
    CustomerStatus status;
    Customer(String name, Account a, Card c) {
        this.name = name; this.account = a; this.card = c;
        this.status = CustomerStatus.ACTIVE;
    }
}

enum CustomerStatus { ACTIVE, BLOCKED, CLOSED; }

enum TxnType { BALANCE_INQUIRY, DEPOSIT, WITHDRAW, TRANSFER; }

class Transaction {
    TxnType type;
    double amount;
    int destAccount;
    Transaction(TxnType type, double amount, int destAccount) {
        this.type = type; this.amount = amount; this.destAccount = destAccount;
    }
}

public class ATMSystem {
    public static void main(String[] args) {
        Bank bank = new Bank("HDFC");
        Account a1 = new Account(101, 1000);
        Account a2 = new Account(102, 500);
        bank.addAccount(a1);
        bank.addAccount(a2);

        Card c1 = new Card("1234-5678", new Date(System.currentTimeMillis() + 86400000L), 1234);
        Customer cust = new Customer("Ayush", a1, c1);

        ATM atm = new ATM(1, 2000, bank);

        // Authenticate
        if (atm.authenticate(c1, 1234)) {
            System.out.println("Login Success!\n");

            atm.executeTransaction(cust, new Transaction(TxnType.BALANCE_INQUIRY, 0, 0));
            atm.executeTransaction(cust, new Transaction(TxnType.WITHDRAW, 300, 0));
            atm.executeTransaction(cust, new Transaction(TxnType.TRANSFER, 200, 102));
            atm.executeTransaction(cust, new Transaction(TxnType.BALANCE_INQUIRY, 0, 0));
        }
    }
}
