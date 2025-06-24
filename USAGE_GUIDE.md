# SupplyMate Usage Guide

## Getting Started

### First Launch
1. Compile and run the application using `compile_and_run.bat`
2. You'll see the main menu with 9 options
3. Start by loading sample data (Option 1) to get familiar with the system

### Quick Start Workflow
1. **Load Sample Data** → Load Sample Families
2. **Run Allocation** → Execute the allocation algorithm
3. **View Reports** → See detailed allocation results
4. **Export Reports** → Save results to CSV file

## Main Menu Options

### 1. Load Sample Data
**Purpose**: Populate the system with test data

**Sub-options**:
- **Load Sample Families**: 10 predefined families with varied characteristics
- **Load Random Families**: Generate 1-50 random families for testing
- **Reset Inventory**: Restore inventory to default quantities

**When to use**: First time setup, testing different scenarios

### 2. Manage Families
**Purpose**: Add, remove, or modify family information

**Sub-options**:
- **View All Families**: Display all registered families sorted by priority
- **Add Family**: Register a new family needing supplies
- **Remove Family**: Delete a family from the system
- **Update Family Status**: Mark families as active/inactive

**Family Information Required**:
- **Family ID**: Unique identifier (e.g., "FAM001")
- **Family Size**: Number of people (1-20)
- **Distance**: Distance from relief center in km (0.1-100.0)
- **Urgency Score**: Medical/emergency urgency (1-10, where 10 is critical)

### 3. Manage Inventory
**Purpose**: Control available supplies

**Sub-options**:
- **View Inventory**: See current stock levels and capacity usage
- **Add Supplies**: Increase quantities of existing supply types
- **Remove Supplies**: Decrease quantities (useful for testing scarcity)
- **Reset Inventory**: Restore to default quantities

**Default Supply Types**:
- **Food Ration**: 2kg weight, 8 value, 100 packs
- **Water Bottle**: 1kg weight, 10 value, 150 bottles
- **Medicine Kit**: 1kg weight, 15 value, 50 kits
- **Blanket**: 3kg weight, 6 value, 75 pieces
- **First Aid**: 1kg weight, 12 value, 40 kits

### 4. Run Allocation
**Purpose**: Execute the main allocation algorithm

**Process**:
1. Validates that families and supplies are available
2. Sorts families by priority score
3. Allocates supplies using Greedy + Knapsack algorithms
4. Updates inventory quantities
5. Displays quick summary

**Priority Factors** (automatically calculated):
- **Urgency** (50% weight): Medical emergency level
- **Family Size** (30% weight): Number of people to serve
- **Distance** (20% weight): Proximity to relief center

### 5. View Reports
**Purpose**: Display comprehensive allocation results

**Report Sections**:
- **Allocation Summary**: Statistics on families served, total value distributed
- **Family Allocations**: Detailed breakdown per family
- **Remaining Inventory**: Current stock levels after allocation
- **Supply Usage Chart**: ASCII visualization of supply consumption

### 6. Export Reports
**Purpose**: Save allocation results to CSV file

**Features**:
- Customizable filename
- Comprehensive data export including family details and allocations
- Compatible with Excel and other spreadsheet applications

**CSV Columns**:
- Family_ID, Family_Size, Distance, Urgency, Priority_Score
- Supplies_Allocated, Total_Value, Total_Weight, Allocation_Score

### 7. Test Scenarios
**Purpose**: Run predefined test cases

**Available Scenarios**:
- **Balanced Scenario**: Standard disaster relief situation
- **High Demand Scenario**: 25 families with limited supplies
- **Emergency Scenario**: High-urgency families requiring immediate aid

**Benefits**: 
- Compare algorithm performance under different conditions
- Validate system behavior in extreme situations
- Demonstrate scalability

### 8. System Status
**Purpose**: Quick overview of current system state

**Information Displayed**:
- Number of registered and active families
- Available allocation results
- Inventory capacity usage
- Supply type availability

### 9. Exit
**Purpose**: Safely close the application

## Usage Examples

### Example 1: Basic Allocation
```
1. Load Sample Data → Load Sample Families
2. Run Allocation
3. View Reports
```

**Expected Result**: 10 families processed, most high-priority families receive supplies

### Example 2: Custom Family Setup
```
1. Manage Families → Add Family
   - Family ID: "CUSTOM001"
   - Size: 6
   - Distance: 3.5
   - Urgency: 9
2. Run Allocation
3. View Reports
```

**Expected Result**: High-priority family receives priority allocation

### Example 3: Scarcity Testing
```
1. Load Sample Data → Load Sample Families
2. Manage Inventory → Remove Supplies (reduce to very low quantities)
3. Run Allocation
4. View Reports
```

**Expected Result**: Only highest priority families receive supplies

### Example 4: Stress Testing
```
1. Load Sample Data → Load Random Families (50 families)
2. Run Allocation
3. Export Reports → Save results
```

**Expected Result**: Large-scale allocation with performance metrics

## Understanding Priority Scores

### Priority Calculation
The system automatically calculates priority using this formula:
```
Priority = (Urgency × 0.5) + (Family_Size × 0.3) + (1/Distance × 0.2)
```

### Example Priority Calculations:
- **High Priority**: Size=7, Distance=1km, Urgency=10 → Priority ≈ 7.3
- **Medium Priority**: Size=4, Distance=5km, Urgency=6 → Priority ≈ 4.24
- **Low Priority**: Size=2, Distance=15km, Urgency=3 → Priority ≈ 2.11

### Allocation Strategy:
1. Families sorted by priority (highest first)
2. Each family allocated based on:
   - Available supplies
   - Family-specific capacity
   - Supply type priorities (medicine/water for urgent cases)

## Understanding Supply Allocation

### Capacity Calculation:
Each family gets capacity based on:
```
Capacity = 20 + (Family_Size × 2) + (Priority_Score/10 × 5)
```

### Supply Type Priorities:
- **High Urgency Families** (≥8): Medicine and water prioritized
- **Large Families**: More food and blankets
- **All Families**: Balanced allocation based on available capacity

### Allocation Rules:
- **Water**: 1 bottle per person minimum
- **Food**: 1 ration per 2 people
- **Medicine**: 2 kits for high urgency, 1 otherwise
- **Blankets**: 1 per 3 people
- **First Aid**: Based on available capacity

## Tips for Effective Usage

### Best Practices:
1. **Start with sample data** to understand the system
2. **Run allocation after any changes** to see updated results
3. **Use test scenarios** to validate different conditions
4. **Export reports** for external analysis
5. **Check system status** regularly to monitor capacity

### Common Workflows:
- **Daily Operations**: View families → Run allocation → Generate reports
- **Data Management**: Add/remove families → Update inventory → Reallocate
- **Analysis**: Run test scenarios → Export results → Compare outcomes

### Troubleshooting:
- **No allocation results**: Check if families are active and supplies available
- **Unexpected allocations**: Verify family priority scores and urgency levels
- **Export failures**: Ensure sufficient disk space and write permissions

## Advanced Features

### Reallocation:
- System automatically handles inventory changes
- Adding/removing families triggers rebalancing
- Status changes (active/inactive) update allocations

### Dynamic Adjustments:
- Modify family urgency scores as situations change
- Update inventory as new supplies arrive
- Deactivate families when they're no longer in need

### Reporting Features:
- ASCII charts show supply usage visually
- CSV exports enable further analysis in Excel
- Comprehensive statistics for decision-making