

package Drew.TraceViewer;
/*
xmlns:pws="http://www.euroscale.org/PWS/version-0.01"
pws:getDate
*/
import java.util.*;
import java.text.*;

public class foo {

	public static String getDate( String str ) {
		String strdate = "Unknown";
		try {
			Date date = new Date( Long.parseLong(str));
			//strdate = DateFormat.getDateTimeInstance(DateFormat.LONG,DateFormat.LONG,Locale.FRANCE).format(date);
			strdate = DateFormat.getDateTimeInstance().format(date);
		}
		catch( Exception e ) {}
		return strdate;
	}

	public static String getXlsDate( String str ) {
		String strdate = "Unknown";
		try {
			// 2006-07-25T12:31:24.000
			Date date = new Date( Long.parseLong(str));
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.S");
                        strdate = df.format(date);
		}
		catch( Exception e ) {}
		return strdate;
	}

	public static String getSlkDate( String str ) {
		double date = 0.;
		try {
			// 2006-07-25T12:31:24.000
			long curr = Long.parseLong(str);
			// excel store date as number of day from 1/1/1900 (in window mode), java in milli seconds from 1/1/1970, GMT zone.
			// 25569 is the number of days between 1/1/1900 and 1/1/1970.
			date = curr/(60.*60.*24.*1000.) + 25569 + (2*1./24.);
		}
		catch( Exception e ) {}
		return String.valueOf(date);
	}
}
