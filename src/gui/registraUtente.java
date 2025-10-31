package gui;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import net.miginfocom.swing.MigLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JButton;
import javax.swing.JComboBox;
import controller.ControllerReg;
import controller.ControllerProprietario;


public class registraUtente extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField FieldUsername;
	private JPasswordField FieldPassword;
	
	
	@SuppressWarnings("unused")
	private JButton buttonRegistra;
	private JPasswordField passwordField;
	private JLabel lblConfermaPassword;
	private JComboBox<String> comboBox; 
	private JLabel lblRuolo;
	private JButton reg;
	private JLabel CF;
	private JTextField CodFfield;
	private JTextField name;
	private JTextField surname;
	private JLabel cognomelbl;
	private JLabel lblNome;
	private JButton btnback;
	private JComboBox<String> ComboProprietari;
	private JLabel lblProprietari;

	@SuppressWarnings("unused")
	public registraUtente() {
		setTitle("Login Schede");
		 setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		    setBounds(100, 100, 842, 577);
		    
		    URL imageUrl = getClass().getResource("/img/sfondo.PNG");
		    contentPane = new BackgroundPanel(imageUrl);
		    contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		    setContentPane(contentPane);


		    contentPane.setLayout(new MigLayout("", "[grow][grow][grow][grow][grow][grow][grow][grow][grow][grow][grow][grow][grow][grow][grow]", 
		    									"[grow][grow][grow][grow][grow][][grow][grow][grow][grow][grow][][grow][grow][grow][grow]"));
		    
		    ImageIcon originalIcon = new ImageIcon(getClass().getResource("/img/logo.png"));
		    Image scaledImage = originalIcon.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
		    
		    lblNome = new JLabel("nome");
		    contentPane.add(lblNome, "cell 6 2,alignx center");
		    
		    name = new JTextField();
		    name.setColumns(10);
		    contentPane.add(name, "cell 7 2,growx");
		    
		    //Pulsante freccia indietro
		    btnback = new JButton("◀");
		    btnback.addActionListener(new ActionListener() {
		    	public void actionPerformed(ActionEvent e) {
		    		Login login = new Login();
		    		registraUtente.this.setVisible(false);
		    		login.setVisible(true);}
		    });
		    contentPane.add(btnback, "cell 12 2");
		    
		    cognomelbl = new JLabel("cognome");
		    contentPane.add(cognomelbl, "cell 6 3,alignx center");
		    
		    surname = new JTextField();
		    surname.setColumns(10);
		    contentPane.add(surname, "cell 7 3,growx");
		    
		    lblRuolo = new JLabel("RUOLO");
		    contentPane.add(lblRuolo, "cell 6 4,alignx center");
		    
		    comboBox =  new JComboBox<>(); 
		    contentPane.add(comboBox, "cell 7 4,growx");
		    
		    comboBox.setModel(new DefaultComboBoxModel<>(
		    	    new String[] { "-- Seleziona ruolo --",
		    	    				"Proprietario", 
		    	    				"Coltivatore" }
		    	));
		    ComboProprietari = new JComboBox<String>();
		    contentPane.add(ComboProprietari, "cell 7 5,growx");
		    
		    ComboProprietari.setEnabled(false);
		    ControllerReg controller = new ControllerReg();
		    
		    
		    comboBox.addActionListener(new ActionListener() { 	// Popola ComboProprietari usando il controller
		        public void actionPerformed(ActionEvent e) {
		        	String ruoloSelezionato = (String) comboBox.getSelectedItem();
		            if ("Coltivatore".equals(ruoloSelezionato)) {
		                ComboProprietari.setEnabled(true); // Abilita la ComboProprietari
		                ArrayList<String> proprietari = controller.popolaComboProprietari(); // Cattura il risultato
		                ComboProprietari.removeAllItems(); // Pulisci la combo
		                ComboProprietari.addItem("--Seleziona--");
		                for (String username : proprietari) {
		                    ComboProprietari.addItem(username); // Aggiungi ogni username
		                }
		            }
		            else {
		                ComboProprietari.setEnabled(false); // Disabilita se non è Coltivatore
		                ComboProprietari.removeAllItems(); 
		            }
		        }
		    });
		    lblProprietari = new JLabel("collaborazione con proprietario");
		    contentPane.add(lblProprietari, "cell 6 5,alignx center");
		    
		    CF = new JLabel("Codice Fiscale");
		    contentPane.add(CF, "cell 6 6,alignx center");
		    
		    CodFfield = new JTextField();
		    CodFfield.setColumns(10);
		    contentPane.add(CodFfield, "cell 7 6,growx");
		    
		    JLabel LabelUsername = new JLabel("Username");
		    contentPane.add(LabelUsername, "cell 6 7,alignx center,aligny center");
		    
		    FieldUsername = new JTextField();
		    contentPane.add(FieldUsername, "cell 7 7,growx");
		    FieldUsername.setColumns(10);
		    
		    JLabel LabelPassword = new JLabel("Password");
		    contentPane.add(LabelPassword, "cell 6 8,alignx center,aligny center");
		    
		    FieldPassword = new JPasswordField();
		    contentPane.add(FieldPassword, "cell 7 8,growx");
		    
		    lblConfermaPassword = new JLabel("conferma Password");
		    contentPane.add(lblConfermaPassword, "cell 6 9,alignx trailing");
		    
		    passwordField = new JPasswordField();
		    contentPane.add(passwordField, "cell 7 9,growx");
		    
		    reg = new JButton("salva profilo");
		    contentPane.add(reg, "cell 7 11,alignx center");	    
		    
		    reg.addActionListener(new ActionListener() { 	//registrazione utente
		        public void actionPerformed(ActionEvent e) {
		            Login login = new Login();
		            String nome = name.getText();
		            String cognome = surname.getText();
		            String RUOLO = (String) comboBox.getSelectedItem();
		            String cf = CodFfield.getText();
		            String user = FieldUsername.getText();
		            String pass = new String(FieldPassword.getPassword());
		            String confermaPass = new String(passwordField.getPassword());
		            String usernameProprietario = (String)ComboProprietari.getSelectedItem();
		            boolean[] value = new boolean[4];
		            
		            // ---CONTROLLI SUI FIELDS---
		            if ("Coltivatore".equals(RUOLO) && "--Seleziona--".equals(ComboProprietari.getSelectedItem())) {
		                JOptionPane.showMessageDialog(registraUtente.this, 
		                							  "Selezionare il proprietario con cui si vuole collaborare", "Errore", JOptionPane.ERROR_MESSAGE);
		                return;
		            }
		            
		            if (!pass.equals(confermaPass)) {
		                JOptionPane.showMessageDialog(registraUtente.this, 
		                							  "Le password non corrispondono", "Errore", JOptionPane.ERROR_MESSAGE);
		            } else if (nome.equals(cognome)) {
		                JOptionPane.showMessageDialog(registraUtente.this, 
		                							  "Nome e cognome devono essere diversi", "Errore", JOptionPane.ERROR_MESSAGE);
		            } else if (nome.isEmpty() || cognome.isEmpty() || cf.isEmpty() || user.isEmpty() || pass.isEmpty() || confermaPass.isEmpty() || RUOLO.equals("-- Seleziona ruolo --")) {
		                JOptionPane.showMessageDialog(registraUtente.this, "COMPILA TUTTI I CAMPI!!!", "Errore", JOptionPane.ERROR_MESSAGE);
		            } else if (pass.length() > 8) {
		                JOptionPane.showMessageDialog(contentPane, 
		                							  "La password deve essere lunga al massimo 8 caratteri.", "Errore", JOptionPane.ERROR_MESSAGE);
		            }else if (cf.length() >16) {
		                JOptionPane.showMessageDialog(registraUtente.this, 
		                                                  "Il codice fiscale deve essere al massimo 16 caratteri alfanumerici", 
		                                                  "Errore", JOptionPane.ERROR_MESSAGE);
		             
		                
		            } else {
		            	ControllerProprietario controllerP = new ControllerProprietario();//associa il primo lotto libero al proprietario registrato
		                ControllerReg controller = new ControllerReg();
		                
		               if (RUOLO.equals("Coltivatore")) {
		            	   value = controller.registra(nome, cognome, user, pass, cf, RUOLO.toString(), usernameProprietario);
		            	   
		               }else if (RUOLO.equals("Proprietario")) {
		                value = controller.registra(nome, cognome, user, pass, cf, RUOLO.toString(), usernameProprietario);
		                controllerP.aggiungiL(cf);
		                
		                }
		            
		            // Messaggi di avviso stato registrazione
		            try {
		                if (value[0] == false && value[1] == false && value[2] == false && value[3] == true) {
		                    JOptionPane.showMessageDialog(registraUtente.this, 
		                    							  "USERNAME ESISTENTE ", "Errore", JOptionPane.ERROR_MESSAGE);
		                } else if (value[0] == true && value[2] == true) {
		                    JOptionPane.showMessageDialog(registraUtente.this, 
		                    							  "Registrazione proprietario avvenuta con successo", "ESITO POSITIVO", JOptionPane.INFORMATION_MESSAGE);
		                    dispose();
		                    registraUtente.this.setVisible(false);
		                    login.setVisible(true);
		                } else if (value[1] == true && value[2] == true) {
		                    JOptionPane.showMessageDialog(registraUtente.this, 
		                    							"Registrazione coltivatore avvenuta con successo", "ESITO POSITIVO", JOptionPane.INFORMATION_MESSAGE);
		                    dispose();
		                    registraUtente.this.setVisible(false);
		                    login.setVisible(true);
		                    
		                } else if (value[2] == false) {
		                    JOptionPane.showMessageDialog(registraUtente.this, 
		                    							  "Registrazione non riuscita", "Errore", JOptionPane.ERROR_MESSAGE);
		                }
		            } catch (Exception ex) {
		                ex.printStackTrace();		            		        
		            }
		       }//chisura else
		    }
		    });
		    }
		    } 
	
	
	
