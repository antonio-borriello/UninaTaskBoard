package boundary;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import control.LoginController;


public class SchermataRegistrazione extends JFrame {

    private LoginController loginController;
    private JTextField campoNome;
    private JTextField campoCognome;
    private JTextField campoNickname;
    private JTextField campoEmail;
    private JPasswordField campoPassword;

    public SchermataRegistrazione(LoginController loginController) {
        this.loginController = loginController;
        inizializzaInterfaccia();
    }

    private void inizializzaInterfaccia() {
        setTitle("Registrazione - UninaTaskBoard");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(6, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        panel.add(new JLabel("Nome:"));
        campoNome = new JTextField();
        panel.add(campoNome);

        panel.add(new JLabel("Cognome:"));
        campoCognome = new JTextField();
        panel.add(campoCognome);

        panel.add(new JLabel("Nickname:"));
        campoNickname = new JTextField();
        panel.add(campoNickname);

        panel.add(new JLabel("Email:"));
        campoEmail = new JTextField();
        panel.add(campoEmail);

        panel.add(new JLabel("Password:"));
        campoPassword = new JPasswordField();
        panel.add(campoPassword);

        JButton bottoneRegistra = new JButton("Registrati");
        JButton bottoneAnnulla = new JButton("Annulla");

        bottoneRegistra.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registraUtente();
            }
        });
        
        bottoneAnnulla.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SchermataRegistrazione.this.dispose();
                loginController.avvia();
            }
        });

        panel.add(bottoneRegistra);
        panel.add(bottoneAnnulla);

        add(panel);
    }

    private void registraUtente() {
        String nome = campoNome.getText();
        String cognome = campoCognome.getText();
        String nickname = campoNickname.getText();
        String email = campoEmail.getText();
        String password = new String(campoPassword.getPassword());

        if (nome.isEmpty() || cognome.isEmpty() || nickname.isEmpty() || email.isEmpty() || password.isEmpty()) {
            GestoreNotifiche.mostraErrore(this, "Compila tutti i campi.");
            return;
        }

        if (!email.contains("@") || !email.contains(".")) {
            GestoreNotifiche.mostraErrore(this, "Il formato dell'email non è valido.");
            return;
        }

        boolean successo = loginController.registraNuovoUtente(nome, cognome, nickname, email, password);

        if (successo) {
            GestoreNotifiche.mostraSuccesso(this, "Registrazione completata con successo!");
            this.dispose();
            loginController.avvia();
        } else {
            GestoreNotifiche.mostraErrore(this, "Nickname già in uso o errore di connessione.");
        }
    }}
