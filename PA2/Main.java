import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Main class for running Safe-lock and Artifact transportation operations.
 * This program takes two input files: one representing safe-lock data (complexity, scrolls)
 * and the other representing artifact transport data (artifact sizes).
 * It determines which file is which and runs appropriate optimization algorithms.
 */
public class Main {
    /**
     * Entry point of the application. Expects two command-line arguments representing input files.
     * @param args Command line arguments: two file paths.
     * @throws IOException If reading from the files fails.
     */
    public static void main(String[] args) throws IOException {
        File fileA = new File(args[0]);
        File fileB = new File(args[1]);

        File safeFile;
        File artifactFile;

        /**
         * Determine which file is the safe-lock file by trying to parse the first line as an integer.
         * The safe file always starts with a number (number of safes).
         */
        boolean isFileA_Safe = false;
        try (Scanner testScanner = new Scanner(fileA)) {
            String firstLine = testScanner.nextLine().trim();
            Integer.parseInt(firstLine); // safe dosyasıysa ilk satır parse edilebilir
            isFileA_Safe = true;
        } catch (Exception ignored) {}

        if (isFileA_Safe) {
            safeFile = fileA;
            artifactFile = fileB;
        } else {
            safeFile = fileB;
            artifactFile = fileA;
        }

        /**
         * ================================
         * Safe-lock Opening Algorithm
         * ================================
         */
        System.out.println("##Initiate Operation Safe-lock##");

        Scanner safeScanner = new Scanner(safeFile);
        if (safeScanner.hasNextLine()) safeScanner.nextLine(); // Skip metadata line (number of safes)

        ArrayList<ArrayList<Integer>> safes = new ArrayList<>();

        /**
         * Parse each line in the safe file to extract pairs of [complexity, scrolls].
         */
        while (safeScanner.hasNextLine()) {
            String line = safeScanner.nextLine().trim();
            if (line.isEmpty()) continue;
            String[] parts = line.split(",");
            ArrayList<Integer> pair = new ArrayList<>();
            pair.add(Integer.parseInt(parts[0].trim())); // complexity
            pair.add(Integer.parseInt(parts[1].trim())); // scrolls
            safes.add(pair);
        }

        /**
         * Apply dynamic programming to find the maximum number of scrolls collectible
         * given time constraints and knowledge growth.
         */
        MaxScrollsDP dpSolver = new MaxScrollsDP(safes);
        OptimalScrollSolution scrollSolution = dpSolver.optimalSafeOpeningAlgorithm();
        scrollSolution.printSolution(scrollSolution);
        System.out.println("##Operation Safe-lock Completed##");

        /**
         * ================================
         * Artifact Carrying Optimization
         * ================================
         */
        System.out.println("##Initiate Operation Artifact##");

        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(artifactFile));
            String line;

            /**
             * For each line (artifact batch), parse the list of artifact weights
             * and run the greedy optimization to minimize the number of ships used.
             */
            while ((line = reader.readLine()) != null) {
                line = line.replaceAll("[\\[\\]\"]", ""); // remove brackets and quotes
                String[] values = line.split(",");

                ArrayList<Integer> tempArrayList = new ArrayList<>();
                for (String value : values) {
                    if (!value.trim().isEmpty()) {
                        tempArrayList.add(Integer.parseInt(value.trim()));
                    }
                }

                MinShipsGP minShipHandler = new MinShipsGP(tempArrayList);
                OptimalShipSolution shipSolutionHandler = minShipHandler.optimalArtifactCarryingAlgorithm();
                shipSolutionHandler.printSolution(shipSolutionHandler);
            }

        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + artifactFile.getName());
        } catch (IOException e) {
            System.err.println("Error reading file: " + artifactFile.getName());
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                System.err.println("Error closing the reader.");
            }
        }

        System.out.print("##Operation Artifact Completed##");
    }
}
