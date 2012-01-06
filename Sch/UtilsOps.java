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
 * File: UtilsOps.java
 * Author:
 * Description:
 *
 * $Id: UtilsOps.java,v 1.5 2007/02/20 16:03:41 collins Exp $
 */

package Sch;
import java.lang.reflect.*;


/**
 *
 *  General Utilities
 *
 *
 * Implements the following procedures
 * <UL>
 * <LI><B>load-misc-module</B>
 * <LI><B>end</B>
 * <LI><B>quit</B>
 * <LI><B>stop</B>
 * <LI><B>exit</B>
 * <LI><B>yield</B>
 * <LI><B>gc</B>
 * <LI><B>error</B>
 * <LI><B>procedure?</B>
 * <LI><B>type</B>
 * <LI><B>the-undefined-object</B>
 * </UL>
 *
 *     @author Jean-Jacques Girardot
 */

public class UtilsOps extends SchPrimitive {

    private static UtilsOps proto;

    public static final int OPLDMOD = 0;
    public static final int OPEND = 1;
    public static final int OPQUIT = 2;
    public static final int OPSTOP = 3;
    public static final int OPEXIT = 4;
    public static final int OPYIELD = 5;
    public static final int OPGC = 6;
    public static final int OPERR = 7;
    public static final int OPPROC = 8;
    public static final int OPUNDEF = OPPROC+1;
    public static final int OPTYPE = OPUNDEF+1;

    public static final int NBOP = OPTYPE+1;

    public static void dcl()
    {
        proto = new UtilsOps();
        proto.fnames = new String[NBOP];
        proto.fnames[OPLDMOD] = "load-misc-module";
        proto.fnames[OPEND] = "end";
        proto.fnames[OPQUIT] = "quit";
        proto.fnames[OPSTOP] = "stop";
        proto.fnames[OPEXIT] = "exit";
        proto.fnames[OPYIELD] = "yield";
        proto.fnames[OPGC] = "gc";
        proto.fnames[OPERR] = "error";
        proto.fnames[OPPROC] = "procedure?";
        proto.fnames[OPTYPE] = "type";
        proto.fnames[OPUNDEF] = "the-undefined-object";

        Environment.dcl(proto);
    }

    /**
       This method can be used to dynamically load
       a Misc module and define its primitive operations
    */
    public static int ldmod(String str)
    throws SchRunTimeException
    {
        int res = CNULL;
        try {
             Class c = Class.forName(str);
             res = Gdm.newcons((Object)c);
             if (SchPrimitive.class.equals(c.getSuperclass()))
             {
                  // Get the dcl method
                  Method m = c.getMethod("dcl", new Class [0]);
                  m.invoke(null, new Object [0]);
             }
        } catch (Exception e) { res = CFALSE; }
        return res;
    }

    public void eval(int op, int count, Evaluator evl)
    throws SchRunTimeException
    {
        int cell, res, ty;
        switch (op)
        {


        case OPEND :
        case OPEXIT :
           flagexec = false;
        case OPQUIT :
        case OPSTOP :
           if (count > 2)
              throw new SchRunTimeException(ERRWAC);
           if (count == 0)
               evl.push(CNULL);
           evl.pushCtl(BCHALT);
           break;

        case OPYIELD :
           if (count > 2)
              throw new SchRunTimeException(ERRWAC);
           if (count == 0)
               evl.push(CNULL);
           evl.pushCtl(BCYIEL);
           break;

        case OPTYPE :
           {
               String idt;
               if (count != 1)
                  throw new SchRunTimeException(ERR1AR);
               cell = evl.pop();
               idt = Gdm.celltype(cell);
               res = Symbols.get(idt);
               evl.push(res);
           }
           break;


        case OPGC :
           if (count != 0)
              throw new SchRunTimeException(ERR0AR);
           res = Gdm.gc();
           evl.push(Gdm.newint(res));
           break;


        case OPPROC :
           if (count != 1)
              throw new SchRunTimeException(ERR1AR);
           cell = evl.pop();
           ty = Gdm.typ(cell);
           evl.push((ty==TSUBR || ty==TLAMBDA || ty==TKONT
                    || ty==TJMETH)
               ? CTRUE : CFALSE);
           break;


        case OPLDMOD:
           {
               if (count != 1)
                  throw new SchRunTimeException(ERR1AR);
               cell = evl.valstk[evl.ptvalstk-1];
               if (Gdm.celltyp[cell] != TSTR)
                  throw new SchRunTimeException(ERRDOM);
               res = ldmod((String)Gdm.cellobj[cell]);
               evl.valstk[evl.ptvalstk-1] = res;
           }
           break;


        case OPERR:
           {
               if (count != 1)
                  throw new SchRunTimeException(ERR1AR);
               cell = evl.valstk[evl.ptvalstk-1];
               if (Gdm.celltyp[cell] != TSTR)
                  throw new SchRunTimeException(ERRDOM);
               if (count == 1)
                  throw new SchRunTimeException((String)Gdm.cellobj[cell]);
           }
           break;


        case OPUNDEF:
           if (count != 0)
               throw new SchRunTimeException(ERR0AR);
           evl.push(CUNDEF);
           break;


        default :
           throw new SchRunTimeException(ERRUSP);
        }
    }
}

