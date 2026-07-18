package boundary;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import control.ReportController;
import entity.Attivita;
import entity.Progetto;

public class SchermataReport extends JFrame {

    private Progetto progetto;
    private ReportController reportController;

    public SchermataReport(Progetto progetto) {
        this.progetto = progetto;
        this.reportController = new ReportController();
        inizializzaInterfaccia();
    }

    private void inizializzaInterfaccia() {
        setTitle("Report Progetto - " + progetto.getTitolo());
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        List<Attivita> attivita = reportController.getAttivitaProgetto(progetto);
        int[] statsStato = reportController.getStatisticheStato(attivita);
        int numSviluppo = reportController.getNumeroAttivitaSviluppo(attivita);
        
        List<String> membriConCompletate = reportController.getMembriConAttivitaCompletate(attivita);

        // Informazioni di testata
        JPanel pannelloInfo = new JPanel(new GridLayout(0, 1));
        pannelloInfo.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        pannelloInfo.add(new JLabel("Totale Attività: " + attivita.size()));
        pannelloInfo.add(new JLabel("Completate: " + statsStato[2] + " | In corso: " + statsStato[1] + " | Non iniziate (Da fare): " + statsStato[0]));
        pannelloInfo.add(new JLabel("Attività di Sviluppo: " + numSviluppo));
        
        // Calcolo indiretto: numero medio revisioni = numero attività di sviluppo per questo progetto (singolo file)
        pannelloInfo.add(new JLabel("Numero medio di revisioni per file: " + numSviluppo));
        
        add(pannelloInfo, BorderLayout.NORTH);

        // Grafici
        JPanel pannelloCharts = new JPanel(new GridLayout(1, 2));

        // Grafico a torta: Stato di avanzamento
        DefaultPieDataset pieDataset = new DefaultPieDataset();
        pieDataset.setValue("Da fare", statsStato[0]);
        pieDataset.setValue("In corso", statsStato[1]);
        pieDataset.setValue("Completata", statsStato[2]);
        
        JFreeChart pieChart = ChartFactory.createPieChart(
                "Stato Attività",
                pieDataset,
                true, true, false);
        ChartPanel pieChartPanel = new ChartPanel(pieChart);
        pannelloCharts.add(pieChartPanel);

        // Grafico a barre: Attività completate per utente
        DefaultCategoryDataset barDataset = new DefaultCategoryDataset();
        for (String membro : membriConCompletate) {
            int completate = reportController.getAttivitaCompletateDaMembro(attivita, membro);
            barDataset.addValue(completate, "Attività", membro);
        }

        JFreeChart barChart = ChartFactory.createBarChart(
                "Attività completate per Membro",
                "Membro",
                "Numero Attività",
                barDataset,
                PlotOrientation.VERTICAL,
                false, true, false);
                
        // Forza numeri interi sull'asse Y (es. 1, 2, 3 invece di 1.0, 1.5)
        CategoryPlot plot = barChart.getCategoryPlot();
        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        
        ChartPanel barChartPanel = new ChartPanel(barChart);
        pannelloCharts.add(barChartPanel);

        add(pannelloCharts, BorderLayout.CENTER);

        JPanel pannelloButton = new JPanel();
        JButton bottoneChiudi = new JButton("Chiudi");
        bottoneChiudi.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SchermataReport.this.dispose();
            }
        });
        pannelloButton.add(bottoneChiudi);
        add(pannelloButton, BorderLayout.SOUTH);
    }
}

