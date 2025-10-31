package gui;

import javax.swing.*;
import java.net.URL;

import java.awt.*;

@SuppressWarnings("serial")
public class BackgroundPanel extends JPanel {		//imposto l'immagine di background
	private Image backgroundImage;

    public BackgroundPanel(URL imageUrl) {
        if (imageUrl != null) {
            backgroundImage = new ImageIcon(imageUrl).getImage();
        } else {
            System.err.println("Immagine non trovata!");
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}
