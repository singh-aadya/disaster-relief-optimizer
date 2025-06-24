import java.util.*;

/**
 * Core allocation engine using Greedy and Knapsack algorithms
 */
public class SupplyAllocator {
    private List<Family> families;
    private Inventory inventory;
    private List<AllocationResult> allocationResults;
    private int baseCapacityPerFamily;
    
    public SupplyAllocator(int baseCapacityPerFamily) {
        this.families = new ArrayList<>();
        this.inventory = new Inventory(1000); // Default capacity
        this.allocationResults = new ArrayList<>();
        this.baseCapacityPerFamily = baseCapacityPerFamily;
    }
    
    public SupplyAllocator(List<Family> families, Inventory inventory, int baseCapacityPerFamily) {
        this.families = new ArrayList<>(families);
        this.inventory = inventory;
        this.allocationResults = new ArrayList<>();
        this.baseCapacityPerFamily = baseCapacityPerFamily;
    }
    
    /**
     * Main allocation method using Greedy + Knapsack approach
     */
    public List<AllocationResult> allocateSupplies() {
        allocationResults.clear();
        
        if (families.isEmpty() || !inventory.hasSupplies()) {
            System.out.println("No families to serve or no supplies available.");
            return allocationResults;
        }
        
        // Step 1: Sort families by priority (Greedy approach)
        List<Family> sortedFamilies = new ArrayList<>(families);
        sortedFamilies.sort(Collections.reverseOrder()); // Sort by priority score descending
        
        // Filter only active families
        sortedFamilies.removeIf(family -> !family.isActive());
        
        System.out.println("=== ALLOCATION PROCESS ===");
        System.out.println("Families sorted by priority:");
        for (int i = 0; i < sortedFamilies.size(); i++) {
            System.out.printf("%d. %s\n", i+1, sortedFamilies.get(i));
        }
        
        // Step 2: Allocate to each family using modified Knapsack
        for (Family family : sortedFamilies) {
            AllocationResult result = allocateToFamily(family);
            allocationResults.add(result);
        }
        
        return allocationResults;
    }
    
    /**
     * Allocate supplies to a specific family using 0/1 Knapsack approach
     */
    private AllocationResult allocateToFamily(Family family) {
        AllocationResult result = new AllocationResult(family.getFamilyId());
        
        // Calculate family capacity based on size and priority
        int familyCapacity = calculateFamilyCapacity(family);
        
        // Get available supplies
        List<Supply> availableSupplies = inventory.getAvailableSupplies();
        
        if (availableSupplies.isEmpty()) {
            return result;
        }
        
        // Apply Knapsack algorithm
        Map<String, Integer> allocation = knapsackAllocation(availableSupplies, familyCapacity, family);
        
        // Update inventory and result
        for (Map.Entry<String, Integer> entry : allocation.entrySet()) {
            String supplyName = entry.getKey();
            int quantity = entry.getValue();
            
            if (quantity > 0) {
                Supply supply = inventory.getSupply(supplyName);
                if (supply != null && supply.reduceQuantity(quantity)) {
                    result.addAllocatedSupply(supplyName, quantity, supply.getValue(), supply.getWeight());
                }
            }
        }
        
        result.calculateAllocationScore(family.getPriorityScore());
        
        System.out.printf("Allocated to %s (Priority: %.2f): %s\n", 
                family.getFamilyId(), family.getPriorityScore(), 
                result.hasAllocations() ? "Success" : "No allocation possible");
        
        return result;
    }
    
    /**
     * Calculate capacity allocation for a family based on size and priority
     */
    private int calculateFamilyCapacity(Family family) {
        // Base capacity + family size multiplier + priority bonus
        double priorityMultiplier = family.getPriorityScore() / 10.0; // Normalize to 0-1
        int capacity = (int) (baseCapacityPerFamily + (family.getSize() * 2) + (priorityMultiplier * 5));
        return Math.max(capacity, 5); // Minimum capacity of 5
    }
    
    /**
     * Modified 0/1 Knapsack algorithm for supply allocation
     */
    private Map<String, Integer> knapsackAllocation(List<Supply> supplies, int capacity, Family family) {
        Map<String, Integer> allocation = new HashMap<>();
        
        // Sort supplies by value-to-weight ratio (Greedy approach within Knapsack)
        supplies.sort((s1, s2) -> Double.compare(s2.getValueWeightRatio(), s1.getValueWeightRatio()));
        
        int remainingCapacity = capacity;
        
        // Priority allocation based on family urgency
        if (family.getUrgencyScore() >= 8) {
            // High urgency: prioritize medicine and water
            remainingCapacity = prioritizedAllocation(supplies, allocation, remainingCapacity, 
                    Arrays.asList("Medicine Kit", "Water Bottle", "First Aid"));
        }
        
        // Standard knapsack allocation for remaining capacity
        for (Supply supply : supplies) {
            if (remainingCapacity <= 0) break;
            
            String name = supply.getName();
            int maxUnits = Math.min(supply.getQuantity(), remainingCapacity / Math.max(1, supply.getWeight()));
            
            if (maxUnits > 0) {
                // Calculate optimal units based on family size and remaining capacity
                int optimalUnits = calculateOptimalUnits(supply, family, maxUnits, remainingCapacity);
                
                if (optimalUnits > 0) {
                    allocation.put(name, optimalUnits);
                    remainingCapacity -= optimalUnits * supply.getWeight();
                }
            }
        }
        
        return allocation;
    }
    
    /**
     * Prioritized allocation for high-urgency families
     */
    private int prioritizedAllocation(List<Supply> supplies, Map<String, Integer> allocation, 
                                    int capacity, List<String> prioritySupplies) {
        for (String priorityName : prioritySupplies) {
            Supply supply = supplies.stream()
                    .filter(s -> s.getName().equals(priorityName))
                    .findFirst()
                    .orElse(null);
            
            if (supply != null && capacity > 0) {
                int maxUnits = Math.min(supply.getQuantity(), capacity / Math.max(1, supply.getWeight()));
                if (maxUnits > 0) {
                    int units = Math.min(maxUnits, 3); // Limit priority allocation
                    allocation.put(priorityName, units);
                    capacity -= units * supply.getWeight();
                }
            }
        }
        return capacity;
    }
    
    /**
     * Calculate optimal units for a supply based on family characteristics
     */
    private int calculateOptimalUnits(Supply supply, Family family, int maxUnits, int remainingCapacity) {
        // Base allocation
        int baseUnits = Math.max(1, family.getSize() / 2);
        
        // Adjust based on supply type
        switch (supply.getName()) {
            case "Water Bottle":
                baseUnits = family.getSize(); // 1 per person minimum
                break;
            case "Food Ration":
                baseUnits = Math.max(1, family.getSize() / 2); // 1 per 2 people
                break;
            case "Medicine Kit":
                baseUnits = family.getUrgencyScore() >= 7 ? 2 : 1;
                break;
            case "Blanket":
                baseUnits = Math.max(1, family.getSize() / 3); // 1 per 3 people
                break;
        }
        
        // Ensure within constraints
        return Math.min(baseUnits, Math.min(maxUnits, remainingCapacity / Math.max(1, supply.getWeight())));
    }
    
    /**
     * Rebalance allocations when inventory or families change
     */
    public List<AllocationResult> rebalanceAllocations() {
        System.out.println("=== REBALANCING ALLOCATIONS ===");
        
        // Restore supplies from previous allocations
        restoreSuppliesFromAllocations();
        
        // Re-run allocation
        return allocateSupplies();
    }
    
    /**
     * Restore supplies from current allocations back to inventory
     */
    private void restoreSuppliesFromAllocations() {
        for (AllocationResult result : allocationResults) {
            for (Map.Entry<String, Integer> entry : result.getAllocatedSupplies().entrySet()) {
                Supply supply = inventory.getSupply(entry.getKey());
                if (supply != null) {
                    supply.addQuantity(entry.getValue());
                }
            }
        }
    }
    
    // Getters and Setters
    public List<Family> getFamilies() { return new ArrayList<>(families); }
    public Inventory getInventory() { return inventory; }
    public List<AllocationResult> getAllocationResults() { return new ArrayList<>(allocationResults); }
    
    public void addFamily(Family family) {
        if (family != null && !families.contains(family)) {
            families.add(family);
        }
    }
    
    public boolean removeFamily(String familyId) {
        return families.removeIf(family -> family.getFamilyId().equals(familyId));
    }
    
    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }
    
    public void updateFamilyStatus(String familyId, boolean active) {
        families.stream()
                .filter(family -> family.getFamilyId().equals(familyId))
                .findFirst()
                .ifPresent(family -> family.setActive(active));
    }
}