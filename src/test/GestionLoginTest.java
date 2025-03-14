package test;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class GestionLoginTest {

    @Test
    public void testValiderEmailValide() {
        GestionLogin gestionLogin = new GestionLogin();
        assertTrue(gestionLogin.validerEmail("test@example.com"));
        assertTrue(gestionLogin.validerEmail("user.name+tag+sorting@example.com"));
        assertTrue(gestionLogin.validerEmail("user@sub.example.com"));
    }

    @Test
    public void testValiderEmailInvalide() {
        GestionLogin gestionLogin = new GestionLogin();
        assertFalse(gestionLogin.validerEmail("test@example")); // Manque le domaine
        assertFalse(gestionLogin.validerEmail("test.example.com")); // Manque le @
        assertFalse(gestionLogin.validerEmail("test@.com")); // Domaine manquant
        assertFalse(gestionLogin.validerEmail("test@com")); // Domaine trop court
        assertFalse(gestionLogin.validerEmail("test@example..com")); // Double point
        assertFalse(gestionLogin.validerEmail(null)); // Email null
    }

    @Test
    public void testAjouterUtilisateurEmailInvalide() {
        GestionLogin gestionLogin = new GestionLogin();
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            gestionLogin.ajouterUtilisateur("user", "password", "User", "invalid-email");
        });
        assertEquals("Format d'email invalide.", exception.getMessage());
    }
}
