# Graph Analyzer

## Overview
Graph Explorer is a Java-based application for generating, visualizing, and analyzing graphs. It allows users to generate different types of graphs, find shortest paths using Dijkstra's algorithm, detect cycles, and compare different graph structures. The tool provides an interactive GUI to explore graphs with weighted edges and highlights key algorithms in graph theory.

<img width="1006" alt="Zrzut ekranu 2025-03-7 o 11 38 27" src="https://github.com/user-attachments/assets/874f0dab-a1e9-491c-a9ab-f3db796a93e3" />

<img width="1006" alt="Zrzut ekranu 2025-03-7 o 11 37 53" src="https://github.com/user-attachments/assets/7e113ef0-05cb-4b60-a1c7-959e47423b9d" />

<img width="1006" alt="Zrzut ekranu 2025-03-7 o 11 37 24" src="https://github.com/user-attachments/assets/c16cf4ce-ebfe-434f-b9a7-c3d7d7699cad" />


## Features
- **Graph Generation:** Supports different types of graphs:
  - Random Graph
  - Gabriel Graph
  - Relative Neighborhood Graph (RN Graph)
- **Pathfinding:** Implements **Dijkstra's Algorithm** to find the shortest paths with edge weights.
- **Cycle Detection:** Identifies cycles within the graph structure.
- **Graph Comparison:** Allows users to compare **Random Graph** and **Relative Neighborhood Graph**.
- **User Interaction:**
  - Users can specify the number of vertices.
  - The graph is generated dynamically based on the chosen algorithm.
  - Edge weights are displayed for better visualization.
  - Option to show node influence using circles.

## How to Use
1. **Enter the number of vertices** in the input field.
2. **Generate a graph** by selecting one of the available types.
3. Use the **"Find Shortest Path"** button to compute and display the shortest path between selected nodes.
4. Click **"Find Cycles"** to detect and highlight cycles in the graph.
5. Use **"Compare"** to analyze the differences between Random Graph and RN Graph.
6. Optionally, enable **"Show Circles"** to visualize node influence areas.

## Installation & Running the Project
### Requirements:
- Java 8+
- Swing (GUI)

## Future Improvements
- Adding more graph algorithms (e.g., Prim’s, Kruskal’s for MST)
- Implementing breadth-first and depth-first search
- Enhancing the UI with more customization options

