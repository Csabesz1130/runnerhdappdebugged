package org.example.views;

import org.example.controllers.CompanyController;
import org.example.models.Company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TaskView extends JPanel {
    private CompanyController companyController;
    private Company company;

    private JLabel telephelyLabel;
    private JLabel statuszLabel;
    private JComboBox<String> statuszComboBox;
    private JTextArea megjegyzesTextArea;
    private JButton frissitesButton;

    public TaskView(CompanyController companyController, Company company) {
        this.companyController = companyController;
        this.company = company;
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        JPanel infoPanel = new JPanel(new GridLayout(3, 2));
        telephelyLabel = new JLabel("Telephely: " + company.getCompanyName());
        statuszLabel = new JLabel("Státusz:");
        statuszComboBox = new JComboBox<>(new String[]{"Felderítés", "Telepíthető", "Kirakható", "Nem rakható ki"});
        megjegyzesTextArea = new JTextArea(5, 20);
        frissitesButton = new JButton("Frissítés");

        infoPanel.add(telephelyLabel);
        infoPanel.add(new JLabel()); // Empty label for spacing
        infoPanel.add(statuszLabel);
        infoPanel.add(statuszComboBox);
        infoPanel.add(new JLabel("Megjegyzés:"));
        infoPanel.add(new JScrollPane(megjegyzesTextArea));

        add(infoPanel, BorderLayout.CENTER);
        add(frissitesButton, BorderLayout.SOUTH);

        frissitesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String ujStatusz = (String) statuszComboBox.getSelectedItem();
                String megjegyzes = megjegyzesTextArea.getText();
                companyController.updateTask(company, ujStatusz, megjegyzes);
            }
        });
    }
}
