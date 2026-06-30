package entity;

import java.util.Date;

public class Partecipazione {
    private Utente utente;
    private Progetto progetto;
    private String statoInvito;
    private Date dataInvito;
    private Date dataScadenza;

    public Partecipazione(Utente utente, Progetto progetto, String statoInvito, Date dataInvito, Date dataScadenza) {
        this.utente = utente;
        this.progetto = progetto;
        this.statoInvito = statoInvito;
        this.dataInvito = dataInvito;
        this.dataScadenza = dataScadenza;
    }

    public void accettaInvito() {
        this.statoInvito = "Accettato";
    }

    public void rifiutaInvito() {
        this.statoInvito = "Rifiutato";
    }

    public boolean isInvitoScaduto() {
        if (dataScadenza == null) return false;
        return new Date().after(dataScadenza);
    }

    public Utente getUtente() { return utente; }
    public void setUtente(Utente utente) { this.utente = utente; }
    public Progetto getProgetto() { return progetto; }
    public void setProgetto(Progetto progetto) { this.progetto = progetto; }
    public String getStatoInvito() { return statoInvito; }
    public void setStatoInvito(String statoInvito) { this.statoInvito = statoInvito; }
    public Date getDataInvito() { return dataInvito; }
    public void setDataInvito(Date dataInvito) { this.dataInvito = dataInvito; }
    public Date getDataScadenza() { return dataScadenza; }
    public void setDataScadenza(Date dataScadenza) { this.dataScadenza = dataScadenza; }
}
