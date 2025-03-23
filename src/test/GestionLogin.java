package test;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class GestionLogin {
    private static final String FICHIER_BASE_DONNEES = "base_de_donnees.txt";
    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";

    public GestionLogin() {
        initialiserBaseDeDonnees();
    }

    private void initialiserBaseDeDonnees() {
        File fichier = new File(FICHIER_BASE_DONNEES);
        if (!fichier.exists()) {
            try (PrintWriter writer = new PrintWriter(new FileWriter(fichier))) {
                writer.println("admin:admin123:Admin:admin@example.com");
                writer.println("user:user123:User:user@example.com");
            } catch (IOException e) {
                System.err.println("Erreur lors de l'initialisation de la base de données: " + e.getMessage());
            }
        }
    }

    boolean validerFormatEmail(String email) {
        return Pattern.matches(EMAIL_REGEX, email);
    }

    public boolean validerMotDePasse(String motDePasse) {
        if (motDePasse.length() < 12) return false;
        if (!motDePasse.matches(".*[A-Z].*")) return false;
        if (!motDePasse.matches(".*[0-9].*")) return false;
        if (!motDePasse.matches(".*[@#$%^&*].*")) return false;
        return true;
    }

    public boolean emailEstUnique(String email) throws CustomException {
        Map<String, String> baseDeDonnees = chargerBaseDeDonnees();
        return !baseDeDonnees.containsKey(email);
    }

    public void ajouterUtilisateur(String utilisateur, String motDePasse, String nomComplet, String email) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FICHIER_BASE_DONNEES, true))) {
            writer.println(utilisateur + ":" + motDePasse + ":" + nomComplet + ":" + email);
        } catch (IOException e) {
            System.err.println("Erreur lors de l'ajout de l'utilisateur: " + e.getMessage());
        }
    }

    public void creerUtilisateur(String utilisateur, String motDePasse, String nomComplet, String email) throws CustomException {
        if (!emailEstUnique(email)) {
            throw new CustomException("Erreur: L'email existe déjà.");
        }
        if (!validerMotDePasse(motDePasse)) {
            throw new CustomException("Erreur: Le mot de passe ne respecte pas les critères de sécurité.");
        }
        ajouterUtilisateur(utilisateur, motDePasse, nomComplet, email);
    }

    public String lireUtilisateur(String email) throws CustomException {
        Map<String, String> baseDeDonnees = chargerBaseDeDonnees();
        if (!baseDeDonnees.containsKey(email)) {
            throw new CustomException("Erreur: Utilisateur non trouvé.");
        }
        return baseDeDonnees.get(email);
    }

    public void mettreAJourUtilisateur(String email, String nouveauMotDePasse) throws CustomException {
        Map<String, String> baseDeDonnees = chargerBaseDeDonnees();
        if (!baseDeDonnees.containsKey(email)) {
            throw new CustomException("Erreur: Utilisateur non trouvé.");
        }
        if (!validerMotDePasse(nouveauMotDePasse)) {
            throw new CustomException("Erreur: Le mot de passe ne respecte pas les critères de sécurité.");
        }
        baseDeDonnees.put(email, nouveauMotDePasse);
        sauvegarderBaseDeDonnees(baseDeDonnees);
    }

    public void supprimerUtilisateur(String email) throws CustomException {
        Map<String, String> baseDeDonnees = chargerBaseDeDonnees();
        if (!baseDeDonnees.containsKey(email)) {
            throw new CustomException("Erreur: Utilisateur non trouvé.");
        }
        baseDeDonnees.remove(email);
        sauvegarderBaseDeDonnees(baseDeDonnees);
    }

    private void sauvegarderBaseDeDonnees(Map<String, String> baseDeDonnees) throws CustomException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FICHIER_BASE_DONNEES))) {
            for (Map.Entry<String, String> entry : baseDeDonnees.entrySet()) {
                writer.println(entry.getKey() + ":" + entry.getValue());
            }
        } catch (IOException e) {
            throw new CustomException("Erreur lors de la sauvegarde de la base de données: " + e.getMessage());
        }
    }

    private Map<String, String> chargerBaseDeDonnees() throws CustomException {
        Map<String, String> baseDeDonnees = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FICHIER_BASE_DONNEES))) {
            String ligne;
            while ((ligne = reader.readLine()) != null) {
                String[] parts = ligne.split(":");
                if (parts.length >= 2) {
                    baseDeDonnees.put(parts[0], parts[1]);
                }
            }
        } catch (IOException e) {
            throw new CustomException("Erreur lors du chargement de la base de données: " + e.getMessage());
        }
        return baseDeDonnees;
    }

    public class CustomException extends Exception {
        public CustomException(String message) {
            super(message);
        }
    }

	public boolean verifierLogin(String email, String motDePasse) {
		// TODO Auto-generated method stub
		return false;
	}
}