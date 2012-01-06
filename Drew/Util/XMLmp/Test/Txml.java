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
 * File: Txml.java
 * Author:
 * Description:
 *
 * $Id: Txml.java,v 1.7 2007/02/20 16:03:42 collins Exp $
 */

/**

   A very simple XML parser in java

 */
// package XMLcheap;

import java.io.*;
import java.util.*;
import Drew.Util.XMLmp.*;

public class Txml {

  static XMLParser parser;
  static XMLPrinter printer;
  static StringBuffer sb;

  static boolean flglisp = false;

  static XMLTree res = XMLParser.PERROR;
  static XMLTree t1, t2, t3;

  public static void lshow(Pair p)
  {
     System.out.println(Pair.toString(p));
  }

  public static void show()
  {
        System.out.println(XMLTree.toString(res));
        printer.print(res);
    	printer.flush();
  }

  public static void main (String argv[])
  {
     int opts = 0;
     Pair p;

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
     printer = new XMLPrinter(new PrintWriter(System.out));


     System.out.println("Test 1.");
     res = new XMLTree("toto");
     show();

     res.add("Hello, world");
     show();

     res.setAttribute("size","12");
     show();

     p = res.contents();
     lshow(p);

     t1 = new XMLTree("hr");
     res.add(t1);
     show();

     res.setAttribute("name","abcd");
     show();

     res.setAttribute("size","18");
     res.add("Good");
     show();

     System.out.println("");
     System.out.println("");

     System.out.println("Test 2.");
     // Autre exemple
     res = new XMLTree("text",
             XMLTree.Attributes("size","18","name","abcd"),
             XMLTree.Contents("this is ",
                      new XMLTree("b",
                                  XMLTree.Attributes(),
                                  XMLTree.Contents("bold")),
                      " text.",
                      new XMLTree("hr")));
     printer.printText(res);
     show();
     // Infos
     System.out.println("");
     System.out.println("");

     System.out.println("Test 3.");
     System.out.println("Contents length = " +
         res.contents().length());
 //    System.out.println("Attributes length = " +
 //        res.attributes().length());
     System.out.println("Attribute name = " +
         res.getAttributeValue("name"));
     System.out.println("Attribute size = " +
         res.getAttributeValue("size"));
     System.out.println("Attribute toto = " +
         res.getAttributeValue("toto"));
     System.out.println("Attribute toto = " +
         res.getAttributeValue("toto", null));
     System.out.println("Attribute toto = " +
         res.getAttributeValue("toto", "undefined"));


     // Test
     /* nil is not public
     Pair.nil.setValue("hello");
     System.out.println("nil = " + Pair.nil.toString());
     System.out.println("");
     System.out.println("");
     */

     System.out.println("Test 4.");
     parser = new XMLParser("<test a='hello'>Contents</test>");
     res = parser.parse(0);
     printer.printText(res);
     show();

     System.out.println("");
     System.out.println("");

     System.out.println("Test 5.");
     res = new XMLTree("text", "A single sentence");
     printer.printText(res);
     show();
     System.out.println("");
     System.out.println("");

     System.out.println("Test 6.");
     sb = new StringBuffer();
     printer = new XMLPrinter(sb);
     show();
     System.out.println("");
     System.out.println("String buffer = " + sb.toString());
     printer.flush();

     System.out.println("Test 7.");
     StringWriter sw = new StringWriter();
     printer = new XMLPrinter(sw);
     show();
     System.out.println("");
     System.out.println("String Writer = " + sw.toString());
     printer.flush();
  }
}

