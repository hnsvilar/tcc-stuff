package damas.gui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

public class ImagePanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8417566261933797981L;

	private Image background;
	
	private BufferedImage buffImage;

	private String localImagem;

	private Rectangle bounds;

	public ImagePanel(String localImagem, Rectangle bounds) {
		this.localImagem = localImagem;
		this.bounds = bounds;
		this.setLayout(null);
		this.setBounds(bounds);
		this.initialize();
	}

	protected void initialize() {
		this.background = this.getToolkit().getImage(
				this.getClass().getResource(localImagem));
		this.setLayout(null);
	}
	
	public void drawDemo(int w, int h, Graphics2D g2) {
		g2.drawImage(this.background, 0, 0, w, h, this);
	}
	
	public Graphics2D createGraphics2D(int w, int h) {
		Graphics2D g2 = null;
		if (buffImage == null || buffImage.getWidth() != w
				|| buffImage.getHeight() != h) {
			buffImage = (BufferedImage) createImage(w, h);
		}
		g2 = buffImage.createGraphics();
		g2.setBackground(getBackground());
		g2.setRenderingHint(RenderingHints.KEY_RENDERING,
				RenderingHints.VALUE_RENDER_QUALITY);
		// g2.clearRect(0, 0, w, h); Limpar fara a img piscar mostrando o
		// background.
		return g2;
	}

	protected void paintComponent(Graphics graphics) {
		Dimension d = bounds.getSize();
		Graphics2D g2 = createGraphics2D(d.width, d.height);
		drawDemo(d.width, d.height, g2);
		g2.dispose();

		Graphics2D g22 = (Graphics2D) graphics.create();
		g22.drawImage(buffImage, 0, 0, this);
		g22.drawRect(0, 0, this.getWidth(), this.getHeight());
		g22.dispose();
	}

	// overrides imageUpdate to control the animated gif's animation
	@Override
	public boolean imageUpdate(Image img, int infoflags, int x, int y,
			int width, int height) {
		if (isShowing() && (infoflags & ALLBITS) != 0)
			repaint();
		if (isShowing() && (infoflags & FRAMEBITS) != 0)
			repaint();

		return isShowing();
	}
}
