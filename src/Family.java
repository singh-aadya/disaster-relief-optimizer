/**
 * Represents a family in need of disaster relief supplies
 */
public class Family implements Comparable<Family> {
    private String familyId;
    private int size;
    private double distance; // km from relief center
    private int urgencyScore; // 1-10, 10 being most urgent
    private boolean isActive;
    private double priorityScore;
    
    public Family(String familyId, int size, double distance, int urgencyScore) {
        this.familyId = familyId;
        this.size = size;
        this.distance = distance;
        this.urgencyScore = Math.max(1, Math.min(10, urgencyScore)); // Clamp between 1-10
        this.isActive = true;
        calculatePriorityScore();
    }
    
    /**
     * Calculate priority score using weighted formula
     * Higher score = higher priority
     */
    private void calculatePriorityScore() {
        // Formula: (urgency * 0.5) + (size * 0.3) + (1/distance * 0.2)
        // Urgency is most important, then family size, then proximity
        double distanceWeight = distance > 0 ? (1.0 / distance) : 1.0;
        this.priorityScore = (urgencyScore * 0.5) + (size * 0.3) + (distanceWeight * 0.2);
    }
    
    // Getters
    public String getFamilyId() { return familyId; }
    public int getSize() { return size; }
    public double getDistance() { return distance; }
    public int getUrgencyScore() { return urgencyScore; }
    public boolean isActive() { return isActive; }
    public double getPriorityScore() { return priorityScore; }
    
    // Setters
    public void setSize(int size) { 
        this.size = size; 
        calculatePriorityScore();
    }
    
    public void setDistance(double distance) { 
        this.distance = distance; 
        calculatePriorityScore();
    }
    
    public void setUrgencyScore(int urgencyScore) { 
        this.urgencyScore = Math.max(1, Math.min(10, urgencyScore));
        calculatePriorityScore();
    }
    
    public void setActive(boolean active) { this.isActive = active; }
    
    @Override
    public int compareTo(Family other) {
        // Higher priority score comes first (descending order)
        return Double.compare(other.priorityScore, this.priorityScore);
    }
    
    @Override
    public String toString() {
        return String.format("Family[ID=%s, Size=%d, Distance=%.1fkm, Urgency=%d, Priority=%.2f, Active=%s]",
                familyId, size, distance, urgencyScore, priorityScore, isActive);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Family family = (Family) obj;
        return familyId.equals(family.familyId);
    }
    
    @Override
    public int hashCode() {
        return familyId.hashCode();
    }
}