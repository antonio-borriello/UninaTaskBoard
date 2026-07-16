package dao;

import entity.Attivita;
import entity.Progetto;
import entity.Utente;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AttivitaDAO {

    public boolean save(Attivita attivita) {
        String query = "INSERT INTO Attivita (titolo, descrizione, dataCreazione, dataScadenza, statoAvanzamento, infoSpecifiche, progetto_id) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setString(1, attivita.getTitolo());
            stmt.setString(2, attivita.getDescrizione());
            stmt.setDate(3, new java.sql.Date(attivita.getDataCreazione().getTime()));
            if (attivita.getDataScadenza() != null) {
                stmt.setDate(4, new java.sql.Date(attivita.getDataScadenza().getTime()));
            } else {
                stmt.setNull(4, Types.DATE);
            }
            stmt.setString(5, attivita.getStatoAvanzamento());
            stmt.setString(6, attivita.getInfoSpecifiche());
            stmt.setInt(7, attivita.getProgetto().getId());
            
            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Attivita> findByProgetto(Progetto progetto) {
        List<Attivita> attivitaList = new ArrayList<>();
        String query = "SELECT * FROM Attivita WHERE progetto_id = ?";
        
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, progetto.getId());
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                	  Attivita a = new Attivita(
                              rs.getInt("id_attivita"),
                              rs.getString("titolo"),
                              rs.getString("descrizione"),
                              rs.getDate("dataCreazione"),
                              rs.getDate("dataScadenza"),
                              rs.getString("statoAvanzamento"),
                              rs.getString("infoSpecifiche"),
                              progetto
                          );
                          attivitaList.add(a);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return attivitaList;
    }

    public boolean delete(Attivita attivita) {
        String query = "DELETE FROM Attivita WHERE id_attivita = ?";
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, attivita.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean assegnaUtente(Utente utente, Attivita attivita) {
        String query = "INSERT INTO Assegnazione (utente_nickname, attivita_id, dataCreazione) VALUES (?, ?, ?)";
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setString(1, utente.getNickname());
            stmt.setInt(2, attivita.getId());
            stmt.setDate(3, new java.sql.Date(new java.util.Date().getTime()));
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<String> getUtentiAssegnati(Attivita attivita) {
        List<String> utenti = new ArrayList<>();
        String query = "SELECT utente_nickname FROM Assegnazione WHERE attivita_id = ?";
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, attivita.getId());
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    utenti.add(rs.getString("utente_nickname"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return utenti;
    }

    public boolean updateStato(Attivita attivita) {
        String query = "UPDATE Attivita SET statoAvanzamento = ? WHERE id_attivita = ?";
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, attivita.getStatoAvanzamento());
            stmt.setInt(2, attivita.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
