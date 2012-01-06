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
 * File: SchPrinter.java
 * Author:
 * Description:
 *
 * $Id: SchPrinter.java,v 1.5 2003/11/24 10:54:30 serpaggi Exp $
 */

package Sch;

import java.io.*;

/**
 *
 *  Output.
 *
 *
 *     @author Jean-Jacques Girardot
 */

public class SchPrinter extends Globaldata {

    /*
        Wariables de classe
    */


    /* 
        Variables d'instance
    */

    public Writer wr;

    public boolean skip;
    public boolean dspmode;
    public boolean dmpenv;

    private int status;

    private int prcntr;
    private int maxcntr;
    
    private void init()
    {
        skip=false;
        dspmode=false;
        dmpenv=false;
        status = POPENED;
        prcntr = 0;
        maxcntr = KSMPRC;
    }


    public SchPrinter (OutputStream out)
    {
        this.init();
        wr = new OutputStreamWriter(out);
    }

    public SchPrinter ()
    {
        this(System.out);
    }

    public SchPrinter (Writer strw)
    {
        this.init();
        wr = strw;
    }


    private void wrwrite(String str)
    throws SchRunTimeException
    {
        if (status != POPENED)
            throw new SchRunTimeException(ERRPCL);
        try {
            wr.write(str);
        } catch (java.io.IOException e) {
            // que faire ?
            throw new SchRunTimeException(ERRWRT);
        }
    }

    public void flush()
    throws SchRunTimeException
    {
        if (status != POPENED)
            return;
        try {
            wr.flush();
        } catch (java.io.IOException e) {
            // que faire ?
            throw new SchRunTimeException(ERRWRT);
        }
    }

    public void close()
    throws SchRunTimeException
    {
        boolean signal = false;
        if (status != POPENED)
            return;
        status = PCLOSED;
        if (wr != null)
        {
            try {
                wr.flush();
                wr.close();
            } catch (java.io.IOException e) {
                signal = true;
            }
            wr = null;
        }
        if (signal)
            throw new SchRunTimeException(ERRWRT);
    }


    public String printerStrReset()
    throws SchRunTimeException
    {
        this.flush();
        String res = wr.toString();
        wr = new java.io.StringWriter();
        return res;
    }


    public String printable(String str)
    {
        StringBuffer sb=new StringBuffer("\"");
        char ch; int i, l;
        l=str.length();
        for (i=0; i<l; i++)
        {
            ch = str.charAt(i);
            switch(ch)
            {
            case '"' :
                sb.append('\\'); sb.append('"'); break;
            case '\\' :
                sb.append('\\'); sb.append('\\'); break;
            case ACNL :
                sb.append('\\'); sb.append('n'); break;
            default :
                sb.append(ch);
            }
        }
        sb.append('"');
        return sb.toString();
    }


    public String printChar(int ch)
    {
        StringBuffer sb=new StringBuffer("#\\");
        char xch; Character zch;
        switch (ch)
        {
        case ACNL :
            sb.append("newline");
            break;
        case ACSP : 
            sb.append("space");
            break;
        default :
            xch = (char) ch;
            zch = new Character(xch);
            sb.append(zch.toString());
        }
        return sb.toString();
    }


    /** Impressions */
    public void print(int cell)
    throws SchRunTimeException
    {
        if (prcntr++ >= maxcntr)
        {
            wrwrite("... ");
            return;
        }
        if (skip)
        {
            skip=false;
            if (!dspmode) wrwrite(" ");
        }

        if (cell < 0 || cell >= Gdm.cellcount)
        {
            wrwrite("#<U>");
        }
        else
        {
            int k=Gdm.celltyp[cell];
            switch(k)
            {

            case TNULL : 
                wrwrite("()");
                break;

            case TINT : 
                wrwrite(""+Gdm.cellcar[cell]);
                break;

            case TEOF : 
                wrwrite("#<EOF>");
                break;

            case TBOOL :
                if (cell == CTRUE)
                   wrwrite("#t");
                else
                   wrwrite("#f");
                break;

            case TSYM :
                wrwrite((String)Gdm.cellobj[cell]);
                break;

            case TCOMM :
                wrwrite((String)Gdm.cellobj[cell]);
                break;

            case TCHAR :
                if (dspmode)
                {
                    wrwrite(
                       (new Character((char)Gdm.cellcar[cell]))
                       .toString());
                }
                else
                     wrwrite(printChar(Gdm.cellcar[cell]));
                break;

            case TSTR :
                if (dspmode)
                    wrwrite(
                        (String)Gdm.cellobj[cell]);
                else
                    wrwrite(
                        printable((String)Gdm.cellobj[cell]));
                break;

            case TSUBR : 
                wrwrite("#<SUBR:" +
                RefOps.getname(Gdm.cellobj[cell].getClass()) + ":" +
                ((SchPrimitive)Gdm.cellobj[cell]).name(Gdm.cellcdr[cell]) + ">");
                break;

            case TMACRO : 
                wrwrite("#<MACRO:" +
                Gdm.cellcdr[cell] + ">");
                break;

            case TLAMBDA : 
                wrwrite("#<LAMBDA ");
                this.print(Gdm.car(cell));
                this.print(Gdm.cdr(cell));
                wrwrite(">");
                break;

            case TPAIR :
                if (ConsOps.len(cell) == 2)
                {
                    int w = 1;
                    switch (Gdm.car(cell))
                    {
                    case CQUOTE : 
                        wrwrite("'");
                        break;
                    case CQQUOTE :
                        wrwrite("`");
                        break;
                    case CUQUOTE :
                        wrwrite(",");
                        break;
                    case CUQUOTES :
                        wrwrite(",@");
                        break;
                    default : w = 0;
                    }
                    if (w != 0)
                    {
                        this.print(Gdm.cadr(cell));
                        break;
                    }
                }
                wrwrite("(");
                while ((k==TPAIR) && (prcntr <= maxcntr))
                {
                   this.print(Gdm.car(cell));
                   cell = Gdm.cdr(cell);
                   k=Gdm.celltyp[cell];
                }
                if ((k != TNULL) && (prcntr <= maxcntr))
                {
                   wrwrite(" . ");
                   skip=false;
                   this.print(cell);
                }
                wrwrite(")");
                break;

            case TUNDEF :
                wrwrite("#<UNDEF>");
                break;

            case TSPEC :
                switch (cell)
                {
                case CEOF :
                    wrwrite("#<EOF>");
                    break;
                case CTERR :
                    wrwrite("#<ERR>");
                    break;
                case CNDO :
                    wrwrite("#<UNDISP>");
                    break;
                default :
                    wrwrite("#<SPEC:" +
                    Gdm.cellcar[cell] + ":" + Gdm.cellcdr[cell] + ">");
                    break;
                }
                break;

            case TVC :
                if (dmpenv)
                {
                   dmpenv = false;
                   wrwrite("{"); 
                   skip=false;
                   this.print(Gdm.car(cell));
                   wrwrite(":"); 
                   skip=false;
                   this.print(Gdm.cdr(cell));
                   wrwrite("}"); 
                   dmpenv = true;
                }
                else
                {
                   wrwrite("{"); 
                   skip=false;
                   this.print(Gdm.car(cell));
                   wrwrite("}"); 
                }
                break;

            case TENV :
                if (dmpenv)
                {
                    wrwrite("#<ENV: ");
                    this.print(Gdm.car(cell));
                    this.print(Gdm.cdr(cell));
                    wrwrite(">");
                }
                else
                {
                   wrwrite("#<ENV:" +
                   Gdm.cellcar[cell] + ":" + Gdm.cellcdr[cell] + ">");
                }
                break;

            case TCTRL :
                wrwrite(Gdm.cellname(cell));
                break;

            case TKONT :
                wrwrite("#<CONT:"+cell+">");
                break;

            case TDELAY :
                wrwrite("#<DELAY:"+cell+">");
                break;

            case TTHRD :
                {
                    Evaluator e = (Evaluator)Gdm.cellobj[cell];
                    wrwrite("#<THREAD:");
                    this.print(e.name);
                    wrwrite(":"+Gdm.cellcar[e.timestamp]+">");
                }
                break;

            case TLOCK :
                {
                    wrwrite("#<LOCK:"+Gdm.cellcar[cell]+":");
                    this.print(Gdm.cellcdr[cell]);
                    wrwrite(">");
                }
                break;

            case TCELL :
                {
                    wrwrite("#<CELL:");
                    this.print(Gdm.cellcar[cell]);
                    wrwrite(":");
                    skip=false;
                    this.print(Gdm.cellcdr[cell]);
                    wrwrite(">");
                }
                break;

            case TPORT :
                {
                    wrwrite("#<PORT:"+
                         Gdm.cellcar[cell]+":"+cell+">");
                }
                break;

            case TERR :
            case TJMETH :
            case TJOBJ :
                {
                    Object o=Gdm.cellobj[cell];
                    String str = o.toString();
                    if (dspmode)
                        wrwrite(str);
                    else
                        wrwrite(printable(str));
                }
                break;

            default : wrwrite("#<TY:" + 
                Gdm.celltyp[cell] + ":" +
                Gdm.cellcar[cell] + ":" + Gdm.cellcdr[cell] + ">");
                break;
            }
        }
        skip=true;
     }


     /* Entrees externes */
     public void print(String str)
     throws SchRunTimeException
     {
         wrwrite(str);
     }

     public void println(String str)
     throws SchRunTimeException
     {
         wrwrite(str); wrwrite("\n");
     }

     public void ppprint(String str)
     {
         try {
             wrwrite(str);
         }
         catch (SchRunTimeException e) {}
     }


     public void println()
     throws SchRunTimeException
     {
         dspmode=false;
         wrwrite("\n");
         skip=false;
         flush();
     }

     public void writeln(int cell)
     throws SchRunTimeException
     {
         prcntr = 0;
         dspmode=false;
         this.print(cell); 
         wrwrite("\n");
         skip=false;
         flush();
     }

     public void write(int cell)
     throws SchRunTimeException
     {
         prcntr = 0;
         dspmode=false;
         this.print(cell);
         flush();
     }

     public void println(int cell)
     throws SchRunTimeException
     {
         prcntr = 0;
         dspmode=false;
         this.print(cell); 
         wrwrite("\n");
         skip=false;
         flush();
     }

     public void display(int cell)
     throws SchRunTimeException
     {
         prcntr = 0;
         dspmode=true;
         this.print(cell);
         skip=false;
         flush();
     }

     public void println(int cell, String msg)
     throws SchRunTimeException
     {
         prcntr = 0;
         wrwrite(msg);
         this.print(cell); 
         wrwrite("\n");
         skip=false;
         flush();
     }

     public void dump(int cell)
     throws SchRunTimeException
     {
         dmpenv=true;
         prcntr = 0;
         this.print(cell); 
         wrwrite("\n");
         skip=false;
         dmpenv=false;
         flush();
     }



}

