package me.frikk.oblig2;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

class Oblig2 {
    public static void main(String[] args) throws FileNotFoundException {
        //File file = new File(args[0]);
        File file = new File("/Users/frikk/Documents/3. semester/IN2010/ProjectsAoDS/me.frikk.obliger/src/me/frikk/oblig21/buildgarage.txt");
        Project project = Project.readFromFile(file);
        List<Task> topSorted = project.topologicalSort();

        if (topSorted == null) {
            System.out.println("****  Project contains a cycle, and is not realizable  ****");
            String cycleString = project.cycle.stream()
                                    .map(Task::getIdString)
                                    .collect(Collectors.joining(" -> "));
            System.out.println("\nThe following cycle was found: " + cycleString);
        } else {  
            project.createTimeTable(topSorted);
            project.simulateExecution();
            project.printInfo();
        }
    }
}