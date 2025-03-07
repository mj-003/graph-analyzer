import GUI.GraphView;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GraphView view = new GraphView();
            view.setVisible(true);
        });
    }
}