package control;

import dao.IUtenteDAO;
import dao.UtenteDAO;
import entity.Utente;

public class LoginController {
    private IUtenteDAO utenteDAO;
    private Utente utenteLoggato;

    public LoginController() {
        this.utenteDAO = new UtenteDAO();
    }

    public void autentica(String nickname, String password) throws exception.AuthenticationException {
        Utente u = utenteDAO.findByNicknameAndPassword(nickname, password);
        if (u != null) {
            this.utenteLoggato = u;
        } else {
            throw new exception.AuthenticationException("Nickname o password non validi!");
        }
    }

    public void avvia() {
        boundary.SchermataLogin login = new boundary.SchermataLogin(this);
        login.setVisible(true);
    }

    public void apriRegistrazione() {
        boundary.SchermataRegistrazione registrazione = new boundary.SchermataRegistrazione(this);
        registrazione.setVisible(true);
    }

    public void registraNuovoUtente(String nome, String cognome, String nickname, String email, String password) throws exception.ValidationException {
        // Vincolo: Validità formato Email
        if (email == null || !email.contains("@") || !email.contains(".")) {
            throw new exception.ValidationException("Il formato dell'email non è valido!");
        }
        
        if (utenteDAO.findByNickname(nickname) != null) {
            throw new exception.ValidationException("Il nickname scelto è già in uso!");
        }
        Utente u = new Utente(nickname, nome, cognome, email, password);
        boolean success = utenteDAO.save(u);
        if (!success) {
            throw new exception.ValidationException("Errore durante il salvataggio sul database.");
        }
    }

    public Utente getUtenteLoggato() {
        return utenteLoggato;
    }
}