package main;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class Main {
    public static void main(String[] args) {
        // Setta il look and feel di sistema per renderlo un po' più moderno
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Avvia l'interfaccia grafica
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                control.LoginController loginController = new control.LoginController();
                loginController.avvia();
            }
        });
    }
}
