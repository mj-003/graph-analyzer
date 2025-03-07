package graph;

import common.Vertex;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.Map;
import java.util.Random;


public class RandomGraph extends AbstractGraph {
    private static int CIRCLE_RADIUS = 20;

    public RandomGraph() {
    }


    @Override
    public void generate(int maxWidth, int maxHeight, int vertexCount, int edgeCount, int maxWeight, boolean addNeighbors) {
        generateVertices(vertexCount, maxWidth, maxHeight);
        generateRandomEdges(edgeCount, maxWeight);

    }

    private void generateRandomEdges(int edgeCount, int maxWeight) {

        Random random = new Random();
        edgeCount = adjustEdgeNumber(edgeCount, vertices.size());
        for (int i = 0; i < edgeCount; i++) {
            Vertex startVertex;
            Vertex endVertex;
            int weight;
            do {
                startVertex = vertices.get(random.nextInt(vertices.size()));
                endVertex = vertices.get(random.nextInt(vertices.size()));
            } while (startVertex == endVertex || edgeAlreadyExists(startVertex, endVertex));

            weight = random.nextInt(maxWeight);
            startVertex.addNeighbor(endVertex, weight);
        }
    }

    private int adjustEdgeNumber(int edgeCount, int vertexCount) {
        int maxEdgeCount = vertexCount * (vertexCount - 1) / 2;
        return Math.min(edgeCount, maxEdgeCount);
    }

    private boolean edgeAlreadyExists(Vertex startVertex, Vertex endVertex) {
        return (startVertex.getNeighbors().containsKey(endVertex) || endVertex.getNeighbors().containsKey(startVertex));
    }

    public void paint(Graphics2D g2, ArrayList<Vertex> vertices, ArrayList<Point> shortestPath, ArrayList<Vertex> highlightedCycle) {
        CIRCLE_RADIUS = 30;
        for (Vertex vertex : vertices) {
            for (Map.Entry<Vertex, Integer> entry : vertex.getNeighbors().entrySet()) {
                Vertex neighbor = entry.getKey();
                drawArrow(g2, vertex.getPoint().x, vertex.getPoint().y, neighbor.getPoint().x, neighbor.getPoint().y);
                int x = (vertex.getPoint().x + neighbor.getPoint().x) / 2;
                int y = (vertex.getPoint().y + neighbor.getPoint().y) / 2;
                g2.drawString(Integer.toString(entry.getValue()), x, y);
            }
        }

        // rysowanie wiezcholkow
        g2.setColor(Color.DARK_GRAY);
        for (Vertex vertex : vertices) {
            int x = vertex.getPoint().x - CIRCLE_RADIUS / 2;
            int y = vertex.getPoint().y - CIRCLE_RADIUS / 2;
            g2.fillOval(x, y, CIRCLE_RADIUS, CIRCLE_RADIUS);
            g2.setColor(Color.WHITE);
            g2.drawString(Integer.toString(vertex.getValue()), vertex.getPoint().x - 5, vertex.getPoint().y + 5);
            g2.setColor(Color.DARK_GRAY);
        }

        Stroke oldStroke = g2.getStroke();
        // rysowanie najkrotszej sciezki
        if (shortestPath != null) {
            g2.setColor(customColor1);
            drawShortestPath(g2, shortestPath, oldStroke, true);
        }

        g2.setStroke(new BasicStroke(3));
        drawHighlightedCycle(g2, highlightedCycle, oldStroke, false);
    }


}
