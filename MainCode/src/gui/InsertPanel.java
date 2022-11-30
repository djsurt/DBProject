package gui;

import javax.swing.*;
import java.awt.*;

public class InsertPanel {

    private JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    private JComboBox<Relation> comboBox = new JComboBox<>(Relation.values());

    public InsertPanel() {
        panel.add(comboBox);
    }

    public JPanel panel() {
        return panel;
    }

}
