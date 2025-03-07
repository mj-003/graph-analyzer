package algorithm;

import common.Vertex;

import java.util.*;

public class DijkstraAlgorithm {
    private final List<Vertex> vertices;
    public DijkstraAlgorithm(List<Vertex> graphVertices) {
        vertices = graphVertices;
    }

    public List<Vertex> findShortestPath(Vertex startVertex, Vertex endVertex) {
        List<Vertex> shortestPath = new ArrayList<>();

        Map<Vertex, Integer> distances = new HashMap<>();
        Map<Vertex, Vertex> previous = new HashMap<>();
        PriorityQueue<Vertex> priorityQueue = new PriorityQueue<>(Comparator.comparingInt(distances::get));

        for (Vertex vertex : vertices) {
            if (vertex.equals(startVertex)) {
                distances.put(vertex, 0);
                vertex.setDistance(0);
                priorityQueue.add(vertex);
            } else {
                distances.put(vertex, Integer.MAX_VALUE);
                vertex.setDistance(Integer.MAX_VALUE);
                priorityQueue.add(vertex);
            }
            previous.put(vertex, null);
        }

        while (!priorityQueue.isEmpty()) {
            Vertex currentVertex = priorityQueue.poll();

            if (currentVertex.equals(endVertex)) {
                shortestPath.add(currentVertex);
                while (previous.get(currentVertex) != null) {
                    currentVertex = previous.get(currentVertex);
                    shortestPath.add(currentVertex);
                }
                Collections.reverse(shortestPath);
                break;
            }

            if (distances.get(currentVertex) == Integer.MAX_VALUE) {
                break;
            }

            for (Vertex neighbor : currentVertex.getNeighbors().keySet()) {
                int alt = distances.get(currentVertex) + currentVertex.getWeightToNeighbor(neighbor);

                if (alt < distances.get(neighbor)) {
                    distances.put(neighbor, alt);
                    neighbor.setDistance(alt);
                    previous.put(neighbor, currentVertex);

                    priorityQueue.remove(neighbor);
                    priorityQueue.add(neighbor);
                }
            }
        }

        System.out.println("Shortest path: " + shortestPath);

        return shortestPath;
    }
}
