package damas.game.animacao;

/*
 * DTPeca.java is used by the 1.4 DragPictureDemo.java example.
 */
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DragSourceEvent;
import java.awt.dnd.DragSourceListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import javax.swing.JComponent;
import javax.swing.TransferHandler;

// A subclass of QuadradoTabuleiro that supports Data Transfer.
class AnimacaoPeca extends AnimacaoCasa implements MouseMotionListener,
		DragSourceListener, DragGestureListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3632107317997767452L;
	private MouseEvent firstMouseEvent = null;

	public AnimacaoPeca() {
		addMouseMotionListener(this);
	}

	public void mousePressed(MouseEvent e) {
		if (image == null)
			return;

		firstMouseEvent = e;
		e.consume();
	}

	public void mouseDragged(MouseEvent e) {
		if (image == null)
			return;

		if (firstMouseEvent != null) {
			e.consume();

			int action = TransferHandler.MOVE;

			int dx = Math.abs(e.getX() - firstMouseEvent.getX());
			int dy = Math.abs(e.getY() - firstMouseEvent.getY());
			// Arbitrarily define a 5-pixel shift as the
			// official beginning of a drag.
			if (dx > 5 || dy > 5) {
				// This is a drag, not a click.
				JComponent c = (JComponent) e.getSource();
				TransferHandler handler = c.getTransferHandler();
				// Tell the transfer handler to initiate the drag.
				if (handler != null)
					handler.exportAsDrag(c, firstMouseEvent, action);
				firstMouseEvent = null;
			}
		}
	}

	public void mouseReleased(MouseEvent e) {
		firstMouseEvent = null;
	}

	public void mouseMoved(MouseEvent e) {
	}

	@Override 
	public void dragDropEnd(DragSourceDropEvent dsde) {
		// TODO Auto-generated method stub

	}

	@Override
	public void dragEnter(DragSourceDragEvent dsde) {
		// TODO Auto-generated method stub

	}

	@Override
	public void dragExit(DragSourceEvent dse) {
		// TODO Auto-generated method stub

	}

	@Override
	public void dragOver(DragSourceDragEvent dsde) {
		// TODO Auto-generated method stub

	}

	@Override
	public void dropActionChanged(DragSourceDragEvent dsde) {
		// TODO Auto-generated method stub

	}

	@Override
	public void dragGestureRecognized(DragGestureEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}
