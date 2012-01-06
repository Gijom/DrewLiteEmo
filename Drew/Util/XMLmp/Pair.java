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
 * File: Pair.java
 * Author: Jean-Jacques Girardot
 * Description:
 *
 * $Id: Pair.java,v 1.6 2006/04/28 08:35:29 girardot Exp $
 */

package Drew.Util.XMLmp;

import java.util.*;

/**
   A cheap implantation of Scheme pairs

 */

public class Pair implements Enumeration {

   protected Object lecar;
   protected Object lecdr;

   public static final Pair nil;

   private static boolean opt = true;
   private static String POPA = "(";
   private static String PCPA = ")";
   private static String PNUL = "()";
   private static String PSEP = " . ";

   /**
      At initialization, create nil, an instance of Pair,
      which marks end-of-list, and is recognized by the
      methods of the Class.
   */
   static {
      nil = new Pair(null,null);
      nil.lecar = nil;
      nil.lecdr = nil;
   }

   /**
      The method delimiters has for only purpose to change the
      way a pair is printed - parameter value of 0, 1, or 2 selects
      one of the three appearence.
   */
   public static void delimiters(int val)
   {
       switch (val)
       {
       default :
           POPA = "("; PCPA = ")";
           PNUL = "()"; PSEP = " . ";
           opt = true;
           break;
       case 1 :
           POPA = "["; PCPA = "]";
           PNUL = "--"; PSEP = " ";
           opt = false;
           break;
       case 2 :
           POPA = " ["; PCPA = "] ";
           PNUL = "--"; PSEP = " | ";
           opt = false;
           break;
       }
   }
   
   /**
      Creation of a Pair
   */
   public Pair()
   {
      lecar = nil;
      lecdr = nil;
   }

   /**
      Creation of a Pair
   */
   public Pair(Object uncar, Object uncdr)
   {
      lecar = uncar;
      lecdr = uncdr;
   }

   /**
      Obtain the value of the car of a Pair
   */
   public Object car()
   {
      return lecar;
   }

   /**
      Obtain the value of the cdr of a Pair
   */
   public Object cdr()
   {
      return lecdr;
   }

   /**
      Sets the value of the car of a Pair.
   */
   public void setCar(Object uncar)
   {
      lecar = uncar;
   }

   /**
      Sets the value of the cdr of a Pair.
   */
   public void setCdr(Object uncdr)
   {
      lecdr = uncdr;
   }

   /**
      Indicates whether this pair is nil or not.
   */
   public boolean isNil()
   {
      return this == nil;
   }

   /**
      Indicates whether this pair has a non-empty pair as cdr.
   */
   public boolean hasMoreElements()
   {
      return (lecdr instanceof Pair) && (lecdr != nil);
   }

   /**
      Returns the next element of a list
      or the Exception "NoSuchElementException".
   */
   public Object nextElement()
   throws NoSuchElementException
   {
      if ((lecdr instanceof Pair) && (lecdr != nil))
          return lecdr;
      throw new NoSuchElementException();
   }

   /**
      Returns the length of a list
   */
   public int length()
   {
       Object p = this;
       int c = 0;
       while ((p instanceof Pair) && (p != nil))
       {
           Pair q = (Pair) p;
           p = q.lecdr;
           c++;
       }
       return c;
   }

   /** 
      Return a specific element of a list
   */
   public Object elt(int n)
   {
       Object p = this;
       Pair q = this;
       while ((p instanceof Pair) && (p != nil) && (n > 0))
       {
           q = (Pair) p;
           p = q.lecdr;
           n--;
       }
       return n <= 0 ? q.lecar : null;
   }

   /**
      Converts the contents of a Pair to a String.
   */
   public String toString()
   {
      String scar, scdr, ssep;
      if (this == nil)
          return PNUL;
      ssep = PSEP;
      if ((lecar == null) || (lecar == nil))
          scar = PNUL;
      else
          scar = lecar.toString();
      if ((lecdr == null) || (lecdr == nil))
      {
          scdr = ""; ssep = "";
      }
      else
          scdr = lecdr.toString();
      return (POPA + scar + ssep + scdr + PCPA);
   }

   public Object assq(Object obj)
   {
       Object p = this, q;
       Pair pp, qq;

       while ((p instanceof Pair) && (p != nil))
       {
           pp = (Pair)p;
           q = pp.lecar;
           if (q instanceof Pair)
           {
               qq = (Pair)q;
               if (obj.equals(qq.lecar))
                   return q;
           }
           p = pp.lecdr;
       }
       return Boolean.FALSE;
   }
       
   public Object cassq(Object obj)
   {
       Object r=this.assq(obj);
       Pair rr;
       if (r instanceof Pair)
       {
           rr = (Pair)r;
           r = rr.lecdr;
       }
       return r;
   }

   /** 
       Compares the object with another Pair
   */
   public boolean equals(Object obj)
   {
       if (obj == this)
           return true;
       if (obj instanceof Pair)
       {
          
          Pair pp = (Pair)obj;
          return (lecar.equals(pp.lecar)
                && lecdr.equals(pp.lecdr));
       }
       return false;
   }

   /**
       A general static method to print an object that can
       be a Pair.
   */
   public static String toString(Object obj)
   {
       if ((obj == null) || (obj == nil))
           return PNUL;
       if (obj instanceof String)
           return "\""+obj+"\"";
       if (obj instanceof Pair)
       {
           Pair p;
           StringBuffer sb = new StringBuffer();
           sb.append(POPA);
           if (opt)
           {
              while ((obj instanceof Pair) && (obj != nil))
              {
                 p = (Pair)obj;
                 sb.append(toString(p.car()));
                 obj = p.cdr();
                 if ((obj == null) || (obj == nil))
                 {
                 }
                 else
                 if (obj instanceof Pair)
                 {
                     sb.append(" ");
                 }
                 else
                 {
                     sb.append(PSEP);
                     sb.append(toString(p.cdr()));
                 }
              }
           }
           else
           {
              p = (Pair)obj;
              sb.append(toString(p.car()));
              sb.append(PSEP);
              sb.append(toString(p.cdr()));
           }
           sb.append(PCPA);
           return sb.toString();
       }
       return obj.toString();    
   }

   /**
      Return either the String which is in the car of the Pair,
      either null. This is to be applied to a Pair that represents
      the name/value pair of an attribute.
   */
   public String name()
   {
       if (lecar instanceof String)
           return (String)lecar;
       return null;
   }

   /**
      Sets the car of the pair to be the string passed as parameter.
      Equivalent to setCar(), except that the parameter is supposed
      to be a String, and not any Object.
   */
   public void setName(String s)
   {
       lecar = s;
   }

   /**
      Return either the String which is in the cdr of the Pair,
      either null. This is to be applied to a Pair that represents
      the name/value pair of an attribute.
   */
   public String value()
   {
       if (lecdr instanceof String)
           return (String)lecdr;
       return null;
   }

   /**
      Sets the cdr of the pair to be the string passed as parameter.
      Equivalent to setCdr(), except that the parameter is supposed
      to be a String, and not any Object.
   */
   public void setValue(String s)
   {
       lecdr = s;
   }

}


