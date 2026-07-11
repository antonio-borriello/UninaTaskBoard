package control;

import dao.UtenteDAO;
import entity.Utente;

public class LoginController {

    private UtenteDAO utenteDAO;
    private Utente utenteLoggato;

    public LoginController() {
        this.utenteDAO = new UtenteDAO();
    }

    public boolean autentica(String nickname, String password) {
        Utente u = utenteDAO.findByNicknameAndPassword(nickname, password);
        if (u != null) {
            this.utenteLoggato = u;
            return true;
        }
        return false;
    }

   /* public void avvia() {
        boundary.SchermataLogin login = new boundary.SchermataLogin(this);
        login.setVisible(true);
    }

    public void apriRegistrazione() {
        boundary.SchermataRegistrazione registrazione = new boundary.SchermataRegistrazione(this);
        registrazione.setVisible(true);
    }*/

    public boolean registraNuovoUtente(String nome, String cognome, String nickname, String email, String password) {
        // Vincolo: Validità formato Email
        if (email == null || !email.contains("@") || !email.contains(".")) {
            return false; // Email non valida
        }
        
        if (utenteDAO.findByNickname(nickname) != null) {
            return false; // Nickname già in uso
        }
        Utente u = new Utente(nickname, nome, cognome, email, password);
        return utenteDAO.save(u);
    }

    public Utente getUtenteLoggato() {
        return utenteLoggato;
    }
}
