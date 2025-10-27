import java.util.*;

enum SplitType {
    EQUAL,
    UNEQUAL,
    PERCENTAGE
}

// ===== User =====
class User {
    String userId;
    String name;
    String email;

    User(String userId, String name, String email) {
        this.userId = userId;
        this.name = name;
        this.email = email;
    }

    public String toString() {
        return name;
    }
}

// ===== Split =====
abstract class Split {
    User user;
    double amount;

    Split(User user, double amount) {
        this.user = user;
        this.amount = amount;
    }

    User getUser() { return user; }
    double getAmount() { return amount; }
}

class EqualSplit extends Split {
    EqualSplit(User user) {
        super(user, 0);
    }
}

class UnequalSplit extends Split {
    UnequalSplit(User user, double amount) {
        super(user, amount);
    }
}

class PercentageSplit extends Split {
    double percent;
    PercentageSplit(User user, double percent) {
        super(user, 0);
        this.percent = percent;
    }
}

// ===== Expense =====
class Expense {
    String id;
    String description;
    double amount;
    User paidBy;
    SplitType splitType;
    List<Split> splits;

    Expense(String id, String description, double amount, User paidBy,
            SplitType splitType, List<Split> splits) {
        this.id = id;
        this.description = description;
        this.amount = amount;
        this.paidBy = paidBy;
        this.splitType = splitType;
        this.splits = splits;
    }

    boolean validate() {
        if (splitType == SplitType.EQUAL) return true;
        double total = 0;
        if (splitType == SplitType.UNEQUAL) {
            for (Split s : splits) total += s.amount;
        } else if (splitType == SplitType.PERCENTAGE) {
            for (Split s : splits) total += ((PercentageSplit) s).percent;
            return total == 100;
        }
        return Math.abs(total - amount) < 0.01;
    }
}

// ===== BalanceSheet =====
class BalanceSheet {
    Map<User, Map<User, Double>> balanceSheet = new HashMap<>();

    void updateBalance(User paidBy, List<Split> splits, double totalAmount) {
        for (Split split : splits) {
            User user = split.user;
            if (user == paidBy) continue;

            balanceSheet.putIfAbsent(user, new HashMap<>());
            balanceSheet.putIfAbsent(paidBy, new HashMap<>());

            double amount = split.amount;
            balanceSheet.get(user).put(paidBy,
                balanceSheet.get(user).getOrDefault(paidBy, 0.0) + amount);
            balanceSheet.get(paidBy).put(user,
                balanceSheet.get(paidBy).getOrDefault(user, 0.0) - amount);
        }
    }

    void showBalances() {
        for (User u1 : balanceSheet.keySet()) {
            for (User u2 : balanceSheet.get(u1).keySet()) {
                double amount = balanceSheet.get(u1).get(u2);
                if (amount > 0) {
                    System.out.println(u1.name + " owes " + u2.name + " : " + amount);
                }
            }
        }
    }
}

// ===== ExpenseManager =====
class ExpenseManager {
    List<User> users = new ArrayList<>();
    BalanceSheet balanceSheet = new BalanceSheet();

    void addUser(User user) {
        users.add(user);
    }

    void addExpense(String id, String description, double amount, User paidBy,
                    SplitType splitType, List<Split> splits) {
        Expense expense = new Expense(id, description, amount, paidBy, splitType, splits);
        if (!expense.validate()) {
            System.out.println("Invalid Expense!");
            return;
        }

        if (splitType == SplitType.EQUAL) {
            double splitAmount = amount / splits.size();
            for (Split split : splits) split.amount = splitAmount;
        } else if (splitType == SplitType.PERCENTAGE) {
            for (Split split : splits) {
                PercentageSplit ps = (PercentageSplit) split;
                split.amount = (amount * ps.percent) / 100.0;
            }
        }

        balanceSheet.updateBalance(paidBy, splits, amount);
    }

    void showBalances() {
        balanceSheet.showBalances();
    }
}

// ===== Test =====
public class Main {
    public static void main(String[] args) {
        ExpenseManager manager = new ExpenseManager();

        User u1 = new User("u1", "Ayush", "a@x.com");
        User u2 = new User("u2", "Ravi", "r@x.com");
        User u3 = new User("u3", "Neha", "n@x.com");

        manager.addUser(u1);
        manager.addUser(u2);
        manager.addUser(u3);

        // Example 1: Equal Split
        List<Split> splits1 = Arrays.asList(new EqualSplit(u1), new EqualSplit(u2), new EqualSplit(u3));
        manager.addExpense("E1", "Dinner", 900, u1, SplitType.EQUAL, splits1);

        // Example 2: Unequal Split
        List<Split> splits2 = Arrays.asList(
            new UnequalSplit(u1, 100),
            new UnequalSplit(u2, 400),
            new UnequalSplit(u3, 500)
        );
        manager.addExpense("E2", "Movie", 1000, u2, SplitType.UNEQUAL, splits2);

        // Example 3: Percentage Split
        List<Split> splits3 = Arrays.asList(
            new PercentageSplit(u1, 20),
            new PercentageSplit(u2, 30),
            new PercentageSplit(u3, 50)
        );
        manager.addExpense("E3", "Trip", 2000, u3, SplitType.PERCENTAGE, splits3);

        System.out.println("\nFinal Balances:");
        manager.showBalances();
    }
}
