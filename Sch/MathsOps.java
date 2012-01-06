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
 * File: MathsOps.java
 * Author:
 * Description:
 *
 * $Id: MathsOps.java,v 1.4 2003/11/24 10:54:30 serpaggi Exp $
 */

package Sch;


/**
 *
 *  All the mathematical procedures.
 *
 *
 * Implements the following procedures
 * <UL>
 * <LI><B>+</B> 
 * <LI><B>-</B> 
 * <LI><B>*</B> 
 * <LI><B>quotient</B> 
 * <LI><B>modulo</B> 
 * <LI><B>remainder</B> 
 * <LI><B>abs</B> 
 * <LI><B>&lt;</B> 
 * <LI><B>&lt;=</B> 
 * <LI><B>&gt;</B> 
 * <LI><B>&gt;=</B> 
 * <LI><B>=</B> 
 * <LI><B>max</B> 
 * <LI><B>min</B> 
 * <LI><B>zero?</B> 
 * <LI><B>positive?</B> 
 * <LI><B>negative?</B> 
 * <LI><B>odd?</B> 
 * <LI><B>even?</B> 
 * <LI><B>number?</B>
 * <LI><B>integer?</B>
 * </UL>
 *
 *     @author Jean-Jacques Girardot
 */

public class MathsOps extends SchPrimitive {

    private static MathsOps proto;

    public static final int OPADD = 1;
    public static final int OPSUB = 2;
    public static final int OPMUL = 3;
    public static final int OPDIV = 4;
    public static final int OPABS = 5;
    public static final int OPCPGT = 6;
    public static final int OPCPGE = 7;
    public static final int OPCPLT = 8;
    public static final int OPCPLE = 9;
    public static final int OPCPEQ = 10;
    public static final int OPCPNE = 11;
    public static final int OPMAX = 12;
    public static final int OPMIN = 13;
    public static final int OPZERO = 14;
    public static final int OPPLUS = 15;
    public static final int OPNEGA = 16;
    public static final int OPNUMB = 17;
    public static final int OPNUMB1 = 18;
    public static final int OPODD  = 19;
    public static final int OPEVEN = 20;
    public static final int OPMOD = 21;
    public static final int OPQUO = 22;
    public static final int OPREM = 23;

    public static final int NBOP = OPREM+1;

    public static void dcl()
    {
        proto = new MathsOps();

        proto.fnames = new String[NBOP];

        proto.fnames[OPADD] = "+";
        proto.fnames[OPSUB] = "-";
        proto.fnames[OPMUL] = "*";
        proto.fnames[OPABS] = "abs";
        proto.fnames[OPCPGT] = ">";
        proto.fnames[OPCPGE] = ">=";
        proto.fnames[OPCPLT] = "<";
        proto.fnames[OPCPLE] = "<=";
        proto.fnames[OPCPEQ] = "=";
        proto.fnames[OPCPNE] = "!=";
        proto.fnames[OPMAX] = "max";
        proto.fnames[OPMIN] = "min";
        proto.fnames[OPZERO] = "zero?";
        proto.fnames[OPPLUS] = "positive?";
        proto.fnames[OPNEGA] = "negative?";
        proto.fnames[OPNUMB] = "number?";
        proto.fnames[OPNUMB1] = "integer?";
        proto.fnames[OPODD] = "odd?";
        proto.fnames[OPEVEN] = "even?";
        proto.fnames[OPQUO] = "quotient";
        proto.fnames[OPREM] = "remainder";
        proto.fnames[OPMOD] = "modulo";

        Environment.dcl(proto);
    }

    public void eval(int op, int count, Evaluator evl)
    throws SchRunTimeException
    {
        switch (op)
        {

        case OPADD :
           {
              int sum=0;
              while (count > 0)
              {
                  sum += evl.unstackint();
                  count--;
              }
              evl.push(Gdm.newint(sum));
           }
           break;


        case OPSUB :
           {
              int sum=0;
              if (count == 1)
              {
                 sum = - evl.unstackint();
              }
              else
              if (count > 1)
              {
                 sum = evl.unstackint();
                 while (count > 1)
                 {
                     sum -= evl.unstackint();
                     count--;
                 }
              }
              evl.push(Gdm.newint(sum));
           }
           break;


        case OPMUL :
           {
              int sum=1;
              while (count > 0)
              {
                  sum *= evl.unstackint();
                  count--;
              }
              evl.push(Gdm.newint(sum));
           }
           break;


        case OPABS :
           {
              int num;
              if (count != 1)
                 throw new SchRunTimeException(ERR1AR);
              num = evl.unstackint();
              evl.push(Gdm.newint(num < 0 ? -num : num));
           }
           break;


        case OPCPGT :
           {
              int num1, num2;
              if (count != 2)
                 throw new SchRunTimeException(ERR2AR);
              num1 = evl.unstackint();
              num2 = evl.unstackint();
              evl.push(num1 > num2 ? CTRUE : CFALSE);
           }
           break;


        case OPCPGE :
           {
              int num1, num2;
              if (count != 2)
                 throw new SchRunTimeException(ERR2AR);
              num1 = evl.unstackint();
              num2 = evl.unstackint();
              evl.push(num1 >= num2 ? CTRUE : CFALSE);
           }
           break;


        case OPCPLT :
           {
              int num1, num2;
              if (count != 2)
                 throw new SchRunTimeException(ERR2AR);
              num1 = evl.unstackint();
              num2 = evl.unstackint();
              evl.push(num1 < num2 ? CTRUE : CFALSE);
           }
           break;


        case OPCPLE :
           {
              int num1, num2;
              if (count != 2)
                 throw new SchRunTimeException(ERR2AR);
              num1 = evl.unstackint();
              num2 = evl.unstackint();
              evl.push(num1 <= num2 ? CTRUE : CFALSE);
           }
           break;


        case OPCPEQ :
           {
              int num1, num2;
              if (count != 2)
                 throw new SchRunTimeException(ERR2AR);
              num1 = evl.unstackint();
              num2 = evl.unstackint();
              evl.push(num1 == num2 ? CTRUE : CFALSE);
           }
           break;


        case OPCPNE :
           {
              int num1, num2;
              if (count != 2)
                 throw new SchRunTimeException(ERR2AR);
              num1 = evl.unstackint();
              num2 = evl.unstackint();
              evl.push(num1 != num2 ? CTRUE : CFALSE);
           }
           break;


        case OPMAX :
           {
              int res=0x80000000;
              if (count == 0)
              {
                  evl.push(Gdm.newint(res));
              }
              else
              {
                  int val=0, num=0, elt=0;
                  while (count > 0)
                  {
                      elt = evl.pop();
                      Check.integ(elt);
                      val = Gdm.car(elt);
                      if (val >= res)
                      {
                          res = val;
                          num = elt;
                      }
                      count--;
                  }
                  evl.push(num);
              }
           }
           break;


        case OPMIN :
           {
              int res=0x7FFFFFFF;
              if (count == 0)
              {
                  evl.push(Gdm.newint(res));
              }
              else
              {
                  int val=0, num=0, elt=0;
                  while (count > 0)
                  {
                      elt = evl.pop();
                      Check.integ(elt);
                      val = Gdm.car(elt);
                      if (val <= res)
                      {
                          res = val;
                          num = elt;
                      }
                      count--;
                  }
                  evl.push(num);
              }
           }
           break;


        case OPZERO :
           {
              int num;
              if (count != 1)
                 throw new SchRunTimeException(ERR1AR);
              num = evl.unstackint();
              evl.push(num == 0 ? CTRUE : CFALSE);
           }
           break;


        case OPPLUS :
           {
              int num;
              if (count != 1)
                 throw new SchRunTimeException(ERR1AR);
              num = evl.unstackint();
              evl.push(num > 0 ? CTRUE : CFALSE);
           }
           break;


        case OPNEGA :
           {
              int num;
              if (count != 1)
                 throw new SchRunTimeException(ERR1AR);
              num = evl.unstackint();
              evl.push(num < 0 ? CTRUE : CFALSE);
           }
           break;


        case OPODD :
           {
              int num;
              if (count != 1)
                 throw new SchRunTimeException(ERR1AR);
              num = evl.unstackint();
              evl.push((num & 1)!=0 ? CTRUE : CFALSE);
           }
           break;


        case OPEVEN :
           {
              int num;
              if (count != 1)
                 throw new SchRunTimeException(ERR1AR);
              num = evl.unstackint();
              evl.push((num & 1)==0 ? CTRUE : CFALSE);
           }
           break;


        case OPNUMB :
        case OPNUMB1 :
           {
              int val;
              if (count != 1)
                 throw new SchRunTimeException(ERR1AR);
              val = evl.pop();
              evl.push(Gdm.typ(val) == TINT ? CTRUE : CFALSE);
           }
           break;


        case OPQUO :
           {
              // On utilise le fait que la divion entière java
              // correspond au "quotient" de Scheme.
              int num, div, quo;
              if (count != 2)
                 throw new SchRunTimeException(ERR2AR);
              num = evl.unstackint();
              div = evl.unstackint();
              if (div == 0)
                 throw new SchRunTimeException(ERRZDV);
              quo = num/div;
              evl.push(Gdm.newint(quo));
           }
           break;


        case OPREM :
           {
              int num, div, quo, rem;
              if (count != 2)
                 throw new SchRunTimeException(ERR2AR);
              num = evl.unstackint();
              div = evl.unstackint();
              if (div == 0)
                 throw new SchRunTimeException(ERRZDV);
              quo = num/div;
              rem = num - div*quo ;
              evl.push(Gdm.newint(rem));
           }
           break;


        case OPMOD :
           {
              int num, div, quo, mod;
              if (count != 2)
                 throw new SchRunTimeException(ERR2AR);
              num = evl.unstackint();
              div = evl.unstackint();
              if (div == 0)
                 throw new SchRunTimeException(ERRZDV);
              quo = num/div;
              mod = num - div*quo ;
              if ((div<0 ? -1 : 1) != (mod<0 ? -1 : 1))
                  mod = mod + div;
              evl.push(Gdm.newint(mod));
           }
           break;


        default :
           throw new SchRunTimeException(ERRUSP);
        }
    }
}

