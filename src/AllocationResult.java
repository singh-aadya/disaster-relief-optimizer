import java.util.*;

/**
 * Represents the result of supply allocation for a family
 */
public class AllocationResult {
    private String familyId;
    private Map<String, Integer> allocatedSupplies;
    private int totalValue;
    private int totalWeight;
    private double allocationScore;
    
    public AllocationResult(String familyId) {
        this.familyId = familyId;
        this.allocatedSupplies = new HashMap<>();
        this.totalValue = 0;
        this.totalWeight = 0;
        this.allocationScore = 0.0;
    }
    
    /**
     * Add allocated supply
     */
    public void addAllocatedSupply(String supplyName, int quantity, int unitValue, int unitWeight) {
        if (quantity > 0) {
            allocatedSupplies.put(supplyName, allocatedSupplies.getOrDefault(supplyName, 0) + quantity);
            totalValue += quantity * unitValue;
            totalWeight += quantity * unitWeight;
        }
    }
    
    /**
     * Remove allocated supply
     */
    public void removeAllocatedSupply(String supplyName) {
        allocatedSupplies.remove(supplyName);
        // Note: This doesn't update totalValue/totalWeight - use recalculate() if needed
    }
    
    /**
     * Check if family received any supplies
     */
    public boolean hasAllocations() {
        return !allocatedSupplies.isEmpty() && totalValue > 0;
    }
    
    /**
     * Get allocated quantity for specific supply
     */
    public int getAllocatedQuantity(String supplyName) {
        return allocatedSupplies.getOrDefault(supplyName, 0);
    }
    
    /**
     * Get all allocated supplies
     */
    public Map<String, Integer> getAllocatedSupplies() {
        return new HashMap<>(allocatedSupplies);
    }
    
    /**
     * Calculate allocation score based on value and weight efficiency
     */
    public void calculateAllocationScore(double familyPriority) {
        if (totalWeight > 0) {
            allocationScore = (double) totalValue / totalWeight * familyPriority;
        } else {
            allocationScore = 0.0;
        }
    }
    
    // Getters
    public String getFamilyId() { return familyId; }
    public int getTotalValue() { return totalValue; }
    public int getTotalWeight() { return totalWeight; }
    public double getAllocationScore() { return allocationScore; }
    
    /**
     * Get formatted allocation summary
     */
    public String getAllocationSummary() {
        if (!hasAllocations()) {
            return String.format("Family %s: No supplies allocated", familyId);
        }
        
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Family %s:\n", familyId));
        
        for (Map.Entry<String, Integer> entry : allocatedSupplies.entrySet()) {
            sb.append(String.format("  - %s: %d units\n", entry.getKey(), entry.getValue()));
        }
        
        sb.append(String.format("  Total Value: %d, Total Weight: %d, Score: %.2f\n", 
                totalValue, totalWeight, allocationScore));
        
        return sb.toString();
    }
    
    /**
     * Get CSV formatted string
     */
    public String toCSV() {
        StringBuilder sb = new StringBuilder();
        sb.append(familyId);
        
        // Add each supply allocation
        for (Map.Entry<String, Integer> entry : allocatedSupplies.entrySet()) {
            sb.append(",").append(entry.getKey()).append(":").append(entry.getValue());
        }
        
        sb.append(",").append(totalValue).append(",").append(totalWeight).append(",").append(String.format("%.2f", allocationScore));
        
        return sb.toString();
    }
    
    @Override
    public String toString() {
        return getAllocationSummary();
    }
    
    /**
     * Clear all allocations
     */
    public void clear() {
        allocatedSupplies.clear();
        totalValue = 0;
        totalWeight = 0;
        allocationScore = 0.0;
    }
}