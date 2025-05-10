import java.io.Serializable;
import java.util.*;

public class Project implements Serializable {
    static final long serialVersionUID = 33L;
    private final String name;
    private final List<Task> tasks;

    public Project(String name, List<Task> tasks) {
        this.name = name;
        this.tasks = tasks;
    }

    public List<Integer> topologicalSort(Map<Integer, List<Integer>> dependencyGraph) {
        LinkedList<Integer> result = new LinkedList<>();
        boolean[] isVisited = new boolean[tasks.size()];
        topologicalSortRecursive(0, isVisited, result, dependencyGraph);
        return result;
    }

    private void topologicalSortRecursive(int current, boolean[] isVisited, LinkedList<Integer> result, Map<Integer, List<Integer>> dependencyGraph) {
        isVisited[current] = true;
        for (int dest : dependencyGraph.get(current)) {
            if (!isVisited[dest])
                topologicalSortRecursive(dest, isVisited, result, dependencyGraph);
        }
        result.addFirst(current);
    }

    /**
     * @return the total duration of the project in days
     */
    public int getProjectDuration() {
        int projectDuration = 0;


        int[] schedule = getEarliestSchedule();
        for (int i = 0; i < schedule.length; i++) {
            int end = schedule[i] + tasks.get(i).getDuration();
            if (end > projectDuration) {
                projectDuration = end;
            }
        }

        return projectDuration;
    }

    /**
     * Schedule all tasks within this project such that they will be completed as early as possible.
     *
     * @return An integer array consisting of the earliest start days for each task.
     */
    public int[] getEarliestSchedule() {
        int n = tasks.size();
        int[] startTimes = new int[n];
        Map<Integer, List<Integer>> graph = new HashMap<>();
        int[] indegree = new int[n];

        for (int i = 0; i < n; i++) {
            graph.put(i, new ArrayList<>());
        }

        // Bağımlılıkları oluştur (indexOf kullanmadan)
        for (int i = 0; i < n; i++) {
            for (int dep : tasks.get(i).getDependencies()) {
                graph.get(dep).add(i);
                indegree[i]++;
            }
        }

        Queue<Integer> queue = new LinkedList<>();
        for (int i = 0; i < n; i++) {
            if (indegree[i] == 0) {
                queue.add(i);
            }
        }

        while (!queue.isEmpty()) {
            int u = queue.poll();
            int uEnd = startTimes[u] + tasks.get(u).getDuration();
            for (int v : graph.get(u)) {
                startTimes[v] = Math.max(startTimes[v], uEnd);
                indegree[v]--;
                if (indegree[v] == 0) {
                    queue.add(v);
                }
            }
        }

        return startTimes;
    }



    public static void printlnDash(int limit, char symbol) {
        for (int i = 0; i < limit; i++) System.out.print(symbol);
        System.out.println();
    }

    /**
     * Some free code here. YAAAY! 
     */
    public void printSchedule(int[] schedule) {
        int limit = 65;
        char symbol = '-';
        printlnDash(limit, symbol);
        System.out.println(String.format("Project name: %s", name));
        printlnDash(limit, symbol);
        System.out.println(String.format("%-10s%-45s%-7s%-5s","Task ID","Description","Start","End"));
        printlnDash(limit, symbol);

        // 💡 Değişiklik burada başlıyor
        List<Integer> order = new ArrayList<>();
        for (int i = 0; i < schedule.length; i++) {
            order.add(i);
        }

        order.sort((a, b) -> {
            if (schedule[a] != schedule[b])
                return Integer.compare(schedule[a], schedule[b]);
            return Integer.compare(a, b);
        });
        // 💡 Değişiklik burada bitiyor

        for (int i : order) {
            Task t = tasks.get(i);
            System.out.println(String.format("%-10d%-45s%-7d%-5d", i, t.getDescription(), schedule[i], schedule[i]+t.getDuration()));
        }
        printlnDash(limit, symbol);
        System.out.println(String.format("Project will be completed in %d days.", tasks.get(schedule.length-1).getDuration() + schedule[schedule.length-1]));
        printlnDash(limit, symbol);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Project project = (Project) o;

        int equal = 0;

        for (Task otherTask : ((Project) o).tasks) {
            if (tasks.stream().anyMatch(t -> t.equals(otherTask))) {
                equal++;
            }
        }

        return name.equals(project.name) && equal == tasks.size();
    }
}