package test; 

import java.awt.EventQueue;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class cru extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTable table;
    private JTextField textField;
    private JTextField textField_1;
    private JTextField textField_2;
    private JTextField textField_3;
    private Connection connection;

    // Main Method
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    cru frame = new cru();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    // Constructeur de la fenêtre
    public cru() {
        // Initialisation de la connexion à la base de données
        initializeDB();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 685, 545);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        table = new JTable();
        table.setBounds(10, 184, 651, 313);
        contentPane.add(table);

        // Bouton Créer
        JButton btnNewButton = new JButton("CREER");
        btnNewButton.setForeground(new Color(255, 255, 255));
        btnNewButton.setBackground(Color.GREEN);
        btnNewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                createUser();
                displayUsers();
            }
        });
        btnNewButton.setBounds(10, 23, 123, 23);
        contentPane.add(btnNewButton);

        // Bouton Mise à jour
        JButton btnMiseAJour = new JButton("MISE A JOUR");
        btnMiseAJour.setForeground(new Color(255, 255, 255));
        btnMiseAJour.setBackground(Color.CYAN);
        btnMiseAJour.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                updateUser();
                displayUsers();
            }
        });
        btnMiseAJour.setBounds(511, 23, 123, 23);
        contentPane.add(btnMiseAJour);

        // Bouton Supprimer
        JButton btnSupprimer = new JButton("SUPPRIMER");
        btnSupprimer.setForeground(new Color(255, 255, 255));
        btnSupprimer.setBackground(new Color(255, 0, 0));
        btnSupprimer.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                deleteUser();
                displayUsers();
            }
        });
        btnSupprimer.setBounds(347, 23, 123, 23);
        contentPane.add(btnSupprimer);

        // Bouton Modifier
        JButton btnModifier = new JButton("MODIFIER");
        btnModifier.setForeground(new Color(255, 255, 255));
        btnModifier.setBackground(Color.BLUE);
        btnModifier.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                readUser();
            }
        });
        btnModifier.setBounds(170, 23, 123, 23);
        contentPane.add(btnModifier);

        // Étiquettes
        JLabel lblNewLabel = new JLabel("ID");
        lblNewLabel.setBounds(30, 77, 66, 23);
        contentPane.add(lblNewLabel);

        JLabel lblNom = new JLabel("NOM");
        lblNom.setBounds(30, 119, 66, 23);
        contentPane.add(lblNom);

        textField = new JTextField();
        textField.setBounds(135, 78, 96, 20);
        contentPane.add(textField);
        textField.setColumns(10);

        textField_1 = new JTextField();
        textField_1.setBounds(135, 120, 96, 20);
        contentPane.add(textField_1);
        textField_1.setColumns(10);

        JLabel lblMotDePasse = new JLabel("MOT DE PASSE");
        lblMotDePasse.setBounds(314, 77, 85, 23);
        contentPane.add(lblMotDePasse);

        JLabel lblEmail = new JLabel("EMAIL");
        lblEmail.setBounds(314, 119, 54, 23);
        contentPane.add(lblEmail);

        textField_2 = new JTextField();
        textField_2.setColumns(10);
        textField_2.setBounds(423, 78, 96, 20);
        contentPane.add(textField_2);

        textField_3 = new JTextField();
        textField_3.setColumns(10);
        textField_3.setBounds(423, 120, 96, 20);
        contentPane.add(textField_3);

        // Affichage initial des utilisateurs
        displayUsers();
    }

    // Initialisation de la connexion à la base de données
    private void initializeDB() {
        try {
            // Charger le driver JDBC SQLite
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:utilisateurs.db");

            // Créer la table si elle n'existe pas
            String createTableSQL = "CREATE TABLE IF NOT EXISTS utilisateurs (" +
                                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                                    "nom TEXT NOT NULL, " +
                                    "email TEXT NOT NULL UNIQUE, " +
                                    "mot_de_passe TEXT NOT NULL)";
            connection.createStatement().execute(createTableSQL);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Méthode pour créer un utilisateur
    private void createUser() {
        try {
            String insertSQL = "INSERT INTO utilisateurs (nom, email, mot_de_passe) VALUES (?, ?, ?)";
            PreparedStatement pstmt = connection.prepareStatement(insertSQL);
            pstmt.setString(1, textField_1.getText());
            pstmt.setString(2, textField_3.getText());
            pstmt.setString(3, textField_2.getText());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Méthode pour lire un utilisateur
    private void readUser() {
        try {
            String selectSQL = "SELECT * FROM utilisateurs WHERE id = ?";
            PreparedStatement pstmt = connection.prepareStatement(selectSQL);
            pstmt.setInt(1, Integer.parseInt(textField.getText()));
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                textField_1.setText(rs.getString("nom"));
                textField_3.setText(rs.getString("email"));
                textField_2.setText(rs.getString("mot_de_passe"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Méthode pour mettre à jour un utilisateur
    private void updateUser() {
        try {
            String updateSQL = "UPDATE utilisateurs SET nom = ?, email = ?, mot_de_passe = ? WHERE id = ?";
            PreparedStatement pstmt = connection.prepareStatement(updateSQL);
            pstmt.setString(1, textField_1.getText());
            pstmt.setString(2, textField_3.getText());
            pstmt.setString(3, textField_2.getText());
            pstmt.setInt(4, Integer.parseInt(textField.getText()));
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Méthode pour supprimer un utilisateur
    private void deleteUser() {
        try {
            String deleteSQL = "DELETE FROM utilisateurs WHERE id = ?";
            PreparedStatement pstmt = connection.prepareStatement(deleteSQL);
            pstmt.setInt(1, Integer.parseInt(textField.getText()));
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Méthode pour afficher tous les utilisateurs
    private void displayUsers() {
        try {
            String selectSQL = "SELECT * FROM utilisateurs";
            PreparedStatement pstmt = connection.prepareStatement(selectSQL);
            ResultSet rs = pstmt.executeQuery();

            DefaultTableModel model = new DefaultTableModel(new String[]{"ID", "Nom", "Email", "Mot de passe"}, 0);
            while (rs.next()) {
                int id = rs.getInt("id");
                String nom = rs.getString("nom");
                String email = rs.getString("email");
                String motDePasse = rs.getString("mot_de_passe");
                model.addRow(new Object[]{id, nom, email, motDePasse});
            }
            table.setModel(model);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
