package form;
//ian simiyu
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Vector;
import javax.swing.table.DefaultTableModel;

public class form extends JFrame {

    private JTextField idTextField;
    private JTextField nameTextField;
    private JRadioButton maleButton;
    private JRadioButton femaleButton;
    private JTextField addressTextField;
    private JTextField contactTextField;
    private JButton registerButton;
    private JButton exitButton;
    private JTable dataTable;
    private DefaultTableModel tableModel;

    public form() {
        // Initialize the table model with column names
        String[] columnNames = {"ID", "Name", "Gender", "Address", "Contact"};
        tableModel = new DefaultTableModel(columnNames, 0);
        dataTable = new JTable(tableModel);

        // Create UI elements
        idTextField = new JTextField(20);
        nameTextField = new JTextField(20);
        maleButton = new JRadioButton("Male");
        femaleButton = new JRadioButton("Female");
        ButtonGroup genderGroup = new ButtonGroup();
        genderGroup.add(maleButton);
        genderGroup.add(femaleButton);
        addressTextField = new JTextField(20);
        contactTextField = new JTextField(20);
        registerButton = new JButton("Register");
        exitButton = new JButton("Exit");

        // Layout the form
        JPanel leftPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        leftPanel.add(new JLabel("ID:"));
        leftPanel.add(idTextField);
        leftPanel.add(new JLabel("Name:"));
        leftPanel.add(nameTextField);
        leftPanel.add(new JLabel("Gender:"));
        JPanel genderPanel = new JPanel(new FlowLayout());
        genderPanel.add(maleButton);
        genderPanel.add(femaleButton);
        leftPanel.add(genderPanel);
        leftPanel.add(new JLabel("Address:"));
        leftPanel.add(addressTextField);
        leftPanel.add(new JLabel("Contact:"));
        leftPanel.add(contactTextField);
        leftPanel.add(exitButton);
        leftPanel.add(registerButton);

        // Add listeners
        exitButton.addActionListener(e -> dispose());
        registerButton.addActionListener(e -> {
            // Insert data into database and update the table
            insertData();
        });

        // Scroll pane for table
        JScrollPane scrollPane = new JScrollPane(dataTable);
        dataTable.setFillsViewportHeight(true);

        // Split pane to contain the form and the table
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, scrollPane);
        splitPane.setDividerLocation(400);

        // Configure the frame
        add(splitPane);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setVisible(true);
    }

    private void insertData() {
        String id = idTextField.getText();
        String name = nameTextField.getText();
        String gender = maleButton.isSelected() ? "Male" : "Female";
        String address = addressTextField.getText();
        String contact = contactTextField.getText();

        // Add data to the table
        tableModel.addRow(new Object[]{id, name, gender, address, contact});

        // Insert data into the database
        String databaseUrl = "jdbc:mysql://localhost:3306/registrationform";
        String user = "root";
        String password = "";
        String sql = "INSERT INTO registrationform (id, name, gender, address, contact) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(databaseUrl, user, password);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, id);
            stmt.setString(2, name);
            stmt.setString(3, gender);
            stmt.setString(4, address);
            stmt.setString(5, contact);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(form.this, "Data inserted successfully.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(form.this, "Error saving data: " + ex.getMessage());
        }

        // Clear input fields after insertion
        idTextField.setText("");
        nameTextField.setText("");
        genderGroup.clearSelection();
        addressTextField.setText("");
        contactTextField.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new form());
    }
}
