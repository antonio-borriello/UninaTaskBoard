package dao;

import java.sql.Connection;
import java.sql.PreparedStatement; //Che vanno a piazzare al posto dei ? il parametro passatop
import java.sql.ResultSet;
import java.sql.SQLException;

import entity.Utente;

public class UtenteDAO implements IUtenteDAO {
	//Primo metodo per cercare un utente da nome e password che ci servirà per scrivere poi il logincontroller
    public Utente findByNicknameAndPassword(String nickname, String password) {
        String query = "SELECT * FROM Utente WHERE nickname = ? AND password = ?";
        try (Connection conn = DbConnection.getConnection(); //Come abbiamo visto su discord il try-with-resources 
        														//che chiude la conn automaticamente senza il finally
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setString(1, nickname);
            stmt.setString(2, password);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Utente(	
                        rs.getString("nickname"),
                        rs.getString("nome"),
                        rs.getString("cognome"),
                        rs.getString("email"),
                        rs.getString("password")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
//Questo etodo per invitare
    public Utente findByNickname(String nickname) {
        String query = "SELECT * FROM Utente WHERE nickname = ?";
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setString(1, nickname);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Utente(
                        rs.getString("nickname"),
                        rs.getString("nome"),
                        rs.getString("cognome"),
                        rs.getString("email"),
                        rs.getString("password")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
 //Questo invece per salvare un utente che non é registrato
    public boolean save(Utente utente) {
        String query = "INSERT INTO Utente (nickname, nome, cognome, email, password) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setString(1, utente.getNickname());
            stmt.setString(2, utente.getNome());
            stmt.setString(3, utente.getCognome());
            stmt.setString(4, utente.getEmail());
            stmt.setString(5, utente.getPassword());
            
            return stmt.executeUpdate() > 0; //true è andato a buon fine
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}
