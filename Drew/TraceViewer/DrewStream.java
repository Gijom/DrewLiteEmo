
package Drew.TraceViewer;

import java.io.*;

public class DrewStream extends InputStream {

private final static int READ  = 1;
private final static int END   = 2;
private final static int CLOSE = 3;

private InputStream in = null;
private int BSIZE = 4096;
private byte buff[] = new byte[BSIZE];
private int curr =0, end = 0;
private int state = 0;

	public DrewStream( InputStream in ) {
		this.in = in;
		end = set( "<?xml version=\"1.0\"?>\n<drew>" ); state = READ;
	} 

	protected int set( String str ) {
	byte s[] = str.getBytes();

		System.arraycopy( s, 0, buff, 0, s.length );
		return s.length;
	}

	protected int buffread() throws IOException {
		curr = 0;
		end = in.read( buff );
		if( (end == -1) && (state != END) ) {
			end = set( "</drew>\n" );	
			state = END;
		}
		return end;
	}

// implement my version of InputStream

	public void close() throws IOException {
		in.close(); state = CLOSE;
	}

	public int read() throws IOException {
		if( curr >= end ) buffread();
		if( end == -1 ) return -1;
		return (int)buff[curr++];
	}
	
	public static void main( String[] args ) {
		try {
			InputStream in = new DrewStream( new FileInputStream(args[0]));

			int c;	
			while( (c = in.read() ) != -1 ) {
				System.out.print( (char)c );
				System.out.flush();
			}
			in.close();
		}
		catch( IOException e ) {}
	}
}

