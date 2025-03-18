package main;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Cette classe représente l'interface graphique pour l'inscription.
 * Elle permet à l'utilisateur de saisir ses informations personnelles
 * et de s'inscrire dans la base de données.
 */
public class InscriptionInterface extends JFrame {
    private JTextField champNomComplet;
    private JTextField champEmail;
    private JTextField champUtilisateur;
    private JPasswordField champMotDePasse;
    private JPasswordField champConfirmationMotDePasse;
    private JButton boutonValider;
    private JLabel labelMessage;

    /**
     * Constructeur de la classe InscriptionInterface.
     * Initialise l'interface graphique.
     */
    public InscriptionInterface() {
        setTitle("Inscription");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(null);

        // Champ nom complet
        JLabel labelNomComplet = new JLabel("Nom complet:");
        labelNomComplet.setBounds(10, 20, 100, 25);
        panel.add(labelNomComplet);

        champNomComplet = new JTextField(20);
        champNomComplet.setBounds(120, 20, 250, 25);
        panel.add(champNomComplet);

        // Champ email
        JLabel labelEmail = new JLabel("Email:");
        labelEmail.setBounds(10, 50, 100, 25);
        panel.add(labelEmail);

        champEmail = new JTextField(20);
        champEmail.setBounds(120, 50, 250, 25);
        panel.add(champEmail);

        // Champ utilisateur
        JLabel labelUtilisateur = new JLabel("Nom d'utilisateur:");
        labelUtilisateur.setBounds(10, 80, 100, 25);
        panel.add(labelUtilisateur);

        champUtilisateur = new JTextField(20);
        champUtilisateur.setBounds(120, 80, 250, 25);
        panel.add(champUtilisateur);

        // Champ mot de passe
        JLabel labelMotDePasse = new JLabel("Mot de passe:");
        labelMotDePasse.setBounds(10, 110, 100, 25);
        panel.add(labelMotDePasse);

        champMotDePasse = new JPasswordField(20);
        champMotDePasse.setBounds(120, 110, 250, 25);
        panel.add(champMotDePasse);

        // Champ confirmation de mot de passe
        JLabel labelConfirmationMotDePasse = new JLabel("Confirmer mot de passe:");
        labelConfirmationMotDePasse.setBounds(10, 140, 150, 25);
        panel.add(labelConfirmationMotDePasse);

        champConfirmationMotDePasse = new JPasswordField(20);
        champConfirmationMotDePasse.setBounds(160, 140, 210, 25);
        panel.add(champConfirmationMotDePasse);

        // Bouton de validation
        boutonValider = new JButton("Valider");
        boutonValider.setBounds(10, 180, 100, 25);
        panel.add(boutonValider);

        // Label pour les messages
        labelMessage = new JLabel("");
        labelMessage.setBounds(10, 210, 350, 25);
        panel.add(labelMessage);

        // Action pour le bouton de validation
        boutonValider.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nomComplet = champNomComplet.getText();
                String email = champEmail.getText();
                String utilisateur = champUtilisateur.getText();
                String motDePasse = new String(champMotDePasse.getPassword());
                String confirmationMotDePasse = new String(champConfirmationMotDePasse.getPassword());

                GestionLogin gestionLogin = new GestionLogin();

                if (nomComplet.isEmpty() || email.isEmpty() || utilisateur.isEmpty() || motDePasse.isEmpty() || confirmationMotDePasse.isEmpty()) {
                    labelMessage.setText("Erreur: Tous les champs doivent être remplis.");
                } else if (!motDePasse.equals(confirmationMotDePasse)) {
                    labelMessage.setText("Erreur: Les mots de passe ne correspondent pas.");
                } else if (!gestionLogin.validerEmail(email)) {
                    labelMessage.setText("Erreur: Format d'email invalide.");
                } else if (!gestionLogin.validerMotDePasse(motDePasse)) {
                    labelMessage.setText("Erreur: Le mot de passe doit contenir au moins 12 caractères, une majuscule, un chiffre et un caractère spécial (@#$%^&*).");
                } else {
                    gestionLogin.ajouterUtilisateur(utilisateur, motDePasse, nomComplet, email);
                    labelMessage.setText("Inscription réussie!");
                }
            }
        });

        add(panel);
    }
}