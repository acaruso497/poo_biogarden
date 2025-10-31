package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import controller.ControllerGrafico;
import controller.ControllerLogin;
import controller.CreaProgettoController;
import dao.DAO;
import net.miginfocom.swing.MigLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JComboBox;
import java.awt.Dimension;
import javax.swing.JButton;
import javax.swing.plaf.basic.BasicArrowButton;

public class Grafico extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	CreaProgettoController creaProgettoController;
	ControllerGrafico controllerGrafico;
	VisualizzaProgetti visualizzaProgetti;
	HomePageProprietario home;
	JComboBox<String> ComboLotto = new JComboBox<String>();
	JComboBox<String> ComboColtura = new JComboBox<String>();

	public Grafico() {
		home = new HomePageProprietario();
		visualizzaProgetti = new VisualizzaProgetti(home);
		
		setTitle("Grafico");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 884, 553);
		
		URL imageUrl = getClass().getResource("/img/sfondoschede.PNG");
	    contentPane = new BackgroundPanel(imageUrl);
	    contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
	    setContentPane(contentPane);

	    contentPane.setLayout(new MigLayout("", "[grow][grow][grow][grow][grow][grow][grow][grow][grow][grow][grow][grow][grow][grow]", 
	    										"[grow][grow][grow][grow][grow][grow][grow][grow][grow][grow][grow][grow][grow][grow][grow][grow]"));
	    
	    // Pulsante freccia indietro
	    BasicArrowButton ButtonIndietro = new BasicArrowButton(7);
	    ButtonIndietro.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e) {
	    		Grafico.this.setVisible(false);
	    		visualizzaProgetti.setVisible(true);
	    		
	    		//reset campi
	    		ComboLotto.setSelectedIndex(-1);
	    		ComboColtura.setSelectedIndex(-1);
	    	}
	    });
	    ButtonIndietro.setPreferredSize(new Dimension(40, 40));
	    contentPane.add(ButtonIndietro, "cell 12 0");
	    
	    JLabel LabelLotto = new JLabel("Lotto");
	    contentPane.add(LabelLotto, "cell 0 1,alignx trailing");
	    
	    ComboLotto.setSelectedIndex(-1);
	    ComboLotto.setPreferredSize(new Dimension(150, 20));
	    contentPane.add(ComboLotto, "cell 1 1,growx");
	    
	    JLabel LabelColtura = new JLabel("Coltura");
	    contentPane.add(LabelColtura, "cell 0 2,alignx trailing");
	    
	    ComboColtura.setSelectedIndex(-1);
	    ComboColtura.setPreferredSize(new Dimension(150, 20));
	    contentPane.add(ComboColtura, "cell 1 2,growx");
	    
	    JButton ButtonReport = new JButton("Report grafico");
	    ButtonReport.addActionListener(new ActionListener() { //controllo che i campi non siano vuoti e inizializzo il grafico
	    	public void actionPerformed(ActionEvent e) { 
	            Object selLotto = ComboLotto.getSelectedItem();
	            Object selColt  = ComboColtura.getSelectedItem();

	            if (selLotto == null || selColt == null) {
	                JOptionPane.showMessageDialog(Grafico.this, "Seleziona Lotto e Coltura");
	                return;
	            }

	            final int idlotto;
	            try {
	                idlotto = Integer.parseInt(selLotto.toString().trim());
	            } catch (NumberFormatException ex) {
	                JOptionPane.showMessageDialog(Grafico.this, "ID lotto non valido.");
	                return;
	            }
	            final String varieta = selColt.toString().trim();

	            ControllerGrafico controller = new ControllerGrafico();

	            double[] stats = controller.getStatistiche(idlotto, varieta);

	            //se non c'è nessun dato, avvisa
	            if (stats == null) {
	                JOptionPane.showMessageDialog(Grafico.this,
	                        "Errore nel recupero delle statistiche.");
	                return;
	            }
	            long count = Math.round(stats[0]);
	            boolean noNumeri = (Double.isNaN(stats[1]) && Double.isNaN(stats[2]) && Double.isNaN(stats[3]));
	            if (count <= 0 || noNumeri) {
	                JOptionPane.showMessageDialog(Grafico.this,
	                        "Nessuna raccolta per Lotto " + idlotto + " e " + varieta);
	                return;
	            }

	            //preparo il dataset per JFreeChart
	            DefaultCategoryDataset dataset = new DefaultCategoryDataset();
	            String serie = varieta;                   // riga/serie (legenda)
	            dataset.addValue(count,     serie, "Totale raccolte");
	            dataset.addValue(stats[1],  serie, "Media");
	            dataset.addValue(stats[2],  serie, "Min");
	            dataset.addValue(stats[3],  serie, "Max");

	            //creo e mostro il grafico
	            String titolo = "Lotto " + idlotto + " - " + varieta;
	            JFreeChart chart = ChartFactory.createBarChart(
	                    titolo,
	                    "Statistiche",
	                    "Quantità",
	                    dataset,
	                    PlotOrientation.HORIZONTAL,
	                    true,   // legenda
	                    true,   // tooltips
	                    false   // urls
	            );

	            JFrame chartFrame = new JFrame("Grafico");
	            chartFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	            chartFrame.setSize(700, 500);
	            chartFrame.getContentPane().add(new ChartPanel(chart));
	            chartFrame.setLocationRelativeTo(Grafico.this);
	            chartFrame.setVisible(true);
	        }
	    });
	    contentPane.add(ButtonReport, "cell 1 4,alignx center");
	    
	    
	    DAO dao = new DAO(); //creo il DAO
	    creaProgettoController = new CreaProgettoController(dao); //creo il controller di crea progetto
	    
	    controllerGrafico = new ControllerGrafico(); //creo il controller
	    
	    popolaComboLotto(); //Popola la ComboLotto con i lotti del proprietario loggato
	    

        // Aggiungi listener per aggiornare ComboColtura quando cambia ComboLotto
        ComboLotto.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedLotto = (String) ComboLotto.getSelectedItem();
                popolaComboColtura(selectedLotto);
            }
        });
	}
	
	private void popolaComboLotto() {  //Popolo il combolotto 
        String username = ControllerLogin.getUsernameGlobale(); // Usa l'username globale
		List<String> lotti = controllerGrafico.getLotti(username);  
        for (String lotto : lotti) { 
            ComboLotto.addItem(lotto); 
        }
        ComboLotto.setSelectedIndex(-1);
    }
	
	private void popolaComboColtura(String idLotto) { //Popolo il combocoltura 
        if (idLotto != null) {
            List<String> colture = controllerGrafico.getColturaByLotto(idLotto); 
            ComboColtura.removeAllItems();;
            for (String coltura : colture) {
                ComboColtura.addItem(coltura); 
            }
        }
        ComboColtura.setSelectedIndex(-1);
    }

}