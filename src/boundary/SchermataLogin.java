package boundary;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import control.LoginController;

public class SchermataLogin extends JFrame {

    private JTextField campoNickname;
    private JPasswordField campoPassword;
    private JButton bottoneLogin;
    private JButton bottoneRegistrati;
    private LoginController loginController;

    // Costruttore di default
    public SchermataLogin() {
        this.loginController = new LoginController();
        inizializzaInterfaccia();
    }

    public SchermataLogin(LoginController loginController) {
        this.loginController = loginController;
        inizializzaInterfaccia();
    }

    private void inizializzaInterfaccia() {
        setTitle("UninaTaskBoard - Login");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centra sullo schermo

        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        // Nickname Label
        JLabel lblNickname = new JLabel("Nickname:");
        GridBagConstraints gbc_lblNickname = new GridBagConstraints();
        gbc_lblNickname.insets = new Insets(10, 10, 10, 10);
        gbc_lblNickname.fill = GridBagConstraints.HORIZONTAL;
        gbc_lblNickname.gridx = 0;
        gbc_lblNickname.gridy = 0;
        panel.add(lblNickname, gbc_lblNickname);

        // Nickname TextField
        campoNickname = new JTextField(15);
        GridBagConstraints gbc_campoNickname = new GridBagConstraints();
        gbc_campoNickname.insets = new Insets(10, 10, 10, 10);
        gbc_campoNickname.fill = GridBagConstraints.HORIZONTAL;
        gbc_campoNickname.gridx = 1;
        gbc_campoNickname.gridy = 0;
        panel.add(campoNickname, gbc_campoNickname);

        // Password Label
        JLabel lblPassword = new JLabel("Password:");
        GridBagConstraints gbc_lblPassword = new GridBagConstraints();
        gbc_lblPassword.insets = new Insets(10, 10, 10, 10);
        gbc_lblPassword.fill = GridBagConstraints.HORIZONTAL;
        gbc_lblPassword.gridx = 0;
        gbc_lblPassword.gridy = 1;
        panel.add(lblPassword, gbc_lblPassword);

        // Password Field
        campoPassword = new JPasswordField(15);
        GridBagConstraints gbc_campoPassword = new GridBagConstraints();
        gbc_campoPassword.insets = new Insets(10, 10, 10, 10);
        gbc_campoPassword.fill = GridBagConstraints.HORIZONTAL;
        gbc_campoPassword.gridx = 1;
        gbc_campoPassword.gridy = 1;
        panel.add(campoPassword, gbc_campoPassword);

        // Bottone Login
        bottoneLogin = new JButton("Login");
        GridBagConstraints gbc_bottoneLogin = new GridBagConstraints();
        gbc_bottoneLogin.insets = new Insets(10, 10, 10, 10);
        gbc_bottoneLogin.fill = GridBagConstraints.HORIZONTAL;
        gbc_bottoneLogin.gridwidth = 2;
        gbc_bottoneLogin.gridx = 0;
        gbc_bottoneLogin.gridy = 2;
        panel.add(bottoneLogin, gbc_bottoneLogin);

        // Bottone Registrati
        bottoneRegistrati = new JButton("Non hai un account? Registrati");
        GridBagConstraints gbc_bottoneRegistrati = new GridBagConstraints();
        gbc_bottoneRegistrati.insets = new Insets(10, 10, 10, 10);
        gbc_bottoneRegistrati.fill = GridBagConstraints.HORIZONTAL;
        gbc_bottoneRegistrati.gridwidth = 2;
        gbc_bottoneRegistrati.gridx = 0;
        gbc_bottoneRegistrati.gridy = 3;
        panel.add(bottoneRegistrati, gbc_bottoneRegistrati);

        bottoneLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                effettuaLogin();
            }
        });
        
        bottoneRegistrati.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SchermataLogin.this.dispose();
                loginController.apriRegistrazione();
            }
        });

        add(panel);
    }

    private void effettuaLogin() {
        String nickname = campoNickname.getText();
        String password = new String(campoPassword.getPassword());

        if (nickname.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Inserisci nickname e password.", "Errore", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            loginController.autentica(nickname, password);
            JOptionPane.showMessageDialog(this, "Login effettuato con successo!");
            
            // Traccio la dashboard
            control.ProgettoController pc = new control.ProgettoController(loginController.getUtenteLoggato());
            SchermataDashboard dashboard = new SchermataDashboard(loginController.getUtenteLoggato(), pc);
            pc.setDashboard(dashboard);
            dashboard.setVisible(true);
            
            this.dispose(); // Chiude la finestra di login
        } catch (exception.AuthenticationException ex) {
            GestoreNotifiche.mostraErrore(this, ex.getMessage());
        }
    }
}