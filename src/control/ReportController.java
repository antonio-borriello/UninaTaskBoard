package control;

import entity.Attivita;
import java.util.List;

public class ReportController {

    // Indici: 0 = Da fare, 1 = In corso, 2 = Completata
    public int[] getStatisticheStato(List<Attivita> attivita) {
        int[] stats = new int[3];
        
        // Controllo di sicurezza: se la lista è nulla, restituisce l'array inizializzato a zero
        if (attivita == null) return stats;

        for (Attivita a : attivita) {
            // Controllo per evitare NullPointerException su oggetti o stati nulli
            if (a == null || a.getStatoAvanzamento() == null) continue;

            String stato = a.getStatoAvanzamento();
            if ("Da fare".equalsIgnoreCase(stato)) { //Metodo che confornta dur String e ignora se è maiusc o min
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
        
        // Controllo di sicurezza per la lista
        if (attivita == null) return 0;

        for (Attivita a : attivita) {
            // Controllo null aggiunto per evitare errori se tipoAttivita è nullo
            if (a != null && a.getTipoAttivita() != null && "Sviluppo".equalsIgnoreCase(a.getTipoAttivita())) {
                count++;
            }
        }
        return count;
    }
}