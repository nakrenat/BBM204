import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

/**
 * The MinShipsGP class implements a greedy approximation algorithm to solve
 * the Bin Packing problem where artifacts with certain weights must be packed
 * into ships of limited capacity (100 units).
 */
public class MinShipsGP {

    /**
     * List of artifact weights that need to be carried.
     */
    private final ArrayList<Integer> artifactsFound = new ArrayList<>();

    /**
     * Getter for the list of artifact weights.
     *
     * @return List of integers representing artifact weights.
     */
    public ArrayList<Integer> getArtifactsFound() {
        return artifactsFound;
    }

    /**
     * Constructor that initializes the list of artifact weights.
     *
     * @param artifactsFound List of weights to be loaded into ships.
     */
    MinShipsGP(ArrayList<Integer> artifactsFound) {
        this.artifactsFound.addAll(artifactsFound);
    }

    /**
     * Greedy algorithm to minimize the number of ships needed to carry the artifacts.
     * Each ship has a fixed capacity (100 units), and artifacts are placed in
     * decreasing order of size to improve packing efficiency.
     *
     * The method works as follows:
     * - Sort artifacts in decreasing order.
     * - Try to fit each artifact into an existing ship.
     * - If it doesn't fit in any, open a new ship.
     *
     * @return An OptimalShipSolution object that includes the input artifacts and the number of ships used.
     * @throws FileNotFoundException This signature allows compatibility with other interfaces but is not used here.
     */
    public OptimalShipSolution optimalArtifactCarryingAlgorithm() throws FileNotFoundException {
        final int MAX_CAPACITY = 100;
        ArrayList<Integer> artifacts = new ArrayList<>(artifactsFound);
        Collections.sort(artifacts, Collections.reverseOrder());

        ArrayList<Integer> ships = new ArrayList<>();

        for (int artifact : artifacts) {
            boolean placed = false;
            for (int i = 0; i < ships.size(); i++) {
                if (ships.get(i) + artifact <= MAX_CAPACITY) {
                    ships.set(i, ships.get(i) + artifact);
                    placed = true;
                    break;
                }
            }
            if (!placed) {
                ships.add(artifact);
            }
        }

        return new OptimalShipSolution(artifactsFound, ships.size());
    }
}
