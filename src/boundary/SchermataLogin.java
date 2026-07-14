package boundary;

import control.LoginController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Nickname
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Nickname:"), gbc);

        gbc.gridx = 1;
        campoNickname = new JTextField(15);
        panel.add(campoNickname, gbc);

        // Password
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Password:"), gbc);

        gbc.gridx = 1;
        campoPassword = new JPasswordField(15);
        panel.add(campoPassword, gbc);

        // Pulsante Login
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        bottoneLogin = new JButton("Login");
        panel.add(bottoneLogin, gbc);

        // Pulsante Registrati
        gbc.gridy = 3;
        bottoneRegistrati = new JButton("Non hai un account? Registrati");
        panel.add(bottoneRegistrati, gbc);

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

        boolean autenticato = loginController.autentica(nickname, password);

        if (autenticato) {
            JOptionPane.showMessageDialog(this, "Login effettuato con successo!");
            
            // Traccio la dashboard
            control.ProgettoController pc = new control.ProgettoController(loginController.getUtenteLoggato());
            SchermataDashboard dashboard = new SchermataDashboard(loginController.getUtenteLoggato(), pc);
            pc.setDashboard(dashboard);
            dashboard.setVisible(true);
            
            this.dispose(); // Chiude la finestra di login
        } else {
            JOptionPane.showMessageDialog(this, "Credenziali non valide.", "Errore Login", JOptionPane.ERROR_MESSAGE);
        }
    }
}
