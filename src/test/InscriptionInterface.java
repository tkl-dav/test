package test;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class InscriptionInterface extends JFrame {
    private JTextField champNomComplet;
    private JTextField champEmail;
    private JPasswordField champMotDePasse;
    private JPasswordField champConfirmationMotDePasse;
    private JButton boutonValider;
    private JLabel labelMessage;

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

        // Champ mot de passe
        JLabel labelMotDePasse = new JLabel("Mot de passe:");
        labelMotDePasse.setBounds(10, 80, 100, 25);
        panel.add(labelMotDePasse);

        champMotDePasse = new JPasswordField(20);
        champMotDePasse.setBounds(120, 80, 250, 25);
        panel.add(champMotDePasse);

        // Champ confirmation de mot de passe
        JLabel labelConfirmationMotDePasse = new JLabel("Confirmer mot de passe:");
        labelConfirmationMotDePasse.setBounds(10, 110, 150, 25);
        panel.add(labelConfirmationMotDePasse);

        champConfirmationMotDePasse = new JPasswordField(20);
        champConfirmationMotDePasse.setBounds(160, 110, 210, 25);
        panel.add(champConfirmationMotDePasse);

        // Bouton de validation
        boutonValider = new JButton("Valider");
        boutonValider.setBounds(10, 140, 100, 25);
        panel.add(boutonValider);

        // Label pour les messages
        labelMessage = new JLabel("");
        labelMessage.setBounds(10, 170, 350, 25);
        panel.add(labelMessage);

        // Action pour le bouton de validation
        boutonValider.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nomComplet = champNomComplet.getText();
                String email = champEmail.getText();
                String motDePasse = new String(champMotDePasse.getPassword());
                String confirmationMotDePasse = new String(champConfirmationMotDePasse.getPassword());

                GestionLogin gestionLogin = new GestionLogin();

                try {
                    // Validation des champs
                    if (nomComplet.isEmpty() || email.isEmpty() || motDePasse.isEmpty() || confirmationMotDePasse.isEmpty()) {
                        labelMessage.setText("Erreur: Tous les champs doivent être remplis.");
                    } else if (!gestionLogin.validerFormatEmail(email)) {
                        labelMessage.setText("Erreur: Format d'email invalide.");
                    } else if (!motDePasse.equals(confirmationMotDePasse)) {
                        labelMessage.setText("Erreur: Les mots de passe ne correspondent pas.");
                    } else if (!gestionLogin.validerMotDePasse(motDePasse)) {
                        labelMessage.setText("Erreur: Le mot de passe ne respecte pas les critères de sécurité.");
                    } else {
                        gestionLogin.creerUtilisateur(email, motDePasse, nomComplet, email);
                        labelMessage.setText("Inscription réussie!");
                    }
                } catch (GestionLogin.CustomException ex) {
                    labelMessage.setText(ex.getMessage());
                }
            }
        });

        add(panel);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new InscriptionInterface().setVisible(true);
            }
        });
    }
}