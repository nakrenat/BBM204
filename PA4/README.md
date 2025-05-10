# PA4 - Campus Task Scheduler & Route Finder  
**Course:** BBM204 - Software Laboratory II  
**University:** Hacettepe University  
**Term:** Spring 2025  
**Student:** Erkan TAN  
**Student ID:** B2220356098  

---

## 📌 Project Overview

This project focuses on parsing structured textual data and building efficient algorithms to achieve two main objectives:

1. **Task Scheduling**: Parsing tasks and dependencies from an XML-like file format, then generating a valid execution plan using **topological sorting**.
2. **Shortest Route Calculation**: Parsing transportation network data, constructing a **graph**, and computing the shortest path using **graph traversal algorithms** (e.g., Dijkstra’s algorithm).

Additionally, the project makes use of **regular expressions** to extract meaningful information from custom formatted text.

---

## 🧠 Key Features

- XML/text parser using Regular Expressions  
- Dependency graph creation and topological sort  
- Campus navigation with cart/walk speed handling  
- Support for different transit lines and paths  
- Modular and reusable object-oriented Java design  

---

## 🛠️ How to Compile & Run

```bash
javac *.java
java Main <stations.txt> <paths.txt> <tasks.txt> <commands.txt> <output.txt>
