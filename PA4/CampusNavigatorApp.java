import java.io.Serializable;
import java.util.*;

public class CampusNavigatorApp implements Serializable {
    static final long serialVersionUID = 99L;

    public HashMap<Station, Station> predecessors = new HashMap<>();
    public HashMap<Set<Station>, Double> times = new HashMap<>();

    public CampusNavigatorNetwork readCampusNavigatorNetwork(String filename) {
        CampusNavigatorNetwork network = new CampusNavigatorNetwork();
        network.readInput(filename);
        return network;
    }

    /**
     * Calculates the fastest route from the user's selected starting point to
     * the desired destination, using the campus golf cart network and walking paths.
     * @return List of RouteDirection instances
     */
    public List<RouteDirection> getFastestRouteDirections(CampusNavigatorNetwork network) {
        List<RouteDirection> routeDirections = new ArrayList<>();
        List<Station> allStations = new ArrayList<>();
        allStations.add(network.startPoint);
        for (CartLine line : network.lines) {
            allStations.addAll(line.cartLineStations);
        }
        allStations.add(network.destinationPoint);

        Map<Station, List<Station>> adj = new HashMap<>();
        Map<String, String> stationToLineName = new HashMap<>();

        for (Station s : allStations) adj.put(s, new ArrayList<>());

        // Walking edges between all pairs
        for (Station a : allStations) {
            for (Station b : allStations) {
                if (!a.equals(b)) {
                    adj.get(a).add(b);
                    double dist = Math.sqrt(Math.pow(a.coordinates.x - b.coordinates.x, 2) +
                            Math.pow(a.coordinates.y - b.coordinates.y, 2));
                    double time = dist / network.averageWalkingSpeed;
                    Set<Station> key = new HashSet<>(Arrays.asList(a, b));
                    times.put(key, time);
                }
            }
        }

        // Add cart line edges (overwrites walking time if shorter)
        for (CartLine line : network.lines) {
            List<Station> stations = line.cartLineStations;
            for (int i = 0; i < stations.size() - 1; i++) {
                Station a = stations.get(i);
                Station b = stations.get(i + 1);
                double dist = Math.sqrt(Math.pow(a.coordinates.x - b.coordinates.x, 2) +
                        Math.pow(a.coordinates.y - b.coordinates.y, 2));
                double time = dist / (network.averageCartSpeed * 1000.0 / 60.0); // convert km/h to m/min
                adj.get(a).add(b);
                adj.get(b).add(a);
                Set<Station> key = new HashSet<>(Arrays.asList(a, b));
                times.put(key, time);
                stationToLineName.put(a.toString() + "_" + b.toString(), line.cartLineName);
                stationToLineName.put(b.toString() + "_" + a.toString(), line.cartLineName);
            }
        }

        Map<Station, Double> dist = new HashMap<>();
        for (Station s : allStations) dist.put(s, Double.POSITIVE_INFINITY);
        dist.put(network.startPoint, 0.0);

        PriorityQueue<Station> pq = new PriorityQueue<>(Comparator.comparingDouble(dist::get));
        pq.add(network.startPoint);

        while (!pq.isEmpty()) {
            Station current = pq.poll();
            for (Station neighbor : adj.get(current)) {
                Set<Station> key = new HashSet<>(Arrays.asList(current, neighbor));
                double alt = dist.get(current) + times.get(key);
                if (alt < dist.get(neighbor)) {
                    dist.put(neighbor, alt);
                    predecessors.put(neighbor, current);
                    pq.remove(neighbor);
                    pq.add(neighbor);
                }
            }
        }

        List<Station> path = new ArrayList<>();
        Station curr = network.destinationPoint;
        while (curr != null) {
            path.add(0, curr);
            curr = predecessors.get(curr);
        }

        for (int i = 0; i < path.size() - 1; i++) {
            Station from = path.get(i);
            Station to = path.get(i + 1);
            Set<Station> key = new HashSet<>(Arrays.asList(from, to));
            double t = times.get(key);
            boolean isCart = stationToLineName.containsKey(from.toString() + "_" + to.toString());
            routeDirections.add(new RouteDirection(from.toString(), to.toString(), t, isCart));
        }
        return routeDirections;
    }

    /**
     * Function to print the route directions to STDOUT
     */
    public void printRouteDirections(List<RouteDirection> directions) {
        double total = 0.0;
        for (RouteDirection rd : directions) {
            total += rd.duration;
        }
        System.out.println("The fastest route takes " + Math.round(total) + " minute(s).");
        System.out.println("Directions");
        System.out.println("----------");
        int step = 1;
        for (RouteDirection rd : directions) {
            System.out.print(step++ + ". ");
            if (rd.cartRide) {
                System.out.print("Ride the cart from ");
            } else {
                System.out.print("Walk from ");
            }
            System.out.printf("\"%s\" to \"%s\" for %.2f minutes.\n", rd.startStationName, rd.endStationName, rd.duration);
        }
    }
}
