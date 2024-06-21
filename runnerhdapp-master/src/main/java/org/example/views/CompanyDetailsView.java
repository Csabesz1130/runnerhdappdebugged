package org.example.views;

import org.example.controllers.CompanyController;
import org.example.models.Company;

import javax.swing.*;
import java.awt.*;

public class CompanyDetailsView extends JPanel {
    private final CompanyController companyController;
    private Company company;

    private JLabel companyNameLabel;
    private JLabel programNameLabel;
    private JTextArea detailsArea;

    public CompanyDetailsView(CompanyController companyController) {
        this.companyController = companyController;
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        JPanel infoPanel = new JPanel(new GridLayout(3, 1));
        companyNameLabel = new JLabel();
        programNameLabel = new JLabel();
        detailsArea = new JTextArea();
        detailsArea.setEditable(false);

        infoPanel.add(companyNameLabel);
        infoPanel.add(programNameLabel);
        infoPanel.add(new JScrollPane(detailsArea));

        add(infoPanel, BorderLayout.CENTER);

        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> closeDetails());
        add(closeButton, BorderLayout.SOUTH);
    }

    public void setCompany(Company company) {
        this.company = company;
        updateView();
    }

    private void updateView() {
        if (company != null) {
            companyNameLabel.setText("Company Name: " + company.getCompanyName());
            programNameLabel.setText("Program: " + company.getProgramName());
            detailsArea.setText("ID: " + company.getId() + "\n"
                    + "Last Modified: " + company.getLastModified() + "\n"
                    + "Equipment: " + company.getEquipmentList().size() + " items");
            System.out.println("Updated CompanyDetailsView for company: " + company.getId());
        } else {
            System.out.println("No company set in CompanyDetailsView");
        }
    }

    private void closeDetails() {
        System.out.println("Closing company details view");
    }
}