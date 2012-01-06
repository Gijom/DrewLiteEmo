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
 * File: SinkWriter.java
 * Author:
 * Description:
 *
 * $Id: SinkWriter.java,v 1.4 2003/11/24 10:54:31 serpaggi Exp $
 */

package Sch;
import java.io.*;


/**
 *
 *  A writer that absorbs characters
 *
 *
 *
 *     @author Jean-Jacques Girardot
 */

public class SinkWriter extends Writer {

   private Object lk;

   public SinkWriter()
   {
       lk = null;
   }

   public SinkWriter(Object lock)
   {
       lk = lock;
   }


   public void close()
   {
   }

   
   public void flush()
   {
   }
 
   public void write(char[] cbuf, int off, int len)
   {
   }

   public void write(int c) 
   {
   }

   public void write(String str)
   {
   }

   public void write(String str, int off, int len)
   {
   }

   public void write(char[] cbuf)
   {
   }
}

