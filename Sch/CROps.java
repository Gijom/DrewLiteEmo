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
 * File: CROps.java
 * Author: Jean-Jacques Girardot
 * Description:
 *
 * $Id: CROps.java,v 1.5 2006/04/28 08:02:51 girardot Exp $
 */

package Sch;

/**
 *
 *  The car/cdr combinations
 *
 *
 *     @author Jean-Jacques Girardot
 */

public class CROps extends SchPrimitive {

    private static CROps proto;

    public static final int DOCAR = 1;
    public static final int DOCDR = 2;

    public static final int OPCAAR = DOCAR + (4*DOCAR);
    public static final int OPCADR = DOCDR + (4*DOCAR);
    public static final int OPCDAR = DOCAR + (4*DOCDR);
    public static final int OPCDDR = DOCDR + (4*DOCDR);

    public static final int OPCAAAR = DOCAR + 
           (4*DOCAR) + (16*DOCAR);
    public static final int OPCAADR = DOCDR + 
           (4*DOCAR) + (16*DOCAR);
    public static final int OPCADAR = DOCAR + 
           (4*DOCDR) + (16*DOCAR);
    public static final int OPCADDR = DOCDR + 
           (4*DOCDR) + (16*DOCAR);
    public static final int OPCDAAR = DOCAR + 
           (4*DOCAR) + (16*DOCDR);
    public static final int OPCDADR = DOCDR + 
           (4*DOCAR) + (16*DOCDR);
    public static final int OPCDDAR = DOCAR + 
           (4*DOCDR) + (16*DOCDR);
    public static final int OPCDDDR = DOCDR + 
           (4*DOCDR) + (16*DOCDR);

    public static final int OPCAAAAR = DOCAR + 
           (4*DOCAR) + (16*DOCAR) + (64*DOCAR);
    public static final int OPCAAADR = DOCDR + 
           (4*DOCAR) + (16*DOCAR) + (64*DOCAR);
    public static final int OPCAADAR = DOCAR + 
           (4*DOCDR) + (16*DOCAR) + (64*DOCAR);
    public static final int OPCAADDR = DOCDR + 
           (4*DOCDR) + (16*DOCAR) + (64*DOCAR);
    public static final int OPCADAAR = DOCAR + 
           (4*DOCAR) + (16*DOCDR) + (64*DOCAR);
    public static final int OPCADADR = DOCDR + 
           (4*DOCAR) + (16*DOCDR) + (64*DOCAR);
    public static final int OPCADDAR = DOCAR + 
           (4*DOCDR) + (16*DOCDR) + (64*DOCAR);
    public static final int OPCADDDR = DOCDR + 
           (4*DOCDR) + (16*DOCDR) + (64*DOCAR);

    public static final int OPCDAAAR = DOCAR + 
           (4*DOCAR) + (16*DOCAR) + (64*DOCDR);
    public static final int OPCDAADR = DOCDR + 
           (4*DOCAR) + (16*DOCAR) + (64*DOCDR);
    public static final int OPCDADAR = DOCAR + 
           (4*DOCDR) + (16*DOCAR) + (64*DOCDR);
    public static final int OPCDADDR = DOCDR + 
           (4*DOCDR) + (16*DOCAR) + (64*DOCDR);
    public static final int OPCDDAAR = DOCAR + 
           (4*DOCAR) + (16*DOCDR) + (64*DOCDR);
    public static final int OPCDDADR = DOCDR + 
           (4*DOCAR) + (16*DOCDR) + (64*DOCDR);
    public static final int OPCDDDAR = DOCAR + 
           (4*DOCDR) + (16*DOCDR) + (64*DOCDR);
    public static final int OPCDDDDR = DOCDR + 
           (4*DOCDR) + (16*DOCDR) + (64*DOCDR);

    public static final int NBOP = OPCDDDDR+1;

    public static void dcl()
    {
        proto = new CROps();
        proto.fnames = new String[NBOP];

        proto.fnames[OPCAAR] = "caar";
        proto.fnames[OPCADR] = "cadr";
        proto.fnames[OPCDAR] = "cdar";
        proto.fnames[OPCDDR] = "cddr";

        proto.fnames[OPCAAAR] = "caaar";
        proto.fnames[OPCAADR] = "caadr";
        proto.fnames[OPCADAR] = "cadar";
        proto.fnames[OPCADDR] = "caddr";

        proto.fnames[OPCDAAR] = "cdaar";
        proto.fnames[OPCDADR] = "cdadr";
        proto.fnames[OPCDDAR] = "cddar";
        proto.fnames[OPCDDDR] = "cdddr";

        proto.fnames[OPCAAAAR] = "caaaar";
        proto.fnames[OPCAAADR] = "caaadr";
        proto.fnames[OPCAADAR] = "caadar";
        proto.fnames[OPCAADDR] = "caaddr";

        proto.fnames[OPCADAAR] = "cadaar";
        proto.fnames[OPCADADR] = "cadadr";
        proto.fnames[OPCADDAR] = "caddar";
        proto.fnames[OPCADDDR] = "cadddr";

        proto.fnames[OPCDAAAR] = "cdaaar";
        proto.fnames[OPCDAADR] = "cdaadr";
        proto.fnames[OPCDADAR] = "cdadar";
        proto.fnames[OPCDADDR] = "cdaddr";

        proto.fnames[OPCDDAAR] = "cddaar";
        proto.fnames[OPCDDADR] = "cddadr";
        proto.fnames[OPCDDDAR] = "cdddar";
        proto.fnames[OPCDDDDR] = "cddddr";

        Environment.dcl(proto);
    }

    public void eval(int op, int count, Evaluator evl)
    throws SchRunTimeException
    {
        int cell, res;

        if (count != 1)
            throw new SchRunTimeException(ERR1AR);
        cell = evl.valstk[evl.ptvalstk-1];
        while (op != 0)
        {
            if (Gdm.celltyp[cell] != TPAIR)
                throw new SchRunTimeException(ERRCXP);
            cell = ((op & 0x3) == DOCAR) ?
                Gdm.cellcar[cell] : Gdm.cellcdr[cell];
            op = op >> 2;
        }
        evl.valstk[evl.ptvalstk-1] = cell;

    }

}

