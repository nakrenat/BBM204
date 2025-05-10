import java.util.*;
/**
 * Represents a genome cluster (connected component) where genomes are linked via adaptation.
 */
public class GenomeCluster {
    /** Map from genome ID to Genome object. */
    public Map<String, Genome> genomeMap = new HashMap<>();
    /**
     * Adds a genome to this cluster.
     *
     * @param genome Genome object to be added
     */
    public void addGenome(Genome genome) {
        genomeMap.put(genome.id, genome);
    }
    /**
     * Checks if the cluster contains a genome with the given ID.
     *
     * @param genomeId ID to be checked
     * @return true if genome exists in the cluster, false otherwise
     */
    public boolean contains(String genomeId) {
        return genomeMap.containsKey(genomeId);
    }
    /**
     * Returns the genome with the minimum evolution factor in the cluster.
     *
     * @return Genome with minimum evolution factor
     */
    public Genome getMinEvolutionGenome() {
        Genome minGenome = null;
        for (Genome genome : genomeMap.values()) {
            if (minGenome == null || genome.evolutionFactor < minGenome.evolutionFactor) {
                minGenome = genome;
            }
        }
        return minGenome;
    }
    /**
     * Finds the shortest adaptation path between two genomes in the cluster using Dijkstra's algorithm.
     *
     * @param startId Start genome ID
     * @param endId   Target genome ID
     * @return Minimum adaptation factor between the two genomes, or -1 if unreachable
     */
    public int dijkstra(String startId, String endId) {
        Map<String, Integer> distances = new HashMap<>();
        PriorityQueue<String> queue = new PriorityQueue<>(Comparator.comparingInt(distances::get));

        for (String id : genomeMap.keySet()) {
            distances.put(id, Integer.MAX_VALUE);
        }
        distances.put(startId, 0);
        queue.add(startId);

        while (!queue.isEmpty()) {
            String currentId = queue.poll();
            Genome currentGenome = genomeMap.get(currentId);

            for (Genome.Link link : currentGenome.links) {
                String neighborId = link.target;
                int newDist = distances.get(currentId) + link.adaptationFactor;
                if (newDist < distances.get(neighborId)) {
                    distances.put(neighborId, newDist);
                    queue.add(neighborId);
                }
            }
        }

        return distances.get(endId) == Integer.MAX_VALUE ? -1 : distances.get(endId);
    }
}
