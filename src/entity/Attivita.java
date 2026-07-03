package entity;

import java.util.Date;

public class Attivita {
    private int id; 
    private String titolo;
    private String descrizione;
    private Date dataCreazione;
    private Date dataScadenza;
    private String statoAvanzamento;
    private String tipoAttivita; 
    private String infoSpecifiche;
    private Progetto progetto;

//aggiunta del costruttore con l'id che ci eravamo dimenticati altrimenti failava la query nel dao 
    public Attivita(int id, String titolo, String descrizione, Date dataCreazione, Date dataScadenza, String statoAvanzamento, String tipoAttivita, String infoSpecifiche, Progetto progetto) {
        this.id = id;
        this.titolo = titolo;
        this.descrizione = descrizione;
        this.dataCreazione = dataCreazione;
        this.dataScadenza = dataScadenza;
        this.statoAvanzamento = statoAvanzamento;
        this.tipoAttivita = tipoAttivita;
        this.infoSpecifiche = infoSpecifiche;
        this.progetto = progetto;
    } 

    public Attivita(String titolo, String descrizione, Date dataCreazione, Date dataScadenza, String statoAvanzamento, String tipoAttivita, String infoSpecifiche, Progetto progetto) {
        this.titolo = titolo;
        this.descrizione = descrizione;
        this.dataCreazione = dataCreazione;
        this.dataScadenza = dataScadenza;
        this.statoAvanzamento = statoAvanzamento;
        this.tipoAttivita = tipoAttivita;
        this.infoSpecifiche = infoSpecifiche;
        this.progetto = progetto;
    }

    public boolean isScaduta() {
        if (dataScadenza == null) return false;
        return new Date().after(dataScadenza); //data din oggi viene dopo dataScadenza? se si return true
    }

    public void avanzaStato() {
        if ("Da fare".equals(this.statoAvanzamento)) {
            this.statoAvanzamento = "In corso";
        } else if ("In corso".equals(this.statoAvanzamento)) {
            this.statoAvanzamento = "Completata";
        }
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getTitolo() { return titolo; }
    public void setTitolo(String titolo) { this.titolo = titolo; }
    public String getDescrizione() { return descrizione; }
    public void setDescrizione(String descrizione) { this.descrizione = descrizione; }
    public Date getDataCreazione() { return dataCreazione; }
    public void setDataCreazione(Date dataCreazione) { this.dataCreazione = dataCreazione; }
    public Date getDataScadenza() { return dataScadenza; }
    public void setDataScadenza(Date dataScadenza) { this.dataScadenza = dataScadenza; }
    public String getStatoAvanzamento() { return statoAvanzamento; }
    public void setStatoAvanzamento(String statoAvanzamento) { this.statoAvanzamento = statoAvanzamento; }
    public String getTipoAttivita() { return tipoAttivita; }
    public void setTipoAttivita(String tipoAttivita) { this.tipoAttivita = tipoAttivita; }
    public String getInfoSpecifiche() { return infoSpecifiche; }
    public void setInfoSpecifiche(String infoSpecifiche) { this.infoSpecifiche = infoSpecifiche; }
    public Progetto getProgetto() { return progetto; }
    public void setProgetto(Progetto progetto) { this.progetto = progetto; }
}
