package dao;

import entity.Utente;

public interface IUtenteDAO {
    Utente findByNicknameAndPassword(String nickname, String password);
    Utente findByNickname(String nickname);
    boolean save(Utente utente);
}
