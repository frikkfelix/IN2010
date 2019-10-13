package me.frikk.oblig2;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

class Task {
    int id, time, staff, timeUntilFinished;
    String name;
    int earliestStart, latestStart;
    
    List<Task> inEdges = new ArrayList<>();
    List<Task> outEdges = new ArrayList<>();

    int[] dependencyEdges;
    int cntInEdges;

    public Task(int id, String name, int time, int staff, int[] dependencyEdges) {
        this.id = id;
        this.name = name;
        this.time = time;
        this.staff = staff;
        this.timeUntilFinished = time;
        this.dependencyEdges = dependencyEdges;
    }

    public int getSlack() {
        return latestStart - earliestStart;
    }

    public boolean isCritical() {
        return (getSlack() == 0);
    }

    public String getIdString() {
        return String.format("%d", id);
    }

    @Override
    public String toString() {
        String depString = (outEdges.size() > 0)
            ? "\nOutgoing edges: " 
                + outEdges.stream()
                    .map(Task::getIdString)
                    .collect(Collectors.joining(", "))
            : "";
        
        return ("Task " + id + ": " + name
            + "\nTime to complete: " + time
            + "\nManpower required: " + staff
            + "\nEarliest starting time: " + earliestStart
            + "\nSlack: " + getSlack()
            + depString);
    }

    public List<Task> getOutEdges() {
        return outEdges;
    }
}