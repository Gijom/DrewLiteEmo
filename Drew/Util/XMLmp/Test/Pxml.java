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
 * File: Pxml.java
 * Author:
 * Description:
 *
 * $Id: Pxml.java,v 1.5 2003/11/24 10:54:30 serpaggi Exp $
 */

/**

   A very simple XML parser in java

 */
// package XMLcheap;

import java.util.*;
import Drew.Util.XMLmp.*;

public class Pxml {

  static XMLParser parser;
  static XMLPrinter printer;

  static boolean flglisp = false;

  public static void main (String argv[])
  {
     int opts = 0;
     if (argv.length != 0)
     {
         int max = argv.length;
         int j = 0, k;
         while (j < max)
         {
             if (argv[j].equals("-x"))
             {
                 k=++j;
                 if (k < max)
                 try {
                    opts = Integer.parseInt(argv[k]);
                 } catch (NumberFormatException e) {
			opts = 0;
		 }
             }
             else
             if (argv[j].equals("-l"))
             {
                 flglisp = true;
             }
             else
             if (argv[j].equals("-b"))
             {
                 Pair.delimiters(1);
             }
             else
             if (argv[j].equals("-c"))
             {
                 Pair.delimiters(2);
             }
             j++;
         }
     }
     parser = new XMLParser(System.in);
     printer = new XMLPrinter(System.out);
     XMLTree res = XMLParser.PERROR;

     while (res != XMLParser.PEOF)
     {
         // res = parser.parse(XMLParser.PERMISSIVE | XMLParser.IGNORESPACES);
         res = parser.parse(opts);
         // System.out.println(XMLTree.toString(res));
         if (res == XMLParser.PERROR)
             System.out.println("Erreur");
         else
         if (flglisp)
             System.out.println(XMLTree.toString(res));
         else
         {
             printer.print(res); printer.flush();
         }
     }
  }
}

