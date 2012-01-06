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
 * File: messages_hu.java
 * Author:
 * Description:
 *
 * $Id: messages_hu.java,v 1.11 2003/11/24 10:54:31 serpaggi Exp $
 */

package alex;

import java.util.*;

public class messages_hu extends ListResourceBundle {
        public Object[][] getContents() {
                return contents;
        }

        static final Object[][] contents = {
                // LOCALIZE THIS
          {"template_file", "TemplatesUTF8.txt.hu"},
  //        {"TopicString", "0  A genetikailag m�dos�tott �lelmiszerek vesz�lyesek a k�rnyezetre �s az eg�szs�gre " },
          {"select_template", "V�lassz sablont!"},
          {"advisor_thinking", "Advisor is thinking 0 default"},
          {"make_Argument","�rvelj!                                                                                  "},
          //{"number_Prefix", ""},
          {"number_Postfix", " "},
          {"argue_Label", "�rv:"},
          {"explore_Label", "Kifejt�s:"},
          {"opinion_Label", "V�lem�ny:"},
          {"dialogue_Label", "Komment�r:"},
          {"submit_Label", "3 K�ldd el!"},
          {"Advice1", "Ellen\u0151rizd, amit �rt�l, �s a kapcsolatot is!"},
          {"advice2", "Advice 2 "},
          {"advice3", "Advice 3"},
          {"OKstatus", "V�lassz egy mint�t az egyik list�b�l!"},
          {"invalid_Status", "�rv�nytelen. �rnod kell, mielott elk�ld�d."},
          {"QA", "Tiszt�zn�d, mi az �ll�spontod a vit�ban?"},
          {"QB","M�s szempontb�l is �tgondolhatn�d a t�m�t!"},
          {"QC", "Nem akarsz t�bbet megtudni vitapartnered �ll�spontj�r�l?"},
          {"QD", "Biztosan kifejtett�l minden kapcsol�d� t�m�t?"},
          {"QE", "J� lenne, ha felhozn�l  valamilyen �rvet!"},
          {"QF","Nem akarsz visszat�rni egy kor�bbi �rvhez, hogy b\u0151vebben kifejtsd?"},
          {"QG","Van m�g �j �rved?"},
          {"QH","Van m�g �j �rved?"}
          // END OF MATERIAL TO LOCALIZE
        };
}

