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
 * File: Symbols.java
 * Author:
 * Description:
 *
 * $Id: Symbols.java,v 1.4 2003/11/24 10:54:31 serpaggi Exp $
 */

package Sch;

import java.util.*;

/**
 *
 *  Internal Handling of Symbols
 *
 *
 *     @author Jean-Jacques Girardot
 */
public class Symbols {
    public static Hashtable atoms;
    public static String genname="gen";
    public static int gennum=100;

    static {
        atoms=new Hashtable();
    }

    public static int newUninternedSym()
    throws SchRunTimeException
    {
        int res=Gdm.newcons(Globaldata.TSYM);
        int c=Gdm.newcons(Globaldata.TPAIR);
        Gdm.cellcdr[c]=Gdm.cellcdr[Globaldata.Catms];
        Gdm.cellcdr[Globaldata.Catms]=c;
        Gdm.cellcar[c]=res;
        String str = genname+gennum++;
        Gdm.cellobj[res]=str;
        return res;
    }

    public static int get(String str)
    throws SchRunTimeException
    {
        Object x;
        int res;
        x = atoms.get(str);
        if (x == null)
        {
            res=Gdm.newcons(Globaldata.TSYM);
            int c=Gdm.newcons(Globaldata.TPAIR);
            Gdm.cellcdr[c]=Gdm.cellcdr[Globaldata.Catms];
            Gdm.cellcdr[Globaldata.Catms]=c;
            Gdm.cellcar[c]=res;
            Gdm.cellobj[res]=str;
            atoms.put(str, new Integer(res));
        }
        else
        {
            res = ((Integer)x).intValue();
        }
        //str.println("Symbols.get "+str+" => "+res);
        return res;
    }

    public static void set(String str, int num)
    {
        atoms.put(str, new Integer(num));
    }

    public static String get(int numb)
    {
        if (numb < 0 || numb >= Gdm.cellcount || 
             Gdm.celltyp[numb] != Globaldata.TSYM)
        {
            return "?";
        }
        else
        {
            return (String)(Gdm.cellobj[numb]);
        }
    }

}

