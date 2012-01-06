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
 * File: IOOps.java
 * Author:
 * Description:
 *
 * $Id: IOOps.java,v 1.4 2003/11/24 10:54:30 serpaggi Exp $
 */

package Sch;
import java.net.*;
import java.io.*;


/**
 *
 *  The Input/Output Operations
 *
 * Implements the following procedures
 * <UL>
 * <LI><B>write</B> 
 * <LI><B>read</B> 
 * <LI><B>display</B> 
 * <LI><B>newline</B> 
 * <LI><B>read-char</B> 
 * <LI><B>read-token</B> 
 * <LI><B>peak-char</B> 
 * </UL>
 *
 *     @author Jean-Jacques Girardot
 */


public class IOOps extends SchPrimitive {

    private static IOOps proto;

    public static final int OPWRITE = 1;
    public static final int OPDUMP = 3;
    public static final int OPCELL = 4;
    public static final int OPCELLADD = 5;
    public static final int OPREAD = 6;
    public static final int OPGETNDO = 7;
    public static final int OPDISP = 8;
    public static final int OPNEWL = 10;
    public static final int OPREADCH = 11;
    public static final int OPREADTK = 12;
    public static final int OPREADLI = 13;
    public static final int OPPEAKCH = 14;
    public static final int OPCHREADY = 15;
    public static final int OPEOFOBJQ = 16;
    public static final int OPCURINPP = 17;
    public static final int OPCUROUTP = 18;
    public static final int OPPORT = 19;
    public static final int OPIPORT = 20;
    public static final int OPOPORT = 21;
    public static final int OPCDUMP = 22;
    public static final int OPOPINPSTR = 23;
    public static final int OPOPOUTSTR = 24;
    public static final int OPCLINPP = 25;
    public static final int OPCLOUTP = 26;
    public static final int OPGETPSTR = 27;
    public static final int OPOPINPURL = 28;
    public static final int OPOPINPFIL = 29;
    public static final int OPOPOUTFIL = 30;
    public static final int OPOPINPSINK = 31;
    public static final int OPOPOUTSINK = 32;

    public static final int NBOP = OPOPOUTSINK+1;

    public static void dcl()
    {
        proto = new IOOps();
        proto.fnames = new String[NBOP];

        proto.fnames[OPWRITE] = "write";
        proto.fnames[OPDISP] = "display";
        proto.fnames[OPDUMP] = "dump";
        proto.fnames[OPCELL] = "cell";
        proto.fnames[OPCELLADD] = "cell-address";
        proto.fnames[OPCDUMP] = "cell-dump";
        proto.fnames[OPREAD] = "read";
        proto.fnames[OPREADCH] = "read-char";
        proto.fnames[OPREADTK] = "read-token";
        proto.fnames[OPPEAKCH] = "peak-char";
        proto.fnames[OPCHREADY] = "char-ready?";
        proto.fnames[OPEOFOBJQ] = "eof-object?";
        proto.fnames[OPREADLI] = "read-line";
        proto.fnames[OPGETNDO] = "the-non-displayable-object";
        proto.fnames[OPNEWL] = "newline";
        proto.fnames[OPCURINPP] = "current-input-port";
        proto.fnames[OPCUROUTP] = "current-output-port";
        proto.fnames[OPPORT] = "port?";
        proto.fnames[OPIPORT] = "input-port?";
        proto.fnames[OPOPORT] = "output-port?";
        proto.fnames[OPOPINPSTR] = "open-input-string";
        proto.fnames[OPOPOUTSTR] = "open-output-string";
        proto.fnames[OPCLINPP] = "close-input-port";
        proto.fnames[OPCLOUTP] = "close-output-port";
        proto.fnames[OPGETPSTR] = "get-port-string";
        proto.fnames[OPOPINPURL] = "open-URL";
        proto.fnames[OPOPINPFIL] = "open-input-file";
        proto.fnames[OPOPOUTFIL] = "open-output-file";
        proto.fnames[OPOPINPSINK] = "open-input-sink";
        proto.fnames[OPOPOUTSINK] = "open-output-sink";

        Environment.dcl(proto);
    }

    public void eval(int op, int count, Evaluator evl)
    throws SchRunTimeException
    {
        int cell, res;
        switch (op)
        {

        case OPWRITE :
           {
               int port = CurOutP;
               if (count == 1)
               {
                   cell = evl.pop();
               }
               else
               {
                   if (count != 2)
                       throw new SchRunTimeException(ERRWAC);
                   cell = evl.pop();
                   port = evl.pop();
               }
               if ((Gdm.celltyp[port] != TPORT) ||
                    ((Gdm.cellcar[port] & PSTOUT) != PSTOUT))
                    throw new SchRunTimeException(ERRDOM);
               SchPrinter sp = (SchPrinter) Gdm.cellobj[port];
               evl.push(cell);
               if (cell != CNDO) sp.write(cell);
           }
           break;


        case OPDISP :
           {
               int port = CurOutP;
               if (count == 1)
               {
                   cell = evl.pop();
               }
               else
               {
                   if (count != 2)
                       throw new SchRunTimeException(ERRWAC);
                   cell = evl.pop();
                   port = evl.pop();
               }
               if ((Gdm.celltyp[port] != TPORT) ||
                    ((Gdm.cellcar[port] & PSTOUT) != PSTOUT))
                    throw new SchRunTimeException(ERRDOM);
               SchPrinter sp = (SchPrinter) Gdm.cellobj[port];
               evl.push(cell);
               if (cell != CNDO) sp.display(cell);
           }
           break;


        case OPCELL :
           {
              int num;
              if (count != 1)
                 throw new SchRunTimeException(ERR1AR);
              num = evl.unstackint();
              if (num < 0 || num >= Gdm.cellcount)
                  num = 0;
              evl.push(num);
           }
           break;


        case OPCDUMP :
           {
              int start, end;
              long add;
              if (count < 1 | count > 2)
                 throw new SchRunTimeException(ERR2AR);
              start = evl.unstackint();
              if (count == 2)
                  end = evl.unstackint();
              else
                  end = start;
              if (start < 0) start = 0;
              if (end < 0) end = 0;
              if (end >= Gdm.cellcount) end = Gdm.cellcount;
              if (start > end) start = end;
              for (int i = start; i <= end; i++)
              {
                 str.println(" " + 
                  Integer.toHexString(i)
                  +":"+Integer.toHexString(Gdm.celltyp[i])
                  +":"+Integer.toHexString(Gdm.cellcar[i])
                  +":"+Integer.toHexString(Gdm.cellcdr[i])
                  +":"+(Gdm.cellobj[i] == null ?
                    "()" : Gdm.cellobj[i].toString()));
              }
              evl.push(CNULL);
           }
           break;


        case OPCELLADD :
           {
              int num;
              if (count != 1)
                 throw new SchRunTimeException(ERR1AR);
              num = evl.pop();
              evl.push(Gdm.newint(num));
           }
           break;


        case OPGETNDO :
           if (count != 0)
               throw new SchRunTimeException(ERR0AR);
           evl.push(CNDO);
           break;


        case OPDUMP :
           cell = CNULL;
           while (count > 0)
           {
              cell = evl.pop();
              count--;
              spr.dump(cell);
           }
           evl.push(cell);
           break;


        case OPREAD :
           {
               if (count == 0)
               {
                   evl.push(CurInpP);
               }
               else
               {
                   if (count != 1)
                       throw new SchRunTimeException(ERRWAC);
               }
               cell = evl.valstk[evl.ptvalstk-1];
               if ((Gdm.celltyp[cell] != TPORT) ||
                    ((Gdm.cellcar[cell] & PSTIN) != PSTIN))
                    throw new SchRunTimeException(ERRDOM);
               SchReader scr = (SchReader) Gdm.cellobj[cell];
               evl.valstk[evl.ptvalstk-1] = scr.read();
           }
           break;


        case OPREADTK :
           {
               if (count == 0)
               {
                   evl.push(CurInpP);
               }
               else
               {
                   if (count != 1)
                       throw new SchRunTimeException(ERRWAC);
               }
               cell = evl.valstk[evl.ptvalstk-1];
               if ((Gdm.celltyp[cell] != TPORT) ||
                    ((Gdm.cellcar[cell] & PSTIN) != PSTIN))
                    throw new SchRunTimeException(ERRDOM);
               SchReader scr = (SchReader) Gdm.cellobj[cell];
               evl.valstk[evl.ptvalstk-1] = scr.readToken();
           }
           break;


        case OPREADLI :
           {
               if (count == 0)
               {
                   evl.push(CurInpP);
               }
               else
               {
                   if (count != 1)
                       throw new SchRunTimeException(ERRWAC);
               }
               cell = evl.valstk[evl.ptvalstk-1];
               if ((Gdm.celltyp[cell] != TPORT) ||
                    ((Gdm.cellcar[cell] & PSTIN) != PSTIN))
                    throw new SchRunTimeException(ERRDOM);
               SchReader scr = (SchReader) Gdm.cellobj[cell];
               evl.valstk[evl.ptvalstk-1] = scr.readLine();
           }
           break;


        case OPREADCH :
           {
               if (count == 0)
               {
                   evl.push(CurInpP);
               }
               else
               {
                   if (count != 1)
                       throw new SchRunTimeException(ERRWAC);
               }
               cell = evl.valstk[evl.ptvalstk-1];
               if ((Gdm.celltyp[cell] != TPORT) ||
                    ((Gdm.cellcar[cell] & PSTIN) != PSTIN))
                    throw new SchRunTimeException(ERRDOM);
               SchReader scr = (SchReader) Gdm.cellobj[cell];
               evl.valstk[evl.ptvalstk-1] = scr.readCh();
           }
           break;


        case OPPEAKCH :
           {
               if (count == 0)
               {
                   evl.push(CurInpP);
               }
               else
               {
                   if (count != 1)
                       throw new SchRunTimeException(ERRWAC);
               }
               cell = evl.valstk[evl.ptvalstk-1];
               if ((Gdm.celltyp[cell] != TPORT) ||
                    ((Gdm.cellcar[cell] & PSTIN) != PSTIN))
                    throw new SchRunTimeException(ERRDOM);
               SchReader scr = (SchReader) Gdm.cellobj[cell];
               evl.valstk[evl.ptvalstk-1] = scr.peakCh();
           }
           break;


        case OPCHREADY :
           {
               if (count == 0)
               {
                   evl.push(CurInpP);
               }
               else
               {
                   if (count != 1)
                       throw new SchRunTimeException(ERRWAC);
               }
               cell = evl.valstk[evl.ptvalstk-1];
               if ((Gdm.celltyp[cell] != TPORT) ||
                    ((Gdm.cellcar[cell] & PSTIN) != PSTIN))
                    throw new SchRunTimeException(ERRDOM);
               SchReader scr = (SchReader) Gdm.cellobj[cell];
               evl.valstk[evl.ptvalstk-1] = scr.ready() ? CTRUE : CFALSE;
           }
           break;


        case OPEOFOBJQ :
           if (count != 1)
               throw new SchRunTimeException(ERR1AR);
           cell = evl.valstk[evl.ptvalstk-1];
           evl.valstk[evl.ptvalstk-1] = cell == CEOF ? CTRUE : CFALSE;
           break;


        case OPNEWL :
           {
               int port = CurOutP;
               if (count == 1)
               {
                   port = evl.pop();
               }
               else
               {
                   if (count != 0)
                       throw new SchRunTimeException(ERRWAC);
               }
               if ((Gdm.celltyp[port] != TPORT) ||
                    ((Gdm.cellcar[port] & PSTOUT) != PSTOUT))
                    throw new SchRunTimeException(ERRDOM);
               SchPrinter sp = (SchPrinter) Gdm.cellobj[port];
               sp.println();
               // A result, because one is needed
               evl.push(CTRUE);
           }
           break;


        case OPCURINPP :
           if (count != 0)
               throw new SchRunTimeException(ERRWAC);
           evl.push(CurInpP);
           break;


        case OPCUROUTP :
           if (count != 0)
               throw new SchRunTimeException(ERRWAC);
           evl.push(CurOutP);
           break;


        case OPPORT :
           if (count != 1)
               throw new SchRunTimeException(ERRWAC);
           cell = evl.pop();
           res = Gdm.celltyp[cell] == TPORT ? CTRUE : CFALSE;
           evl.push(res);
           break;


        case OPIPORT :
           {
               if (count != 1)
                   throw new SchRunTimeException(ERRWAC);
               cell = evl.pop();
               int x = Gdm.cellcar[cell];
               res = ((Gdm.celltyp[cell] == TPORT) &&
                         ((x == PSTIN) || (x == PSTIO))    )
                             ? CTRUE : CFALSE;
               evl.push(res);
           }
           break;


        case OPOPORT :
           {
               if (count != 1)
                   throw new SchRunTimeException(ERRWAC);
               cell = evl.pop();
               int x = Gdm.cellcar[cell];
               res = ((Gdm.celltyp[cell] == TPORT) &&
                         ((x == PSTOUT) || (x == PSTIO))    )
                             ? CTRUE : CFALSE;
               evl.push(res);
           }
           break;


        case OPOPINPSTR :
           {
               if (count != 1)
                   throw new SchRunTimeException(ERRWAC);
               cell = evl.valstk[evl.ptvalstk-1];
               if (Gdm.celltyp[cell] != TSTR)
                    throw new SchRunTimeException(ERRDOM);
               int w = PSTIN;
               SchReader s = new SchReader(w,(String)Gdm.cellobj[cell]);
               res = Gdm.newcons(TPORT);
               Gdm.cellcar[res] = w;
               Gdm.cellobj[res] = s;
               evl.valstk[evl.ptvalstk-1] = res;
           }
           break;


        case OPOPOUTSTR :
           {
               if (count != 0)
                   throw new SchRunTimeException(ERRWAC);
               SchPrinter w = new SchPrinter(new java.io.StringWriter());
               res = Gdm.newcons(TPORT);
               Gdm.cellcar[res] = PSTOUT | PSTSTRING;
               Gdm.cellobj[res] = w;
               evl.push(res);
           }
           break;


        case OPCLINPP :
           {
               if (count != 1)
                   throw new SchRunTimeException(ERRWAC);
               cell = evl.valstk[evl.ptvalstk-1];
               int x = Gdm.cellcar[cell];
               if ((Gdm.celltyp[cell] != TPORT) || ((x & PSTIN) != PSTIN))
                    throw new SchRunTimeException(ERRDOM);
               SchReader s = (SchReader)Gdm.cellobj[cell];
               s.close(x);
           }
           break;


        case OPCLOUTP :
           {
               if (count != 1)
                   throw new SchRunTimeException(ERRWAC);
               cell = evl.valstk[evl.ptvalstk-1];
               int x = Gdm.cellcar[cell];
               if ((Gdm.celltyp[cell] != TPORT) || ((x & PSTOUT) != PSTOUT))
                    throw new SchRunTimeException(ERRDOM);
               SchPrinter p = (SchPrinter)Gdm.cellobj[cell];
               p.close();
           }
           break;


        case OPGETPSTR :
           {
               if (count != 1)
                   throw new SchRunTimeException(ERRWAC);
               cell = evl.valstk[evl.ptvalstk-1];
               int mask = PSTOUT | PSTSTRING ;
               int x = Gdm.cellcar[cell];
               if ((Gdm.celltyp[cell] != TPORT) || ((x & mask) != mask))
                    throw new SchRunTimeException(ERRDOM);
               SchPrinter w = (SchPrinter)Gdm.cellobj[cell];
               String s = w.wr.toString();
               res = Gdm.newcons(TSTR);
               Gdm.cellobj[res] = s;
               evl.valstk[evl.ptvalstk-1] = res;
           }
           break;


        case OPOPINPURL :
           {
               if (count != 1)
                   throw new SchRunTimeException(ERRWAC);
               cell = evl.valstk[evl.ptvalstk-1];
               if (Gdm.celltyp[cell] != TSTR)
                    throw new SchRunTimeException(ERRDOM);
               try {
                   URL u=new URL((String)Gdm.cellobj[cell]);
                   InputStream i=u.openStream();
                   int w = PSTIN|PSTISR;
                   SchReader s = new SchReader(w,i);
                   res = Gdm.newcons(TPORT);
                   Gdm.cellcar[res] = w;
                   Gdm.cellobj[res] = s;
               } catch (Exception e) {
                   throw new SchRunTimeException(ERROPF);
               }
               evl.valstk[evl.ptvalstk-1] = res;
           }
           break;


        case OPOPINPFIL :
           {
               if (count != 1)
                   throw new SchRunTimeException(ERRWAC);
               cell = evl.valstk[evl.ptvalstk-1];
               if (Gdm.celltyp[cell] != TSTR)
                    throw new SchRunTimeException(ERRDOM);
               try {
                   FileInputStream i = new
                      FileInputStream((String)Gdm.cellobj[cell]);
                   int w = PSTIN|PSTISR;
                   SchReader s = new SchReader(w,i);
                   res = Gdm.newcons(TPORT);
                   Gdm.cellcar[res] = w;
                   Gdm.cellobj[res] = s;
               } catch (Exception e) {
                   throw new SchRunTimeException(ERROPF);
               }
               evl.valstk[evl.ptvalstk-1] = res;
           }
           break;


        case OPOPOUTFIL :
           {
               if (count != 1)
                   throw new SchRunTimeException(ERRWAC);
               cell = evl.valstk[evl.ptvalstk-1];
               if (Gdm.celltyp[cell] != TSTR)
                    throw new SchRunTimeException(ERRDOM);
               try {
                   FileOutputStream o = new
                      FileOutputStream((String)Gdm.cellobj[cell]);
                   SchPrinter p = new SchPrinter(o);
                   res = Gdm.newcons(TPORT);
                   Gdm.cellcar[res] = PSTOUT|PSTISR;
                   Gdm.cellobj[res] = p;
               } catch (Exception e) {
                   throw new SchRunTimeException(ERROPF);
               }
               evl.valstk[evl.ptvalstk-1] = res;
           }
           break;


        case OPOPINPSINK :
           {
               if (count != 0)
                   throw new SchRunTimeException(ERRWAC);
               try {
                   int w = PSTIN|PSTFCLOSE;
                   SchReader p = new SchReader(w,new SinkReader());
                   res = Gdm.newcons(TPORT);
                   Gdm.cellcar[res] = w;
                   Gdm.cellobj[res] = p;
               } catch (Exception e) {
                   throw new SchRunTimeException(ERROPF);
               }
               evl.push(res);
           }
           break;


        case OPOPOUTSINK :
           {
               if (count != 0)
                   throw new SchRunTimeException(ERRWAC);
               try {
                   SchPrinter p = new SchPrinter(new SinkWriter());
                   res = Gdm.newcons(TPORT);
                   Gdm.cellcar[res] = PSTOUT|PSTFCLOSE;
                   Gdm.cellobj[res] = p;
               } catch (Exception e) {
                   throw new SchRunTimeException(ERROPF);
               }
               evl.push(res);
           }
           break;


        default :
           throw new SchRunTimeException(ERRUSP);
        }
    }
}

