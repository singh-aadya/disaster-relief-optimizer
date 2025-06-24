import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Generates allocation reports and exports to various formats
 */
public class ReportGenerator {
    private List<AllocationResult> allocationResults;
    private List<Family> families;
    private Inventory inventory;
    
    public ReportGenerator(List<AllocationResult> allocationResults, List<Family> families, Inventory inventory) {
        this.allocationResults = allocationResults;
        this.families = families;
        this.inventory = inventory;
    }
    
    /**
     * Generate comprehensive allocation report
     */
    public String generateAllocationReport() {
        StringBuilder report = new StringBuilder();
        
        // Header
        report.append("=====================================\n");
        report.append("     DISASTER RELIEF ALLOCATION     \n");
        report.append("           REPORT                    \n");
        report.append("=====================================\n");
        report.append("Generated: ").append(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))).append("\n\n");
        
        // Summary statistics
        report.append(generateSummarySection());
        
        // Family allocation details
        report.append(generateFamilyAllocationSection());
        
        // Inventory status
        report.append(generateInventoryStatusSection());
        
        // Supply usage chart (ASCII)
        report.append(generateSupplyUsageChart());
        
        return report.toString();
    }
    
    /**
     * Generate summary statistics section
     */
    private String generateSummarySection() {
        StringBuilder section = new StringBuilder();
        section.append("=== ALLOCATION SUMMARY ===\n");
        
        int totalFamilies = families.size();
        int activeFamilies = (int) families.stream().filter(Family::isActive).count();
        int servedFamilies = (int) allocationResults.stream().filter(AllocationResult::hasAllocations).count();
        int unservedFamilies = activeFamilies - servedFamilies;
        
        int totalValue = allocationResults.stream().mapToInt(AllocationResult::getTotalValue).sum();
        int totalWeight = allocationResults.stream().mapToInt(AllocationResult::getTotalWeight).sum();
        
        section.append(String.format("Total Families: %d\n", totalFamilies));
        section.append(String.format("Active Families: %d\n", activeFamilies));
        section.append(String.format("Families Served: %d (%.1f%%)\n", servedFamilies, (double)servedFamilies/activeFamilies*100));
        section.append(String.format("Families Unserved: %d\n", unservedFamilies));
        section.append(String.format("Total Value Distributed: %d\n", totalValue));
        section.append(String.format("Total Weight Distributed: %d\n", totalWeight));
        section.append(String.format("Average Value per Family: %.1f\n", servedFamilies > 0 ? (double)totalValue/servedFamilies : 0));
        section.append("\n");
        
        return section.toString();
    }
    
    /**
     * Generate family allocation details section
     */
    private String generateFamilyAllocationSection() {
        StringBuilder section = new StringBuilder();
        section.append("=== FAMILY ALLOCATIONS ===\n");
        
        // Sort results by allocation score (highest first)
        List<AllocationResult> sortedResults = new ArrayList<>(allocationResults);
        sortedResults.sort((r1, r2) -> Double.compare(r2.getAllocationScore(), r1.getAllocationScore()));
        
        for (AllocationResult result : sortedResults) {
            // Find corresponding family
            Family family = families.stream()
                    .filter(f -> f.getFamilyId().equals(result.getFamilyId()))
                    .findFirst()
                    .orElse(null);
            
            if (family != null) {
                section.append(String.format("Family ID: %s (Size: %d, Distance: %.1fkm, Urgency: %d, Priority: %.2f)\n",
                        family.getFamilyId(), family.getSize(), family.getDistance(), 
                        family.getUrgencyScore(), family.getPriorityScore()));
                
                if (result.hasAllocations()) {
                    for (Map.Entry<String, Integer> entry : result.getAllocatedSupplies().entrySet()) {
                        section.append(String.format("  ✓ %s: %d units\n", entry.getKey(), entry.getValue()));
                    }
                    section.append(String.format("  Total Value: %d, Weight: %d, Score: %.2f\n",
                            result.getTotalValue(), result.getTotalWeight(), result.getAllocationScore()));
                } else {
                    section.append("  ✗ No supplies allocated\n");
                }
                section.append("\n");
            }
        }
        
        return section.toString();
    }
    
    /**
     * Generate inventory status section
     */
    private String generateInventoryStatusSection() {
        StringBuilder section = new StringBuilder();
        section.append("=== REMAINING INVENTORY ===\n");
        
        List<Supply> supplies = inventory.getAllSupplies();
        supplies.sort((s1, s2) -> s1.getName().compareTo(s2.getName()));
        
        for (Supply supply : supplies) {
            String status = supply.isAvailable() ? "Available" : "OUT OF STOCK";
            section.append(String.format("%-15s: %3d %-8s [%s]\n", 
                    supply.getName(), supply.getQuantity(), supply.getUnit(), status));
        }
        
        section.append(String.format("\nCapacity Usage: %d/%d (%.1f%%)\n", 
                inventory.getCurrentWeight(), inventory.getMaxCapacity(), 
                (double)inventory.getCurrentWeight()/inventory.getMaxCapacity()*100));
        section.append("\n");
        
        return section.toString();
    }
    
    /**
     * Generate ASCII chart showing supply usage
     */
    private String generateSupplyUsageChart() {
        StringBuilder chart = new StringBuilder();
        chart.append("=== SUPPLY USAGE CHART ===\n");
        
        // Calculate initial quantities vs remaining
        Map<String, Integer> initialQuantities = getInitialQuantities();
        
        for (Supply supply : inventory.getAllSupplies()) {
            String name = supply.getName();
            int initial = initialQuantities.getOrDefault(name, 0);
            int remaining = supply.getQuantity();
            int used = initial - remaining;
            
            double usagePercent = initial > 0 ? (double)used / initial * 100 : 0;
            
            chart.append(String.format("%-15s ", name));
            
            // ASCII bar chart (50 chars max)
            int barLength = (int)(usagePercent / 2); // Scale to 50 chars
            for (int i = 0; i < 50; i++) {
                if (i < barLength) {
                    chart.append("█");
                } else {
                    chart.append("░");
                }
            }
            
            chart.append(String.format(" %.1f%% (%d/%d)\n", usagePercent, used, initial));
        }
        
        chart.append("\nLegend: █ = Used, ░ = Remaining\n\n");
        
        return chart.toString();
    }
    
    /**
     * Get initial quantities (hardcoded based on default initialization)
     */
    private Map<String, Integer> getInitialQuantities() {
        Map<String, Integer> initial = new HashMap<>();
        initial.put("Food Ration", 100);
        initial.put("Water Bottle", 150);
        initial.put("Medicine Kit", 50);
        initial.put("Blanket", 75);
        initial.put("First Aid", 40);
        return initial;
    }
    
    /**
     * Export allocation results to CSV file
     */
    public boolean exportToCSV(String filename) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            // CSV Header
            writer.println("Family_ID,Family_Size,Distance,Urgency,Priority_Score,Supplies_Allocated,Total_Value,Total_Weight,Allocation_Score");
            
            for (AllocationResult result : allocationResults) {
                // Find corresponding family
                Family family = families.stream()
                        .filter(f -> f.getFamilyId().equals(result.getFamilyId()))
                        .findFirst()
                        .orElse(null);
                
                if (family != null) {
                    StringBuilder suppliesStr = new StringBuilder();
                    for (Map.Entry<String, Integer> entry : result.getAllocatedSupplies().entrySet()) {
                        if (suppliesStr.length() > 0) suppliesStr.append(";");
                        suppliesStr.append(entry.getKey()).append(":").append(entry.getValue());
                    }
                    
                    writer.printf("%s,%d,%.1f,%d,%.2f,\"%s\",%d,%d,%.2f\n",
                            family.getFamilyId(),
                            family.getSize(),
                            family.getDistance(),
                            family.getUrgencyScore(),
                            family.getPriorityScore(),
                            suppliesStr.toString(),
                            result.getTotalValue(),
                            result.getTotalWeight(),
                            result.getAllocationScore());
                }
            }
            
            System.out.println("Report exported to: " + filename);
            return true;
            
        } catch (IOException e) {
            System.err.println("Error exporting to CSV: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Generate quick summary for console display
     */
    public String generateQuickSummary() {
        int served = (int) allocationResults.stream().filter(AllocationResult::hasAllocations).count();
        int total = families.size();
        int totalValue = allocationResults.stream().mapToInt(AllocationResult::getTotalValue).sum();
        
        return String.format("Allocation Complete: %d/%d families served, Total value distributed: %d", 
                served, total, totalValue);
    }
}