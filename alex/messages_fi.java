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
 * File: messages_fi.java
 * Author:
 * Description:
 *
 * $Id: messages_fi.java,v 1.11 2003/11/24 10:54:31 serpaggi Exp $
 */


package alex;

import java.util.*;

public class messages_fi extends ListResourceBundle {
        public Object[][] getContents() {
                return contents;
        }

        static final Object[][] contents = {
           // LOCALIZE THIS
          {"template_file", "TemplatesUTF8.txt.fi"},
          {"select_template", "1 Valitse yksi vaihtoehto"},
          {"make_Argument", "2 Laadi puheenvuorosi                                                                   "},
          {"number_Prefix", ""},
          {"number_Postfix", " "},
          {"argue_Label", "Argumentit:"},
          {"opinion_Label", "Mielipiteet:"},
          {"explore_Label", "Tarkennukset:"},
          {"dialogue_Label", "Kommentit:"},
          {"submit_Label", "3 Hyv‰ksy"},
          {"Advice1", "Tarkista v‰itteiden numerot ja tekstisi ennen hyv‰ksymist‰"},
          {"OKStatus", "Valitse sopiva vaihtoehto listasta"},
          {"invalid_Status", "Ei kelpaa - sinun pit‰‰ kirjoittaa jotakin teksti‰ ennen hyv‰ksymist‰"},
          {"QA", "Haluatko selvent‰‰ mielipidett‰si?"},
          {"QB", "Haluatko vaihtaa keskustelun aihetta?"},
          {"QC", "Haluatko, ett‰ keskustelukumppanisi selvent‰‰ mielipidett‰‰n?"},
          {"QD", "Oletko varma, ett‰ olet ottanut kaikki asiat huomioon? "},
          {"QE", "Haluatko perustella mielipiteesi keskustelussa?"},
          {"QF", "Mit‰ aiemmin esitettyj‰ perusteluita haluaisit tarkentaa?"},
          {"QG", "Haluatko tuoda keskusteluun uuden n‰kˆkulman?"}

          // END OF MATERIAL TO LOCALIZE
        };
}

