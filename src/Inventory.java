import java.util.*;

/**
 * Manages the inventory of relief supplies
 */
public class Inventory {
    private Map<String, Supply> supplies;
    private int maxCapacity; // Maximum weight capacity
    private int currentWeight;
    
    public Inventory(int maxCapacity) {
        this.maxCapacity = maxCapacity;
        this.supplies = new HashMap<>();
        this.currentWeight = 0;
        initializeDefaultSupplies();
    }
    
    /**
     * Initialize with default disaster relief supplies
     */
    private void initializeDefaultSupplies() {
        // Supply(name, weight_per_unit, value_per_unit, quantity, unit)
        addSupply(new Supply("Food Ration", 2, 8, 100, "packs"));
        addSupply(new Supply("Water Bottle", 1, 10, 150, "bottles"));
        addSupply(new Supply("Medicine Kit", 1, 15, 50, "kits"));
        addSupply(new Supply("Blanket", 3, 6, 75, "pieces"));
        addSupply(new Supply("First Aid", 1, 12, 40, "kits"));
    }
    
    /**
     * Add a supply to inventory
     */
    public boolean addSupply(Supply supply) {
        if (supply == null) return false;
        
        String name = supply.getName();
        if (supplies.containsKey(name)) {
            // Update existing supply
            Supply existing = supplies.get(name);
            existing.addQuantity(supply.getQuantity());
        } else {
            supplies.put(name, supply);
        }
        updateCurrentWeight();
        return true;
    }
    
    /**
     * Remove supply from inventory
     */
    public boolean removeSupply(String supplyName, int quantity) {
        Supply supply = supplies.get(supplyName);
        if (supply != null && supply.reduceQuantity(quantity)) {
            updateCurrentWeight();
            return true;
        }
        return false;
    }
    
    /**
     * Get supply by name
     */
    public Supply getSupply(String name) {
        return supplies.get(name);
    }
    
    /**
     * Get all available supplies
     */
    public List<Supply> getAvailableSupplies() {
        List<Supply> available = new ArrayList<>();
        for (Supply supply : supplies.values()) {
            if (supply.isAvailable()) {
                available.add(supply);
            }
        }
        return available;
    }
    
    /**
     * Get all supplies (including out of stock)
     */
    public List<Supply> getAllSupplies() {
        return new ArrayList<>(supplies.values());
    }
    
    /**
     * Check if inventory has any supplies
     */
    public boolean hasSupplies() {
        return !getAvailableSupplies().isEmpty();
    }
    
    /**
     * Update current weight based on all supplies
     */
    private void updateCurrentWeight() {
        currentWeight = 0;
        for (Supply supply : supplies.values()) {
            currentWeight += supply.getWeight() * supply.getQuantity();
        }
    }
    
    /**
     * Get current total weight
     */
    public int getCurrentWeight() {
        return currentWeight;
    }
    
    /**
     * Get maximum capacity
     */
    public int getMaxCapacity() {
        return maxCapacity;
    }
    
    /**
     * Get remaining capacity
     */
    public int getRemainingCapacity() {
        return maxCapacity - currentWeight;
    }
    
    /**
     * Check if inventory is at capacity
     */
    public boolean isAtCapacity() {
        return currentWeight >= maxCapacity;
    }
    
    /**
     * Get inventory summary
     */
    public String getInventorySummary() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== INVENTORY SUMMARY ===\n");
        sb.append(String.format("Capacity: %d/%d (%.1f%% used)\n", 
                currentWeight, maxCapacity, (double)currentWeight/maxCapacity*100));
        sb.append("Supplies:\n");
        
        for (Supply supply : supplies.values()) {
            sb.append(String.format("  %s\n", supply.toString()));
        }
        
        return sb.toString();
    }
    
    /**
     * Reset inventory to initial state
     */
    public void reset() {
        supplies.clear();
        currentWeight = 0;
        initializeDefaultSupplies();
    }
    
    /**
     * Create a deep copy of the inventory
     */
    public Inventory createCopy() {
        Inventory copy = new Inventory(maxCapacity);
        copy.supplies.clear(); // Remove default supplies
        
        for (Supply supply : supplies.values()) {
            copy.addSupply(supply.createCopy(supply.getQuantity()));
        }
        
        return copy;
    }
}