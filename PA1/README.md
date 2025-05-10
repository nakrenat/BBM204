# PA1 - Sorting Algorithm Performance Analysis  
**Course:** BBM204 - Software Laboratory II  
**University:** Hacettepe University  
**Term:** Spring 2025  
**Student:** Erkan TAN  
**Student ID:** B2220356098  

---

## 📌 Project Overview

This project focuses on the implementation and performance comparison of multiple sorting algorithms using Java. The implemented algorithms are:

- Comb Sort  
- Insertion Sort  
- Shaker Sort  
- Shell Sort  
- Radix Sort  

The dataset used is `TrafficFlowDataset.csv`, and the sorting is performed based on the "Flow Duration" column. The analysis includes sorting performance over three different input types:

- Randomly ordered data  
- Already sorted data  
- Reversely sorted data  

---

## 🧠 Purpose

The main goal is to observe and compare the time complexity, auxiliary space usage, and practical performance of each algorithm when applied to datasets of increasing size. Additionally, the study aims to visualize performance differences through plotted charts.

---

## ⚙️ Technical Details

### ✅ Input Format
- CSV file with traffic data
- Sorting is done using integer values in a specified column

### 🧮 Implemented Algorithms

Each algorithm is implemented in a separate method with reusable structures:
- `CombSort.java`
- `InsertionSort.java`
- `ShakerSort.java`
- `ShellSort.java`
- `RadixSort.java`

### 🛠️ How to Compile & Run

```bash
javac *.java
java Main <input_csv> <column_index> <output_path>
``
