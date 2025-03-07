package GUI;

import common.Vertex;
import graph.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

public class GraphView extends JFrame {
    static final int PANEL_WIDTH = 800;
    static final int PANEL_HEIGHT = 600;
    static final int CIRCLE_RADIUS = 30;
    static final int RIGHT_PANEL_WIDTH = 210;

    GraphPanel graphPanel;
    JButton addVertexButton;
    JButton addEdgeButton;
    JButton clearButton;
    JButton generateRNGBtn;
    JButton generateGGGBtn;
    JButton findCyclesBtn;
    JButton generateRandomGraphButton;
    JButton findShortestPathButton;
    GraphModel rngModel;
    GraphModel ggModel;
    JLabel enterVertices;
    public static JCheckBox showGabrielCirclesCheckBox;
    static JCheckBox showLabelsCheckBox;
    public static JCheckBox compareCheckBox;
    private final JTextField numVerticesField;
    private static JComboBox<Vertex> startVertexComboBox;
    private static JComboBox<Vertex> endVertexComboBox;
    private final JTextArea outputArea;
    private JLabel messageLabel;
    static AbstractGraph graph;

    public GraphView() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Graph GUI");
        setLayout(new BorderLayout());

        graphPanel = new GraphPanel();
        add(graphPanel, BorderLayout.CENTER);

        JPanel controlPanel = new JPanel();

        // ##########  CONTROL PANEL  ##########
        // tworzenie przyciskow i pol tekstowych

        addVertexButton = new JButton("Add Vertex");
        addVertexButton.setPreferredSize(new Dimension(200, 40));
        addVertexButton.setVisible(false);
        addEdgeButton = new JButton("Add Edge");
        addEdgeButton.setPreferredSize(new Dimension(200, 40));
        addEdgeButton.setVisible(false);
        clearButton = new JButton("Clear");
        clearButton.setPreferredSize(new Dimension(200, 50));
        generateRandomGraphButton = new JButton("Generate Random Graph");
        generateRandomGraphButton.setPreferredSize(new Dimension(200, 50));
        generateRNGBtn = new JButton("Generate RN Graph");
        generateRNGBtn.setPreferredSize(new Dimension(200, 50));
        generateGGGBtn = new JButton("Generate Gabriel Graph");
        generateGGGBtn.setPreferredSize(new Dimension(200, 50));
        numVerticesField = new JTextField(5);
        startVertexComboBox = new JComboBox<>();
        endVertexComboBox = new JComboBox<>();
        findCyclesBtn = new JButton("Find Cycles");
        findCyclesBtn.setPreferredSize(new Dimension(200, 50));
        findShortestPathButton = new JButton("Find Shortest Path");
        findShortestPathButton.setPreferredSize(new Dimension(200, 50));
        outputArea = new JTextArea(5, 20);
        showGabrielCirclesCheckBox = new JCheckBox("Show Circles");
        showLabelsCheckBox = new JCheckBox("Show Labels");
        showGabrielCirclesCheckBox.setVisible(false);
        compareCheckBox = new JCheckBox("Compare");
        compareCheckBox.setVisible(false);
        enterVertices = new JLabel("Vertices:");
        graph = new AbstractGraph() {
        };


        // dodanie action listinerow do przyciskow i pol tekstowych

        startVertexComboBox.addActionListener(e -> {
            Vertex selectedVertex = (Vertex) startVertexComboBox.getSelectedItem();
            if (selectedVertex != null) {
                graphPanel.getModel().setStartVertex();
            }
        });

        endVertexComboBox.addActionListener(e -> {
            Vertex selectedVertex = (Vertex) endVertexComboBox.getSelectedItem();
            if (selectedVertex != null) {
                graphPanel.getModel().setEndVertex();
            }
        });

        addVertexButton.addActionListener(e -> {
            outputArea.setText("");
            graphPanel.setAddingVertex(true);
            drawGraph(graphPanel.getModel());
        });

        addEdgeButton.addActionListener(e -> {
            graphPanel.setAddingEdge(true);
            drawGraph(graphPanel.getModel());
        });

        clearButton.addActionListener(e -> {
            graphPanel.clearGraph();
            messageLabel.setText("");
            graphPanel.getModel().setHighlightedCycle(null);
            clearComboBoxes();
            showGabrielCirclesCheckBox.setVisible(false);
            compareCheckBox.setVisible(false);
            numVerticesField.setText("");
        });

        generateRandomGraphButton.addActionListener(e -> {
            graphPanel.getModel().getVertices().clear();
            graph = new RandomGraph();
            GraphPanel.shortestPath.clear();
            messageLabel.setText("");
            JPanel inputPanel = new JPanel();
            inputPanel.setLayout(new GridLayout(1, 2));
            JTextField numEdgesInput = new JTextField(5);
            inputPanel.add(new JLabel("Edges:"));
            inputPanel.add(numEdgesInput);

            int result = JOptionPane.showConfirmDialog(null, inputPanel, "Enter Number of Edges",
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (result == JOptionPane.OK_OPTION) {
                int numVertices = Integer.parseInt(numVerticesField.getText());
                int numEdges = Integer.parseInt(numEdgesInput.getText());
                graphPanel.generateGraph(graph, numVertices, numEdges);
                drawGraph(graphPanel.getModel());

            }
            compareCheckBox.setVisible(false);
            showGabrielCirclesCheckBox.setVisible(false);
            addVertexButton.setVisible(true);
            addEdgeButton.setVisible(true);
        });


        generateRNGBtn.addActionListener(e -> {
            graphPanel.getModel().getVertices().clear();
            graph = new RelativeNeighborhoodGraph();
            GraphPanel.shortestPath.clear();
            messageLabel.setText("");
            rngModel = graphPanel.getModel();
            graphPanel.setModel(rngModel);
            int numVertices = Integer.parseInt(numVerticesField.getText());
            graphPanel.generateGraph(graph, numVertices, 0);
            drawGraph(graphPanel.getModel());
            graphPanel.getModel().setHighlightedCycle(null);
            graphPanel.setModel(rngModel);
            compareCheckBox.setVisible(false);
            showGabrielCirclesCheckBox.setVisible(false);
            addVertexButton.setVisible(false);
            addEdgeButton.setVisible(false);

        });

        generateGGGBtn.addActionListener(e -> {
            graphPanel.getModel().getVertices().clear();
            graph = new GabrielGraph();
            GraphPanel.shortestPath.clear();
            messageLabel.setText("");
            ggModel = graphPanel.getModel();
            int numVertices = Integer.parseInt(numVerticesField.getText());
            graphPanel.getModel().setHighlightedCycle(null);
            graphPanel.generateGraph(graph, numVertices, 0);
            drawGraph(graphPanel.getModel());
            compareCheckBox.setVisible(true);
            showGabrielCirclesCheckBox.setVisible(true);
            addVertexButton.setVisible(false);
            addEdgeButton.setVisible(false);

        });


        findCyclesBtn.addActionListener(e -> {
            messageLabel.setText("");
            GraphPanel.shortestPath.clear();
            graphPanel.repaint();
            Map<List<Vertex>, Double> cycles = graphPanel.getModel().findCycles();
            int totalCycles = cycles.size();
            if (totalCycles == 0) {
                messageLabel.setText("No cycles found.");
            } else {
                JPanel cyclesPanel = new JPanel();
                cyclesPanel.setLayout(new BoxLayout(cyclesPanel, BoxLayout.Y_AXIS));

                int i = 1;
                for (Map.Entry<List<Vertex>, Double> entry : cycles.entrySet()) {
                    List<Vertex> cycle = entry.getKey();
                    double cycleWeight = entry.getValue();

                    JButton cycleButton = new JButton("Cycle " + i + " weight: " + formatWeight(cycleWeight));
                    cycleButton.addActionListener(event -> {
                        graphPanel.getModel().setHighlightedCycle(cycle);
                        graphPanel.repaint();
                    });
                    cyclesPanel.add(cycleButton);

                    i++;
                }

                JScrollPane scrollPane = new JScrollPane(cyclesPanel);
                scrollPane.setPreferredSize(new Dimension(250, 350));

                JDialog dialog = new JDialog();
                dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                dialog.setTitle("Cycles");
                dialog.getContentPane().add(scrollPane);
                dialog.pack();
                dialog.setLocationRelativeTo(null);
                dialog.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosing(WindowEvent e) {
                        cyclesWindowClosed(graphPanel.getModel());
                        graphPanel.getModel().setHighlightedCycle(null);
                        messageLabel.setText("");
                        graphPanel.repaint();
                    }
                });
                dialog.setVisible(true);

                messageLabel.setText("Cycles found: " + totalCycles);
                graphPanel.getModel().setCycles(cycles);
                graphPanel.repaint();
            }
        });

        findShortestPathButton.addActionListener(e -> {
            messageLabel.setText("");
            GraphPanel.shortestPath.clear();
            JPanel inputPanel = new JPanel();
            inputPanel.setLayout(new GridLayout(2, 2));
            JComboBox<Vertex> startVertexInput = new JComboBox<>();
            JComboBox<Vertex> endVertexInput = new JComboBox<>();
            for (Vertex vertex : graphPanel.getModel().getVertices()) {
                startVertexInput.addItem(vertex);
                endVertexInput.addItem(vertex);
            }
            inputPanel.add(new JLabel("Start Vertex:"));
            inputPanel.add(startVertexInput);
            inputPanel.add(new JLabel("End Vertex:"));
            inputPanel.add(endVertexInput);

            int result = JOptionPane.showConfirmDialog(null, inputPanel, "Enter Start and End Vertex",
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (result == JOptionPane.OK_OPTION) {
                Vertex startVertex = (Vertex) startVertexInput.getSelectedItem();
                Vertex endVertex = (Vertex) endVertexInput.getSelectedItem();
                if (startVertex != null && endVertex != null) {
                    List<Vertex> shortestPath = graphPanel.getModel().findShortestPath(startVertex, endVertex);
                    if (shortestPath.isEmpty() || shortestPath.size() == 1) {
                        messageLabel.setText("Path not found.");
                    } else {
                        int length = graphPanel.getModel().shortestPathLength(shortestPath);
                        messageLabel.setText("Path length: " + length);
                        graphPanel.setShortestPath(shortestPath);
                        graphPanel.repaint();
                    }
                }
            }
        });

        compareCheckBox.addActionListener(e -> {
            messageLabel.setText("");
            GraphPanel.shortestPath.clear();
            if (rngModel != null || ggModel != null) {
                graphPanel.repaint();
            } else {
                JOptionPane.showMessageDialog(null, "Please generate RNG or GG first.");
            }
        });


        showGabrielCirclesCheckBox.addActionListener(e -> {
            messageLabel.setText("");
            GraphPanel.shortestPath.clear();
            repaint();
        });
        showLabelsCheckBox.addActionListener(e -> repaint());


        // ##########  CONTROL PANEL  ##########
        // dodanie elementow do control panel

        controlPanel.setLayout(new FlowLayout());

        JLabel label = new JLabel("GENERATE GRAPH");
        Font currentFont = label.getFont();
        int fontSize = currentFont.getSize() + 2;
        Font newFont = currentFont.deriveFont((float) fontSize);
        label.setFont(newFont);
        label.setPreferredSize(new Dimension(200, 70));
        label.setHorizontalAlignment(SwingConstants.CENTER);
        controlPanel.add(label);

        controlPanel.add(enterVertices);
        controlPanel.add(numVerticesField);
        controlPanel.add(generateRandomGraphButton);
        controlPanel.add(generateRNGBtn);
        controlPanel.add(generateGGGBtn);

        JLabel labelPath = new JLabel("OTHER OPTIONS");
        labelPath.setFont(newFont);
        labelPath.setPreferredSize(new Dimension(200, 70));
        labelPath.setHorizontalAlignment(SwingConstants.CENTER);
        controlPanel.add(labelPath);

        controlPanel.add(findShortestPathButton);
        controlPanel.add(findCyclesBtn);

        controlPanel.add(clearButton);
        //controlPanel.add(compareCheckBox);
//        controlPanel.add(addVertexButton);
//        controlPanel.add(addEdgeButton);

        messageLabel = new JLabel();
        messageLabel.setForeground(AbstractGraph.customColor1);
        messageLabel.setFont(newFont.deriveFont(Font.BOLD));


        // ##########  RIGHT PANEL  ##########
        // dodanie control panel do right panel

        JPanel rightPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                int width = getWidth();
                int height = getHeight();
                int margin = 10;

                g.setColor(Color.LIGHT_GRAY);
                g.fillRect(margin, margin, width - 2 * margin, height - 2 * margin);
            }
        };


        rightPanel.setPreferredSize(new Dimension(RIGHT_PANEL_WIDTH, PANEL_HEIGHT));
        rightPanel.setLayout(new BorderLayout());
        rightPanel.add(controlPanel, BorderLayout.CENTER);

        add(rightPanel, BorderLayout.EAST);

        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.Y_AXIS));
        buttonsPanel.add(showGabrielCirclesCheckBox);
        buttonsPanel.add(compareCheckBox);

        // Dodanie panelu z przyciskami na dole kontrolnego panelu
        controlPanel.add(buttonsPanel, BorderLayout.SOUTH);
        controlPanel.add(messageLabel, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);
    }

    // Metoda wywoływana po zamknięciu okna cykli
    private void cyclesWindowClosed(GraphModel graphModel) {
        graphModel.clearCycles(); // Wyczyść cykle w modelu grafu
    }

    private String formatWeight(double weight) {
        DecimalFormat df = new DecimalFormat("#.##");
        return df.format(weight);
    }
    public void drawGraph(GraphModel model) {
        graphPanel.setModel(model);
        updateVertexComboBoxes(model.getVertices());
        graphPanel.repaint();

    }
    public static void updateVertexComboBoxes(List<Vertex> vertices) {
        DefaultComboBoxModel<Vertex> startComboBoxModel = new DefaultComboBoxModel<>();
        DefaultComboBoxModel<Vertex> endComboBoxModel = new DefaultComboBoxModel<>();

        for (Vertex vertex : vertices) {
            startComboBoxModel.addElement(vertex);
            endComboBoxModel.addElement(vertex);
        }

        startVertexComboBox.setModel(startComboBoxModel);
        endVertexComboBox.setModel(endComboBoxModel);
    }

    public void clearComboBoxes() {
        startVertexComboBox.removeAllItems();
        endVertexComboBox.removeAllItems();
    }

}
