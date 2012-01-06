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
 * File: Environment.java
 * Author: Jean-Jacques Girardot
 * Description:
 *
 * $Id: Environment.java,v 1.6 2006/04/28 08:02:51 girardot Exp $
 */

package Sch;

/**
 *
 *  Management of environments
 *
 *     @author Jean-Jacques Girardot
 */


import java.util.*;
public class Environment extends Gdm {
   
   static final int RNIL = -1;


   /**
      Search a cell value in env. ev
   */
   public static int getvc(int ev, int at)
   {
      int cev = ev;
      int vcl, vc, a;
      while (cev != CNULL)
      {
         vcl = cellcar[cev];
         while (vcl != CNULL)
         {
             vc = cellcar[vcl];
             a = cellcar[vc];
             if (a == at)
             {
                 return vc;
             }
             vcl = cellcdr[vcl];
         }
         cev = cellcdr[cev];
      }
      return RNIL;
   }

   /**
      Search a value in env. ev
   */
   public static int get(int ev, int at)
   {
       int vc = getvc(ev, at);
       if (vc == RNIL)
          return CUNDEF;
       else
          return cellcdr[vc];
   }


   /**
       Search a name in environment
   */
   public static int get(int ev, String name)
   {
      int at;
      try {
         at = Symbols.get(name);
      }
      catch (Exception e)
      {
         at = 0;
      }
      return get(ev, at);
   }

   /**
       Set value of atom "at" in environment "ev"
   */
   public static int set(int ev, int at, int val)
   throws SchRunTimeException
   {
       int vc = getvc(ev, at);
       if (vc == RNIL)
           vc = dcl(ev, at);
       cellcdr[vc] = val;
      /*
      str.println("dcl "+Symbols.get(at)+" ("+vc+
          ") = "+at+" "+val);
      */
       return vc;
   }
         
   /**
      Declare a new "Value Cell"
      in environment ev
   */
   public static int dclnew(int ev, int at, int entry)
   throws SchRunTimeException
   {
      int vcl, vc, a;
      vcl = cellcar[ev];
      while (vcl != CNULL)
      {
          vc = cellcar[vcl];
          a = cellcar[vc];
          if (a == at)
          {
             return vc;
          }
          vcl = cellcdr[vcl];
      }
      if (entry !=0)
      {
         vc = entry;
         celltyp[vc] = TVC;
         cellcar[vc] = at;
         cellcdr[vc] = CUNDEF;
      }
      else
      {
         vc = Gdm.newcons(TVC, at, CUNDEF);
      }
      a = Gdm.newcons(TPAIR, vc, cellcar[ev]);
      cellcar[ev] = a;
      return vc;
   }


   /**
      Declare a new "Value Cell"
      in environment ev
   */
   public static int dclnew(int ev, int at)
   throws SchRunTimeException
   {
      return dclnew(ev, at, CNULL);
   }

   public static int dclnew(int ev, String name, int entry)
   throws SchRunTimeException
   {
      int at = Symbols.get(name);
      int vc = dclnew(ev, at, entry);
      return entry;
   }

   /**
      Declare a "Value Cell"
      in environment ev or a parent
   */
   public static int dcl(int ev, int at, int val)
   throws SchRunTimeException
   {
       int vc = getvc(ev, at);
       if (vc == RNIL)
       {
           vc = Gdm.newcons(TVC, at, val);
           int nc = Gdm.newcons(TPAIR, vc, cellcar[ev]);
           cellcar[ev] = nc;
       }
       else
       {
          cellcdr[vc]=val;
       }
       return vc;
   }

   public static int dcl(int ev, int at)
   throws SchRunTimeException
   {
       int vc = getvc(ev, at);
       if (vc == RNIL)
       {
           vc = at;
       }
       return vc;
   }

   public static int dcl(int ev, String name, int entry)
   throws SchRunTimeException
   {
      int at = Symbols.get(name);
      int vc = dcl(ev, at, entry);
      return entry;
   }

   public static int dcl(int ev, String name, byte ty, int p1, int p2)
   throws SchRunTimeException
   {
      int val=Gdm.newcons(ty,p1,p2);
      int at = Symbols.get(name);
      int vc = dcl(ev, at, val);
      //cellcdr[vc] = val;
      //str.println("dcl "+name+" = "+at+" "+vc);
      //Gdm.dmp(val);
      return val;
   }

   /**
     *   Declare primitive SUBR in system environment.
     *   Calls the general method
     *
     */
   public static int dcl(String name, int p2, Object o)
   throws SchRunTimeException
   {
       int val = dcl(systenv, name, TSUBR, 0, p2);
       Gdm.cellobj[val]=o;
       return val;
   }


   /**
     *  This method is specifically used by *Ops classes
     *  to declare their primitives.
     */
   public static void dcl(SchPrimitive o)
   {
       int val, n, s;
       String fn;
       String [] fnames = o.fnames;
       s = fnames.length;
       try {
           for (n=0; n<s; n++)
           {
               fn = fnames[n];
               if (fn != null)
               {
                   val = dcl(systenv, fnames[n], TSUBR, 0, n);
                   Gdm.cellobj[val]=o;
               }
           }
       } catch (Exception e) {
           str.ppprint("Init Exception (" +
              RefOps.getname(o.getClass()) + ") : " +
              e.getMessage() + "\n"); 
       }
   }

}


