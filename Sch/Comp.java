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
 * File: Comp.java
 * Author:
 * Description:
 *
 * $Id: Comp.java,v 1.4 2003/11/24 10:54:30 serpaggi Exp $
 */

package Sch;


/**

   The compilation part

*/
public class Comp extends Globaldata {


  /**

    This is the "expression compiler", i.e. the
    code which transforms a misc object
    (usually a list) to an internal representation,
    suitable as an input for the "evaluator".

    Quite complex. We need to determine from
    which environement is coming any identifier
    used in expression, and maintain the "future"
    environment at execution time.

    Parameters of the "comp" method

    exp = the expression to compile
    env = the compile time environment
    lvl = a list of local variables
  */
  public static int comp(int exp, int lvl, int env)
  throws SchRunTimeException
  {
    int ty = Gdm.typ(exp);
    int doexp=CNULL;
    int c, d, id, x, y, z;
    int w = ConsOps.len(exp);


    switch (ty)
    {
    case TPAIR :
      c = Gdm.cellcar[exp];
      d = Gdm.cellcdr[exp];
      int tyc = Gdm.celltyp[c];
      if (tyc == TSYM)
      {

      // A symbol - is the object a system macro ?
      z = 0;
      if (tyc == TSYM)
      {
         if (ConsOps.memq(c,lvl) > 0)
         {
           // A Local Symbol
/*
str.println("Local symbol : "+(String)Gdm.cellobj[exp]);
*/
         }
         else
         {
           // External symbol. Look for it in the
           // provided environment
           int vc = Environment.getvc(env, c);
           if (vc <= 0)
           {
               // Not defined in the environment
               // We declare it
               vc = Environment.dcl(env, c);
           }
/*
str.println("Global symbol : "+(String)Gdm.cellobj[exp]+
     " "+vc);
*/
           x = vc;
           // x is a value cell - is this a system macro ?
           y = Gdm.cellcdr[x];
           if ((Gdm.celltyp[y] == TSYM))
              // y est le mot-clef
              z=y;
           else
           if (Gdm.celltyp[y] == TMACRO)  // TMACRO unused
              // le cdr est le mot-clef
              z = Gdm.cellcdr[y];
         }
      }

      /**
          Is the first element of the list
          a keyword of the language ?
      */
      switch (z)
      {


      case CQUOTE :
        if (w != 2)
        {
//str.println("Comp 0000");
          throw new SchRunTimeException(ERRSYN);
        }
        exp=Gdm.list(Gdm.cellcar[d],BCQUOTE);
        break;


      case CLAMBDA :
        if (w < 3)
        {
//str.println("Comp 0001");
          throw new SchRunTimeException(ERRSYN);
        }
        int vl=Gdm.cellcar[d]; // formal arguments list
        int re=Gdm.cellcdr[d]; // rest of the lambda
        //
        // We now verify that all formals are symbols
        c=vl;
        ty=Gdm.typ(c);
        while (ty == TPAIR)
        {
            d=Gdm.cellcar[c];
            if (Gdm.celltyp[d] != TSYM)
            {
//str.print("Comp exp=");
//str.println(exp);
               throw new SchRunTimeException(ERRSYN);
            }
            c = Gdm.cellcdr[c];
            ty = Gdm.celltyp[c];
        }
        // The last element of the formal list should be 
        // null or a symbol
        if ((ty != TNULL) && (ty != TSYM))
        {
//str.println("Comp 0003");
            throw new SchRunTimeException(ERRSYN);
        }
        // In any case, we change the body
        // of the "lambda expression" to a 
        // "begin" form
        re = beginify(re);
        // On fait une passe sur le corps
        // pour repérer les "define"
        re = checkdefine(re);
        // On peut construire la "lambda"
        exp=Gdm.list(vl,re,BCLMBDA);
        break;


      case CIF :
        {
           if (w == 3)
           {
              // (if a b) => (and a b)
              exp=comp(Gdm.cons(CAND,d), lvl, env);
           }
           else
           {
              int vtst;
              if (w != 4)
              {
//str.println("Comp 0004");
                throw new SchRunTimeException(ERRSYN);
              }
              vtst = comp(Gdm.car(d), lvl, env);
              if (vtst == CTRUE)
                  exp=comp(Gdm.cadr(d), lvl, env);
              else
              if (vtst == CFALSE)
                  exp=comp(Gdm.caddr(d), lvl, env);
              else
                  exp=Gdm.list(comp(Gdm.caddr(d), lvl, env),
                        comp(Gdm.cadr(d), lvl, env), BCIF, vtst);
           }
        }
        break;


      case CCOND :
        {
           if (w < 2)
             throw new SchRunTimeException(ERRSYN);
           int k = Gdm.car(d);
           int n = 0;
           if (Gdm.celltyp[k] != TPAIR)
             throw new SchRunTimeException(ERRSYN);
           // Length of 1st subclause
           n = ConsOps.len(k);
           if (Gdm.car(k) == CELSE)
           {
              if (w !=2)
                 throw new SchRunTimeException(ERRSYN);
              exp=comp(Gdm.cons(CBEGIN, Gdm.cdr(k)), lvl, env);
           }
           else
           {
              if (w == 2)
              {
                 // The last one
                 switch (n)
                 {
                 case 0 :
                 case -1 :
                 case -2 :
                     throw new SchRunTimeException(ERRSYN);
                 case 1 :
                     exp=comp(Gdm.car(k), lvl, env);
                     break;
                 case 2 :
                     exp=comp(Gdm.cons(CIF, k), lvl, env);
                     break;
                 default :
                     if (Gdm.cadr(k) == CAPPLY)
                     {
                         exp=Gdm.list(
                             CFALSE,
                             comp(Gdm.caddr(k), lvl, env),
                             BCCOND,
                             comp(Gdm.car(k), lvl, env));
                     }
                     else
                     {
                         exp=comp(Gdm.cons(CIF, Gdm.cons(Gdm.car(k),
                             Gdm.list(Gdm.cons(CBEGIN, Gdm.cdr(k))))), lvl, env);
                     }
                 }
              }
              else
              {
                 // Not the last one
                 switch (n)
                 {
                 case 0 :
                 case -1 :
                 case -2 :
                     throw new SchRunTimeException(ERRSYN);
                 case 1 :
                     exp=comp(Gdm.list(COR, Gdm.car(k),
                              Gdm.cons(CCOND, Gdm.cdr(d))), lvl, env);
                     break;
                 case 2 :
                     exp=comp(Gdm.list(CIF, Gdm.car(k), Gdm.cadr(k),
                          Gdm.cons(CCOND, Gdm.cdr(d))), lvl, env);
                     break;
                 default :
                     if (Gdm.cadr(k) == CAPPLY)
                     {
                         exp=Gdm.list(
                             comp(Gdm.cons(CCOND, Gdm.cdr(d)), lvl, env),
                             comp(Gdm.caddr(k), lvl, env),
                             BCCOND,
                             comp(Gdm.car(k), lvl, env));
                     }
                     else
                     {
/*
                         int ww = Gdm.list(CIF, Gdm.car(k),
                              Gdm.cons(CBEGIN, Gdm.cdr(k)),
                              Gdm.cons(CCOND, Gdm.cdr(d)));
str.print("Cond exp=");
str.println(ww);
                         exp=comp(ww, lvl, env);
*/
                         exp=comp(Gdm.list(CIF, Gdm.car(k),
                              Gdm.cons(CBEGIN, Gdm.cdr(k)),
                              Gdm.cons(CCOND, Gdm.cdr(d))), lvl, env);
                     }
                     break;
                 }
                 
                 //   if (Gdm.celltyp[k] == TPAIR)
                 //       throw new SchRunTimeException(255);
              }
           }
        }
        break;


      case CCASE :
        {
           int k, e, l, g, tmp, m, t;
           if (w < 3)
             throw new SchRunTimeException(ERRSYN);
           // Creation du code
           m = tmp = 0;
           k = Gdm.cellcar[d];
           exp = CNULL;
           // Analyse des differentes clauses
           d = Gdm.cellcdr[d];
           while (Gdm.celltyp[d] == TPAIR)
           {
               // The clause
               e = Gdm.cellcar[d];
               d = Gdm.cellcdr[d];
               if (Gdm.celltyp[e] != TPAIR)
                   throw new SchRunTimeException(ERRSYN);
               // The list
               l = Gdm.cellcar[e];
               e = Gdm.cellcdr[e];
               g = Gdm.cons(CBEGIN, e);
               t = Gdm.celltyp[l];
               if ((t != TPAIR) && (t != TNULL))
               {
                   //if ((l != CELSE) || (exp == 0) || (Gdm.celltyp[d] != TNULL))
                   if ((l != CELSE) || (Gdm.celltyp[d] != TNULL))
                       throw new SchRunTimeException(ERRSYN);
                   // The last clause is an "else"
                   if (exp == 0)
                   {
                       // It is also the first
                       m = Gdm.list(comp(g, lvl, env), BCPOPV, comp(k, lvl, env));
                   }
                   else
                   {
                       // It is not the first
                       m = Gdm.list(comp(g, lvl, env), BCPOPV);
                   }
               }
               else
               {
                   // On imagine que on se debrouille 
                   if (exp == 0)
                   {
                       // First generated code
                       m = Gdm.cons(BCFALSE, Gdm.list(comp(g, lvl, env), l,
                                          BCCASE, comp(k, lvl, env)));
                   }
                   else
                   {
                       m = Gdm.list(BCFALSE, comp(g, lvl, env), l,
                                      BCCASE);
                   }
               }
               if (exp == 0)
               {
                   exp = m; tmp = exp;
               }
               else
               {
                   Gdm.cellcar[tmp] = m; tmp = m;
               }
           }
        }
        break;


      case CBEGIN :
        if (w == 1)
        {
          exp=CNULL;
        }
        else
        if (w == 2)
        {
          exp=comp(Gdm.car(d), lvl, env);
        }
        else
        {
          exp=Gdm.cdr(beginify(d));
          y=CNULL;
          int v;
          while (exp != CNULL)
          {
              v = comp(Gdm.car(exp), lvl, env);
              exp = Gdm.cdr(exp);
              if ((exp == CNULL) || ((v != CNULL) && (v != CTRUE)
                  && (v != CFALSE)))
              {
                  if (y != CNULL)
                      y = Gdm.cons(BCPOPV,y);
                  y = Gdm.cons(v,y);
              }
          }
          if ((Gdm.celltyp[y] == TPAIR) && (Gdm.cellcdr[y] == CNULL))
              y = Gdm.cellcar[y];
          exp = y;
        }
        break;


      case CAND :
        {
           int v1=CTRUE;
           if (w == 1)
           {
             exp=CTRUE;
           }
           else
           if (w == 2)
           {
             exp=comp(Gdm.cadr(exp), lvl, env);
           }
           else
           {
             v1 = comp(Gdm.cadr(exp), lvl, env);
             if (v1 == CTRUE)
                 exp=comp(Gdm.cons(c,Gdm.cddr(exp)),lvl, env);
             else
             if (v1 == CFALSE)
                 exp=CFALSE;
             else
                 exp=Gdm.list(
                   comp(Gdm.cons(c,Gdm.cddr(exp)),
                          lvl, env), BCAND, v1);
           }
        }
        break;


      case COR :
        {
           int v1=CTRUE;
           if (w == 1)
           {
             exp=CFALSE;
           }
           else
           if (w == 2)
           {
             exp=comp(Gdm.cadr(exp), lvl, env);
           }
           else
           {
              v1 = comp(Gdm.cadr(exp), lvl, env);
              if (v1 == CTRUE)
                  exp = CTRUE;
              else
              if (v1 == CFALSE)
                  exp = comp(Gdm.cons(c,Gdm.cddr(exp)),lvl, env);
              else
                  exp=Gdm.list(
                         comp(Gdm.cons(c,Gdm.cddr(exp)),
                              lvl, env), BCOR, v1);
           }
        }
        break;


      case CLETR :
        {
           // (letrec ([id val]... ) insts...)
           exp=completrec(exp, lvl, env);
        }
        break;


      case CLETE :
        {
           // (let* ([id val]... ) insts...)
           exp=completet(exp, lvl, env);
        }
        break;


      case CLET :
        {
           // (let ([id val]... ) insts...)
           exp=complet(exp, lvl, env);
        }
        break;


      case CDEFINE :
        {
          exp = definify(exp);
          id = Gdm.cadr(exp);
          exp=Gdm.list(
               Environment.dclnew(env,id),
               BCDEF,
               comp(Gdm.caddr(exp), lvl, env));
          break;
        }


      case CSET :
          if (w != 3)
          {
//str.println("Comp 0005");
             throw new SchRunTimeException(ERRSYN);
          }
          id = Gdm.cadr(exp);
          Check.id(id);
          exp=Gdm.list(
               //Environment.dcl(env,id),
               comp(id,lvl,env),
               BCSET,
               comp(Gdm.caddr(exp), lvl, env));
        break;


      case CDELAY :
          if (w != 2)
          {
//str.println("Comp 0006");
             throw new SchRunTimeException(ERRSYN);
          }
          exp=Gdm.list(
               BCDELAY,
               comp(Gdm.list(CLAMBDA,CNULL,Gdm.cadr(exp)),
                     lvl, env));
          break;


      case CQQUOTE :
          if (w != 2)
          {
//str.println("Comp 0100");
            throw new SchRunTimeException(ERRSYN);
          }
if ((trace & TRPBQOP) != 0)
{
    str.print("backquote::");
    str.println(exp);
}
          exp=qqtfy(Gdm.car(d),1);
if ((trace & TRPBQOP) != 0)
{
    str.print("backquote=>");
    str.println(exp);
}
          exp=comp(exp, lvl, env);
          break;


      case CUQUOTE :
      case CUQUOTES :
          throw new SchRunTimeException(ERRSYN);


      default :
          doexp = exp;
          break;
      } // switch


      } // if
      else
      {
        doexp = exp;
      }
      break;


    case TSYM :
      if (ConsOps.memq(exp,lvl) > 0)
      {
        // Local Symbol
/*
str.println("Local symbol : "+(String)Gdm.cellobj[exp]);
*/
      }
      else
      {
        // Global
        int vc = Environment.getvc(env, exp);
        if (vc <= 0)
            vc = Environment.dcl(env, exp);
/*
str.println("Global symbol : "+(String)Gdm.cellobj[exp]+
     " "+vc);
*/
        exp = vc;
      }
      break;
    }

    if (doexp != CNULL)
    {
      int res, k, nc, v;
      if (w <= MAXARGC+1)
      {
        res = Gdm.list(BCF00+w-1);
        k = res;
      }
      else
      {
        k = Gdm.list(BCFnn);
        res = Gdm.cons(Gdm.newint(w-1),k);
      }
      // Fonction et Opérandes
      while (doexp != CNULL)
      {
        nc=comp(Gdm.cellcar[doexp], lvl, env);
        v=Gdm.list(nc);
        Gdm.cellcdr[k]=v;
        k=v;
        doexp=Gdm.cellcdr[doexp];
      }
      exp = res;
    }
    return exp;
  }


    /**
        Compile LET expressions
    */
    private static int complet(int exp, int lvl, int env)
    throws SchRunTimeException
    {
        int kw, dcl, ist, ids, vls, clvl, fname;
        int id, x, y, v, res;
        // Verif. syntaxe
        // (let ([id val] ...) exprs)
        kw = Gdm.car(exp);
        dcl = Gdm.cadr(exp);
        if (Gdm.typ(dcl) == TSYM)
        {
           /* Syntax :
                  (letrec <name> ((<v1> <e1>) ...) <body>)
              We change this to :
                  (letrec ((<name> (lambda (<v1> ...) <body>)
                        (name <e1> ...))
           */
/*
str.print("Comp exp="); str.println(exp);
*/
           fname = dcl;
           v = Gdm.cddr(exp);
           dcl = Gdm.car(v);
           ist = Gdm.cdr(v);
           ids = CNULL; vls = CNULL;
           while (Gdm.typ(dcl) == TPAIR)
           {
               x = Gdm.car(dcl);
               if (Gdm.typ(x) != TPAIR)
                  throw new SchRunTimeException(ERRSYN);
               id = Gdm.car(x);
               if (Gdm.typ(id) != TSYM)
                  throw new SchRunTimeException(ERRSYN);
               ids = Gdm.cons(id, ids);
               x = Gdm.cdr(x);
               if ((Gdm.typ(x) != TPAIR) || (Gdm.cdr(x) != CNULL))
                  throw new SchRunTimeException(ERRSYN);
               v = Gdm.car(x);
               vls = Gdm.cons(v, vls);
               dcl = Gdm.cdr(dcl);
           }
           x = CNULL;
           dcl = CNULL;
           while (Gdm.typ(ids) != CNULL)
           {
              x = Gdm.cons(Gdm.car(vls), x);
              dcl = Gdm.cons(Gdm.car(ids), dcl);
              ids = Gdm.cdr(ids);
              vls = Gdm.cdr(vls);
           }
           res = Gdm.list(Gdm.list(fname, Gdm.cons(CLAMBDA, 
                    Gdm.cons(dcl, ist))));
           Gdm.cellcdr[exp] = Gdm.list(res, Gdm.cons(fname, x));
/*
str.print("Comp exp=>"); str.println(exp);
*/
           dcl = Gdm.cadr(exp);
        }
        ist = Gdm.cddr(exp);
        if (Gdm.cdr(ist) == CNULL)
            ist = Gdm.car(ist);
        else
            ist = beginify(ist);
        ids = CNULL; vls = CNULL;
        clvl = lvl;
        // Recuperation des locales
        while (Gdm.typ(dcl) == TPAIR)
        {
           x = Gdm.car(dcl);
           if (Gdm.typ(x) != TPAIR)
           {
//str.println("Comp 0007");
              throw new SchRunTimeException(ERRSYN);
           }
           id = Gdm.car(x);
           if (Gdm.typ(id) != TSYM)
           {
//str.println("Comp 0008");
              throw new SchRunTimeException(ERRSYN);
           }
           ids = Gdm.cons(id, ids);
           x = Gdm.cdr(x);
           if (Gdm.typ(x) != TPAIR)
           {
//str.println("Comp 0009");
              throw new SchRunTimeException(ERRSYN);
           }
           if (Gdm.cdr(x) != CNULL)
           {
//str.println("Comp 0010");
              throw new SchRunTimeException(ERRSYN);
           }
           v = Gdm.car(x);
           v = comp(v, clvl, env);
           vls = Gdm.cons(v, vls);
           dcl = Gdm.cdr(dcl);
        }
        if (Gdm.typ(dcl) != TNULL)
        {
//str.println("Comp 0011");
           throw new SchRunTimeException(ERRSYN);
        }
        // Génération du code
        res = vls;
        while (Gdm.typ(ids) != CNULL)
        {
           id = Gdm.car(ids);
           res = Gdm.cons(id,
                     Gdm.cons(BCADENV,res));
           clvl = Gdm.cons(id, clvl);
           ids = Gdm.cdr(ids);
        }
        res = Gdm.cons(comp(ist, clvl, env), res);
        res = Gdm.list(res, BCLET);
        return res;
    }



    /**
        Compile LET* expressions
    */
    private static int completet(int exp, int lvl, int env)
    throws SchRunTimeException
    {
        int kw, dcl, ist, clvl;
        int id, x, y, v, res;
        // Verif. syntaxe
        // (let ([id val] ...) exprs)
        kw = Gdm.car(exp);
        dcl = Gdm.cadr(exp);
        ist = Gdm.cddr(exp);
        if (Gdm.cdr(ist) == CNULL)
            ist = Gdm.car(ist);
        else
            ist = beginify(ist);
        clvl = lvl;
        // Recuperation des locales
        // et génération du code
        res = CNULL;
        while (Gdm.typ(dcl) == TPAIR)
        {
           x = Gdm.car(dcl);
           if (Gdm.typ(x) != TPAIR)
           {
//str.println("Comp 0012");
              throw new SchRunTimeException(ERRSYN);
           }
           id = Gdm.car(x);
           if (Gdm.typ(id) != TSYM)
           {
//str.println("Comp 0013");
              throw new SchRunTimeException(ERRSYN);
           }
           x = Gdm.cdr(x);
           if (Gdm.typ(x) != TPAIR)
           {
//str.println("Comp 0014");
              throw new SchRunTimeException(ERRSYN);
           }
           if (Gdm.cdr(x) != CNULL)
           {
//str.println("Comp 0015");
              throw new SchRunTimeException(ERRSYN);
           }
           v = Gdm.car(x);
           // Compilation dans l'environnement
           v = comp(v, clvl, env);
           res = Gdm.cons(id,
                    Gdm.cons(BCADENV,
                       Gdm.cons(v,res)));
           clvl = Gdm.cons(id, clvl);
           dcl = Gdm.cdr(dcl);
        }
        if (Gdm.typ(dcl) != TNULL)
        {
//str.println("Comp 0016");
           throw new SchRunTimeException(ERRSYN);
        }
        res = Gdm.cons(comp(ist, clvl, env), res);
        res = Gdm.list(res, BCLET);
        return res;
    }



    /**
        Compile LETREC expressions
    */
    private static int completrec(int exp, int lvl, int env)
    throws SchRunTimeException
    {
        int kw, dcl, ist, ids, vls, clvl;
        int id, sids, x, y, v, res;
        // Verif. syntaxe
        kw = Gdm.car(exp);
        dcl = Gdm.cadr(exp);
        // Handle the syntax :
        //    (let ([id val] ...) exprs)
        ist = Gdm.cddr(exp);
        if (Gdm.cdr(ist) == CNULL)
            ist = Gdm.car(ist);
        else
            ist = beginify(ist);
        ids = CNULL; vls = CNULL;
        clvl = lvl;
        // Recuperation des locales
        while (Gdm.typ(dcl) == TPAIR)
        {
           x = Gdm.car(dcl);
           if (Gdm.typ(x) != TPAIR)
           {
//str.println("Comp 0017");
              throw new SchRunTimeException(ERRSYN);
           }
           id = Gdm.car(x);
           if (Gdm.typ(id) != TSYM)
           {
//str.println("Comp 0018");
              throw new SchRunTimeException(ERRSYN);
           }
           ids = Gdm.cons(id, ids);
           x = Gdm.cdr(x);
           if (Gdm.typ(x) != TPAIR)
           {
//str.println("Comp 0019");
              throw new SchRunTimeException(ERRSYN);
           }
           if (Gdm.cdr(x) != CNULL)
           {
//str.println("Comp 0020");
              throw new SchRunTimeException(ERRSYN);
           }
           v = Gdm.car(x);
           vls = Gdm.cons(v, vls);
           dcl = Gdm.cdr(dcl);
           clvl = Gdm.cons(id, clvl);
        }
        if (Gdm.typ(dcl) != TNULL)
        {
//str.println("Comp 0021");
           throw new SchRunTimeException(ERRSYN);
        }
        // Génération du code
        res = CNULL;
        sids = ids;
        while (Gdm.typ(ids) != CNULL)
        {
           id = Gdm.car(ids);
           v = Gdm.car(vls);
           res = Gdm.cons(id,
                     Gdm.cons(BCSETU,
                        Gdm.cons(comp(v, clvl, env),res)));
           ids = Gdm.cdr(ids);
           vls = Gdm.cdr(vls);
        }
        res = Gdm.cons(comp(ist, clvl, env), res);
        res = Gdm.list(res, sids, BCLETREC);
        return res;
    }


    /**
        Generate a "begin" in front of an s-exp list ;
        Aggregate inbedded begins
    */
    private static int beginify(int exp)
    throws SchRunTimeException
    {
        int todo = exp;
        int res = CNULL;
        int cc = CNULL, k, w;
        while (todo != CNULL)
        {
            w = Gdm.car(todo);
            todo = Gdm.cdr(todo);
            if ((Gdm.typ(w) == TPAIR) && (Gdm.car(w) == CBEGIN))
            {
                todo = ConsOps.lappend(Gdm.cdr(w), todo);
            }
            else
            {
                if (res == CNULL)
                {
                    res = cc = Gdm.list(w);
                    Gdm.protect(res);
                }
                else
                {
                    k = Gdm.list(w);
                    Gdm.cellcdr[cc] = k;
                    cc = k;
                }
            }
        }
        return Gdm.cons(CBEGIN, res);
    }


    /**
         Handle (define name)
                (define name value)
                (define (list) inst ...)
    */
    private static int definify(int exp)
    throws SchRunTimeException
    {
       int id, w = ConsOps.len(exp);
       Gdm.protect(exp);
       if (w == 2)
       {
          exp=Gdm.list(Gdm.car(exp), Gdm.cadr(exp), CNULL);
       }
       else
       if (w > 3)
       {
          exp=Gdm.list(Gdm.car(exp), Gdm.cadr(exp),
                          beginify(Gdm.cddr(exp)));
       }
       else
       if (w != 3)
       {
//str.println("Comp 0022");
          throw new SchRunTimeException(ERRSYN);
       }
       id = Gdm.cadr(exp);
       while (Gdm.typ(id) == TPAIR)
       {
          /** expend (define (exp list) inst)
              to     (define exp (lambda (list) inst))
          */
          exp=Gdm.list(CDEFINE, Gdm.car(id),
                  Gdm.cons(CLAMBDA, 
                      Gdm.cons(Gdm.cdr(id), Gdm.cddr(exp))));
          id = Gdm.cadr(exp);
       }
       Check.id(id);
       return exp;
    }

    /**
        Rechercher les "define" dans un "begin"
    */
    private static int checkdefine(int exp)
    throws SchRunTimeException
    {
       int cnt=0, x, wl=Gdm.cdr(exp);
       int recl = CNULL;
       boolean bf=true;
       while (Gdm.typ(wl) == TPAIR && bf)
       {
           x = Gdm.cellcar[wl];
           if ((Gdm.typ(x) == TPAIR) &&
               (Gdm.car(x) == CDEFINE))
           {
               x = definify(x);
               cnt++;
               Gdm.cellcar[wl]=x;
               recl = Gdm.cons(Gdm.cdr(x), recl);
               wl = Gdm.cellcdr[wl];
           }
           else
           {
               bf = false;
           }
       }
       if (cnt > 0)
       {
           exp = Gdm.cons(CLETR,
                    Gdm.cons(recl, wl));
       }
//str.println(exp,"Checkdefine ");
       return exp;
    }


    /*
        Handle "quasi-quote"
    */
    private static int qqtfy(int exp, int lev)
    throws SchRunTimeException
    {
        //int w = ConsOps.len(exp);
if ((trace & TRPBQOP2) != 0)
{
    str.print("qqtfy("+lev+"):");
    str.println(exp);
}
        if (lev <= 0)
            return simplify(exp);

        boolean cy=true;
        int a, d, u, v, z, op;
        switch (Gdm.celltyp[exp])
        {
        case TPAIR :
            a = Gdm.cellcar[exp];
            d = Gdm.cellcdr[exp];
            switch (a)
            {
            case CUQUOTE :
                exp = Gdm.cellcar[d];
                break;
            default :
                op = refcons;
                z = -1;
                if (Gdm.celltyp[a] == TPAIR)
                {
                    u = Gdm.cellcar[a];
                    v = Gdm.cellcdr[a];
                    switch (u)
                    {
                    case CQQUOTE : 
                        z = qqtfy(qqtfy(Gdm.cellcar[v], lev+1), lev);
                        break;
                    case CUQUOTE : 
                        if (lev <= 1)
                        {
                           z = Gdm.cellcar[v];
                        }
                        break;
                    case CUQUOTES :
                        if (lev <= 1)
                        {
                           z = Gdm.cellcar[v];
                           op = refappend;
                        }
                        break;
                    }
                }
                if (z == -1)
                    z = qqtfy(a, lev);
                exp = Gdm.list(op, z, qqtfy(d, lev));
                break;
            }
            break;
        default :
            if (lev == 1)
                exp = Gdm.list(CQUOTE,exp);
            else
                exp = Gdm.list(CQQUOTE,exp);
        }
        return simplify(exp);
    }

    
    private static int simplify(int exp)
    throws SchRunTimeException
    {
        if (Gdm.celltyp[exp] == TPAIR)
        {
//str.print("simplify:");
//str.println(exp);
            int w = ConsOps.len(exp);
            int a = Gdm.cellcar[exp];
            if ((a == refcons) && (w == 3))
            {
                // (cons 'x 'y) => '(x . y)
                int x = Gdm.cadr(exp);
                int y = Gdm.caddr(exp);
                int tx = Gdm.celltyp[x];
                int ty = Gdm.celltyp[y];
                int op = -1;
                if ((tx == TPAIR) && (ty == TPAIR))
                {
                   if ((Gdm.car(x) == CQUOTE)
                       && (Gdm.car(y) == CQUOTE))
                       op = CQUOTE;
                   else
                   if ((Gdm.car(x) == CQQUOTE)
                       && (Gdm.car(y) == CQQUOTE))
                       op = CQQUOTE;
                   if (op != -1)
                       exp = Gdm.list(op,
                          Gdm.cons(Gdm.cadr(x), Gdm.cadr(y)));
                }
            }
        }
if ((trace & TRPBQOP2) != 0)
{
    str.print("simplify=>");
    str.println(exp);
}
        return exp;
    }

    
}

