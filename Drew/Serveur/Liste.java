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
 * File: 
 * Author: Ph.jaillon
 * Description:
 *
 * $Id: Liste.java,v 1.5 2003/11/24 10:54:29 serpaggi Exp $
 */


package Drew.Serveur;

import java.util.NoSuchElementException;

/**
* Another double linked list implementation
*/
class Liste
{
	class Enumeration implements java.util.Enumeration {
		Cellule q, curr;

		Enumeration(Liste l) {
			curr = q = l.queue;
		}

		public boolean hasMoreElements() {
			if( q == null ) return false;
			return true;
		}

		public Object nextElement() throws NoSuchElementException {

			if( q == null ) throw new NoSuchElementException();

			curr = curr.suivant;
			if( curr == q ) q = null;
			return curr.contenu;
		}
	}

	private class Cellule {
		/**
		* content for the cell
		*/
		Object contenu;

		/**
		* next cell in the list
		*/
		Cellule suivant;

		/**
		* previous cell in the list
		*/
		Cellule precedent;

		/**
		* Cellule Constructor
		* @param contenu content for the cell
		*/
		Cellule(Object contenu) {
			this.contenu = contenu;
			suivant = precedent = null;
		}
	}

	/**
	* maillon de la liste
	*/
	private Cellule queue =null;

	/**
	* Constructeur : initialisation de la Liste
	*/
	Liste() {
	}

	/**
	* give a enumeration object to user
	*/
	public Enumeration elements() {
		return new Enumeration( this );
	}

    	/**
	* Add a cell to the end of list
	*/
	public synchronized void append(Object o) {
		insert( o );
		queue = queue.suivant;
	}

	public synchronized void insert(Object o) {
        	if(queue == null){
			queue = new Cellule( o );
			queue.precedent = queue.suivant = queue;
		}
		else {
			Cellule c = new Cellule(o);
			c.suivant = queue.suivant;
			c.precedent = queue;
			queue.suivant.precedent = c;
        		queue.suivant = c;
		}
    	}

	/**
	* Supression d'un maillon
	* @param cell maillon recherche
	* @return retourne l'objet supprime
	*/
	private Object remove(Cellule cell) {
		Object objet;

		if( queue == null ) return null;

		// cell is at the end of the list
		if(cell == queue) queue = queue.precedent;	

		// list with just one cell
		if( cell == queue ) queue = null;
		else {
			cell.suivant.precedent = cell.precedent;
			cell.precedent.suivant = cell.suivant;
		}
		return cell.contenu;
	}

	/**
	* look for the cell with o as content
	* @param o object to find
	* @return the cell (or null)
	*/
	private Cellule cherche(Object o) {
		Cellule q, curr;
		q = curr = queue;
		while( q != null) {
			curr = curr.suivant; if( curr == q ) q = null;
			if( curr.contenu == o ) return curr;
		}
		return null;
	}
	
	/**
	* Remove object o from the list. 
	* @param o object to remove
	* @return o or null if not in the list
	*/
	public synchronized Object remove( Object o ) {
		Cellule cell = cherche( o );
		if( cell != null ) {
			remove( cell );
		}
		return null;
	}

	/**
	* Test if the list is empty
	* @return true if list empty, false otherwise
	*/
	public boolean vide() {
		if(queue == null) return true;
		return false;
	}

	void print() {
		for (Enumeration e = elements() ; e.hasMoreElements() ;) {
			System.out.print( e.nextElement() + " " );
		}
		System.out.println();
	}

/* test method
//javac -g Drew/Serveur/Liste.java && java Drew.Serveur.Liste

	public static void main( String[] arg ) {
		Liste l = new Liste();

		String a = "aaa";
		String b = "bbb";
		String c = "ccc";

		l.append( a );
		l.append( b );
		l.append( c );

		l.print();

		l.remove( c ); l.print();
		l.remove( b ); l.print();
		l.remove( a ); l.print();
	} 
*/
}
