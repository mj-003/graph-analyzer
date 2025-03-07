package graph;

import GUI.GraphView;
import common.Vertex;

import java.awt.*;
import java.util.ArrayList;

public class RelativeNeighborhoodGraph extends AbstractGraph {
    private static final int CIRCLE_RADIUS = 10;

    public RelativeNeighborhoodGraph() {

    }


    @Override
    public void generate(int maxWidth, int maxHeight, int vertexCount, int edgeCount, int maxWeight, boolean addNeighbors) {
        generateVertices(vertexCount, maxWidth, maxHeight);
        generateRNGEdges(addNeighbors);

    }

    static void generateRNGEdges(boolean addNeighbors) {
        ArrayList<Vertex> rngVertices = new ArrayList<>(vertices);
        for (int i = 0; i < rngVertices.size(); i++) {
            Vertex v1 = rngVertices.get(i);

            for (int j = 0; j < rngVertices.size(); j++) {
                Vertex v2 = rngVertices.get(j);

                if (v1 == v2) {
                    continue;
                }

                double d_v1v2 = v1.distanceTo(v2.getPoint());
                boolean isCloser = true;

                for (Vertex v3 : rngVertices) {
                    if (v1 == v3 || v2 == v3) {
                        continue;
                    }

                    double d_v1v3 = v1.distanceTo(v3.getPoint());
                    double d_v3v2 = v3.distanceTo(v2.getPoint());

                    if (Math.max(d_v1v3, d_v3v2) < d_v1v2) {
                        isCloser = false;
                        break;
                    }
                }

                if (isCloser) {
                    v1.addClosestNeighbor(v2);
                    if (addNeighbors) {
                        v1.addNeighbor(v2, (int) d_v1v2);
                    }
                }
            }
        }
    }

    public void paint(Graphics2D g2, ArrayList<Vertex> vertices, ArrayList<Point> shortestPath, ArrayList<Vertex> highlightedCycle) {

        // graf do porownania
        Stroke oldStroke = g2.getStroke();
        if (GraphView.compareCheckBox.isSelected()) {
            g2.setColor(Color.LIGHT_GRAY);
            for (Vertex vertex : vertices) {
                if (!vertex.getGabrielNeighbors().isEmpty())
                    for (Vertex neighbor : vertex.getGabrielNeighbors())
                        drawArrow2(g2, vertex.getPoint().x, vertex.getPoint().y, neighbor.getPoint().x, neighbor.getPoint().y);
            }
            g2.setColor(customColor1);
            g2.setStroke(new BasicStroke(2));
        }
        else g2.setColor(Color.LIGHT_GRAY);

        // rysowanie krawedzi
        for (Vertex vertex : vertices) {
            if (!vertex.getClosestNeighbor().isEmpty())
                for (Vertex neighbor : vertex.getClosestNeighbor()) {
                    drawArrow2(g2, vertex.getPoint().x, vertex.getPoint().y, neighbor.getPoint().x, neighbor.getPoint().y);
                }
        }

        // rysowanie wiezcholkow
        g2.setStroke(oldStroke);
        g2.setColor(Color.DARK_GRAY);
        drawVertex(g2, vertices);

        // rysowanie cyklu
        drawHighlightedCycle(g2, highlightedCycle, oldStroke, true);

        // rysowanie najkrotszej sciezki
        if (shortestPath != null) {  //if (!shortestPath.isEmpty()) {
            g2.setColor(customColor1);
            drawShortestPath(g2, shortestPath, oldStroke, false);
        }
    }

    void drawVertex(Graphics2D g2, ArrayList<Vertex> vertices) {
        g2.setColor(Color.DARK_GRAY);
        for (Vertex vertex : vertices) {
            int x = vertex.getPoint().x - (CIRCLE_RADIUS ) / 2;
            int y = vertex.getPoint().y - (CIRCLE_RADIUS) / 2;
            g2.fillOval(x, y, CIRCLE_RADIUS , CIRCLE_RADIUS );
            g2.setColor(Color.WHITE);
            g2.setColor(Color.DARK_GRAY);
            g2.drawString(Integer.toString(vertex.getValue()), x, y);
        }

        g2.setColor(customColor1);
        g2.setStroke(new BasicStroke(3));
    }

}
