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
 * File: LangOps.java
 * Author:
 * Description:
 *
 * $Id: LangOps.java,v 1.6 2007/02/20 16:03:41 collins Exp $
 */

package Sch;
import java.io.*;


/**
 *
 *  Language specifi primitives
 *
 *
 * Implements the following procedures
 * <UL>
 * <LI><B>eval</B> 
 * <LI><B>compile</B> 
 * <LI><B>trace</B> 
 * <LI><B>debug</B> 
 *
 * </UL>
 *
 *     @author Jean-Jacques Girardot
 */

public class LangOps extends SchPrimitive {

    private static LangOps proto;

    public static final int OPCOMP = 0;
    public static final int OPEVAL = 1;
    public static final int OPENVQ = 2;
    public static final int OPCURENV = 3;
    public static final int OPBINDGS = 4;
    public static final int OPENVPAR = 5;
    public static final int OPTRACE = 6;
    public static final int OPDEBUG = 7;
    public static final int OPRUNM = 8;
    public static final int OPKONT = 9;
    public static final int OPKONT1 = 10;
    public static final int OPKONTQ = 11;
    public static final int OPPROMISEQ = 12;
    public static final int OPFORCE = 13;
    public static final int OPDYNW = 14;
    public static final int OPNULLEV = 15;
    public static final int OPSREPEV = 16;
    public static final int OPINTREV = 17;
    public static final int OPLOAD = 18;
    public static final int OPLOADK = 19;
    public static final int OPTRY = 20;

    public static final int NBOP = OPTRY+1;

    private static int timestamp = 0;

    public static void dcl()
    {
        proto = new LangOps();
        proto.fnames = new String[NBOP];

        try {
            // Some keywords
            Environment.dcl(emptenv,"quote",CQUOTE);
            Environment.dcl(emptenv,"quasiquote",CQQUOTE);
            Environment.dcl(emptenv,"unquote",CUQUOTE);
            Environment.dcl(emptenv,"unquote-splicing",CUQUOTES);
            Environment.dcl(emptenv,"define",CDEFINE);
            Environment.dcl(emptenv,"if",CIF);
            Environment.dcl(emptenv,"begin",CBEGIN);
            Environment.dcl(emptenv,"lambda",CLAMBDA);
            Environment.dcl(emptenv,"case",CCASE);
            Environment.dcl(emptenv,"cond",CCOND);
            Environment.dcl(emptenv,"else",CELSE);
            Environment.dcl(emptenv,"do",CDO);
            Environment.dcl(emptenv,"=>",CAPPLY);
            Environment.dcl(emptenv,"delay",CDELAY);
            Environment.dcl(emptenv,"set!",CSET);
            Environment.dcl(emptenv,"and",CAND);
            Environment.dcl(emptenv,"or",COR);
            Environment.dcl(emptenv,"let",CLET);
            Environment.dcl(emptenv,"let*",CLETE);
            Environment.dcl(emptenv,"letrec",CLETR);
            Environment.dcl(emptenv,"fluid-let",CFLLET);
        }
        catch (Exception e) {
            str.ppprint("Init Exception (LangOps) : " +
                  e.getMessage() + "\n");
        }
        proto.fnames[OPCOMP] = "compile";
        proto.fnames[OPEVAL] = "eval";
        proto.fnames[OPENVQ] = "environment?";
        proto.fnames[OPCURENV] = "current-environment";
        proto.fnames[OPNULLEV] = "null-environment";
        proto.fnames[OPSREPEV] = "scheme-report-environment";
        proto.fnames[OPINTREV] = "interaction-environment";
        proto.fnames[OPBINDGS] = "environment-bindings";
        proto.fnames[OPENVPAR] = "environment-parent";
        proto.fnames[OPTRACE] = "trace";
        proto.fnames[OPDEBUG] = "debug";
        proto.fnames[OPKONTQ] = "continuation?";
        proto.fnames[OPRUNM] = "run-mode";
        proto.fnames[OPKONT] = "call-with-current-continuation";
        proto.fnames[OPKONT1] = "call/cc";
        proto.fnames[OPFORCE] = "force";
        proto.fnames[OPPROMISEQ] = "promise?";
        proto.fnames[OPDYNW] = "dynamic-wind";
        proto.fnames[OPLOAD] = "load";
        // OPLOADK is the continuation of OPLOAD
        proto.fnames[OPTRY] = "try";

        Environment.dcl(proto);

    }

    public void eval(int op, int count, Evaluator evl)
    throws SchRunTimeException
    {
        int cell, res;
        switch (op)
        {


        case OPCOMP :
           {
              int exp, env = globenv;
              if (count == 2)
              {
                  exp = evl.pop();
                  Gdm.protect(exp);
                  env = evl.pop();
                  if ((Gdm.celltyp[env] != TENV) && (Gdm.celltyp[env] != TNULL))
                     throw new SchRunTimeException(ERREEX);
                  Gdm.protect(env);
              }
              else
              if (count == 1)
              {
                  exp = evl.pop();
                  Gdm.protect(exp);
              }
              else
                 throw new SchRunTimeException(ERRWAC);
              // Create as a niladic function
              exp = Gdm.list(CLAMBDA, CNULL, exp);
              if (env != evl.currenv)
              {
                  evl.pushCtl(evl.currenv);
                  evl.pushCtl(BCLDENV);
              }
              evl.pushCtl(Comp.comp(exp, CNULL, env));
              if (env != evl.currenv)
              {
                  evl.pushCtl(env);
                  evl.pushCtl(BCLDENV);
              }
           }
           break;


        case OPEVAL :
           {
              int exp, env = globenv;
              if (count == 2)
              {
                  exp = evl.pop();
                  Gdm.protect(exp);
                  env = evl.pop();
                  if ((Gdm.celltyp[env] != TENV) && (Gdm.celltyp[env] != TNULL))
                     throw new SchRunTimeException(ERREEX);
                  Gdm.protect(env);
              }
              else
              if (count == 1)
              {
                  exp = evl.pop();
                  Gdm.protect(exp);
              }
              else
                 throw new SchRunTimeException(ERRWAC);
              if (env != evl.currenv)
              {
                  evl.pushCtl(evl.currenv);
                  evl.pushCtl(BCLDENV);
              }
              evl.pushCtl(Comp.comp(exp, CNULL, env));
              if (env != evl.currenv)
              {
                  evl.pushCtl(env);
                  evl.pushCtl(BCLDENV);
              }
           }
           break;


        case OPENVQ :
           {
              int val;
              if (count != 1)
                 throw new SchRunTimeException(ERR1AR);
              val = evl.pop();
              evl.push(Gdm.typ(val) == TENV ? CTRUE : CFALSE);
           }
           break;


        case OPNULLEV :
           {
              if (count == 1)
                 evl.pop();
              else
              if (count != 0)
                 throw new SchRunTimeException(ERRSYN);
              evl.push(emptenv);
           }
           break;


        case OPSREPEV :
           {
              if (count == 1)
                 evl.pop();
              else
              if (count != 0)
                 throw new SchRunTimeException(ERRSYN);
              evl.push(systenv);
           }
           break;


        case OPINTREV :
           {
              if (count == 1)
                 evl.pop();
              else
              if (count != 0)
                 throw new SchRunTimeException(ERRSYN);
              evl.push(globenv);
           }
           break;


        case OPCURENV :
           {
              res = CNULL;
              if (count == 0)
                 res = evl.currenv;
              else
              if (count == 1)
              {
                 cell = evl.pop();
                 if (Gdm.celltyp[cell] == TLAMBDA)
                     res = Gdm.cellcdr[Gdm.cellcar[cell]];
                 else
                 if (Gdm.celltyp[cell] == TTHRD)
                     res = ((Evaluator)Gdm.cellobj[cell]).currenv;
                 else
                     throw new SchRunTimeException(ERRTYE);
              }
              else
                 throw new SchRunTimeException(ERRSYN);
              evl.push(res);
           }
           break;


        case OPBINDGS :
           {
              int exp, w;
              if (count != 1)
                 throw new SchRunTimeException(ERR1AR);
              res = CNULL;
              w = CNULL;
              exp = evl.pop();
              if (Gdm.celltyp[exp] == TLAMBDA)
                  exp = Gdm.cellcdr[Gdm.cellcar[exp]];
              if (Gdm.celltyp[exp] == TENV)
              {
                  res=bindings(exp);
              }
              else
              if (Gdm.celltyp[exp] != TNULL)
                 throw new SchRunTimeException(ERRTYE);
              evl.push(res);
           }
           break;


        case OPENVPAR :
           {
              int exp, w;
              if (count != 1)
                 throw new SchRunTimeException(ERR1AR);
              res = CNULL;
              w = CNULL;
              exp = evl.pop();
              if (Gdm.celltyp[exp] == TENV)
              {
                  res = Gdm.cellcdr[exp];
              }
              else
              if (Gdm.celltyp[exp] != TNULL)
                 throw new SchRunTimeException(ERRTYE);
              evl.push(res);
           }
           break;


        case OPTRACE :
           {
              int v = trace;
              if (count == 1)
              {
                  trace = evl.unstackint();
              }
              else
              {
                  if (count != 0)
                     throw new SchRunTimeException(ERR1AR);
              }
              evl.push(Gdm.newint(v));
           }
           break;


        case OPDEBUG :
           {
              int v = debug;
              if (count == 1)
              {
                  debug = evl.unstackint();
              }
              else
              {
                  if (count != 0)
                     throw new SchRunTimeException(ERR1AR);
              }
              evl.push(Gdm.newint(v));
           }
           break;


        case OPRUNM :
           {
              int v = runmode;
              if (count == 1)
              {
                  runmode = evl.unstackint();
              }
              else
              {
                  if (count != 0)
                     throw new SchRunTimeException(ERR1AR);
              }
              evl.push(Gdm.newint(v));
           }
           break;


        case OPKONT :
        case OPKONT1 :
           {
              // Cr�ation d'une continuation
              if (count != 1)
                 throw new SchRunTimeException(ERR1AR);
              res = Gdm.newcons(TKONT);
              int proc=evl.pop();
              byte ty = Gdm.celltyp[proc];
              if ((ty != TSUBR) && (ty != TLAMBDA)
                  && (ty != TKONT))
                 throw new SchRunTimeException(ERRPROC);
              Evaluator newev = new Evaluator(evl);
              Gdm.cellobj[res]=newev;
              newev.schcell = res;
              evl.push(res);
              evl.push(proc);
              evl.pushCtl(BCF01);
           }
           break;


        case OPKONTQ :
           {
               if (count != 1)
                  throw new SchRunTimeException(ERR1AR);
               cell = evl.valstk[evl.ptvalstk-1];
               evl.valstk[evl.ptvalstk-1] =
                   (Gdm.typ(cell) == TKONT) ? CTRUE : CFALSE;
           }
           break;


        case OPFORCE :
           {
              if (count != 1)
                 throw new SchRunTimeException(ERR1AR);
              int exp=evl.pop();
              if (Gdm.celltyp[exp] != TDELAY)
                 throw new SchRunTimeException(ERRSYN);
              if (Gdm.cellcar[exp] == CTRUE)
              {
                  evl.push(Gdm.cellcdr[exp]);
              }
              else
              {
                  evl.push(exp);
                  evl.pushCtl(BCDEL2);
                  evl.pushCtl(BCF00);
                  evl.pushCtl(Gdm.cellcdr[exp]);
              }
           }
           break;

        case OPPROMISEQ :
           if (count != 1)
              throw new SchRunTimeException(ERR1AR);
           cell = evl.valstk[evl.ptvalstk-1];
           evl.valstk[evl.ptvalstk-1] =
               (Gdm.typ(cell) == TDELAY) ? CTRUE : CFALSE;
           break;


        case OPDYNW :
           {
              if (count != 3)
                 throw new SchRunTimeException(ERRSYN);
              int bef=Gdm.list(BCF00,evl.pop());
              int thu=Gdm.list(BCF00,evl.pop());
              int aft=Gdm.list(BCF00,evl.pop());
              evl.pushCtl(Gdm.newint(++timestamp));
              evl.pushCtl(aft);
              evl.pushCtl(thu);
              evl.pushCtl(bef);
              evl.pushCtl(BCDWBFR);
              evl.pushCtl(bef);
              evl.push(BCDWDAT);
           }
           break;


        case OPLOAD :
           {
              if (count != 1)
                 throw new SchRunTimeException(ERR1AR);
               res = cell = evl.valstk[evl.ptvalstk-1];
               if (Gdm.celltyp[cell] == TSTR)
               {
                   // (load <string>)
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
               else
               {
                   // (load <input-port>)
                   if ((Gdm.celltyp[cell] != TPORT) ||
                       ((Gdm.cellcar[cell] & PSTIN) != PSTIN))
                            throw new SchRunTimeException(ERRDOM);
               }
               // Le sommet de pile est le port a lire
               int fun=Gdm.newcons(TSUBR,0,OPLOADK,proto);
               evl.ptvalstk--;
               //
               // On empile, dans l'ordre...
               // 1 - l'ancien environnement
               evl.push(evl.currenv);
               // 2 - le port d'entree
               evl.push(CurInpP);
               // 3 - le port de sortie
               evl.push(CurOutP);
               // 4 - le port de trace
               evl.push(CurTrcP);
               //
               // On positionne l'environnement global
               // pour l'execution du "load"
               evl.currenv = globenv;
               CurInpP = CstdNulIn;
               CurOutP = CstdNulOut;
               CurTrcP = CstdNulOut;
               //
               // On empile enfin
               // le port a lire
               evl.push(res);
               // la fonction a executer
               evl.push(fun);
               // un resultat bidon
               evl.push(CNULL);
               //
               // On se prepare ensuite a executer la fonction
               evl.pushCtl(BCF00);
               evl.pushCtl(fun);
//str.print("LOAD "+"    "+evl.ptvalstk+"\n"); // trace
//str.print("LOAD end\n"); // trace
           }
           break;


        case OPLOADK :
           {
//str.print("LOADK start\n"); // trace
               // Not a function - but the load continuation
               //
               // Eliminate result of the previous exec
               evl.ptvalstk--;
               if (count != 0)
                  throw new SchRunTimeException(ERR0AR);
               // Need the input port and the operation
//str.print("LOADK "+"    "+evl.ptvalstk+"\n"); // trace
               cell = evl.valstk[evl.ptvalstk-2];
               if ((Gdm.celltyp[cell] != TPORT) ||
                   ((Gdm.cellcar[cell] & PSTIN) != PSTIN))
                   throw new SchRunTimeException(ERRDOM);
               int fun = evl.valstk[evl.ptvalstk-1];
//str.print("LOADK "+cell+" "+evl.ptvalstk+"\n"); // trace
               SchReader scr = (SchReader) Gdm.cellobj[cell];
               int exp = scr.read();
               if ((exp == CTERR) || (exp == CEOF))
               {
                   // close port
                   scr.close();
                   // Suppress function and port
                   evl.ptvalstk -= 2;
                   // Restore context
                   // 4 - le port de trace
                   CurTrcP = evl.valstk[--evl.ptvalstk];
                   // 3 - le port de sortie
                   CurOutP = evl.valstk[--evl.ptvalstk];
                   // 2 - le port d'entree
                   CurInpP = evl.valstk[--evl.ptvalstk];
                   // 1 - l'ancien environnement
                   evl.currenv = evl.valstk[--evl.ptvalstk];
                   // return #t as result
                   evl.valstk[evl.ptvalstk++] = CTRUE;
               }
               else
               {
//str.print(exp); str.print("\n"); // trace
                   // Prepare for next operation
                   evl.pushCtl(BCF00);
                   evl.pushCtl(fun);
                   // Prepare to evaluation
                   evl.pushCtl(Comp.comp(exp, CNULL, globenv));
               }
           }
           break;


        case OPTRY :
           {
               /* (try thunk0 thunk1) */
               if (count != 2)
                  throw new SchRunTimeException(ERR2AR);
               int thunk0 = evl.pop();
               int thunk1 = evl.pop();
               evl.push(BCTRY1);
               evl.push(thunk0);
               evl.pushCtl(thunk1);
               evl.pushCtl(BCTRY1);
               evl.pushCtl(BCF00);
           }
           break;


        default :
str.ppprint("op="+op+"\n");
           throw new SchRunTimeException(ERRUSP);
        }
    }

    /**
        Generate the bindings of an environment
    */
    public static int bindings(int exp)
    throws SchRunTimeException
    {
        int res = CNULL;
        int w = CNULL;
        if (Gdm.celltyp[exp] == TENV)
        {
            int l=Gdm.cellcar[exp], c=CNULL, x;
            while (l != CNULL)
            {
                w=Gdm.cellcar[l];
                x=Gdm.cons(Gdm.cellcar[w], Gdm.cellcdr[w]);
                if (c == CNULL)
                {
                    c = Gdm.list(x);
                    res = c;
                    Gdm.protect(res);
                }
                else
                {
                    w = Gdm.list(x);
                    Gdm.cellcdr[c] = w;
                    c = w;
                }
                l = Gdm.cellcdr[l];
            }
        }
        return res;
    }


    public static void showbindings(int env)
    throws SchRunTimeException
    {
        while (Gdm.celltyp[env] == TENV)
        {
            str.print(" Env "+env+" = ");
            if (env == systenv)
            {
                str.println("{{{ System environment }}}");
            }
            else
            if (env == globenv)
            {
                str.println("{{{ User environment }}}");
            }
            else
            {
                spr.println(bindings(env));
            }
            env = Gdm.cellcdr[env];
        }
    }
}

