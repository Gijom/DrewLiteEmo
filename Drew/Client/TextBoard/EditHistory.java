package Drew.Client.TextBoard;

import java.awt.Color;
import java.util.*;

import javax.swing.text.*;

import Drew.Client.TextBoard.TextBoard.EditOwnerDocument;

public class EditHistory {
	
	public Modif firstbydate;
	public Modif lastbydate;
	public Modif firstbyoffset;
	public Modif lastbyoffset;
	
	EditHistory() {//constructeur de History
	}
	
	/**
	 * cette classe est untiliser pour mettre à jour la liste des modifications apres chaque 
	 * nouvelle insertion faite dans le document
	 * @param offs
	 * @param length
	 * @param user
	 */
	
	public void insertModif(int offs,int length, String user){
		Modif newModif = new Modif(offs,length,user);
	
		
		Modif mod = this.lastbyoffset;
		// find *mod* whose offset is strictly before that of newmodif
		while (mod!= null && mod.offs>=newModif.offs) {
			mod.offs+=newModif.length;
			mod=mod.prevbyoffset; // update offsets to take insertion before them into account
		}	
		if (mod!=null) {
			if (mod.offs+mod.length!=newModif.offs) { // newModif needs to be inserted into mod
				int delta = newModif.offs-mod.offs;

				//mod becomes mod,right splitting mod at delta
				Modif right = new Modif(newModif.offs+newModif.length,mod.length-delta, mod.owner);
				right.createTime=mod.createTime;
				mod.length = delta;

				// update date list on mod,right
				right.nextbydate=mod.nextbydate;
				if (right.nextbydate!=null) {
					right.nextbydate.prevbydate=right;
				} else {
					this.lastbydate=right;
				}
				mod.nextbydate=right;
				right.prevbydate=mod;

				// update offset list on mod,right
				right.nextbyoffset = mod.nextbyoffset;
				if (right.nextbyoffset!=null) {
					right.nextbyoffset.prevbyoffset=right;
				} else {
					this.lastbyoffset=right;
				}
				mod.nextbyoffset=right;
				right.prevbyoffset=mod;
			}
		    // insert newModif after mod (by offset)
			newModif.prevbyoffset=mod;
			newModif.nextbyoffset=mod.nextbyoffset;
		} else {
			newModif.nextbyoffset=this.firstbyoffset;
		}
		//update date list
		newModif.prevbydate=this.lastbydate;
		
		newModif.updateReferences();
	}
	
	
	/**
	 * cette classe est untiliser pour mettre à jour la liste des modifications apres chaque 
	 * nouvelle supression faite dans le document
	 * @param offs
	 * @param length
	 */
	public void removeModif(int offs,int length) {
		Modif mod = this.lastbyoffset;
		if (mod==null) {
			return;
		}
		// find first modification which will be affected by the removal
		while (mod.prevbyoffset!=null && (mod.prevbyoffset.offs + mod.prevbyoffset.length) > offs) {
			mod.offs-=length; // and reduce the offset of each modification (except the one we are interested in)
			mod = mod.prevbyoffset;
		}
		// adujst first modification
		int removablefrommod = mod.length - (offs-mod.offs);
		if (length >= removablefrommod) {
			mod.length = offs-mod.offs;
			length-=removablefrommod; 
			if (mod.length==0) {
				mod.deleteModif();
			}
		} else {
			mod.length-=length;
			length=0;
		}
        mod = mod.nextbyoffset;
		// delete all modifications which no longer exist
		while (mod!=null && length >= mod.length) {
			mod.deleteModif();
			length -= mod.length;
			mod = mod.nextbyoffset;
		}
		
		// adjust last modification
		if (mod!=null && length>0) {
			mod.length-=length;
			mod.offs+=length;
		}
	}
	
	public void print() {
		Modif mod=this.firstbyoffset;
		int count=0;
		while (mod!=null) {
			mod.print();
			count+=mod.length;
			mod=mod.nextbyoffset;
		}
		System.out.println("chars covered:"+count);
	}
	
	public void printByDate() {
		Modif mod=this.firstbydate;
		int count=0;
		Vector v=new Vector();
		while (mod!=null) {
			mod.print();
			count+=mod.length;
			mod=mod.nextbydate;
		}
		System.out.println("chars covered:"+count);
	}
	
	public void check() {
		Modif mod=this.firstbyoffset;
		int pos = 0;
		while (mod!=null) {
			if (mod.offs!=pos) {
				System.err.println("problem at pos " + pos);
				this.print();
				return;
			} else {
				pos+=mod.length;
			}
			mod = mod.nextbyoffset;
		}
	}
	
	public void reset() {
		Modif mod=this.firstbyoffset;
		while (mod!=null) {
			mod.print();
			mod.prevbyoffset=null;
			mod.nextbydate=null;
			mod.prevbydate=null;
			mod=mod.nextbyoffset;
			mod.prevbyoffset.nextbyoffset=null;
		}
		this.firstbyoffset=null;
		this.firstbydate=null;
		this.lastbyoffset=null;
		this.lastbydate=null;
	}


		
	
	public class Modif {
		/**
		 * 
		 * represents a modification to a document. Can't be instantiated more than once
		 *
		 */

		//public static final int MINUTE;
		public long createTime;
		public String owner;
		public int offs;
		public int length;
		public Modif nextbydate = null;
		public Modif prevbydate = null;
		public Modif nextbyoffset = null;
		public Modif prevbyoffset = null;

		Modif(int i, int l, String s){
			owner=s;
			offs=i;
			length=l;
			createTime = System.currentTimeMillis();			
		}
		
		public void print() {
			System.out.println("From "+ this.offs +  " to " + (this.offs+this.length-1) + " by " + this.owner + " at " + this.createTime);			
		}

		public void updateReferences() {
			if (this.prevbyoffset == null) {
				firstbyoffset=this;
			} else {
				this.prevbyoffset.nextbyoffset=this;
			}

			if (this.nextbyoffset == null) {
				lastbyoffset=this;
			} else {
				this.nextbyoffset.prevbyoffset=this;
			}
			
			if (this.prevbydate == null) {
				firstbydate=this;
			} else {
				this.prevbydate.nextbydate=this;
			}

			if (this.nextbydate == null) {
				lastbydate=this;
			} else {
				this.nextbydate.prevbydate=this;
			}
		}

		public void deleteModif() {
			if (this.prevbyoffset == null) {
				firstbyoffset=this.nextbyoffset;
			} else {
				this.prevbyoffset.nextbyoffset=this.nextbyoffset;
			}

			if (this.nextbyoffset == null) {
				lastbyoffset=this.prevbyoffset;
			} else {
				this.nextbyoffset.prevbyoffset=this.prevbyoffset;
			}
			
			if (this.prevbydate == null) {
				firstbydate=this.nextbydate;
			} else {
				this.prevbydate.nextbydate=this.nextbydate;
			}

			if (this.nextbydate == null) {
				lastbydate=this.prevbydate;
			} else {
				this.nextbydate.prevbydate=this.prevbydate;
			}
		}

	}
	
}

