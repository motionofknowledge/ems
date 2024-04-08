package com.ems;

import javax.swing.*;
import java.awt.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.NumberFormatter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class Main {

    public static void main(String[] args) {
        // Set look and feel to Nimbus for a modern appearance
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        }


        JFrame frame = new JFrame("Employee Management System");

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);

        // Center the frame on the screen
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel();

        panel.setLayout(new GridLayout(7, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        Font labelFont = new Font("Arial", Font.BOLD, 14);
        Font fieldFont = new Font("Arial", Font.PLAIN, 14);

        JLabel idLabel = new JLabel("Employee ID:");
        idLabel.setFont(labelFont);

        JTextField idField = new JTextField(10);
        idField.setFont(fieldFont);

        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setFont(labelFont);

        JTextField nameField = new JTextField(20);
        nameField.setFont(fieldFont);

        JLabel contactLabel = new JLabel("Contact No:");
        contactLabel.setFont(labelFont);

        JTextField contactField = new JTextField(10);
        contactField.setFont(fieldFont);


        JLabel salaryLabel = new JLabel("Salary:");
        salaryLabel.setFont(labelFont);

        NumberFormatter salaryFormatter = new NumberFormatter();
        salaryFormatter.setValueClass(Double.class);
        salaryFormatter.setMinimum(0.0);

        JFormattedTextField salaryField = new JFormattedTextField(salaryFormatter);
        salaryField.setFont(fieldFont);
        salaryField.setColumns(10);

        JLabel departmentLabel = new JLabel("Department:");
        departmentLabel.setFont(labelFont);

        String[] departments = {"Select Department", "IT", "Sale", "Purchase", "Marketing", "HR", "Admin"};

        JComboBox<String> departmentComboBox = new JComboBox<>(departments);
        departmentComboBox.setFont(fieldFont);

        JButton addButton = new JButton("Add Employee");
        addButton.setFont(labelFont);

        JButton updateButton = new JButton("Update Employee");
        updateButton.setFont(labelFont);

        JButton viewButton = new JButton("View All Employees");
        viewButton.setFont(labelFont);

        JButton deleteButton = new JButton("Delete Employee");
        deleteButton.setFont(labelFont);

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Get values from text fields and combo box
                String name = nameField.getText();
                String contactNo = contactField.getText();
                double salary = 0.0;
                try {
                    salary = Double.parseDouble(salaryField.getText().replace(",", ""));
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Please enter a valid salary");
                    return; // Exit the method if salary parsing fails
                }
                String department = (String) departmentComboBox.getSelectedItem();

                // Insert values into the database
                try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/employee_management",
                        "root", "Shiv@321")) {
                    String sql = "INSERT INTO employees (name, contact_no, salary, department) VALUES (?, ?, ?, ?)";
                    try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                        preparedStatement.setString(1, name);
                        preparedStatement.setString(2, contactNo);
                        preparedStatement.setDouble(3, salary);
                        preparedStatement.setString(4, department);
                        preparedStatement.executeUpdate();


                        // Clear text fields
                        idField.setText("");
                        nameField.setText("");
                        contactField.setText(null);
                        salaryField.setValue(null);

                        // Reset departmentComboBox to the first item
                        departmentComboBox.setSelectedIndex(0);


                        JOptionPane.showMessageDialog(null, "Employee added successfully");
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Error: Unable to add employee");
                }
            }
        });


        // Add this code after the creation of viewButton

        viewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/employee_management",
                        "root", "Shiv@321")) {
                    String sql = "SELECT * FROM employees";
                    try (PreparedStatement preparedStatement = connection.prepareStatement(sql);
                         ResultSet resultSet = preparedStatement.executeQuery()) {

                        // Create a table model to hold the data
                        DefaultTableModel tableModel = new DefaultTableModel();
                        JTable table = new JTable(tableModel);

                        // Add columns to the table model
                        tableModel.addColumn("Employee ID");
                        tableModel.addColumn("Name");
                        tableModel.addColumn("Contact No");
                        tableModel.addColumn("Salary");
                        tableModel.addColumn("Department");

                        // Add rows to the table model from the result set
                        while (resultSet.next()) {
                            Object[] row = new Object[5];
                            row[0] = resultSet.getString("id");
                            row[1] = resultSet.getString("name");
                            row[2] = resultSet.getString("contact_no");
                            row[3] = resultSet.getDouble("salary");
                            row[4] = resultSet.getString("department");
                            tableModel.addRow(row);
                        }

                        // Create a scroll pane to hold the table
                        JScrollPane scrollPane = new JScrollPane(table);

                        // Create a new window to display the table
                        JFrame tableFrame = new JFrame("Employee Data");
                        tableFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                        tableFrame.setSize(800, 600);
                        tableFrame.add(scrollPane);
                        tableFrame.setVisible(true);
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Error: Unable to fetch employee data");
                }
            }
        });


        // Add this code after the creation of updateButton

        // Inside your Main class
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                UpdateForm updateForm = new UpdateForm();
            }
        });


        // Open DeleteEmployee
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DeleteEmployee deleteEmployee = new DeleteEmployee();
            }
        });




        panel.add(idLabel);
        panel.add(idField);

        panel.add(nameLabel);
        panel.add(nameField);

        panel.add(contactLabel);
        panel.add(contactField);

        panel.add(salaryLabel);
        panel.add(salaryField);

        panel.add(departmentLabel);
        panel.add(departmentComboBox);

        // Buttons
        panel.add(addButton);
        panel.add(updateButton);
        panel.add(viewButton);
        panel.add(deleteButton);

        frame.getContentPane().add(BorderLayout.CENTER, panel);
        frame.setVisible(true);
    }


}
