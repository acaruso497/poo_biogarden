package gui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import controller.ControllerLogin;
import controller.ControllerProprietario;
import net.miginfocom.swing.MigLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JButton;
import java.awt.Font;


public class HomePageProprietario extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private VisualizzaProgetti visualizza;
	private CreaNotifica creanotifica;
	private CreaProgetto creaprogetto;
	private ControllerProprietario controllerProprietario;
	private String username = ControllerLogin.getUsernameGlobale();
	private String CFProprietario = ControllerLogin.getCodiceFiscaleByUsername(username);
	
	public HomePageProprietario() {
		controllerProprietario = new ControllerProprietario();
		setTitle("HomePageProprietario");
	    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    setBounds(100, 100, 843, 564);
	    
	    URL imageUrl = getClass().getResource("/img/sfondoschede.PNG");
	    contentPane = new BackgroundPanel(imageUrl);
	    contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
	    setContentPane(contentPane);

	    contentPane.setLayout(new MigLayout("", "[grow][grow][grow][grow][grow][grow][grow][grow][grow][grow][grow][grow][grow][grow]", 
	    									"[grow][grow][grow][grow][grow][grow][grow][grow][grow][grow][][grow][grow][grow][grow][grow][grow]"));
	  
	    visualizza = new VisualizzaProgetti(this);
	    creanotifica = new CreaNotifica(this);
	    creaprogetto = new CreaProgetto(this); 
	    
	    JLabel LabelBenvenuto = new JLabel("Benvenuto! Sei un Proprietario");
	    LabelBenvenuto.setFont(new Font("Tahoma", Font.BOLD, 17));
	    contentPane.add(LabelBenvenuto, "cell 0 0,alignx center");
	    
	    
	    JTextArea TxtScelta = new JTextArea();
	    TxtScelta.setFont(new Font("Monospaced", Font.BOLD, 13));
	    TxtScelta.setEditable(false);
	    TxtScelta.setText("Scegli se visualizzare\r\no creare un progetto");
	    contentPane.add(TxtScelta, "cell 5 4,alignx center,aligny center");
	    TxtScelta.setOpaque(false);
	    
	    JButton ButtonVisualizza = new JButton("Visualizza");
	    contentPane.add(ButtonVisualizza, "cell 5 6,alignx center");
	    ButtonVisualizza.setPreferredSize(new Dimension(150, 20));
	    ButtonVisualizza.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				visualizza.setVisible(true);
			}
		});
	    
	    JButton ButtonCreaP = new JButton("Crea Progetto");
	    contentPane.add(ButtonCreaP, "cell 5 7,alignx center");
	    ButtonCreaP.setPreferredSize(new Dimension(150, 20));
	    ButtonCreaP.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				creaprogetto.setVisible(true);
			}
		});
	    
	    JButton ButtonCreaN = new JButton("Crea Notifica");
	    contentPane.add(ButtonCreaN, "cell 5 8,alignx center");
	    ButtonCreaN.setPreferredSize(new Dimension(150, 20));
	    
	    JButton ButtonAggiungiL = new JButton("Aggiungi Lotto");
	    ButtonAggiungiL.setPreferredSize(new Dimension(150, 20));
	    contentPane.add(ButtonAggiungiL, "cell 5 9,alignx center");
	    
	    ButtonCreaN.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				creanotifica.setVisible(true);
			}
		});
	    
	    ButtonAggiungiL.addActionListener(new ActionListener() { //aggiunge il primo lotto disponibile al proprietario
	        public void actionPerformed(ActionEvent e) {
	            boolean aggiuntaLotto = controllerProprietario.aggiungiL(CFProprietario);
	            
	            if (aggiuntaLotto==true) {
	            	creaprogetto.popolaComboLotto();
	                //aggiunta lotto
	                JOptionPane.showMessageDialog(HomePageProprietario.this, 
	                    "Lotto aggiunto con successo!", "Successo", 
	                    JOptionPane.INFORMATION_MESSAGE);
	            } else {
	                JOptionPane.showMessageDialog(HomePageProprietario.this, 
	                    "Errore: nessun lotto libero disponibile", "Errore", 
	                    JOptionPane.ERROR_MESSAGE);
	            }
	        }
	    });
	   
	}

}
