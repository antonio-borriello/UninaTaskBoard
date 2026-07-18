package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import entity.Progetto;
import entity.Utente; 

public class ProgettoDAO implements IProgettoDAO {

    public Progetto save(Progetto progetto) {
        String query = "INSERT INTO Progetto (titolo, dataCreazione, tipoProgetto, creatore_nickname, estensione, nomeFile, percorso, tipoDocumentazione) VALUES (?, ?, ?, ?, ?, ?, ?, ?) RETURNING id_progetto";
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setString(1, progetto.getTitolo());
            stmt.setDate(2, new java.sql.Date(progetto.getDataCreazione().getTime()));
            stmt.setString(3, progetto.getTipoProgetto());
            stmt.setString(4, progetto.getCreatore().getNickname());
            stmt.setString(5, progetto.getEstensione());
            stmt.setString(6, progetto.getNomeFile());
            stmt.setString(7, progetto.getPercorso());
            stmt.setString(8, progetto.getTipoDocumentazione());
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    progetto.setId(rs.getInt("id_progetto"));
                    return progetto;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Progetto> findByUtente(Utente utente) {
        List<Progetto> progetti = new ArrayList<>();
        // Trova progetti creati dall'utente oppure a cui partecipa (accettati)
        String query = "SELECT p.* FROM Progetto p WHERE p.creatore_nickname = ? " +
                       "UNION " +
                       "SELECT p.* FROM Progetto p JOIN Partecipazione part ON p.id_progetto = part.progetto_id " +
                       "WHERE part.utente_nickname = ? AND part.statoInvito = 'Accettato'";
        
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setString(1, utente.getNickname());
            stmt.setString(2, utente.getNickname());
            
            UtenteDAO utenteDAO = new UtenteDAO();
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Utente creatore = utenteDAO.findByNickname(rs.getString("creatore_nickname"));
                    Progetto p = new Progetto(
                        rs.getInt("id_progetto"),
                        rs.getString("titolo"),
                        rs.getDate("dataCreazione"),
                        rs.getString("tipoProgetto"),
                        creatore
                    );
                    p.setEstensione(rs.getString("estensione"));
                    p.setNomeFile(rs.getString("nomeFile"));
                    p.setPercorso(rs.getString("percorso"));
                    p.setTipoDocumentazione(rs.getString("tipoDocumentazione"));
                    progetti.add(p);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return progetti;
    }

    public boolean addPartecipante(Progetto progetto, Utente utente, Date dataScadenza) {
        String query = "INSERT INTO Partecipazione (utente_nickname, progetto_id, statoInvito, dataInvito, dataScadenza) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setString(1, utente.getNickname());
            stmt.setInt(2, progetto.getId());
            stmt.setString(3, "In attesa");
            stmt.setDate(4, new java.sql.Date(new Date().getTime()));
            stmt.setDate(5, new java.sql.Date(dataScadenza.getTime()));
            
            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(Progetto progetto) {
        String query = "DELETE FROM Progetto WHERE id_progetto = ?";
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, progetto.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean rimuoviPartecipante(Progetto progetto, Utente utente) {
        String query = "DELETE FROM Partecipazione WHERE progetto_id = ? AND utente_nickname = ?";
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, progetto.getId());
            stmt.setString(2, utente.getNickname());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    


    public boolean pulisciInvitiScaduti(Utente utente) {
        String query = "CALL rimuovi_miei_inviti_scaduti(?)";
        try (Connection conn = DbConnection.getConnection();
             java.sql.CallableStatement stmt = conn.prepareCall(query)) {
             
            stmt.setString(1, utente.getNickname());
            stmt.execute(); // Invoca la procedura nel DB
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    
    
    
    public List<entity.Partecipazione> getInvitiPendenti(Utente utente) {
        List<entity.Partecipazione> inviti = new ArrayList<>();
        String query = "SELECT * FROM Partecipazione WHERE utente_nickname = ? AND statoInvito = 'In attesa'";
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setString(1, utente.getNickname());
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    // Recupero il progetto dal database
                    Progetto progetto = findById(rs.getInt("progetto_id"));
                    entity.Partecipazione p = new entity.Partecipazione(
                        utente,
                        progetto,
                        rs.getString("statoInvito"),
                        rs.getDate("dataInvito"),
                        rs.getDate("dataScadenza")
                    );
                    inviti.add(p);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return inviti;
    }

    public boolean updateStatoInvito(entity.Partecipazione p) {
        String query = "UPDATE Partecipazione SET statoInvito = ? WHERE utente_nickname = ? AND progetto_id = ?";
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, p.getStatoInvito());
            stmt.setString(2, p.getUtente().getNickname());
            stmt.setInt(3, p.getProgetto().getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Progetto findById(int id) {
        String query = "SELECT * FROM Progetto WHERE id_progetto = ?";
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    UtenteDAO utenteDAO = new UtenteDAO();
                    Utente creatore = utenteDAO.findByNickname(rs.getString("creatore_nickname"));
                    Progetto p = new Progetto(
                        rs.getInt("id_progetto"),
                        rs.getString("titolo"),
                        rs.getDate("dataCreazione"),
                        rs.getString("tipoProgetto"),
                        creatore
                    );
                    p.setEstensione(rs.getString("estensione"));
                    p.setNomeFile(rs.getString("nomeFile"));
                    p.setPercorso(rs.getString("percorso"));
                    p.setTipoDocumentazione(rs.getString("tipoDocumentazione"));
                    return p;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<String> getNicknamesPartecipanti(Progetto progetto) {
        List<String> nicknames = new ArrayList<>();
        String query = "SELECT utente_nickname FROM Partecipazione WHERE progetto_id = ? AND statoInvito = 'Accettato'";
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, progetto.getId());
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    nicknames.add(rs.getString("utente_nickname"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return nicknames;
    }
}
