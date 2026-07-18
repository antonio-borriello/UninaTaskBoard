package boundary;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import control.AttivitaController;
import entity.Progetto;

public class SchermataCreazioneAttivita extends JDialog {

    private Progetto progetto;
    private AttivitaController attivitaController;

    private JTextField campoTitolo;
    private JTextArea areaDescrizione;
    private JTextField campoInfoSpecifiche;
    private JTextField campoScadenza;

    public SchermataCreazioneAttivita(AttivitaController controller, Progetto progetto) {
        this.attivitaController = controller;
        this.progetto = progetto;
        inizializzaInterfaccia();
    }

    private void inizializzaInterfaccia() {
        setTitle("Nuova Attività");
        setSize(400, 350);
        setLocationRelativeTo(null);
        setModal(true);
        setLayout(new BorderLayout());

        JPanel pannelloForm = new JPanel(new GridLayout(4, 2, 5, 5));
        pannelloForm.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        pannelloForm.add(new JLabel("Titolo:"));
        campoTitolo = new JTextField();
        pannelloForm.add(campoTitolo);

        pannelloForm.add(new JLabel("Descrizione:"));
        areaDescrizione = new JTextArea();
        pannelloForm.add(new JScrollPane(areaDescrizione));

        pannelloForm.add(new JLabel("Info Specifiche:"));
        campoInfoSpecifiche = new JTextField();
        pannelloForm.add(campoInfoSpecifiche);
        
        pannelloForm.add(new JLabel("Scadenza (yyyy-MM-dd):"));
        campoScadenza = new JTextField();
        pannelloForm.add(campoScadenza);


        add(pannelloForm, BorderLayout.CENTER);

        JPanel pannelloButton = new JPanel();
        
        JButton bottoneSalva = new JButton("Crea");
        bottoneSalva.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                salvaNuovaAttivita();
            }
        });
        pannelloButton.add(bottoneSalva);

        JButton bottoneAnnulla = new JButton("Annulla");
        bottoneAnnulla.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SchermataCreazioneAttivita.this.dispose();
            }
        });
        pannelloButton.add(bottoneAnnulla);

        add(pannelloButton, BorderLayout.SOUTH);
    }

    private void salvaNuovaAttivita() {
        String titolo = campoTitolo.getText();
        String descrizione = areaDescrizione.getText();
        String info = campoInfoSpecifiche.getText();
        String scadenzaStr = campoScadenza.getText();
        
        if (titolo.isEmpty()) {
            GestoreNotifiche.mostraErrore(this, "Il titolo è obbligatorio.");
            return;
        }

        Date dataScadenza = null;
        if (!scadenzaStr.isEmpty()) {
            try {
                java.time.LocalDate localDate = java.time.LocalDate.parse(scadenzaStr);
                dataScadenza = java.sql.Date.valueOf(localDate);
            } catch (java.time.format.DateTimeParseException e) {
                GestoreNotifiche.mostraErrore(this, "Formato data non valido (usa yyyy-MM-dd con date reali).");
                return;
            }
        }

        boolean success = attivitaController.aggiungiAttivitaCompleta(titolo, descrizione, info, dataScadenza, progetto);
        if (success) {
            GestoreNotifiche.mostraSuccesso(this, "Attività creata!");
            attivitaController.mostraPannelloAttivita(progetto); // Aggiorna la view padre
            this.dispose();
        } else {
            GestoreNotifiche.mostraErrore(this, "Errore: controlla che la data di scadenza non sia nel passato rispetto al progetto.");
        }
    }
}
