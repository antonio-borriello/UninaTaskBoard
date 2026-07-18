package dao;

import java.util.List;

import entity.Attivita;
import entity.Progetto;
import entity.Utente;

public interface IAttivitaDAO {
    boolean save(Attivita attivita);
    List<Attivita> findByProgetto(Progetto progetto);
    boolean delete(Attivita attivita);
    boolean assegnaUtente(Utente utente, Attivita attivita);
    List<String> getUtentiAssegnati(Attivita attivita);
    boolean updateStato(Attivita attivita);
}
