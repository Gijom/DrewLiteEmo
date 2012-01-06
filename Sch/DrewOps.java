/*  
 * The Drew software is a CMI (Computer Mediated Interaction) set of tools that
 * combines synchronous exchanges activities with browser-driven web page
 * consultation.
 * Copyright (C) 2005  The Drew Team
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
 * File: DrewOps.java
 * Author: Eric Sigoillot
 * Description:
 *
 * $Id: DrewOps.java,v 1.1 2006/04/28 08:02:51 girardot Exp $
 */

package Sch;

import Drew.Client.MiSc.MiSc;
import Drew.Util.XMLmp.*;

public class DrewOps extends SchPrimitive {

	private static DrewOps proto;
	
	public static final int OPCATCH = 0;
	public static final int OPXML = 1;
	public static final int OPATTR = 2;
	public static final int OPCONTENTS = 3;
	public static final int OPEVENT = 4;
	
	public static final int NBOP = OPEVENT+1;
	
	public static void dcl()
	{
		proto = new DrewOps();

		proto.fnames = new String[NBOP];

		proto.fnames[OPCATCH] = "drew.catchevents";
		proto.fnames[OPXML] = "drew.xml";
		proto.fnames[OPATTR] = "drew.attr";
		proto.fnames[OPCONTENTS] = "drew.contents";
		proto.fnames[OPEVENT] = "drew.event";

		Environment.dcl(proto);
	}
	
	private void showMessage(String msg)
	throws SchRunTimeException {
		int port = CurOutP;
		
		if ((Gdm.celltyp[port] != TPORT) || 
			((Gdm.cellcar[port] & PSTOUT) != PSTOUT))
			throw new SchRunTimeException(ERRDOM);
		
		SchPrinter sp = (SchPrinter)Gdm.cellobj[port];
		sp.display(Gdm.newstring("; " + msg));
	}
	
	public void eval(int op, int count, Evaluator evl)
	throws SchRunTimeException
	{
		switch (op)
		{
		case OPCATCH:
			{
				int cell;
			
				if (count != 1)
					throw new SchRunTimeException(ERR1AR);
				
				cell = evl.pop();
				MiSc.catchevents(cell == CTRUE);
				
				
				if (cell == CFALSE)
					showMessage("Drew event catching disabled");
				else
					showMessage("Drew event catching enabled");
			}
			break;
			
		case OPXML:
			{
				int cell;
				XMLTree tree = null;
				String tag;
				Pair attr = Pair.nil;
				Pair contents = Pair.nil;
			
				if ((count < 1) || (count > 3))
					throw new SchRunTimeException(ERRWAC);
				
				/** Récupération du tag de l'évènement XML */
				cell = evl.pop();
				if (Gdm.celltyp[cell] != TSTR)
					throw new SchRunTimeException(ERRTYE);
				
				tag = (String)Gdm.cellobj[cell];
				
				if (count > 1) {
					/**
					* S'il y a trois arguments, alors le deuxième contient
					* les attributs de l'évènement XML
					*/
					if (count > 2) {
						cell = evl.pop();
						if (!(Gdm.cellobj[cell] instanceof Pair))
							throw new SchRunTimeException(ERRTYE);
						
						attr = (Pair)Gdm.cellobj[cell];
					}
					
					/** Le troisième paramètre est le contenu de l'évènement */
					cell = evl.pop();
					if (!(Gdm.cellobj[cell] instanceof Pair))
						throw new SchRunTimeException(ERRTYE);
								
					contents = (Pair)Gdm.cellobj[cell];
				}
				
				/** Création de l'évènement */
				tree = new XMLTree(tag, attr, contents);
				
				evl.push(Gdm.newcons(tree));					
			}
			break;
			
		case OPATTR:
			{
				int cell;
				int cellcar, cellcdr;
				XMLTree tree = null;
				Pair attr = Pair.nil;
				
				while (count > 0) {
					cell = evl.pop();
					
					if (Gdm.celltyp[cell] != TPAIR)
						throw new SchRunTimeException(ERRCXP);
					cellcar = Gdm.cellcar[cell];
					cellcdr = Gdm.cellcar[Gdm.cellcdr[cell]];
					
					if (Gdm.cellobj[cellcar] instanceof String) {
						attr = new Pair(new Pair((String)Gdm.cellobj[cellcar], Gdm.cellobj[cellcdr]), attr);
					}
					else
						throw new SchRunTimeException(ERRTYE);
					
					count--;
				}
				
				evl.push(Gdm.newcons(attr));
			}
			break;
			
		case OPCONTENTS:
			{
				int cell;
				Pair contents = Pair.nil;
										
				while (count > 0) {
					cell = evl.pop();
					contents = new Pair(Gdm.cellobj[cell], contents);					
					
					count--;
				}
				
				evl.push(Gdm.newcons(contents));
			}
			break;
			
		case OPEVENT:
			{
				int cell;
				String code;
				XMLTree tree;
				
				if (count != 2)
					throw new SchRunTimeException(ERR2AR);
					
				/** Récupération du code */
				cell = evl.pop();
				if (Gdm.celltyp[cell] != TSTR)
					throw new SchRunTimeException(ERRTYE);
				code = (String)Gdm.cellobj[cell];
				
				/** Récupération de l'évènement XML */
				cell = evl.pop();
				if (!(Gdm.cellobj[cell] instanceof XMLTree))
					throw new SchRunTimeException(ERRTYE);
				
				tree = (XMLTree)Gdm.cellobj[cell];
				
				MiSc.sendevent(new XMLTree(code, tree));
														
				evl.push(cell);
				// showMessage(String.format("Event sent to the %s applet", code));
			}
			break;
			
		default:
		   throw new SchRunTimeException(ERRUSP);
		}
	}
}
