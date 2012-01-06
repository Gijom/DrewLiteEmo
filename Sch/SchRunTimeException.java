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
 * File: SchRunTimeException.java
 * Author:
 * Description:
 *
 * $Id: SchRunTimeException.java,v 1.4 2003/11/24 10:54:31 serpaggi Exp $
 */

package Sch;

/**
 *
 *  Signalling Errors
 *
 *     @author Jean-Jacques Girardot
 */
public class SchRunTimeException extends Exception {
   int errno;
   String cpl;

   public SchRunTimeException()
   {
       super("Undefined SchRunTimeException");
       errno = 0;
       cpl = "";
   }

   public SchRunTimeException(String str)
   {
       super(str);
       errno = 0;
       cpl = "";
   }

   public SchRunTimeException(int err)
   {
       super("SchRunTimeException");
       errno = err;
       cpl = "";
   }

   public SchRunTimeException(int err, String s)
   {
       super("SchRunTimeException");
       errno = err;
       cpl = s+" ";
   }

   public String getMessage()
   {
       if (errno == 0)
       {
           return super.getMessage();
       }
       else
       {
          String s = cpl;
          int err = errno & 0xff;
          int n=errno >> 8;
          switch (n)
          {
          case 0 :
              switch (err)
              {
              case Globaldata.ERRCXP :
                  return s+"pair expected";
              case Globaldata.ERRLXP :
                  return s+"list expected";
              case Globaldata.ERR0AR :
                  return s+"no argument expected";
              case Globaldata.ERR1AR :
                  return s+"one argument expected";
              case Globaldata.ERR2AR :
                  return s+"two arguments expected";
              case Globaldata.ERRUND :
                  return s+"undefined";
              case Globaldata.ERRSYM :
                  return s+"symbol expected";
              case Globaldata.ERRSYN :
                  return s+"syntax error";
              case Globaldata.ERRMFL :
                  return s+"memory full";
              case Globaldata.ERRCLT :
                  return s+"circular structure";
              case Globaldata.ERRINT :
                  return s+"integer expected";
              case Globaldata.ERRUNA :
                  return s+"unapplicable object";
              case Globaldata.ERRUNO :
                  return s+"unexpected object";
              case Globaldata.ERRUNC :
                  return s+"unknown op code";
              case Globaldata.ERRWAC :
                  return s+"wrong argument count";
              case Globaldata.ERREEX :
                  return s+"environment expected";
              case Globaldata.ERRZDV :
                  return s+"zero divide";
              case Globaldata.ERRTYE :
                  return s+"type error";
              case Globaldata.ERRSTKF :
                  return s+"stack full";
              case Globaldata.ERRSTKE :
                  return s+"stack empty";
              case Globaldata.ERRPROC :
                  return s+"procedure expected";
              case Globaldata.ERRNEC :
                  return s+"not enough cells";
              case Globaldata.ERRRSE:
                  return s+"read syntax error";
              case Globaldata.ERRUSP:
                  return s+"undefined system primitive";
              case Globaldata.ERRDOM:
                  return s+"domain error";
              case Globaldata.ERROPF:
                  return s+"can't open file";
              case Globaldata.ERRWRT:
                  return s+"write error";
              case Globaldata.ERREAD:
                  return s+"read error";
              case Globaldata.ERRPCL:
                  return s+"port closed";
              case Globaldata.ERRNEM :
                  return s+"not enough memory";
              case Globaldata.ERRSYS :
                  return s+"system error";
              }
          default : s = s + "Type " + n + " "; break;
          case 1 : s = s + "Arithmetic "; break;
          case 2 : s = s + "Memory "; break;
          }
          s = "Sch " + s + "Exception " + err;
          return s;
       }
   }

}

