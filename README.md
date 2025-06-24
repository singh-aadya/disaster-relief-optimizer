# SupplyMate - Disaster Relief Distribution Optimizer

A Java console-based application that simulates optimized disaster relief distribution using Data Structures and Algorithms (DSA).

## Features

- **Smart Allocation**: Uses Greedy algorithms for priority-based sorting and 0/1 Knapsack for optimal resource allocation
- **Priority Factors**: Considers family size, distance from relief center, and medical urgency
- **Dynamic Reallocation**: Automatically recalculates allocations when inventory or family data changes
- **Comprehensive Reporting**: Generates detailed allocation reports with CSV export capability
- **Visual Analytics**: ASCII-based charts showing supply usage
- **Robust Error Handling**: Handles edge cases like zero inventory, equal urgency, and overflow demands

## Core Components

- **Family Management**: Track families with unique IDs, size, location, and urgency scores
- **Inventory System**: Manage food, water, and medicine supplies with weight and value constraints
- **Allocation Engine**: Greedy-based prioritization with Knapsack optimization
- **Reporting System**: Generate detailed reports and export to CSV

## Getting Started

1. Compile the Java files
2. Run the SupplyMate main class
3. Follow the interactive console prompts

## Algorithm Details

- **Greedy Algorithm**: Sorts families by priority score (urgency, family size, distance)
- **0/1 Knapsack**: Optimizes supply allocation within weight constraints
- **Rebalancing**: Dynamically adjusts allocations when conditions change