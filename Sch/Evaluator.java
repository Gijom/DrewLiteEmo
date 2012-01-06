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
 * File: Evaluator.java
 * Author:
 * Description:
 *
 * $Id: Evaluator.java,v 1.7 2007/02/20 16:03:41 collins Exp $
 */

package Sch;

/**
 *
 *  The Misc Evaluator
 *
 *
 *     @author Jean-Jacques Girardot
 */

import java.lang.reflect.*;

public class Evaluator extends Globaldata {

   public int [] valstk;
   public int ptvalstk;
   public int maxvalstk;
   public int [] ctlstk;
   public int ptctlstk;
   public int maxctlstk;
   public int currenv;
   public int depthvalstk;
   public int depthctlstk;
   public int status;
   public int value;
   public int name;
   public int timestamp;
   public int schcell;
   public int opr;
   public long ticksmax;
   public long ticksused;
   public long stdtickcount;

   /**
      Called by Garbage Collector
   */
   public void mark()
   throws SchRunTimeException
   {
      Gdm.mark(valstk, ptvalstk);
      Gdm.mark(ctlstk, ptctlstk);
      Gdm.mark(currenv);
      Gdm.mark(schcell);
      Gdm.mark(value);
      Gdm.mark(name);
      Gdm.mark(opr);
      Gdm.mark(timestamp);
      //str.println("Evaluator Marked");
   }

   /**
      Reset evaluator
   */
   public void reset()
   {
       ptvalstk = 0;
       ptctlstk = 1;
       ctlstk[0]=BCENDX;
       currenv = globenv;
       depthvalstk=0;
       depthctlstk=0;
       ticksmax = stdtickcount;
       ticksused = 0;
       status = RCINI;
       value = CNULL;
       opr = CNULL;
   }

   /**
      Main initialization
   */
   Evaluator()
   throws SchRunTimeException
   {
       //timestamp = schts++;
       timestamp = Gdm.newint(schts++);
       valstk = new int[KSSTACK];
       maxvalstk = KSSTACK;
       ctlstk = new int[KSSTACK];
       maxctlstk = KSSTACK;
       stdtickcount = KSEVTCC;
       schcell = CNULL;
       name = CNULL;
       reset();
   }

   static int makeEvaluator()
   throws SchRunTimeException
   {
       Evaluator ev = new Evaluator();
       int cell = Gdm.newcons();
       Gdm.celltyp[cell]=TTHRD;
       Gdm.cellobj[cell]=ev;
       ev.schcell = cell;
       return cell;
   }

   /**
       Clone evaluator
   */
   Evaluator(Evaluator ev)
   throws SchRunTimeException
   {
       int w;
       ptvalstk = w = ev.ptvalstk;
       w = w + ((w >> 4)|1);
       maxvalstk = w > KSSTACK ? w : KSSTACK;
       ptctlstk = w = ev.ptctlstk;
       w = w + ((w >> 4)|1);
       maxctlstk = w > KSSTACK ? w : KSSTACK;
       valstk = new int[maxvalstk];
       ctlstk = new int[maxctlstk];
       currenv = ev.currenv;
       depthvalstk= ev.depthvalstk;
       depthctlstk= ev.depthctlstk;
       ticksmax = ev.ticksmax;
       ticksused = ev.ticksused;
       stdtickcount = ev.stdtickcount;
       for (w=0; w<ptctlstk; w++)
           ctlstk[w]=ev.ctlstk[w];
       for (w=0; w<ptvalstk; w++)
           valstk[w]=ev.valstk[w];
       schcell = ev.schcell;
       //timestamp = schts++;
       timestamp = Gdm.newint(schts++);
   }


   public void load(int cell)
   throws SchRunTimeException
   {
       this.load(cell, globenv);
   }

   /*
        Evaluate a S-Expression
   */
   public void load(int cell, int env)
   throws SchRunTimeException
   {
       this.pushCtl(cell);
       this.pushCtl(env);
       this.pushCtl(BCLDENV);
   }


  /**
      Grand Eval

      The most complex part of the Scheme System

      Purpose : evaluate a stack of "op codes"
  */
  public int eval()
  //throws SchRunTimeException
  throws Exception
  {
     int w,k,cell;
     int code;
     byte ty;

     boolean cy=true;

     cell = CUNDEF;
     status = RCRUN;
     ticksmax = stdtickcount;

     if (ptctlstk <= 0)
     {
         ptctlstk = 1;
         ctlstk[ptctlstk-1] = BCENDX;
     }

     // the MAIN LOOP
     while (cy)
     {
     try {


     do
     {
        ticksused++;
        code = ctlstk[--ptctlstk];
        ty = Gdm.celltyp[code];

        if ((trace & TREVAL) != 0)
        {
           str.println("   "+ Gdm.toString(code));
        }

        switch (ty)
        {


        case TCTRL :
           switch(code)
           {


           case BCF00 :
           case BCF01 :
           case BCF02 :
           case BCF03 :
           case BCF04 :
           case BCF05 :
           case BCF06 :
           case BCF07 :
           case BCF08 :
           case BCFnn :
              {
                 int argc;
                 if (code == BCFnn)
                 {
                    argc=ctlstk[--ptctlstk];
                    argc=Gdm.cellcar[argc];
                 }
                 else
                 {
                    argc=code-BCF00;
                 }
                 // keep operation
                 opr = valstk[--ptvalstk];
                 if ((debug & DBGCHK) != 0)
                 {
                     // Call Memory check
                     Gdm.gc();
                 }
                 ty = Gdm.celltyp[opr];
                 int p1, p2;

                 switch(ty)
                 {
          

                 case TSUBR :
                    {
                       p2 = Gdm.cellcdr[opr];
                       Object proto = Gdm.cellobj[opr];

                       if (proto instanceof SchPrimitive)
                       {
/*
str.ppprint("(SchPrimitive : " + proto.toString()+ ")
");
*/
                           ((SchPrimitive)proto).eval(p2,argc,this);
                       }
                       else
                       {
                          p1 = Gdm.cellcar[opr];
str.ppprint("(SchPrimitive "+ opr +" : " + p1 + " " + p2 );
str.ppprint(" : " + proto.toString()+ ")\n");
                          throw new SchRunTimeException(0x088);
                       }
                    }
                    break;


                  case TLAMBDA :
                     {
                       int vx=Gdm.cellcar[opr];
                       int vl=Gdm.cellcar[vx];
                       int fenv=Gdm.cellcdr[vx];
                       int insts=Gdm.cellcdr[opr];
                       int id, sym, val, i, n, t;
                       int newe = fenv;
                       t = Gdm.celltyp[vl];

                       if ((argc != 0) || (t != TNULL))
                       {
                         // Must create a new environment
                         // for parameters
                         newe = Gdm.newcons(TENV,CNULL,fenv);
                         Gdm.protect1(newe);
                         while ((argc > 0) && (t == TPAIR))
                         {
                            // Create a new binding
                            // make p2
                            p2 = Gdm.newcons();
                            Gdm.cellcdr[p2]=Gdm.cellcar[newe];
                            Gdm.cellcar[newe]=p2;
                            // p2 is now protected
                            // make p1
                            p1 = Gdm.newcons(TVC);
                            Gdm.cellcar[p2]=p1;
                            // p1 is now protected
                            sym = Gdm.cellcar[vl];
                            Gdm.cellcar[p1]=sym;
                            val = valstk[--ptvalstk];
                            Gdm.cellcdr[p1]=val;
                            vl = Gdm.cellcdr[vl];
                            t = Gdm.celltyp[vl];
                            argc--;
                         }
                         // Should we handle some special case ?
                         if ((argc != 0) || (t != TNULL))
                         {
                            // The remaining object *must* be a symbol
                            if (t != TSYM)
                               signal(ERRWAC);
                            // it will get the rest of the values
                            // make p2
                            p2 = Gdm.newcons();
                            Gdm.cellcdr[p2]=Gdm.cellcar[newe];
                            Gdm.cellcar[newe]=p2;
                            // p2 is now protected
                            // make p1
                            p1 = Gdm.newcons(TVC);
                            Gdm.cellcar[p2]=p1;
                            // p1 is now protected
                            // vl is the name of the symbol
                            Gdm.cellcar[p1]=vl;
                            t = p1;
                            while (argc > 0)
                            {
                               i = Gdm.newcons();
                               Gdm.cellcdr[t]=i;
                               val = valstk[--ptvalstk];
                               Gdm.cellcar[i]=val;
                               t = i;
                               argc--;
                            }
                         }
                       }
                       if (newe != currenv)
                       {
                         if (ctlstk[ptctlstk-1] != BCLDENV)
                         {
                           // Recursion non terminale
                           // Ancien environnement
                           pushCtl(currenv);
                           pushCtl(BCLDENV);
                         }
                         // Le code
                         pushCtl(insts);
                         // Nouvel environnement
                         pushCtl(newe);
                         pushCtl(BCLDENV);
                       }
                       else
                       {
                         // Le code
                         pushCtl(insts);
                       }
                     }
                     break;


                  case TKONT :
                     {
                        // Le code est une continuation
                        // On attend un param�tre unique
                        if (argc != 1)
                           signal(ERR1AR, "cont");
                        int par = valstk[--ptvalstk];
                        Evaluator ev=(Evaluator)Gdm.cellobj[opr];
                        /*
                            On va comparer les deux �valuateurs
                        */
                        int imin;
                        for (imin=0; imin<ptctlstk
                               && imin<ev.ptctlstk
                               && ctlstk[imin] == ev.ctlstk[imin];
                               imin++)
                        ;
                        // Compter les DW "actifs" (phase 2)
                        int act=0, i=0, savedpt=0;
                        for (i=imin ; i<ptctlstk; i++)
                           if (ctlstk[i] == BCDWACT)
                              act++;
                        for (i=imin ; i<ev.ptctlstk; i++)
                           if (ev.ctlstk[i] == BCDWACT)
                              act++;
                        //str.println("Active DW : "+act);
                        /* Copie du stack des op�randes */
                        ptvalstk = w = ev.ptvalstk;
                        w = w + ((w >> 4)|1);
                        maxvalstk = w > KSSTACK ? w : KSSTACK;
                        valstk = new int[maxvalstk];
                        for (w=0; w<ptvalstk; w++)
                            valstk[w]=ev.valstk[w];
                        depthvalstk= ev.depthvalstk;
                        /* On conserve �ventuellement les
                           op�rations � effectuer dans les donn�es */
                        if (act != 0)
                        {
                          savedpt = ptvalstk;
                          // les "before" de la future cont.
                          for (i=imin ; i<ev.ptctlstk; i++)
                            if (ev.ctlstk[i] == BCDWACT)
                            {
                              push(BCPOPV);
                              push(ev.ctlstk[i-1]);
                            }
                          // les "after" de la pr�sente
                          for (i=imin ; i<ptctlstk; i++)
                            if (ctlstk[i] == BCDWACT)
                            {
                              push(BCPOPV);
                              push(ev.ctlstk[i-3]);
                            }
                        }
                        /* Copy du stack de contr�le */
                        ptctlstk = w = ev.ptctlstk;
                        w = w + ((w >> 4)|1);
                        maxctlstk = w > KSSTACK ? w : KSSTACK;
                        ctlstk = new int[maxctlstk];
                        for (w=0; w<ptctlstk; w++)
                            ctlstk[w]=ev.ctlstk[w];
                        depthctlstk= ev.depthctlstk;
                        if (act != 0)
                        {
                          for (i=savedpt; i<ptvalstk; i++)
                          {
                            w = valstk[i];
                            //str.println("   "+ Gdm.toString(w));
                            pushCtl(w);
                          }
                          ptvalstk = savedpt;
                        }
                        // Mise � jour du reste
                        currenv = ev.currenv;
                        ticksmax = ev.ticksmax;
                        ticksused = ev.ticksused;
                        stdtickcount = ev.stdtickcount;
                        //Gdm.cellobj[schcell]=ev;
                        push(par);
                     }
                     break;



                  case TJMETH :
                     {
                        // A Java method
                        Object obj = null;
                        Object re = null, ob = null;
                        p1 = Gdm.cellcar[opr];
                        p2 = Gdm.cellcdr[opr];
                        int np = argc;
 
                        Object meth = Gdm.cellobj[opr];

                        if (meth instanceof Method)
                        {
                            // Is type of method already determined ?
                            // 1 is for Class method
                            // 2 is for instance method
                            if (p1 == 0)
                            {
                               // We need to determine it dynamically
                               String sn = RefOps.getname(meth, "toString");
                               p1 = StringsOps.match("* static *",sn) ? 1 : 2;
                               Gdm.cellcar[opr] = p1;
                            }
                        }
                        else
                        if (meth instanceof Constructor)
                        {
                            if (p1 == 0)
                            {
                               Gdm.cellcar[opr] = p1 = 3;
                            }
                        }
                        else
                        if (meth instanceof Field)
                        {
                            // Is type of field already determined ?
                            // 4 is for Class field
                            // 5 is for instance field
                            if (p1 == 0)
                            {
                               // We need to determine it dynamically
                               String sn = RefOps.getname(meth, "toString");
                               p1 = StringsOps.match("* static *",sn) ? 4 : 5;
                               Gdm.cellcar[opr] = p1;
                            }
                        }
                        else
                        {
                           // This is an error
                           str.ppprint("(Method "+ opr +" : " + p1 + " " + p2 );
                           str.ppprint(" : " + meth.toString()+ ")\n");
                           throw new SchRunTimeException(0x088);
                        }
                        // Is there an object on which we operate ?
                        // It is the case for instance methods or instance fields
                        if ((p1 == 2) || (p2 == 5))
                        {
                            // The object on which the method will operate
                            obj = RefOps.getObj(valstk[ptvalstk-1]);
                            // Actual length of the array
                            np = argc-1;
                            ptvalstk--;
                        }
                        // Let's build an array of arguments
                        if (np < 0)
                            throw new SchRunTimeException(ERR1AR);
                        Object [] tpar = new Object [np];
                        for (int p=0; p<np; p++)
                        {
                            ob = RefOps.getObj(valstk[ptvalstk-1-p]);
                            tpar[p] = ob;
                        }
                        int res = CNULL;
                        // Let't invoke the thing
                        try {
                             switch (p1)
                             {
                             case 1 :
                             case 2 :
                                 re = ((Method)meth).invoke(obj, tpar);
                                 break;
                             case 3 :
                                 re = ((Constructor)meth).newInstance(tpar);
                                 break;
                             case 4 :
                             case 5 :
                                 if (np == 0)
                                 {
                                    // No "parameter" => it's a get
                                    re = ((Field)meth).get(obj);
                                 }
                                 else
                                 {
                                    // 1 parameter : a "set"
                                    // Returns the previous value
                                    re = ((Field)meth).get(obj);
                                    ((Field)meth).set(obj, tpar[0]);
                                 }
                                 break;
                             default :
                                 re = null;
                                 break;
                             }
                             res = RefOps.pack(re);
                        } catch (Exception e) {
                             res = RefOps.pack(e); }
                        ptvalstk -= np-1;
                        valstk[ptvalstk-1] = res;
                     }
                     break;



                  default :
                     signal(ERRUNA,
                          "[" + Gdm.celltype(opr) + ":" + 
                          Gdm.celltostring(opr) + "]  ");
                  }
               }
               break;


           case BCIF :
              {
                 int cthen = ctlstk[--ptctlstk];
                 int celse = ctlstk[--ptctlstk];
                 int res = valstk[--ptvalstk];
                 res = res == CFALSE ? celse : cthen ;
                 ctlstk[ptctlstk++]=res;
              }
              break;


           case BCAND :
              {
                  int lr=valstk[--ptvalstk];
                  if (lr == CFALSE)
                  {
                     // Ne pas continuer
                     --ptctlstk;
                     ptvalstk++;
                  }
              }
              break;


           case BCOR :
              {
                 int lr=valstk[--ptvalstk];
                 if (lr != CFALSE)
                 {
                     // Ne pas continuer
                     --ptctlstk;
                     ptvalstk++;
                 }
              }
              break;


           // Select the environment
           case BCLDENV :
              currenv = ctlstk[--ptctlstk];
              if ((trace & TREVAL) != 0)
              {
                 str.println("   ----> "+ 
                   currenv + "  " +
                   Gdm.toString(currenv));
              }
              break;


           case BCLMBDA :
              {
                 int res, dcl, fcode;
                 fcode = ctlstk[--ptctlstk];
                 dcl = ctlstk[--ptctlstk];
                 fcode=Comp.comp(fcode,dcl,currenv);
 //LangOps.showbindings(currenv);
                 dcl = Gdm.newcons(TPAIR, dcl, currenv);
                 res = Gdm.newcons(TLAMBDA, dcl, fcode);
                 push(res);
              }
              break;


           case BCDEF :
              {
                 int id = popCtl();
                 int val = pop();
                 ty = Gdm.celltyp[id];
                 if (ty == TVC)
                 {
                    Gdm.cellcdr[id]=val;
                    id = Gdm.cellcar[id];
                 }
                 else
                 {
                    Check.id(id);
                    Environment.dclnew(currenv,id);
                    Environment.set(currenv,id,val);
                 }
                 push(id);
              }
              break;


           case BCSET :
              // set - value stays in stack
              {
                 int id = ctlstk[ptctlstk-1];
                 int val = valstk[ptvalstk-1];
                 ty = Gdm.celltyp[id];
                 if (ty == TVC)
                 {
                    Gdm.cellcdr[id]=val;
                 }
                 else
                 {
                    Check.id(id);
                    Environment.dcl(currenv,id);
                    Environment.set(currenv,id,val);
                 }
                 ptctlstk--;
              }
              break;


           case BCSETU :
              // set - value is removed from stack
              {
                 int id = ctlstk[ptctlstk-1];
                 int val = valstk[ptvalstk-1];
                 ty = Gdm.celltyp[id];
                 if (ty == TVC)
                 {
                    Gdm.cellcdr[id]=val;
                 }
                 else
                 {
                    Check.id(id);
                    Environment.dcl(currenv,id);
                    Environment.set(currenv,id,val);
                 }
                 ptctlstk--;
                 ptvalstk--;
              }
              break;


           case BCQUOTE :
              {
                 push(ctlstk[--ptctlstk]);
              }
              break;


           case BCPOPV :
              --ptvalstk;
              break;


           case BCLET :
              {
                 if (ctlstk[ptctlstk-2] != BCLDENV)
                 {
                   // We should save the old environment
                   int exp = ctlstk[--ptctlstk];
                   // Ancien environnement
                   pushCtl(currenv);
                   pushCtl(BCLDENV);
                   pushCtl(exp);
                 }
                 currenv=Gdm.newcons(TENV,CNULL,currenv);
              }
              break;


           case BCLETREC :
              {
                 int vls = ctlstk[--ptctlstk];
                 int exp = ctlstk[--ptctlstk];
                 // should we save the old environment ?
                 if (ctlstk[ptctlstk-1] != BCLDENV)
                 {
                   pushCtl(currenv);
                   pushCtl(BCLDENV);
                 }
                 pushCtl(exp);
                 pushCtl(vls); // protect vls against GC
                 currenv=Gdm.newcons(TENV,CNULL,currenv);
                 while (Gdm.celltyp[vls] == TPAIR)
                 {
                     int x = Gdm.cellcar[vls];
                     int vc = Gdm.newcons(TVC,x,CUNDEF);
                     int cc = Gdm.newcons(TPAIR,vc,Gdm.car(currenv));
                     Gdm.cellcar[currenv]=cc;
                     vls = Gdm.cellcdr[vls];
                 }
                 ptctlstk--; // "unstack" vls
              }
              break;


           case BCADENV :
              {
                 int val = valstk[ptvalstk-1];
                 int nid = ctlstk[ptctlstk-1];
                 int vc = Gdm.newcons(TVC,nid,val);
                 int cc = Gdm.newcons(TPAIR,vc,Gdm.car(currenv));
                 Gdm.cellcar[currenv]=cc;
                 ptvalstk--;
                 ptctlstk--;
              }
              break;


           case BCMAP :
              {
                 int n, i, t;
                 int val = valstk[--ptvalstk];
                 int nargs = ctlstk[ptctlstk-1];
                 int fct = ctlstk[ptctlstk-2];
                 int res = ctlstk[ptctlstk-3];
                 if (res != CFALSE)
                 {
                    res = Gdm.newcons(TPAIR,val,res);
                    ctlstk[ptctlstk-3] = res;
                 }
                 int b = ptvalstk-nargs;
                 int c = ptvalstk-1;
                 boolean x=true;
                 for (i=b; i<=c; i++)
                 {
                     n=valstk[i];
                     t = Gdm.celltyp[n];
                     if (t == TNULL)
                         x=false;
                     else
                     {
                         push(Gdm.cellcar[n]);
                         valstk[i] = Gdm.cellcdr[n];
                     }
                 }
                 //
                 if (x)
                 {
                     pushCtl(BCMAP);
                     push(fct);
                     if (nargs <= MAXARGC)
                     {
                         pushCtl(BCF00+nargs);
                     }
                     else
                     {
                         pushCtl(Gdm.newint(nargs));
                         pushCtl(BCFnn);
                     }
                 }
                 else
                 {
                     // pas d'application
                     ptctlstk -= 3;
                     ptvalstk = b;
                     // On execute un "reverse!" du resultat
                     if (res != CFALSE)
                     {
                         val = CNULL;
                         while (Gdm.celltyp[res] == TPAIR)
                         {
                             n=Gdm.cellcdr[res];
                             Gdm.cellcdr[res] = val;
                             val = res; res = n;
                         }
                     }
                     else
                         val = CFALSE;
                     push(val);
                 }
              }
              break;


           case BCCOND :
              {
                 int cfct, res;
                 // handle the => part of cond
                 res = valstk[ptvalstk-1];
                 if (res == CFALSE)
                 {
                     // execute the "else" part
                     --ptvalstk; --ptctlstk;
                 }
                 else
                 {
                     // apply the function on result
                     // get the function
                     cfct = ctlstk[--ptctlstk];
                     // remove the "else" part
                     --ptctlstk;
                     // push the "eval 1 arg fct"
                     ctlstk[ptctlstk++] = BCF01;
                     // push the function back
                     ctlstk[ptctlstk++] = cfct;
                 }
              }
              break;


           case BCCASE :
              {
                  // The "case handling"
                  int key = valstk[ptvalstk-1];
                  int res = 0;
                  int l = ctlstk[--ptctlstk];
                  // Prepare to call memv
                  res = ConsOps.memv(key, l);
//str.print("Case exp="+res);
//str.println(res);
                  if (res <= 0)
                  {
                      // Not found - remove some objects
                      --ptctlstk;
                  }
                  else
                  {
                      l = ctlstk[--ptctlstk];
                      ctlstk[ptctlstk-1] = l;
                      --ptvalstk;
                  }
              }
              break;


           case BCDELAY :
              {
                 int val = valstk[--ptvalstk];
                 val = Gdm.newcons(TDELAY,CFALSE,val);
                 valstk[ptvalstk++] = val;
              }
              break;


           case BCDEL2 :
              {
                 int val = valstk[--ptvalstk];
                 int exp = valstk[--ptvalstk];
                 Gdm.cellcdr[exp] = val;
                 Gdm.cellcar[exp] = CTRUE;
                 valstk[ptvalstk++] = val;
              }
              break;


           case BCDWBFR :
              {
                 // Before is executed
                 --ptvalstk; // unstack "before" value
                 pushCtl(BCDWACT);
                 // Push "thunk" code
                 pushCtl(ctlstk[ptctlstk-3]);
              }
              break;


           case BCDWACT :
              {
                 // Before and thunk are executed
                 pushCtl(BCDWAFT);
                 // Push "after" code
                 pushCtl(ctlstk[ptctlstk-4]);
              }
              break;


           case BCDWAFT :
              {
                 // Before, thunk and after executed
                 --ptvalstk; // unstack "after" value
                 // remove "before", "thunk", "aft" and timestamp
                 ptctlstk -= 4;
                 int val=pop();
                 int chk=pop();
                 if (chk != BCDWDAT)
                    signal(ERRUND, "dynamic-wind");
                 push(val);
              }
              break;


           case BCFALSE :
              valstk[ptvalstk-1] = CFALSE;
              break;


           case BCNOP :
              break;

          
           case BCTRY1 :
              /* Exit from a try */
              {
                  /* We expect a value on TOS */
                  int val = pop();
                  /* Then a TRY1 op code */
                  int opc = pop();
                  if (opc != BCTRY1)
                      throw new SchRunTimeException(ERRSYS);
                  // eliminate error handling procedure
                  popCtl();
                  // push result
                  push(val);
              }
              break;


           case BCTRY2 :
              /* Abnormal end of some procedure */
              {
                  int val, err;
                  err = pop();
                  while ((ptvalstk >= 0) && ((val = valstk[ptvalstk-1]) != BCTRY1)
                       && (val != BCTRY2))
                      ptvalstk--;
                  int opr = popCtl();
                  push(err);
                  push(opr);
                  pushCtl(BCF01);
              }
              break;


           case BCENDX :
              value = ptvalstk <= 0 ? CNULL : valstk[ptvalstk-1];
              return status=RCEND;


           case BCYIEL :
              value = ptvalstk <= 0 ? CNULL : valstk[ptvalstk-1];
              return status=RCYEL;


           case BCWAIT :
              value = ptvalstk <= 0 ? CNULL : valstk[ptvalstk-1];
              return status=RCWAIT;


           case BCHALT :
              value = ptvalstk <= 0 ? CNULL : valstk[ptvalstk-1];
              return status=RCHLT;


           default :
               signal(ERRUNC,"X["+ty+","+code+"] ");
           }
           break;


        // Scheme Data
        case TNULL :
        case TINT :
        case TSTR :
        case TBOOL :
        case TCHAR :
        case TENV :
        case TLAMBDA :
        case TSUBR :
           push(code);
           break;


        // Undefined value - should it be there ?
        case TUNDEF :
           signal(ERRUND);


        // A Value Cell
        case TVC :
           w = Gdm.cellcdr[code];
           if (w == CUNDEF)
              signal(ERRUND, (String)Gdm.cellobj[Gdm.cellcar[code]]);
           push(w);
           break;


        // A symbol => get the value in the environment
        case TSYM :
          {
             int vc=CNULL;
             int vcl = Gdm.cellcar[currenv];
             int cev = Gdm.cellcdr[currenv];
             boolean lp=true;
             do
             {
                if (vcl != CNULL)
                {
                    w = Gdm.cellcar[vcl];
                    if (Gdm.cellcar[w] == code)
                    {
                       vc = w;
                       lp=false;
                    }
                    else
                    {
                       vcl = Gdm.cellcdr[vcl];
                    }
                }
                else
                if (cev == CNULL)
                {
                    lp=false;
                }
                else
                {
                    vcl = Gdm.cellcar[cev];
                    cev = Gdm.cellcdr[cev];
                }
             } while (lp);
             w = Gdm.cellcdr[vc];
             if ((vc == CNULL) || (w == CUNDEF))
                signal(ERRUND, (String)Gdm.cellobj[code]);
             push(w);
          }
          break;


        // A list - therefore an instruction
        case TPAIR :
           /**  Nouvel algorithme - on empile
                 les �l�ments   */
           {
              w = code;
              while (w != CNULL)
              {
                  pushCtl(Gdm.cellcar[w]);
                  w=Gdm.cellcdr[w];
              }
          }
          break;


        // An unknown type object
        default :
            signal(ERRUNO, "X["+ty+","+code+"] ");
        }

      } while (--ticksmax > 0);

      // On est ici en fin de tick - renvoyer un "cr"
      return status=RCCNT;

      } catch (Exception e) {
          status = RCERR;
          /*
              Tester ici si on est dans un "try" scheme
          */
          int pt = ptctlstk-1;
          int v;
          while (pt >= 0)
          {
              v = ctlstk[pt];
              if (v == BCTRY1)
              {
                  status = 0;
                  ctlstk[pt] = BCTRY2;
                  ptctlstk = pt+1;
                  v = Gdm.newcons(TERR);
                  Gdm.cellobj[v] = e;
                  push(v);
                  pt = 0;
              }
              pt--;
          }
          if (status != 0)
          {
              cy = false; 
              if ((trace & TRSTACK) != 0)
              {
                  dmpstk("Value Stack", valstk, ptvalstk);
                  dmpstk("Control Stack", ctlstk, ptctlstk+1);
              }
              throw e;
          }
      } // try/catch


      } // while(cy)
      return status=RCERR;
  }



   public void push(int cell)
   throws SchRunTimeException
   {
      if (ptvalstk >= maxvalstk)
      {
         int newsiz = maxvalstk + ((maxvalstk >> 2)|1);
         //str.println("Stack : "+maxvalstk+" -> "+newsiz);
         if (newsiz > KSMSTACK)
             throw new SchRunTimeException(ERRSTKF);
         int [] newstack = new int[newsiz];
         for (int i=0; i<maxvalstk; i++)
             newstack[i]=valstk[i];
         maxvalstk = newsiz;
         valstk = newstack;
      }
      valstk[ptvalstk++] = cell;
      if (ptvalstk > depthvalstk)
          depthvalstk = ptvalstk;
   }

   public int top()
   throws SchRunTimeException
   {
      if (ptvalstk <= 0)
          throw new SchRunTimeException(ERRSTKE);
      return valstk[ptvalstk-1];
   }


   public int pop()
   throws SchRunTimeException
   {
      if (ptvalstk <= 0)
          throw new SchRunTimeException(ERRSTKE);
      return valstk[--ptvalstk];
   }

   public boolean notempty()
   {
      return (ptvalstk > 0);
   }


   public void pushCtl(int cell)
   throws SchRunTimeException
   {
      if (ptctlstk >= maxctlstk)
      {
         int newsiz = maxctlstk + ((maxctlstk >> 2)|1);
         //str.println("Stack : "+maxctlstk+" -> "+newsiz);
         if (newsiz > KSMSTACK)
             throw new SchRunTimeException(ERRSTKF);
         int [] newstack = new int[newsiz];
         for (int i=0; i<maxctlstk; i++)
             newstack[i]=ctlstk[i];
         maxctlstk = newsiz;
         ctlstk = newstack;
      }
      ctlstk[ptctlstk++] = cell;
      if (ptctlstk > depthctlstk)
          depthctlstk = ptctlstk;
   }

   public int popCtl()
   {
      if (ptctlstk <= 0)
          return BCENDX;
      return ctlstk[--ptctlstk];
   }

   public int unstackint()
   throws SchRunTimeException
   {
      int cell;
      if (ptvalstk <= 0)
          throw new SchRunTimeException(ERRSTKE);
      cell = valstk[--ptvalstk];
      if (Gdm.celltyp[cell] != Globaldata.TINT)
          throw new SchRunTimeException(ERRINT);
      return Gdm.cellcar[cell];
   }


   public void dmpstk(String id, int [] stk, int pst)
   {
       int e, s, w;
       str.ppprint(" "+id+" ("+pst+")" + "\n");
       for (int i=0; i<pst; i++)
       {
           e = stk[i];
           str.ppprint(Gdm.strEdit(i,6)+"   "+
               Gdm.toString(e) + "\n");
       }
   }


   public void signal(int errno, String msg)
   throws SchRunTimeException
   {
        //status = RCERR;
        //dmpstk("Value Stack", valstk, ptvalstk);
        //dmpstk("Control Stack", ctlstk, ptctlstk+1);
        throw new SchRunTimeException(errno, msg);
   }


   public void signal(int errno)
   throws SchRunTimeException
   {
        //status = RCERR;
        //dmpstk("Value Stack", valstk, ptvalstk);
        //dmpstk("Control Stack", ctlstk, ptctlstk+1);
        throw new SchRunTimeException(errno);
   }


}

