package control;

import dao.AttivitaDAO;
import dao.ProgettoDAO;
import dao.UtenteDAO;
import entity.Attivita;
import entity.Partecipazione;
import entity.Progetto;
import entity.Utente;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AttivitaController {

    private AttivitaDAO attivitaDAO;
    private boundary.SchermataDettaglioProgetto parentView;

    public AttivitaController(boundary.SchermataDettaglioProgetto parentView) {
        this.attivitaDAO = new AttivitaDAO();
        this.parentView = parentView;
    }

    public void mostraPannelloAttivita(Progetto progetto) {
        // Aggiorna la vista padre
        parentView.caricaAttivita();
    }

    public void apriFormCreazioneAttivita(Progetto progetto) {
        boundary.SchermataCreazioneAttivita creation = new boundary.SchermataCreazioneAttivita(this, progetto);
        creation.setVisible(true);
    }

    public List<Attivita> caricaAttivitaProgetto(Progetto progetto) {
        return attivitaDAO.findByProgetto(progetto);
    }

    public List<Attivita> filtraAttivita(List<Attivita> tutte, String stato, String membro, String scadenza) {
        List<Attivita> filtrate = new ArrayList<>();
        for (Attivita a : tutte) {
            boolean okStato = stato.equals("Tutti") || a.getStatoAvanzamento().equalsIgnoreCase(stato);
            
            boolean okMembro = true;
            if (membro != null && !membro.trim().isEmpty()) {
                List<String> assegnati = getUtentiAssegnatiAAttivita(a);
                okMembro = false;
                for (String u : assegnati) {
                    if (u.toLowerCase().contains(membro.toLowerCase().trim())) {
                        okMembro = true;
                        break;
                    }
                }
            }
            
            boolean okScadenza = true;
            if (scadenza != null && !scadenza.trim().isEmpty()) {
                // confronto base per la data di scadenza
                if (a.getDataScadenza() != null) {
                    String scadenzaStr = new java.text.SimpleDateFormat("yyyy-MM-dd").format(a.getDataScadenza());
                    okScadenza = scadenzaStr.compareTo(scadenza.trim()) <= 0;
                } else {
                    okScadenza = false; // se si filtra per scadenza ma l\'attività non ne ha una
                }
            }
            
            if (okStato && okMembro && okScadenza) {
                filtrate.add(a);
            }
        }
        return filtrate;
    }

    public boolean aggiungiAttivitaCompleta(String titolo, String descrizione, String infoSpecifiche, Date dataScadenza, Progetto progetto) {
        Date dataCreazione = new Date();
        
        // Vincolo: Cronologia Progetto-Attività
        if (dataCreazione.before(progetto.getDataCreazione())) {
            // Se la data odierna fosse per qualche strano motivo precedente a quella di creazione del progetto (es. fuso orario o date retroattive)
            dataCreazione = progetto.getDataCreazione(); 
        }

        // Vincolo: Cronologia Scadenza Attività
        if (dataScadenza != null && dataScadenza.before(dataCreazione)) {
            return false; // Scadenza non valida
        }

        Attivita nuovaAttivita = new Attivita(titolo, descrizione, dataCreazione, dataScadenza, "Da fare", infoSpecifiche, progetto);
        return attivitaDAO.save(nuovaAttivita);
    }

    public List<String> getMembriProgetto(Progetto progetto) {
        List<String> membri = new ArrayList<>();
        membri.add(progetto.getCreatore().getNickname()); // Creatore is a member
        
        ProgettoDAO pDao = new ProgettoDAO();
        // Recupero i partecipanti dal DAO
        
        
        
        
        membri.addAll(pDao.getNicknamesPartecipanti(progetto));
        return membri;
    }

    public List<String> getUtentiAssegnatiAAttivita(Attivita attivita) {
        return attivitaDAO.getUtentiAssegnati(attivita);
    }

    public boolean assegnaUtente(String nickname, Attivita attivita) {
        // Vincolo: Coerenza Assegnazione-Partecipazione
        List<String> membriAttivi = getMembriProgetto(attivita.getProgetto());
        if (!membriAttivi.contains(nickname)) {
            return false; // L'utente non fa parte del progetto o non ha accettato l'invito
        }

        Utente u = new UtenteDAO().findByNickname(nickname);
        if (u != null) {
            return attivitaDAO.assegnaUtente(u, attivita);
        }
        return false;
    }

    public boolean aggiornaStato(Attivita attivita, String nuovoStato) {
        attivita.setStatoAvanzamento(nuovoStato);
        return attivitaDAO.updateStato(attivita);
    }

    public boolean eliminaAttivita(Attivita attivita) {
        return attivitaDAO.delete(attivita);
    }
}
