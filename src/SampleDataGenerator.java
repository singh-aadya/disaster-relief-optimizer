import java.util.*;

/**
 * Generates sample data for testing the SupplyMate application
 */
public class SampleDataGenerator {
    private Random random;
    
    public SampleDataGenerator() {
        this.random = new Random();
    }
    
    /**
     * Generate sample families for testing
     */
    public List<Family> generateSampleFamilies() {
        List<Family> families = new ArrayList<>();
        
        // Predefined sample families with varying characteristics
        families.add(new Family("FAM001", 5, 2.5, 9)); // Large, close, very urgent
        families.add(new Family("FAM002", 3, 8.2, 7)); // Medium, far, urgent
        families.add(new Family("FAM003", 2, 1.0, 10)); // Small, very close, critical
        families.add(new Family("FAM004", 7, 15.5, 5)); // Large, very far, moderate
        families.add(new Family("FAM005", 1, 3.8, 8)); // Single person, close, urgent
        families.add(new Family("FAM006", 4, 6.1, 6)); // Medium, medium distance, moderate
        families.add(new Family("FAM007", 2, 12.0, 9)); // Small, far, very urgent
        families.add(new Family("FAM008", 6, 4.2, 4)); // Large, close, low urgency
        families.add(new Family("FAM009", 3, 9.8, 7)); // Medium, far, urgent
        families.add(new Family("FAM010", 1, 0.5, 10)); // Single, very close, critical
        
        return families;
    }
    
    /**
     * Generate random families for stress testing
     */
    public List<Family> generateRandomFamilies(int count) {
        List<Family> families = new ArrayList<>();
        
        for (int i = 1; i <= count; i++) {
            String familyId = String.format("RND%03d", i);
            int size = random.nextInt(8) + 1; // 1-8 people
            double distance = random.nextDouble() * 20; // 0-20 km
            int urgency = random.nextInt(10) + 1; // 1-10
            
            families.add(new Family(familyId, size, Math.round(distance * 10.0) / 10.0, urgency));
        }
        
        return families;
    }
    
    /**
     * Generate a custom inventory with specified quantities
     */
    public Inventory generateCustomInventory(int capacity) {
        Inventory inventory = new Inventory(capacity);
        return inventory; // Uses default supplies from Inventory constructor
    }
    
    /**
     * Generate low-stock inventory for testing scarcity scenarios
     */
    public Inventory generateLowStockInventory() {
        Inventory inventory = new Inventory(200);
        
        // Clear default supplies and add limited quantities
        inventory.getAllSupplies().forEach(supply -> supply.setQuantity(0));
        
        inventory.addSupply(new Supply("Food Ration", 2, 8, 15, "packs"));
        inventory.addSupply(new Supply("Water Bottle", 1, 10, 25, "bottles"));
        inventory.addSupply(new Supply("Medicine Kit", 1, 15, 8, "kits"));
        inventory.addSupply(new Supply("Blanket", 3, 6, 12, "pieces"));
        inventory.addSupply(new Supply("First Aid", 1, 12, 5, "kits"));
        
        return inventory;
    }
    
    /**
     * Generate high-demand scenario: many families, limited supplies
     */
    public TestScenario generateHighDemandScenario() {
        List<Family> families = generateRandomFamilies(25);
        Inventory inventory = generateLowStockInventory();
        
        return new TestScenario("High Demand Scenario", 
                "25 families competing for limited supplies", families, inventory);
    }
    
    /**
     * Generate emergency scenario: high urgency families
     */
    public TestScenario generateEmergencyScenario() {
        List<Family> families = new ArrayList<>();
        
        // Create families with high urgency scores
        for (int i = 1; i <= 10; i++) {
            String id = String.format("EMG%03d", i);
            int size = random.nextInt(6) + 2; // 2-7 people
            double distance = random.nextDouble() * 10; // 0-10 km
            int urgency = random.nextInt(3) + 8; // 8-10 urgency
            
            families.add(new Family(id, size, Math.round(distance * 10.0) / 10.0, urgency));
        }
        
        Inventory inventory = generateCustomInventory(800);
        
        return new TestScenario("Emergency Scenario", 
                "High urgency families requiring immediate assistance", families, inventory);
    }
    
    /**
     * Generate balanced scenario: normal distribution of needs
     */
    public TestScenario generateBalancedScenario() {
        List<Family> families = generateSampleFamilies();
        Inventory inventory = generateCustomInventory(1000);
        
        return new TestScenario("Balanced Scenario", 
                "Standard disaster relief scenario with varied family needs", families, inventory);
    }
    
    /**
     * Test scenario container class
     */
    public static class TestScenario {
        private String name;
        private String description;
        private List<Family> families;
        private Inventory inventory;
        
        public TestScenario(String name, String description, List<Family> families, Inventory inventory) {
            this.name = name;
            this.description = description;
            this.families = families;
            this.inventory = inventory;
        }
        
        // Getters
        public String getName() { return name; }
        public String getDescription() { return description; }
        public List<Family> getFamilies() { return families; }
        public Inventory getInventory() { return inventory; }
        
        public String getSummary() {
            return String.format("%s: %s (%d families, %d supply types)", 
                    name, description, families.size(), inventory.getAllSupplies().size());
        }
    }
    
    /**
     * Get all predefined test scenarios
     */
    public List<TestScenario> getAllTestScenarios() {
        List<TestScenario> scenarios = new ArrayList<>();
        scenarios.add(generateBalancedScenario());
        scenarios.add(generateHighDemandScenario());
        scenarios.add(generateEmergencyScenario());
        
        return scenarios;
    }
    
    /**
     * Print sample data information
     */
    public void printSampleDataInfo() {
        System.out.println("=== SAMPLE DATA INFORMATION ===");
        System.out.println("Available test scenarios:");
        
        List<TestScenario> scenarios = getAllTestScenarios();
        for (int i = 0; i < scenarios.size(); i++) {
            System.out.printf("%d. %s\n", i + 1, scenarios.get(i).getSummary());
        }
        
        System.out.println("\nSample Families:");
        List<Family> sampleFamilies = generateSampleFamilies();
        for (Family family : sampleFamilies) {
            System.out.println("  " + family);
        }
        
        System.out.println("\nDefault Inventory:");
        Inventory sampleInventory = generateCustomInventory(1000);
        System.out.println(sampleInventory.getInventorySummary());
    }
}