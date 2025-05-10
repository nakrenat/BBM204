import java.util.ArrayList;

/**
 * Represents the result of the optimal scroll collection algorithm.
 * Stores the set of safes and the maximum number of scrolls that can be acquired.
 */
public class OptimalScrollSolution {

    /**
     * The list of safes considered in the solution.
     * Each safe is represented as a list: [0] = complexity, [1] = scrolls.
     */
    private final ArrayList<ArrayList<Integer>> safeSet;

    /**
     * The optimal number of scrolls collected.
     */
    private final int solution;

    /**
     * Constructs a solution object for the scroll collection problem.
     *
     * @param safeSet  The list of safes with their [complexity, scrolls] information.
     * @param solution The maximum number of scrolls collected.
     */
    OptimalScrollSolution(ArrayList<ArrayList<Integer>> safeSet, int solution) {
        this.safeSet = safeSet;
        this.solution = solution;
    }

    /**
     * Gets the optimal number of scrolls collected.
     *
     * @return The maximum scrolls collected.
     */
    public int getSolution() {
        return solution;
    }

    /**
     * Gets the list of safes used in the solution.
     *
     * @return A list of safes, where each safe is represented by [complexity, scrolls].
     */
    public ArrayList<ArrayList<Integer>> getSafeSet() {
        return safeSet;
    }

    /**
     * Prints the solution details to the console.
     *
     * @param solution The solution object containing the safes and scroll count.
     */
    public void printSolution(OptimalScrollSolution solution) {
        System.out.println("Maximum scrolls acquired: " + solution.getSolution());
        System.out.println("For the safe set of :" + solution.getSafeSet());
    }
}
