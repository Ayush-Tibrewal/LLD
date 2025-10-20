import java.util.*;

// ======================= Expense & Split =======================

class Expense {
    String expenseId;
    String description;
    public double expenseAmount;
    public User paidByUser;
    ExpenseSplitType splitType;
    public List<Split> splitDetails = new ArrayList<>();

    public Expense(String expenseId, double expenseAmount, String description,
                   User paidByUser, ExpenseSplitType splitType, List<Split> splitDetails) {
        this.expenseId = expenseId;
        this.expenseAmount = expenseAmount;
        this.description = description;
        this.paidByUser = paidByUser;
        this.splitType = splitType;
        this.splitDetails.addAll(splitDetails);
    }
}

enum ExpenseSplitType {
    EQUAL,
    UNEQUAL,
    PERCENTAGE,
}

class Split {
    User user;
    double amountOwe;

    public Split(User user, double amountOwe) {
        this.user = user;
        this.amountOwe = amountOwe;
    }

    public User getUser() {
        return user;
    }

    public double getAmountOwe() {
        return amountOwe;
    }

    public void setAmountOwe(double amountOwe) {
        this.amountOwe = amountOwe;
    }
}

// ======================= Split Strategies =======================

interface ExpenseSplitStrategy {
    void validateSplitRequest(List<Split> splitDetails, double totalAmount);
}

class EqualExpenseSplit implements ExpenseSplitStrategy {
    @Override
    public void validateSplitRequest(List<Split> splitDetails, double totalAmount) {
        int n = splitDetails.size();
        double equalAmount = totalAmount / n;
        for (Split s : splitDetails) {
            s.setAmountOwe(equalAmount);
        }
    }
}

class UnequalExpenseSplit implements ExpenseSplitStrategy {
    @Override
    public void validateSplitRequest(List<Split> splitDetails, double totalAmount) {
        double sum = 0;
        for (Split s : splitDetails) sum += s.getAmountOwe();
        if (Math.abs(sum - totalAmount) > 0.01)
            throw new IllegalArgumentException("Unequal split amounts do not sum to total expense!");
    }
}

class PercentageExpenseSplit implements ExpenseSplitStrategy {
    @Override
    public void validateSplitRequest(List<Split> splitDetails, double totalAmount) {
        double sumPercent = 0;
        for (Split s : splitDetails) sumPercent += s.getAmountOwe();
        if (Math.abs(sumPercent - 100) > 0.01)
            throw new IllegalArgumentException("Total percentage must be 100%");
        for (Split s : splitDetails)
            s.setAmountOwe((s.getAmountOwe() / 100.0) * totalAmount);
    }
}

// ======================= Balance & User =======================

class Balance {
    double amountOwe;
    double amountGetBack;

    public double getAmountOwe() {
        return amountOwe;
    }

    public void setAmountOwe(double amountOwe) {
        this.amountOwe = amountOwe;
    }

    public double getAmountGetBack() {
        return amountGetBack;
    }

    public void setAmountGetBack(double amountGetBack) {
        this.amountGetBack = amountGetBack;
    }

    @Override
    public String toString() {
        return "{Owe=" + amountOwe + ", GetBack=" + amountGetBack + "}";
    }
}

class UserExpenseBalanceSheet {
    Map<String, Balance> userVsBalance;
    double totalYourExpense;
    double totalPayment;
    double totalYouOwe;
    double totalYouGetBack;

    public UserExpenseBalanceSheet() {
        userVsBalance = new HashMap<>();
    }

    public Map<String, Balance> getUserVsBalance() {
        return userVsBalance;
    }
}

class User {
    String userId;
    String userName;
    UserExpenseBalanceSheet userExpenseBalanceSheet;

    public User(String id, String userName) {
        this.userId = id;
        this.userName = userName;
        userExpenseBalanceSheet = new UserExpenseBalanceSheet();
    }

    public String getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public UserExpenseBalanceSheet getUserExpenseBalanceSheet() {
        return userExpenseBalanceSheet;
    }
}

class UserController {
    List<User> userList = new ArrayList<>();

    public void addUser(User user) {
        userList.add(user);
    }

    public User getUser(String userID) {
        for (User user : userList)
            if (user.getUserId().equals(userID)) return user;
        return null;
    }
}

// ======================= Balance Sheet Controller =======================

class BalanceSheetController {
    public void updateUserExpenseBalanceSheet(User paidByUser, List<Split> splitDetails, double totalAmount) {
        for (Split split : splitDetails) {
            User owedUser = split.getUser();
            if (owedUser.getUserId().equals(paidByUser.getUserId())) continue;

            double oweAmount = split.getAmountOwe();

            Balance balance = paidByUser.getUserExpenseBalanceSheet()
                    .getUserVsBalance()
                    .getOrDefault(owedUser.getUserId(), new Balance());
            balance.setAmountGetBack(balance.getAmountGetBack() + oweAmount);
            paidByUser.getUserExpenseBalanceSheet().getUserVsBalance().put(owedUser.getUserId(), balance);

            Balance owedBalance = owedUser.getUserExpenseBalanceSheet()
                    .getUserVsBalance()
                    .getOrDefault(paidByUser.getUserId(), new Balance());
            owedBalance.setAmountOwe(owedBalance.getAmountOwe() + oweAmount);
            owedUser.getUserExpenseBalanceSheet().getUserVsBalance().put(paidByUser.getUserId(), owedBalance);
        }
    }
}

// ======================= Expense Controller =======================

class ExpenseController {
    BalanceSheetController balanceSheetController = new BalanceSheetController();

    public Expense createExpense(String expenseId, String description, double expenseAmount,
                                 List<Split> splitDetails, ExpenseSplitType splitType, User paidByUser) {

        ExpenseSplitStrategy strategy = switch (splitType) {
            case EQUAL -> new EqualExpenseSplit();
            case UNEQUAL -> new UnequalExpenseSplit();
            case PERCENTAGE -> new PercentageExpenseSplit();
        };

        strategy.validateSplitRequest(splitDetails, expenseAmount);
        Expense expense = new Expense(expenseId, expenseAmount, description, paidByUser, splitType, splitDetails);
        balanceSheetController.updateUserExpenseBalanceSheet(paidByUser, splitDetails, expenseAmount);
        return expense;
    }
}

// ======================= Group =======================

class Group {
    String groupId;
    String groupName;
    List<User> groupMembers;
    List<Expense> expenseList;
    ExpenseController expenseController;

    public Group() {
        groupMembers = new ArrayList<>();
        expenseList = new ArrayList<>();
        expenseController = new ExpenseController();
    }

    public void addMember(User member) {
        if (groupMembers.contains(member)) {
            System.out.println("User " + member.getUserName() + " is already a member of the group!");
        } else {
            groupMembers.add(member);
            System.out.println("User " + member.getUserName() + " added to the group.");
        }
    }

    public Expense createExpense(String expenseId, String description, double expenseAmount,
                                 List<Split> splitDetails, ExpenseSplitType splitType, User paidByUser) {
        Expense expense = expenseController.createExpense(expenseId, description, expenseAmount, splitDetails, splitType, paidByUser);
        expenseList.add(expense);
        return expense;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
}

class GroupController {
    List<Group> groupList = new ArrayList<>();

    public Group createNewGroup(String groupId, String groupName, User createdByUser) {
        Group group = new Group();
        group.setGroupId(groupId);
        group.setGroupName(groupName);
        group.addMember(createdByUser);
        groupList.add(group);
        return group;
    }

    public Group getGroup(String groupId) {
        for (Group g : groupList)
            if (g.getGroupId().equals(groupId)) return g;
        System.out.println("No such group exists!");
        return null;
    }
}

// ======================= DEMO MAIN =======================

public class Main {
    public static void main(String[] args) {
        UserController userController = new UserController();
        User u1 = new User("U1", "Ayush");
        User u2 = new User("U2", "Rahul");
        User u3 = new User("U3", "Neha");

        userController.addUser(u1);
        userController.addUser(u2);
        userController.addUser(u3);

        GroupController groupController = new GroupController();
        Group tripGroup = groupController.createNewGroup("G1", "Goa Trip", u1);
        tripGroup.addMember(u2);
        tripGroup.addMember(u3);

        List<Split> splits = Arrays.asList(
                new Split(u1, 0),
                new Split(u2, 0),
                new Split(u3, 0)
        );

        tripGroup.createExpense("E1", "Dinner", 900, splits, ExpenseSplitType.EQUAL, u1);

        System.out.println("\n=== Final Balances ===");
        for (User user : List.of(u1, u2, u3)) {
            System.out.println(user.getUserName() + " => " + user.getUserExpenseBalanceSheet().getUserVsBalance());
        }
    }
}
