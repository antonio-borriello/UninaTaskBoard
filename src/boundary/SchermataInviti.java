package boundary;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import control.ProgettoController;
import entity.Partecipazione;

public class SchermataInviti extends JFrame {

    private ProgettoController progettoController;
    private DefaultListModel<String> listModel;
    private JList<String> listInviti;
    private List<Partecipazione> invitiPendenti;

    public SchermataInviti(ProgettoController progettoController) {
        this.progettoController = progettoController;
        inizializzaInterfaccia();
        caricaInviti();
    }

    private void inizializzaInterfaccia() {
        setTitle("Gestione Inviti");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel pannelloHeader = new JPanel();
        pannelloHeader.add(new JLabel("I tuoi inviti pendenti:"));
        add(pannelloHeader, BorderLayout.NORTH);

        listModel = new DefaultListModel<>();
        listInviti = new JList<>(listModel);
        listInviti.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(new JScrollPane(listInviti), BorderLayout.CENTER);

        JPanel pannelloButton = new JPanel();
        JButton bottoneAccetta = new JButton("Accetta");
        JButton bottoneRifiuta = new JButton("Rifiuta");
        JButton bottoneChiudi = new JButton("Chiudi");
        JButton bottonePulisci = new JButton("Pulisci Scaduti/Rifiutati");

        bottoneAccetta.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                rispondi(true);
            }
        });
        
        bottoneRifiuta.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                rispondi(false);
            }
        });
        
        bottoneChiudi.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SchermataInviti.this.dispose();
            }
        });

        bottonePulisci.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean success = progettoController.pulisciInvitiScaduti();
                if (success) {
                    GestoreNotifiche.mostraSuccesso(SchermataInviti.this, "Pulizia completata con successo!");
                    caricaInviti();
                } else {
                    GestoreNotifiche.mostraErrore(SchermataInviti.this, "Errore durante la pulizia.");
                }
            }
        });

        pannelloButton.add(bottoneAccetta);
        pannelloButton.add(bottoneRifiuta);
        pannelloButton.add(bottonePulisci);
        pannelloButton.add(bottoneChiudi);

        add(pannelloButton, BorderLayout.SOUTH);
    }

    private void caricaInviti() {
        listModel.clear();
        invitiPendenti = progettoController.caricaInvitiPendenti();
        if (invitiPendenti.isEmpty()) {
            listModel.addElement("Nessun invito in sospeso.");
            listInviti.setEnabled(false);
        } else {
            listInviti.setEnabled(true);
            for (Partecipazione p : invitiPendenti) {
                listModel.addElement("Progetto: " + p.getProgetto().getTitolo() + " (Scadenza: " + p.getDataScadenza() + ")");
            }
        }
    }

    private void rispondi(boolean accetta) {
        int index = listInviti.getSelectedIndex();
        if (index != -1 && !invitiPendenti.isEmpty()) {
            Partecipazione p = invitiPendenti.get(index);
            boolean success = progettoController.rispondiAInvito(p, accetta);
            if (success) {
                GestoreNotifiche.mostraSuccesso(this, accetta ? "Invito accettato!" : "Invito rifiutato.");
                caricaInviti();
                progettoController.aggiornaDashboard();
                
            } else {
                GestoreNotifiche.mostraErrore(this, "Errore durante l'elaborazione.");
            }
        }
    }
}