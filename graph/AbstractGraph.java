package graph;

import common.Vertex;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.Random;

public abstract class AbstractGraph {
    static ArrayList<Vertex> vertices;
    private static int CIRCLE_RADIUS;
    public static Color customColor1 = new Color(100,149,237);
    static Color customColor4 = new Color(209,224,238 );


    void generateVertices(int vertexCount, int maxWidth, int maxHeight) {
        vertices = new ArrayList<>();
        //vertices.clear();

        Random random = new Random();

        int minDistance = (int) (Math.min(maxWidth, maxHeight) / (int) Math.sqrt(vertexCount) * 0.7);
        int maxAttempts = vertexCount * 10;

        int attempts = 0;

        while (vertices.size() < vertexCount && attempts < maxAttempts) {
            int x = random.nextInt(maxWidth);
            int y = random.nextInt(maxHeight);

            Point point = new Point(x, y);
            boolean isValid = true;

            for (Vertex vertex : vertices) {
                double distance = vertex.distanceTo(point);
                if (distance < minDistance) {
                    isValid = false;
                    break;
                }
            }

            if (isValid) {
                Vertex vertex = new Vertex(point, vertices.size() + 1);
                vertices.add(vertex);
            }

            attempts++;
        }
    }

    public ArrayList<Vertex> getVertices() {
        return vertices;
    }

    void drawShortestPath(Graphics2D g2, ArrayList<Point> shortestPath, Stroke oldStroke, boolean randomGraph) {
        for (int i = 0; i < shortestPath.size() - 1; i++) {
            g2.setStroke(new BasicStroke(3));
            Point p1 = shortestPath.get(i);
            Point p2 = shortestPath.get(i + 1);
            g2.drawLine(p1.x, p1.y, p2.x, p2.y);
            double distance = p1.distance(p2);
            if (!randomGraph) {
                g2.setFont(g2.getFont().deriveFont(g2.getFont().getSize() - 2f));
                g2.drawString(String.format("%.2f", distance), (p1.x + p2.x) / 2, (p1.y + p2.y) / 2);
                g2.setFont(g2.getFont().deriveFont(g2.getFont().getSize() + 2f));
            }
        }
        g2.setStroke(oldStroke);
    }
    void drawArrow2(Graphics2D g2, int x1, int y1, int x2, int y2) {
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int dx = x2 - x1;
        int dy = y2 - y1;
        double angle = Math.atan2(dy, dx);
        int len = (int) Math.sqrt(dx * dx + dy * dy);

        AffineTransform transform = g2.getTransform();
        g2.translate(x1, y1);
        g2.rotate(angle);
        g2.drawLine(0, 0, len, 0);

        g2.setTransform(transform);
    }

    void drawHighlightedCycle(Graphics2D g2, ArrayList<Vertex> highlightedCycle, Stroke oldStroke, boolean drawWeight) {
        g2.setColor(customColor1);
        Font originalFont = g2.getFont();
        if (highlightedCycle != null && highlightedCycle.size() > 1) {
            for (int i = 0; i < highlightedCycle.size() - 1; i++) {
                Point vertex1 = highlightedCycle.get(i).getPoint();
                Point vertex2 = highlightedCycle.get(i + 1).getPoint();
                drawArrow2(g2, vertex1.x, vertex1.y, vertex2.x, vertex2.y);
                double distance = vertex1.distance(vertex2);
                Font smallerFont = originalFont.deriveFont(originalFont.getSize() - 2f);
                g2.setFont(smallerFont);
                if (drawWeight)
                    g2.drawString(String.format("%.2f", distance), (vertex1.x + vertex2.x) / 2, (vertex1.y + vertex2.y) / 2);
            }
            Point lastVertex = highlightedCycle.get(highlightedCycle.size() - 1).getPoint();
            Point firstVertex = highlightedCycle.get(0).getPoint();
            double distance = firstVertex.distance(lastVertex);
            drawArrow2(g2, lastVertex.x, lastVertex.y, firstVertex.x, firstVertex.y);
            if (drawWeight)
                g2.drawString(String.format("%.2f", distance), (lastVertex.x + firstVertex.x) / 2, (lastVertex.y + firstVertex.y) / 2);
        }
        g2.setStroke(oldStroke);
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

    void drawArrow(Graphics2D g2, int x1, int y1, int x2, int y2) {
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int dx = x2 - x1;
        int dy = y2 - y1;
        double angle = Math.atan2(dy, dx);
        int len = (int) Math.sqrt(dx * dx + dy * dy) - 13;

        AffineTransform transform = g2.getTransform();
        g2.translate(x1, y1);
        g2.rotate(angle);
        g2.drawLine(0, 0, len, 0);

        int arrowSize = 5;
        int[] xPoints = {len, len - arrowSize, len - arrowSize, len};
        int[] yPoints = {0, -arrowSize, arrowSize, 0};
        Polygon arrowHead = new Polygon(xPoints, yPoints, 4);

        g2.setColor(Color.GRAY);
        g2.fillPolygon(arrowHead);

        g2.setTransform(transform);
    }


    public void draw(Graphics2D g2, ArrayList<Vertex> vertices, ArrayList<Point> shortestPath, ArrayList<Vertex> highlightedCycle) {
        paint(g2, vertices, shortestPath, highlightedCycle);
    }

    public void paint(Graphics2D g2, ArrayList<Vertex> vertices, ArrayList<Point> shortestPath, ArrayList<Vertex> highlightedCycle) {
    }

    public void generate(int maxWidth, int maxHeight, int vertexCount, int edgeCount, int maxWeight, boolean addNeighbors) {
    }


    public AbstractGraph() {
    }



}
