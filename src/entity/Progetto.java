package entity;

import java.util.Date;

public class Progetto {
    private int id;
    private String titolo;
    private Date dataCreazione;
    private String estensione;
    private String nomeFile;
    private String percorso;
    private String tipoDocumentazione;
    private String tipoProgetto;
    private Utente creatore;

  //aggiunta del costruttore con l'id che ci eravamo dimenticati altrimenti failava la query nel dao 
    public Progetto(int id, String titolo, Date dataCreazione, String tipoProgetto, Utente creatore) {
        this.id = id;
        this.titolo = titolo;
        this.dataCreazione = dataCreazione;
        this.tipoProgetto = tipoProgetto;
        this.creatore = creatore;
    }
    
    public Progetto(String titolo, Date dataCreazione, String estensione, String nomeFile, String percorso, String tipoDocumentazione, String tipoProgetto, Utente creatore) {
        this.titolo = titolo;
        this.dataCreazione = dataCreazione;
        this.estensione = estensione;
        this.nomeFile = nomeFile;
        this.percorso = percorso;
        this.tipoDocumentazione = tipoDocumentazione;
        this.tipoProgetto = tipoProgetto;
        this.creatore = creatore;
    }


    public int calcolaAvanzamentoGlobale(java.util.List<Attivita> attivitaList) {
        if (attivitaList == null || attivitaList.isEmpty()) return 0;
        int completate = 0;
        for (Attivita a : attivitaList) {
            if ("Completata".equals(a.getStatoAvanzamento())) {
                completate++;
            }
        }
        return (completate * 100) / attivitaList.size();
    }

    public Utente getCreatore() {
        return creatore;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getTitolo() { return titolo; }
    public void setTitolo(String titolo) { this.titolo = titolo; }
    public Date getDataCreazione() { return dataCreazione; }
    public void setDataCreazione(Date dataCreazione) { this.dataCreazione = dataCreazione; }
    public String getEstensione() { return estensione; }
    public void setEstensione(String estensione) { this.estensione = estensione; }
    public String getNomeFile() { return nomeFile; }
    public void setNomeFile(String nomeFile) { this.nomeFile = nomeFile; }
    public String getPercorso() { return percorso; }
    public void setPercorso(String percorso) { this.percorso = percorso; }
    public String getTipoDocumentazione() { return tipoDocumentazione; }
    public void setTipoDocumentazione(String tipoDocumentazione) { this.tipoDocumentazione = tipoDocumentazione; }
    public String getTipoProgetto() { return tipoProgetto; }
    public void setTipoProgetto(String tipoProgetto) { this.tipoProgetto = tipoProgetto; }
    public void setCreatore(Utente creatore) { this.creatore = creatore; }
}
