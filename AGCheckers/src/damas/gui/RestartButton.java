package damas.gui;

import java.awt.Image;
import java.awt.Rectangle;

import javax.swing.ImageIcon;
import javax.swing.JButton;

public class RestartButton extends JButton {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4696335217814053277L;

	public RestartButton(String pathOff, String pathOn, Rectangle bounds) {

		Image imgOff = this.getToolkit().getImage(
				this.getClass().getResource(pathOff));
		imgOff = imgOff.getScaledInstance(bounds.width, bounds.height,
				Image.SCALE_SMOOTH);
		ImageIcon iconOff = new ImageIcon(imgOff);

		Image imgOn = this.getToolkit().getImage(
				this.getClass().getResource(pathOn));
		imgOn = imgOn.getScaledInstance(bounds.width, bounds.height,
				Image.SCALE_SMOOTH);
		ImageIcon iconOn = new ImageIcon(imgOn);

		this.setIcon(iconOff);
		this.setFocusable(false);
		this.setContentAreaFilled(false);
		this.setBorder(null);
		this.setBounds(bounds);
		this.setRolloverEnabled(true);
		this.setToolTipText("Novo Jogo");
		this.setRolloverIcon(iconOn);
		this.setPressedIcon(iconOn);
	}
}