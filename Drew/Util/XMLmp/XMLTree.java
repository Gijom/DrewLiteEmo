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
 * File: XMLTree.java
 * Author: Jean-Jacques Girardot
 * Description:
 *
 * $Id: XMLTree.java,v 1.11 2006/04/28 08:35:29 girardot Exp $
 */

package Drew.Util.XMLmp;

import java.util.NoSuchElementException;
import java.util.Enumeration;

/**
   A cheap implantation of XML Trees

 */
public class XMLTree extends Pair {


	/**
	 * Purpose of this class is managing contents or attributes of an XMLTree as
	 * an Enumeration
	 */
	private class ObjectList implements java.util.Enumeration {
		private Pair link = nil;

		/**
		 * Construct iterator from a Pair
		 */
		ObjectList( Pair p) {
			link = new Pair(nil,p);
		}

		/**
		 * Look if it's the end of the list
		 */
		public boolean hasMoreElements() {
			return  link.hasMoreElements();
		}
	
		/**
		 * return the element reference itself, not the Pair refering to. Here
		 * we should find Strings or XMLTree for content Liste or attributes itself
		 * for attributes list
		 */
		public Object nextElement() throws NoSuchElementException {
			link = (Pair)link.nextElement();
			return link.lecar;
		}
	}

   /**
      Create an instance of XMLTree, whose car is Pair(nil,nil),
      and whode cdr is nil.
   */
   public XMLTree()
   {
      lecar = new Pair();
      lecdr = nil;
   }

   /**
      Create an instance of XMLTree, whose tag ident is the
      string passed as parameter.
   */
   public XMLTree(String s)
   {
      lecar = new Pair(s,nil);
      lecdr = nil;
   }

   public XMLTree(String s, Pair atts, Pair cont)
   {
       lecar = new Pair(s,atts);
       lecdr = cont;
   }

   public XMLTree(String s, Object cont)
   {
      lecar = new Pair(s,nil);
      lecdr = new Pair(cont,nil);
   }

   /**
    * Create XMLTree from a pair, using first element of the list (PJ-20021010)
    */
   public XMLTree(Pair p)
   {
      if (p.lecar instanceof Pair) {
	  p = (Pair)p.lecar;
          lecar = p.lecar;
          lecdr = p.lecdr;
      }
      else  {
          lecar = new Pair();
          lecdr = nil;
      }
   }

   /**
       Returns the tag name of the XMLTree.
   */
   public String tag()
   {
      Pair p;
      if (!(lecar instanceof Pair))
          return "";
      p = (Pair)lecar;
      if (!(p.lecar instanceof String))
          return "";
      return (String) p.lecar;
   }

   /**
       Returns the list that represent the contents of the tree.
   */
   public Pair contents()
   {
       if (lecdr instanceof Pair)
           return (Pair)lecdr;
       return nil;
   }

   public Enumeration elements()
   {
       if (lecdr instanceof Pair)
           return new ObjectList((Pair)lecdr);
       return new ObjectList(nil);
   }

   /**
       Returns the list that represent the attributes of the tree.
   */
   public Enumeration attributes()
   {
       Pair p;
       if (!(lecar instanceof Pair)) return new ObjectList(nil);
       p = (Pair)lecar;
       if (!(p.lecdr instanceof Pair)) return new ObjectList(nil);
       return new ObjectList((Pair) p.lecdr);

   }

/*
   public Pair attributes()
   {
       Pair p;
       if (!(lecar instanceof Pair))
          return nil;
       p = (Pair)lecar;
       if (!(p.lecdr instanceof Pair))
          return nil;
      return (Pair) p.lecdr;
   }
*/
   /**
       Add a new element to the contents of the tree, at the end
       of the content.
   */
   public XMLTree add(Object obj)
   {
       Pair p = this;
       while ((p.lecdr instanceof Pair) && (p.lecdr != nil))
           p = (Pair)p.lecdr;
       p.lecdr = new Pair(obj,nil);
       return this;
   }

   /**
       insert obj as the first element in the content of the tree (PJ 20021010)	
   */
   public XMLTree insert(Object obj)
   {
       Pair p = (Pair)lecdr;
       lecdr = new Pair(obj,p);
       return this;
   }

   /**
       remove obj from the content of the tree and return it (PJ 20021010)	
   */
   public Object remove(Object obj) throws NoSuchElementException
   {
       Pair p = this;
       while ((p.lecdr instanceof Pair) && (p.lecdr != nil)) {
	   Pair pp = (Pair)p.lecdr;
           if( pp.lecar == obj ) {
		p.lecdr = pp.lecdr;
		return pp.lecar;
	   }
	   p = (Pair)p.lecdr;
       }
       throw new NoSuchElementException();
   }

   /**
   *	Get the first XMLTree with the tag equal to name (PJ 20021010)
   */
   public XMLTree getByTagName( String name ) throws NoSuchElementException
   {
	Pair p = this;
	while ((p.lecdr instanceof Pair) && (p.lecdr != nil)) {
		p = (Pair)p.lecdr;
		if( p.lecar instanceof Pair) {
			XMLTree x = (XMLTree)p.lecar;
			if( name.equalsIgnoreCase( x.tag()) ) return x;
		}
	}
	throw new NoSuchElementException();
   }

   /** 
	Build the string content of this XMLTree (PJ 20021010)
   */
   public String getText() 
   {
	Pair p = this;
	String res = "";
	while ((p.lecdr instanceof Pair) && (p.lecdr != nil)) {
		p = (Pair)p.lecdr;
		if( p.lecar instanceof Pair) {
			XMLTree x = (XMLTree)p.lecar;
			res += x.getText();	
		}
		else {
			res += p.lecar.toString();
		}
	}
	return res;
   }

   /**
	Set the tag name of the element to the parameter
   */
   public XMLTree setTagName( String s) {
	lecar = new Pair(s,lecar==null ? nil : ((Pair)lecar).lecdr );
	return this;
   }

   /**
	Set the contents of the element to the parameter
   */
   public XMLTree setContents(Object t) {
	lecdr = new Pair(t,nil);
	return this;
   }

   /**
       Add, or change the value of an attribute.
   */
   public XMLTree setAttribute(Pair p) {
	return setAttribute( p.lecar.toString(), p.lecdr );
   }

   public XMLTree setAttribute(String n, Object v)
   {
       Pair p = (Pair)this.lecar, q;
       while ((p.lecdr instanceof Pair) && (p.lecdr != nil))
       {
           p = (Pair)p.lecdr;
           if (p.lecar instanceof Pair)
           {
               q = (Pair)p.lecar;
               if (n.equals(q.lecar))
               {
                   q.lecdr = v;
                   return this;
               }
           }
       }
       p.lecdr = new Pair(new Pair(n,v),nil);
       return this;
   }


   /**
      Get an attribute ; return a default value if
      the attribute is not present.
   */
   public String getAttributeValue(String n, String def)
   {
       Pair p = (Pair)this.lecar, q;
       while ((p.lecdr instanceof Pair) && (p.lecdr != nil))
       {
           p = (Pair)p.lecdr;
           if (p.lecar instanceof Pair)
           {
               q = (Pair)p.lecar;
               if (n.equals(q.lecar))
               {
                   return (String)q.lecdr;
               }
          }
      }
      return def;
   }

               
   /**
      Get an attribute ; return a null value if
      the attribute is not present.
   */
   public String getAttributeValue(String n)
   {
       return getAttributeValue(n, null);
   }


   /**
      Construct an empy contents for a Tree.
   */
   public static Pair Contents()
   {
      return nil;
   }

   public static Pair Contents(Object a)
   {
      return new Pair(a,nil);
   }

   public static Pair Contents(Object a, Object b)
   {
      return new Pair(a, new Pair(b,nil));
   }

   public static Pair Contents(Object a, Object b, Object c)
   {
      return new Pair(a, new Pair(b, new Pair(c,nil)));
   }

   public static Pair Contents(Object a, Object b, Object c, Object d)
   {
      return new Pair(a, new Pair(b, new Pair(c, new Pair(d,nil))));
   }

   public static Pair Contents(Object a, Object b, Object c, Object d,
                               Object e)
   {
      return new Pair(a, new Pair(b, new Pair(c, new Pair(d,
          new Pair(e,nil)))));
   }

   public static Pair Contents(Object a, Object b, Object c, Object d,
                               Object e, Object f)
   {
      return new Pair(a, new Pair(b, new Pair(c, new Pair(d,
          new Pair(e, new Pair(f,nil))))));
   }

   public static Pair Attributes()
   {
      return nil;
   }

   public static Pair Attributes(String n1, Object v1)
   {
      return new Pair(new Pair(n1, v1.toString()), nil);
   }

   public static Pair Attributes(String n1, Object v1, String n2, Object v2)
   {
      return new Pair(new Pair(n1, v1.toString()), 
          new Pair(new Pair(n2, v2.toString()), nil));
   }

   public static Pair Attributes(String n1, Object v1, String n2, Object v2,
                                 String n3, Object v3)
   {
      return new Pair(new Pair(n1, v1.toString()), 
          new Pair(new Pair(n2, v2.toString()), 
            new Pair(new Pair(n3, v3.toString()), nil)));
   }

   public static Pair Attributes(String n1, Object v1, String n2, Object v2,
                                 String n3, Object v3, String n4, Object v4)
   {
      return new Pair(new Pair(n1, v1.toString()), 
          new Pair(new Pair(n2, v2.toString()), 
            new Pair(new Pair(n3, v3.toString()), 
              new Pair(new Pair(n4, v4.toString()), nil))));
   }

   public static Pair Attributes(String n1, Object v1, String n2, Object v2,
                                 String n3, Object v3, String n4, Object v4,
                                 String n5, Object v5)
   {
      return new Pair(new Pair(n1, v1.toString()),
          new Pair(new Pair(n2, v2.toString()),
            new Pair(new Pair(n3, v3.toString()),
              new Pair(new Pair(n4, v4.toString()),
                new Pair(new Pair(n5, v5.toString()), nil)))));
   }

   public static Pair Attributes(String n1, Object v1, String n2, Object v2,
                                 String n3, Object v3, String n4, Object v4,
                                 String n5, Object v5, String n6, Object v6)
   {
      return new Pair(new Pair(n1, v1.toString()),
          new Pair(new Pair(n2, v2.toString()),
            new Pair(new Pair(n3, v3.toString()),
              new Pair(new Pair(n4, v4.toString()),
                new Pair(new Pair(n5, v5.toString()),
                  new Pair(new Pair(n6, v6.toString()), nil))))));
   }

}

