package entity;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Assegnazione {
    private Utente utente;
    private Attivita attivita;
    private Date dataCreazione;

    public Assegnazione(Utente utente, Attivita attivita, Date dataCreazione) {
        this.utente = utente;
        this.attivita = attivita;
        this.dataCreazione = dataCreazione;
    }

    public int calcolaTempoTrascorsoDaAssegnazione() {
        if (dataCreazione == null) return 0;
        long diffInMillies = Math.abs(new Date().getTime() - dataCreazione.getTime());
        long diffInDays = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
        return (int) diffInDays;
    }

    public Utente getUtente() { return utente; }
    public void setUtente(Utente utente) { this.utente = utente; }
    public Attivita getAttivita() { return attivita; }
    public void setAttivita(Attivita attivita) { this.attivita = attivita; }
    public Date getDataCreazione() { return dataCreazione; }
    public void setDataCreazione(Date dataCreazione) { this.dataCreazione = dataCreazione; }
}
