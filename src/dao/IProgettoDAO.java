package dao;

import java.util.Date;
import java.util.List;

import entity.Partecipazione;
import entity.Progetto;
import entity.Utente;

public interface IProgettoDAO {
    Progetto save(Progetto progetto);
    List<Progetto> findByUtente(Utente utente);
    boolean addPartecipante(Progetto progetto, Utente utente, Date dataScadenza);
    boolean delete(Progetto progetto);
    boolean rimuoviPartecipante(Progetto progetto, Utente utente);
    boolean pulisciInvitiScaduti(Utente utente);
    List<Partecipazione> getInvitiPendenti(Utente utente);
    boolean updateStatoInvito(Partecipazione p);
    Progetto findById(int id);
    List<String> getNicknamesPartecipanti(Progetto progetto);
}