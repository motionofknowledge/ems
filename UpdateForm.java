package com.ems;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class UpdateForm extends JFrame {
    private JTextField idField;
    private JTextField nameField;
    private JTextField contactField;
    private JTextField salaryField;
    private JComboBox<String> departmentComboBox;

    public UpdateForm() {
        setTitle("Update Employee");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Set the location of the frame to the center of the screen
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(7, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        Font labelFont = new Font("Arial", Font.BOLD, 14);
        Font fieldFont = new Font("Arial", Font.PLAIN, 14);

        JLabel idLabel = new JLabel("Employee ID:");
        idLabel.setFont(labelFont);
        idField = new JTextField(10);
        idField.setFont(fieldFont);

        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setFont(labelFont);
        nameField = new JTextField(20);
        nameField.setFont(fieldFont);

        JLabel contactLabel = new JLabel("Contact No:");
        contactLabel.setFont(labelFont);
        contactField = new JTextField(15);
        contactField.setFont(fieldFont);

        JLabel salaryLabel = new JLabel("Salary:");
        salaryLabel.setFont(labelFont);
        salaryField = new JTextField(10);
        salaryField.setFont(fieldFont);

        JLabel departmentLabel = new JLabel("Department:");
        departmentLabel.setFont(labelFont);

        String[] departments = {"Select Department", "IT", "Sale", "Purchase", "Marketing", "HR", "Admin"};
        departmentComboBox = new JComboBox<>(departments);
        departmentComboBox.setFont(fieldFont);

        JButton searchButton = new JButton("Search");
        searchButton.setFont(labelFont);

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String employeeId = idField.getText().trim();

                if (employeeId.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please enter Employee ID");
                    return;
                }

                // Perform database search operation
                searchEmployee(employeeId);
            }
        });

        JButton updateButton = new JButton("Update");
        updateButton.setFont(labelFont);

        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Perform database update operation
                updateEmployee();
            }
        });

        panel.add(idLabel);
        panel.add(idField);
        panel.add(searchButton);
        panel.add(new JLabel()); // Empty label for alignment
        panel.add(nameLabel);
        panel.add(nameField);
        panel.add(contactLabel);
        panel.add(contactField);
        panel.add(salaryLabel);
        panel.add(salaryField);
        panel.add(departmentLabel);
        panel.add(departmentComboBox);
        panel.add(updateButton);

        getContentPane().add(BorderLayout.CENTER, panel);
        setVisible(true);
    }

    private void searchEmployee(String employeeId) {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/employee_management",
                "root", "Shiv@321")) {
            //sql query
            String sql = "SELECT * FROM employees WHERE id = ?";

            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, employeeId);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        String name = resultSet.getString("name");
                        String contactNo = resultSet.getString("contact_no");
                        double salary = resultSet.getDouble("salary");
                        String department = resultSet.getString("department");

                        nameField.setText(name);
                        contactField.setText(contactNo);
                        salaryField.setText(String.valueOf(salary));
                        departmentComboBox.setSelectedItem(department);
                    } else {
                        JOptionPane.showMessageDialog(null, "Employee with ID " + employeeId + " not found");
                    }
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error: Unable to fetch employee details");
        }
    }

    private void updateEmployee() {

        String employeeId = idField.getText().trim();
        String name = nameField.getText().trim();
        String contactNo = contactField.getText().trim();
        String salaryStr = salaryField.getText().trim().replace(",", ""); // Remove commas from salary field
        String department = (String) departmentComboBox.getSelectedItem();

        //validating the form data
        if (employeeId.isEmpty() || name.isEmpty() || contactNo.isEmpty() || salaryStr.isEmpty() || department.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please fill in all fields");
            return;
        }

        double salary;
        try {
            salary = Double.parseDouble(salaryStr);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Please enter a valid salary");
            return;
        }

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/employee_management",
                "root", "Shiv@321")) {
            //sql query
            String sql = "UPDATE employees SET name = ?, contact_no = ?, salary = ?, department = ? WHERE id = ?";

            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

                preparedStatement.setString(1, name);
                preparedStatement.setString(2, contactNo);
                preparedStatement.setDouble(3, salary);
                preparedStatement.setString(4, department);
                preparedStatement.setString(5, employeeId);
                int rowsAffected = preparedStatement.executeUpdate();

                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(null, "Employee details updated successfully");
                } else {
                    JOptionPane.showMessageDialog(null, "No employee found with ID " + employeeId);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error: Unable to update employee details");
        }
    }

}

