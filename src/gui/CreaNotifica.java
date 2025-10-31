package gui;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import java.awt.Color;
import java.awt.Dimension;
import java.net.URL;
import java.sql.Date;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import net.miginfocom.swing.MigLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.JButton;
import javax.swing.plaf.basic.BasicArrowButton;
import controller.ControllerCreaN;
import controller.ControllerLogin;
import java.util.ArrayList;

import javax.swing.JComboBox;

public class CreaNotifica extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField FieldData;
	private JTextField FieldUsernameC;
	private JTextField FieldTitolo;
	private boolean tutti;
	HomePageProprietario home;
	private String usernameP = ControllerLogin.getUsernameGlobale();
	JComboBox<String> ComboColtivatori = new JComboBox<String>();

	public CreaNotifica(HomePageProprietario home) {
			setTitle("Crea Notifica");
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		    setBounds(100, 100, 842, 577);
		    
		    URL imageUrl = getClass().getResource("/img/sfondoschede.PNG");
		    contentPane = new BackgroundPanel(imageUrl);
		    contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		    setContentPane(contentPane);


		    contentPane.setLayout(new MigLayout("", "[grow][grow][grow][grow][grow][grow][grow][grow][grow][grow][grow][grow][grow][grow]", 
		    										"[grow][grow][grow][grow][grow][grow][grow][grow][grow][grow][grow][grow][grow][grow]"));
		    
		    JLabel LabelNotifica = new JLabel("Crea La Tua Notifica!");
		    LabelNotifica.setFont(new Font("Tahoma", Font.BOLD, 17));
		    contentPane.add(LabelNotifica, "cell 0 0");
		    
		    JTextArea TxtDescrizione = new JTextArea();
		    
		    
		    JLabel LabelData = new JLabel("Data");
		    contentPane.add(LabelData, "cell 0 2,alignx trailing");
		    
		    FieldData = new JTextField();
		    contentPane.add(FieldData, "cell 1 2,growx");
		    FieldData.setColumns(10);
		    
		    JLabel LabelUsernameC = new JLabel("Username Coltivatori");
		    contentPane.add(LabelUsernameC, "cell 0 4,alignx trailing");
		    
		    FieldUsernameC = new JTextField();
		    contentPane.add(FieldUsernameC, "cell 1 4,growx");
		    FieldUsernameC.setColumns(10);
		    ComboColtivatori.setEnabled(true);
		    ComboColtivatori.setSelectedIndex(-1);
		    ComboColtivatori.setPreferredSize(new Dimension(150, 20));
		    contentPane.add(ComboColtivatori, "cell 2 4,growx");
		    
		    JCheckBox CheckTuttiColtivatori = new JCheckBox("Tutti i coltivatori");
		    contentPane.add(CheckTuttiColtivatori, "cell 1 5");
		    CheckTuttiColtivatori.setOpaque(false);
		    CheckTuttiColtivatori.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) { //tiene traccia dello stato del pulsante
				if(CheckTuttiColtivatori.isSelected()) {
					FieldUsernameC.setEditable(false);
					FieldUsernameC.setBackground(Color.LIGHT_GRAY);
					FieldUsernameC.setText("");
					tutti = true;
					ComboColtivatori.setEnabled(false);
				}
				else {
					FieldUsernameC.setEditable(true);
					FieldUsernameC.setBackground(Color.WHITE);
					tutti = false;
					ComboColtivatori.setEnabled(false);
				}
				}
		    });
		    
		 // Pulsante freccia indietro
		    BasicArrowButton ButtonIndietro = new BasicArrowButton(BasicArrowButton.WEST);
		    ButtonIndietro.setPreferredSize(new Dimension(40, 40));
		    contentPane.add(ButtonIndietro, "cell 13 0,alignx right,aligny center");
		    ButtonIndietro.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					setVisible(false);
					home.setVisible(true);
					
					//reset dei campi quando viene cliccato il pulsante per andare indietro
					FieldData.setText("");
					FieldUsernameC.setText("");
					FieldTitolo.setText("");
					TxtDescrizione.setText("");
					ComboColtivatori.setEnabled(false);
					CheckTuttiColtivatori.setSelected(false);
					FieldUsernameC.setBackground(Color.WHITE);
					ComboColtivatori.setSelectedIndex(-1);
				}
			});
		    
		    
		    JLabel LabelTitolo = new JLabel("Titolo");
		    contentPane.add(LabelTitolo, "cell 0 7,alignx trailing");
		    
		    FieldTitolo = new JTextField();
		    contentPane.add(FieldTitolo, "cell 1 7,growx");
		    FieldTitolo.setColumns(10);
		    
		    JLabel LabelDescrizione = new JLabel("Descrizione");
		    contentPane.add(LabelDescrizione, "cell 0 9,alignx right");
		    
		    
		    contentPane.add(TxtDescrizione, "cell 1 9 5 4,grow");
		    //wrapping : andare a capo automaticamente e non spezza le parole
		    TxtDescrizione.setLineWrap(true);
		    TxtDescrizione.setWrapStyleWord(true);
		    
		    JButton ButtonInviaN = new JButton("Invia Notifica");
		    contentPane.add(ButtonInviaN, "cell 13 13");
		    ButtonInviaN.addActionListener(new ActionListener() {
		    	public void actionPerformed(ActionEvent e) {
					String DataInserita = FieldData.getText();
					String usernameC = FieldUsernameC.getText().trim();
					String titolo = FieldTitolo.getText();
					String descrizione = TxtDescrizione.getText();
					
					// ---CONTROLLI SUI FIELDS---
					
					if (usernameC.isEmpty() && tutti == false || titolo.isEmpty() || descrizione.isEmpty()) {
					    JOptionPane.showMessageDialog(CreaNotifica.this, "COMPILA TUTTI I CAMPI !!", 
					    							  "Errore", JOptionPane.ERROR_MESSAGE);
					    return; 
					} else if (tutti == false) {
			            ControllerCreaN CreaN = new ControllerCreaN();
			            boolean checkUser = CreaN.controllaUsername(usernameC); //flag per controllare l'esistenza dell'username
			            if (checkUser == false) { //se l'username non è presente nel database, stampa un errore
			                JOptionPane.showMessageDialog(CreaNotifica.this, 
			                							  "L'username non esiste !!", 
			                							   "Errore", JOptionPane.ERROR_MESSAGE);
			                return; 
			            }
			        }
					
					try { 				// Converte le date dell'attività ed effettua controlli
			            LocalDate dataInserita = LocalDate.parse(DataInserita, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
			            if (dataInserita.isBefore(LocalDate.now())) {
			                JOptionPane.showMessageDialog(CreaNotifica.this, 
			                							  "La data non può essere minore di oggi!");
			                FieldData.setBackground(Color.RED);
			                return;
			            }
			        } catch (DateTimeParseException ex) { 			// controlla il formato
			            JOptionPane.showMessageDialog(CreaNotifica.this, 
			            		                      "Inserisci una data valida con formato: 'GG/MM/AAAA'");
			            FieldData.setBackground(Color.RED);
			            return;
			        }
					
					ControllerCreaN CreaN = new ControllerCreaN();
					LocalDate datalocal = LocalDate.parse(DataInserita, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
					Date data = Date.valueOf(datalocal);
					
					if (tutti == false) {
						boolean controlloUsername = CreaN.dividiUsername(usernameP, usernameC, data, titolo, descrizione);
							if(controlloUsername==true) { //se l'username inserito corrisponde ai coltivatori del proprietario invia la notifica
								JOptionPane.showMessageDialog(CreaNotifica.this, 
															  "Notifica inviata con successo!");
							}else {
								JOptionPane.showMessageDialog(CreaNotifica.this, 
												             "Uno o più username non appartengono ai tuoi coltivatori!", 
												             "Errore", 
												             JOptionPane.ERROR_MESSAGE);
								FieldUsernameC.setBackground(Color.RED);
							}
			            } else {
						CreaN.dividiUsernameTutti(ControllerLogin.getUsernameGlobale(), data, titolo, descrizione);
						JOptionPane.showMessageDialog(CreaNotifica.this, "Notifica inviata con successo!");
						
					}
			}    
		    });
		    
		    popolaComboColtivatori();
	}
	
	private void popolaComboColtivatori() { 	//popola la combobox dei coltivatori per verificare i loro username
		ControllerCreaN CreaN = new ControllerCreaN();
        ArrayList<String> coltivatori = CreaN.getColtivatoriByProprietario(usernameP);
        for (String coltivatore : coltivatori) {
            ComboColtivatori.addItem(coltivatore); 
        }
        ComboColtivatori.setSelectedIndex(-1);
    }
	

}
