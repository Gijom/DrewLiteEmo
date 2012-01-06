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
 * File: XMLParser.java
 * Author: Jean-Jacques Girardot
 * Description:
 *
 * $Id: XMLParser.java,v 1.9 2006/04/28 08:35:29 girardot Exp $
 */

package Drew.Util.XMLmp;

import java.util.*;
import java.io.*;

/**

   A very simple XML parser in java

 */

public class XMLParser {

    // Parser Flags
    public static final int STRICT = 0;
    public static final int PERMISSIVE = 1;
    public static final int IGNORESPACES = 2;
    public static final int ACCEPTERRORS = 4;
    public static final int FDBG1 = 256;

    public static final XMLTree PERROR = new XMLTree("ERROR");
    public static final XMLTree PEOF = new XMLTree("EOF");

    // The "private" part of the class
    public static final int ACEOF  = -1;

    public static final int CTERR =      1;
    public static final int CTEOF =      2;
    public static final int CTXMLLT =    3;
    public static final int CTXMLLTSL =  4;
    public static final int CTXMLLTQM =  5;
    public static final int CTXMLLTXM =  6;
    public static final int CTXMLGT =    7;
    public static final int CTXMLSLGT =  8;
    public static final int CTXMLQMGT =  9;
    public static final int CTXMLEQ =    10;
    public static final int CTXMLLB =    11;
    public static final int CTXMLRB =    12;
    public static final int CTXMLCST =   13;
    public static final int CTXMLSTR =   14;
    public static final int CTXMLIDNT =  15;
    public static final int CTXMLSSP =   16;
    public static final int CTXMLELT =   17;

    private static final int PSXMLOUT = 0;
    private static final int PSXMLCOM = 1;
    private static final int PSXMLTAG = 2;
    private static final int PSXMLIN = 3;

    private static final int SAXMLEXIT = 0;
    private static final int SAXMLWOTG = 1;
    private static final int SAXMLIDTG = 2;
    private static final int SAXMLATTR = 3;
    private static final int SAXMLWFEQ = 4;
    private static final int SAXMLSTVL = 5;
    private static final int SAXMLCNTS = 6;
    private static final int SAXMLCLID = 7;
    private static final int SAXMLCTAG = 8;
    private static final int SAXMLDCLS = 9;

    public static final char CSP = ' ';
    public static final char CTB = '\t';
    public static final char CLF = '\n';
    public static final char CCR = '\r';

    private StringBuffer sbXML = null;

    private int XMLdbg = 0;
    private int XMLparstate = PSXMLOUT;
    private int [] XMLstk = new int[32];
    private int XMLptst = 0;

    private Object tkValue;
    private boolean onlySpaces = true;
    private boolean permissive = false;
    private boolean ignorespaces = false;
    private boolean accepterrors = false;

    private int parserType = 0;
    private Reader is = null;
    private String ist = null;;
    private int ptr = 0;

    private int lastch = 0;


    /**
       Create a new parser, taking its input from 
       an InputStream.
    */
    public XMLParser (InputStream s)
    {
       this( new InputStreamReader(s) );
    }

    /**
       Create a new parser, taking its input from 
       an InputStreamReader.
    */
    public XMLParser (InputStreamReader s)
    {
       is = new BufferedReader(s, 10*1024);
    } 

    /**
       Create a new parser, taking its input from 
       a String.
    */
    public XMLParser (String s)
    {
       parserType = 1;
       ist = s;
    }
    
    private int readch()
    {
       int ch = lastch;
       if (ch != 0)
       {
           lastch = 0;
           return ch;
       }
       switch (parserType)
       {
       case 0 :
           try {
               ch= is.read();
//System.out.println( "XML readch : " + String.valueOf( ch ) );
           }
           catch (java.io.IOException e) {
               ch = ACEOF;
           }
           break;
       case 1 :
           if (ptr >= ist.length())
               return ACEOF;
           ch = ist.charAt(ptr++);
           break;
       default :
           ch = ACEOF;
       }
       return ch;
    }

    private void unputch(int ch)
    {
        lastch = ch;
    }


    //    XML handling


    static String trace(Object x)
    {
       return Pair.toString(x);
    }

    private void XMLadd(int ch)
    {
        if (sbXML == null)
        {
            sbXML = new StringBuffer();
            onlySpaces = true;
        }
        if ((ch != CSP) && (ch != CTB)
            && (ch != CCR) && (ch != CLF))
            onlySpaces = false;
        sbXML.append((char)ch);
    }

    private void XMLaddentity()
    {
        int ch, cc, i;

        cc = 0;
        ch = readch();
        if (ch == '#')
        {
            ch = readch();
            if (ch == 'x')
            {
                for (i=0; i<4; i++)
                {
                    ch = readch();
                    if (ch >= '0' && ch <= '9')
                    {
                        cc = (cc << 4) | (ch - '0');
                    }
                    else
                    if (ch >= 'A' && ch <= 'F')
                    {
                        cc = (cc << 4) | (ch - 'A' + 10);
                    }
                    else
                    if (ch >= 'a' && ch <= 'f')
                    {
                        cc = (cc << 4) | (ch - 'a' + 10);
                    }
                    else
                    {
                        unputch(ch);
                    }
                }
            }
            else
            {
                while (ch >= '0' && ch <= '9')
                {
                    cc = (10*cc) + (ch - '0');
                    ch = readch();
                }
                unputch(ch);
            }
            ch = readch();
            if (ch != ';')
                unputch(ch);
            XMLadd(cc);
            return;
        }
        if ((ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z'))
        {
            i = 0;
            StringBuffer sb = new StringBuffer();
            String str;
            while ((ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <=
'Z'))
            {
                sb.append((char)ch);
                ch = readch();
                i++;
            }
            if (ch != ';')
                unputch(ch);
            str = sb.toString();
            if (str.equals("lt"))
            {
                cc = '<';
            }
            else
            if (str.equals("gt"))
            {
                cc = '>';
            }
            else
            if (str.equals("amp"))
            {
                cc = '&';
            }
            else
            if (str.equals("quot"))
            {
                cc = '"';
            }
            else
            if (str.equals("apos"))
            {
                cc = '\'';
            }
            if (cc != 0)
                XMLadd(cc);
            else
            {
                XMLadd('&');
                sbXML.append(str);
                XMLadd(';');
            }
        }
    }

    private String XMLgetString()
    {
        if (sbXML == null)
            sbXML = new StringBuffer();
        String s = sbXML.toString();
        sbXML = null;
        return s;
    }

    private void XMLpushstate(int k)
    {
       XMLstk[XMLptst] = XMLparstate;
       if (XMLptst < 31) XMLptst ++;
       XMLparstate = k;
    }

    private void XMLpopstate()
    {
       if (XMLptst > 0) XMLptst --;
       XMLparstate = XMLstk[XMLptst];
    }

    private int XMLcheckcmt()
    {
        int ch;
        ch = readch();
        if (ch != '!')
        {
            if (ch == '/')
                return CTXMLLTSL;
            if (ch == '?')
                return CTXMLLTQM;
            unputch(ch);
            return CTXMLLT;
        }
        ch = readch();
        if (ch != '-')
        {
            unputch(ch);
            return CTXMLLTXM;
        }
        ch = readch();
        if (ch != '-')
        {
            unputch(ch);
            return CTERR;
        }
        XMLpushstate(PSXMLCOM);
        return CTXMLCST;
    }


    private int readXMLToken()
    {
       int ch, chty;
       int res = 0;
       int mcnt = 0;

       tkValue = null;

       for (;;)
       {
           ch = readch();
           if (ch == ACEOF)
               return CTEOF;
           switch (XMLparstate)
           {


           // Out of the Main construct
           case PSXMLOUT :
              if (ch == '<')
                  return XMLcheckcmt();
              if ((ch != CSP) && (ch != CTB)
                  && (ch != CLF) && (ch != CCR))
                  return CTERR;
              break;


           // Inside of a comment
           case PSXMLCOM :
              {
                  if (ch == '-')
                      mcnt++;
                  else
                  if (ch == '>')
                  {
                      if (mcnt >= 2)
                      {
                          XMLpopstate();
                      }
                      mcnt = 0;
                  }
                  else
                      mcnt = 0;
              }
              break;


           // Decoding a TAG
           case PSXMLTAG :
              if (ch == '/')
              {
                  ch = readch();
                  if (ch == '>')
                  {
                      return CTXMLSLGT;
                  }
                  unputch(ch);
                  return CTERR;
              }
              if (ch == '?')
              {
                  ch = readch();
                  if (ch == '>')
                  {
                      return CTXMLQMGT;
                  }
                  unputch(ch);
                  return CTERR;
              }
              if (ch == '=')
                  return CTXMLEQ;
              if (ch == '>')
                  return CTXMLGT;
              if ((ch == CSP) || (ch == CTB)
                  || (ch == CLF) || (ch == CCR))
              {
                  while ((ch == CSP) || (ch == CTB)
                  || (ch == CLF)|| (ch == CCR))
                  {
                      ch = readch();
                  }
                  unputch(ch);
                  return CTXMLSSP;
              }
              if ((ch == '\'') || (ch == '"'))
              {
                  int sep = ch;
                  ch = readch();
                  while ((ch != ACEOF) && (ch != sep)
                  // && (ch != '&')
                  && (ch != '>'))
                  {
                      if (ch == '&')
                          XMLaddentity();
                      else
                          XMLadd(ch);
                      ch = readch();
                  }
                  tkValue = XMLgetString();
                  return CTXMLSTR;
              }
              if (ch == '[')
                  return CTXMLLB;
              if (ch == ']')
                  return CTXMLRB;
              if ((ch != CSP) && (ch != CTB)
                  && (ch != CLF) && (ch != CCR)
                  && (ch != ACEOF))
              {
                  while ((ch != CSP) && (ch != CTB)
                         && (ch != CLF) && (ch != CCR)
                         && (ch != ACEOF) && (ch != '>')
                         && (ch != '?') && (ch != '<')
                         && (ch != '[') && (ch != ']')
                         && (ch != '"') && (ch != '\'')
                         && (ch != '/') && (ch != '='))
                  {
                      if (ch == '&')
                          XMLaddentity();
                      else
                          XMLadd(ch);
                      ch = readch();
                  }
                  unputch(ch);
                  tkValue = XMLgetString();
                  return CTXMLIDNT;
              }
              break;


           // Between TAGS
           case PSXMLIN :
              if (ch == '<')
                  return XMLcheckcmt();
              while ((ch != ACEOF) && (ch != '<'))
              {
                  if (ch == '&')
                      XMLaddentity();
                  else
                      XMLadd(ch);
                  ch = readch();
              }
              unputch(ch);
              tkValue = XMLgetString();
              return CTXMLSTR;

           }
       }
    }


    private XMLTree readXMLin(int synstate)
    {
        XMLTree st;
        Pair tg, cp, wp, ap;
        int tk, md, bk; 
        String tgid;
        boolean spaces;
        boolean texts;

        // cp = tg = new Pair("* undefined *", Pair.nil);
        // The "main" pair is a XMLTree
        st = new XMLTree("* undefined *", Pair.nil, Pair.nil);
        cp = tg = (Pair)st.car();
        wp = ap = null;
        tgid = null;
        md = bk = 0;

        texts = false;
        spaces = true;

        while (synstate != SAXMLEXIT)
        {
           tk = readXMLToken();

           if ((XMLdbg & FDBG1) != 0)
              System.err.println("Etat = "+synstate + "   " +
                    "Token = "+ tk + "  " + trace(tkValue));

           if (tk == CTERR)
           {
               if (accepterrors)
                   synstate = (synstate + 1) % 9;
               else
                   return PERROR;
           }
           if (tk == CTEOF)
           {
               if ((accepterrors) && (synstate > 2))
                   synstate = SAXMLEXIT;
               else
                   return PEOF;
           }


           switch (synstate)
           {
           case SAXMLEXIT :
               break;


           // Waiting for '<' '<!' or '<?' ; skiping spaces
           case SAXMLWOTG :
               if (tk == CTXMLCST)
               {
                    break;
               }
               if (tk == CTXMLLT)
               {
                    XMLparstate = PSXMLTAG;
                    synstate = SAXMLIDTG;
                    break;
               }
               if (tk == CTXMLLTQM)
               {
                    md = 1;
                    XMLparstate = PSXMLTAG;
                    synstate = SAXMLIDTG;
                    break;
               }
               if (tk == CTXMLLTXM)
               {
                    md = 2;
                    XMLparstate = PSXMLTAG;
                    synstate = SAXMLIDTG;
                    break;
               }
               return PERROR;


           // Waiting for an identificator
           case SAXMLIDTG :
               if (tk == CTXMLIDNT)
               {
                    synstate = SAXMLATTR;
                    tgid = (String)tkValue;
                    if (md == 1)
                    {
                        tkValue = new Pair("?",tgid);
                    }
                    else
                    if (md == 2)
                    {
                        tkValue = new Pair("!",tgid);
                        synstate = SAXMLDCLS;
                    }
                    tg.setCar(tkValue);
                    break;
               }
               if ((tk == CTXMLSSP) && permissive)
                   break;
               return PERROR;


           // Waiting for attributes or '>', '/>' or '?>'
           // Skipping spaces
           case SAXMLATTR :
               if (tk == CTXMLSSP)
                   break;
               if (tk == CTXMLSLGT)
               {
                   if (md == 0)
                   {
                       synstate = SAXMLEXIT;
                       break;
                   }
               }
               if (tk == CTXMLQMGT)
               {
                   if (md == 1)
                   {
                       synstate = SAXMLEXIT;
                       break;
                   }
               }
               if (tk == CTXMLGT)
               {
                    if (md != 0)
                        return PERROR;
                    cp = st;
                    XMLparstate = PSXMLIN;
                    synstate = SAXMLCNTS;
                    break;
               }
               if (tk == CTXMLIDNT)
               {
                    wp = new Pair();
                    cp.setCdr(wp);
                    ap = new Pair();
                    wp.setCar(ap);
                    ap.setCar(tkValue);
                    cp = wp;
                    synstate = SAXMLWFEQ;
                    break;
               }
               return PERROR;


           // Attribute found - Waiting for '='
           // Skipping spaces
           case SAXMLWFEQ :
               if (tk == CTXMLSSP)
                   break;
               if (tk == CTXMLEQ)
               {
                    synstate = SAXMLSTVL;
                    break;
               }
               return PERROR;


           // Attribute found - Waiting for a string
           // Skipping spaces
           case SAXMLSTVL :
               if (tk == CTXMLSSP)
                   break;
               if ((tk == CTXMLSTR) || ((tk == CTXMLIDNT) &&
                                       permissive))
               {
                    ap.setCdr(tkValue);;
                    synstate = SAXMLATTR;
                    break;
               }
               return PERROR;


           // Waiting for '</' or Contents
           case SAXMLCNTS :
               if (tk == CTXMLLTSL)
               {
                    if (permissive && texts && spaces)
                    {
/*
System.err.println("Content 'parse' spaces="+spaces+
    "     texts="+texts);
*/
                        Object w = st.cdr();
                        wp = st;
                        while ((w instanceof Pair) && (w != Pair.nil))
                        {
                            cp = (Pair)w;
                            if (cp.car() instanceof String)
                            {
                                wp.setCdr(cp.cdr());
                            }
                            else
                            {
                                wp = cp;
                            }
                            w = cp.cdr();
                            
                        }
                    }
                    XMLparstate = PSXMLTAG;
                    synstate = SAXMLCLID;
                    break;
               }
               if (tk == CTXMLCST)
               {
                    break;
               }
               if (tk == CTXMLLT)
               {
                    XMLparstate = PSXMLTAG;
                    tkValue = readXMLin(SAXMLIDTG);
                    XMLparstate = PSXMLIN;

                    if (tkValue == PERROR)
                        tk = CTERR;
                    else
                    if (tkValue == PEOF)
                        tk = CTEOF;
                    else
                        tk = CTXMLELT;
                    
               }
               if (tk == CTERR)
               {
                    if (accepterrors)
                    {
                        XMLparstate = PSXMLTAG;
                        synstate = SAXMLCLID;
                        break;
                    }
                    return PERROR;
               }
               if (tk == CTEOF)
               {
                    if (accepterrors)
                    {
                        XMLparstate = PSXMLTAG;
                        synstate = SAXMLEXIT;
                        break;
                    }
                    return PEOF;
               }
               if (tk == CTXMLSTR)
               {
                    spaces = spaces && onlySpaces;
                    texts = true;
               }
               wp = new Pair();
               wp.setCar(tkValue);
               cp.setCdr(wp);
               cp = wp;
               break;


           case SAXMLCLID :
               if (tk == CTXMLIDNT)
               {
                    if (tgid.equals(tkValue))
                    {
                        synstate = SAXMLCTAG;
                        break;
                    }
               }
               if ((tk == CTXMLGT) && permissive)
               {
                   synstate = SAXMLEXIT;
                   break;
               }
               if ((tk == CTXMLSSP) && permissive)
                   break;
               return PERROR;


           case SAXMLCTAG :
               if (tk == CTXMLSSP)
                   break;
               if (tk == CTXMLGT)
               {
                   synstate = SAXMLEXIT;
                   break;
               }
               return PERROR;


           case SAXMLDCLS :
               if (tk == CTXMLSSP)
                   break;
               if (tk == CTXMLGT)
               {
                   if (bk != 0)
                   {
                       return PERROR;
                   }
                   synstate = SAXMLEXIT;
                   break;
               }
               if (tk == CTXMLLB)
               {
                   bk++;
                   break;
               }
               if (tk == CTXMLRB)
               {
                   bk--;
                   if (bk < 0)
                   {
                       return PERROR;
                   }
                   break;
               }
               if ((tk == CTXMLSTR) || (tk == CTXMLIDNT))
               {
                   wp = new Pair();
                   wp.setCar(tkValue);
                   cp.setCdr(wp);
                   cp = wp;
                   break;
               }
               break;


           default :
               System.err.println("Error ; State = "+synstate);
               return PERROR;
           }
        }

        if ((XMLdbg & FDBG1) != 0)
            System.err.println("Returning : "+ trace(st));

        return st;
    }

    /**
       Parsing of the input. The parameters allows to specify parser's
      options.
    */
    public XMLTree parse(int trace)
    {
        XMLparstate = PSXMLOUT;
        XMLptst = 0;
        XMLdbg = trace;
        sbXML = null;
        permissive = (trace & (PERMISSIVE|ACCEPTERRORS)) != 0;
        ignorespaces = (trace & IGNORESPACES) != 0;
        accepterrors = (trace & ACCEPTERRORS) != 0;
        if ((XMLdbg & FDBG1) != 0)
            System.err.println("Inside 'parse'");
        return readXMLin(SAXMLWOTG);
    }

    /**
      Strict parsing of the input.
    */
    public XMLTree parse()
    {
        return parse(STRICT);
    }

    public void finalizer() {
	try {
	    is.close();
	}
	catch(IOException e) {}
    }
}


