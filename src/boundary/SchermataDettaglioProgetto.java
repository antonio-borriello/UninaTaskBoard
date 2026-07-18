package boundary;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

import control.AttivitaController;
import control.ProgettoController;
import entity.Attivita;
import entity.Progetto;
import entity.Utente;

public class SchermataDettaglioProgetto extends JFrame {

    private Progetto progetto;
    private Utente utenteLoggato;
    private AttivitaController attivitaController;
    private ProgettoController progettoController;
    private SchermataDashboard dashboard;
    private DefaultListModel<String> listModelAttivita;
    private JList<String> listAttivita;
    private List<Attivita> attivitaCorrenti;
    private List<Attivita> tutteAttivitaProgetto; // Per il filtraggio
    private JLabel etichettaAvanzamento;
    
    // Controlli per il filtro
    private JComboBox<String> comboStatoFiltro;

    private JTextField campoMembroFiltro;
    private JTextField campoScadenzaFiltro;

    public SchermataDettaglioProgetto(Progetto progetto, Utente utente, SchermataDashboard dashboard) {
        this.progetto = progetto;
        this.utenteLoggato = utente;
        this.dashboard = dashboard;
        this.attivitaController = new AttivitaController(this);
        this.progettoController = new ProgettoController(utenteLoggato);
        this.progettoController.setDashboard(dashboard);
        inizializzaInterfaccia();
        caricaAttivita();
    }

    private void inizializzaInterfaccia() {
        setTitle("Dettaglio Progetto - " + progetto.getTitolo());
        setSize(900, 650);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Intestazione Info
        JPanel pannelloInfo = new JPanel(new GridLayout(0, 1)); // 0 righe significa altezza dinamica
        pannelloInfo.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        pannelloInfo.add(new JLabel("Titolo: " + progetto.getTitolo()));
        pannelloInfo.add(new JLabel("Data Creazione: " + new SimpleDateFormat("dd/MM/yyyy").format(progetto.getDataCreazione())));
        pannelloInfo.add(new JLabel("Creatore: " + progetto.getCreatore().getNickname()));
        pannelloInfo.add(new JLabel("Tipo: " + progetto.getTipoProgetto()));
        
        if ("Documentazione".equals(progetto.getTipoProgetto())) {
            pannelloInfo.add(new JLabel("Tipo Doc: " + progetto.getTipoDocumentazione()));
        } else if ("AttivitàSviluppo".equals(progetto.getTipoProgetto())) {
            pannelloInfo.add(new JLabel("Linguaggio/Estensione: " + progetto.getEstensione()));
            pannelloInfo.add(new JLabel("File: " + progetto.getNomeFile() + " (" + progetto.getPercorso() + ")"));
        }
        
        etichettaAvanzamento = new JLabel("Avanzamento Globale: 0%");
        etichettaAvanzamento.setFont(new Font("Arial", Font.BOLD, 14));
        pannelloInfo.add(etichettaAvanzamento);
        
        add(pannelloInfo, BorderLayout.NORTH);

        // Filtri
        JPanel pannelloFilter = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pannelloFilter.setBorder(BorderFactory.createTitledBorder("Filtri"));
        
        comboStatoFiltro = new JComboBox<>(new String[]{"Tutti", "Da fare", "In corso", "Completata"});

        campoMembroFiltro = new JTextField(8);
        campoScadenzaFiltro = new JTextField(8); // yyyy-mm-dd
        
        pannelloFilter.add(new JLabel("Stato:"));
        pannelloFilter.add(comboStatoFiltro);

        pannelloFilter.add(new JLabel("Membro:"));
        pannelloFilter.add(campoMembroFiltro);
        pannelloFilter.add(new JLabel("Scadenza (<= yyyy-MM-dd):"));
        pannelloFilter.add(campoScadenzaFiltro);
        
        JButton bottoneFiltra = new JButton("Filtra");
        bottoneFiltra.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                applicaFiltri();
            }
        });
        pannelloFilter.add(bottoneFiltra);
        
        JButton bottoneReset = new JButton("Reset");
        bottoneReset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                comboStatoFiltro.setSelectedIndex(0);

                campoMembroFiltro.setText("");
                campoScadenzaFiltro.setText("");
                applicaFiltri();
            }
        });
        pannelloFilter.add(bottoneReset);

        JPanel pannelloCenter = new JPanel(new BorderLayout());
        pannelloCenter.add(pannelloFilter, BorderLayout.NORTH);

        // Lista Attività
        listModelAttivita = new DefaultListModel<>();
        listAttivita = new JList<>(listModelAttivita);
        listAttivita.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(listAttivita);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Attività del Progetto"));
        pannelloCenter.add(scrollPane, BorderLayout.CENTER);
        
        add(pannelloCenter, BorderLayout.CENTER);

        // Bottoni Azione
        JPanel pannelloButton = new JPanel(new FlowLayout());
        JButton bottoneNuovaAttivita = new JButton("Nuova Attività");
        JButton bottoneGestisciAttivita = new JButton("Gestisci Selezionata");
        JButton bottoneInvita = new JButton("Invita Utente");
        
        // Pulsante Elimina/Abbandona
        JButton bottoneAzioneProgetto = new JButton();
        boolean isCreatore = progetto.getCreatore().getNickname().equals(utenteLoggato.getNickname());
        if (isCreatore) {
            bottoneAzioneProgetto.setText("Elimina Progetto");
            bottoneAzioneProgetto.setForeground(Color.RED);
        } else {
            bottoneAzioneProgetto.setText("Abbandona Progetto");
            bottoneAzioneProgetto.setForeground(Color.ORANGE);
        }
        
        JButton bottoneChiudi = new JButton("Chiudi");

        pannelloButton.add(bottoneNuovaAttivita);
        pannelloButton.add(bottoneGestisciAttivita);
        pannelloButton.add(bottoneInvita);
        pannelloButton.add(bottoneAzioneProgetto);
        pannelloButton.add(bottoneChiudi);
        add(pannelloButton, BorderLayout.SOUTH);

        // Eventi
        bottoneNuovaAttivita.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                attivitaController.apriFormCreazioneAttivita(progetto);
            }
        });

        bottoneGestisciAttivita.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                int index = listAttivita.getSelectedIndex();
                if (index != -1) {
                    Attivita selezionata = attivitaCorrenti.get(index);
                    SchermataGestioneAttivita gestione = new SchermataGestioneAttivita(attivitaController, progetto, selezionata);
                    gestione.setVisible(true);
                } else {
                    GestoreNotifiche.mostraErrore(SchermataDettaglioProgetto.this, "Seleziona un'attività dalla lista.");
                }
            }
        });

        bottoneInvita.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                invitaUtente();
            }
        });
        
        bottoneAzioneProgetto.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                gestisciAzioneProgetto(isCreatore);
            }
        });

        bottoneChiudi.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                SchermataDettaglioProgetto.this.dispose();
            }
        });
    }

    public void caricaAttivita() {
        tutteAttivitaProgetto = attivitaController.caricaAttivitaProgetto(progetto);
        applicaFiltri(); // Popola la lista delle attività
        
        // Aggiorna la percentuale di avanzamento usando tutte le attività del progetto (non quelle filtrate)
        int avanzamento = progetto.calcolaAvanzamentoGlobale(tutteAttivitaProgetto);
        etichettaAvanzamento.setText("Avanzamento Globale: " + avanzamento + "%");
    }

    private void applicaFiltri() {
        String stato = (String) comboStatoFiltro.getSelectedItem();

        String membro = campoMembroFiltro.getText();
        String scadenza = campoScadenzaFiltro.getText();
        
        attivitaCorrenti = attivitaController.filtraAttivita(tutteAttivitaProgetto, stato, membro, scadenza);
        
        listModelAttivita.clear();
        for (Attivita a : attivitaCorrenti) {
            listModelAttivita.addElement(a.getTitolo() + " [" + a.getStatoAvanzamento() + "]");
        }
    }

    private void invitaUtente() {
        String nickname = JOptionPane.showInputDialog(this, "Nickname Utente da invitare (l'invito durerà 7 giorni):");

        if (nickname != null && !nickname.isEmpty()) {
            String result = progettoController.invitaUtente(nickname, progetto);
            if (result.contains("successo")) {
                GestoreNotifiche.mostraSuccesso(this, result);
            } else {
                GestoreNotifiche.mostraErrore(this, result);
            }
        }
    }
    
    private void gestisciAzioneProgetto(boolean isCreatore) {
        String msg = isCreatore ? "Sei sicuro di voler eliminare definitivamente questo progetto?" 
                                : "Sei sicuro di voler abbandonare questo progetto?";
        int confirm = JOptionPane.showConfirmDialog(this, msg, "Conferma Azione", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            boolean success;
            if (isCreatore) {
                success = progettoController.eliminaProgetto(progetto);
            } else {
                success = progettoController.abbandonaProgetto(progetto, utenteLoggato);
            }
            
            if (success) {
                GestoreNotifiche.mostraSuccesso(this, isCreatore ? "Progetto eliminato." : "Progetto abbandonato.");
                dashboard.caricaProgetti(); // Aggiorna la dashboard
                this.dispose(); // Chiude la schermata in modo che non sia più accessibile
            } else {
                GestoreNotifiche.mostraErrore(this, "Si è verificato un errore durante l'operazione.");
            }
        }
    }
}

