package entity;

import java.util.Date;


public class Assegnazione {
    private Utente utente;
    private Attivita attivita;
    private Date dataCreazione;

    public Assegnazione(Utente utente, Attivita attivita, Date dataCreazione) {
        this.utente = utente;
        this.attivita = attivita;
        this.dataCreazione = dataCreazione;
    }

    public int calcolaGiorniTrascorsi() { 
        if (dataCreazione == null) {
            return 0;
        }
        
        long tempoOggi = new Date().getTime();
        long tempoAssegnazione = dataCreazione.getTime();
        
        long differenza = tempoOggi - tempoAssegnazione;
        
        // Calcolo i giorni dividendo per i millisecondi in un giorno (1000 * 60 * 60 * 24)
        int giorni = (int) (differenza / 86400000);
        
        return Math.abs(giorni);
    }

    public Utente getUtente() { return utente; }
    public void setUtente(Utente utente) { this.utente = utente; }
    public Attivita getAttivita() { return attivita; }
    public void setAttivita(Attivita attivita) { this.attivita = attivita; }
    public Date getDataCreazione() { return dataCreazione; }
    public void setDataCreazione(Date dataCreazione) { this.dataCreazione = dataCreazione; }
}
