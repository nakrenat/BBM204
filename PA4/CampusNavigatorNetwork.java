import java.io.Serializable;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CampusNavigatorNetwork implements Serializable {
    static final long serialVersionUID = 11L;
    public double averageCartSpeed;
    public final double averageWalkingSpeed = 1000 / 6.0;
    public int numCartLines;
    public Station startPoint;
    public Station destinationPoint;
    public List<CartLine> lines;

    /**
     * Write the necessary Regular Expression to extract string constants from the fileContent
     * @return the result as String
     */
    public String getStringVar(String varName, String fileContent) {
        Pattern p = Pattern.compile("[\\t ]*" + varName + "[\\t ]*=[\\t ]*\"([^\"]+)\"");
        Matcher m = p.matcher(fileContent);
        if (m.find()) {
            return m.group(1);
        }
        return "";
    }

    /**
     * Write the necessary Regular Expression to extract floating point numbers from the fileContent
     * Your regular expression should support floating point numbers with an arbitrary number of
     * decimals or without any (e.g. 5, 5.2, 5.02, 5.0002, etc.).
     * @return the result as Double
     */
    public Double getDoubleVar(String varName, String fileContent) {
        Pattern p = Pattern.compile("[\\t ]*" + varName + "[\\t ]*=[\\t ]*([0-9]+(?:\\.[0-9]+)?)");
        Matcher m = p.matcher(fileContent);
        if (m.find()) {
            return Double.parseDouble(m.group(1));
        }
        return 0.0;
    }

    public int getIntVar(String varName, String fileContent) {
        Pattern p = Pattern.compile("[\\t ]*" + varName + "[\\t ]*=[\\t ]*([0-9]+)");
        Matcher m = p.matcher(fileContent);
        m.find();
        return Integer.parseInt(m.group(1));
    }

    /**
     * Write the necessary Regular Expression to extract a Point object from the fileContent
     * points are given as an x and y coordinate pair surrounded by parentheses and separated by a comma
     * @return the result as a Point object
     */
    public Point getPointVar(String varName, String fileContent) {
        Point p = new Point(0, 0);
        Pattern pattern = Pattern.compile("[\\t ]*" + varName + "[\\t ]*=\\s*\\((\\d+)\\s*,\\s*(\\d+)\\)");
        Matcher matcher = pattern.matcher(fileContent);
        if (matcher.find()) {
            int x = Integer.parseInt(matcher.group(1));
            int y = Integer.parseInt(matcher.group(2));
            p = new Point(x, y);
        }
        return p;
    }

    /**
     * Function to extract the cart lines from the fileContent by reading train line names and their
     * respective stations.
     * @return List of CartLine instances
     */
    public List<CartLine> getCartLines(String fileContent) {
        List<CartLine> cartLines = new ArrayList<>();
        Pattern p = Pattern.compile("cart_line_name\\s*=\\s*\"([^\"]+)\"\\s*cart_line_stations\\s*=\\s*((?:\\(\\d+\\s*,\\s*\\d+\\)\\s*)+)");
        Matcher m = p.matcher(fileContent);

        while (m.find()) {
            String name = m.group(1);
            String stationsBlock = m.group(2);

            List<Station> stations = new ArrayList<>();
            Pattern stationPattern = Pattern.compile("\\((\\d+)\\s*,\\s*(\\d+)\\)");
            Matcher sm = stationPattern.matcher(stationsBlock);
            int index = 1;
            while (sm.find()) {
                int x = Integer.parseInt(sm.group(1));
                int y = Integer.parseInt(sm.group(2));
                String description = name + " Station " + index++;
                stations.add(new Station(new Point(x, y), description));
            }

            cartLines.add(new CartLine(name, stations));
        }
        return cartLines;
    }

    /**
     * Function to populate the given instance variables of this class by calling the functions above.
     */
    public void readInput(String filename) {
        try {
            Scanner sc = new Scanner(new java.io.File(filename));
            StringBuilder sb = new StringBuilder();
            while (sc.hasNextLine()) {
                sb.append(sc.nextLine()).append("\n");
            }
            String content = sb.toString();

            this.numCartLines = getIntVar("num_cart_lines", content);
            this.startPoint = new Station(getPointVar("starting_point", content), "Starting Point");
            this.destinationPoint = new Station(getPointVar("destination_point", content), "Final Destination");
            this.averageCartSpeed = getDoubleVar("average_cart_speed", content);
            this.lines = getCartLines(content);

            sc.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
