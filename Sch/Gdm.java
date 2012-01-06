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
 * File: Gdm.java
 * Author:
 * Description:
 *
 * $Id: Gdm.java,v 1.6 2007/02/20 16:03:41 collins Exp $
 */

package Sch;


import java.util.*;

/**
 *
 *    The Memory Management
 *
 *
 *     @author Jean-Jacques Girardot
 */

public class Gdm extends Globaldata {

    private static final byte FU=-128;
    private static final byte FM= 127;

    private static int p0, p1, p2, p3, p4, p5, p6, p7;
    private static int q0, q1, q2, q3, q4, q5, q6, q7;

    public static long cucount;
    public static long gccount;
    public static int cellcount;

    public static byte [] celltyp;
    public static int [] cellcar;
    public static int [] cellcdr;
    public static Object [] cellobj;
    public static int cellfree;

    public static int cellpro;
    public static final int PTMKCMAX = 256;
    public static int [] mkc;
    public static int ptmkc;
    public static int ptmkcmax;


    private static void dclkwd(String id, int num)
    {
       String sym=id;
       Symbols.set(sym, num);
       celltyp[num]=TSYM;
       cellobj[num]=sym;
    }

    private static void dclbc(int num)
    {
       celltyp[num]=TCTRL;
    }

    private static void dclspec(int num)
    {
       celltyp[num]=TSPEC;
       cellcar[num]=num;
    }

    static {
       int i;

       cellcount=KSGDM;
       cellfree= -1;
       gccount=0;
       celltyp = new byte[KSGDM];
       cellcar = new int[KSGDM];
       cellcdr = new int[KSGDM];
       cellobj = new Object[KSGDM];

       for (i=0; i<KSGDM; i++) {
          cellcar[i]=CNULL;
          cellcdr[i]=CNULL;
          celltyp[i]=TNULL;
          cellobj[i]=null;
       }

       // Objet "null"
       // entr�e CNULL
       // Objet "undef"
       celltyp[CUNDEF]=TUNDEF;
       // Objet "false"
       celltyp[CFALSE]=TBOOL;
       // Objet "true"
       celltyp[CTRUE]=TBOOL;
       //
       // Objet "eof"
       dclspec(CEOF);
       // Objet "open parenthesis"
       dclspec(CTOP);
       // Objet "close parenthesis"
       dclspec(CTCP);
       // Objet "dot"
       dclspec(CTDOT);
       // Objet "quote"
       dclspec(CTQUOT);
       // Objet "backquote"
       dclspec(CTBQUOT);
       // Objet "comma"
       dclspec(CTCOMMA);
       // Objet "comma-at"
       dclspec(CTCOMAT);
       // Objet "read-error"
       dclspec(CTERR);
       // Objet "non displaying object"
       dclspec(CNDO);
       //
       // Objet "quote" 
       dclkwd("quote",CQUOTE);
       // Objet "quasiquote" 
       dclkwd("quasiquote",CQQUOTE);
       // Objet "define" 
       dclkwd("define",CDEFINE);
       // Objet "if" 
       dclkwd("if",CIF);
       // Objet "begin"
       dclkwd("begin",CBEGIN);
       // Objet "set!"
       dclkwd("set!",CSET);
       // Objet "lambda"
       dclkwd("lambda",CLAMBDA);
       // Objet "and"
       dclkwd("and",CAND);
       // Objet "or"
       dclkwd("or",COR);
       // Objet "case"
       dclkwd("case",CCASE);
       // Objet "cond"
       dclkwd("cond",CCOND);
       // Objet "delay"
       dclkwd("delay",CDELAY);
       // Objet "do"
       dclkwd("do",CDO);
       // Objet "else"
       dclkwd("else",CELSE);
       // Objet "let"
       dclkwd("let",CLET);
       // Objet "let*"
       dclkwd("let*",CLETE);
       // Objet "letrec"
       dclkwd("letrec",CLETR);
       // Objet "fluid-let"
       dclkwd("fluid-let",CFLLET);
       // Objet "unquote"
       dclkwd("unquote",CUQUOTE);
       // Objet "unquote-splicing"
       dclkwd("unquote-splicing",CUQUOTES);
       // Objet "=>"
       dclkwd("=>",CAPPLY);
       //
       // Bytes Codes
       dclbc(BCENDX);
       dclbc(BCF00);
       dclbc(BCF01);
       dclbc(BCF02);
       dclbc(BCF03);
       dclbc(BCF04);
       dclbc(BCF05);
       dclbc(BCF06);
       dclbc(BCF07);
       dclbc(BCF08);
       dclbc(BCFnn);
       dclbc(BCSET);
       dclbc(BCSETU);
       dclbc(BCDEF);
       dclbc(BCLDENV);
       dclbc(BCPOPV);
       dclbc(BCIF);
       dclbc(BCCOND);
       dclbc(BCCASE);
       dclbc(BCNOP);
       dclbc(BCFALSE);
       dclbc(BCOR);
       dclbc(BCAND);
       dclbc(BCQUOTE);
       dclbc(BCLMBDA);
       dclbc(BCHALT);
       dclbc(BCDELAY);
       dclbc(BCDEL2);
       dclbc(BCLET);
       dclbc(BCLETREC);
       dclbc(BCADENV);
       dclbc(BCDWBFR);
       dclbc(BCDWACT);
       dclbc(BCDWAFT);
       dclbc(BCDWDAT);
       dclbc(BCMAP);
       dclbc(BCYIEL);
       dclbc(BCWAIT);
       dclbc(BCTRY1);
       dclbc(BCTRY2);
       // Empty environment
       celltyp[Cemptyenv]=TENV;
       // Glob. envir.
       celltyp[Csysenv]=TENV;
       cellcdr[Csysenv]=Cemptyenv;
       // User environment
       celltyp[Cusrenv]=TENV;
       cellcdr[Cusrenv]=Csysenv;
       // Atom-list
       celltyp[Catms]=TPAIR;
       // Evaluator list
       celltyp[Cxq]=TPAIR;
       celltyp[Cwq]=TPAIR;

       for (i=KSGDM-1; i>=CFREE; i--) {
           free(i);
       }

       cucount = CFREE;
       p0 = p1 = p2 = p3 = p4 = p5 = p6 = p7 = 0;
       q0 = q1 = q2 = q3 = q4 = q5 = q6 = q7 = 0;
       cellpro = 0;
       //  str.println("Gdm Initialized");
       // System.err.println("Gdm Initialized");

       mkc = new int[PTMKCMAX+4];
       ptmkc = 0;
       ptmkcmax = PTMKCMAX;
    }


    public static void dmp(int cell)
    {
       if (cell<0 || cell>cellcount)
       {
           str.ppprint("Gdm.dmp : # out of bounds : "+cell+"\n");
       }
       else
       {
           str.ppprint("Gdm.dmp : #"+cell+"= <"+
           celltyp[cell]+":"+cellcar[cell]+":"+cellcdr[cell]+">\n");
       }
    }


    public static void increasemkc()
    {
       int [] newmkc;
       int newptmkcmax = ptmkcmax + (ptmkcmax>>2);
       newmkc = new int[newptmkcmax+4];
       for (int i=0; i<= ptmkc; i++)
           newmkc[i] = mkc[i];
       mkc = newmkc;
       ptmkcmax = newptmkcmax;
       newmkc = null;
       // str.ppprint("Mark : increasing stack to "+newptmkcmax+"\n");
    }

    public static void mark(int [] tab, int c)
    throws SchRunTimeException
    {
        int n;
        for (int i=0; i<c; i++)
        {
            n = tab[i];
            if (n>cellcount)
               throw new SchRunTimeException(ERRMFL);
            if (n<CLAST)
            {
               if (n<0)
                   throw new SchRunTimeException(ERRMFL);
            }
            else
            {
                if ((celltyp[n] & FU) == 0)
                {
                    mkc[ptmkc++] = n;
                    if (ptmkc >= ptmkcmax)
                    {
                        increasemkc();
                    }
                }
            }
        }
    }

    public static void mark(int n)
    throws SchRunTimeException
    {
        if (n>cellcount)
           throw new SchRunTimeException(ERRMFL);
        if (n<CLAST)
        {
           if (n<0)
               throw new SchRunTimeException(ERRMFL);
           return;
        }
        if ((celltyp[n] & FU) == 0)
        {
            mkc[ptmkc++] = n;
            if (ptmkc >= ptmkcmax)
            {
                increasemkc();
            }
        }
    }

    public static void sweep()
    throws SchRunTimeException
    {
       int n, k;
       while (ptmkc > 0)
       {
          n = mkc[--ptmkc];
          //str.print("Mark : "+n+"   ");
/*
          if (n<0 || n>cellcount)
             throw new SchRunTimeException(ERRMFL);
*/
          if ((celltyp[n] & FU) == 0)
          {
            int ty=celltyp[n];
            celltyp[n]|=FU;
            switch(ty)
            {
            case TPAIR :
            case TENV :
            case TVC :
            case TLAMBDA :
            case TCELL :
            case TBARR :
                k = cellcdr[n];
                if ((celltyp[k] & FU) == 0)
                    mkc[ptmkc++] = k;
                k = cellcar[n];
                if ((celltyp[k] & FU) == 0)
                    mkc[ptmkc++] = k;
                if (ptmkc >= ptmkcmax)
                {
                   increasemkc();
                }
                break;
            case TLOCK :
            case TDELAY :
                {
                    mkc[ptmkc++] = cellcdr[n];
                    if (ptmkc >= ptmkcmax)
                    {
                       increasemkc();
                    }
                }
                break;
            case TTHRD :
            case TKONT :
                {
                    Object x=cellobj[n];
                    if (x instanceof Evaluator)
                    {
                        ((Evaluator)x).mark();
                    }
                }
                break;
            case TFREE :
                throw new SchRunTimeException(ERRMFL);
            }
         }
      }
    }

    public static int gc()
    throws SchRunTimeException
    {
       int i, nc;

       gccount++;
       //
       // Les premieres cellules sont indispensables
       for (i=0; i<CLAST; i++)
          celltyp[i]|=FU;

       // On marque ce qui doit l'etre
       ptmkc=0;
       for (i=CLAST; i<CFREE; i++)
           mkc[ptmkc++] = i;
       mkc[ptmkc++] = cellpro;
       mkc[ptmkc++] = p0; mkc[ptmkc++] = p1;
       mkc[ptmkc++] = p2; mkc[ptmkc++] = p3;
       mkc[ptmkc++] = p4; mkc[ptmkc++] = p5;
       mkc[ptmkc++] = p6; mkc[ptmkc++] = p7;
       mkc[ptmkc++] = q0; mkc[ptmkc++] = q1;
       mkc[ptmkc++] = q2; mkc[ptmkc++] = q3;
       mkc[ptmkc++] = q4; mkc[ptmkc++] = q5;
       mkc[ptmkc++] = q6; mkc[ptmkc++] = q7;

       // On lance l'analyse
       sweep();

       nc = 0;
       cellfree = -1;
       for (i=cellcount-1; i>=0; i--)
       {
           if ((celltyp[i] & FU) != 0)
           {
               celltyp[i] &= FM;
           }
           else
           {
               celltyp[i]=TFREE;
               cellcdr[i]=cellfree;
               cellcar[i]=CNULL;
               cellobj[i]=null;
               cellfree = i;
               nc++;
           }
       }
/*
       // DEBUG
       str.println("GC : "+nc+" cells collected out of "
           +cellcount);
*/
       if ((debug & 4) != 0)
           str.println("*GC : "+nc+" cells collected out of "
               +cellcount);
       if (nc < (cellcount>>3))
       {
           int newcar[];
           int newcdr[];
           Object newobj [];
           byte newbyt [];
           int newcount;
           // Nouvelle taille
           newcount = (nc < (cellcount>>4)) ?
                cellcount + (cellcount>>2) :
                cellcount + (cellcount>>3);
           if (newcount > KSMGDM)
               throw new SchRunTimeException(ERRNEC);

           try {
               // We try to allocate all new arrays first
               // so we can free them in case of error...
               // Copie des objets
               newobj = new Object[newcount];
               newcar = new int[newcount];
               newcdr = new int[newcount];
               newbyt = new byte[newcount];
               // Now, everything should be ok
               for (i=0; i<cellcount; i++)
                   newobj[i]=cellobj[i];
               // Copie des car
               for (i=0; i<cellcount; i++)
                   newcar[i]=cellcar[i];
               // Copie des cdr
               for (i=0; i<cellcount; i++)
                   newcdr[i]=cellcdr[i];
               // Copie des types
               for (i=0; i<cellcount; i++)
                   newbyt[i]=celltyp[i];
               // Pointers can be changed
               cellobj = newobj;
               newobj = null;
               cellcar = newcar;
               newcar = null;
               cellcdr = newcdr;
               newcdr = null;
               celltyp = newbyt;
               newbyt = null;
               // Les nouvelles cellules sont lib�r�es
               for (i=newcount-1; i>=cellcount; i--)
               {
                   celltyp[i]=TFREE;
                   cellcdr[i]=cellfree;
                   cellcar[i]=CNULL;
                   cellobj[i]=null;
                   cellfree = i;
                   nc++;
               }
               cellcount = newcount;
           } catch (OutOfMemoryError e) {
               newobj = null;
               newcar = null;
               newcdr = null;
               newbyt = null;
               throw new SchRunTimeException(ERRNEM);
           }
       }
       return nc;
    }


    public static void free(int cell)
    {
        celltyp[cell]=TFREE;
        cellcdr[cell]=cellfree;
        cellcar[cell]=CNULL;
        cellobj[cell]=null;
        cellfree = cell;
    }

    public static void protect(int c)
    throws SchRunTimeException
    {
        q7=c;
        int np=newcons();
        cellcdr[np]=cellpro;
        cellcar[np]=c;
        cellpro=np;
    }

    public static void protect1(int c)
    {
        q7=q6; q6=q5; q5=q4; q4=q3;
        q3=q2; q2=q1; q1=q0; q0=c;
    }

    /**
       Procedure principale d'allocation de cellule
    */
    public static int newcons()
    throws SchRunTimeException
    {
        int cell=cellfree;
        if (cell <= 0)
        {
            gc();
            cell=cellfree;
            if (cell <= 0)
               throw new SchRunTimeException(ERRMFL);
        }
        cellfree=cellcdr[cell];
        cellcdr[cell]=CNULL;
        celltyp[cell]=TPAIR;
        cucount++;
        p7=p6; p6=p5; p5=p4; p4=p3;
        p3=p2; p2=p1; p1=p0; p0=cell;
// DEBUG
        if ((debug & DBGGDM) != 0)
               cellfree=-1;
        return cell;
    }

    public static int newcons(byte typ)
    throws SchRunTimeException
    {
        int cell=newcons();
        celltyp[cell]=typ;
        return cell;
    }

    public static int newcons(byte typ, int lecar, int lecdr)
    throws SchRunTimeException
    {
        int cell=newcons();
        celltyp[cell]=typ;
        cellcar[cell]=lecar;
        cellcdr[cell]=lecdr;
        return cell;
    }

    public static int newcons(Object obj)
    throws SchRunTimeException
    {
        int cell=newcons();
        celltyp[cell]=TJOBJ;
        cellobj[cell]=obj;
        return cell;
    }


    public static int newcons(Object obj, byte typ)
    throws SchRunTimeException
    {
        int cell=newcons();
        celltyp[cell]=typ;
        cellobj[cell]=obj;
        return cell;
    }

    public static int newcons(byte typ, int lecar, int lecdr, Object obj)
    throws SchRunTimeException
    {
        int cell=newcons();
        celltyp[cell]=typ;
        cellcar[cell]=lecar;
        cellcdr[cell]=lecdr;
        cellobj[cell]=obj;
        return cell;
    }

    public static int newstring(String obj)
    throws SchRunTimeException
    {
        int cell=newcons();
        celltyp[cell]=TSTR;
        cellobj[cell]=obj;
        return cell;
    }

    /*
        Create a new "protected" cell
    */
    public static int cons(int elt1, int elt2)
    throws SchRunTimeException
    {
        q4 = elt1; q5 = elt2;
        int np=newcons();
        cellcdr[np]=cellpro;
        cellpro=np;
        int cell=newcons();
        cellcar[cell]=elt1;
        cellcdr[cell]=elt2;
        cellcar[np]=cell;
        return cell;
    }

    /*
        Create a new "protected" 1 elt list
    */
    public static int list(int elt1)
    throws SchRunTimeException
    {
        q4 = elt1;
        int np=newcons();
        cellcdr[np]=cellpro;
        cellpro=np;
        int cell=newcons();
        cellcar[cell]=elt1;
        cellcar[np]=cell;
        return cell;
    }

    /*
        Create a new "protected" 2 elt list
    */
    public static int list(int elt1, int elt2)
    throws SchRunTimeException
    {
        q4 = elt1; q5 = elt2;
        int np=newcons();
        cellcdr[np]=cellpro;
        cellpro=np;
        int cell=newcons();
        cellcar[np]=cell;
        int n=newcons();
        cellcar[cell]=elt1;
        cellcdr[cell]=n;
        cellcar[n]=elt2;
        return cell;
    }

    /*
        Create a new "protected" 3 elt list
    */
    public static int list(int elt1, int elt2, int elt3)
    throws SchRunTimeException
    {
        q4 = elt1; q5 = elt2;
        q6 = elt3;
        int np=newcons();
        cellcdr[np]=cellpro;
        cellpro=np;
        int cell=newcons();
        cellcar[cell]=elt1;
        cellcar[np]=cell;
        int n=newcons();
        cellcdr[cell]=n;
        cellcar[n]=elt2;
        int p=newcons();
        cellcdr[n]=p;
        cellcar[p]=elt3;
        return cell;
    }

    /*
        Create a new "protected" 4 elt list
    */
    public static int list(int elt1, int elt2, int elt3, int elt4)
    throws SchRunTimeException
    {
        q4 = elt1; q5 = elt2;
        q6 = elt3; q7 = elt4;
        int np=newcons();
        cellcdr[np]=cellpro;
        cellpro=np;
        int cell=newcons();
        cellcar[np]=cell;
        cellcar[cell]=elt1;
        int n=newcons();
        cellcar[n]=elt2;
        cellcdr[cell]=n;
        int p=newcons();
        cellcar[p]=elt3;
        cellcdr[n]=p;
        int q=newcons();
        cellcdr[p]=q;
        cellcar[q]=elt4;
        return cell;
    }

    public static int typ(int cell)
    {
        return celltyp[cell];
    }

    public static int typcar(int cell)
    {
        return celltyp[cellcar[cell]];
    }

    public static int typcdr(int cell)
    {
        return celltyp[cellcdr[cell]];
    }

    public static int car(int cell)
    {
        return cellcar[cell];
    }

    public static int cdr(int cell)
    {
        return cellcdr[cell];
    }

    public static int cadr(int cell)
    {
        int w=cellcdr[cell];
        return cellcar[w];
    }

    public static int cddr(int cell)
    {
        int w=cellcdr[cell];
        return cellcdr[w];
    }

    public static int caddr(int cell)
    {
        int w=cellcdr[cell];
        w=cellcdr[w];
        return cellcar[w];
    }

    public static int cadddr(int cell)
    {
        int w=cellcdr[cell];
        w=cellcdr[w];
        w=cellcdr[w];
        return cellcar[w];
    }

    public static void setcar(int cell, int newcar)
    {
        cellcar[cell] = newcar;
    }

    public static void setcdr(int cell, int newcdr)
    {
        cellcdr[cell] = newcdr;
    }

    public static boolean isnil(int cell)
    {
        return cell<=0;
    }

    public static int newint(int value)
    throws SchRunTimeException
    {
        int cell=cellfree;
        if (cell <= 0)
        {
            cell=newcons();
        }
        else
        {
           cellfree=cellcdr[cell];
           cucount++;
           p7=p6; p6=p5; p5=p4; p4=p3;
           p3=p2; p2=p1; p1=p0; p0=cell;
// DEBUG
           if ((debug & DBGGDM) != 0)
               cellfree=-1;
        }
        // int cell=newcons();
        celltyp[cell]=TINT;
        cellcar[cell]=value;
        return cell;
    }

    public static int newchar(int value)
    throws SchRunTimeException
    {
        int cell=cellfree;
        if (cell <= 0)
        {
            cell=newcons();
        }
        else
        {
           cellfree=cellcdr[cell];
           cucount++;
           p7=p6; p6=p5; p5=p4; p4=p3;
           p3=p2; p2=p1; p1=p0; p0=cell;
           // DEBUG
           if ((debug & DBGGDM) != 0)
               cellfree=-1;
        }
        // int cell=newcons();
        celltyp[cell]=TCHAR;
        cellcar[cell]=value;
        return cell;
    }

    public static String strEdit(int num, int lng)
    {
        StringBuffer s = new StringBuffer("");
        s.append(num);
        while (s.length() < lng)
            s.insert(0," ");
        return s.toString();
    }

    public static String toString(int c)
    {
        StringBuffer s = new StringBuffer("");
        int ty;
        int va;
        int vd;
        if (c < 0 || c >= cellcount)
        {
            s.append("Out-of-bounds(");
            s.append(c);
            s.append(")");
            return s.toString();
        }
        ty = celltyp[c];
        va = cellcar[c];
        vd = cellcdr[c];
        switch(ty)
        {
        case TCTRL :
            s.append(cellname(c));
            break;
        case TNULL :
            s.append("()");
            break;
        case TINT :
            s.append(va);
            break;
        case TSYM :
            s.append((String)cellobj[c]);
            break;
        case TSUBR :
            s.append("Subr:");
            s.append(va);
            s.append(":");
            s.append(vd);
            break;
        case TMACRO :
            s.append("Macro:");
            s.append(vd);
            break;
        case TKONT :
            s.append("Cont:");
            s.append(c);
            break;
        case TTHRD :
            s.append("Thread:");
            s.append(c);
            break;
        case TVC :
            s.append("{");
            s.append((String)cellobj[va]);
            s.append("}");
            break;
        case TENV :
            s.append("{{");
            s.append(va);
            s.append(":");
            s.append(vd);
            s.append("}}");
            break;
        case TPAIR :
            {
               int x=c, y=0, p=0, mxc=10;
               s.append("(");
               while ((celltyp[x] == TPAIR) && (mxc > 0))
               {
                   if (y !=0) s.append(" ");
                   s.append(toString(cellcar[x]));
                   y=1;
                   x=cellcdr[x];
                   mxc--;
               }
               if ((celltyp[x] != TNULL) && (mxc > 0))
               {
                   s.append(". ");
                   s.append(toString(x));
               }
               s.append(")");
            }
            break;
        case TLAMBDA : 
            s.append("{lambda{");
            s.append(c);
            s.append("}}");
            break;
        default :
            {
                s.append("[");
                s.append(ty);
                s.append(":");
                s.append(c);
                Object obj=cellobj[c];
                if (obj != null)
                {
                    try {
                        s.append(":");
                        s.append(obj.toString());
                    } catch (Exception E) { }
                }
                s.append("]");
            }
            break;
        }
        return s.toString();
    }


    private static void dupcell(int src, int dst)
    throws SchRunTimeException
    {
         int car, cdr, newcar, newcdr;
         byte tycar, tycdr;
         boolean cy=true;
         while (cy) {
             car = cellcar[src];
             cdr = cellcdr[src];
             tycar = celltyp[car];
             tycdr = celltyp[cdr];
             newcar = -1;
             newcdr = -1;
             if (tycar != TPAIR)
             {
                 cellcar[dst]= newcar = car;
                 if (tycdr != TPAIR)
                 {
                     cellcdr[dst]= newcdr = cdr;
                     cy = false;
                 }
                 else
                 {
                     // Travailler sur les cdrs
                     newcdr = newcons(tycdr);
                     cellcdr[dst]=newcdr;
                     // On passe aux cdrs
                     src = cdr;
                     dst = newcdr;
                 }
             }
             else
             {
                 newcar = newcons(tycar);
                 cellcar[dst]=newcar;
                 if (tycdr != TPAIR)
                 {
                     cellcdr[dst]= newcdr = cdr;
                     // Travailler sur les cars
                     // On passe aux cars
                     src = car;
                     dst = newcar;
                 }
                 else
                 {
                     dupcell(car, newcar);
                     // Travailler sur les cdrs
                     newcdr = newcons(tycdr);
                     cellcdr[dst]=newcdr;
                     // On passe aux cdrs
                     src = cdr;
                     dst = newcdr;
                 }
             }
         }
    }


    public static void cellcpy(int dst, int src)
    {
        celltyp[dst] = celltyp[src];
        cellcar[dst] = cellcar[src];
        cellcdr[dst] = cellcdr[src];
        cellobj[dst] = cellobj[src];
    }


    public static int dupcell(int src)
    throws SchRunTimeException
    {
        int res = src;
        byte ty = celltyp[src];
        if (ty == TPAIR)
        {
            protect(src);
            res = newcons(ty);
            protect(res);
            dupcell(src, res);
        }
        return res;
    }


    public static String cellname(int cell)
    {
        String res;
        switch(cell)
        {
        case CNULL : res = "()"; break;
        case CUNDEF : res = "#<UNDEF>"; break;
        case CFALSE : res = "#f"; break;
        case CTRUE : res = "#t"; break;
        case CEOF : res = "#<EOF>"; break;
        case CTERR : res = "#<ERR>"; break;
        case CNDO : res = "#<UNDISP>"; break;
        case BCENDX : res = "[END]"; break;
        case BCFnn : res = "[Fn]"; break;
        case BCF00 : res = "[F0]"; break;
        case BCF01 : res = "[F1]"; break;
        case BCF02 : res = "[F2]"; break;
        case BCF03 : res = "[F3]"; break;
        case BCF04 : res = "[F4]"; break;
        case BCF05 : res = "[F5]"; break;
        case BCF06 : res = "[F6]"; break;
        case BCF07 : res = "[F7]"; break;
        case BCF08 : res = "[F8]"; break;
        case BCLMBDA : res = "[LAMBDA]"; break;
        case BCQUOTE : res = "[QUOTE]"; break;
        case BCSET : res = "[SET]"; break;
        case BCSETU : res = "[SETU]"; break;
        case BCDEF : res = "[DEFINE]"; break;
        case BCLDENV : res = "[ENV]"; break;
        case BCOR : res = "[OR]"; break;
        case BCAND : res = "[AND]"; break;
        case BCPOPV : res = "[POP]"; break;
        case BCIF : res = "[IF]"; break;
        case BCCOND : res = "[=>]"; break;
        case BCCASE : res = "[CASE]"; break;
        case BCNOP : res = "[NOP]"; break;
        case BCFALSE : res = "[FALSE]"; break;
        case BCDELAY : res = "[DELAY]"; break;
        case BCDEL2 : res = "[DELAY2]"; break;
        case BCLET : res = "[LET]"; break;
        case BCLETREC : res = "[LETREC]"; break;
        case BCADENV : res = "[ADDENV]"; break;
        case BCDWBFR : res = "[DYN-WIND-BFR]"; break;
        case BCDWACT : res = "[DYN-WIND-ACT]"; break;
        case BCDWAFT : res = "[DYN-WIND-AFT]"; break;
        case BCDWDAT : res = "[DYN-WIND-DAT]"; break;
        case BCTRY1 : res = "[TRY-1]"; break;
        case BCTRY2 : res = "[TRY-2]"; break;
        case BCWAIT : res = "[WAIT]"; break;
        case BCYIEL : res = "[YIELD]"; break;
        case BCMAP : res = "[MAP]"; break;
        default :
           if (cell < 0 || cell >= Gdm.cellcount)
           {
               res = "#<U>";
               break;
           }
           res = "#<CELL:" +
                   Gdm.celltyp[cell] + ":" +
                   Gdm.cellcar[cell] + ":" + Gdm.cellcdr[cell] + ">";
           break;
        }
        return res;
    }
    

    public static String celltype(int cell)
    {
        String res;
        int ty = celltyp[cell];
        switch(ty)
        {
         case TNULL :
             res = "null"; break;
         case TPAIR :
             res = "pair"; break;
         case TINT :
             res = "number"; break;
         case TCHAR :
             res = "character"; break;
         case TSTR :
             res = "string"; break;
         case TSYM :
             res = "symbol"; break;
         case TEOF :
             res = "end-of-file"; break;
         case TSPEC :
             switch (cell)
             {
             case CEOF : res = "end-of-file"; break;
             default :
                 res = "spec-"+Gdm.cellcar[cell]; break;
             }
             break;
         case TSUBR :
             res = "subr"; break;
         case TBOOL :
             res = "boolean"; break;
         case TLAMBDA :
             res = "expr"; break;
         case TKONT :
             res = "continuation"; break;
         case TENV :
             res = "environment"; break;
         case TPORT :
             res = "port"; break;
         case TVC :
             res = "binding"; break;
         case TUNDEF :
             res = "undefined"; break;
         case TCTRL :
             res = "control"; break;
         case TTHRD :
             res = "thread"; break;
         case TDELAY :
             res = "delay"; break;
         case TCOMM :
             res = "comment"; break;
         case TLOCK :
             res = "lock"; break;
         case TERR :
             res = "error"; break;
         case TJMETH :
         case TJOBJ :
             {
                 Object o = Gdm.cellobj[cell];
                 res = "java:" + ((o.getClass()).getName());
             }
             break;
         default :
             res = "type-"+ty; break;
        }
        return res;
    }
    

    public static String celltostring(int cell)
    {
        String res;
        int ty = celltyp[cell];
        switch(ty)
        {
        case TSTR :
            res = (String)Gdm.cellobj[cell];
            break;
        case TINT :
            res = "" + Gdm.cellcar[cell];
            break;
        case TCHAR :
            {
                char [] c = new char[1];
                c[0] = (char)Gdm.cellcar[cell];
                res = new String(c);
            }
            break;
        case TSYM :
            res = (String)Gdm.cellobj[cell];
            break;
        default :
            try {
                Object o = Gdm.cellobj[cell];
                res = o.toString();
                break;
            } catch (Exception e) { }
            res = cellname(cell);
        }
        return res;
    }
    
}

