import java.util.*;

// ===== Strategy Interface =====
interface ReplenishmentStrategy {
    void replenish(InventoryItem item);
    int getIntervalDays(); // how often replenishment occurs
}

// ===== Concrete Strategies =====
class MinMaxStrategy implements ReplenishmentStrategy {
    @Override
    public void replenish(InventoryItem item) {
        if (item.getQuantity() < item.getMinLevel()) {
            int orderQty = item.getMaxLevel() - item.getQuantity();
            System.out.println("[Min-Max] Ordering " + orderQty + " units of " + item.getName());
            item.addStock(orderQty);
        }
    }

    @Override
    public int getIntervalDays() { return 1; } // can check daily
}

class EOQStrategy implements ReplenishmentStrategy {
    @Override
    public void replenish(InventoryItem item) {
        int eoq = (int) Math.sqrt((2 * item.getDemand() * item.getOrderingCost()) / item.getHoldingCost());
        System.out.println("[EOQ] Ordering " + eoq + " units of " + item.getName());
        item.addStock(eoq);
    }

    @Override
    public int getIntervalDays() { return 7; } // weekly review
}

class JITStrategy implements ReplenishmentStrategy {
    @Override
    public void replenish(InventoryItem item) {
        if (item.getQuantity() <= 0) {
            System.out.println("[JIT] Ordering exactly what's needed now for " + item.getName());
            item.addStock(item.getDemand());
        }
    }

    @Override
    public int getIntervalDays() { return 0; } // only on-demand
}

class DailyReplenishmentStrategy implements ReplenishmentStrategy {
    @Override
    public void replenish(InventoryItem item) {
        int requiredQty = item.getDemand() / 7; // assuming weekly demand split daily
        System.out.println("[Daily] Ordering " + requiredQty + " units of " + item.getName());
        item.addStock(requiredQty);
    }

    @Override
    public int getIntervalDays() { return 1; }
}

class WeeklyReplenishmentStrategy implements ReplenishmentStrategy {
    @Override
    public void replenish(InventoryItem item) {
        int requiredQty = item.getDemand(); // full week's demand
        System.out.println("[Weekly] Ordering " + requiredQty + " units of " + item.getName());
        item.addStock(requiredQty);
    }

    @Override
    public int getIntervalDays() { return 7; }
}

// ===== Inventory Item =====
class InventoryItem {
    private String name;
    private int quantity;
    private int minLevel;
    private int maxLevel;
    private int demand;
    private double orderingCost;
    private double holdingCost;

    public InventoryItem(String name, int quantity, int minLevel, int maxLevel,
                         int demand, double orderingCost, double holdingCost) {
        this.name = name;
        this.quantity = quantity;
        this.minLevel = minLevel;
        this.maxLevel = maxLevel;
        this.demand = demand;
        this.orderingCost = orderingCost;
        this.holdingCost = holdingCost;
    }

    public String getName() { return name; }
    public int getQuantity() { return quantity; }
    public int getMinLevel() { return minLevel; }
    public int getMaxLevel() { return maxLevel; }
    public int getDemand() { return demand; }
    public double getOrderingCost() { return orderingCost; }
    public double getHoldingCost() { return holdingCost; }

    public void addStock(int qty) { this.quantity += qty; }
}

// ===== Context Class =====
class InventoryManager {
    private ReplenishmentStrategy strategy;

    public void setStrategy(ReplenishmentStrategy strategy) {
        this.strategy = strategy;
    }

    public void manageInventory(InventoryItem item, int day) {
        if (strategy.getIntervalDays() != 0 && day % strategy.getIntervalDays() == 0) {
            strategy.replenish(item);
        }
    }
}

// ===== Main Simulation =====
public class Main {
    public static void main(String[] args) {
        InventoryItem item = new InventoryItem("Sugar", 20, 30, 100, 70, 50, 5);
        InventoryManager manager = new InventoryManager();

        List<ReplenishmentStrategy> strategies = Arrays.asList(
            new MinMaxStrategy(),
            new EOQStrategy(),
            new JITStrategy(),
            new DailyReplenishmentStrategy(),
            new WeeklyReplenishmentStrategy()
        );

        // simulate 14 days
        for (int day = 1; day <= 14; day++) {
            System.out.println("\n=== Day " + day + " ===");
            for (ReplenishmentStrategy s : strategies) {
                manager.setStrategy(s);
                manager.manageInventory(item, day);
            }
        }
    }
}
