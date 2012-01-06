package Drew.Client.TokenTextBoard;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPanel;
import javax.swing.event.DocumentEvent;

import Drew.Client.TextBoard.*;

public class TokenTextBoard extends TextBoard implements MouseListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2219622548664154384L;
	
	static final String CODE  = "tokentextboard";
	
	public String getCode() {
		return CODE;
	}
	
	public void textUpdate(DocumentEvent e) {
		super.textUpdate(e);
		if (token!=null && !isReplayer()) {
			token.requestKeepTokenAlive();
		}
	}
	
	public void init() {
		super.init();
		JPanel p = new JPanel();
		setupTokenRing(false,true);
		add(token.getGUIControl(),BorderLayout.SOUTH);
		if (!isReplayer()) {
			E.addMouseListener(this);
		}
	}

	
	public void setTokenIsFree() {
		E.setEditable(false);
		E.setBackground(new Color(0.9f,1f,0.8f));
		
	}

	public void setTokenIsTaken() {
		E.setEditable(false);
		E.setBackground(new Color(1f,0.9f,0.8f));
	}

	public void setTokenIsYours() {
		E.setEditable(true);
		E.setBackground(new Color(0.8f,1f,0.8f));
	}

	public void mouseClicked(MouseEvent arg0) {
		if (!E.isEditable()) {
			token.requestToken();
		} else {
			token.requestKeepTokenAlive();
		}
	}

	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	

}
