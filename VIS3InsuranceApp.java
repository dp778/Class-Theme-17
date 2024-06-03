package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.security.NoSuchAlgorithmException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class VIS3InsuranceApp extends JFrame {


    public VIS3InsuranceApp() {
        setTitle("VIS-3 Insurance App");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel titleLabel = new JLabel("VIS-3 Insurance App");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));

        JPanel loginPanel = new JPanel();
        loginPanel.setLayout(new BoxLayout(loginPanel, BoxLayout.PAGE_AXIS));

        JTextField usernameField = new JTextField(20);
        JPasswordField passwordField = new JPasswordField(20);

        JLabel usernameLabel = new JLabel("Username: ");
        JLabel passwordLabel = new JLabel("Password: ");

        loginPanel.add(usernameLabel);
        loginPanel.add(usernameField);
        loginPanel.add(passwordLabel);
        loginPanel.add(passwordField);

        JButton loginButton = new JButton("Log In");

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Get the username and password from the text fields
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());

                // TODO: Check if the username and password are valid

                try {
                    if (isValidLogin(username, password)) {
                        MainMenu mainMenu = new MainMenu(username);
                        mainMenu.setVisible(true);
                        setVisible(false);
                    } else {
                        JOptionPane.showMessageDialog(VIS3InsuranceApp.this, "Invalid username or password.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (SQLException | NoSuchAlgorithmException throwables) {
                    throwables.printStackTrace();
                }

                usernameField.setText("");
                passwordField.setText("");
            }
        });

        loginPanel.add(loginButton);

        add(titleLabel, BorderLayout.NORTH);
        add(loginPanel, BorderLayout.CENTER);

        setSize(400, 300);
    }

    private boolean isValidLogin(String username, String password) throws SQLException, NoSuchAlgorithmException {
        StringBuilder admin = new StringBuilder();
        ArrayList<String> admins = new ArrayList<>();

        String query = "SELECT * FROM administrators";
        Statement st = DatabaseConnector.getConnection().createStatement();
        ResultSet rs = st.executeQuery(query);

        while (rs.next())
        {
            if(rs.getString("username").equals(username)){
                return Auth.match(password, rs.getString("password"), rs.getString("salt"));
            }
        }


        return false;
    }

    public class MainMenu extends JFrame {
        private String username;

        public MainMenu(String username) {
            this.username = username;

            setTitle("VIS-3 Insurance App - Welcome " + username);
            setSize(400, 500);

            JLabel welcomeLabel = new JLabel("Admin: " + username + " | Select client's insurance category:");
            welcomeLabel.setFont(new Font("Arial", Font.BOLD, 16));

            ButtonGroup categoryGroup = new ButtonGroup();
            JRadioButton carButton = new JRadioButton("Car Insurance");
            JRadioButton propertyButton = new JRadioButton("Property Insurance");
            JRadioButton healthButton = new JRadioButton("Health Insurance");
            JRadioButton travelButton = new JRadioButton("Travel Insurance");

            categoryGroup.add(carButton);
            categoryGroup.add(propertyButton);
            categoryGroup.add(healthButton);
            categoryGroup.add(travelButton);

            JPanel categoryPanel = new JPanel();
            categoryPanel.setLayout(new BoxLayout(categoryPanel, BoxLayout.PAGE_AXIS));
            categoryPanel.add(carButton);
            categoryPanel.add(propertyButton);
            categoryPanel.add(healthButton);
            categoryPanel.add(travelButton);

            JLabel firstNameLabel = new JLabel("First Name:");
            JTextField firstNameField = new JTextField(20);
            JLabel lastNameLabel = new JLabel("Last Name:");
            JTextField lastNameField = new JTextField(20);
            JLabel addressLabel = new JLabel("Address:");
            JTextField addressField = new JTextField(20);
            JLabel emailLabel = new JLabel("Email:");
            JTextField emailField = new JTextField(20);
            JLabel ssnLabel = new JLabel("Social Security Number:");
            JTextField ssnField = new JTextField(20);
            JLabel phoneLabel = new JLabel("Phone Number:");
            JTextField phoneField = new JTextField(20);

            JPanel infoPanel = new JPanel();
            infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.PAGE_AXIS));
            infoPanel.add(firstNameLabel);
            infoPanel.add(firstNameField);
            infoPanel.add(lastNameLabel);
            infoPanel.add(lastNameField);
            infoPanel.add(addressLabel);
            infoPanel.add(addressField);
            infoPanel.add(emailLabel);
            infoPanel.add(emailField);
            infoPanel.add(ssnLabel);
            infoPanel.add(ssnField);
            infoPanel.add(phoneLabel);
            infoPanel.add(phoneField);

            JButton submitButton = new JButton("Submit");

            submitButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Get the selected insurance category and personal information from the fields
                    String category = "";
                    String db_cat = "";

                    if (carButton.isSelected()) {
                        category = "Car Insurance";
                        db_cat = "car_policies";
                    } else if (propertyButton.isSelected()) {
                        category = "Property Insurance";
                        db_cat = "property_policies";
                    } else if (healthButton.isSelected()) {
                        category = "Health Insurance";
                        db_cat = "health_policies";
                    } else if (travelButton.isSelected()) {
                        category = "Travel Insurance";
                        db_cat = "travel_policies";
                    }

                    String firstName = firstNameField.getText();
                    String lastName = lastNameField.getText();
                    String address = addressField.getText();
                    String email = emailField.getText();
                    String ssn = ssnField.getText();
                    String phone = phoneField.getText();

                    String query = "INSERT INTO " + db_cat + " (firstName, lastName, address, email, ssn, phone, insurance, agent_id)"
                            + " VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

                    System.out.println(query);

                    try {
                        PreparedStatement preparedStmt = DatabaseConnector.getConnection().prepareStatement(query);

                        preparedStmt.setString(1, firstName);
                        preparedStmt.setString(2, lastName);
                        preparedStmt.setString(3, address);
                        preparedStmt.setString(4, email);
                        preparedStmt.setString(5, ssn);
                        preparedStmt.setString(6, phone);
                        preparedStmt.setString(7, category);
                        preparedStmt.setString(8, username);

                        preparedStmt.execute();

                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }

                    // TODO: Validate the personal information

                    JOptionPane.showMessageDialog(MainMenu.this, "Thank you for submitting your information.\n\nInsurance Category: " + category + "\n\nPersonal Information:\nFirst Name: " + firstName + "\nLast Name: " + lastName + "\nAddress: " + address + "\nEmail: " + email + "\nSSN: " + ssn + "\nPhone Number: " + phone, "Success", JOptionPane.PLAIN_MESSAGE);

                    // Clear the fields
                    firstNameField.setText("");
                    lastNameField.setText("");
                    addressField.setText("");
                    emailField.setText("");
                    ssnField.setText("");
                    phoneField.setText("");
                }
            });

            JPanel submitPanel = new JPanel();
            submitPanel.add(submitButton);

            JPanel welcomePanel = new JPanel();
            welcomePanel.setLayout(new BoxLayout(welcomePanel, BoxLayout.PAGE_AXIS));
            welcomePanel.add(welcomeLabel);
            welcomePanel.add(categoryPanel);

            add(welcomePanel, BorderLayout.NORTH);
            add(infoPanel, BorderLayout.CENTER);
            add(submitPanel, BorderLayout.SOUTH);
        }
    }

    public static void main(String[] args) {
        VIS3InsuranceApp app = new VIS3InsuranceApp();
        app.setVisible(true);
    }
}