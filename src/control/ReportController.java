package control;

import dao.AttivitaDAO;
import dao.ProgettoDAO;
import entity.Attivita;
import entity.Progetto;

import java.util.ArrayList;
import java.util.List;

public class ReportController {

    private AttivitaDAO attivitaDAO;
    private ProgettoDAO progettoDAO;

    public ReportController() {
        this.attivitaDAO = new AttivitaDAO();
        this.progettoDAO = new ProgettoDAO();
    }

    public List<Attivita> getAttivitaProgetto(Progetto progetto) {
        return attivitaDAO.findByProgetto(progetto);
    }

    // Indici: 0 = Da fare, 1 = In corso, 2 = Completata
    public int[] getStatisticheStato(List<Attivita> attivita) {
        int[] stats = new int[3];

        for (Attivita a : attivita) {
            String stato = a.getStatoAvanzamento();
            if ("Da fare".equalsIgnoreCase(stato)) {
                stats[0]++;
            } else if ("In corso".equalsIgnoreCase(stato)) {
                stats[1]++;
            } else if ("Completata".equalsIgnoreCase(stato)) {
                stats[2]++;
            }
        }
        return stats;
    }

    public int getNumeroAttivitaSviluppo(List<Attivita> attivita) {
        int count = 0;
        for (Attivita a : attivita) {
            if ("AttivitàSviluppo".equalsIgnoreCase(a.getProgetto().getTipoProgetto())) {
                count++;
            }
        }
        return count;
    }

    public List<String> getMembriConAttivitaCompletate(List<Attivita> attivita) {
        List<String> membri = new ArrayList<>();
        
        for (Attivita a : attivita) {
            if ("Completata".equals(a.getStatoAvanzamento())) {
                List<String> assegnati = attivitaDAO.getUtentiAssegnati(a);
                for (String utente : assegnati) {
                    if (!membri.contains(utente)) {
                        membri.add(utente);
                    }
                }
            }
        }
        return membri;
    }
    
    public int getAttivitaCompletateDaMembro(List<Attivita> attivita, String membro) {
        int count = 0;
        for (Attivita a : attivita) {
            if ("Completata".equals(a.getStatoAvanzamento())) {
                List<String> assegnati = attivitaDAO.getUtentiAssegnati(a);
                if (assegnati.contains(membro)) {
                    count++;
                }
            }
        }
        return count;
    }
}
