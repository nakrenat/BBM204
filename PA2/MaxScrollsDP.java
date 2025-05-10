import java.io.FileNotFoundException;
import java.util.ArrayList;

/**
 * The MaxScrollsDP class implements a dynamic programming solution
 * to determine the maximum number of scrolls that can be collected
 * by choosing optimal actions (produce knowledge, open safe, skip).
 */
public class MaxScrollsDP {

    /**
     * A list where each element represents a safe.
     * Each safe is represented as a list of two integers:
     * [0] = complexity (knowledge required to open),
     * [1] = scrolls (reward if opened).
     */
    private final ArrayList<ArrayList<Integer>> safesDiscovered;

    /**
     * Constructor to initialize the safesDiscovered list.
     *
     * @param safesDiscovered A list of safes, each represented by [complexity, scrolls].
     */
    public MaxScrollsDP(ArrayList<ArrayList<Integer>> safesDiscovered) {
        this.safesDiscovered = safesDiscovered;
    }

    /**
     * Returns the list of discovered safes.
     *
     * @return A list of safes, each with [complexity, scrolls].
     */
    public ArrayList<ArrayList<Integer>> getSafesDiscovered() {
        return safesDiscovered;
    }

    /**
     * Computes the optimal number of scrolls that can be collected
     * by making the best decisions at each step using dynamic programming.
     *
     * Actions available at each step:
     *  - Produce knowledge (+5 knowledge).
     *  - Open a safe (if enough knowledge is available).
     *  - Skip the safe (do nothing).
     *
     * @return An OptimalScrollSolution containing the input data and the maximum number of scrolls collected.
     * @throws FileNotFoundException This method signature allows for compatibility, but no file operations occur here.
     */
    public OptimalScrollSolution optimalSafeOpeningAlgorithm() throws FileNotFoundException {
        int T = safesDiscovered.size();
        int maxKnowledge = 5 * T;

        // +2 because we sometimes look one step ahead; +10 keeps a little buffer
        int[][] dp = new int[T + 2][maxKnowledge + 10];

        // Fill with -1 (unreachable states)
        for (int i = 0; i <= T + 1; i++) {
            for (int k = 0; k <= maxKnowledge + 5; k++) {
                dp[i][k] = -1;
            }
        }

        // Start: time 0, knowledge 0 ⇒ 0 scrolls collected
        dp[0][0] = 0;

        for (int i = 0; i < T; i++) {
            int complexity = safesDiscovered.get(i).get(0); // knowledge cost
            int scrolls    = safesDiscovered.get(i).get(1); // reward

            for (int k = 0; k <= maxKnowledge; k++) {
                if (dp[i][k] < 0) continue; // unreachable state, skip

                // Option 1 – produce knowledge (+5)
                dp[i + 1][k + 5] = Math.max(dp[i + 1][k + 5], dp[i][k]);

                // Option 2 – open safe if enough knowledge
                if (k >= complexity) {
                    dp[i + 1][k - complexity] =
                            Math.max(dp[i + 1][k - complexity], dp[i][k] + scrolls);
                }

                // Option 3 – skip (do nothing)
                dp[i + 1][k] = Math.max(dp[i + 1][k], dp[i][k]);
            }
        }

        int maxScrolls = 0;
        for (int k = 0; k <= maxKnowledge; k++) {
            maxScrolls = Math.max(maxScrolls, dp[T][k]);
        }

        return new OptimalScrollSolution(safesDiscovered, maxScrolls);
    }
}
