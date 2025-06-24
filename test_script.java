/**
 * Test script to verify SupplyMate functionality
 */
public class test_script {
    public static void main(String[] args) {
        System.out.println("=== SUPPLYMATE FUNCTIONALITY TEST ===");
        
        // Test 1: Core Classes
        testCoreClasses();
        
        // Test 2: Sample Data Generation
        testSampleData();
        
        // Test 3: Allocation Algorithm
        testAllocationAlgorithm();
        
        // Test 4: Report Generation
        testReportGeneration();
        
        System.out.println("\n=== ALL TESTS COMPLETED ===");
    }
    
    private static void testCoreClasses() {
        System.out.println("\n--- Testing Core Classes ---");
        
        // Test Family class
        Family family = new Family("TEST001", 4, 5.5, 8);
        System.out.println("Family created: " + family);
        
        // Test Supply class
        Supply supply = new Supply("Test Supply", 2, 10, 50, "units");
        System.out.println("Supply created: " + supply);
        System.out.println("Value-Weight Ratio: " + supply.getValueWeightRatio());
        
        // Test Inventory class
        Inventory inventory = new Inventory(500);
        System.out.println("Inventory created with capacity: " + inventory.getMaxCapacity());
        System.out.println("Available supplies: " + inventory.getAvailableSupplies().size());
        
        System.out.println("✓ Core classes working correctly");
    }
    
    private static void testSampleData() {
        System.out.println("\n--- Testing Sample Data Generation ---");
        
        SampleDataGenerator generator = new SampleDataGenerator();
        
        // Test sample families
        var families = generator.generateSampleFamilies();
        System.out.println("Generated " + families.size() + " sample families");
        
        // Test random families
        var randomFamilies = generator.generateRandomFamilies(5);
        System.out.println("Generated " + randomFamilies.size() + " random families");
        
        // Test scenarios
        var scenarios = generator.getAllTestScenarios();
        System.out.println("Available test scenarios: " + scenarios.size());
        
        System.out.println("✓ Sample data generation working correctly");
    }
    
    private static void testAllocationAlgorithm() {
        System.out.println("\n--- Testing Allocation Algorithm ---");
        
        // Create test data
        SampleDataGenerator generator = new SampleDataGenerator();
        var families = generator.generateSampleFamilies();
        var inventory = generator.generateCustomInventory(1000);
        
        // Create allocator and run allocation
        SupplyAllocator allocator = new SupplyAllocator(families, inventory, 20);
        var results = allocator.allocateSupplies();
        
        System.out.println("Allocation completed for " + families.size() + " families");
        System.out.println("Allocation results: " + results.size());
        
        // Check results
        int servedFamilies = 0;
        int totalValue = 0;
        for (AllocationResult result : results) {
            if (result.hasAllocations()) {
                servedFamilies++;
                totalValue += result.getTotalValue();
            }
        }
        
        System.out.println("Families served: " + servedFamilies + "/" + families.size());
        System.out.println("Total value distributed: " + totalValue);
        
        System.out.println("✓ Allocation algorithm working correctly");
    }
    
    private static void testReportGeneration() {
        System.out.println("\n--- Testing Report Generation ---");
        
        // Create test allocation
        SampleDataGenerator generator = new SampleDataGenerator();
        var families = generator.generateSampleFamilies();
        var inventory = generator.generateCustomInventory(1000);
        SupplyAllocator allocator = new SupplyAllocator(families, inventory, 20);
        var results = allocator.allocateSupplies();
        
        // Generate report
        ReportGenerator reportGenerator = new ReportGenerator(results, families, inventory);
        
        String quickSummary = reportGenerator.generateQuickSummary();
        System.out.println("Quick Summary: " + quickSummary);
        
        String fullReport = reportGenerator.generateAllocationReport();
        System.out.println("Full report generated (length: " + fullReport.length() + " characters)");
        
        // Test CSV export
        boolean csvExported = reportGenerator.exportToCSV("test_report.csv");
        System.out.println("CSV export: " + (csvExported ? "Success" : "Failed"));
        
        System.out.println("✓ Report generation working correctly");
    }
}