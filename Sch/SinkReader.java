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
 * File: SinkReader.java
 * Author:
 * Description:
 *
 * $Id: SinkReader.java,v 1.4 2003/11/24 10:54:31 serpaggi Exp $
 */

package Sch;
import java.io.*;


/**
 *
 *  A Reader that returns end-of-files
 *
 *     @author Jean-Jacques Girardot
 */

public class SinkReader extends Reader {

   private Object lk;

   public SinkReader()
   {
       lk = null;
   }

   public SinkReader(Object lock)
   {
       lk = lock;
   }


   public void close()
   {
   }

   
   public boolean markSupported()
   {
       return false;
   }
 
   public void mark(int n)
   {
   }
 
   public long skip(int n)
   {
        return 0L;
   }
 
   public int read() 
   {
       // Return "end of file"
       return -1;
   }

   public int read(char[] cbuf)
   {
       // Return "no char read"
       return -1;
   }

   public int read(char[] cbuf, int off, int len)
   {
       // Return "no char read"
       return -1;
   }

   public boolean ready()
   {
       return true;
   }

   public void reset()
   {
   }
}

