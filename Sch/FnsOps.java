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
 * File: FnsOps.java
 * Author:
 * Description:
 *
 * $Id: FnsOps.java,v 1.4 2003/11/24 10:54:30 serpaggi Exp $
 */

package Sch;


/**
 *
 *  Procedure handling other procedures.
 *
 *
 * Implements the following procedures
 * <UL>
 * <LI><B>apply</B> applies a procedure (its first parameter) to the
 *                  other parameters
 * <LI><B>map</B> applies a procedure (its first parameter) to all the
 *                elements of a list or a set of lists, an return
 *                the list of the results of the applications
 * <LI><B>fot-each</B> applies a procedure (its first parameter) to all the
 *                elements of a list or a set of lists, no result returned
 * </UL>
 *
 *     @author Jean-Jacques Girardot
 */

public class FnsOps extends SchPrimitive {

    private static FnsOps proto;

    public static final int OPAPPLY = 1;
    public static final int OPMAP = 2;
    public static final int OPFEACH = 3;

    public static final int NBOP = OPFEACH+1;

    public static void dcl()
    {
        proto = new FnsOps();
        proto.fnames = new String[NBOP];

        proto.fnames[OPAPPLY] = "apply";
        proto.fnames[OPMAP] = "map";
        proto.fnames[OPFEACH] = "for-each";

        Environment.dcl(proto);
    }

    public void eval(int op, int count, Evaluator evl)
    throws SchRunTimeException
    {
        int cell;
        switch (op)
        {

        case OPAPPLY :
           {
               int aux, nargs, i;
               nargs = 0;
               if (count < 1)
                  throw new SchRunTimeException(ERRWAC);
               aux = evl.pop();
               while (count > 1)
               {
                   evl.pushCtl(aux);
                   aux = evl.pop();
                   count--;
                   nargs++;
               }
               // On ajoute le contenu de aux ;
               while (Gdm.celltyp[aux] == TPAIR)
               {
                   evl.pushCtl(Gdm.cellcar[aux]);
                   aux = Gdm.cellcdr[aux];
                   nargs++;
               }
               if (Gdm.celltyp[aux] != TNULL)
                  throw new SchRunTimeException(ERRWAC);
               // On transfère dans les données
               i = nargs;
               while (i > 0)
               {
                   evl.push(evl.popCtl());
                   i--;
               }
               // On dépose la fonction
               //evl.push(fct);
               nargs--;
               if (nargs < 0)
                  throw new SchRunTimeException(ERRWAC);
               if (nargs <= MAXARGC)
               {
                   evl.pushCtl(BCF00+nargs);
               }
               else
               {
                   evl.pushCtl(Gdm.newint(nargs));
                   evl.pushCtl(BCFnn);
               }
           }
           break;


        case OPMAP :
        case OPFEACH :
           {
               int fct, i, n, t, b, c;
               if (count < 2)
                  throw new SchRunTimeException(ERRWAC);
               fct = evl.pop();
               int nargs = count-1;
               b = evl.ptvalstk-nargs;
               c = evl.ptvalstk-1;
               for (i=b; i<=c; i++)
               {
                   n=evl.valstk[i];
                   t = Gdm.celltyp[n];
                   if (t != TPAIR && t != TNULL)
                      throw new SchRunTimeException(ERRTYE);
               }
               // 
               boolean x=true;
               for (i=b; i<=c; i++)
               {
                   n=evl.valstk[i];
                   t = Gdm.celltyp[n];
                   if (t == TNULL)
                       x=false;
                   else
                   {
                       evl.push(Gdm.cellcar[n]);
                       evl.valstk[i] = Gdm.cellcdr[n];
                   }
               }
               //
               int res = op == OPMAP ? CNULL : CFALSE ;
               if (x)
               {
                   evl.pushCtl(res);
                   evl.pushCtl(fct);
                   evl.pushCtl(nargs);
                   evl.pushCtl(BCMAP);
                   evl.push(fct);
                   if (nargs <= MAXARGC)
                   {
                       evl.pushCtl(BCF00+nargs);
                   }
                   else
                   {
                       evl.pushCtl(Gdm.newint(nargs));
                       evl.pushCtl(BCFnn);
                   }
               }
               else
               {
                   // pas d'application
                   evl.ptvalstk = b;
                   evl.push(res);
               }
           }
           break;


        default :
           throw new SchRunTimeException(ERRUSP);
        }
    }
}

