package graph;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.Random;

import GUI.GraphView;
import common.Vertex;
public class GabrielGraph extends AbstractGraph {

    private static final int CIRCLE_RADIUS = 10;

    public GabrielGraph() {
    }

    @Override
    public void generate(int maxWidth, int maxHeight, int vertexCount, int edgeCount, int maxWeight, boolean addNeighbors) {
        generateVertices(vertexCount, maxWidth, maxHeight);
        generateGGEdges(addNeighbors);

    }


    public void generateGGEdges(boolean addNeighbors) {
        ArrayList<Vertex> ggVertices = new ArrayList<>(vertices);

        for (int i = 0; i < ggVertices.size(); i++) {
            Vertex v1 = ggVertices.get(i);

            for (int j = 0; j < ggVertices.size(); j++) {
                Vertex v2 = ggVertices.get(j);

                if (v1 == v2) {
                    continue;
                }

                double d_v1v2 = v1.distanceTo(v2.getPoint());
                boolean isGabrielEdge = true;

                for (Vertex v3 : ggVertices) {
                    if (v1 == v3 || v2 == v3) {
                        continue;
                    }

                    double centerX = (v1.getPoint().x + v2.getPoint().x) / 2.0;
                    double centerY = (v1.getPoint().y + v2.getPoint().y) / 2.0;
                    double r = Math.sqrt(Math.pow(v1.getPoint().x - v2.getPoint().x, 2) + Math.pow(v1.getPoint().y - v2.getPoint().y, 2)) / 2.0;

                    double dx_Ov3 = v3.getPoint().x - centerX;
                    double dy_Ov3 = v3.getPoint().y - centerY;
                    double distance_Ov3 = Math.sqrt(dx_Ov3 * dx_Ov3 + dy_Ov3 * dy_Ov3);

                    if (distance_Ov3 <= r) {
                        isGabrielEdge = false;
                        break;
                    }

                }

                if (isGabrielEdge) {
                    v1.addGabrielNeighbor(v2);
                    if (addNeighbors) {
                        v1.addNeighbor(v2, (int) d_v1v2);
                    }
                }
            }
        }
    }

    public void paint(Graphics2D g2, ArrayList<Vertex> vertices, ArrayList<Point> shortestPath, ArrayList<Vertex> highlightedCycle) {
        Stroke oldStroke = g2.getStroke();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Rysowanie okręgów dla par wierzchołków
        if (GraphView.showGabrielCirclesCheckBox.isSelected()) {
            g2.setStroke(oldStroke);
            g2.setColor(customColor4);
            for (Vertex vertex : vertices) {
                for (Vertex neighbor : vertex.getGabrielNeighbors()) {
                    int centerX = (vertex.getPoint().x + neighbor.getPoint().x) / 2;
                    int centerY = (vertex.getPoint().y + neighbor.getPoint().y) / 2;
                    int radius = (int) Math.sqrt(Math.pow(vertex.getPoint().x - neighbor.getPoint().x, 2) + Math.pow(vertex.getPoint().y - neighbor.getPoint().y, 2)) / 2;
                    int x = centerX - radius;
                    int y = centerY - radius;
                    g2.drawOval(x, y, radius * 2, radius * 2);
                }
            }
        }

        g2.setStroke(oldStroke);
        g2.setColor(Color.LIGHT_GRAY);

        // Rysowanie krawędzi
        for (Vertex vertex : vertices) {
            if (!vertex.getGabrielNeighbors().isEmpty()) {
                for (Vertex neighbor : vertex.getGabrielNeighbors()) {
                    drawArrow2(g2, vertex.getPoint().x, vertex.getPoint().y, neighbor.getPoint().x, neighbor.getPoint().y);
                }
            }
        }

        // graf do porownania
        if (GraphView.compareCheckBox.isSelected()) {
            RelativeNeighborhoodGraph.generateRNGEdges(false);
            g2.setColor(customColor1);
            g2.setStroke(new BasicStroke(2));
            for (Vertex vertex : vertices) {
                // System.out.println("rng: " + vertex.getValue() + " " + vertex.getClosestNeighbor());
                if (!vertex.getClosestNeighbor().isEmpty())
                    for (Vertex neighbor : vertex.getClosestNeighbor())
                        drawArrow2(g2, vertex.getPoint().x, vertex.getPoint().y, neighbor.getPoint().x, neighbor.getPoint().y);
            }

        }

        g2.setColor(Color.DARK_GRAY);

        // Rysowanie wierzchołków
        drawVertex(g2, vertices);

        // Rysowanie highlightedCycle
        drawHighlightedCycle(g2, highlightedCycle, oldStroke, true);

        // rysowanie najkrotszej sciezki
        if (shortestPath != null && !shortestPath.isEmpty()) {
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
