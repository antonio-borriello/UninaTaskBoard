package boundary;

import javax.swing.*;
import java.awt.Component;

public class GestoreNotifiche {

    public static void mostraErrore(Component parent, String messaggio) {
        JOptionPane.showMessageDialog(parent, messaggio, "Errore", JOptionPane.ERROR_MESSAGE);
    }

    public static void mostraSuccesso(Component parent, String messaggio) {
        JOptionPane.showMessageDialog(parent, messaggio, "Successo", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void mostraInfo(Component parent, String messaggio) {
        JOptionPane.showMessageDialog(parent, messaggio, "Informazione", JOptionPane.INFORMATION_MESSAGE);
    }
}
