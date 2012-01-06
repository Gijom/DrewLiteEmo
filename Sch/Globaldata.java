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
 * File: Globaldata.java
 * Author: Jean-Jacques Girardot
 * Description:
 *
 * $Id: Globaldata.java,v 1.5 2006/04/28 08:02:51 girardot Exp $
 */

package Sch;

/**
 *   Class defining Common Data
 *
 *
 *    All the constants and the main variables of the interpreter.
 *
 *    This class defines no methods, only class variables that are
 *    herited by other classes
 *
 *     @author Jean-Jacques Girardot
 */
import java.util.*;
public class Globaldata {
 
   // Constants that can be modified 
   // 
   // Initial size of control and value stacks
   public static final int KSSTACK=16;
   // Maximum size of control and value stacks
   public static final int KSMSTACK=65536;
   // Initial number of memory cells
   public static final int KSGDM=0x2000;
   // Maximum size of memory : 16M cells
   public static final int KSMGDM=0x1000000;
   // Default thread tick-count
   //public static final long KSEVTCC=0x7FFFFFFFFFFFFFFFL;
   public static final long KSEVTCC=0x7FFFL;
   // Maximum number of printed objects in a call to print methods
   public static final int KSMPRC=0x1000;

   //
   // Messages 
   public static final String SCHVERS="1.1.5";
   public static final String SCHNAME="Misc";
   public static final String MSGINI=SCHNAME+" V"+SCHVERS+" Started";
   public static final String MSGEND=SCHNAME+" ended";

   //
   // No useful modifications can be made after this comment.
   //

   // Running mode
   // Run in interactive mode
   public static final int RFINTER = 1;
   // Using Terminal input
   public static final int RFTERIN = 2;
   // Using terminal output
   public static final int RFTEROUT = 4;
   // Run flags
   public static int runmode = 0;

   //
   // Trace
   // Trace infos after execution
   public static final int TRPRINFO = 1;
   // Trace exec. stack in case of error
   public static final int TRSTACK = 2;
   // Trace compiled code
   public static final int TRCODE = 4;
   // Trace internal op-codes
   public static final int TREVAL = 8;
   // Trace some GDM infos
   public static final int TRPRGDM = 16;
   // Trace back-quote operation
   public static final int TRPBQOP = 32;
   public static final int TRPBQOP2 = 64;
   // Trace flags
   public static int trace=0;

   //
   // Debug
   public static int debug=0;
   // Debug GDM
   public static final int DBGGDM = 1;
   // Check GDM before any primitive
   public static final int DBGCHK = 2;


   //
   // Données de base
   // Current reader
   public static SchReader scr;
   // ...and its port
   public static int CurInpP;
   // Current printer
   public static SchPrinter spr;
   // ...and its port
   public static int CurOutP;
   // Current I/O tracer
   public static SchPrinter str;
   // ...and its port
   public static int CurTrcP;
   // Current evaluator
   public static Evaluator evl;
   // R5RS "Empty" environment
   public static int emptenv;
   // R5RS "System" environment
   public static int systenv;
   // User initial environment
   public static int globenv;
   // Scheme evaluator
   public static int schevl;
   // General time-stamp
   public static int schts;

   //
   // Execution control flag
   public static boolean flagexec;

   //
   // Data types
   public static final byte TNULL = 0;        // 0
   public static final byte TFREE = TNULL+1;
   public static final byte TPAIR = TFREE+1;
   public static final byte TINT = TPAIR+1;
   public static final byte TCHAR = TINT+1;
   public static final byte TSTR = TCHAR+1;
   public static final byte TSYM = TSTR+1;   
   public static final byte TERR = TSYM+1;   
   public static final byte TEOF = TERR+1;
   public static final byte TSPEC = TEOF+1;
   public static final byte TSUBR = TSPEC+1;
   public static final byte TMACRO = TSUBR+1;
   public static final byte TBOOL = TMACRO+1;
   public static final byte TLAMBDA = TBOOL+1; 
   public static final byte TENV = TLAMBDA+1;
   public static final byte TPORT = TENV+1;
   public static final byte TVC = TPORT+1;
   public static final byte TUNDEF = TVC+1; 
   public static final byte TCTRL = TUNDEF+1;  
   public static final byte TTHRD = TCTRL+1;
   public static final byte TKONT = TTHRD+1;
   public static final byte TDELAY = TKONT+1;
   public static final byte TCOMM = TDELAY+1;
   public static final byte TLOCK = TCOMM+1;
   public static final byte TCELL = TLOCK+1;
   public static final byte TBARR = TCELL+1;
   public static final byte TJOBJ = TBARR+1;
   public static final byte TJMETH = TJOBJ+1;

   //
   // Internal sub-types of PORTS
   public static final int PSTIN   = 1;
   public static final int PSTOUT  = 2;
   public static final int PSTIO   = 3;
   public static final int PSTISR  = 0x200;
   public static final int PSTSTRING = 0x400;
   public static final int PSTFCLOSE = 0x800;
   // Ports status
   public static final int POPENED = 1;
   public static final int PCLOSED = 0;

   //
   // Evaluator Return Codes
   //
   //    End of execution
   public static final byte RCINI  = 1;
   public static final byte RCRUN  = 2;
   public static final byte RCEND  = 3;
   //    Halt
   public static final byte RCHLT  = 4;
   //    Ticks-count exhausted
   public static final byte RCCNT  = 5;
   public static final byte RCNXT  = 6;
   //    Evaluator in error
   public static final byte RCERR  = 7;
   //    Evaluator yielding hand
   public static final byte RCYEL  = 8;
   //    Evaluator waiting
   public static final byte RCWAIT = 9;


   // Errors
   //    no argument expected
   public static final byte ERR0AR = 1;
   //    one argument expected
   public static final byte ERR1AR = 2;
   //    two arguments expected
   public static final byte ERR2AR = 3;
   //    undefined
   public static final byte ERRUND = 4;
   //    symbol expected
   public static final byte ERRSYM = 5;
   //    syntax error
   public static final byte ERRSYN = 6;
   //    memory full
   public static final byte ERRMFL = 7;
   //    circular structure
   public static final byte ERRCLT = 8;
   //    integer expected
   public static final byte ERRINT = 9;
   //    unapplicable object
   public static final byte ERRUNA = 10;
   //    unexpected object
   public static final byte ERRUNO = 11;
   //    unknown op code
   public static final byte ERRUNC = 12;
   //    wrong argument count
   public static final byte ERRWAC = 13;
   //    environment expected
   public static final byte ERREEX = 14;
   //    zero divide
   public static final byte ERRZDV = 15;
   //    type error
   public static final byte ERRTYE = 16;
   //    stack full
   public static final byte ERRSTKF = 17;
   //    stack empty
   public static final byte ERRSTKE = 18;
   //    procedure expected
   public static final byte ERRPROC = 19;
   //    not enough cells
   public static final byte ERRNEC  = 20;
   //    read syntax error
   public static final byte ERRRSE = 21;
   //    pair expected
   public static final byte ERRCXP = 22;
   //    list expected
   public static final byte ERRLXP = 23;
   //    undefined system primitive
   public static final byte ERRUSP = 24;
   //    domain error
   public static final byte ERRDOM = 25;
   //    can't open file
   public static final byte ERROPF = 26;
   //    write error
   public static final byte ERRWRT = 27;
   //    read error
   public static final byte ERREAD = 28;
   //    port closed
   public static final byte ERRPCL = 29;
   //    Not enough memory
   public static final byte ERRNEM = 30;
   //    System Error
   public static final byte ERRSYS = 31;


   // ASCII Chars Names
   public static final int ACNL    = 10;
   public static final int ACCR    = 13;
   public static final int ACSP    = 32;
   // Other Chars
   public static final int ACEOF  = -1;


   // Some atoms
   // null, ()
   public static final int CNULL   = 0;
   // undefined value
   public static final int CUNDEF  = CNULL+1;
   // #f
   public static final int CFALSE  = CUNDEF+1;
   // #t
   public static final int CTRUE   = CFALSE+1;
   // {end of file}
   public static final int CEOF    = CTRUE+1;
   // {open parenthesis token}
   public static final int CTOP    = CEOF+1;
   // {close parenthesis token}
   public static final int CTCP    = CTOP+1;
   // {dot token}
   public static final int CTDOT   = CTCP+1;
   // {quote token}
   public static final int CTQUOT  = CTDOT+1;
   // {backquote token}
   public static final int CTBQUOT = CTQUOT+1;
   // {comma token}
   public static final int CTCOMMA = CTBQUOT+1;
   // {comma-at token}
   public static final int CTCOMAT = CTCOMMA+1;
   // {read error}
   public static final int CTERR   = CTCOMAT+1;
   // {non displaying object}
   public static final int CNDO    = CTERR+1;
   // Atom quote
   public static final int CQUOTE  = CNDO+1;
   // Atom define
   public static final int CDEFINE = CQUOTE+1;
   // Atom if
   public static final int CIF     = CDEFINE+1;
   // Atom begin
   public static final int CBEGIN  = CIF+1;
   // Atom set!
   public static final int CSET    = CBEGIN+1;
   // Atom lambda
   public static final int CLAMBDA = CSET+1;
   // Atom and
   public static final int CAND    = CLAMBDA+1;
   // Atom or
   public static final int COR     = CAND+1;
   // Atom case
   public static final int CCASE   = COR+1;
   // Atom cond
   public static final int CCOND   = CCASE+1;
   // Atom delay
   public static final int CDELAY  = CCOND+1;
   // Atom do
   public static final int CDO     = CDELAY+1;
   // Atom else
   public static final int CELSE   = CDO+1;
   // Atom let
   public static final int CLET    = CELSE+1;
   // Atom let*
   public static final int CLETE   = CLET+1;
   // Atom letrec
   public static final int CLETR   = CLETE+1;
   // Atom fluid-let
   public static final int CFLLET  = CLETR+1;
   // Atom quasiquote
   public static final int CQQUOTE = CFLLET+1;
   // Atome unquote
   public static final int CUQUOTE = CQQUOTE+1;
   // Atome unquote-splicing
   public static final int CUQUOTES= CUQUOTE+1;
   // Atome =>
   public static final int CAPPLY  = CUQUOTES+1;
   //
   // Op codes
   // End of instruction
   public static final int BCENDX  = CAPPLY+1;
   // "n" parameters function call
   public static final int BCFnn   = BCENDX+1;
   // "0" parameters function call
   public static final int BCF00   = BCFnn+1;
   // "1" parameter function call
   public static final int BCF01   = BCF00+1;
   // "2" parameters function call
   public static final int BCF02   = BCF00+2;
   // "3" parameters function call
   public static final int BCF03   = BCF00+3;
   // "4" parameters function call
   public static final int BCF04   = BCF00+4;
   // "5" parameters function call
   public static final int BCF05   = BCF00+5;
   // "6" parameters function call
   public static final int BCF06   = BCF00+6;
   // "7" parameters function call
   public static final int BCF07   = BCF00+7;
   // "8" parameters function call 
   public static final int BCF08   = BCF00+8;
   public static final int MAXARGC = 8;
   //
   // map - phase 2
   public static final int BCMAP = BCF08+1;
   // dynamic-wind, phases 1-4
   public static final int BCDWBFR = BCMAP+1;
   public static final int BCDWACT = BCDWBFR+1;
   public static final int BCDWAFT = BCDWACT+1;
   public static final int BCDWDAT = BCDWAFT+1;
   // try-catch, phases 1-2
   public static final int BCTRY1  = BCDWDAT+1;
   public static final int BCTRY2  = BCTRY1+1;
   // add to environment
   public static final int BCADENV = BCTRY2+1;
   // let forms
   public static final int BCLET   = BCADENV+1;
   // letrec forms
   public static final int BCLETREC= BCLET+1;
   // delay execution, phase 2
   public static final int BCDEL2  = BCLETREC+1;
   // delay execution, phase 1
   public static final int BCDELAY = BCDEL2+1;
   // halt execution
   public static final int BCHALT  = BCDELAY+1;
   // lambda execution
   public static final int BCLMBDA = BCHALT+1;
   // quote execution
   public static final int BCQUOTE = BCLMBDA+1;
   // set! execution
   public static final int BCSET   = BCQUOTE+1;
   // set and unstack value
   public static final int BCSETU  = BCSET+1;
   // define execution
   public static final int BCDEF   = BCSETU+1;
   // set environment
   public static final int BCLDENV = BCDEF+1;
   // or execution
   public static final int BCOR    = BCLDENV+1;
   // and execution
   public static final int BCAND   = BCOR+1;
   // unstack value
   public static final int BCPOPV  = BCAND+1;
   // if execution
   public static final int BCIF    = BCPOPV+1;
   // the => part of cond 
   public static final int BCCOND  = BCIF+1;
   // the case operation
   public static final int BCCASE  = BCCOND+1;
   // the no-op operation
   public static final int BCNOP   = BCCASE+1;
   // the "false" operation
   public static final int BCFALSE = BCNOP+1;
   // yield control
   public static final int BCYIEL  = BCFALSE+1;
   // wait
   public static final int BCWAIT  = BCYIEL+1;
   //
   // First object to be marked by the garbage collector
   public static final int CLAST   = BCWAIT+1; // dernier
   // Macro-environement
   public static final int Cemptyenv = CLAST; 
   // system environment
   public static final int Csysenv = Cemptyenv+1; 
   // user environment
   public static final int Cusrenv = Csysenv+1; 
   // atomes
   public static final int Catms   = Cusrenv+1;
   // value-cell of last result "it"
   public static final int Cit     = Catms+1;
   // Current evaluator
   public static final int Cev     = Cit+1;
   // exec. queue
   public static final int Cxq     = Cev+1;
   // wait queue
   public static final int Cwq     = Cxq+1;
   // end queue
   public static final int Ceq     = Cwq+1;
   // terminal input
   public static final int CstdIn  = Ceq+1;
   // terminal output
   public static final int CstdOut = CstdIn+1;
   // trace port
   public static final int CstdTrc = CstdOut+1;
   // sink output port
   public static final int CstdNulOut = CstdTrc+1;
   // sink input port
   public static final int CstdNulIn = CstdNulOut+1;
   // first free cell
   public static final int CFREE   = CstdNulIn+1;

   // Some shared values
   public static int refcons;
   public static int refappend;
}

