package boundary;

import control.AttivitaController;
import entity.Attivita;
import entity.Progetto;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class SchermataGestioneAttivita extends JDialog {

    private Progetto progetto;
    private Attivita attivitaEsistente;
    private AttivitaController attivitaController;

    public SchermataGestioneAttivita(AttivitaController controller, Progetto progetto, Attivita attivitaEsistente) {
        this.attivitaController = controller;
        this.progetto = progetto;
        this.attivitaEsistente = attivitaEsistente;
        inizializzaInterfaccia();
    }

    private void inizializzaInterfaccia() {
        setTitle("Gestione Attività: " + attivitaEsistente.getTitolo());
        setSize(400, 434);
        setLocationRelativeTo(null);
        setModal(true);
        getContentPane().setLayout(new BorderLayout());

        JPanel pannelloInfo = new JPanel(new GridLayout(3, 1));
        pannelloInfo.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        pannelloInfo.add(new JLabel("Titolo: " + attivitaEsistente.getTitolo()));
        pannelloInfo.add(new JLabel("Stato attuale: " + attivitaEsistente.getStatoAvanzamento()));
        pannelloInfo.add(new JLabel("Info: " + (attivitaEsistente.getInfoSpecifiche() != null ? attivitaEsistente.getInfoSpecifiche() : "N/D")));

        getContentPane().add(pannelloInfo, BorderLayout.NORTH);

        // Lista utenti assegnati
        DefaultListModel<String> listModel = new DefaultListModel<>();
        JList<String> listUtenti = new JList<>(listModel);
        List<String> assegnati = attivitaController.getUtentiAssegnatiAAttivita(attivitaEsistente);
        for (String u : assegnati) {
            listModel.addElement(u);
        }
        
        JScrollPane scrollPane = new JScrollPane(listUtenti);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Utenti Assegnati"));
        getContentPane().add(scrollPane, BorderLayout.CENTER);

        JPanel pannelloButton = new JPanel(new GridLayout(2, 2, 5, 5));
        
        JButton bottoneAssegna = new JButton("Assegna Utente");
        JButton bottoneAvanza = new JButton("Avanza Stato");
        JButton bottoneElimina = new JButton("Elimina Attività");
        bottoneElimina.setForeground(Color.RED);
        JButton bottoneChiudi = new JButton("Chiudi");

        bottoneAssegna.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                assegnaUtente();
            }
        });
        
        bottoneAvanza.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                avanzaStato();
            }
        });
        
        bottoneChiudi.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                SchermataGestioneAttivita.this.dispose();
            }
        });
        
        bottoneElimina.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                eliminaAttivita();
            }
        });
        
        pannelloButton.add(bottoneAssegna);
        pannelloButton.add(bottoneAvanza);
        pannelloButton.add(bottoneElimina);
        pannelloButton.add(bottoneChiudi);

        getContentPane().add(pannelloButton, BorderLayout.SOUTH);
    }

    private void assegnaUtente() {
        // Mostra popup con la lista dei membri del progetto
        List<String> membri = attivitaController.getMembriProgetto(progetto);
        String nickname = (String) JOptionPane.showInputDialog(this, "Seleziona utente:", "Assegna Utente", 
            JOptionPane.QUESTION_MESSAGE, null, membri.toArray(), null);
            
        if (nickname != null && !nickname.isEmpty()) {
            boolean success = attivitaController.assegnaUtente(nickname, attivitaEsistente);
            if (success) {
                GestoreNotifiche.mostraSuccesso(this, "Assegnato con successo!");
                this.dispose();
                attivitaController.mostraPannelloAttivita(progetto);
            } else {
                GestoreNotifiche.mostraErrore(this, "Errore: Utente già assegnato.");
            }
        }
    }

    private void avanzaStato() {
        String[] stati = {"Da fare", "In corso", "Completata"};
        String nuovoStato = (String) JOptionPane.showInputDialog(this, "Scegli il nuovo stato:", "Avanza Stato", 
            JOptionPane.QUESTION_MESSAGE, null, stati, attivitaEsistente.getStatoAvanzamento());
            
        if (nuovoStato != null && !nuovoStato.equals(attivitaEsistente.getStatoAvanzamento())) {
            boolean success = attivitaController.aggiornaStato(attivitaEsistente, nuovoStato);
            if (success) {
                GestoreNotifiche.mostraSuccesso(this, "Stato aggiornato.");
                this.dispose();
                attivitaController.mostraPannelloAttivita(progetto);
            } else {
                GestoreNotifiche.mostraErrore(this, "Errore nell'aggiornamento.");
            }
        }
    }

    private void eliminaAttivita() {
        int confirm = JOptionPane.showConfirmDialog(this, "Sei sicuro di voler eliminare questa attività?", "Conferma Eliminazione", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            boolean success = attivitaController.eliminaAttivita(attivitaEsistente);
            if (success) {
                GestoreNotifiche.mostraSuccesso(this, "Attività eliminata con successo.");
                this.dispose();
                attivitaController.mostraPannelloAttivita(progetto);
            } else {
                GestoreNotifiche.mostraErrore(this, "Impossibile eliminare l'attività. Le attività completate non possono essere eliminate dal database.");
            }
        }
    }
}
