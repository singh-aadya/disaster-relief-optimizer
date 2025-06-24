/**
 * Represents a supply item with weight and value properties
 */
public class Supply {
    private String name;
    private int weight; // weight per unit
    private int value;  // value/importance per unit
    private int quantity; // available quantity
    private String unit; // measurement unit (kg, liters, pieces)
    
    public Supply(String name, int weight, int value, int quantity, String unit) {
        this.name = name;
        this.weight = weight;
        this.value = value;
        this.quantity = quantity;
        this.unit = unit;
    }
    
    // Getters
    public String getName() { return name; }
    public int getWeight() { return weight; }
    public int getValue() { return value; }
    public int getQuantity() { return quantity; }
    public String getUnit() { return unit; }
    
    // Setters
    public void setQuantity(int quantity) { 
        this.quantity = Math.max(0, quantity); 
    }
    
    public void setWeight(int weight) { this.weight = weight; }
    public void setValue(int value) { this.value = value; }
    
    /**
     * Reduce quantity by specified amount
     * @param amount Amount to reduce
     * @return True if successful, false if insufficient quantity
     */
    public boolean reduceQuantity(int amount) {
        if (amount <= quantity) {
            quantity -= amount;
            return true;
        }
        return false;
    }
    
    /**
     * Add quantity
     * @param amount Amount to add
     */
    public void addQuantity(int amount) {
        if (amount > 0) {
            quantity += amount;
        }
    }
    
    /**
     * Check if supply is available
     * @return true if quantity > 0
     */
    public boolean isAvailable() {
        return quantity > 0;
    }
    
    /**
     * Get value-to-weight ratio for optimization
     * @return ratio or 0 if weight is 0
     */
    public double getValueWeightRatio() {
        return weight > 0 ? (double) value / weight : 0;
    }
    
    @Override
    public String toString() {
        return String.format("Supply[%s: %d%s, Weight=%d, Value=%d, Available=%s]",
                name, quantity, unit, weight, value, isAvailable());
    }
    
    /**
     * Create a copy of this supply with specified quantity
     */
    public Supply createCopy(int newQuantity) {
        return new Supply(name, weight, value, newQuantity, unit);
    }
}