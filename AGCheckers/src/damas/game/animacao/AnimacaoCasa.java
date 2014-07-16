package damas.game.animacao;

/*
 * QuadradoTabuleiro.java is used by the 1.4
 * TrackFocusDemo.java and DragPictureDemo.java examples.
 */
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.RenderingHints;
import java.awt.color.ColorSpace;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ColorConvertOp;
import java.awt.image.ImageObserver;

import javax.accessibility.Accessible;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;

class AnimacaoCasa extends JButton implements MouseListener, FocusListener,
		Accessible, ImageObserver {
	/**
	 *
	 */
	private static final long serialVersionUID = -7525175485064687035L;
	public Image image;
	private BufferedImage buffImage;

	public AnimacaoCasa() {

		setFocusable(true);
		addMouseListener(this);
		addFocusListener(this);
	}

	public void setImage(Image image) {
		this.image = getImage(image);
		super.setIcon((image != null) ? new ImageIcon(image) : null);
	}

	public void setGrayScaleImage(Icon icon) {
		if (icon != null)
			this.setGrayScaleImage((ImageIcon) icon);
	}

	public void setGrayScaleImage(ImageIcon icon) {
		if (icon != null)
			this.setGrayScaleImage(icon.getImage());
	}

	public void setGrayScaleImage(Image image) {
		if (image == null)
			return;
		Dimension d = getSize();
		BufferedImage buff = (BufferedImage) createImage(d.width, d.height);
		if (buff != null) {
			Graphics2D g2 = buff.createGraphics();
			g2.setBackground(getBackground());
			g2.setRenderingHint(RenderingHints.KEY_RENDERING,
					RenderingHints.VALUE_RENDER_QUALITY);
			g2.drawImage(image, 0, 0, d.width, d.height, this);

			BufferedImageOp op = new ColorConvertOp(ColorSpace
					.getInstance(ColorSpace.CS_GRAY), null);
			// eh um buffered img
			this.setImage(op.filter(buff, null).getScaledInstance(
					this.getWidth(), this.getHeight(), Image.SCALE_SMOOTH));
			g2.dispose();
		}
	}

	private Image getImage(Image img) {
		try {
			MediaTracker tracker = new MediaTracker(this);
			tracker.addImage(img, 0);
			tracker.waitForID(0);
		} catch (Exception e) {
			e.printStackTrace(System.out);
		}
		return img;
	}

	public void setRolloverImage(Image image) {
		super.setRolloverIcon((image != null) ? new ImageIcon(image) : null);
	}

	public void setPressedImage(Image image) {
		super.setPressedIcon((image != null) ? new ImageIcon(image) : null);
	}

	public void mouseClicked(MouseEvent e) {
		// Since the user clicked on us, let's get focus!
		requestFocusInWindow();
	}

	public void mouseEntered(MouseEvent e) {
		// Since the user clicked on us, let's get focus!
		if (!this.isRolloverEnabled())
			return;
		requestFocusInWindow();
		ImageIcon icon = (ImageIcon) super.getRolloverIcon();
		this.image = getImage(icon.getImage());

	}

	public void mouseExited(MouseEvent e) {
		ImageIcon icon = (ImageIcon) super.getIcon();
		this.image = getImage(icon.getImage());
	}

	public void mousePressed(MouseEvent e) {
		ImageIcon icon = (ImageIcon) super.getRolloverIcon();
		this.image = getImage(icon.getImage());
	}

	public void mouseReleased(MouseEvent e) {
	}

	public void focusGained(FocusEvent e) {
		// Draw the component with a red border
		// indicating that it has focus.
		this.repaint();
	}

	public void focusLost(FocusEvent e) {
		// Draw the component with a black border
		// indicating that it doesn't have focus.
		this.repaint();
	}

	public void drawDemo(int w, int h, Graphics2D g2) {
//		System.out.println(image.getHeight(null)+ " " + image.getWidth(this));
		g2.drawImage(this.image, 0, 0, w, h, this);
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
		Dimension d = getSize();
		Graphics2D g2 = createGraphics2D(d.width, d.height);
		drawDemo(d.width, d.height, g2);
		g2.dispose();
		graphics.drawImage(buffImage, 0, 0, this);
		graphics.setColor(Color.BLACK);
		graphics.drawRect(0, 0, this.getWidth(), this.getHeight());
		graphics.dispose();
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