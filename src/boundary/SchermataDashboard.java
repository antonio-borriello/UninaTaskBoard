package boundary;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import control.ProgettoController;
import entity.Progetto;
import entity.Utente;

public class SchermataDashboard extends JFrame {

    private Utente utenteLoggato;
    private ProgettoController progettoController;
    private DefaultListModel<String> listModel;
    private JList<String> listProgetti;
    private List<Progetto> progettiCorrenti;

    public SchermataDashboard(Utente utente, ProgettoController progettoController) {
        this.utenteLoggato = utente;
        this.progettoController = progettoController;
        inizializzaInterfaccia();
        caricaProgetti();
    }

    private void inizializzaInterfaccia() {
        setTitle("Dashboard - " + utenteLoggato.getNome() + " " + utenteLoggato.getCognome());
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Intestazione
        JPanel pannelloHeader = new JPanel(new BorderLayout());
        pannelloHeader.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        pannelloHeader.add(new JLabel("I tuoi Progetti", SwingConstants.CENTER), BorderLayout.CENTER);
        add(pannelloHeader, BorderLayout.NORTH);

        // Lista Progetti
        listModel = new DefaultListModel<>();
        listProgetti = new JList<>(listModel);
        listProgetti.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(listProgetti);
        add(scrollPane, BorderLayout.CENTER);

        // Bottoni
        JPanel pannelloButton = new JPanel(new FlowLayout());
        JButton bottoneNuovoProgetto = new JButton("Nuovo Progetto");
        JButton bottoneApriProgetto = new JButton("Apri Selezionato");
        JButton bottoneReport = new JButton("Genera Report");
        JButton bottoneInviti = new JButton("Visualizza Inviti");
        JButton bottoneLogout = new JButton("Logout");

        pannelloButton.add(bottoneNuovoProgetto);
        pannelloButton.add(bottoneApriProgetto);
        pannelloButton.add(bottoneReport);
        pannelloButton.add(bottoneInviti);
        pannelloButton.add(bottoneLogout);
        add(pannelloButton, BorderLayout.SOUTH);

        // Eventi
        bottoneNuovoProgetto.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                creaNuovoProgetto();
            }
        });
        
        bottoneApriProgetto.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                apriProgetto();
            }
        });
        
        bottoneReport.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generaReport();
            }
        });
        
        bottoneInviti.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SchermataInviti schermataInviti = new SchermataInviti(progettoController);
                schermataInviti.setVisible(true);
                // Opzionale: aggiorna dopo la chiusura
                schermataInviti.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosed(java.awt.event.WindowEvent windowEvent) {
                        caricaProgetti();
                    }
                });
            }
        });

        bottoneLogout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SchermataDashboard.this.dispose();
                control.LoginController lc = new control.LoginController();
                lc.avvia();
            }
        });
    }

    public void caricaProgetti() {
        listModel.clear();
        progettiCorrenti = progettoController.caricaProgetti();
        for (Progetto p : progettiCorrenti) {
            listModel.addElement(p.getTitolo() + " (" + p.getTipoProgetto() + ")");
        }
    }

    private void creaNuovoProgetto() {
        JPanel pannelloMain = new JPanel(new BorderLayout(0, 10));
        
        JPanel pannelloTop = new JPanel(new GridLayout(2, 2, 5, 5));
        pannelloTop.add(new JLabel("Titolo Progetto:"));
        JTextField campoTitolo = new JTextField();
        pannelloTop.add(campoTitolo);
        
        pannelloTop.add(new JLabel("Tipo Progetto:"));
        String[] tipiProgetto = {"Documentazione", "AttivitàSviluppo"};
        JComboBox<String> comboTipo = new JComboBox<>(tipiProgetto);
        pannelloTop.add(comboTipo);
        pannelloMain.add(pannelloTop, BorderLayout.NORTH);

        JPanel pannelloDynamic = new JPanel(new CardLayout());
        
        JPanel pannelloDoc = new JPanel(new GridLayout(1, 2, 5, 5));
        pannelloDoc.add(new JLabel("Tipo Documentazione:"));
        JTextField campoTipoDoc = new JTextField();
        pannelloDoc.add(campoTipoDoc);
        pannelloDynamic.add(pannelloDoc, "Documentazione");

        JPanel pannelloDev = new JPanel(new GridLayout(3, 2, 5, 5));
        pannelloDev.add(new JLabel("Estensione (es. .c, .java):"));
        JTextField campoEstensione = new JTextField();
        pannelloDev.add(campoEstensione);
        pannelloDev.add(new JLabel("Nome File:"));
        JTextField campoNomeFile = new JTextField();
        pannelloDev.add(campoNomeFile);
        pannelloDev.add(new JLabel("Percorso:"));
        JTextField campoPercorso = new JTextField();
        pannelloDev.add(campoPercorso);
        pannelloDynamic.add(pannelloDev, "AttivitàSviluppo");

        pannelloMain.add(pannelloDynamic, BorderLayout.CENTER);

        comboTipo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CardLayout cl = (CardLayout)(pannelloDynamic.getLayout());
                cl.show(pannelloDynamic, (String)comboTipo.getSelectedItem());
                Window w = SwingUtilities.getWindowAncestor(pannelloMain);
                if (w != null) w.pack();
            }
        });

        boolean valid = false;
        while (!valid) {
            int option = JOptionPane.showConfirmDialog(this, pannelloMain, "Crea Nuovo Progetto", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (option == JOptionPane.OK_OPTION) {
                String titolo = campoTitolo.getText();
                String tipoProg = (String) comboTipo.getSelectedItem();
                
                if (titolo.isEmpty()) {
                    GestoreNotifiche.mostraErrore(this, "Il titolo è obbligatorio.");
                    continue;
                }

                // Validazione vincoli
                String tipoDoc = null;
                String estensione = null;
                String nomeFile = null;
                String percorso = null;

                if ("Documentazione".equals(tipoProg)) {
                    tipoDoc = campoTipoDoc.getText();
                    if (tipoDoc.isEmpty()) {
                        GestoreNotifiche.mostraErrore(this, "Il campo 'Tipo Documentazione' non può essere vuoto per questo progetto.");
                        continue;
                    }
                } else if ("AttivitàSviluppo".equals(tipoProg)) {
                    estensione = campoEstensione.getText();
                    nomeFile = campoNomeFile.getText();
                    percorso = campoPercorso.getText();

                    if (estensione.isEmpty() || nomeFile.isEmpty() || percorso.isEmpty()) {
                        GestoreNotifiche.mostraErrore(this, "Compila estensione, nome file e percorso per un progetto di sviluppo.");
                        continue;
                    }
                    
                    // Validazione estensione: non enum, ma stringa vincolata
                    if (!estensione.equals(".c") && !estensione.equals(".java") && !estensione.equals(".cpp") && !estensione.equals(".py")) {
                        GestoreNotifiche.mostraErrore(this, "L'estensione non è valida. Usa .c, .java, .cpp o .py");
                        continue;
                    }
                }

                boolean success = progettoController.creaNuovoProgetto(titolo, tipoProg, tipoDoc, estensione, nomeFile, percorso, utenteLoggato);
                if (success) {
                    GestoreNotifiche.mostraSuccesso(this, "Progetto creato con successo.");
                    caricaProgetti(); // Aggiorna la lista
                    valid = true;
                } else {
                    GestoreNotifiche.mostraErrore(this, "Errore durante la creazione del progetto nel database.");
                }
            } else {
                break; // Annullato dall'utente
            }
        }
    }

    private void apriProgetto() {
        int index = listProgetti.getSelectedIndex();
        if (index != -1) {
            Progetto selezionato = progettiCorrenti.get(index);
            progettoController.apriDettaglioProgetto(selezionato);
        } else {
            JOptionPane.showMessageDialog(this, "Seleziona un progetto dalla lista.", "Attenzione", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void generaReport() {
        int index = listProgetti.getSelectedIndex();
        if (index != -1) {
            Progetto selezionato = progettiCorrenti.get(index);
            SchermataReport report = new SchermataReport(selezionato);
            report.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Seleziona un progetto dalla lista per generare il report.", "Attenzione", JOptionPane.WARNING_MESSAGE);
        }
    }
}
