# PA2 - Safe-Lock Optimization & Artifact Transport  
**Course:** BBM204 - Software Laboratory II  
**University:** Hacettepe University  
**Term:** Spring 2025  
**Student:** Erkan TAN  
**Student ID:** B2220356098  

---

## 🧭 Project Overview

This project addresses two algorithmic optimization problems:

### 🔐 1. Safe-lock Scroll Collection (Dynamic Programming)
Given a list of safes with required knowledge levels and scroll rewards, the goal is to determine the maximum number of scrolls that can be collected through an optimal sequence of actions:

- Produce knowledge (+5 points)
- Open a safe (if sufficient knowledge is available)
- Skip the safe

A dynamic programming approach is used to simulate all feasible decisions and track maximum achievable scrolls over time.

### 🚀 2. Artifact Transport with Spaceships (Greedy Algorithm)
Artifacts of varying weights must be transported using a minimal number of spaceships, each with a fixed capacity of 100 units.

A greedy approximation is applied:
- Sort artifacts in decreasing order
- Try to fit each item into an existing ship
- If it doesn't fit, open a new one

This mimics the First-Fit Decreasing (FFD) approach to the Bin Packing problem.

---

## 🗂 File Structure

- `Main.java` — Program entry point, auto-detects file type and runs correct algorithm
- `MaxScrollsDP.java` — Dynamic programming implementation for safe-lock problem
- `OptimalScrollSolution.java` — Data structure for scroll result
- `MinShipsGP.java` — Greedy implementation for artifact transport
- `OptimalShipSolution.java` — Data structure for ship packing result

---

## 🛠️ How to Compile & Run

```bash
javac *.java
java Main <file1.txt> <file2.txt>
