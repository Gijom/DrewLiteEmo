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
 * File: MyAppletStub.java
 * Author:
 * Description:
 *
 * $Id: MyAppletStub.java,v 1.5 2007/02/20 16:03:41 collins Exp $
 */


package Drew.Client.App;

import java.applet.*;
import java.net.*;

class MyAppletStub implements AppletStub {
private AppletContext context;

        public MyAppletStub( java.awt.Toolkit tk ) {
		context = new MyAppletContext( tk );
        }

        public boolean isActive() {
                return true;
        }

        private URL urlByProperty( String str )  {
	URL url;

		try {
			url = new URL( getParameter( str ) );
		}
		catch( MalformedURLException e) {
			url = null;
		}
		return url;
	}

        public URL getDocumentBase()  {
                return urlByProperty("DocumentBase");
        }

        public URL getCodeBase() {
                return urlByProperty("CodeBase");
        }

        public String getParameter(String name) {
                if( System.getProperty(name) != null )  {
			return System.getProperty(name);
		}
		else {
			return System.getProperty("drew."+name);
		}
        }

        public AppletContext getAppletContext() {
                return context;
        }

        public void appletResize(int width,int height) {
                System.out.println("appletResize : " + width + ", " + height);
        }
}
