package control;

import dao.ProgettoDAO;
import entity.Progetto;
import entity.Utente;

import java.util.Date;
import java.util.List;

public class ProgettoController {

    private ProgettoDAO progettoDAO;
    private Utente utenteLoggato;
    private boundary.SchermataDashboard dashboard;

    public ProgettoController() {
        this.progettoDAO = new ProgettoDAO();
    }

    public ProgettoController(Utente utenteLoggato) {
        this.progettoDAO = new ProgettoDAO();
        this.utenteLoggato = utenteLoggato;
    }

    public void setDashboard(boundary.SchermataDashboard dashboard) {
        this.dashboard = dashboard;
    }

    public Utente getUtenteLoggato() {
        return utenteLoggato;
    }

    public void aggiornaDashboard() {
        if (dashboard != null) {
            dashboard.caricaProgetti();
        }
    }

    public boolean creaNuovoProgetto(String titolo, String tipoProgetto, String tipoDoc, String estensione, String nomeFile, String percorso, Utente creatore) {
        Progetto p = new Progetto(titolo, new Date(), estensione, nomeFile, percorso, tipoDoc, tipoProgetto, creatore);
        Progetto salvato = progettoDAO.save(p);
        if (salvato != null) {
            // Aggiunge anche il creatore come partecipante automatico (manager/owner)
            // Vincolo: Coerenza Assegnazione-Partecipazione (il creatore partecipa di default)
            java.util.Calendar cal = java.util.Calendar.getInstance();
            cal.add(java.util.Calendar.YEAR, 100); // no vera scadenza per il creatore
            progettoDAO.addPartecipante(salvato, creatore, cal.getTime());
            
            // Forza lo stato "Accettato" subito dopo per il creatore
            entity.Partecipazione part = new entity.Partecipazione(creatore, salvato, "Accettato", new Date(), cal.getTime());
            progettoDAO.updateStatoInvito(part);
            return true;
        }
        return false;
    }

    public String invitaUtente(String nicknameInvitato, Progetto progetto) {

        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.add(java.util.Calendar.DAY_OF_MONTH, 7);
        java.util.Date dataScadenza = cal.getTime();
        
        dao.UtenteDAO uDao = new dao.UtenteDAO();
        Utente u = uDao.findByNickname(nicknameInvitato);
        if (u == null) {
            return "Utente non trovato.";
        }
        boolean success = progettoDAO.addPartecipante(progetto, u, dataScadenza);
        return success ? "Utente invitato con successo." : "Errore durante l'invito.";
    }

    public boolean validaPercorsoFile(String formato) {

        if (formato == null || formato.trim().isEmpty()) {
            return false;
        }
        return formato.matches("^[a-zA-Z0-9_/\\\\.-]+$");
    }

    public List<Progetto> caricaProgetti() {
        return progettoDAO.findByUtente(utenteLoggato);
    }

    public boolean pulisciInvitiScaduti() {
        return progettoDAO.pulisciInvitiScaduti(utenteLoggato);
    }

    public List<entity.Partecipazione> caricaInvitiPendenti() {
        return progettoDAO.getInvitiPendenti(utenteLoggato);
    }

    public boolean rispondiAInvito(entity.Partecipazione p, boolean accetta) {
        if (accetta) {
            p.accettaInvito();
        } else {
            p.rifiutaInvito();
        }
        return progettoDAO.updateStatoInvito(p);
    }

    public void apriDettaglioProgetto(Progetto progettoSelezionato) {
        boundary.SchermataDettaglioProgetto dettaglio = new boundary.SchermataDettaglioProgetto(progettoSelezionato, utenteLoggato, dashboard);
        dettaglio.setVisible(true);
    }

    public boolean eliminaProgetto(Progetto progetto) {
        return progettoDAO.delete(progetto);
    }

    public boolean abbandonaProgetto(Progetto progetto, Utente utente) {
        return progettoDAO.rimuoviPartecipante(progetto, utente);
    }
}
