package GUI;

import common.Vertex;
import graph.AbstractGraph;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import static GUI.GraphView.*;
import static GUI.GraphView.CIRCLE_RADIUS;

public class GraphPanel extends JPanel {
    private boolean addingVertex;
    private boolean addingEdge;
    private Point startPoint;
    private final ArrayList<Vertex> vertices;
    private GraphModel model;
    static ArrayList<Point> shortestPath;
    private final List<Integer> weights;
    private Vertex draggedVertex;
    private final int MAX_WEIGHT = 10;

    public GraphModel getModel() {
        return model;
    }

    public GraphPanel() {
        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
        setFocusable(true);

        addingVertex = false;
        addingEdge = false;
        vertices = new ArrayList<>();
        model = new GraphModel();
        shortestPath = new ArrayList<>();
        weights = new ArrayList<>();

        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent evt) {
                if (addingVertex) {
                    Vertex newVertex = new Vertex(evt.getPoint());
                    vertices.add(newVertex);
                    model.addVertex(evt.getPoint());
                    draggedVertex = newVertex;
                    addingVertex = false;
                    repaint();
                } else if (addingEdge) {
                    if (startPoint == null) {
                        startPoint = evt.getPoint();
                    } else {
                        Point endPoint = evt.getPoint();
                        int weight = Integer.parseInt(JOptionPane.showInputDialog("Enter the weight of the edge:"));
                        model.addEdge(startPoint, endPoint, weight);
                        weights.add(weight);
                        startPoint = null;
                        addingEdge = false;
                        repaint();
                    }
                } else {
                    draggedVertex = getVertexAtPoint(evt.getPoint());
                }
            }

            public void mouseReleased(MouseEvent evt) {
                draggedVertex = null;
            }
        });

        addMouseMotionListener(new MouseAdapter() {
            public void mouseDragged(MouseEvent evt) {
                if (draggedVertex != null) {
                    dragVertex(evt.getPoint());
                }
            }
        });
    }

    public void setAddingVertex(boolean addingVertex) {
        this.addingVertex = addingVertex;
    }

    public void setAddingEdge(boolean addingEdge) {
        this.addingEdge = addingEdge;
    }

    public void clearGraph() {
        model.clear();
        vertices.clear();
        shortestPath.clear();
        weights.clear();
        repaint();
    }


    public void generateGraph(AbstractGraph graph, int numVertices, int numEdges) {
        vertices.clear();
        model.generateGraphModel(numVertices, numEdges, (int) (PANEL_WIDTH * 0.8), (int) (PANEL_HEIGHT * 0.8), MAX_WEIGHT, graph, true);
        vertices.addAll(graph.getVertices());
    }

    public void setShortestPath(List<Vertex> shortestPath) {
        if (shortestPath == null) {
            GraphPanel.shortestPath = null;
        } else {
            GraphPanel.shortestPath = convertVerticesToPoints(shortestPath);
        }
        repaint();
    }

    public ArrayList<Point> convertVerticesToPoints(List<Vertex> vertices) {
        ArrayList<Point> points = new ArrayList<>();
        for (Vertex vertex : vertices) {
            points.add(vertex.getPoint());
        }
        return points;
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(Color.GRAY);
        centerGraph();
        List<Vertex> vertices = model.getVertices();
        GraphView.graph.draw(g2, (ArrayList<Vertex>) vertices, shortestPath, GraphModel.highlightedCycle);

    }

    public void centerGraph() {
        int minX = Integer.MAX_VALUE;
        int minY = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE;
        int maxY = Integer.MIN_VALUE;

        for (Vertex vertex : vertices) {
            int x = vertex.getPoint().x;
            int y = vertex.getPoint().y;

            minX = Math.min(minX, x);
            minY = Math.min(minY, y);
            maxX = Math.max(maxX, x);
            maxY = Math.max(maxY, y);
        }

        int graphWidth = maxX - minX;
        int graphHeight = maxY - minY;

        int offsetX = (getWidth() - graphWidth) / 2 - minX;
        int offsetY = (getHeight() - graphHeight) / 2 - minY;

        for (Vertex vertex : vertices) {
            Point point = vertex.getPoint();
            point.translate(offsetX, offsetY);
        }
        repaint();
    }

    private Vertex getVertexAtPoint(Point point) {
        for (Vertex vertex : vertices) {
            if (isPointInCircle(point, vertex.getPoint())) {
                return vertex;
            }
        }
        return null;
    }

    private boolean isPointInCircle(Point point, Point circleCenter) {
        int dx = point.x - circleCenter.x;
        int dy = point.y - circleCenter.y;
        int radiusSquared = (CIRCLE_RADIUS / 2) * (CIRCLE_RADIUS / 2);
        return dx * dx + dy * dy <= radiusSquared;
    }

    private void dragVertex(Point point) {
        if (draggedVertex != null) {
            draggedVertex.setPoint(point);
            repaint();
        }
    }

    public void setModel(GraphModel model) {
        this.model = model;
    }

}
