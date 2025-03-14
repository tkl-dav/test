package main;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginInterface extends JFrame {
    private JTextField champUtilisateur;
    private JPasswordField champMotDePasse;
    private JButton boutonLogin, boutonInscription;
    private JLabel labelMessage;

    public LoginInterface() {
        setTitle("Login");
        setSize(300, 250); /** Augmentation de la taille pour le nouveau bouton*/
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(null);

        
        JLabel labelUtilisateur = new JLabel("Utilisateur:"); /** Champ utilisateur*/
        labelUtilisateur.setBounds(10, 20, 80, 25);
        panel.add(labelUtilisateur);

        champUtilisateur = new JTextField(20);
        champUtilisateur.setBounds(100, 20, 165, 25);
        panel.add(champUtilisateur);

        JLabel labelMotDePasse = new JLabel("Mot de passe:");/** Champ mot de passe*/
        labelMotDePasse.setBounds(10, 50, 80, 25);
        panel.add(labelMotDePasse);

        champMotDePasse = new JPasswordField(20);
        champMotDePasse.setBounds(100, 50, 165, 25);
        panel.add(champMotDePasse);

        boutonLogin = new JButton("Login"); /** Bouton de login*/
        boutonLogin.setBounds(10, 80, 80, 25);
        panel.add(boutonLogin);

        boutonInscription = new JButton("S'inscrire");/** Bouton d'inscription*/
        boutonInscription.setBounds(100, 80, 100, 25);
        panel.add(boutonInscription);

        labelMessage = new JLabel("");/** Label pour les messages*/
        labelMessage.setBounds(10, 110, 300, 25);
        panel.add(labelMessage);

        boutonLogin.addActionListener(new ActionListener() { /** Action pour le bouton de login*/
            @Override
            public void actionPerformed(ActionEvent e) {
                String utilisateur = champUtilisateur.getText();
                String motDePasse = new String(champMotDePasse.getPassword());
                GestionLogin gestionLogin = new GestionLogin();
                if (gestionLogin.verifierLogin(utilisateur, motDePasse)) {
                    labelMessage.setText("Login réussi!");
                } else {
                    labelMessage.setText("Erreur: Utilisateur non trouvé.");
                }
            }
        });

        boutonInscription.addActionListener(new ActionListener() {/** Action pour le bouton d'inscription*/
            @Override
            public void actionPerformed(ActionEvent e) {
                new InscriptionInterface().setVisible(true);/** Ouvrir une nouvelle fenêtre pour l'inscription*/
            }
        });

        add(panel);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new LoginInterface().setVisible(true);
            }
        });
    }
}