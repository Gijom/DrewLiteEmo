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
 * File: StringsOps.java
 * Author:
 * Description:
 *
 * $Id: StringsOps.java,v 1.4 2003/11/24 10:54:31 serpaggi Exp $
 */

package Sch;


/**
 *
 *  Misc Strings Operations.
 *
 *
 * Implements the following procedures
 * <UL>
 * <LI><B>strings?</B>
 * <LI><B>symbol?</B>
 * <LI><B>char?</B>
 * <LI><B>string</B>
 * <LI><B>string-append</B>
 * <LI><B>string-length</B>
 * <LI><B>make-string</B>
 * <LI><B>string-ref</B>
 * <LI><B>string-set</B>
 * <LI><B>substring</B>
 * <LI><B>string->symbol</B>
 * <LI><B>symbol->string</B>
 * <LI><B>number->string</B>
 * <LI><B>integer->char</B>
 * <LI><B>char->integer</B>
 * <LI><B>string-match</B>
 * </UL>
 *
 *     @author Jean-Jacques Girardot
 */


public class StringsOps extends SchPrimitive {

    private static StringsOps proto;

    public static final int OPSTRINGQ = 1;
    public static final int OPSTRING  = 2;
    public static final int OPSTAPPND = 3;
    public static final int OPSTLEN = 4;
    public static final int OPMKSTR = 5;
    public static final int OPSTREF = 6;
    public static final int OPSTSET = 7;
    public static final int OPSUBSTR = 8;
    public static final int OPSYMBQ = 9;
    public static final int OPSTTSYM = 10;
    public static final int OPSYMTST = 11;
    public static final int OPNUMTST = 12;
    public static final int OPCHTOINT = 13;
    public static final int OPINTTOCH = 14;
    public static final int OPCHARQ = 15;
    public static final int OPMATCH = 16;

    public static final int NBOP = OPMATCH+1;

    public static void dcl()
    {
        proto = new StringsOps();
        proto.fnames = new String[NBOP];
 
        proto.fnames[OPSTRINGQ] = "string?";
        proto.fnames[OPSYMBQ] = "symbol?";
        proto.fnames[OPSTRING] = "string";
        proto.fnames[OPSTAPPND] = "string-append";
        proto.fnames[OPSTLEN] = "string-length";
        proto.fnames[OPMKSTR] = "make-string";
        proto.fnames[OPSTREF] = "string-ref";
        proto.fnames[OPSTSET] = "string-set!";
        proto.fnames[OPSUBSTR] = "substring";
        proto.fnames[OPSTTSYM] = "string->symbol";
        proto.fnames[OPSYMTST] = "symbol->string";
        proto.fnames[OPNUMTST] = "number->string";
        proto.fnames[OPINTTOCH] = "integer->char";
        proto.fnames[OPCHTOINT] = "char->integer";
        proto.fnames[OPCHARQ] = "char?";
        proto.fnames[OPMATCH] = "string-match";

        Environment.dcl(proto);
    }

    public static final char WC = '*';

    public static boolean match(String p, int ps, int pe, String s, int ss, int se)
    {
        boolean res=false;
        char pc=0, sc=0;

        for (;;)
        {
            if (ps >= pe)
            {
                // patern empty
                // match if string empty
                return (ss >= se);
            }
            else
            if (ss >= se)
            {
                // string empty, but pattern non empty
                // first char of pattern must be the WC
                pc = p.charAt(ps); 
                if (pc != WC) return false;
                ps++;
            }
            else
            {
                pc = p.charAt(ps); 
                sc = s.charAt(ss);
                if (pc == WC)
                {
                    // We aggregate successive WCs
                    while ((ps < (pe-1)) && (p.charAt(ps+1) == WC))
                        ps++;
                    // We try the WC consuming method
                    res = match(p, ps+1, pe, s, ss, se);
                    if (res) return res;
                    // We try the string consuming method
                    ss++;
                }
                else
                {
                    // both characters must be equal
                    if (pc != sc) return false;
                    ps++; ss++;
                }
            }
        }
    }

    public static boolean match(String p, String s)
    {
        return match(p, 0, p.length(), s, 0, s.length());
    }


    public void eval(int op, int count, Evaluator evl)
    throws SchRunTimeException
    {
        int cell, res;
        switch (op)
        {

        case OPSTRINGQ :
           if (count != 1)
              throw new SchRunTimeException(ERR1AR);
           cell = evl.pop();
           evl.push(Gdm.typ(cell)==TSTR ? CTRUE : CFALSE);
           break;


        case OPSYMBQ :
           if (count != 1)
              throw new SchRunTimeException(ERR1AR);
           cell = evl.pop();
           evl.push(Gdm.typ(cell)==TSYM ? CTRUE : CFALSE);
           break;


        case OPNUMTST :
        case OPSTAPPND :
        case OPSTRING :
           {
               StringBuffer sb=new StringBuffer();
               String str;
               while (count > 0)
               {
                   cell = evl.pop();
                   str = Gdm.celltostring(cell);
                   sb.append(str);
                   count--;
               }
               str = sb.toString();
               res = Gdm.newcons(TSTR);
               Gdm.cellobj[res]=str;
               evl.push(res);
           }
           break;


/*
        case OPNUMTST :
        case OPSTAPPND :
           {
               StringBuffer sb=new StringBuffer();
               String str;
               while (count > 0)
               {
                   cell = evl.pop();
                   switch (Gdm.celltyp[cell])
                   {
                   case TSTR :
                       str = (String)Gdm.cellobj[cell];
                       break;
                   case TINT :
                       str = "" + Gdm.cellcar[cell];
                       break;
                   case TCHAR :
                       {
                           char [] c = new char[1];
                           c[0] = (char)Gdm.cellcar[cell];
                           str = new String(c);
                       }
                       break;
                   case TSYM :
                       str = (String)Gdm.cellobj[cell];
                       break;
                   default :
                       throw new SchRunTimeException(ERRDOM);
                   }
                   sb.append(str);
                   count--;
               }
               str = sb.toString();
               res = Gdm.newcons(TSTR);
               Gdm.cellobj[res]=str;
               evl.push(res);
           }
           break;
*/


        case OPINTTOCH :
           {
              if (count != 1)
                 throw new SchRunTimeException(ERR1AR);
              cell = evl.valstk[evl.ptvalstk-1];
              if (Gdm.celltyp[cell] != TINT)
                  throw new SchRunTimeException(ERRDOM);
              res = Gdm.newchar(Gdm.cellcar[cell]);
              evl.valstk[evl.ptvalstk-1] = res;
           }
           break;


        case OPCHTOINT :
           {
              if (count != 1)
                 throw new SchRunTimeException(ERR1AR);
              cell = evl.valstk[evl.ptvalstk-1];
              if (Gdm.celltyp[cell] != TCHAR)
                  throw new SchRunTimeException(ERRDOM);
              res = Gdm.newint(Gdm.cellcar[cell]);
              evl.valstk[evl.ptvalstk-1] = res;
           }
           break;


        case OPSTLEN :
           {
              if (count != 1)
                 throw new SchRunTimeException(ERR1AR);
              cell = evl.valstk[evl.ptvalstk-1];
              if (Gdm.celltyp[cell] != TSTR)
                  throw new SchRunTimeException(ERRDOM);
              res = Gdm.newint(((String)Gdm.cellobj[cell])
                  .length());
              evl.valstk[evl.ptvalstk-1] = res;
           }
           break;


        case OPMKSTR :
           {
              if (count < 1 || count > 2)
                 throw new SchRunTimeException(ERRWAC);
              int k = evl.unstackint();
              int val = 32;
              if (count == 2)
                 val = evl.unstackint();
              char [] c = new char[k];
              for (int i=0; i<k; i++)
                 c[i] = (char)val;
              String str = new String(c);
              res = Gdm.newcons(TSTR);
              Gdm.cellobj[res]=str;
              evl.push(res);
           }
           break;


        case OPSTREF :
           {
              if (count != 2)
                 throw new SchRunTimeException(ERRWAC);
              int s = evl.pop();
              if (Gdm.celltyp[s] != TSTR)
                  throw new SchRunTimeException(ERRDOM);
              int k = evl.unstackint();
              String str = (String)Gdm.cellobj[s];
              char c;
              try {
                  c = str.charAt(k);
              } catch (Exception e) {
                  throw new SchRunTimeException(ERRDOM);
              }
              res = Gdm.newchar(c);
              evl.push(res);
           }
           break;


        case OPSTSET :
           {
              if (count != 3)
                 throw new SchRunTimeException(ERRWAC);
              // The string
              int s = evl.pop();
              if (Gdm.celltyp[s] != TSTR)
                  throw new SchRunTimeException(ERRDOM);
              Gdm.protect1(s);
              // The index
              int k = evl.unstackint();
              // The new char
              int v = evl.pop();
              if ((Gdm.celltyp[v] != TCHAR) & (Gdm.celltyp[v] != TINT))
                  throw new SchRunTimeException(ERRDOM);
              int val = Gdm.cellcar[v];
              StringBuffer sb = new
                 StringBuffer((String)Gdm.cellobj[s]);
              char c;
              try {
                  c = sb.charAt(k);
                  sb.setCharAt(k, (char)val);
              } catch (Exception e) {
                  throw new SchRunTimeException(ERRDOM);
              }
              Gdm.cellobj[s] = sb.toString();
              res = Gdm.newchar(c);
              evl.push(res);
           }
           break;


        case OPSUBSTR :
           {
              if (count != 3)
                 throw new SchRunTimeException(ERRWAC);
              int s = evl.pop();
              if (Gdm.celltyp[s] != TSTR)
                  throw new SchRunTimeException(ERRDOM);
              Gdm.protect1(s);
              int beg = evl.unstackint();
              int end = evl.unstackint();
              String str;
              try {
                  str = ((String)Gdm.cellobj[s]).
                      substring(beg,end);
              } catch (Exception e) {
                  throw new SchRunTimeException(ERRDOM);
              }
              res = Gdm.newcons(TSTR);
              Gdm.cellobj[res]=str;
              evl.push(res);
           }
           break;


        case OPSTTSYM :
           {
              if (count != 1)
                   throw new SchRunTimeException(ERR1AR);
              cell = evl.valstk[evl.ptvalstk-1];
              if (Gdm.celltyp[cell] != TSTR)
                  throw new SchRunTimeException(ERRDOM);
              res = Symbols.get((String)Gdm.cellobj[cell]);
              evl.valstk[evl.ptvalstk-1] = res;
           }
           break;


        case OPSYMTST :
           {
              if (count != 1)
                   throw new SchRunTimeException(ERR1AR);
              cell = evl.valstk[evl.ptvalstk-1];
              if (Gdm.celltyp[cell] != TSYM)
                  throw new SchRunTimeException(ERRDOM);
              res = Gdm.newcons(TSTR);
              Gdm.cellobj[res]=Gdm.cellobj[cell];
              evl.valstk[evl.ptvalstk-1] = res;
           }
           break;


        case OPMATCH :
           {
              if (count != 2)
                   throw new SchRunTimeException(ERR2AR);
              int pat = evl.valstk[evl.ptvalstk-1];
              if (Gdm.celltyp[pat] != TSTR)
                  throw new SchRunTimeException(ERRDOM);
              int str = evl.valstk[evl.ptvalstk-2];
              if (Gdm.celltyp[str] != TSTR)
                  throw new SchRunTimeException(ERRDOM);
              evl.valstk[evl.ptvalstk-2] =
                  match((String)Gdm.cellobj[pat], (String)Gdm.cellobj[str]) ?
                      CTRUE : CFALSE;
              evl.ptvalstk--;
           }
           break;


        case OPCHARQ :
           if (count != 1)
              throw new SchRunTimeException(ERR1AR);
           cell = evl.pop();
           evl.push(Gdm.typ(cell)==TCHAR ? CTRUE : CFALSE);
           break;


        default :
           throw new SchRunTimeException(ERRUSP);
        }
    }
}

