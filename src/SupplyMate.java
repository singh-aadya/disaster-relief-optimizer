import java.util.*;

/**
 * Main application class for SupplyMate - Disaster Relief Distribution Optimizer
 */
public class SupplyMate {
    private SupplyAllocator allocator;
    private Scanner scanner;
    private SampleDataGenerator dataGenerator;
    private boolean isRunning;
    
    public SupplyMate() {
        this.allocator = new SupplyAllocator(20); // Base capacity per family
        this.scanner = new Scanner(System.in);
        this.dataGenerator = new SampleDataGenerator();
        this.isRunning = true;
    }
    
    /**
     * Main application entry point
     */
    public static void main(String[] args) {
        System.out.println("==============================================");
        System.out.println("    WELCOME TO SUPPLYMATE                   ");
        System.out.println("    Disaster Relief Distribution Optimizer   ");
        System.out.println("==============================================");
        System.out.println();
        
        SupplyMate app = new SupplyMate();
        app.run();
    }
    
    /**
     * Main application loop
     */
    public void run() {
        while (isRunning) {
            displayMainMenu();
            int choice = getValidChoice(1, 9);
            processMainMenuChoice(choice);
        }
        
        System.out.println("Thank you for using SupplyMate!");
        scanner.close();
    }
    
    /**
     * Display main menu options
     */
    private void displayMainMenu() {
        System.out.println("\n=== SUPPLYMATE MAIN MENU ===");
        System.out.println("1. Load Sample Data");
        System.out.println("2. Manage Families");
        System.out.println("3. Manage Inventory");
        System.out.println("4. Run Allocation");
        System.out.println("5. View Reports");
        System.out.println("6. Export Reports");
        System.out.println("7. Test Scenarios");
        System.out.println("8. System Status");
        System.out.println("9. Exit");
        System.out.print("Enter your choice (1-9): ");
    }
    
    /**
     * Process main menu choice
     */
    private void processMainMenuChoice(int choice) {
        switch (choice) {
            case 1:
                loadSampleDataMenu();
                break;
            case 2:
                manageFamiliesMenu();
                break;
            case 3:
                manageInventoryMenu();
                break;
            case 4:
                runAllocation();
                break;
            case 5:
                viewReports();
                break;
            case 6:
                exportReports();
                break;
            case 7:
                testScenariosMenu();
                break;
            case 8:
                displaySystemStatus();
                break;
            case 9:
                isRunning = false;
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
        }
    }
    
    /**
     * Load sample data menu
     */
    private void loadSampleDataMenu() {
        System.out.println("\n=== LOAD SAMPLE DATA ===");
        System.out.println("1. Load Sample Families");
        System.out.println("2. Load Random Families");
        System.out.println("3. Reset Inventory");
        System.out.println("4. Back to Main Menu");
        System.out.print("Enter your choice (1-4): ");
        
        int choice = getValidChoice(1, 4);
        
        switch (choice) {
            case 1:
                loadSampleFamilies();
                break;
            case 2:
                loadRandomFamilies();
                break;
            case 3:
                resetInventory();
                break;
            case 4:
                return;
        }
    }
    
    /**
     * Load sample families
     */
    private void loadSampleFamilies() {
        List<Family> sampleFamilies = dataGenerator.generateSampleFamilies();
        allocator = new SupplyAllocator(sampleFamilies, allocator.getInventory(), 20);
        System.out.printf("Loaded %d sample families.\n", sampleFamilies.size());
        
        // Display loaded families
        System.out.println("\nLoaded Families:");
        for (Family family : sampleFamilies) {
            System.out.println("  " + family);
        }
    }
    
    /**
     * Load random families
     */
    private void loadRandomFamilies() {
        System.out.print("Enter number of random families to generate (1-50): ");
        int count = getValidChoice(1, 50);
        
        List<Family> randomFamilies = dataGenerator.generateRandomFamilies(count);
        allocator = new SupplyAllocator(randomFamilies, allocator.getInventory(), 20);
        System.out.printf("Generated and loaded %d random families.\n", count);
    }
    
    /**
     * Reset inventory to default state
     */
    private void resetInventory() {
        allocator.getInventory().reset();
        System.out.println("Inventory has been reset to default state.");
        System.out.println(allocator.getInventory().getInventorySummary());
    }
    
    /**
     * Manage families menu
     */
    private void manageFamiliesMenu() {
        System.out.println("\n=== MANAGE FAMILIES ===");
        System.out.println("1. View All Families");
        System.out.println("2. Add Family");
        System.out.println("3. Remove Family");
        System.out.println("4. Update Family Status");
        System.out.println("5. Back to Main Menu");
        System.out.print("Enter your choice (1-5): ");
        
        int choice = getValidChoice(1, 5);
        
        switch (choice) {
            case 1:
                viewAllFamilies();
                break;
            case 2:
                addFamily();
                break;
            case 3:
                removeFamily();
                break;
            case 4:
                updateFamilyStatus();
                break;
            case 5:
                return;
        }
    }
    
    /**
     * View all families
     */
    private void viewAllFamilies() {
        List<Family> families = allocator.getFamilies();
        
        if (families.isEmpty()) {
            System.out.println("No families registered.");
            return;
        }
        
        System.out.println("\n=== REGISTERED FAMILIES ===");
        families.sort(Collections.reverseOrder()); // Sort by priority
        
        for (int i = 0; i < families.size(); i++) {
            Family family = families.get(i);
            String status = family.isActive() ? "ACTIVE" : "INACTIVE";
            System.out.printf("%d. %s [%s]\n", i + 1, family, status);
        }
    }
    
    /**
     * Add new family
     */
    private void addFamily() {
        System.out.println("\n=== ADD FAMILY ===");
        
        System.out.print("Enter Family ID: ");
        String familyId = scanner.nextLine().trim();
        
        if (familyId.isEmpty()) {
            System.out.println("Family ID cannot be empty.");
            return;
        }
        
        // Check if family already exists
        if (allocator.getFamilies().stream().anyMatch(f -> f.getFamilyId().equals(familyId))) {
            System.out.println("Family with this ID already exists.");
            return;
        }
        
        System.out.print("Enter family size (1-20): ");
        int size = getValidChoice(1, 20);
        
        System.out.print("Enter distance from relief center (km): ");
        double distance = getValidDouble(0.1, 100.0);
        
        System.out.print("Enter urgency score (1-10): ");
        int urgency = getValidChoice(1, 10);
        
        Family newFamily = new Family(familyId, size, distance, urgency);
        allocator.addFamily(newFamily);
        
        System.out.printf("Family %s added successfully.\n", familyId);
        System.out.println("Family details: " + newFamily);
    }
    
    /**
     * Remove family
     */
    private void removeFamily() {
        if (allocator.getFamilies().isEmpty()) {
            System.out.println("No families to remove.");
            return;
        }
        
        System.out.print("Enter Family ID to remove: ");
        String familyId = scanner.nextLine().trim();
        
        if (allocator.removeFamily(familyId)) {
            System.out.printf("Family %s removed successfully.\n", familyId);
        } else {
            System.out.printf("Family %s not found.\n", familyId);
        }
    }
    
    /**
     * Update family status (active/inactive)
     */
    private void updateFamilyStatus() {
        if (allocator.getFamilies().isEmpty()) {
            System.out.println("No families to update.");
            return;
        }
        
        System.out.print("Enter Family ID: ");
        String familyId = scanner.nextLine().trim();
        
        System.out.println("1. Set Active");
        System.out.println("2. Set Inactive");
        System.out.print("Enter choice (1-2): ");
        
        int choice = getValidChoice(1, 2);
        boolean active = (choice == 1);
        
        allocator.updateFamilyStatus(familyId, active);
        System.out.printf("Family %s status updated to %s.\n", familyId, active ? "ACTIVE" : "INACTIVE");
    }
    
    /**
     * Manage inventory menu
     */
    private void manageInventoryMenu() {
        System.out.println("\n=== MANAGE INVENTORY ===");
        System.out.println("1. View Inventory");
        System.out.println("2. Add Supplies");
        System.out.println("3. Remove Supplies");
        System.out.println("4. Reset Inventory");
        System.out.println("5. Back to Main Menu");
        System.out.print("Enter your choice (1-5): ");
        
        int choice = getValidChoice(1, 5);
        
        switch (choice) {
            case 1:
                viewInventory();
                break;
            case 2:
                addSupplies();
                break;
            case 3:
                removeSupplies();
                break;
            case 4:
                resetInventory();
                break;
            case 5:
                return;
        }
    }
    
    /**
     * View current inventory
     */
    private void viewInventory() {
        System.out.println(allocator.getInventory().getInventorySummary());
    }
    
    /**
     * Add supplies to inventory
     */
    private void addSupplies() {
        System.out.println("\n=== ADD SUPPLIES ===");
        
        // Show available supply types
        List<Supply> allSupplies = allocator.getInventory().getAllSupplies();
        System.out.println("Available supply types:");
        for (int i = 0; i < allSupplies.size(); i++) {
            System.out.printf("%d. %s\n", i + 1, allSupplies.get(i).getName());
        }
        
        System.out.print("Select supply type (1-" + allSupplies.size() + "): ");
        int choice = getValidChoice(1, allSupplies.size());
        
        Supply selectedSupply = allSupplies.get(choice - 1);
        
        System.out.printf("Enter quantity to add for %s: ", selectedSupply.getName());
        int quantity = getValidChoice(1, 1000);
        
        selectedSupply.addQuantity(quantity);
        System.out.printf("Added %d units of %s to inventory.\n", quantity, selectedSupply.getName());
    }
    
    /**
     * Remove supplies from inventory
     */
    private void removeSupplies() {
        System.out.println("\n=== REMOVE SUPPLIES ===");
        
        List<Supply> availableSupplies = allocator.getInventory().getAvailableSupplies();
        if (availableSupplies.isEmpty()) {
            System.out.println("No supplies available to remove.");
            return;
        }
        
        System.out.println("Available supplies:");
        for (int i = 0; i < availableSupplies.size(); i++) {
            Supply supply = availableSupplies.get(i);
            System.out.printf("%d. %s (Available: %d)\n", i + 1, supply.getName(), supply.getQuantity());
        }
        
        System.out.print("Select supply to remove (1-" + availableSupplies.size() + "): ");
        int choice = getValidChoice(1, availableSupplies.size());
        
        Supply selectedSupply = availableSupplies.get(choice - 1);
        
        System.out.printf("Enter quantity to remove (max %d): ", selectedSupply.getQuantity());
        int quantity = getValidChoice(1, selectedSupply.getQuantity());
        
        if (allocator.getInventory().removeSupply(selectedSupply.getName(), quantity)) {
            System.out.printf("Removed %d units of %s from inventory.\n", quantity, selectedSupply.getName());
        } else {
            System.out.println("Failed to remove supplies.");
        }
    }
    
    /**
     * Run allocation process
     */
    private void runAllocation() {
        System.out.println("\n=== RUNNING ALLOCATION ===");
        
        if (allocator.getFamilies().isEmpty()) {
            System.out.println("No families registered. Please add families first.");
            return;
        }
        
        if (!allocator.getInventory().hasSupplies()) {
            System.out.println("No supplies available. Please add supplies first.");
            return;
        }
        
        System.out.println("Starting allocation process...\n");
        
        List<AllocationResult> results = allocator.allocateSupplies();
        
        System.out.println("\nAllocation completed!");
        
        // Generate quick summary
        ReportGenerator reportGenerator = new ReportGenerator(results, allocator.getFamilies(), allocator.getInventory());
        System.out.println(reportGenerator.generateQuickSummary());
        
        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
    }
    
    /**
     * View reports
     */
    private void viewReports() {
        if (allocator.getAllocationResults().isEmpty()) {
            System.out.println("No allocation results available. Please run allocation first.");
            return;
        }
        
        ReportGenerator reportGenerator = new ReportGenerator(
                allocator.getAllocationResults(), 
                allocator.getFamilies(), 
                allocator.getInventory()
        );
        
        System.out.println(reportGenerator.generateAllocationReport());
        
        System.out.println("Press Enter to continue...");
        scanner.nextLine();
    }
    
    /**
     * Export reports
     */
    private void exportReports() {
        if (allocator.getAllocationResults().isEmpty()) {
            System.out.println("No allocation results available. Please run allocation first.");
            return;
        }
        
        System.out.print("Enter filename for CSV export (without extension): ");
        String filename = scanner.nextLine().trim();
        
        if (filename.isEmpty()) {
            filename = "allocation_report";
        }
        
        filename += ".csv";
        
        ReportGenerator reportGenerator = new ReportGenerator(
                allocator.getAllocationResults(), 
                allocator.getFamilies(), 
                allocator.getInventory()
        );
        
        if (reportGenerator.exportToCSV(filename)) {
            System.out.println("Report exported successfully!");
        } else {
            System.out.println("Failed to export report.");
        }
    }
    
    /**
     * Test scenarios menu
     */
    private void testScenariosMenu() {
        System.out.println("\n=== TEST SCENARIOS ===");
        
        List<SampleDataGenerator.TestScenario> scenarios = dataGenerator.getAllTestScenarios();
        
        for (int i = 0; i < scenarios.size(); i++) {
            System.out.printf("%d. %s\n", i + 1, scenarios.get(i).getSummary());
        }
        System.out.printf("%d. Back to Main Menu\n", scenarios.size() + 1);
        
        System.out.print("Select scenario (1-" + (scenarios.size() + 1) + "): ");
        int choice = getValidChoice(1, scenarios.size() + 1);
        
        if (choice == scenarios.size() + 1) {
            return;
        }
        
        SampleDataGenerator.TestScenario scenario = scenarios.get(choice - 1);
        runTestScenario(scenario);
    }
    
    /**
     * Run a specific test scenario
     */
    private void runTestScenario(SampleDataGenerator.TestScenario scenario) {
        System.out.printf("\n=== RUNNING SCENARIO: %s ===\n", scenario.getName());
        System.out.println(scenario.getDescription());
        
        // Set up scenario
        allocator = new SupplyAllocator(scenario.getFamilies(), scenario.getInventory(), 20);
        
        System.out.printf("Scenario loaded: %d families, %d supply types\n", 
                scenario.getFamilies().size(), scenario.getInventory().getAllSupplies().size());
        
        // Run allocation
        List<AllocationResult> results = allocator.allocateSupplies();
        
        // Generate and display report
        ReportGenerator reportGenerator = new ReportGenerator(results, scenario.getFamilies(), scenario.getInventory());
        System.out.println(reportGenerator.generateQuickSummary());
        
        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
    }
    
    /**
     * Display system status
     */
    private void displaySystemStatus() {
        System.out.println("\n=== SYSTEM STATUS ===");
        System.out.printf("Registered Families: %d\n", allocator.getFamilies().size());
        System.out.printf("Active Families: %d\n", allocator.getFamilies().stream().mapToInt(f -> f.isActive() ? 1 : 0).sum());
        System.out.printf("Allocation Results: %d\n", allocator.getAllocationResults().size());
        System.out.printf("Inventory Capacity: %d/%d\n", 
                allocator.getInventory().getCurrentWeight(), allocator.getInventory().getMaxCapacity());
        System.out.printf("Available Supply Types: %d\n", allocator.getInventory().getAvailableSupplies().size());
        
        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
    }
    
    /**
     * Get valid integer choice within range
     */
    private int getValidChoice(int min, int max) {
        while (true) {
            try {
                String input = scanner.nextLine().trim();
                int choice = Integer.parseInt(input);
                if (choice >= min && choice <= max) {
                    return choice;
                }
                System.out.printf("Please enter a number between %d and %d: ", min, max);
            } catch (NumberFormatException e) {
                System.out.printf("Invalid input. Please enter a number between %d and %d: ", min, max);
            }
        }
    }
    
    /**
     * Get valid double within range
     */
    private double getValidDouble(double min, double max) {
        while (true) {
            try {
                String input = scanner.nextLine().trim();
                double value = Double.parseDouble(input);
                if (value >= min && value <= max) {
                    return value;
                }
                System.out.printf("Please enter a number between %.1f and %.1f: ", min, max);
            } catch (NumberFormatException e) {
                System.out.printf("Invalid input. Please enter a number between %.1f and %.1f: ", min, max);
            }
        }
    }
}