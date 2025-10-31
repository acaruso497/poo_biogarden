package gui;

import java.awt.Color;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import controller.CreaProgettoController;
import dao.DAO;
import net.miginfocom.swing.MigLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;


public class Attivita extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField FieldTitolo;
	private JTextField FieldDataIP;
	private JTextField FieldDataFP;
	private JTextField FieldLotto;
	private JTextField FieldDataIA;
	private JTextField FieldDataFA;

	private CreaProgettoController creaProgettoController; 
	private JTextField FieldTipoSemina;
	private JTextField FieldTipologia;
	private JTextField FieldProfondita;
	private JTextField FieldStimaRaccolto;
	private JButton ButtonHomePage;
	private HomePageProprietario home; 
	
	public Attivita(String titolo, String lotto, String stimaRaccolto, String tipologiaColtura, String dataInizioP, String dataFineP, String descrizione, Integer idProgetto) {
		home = new HomePageProprietario();
		setTitle("Attività");
	    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    setBounds(100, 100, 843, 564);
	    
	    URL imageUrl = getClass().getResource("/img/sfondoschede.PNG");
	    contentPane = new BackgroundPanel(imageUrl);
	    contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
	    setContentPane(contentPane);


	    contentPane.setLayout(new MigLayout("", "[grow][grow][grow][grow][grow][grow][grow][grow][grow][grow][][grow][grow][grow][grow]", 
	    										"[grow][grow][grow][][grow][][grow][grow][][][grow][][][][grow][grow][][grow][grow][grow][grow][grow][grow][grow]"));
	    
	    
	    JLabel LabelTitolo = new JLabel("Titolo del progetto");
	    contentPane.add(LabelTitolo, "flowx,cell 0 0");
	    
	    JLabel LabelLotto = new JLabel("Lotto Numero");
	    contentPane.add(LabelLotto, "flowx,cell 2 0");
	    
	    JLabel LabelDataIP = new JLabel("Data Inizio");
	    contentPane.add(LabelDataIP, "flowx,cell 0 1,alignx right");
	    
	    JLabel LabelDataFP = new JLabel("Data Fine");
	    contentPane.add(LabelDataFP, "flowx,cell 2 1,alignx right");
	    
	    FieldTitolo = new JTextField();
	    contentPane.add(FieldTitolo, "cell 0 0");
	    FieldTitolo.setColumns(10);
	    FieldTitolo.setEditable(false); 				//blocca il textfield per impedirne la modifica
	    
	    FieldDataIP = new JTextField();
	    contentPane.add(FieldDataIP, "cell 0 1,alignx right");
	    FieldDataIP.setColumns(10);
	    FieldDataIP.setEditable(false); 
	    
	    FieldDataFP = new JTextField();
	    contentPane.add(FieldDataFP, "cell 2 1,alignx right");
	    FieldDataFP.setColumns(10);
	    FieldDataFP.setEditable(false); 
	    
	    FieldLotto = new JTextField();
	    contentPane.add(FieldLotto, "cell 2 0");
	    FieldLotto.setColumns(10);
	    FieldLotto.setEditable(false); 
	    
	    JLabel LabelAttivita = new JLabel("Attività");
	    contentPane.add(LabelAttivita, "flowx,cell 8 1,alignx trailing,aligny bottom");
	    
	    
	    JComboBox<String> ComboAttivita = new JComboBox<>(); 			//Tipo di attività selezionabile
		ComboAttivita.setModel(new DefaultComboBoxModel<>(
	    	    new String[] { "-- Seleziona --",
	    	    				"Semina", 
	    	    				"Irrigazione",
	    	    				"Raccolta" }));
	    contentPane.add(ComboAttivita, "cell 9 1 5 1,growx");
	    
	    JLabel LabelDataIA = new JLabel("Data Inizio");
	    contentPane.add(LabelDataIA, "cell 8 2,alignx trailing");
	    
	    FieldDataIA = new JTextField();
	    contentPane.add(FieldDataIA, "cell 9 2,growx");
	    FieldDataIA.setColumns(10);
	    
	    JLabel date = new JLabel("GG/MM/AAAA");
	    contentPane.add(date, "cell 10 2");
	    
	    JLabel LabelDescrizione = new JLabel("Descrizione");
	    contentPane.add(LabelDescrizione, "cell 0 3");
	    
	    JLabel LavelDataFA = new JLabel("Data Fine");
	    contentPane.add(LavelDataFA, "cell 8 3,alignx trailing");
	    
	    FieldDataFA = new JTextField();
	    contentPane.add(FieldDataFA, "cell 9 3,growx");
	    FieldDataFA.setColumns(10);
	    
	    JLabel date_1 = new JLabel("GG/MM/AAAA");
	    contentPane.add(date_1, "cell 10 3");
	    
	    JTextArea TextDescrizione = new JTextArea();
	    contentPane.add(TextDescrizione, "cell 0 4 3 10,grow");
	    TextDescrizione.setEditable(false); 
	    
	    JLabel LabelTipologiaI = new JLabel("Tipologia Irrigazione");
	    contentPane.add(LabelTipologiaI, "cell 8 5,alignx trailing");
	    
	    JComboBox<String> ComboTipoIrr = new JComboBox<String>(); 				//Tipo di irrigazione selezionabile
	    contentPane.add(ComboTipoIrr, "cell 9 5,growx");
	    ComboTipoIrr.setModel(new DefaultComboBoxModel<>(
	    	    new String[] { "-- Seleziona --",
	    	    				"a goccia", 
	    	    				"a pioggia",
	    	    				"per scorrimento" }));
	    ComboTipoIrr.setEnabled(false);
	    
	    JLabel LabelTipoSemina = new JLabel("Tipo Semina");
	    contentPane.add(LabelTipoSemina, "cell 8 6,alignx trailing");
	    
	    FieldTipoSemina = new JTextField();
	    FieldTipoSemina.setColumns(10);
	    contentPane.add(FieldTipoSemina, "cell 9 6,growx");
	    FieldTipoSemina.setEnabled(false); 
	    
	    
	    ComboAttivita.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e) {
	            String selectedAttivita = (String) ComboAttivita.getSelectedItem();
	            
	            // se non viene selezionata l'attività, blocca i field relativi alle attività
	            if (selectedAttivita == null || selectedAttivita.equals("-- Seleziona --")) {
	                ComboTipoIrr.setEnabled(false);
	                FieldTipoSemina.setEnabled(false);
	                FieldTipoSemina.setEditable(false);
	                ComboTipoIrr.setSelectedIndex(0);
	                FieldTipoSemina.setText("");
	            // se viene selezionata l'attività Irrigazione, attiva la combobox relativa al tipo di irrigazione    
	            } else if (selectedAttivita.equals("Irrigazione")) {
	                ComboTipoIrr.setEnabled(true);
	                FieldTipoSemina.setEnabled(false);
	                FieldTipoSemina.setText("");
	             // se viene selezionata l'attività Semina, attiva il textfield relativo al tipo di semina 
	            } else if (selectedAttivita.equals("Semina")) {
	                ComboTipoIrr.setEnabled(false);
	                FieldTipoSemina.setEnabled(true);
	                FieldTipoSemina.setEditable(true);
	                ComboTipoIrr.setSelectedIndex(0);
	             // se viene selezionata l'attività Raccolta, blocca i field relativi alle altre attività
	            } else if (selectedAttivita.equals("Raccolta")) {
	                ComboTipoIrr.setEnabled(false);
	                FieldTipoSemina.setEnabled(false);
	                ComboTipoIrr.setSelectedIndex(0);
	                FieldTipoSemina.setText("");
	            }
	        }
	    });
        
        ButtonHomePage = new JButton("HomePage");
        ButtonHomePage.setEnabled(false); 
        ButtonHomePage.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
                if (creaProgettoController != null) {
                    creaProgettoController.resetContatori(); 			// Reset dei contatori per il nuovo ciclo di attività
                    Attivita.this.setVisible(false);
                    home.setVisible(true);
                    
              }
        	}
        });
        

        JButton ButtonSalva = new JButton("Salva");
        ButtonSalva.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		String attivita = (String) ComboAttivita.getSelectedItem();
        		String dataInizioA = FieldDataIA.getText();
				String dataFineA = FieldDataFA.getText();
				String tipoIrrigazione = "";
			    String tipoSemina = "";
			    
			    // ---CONTROLLI SUI FIELDS---
			    
			    if (attivita.equals("Irrigazione")) {
			        tipoIrrigazione = (String) ComboTipoIrr.getSelectedItem();
			        if (tipoIrrigazione.equals("-- Seleziona --")) {
			            JOptionPane.showMessageDialog(Attivita.this, "Seleziona un tipo di irrigazione valido!", "Errore", JOptionPane.ERROR_MESSAGE);
			            return;
			        }
			    } else if (attivita.equals("Semina")) {
			        tipoSemina = FieldTipoSemina.getText();
			        if (tipoSemina.isEmpty()) {
			            JOptionPane.showMessageDialog(Attivita.this, "Inserisci un tipo di semina!", "Errore", JOptionPane.ERROR_MESSAGE);
			            return;
			        }
			    }
				
				if (dataInizioA.isEmpty() || dataFineA.isEmpty()) {
				    JOptionPane.showMessageDialog(Attivita.this, "COMPILA TUTTI I CAMPI !!", "Errore", JOptionPane.ERROR_MESSAGE);
				    return; 
				}
				
				if (ComboAttivita.getSelectedItem().equals("-- Seleziona --")) {
				    JOptionPane.showMessageDialog(Attivita.this, "Seleziona un'attività!", "Errore", JOptionPane.ERROR_MESSAGE);
				    return;
				}
				
				
				try { 				// Converte le date dell'attività ed effettua controlli
					
					LocalDate dataInseritaIA = LocalDate.parse(dataInizioA, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
		            if (dataInseritaIA.isBefore(LocalDate.now())) { //data inizio attività < oggi
		                JOptionPane.showMessageDialog(Attivita.this, 
		                							  "La data non può essere minore di oggi!");
		                FieldDataIA.setBackground(Color.RED);
		                return;
		            }
		            
		            LocalDate dataInseritaFA = LocalDate.parse(dataFineA, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
		            if (dataInseritaFA.isBefore(LocalDate.now())) { //data fine attività < oggi
		                JOptionPane.showMessageDialog(Attivita.this, 
		                							  "La data non può essere minore di oggi!");
		                FieldDataFA.setBackground(Color.RED);
		                return;
		            }
		   
		            if (dataInseritaFA.isBefore(dataInseritaIA)) { //data fine attività < data inizio attività
		                JOptionPane.showMessageDialog(Attivita.this, 
		                							  "La data non può essere minore della data di inizio!");
		                FieldDataFA.setBackground(Color.RED);
		                return;
		            }
		            
		            if (dataInseritaIA.isAfter(dataInseritaFA)) { //data inizio attività > data fine attività
		                JOptionPane.showMessageDialog(Attivita.this, 
		                							 "La data non può essere maggiore della data di fine!");
		                FieldDataIA.setBackground(Color.RED);
		                return;
		            }
		            
		            // Converte le date del progetto
                    LocalDate dataInizioProgetto = LocalDate.parse(dataInizioP, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                    if (dataInseritaIA.isBefore(dataInizioProgetto)) { // Controlla che la data inizio attività non sia precedente alla data di inizio del progetto
                        JOptionPane.showMessageDialog(Attivita.this, 
                        					"La data di inizio attività non può essere precedente alla data di inizio del progetto (" 
                        					+ dataInizioP + ")!", "Errore", JOptionPane.ERROR_MESSAGE);
                        FieldDataIA.setBackground(Color.RED);
                        return;
                    }
                    
                    LocalDate dataFineProgetto = LocalDate.parse(dataFineP, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                    if (dataInseritaFA.isAfter(dataFineProgetto)) {  // Controlla che la data fine attività non sia successiva alla data di fine del progetto
                        JOptionPane.showMessageDialog(Attivita.this, 
                        						"La data di fine attività non può essere successiva alla data di fine del progetto (" 
                        						+ dataFineP + ")!", "Errore", JOptionPane.ERROR_MESSAGE);
                        FieldDataFA.setBackground(Color.RED);
                        return;
                    }
                    
                    
					
		        } catch (DateTimeParseException ex) { 		// controlla il formato
		            JOptionPane.showMessageDialog(Attivita.this, "Inserisci una data valida con formato: 'GG/MM/AAAA'");
		            return;
					
				}
				
				LocalDate datalocalIA = LocalDate.parse(dataInizioA, DateTimeFormatter.ofPattern("dd/MM/yyyy")); //converte il textfield della data inizio in tipo data di sql
				Date dataIA = Date.valueOf(datalocalIA);
				
				LocalDate datalocalFA = LocalDate.parse(dataFineA, DateTimeFormatter.ofPattern("dd/MM/yyyy")); //converte il textfield della data fine in tipo data di sql
				Date dataFA = Date.valueOf(datalocalFA);
				

				boolean creaAttivita = creaProgettoController.creaAttivita(attivita, dataIA, dataFA, tipoIrrigazione, tipoSemina, lotto, idProgetto);

				if(creaAttivita==true) { //crea l'attività
				    JOptionPane.showMessageDialog(Attivita.this, "Attività creata con successo!");
				    if (creaProgettoController.puoAvanzare()==true) {
				        ButtonHomePage.setEnabled(true);
				        ButtonSalva.setEnabled(false);
				    } 
				    
				    // pulizia campi
				    ComboAttivita.removeItem(attivita);
				    ComboAttivita.setSelectedIndex(0);
				    FieldDataIA.setText("");
				    FieldDataFA.setText("");
				    FieldDataIA.setBackground(Color.WHITE);
				    FieldDataFA.setBackground(Color.WHITE);
				} else { //se non trova un coltivatore associato al lotto dà errore
				    JOptionPane.showMessageDialog(Attivita.this, 
				        "Devi prima assegnare un coltivatore al lotto!", 
				        "Errore", 
				        JOptionPane.ERROR_MESSAGE);
				}
			     
        	}
        });
        contentPane.add(ButtonSalva, "cell 9 7,alignx center");
        contentPane.add(ButtonHomePage, "cell 9 9,alignx center");
	    
	    
        
        JLabel LabelStimaRaccolto = new JLabel("Stima Raccolto");
        contentPane.add(LabelStimaRaccolto, "cell 0 14");
        
        FieldStimaRaccolto = new JTextField();
        FieldStimaRaccolto.setEditable(false); 
        FieldStimaRaccolto.setColumns(10);
        contentPane.add(FieldStimaRaccolto, "cell 0 15,growx");
        
        JLabel LabelTipologia = new JLabel("Tipologia Coltura");
        contentPane.add(LabelTipologia, "cell 0 16");
        
        FieldTipologia = new JTextField();
        FieldTipologia.setColumns(10);
        contentPane.add(FieldTipologia, "cell 0 17,growx");
        FieldTipologia.setEditable(false); 
        
        JLabel LabelProfondita = new JLabel("Profondità Semina");
        contentPane.add(LabelProfondita, "cell 0 18");
        
        FieldProfondita = new JTextField();
        FieldProfondita.setText("10cm (default)");
        FieldProfondita.setEditable(false); 
        FieldProfondita.setColumns(10);
        contentPane.add(FieldProfondita, "cell 0 19,growx");
        
        //controlla ed imposta tutti field ottenuti tramite variabile locali (provienienti dalla GUI CreaProgetto)
        if (titolo != null && !titolo.isEmpty()) {
            FieldTitolo.setText(titolo);
        }
        
        if (lotto != null && !lotto.isEmpty()) {
            FieldLotto.setText(lotto);
        }
        
        if (stimaRaccolto != null && !stimaRaccolto.isEmpty()) {
        	FieldStimaRaccolto.setText(stimaRaccolto + " kg");
        }
        
        if (tipologiaColtura != null && !tipologiaColtura.isEmpty()) {
        	FieldTipologia.setText(tipologiaColtura);
        }
        
        if (dataInizioP != null && !dataInizioP.isEmpty()) {
            FieldDataIP.setText(dataInizioP);
        }
        
        if (dataFineP != null && !dataFineP.isEmpty()) {
            FieldDataFP.setText(dataFineP);
        }
        
        if (descrizione != null && !descrizione.isEmpty()) {
            TextDescrizione.setText(descrizione);
        }
        
       
        
        DAO dao = new DAO();
	    creaProgettoController = new CreaProgettoController(dao);
	    
        
	}



}