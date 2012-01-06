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
 * File: Duration.java
 * Author:
 * Description:
 *
 * $Id: Duration.java,v 1.2 2003/11/24 10:54:26 serpaggi Exp $
 */             
        
package Drew.Client.Util;

public class Duration {

private long begin, end;
private boolean valid = false;

	public Duration() {
		clear();
	}

	public void init() {
		valid = true;
		begin = System.currentTimeMillis();	
	}

	public void stamp() {
		end = System.currentTimeMillis();	
	}

	public long value() {
		if( valid ) return end - begin; else return -1;
	}

	public void clear() {
		valid = false;	
	}

	public boolean isCleared() {
		return valid == false;
	}

	public String toString() {
		return String.valueOf( value() );
	}
        
        public long getStampTime() {
            if( valid ) return end; else return -1;
        }
}
