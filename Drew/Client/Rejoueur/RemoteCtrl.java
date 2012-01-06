/*  
 * The Drew software is a CMI (Computer Mediated Interaction) set of tools that
 * combines synchronous exchanges activities with browser-driven web page
 * consultation.
 * Copyright (C) 2003  The Drew Team
 * 
 * The Drew software is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * The Drew software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

/*
 * File: RemoteCtrl.java
 * Author: Ph. Jaillon
 * Description: Description of the remote control command used in the replayer
 *
 * $Id: RemoteCtrl.java,v 1.2 2003/11/24 10:54:25 serpaggi Exp $
 */

package Drew.Client.Rejoueur;

import java.awt.*;

class RemoteCtrl extends Panel {

/** Bouton de controle pour joueur en continu la session demandee */
private Button Bplay;
/** Bouton de controle pour joueur la session demandee en pas a pas */
private Button Bstep;
/** Bouton de controle pour arreter la lecture en continue de la session */
private Button Bstop;
/** Bouton de controle pour revenir au debut de la session a rejouer */
private Button Brewd;
/** Bouton de controle pour lancer la lecture en avance rapide */
private Button Bffwd;

private Rejoueur ctrl = null;

	RemoteCtrl ( Rejoueur ctrl ) {
	Drew.Util.Locale comment  = ctrl.Comment();
	this.ctrl = ctrl;

                Bplay = new Button(comment.format("ButtonPLAY"));
                Bstep = new Button(comment.format("ButtonSTEP"));
                Bstop = new Button(comment.format("ButtonSTOP"));
                Brewd = new Button(comment.format("ButtonRWD"));
                Bffwd = new Button("FFWD");

                // Boutons RWD STOP PLAY STEP FFWD
                setLayout(new GridLayout()) ;
                add(Brewd);
                add(Bstop);
                add(Bplay);
                add(Bstep);
                add(Bffwd);

                // Initialisation des objets graphiques
                Bplay.addActionListener(ctrl);
                Bplay.setActionCommand("PLAY");
                Bstop.addActionListener(ctrl);
                Bstop.setActionCommand("STOP");
                Brewd.addActionListener(ctrl);
                Brewd.setActionCommand("RWD");
                Bstep.addActionListener(ctrl);
                Bstep.setActionCommand("STEP");
                Bffwd.addActionListener(ctrl);
                Bffwd.setActionCommand("FFWD");
	}

	RemoteCtrl Clone() {
		return new RemoteCtrl( this.ctrl );
	}
}


