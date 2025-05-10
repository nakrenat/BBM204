import java.io.Serializable;
import java.util.*;

public class ClubFairSetupPlanner implements Serializable {
    static final long serialVersionUID = 88L;

    /**
     * Given a list of Project objects, prints the schedule of each of them.
     * Uses getEarliestSchedule() and printSchedule() methods of the current project to print its schedule.
     * @param projectList a list of Project objects
     */
    public void printSchedule(List<Project> projectList) {
        for (Project project : projectList) {
            int[] schedule = project.getEarliestSchedule();
            project.printSchedule(schedule);
        }
    }

    /**
     * TODO: Parse the input XML file and return a list of Project objects
     *
     * @param filename the input XML file
     * @return a list of Project objects
     */
    public List<Project> readXML(String filename) {
        List<Project> projectList = new ArrayList<>();
        try {
            Scanner scanner = new Scanner(new java.io.File(filename));
            StringBuilder sb = new StringBuilder();
            while (scanner.hasNextLine()) {
                sb.append(scanner.nextLine().trim());
            }
            scanner.close();

            String xml = sb.toString();
            String[] projectChunks = xml.split("<Project>");
            for (String projectChunk : projectChunks) {
                if (!projectChunk.contains("</Project>")) continue;

                String projectName = projectChunk.split("<Name>")[1].split("</Name>")[0];

                List<Task> taskList = new ArrayList<>();
                String[] taskChunks = projectChunk.split("<Task>");
                for (String taskChunk : taskChunks) {
                    if (!taskChunk.contains("</Task>")) continue;

                    int id = Integer.parseInt(taskChunk.split("<TaskID>")[1].split("</TaskID>")[0]);
                    String desc = taskChunk.split("<Description>")[1].split("</Description>")[0];
                    int duration = Integer.parseInt(taskChunk.split("<Duration>")[1].split("</Duration>")[0]);

                    List<Integer> dependencies = new ArrayList<>();
                    if (taskChunk.contains("<DependsOnTaskID>")) {
                        String[] depChunks = taskChunk.split("<DependsOnTaskID>");
                        for (int i = 1; i < depChunks.length; i++) {
                            int dep = Integer.parseInt(depChunks[i].split("</DependsOnTaskID>")[0]);
                            dependencies.add(dep);
                        }
                    }

                    taskList.add(new Task(id, desc, duration, dependencies));
                }

                projectList.add(new Project(projectName, taskList));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return projectList;
    }


}
