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
 * File: SchPrimitive.java
 * Author:
 * Description:
 *
 * $Id: SchPrimitive.java,v 1.4 2003/11/24 10:54:30 serpaggi Exp $
 */

package Sch;

/**
 *   The abstract interface for Misc modules implementing primitives.
 *
 *   The methods <B>dcl()</B> and
 *   <B>eval(...)</B> should be redefined in every class
 *   implementing <I>SchPrimitive</I>
 *
 *   @author Jean-Jacques Girardot
 *
 */
public abstract class SchPrimitive extends Globaldata {

    public String [] fnames;
    public static void dcl() {}
    
    public abstract void eval(int op, int count, Evaluator evl)
    throws SchRunTimeException;

    public String name(int num)
    {
        if ((fnames == null) || (num < 0) ||
           (num >= fnames.length) || (fnames[num] == null))
           return "";
        return fnames[num];
    }

}

