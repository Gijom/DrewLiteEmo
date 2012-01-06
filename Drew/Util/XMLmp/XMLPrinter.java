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
 * File: XMLPrinter.java
 * Author: Jean-Jacques Girardot
 * Description:
 *
 * $Id: XMLPrinter.java,v 1.12 2006/04/28 08:35:29 girardot Exp $
 */

package Drew.Util.XMLmp;

import java.util.*;
import java.io.*;

/**

   A very simple XML Printer in java

 */

public class XMLPrinter {

  public static final int ADDNEWLINE = 1;
  public static final int AUTOFLUSH = 2;

  public static final int StdOpts = ADDNEWLINE ;

  private static final int NOQUOT = 1;
  private static final int NOAPOS = 2;
  private static final int NOGT = 4;
  private static final int NOLT = 8;
  private static final int NOAMP = 16; //PJ

  //private PrintWriter pw;
  private Writer pw;
  private int opts;

  /**
      Build a printer that prints on 
      a PrintWriter.
  */
  public XMLPrinter(PrintWriter p)
  {
     pw = p;
     opts = StdOpts;
  }

  /**
      Build a printer that prints on
      an OutputStream.
  */ 
  public XMLPrinter(OutputStream p)
  {
     // Printwriter are buffered in this case
     this( new PrintWriter( p ) );
  }

  /**
      Build a printer that prints on
      a Writer.
  */
  public XMLPrinter(Writer p)
  {
     	// May be, the writer is already buffered
     	pw = new BufferedWriter( new PrintWriter( p ) );
	opts = StdOpts;
  }

  /**
     Build a printer that prints in
     a StringBuffer.
  */
  public XMLPrinter(StringBuffer s)
  {
	s.append( "Not implemented feature, use a  StringWriter" );	
  }

  /**
    Printing options for the XMLPrinter.
  */
  public void setOptions(int o)
  {
      opts = o;
  }

  private char [] ac = new char[1];
  private void printch(char c) throws IOException
  {
      ac[0] = c;
  	 pw.write(ac,0,1);
  }


  private void prtxt(String s, int md) throws IOException
  {
     int i, l;
     char ch;
     l = s.length();
     for (i=0; i<l; i++)
     {
         ch = s.charAt(i);
         if ((ch == '"') && ((md & NOQUOT) != 0))
         {
             pw.write("&quot;");
         }
         else
         if ((ch == '\'') && ((md & NOAPOS) != 0))
         {
             pw.write("&apos;");
         }
         else
         if ((ch == '>') && ((md & NOGT) != 0))
         {
             pw.write("&gt;");
         }
         else
         if ((ch == '<') && ((md & NOLT) != 0))
         {
             pw.write("&lt;");
         }
         else //PJ
         if ((ch == '&') && ((md & NOAMP) != 0))
         {
             pw.write("&amp;");
         }
         else
             printch(ch);
     }
  }

  private void prstr(String s, int md) throws IOException
  {
     int i, l;
     int acnt, qcnt;
     char ch;
     char del = '"';
     int mode = 0;

     l = s.length();
     acnt = qcnt = 0;
     for (i=0; i<l; i++)
     {
         ch = s.charAt(i);
         if (ch == '"') qcnt++;
         if (ch == '\'') acnt++;
     }
     if (qcnt == 0)
         del = '"';
     else
     if (acnt == 0)
         del = '\'';
     else
     if (acnt > qcnt)
     {
         del = '"';
         mode = NOQUOT;
     }
     else
     {
         del = '\'';
         mode = NOAPOS;
     }
     printch(del);
     prtxt(s,mode|md);
     printch(del);
  }

  private void error(String msg)
  {
	try {
     		pw.write("<?ERROR type=");
     		prstr(msg,0);
     		pw.write("?>");
	}
	catch( IOException e ) {}
  }

  private void error(int num)
  {
     String s="";
     switch(num)
     {
     case 1 : s = "invalid element";
         break;
     case 2 : s = "invalid node type";
         break;
     default : s = "erreor " + num;
     }
     error(s);
  }

  private void eltppr(XMLTree p) throws IOException
  {
     Object cnt;
     Object w;
     Pair ptg, pp;
     String tnm;
     int md = 0;
     

     tnm = "";
     w = p.car();
     if (!(w instanceof Pair))
     {
        error(1);
        return;
     }
     ptg = (Pair)w;
     w = ptg.car();
     if (w instanceof String)
     {
         tnm = (String) w;
     }
     else
     if (w instanceof Pair)
     {
         pp = (Pair)w;
         Object x = pp.car();
         String s;
         if (x instanceof String)
         {
             s = (String)x;
             if (s.equals("?"))
             {
                 md = 1;
             }
             else
             if (s.equals("!"))
             {
                 md = 2;
             }
             else
             {
                 error(2);
                 return;
             }
         }
         else
         {
            error(1);
            return;
         }
         x = pp.cdr();
         if (x instanceof String)
         {
             tnm = (String) x;
         }
         else
         {
            error(1);
            return;
         }
     }
     else
     {
        error(1);
        return;
     }
     switch (md)
     {
     case 0 :
        pw.write("<");
        break;
     case 1 :
        pw.write("<?");
        break;
     case 2 :
        pw.write("<!");
        break;
     default :
        error(1);
        return;
     }
     pw.write(tnm);
     cnt = ptg.cdr();
     while ((cnt instanceof Pair) && (cnt != Pair.nil))
     {
        Pair wp = (Pair) cnt;
        Object c = wp.car();
        pw.write(" ");
        if (c instanceof Pair)
        {
             Pair wc = (Pair)c;
             String s;
             s = (String) wc.car();
             pw.write(s);
             pw.write("=");
             if (wc.cdr() instanceof String)
             {
                 s = (String) wc.cdr();
             }
             else
             {
                 s = wc.cdr().toString();
             }
             prstr(s,NOGT|NOAMP); //PJ
        }
        else
        {
             pw.write(c.toString());
        }
        cnt = wp.cdr();
     }
     cnt = p.cdr();
     if ((cnt instanceof Pair) && (cnt != Pair.nil))
     {
         pw.write(">");
         while ((cnt instanceof Pair) && (cnt != Pair.nil))
         {
             Pair qq = (Pair) cnt;
             Object elt = qq.car();
             if (elt instanceof XMLTree)
             {
                 eltppr((XMLTree)elt);
             }
             else
             if (elt instanceof String)
             {
                 prtxt((String)elt,NOLT|NOAMP); //PJ
             }
             else
             {
                 String s = elt.toString();
                 prtxt((String)s,NOLT|NOAMP); //PJ
             }
             cnt = qq.cdr();
         }
         pw.write("</");
         pw.write(tnm);
         pw.write(">");
     }
     else
     {
         switch (md)
         {
         case 0 :
            pw.write("/>");
            break;
         case 1 :
            pw.write("?>");
            break;
         case 2 :
            pw.write(">");
            break;
         default :
            error(1);
            return;
         }
     }
  }

  /**
     close the output 
  */
  public void close() 
  {
	try { pw.close(); } catch( IOException e) {}
  }

  /**
     Flush the output 
  */
  public void flush()
  {
	try { pw.flush(); } catch( IOException e) {}
  }

  /**
     Print an XMLTree.
  */
  public void print(XMLTree pp)
  {
	try {
      		if (pp == XMLParser.PERROR)
      		{
          		// ("<*ERROR*>");
      		}
      		else
      		if (pp == XMLParser.PEOF)
      		{
          		// ("<*EOF*>");
      		}
      		else
      		{
          		eltppr(pp);
          		if ((opts & ADDNEWLINE) != 0)
              		pw.write("\n");
      		}
      		if ((opts & AUTOFLUSH) != 0)
          		flush();
	}
	catch( IOException e ) {}
  }


  private void tprint(XMLTree pp) throws IOException
  {
      Object o = pp.cdr();
      if (o instanceof Pair)
      {
          Pair p = (Pair)o;
          while ((o instanceof Pair) && ((p=(Pair)o) != Pair.nil))
          {
               Object x = p.car();
               o = p.cdr();
               if (x instanceof String)
                   pw.write((String)x);
               else
               if (x instanceof XMLTree)
                   tprint((XMLTree)x);
          }
      }
  }

  /**
     Print only the text of an XMLTree.
  */
  public void printText(XMLTree pp)
  {
	try {
      		tprint(pp);
      		if ((opts & ADDNEWLINE) != 0)
          		pw.write("\n");
      		if ((opts & AUTOFLUSH) != 0)
          		flush();
	}
	catch( IOException e ) {}
  }

}

