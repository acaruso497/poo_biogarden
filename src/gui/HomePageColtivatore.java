package gui;


import java.awt.Color;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import controller.Controller;

import dto.*;

import utils.*;
import net.miginfocom.swing.MigLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JButton;

public class HomePageColtivatore extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField FieldDataIP;
	private JTextField FieldDataFP;
	private JTextField FieldDataIA;
	private JTextField FieldDataFA;
	private JTextField FieldPosizione;
	private JTextField FieldIrrigazione;
	private JTextField FieldEsperienza;
	private JTextField FieldStima;
	private JTextField lottovisualizza;
    private JTextField tipoSeminaField;
    private JTextField FieldRaccoltoColture;
    private JButton ButtonSalva;
	private JComboBox<String> ComboProgetti;	
    private JComboBox<String> ComboAttivita;
    private List<String> tipiAttivita;
    private JComboBox<String> ComboTipologia;
    private RaccoltaDTO RaccoltaDTO;
    Controller controller = new Controller();
  //  ColtivatoreDTO coltivatore = method.getColtivatoreLoggato(); //recupera il il Coltivatore loggato
	private ColtivatoreDTO coltivatore;//AGGIUNTO
//	private NotificaDTO notifica;
//	private AttivitaDTO attivita;
	private ProgettoColtivazioneDTO progetto;
	
	@SuppressWarnings("unused")
	public HomePageColtivatore() {
		//AGGIUNTO
		coltivatore=new ColtivatoreDTO(method.getUsernameGlobale(), method.getPsw());
	    coltivatore=controller.getColtivatore(coltivatore);

	    method.setColtivatoreLoggato(coltivatore); 	    
	  //AGGIUNTO
		setTitle("HomePageColtivatore");
	    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    setBounds(100, 100, 893, 564);
	    
	    URL imageUrl = getClass().getResource("/img/sfondoschede.PNG");
	    contentPane = new BackgroundPanel(imageUrl);
	    contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
	    setContentPane(contentPane);


	    contentPane.setLayout(new MigLayout("", "[grow][grow][grow][grow][grow][grow][][][grow][][][][][][][][grow][grow][grow][grow][grow][grow][grow][][][][]", 
	    									"[grow][grow][grow][grow][grow][grow][grow][][][][][grow][grow][][][grow][grow][grow][grow][grow][grow][grow]"));
	    
	    JLabel LabelBenvenuto = new JLabel("Benvenuto sei un coltivatore!");
	    contentPane.add(LabelBenvenuto, "cell 0 0");
	    
	    JToggleButton TButtonNotifiche = new JToggleButton("");
	    contentPane.add(TButtonNotifiche, "cell 23 0,alignx center,aligny center");
	    TButtonNotifiche.setBorderPainted(false);
	    TButtonNotifiche.setContentAreaFilled(false);
	    TButtonNotifiche.setFocusPainted(false);
	    
	    JTextArea TxtListaNotifiche = new JTextArea();
	    JScrollPane scrollNotifiche = new JScrollPane(TxtListaNotifiche);
	    
	    JLabel LabelEsperienza = new JLabel("Esperienza");
	    FieldEsperienza = new JTextField();
	    
	    JLabel LabelProgetti = new JLabel("Progetti");
	    
	    JLabel LabelDataIP = new JLabel("Data Inizio");
	    
	    ComboProgetti = new JComboBox<>();
	    
	    ComboTipologia = new JComboBox<>();
	    
	    JLabel LabelDataFP = new JLabel("Data Fine");
	    FieldDataFP = new JTextField();
	    
	    JLabel LabelStima = new JLabel("Stima raccolto");
	    FieldStima = new JTextField();
	    
	    JLabel LabelAttivita = new JLabel("Attività");
	    ComboAttivita = new JComboBox<>();
	    
	    JLabel LabelDataIA = new JLabel("Data Inizio");
	    FieldDataIA = new JTextField();
	    
	    JLabel LabelDataFA = new JLabel("Data Fine");
	    FieldDataFA = new JTextField();
	    
	    JLabel LabelTipologia = new JLabel("Tipologia Coltura");
	    FieldDataIP = new JTextField();
	    
	    JLabel lblTipoSemina = new JLabel("Tipo Semina");
	    tipoSeminaField = new JTextField();
	    
	    TxtListaNotifiche.setEditable(false);
	    
	    scrollNotifiche.setVisible(false); // Inizialmente nascosto
	    
	    contentPane.add(LabelEsperienza, "cell 7 1,alignx right");
	   
	    FieldEsperienza.setEditable(false);
	    contentPane.add(FieldEsperienza, "cell 8 1,growx");
	    FieldEsperienza.setColumns(10);
	    contentPane.add(scrollNotifiche, "cell 22 1 2 4,grow");
	    
	    //popola esperienza
	    
	    String esperienza = coltivatore.getEsperienza();
	    FieldEsperienza.setText(esperienza);
	    
	    //CONTROLLO NOTIFICHE - scelgo l'immagine in base alle notifiche in arrivo 
	    if(!controller.checknotifiche(coltivatore)){
		    ImageIcon originalIcon = new ImageIcon(getClass().getResource("/img/notifichevuote.png"));
		    Image scaledImage = originalIcon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);	//ridimensionamento immagine
		    ImageIcon scaledIcon = new ImageIcon(scaledImage);
		    TButtonNotifiche.setIcon(scaledIcon);
	    }
	    else {
	    	TxtListaNotifiche.setText(controller.mostranotifiche(coltivatore));
	    	controller.legginotifiche(coltivatore);
		    ImageIcon originalIcon = new ImageIcon(getClass().getResource("/img/notifichepiene.png"));
		    Image scaledImage = originalIcon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);	//ridimensionamento immagine
		    ImageIcon scaledIcon = new ImageIcon(scaledImage);
		    TButtonNotifiche.setIcon(scaledIcon);
	    }
	    
	    TButtonNotifiche.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
		        // Se il bottone è attivato, mostra la tendina
		        scrollNotifiche.setVisible(TButtonNotifiche.isSelected());
		        // Per ridisegnare il pannello dopo il cambio visibilità
		        contentPane.revalidate();
		        contentPane.repaint();
		        
//		        ControllerColtivatore controllerColtivatore = new ControllerColtivatore();
		        controller.legginotifiche(coltivatore);
			}
		});
	    
	    
	    contentPane.add(LabelProgetti, "cell 1 1,alignx trailing");
	   
	    contentPane.add(ComboProgetti, "cell 2 1,growx");
	    dropdownProg();
	    
	    contentPane.add(LabelDataIP, "flowx,cell 0 2,alignx right");
	    
	    contentPane.add(LabelDataFP, "flowx,cell 1 2,alignx trailing");
	    
	    FieldDataFP.setEditable(false);
	    contentPane.add(FieldDataFP, "cell 2 2,growx");
	    FieldDataFP.setColumns(10);
	    
	    ComboProgetti.addActionListener(e -> {
	        aggiornaLottoEPosizione();    
	        aggiornaCampiProgetto();      
	        popolaAttivita();
	    });
	    
	    contentPane.add(LabelStima, "cell 7 3,alignx right");
	    
	    FieldStima.setEditable(false);
	    contentPane.add(FieldStima, "flowx,cell 8 3,growx");
	    FieldStima.setColumns(10);
	    JLabel kg = new JLabel("KG");
	    
	    contentPane.add(kg, "cell 9 3");
	    
	    contentPane.add(LabelAttivita, "cell 1 4,alignx trailing,aligny baseline");
	    
	    contentPane.add(ComboAttivita, "cell 2 4,growx");
	   
	    contentPane.add(LabelDataIA, "flowx,cell 0 5");
	    
	    contentPane.add(LabelDataFA, "cell 1 5,alignx trailing");
	    
	    FieldDataFA.setEditable(false);
	    contentPane.add(FieldDataFA, "cell 2 5,growx");
	    FieldDataFA.setColumns(10);
	    
	    FieldDataIA.setEditable(false);
	    contentPane.add(FieldDataIA, "cell 0 5");
	    FieldDataIA.setColumns(10);
	    
	    contentPane.add(LabelTipologia, "cell 7 6,alignx trailing");
	    
	    FieldDataIP.setEditable(false);
	    contentPane.add(FieldDataIP, "cell 0 2");
	    FieldDataIP.setColumns(10);
	    
	    contentPane.add(ComboTipologia, "cell 8 6 8 1,growx");
	    
	    contentPane.add(lblTipoSemina, "cell 1 7,alignx trailing");
	    
	    tipoSeminaField.setEditable(false);
	    tipoSeminaField.setColumns(10);
	    contentPane.add(tipoSeminaField, "cell 2 7,growx");
	    JLabel LabelIrrigazione = new JLabel("Irrigazione");
	    
	    contentPane.add(LabelIrrigazione, "cell 7 8,alignx trailing");
	    FieldIrrigazione = new JTextField();
	    	    
	    FieldIrrigazione.setEditable(false);
	    contentPane.add(FieldIrrigazione, "cell 8 8 6 1,growx");
	    FieldIrrigazione.setColumns(10);
	    JLabel lblRaccoltoColture = new JLabel("Raccolto Colture");
	    
	    contentPane.add(lblRaccoltoColture, "cell 7 10,alignx trailing");
	    FieldRaccoltoColture = new JTextField();
	    
	    FieldRaccoltoColture.setColumns(10);
	    contentPane.add(FieldRaccoltoColture, "flowx,cell 8 10,growx");
	    FieldRaccoltoColture.setEnabled(false); 
	    JLabel kgColtura = new JLabel("KG");
	    
	    contentPane.add(kgColtura, "cell 9 10");
	    JLabel LabelLotti = new JLabel("Lotto assegnato");
	    
	    contentPane.add(LabelLotti, "cell 1 12,alignx trailing");
	    lottovisualizza = new JTextField();
	    
	    lottovisualizza.setEditable(false);
	    lottovisualizza.setColumns(10);
	    contentPane.add(lottovisualizza, "cell 2 12,growx");
	    ButtonSalva = new JButton("Salva");
	    
	    ButtonSalva.setEnabled(false); 
	    contentPane.add(ButtonSalva, "cell 8 12,alignx center");
	    RaccoltaDTO= new RaccoltaDTO();
	    ButtonSalva.addActionListener(new ActionListener() { //salva il raccolto delle colture inserite
	    	public void actionPerformed(ActionEvent e) {
	    		try {
	    		//	ControllerColtivatore controller = new ControllerColtivatore();
	    			progetto = new ProgettoColtivazioneDTO((String) ComboProgetti.getSelectedItem());
		    		ColturaDTO selectedColtura = new ColturaDTO((String) ComboTipologia.getSelectedItem());
		    		RaccoltaDTO.sommaRaccolto( (String) FieldRaccoltoColture.getText(),selectedColtura , progetto);
		    		
		    		// ---CONTROLLI SUI FIELDS---
	    			if (selectedColtura.getVarieta() == null || selectedColtura.getVarieta().isEmpty() || RaccoltaDTO.getRaccolto() == null || RaccoltaDTO.getRaccolto().isEmpty()) {
					    JOptionPane.showMessageDialog(HomePageColtivatore.this, 
					    							  "COMPILA TUTTI I CAMPI !!", "Errore", JOptionPane.ERROR_MESSAGE);
					    return; 
		    		}
	    			
	    			
	    			int raccolto = Integer.parseInt(RaccoltaDTO.getRaccolto() );
	    			if (raccolto <= 0) { //controlla se è stato inserito un raccolto minore o pari a 0
		    			JOptionPane.showMessageDialog(HomePageColtivatore.this, 
		    					"Il raccolto deve essere maggiore di 0!", "Errore", 
		    					JOptionPane.ERROR_MESSAGE);
		    			FieldRaccoltoColture.setBackground(Color.RED);
		    			return;
	    			}else {
	    				boolean sommaRaccolto = RaccoltaDTO.sommaRaccolto(RaccoltaDTO.getRaccolto() , selectedColtura, progetto);
	    				JOptionPane.showMessageDialog(HomePageColtivatore.this, 
	    											  "Il raccolto è stato aggiornato con successo!");
	    			}
	            } catch (NumberFormatException ex) { //controllo formato numerico
	                JOptionPane.showMessageDialog(HomePageColtivatore.this, 
	                			"Il raccolto deve essere un numero valido!", 
	                			"Errore", JOptionPane.ERROR_MESSAGE);
	                FieldRaccoltoColture.setBackground(Color.RED);
	                return;
	            }
	    		//pulisce i field
	    		FieldRaccoltoColture.setText(""); 
	    		FieldRaccoltoColture.setBackground(Color.WHITE);
	    	}
	    });
	    JLabel LabelPosizioneLotto = new JLabel("Posizione lotto");
	    
	    contentPane.add(LabelPosizioneLotto, "cell 1 13,alignx trailing");
	    FieldPosizione = new JTextField();
	    
	    FieldPosizione.setEditable(false);
	    contentPane.add(FieldPosizione, "cell 2 13,growx");
	    FieldPosizione.setColumns(10);
	   
	    popolaAttivita();
	    
	    }
	  
	private void dropdownProg() { 	//popola la dropdown con i titoli dei progetti
	    //ControllerColtivatore controller = new ControllerColtivatore();
	    ComboProgetti.removeAllItems();
	    ComboProgetti.addItem("--seleziona--");
	    for (String p : controller.popolaPrComboBox(coltivatore)){
	        ComboProgetti.addItem(p);
	    }
	    ComboProgetti.setSelectedItem("--seleziona--");
	}
	
	
	private void aggiornaCampiProgetto() { //imposta i campi relativi al progetto
	    String progettoSelezionato = (String) ComboProgetti.getSelectedItem();

	    
	    if (progettoSelezionato != null && !progettoSelezionato.equals("--seleziona--")) {
	     
	        
	        // Date progetto
	        List<String> dateProgetto = ProgettoColtivazioneDTO.DateInizioFineP(progettoSelezionato, coltivatore);
	        if (dateProgetto != null && dateProgetto.size() >= 2) {
	            FieldDataIP.setText(dateProgetto.get(0));
	            FieldDataFP.setText(dateProgetto.get(1));
	        }
	        
	        // COLTURA 
	        ComboTipologia.removeAllItems();
	        List<String> colture = controller.getColtura(coltivatore, progettoSelezionato); 
	        for (String tipoColtura : colture) {
	        	ComboTipologia.addItem(tipoColtura); 
	        }
	        ComboTipologia.setSelectedIndex(-1);
	        
	        // Altri campi
	        FieldStima.setText(controller.getStimaRaccolto(coltivatore, progettoSelezionato));
	        FieldIrrigazione.setText(controller.getIrrigazione(coltivatore, progettoSelezionato));
	        
	    } else {
	        // Pulisci tutti i campi
	        FieldDataIP.setText("");
	        FieldDataFP.setText("");
	        FieldStima.setText("");
	        ComboTipologia.removeAllItems();
	        FieldRaccoltoColture.setText("");
	        FieldIrrigazione.setText("");
	    }
	}  
	@SuppressWarnings("unused")
	private void popolaAttivita() { 	//popola i campi relativi alle attività
	    String progettoSelezionato = (String) ComboProgetti.getSelectedItem();
//	    String username = coltivatore.getUsername();
	    ComboAttivita.removeAllItems();
	    ComboAttivita.addItem("--seleziona--");
	    
	    if (progettoSelezionato == null || progettoSelezionato.equals("--seleziona--")) {
	        // PULISCI I CAMPI QUANDO NON C'È PROGETTO
	        FieldDataIA.setText("");
	        FieldDataFA.setText("");
	        tipoSeminaField.setText("");
	        return;
	    }
 
	    tipiAttivita = controller.getTipiAttivita(coltivatore, progettoSelezionato);
	    
	    for (String idSpecifico : controller.getIdAttivita(coltivatore, progettoSelezionato)) {
	        ComboAttivita.addItem(idSpecifico);
	    }
	    
	    ComboAttivita.setSelectedItem("--seleziona--");
	    
	    // RIMUOVI PRIMA I VECCHI LISTENER PER EVITARE DUPLICAZIONE
	    for (ActionListener al : ComboAttivita.getActionListeners()) {
	        ComboAttivita.removeActionListener(al);
	    }
	    
	    // AGGIUNGI IL NUOVO LISTENER
	    ComboAttivita.addActionListener(e -> {
	        aggiornaDateAttivita();  
	        
	    	String selectedAttivita = (String) ComboAttivita.getSelectedItem();
	    	
	    	// se non viene selezionata l'attività, blocca i field relativi alle attività
	        if (selectedAttivita == null || selectedAttivita.equals("-- Seleziona --")) {
	            FieldRaccoltoColture.setEnabled(false); 
	            FieldRaccoltoColture.setEditable(false);
	            FieldRaccoltoColture.setText("");
	            ComboTipologia.setEnabled(false);
	            ButtonSalva.setEnabled(false);  
	        } else if (selectedAttivita.startsWith("Raccolta-")) {  //controlla se l'attività selezionata inizia per Raccolta-
	            FieldRaccoltoColture.setEnabled(true); 
	            FieldRaccoltoColture.setEditable(true);
	            ButtonSalva.setEnabled(true); 
	            ComboTipologia.setEnabled(true);
	        }else { //se vengono selezionate attività diverse da raccolta, vengono bloccati i campi relativi
	        	FieldRaccoltoColture.setEnabled(false);
	            FieldRaccoltoColture.setEditable(false);
	            FieldRaccoltoColture.setText("");
	            ButtonSalva.setEnabled(false);
	            ComboTipologia.setEnabled(false);
	        }
	        
	    });
	}
	
	  
	private void aggiornaDateAttivita() { 	//imposta i campi delle date delle diverse attività
	    String attivitaSelezionata = (String) ComboAttivita.getSelectedItem();
	    
	    if (attivitaSelezionata != null && !attivitaSelezionata.equals("--seleziona--")) {
	        int selectedIndex = ComboAttivita.getSelectedIndex() - 1;
	        
	        if (tipiAttivita != null && tipiAttivita.size() > selectedIndex && selectedIndex >= 0) {
	            String tipo = tipiAttivita.get(selectedIndex); // "Semina", "Irrigazione", "Raccolta"
	            
	            String[] parts = attivitaSelezionata.split("-");
	            if (parts.length >= 2) {
	                String id = parts[1];
	                
	                try {
	                    
	                    String[] date = controller.getDateByAttivitaId(id, tipo);
	                    
	                    if (date != null && date[0] != null) {
	                        FieldDataIA.setText(date[0]);
	                        FieldDataFA.setText(date[1]);
	                    } else {
	                        FieldDataIA.setText("");
	                        FieldDataFA.setText("");
	                    }
	                    
	                    // Gestione tipo semina 
	                    if (tipo.equals("Semina")) {
	                        String tipoSemina = controller.getTipoSemina(id);
	                        tipoSeminaField.setText(tipoSemina);
	                    } else {
	                        tipoSeminaField.setText("");
	                    }
	                    
	                } catch (NumberFormatException e) { 	//controllo del formato numerico
	                    System.err.println("ID non valido: " + id);
	                    FieldDataIA.setText("");
	                    FieldDataFA.setText("");
	                    tipoSeminaField.setText("");
	                }
	            }
	        }
	    } else {
	        FieldDataIA.setText("");
	        FieldDataFA.setText("");
	        tipoSeminaField.setText("");
	    }
	}
	
		private void aggiornaLottoEPosizione() { 	//lotto e posizione in base al progetto selezionato
		    String progettoSelezionato = (String) ComboProgetti.getSelectedItem();
		    
		    if (progettoSelezionato != null && !progettoSelezionato.equals("--seleziona--")) {
		        
		        
		        String lottoEPosizione = controller.getLottoEPosizioneByProgetto(progettoSelezionato,coltivatore);
		        
		        if (lottoEPosizione != null && !lottoEPosizione.isEmpty()) {
		            //  ESTRAI LOTTO E POSIZIONE SEPARATAMENTE
		            String[] parti = lottoEPosizione.split(", ");
		            if (parti.length >= 2) {
		                String lotto = parti[0].replace("Lotto: ", "Lotto ");
		                String posizione = parti[1].replace("Posizione: ", "");
		                
		                lottovisualizza.setText(lotto);       
		                FieldPosizione.setText(posizione);   
		            }
		        } else {
		            lottovisualizza.setText("");
		            FieldPosizione.setText("");
		        }
		    } else {
		        lottovisualizza.setText("");
		        FieldPosizione.setText("");
		    }
		}
	}