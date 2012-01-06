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
 * File: TestOps.java
 * Author:
 * Description:
 *
 * $Id: TestOps.java,v 1.4 2003/11/24 10:54:31 serpaggi Exp $
 */

package Sch;

/**

   Demonstration of addition of new primitives

   TestOps implements "test"

   (test) => #t

*/
public class TestOps extends SchPrimitive {

    /**
        This module implements 1 primitive
    */

    private static TestOps proto;

    public static final int OP0 = 0;
    public static final int OP1 = 1;

    public static final int NBOP = OP1+1;

    /**

             Declare here new primitives
     */
    public static void dcl()
    {
        proto = new TestOps();
        proto.fnames = new String[NBOP];
        proto.fnames[OP0] = "true";
        proto.fnames[OP1] = "truth";
        Environment.dcl(proto);
    }

    public void eval(int op, int count, Evaluator evl)
    throws SchRunTimeException
    {
        int cell, res;
        switch (op)
        {

        /**
           Insert here new code
        */
        case OP0:
           {
               if (count != 0)
                   throw new SchRunTimeException(ERRWAC);
               evl.push(CTRUE);
           }
           break;


        case OP1 :
           {
               if (count != 1)
                   throw new SchRunTimeException(ERR1AR);
               int opr = evl.pop();
               evl.push(opr == CFALSE ? CFALSE : CTRUE);
           }
           break;


        default :
           throw new SchRunTimeException(ERRUSP);
        }

    }

}

 
