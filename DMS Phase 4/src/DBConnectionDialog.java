import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnectionDialog extends JDialog {

    private JTextField hostField;
    private JTextField databaseField;
    private JTextField userField;
    private JPasswordField passField;

    private Connection connection = null;

    public DBConnectionDialog(Frame parent) {
        super(parent, "Connect to MySQL Database", true);
        setSize(400, 250);
        setLocationRelativeTo(parent);
        setLayout(new GridLayout(5, 2, 8, 8));

        // ----- Apply Elegant Nimbus Theme -----
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ignored) {}

        Color background = new Color(43, 43, 43);
        Color textColor = Color.WHITE;
        Color gold = new Color(201, 168, 106);
        Color goldHover = new Color(227, 200, 142);

        getContentPane().setBackground(background);

        // ----- Labels & Fields -----
        JLabel hostLabel = new JLabel("Host (e.g., localhost):");
        hostLabel.setForeground(textColor);
        add(hostLabel);
        hostField = new JTextField("localhost");
        styleField(hostField, background, textColor);
        add(hostField);

        JLabel dbLabel = new JLabel("Database Name:");
        dbLabel.setForeground(textColor);
        add(dbLabel);
        databaseField = new JTextField("moviesdb");
        styleField(databaseField, background, textColor);
        add(databaseField);

        JLabel userLabel = new JLabel("Username:");
        userLabel.setForeground(textColor);
        add(userLabel);
        userField = new JTextField("root");
        styleField(userField, background, textColor);
        add(userField);

        JLabel passLabel = new JLabel("Password:");
        passLabel.setForeground(textColor);
        add(passLabel);
        passField = new JPasswordField();
        styleField(passField, background, textColor);
        add(passField);

        // ----- Buttons -----
        JButton connectBtn = new JButton("Connect");
        JButton cancelBtn = new JButton("Cancel");
        styleButton(connectBtn, gold, goldHover);
        styleButton(cancelBtn, gold, goldHover);
        add(connectBtn);
        add(cancelBtn);

        connectBtn.addActionListener(e -> tryConnect());
        cancelBtn.addActionListener(e -> dispose());
    }

    private void styleField(JTextField field, Color bg, Color fg) {
        field.setBackground(bg.darker());
        field.setForeground(fg);
        field.setCaretColor(fg);
    }

    private void styleButton(JButton btn, Color color, Color hover) {
        btn.setBackground(color);
        btn.setForeground(Color.BLACK);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(6, 12, 6, 12));

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) { btn.setBackground(hover); }
            public void mouseExited(java.awt.event.MouseEvent evt) { btn.setBackground(color); }
        });
    }

    private void tryConnect() {
        String host = hostField.getText().trim();
        String database = databaseField.getText().trim();
        String user = userField.getText().trim();
        String pass = new String(passField.getPassword());

        String url = "jdbc:mysql://" + host + "/" + database + "?useSSL=false";

        try {
            connection = DriverManager.getConnection(url, user, pass);
            JOptionPane.showMessageDialog(this, "✅ Connected Successfully!");
            dispose();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "❌ Connection Failed:\n" + ex.getMessage());
        }
    }

    public static Connection showDialog(Frame parent) {
        DBConnectionDialog dialog = new DBConnectionDialog(parent);
        dialog.setVisible(true);
        return dialog.connection;
    }
}
