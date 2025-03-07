package algorithm;

import common.Vertex;

import java.util.*;
import java.util.stream.Collectors;

public class CycleFinder {
    private List<Vertex> vertices;

    public CycleFinder(List<Vertex> vertices) {
        this.vertices = vertices;

    }
    public Map<List<Vertex>, Double> findCycles() {
        List<List<Vertex>> cycles = new ArrayList<>();

        for (Vertex startVertex : vertices) {
            Set<Vertex> visited = new HashSet<>();
            List<Vertex> currentCycle = new ArrayList<>();
            findRecursive(startVertex, startVertex, visited, currentCycle, cycles);
        }

        return groupCycles(cycles);
    }

    private void findRecursive(Vertex startVertex, Vertex currentVertex, Set<Vertex> visited, List<Vertex> currentCycle, List<List<Vertex>> cycles) {
        visited.add(currentVertex);
        currentCycle.add(currentVertex);

        for (Vertex neighbor : currentVertex.getNeighbors().keySet()) {
            if (!visited.contains(neighbor)) {
                findRecursive(startVertex, neighbor, visited, currentCycle, cycles);
            }
             else if (neighbor == startVertex && currentCycle.size() > 2) {
                cycles.add(new ArrayList<>(currentCycle));
            }
        }

        visited.remove(currentVertex);
        currentCycle.remove(currentCycle.size() - 1);
    }

    private Map<List<Vertex>, Double> groupCycles(List<List<Vertex>> cycles) {
        Map<List<Vertex>, Double> groupedCycles = new HashMap<>();

        for (List<Vertex> cycle : cycles) {
            boolean isDuplicate = false;

            for (List<Vertex> existingCycle : groupedCycles.keySet()) {
                if (existingCycle.containsAll(cycle) && existingCycle.size() == cycle.size()) {
                    isDuplicate = true;
                    break;
                }
            }

            if (!isDuplicate) {
                double cycleWeight = calculateCycleWeight(cycle);
                groupedCycles.put(cycle, cycleWeight);
            }
        }

        System.out.println(groupedCycles.size() + " cycles found");

        // Sortowanie mapy po wartościach (wagi cykli)
        groupedCycles = groupedCycles.entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder())).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (oldValue, newValue) -> oldValue, LinkedHashMap::new));

        return groupedCycles;
    }

    private double calculateCycleWeight(List<Vertex> cycle) {
        double weight = 0.0;

        for (int i = 0; i < cycle.size() - 1; i++) {
            Vertex currentVertex = cycle.get(i);
            Vertex nextVertex = cycle.get(i + 1);
            weight += currentVertex.getWeightToNeighbor(nextVertex);
        }

        weight += cycle.get(cycle.size() - 1).getWeightToNeighbor(cycle.get(0));
        return weight;
    }
}
