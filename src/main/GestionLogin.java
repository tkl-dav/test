package main;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.*;

/**
 * Cette classe gère la logique de sauvegarde et de lecture des informations de login.
 * Elle permet de valider les emails, d'ajouter des utilisateurs et de vérifier les informations de login.
 * Les données sont stockées dans un fichier texte.
 */
public class GestionLogin {
    public static String FICHIER_BASE_DONNEES = "base_de_donnees.txt";

    // Expression régulière pour valider un email
    private static final String EMAIL_REGEX = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

    /**
     * Constructeur de la classe GestionLogin.
     * Initialise la base de données si elle n'existe pas.
     */
    public GestionLogin() {
        initialiserBaseDeDonnees();
    }

    /**
     * Initialise la base de données en créant un fichier texte avec des utilisateurs par défaut
     * si le fichier n'existe pas.
     */
    private void initialiserBaseDeDonnees() {
        File fichier = new File(FICHIER_BASE_DONNEES);
        if (!fichier.exists()) {
            try (PrintWriter writer = new PrintWriter(new FileWriter(fichier))) {
                writer.println("admin:admin123:Admin:admin@example.com");
                writer.println("user:user123:User:user@example.com");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Valide le format d'un email en utilisant une expression régulière.
     *
     * @param email L'email à valider.
     * @return true si l'email est valide, false sinon.
     */
    public boolean validerEmail(String email) {
        if (email == null) {
            return false;
        }
        Matcher matcher = EMAIL_PATTERN.matcher(email);
        return matcher.matches();
    }

    /**
     * Vérifie si les informations de login (email et mot de passe) sont valides.
     *
     * @param email      L'email de l'utilisateur.
     * @param motDePasse Le mot de passe de l'utilisateur.
     * @return true si les informations sont valides, false sinon.
     */
    public boolean verifierLogin(String email, String motDePasse) {
        if (!validerEmail(email)) {
            return false; // Email invalide
        }

        Map<String, String> baseDeDonnees = chargerBaseDeDonnees();
        return baseDeDonnees.containsKey(email) && baseDeDonnees.get(email).equals(motDePasse);
    }

    /**
     * Charge les données de la base de données (fichier texte) dans une Map.
     *
     * @return Une Map contenant les emails comme clés et les mots de passe comme valeurs.
     */
    private Map<String, String> chargerBaseDeDonnees() {
        Map<String, String> baseDeDonnees = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FICHIER_BASE_DONNEES))) {
            String ligne;
            while ((ligne = reader.readLine()) != null) {
                String[] parts = ligne.split(":");
                if (parts.length >= 4) {
                    baseDeDonnees.put(parts[2], parts[1]); // Email comme clé, mot de passe comme valeur
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return baseDeDonnees;
    }

    /**
     * Ajoute un nouvel utilisateur à la base de données.
     *
     * @param utilisateur Le nom d'utilisateur.
     * @param motDePasse  Le mot de passe.
     * @param nomComplet  Le nom complet de l'utilisateur.
     * @param email       L'email de l'utilisateur.
     * @throws IllegalArgumentException Si l'email est invalide.
     */
    public void ajouterUtilisateur(String utilisateur, String motDePasse, String nomComplet, String email) {
        if (!validerEmail(email)) {
            throw new IllegalArgumentException("Email invalide");
        }
        try (PrintWriter writer = new PrintWriter(new FileWriter(FICHIER_BASE_DONNEES, true))) {
            writer.println(utilisateur + ":" + motDePasse + ":" + nomComplet + ":" + email);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}