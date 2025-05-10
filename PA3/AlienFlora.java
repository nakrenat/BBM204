import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.io.File;
import java.util.*;
/**
 * Handles parsing and processing of alien flora genomes from an XML file.
 * It identifies genome clusters, evaluates evolutions across clusters,
 * and adaptations within clusters.
 */
public class AlienFlora {
    /** Input XML file. */
    private File xmlFile;
    /** List of genome clusters detected from genome graph. */
    public static List<GenomeCluster> clusters = new ArrayList<>();
    /**
     * Constructs the AlienFlora processor with the given XML file.
     *
     * @param xmlFile Input XML file describing genomes and links
     */
    public AlienFlora(File xmlFile) {
        this.xmlFile = xmlFile;
    }
    /**
     * Parses genomes and their links from the XML and builds genome clusters.
     * This method identifies connected components in the genome graph.
     */
    public void readGenomes() {
        System.out.println("##Start Reading Flora Genomes##");

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(xmlFile);

            NodeList genomeNodes = doc.getElementsByTagName("genome");

            Map<String, Genome> allGenomes = new HashMap<>();

            for (int i = 0; i < genomeNodes.getLength(); i++) {
                Element genomeElement = (Element) genomeNodes.item(i);
                String id = genomeElement.getElementsByTagName("id").item(0).getTextContent();
                int evolutionFactor = Integer.parseInt(genomeElement.getElementsByTagName("evolutionFactor").item(0).getTextContent());
                Genome genome = new Genome(id, evolutionFactor);

                NodeList linkNodes = genomeElement.getElementsByTagName("link");
                for (int j = 0; j < linkNodes.getLength(); j++) {
                    Element linkElement = (Element) linkNodes.item(j);
                    String target = linkElement.getElementsByTagName("target").item(0).getTextContent();
                    int adaptationFactor = Integer.parseInt(linkElement.getElementsByTagName("adaptationFactor").item(0).getTextContent());
                    genome.addLink(target, adaptationFactor);
                }
                allGenomes.put(id, genome);
            }

            Set<String> visited = new HashSet<>();
            clusters = new ArrayList<>();

            for (String id : allGenomes.keySet()) {
                if (!visited.contains(id)) {
                    GenomeCluster cluster = new GenomeCluster();
                    Queue<String> queue = new LinkedList<>();
                    queue.add(id);
                    visited.add(id);

                    while (!queue.isEmpty()) {
                        String currentId = queue.poll();
                        Genome genome = allGenomes.get(currentId);
                        cluster.addGenome(genome);

                        for (Genome.Link link : genome.links) {
                            if (!visited.contains(link.target)) {
                                queue.add(link.target);
                                visited.add(link.target);
                            }
                        }

                        for (Genome g : allGenomes.values()) {
                            for (Genome.Link l : g.links) {
                                if (l.target.equals(currentId) && !visited.contains(g.id)) {
                                    queue.add(g.id);
                                    visited.add(g.id);
                                }
                            }
                        }
                    }
                    clusters.add(cluster);
                }
            }

            System.out.println("Number of Genome Clusters: " + clusters.size());
            System.out.print("For the Genomes: [");
            List<List<String>> clusterIds = new ArrayList<>();
            for (GenomeCluster cluster : clusters) {
                List<String> ids = new ArrayList<>(cluster.genomeMap.keySet());
                Collections.sort(ids);
                clusterIds.add(ids);
            }
            System.out.println(clusterIds + "]");
            System.out.println("##Reading Flora Genomes Completed##");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * Evaluates possible evolution pairs across clusters.
     * Uses the minimum evolution factor in each cluster and computes their average.
     * Outputs -1 for invalid (intra-cluster) evolutions.
     */
    public void evaluateEvolutions() {
        System.out.println("##Start Evaluating Possible Evolutions##");

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(xmlFile);

            NodeList pairNodes = doc.getElementsByTagName("possibleEvolutionPairs").item(0).getChildNodes();

            int possible = 0;
            int certified = 0;
            List<Double> factors = new ArrayList<>();

            for (int i = 0; i < pairNodes.getLength(); i++) {
                if (pairNodes.item(i).getNodeType() != Node.ELEMENT_NODE) continue;
                Element pairElement = (Element) pairNodes.item(i);
                String firstId = pairElement.getElementsByTagName("firstId").item(0).getTextContent();
                String secondId = pairElement.getElementsByTagName("secondId").item(0).getTextContent();

                GenomeCluster firstCluster = null;
                GenomeCluster secondCluster = null;
                for (GenomeCluster cluster : clusters) {
                    if (cluster.contains(firstId)) {
                        firstCluster = cluster;
                    }
                    if (cluster.contains(secondId)) {
                        secondCluster = cluster;
                    }
                }

                possible++;
                if (firstCluster != null && secondCluster != null && firstCluster != secondCluster) {
                    int firstMin = firstCluster.getMinEvolutionGenome().evolutionFactor;
                    int secondMin = secondCluster.getMinEvolutionGenome().evolutionFactor;
                    double factor = (firstMin + secondMin) / 2.0;
                    factors.add(factor);
                    certified++;
                } else {
                    factors.add(-1.0);
                }
            }

            System.out.println("Number of Possible Evolutions: " + possible);
            System.out.println("Number of Certified Evolution: " + certified);
            System.out.println("Evolution Factor for Each Evolution Pair: " + factors);
            System.out.println("##Evaluated Possible Evolutions##");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * Evaluates possible adaptation pairs within a cluster.
     * Uses Dijkstra's algorithm to find the shortest adaptation path.
     * Outputs -1 if the genomes belong to different clusters.
     */
    public void evaluateAdaptations() {
        System.out.println("##Start Evaluating Possible Adaptations##");

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(xmlFile);

            NodeList pairNodes = doc.getElementsByTagName("possibleAdaptationPairs").item(0).getChildNodes();

            int possible = 0;
            int certified = 0;
            List<Integer> factors = new ArrayList<>();

            for (int i = 0; i < pairNodes.getLength(); i++) {
                if (pairNodes.item(i).getNodeType() != Node.ELEMENT_NODE) continue;
                Element pairElement = (Element) pairNodes.item(i);
                String firstId = pairElement.getElementsByTagName("firstId").item(0).getTextContent();
                String secondId = pairElement.getElementsByTagName("secondId").item(0).getTextContent();

                GenomeCluster firstCluster = null;
                GenomeCluster secondCluster = null;
                for (GenomeCluster cluster : clusters) {
                    if (cluster.contains(firstId)) {
                        firstCluster = cluster;
                    }
                    if (cluster.contains(secondId)) {
                        secondCluster = cluster;
                    }
                }

                possible++;
                if (firstCluster != null && firstCluster == secondCluster) {
                    int factor = firstCluster.dijkstra(firstId, secondId);
                    factors.add(factor);
                    certified++;
                } else {
                    factors.add(-1);
                }
            }

            System.out.println("Number of Possible Adaptations: " + possible);
            System.out.println("Number of Certified Adaptations: " + certified);
            System.out.println("Adaptation Factor for Each Adaptation Pair: " + factors);
            System.out.print("##Evaluated Possible Adaptations##");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}