package com.ems;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DeleteEmployee extends JFrame {

    private JTextField idField;

    public DeleteEmployee() {
        setTitle("Delete Employee");
        setSize(300, 150);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Set the location of the frame to the center of the screen
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(2, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        Font labelFont = new Font("Arial", Font.BOLD, 14);
        Font fieldFont = new Font("Arial", Font.PLAIN, 14);

        JLabel idLabel = new JLabel("Employee ID:");
        idLabel.setFont(labelFont);
        idField = new JTextField(10);
        idField.setFont(fieldFont);

        JButton deleteButton = new JButton("Delete");
        deleteButton.setFont(labelFont);

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String employeeId = idField.getText().trim();

                // Check if the ID field is empty
                if (employeeId.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please enter Employee ID");
                    return;
                }

                // Confirm deletion with the user
                int confirmation = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this employee?", "Confirmation", JOptionPane.YES_NO_OPTION);

                // If the user confirms the deletion
                if (confirmation == JOptionPane.YES_OPTION) {
                    try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/employee_management",
                            "root", "Shiv@321")) {
                        String sql = "DELETE FROM employees WHERE id = ?";
                        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                            preparedStatement.setString(1, employeeId);
                            int rowsDeleted = preparedStatement.executeUpdate();

                            if (rowsDeleted > 0) {
                                JOptionPane.showMessageDialog(null, "Employee deleted successfully");
                                idField.setText(""); // Clear the ID field after successful deletion
                            } else {
                                JOptionPane.showMessageDialog(null, "No employee found with ID: " + employeeId);
                            }
                        }
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(null, "Error: Unable to delete employee");
                    }
                }
            }
        });

        panel.add(idLabel);
        panel.add(idField);
        panel.add(deleteButton);

        getContentPane().add(BorderLayout.CENTER, panel);
        setVisible(true);
    }

    public static void main(String[] args) {
        new DeleteEmployee();
    }
}

