package GUI;

import algorithm.CycleFinder;
import algorithm.DijkstraAlgorithm;
import common.Vertex;
import graph.AbstractGraph;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class GraphModel {
    private final ArrayList<Vertex> vertices;
    static int VERTEX_COUNT;
    private Map<List<Vertex>, Double> cycles;
    static ArrayList<Vertex> highlightedCycle;

    public GraphModel() {
        vertices = new ArrayList<>();
        VERTEX_COUNT = 0;
        cycles = new HashMap<>();
        highlightedCycle = new ArrayList<>();
    }

    public void setCycles(Map<List<Vertex>, Double> cycles) {
        this.cycles = cycles;
    }
    public void setStartVertex() {
    }
    public void setEndVertex() {
    }
    public List<Vertex> getVertices() {
        return vertices;
    }
    public void addVertex(Point vertex) {
        vertices.add(new Vertex(vertex, VERTEX_COUNT));
        VERTEX_COUNT++;
        GraphView.updateVertexComboBoxes(vertices);
    }
    public void addEdge(Point start, Point end, int weight) {
        Vertex startVertex = findVertex(start);
        Vertex endVertex = findVertex(end);
        if (startVertex != null && endVertex != null) {
            startVertex.addNeighbor(endVertex, weight);
        }
    }

    private Vertex findVertex(Point point) {
        for (Vertex vertex : vertices) {
            if (isPointWithinVertex(point, vertex)) {
                return vertex;
            }
        }
        return null;
    }

    private boolean isPointWithinVertex(Point point, Vertex vertex) {
        int dx = point.x - vertex.getPoint().x;
        int dy = point.y - vertex.getPoint().y;
        int distance = (int) Math.sqrt(dx * dx + dy * dy);
        return distance <= 20.0;
    }

    public List<Vertex> findShortestPath(Vertex startVertex, Vertex endVertex) {
        DijkstraAlgorithm dijkstra = new DijkstraAlgorithm(vertices);
        return dijkstra.findShortestPath(startVertex, endVertex);
    }

    // obliczenie dlugosci najkrotszej sciezki
    public int shortestPathLength(List<Vertex> shortestPath) {
        int length = 0;
        for (int i = 0; i < shortestPath.size() - 1; i++) {
            Vertex currVertex = shortestPath.get(i);
            Vertex nextVertex = shortestPath.get(i + 1);
            System.out.println(currVertex.getNeighbors());
            for (Vertex neighbor : currVertex.getNeighbors().keySet()) {
                if (neighbor.equals(nextVertex)) {
                    System.out.println(currVertex.getWeightToNeighbor(neighbor));
                    length += currVertex.getWeightToNeighbor(neighbor);
                }
            }
        }
        return length;
    }

    public Map<List<Vertex>, Double> findCycles() {
        CycleFinder cycleFinder = new CycleFinder(vertices);
        cycles = cycleFinder.findCycles();
        System.out.println("Cycles: " + cycles);
        return cycles;
    }

    // GENERATING GRAPH

    public void generateGraphModel(int vertexCount, int edgeCount, int maxWidth, int maxHeight, int maxWeight, AbstractGraph graph, boolean addNeighbors) {
        vertices.clear();
        graph.generate(maxWidth, maxHeight, vertexCount, edgeCount, maxWeight, addNeighbors);
        vertices.addAll(graph.getVertices());
    }


    public void clear() {
        vertices.clear();
        VERTEX_COUNT = 0;
    }

    public void setHighlightedCycle(List<Vertex> cycle) {
        highlightedCycle = (ArrayList<Vertex>) cycle;
    }

    public void clearCycles() {
        cycles.clear();
    }
}

