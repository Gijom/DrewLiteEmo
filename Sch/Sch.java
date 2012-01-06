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
 * File: Sch.java
 * Author:
 * Description:
 *
 * $Id: Sch.java,v 1.7 2007/02/20 16:03:41 collins Exp $
 */

package Sch;

/**
   <BR> Programme Principal
 */

import java.util.*;
import java.io.*;

public class Sch extends Globaldata {

   public static int smode;
   public static Sch sch = new Sch();
   
   public Sch ()
   {
       //System.err.println("Constructeur Sch appel�");
   }


   /**
       La m�thode d'initialisation de la classe

   */
   public static String schinit(int mode) {

       smode = mode;
       schts = 0;
       globenv = Cusrenv;
       systenv = Csysenv;
       emptenv = Cemptyenv;
       // L'evaluateur global
       try {
           evl=new Evaluator();
       } catch (Exception e) {
           System.exit(2);
       }
       // Les procedures
       MathsOps.dcl();
       ConsOps.dcl();
       UtilsOps.dcl();
       LangOps.dcl();
       BoolOps.dcl();
       IOOps.dcl();
       FnsOps.dcl();
       StringsOps.dcl();
       EvlOps.dcl();
       CROps.dcl();
       RefOps.dcl();
	   DrewOps.dcl();

       /* Ultimes initialisation */
       try {
          int x, w;

          // Choix de l'initialisation en fonction du mode
          switch (smode)
          {
          case 0 :
             // Mode conversationnel
             scr=new SchReader();
             spr=new SchPrinter();
             str=spr;
             // Interpreter mode
             runmode = RFINTER+RFTERIN+RFTEROUT;
             break;
          case 1 :
             // Mode Applet
             scr=new SchReader(PSTIN,"");
             spr=new SchPrinter(new java.io.StringWriter());
             str=spr;
             runmode = 0;
             break;
          default :
             System.exit(3);
          }

          //
          // Build REPL thread
          x=Environment.dcl(systenv,"global-evaluator",TTHRD,0,0);
          Gdm.cellobj[x]=evl;
          schevl = x;
          evl.schcell = x;
          evl.name = Symbols.get("repl");
          //
          // Build input port
          CurInpP = x = CstdIn;
          Environment.dcl(systenv,"terminal-input-port",x);
          Gdm.celltyp[x] = TPORT;
          Gdm.cellcar[x] = PSTIN;
          Gdm.cellcdr[x] = CNULL;
          Gdm.cellobj[x]=scr;
          //
          // Build output port
          CurOutP = x = CstdOut;
          Environment.dcl(systenv,"terminal-output-port",x);
          Gdm.celltyp[x] = TPORT;
          Gdm.cellcar[x] = PSTOUT;
          Gdm.cellcdr[x] = CNULL;
          Gdm.cellobj[x]=spr;
          //
          // Build trace port
          CurTrcP = x = CstdTrc;
          Environment.dcl(systenv,"trace-port",x);
          Gdm.celltyp[x] = TPORT;
          Gdm.cellcar[x] = PSTOUT;
          Gdm.cellcdr[x] = CNULL;
          Gdm.cellobj[x]=str;
          //
          // Build output sink port
          x = CstdNulOut;
          Environment.dcl(systenv,"sink-output-port",x);
          Gdm.celltyp[x] = TPORT;
          Gdm.cellcar[x] = PSTOUT|PSTFCLOSE;
          Gdm.cellcdr[x] = CNULL;
          Gdm.cellobj[x]= new SchPrinter(new SinkWriter());
          //
          // Build input sink port
          x = CstdNulIn;
          w = PSTIN|PSTFCLOSE;
          Environment.dcl(systenv,"sink-input-port",x);
          Gdm.celltyp[x] = TPORT;
          Gdm.cellcar[x] = w;
          Gdm.cellcdr[x] = CNULL;
          Gdm.cellobj[x]= new SchReader(w,new SinkReader());
          //
          // Environments
          Environment.dcl(systenv,
                  "system-global-environment",Csysenv);
          Environment.dcl(systenv,
                  "user-initial-environment",Cusrenv);
          Environment.dclnew(Cusrenv,
                  "it",Cit);
       } catch (Exception e) {
          str.ppprint("Initialisation error" + "\n");
          System.exit(5);
       }
       //str.println(MSGINI);
       return MSGINI;
   }


   /**
      Append an item in a queue
   */
/*
   public static void enqueue(int que, int c)
   throws SchRunTimeException
   {
       Gdm.protect1(c);
       int v = Gdm.newcons();
       Gdm.cellcar[v] = c;
       int k = Gdm.cellcdr[que];
       if (k == CNULL)
       {
           Gdm.cellcar[que] = v;
       }
       else
       {
           Gdm.cellcdr[k] = v;
       }
       Gdm.cellcdr[que] = v;
   }
*/


   /**
       Extract the first item from a queue
   */
   public static int dequeue(int que)
   {
       int k = Gdm.cellcar[que];
       if (k == CNULL)
           return k;
       int r = Gdm.cellcar[k];
       int w = Gdm.cellcdr[k];
       if (w == 0)
       {
           Gdm.cellcdr[que] = w;
       }
       Gdm.cellcar[que] = w;
       return r;
   }



   public static boolean empty(int que)
   {
       return (Gdm.cellcar[que] == CNULL);
   }


   /**
       Make active a Scheme process
   */
   public static void activate(int proc)
   throws SchRunTimeException
   {
       move(proc, Cwq, Cxq);
   }


   /**
       Make inactive a Scheme process
   */
   public static void inactivate(int proc)
   throws SchRunTimeException
   {
       move(proc, Cxq, Cwq);
   }



   public static void move(int proc, int from, int to)
   throws SchRunTimeException
   {
       // Add the process to the active list
       int k, p, n, w, c;
       boolean insert = true;
       k = Gdm.cellcar[to];
       p = CNULL;
       // Is the process already in the "to" list - bug ?
       while (k != CNULL)
       {
           c = Gdm.cellcar[k];
           n = Gdm.cellcdr[k];
           if (c == proc)
               insert = false;
           p = k;
           k = n;
       }
       // We can add the process to the "to" list
       if (insert)
       {
           w = Gdm.list(proc);
           if (p == CNULL)
           {
               // No process in the queue yet.
               Gdm.cellcar[to] = w;
           }
           else
           {
               // Chain to the last process
               Gdm.cellcdr[p] = w;
           }
           // Update queue "last" pointer
           Gdm.cellcdr[to] = w;
       }
       // Remove the process from the "from" queue
       k = Gdm.cellcar[from];
       p = CNULL;
       while (k != CNULL)
       {
           c = Gdm.cellcar[k];
           n = Gdm.cellcdr[k];
           if (c == proc)
           {
               // First process in queue ?
               if (p == CNULL)
               {
                   Gdm.cellcar[from] = n;
               }
               else
               {
                   Gdm.cellcdr[p] = n;
               }
               // Last process in queue ?
               if (n == CNULL)
               {
                   Gdm.cellcdr[from] = p;
               }
               return;
           }
           p = k;
           k = n;
       }
   }

   public static void load(String str)
   {
        scr.readerStrReset(str);
   }

   public static String result()
   {
        try {
            return spr.printerStrReset();
        } catch (SchRunTimeException e) {
            return "";
        }
   }

   /**
      Main execution loop
   */
   public static void loop()
   {
       int cell, res, cr, sev;
       long prevcucount;
       long prevgccount;
       long prevticks;
       //PrintWriter pw=new PrintWriter(System.out);
       PrintWriter pw=new PrintWriter(str.wr);
       flagexec = true;

       cell = CNULL;

       do {
          Evaluator ev;

          try {


             if (empty(Cxq))
             {
                 /*
                  / No executable thread
                  / Let me tell you what I'm gonna do
                  / I KILL the REPL process by resetting it
                  / I read and compile an expression
                  / Then I restart the REPL process
                 */
                 Gdm.cellpro = CNULL;
                 cell=scr.read();
                 if (Gdm.typ(cell) != TSPEC)
                 {
                    evl.reset();
                    res=comp(cell);
                    if ((trace & TRCODE) != 0)
                    {
                       if ((runmode & RFTEROUT) != 0)
                           str.print("-> ");
                       str.println(res);
                    }
                    evl.load(res);
                    //enqueue(Cxq, schevl);
                    activate(schevl);
                 }
                 else
                 {
                    switch(cell)
                    {
                    case CEOF :
                       flagexec = false;
                       break;
                    case CTERR :
                       throw new SchRunTimeException(ERRRSE);
                    default :
                       throw new SchRunTimeException
                                 ("Undefined read result");
                    }
                 }
              }
              else
              {
                  sev = dequeue(Cxq);
                  ev = (Evaluator) Gdm.cellobj[sev];
                  prevcucount = Gdm.cucount;
                  prevgccount = Gdm.gccount;
                  prevticks = ev.ticksused;
                  cr=ev.eval();
                  switch (cr)
                  {
                  case RCEND :
                      res = ev.value;
                      Gdm.cellcdr[Cit] = res;
                      if (res != CNDO)
                      {
                          if ((runmode & RFTEROUT) != 0)
                              str.print("=>  ");
                          str.println(res);
                      }
                      break;
                  case RCHLT :
                      break;
                  case RCYEL :
                      //enqueue(Cxq, sev);
                      activate(sev);
                      break;
                  case RCWAIT :
                      //enqueue(Cwq, sev);
                      inactivate(sev);
                      break;
                  case RCCNT :
                      //str.println(";;; Tick count exhausted");
                      activate(sev);
                      break;
                  default :
                      str.println(";;; Evaluator return code "
                         + cr);
                      break;
                  }
                  if ((trace & TRPRINFO) != 0)
                  {
                     str.ppprint("; Stack use : "+
                        ev.depthctlstk + "/" + ev.maxctlstk + ", " +
                        ev.depthvalstk + "/" + ev.maxvalstk + "\n");
                     str.ppprint("; Cells use : "+
                        (Gdm.cucount-prevcucount) + "/" +
                        Gdm.cellcount + ", " +
                        (Gdm.gccount-prevgccount) + " gc." + "\n");
                     str.ppprint("; Ticks use : "+
                        (ev.ticksused) + "\n");
                  }
             }
          }
          catch (Exception e)
          {
             if (e instanceof SchRunTimeException)
             {
                 str.ppprint("Error : " + e.getMessage() + "\n");
             }
             else
             {
                 Class c = e.getClass();
                 str.ppprint("System error : " +
                    e.getMessage() + "  " + c.getName() + "\n");
                 // Impression
                 e.printStackTrace(pw);
                 pw.flush();
             }
          }

       }
       while (flagexec);
   }

   public static void end()
   {
       try {
           str.println("; Cells use : "+ (Gdm.cucount) + "/" +
              Gdm.cellcount + ", " + (Gdm.gccount) + " gc.");
           str.println(MSGEND);
           str.flush();
       } catch (SchRunTimeException e) {}
   }


   public static int comp(int exp)
   throws SchRunTimeException
   {
       int res;
       Gdm.protect(exp);
       res = Comp.comp(exp, CNULL, globenv);
       Gdm.cellpro = CNULL;
       Gdm.protect(res);
       return res;
   }

}

