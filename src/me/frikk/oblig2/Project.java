package me.frikk.oblig2;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

class Project {
    int num_tasks, completionTime;
    List<Task> tasks;
    List<Task> cycle;

    private Project(int num_tasks, List<Task> tasks) {
        this.num_tasks = num_tasks;
        this.tasks = tasks;
    }

    public void printInfo() {
        System.out.println(String.format("\nThis project consists of the following %d tasks:", num_tasks));
        for (Task task : tasks) {
            System.out.println();
            System.out.println(task);
        }
    }

    /**
     * Static factory-method.
     * Creates Task-objects from text file.
     * @param file
     * @return Project-object specified in file
     * @throws FileNotFoundException
     */
    static Project readFromFile(File file) throws FileNotFoundException {
        String line = null;
        List<Task> tasks = new ArrayList<>();

        try {
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            
            int num_tasks = Integer.parseInt(bufferedReader.readLine());

            while ((line = bufferedReader.readLine()) != null) {
                if (line.length() <= 1) continue;
                
                String[] info = line.split("\\s+");

                int id = Integer.parseInt(info[0]);
                String name = info[1];
                int time = Integer.parseInt(info[2]);
                int staff = Integer.parseInt(info[3]);

                int num_depEdges = info.length - 5;
                int[] dependencyEdges = new int[num_depEdges];

                for (int i = 0; i < num_depEdges; i++) {
                    dependencyEdges[i] = Integer.parseInt(info[i + 4]);
                }

                Task newTask = new Task(id, name, time, staff, dependencyEdges);
                tasks.add(newTask);
            }

            bufferedReader.close();
            Project project = new Project(num_tasks, tasks);
            project.addEdges();
            return project;

        } catch (NumberFormatException e) {
            e.getMessage();

        } catch (IOException e) {
            e.getMessage();
        }
        return null;
    }

    private void addEdges() {
        for (Task task : tasks) {
            for (int inEdge : task.dependencyEdges) {
                tasks.get(inEdge - 1).outEdges.add(task);
            }
        }

        for (Task predecessor : tasks) {
            for (Task successor : predecessor.outEdges) {
                successor.inEdges.add(predecessor);
            }
        }
    }
    
    /**
     * Topoligical sort, based on DFS.
     * Sorts Tasks in project in possible order of execution.
     * The reverse order in which each task is added to sorted gives a topological sort.
     * Time complexity O(|V|+|E|).
     * @return a list of sorted tasks, or null if graph contains at least one cycle
     */
    public List<Task> topologicalSort() {
        List<Task> sorted = new ArrayList<>();
        for (Task t : tasks) {
            if (DFSutil(t, sorted, new ArrayList<>())) {
                return null;
            }
        }
        Collections.reverse(sorted);
        return sorted;
    }

    /**
     * Utility DFS-method for topologicalSort.
     * If cycle is found, it is assigned to this.cycle.
     * @param currentTask
     * @param sorted list of fully explored tasks
     * @param visited list of visited tasks
     * @return true if graph contains a cycle, else false
     */
    public boolean DFSutil(Task currentTask, List<Task> sorted, List<Task> visited) {
        if (sorted.contains(currentTask)) {
            return false;
        }

        if(visited.contains(currentTask)) {
            visited.add(currentTask);
            cycle = visited.subList(visited.indexOf(currentTask), visited.size());
            return true;
        }

        visited.add(currentTask);

        for (Task successor : currentTask.getOutEdges()) {
            if(DFSutil(successor, sorted, new ArrayList<>(visited))) {
                return true;
            }
        }

        sorted.add(currentTask);
        return false;
    }

    /**
     * Calculates earliest and latest start for all tasks in the project,
     * as well as total completion time 
     * @param sorted list of topologically sorted tasks
     */
    public void createTimeTable(List<Task> sorted) {
        for (Task task : sorted) {
            this.completionTime = Math.max(completionTime, calculateEarliestStart(task));
        }
        List<Task> reversed = new ArrayList<>(sorted);
        Collections.reverse(reversed);
        for (Task task : reversed) {
            calculateLatestStart(task, completionTime);
        }
    }

    private int calculateEarliestStart(Task task) {
        int startTime = 0;
        for (Task inNode : task.inEdges) {
            startTime = Math.max(startTime, inNode.earliestStart + inNode.time);
        }

        task.earliestStart = startTime;
        return task.earliestStart + task.time;
    }

    private void calculateLatestStart(Task task, int completionTime) {
        if (task.getOutEdges().isEmpty()) {
            task.latestStart = completionTime - task.time;
        } else {
            int latestStart = Integer.MAX_VALUE;
            for (Task successor : task.getOutEdges()) {
                latestStart = Math.min(latestStart, successor.latestStart - task.time);
            }
            task.latestStart = latestStart;
        }
    }

    /**
     * Prints a simulation of project execution
     */
    public void simulateExecution() {
        System.out.println("****  Simulation of project execution  ****");
        List<Task> upcomingTasks = new ArrayList<>(tasks);
        List<Task> startedTasks = new ArrayList<>();

        int timer = 0;
        int currentStaff = 0;

        while(timer <= completionTime) {
            String tString = "\nTime: " + timer;

            List<Task> finishedTasks = new ArrayList<>();
            
            for (Task task : startedTasks) {
                if (task.timeUntilFinished == 0) {
                    finishedTasks.add(task);
                    currentStaff -= task.staff;
                    String fString = "Finished: " + task.id;
                    System.out.println(padString(tString, fString));
                    tString = "";
                }
            }

            startedTasks.removeAll(finishedTasks);

            if(timer == completionTime) break;

            List<Task> willStart = new ArrayList<>();

            for (Task task : upcomingTasks) {
                if (task.earliestStart == timer) {
                    willStart.add(task);
                    currentStaff += task.staff;
                    String sString = "Starting: " + task.id;
                    System.out.println(padString(tString, sString));
                    tString = "";
                }
            }

            moveTasks(willStart, upcomingTasks, startedTasks);

            String cString = "Current staff: " + currentStaff;
            System.out.println(padString(tString, cString));

            Task finishesNext = Collections.min(startedTasks, Comparator.comparing(t -> t.timeUntilFinished));
            int timeToNextFinish = finishesNext.timeUntilFinished;

            timer += timeToNextFinish;
            startedTasks.forEach(t -> t.timeUntilFinished -= timeToNextFinish);
        }

        System.out.println(String.format("\n****  Shortest possible project execution is %d  ****", completionTime));
    }

    private void moveTasks(List<Task> tasks, List<Task> source, List<Task> destination) {
        source.removeAll(tasks);
        destination.addAll(tasks);
    }

    public static String padString(String s1, String s2) {
        return String.format("%9s \t%s", s1, s2);  
    }
}