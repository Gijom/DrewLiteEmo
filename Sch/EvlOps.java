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
 * File: EvlOps.java
 * Author:
 * Description:
 *
 * $Id: EvlOps.java,v 1.4 2003/11/24 10:54:30 serpaggi Exp $
 */

package Sch;


/**
 *
 *  Thread related procedures
 *
 *
 *     @author Jean-Jacques Girardot
 */

public class EvlOps extends SchPrimitive {

    private static EvlOps proto;

    // Threads
    public static final int OPMAKTHR = 1;
    public static final int OPLOADTHR = 2;
    public static final int OPRUNTHR = 3;
    public static final int OPTHRSTAT = 4;
    public static final int OPTHRV = 5;
    public static final int OPTHRN = 6;
    public static final int OPTHRI = 7;
    public static final int OPTHRQ = 8;
    public static final int OPCTHR = 9;
    // Locks
    public static final int OPMAKLCK = 10;
    public static final int OPLOCKQ = 11;
    public static final int OPOBTLCK = 12;
    public static final int OPRELLCK = 13;
    public static final int OPLOCKVAL = 14;
    // Cells
    public static final int OPMAKCLL = 15;
    public static final int OPCELLQ = 16;
    public static final int OPCELLVAL = 17;
    public static final int OPCELLSET = 18;
    public static final int OPCELLVAV = 19;
    // Barriers
    public static final int OPMAKBAR = 20;
    public static final int OPBARQ = 21;
    public static final int OPBARW = 22;

    public static final int NBOP = OPBARW+1;

    public static void dcl()
    {
        proto = new EvlOps();
        proto.fnames = new String[NBOP];

        // Threads
        proto.fnames[OPMAKTHR] = "make-thread";
        proto.fnames[OPLOADTHR] = "load-thread!";
        proto.fnames[OPRUNTHR] = "run-thread";
        proto.fnames[OPTHRSTAT] = "thread-status";
        proto.fnames[OPTHRV] = "thread-value";
        proto.fnames[OPTHRQ] = "thread?";
        proto.fnames[OPTHRI] = "thread-id";
        proto.fnames[OPTHRN] = "thread-name";
        proto.fnames[OPCTHR] = "current-thread";
        // Locks
        proto.fnames[OPLOCKQ] = "lock?";
        proto.fnames[OPMAKLCK] = "make-lock";
        proto.fnames[OPOBTLCK] = "obtain-lock";
        proto.fnames[OPRELLCK] = "release-lock";
        proto.fnames[OPLOCKVAL] = "lock-value";
        // Cells
        proto.fnames[OPCELLQ] = "cell?";
        proto.fnames[OPMAKCLL] = "make-cell";
        proto.fnames[OPCELLVAL] = "cell-value";
        proto.fnames[OPCELLSET] = "cell-set!";
        proto.fnames[OPCELLVAV] = "cell-value-available?";
        // Barriers
        proto.fnames[OPBARQ] = "barrier?";
        proto.fnames[OPMAKBAR] = "make-barrier";
        proto.fnames[OPBARW] = "barrier-wait";

        Environment.dcl(proto);
    }

    public void eval(int op, int count, Evaluator evl)
    throws SchRunTimeException
    {
        int cell, res;
        switch (op)
        {


        case OPMAKTHR:
           // Make new thread
           {
               if (count != 1)
                   throw new SchRunTimeException(ERR1AR);
               res = Evaluator.makeEvaluator();
               cell = evl.pop();
               if (Gdm.celltyp[cell] != TSYM)
                   throw new SchRunTimeException(ERRDOM);
               Evaluator e = (Evaluator)Gdm.cellobj[res];
               e.name = cell;
               evl.push(res);
           }
           break;


        case OPLOADTHR :
           // Load thread
           {
               if (count < 2)
                   throw new SchRunTimeException(ERRWAC);
               int v;
               int sev = evl.valstk[evl.ptvalstk-1];
               count --;
               if (Gdm.celltyp[sev] != TTHRD)
                   throw new SchRunTimeException(ERRDOM);
               Evaluator ev = (Evaluator)Gdm.cellobj[sev];
               ev.reset();
               for (int i=count; i>0; i--)
               {
                   v = evl.valstk[evl.ptvalstk-1-i];
                   ev.push(v);
               }
               count --;
               if (count <= MAXARGC)
               {
                    ev.pushCtl(BCF00+count);
               }
               else
               {
                    ev.pushCtl(Gdm.newint(count));
                    ev.pushCtl(BCFnn);
               }
               evl.ptvalstk -= count+2;
               evl.push(sev);
           }
           break;


        case OPRUNTHR :
           // Run a thread
           if (count != 1)
               throw new SchRunTimeException(ERR1AR);
           cell = evl.valstk[evl.ptvalstk-1];
           if (Gdm.celltyp[cell] != TTHRD)
               throw new SchRunTimeException(ERRDOM);
           Sch.activate(cell);
           break;


        case OPTHRSTAT :
           // Thread status
           {
               if (count != 1)
                   throw new SchRunTimeException(ERR1AR);
               cell = evl.valstk[evl.ptvalstk-1];
               if (Gdm.celltyp[cell] != TTHRD)
                   throw new SchRunTimeException(ERRDOM);
               Evaluator ev = (Evaluator)Gdm.cellobj[cell];
               String idt;
               switch (ev.status)
               {
               case RCINI  : idt = "init"; break;
               case RCRUN  : idt = "run"; break;
               case RCHLT  : idt = "halt"; break;
               case RCEND  : idt = "end"; break;
               case RCERR  : idt = "error"; break;
               case RCYEL  : idt = "wait"; break;
               case RCWAIT : idt = "wait"; break;
               default : idt = "undefined"; break;
               }
               res = Symbols.get(idt);
               evl.valstk[evl.ptvalstk-1] = res;
           }
           break;


        case OPTHRV :
           // Thread value
           {
               if (count != 1)
                   throw new SchRunTimeException(ERR1AR);
               cell = evl.valstk[evl.ptvalstk-1];
               if (Gdm.celltyp[cell] != TTHRD)
                   throw new SchRunTimeException(ERRDOM);
               Evaluator ev = (Evaluator)Gdm.cellobj[cell];
               res = ev.value;
               evl.valstk[evl.ptvalstk-1] = res;
           }
           break;


        case OPTHRN :
           // Thread name
           {
               if (count != 1)
                   throw new SchRunTimeException(ERR1AR);
               cell = evl.valstk[evl.ptvalstk-1];
               if (Gdm.celltyp[cell] != TTHRD)
                   throw new SchRunTimeException(ERRDOM);
               Evaluator ev = (Evaluator)Gdm.cellobj[cell];
               res = ev.name;
               evl.valstk[evl.ptvalstk-1] = res;
           }
           break;


        case OPTHRI :
           // Thread Id
           {
               if (count != 1)
                   throw new SchRunTimeException(ERR1AR);
               cell = evl.valstk[evl.ptvalstk-1];
               if (Gdm.celltyp[cell] != TTHRD)
                   throw new SchRunTimeException(ERRDOM);
               Evaluator ev = (Evaluator)Gdm.cellobj[cell];
               res = ev.timestamp;
               evl.valstk[evl.ptvalstk-1] = res;
           }
           break;


        case OPTHRQ :
           // Thread?
           {
               if (count != 1)
                   throw new SchRunTimeException(ERR1AR);
               cell = evl.valstk[evl.ptvalstk-1];
               evl.valstk[evl.ptvalstk-1] =
                   (Gdm.celltyp[cell] == TTHRD) ? CTRUE : CFALSE ;
           }
           break;


        case OPCTHR :
           // Current thread
           {
               if (count != 0)
                   throw new SchRunTimeException(ERR0AR);
               cell = evl.schcell;
               evl.push(cell);
           }
           break;


        /*
             Locks
        */

        case OPLOCKQ :
           // Lock ?
           {
               if (count != 1)
                   throw new SchRunTimeException(ERR1AR);
               cell = evl.valstk[evl.ptvalstk-1];
               evl.valstk[evl.ptvalstk-1] =
                   (Gdm.celltyp[cell] == TLOCK) ? CTRUE : CFALSE ;
           }
           break;


        case OPMAKLCK :
           // Make-lock
           {
               int num = 0;
               if (count == 1)
               {
                   num = evl.unstackint();
                   count--;
               }
               if (count != 0)
                   throw new SchRunTimeException(ERR0AR);
               res = Gdm.newcons(TLOCK, num, CNULL);
               evl.push(res);
           }
           break;


        case OPLOCKVAL :
           // LOCK value
           {
               if (count != 1)
                   throw new SchRunTimeException(ERR1AR);
               cell = evl.pop();
               if (Gdm.celltyp[cell] != TLOCK)
                   throw new SchRunTimeException(ERRDOM);
               int n = Gdm.cellcar[cell];
               evl.push(Gdm.newint(n));
           }
           break;


        case OPRELLCK :
           // Release LOCK
           {
               if (count != 1)
                   throw new SchRunTimeException(ERR1AR);
               cell = evl.valstk[evl.ptvalstk-1];
               if (Gdm.celltyp[cell] != TLOCK)
                   throw new SchRunTimeException(ERRDOM);
               int n = ++Gdm.cellcar[cell];
               int c = Gdm.cellcdr[cell];
               if (c != CNULL)
               {
                   int p = cell;
                   int z = Gdm.cellcdr[c];
                   while (z != CNULL)
                   {
                       p = c;
                       c = z;
                       if (Gdm.celltyp[c] != TPAIR)
                           throw new SchRunTimeException(ERRDOM);
                       z = Gdm.cellcdr[c];
                   }
                   // The last thread in wait list can
                   // be made to run
                   int w = Gdm.cellcar[c];
                   if (Gdm.celltyp[w] != TTHRD)
                       throw new SchRunTimeException(ERRDOM);
                   Gdm.cellcdr[p] = CNULL;
                   // Put it in exec queue
                   Sch.activate(w);
               }
           }
           break;


        case OPOBTLCK :
           // Obtain LOCK
           {
               if (count != 1)
                   throw new SchRunTimeException(ERR1AR);
               cell = evl.valstk[evl.ptvalstk-1];
               if (Gdm.celltyp[cell] != TLOCK)
                   throw new SchRunTimeException(ERRDOM);
               int n = --Gdm.cellcar[cell];
               if (n >= 0)
               {
                   // Nothing to do ?
               }
               else
               {
                   // Put the thread in wait state
                   int s = evl.schcell;
                   int c = Gdm.newcons(TPAIR,s,Gdm.cellcdr[cell]);
                   Gdm.cellcdr[cell] = c;
                   // Put it in wait queue
                   //Sch.inactivate(s);
                   evl.pushCtl(BCWAIT);
               }
           }
           break;

        /*
            Cells
        */

        case OPCELLQ :
           // Cell?
           {
               if (count != 1)
                   throw new SchRunTimeException(ERR1AR);
               cell = evl.valstk[evl.ptvalstk-1];
               evl.valstk[evl.ptvalstk-1] =
                   (Gdm.celltyp[cell] == TCELL) ? CTRUE : CFALSE ;
           }
           break;


        case OPMAKCLL :
           // Make-cell
           {
               if (count != 0)
                   throw new SchRunTimeException(ERR0AR);
               res = Gdm.newcons(TCELL, CUNDEF, CNULL);
               evl.push(res);
           }
           break;


        case OPCELLVAL :
           // Cell value
           {
               if (count != 1)
                   throw new SchRunTimeException(ERR1AR);
               cell = evl.valstk[evl.ptvalstk-1];
               if (Gdm.celltyp[cell] != TCELL)
                   throw new SchRunTimeException(ERRDOM);
               int w = Gdm.cellcar[cell];
               if (w == CUNDEF)
               {
                   // No value available - put the
                   // thread in wait-state 
                   // add thread to wait list
                   int s = evl.schcell;
                   int k = Gdm.cons(s, Gdm.cellcdr[cell]);
                   Gdm.cellcdr[cell]=k;
                   // Prepare to wait operation
                   evl.pushCtl(BCWAIT);
               }
               else
               {
                   evl.valstk[evl.ptvalstk-1] = w;
               }
//str.println("Fin du get value");
           }
           break;


        case OPCELLSET :
           // Set Cell value
           {
               if (count != 2)
                   throw new SchRunTimeException(ERR2AR);
               cell = evl.valstk[evl.ptvalstk-1];
               if (Gdm.celltyp[cell] != TCELL)
                   throw new SchRunTimeException(ERRDOM);
               res = evl.valstk[evl.ptvalstk-2];
               // Set value
               Gdm.cellcar[cell] = res;
               int w;
               while ((w = Gdm.cellcdr[cell]) != CNULL)
               {
                   Evaluator we;
                   int c = Gdm.cellcar[w];
                   if (Gdm.celltyp[c] != TTHRD)
                       throw new SchRunTimeException(ERRDOM);
                   Gdm.cellcdr[cell] = Gdm.cellcdr[w];
                   Sch.activate(c);
                   we = (Evaluator)Gdm.cellobj[c];
                   // Set new value as result of evaluators
                   we.valstk[we.ptvalstk-1] = res;
               }
               evl.ptvalstk--;
               evl.valstk[evl.ptvalstk-1] = cell;
           }
           break;


        case OPBARQ :
           // Barrier ?
           {
               if (count != 1)
                   throw new SchRunTimeException(ERR1AR);
               cell = evl.valstk[evl.ptvalstk-1];
               evl.valstk[evl.ptvalstk-1] =
                   (Gdm.celltyp[cell] == TBARR) ? CTRUE : CFALSE ;
           }
           break;


        case OPMAKBAR :
           {
               if (count != 1)
                   throw new SchRunTimeException(ERR1AR);
               int num = evl.unstackint();
               res = Gdm.newcons(TBARR, num, CNULL);
               evl.push(res);
           }
           break;


        case OPBARW :
           // Pass Barrier
           {
               if (count != 1)
                   throw new SchRunTimeException(ERR1AR);
               cell = evl.valstk[evl.ptvalstk-1];
               if (Gdm.celltyp[cell] != TBARR)
                   throw new SchRunTimeException(ERRDOM);
               int n = --Gdm.cellcar[cell];
               if (n <= 0)
               {
                   // Unlock all other thread
                   int c = Gdm.cellcdr[cell];
                   while (c != CNULL)
                   {
                       int p = cell;
                       int z = Gdm.cellcdr[c];
                       while (z != CNULL)
                       {
                           p = c;
                           c = z;
                           if (Gdm.celltyp[c] != TPAIR)
                               throw new SchRunTimeException(ERRDOM);
                           z = Gdm.cellcdr[c];
                       }
                       // The last thread in wait list can
                       // be made to run
                       int w = Gdm.cellcar[c];
                       if (Gdm.celltyp[w] != TTHRD)
                           throw new SchRunTimeException(ERRDOM);
                       Gdm.cellcdr[p] = CNULL;
                       // Put it in exec queue
                       Sch.activate(w);
                       // Add 1 to process count
                       n++;
                       // Next process ?
                       c = Gdm.cellcdr[cell];
                   }
                   // Restore actual barrier value
                   Gdm.cellcar[cell] = ++n;
               }
               else
               {
                   // Put the thread in wait state
                   int s = evl.schcell;
                   int c = Gdm.newcons(TPAIR,s,Gdm.cellcdr[cell]);
                   Gdm.cellcdr[cell] = c;
                   // Put it in wait queue
                   //Sch.inactivate(s);
                   evl.pushCtl(BCWAIT);
               }
           }
           break;

        default :
           throw new SchRunTimeException(ERRUSP);
        }
    }
}

