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
 * File: RefOps.java
 * Author:
 * Description:
 *
 * $Id: RefOps.java,v 1.5 2007/02/20 16:03:41 collins Exp $
 */

package Sch;
import java.lang.reflect.*;

/**
 *
 *  General reflection primitives.
 *
 *
 * Implements the following procedures
 * <UL>
 * <LI><B>java.name</B> returns the name of its parameter, which should
 *     be a Java object.
 * <LI><B>java.get-class</B> loads the Java class defined by its parameter
 *     (a string), and packages the class as an object.
 * <LI><B>java.methods</B> returns a list containing all the <I>methods</I>
 *     of the class passed as a parameter.
 * <LI><B>java.constructors</B> returns a list containing all the <I>constructors</I>
 *     of the class passed as a parameter.
 * <LI><B>java.fields</B> returns a list containing all the <I>fields</I>
 *     of the class passed as a parameter.
 * </UL>
 *
 *     @author Jean-Jacques Girardot
 */
public class RefOps extends SchPrimitive {

    /**
       The prototype of RefOps.
       @see Sch.SchPrimitive
    */

    private static RefOps proto;

    public static final int OPNAME = 1;
    public static final int OPGCLASS = 2;
    public static final int OPISOBJ = 3;
    public static final int OPISCLASS = 4;
    public static final int OPCLASSOF = 5;
    public static final int OPMETHS = 6;
    public static final int OPFIELDS = 7;
    public static final int OPCONSTR = 8;
    public static final int OPERRQ = 9;
    public static final int OPINVOKE = 10;

    public static final int NBOP = OPINVOKE+1;


    /**
     *  The implementation of the declaration method.
     *  Overrides the <B>dcl()</B> method of SchPrimitive.
     */
    public static void dcl()
    {
        proto = new RefOps();

        proto.fnames = new String[NBOP];

        /**

              Declare the primitives
         */
        proto.fnames[OPISCLASS] = "class?";
        proto.fnames[OPISOBJ] = "object?";
        proto.fnames[OPERRQ] = "error?";
        proto.fnames[OPNAME] = "java.name";
        proto.fnames[OPGCLASS] = "java.get-class";
        proto.fnames[OPCLASSOF] = "java.classof";
        proto.fnames[OPMETHS] = "java.methods";
        proto.fnames[OPCONSTR] = "java.constructors";
        proto.fnames[OPFIELDS] = "java.fields";
        proto.fnames[OPINVOKE] = "java.invoke";

        Environment.dcl(proto);
    }


    /**
     *   Obtain a Java object from a Misc value.
     *   The method makes its best to convert Misc values
     *   to Java objects. booleans, chars, integers, strings
     *   and object are converted to the packages equivalent,
     *   Boolean, Character, Integer, String and Objects.
     *   Misc symbols are also converted to Strings.
     *   Other data-types of the language are converted
     *   to Void elements.
     */
    public static Object getObj(int cell)
    throws SchRunTimeException
    {
        switch (cell)
        {
        case CUNDEF :
        case CNULL : return (Object)(Void.class);
        case CFALSE : return (Object)(Boolean.FALSE);
        case CTRUE : return (Object)(Boolean.TRUE);
        }
        switch (Gdm.celltyp[cell])
        {
        case TSYM :
        case TSTR : return Gdm.cellobj[cell];
        case TINT : return (Object)(new Integer(Gdm.cellcar[cell]));
        case TCHAR : return (Object)(new Character((char)Gdm.cellcar[cell]));
        }
        if (Gdm.cellobj[cell] != null)
            return Gdm.cellobj[cell];
        return (Object)(Void.class);
    }

    /**
     *
     *  Pack a Java object as a Misc object.
     *
     *  The procedure makes its best to convert its parameter
     *  to an usable Misc value. This works correctly for
     *  Strings, Booleans, Integers and Characters.
     *  Objects of type Void become the undefined value.
     *  Other Java Objects are packaged as "object" values.
     */
    public static int pack(Object obj)
    throws SchRunTimeException
    {
        if (obj == null)
            return CNULL;
        if (obj instanceof String)
        {
            return Gdm.newstring((String)obj);
        }
        if (obj instanceof Boolean)
        {
            return ((Boolean)obj).booleanValue()  ? CTRUE : CFALSE ;
        }
        if (obj instanceof Integer)
        {
            return Gdm.newint(((Integer)obj).intValue());
        }
        if (obj instanceof Character)
        {
            return Gdm.newchar(((Character)obj).charValue());
        }
        if (obj instanceof Void)
            return CUNDEF;
        if ((obj instanceof Method) || (obj instanceof Constructor) ||
            (obj instanceof Field))
            return Gdm.newcons(obj, TJMETH);
        if (obj instanceof Throwable)
            return Gdm.newcons(obj, TERR);
        return Gdm.newcons(obj);
    }


    /**
     *  Change an array of Java objects into a Scheme list.
     *
     *  The functions use {@link #pack(Object) pack} to convert
     *  elements of the Array into Misc data-types.
     */
    public static int mklist(Object [] ao)
    throws SchRunTimeException
    {
        int res = CNULL;
        int n = Array.getLength(ao);
        Object m;
        int w;
        for (int i = 0; i<n; i++)
        {
             m = Array.get(ao, i);
             w = pack(m);
             res = Gdm.newcons(TPAIR, w, res);
        }
        return res;
    }


    /*
     *  Obtain a printable name for something.
     *
     *  Tries to invoke the "getName()" method on the Object
     *
     */
    public static String getname(Object obj, String mn)
    {
        String res = "";
        try {
            Class c = obj.getClass();
            Method m = c.getMethod(mn, new Class [0]);
            res = (String) m.invoke(obj, new Object [0]);
        } catch (Exception e) { res = ""; }
        return res;
    }

    public static String getname(Object obj)
    {
        return getname(obj, "getName");
    }


    /**
     *   Filter an array of Java Objects so their print name match some pattern.
     *
     *
     *
     */
    public static Object[] pFilter(Object [] ao, String pat)
    {
        int n = Array.getLength(ao);
        Object o;
        String sn;
        int count = 0;
        int i;
        for (i = 0; i<n; i++)
        {
            o = ao[i];
            sn = getname(o, "toString");
            if (StringsOps.match(pat,sn))
            {
                count++;
            }
            else
            {
                ao[i] = null;
            }
        }
        Object [] ar = new Object[count];
        int pr = 0;
        for (i = 0; i<n; i++)
        {
            o = ao[i];
            if (o != null)
            {
                ar [pr] = o; pr++;
            }
        }
        return ar;
    }
 


    /**
     *  Implements the run-time evaluation of the Misc primitives.
     *
     *
     */
    public void eval(int op, int count, Evaluator evl)
    throws SchRunTimeException
    {
        int cell, res, ty;
        switch (op)
        {

        /**
           Insert here new code
        */
        case OPNAME:
           {
               if (count != 1)
                  throw new SchRunTimeException(ERR1AR);
               cell = evl.valstk[evl.ptvalstk-1];
               res = CFALSE;
               Object o = Gdm.cellobj[cell];
               res = pack(getname(o));
               evl.valstk[evl.ptvalstk-1] = res;
           }
           break;


        case OPGCLASS:
           {
               if (count != 1)
                  throw new SchRunTimeException(ERR1AR);
               cell = evl.valstk[evl.ptvalstk-1];
               ty = Gdm.celltyp[cell];
               if ((ty != TSTR) && (ty != TSYM))
                  throw new SchRunTimeException(ERRDOM);
               res = CNULL;
               try {
                  Object o = Class.forName((String)Gdm.cellobj[cell]);
                  res = Gdm.newcons(o);
               }
               catch (Exception e) { }
               evl.valstk[evl.ptvalstk-1] = res;
           }
           break;


        case OPISCLASS:
           {
               if (count != 1)
                  throw new SchRunTimeException(ERR1AR);
               cell = evl.valstk[evl.ptvalstk-1];
               res = CFALSE;
               Object o = Gdm.cellobj[cell];
               if (o != null)
               {
                   res = (o instanceof Class) ? CTRUE : CFALSE;
               }
               evl.valstk[evl.ptvalstk-1] = res;
           }
           break;


        case OPCLASSOF:
           {
               if (count != 1)
                  throw new SchRunTimeException(ERR1AR);
               cell = evl.valstk[evl.ptvalstk-1];
               Class c = Sch.sch.getClass();
               Object o = Gdm.cellobj[cell];
               if (o != null)
               {
                   c = o.getClass();
               }
               evl.valstk[evl.ptvalstk-1] = Gdm.newcons((Object)c);
           }
           break;


        case OPMETHS:
           {
               if ((count < 1) || (count > 2))
                  throw new SchRunTimeException(ERR1AR);
               cell = evl.valstk[evl.ptvalstk-1];
               res = CNULL;
               int filter = 0;
               Object o = Gdm.cellobj[cell];
               String p = null;
               if (count == 2)
               {
                   evl.ptvalstk--;
                   filter = evl.valstk[evl.ptvalstk-1];
                   if (Gdm.celltyp[filter] != TSTR)
                       throw new SchRunTimeException(ERRDOM);
                   p = (String)Gdm.cellobj[filter];
               }
               if ((o != null) && (o instanceof Class))
               {
                   Object [] ma = ((Class)o).getDeclaredMethods();
                   if (filter != 0)
                       ma = pFilter(ma,p);
                   res = mklist(ma);
               }
               evl.valstk[evl.ptvalstk-1] = res;
           }
           break;


        case OPCONSTR:
           {
               if ((count < 1) || (count > 2))
                  throw new SchRunTimeException(ERR1AR);
               cell = evl.valstk[evl.ptvalstk-1];
               res = CNULL;
               int filter = 0;
               Object o = Gdm.cellobj[cell];
               String p = null;
               if (count == 2)
               {
                   evl.ptvalstk--;
                   filter = evl.valstk[evl.ptvalstk-1];
                   if (Gdm.celltyp[filter] != TSTR)
                       throw new SchRunTimeException(ERRDOM);
                   p = (String)Gdm.cellobj[filter];
               }
               if ((o != null) && (o instanceof Class))
               {
                   Object [] ma = ((Class)o).getDeclaredConstructors();
                   if (filter != 0)
                       ma = pFilter(ma,p);
                   res = mklist(ma);
               }
               evl.valstk[evl.ptvalstk-1] = res;
           }
           break;


        case OPFIELDS:
           {
               if ((count < 1) || (count > 2))
                  throw new SchRunTimeException(ERR1AR);
               cell = evl.valstk[evl.ptvalstk-1];
               res = CNULL;
               int filter = 0;
               Object o = Gdm.cellobj[cell];
               String p = null;
               if (count == 2)
               {
                   evl.ptvalstk--;
                   filter = evl.valstk[evl.ptvalstk-1];
                   if (Gdm.celltyp[filter] != TSTR)
                       throw new SchRunTimeException(ERRDOM);
                   p = (String)Gdm.cellobj[filter];
               }
               if ((o != null) && (o instanceof Class))
               {
                   Object [] ma = ((Class)o).getDeclaredFields();
                   if (filter != 0)
                       ma = pFilter(ma,p);
                   res = mklist(ma);
               }
               evl.valstk[evl.ptvalstk-1] = res;
           }
           break;


        case OPERRQ:
           {
               if (count != 1)
                  throw new SchRunTimeException(ERR1AR);
               cell = evl.valstk[evl.ptvalstk-1];
               evl.valstk[evl.ptvalstk-1] =
                    (Gdm.celltyp[cell] == TERR) ? CTRUE : CFALSE;
           }
           break;


        case OPISOBJ:
           {
               if (count != 1)
                  throw new SchRunTimeException(ERR1AR);
               cell = evl.valstk[evl.ptvalstk-1];
               Object o = Gdm.cellobj[cell];
               evl.valstk[evl.ptvalstk-1] = (o != null) ? CTRUE : CFALSE;
           }
           break;


        case OPINVOKE:
           {
               // (invoke method object par.1 par.2 ... par.n)
               int ce; Object ob, re;
               int np = count-2;
               if (np < 0)
                  throw new SchRunTimeException(ERRWAC);
               int met = evl.valstk[evl.ptvalstk-1];
               Object o = Gdm.cellobj[met];
               if (!(o instanceof Method))
                  throw new SchRunTimeException(ERRDOM);
               Method m = (Method)o;
               Object obj = Gdm.cellobj[evl.valstk[evl.ptvalstk-2]];
               Object [] tpar = new Object [np];
               for (int p=0; p<np; p++)
               {
                   ob = getObj(evl.valstk[evl.ptvalstk-3-p]);
                   tpar[p] = ob;
               }
               res = CNULL;
               try {
                    re = m.invoke(obj, tpar);
                    res = pack(re);
               } catch (Exception e) { res = CNULL; }
               evl.ptvalstk -= count-1;
               evl.valstk[evl.ptvalstk-1] = res;
           }
           break;


        default :
           throw new SchRunTimeException(ERRUSP);
        }

    }

}

 
