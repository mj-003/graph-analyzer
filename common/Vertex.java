package common;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class Vertex implements Comparable<Vertex> {
    private Point point;
    private int value;
    private int distance;
    private Map<Vertex, Integer> neighbors;
    private ArrayList<Vertex> closestNeighbor;
    private ArrayList<Vertex> gabrielNeighbors;
    boolean highlighted = false;

    public ArrayList<Vertex> getGabrielNeighbors() {
        return gabrielNeighbors;
    }
    public void addGabrielNeighbor(Vertex neighbor) {
        gabrielNeighbors.add(neighbor);
    }

    public ArrayList<Vertex> getClosestNeighbor() {
        return closestNeighbor;
    }
    public void addClosestNeighbor(Vertex neighbor) {
        closestNeighbor.add(neighbor);
    }

    public Vertex(Point point) {
        this.point = point;
        this.neighbors = new HashMap<>();
        closestNeighbor = new ArrayList<>();
        gabrielNeighbors = new ArrayList<>();
    }

    public Vertex(Point point, int value) {
        this.point = point;
        this.value = value;
        this.neighbors = new HashMap<>();
        closestNeighbor = new ArrayList<>();
        gabrielNeighbors = new ArrayList<>();
    }
    public int getValue() {
        return value;
    }

    public void setPoint(Point point) {
        this.point = point;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public Point getPoint() {
        return point;
    }

    public Map<Vertex, Integer> getNeighbors() {
        return neighbors;
    }

    public void addNeighbor(Vertex neighbor, int weight) {
        neighbors.put(neighbor, weight);
    }

    public int getWeightToNeighbor(Vertex neighbor) {
        System.out.println(neighbors);
        for (Map.Entry<Vertex, Integer> entry : neighbors.entrySet()) {
            Vertex n = entry.getKey();
            int v = entry.getValue();
            if (n.equals(neighbor))
                return v;
        }
        return neighbors.getOrDefault(neighbor, -1);
    }
    public double distanceTo(Point otherVertex) {
        return point.distance(otherVertex);
    }

    @Override
    public int compareTo(Vertex other) {
        return Integer.compare(this.distance, other.distance);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((point == null) ? 0 : point.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Vertex other = (Vertex) obj;
        if (point == null) {
            return other.point == null;
        } else return point.equals(other.point);
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    public double weightTo(Vertex other) {
        return point.distance(other.point);
    }



}
