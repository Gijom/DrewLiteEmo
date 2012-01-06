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
  //        {"TopicString", "0  A genetikailag módosított élelmiszerek veszélyesek a környezetre és az egészségre " },
          {"select_template", "Válassz sablont!"},
          {"advisor_thinking", "Advisor is thinking 0 default"},
          {"make_Argument","Érvelj!                                                                                  "},
          //{"number_Prefix", ""},
          {"number_Postfix", " "},
          {"argue_Label", "Érv:"},
          {"explore_Label", "Kifejtés:"},
          {"opinion_Label", "Vélemény:"},
          {"dialogue_Label", "Kommentár:"},
          {"submit_Label", "3 Küldd el!"},
          {"Advice1", "Ellen\u0151rizd, amit írtál, és a kapcsolatot is!"},
          {"advice2", "Advice 2 "},
          {"advice3", "Advice 3"},
          {"OKstatus", "Válassz egy mintát az egyik listából!"},
          {"invalid_Status", "Érvénytelen. Írnod kell, mielott elküldöd."},
          {"QA", "Tisztáznád, mi az álláspontod a vitában?"},
          {"QB","Más szempontból is átgondolhatnád a témát!"},
          {"QC", "Nem akarsz többet megtudni vitapartnered álláspontjáról?"},
          {"QD", "Biztosan kifejtettél minden kapcsolódó témát?"},
          {"QE", "Jó lenne, ha felhoznál  valamilyen érvet!"},
          {"QF","Nem akarsz visszatérni egy korábbi érvhez, hogy b\u0151vebben kifejtsd?"},
          {"QG","Van még új érved?"},
          {"QH","Van még új érved?"}
          // END OF MATERIAL TO LOCALIZE
        };
}

