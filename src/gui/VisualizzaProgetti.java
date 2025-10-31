package gui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.List;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicArrowButton;

import controller.ControllerLogin;
import controller.ControllerVisualizzaP;
import dao.daoVisualizzaP;
import net.miginfocom.swing.MigLayout;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;

import java.awt.Font;
import javax.swing.JTextField;
import java.awt.Color;

public class VisualizzaProgetti extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	HomePageProprietario home;
	Grafico grafico;
	private JTextField FieldStima;
	private JTextField FieldDataIP;
	private JTextField FieldDataFP;
	private JTextField FieldDataIA;
	private JTextField FieldDataFA;
	private JLabel LabelProgettoTerminato;
	private ControllerVisualizzaP controller; 
    private daoVisualizzaP dao; 
    private ButtonGroup gruppoStato;
    private String selectedProgetto = null;
    private String username = ControllerLogin.getUsernameGlobale();
    private String CFProprietario = ControllerLogin.getCodiceFiscaleByUsername(username);
    static String idLotto = null;
    JComboBox<String> ComboAttivita = new JComboBox<>();
    JComboBox<String> ComboProgetto = new JComboBox<>();
    ArrayList<String> colture= new ArrayList<String>();
    JComboBox<String> ComboColtureRacc = new JComboBox<String>();
    JRadioButton RadioPianificata = new JRadioButton("pianificata");
    JRadioButton RadioInCorso = new JRadioButton("in corso");
    JRadioButton RadioCompletata = new JRadioButton("completata");
    private JTextField FieldLotto;
    private JTextField VisualRaccolto;
    private JButton ButtonTermina;
    private JButton ButtonModificaAttivita;
	
	public VisualizzaProgetti(HomePageProprietario home) {
		this.home = home;
		
		ComboAttivita.setModel(new DefaultComboBoxModel<>( //Tipo di attività selezionabile
	    	    new String[] { "-- Seleziona --",
	    	    				"Semina", 
	    	    				"Irrigazione",
	    	    				"Raccolta" }));
		
		setTitle("Visualizza Progetti");
	    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    setBounds(100, 100, 950, 553);
	    
	    URL imageUrl = getClass().getResource("/img/sfondoschede.PNG");
	    contentPane = new BackgroundPanel(imageUrl);
	    contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
	    setContentPane(contentPane);

	    contentPane.setLayout(new MigLayout("", "[grow][grow][grow][][][grow][grow][][][grow][][grow][][][][][][grow][grow][][][grow][][][grow][grow][grow][grow][grow]", 
	    									"[grow][grow][][grow][grow][grow][][grow][][][][][grow][grow][grow][][grow][grow][grow][grow][grow][grow][grow]"));
	    
	    JLabel LabelVisualizza = new JLabel("Visualizza i tuoi progetti!");
	    LabelVisualizza.setFont(new Font("Tahoma", Font.BOLD, 17));
	    contentPane.add(LabelVisualizza, "cell 0 0");
	    
	        JButton ButtonGrafici = new JButton("Visualizza Grafici");
	        ButtonGrafici.addActionListener(new ActionListener() {
	        	public void actionPerformed(ActionEvent e) {
	        		Grafico grafico = new Grafico();
                    VisualizzaProgetti.this.setVisible(false);
                    grafico.setVisible(true);
	        	}
	        });
	        
	        ButtonTermina = new JButton("Termina");
	        ButtonTermina.setEnabled(false);
	        ButtonModificaAttivita = new JButton("Modifica");
	        
	        // Pulsante freccia indietro
	        BasicArrowButton ButtonIndietro = new BasicArrowButton(BasicArrowButton.WEST);
	        ButtonIndietro.setPreferredSize(new Dimension(40, 40));
	        contentPane.add(ButtonIndietro, "cell 5 0,alignx right,aligny center");
	        ButtonIndietro.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				resetCampi();
				setVisible(false);
				home.setVisible(true);
			}
		});
	        
	    contentPane.add(ButtonGrafici, "cell 5 1");
	    ButtonGrafici.setPreferredSize(new Dimension(150, 20));
	    
	    LabelProgettoTerminato = new JLabel("Progetto Terminato");
	    LabelProgettoTerminato.setVisible(false);
	    LabelProgettoTerminato.setForeground(Color.RED);
	    contentPane.add(LabelProgettoTerminato, "cell 1 2");
	    
	    JLabel LabelProgetto = new JLabel("Progetto");
	    contentPane.add(LabelProgetto, "cell 0 3,alignx trailing");
	    
	    contentPane.add(ComboProgetto, "cell 1 3,growx");
	    ComboProgetto.setPreferredSize(new Dimension(150, 20));
	    
	    contentPane.add(ButtonTermina, "cell 2 3,aligny center");
	    ButtonTermina.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e) { //termina il progetto selezionato
	    		selectedProgetto = (String) ComboProgetto.getSelectedItem();
	    		String lotto = FieldLotto.getText();
	    		
	    		if(selectedProgetto == null || lotto.isEmpty()) {
	                JOptionPane.showMessageDialog(VisualizzaProgetti.this, 
	                							  "Seleziona un progetto valido!");
	                return;
	            }
	            	 
	    		boolean termina = controller.terminaProgetto(selectedProgetto, lotto);
	    		
	    		if(termina==true) {
	    			JOptionPane.showMessageDialog(VisualizzaProgetti.this, "Progetto terminato con successo!");
	    			ButtonTermina.setEnabled(false);
	    			ButtonModificaAttivita.setEnabled(false);
	    			RadioPianificata.setEnabled(false);
                	RadioInCorso.setEnabled(false);
                	RadioCompletata.setEnabled(false);
                	LabelProgettoTerminato.setVisible(true);
	    		}
	    	}
	    });
	    
	    JLabel LabelStima = new JLabel("Stima raccolto");
	    contentPane.add(LabelStima, "cell 4 3,alignx trailing");
	    
	    FieldStima = new JTextField();
	    contentPane.add(FieldStima, "cell 5 3,growx");
	    FieldStima.setColumns(10);
	    FieldStima.setEditable(false); 
	    
	    JLabel LabelDataIP = new JLabel("Data Inizio");
	    contentPane.add(LabelDataIP, "cell 0 4,alignx right,aligny center");
	    
	    FieldDataIP = new JTextField(); 
	    contentPane.add(FieldDataIP, "cell 1 4,growx");
	    FieldDataIP.setColumns(10);
	    FieldDataIP.setEditable(false);
	    
	    JLabel LabelDataFP = new JLabel("Data Fine");
	    contentPane.add(LabelDataFP, "cell 0 5,alignx right,aligny center");
	    
	    FieldDataFP = new JTextField(); 
	    contentPane.add(FieldDataFP, "cell 1 5,growx");
	    FieldDataFP.setColumns(10);
	    FieldDataFP.setEditable(false); 
	    
	    gruppoStato = new ButtonGroup(); 
	    
	    JLabel LabelLotto = new JLabel("Lotto");
	    contentPane.add(LabelLotto, "cell 0 7,alignx trailing");
	    
	    FieldLotto = new JTextField();
	    FieldLotto.setEditable(false);
	    FieldLotto.setColumns(10);
	    contentPane.add(FieldLotto, "cell 1 7,growx");
	    
	    JLabel lblRaccoltoColtura = new JLabel("Raccolto coltura");
	    contentPane.add(lblRaccoltoColtura, "cell 2 10,alignx center,aligny center");
	    
	    JLabel ColtureRaccolte = new JLabel("Colture");
	    contentPane.add(ColtureRaccolte, "cell 0 11,alignx trailing");
	    
	    dao = new daoVisualizzaP();
	    controller = new ControllerVisualizzaP(dao);
	    ComboColtureRacc.setSelectedIndex(-1);
	    ComboColtureRacc.setPreferredSize(new Dimension(150, 20));
	    contentPane.add(ComboColtureRacc, "cell 1 11,growx");
	    VisualRaccolto = new JTextField();
	    VisualRaccolto.setEditable(false);
	    VisualRaccolto.setColumns(10);
	    contentPane.add(VisualRaccolto, "cell 2 11,growx");
	    
	    JLabel LabelAttivita = new JLabel("Ultima Attività");
	    contentPane.add(LabelAttivita, "cell 0 17,alignx trailing");
	
	    ComboColtureRacc.addActionListener(new ActionListener() { //popola la combobox con le colture presenti
	        public void actionPerformed(ActionEvent e) {
	            try {
	                String selectedColtura = (String) ComboColtureRacc.getSelectedItem();
	                if (selectedColtura != null && !selectedColtura.equals("--Seleziona coltura--")) {
	                    String lottoText = FieldLotto.getText();
	                    if (lottoText != null && !lottoText.isEmpty()) {
	                        int idLotto = Integer.parseInt(lottoText);
	                        String raccolto = controller.getRaccoltoProdotto(username, idLotto, selectedColtura);
	                        VisualRaccolto.setText(raccolto);
	                    } else {
	                        VisualRaccolto.setText("no dati"); // Default se il lotto non è valido
	                    }
	                } else {
	                    VisualRaccolto.setText("..."); // Reset se non c'è selezione valida
	                }
	            } catch (NumberFormatException ex) {
	                JOptionPane.showMessageDialog(VisualizzaProgetti.this, 
	                    "Errore nel formato del lotto", "Errore", JOptionPane.ERROR_MESSAGE);
	                VisualRaccolto.setText("error");
	            } catch (NullPointerException ex) {
	                JOptionPane.showMessageDialog(VisualizzaProgetti.this, 
	                    "Seleziona una coltura valida", "Errore", JOptionPane.ERROR_MESSAGE);
	                VisualRaccolto.setText("ponterN");
	            }
	        }
	    });	    
	    contentPane.add(ComboAttivita, "cell 1 17,growx");
	    ComboAttivita.setPreferredSize(new Dimension(150, 20));
	    
	    contentPane.add(RadioPianificata, "cell 3 17");
	    RadioPianificata.setOpaque(false);
	    gruppoStato.add(RadioPianificata);
	    
	    contentPane.add(RadioInCorso, "cell 4 17");
	    RadioInCorso.setOpaque(false);
	    gruppoStato.add(RadioInCorso);
	    
	    contentPane.add(RadioCompletata, "flowx,cell 5 17");
	    RadioCompletata.setOpaque(false);
	    gruppoStato.add(RadioCompletata);
	    
	    JLabel LabelDataIA = new JLabel("Data Inizio");
	    contentPane.add(LabelDataIA, "cell 0 18,alignx trailing");
	    
	    FieldDataIA = new JTextField(); 
	    contentPane.add(FieldDataIA, "cell 1 18,growx");
	    FieldDataIA.setColumns(10);
	    FieldDataIA.setEditable(false); 
	    
	    JLabel LabelDataFA = new JLabel("Data Fine");
	    contentPane.add(LabelDataFA, "cell 0 19,alignx trailing");
	    
	    FieldDataFA = new JTextField(); 
	    contentPane.add(FieldDataFA, "cell 1 19,growx");
	    FieldDataFA.setColumns(10);
	    FieldDataFA.setEditable(false); 
	    
	    ButtonModificaAttivita.addActionListener(new ActionListener() { 	//modifica lo stato delle attività selezionate
	    	public void actionPerformed(ActionEvent e) { 
	    			String selectedProgetto = (String) ComboProgetto.getSelectedItem();
		    	    String selectedLotto = FieldLotto.getText();
		    	    String selectedAttivita = (String) ComboAttivita.getSelectedItem();
		    	    if (ComboAttivita.getSelectedItem().equals("-- Seleziona --") || selectedProgetto.isEmpty() || selectedLotto.isEmpty()) {
		                JOptionPane.showMessageDialog(VisualizzaProgetti.this, 
		                							  "COMPILA TUTTI I CAMPI!!", "Errore", JOptionPane.ERROR_MESSAGE);
		            } else {
		            	String selectedStato = "";
					    if (RadioPianificata.isSelected()) { // setta i pallini del radiobutton
					    	  selectedStato = RadioPianificata.getText(); // Restituisce attività "pianificata"
					    	  controller.aggiornaStato(selectedStato, selectedAttivita, selectedLotto);
					     } else if (RadioInCorso.isSelected()) {
					    	   selectedStato = RadioInCorso.getText(); // Restituisce attività "in corso"
					    	   controller.aggiornaStato(selectedStato, selectedAttivita, selectedLotto);
					     } else if (RadioCompletata.isSelected()) {
					    	   selectedStato = RadioCompletata.getText(); // Restituisce attività "completata"
					    	   controller.aggiornaStato(selectedStato, selectedAttivita, selectedLotto);
					     } 
				            JOptionPane.showMessageDialog(VisualizzaProgetti.this, 
				                "Attività aggiornata con successo!");
		            }	      
	    	}
	    });
	    contentPane.add(ButtonModificaAttivita, "cell 5 17");
	    
	    dao = new daoVisualizzaP();
        controller = new ControllerVisualizzaP(dao);
	    
        popolaComboProgetto();
        
        ComboProgetto.addActionListener(new ActionListener() { //popola la combobox del progetto
            public void actionPerformed(ActionEvent e) {
            	//pulizia campi
            	ComboAttivita.setSelectedIndex(0);
                ComboColtureRacc.setSelectedIndex(-1);
                FieldStima.setText("");
                FieldDataIP.setText("");
                FieldDataFP.setText("");
                FieldDataIA.setText("");
                FieldDataFA.setText("");
                FieldLotto.setText("");
                VisualRaccolto.setText("");
                gruppoStato.clearSelection();
            	
              try {
	                String selectedProgetto = (String) ComboProgetto.getSelectedItem(); 
	                if (selectedProgetto == null || selectedProgetto.equals("") || ComboProgetto.getSelectedIndex() == -1) {
	                    resetCampi();
	                    return;
	                }
	                
	                controller.popolaDatiProgetto(selectedProgetto, FieldStima, 
	                								FieldDataIP, FieldDataFP); // Popola i campi progetto
	                popolaFieldLotto(); //una volta che ha trovato un progetto, popola il field del lotto
	                String lotto = FieldLotto.getText();
	                
	                boolean isCompletato = controller.isCompletata(username, selectedProgetto);
	                if(isCompletato==true) { //se il progetto è completato disabilita i campi
	                	ButtonTermina.setEnabled(false);
	                	ButtonModificaAttivita.setEnabled(false);
	                	RadioPianificata.setEnabled(false);
	                	RadioInCorso.setEnabled(false);
	                	RadioCompletata.setEnabled(false);
	                    LabelProgettoTerminato.setVisible(true);
	                }else { //se non è completato, li abilita
	                	ButtonTermina.setEnabled(true);
	                	ButtonModificaAttivita.setEnabled(true);
	                	RadioPianificata.setEnabled(true);
	                	RadioInCorso.setEnabled(true);
	                	RadioCompletata.setEnabled(true);
	                    LabelProgettoTerminato.setVisible(false);
	                }
	                
	                if (lotto != null && !lotto.isEmpty()) { //prima di popolare la combo delle colture, verifica l'esistenza di un lotto
	            	    colture = controller.getColtureProprietario(Login.CFProprietario, selectedProgetto );
	            	    ComboColtureRacc.removeAllItems();
	            	    ComboColtureRacc.addItem("--Seleziona coltura--");
	            	    for (String coltura : colture) {
	            	        ComboColtureRacc.addItem(coltura);
	            	    }
	                } else { //non trova nulla, reset
	                	ComboColtureRacc.setEnabled(false); 
	                	ComboColtureRacc.removeAllItems();
	                    
	                }	    
                }catch (NullPointerException ex) { //controllo progetto
            		JOptionPane.showMessageDialog(VisualizzaProgetti.this, 
            		"Seleziona un progetto valido", "Errore", JOptionPane.ERROR_MESSAGE);
            		resetCampi();
            	}
            }
        });
        
        
        ComboAttivita.addActionListener(new ActionListener() { //popola la combobox delle attività
            public void actionPerformed(ActionEvent e) {
              try {
            	  	String selectedAttivita = (String) ComboAttivita.getSelectedItem(); //converto il tipo di attività selezionata in una stringa
                    Object selectedProgetto = ComboProgetto.getSelectedItem(); //estraggo l'id del progetto selezionato dalla combobox
                    
                    if (selectedAttivita == null || selectedAttivita.equals("-- Seleziona --") || 
                            selectedProgetto == null || ComboProgetto.getSelectedIndex() == -1) {
                            // Resetta i campi relativi all'attività
                            FieldDataIA.setText("");
                            FieldDataFA.setText("");
                            gruppoStato.clearSelection();
                            return;
                        }
                    
                    String idProgettoStr = selectedProgetto.toString(); //converto l'id del progetto selezionato in una stringa
                    
                    // Chiamata al controller per ottenere lo stato e popolare data inizio e data fine
		    	    String stato = controller.popolaAttivita(idProgettoStr, selectedAttivita, FieldDataIA, FieldDataFA);
		    	    
                    if ("pianificata".equals(stato)) {  // il radio button corrispondente confronta lo stato nel dao e setta il pallino
                        RadioPianificata.setSelected(true);
                    } else if ("in corso".equals(stato)) {
                        RadioInCorso.setSelected(true);
                    } else if ("completata".equals(stato)) {
                        RadioCompletata.setSelected(true);
                    } 
                    
                	}catch (NullPointerException ex) {
                		JOptionPane.showMessageDialog(VisualizzaProgetti.this, 
                									  "Seleziona un'attività valida", "Errore", JOptionPane.ERROR_MESSAGE);
                	}
                }                 
        });       
	}
	
	 
    private void popolaComboProgetto() { 	 // Popola ComboProgetto
    	
        List<String> progetti = controller.getProgettiByProprietario(username); 
        for (String idProgetto : progetti) {
            ComboProgetto.addItem(idProgetto); //popola la combobox con l'id progetto
        }
        ComboProgetto.setSelectedIndex(-1);
    }


    private void popolaFieldLotto() {  //Popola il field del lotto
        String selectedProgetto = (String) ComboProgetto.getSelectedItem(); //mi prendo il progetto selezionato
        
        // se non trovo il progetto resetta i campi
        if (selectedProgetto == null || selectedProgetto.isEmpty()) {
            FieldLotto.setText("");
            FieldLotto.setEnabled(false);
            return;
        }
        
        try {
             idLotto = controller.getLottiByProprietario(selectedProgetto, CFProprietario); 
            
            //controlla l'esistenza di un lotto
            if (idLotto != null) { 
                FieldLotto.setText(String.valueOf(idLotto)); //imposto l'id del lotto nel field
                FieldLotto.setEnabled(true);
            } else {
                FieldLotto.setText("");
                FieldLotto.setEnabled(false);
                JOptionPane.showMessageDialog(VisualizzaProgetti.this, 
                    "Nessun lotto trovato per questo progetto di coltivazione!", 
                    "Errore", JOptionPane.WARNING_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            ex.printStackTrace();
            FieldLotto.setText("");
            FieldLotto.setEnabled(false);
        }
    }
    
    private void resetCampi() { //pulizia dei campi
        ComboAttivita.setSelectedIndex(0);
        ComboColtureRacc.setSelectedIndex(-1);
        ComboProgetto.setSelectedIndex(-1);
        FieldStima.setText("");
        FieldDataIP.setText("");
        FieldDataFP.setText("");
        FieldDataIA.setText("");
        FieldDataFA.setText("");
        FieldLotto.setText("");
        VisualRaccolto.setText("");
        gruppoStato.clearSelection();
        ButtonTermina.setEnabled(true);
        ButtonModificaAttivita.setEnabled(true);
        RadioPianificata.setEnabled(true);
        RadioInCorso.setEnabled(true);
        RadioCompletata.setEnabled(true);
        LabelProgettoTerminato.setVisible(false);
    }

}
