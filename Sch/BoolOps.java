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
 * File: BoolOps.java
 * Author: Jean-Jacques Girardot
 * Description:
 *
 * $Id: BoolOps.java,v 1.5 2006/04/28 08:02:51 girardot Exp $
 */

package Sch;

/**
 *
 *  Boolean Primitives
 *
 *  Implements the following scheme procedures
 * <UL>
 * <LI><B>not</B> returns the boolean inverse of its parameter
 * <LI><B>boolean?</B> returns <I>true</I> if its parameter is boolean
 * </UL>
 *
 *     @author Jean-Jacques Girardot
 */


public class BoolOps extends SchPrimitive {

    private static BoolOps proto;

    public static final int OPNOT = 0;
    public static final int OPBOOL = 1;

    public static final int NBOP = OPBOOL+1;

    public static void dcl()
    {
        proto = new BoolOps();
        proto.fnames = new String[NBOP];
        proto.fnames[OPNOT] = "not";
        proto.fnames[OPBOOL] = "boolean?";

        Environment.dcl(proto);
    }

    public void eval(int op, int count, Evaluator evl)
    throws SchRunTimeException
    {
        int cell, res;
        switch (op)
        {
        case OPNOT :
           if (count != 1)
              throw new SchRunTimeException(ERR1AR);
           cell = evl.pop();
           evl.push(cell == CFALSE ? CTRUE : CFALSE);
           break;
        case OPBOOL :
           if (count != 1)
              throw new SchRunTimeException(ERR1AR);
           cell = evl.pop();
           evl.push(Gdm.typ(cell) == TBOOL ? CTRUE : CFALSE);
           break;
        default :
           throw new SchRunTimeException(ERRUSP);
        }
    }
}

