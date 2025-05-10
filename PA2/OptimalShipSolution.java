import java.util.ArrayList;

/**
 * Represents the result of the artifact packing algorithm into ships.
 * Stores the list of artifact weights and the minimum number of ships required.
 */
public class OptimalShipSolution {

    /**
     * List of artifact weights considered in the solution.
     */
    private final ArrayList<Integer> artifactSet;

    /**
     * The minimum number of ships needed to carry all artifacts.
     */
    private final int solution;

    /**
     * Constructs a solution object for the artifact shipping problem.
     *
     * @param artifactSet The list of artifact weights.
     * @param solution    The minimum number of ships used.
     */
    OptimalShipSolution(ArrayList<Integer> artifactSet, int solution) {
        this.artifactSet = artifactSet;
        this.solution = solution;
    }

    /**
     * Gets the minimum number of ships required.
     *
     * @return The number of ships used in the solution.
     */
    public int getSolution() {
        return solution;
    }

    /**
     * Gets the list of artifact weights used in the solution.
     *
     * @return A list of artifact weights.
     */
    public ArrayList<Integer> getArtifactSet() {
        return artifactSet;
    }

    /**
     * Prints the solution details to the console.
     *
     * @param solution The solution object containing the artifact set and number of ships.
     */
    public void printSolution(OptimalShipSolution solution) {
        System.out.println("Minimum spaceships required: " + solution.getSolution());
        System.out.println("For the artifact set of :" + solution.getArtifactSet());
    }
}
