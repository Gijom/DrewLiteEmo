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
 * File: ExternalService.java
 * Author: Ph.jaillon
 * Description:
 *
 * $Id: ExternalService.java,v 1.5 2003/11/24 10:54:28 serpaggi Exp $
 */

package Drew.Serveur;

import java.util.Hashtable;

/**
 * The main goal of this class is to manage external services for
 * the GrandSioux server. 
 * An external service is the association of an event type and a 
 * class for its processing. This class must implements the
 * service interface.
 * The association is made throught the ExternalService.user and
 * ExternalService.room properties.
 *
 * @see Drew.Serveur.GrandSioux
 * @see Drew.Serveur.Service
 */
class ExternalService {

static private Hashtable H = new Hashtable();
/**
 * Looking for the property ExternalService.user/room.eventtype in the
 * service class cache (H). Load and get an instance of this service
 * if found in cache.
 * 
 * @parameters op a string discribing the service like room.chat or user.dict
 */
	static Service get( String op ) {
		Class c = (Class)H.get( op );

		try {
			if( c == null ) {
				c = Class.forName(Config.properties.getProperty( "ExternalService." + op ));
				H.put(op,c);
			}
			return (Service)c.newInstance();
		}
		catch( Exception e ) {
			System.out.println( "Cant't get ExternalService for : " + op + ", return default" );
			return new DefaultService();
		}
	}

/**
 * Get the object managing type.op service (type is room or user) and op is the 
 * kind of event (dict, cht ...)
 */
	static Service get( String type, String op ) {
		return get( type + "." + op );
	}

/**
 * test the external service existence for such service type
 */
	static boolean exist( String op ) {
		return Config.properties.getProperty( "ExternalService." + op ) != null;
	}

/**
 * test the external service existence for such service type
 */
	static boolean exist( String type, String op ) {
		return Config.properties.getProperty( "ExternalService." + type + "." + op ) != null;
	}
}
