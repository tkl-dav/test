package main;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class InscriptionInterface extends JFrame {
    private JTextField champNomComplet; /** Champs pour les informations de l'utilisateur*/
    private JTextField champEmail;
    private JTextField champUtilisateur;
    private JPasswordField champMotDePasse;
    private JPasswordField champConfirmationMotDePasse;
    private JButton boutonValider;
    private JLabel labelMessage;

    public InscriptionInterface() {
        setTitle("Inscription");
        setSize(400, 300); /** Augmentation de la taille pour les nouveaux champs*/
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); /**Fermer seulement cette fenêtre*/
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(null);

        JLabel labelNomComplet = new JLabel("Nom complet:"); /** Champ nom complet*/
        labelNomComplet.setBounds(10, 20, 100, 25);
        panel.add(labelNomComplet);

        champNomComplet = new JTextField(20);
        champNomComplet.setBounds(120, 20, 250, 25);
        panel.add(champNomComplet);

        JLabel labelEmail = new JLabel("Email:");/** Champ email*/
        labelEmail.setBounds(10, 50, 100, 25);
        panel.add(labelEmail);

        champEmail = new JTextField(20);
        champEmail.setBounds(120, 50, 250, 25);
        panel.add(champEmail);

        JLabel labelUtilisateur = new JLabel("Nom d'utilisateur:"); /** Champ utilisateur*/
        labelUtilisateur.setBounds(10, 80, 100, 25);
        panel.add(labelUtilisateur);

        champUtilisateur = new JTextField(20);
        champUtilisateur.setBounds(120, 80, 250, 25);
        panel.add(champUtilisateur);

        JLabel labelMotDePasse = new JLabel("Mot de passe:");/** Champ mot de passe*/
        labelMotDePasse.setBounds(10, 110, 100, 25);
        panel.add(labelMotDePasse);

        champMotDePasse = new JPasswordField(20);
        champMotDePasse.setBounds(120, 110, 250, 25);
        panel.add(champMotDePasse);

        JLabel labelConfirmationMotDePasse = new JLabel("Confirmer mot de passe:");/** Champ confirmation de mot de passe*/
        labelConfirmationMotDePasse.setBounds(10, 140, 150, 25);
        panel.add(labelConfirmationMotDePasse);

        champConfirmationMotDePasse = new JPasswordField(20);
        champConfirmationMotDePasse.setBounds(160, 140, 210, 25);
        panel.add(champConfirmationMotDePasse);

        boutonValider = new JButton("Valider");/** Bouton de validation*/
        boutonValider.setBounds(10, 180, 100, 25);
        panel.add(boutonValider);

        labelMessage = new JLabel("");/** Label pour les messages*/
        labelMessage.setBounds(10, 210, 350, 25);
        panel.add(labelMessage);

        /** Action pour le bouton de validation*/
        boutonValider.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) { /** Récupération des valeurs saisies*/
                String nomComplet = champNomComplet.getText();
                String email = champEmail.getText();
                String utilisateur = champUtilisateur.getText();
                String motDePasse = new String(champMotDePasse.getPassword());
                String confirmationMotDePasse = new String(champConfirmationMotDePasse.getPassword());
                /** Validation des champs*/
                if (nomComplet.isEmpty() || email.isEmpty() || utilisateur.isEmpty() || motDePasse.isEmpty() || confirmationMotDePasse.isEmpty()) {
                    labelMessage.setText("Erreur: Tous les champs doivent être remplis.");
                } else if (!motDePasse.equals(confirmationMotDePasse)) {
                    labelMessage.setText("Erreur: Les mots de passe ne correspondent pas.");
                } else {
                    /** Ajout de l'utilisateur à la base de données*/
                    GestionLogin gestionLogin = new GestionLogin();
                    gestionLogin.ajouterUtilisateur(utilisateur, motDePasse, nomComplet, email); /** Appel de la méthode mise à jour*/
                    labelMessage.setText("Inscription réussie!");
                }
            }
        });

        add(panel);
    }
}