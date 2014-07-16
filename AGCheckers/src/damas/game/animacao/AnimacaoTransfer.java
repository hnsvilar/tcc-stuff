package damas.game.animacao;

/*
 * PecaTransferHandler.java is used by the 1.4
 * DragPictureDemo.java example.
 */

import java.awt.Image;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

import javax.swing.JComponent;
import javax.swing.TransferHandler;

public abstract class AnimacaoTransfer extends TransferHandler {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1749818721562404555L;
	DataFlavor pictureFlavor = DataFlavor.imageFlavor;
	AnimacaoPeca sourcePic;
	AnimacaoPeca destPic;
	boolean shouldRemove;

	public boolean importData(JComponent c, Transferable t) {
		
		Image image;
		if (canImport(c, t.getTransferDataFlavors())) {
			destPic = (AnimacaoPeca) c;
			
			// Don't drop on myself.
			int de = Integer.valueOf(sourcePic.getName()).intValue();
			int para = Integer.valueOf(destPic.getName()).intValue();

			if (!this.validaJogadaExterna(de, para)) {
				shouldRemove = false;
				return true;
			}		
			if (!destPic.isEnabled()){	
				shouldRemove = false;
				this.alertaJogadaInvalida();
				return true;
			}
			
			try {
				image = (Image) t.getTransferData(pictureFlavor);
				// Set the component to the new picture.
				destPic.setImage(image);
				this.triggerUpdate(de, para);
				return true;
			} catch (UnsupportedFlavorException ufe) {
				System.out.println("importData: unsupported data flavor");
			} catch (IOException ioe) {
				System.out.println("importData: I/O exception");
			}
		}
		return false;
	}

	protected Transferable createTransferable(JComponent c) {
		sourcePic = (AnimacaoPeca) c;
		shouldRemove = true;
		return new PictureTransferable(sourcePic);
	}

	public int getSourceActions(JComponent c) {
		return MOVE;
	}

	protected void exportDone(JComponent c, Transferable data, int action) {
		sourcePic = null;		
	}

	public boolean canImport(JComponent c, DataFlavor[] flavors) {
		for (int i = 0; i < flavors.length; i++) {
			if (pictureFlavor.equals(flavors[i])) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Valida o movimento da interface grafica de acordo com as regras
	 * de movimentacao permitidas para as pecas.
	 * */
	public abstract boolean validaJogadaExterna(int de, int para);
	
	/**
	 * Repassa o movimento da peca na interface grafica para o tabuleiro
	 * (populacao)
	 * */
	public abstract void triggerUpdate(int de, int para);
	
	/**
	 * Popula visualmente a interface grafica de acordo com a populacao
	 * do tabuleiro.
	 * */
	public abstract void showMundo();
	
	public abstract void alertaJogadaInvalida();

	class PictureTransferable implements Transferable {
		private Image image;

		PictureTransferable(AnimacaoPeca destPic) {
			image = destPic.image;
		}

		public Object getTransferData(DataFlavor flavor)
				throws UnsupportedFlavorException {
			if (!isDataFlavorSupported(flavor)) {
				throw new UnsupportedFlavorException(flavor);
			}
			return image;
		}

		public DataFlavor[] getTransferDataFlavors() {
			return new DataFlavor[] { pictureFlavor };
		}

		public boolean isDataFlavorSupported(DataFlavor flavor) {
			return pictureFlavor.equals(flavor);
		}
	}
	
	
}
