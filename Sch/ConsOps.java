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
 * File: ConsOps.java
 * Author:
 * Description:
 *
 * $Id: ConsOps.java,v 1.4 2003/11/24 10:54:30 serpaggi Exp $
 */

package Sch;

/**
 *
 *  All the operations on pairs
 *
 *
 * Implements the following procedures
 * <UL>
 * <LI><B>idt</B> returns its parameter
 * <LI><B>car</B> returns the car of its parameter
 * <LI><B>cdr</B> returns the cdr of its parameter
 * <LI><B>cons</B> creates a new pair, whore car and cdr are the first
 *                 and second parameter of the procedure
 * <LI><B>null?</B> tests for the empty list
 * <LI><B>list?</B> tests for any list
 * <LI><B>pair?</B> tests for a pair
 * <LI><B>length</B> returns the length of a list
 * <LI><B>list</B> creates a new list
 * <LI><B>copy</B> creates a copy at the first level of its parameter
 * <LI><B>deep-copy</B> duplicates a complete structure
 * <LI><B>eq?</B> comparison predicate
 * <LI><B>eqv?</B> comparison predicate
 * <LI><B>equal?</B> comparison predicate
 * <LI><B>memq</B> seach in a list, using <B>eq?</B> as comparison predicate
 * <LI><B>memv</B> seach in a list, using <B>eqv?</B> as comparison predicate
 * <LI><B>member</B> seach in a list, using <B>equal?</B> as comparison predicate
 * <LI><B>assq</B> seach in an association list, using <B>eq?</B>
 *                 as comparison predicate
 * <LI><B>assv</B> seach in an association list, using <B>eqv</B>
 *                 as comparison predicate
 * <LI><B>assoc</B> seach in an association list, using <B>equal?</B>
 *                 as comparison predicate
 * <LI><B>append</B> create a new list by appending copies of its parameters
 * <LI><B>append!</B> create a new list by physically appending its parameters
 * <LI><B>reverse</B> create a new list whose elements are those of its
 *                    parameter, in the reverse order
 * <LI><B>reverse!</B> create a new list by physically reversing its parameter
 * <LI><B>set-car!</B> replaces the car of the first parameter by the second
 * <LI><B>set-cdr!</B> replaces the cdr of the first parameter by the second
 * <LI><B>list-ref</B> returns the nth element of its first parameter
 * <LI><B>list-tail</B> returns the nth cdr of its first parameter
 * </UL>
 *
 *     @author Jean-Jacques Girardot
 */


public class ConsOps extends SchPrimitive {

    private static ConsOps proto;

    public static final int OPCAR = 1;
    public static final int OPCDR = 2;
    public static final int OPCONS = 3;
    public static final int OPNULLQ = 4;
    public static final int OPLISTQ = 5;
    public static final int OPPAIRQ = 6;
    public static final int OPLLEN = 7;
    public static final int OPLIST = 8;
    public static final int OPDCOPY = 9;
    public static final int OPEQ = 10;
    public static final int OPEQV = 11;
    public static final int OPEQL = 12;
    public static final int OPMEMQ = 13;
    public static final int OPMEMV = 14;
    public static final int OPMEMB = 15;
    public static final int OPASSQ = 16;
    public static final int OPASSV = 17;
    public static final int OPASSOC = 18;
    public static final int OPAPPND = 19;
    public static final int OPCOPY = 20;
    public static final int OPSCAR = 21;
    public static final int OPSCDR = 22;
    public static final int OPREV = 23;
    public static final int OPNREV = 24;
    public static final int OPLREF = 25;
    public static final int OPLTAIL = 26;
    public static final int OPPAPPND = 27;
    public static final int OPIDT = 28;

    public static final int NBOP = OPIDT+1;

    public static int lcell = 0;

    public static void dcl()
    {
        proto = new ConsOps();
        proto.fnames = new String[NBOP];

        proto.fnames[OPCAR] = "car";
        proto.fnames[OPCDR] = "cdr";
        proto.fnames[OPCONS] = "cons";
        proto.fnames[OPNULLQ] = "null?";
        proto.fnames[OPPAIRQ] = "pair?";
        proto.fnames[OPLISTQ] = "list?";
        proto.fnames[OPLLEN] = "length";
        proto.fnames[OPLIST] = "list";
        proto.fnames[OPCOPY] = "copy";
        proto.fnames[OPDCOPY] = "deep-copy";
        proto.fnames[OPMEMQ] = "memq";
        proto.fnames[OPMEMV] = "memv";
        proto.fnames[OPMEMB] = "member";
        proto.fnames[OPASSQ] = "assq";
        proto.fnames[OPASSV] = "assv";
        proto.fnames[OPASSOC] = "assoc";
        proto.fnames[OPEQ] = "eq?";
        proto.fnames[OPEQV] = "eqv?";
        proto.fnames[OPEQL] = "equal?";
        proto.fnames[OPAPPND] = "append";
        proto.fnames[OPPAPPND] = "append!";
        proto.fnames[OPSCAR] = "set-car!";
        proto.fnames[OPSCDR] = "set-cdr!";
        proto.fnames[OPREV] = "reverse";
        proto.fnames[OPNREV] = "reverse!";
        proto.fnames[OPLREF] = "list-ref";
        proto.fnames[OPLTAIL] = "list-tail";
        proto.fnames[OPIDT] = "idt";

        Environment.dcl(proto);

        refappend = Environment.get(systenv, "append");
        refcons = Environment.get(systenv, "cons");
    }

    public void eval(int op, int count, Evaluator evl)
    throws SchRunTimeException
    {
        int cell, res;
        switch (op)
        {


        case OPCAR :
           if (count != 1)
              throw new SchRunTimeException(ERR1AR);
           cell = evl.valstk[evl.ptvalstk-1];
           if (Gdm.celltyp[cell] != TPAIR)
              throw new SchRunTimeException(ERRCXP);
           evl.valstk[evl.ptvalstk-1] = Gdm.cellcar[cell];
           break;


        case OPCDR :
           if (count != 1)
              throw new SchRunTimeException(ERR1AR);
           cell = evl.valstk[evl.ptvalstk-1];
           if (Gdm.celltyp[cell] != TPAIR)
              throw new SchRunTimeException(ERRCXP);
           evl.valstk[evl.ptvalstk-1] = Gdm.cellcdr[cell];
           break;


        case OPCONS :
           if (count != 2)
              throw new SchRunTimeException(ERR2AR);
           // Le resultat n'a pas a etre protege
           cell = Gdm.newcons(TPAIR);
           Gdm.cellcar[cell]=evl.valstk[evl.ptvalstk-1];
           evl.ptvalstk--;
           Gdm.cellcdr[cell]=evl.valstk[evl.ptvalstk-1];
           evl.valstk[evl.ptvalstk-1] = cell;
           break;


        case OPNULLQ :
           if (count != 1)
              throw new SchRunTimeException(ERR1AR);
           cell = evl.valstk[evl.ptvalstk-1];
           evl.valstk[evl.ptvalstk-1] = 
               (Gdm.typ(cell) == TNULL) ? CTRUE : CFALSE;
           break;


        case OPPAIRQ :
           if (count != 1)
              throw new SchRunTimeException(ERR1AR);
           cell = evl.valstk[evl.ptvalstk-1];
           evl.valstk[evl.ptvalstk-1] = 
               (Gdm.typ(cell) == TPAIR) ? CTRUE : CFALSE;
           break;


        case OPLISTQ :
           {
              if (count != 1)
                 throw new SchRunTimeException(ERR1AR);
              cell = evl.valstk[evl.ptvalstk-1];
              int w = ConsOps.len(cell);
              evl.valstk[evl.ptvalstk-1] = (w >= 0) ? CTRUE : CFALSE;
           }
           break;


        case OPLLEN :
           {
              if (count != 1)
                 throw new SchRunTimeException(ERR1AR);
              cell = evl.valstk[evl.ptvalstk-1];
              int w = ConsOps.len(cell);
              if (w < 0)
                 throw new SchRunTimeException(ERRLXP);
              evl.valstk[evl.ptvalstk-1] = Gdm.newint(w);
           }
           break;


        case OPAPPND :
           {
              res = cell= CNULL;
              int lst = CNULL;
              byte ty = TNULL;
              int el, cp;
              if (count == 0)
              {
                  evl.push(CNULL);
              }
              else
              if (count == 1)
              {
                  // laisse le resultat en place
              }
              else
              {
                // 2 operands or more
                while (count > 0)
                {
                  count --;
                  el = evl.valstk[evl.ptvalstk-1];
                  ty = Gdm.celltyp[el];
                  if (ty == TPAIR)
                  {
                     if (count == 0)
                        cp = el;
                     else
                        cp = lcopy(el);
                     if (res == CNULL)
                     {
                         res = cp;
                     }
                     else
                     {
                         Gdm.cellcdr[lst]=cp;
                     }
                     lst = lcell;
                  }
                  else
                  {
                     if (count == 0)
                     {
                         if (res == CNULL)
                         {
                             res = el;
                         }
                         else
                         {
                             Gdm.cellcdr[lst]=el;
                         }
                     }
                     else
                     if (ty != TNULL) 
                     {
                         throw new SchRunTimeException(ERRLXP);
                     }
                  }
                  evl.ptvalstk--;
                }
                evl.push(res);
              }
           }
           break;


        case OPPAPPND :
           {
              res = cell= CNULL;
              int lst = CNULL;
              byte ty = TNULL;
              int el, cp;
              if (count == 0)
              {
                  evl.push(CNULL);
              }
              else
              if (count == 1)
              {
                  // laisse le resultat en place
              }
              else
              {
                // 2 operands or more
                //
                // During the loop, no cell is created, so we
                // don't need to be protected from a gabage collection
                while (count > 0)
                {
                  count --;
                  el = evl.valstk[evl.ptvalstk-1];
                  ty = Gdm.celltyp[el];
                  if (ty == TPAIR)
                  {
                     cp = el;
                     if (res == CNULL)
                     {
                         res = cp;
                     }
                     else
                     {
                         if (lst != CNULL)
                             Gdm.cellcdr[lst]=cp;
                     }
                     // recherche de la derniere cellule
                     int cntr = Gdm.cellcount;
                     while ((ty == TPAIR) && (cntr > 0))
                     {
                         lst = el;
                         cntr--;
                         el = Gdm.cellcdr[el];
                         ty = Gdm.celltyp[el];
                     }
                  }
                  else
                  {
                     if (count == 0)
                     {
                         if (res == CNULL)
                         {
                             res = el;
                         }
                         else
                         {
                             Gdm.cellcdr[lst]=el;
                         }
                     }
                     else
                     if (ty != TNULL) 
                     {
                         throw new SchRunTimeException(ERRLXP);
                     }
                  }
                  evl.ptvalstk--;
                }
                evl.push(res);
              }
           }
           break;


        case OPLIST :
           {
              res = cell= CNULL;
              while (count > 0)
              {
                  int c = Gdm.newcons(TPAIR,0,CNULL);
                  int el = evl.pop();
                  Gdm.cellcar[c]=el;
                  if (res == CNULL)
                  {
                     // Protection du resultat
                     res = c;
                     Gdm.protect(res);
                  }
                  else
                  {
                     Gdm.cellcdr[cell]=c;
                  }
                  cell = c;
                  count--;
              }
              evl.push(res);
           }
           break;


        case OPCOPY :
           if (count != 1)
              throw new SchRunTimeException(ERR1AR);
           cell = evl.valstk[evl.ptvalstk-1];
           cell = lcopy(cell);
           evl.valstk[evl.ptvalstk-1]=cell;
           break;


        case OPDCOPY :
           if (count != 1)
              throw new SchRunTimeException(ERR1AR);
           cell = evl.valstk[evl.ptvalstk-1];
           cell = Gdm.dupcell(cell);
           evl.valstk[evl.ptvalstk-1]=cell;
           break;


        case OPMEMQ :
           {
              if (count != 2)
                 throw new SchRunTimeException(ERR2AR);
              cell = evl.valstk[evl.ptvalstk-1];
              int w = evl.valstk[evl.ptvalstk-2];
              res =ConsOps.memq(cell,w);
              evl.ptvalstk--;
              evl.valstk[evl.ptvalstk-1] = res <= 0 ? CFALSE : res ;
           }
           break;


        case OPMEMV :
           {
              if (count != 2)
                 throw new SchRunTimeException(ERR2AR);
              cell = evl.valstk[evl.ptvalstk-1];
              int w = evl.valstk[evl.ptvalstk-2];
              res =ConsOps.memv(cell,w);
              evl.ptvalstk--;
              evl.valstk[evl.ptvalstk-1] = res <= 0 ? CFALSE : res ;
           }
           break;


        case OPMEMB :
           {
              if (count != 2)
                 throw new SchRunTimeException(ERR2AR);
              cell = evl.valstk[evl.ptvalstk-1];
              int w = evl.valstk[evl.ptvalstk-2];
              res =ConsOps.member(cell,w);
              evl.ptvalstk--;
              evl.valstk[evl.ptvalstk-1] = res <= 0 ? CFALSE : res ;
           }
           break;


        case OPASSQ :
           {
              if (count != 2)
                 throw new SchRunTimeException(ERR2AR);
              cell = evl.valstk[evl.ptvalstk-1];
              int w = evl.valstk[evl.ptvalstk-2];
              res =ConsOps.assq(cell,w);
              evl.ptvalstk--;
              evl.valstk[evl.ptvalstk-1] = res <= 0 ? CFALSE : res ;
           }
           break;


        case OPASSV :
           {
              if (count != 2)
                 throw new SchRunTimeException(ERR2AR);
              cell = evl.valstk[evl.ptvalstk-1];
              int w = evl.valstk[evl.ptvalstk-2];
              res =ConsOps.assv(cell,w);
              evl.ptvalstk--;
              evl.valstk[evl.ptvalstk-1] = res <= 0 ? CFALSE : res ;
           }
           break;


        case OPASSOC :
           {
              if (count != 2)
                 throw new SchRunTimeException(ERR2AR);
              cell = evl.valstk[evl.ptvalstk-1];
              int w = evl.valstk[evl.ptvalstk-2];
              res =ConsOps.assoc(cell,w);
              evl.ptvalstk--;
              evl.valstk[evl.ptvalstk-1] = res <= 0 ? CFALSE : res ;
           }
           break;


        case OPEQ :
           {
              if (count != 2)
                 throw new SchRunTimeException(ERR2AR);
              cell = evl.pop();
              int w = evl.pop();
              evl.push(w == cell ? CTRUE : CFALSE);
           }
           break;


        case OPEQV :
           {
              if (count != 2)
                 throw new SchRunTimeException(ERR2AR);
              cell = evl.pop();
              int w = evl.pop();
              evl.push(eqv(w, cell) ? CTRUE : CFALSE);
           }
           break;


        case OPEQL :
           {
              if (count != 2)
                 throw new SchRunTimeException(ERR2AR);
              cell = evl.pop();
              int w = evl.pop();
              evl.push(equal(w, cell) ? CTRUE : CFALSE);
           }
           break;


        case OPSCAR :
           if (count != 2)
              throw new SchRunTimeException(ERR2AR);
           res = evl.pop();
           cell = evl.pop();
           if (Gdm.celltyp[res] != TPAIR)
              throw new SchRunTimeException(ERRCXP);
           Gdm.cellcar[res] = cell;
           evl.push(res);
           break;


        case OPSCDR :
           if (count != 2)
              throw new SchRunTimeException(ERR2AR);
           res = evl.pop();
           cell = evl.pop();
           if (Gdm.celltyp[res] != TPAIR)
              throw new SchRunTimeException(ERRCXP);
           Gdm.cellcdr[res] = cell;
           evl.push(res);
           break;


        case OPREV :
           if (count != 1)
              throw new SchRunTimeException(ERR1AR);
           cell = evl.valstk[evl.ptvalstk-1];
           res = CNULL;
           while (Gdm.celltyp[cell] == TPAIR)
           {
               res = Gdm.cons(Gdm.cellcar[cell],res);
               cell = Gdm.cellcdr[cell];
           }
           evl.valstk[evl.ptvalstk-1]=res;
           break;


        case OPNREV :
           if (count != 1)
              throw new SchRunTimeException(ERR1AR);
           cell = evl.valstk[evl.ptvalstk-1];
           res = CNULL;
           while (Gdm.celltyp[cell] == TPAIR)
           {
               int x=Gdm.cellcdr[cell];
               Gdm.cellcdr[cell] = res;
               res = cell; cell = x;
           }
           evl.valstk[evl.ptvalstk-1]=res;
           break;


        case OPLREF :
           {
               if (count != 2)
                   throw new SchRunTimeException(ERR2AR);
               cell = evl.valstk[evl.ptvalstk-1];
               int k = evl.valstk[evl.ptvalstk-2];
               if (Gdm.celltyp[k] != TINT)
                   throw new SchRunTimeException(ERRINT);
               k = Gdm.cellcar[k];
               while (k > 0)
               {
                   if (Gdm.celltyp[cell] != TPAIR)
                       throw new SchRunTimeException(ERRCXP);
                   k--;
                   cell = Gdm.cellcdr[cell];
               }
               if (Gdm.celltyp[cell] != TPAIR)
                   throw new SchRunTimeException(ERRCXP);
               res = Gdm.cellcar[cell];
               evl.ptvalstk--;
               evl.valstk[evl.ptvalstk-1]=res;
           }
           break;


        case OPLTAIL :
           {
               if (count != 2)
                   throw new SchRunTimeException(ERR2AR);
               cell = evl.valstk[evl.ptvalstk-1];
               int k = evl.valstk[evl.ptvalstk-2];
               if (Gdm.celltyp[k] != TINT)
                   throw new SchRunTimeException(ERRINT);
               k = Gdm.cellcar[k];
               while (k > 0)
               {
                   if (Gdm.celltyp[cell] != TPAIR)
                       throw new SchRunTimeException(ERRCXP);
                   k--;
                   cell = Gdm.cellcdr[cell];
               }
               evl.ptvalstk--;
               evl.valstk[evl.ptvalstk-1]=cell;
           }
           break;


        case OPIDT :
           {
               res = CNULL;
               while (count > 0)
               {
                   res = evl.pop();
                   count--;
               }
               evl.push(res);
           }
           break;


        default :
           throw new SchRunTimeException(ERRUSP);
        }
    }


    public static int len(int cell)
    {
        int l, c, ty;
        l = 0; c = cell;
        while ((ty = Gdm.celltyp[c]) == TPAIR)
        {
           c = Gdm.cellcdr[c];
           if (l++ > Gdm.cellcount)
              return -2;
        }
        if (ty != TNULL) return -1;
        return l;
    }


    public static boolean eq(int c1, int c2)
    {
         return c1 == c2;
    }


    public static boolean eqv(int c1, int c2)
    {
        byte ty1=Gdm.celltyp[c1];
        byte ty2=Gdm.celltyp[c2];
        if (c1 == c2) return true;
        if (ty1 != ty2) return false;
        if (ty1 == TINT)
           return Gdm.cellcar[c1]==Gdm.cellcar[c2];
        if (ty1 == TSTR)
           return ((((String)Gdm.cellobj[c1]).compareTo(
                (String)Gdm.cellobj[c2])) == 0 ? true : false);
        return false;
    }


    public static boolean equal(int c1, int c2)
    {
        byte ty1=Gdm.celltyp[c1];
        byte ty2=Gdm.celltyp[c2];
        if (c1 == c2) return true;
        if (ty1 != ty2) return false;
        if (ty1 == TINT)
           return Gdm.cellcar[c1]==Gdm.cellcar[c2];
        if (ty1 == TSTR)
           return ((((String)Gdm.cellobj[c1]).compareTo(
                (String)Gdm.cellobj[c2])) == 0 ? true : false);
        if (ty1 == TPAIR || ty1 == TENV || ty1 == TLAMBDA)
           return (equal(Gdm.cellcar[c1], Gdm.cellcar[c2]) &&
                   equal(Gdm.cellcdr[c1], Gdm.cellcdr[c2]) ?
                      true : false);
        return false;
    }


    public static int memq(int cell, int li)
    {
        int l, c, ty;
        l = 0; c = li;
        while ((ty = Gdm.celltyp[c]) == TPAIR)
        {
           if (cell == Gdm.cellcar[c])
               return c;
           c = Gdm.cellcdr[c];
           if (l++ > Gdm.cellcount)
              return -2;
        }
        if (ty != TNULL) return -1;
        return 0;
    }


    public static int memv(int cell, int li)
    {
        int l, c, ty;
        l = 0; c = li;
        while ((ty = Gdm.celltyp[c]) == TPAIR)
        {
           if (eqv(cell, Gdm.cellcar[c]))
               return c;
           c = Gdm.cellcdr[c];
           if (l++ > Gdm.cellcount)
              return -2;
        }
        if (ty != TNULL) return -1;
        return 0;
    }


    public static int member(int cell, int li)
    {
        int l, c, ty;
        l = 0; c = li;
        while ((ty = Gdm.celltyp[c]) == TPAIR)
        {
           if (equal(cell, Gdm.cellcar[c]))
               return c;
           c = Gdm.cellcdr[c];
           if (l++ > Gdm.cellcount)
              return -2;
        }
        if (ty != TNULL) return -1;
        return 0;
    }

    public static int assq(int cell, int li)
    {
        int l, c, ty, p, typ;
        l = 0; c = li;
        while ((ty = Gdm.celltyp[c]) == TPAIR)
        {
           p = Gdm.cellcar[c];
           typ = Gdm.celltyp[p];
           if (typ == TPAIR)
           {
               if (cell == Gdm.cellcar[p])
                   return p;
           }
           c = Gdm.cellcdr[c];
           if (l++ > Gdm.cellcount)
              return -2;
        }
        if (ty != TNULL) return -1;
        return 0;
    }


    public static int assv(int cell, int li)
    {
        int l, c, ty, p, typ;
        l = 0; c = li;
        while ((ty = Gdm.celltyp[c]) == TPAIR)
        {
           p = Gdm.cellcar[c];
           typ = Gdm.celltyp[p];
           if (typ == TPAIR)
           {
               if (eqv(cell,Gdm.cellcar[p]))
                   return p;
           }
           c = Gdm.cellcdr[c];
           if (l++ > Gdm.cellcount)
              return -2;
        }
        if (ty != TNULL) return -1;
        return 0;
    }


    public static int assoc(int cell, int li)
    {
        int l, c, ty, p, typ;
        l = 0; c = li;
        while ((ty = Gdm.celltyp[c]) == TPAIR)
        {
           p = Gdm.cellcar[c];
           typ = Gdm.celltyp[p];
           if (typ == TPAIR)
           {
               if (equal(cell,Gdm.cellcar[p]))
                   return p;
           }
           c = Gdm.cellcdr[c];
           if (l++ > Gdm.cellcount)
              return -2;
        }
        if (ty != TNULL) return -1;
        return 0;
    }


    public static int lcopy(int cell)
    throws SchRunTimeException
    {
        int w, res, c;
        lcell = c = CNULL;
        res = cell;
        while (Gdm.celltyp[cell] == TPAIR)
        {
            lcell = w = Gdm.newcons(TPAIR);
            Gdm.cellcar[w] = Gdm.cellcar[cell];
            if (c == CNULL)
            {
                res = w;
                Gdm.protect(res);
            }
            else
            {
                Gdm.cellcdr[c] = w;
            }
            c = w;
            cell = Gdm.cellcdr[cell];
        }
        return res;
    }


    public static int lappend(int l1, int l2)
    throws SchRunTimeException
    {
         int res = lcopy(l1);
         Gdm.cellcdr[lcell] = l2;
         return res;
    }
}

