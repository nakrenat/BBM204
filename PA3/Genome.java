import java.util.*;
/**
 * Represents a genome with its unique ID, evolution factor, and adaptation links.
 */

public class Genome {
    /** Unique identifier of the genome. */
    public String id;
    /** Evolution factor associated with the genome. */
    public int evolutionFactor;
    /** List of directed links to other genomes with adaptation factors. */
    public List<Link> links;
    /**
     * Constructs a Genome with the given ID and evolution factor.
     *
     * @param id Unique genome identifier
     * @param evolutionFactor Integer value representing evolutionary strength
     */
    public Genome(String id, int evolutionFactor) {
        this.id = id;
        this.evolutionFactor = evolutionFactor;
        this.links = new ArrayList<>();
    }
    /**
     * Adds a link from this genome to another genome.
     *
     * @param target ID of the genome being linked to
     * @param adaptationFactor Cost of adaptation to the target genome
     */
    public void addLink(String target, int adaptationFactor) {
        this.links.add(new Link(target, adaptationFactor));
    }
    /**
     * Represents a directed link to another genome with an adaptation cost.
     */
    public static class Link {
        /** Target genome ID. */
        public String target;
        /** Adaptation cost to reach the target genome. */
        public int adaptationFactor;
        /**
         * Constructs a link to a target genome with given adaptation cost.
         *
         * @param target ID of the genome to link
         * @param adaptationFactor Adaptation cost value
         */
        public Link(String target, int adaptationFactor) {
            this.target = target;
            this.adaptationFactor = adaptationFactor;
        }
    }
}