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
 * File: Dxml.java
 * Author:
 * Description:
 *
 * $Id: Dxml.java,v 1.6 2003/11/24 10:54:30 serpaggi Exp $
 */

/**

   A very simple XML parser in java

   How to compile and use it
	javac -classpath .:/users/chic/jaillon/CVS/scale/Drew/java/ Dxml.java 
	java  -classpath .:/users/chic/jaillon/CVS/scale/Drew/java/ Dxml

 */
// package XMLcheap;

import java.io.*;
import java.util.*;
import Drew.Util.XMLmp.*;

class XMLDate {
	public String toString() {
		return String.valueOf(System.currentTimeMillis());
	}
}

class Dxml {

static XMLParser parser;
static XMLPrinter printer;

static XMLTree res = XMLParser.PERROR;
static XMLTree t1, t2, t3;

  	public static void show(XMLTree res) {
     	//    System.out.println(XMLTree.toString(res));
         	printer.print(res);
         	printer.flush();
  	}

  	public static void main (String argv[]) {
     	String user = "pj";
     	String room = "Hall";

		// time stamper for events
		XMLTree xtime = new XMLTree( "time", new XMLTree( "date", new XMLDate() ) );
		XMLTree xuser = new XMLTree( "user", user );

     		printer = new XMLPrinter(new PrintWriter(System.out));

		// I am the chat
     		System.out.println("Chat : ");
     		t1  = new XMLTree("text","I write text in the chat window");
     		res = new XMLTree("chat", t1);
     		show(res);

     		t1.setAttribute("lang","en");
     		show(res);

     		t1.setContents("It's an other text in the chat window");
     		show(res);

		res = new XMLTree("event",XMLTree.Attributes("room",room),res.contents());
     		show(res);
     		System.out.println("");

		// the answer from the server
     		System.out.println("Chat answer : ");
		res.insert( xuser );
		res.insert( xtime );
     		show(res);
     		System.out.println("");

		System.out.println("Using getByTagName : ");
		XMLTree a = res.getByTagName( "user" );
		XMLTree b = res.getByTagName( "text" );
		System.out.print( "user " + a.getText() + ", "); show(a);
		System.out.print( "text " + b.getText() + ", "); show(b);
     		System.out.println("");

		System.out.println("Using iterator : ");
		for (Enumeration e = res.elements() ; e.hasMoreElements() ;) {
			Object o = e.nextElement();	
			if( o instanceof XMLTree ) show( (XMLTree)o );
			else System.out.println( "Elt. : " + o.toString() );
           	}
     		System.out.println("");

		System.out.println("Using iterator (removing user) : ");
		res.remove( xuser );
		for (Enumeration e = res.elements() ; e.hasMoreElements() ;) {
			Object o = e.nextElement();	
			if( o instanceof XMLTree ) show( (XMLTree)o );
			else System.out.println( "Elt. : " + o.toString() );
           	}
     		System.out.println("");

		System.out.println("Using iterator (removing time) : ");
		res.remove( res.getByTagName( "time" ) );
		for (Enumeration e = res.elements() ; e.hasMoreElements() ;) {
			Object o = e.nextElement();	
			if( o instanceof XMLTree ) show( (XMLTree)o );
			else System.out.println( "Elt. : " + o.toString() );
           	}
     		System.out.println("");




		// I am the server
     		System.out.println("Server : ");
     		t2 = new XMLTree("event", res);
     		t2.setAttribute("user", user);
     		t2.setAttribute("room", room);
     		//t2.setAttribute("date", String.valueOf(System.currentTimeMillis()));
		t2.setAttribute("date", new XMLDate() );
     
     		show(t2);
     		System.out.println("");

     		System.out.println("Server : ");
    		//t2.setAttribute("date", String.valueOf(System.currentTimeMillis()));
     		t1  = new XMLTree("grapher", 
				new XMLTree("argument", XMLTree.Attributes("id","pj.589.1","edit","true"),XMLTree.Contents()) 
	   		);

		t2.setContents( t1 );
     		show(t1);
     		show(t2);
     		System.out.println("");
  	}
}

