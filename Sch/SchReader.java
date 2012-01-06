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
 * File: SchReader.java
 * Author: Jean-Jacques Girardot
 * Description:
 *
 * $Id: SchReader.java,v 1.5 2006/04/28 08:02:51 girardot Exp $
 */

package Sch;

import java.io.*;

/**
 *
 *  Misc basic Input 
 *
 *
 *
 *     @author Jean-Jacques Girardot
 */
public class SchReader extends Globaldata {

    /**
        Variables de classes
    */
    public static final byte KSEP = 1;
    public static final byte KNUM = 2;
    public static final byte KIDT = 4;
    public static final byte KSTD = 8;
    public static final byte KIGN = 16;


    public static byte tch[];
    private static final int BBSIZ = 128;


    /**
        Variables d'instance
    */
    public boolean readComments;


    private int bbuf[];
    private int bbufmpt;
    private int bbpt;
    private int bcnt;

    private int status;
    private int flags;
    private int lastch;

    // InputStream is;
    Reader is;

    static {
       int i;
       tch = new byte[256];

       for (i=0; i<256; i++)
       {
           tch[i]=KIGN;
       }
       for (i=33; i<128; i++)
       {
           tch[i]=KIDT;
       }
       for (i=161; i<256; i++)
       {
           tch[i]=KIDT;
       }
       for (i='0'; i<='9'; i++)
       {
           tch[i]|=KNUM;
       }
       tch['\'']=KSEP;
       tch['(']=KSEP;
       tch[')']=KSEP;
       tch['"']=KSEP;
       tch[';']=KSEP;
       tch['`']=KSEP;
       tch[',']=KSEP;
       tch['#']=KSEP;
    }

    private void stdinit(int fl)
    {
       // Initialisations diverses
       status = POPENED;
       flags = fl;
       lastch = 0;

       // Initialisation table des caracteres
       bbuf = new int[BBSIZ+2];
       bbufmpt = BBSIZ;
       bbpt = 0;
       bcnt = 0;

       readComments = false;
    }


    public SchReader(int fl, InputStream s) {
       stdinit(fl);
       is = new InputStreamReader(s);
    }


    public SchReader(int fl, String s) {
       stdinit(fl);
       is = new StringReader(s);
    }


    public SchReader(int fl, Reader r) {
       stdinit(fl);
       is = r;
    }


    public SchReader() {
        this(PSTIN,System.in);
    }


    public void readerStrReset(String s) {
       stdinit(flags);
       is = new StringReader(s);
    }


    void unputch(int ch)
    {
        lastch = ch;
    }


    public boolean ready()
    {
        if (status == PCLOSED)
            return true;
        if ((lastch != 0) || (bcnt > 0))
            return true;
        try {
            return is.ready();
        } catch (java.io.IOException e) {
            return false;
        }
    }


    public int readch()
    {
       int ch=0;

       if (status == PCLOSED)
       {
           ch = ACEOF;
       }
       else
       if (lastch != 0)
       {
           ch=lastch; lastch=0;
       }
       else
       {
          if ((bcnt <= 0) && ((runmode & RFTERIN) == RFTERIN)
                  && this.equals(scr))
          {
              // Print PROMPT
              try {
                  str.print("? ");
                  str.flush();
              } catch (SchRunTimeException e) {}
          }
          while (bcnt <= 0)
          {
             bbpt = 0;
             do
             {
                {
                    try {
                       ch= is.read();
                    }
                    catch (java.io.IOException e) {
                       ch = ACEOF;
                    }
                }
                bbuf[bbpt++]=ch;
		// Correction of buffer overflow bug JJG 2006-04-19
                if (bbpt >= bbufmpt)
                {
                    // Increase buffer size by 25%
                    int m = bbufmpt + (bbufmpt>>2);
                    int p[]= new int[m+2];
                    int w;
                    for (w=0; w<bbpt; w++)
                        p[w] = bbuf[w];
                    bbuf = p;
                    bbufmpt = m;
                }
                if (ch == 8)
                {
                   bbpt-=2; bbpt=bbpt < 0 ? 0 : bbpt;
                }
             } while (ch != ACEOF && ch != ACNL && ch != ACCR);
             bcnt = bbpt;
             bbpt = 0;
          }
          ch = bbuf[bbpt++]; bcnt--;
       }
       if (ch == ACEOF)
       {
           this.close();
       }
       //str.println("ch= "+ch);
       return ch;
    }

    public int peakCh()
    throws SchRunTimeException
    {
        int ch = readch();
        unputch(ch);
        if (ch == ACEOF)
            return CEOF;
        else
            return Gdm.newchar(ch);
    }

    public void close()
    {
        status = PCLOSED;
        if ((is != null) && ((flags & PSTFCLOSE) != 0))
        {
            /* Should close the Reader */
            try {
                is.close();
            } catch (Exception e) { }
            is = null;
        }
    }

    public void close(int flags)
    {
        if ((flags & PSTISR) == PSTISR)
        {
            this.close();
        }
        status = PCLOSED;
    }

    public int readCh()
    throws SchRunTimeException
    {
        int ch = readch();
        if (ch == ACEOF)
            return CEOF;
        else
            return Gdm.newchar(ch);
    }

    public int readLine()
    throws SchRunTimeException
    {
        int ch = readch();
        if (ch == ACEOF)
            return CEOF;
        this.unputch(ch);
        StringBuffer sb=new StringBuffer("");
        String str;
        boolean cy=true;
        int r;

        do {
            ch = this.readch();
            switch (ch)
            {
            case ACEOF :
            case ACNL :
               cy = false;
               break;
            case ACCR :
               break;
            default :
               sb.append((char)ch);
            }
        } while (cy);
        str = sb.toString();
        r = Gdm.newcons(TSTR);
        Gdm.cellobj[r]=str;
        Gdm.protect(r);
        return r;
    }

    public int readToken()
    throws SchRunTimeException
    {
       int ch=0;
       int chty;
       int k;
       for (;;)
       {
           ch=this.readch();
           if (ch == ACEOF)
           {
               return CEOF;
           }
           chty=tch[ch];

           
           if ((chty & KSEP) != 0)
           {
              switch (ch)
              {

              case '(' :
                  return CTOP;

              case ')' :
                 return CTCP;

              case ';' :
                 if (readComments)
                 {
                     StringBuffer sb=new StringBuffer(";");
                     String str;
                     boolean cy=true;
                     int r;

                     do {
                         ch = this.readch();
                         switch (ch)
                         {
                         case ACEOF :
                         case ACNL :
                         case ACCR :
                            cy = false;
                            this.unputch(ch);
                            break;
                         default :
                            sb.append((char)ch);
                         }
                     } while (cy);
                     str = sb.toString();
                     r = Gdm.newcons(TCOMM);
                     Gdm.cellobj[r]=str;
                     Gdm.protect(r);
//str.println("String => \"" + str + "\" (" + r + ")" );
                     return r;
                 }
                 ch=this.readch();
                 while (ch != ACEOF && ch != ACNL && ch != ACCR)
                 {
                    ch=this.readch();
                 }
                 this.unputch(ch);
                 break;

              case '`' :
                 return CTBQUOT;

              case ',' :
                 ch=this.readch();
                 if (ch == '@')
                 {
                     k = CTCOMAT;
                 }
                 else
                 {
                     k = CTCOMMA;
                     this.unputch(ch);
                 }
                 return k;

              case '\'' :
                 return CTQUOT;

              case '#' :
                 ch=this.readch();
                 switch (ch)
                 {
                 case 't' :
                 case 'T' :
                     return CTRUE;
                 case 'f' :
                 case 'F' :
                     return CFALSE;
                 case '\\' :
                     {
                         ch=this.readch();
                         k = Gdm.newchar(ch);
                         Gdm.protect(k);
                     }
                     return k;
                 case '!' :
                     // Accept as end-of-line comment
                     while (ch != ACEOF && ch != ACNL && ch != ACCR)
                     {
                        ch=this.readch();
                     }
                     this.unputch(ch);
                     break;
                 default :
                     return CTERR;
                 }
                 break;

              case '"' :
                 {
                     StringBuffer sb=new StringBuffer();
                     String str;
                     boolean cy=true;
                     int r;

                     do {
                         ch = this.readch();
                         switch (ch)
                         {
                         case '"' :
                         case ACEOF :
                            cy = false;
                            break;
                         case '\\' :
                            ch = this.readch();
                            if (ch == ACEOF)
                            {
                                cy = false; break;
                            }
                            if (ch == ACNL)
                            {
                                break;
                            }
                            switch (ch)
                            {
                            case 'n' : ch = ACNL;
                                break;
                            }
                         default :
                            sb.append((char)ch);
                         }
                     } while (cy);
                     str = sb.toString();
                     r = Gdm.newcons(TSTR);
                     Gdm.cellobj[r]=str;
                     Gdm.protect(r);
//str.println("String => \"" + str + "\" (" + r + ")" );
                     return r;
                 }

              // Sinon
              default : break;
              }
           }
           else
           if ((chty & KIDT) != 0)
           {
               StringBuffer id=new StringBuffer();
               String str;
               int r;
               do {
                   id.append((char)ch);
                   ch=this.readch();
                   if (ch != ACEOF)
                       chty=tch[ch];
                   else
                       chty=0;
               }
               while ((chty & KIDT) != 0);
               this.unputch(ch);
               str = id.toString();
               // Est-ce un point ?
               if (str.equalsIgnoreCase("."))
               {
                  return CTDOT;
               }
               // Est-ce un nombre ?
               boolean anum=true;
               boolean negn=false;
               boolean tnum=false;
               int val = 0;
               for (int i=0; i<str.length() && anum; i++)
               {
                   ch=str.charAt(i);
                   if ((ch < '0') || (ch > '9'))
                   {
                       if ((i != 0) || (ch != '-'))
                           anum = false;
                   }
                   if (ch != '-')
                   {
                       val = val*10 + (ch-48);
                       tnum = true;
                   }
                   else
                       negn = true;
               }
               if (anum && tnum)
               {
                   r = Gdm.newint(negn ? - val : val);
               }
               else
               {
                   r = Symbols.get(str);
               }
               Gdm.protect(r);
               return r;
           }
       }
    }

    private int readIn()
    throws SchRunTimeException
    {
       int tk=0;
       int c;
       for (;;)
       {
           tk = this.readToken();
           switch (Gdm.celltyp[tk])
           {
           case TSPEC :
               switch (tk)
               {
               case CTOP :
                 {
                    int lis= CNULL;
                    int k, kur;
                    kur=CNULL;
                    while (((c=this.readIn()) != CEOF) &&
                           (c != CTCP) && (c!= CTDOT) &&
                           (c != CTERR))
                    {
                        k = Gdm.newcons(TPAIR);
                        Gdm.protect(k);
                        Gdm.setcar(k, c);
                        if (lis == CNULL)
                        {
                           lis = k;
                        }
                        else
                        {
                           Gdm.setcdr(kur, k);
                        }
                        kur = k;
                    }
                    if (c == CTERR)
                        return CTERR;
                    if (c == CTDOT)
                    {
                        if (Gdm.typ(kur) != TPAIR)
                        {
                            return CTERR;
                        }
                        c = this.readIn();
                        if ((c==CEOF) || (c==CTCP) || (c==CTDOT)
                            || (c==CTERR))
                        {
                            return CTERR;
                        }
                        Gdm.setcdr(kur, c);
                        c = this.readIn();
                        if (c != CTCP)
                            return CTERR;
                    }
                    return lis;
                 }
               case CTCP :
               case CTERR :
               case CEOF :
               case CTDOT :
                 return tk;
               case CTQUOT :
                 c=this.readIn();
                 if (Gdm.typ(c) == TSPEC)
                     return CTERR;
                 return Gdm.list(CQUOTE,c);
               case CTBQUOT :
                 c=this.readIn();
                 if (Gdm.typ(c) == TSPEC)
                     return CTERR;
                 return Gdm.list(CQQUOTE,c);
               case CTCOMMA :
                 c=this.readIn();
                 if (Gdm.typ(c) == TSPEC)
                     return CTERR;
                 return Gdm.list(CUQUOTE,c);
               case CTCOMAT :
                 c=this.readIn();
                 if (Gdm.typ(c) == TSPEC)
                     return CTERR;
                 return Gdm.list(CUQUOTES,c);
               }
               break;
           case TEOF :
           case TINT :
           case TSTR :
           case TSYM :
           case TBOOL :
           case TCHAR :
               return tk;
           }
       }
    }


    /*
         General read procedure

         discards ")"
         change "." to read error

    */
    public int read()
    throws SchRunTimeException
    {
       boolean cy = true;
       int res=CTERR;
       while (cy)
       {
           res=readIn();
           switch (res)
           {
           // This discards ")"
           case CTCP :
               break;
           // This converts "." to "read-error"
           case CTDOT :
               res = CTERR;
           // Returns result
           default :
               cy = false;
           }
       }
       return res;
    }


}

