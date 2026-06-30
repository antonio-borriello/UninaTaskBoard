package entity;

public class Utente {
    private String nickname;
    private String nome;
    private String cognome;
    private String email;
    private String password;

    public Utente(String nickname, String nome, String cognome, String email, String password) {
        this.nickname = nickname;
        this.nome = nome;
        this.cognome = cognome;
        this.email = email;
        this.password = password;
    }

    public void modificaProfilo(String nome, String cognome) {
        this.nome = nome;
        this.cognome = cognome;
    }

    public String getNickname() { return nickname; }
    public void setNickname(String nickname) { this.nickname = nickname; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getCognome() { return cognome; }
    public void setCognome(String cognome) { this.cognome = cognome; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
