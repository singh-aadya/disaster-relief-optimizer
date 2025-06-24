# SupplyMate Technical Documentation

## System Architecture

### Core Components

#### 1. Entity Classes
- **Family.java**: Represents families needing relief supplies
  - Attributes: familyId, size, distance, urgencyScore, priorityScore
  - Implements Comparable for priority-based sorting
  - Auto-calculates priority using weighted formula

- **Supply.java**: Represents individual supply items
  - Attributes: name, weight, value, quantity, unit
  - Provides value-to-weight ratio calculation
  - Supports quantity management operations

- **Inventory.java**: Manages collection of supplies
  - Tracks available supplies with capacity constraints
  - Provides CRUD operations for supply management
  - Calculates current weight and remaining capacity

#### 2. Algorithm Classes
- **SupplyAllocator.java**: Core allocation engine
  - Implements Greedy + 0/1 Knapsack hybrid algorithm
  - Handles priority-based family sorting
  - Manages reallocation and rebalancing

- **AllocationResult.java**: Stores allocation outcomes
  - Tracks supplies allocated to each family
  - Calculates total value, weight, and efficiency scores
  - Provides CSV export formatting

#### 3. Utility Classes
- **ReportGenerator.java**: Creates comprehensive reports
  - Generates allocation summaries and statistics
  - Creates ASCII-based supply usage charts
  - Exports data to CSV format

- **SampleDataGenerator.java**: Provides test data
  - Generates sample families with varied characteristics
  - Creates different test scenarios (balanced, high-demand, emergency)
  - Supports random data generation for stress testing

#### 4. Main Application
- **SupplyMate.java**: Console-based user interface
  - Interactive menu system
  - Input validation and error handling
  - Orchestrates all system components

## Algorithm Implementation

### Priority Calculation
Family priority is calculated using a weighted formula:
```
Priority = (Urgency × 0.5) + (Family_Size × 0.3) + (1/Distance × 0.2)
```

Where:
- Urgency: 1-10 scale (10 = most urgent)
- Family_Size: Number of people in family
- Distance: Distance from relief center (km)

### Greedy Sorting Algorithm
1. Calculate priority scores for all families
2. Sort families in descending order of priority
3. Filter out inactive families
4. Process families in priority order

### Modified 0/1 Knapsack Algorithm
For each family:
1. Calculate family-specific capacity based on size and priority
2. Sort available supplies by value-to-weight ratio
3. Apply priority allocation for high-urgency families (medicine, water first)
4. Use standard knapsack approach for remaining capacity
5. Calculate optimal units based on family characteristics

### Capacity Calculation
Family capacity is determined by:
```
Capacity = Base_Capacity + (Family_Size × 2) + (Priority_Multiplier × 5)
```

### Supply-Specific Allocation Rules
- **Water**: 1 bottle per person minimum
- **Food**: 1 ration per 2 people
- **Medicine**: 2 kits for high urgency (≥7), 1 kit otherwise
- **Blankets**: 1 blanket per 3 people
- **First Aid**: Standard allocation based on capacity

## Data Structures Used

### Collections
- **ArrayList**: Dynamic family and supply lists
- **HashMap**: Supply name-to-object mapping, allocation tracking
- **TreeSet**: Automatic sorting of families by priority

### Algorithms
- **Sorting**: Collections.sort() with custom Comparator
- **Searching**: Stream API with filters
- **Optimization**: Greedy + Dynamic Programming hybrid

## Input Validation

### Family Data Validation
- Family ID: Non-empty string, unique identifier
- Size: Integer between 1-20 people
- Distance: Double between 0.1-100.0 km
- Urgency: Integer between 1-10 (auto-clamped)

### Inventory Validation
- Quantity: Non-negative integers
- Weight/Value: Positive integers
- Capacity: Maximum weight constraints enforced

### User Input Validation
- Menu choices: Integer validation with range checking
- Numeric inputs: Exception handling with retry logic
- String inputs: Null and empty string checks

## Error Handling

### Exception Types Handled
- **NumberFormatException**: Invalid numeric inputs
- **IndexOutOfBoundsException**: Array/list access errors
- **NullPointerException**: Null object references
- **IOException**: File operations (CSV export)

### Edge Cases Covered
- Zero inventory scenarios
- No active families
- Equal priority families (stable sorting)
- Capacity overflow situations
- Empty allocation results

## Performance Characteristics

### Time Complexity
- Family sorting: O(n log n) where n = number of families
- Supply allocation: O(f × s) where f = families, s = supplies
- Report generation: O(n) linear with data size

### Space Complexity
- Family storage: O(n) for n families
- Inventory storage: O(s) for s supply types
- Allocation results: O(f × s) worst case

### Memory Usage
- Typical scenario (10 families, 5 supplies): ~1MB
- Large scenario (100 families, 10 supplies): ~10MB
- Memory efficient with object reuse patterns

## Testing Strategy

### Unit Testing Areas
- Priority calculation accuracy
- Allocation algorithm correctness
- Input validation robustness
- Report generation completeness

### Integration Testing
- End-to-end allocation workflow
- Data persistence and retrieval
- User interface interaction flows

### Test Scenarios Included
1. **Balanced Scenario**: Normal distribution of family needs
2. **High Demand**: Many families, limited supplies
3. **Emergency**: High urgency families requiring immediate aid

### Performance Testing
- Stress testing with 100+ families
- Memory usage monitoring
- Response time measurement

## Configuration Options

### Customizable Parameters
- Base capacity per family (default: 20)
- Maximum inventory capacity (default: 1000)
- Priority weight factors (urgency: 0.5, size: 0.3, distance: 0.2)
- Default supply quantities and characteristics

### Extensibility Points
- New supply types can be added via Inventory class
- Priority calculation formula can be modified in Family class
- Additional allocation strategies can be implemented in SupplyAllocator
- New report formats can be added to ReportGenerator

## Security Considerations

### Input Sanitization
- All user inputs are validated and sanitized
- Range checking prevents buffer overflows
- String inputs are trimmed and checked for malicious content

### File Operations
- CSV export uses secure file writing
- File paths are validated
- No external file execution

### Data Integrity
- Immutable data where appropriate
- Defensive copying for collections
- Validation at all data entry points

## Future Enhancements

### Potential Improvements
1. **GUI Interface**: JavaFX or Swing-based desktop application
2. **Database Integration**: Persistent storage for families and inventory
3. **Multi-threaded Processing**: Parallel allocation for large datasets
4. **Machine Learning**: Predictive allocation based on historical data
5. **Geographic Optimization**: Route optimization for supply delivery
6. **Real-time Updates**: Live inventory and family status updates
7. **Mobile App**: Android/iOS companion application
8. **Web Interface**: Browser-based management system