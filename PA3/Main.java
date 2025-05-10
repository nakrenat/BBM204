import java.io.File;
/**
 * Main class for running the Alien Flora analysis.
 * <p>
 * It reads the genome XML file, identifies genome clusters,
 * evaluates possible evolutions and adaptations as specified in the XML.
 *
 * Usage: java Main <path-to-xml>
 */
public class Main {
    public static void main(String[] args) {
        File xmlFile = new File(args[0]);
        AlienFlora flora = new AlienFlora(xmlFile);

        flora.readGenomes();
        flora.evaluateEvolutions();
        flora.evaluateAdaptations();
    }
}