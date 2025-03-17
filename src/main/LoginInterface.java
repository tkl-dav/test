package main;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Cette classe représente l'interface graphique pour le login.
 * Elle permet à l'utilisateur de saisir son email et son mot de passe,
 * et de se connecter ou de s'inscrire.
 */
public class LoginInterface extends JFrame {
    private JTextField champEmail;
    private JPasswordField champMotDePasse;
    private JButton boutonLogin, boutonInscription;
    private JLabel labelMessage;

    /**
     * Constructeur de la classe LoginInterface.
     * Initialise l'interface graphique.
     */
    public LoginInterface() {
        setTitle("Login");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(null);

        // Champ email
        JLabel labelEmail = new JLabel("Email:");
        labelEmail.setBounds(10, 20, 80, 25);
        panel.add(labelEmail);

        champEmail = new JTextField(20);
        champEmail.setBounds(100, 20, 250, 25);
        panel.add(champEmail);

        // Champ mot de passe
        JLabel labelMotDePasse = new JLabel("Mot de passe:");
        labelMotDePasse.setBounds(10, 50, 80, 25);
        panel.add(labelMotDePasse);

        champMotDePasse = new JPasswordField(20);
        champMotDePasse.setBounds(100, 50, 250, 25);
        panel.add(champMotDePasse);

        // Bouton de login
        boutonLogin = new JButton("Login");
        boutonLogin.setBounds(10, 80, 80, 25);
        panel.add(boutonLogin);

        // Bouton d'inscription
        boutonInscription = new JButton("S'inscrire");
        boutonInscription.setBounds(100, 80, 100, 25);
        panel.add(boutonInscription);

        // Label pour les messages
        labelMessage = new JLabel("");
        labelMessage.setBounds(10, 110, 350, 25);
        panel.add(labelMessage);

        // Action pour le bouton de login
        boutonLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String email = champEmail.getText();
                String motDePasse = new String(champMotDePasse.getPassword());
                GestionLogin gestionLogin = new GestionLogin();

                if (gestionLogin.verifierLogin(email, motDePasse)) {
                    labelMessage.setText("Login réussi!");
                } else {
                    labelMessage.setText("Erreur: Email ou mot de passe incorrect.");
                }
            }
        });

        // Action pour le bouton d'inscription
        boutonInscription.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new InscriptionInterface().setVisible(true);
            }
        });

        add(panel);
    }

    /**
     * Méthode principale pour lancer l'application.
     *
     * @param args Les arguments de la ligne de commande (non utilisés).
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new LoginInterface().setVisible(true);
            }
        });
    }
}