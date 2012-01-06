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


package alex;

import java.util.*;

public class messages extends ListResourceBundle {
        public Object[][] getContents() {
                return contents;
        }

        static final Object[][] contents = {
           // LOCALIZE THIS
          {"template_file", "TemplatesUTF8.txt.en"},
          {"select_template", "1  Select a Template"},
          {"make_Argument","2  Make Your Argument                                                                                                           "},
          {"number_Prefix", ""},
          {"number_Postfix", ""},
          {"argue_Label", "Argument:"},
          {"opinion_Label", "Opinion:"},
          {"explore_Label", "Explore:"},
          {"dialogue_Label", "Comment:"},
          {"submit_Label", "3 Submit"},
          {"Advice1", "Check your argument including the links before submitting"},
          {"OKStatus", "Choose a template from one of the lists"},
          {"invalid_Status", "Invalid - you must write some text before you submit"},
          {"QA", "Would you like to clarify your position in the debate and provide an argument for it?"},
          {"QB", "Would you like to move to another topic? "},
          {"QC", "Would you like to find out more about your partner's position in the debate?"},
          {"QD", "Are you sure that you have explored all the issues related to your argument?"},
          {"QE", "Would you like to provide an argument for your position in the debate?"},
          {"QF", "Which of the issues related to the preceding arguments would you like to explore and deepen?"},
          {"QG", "Would you like to provide a new argument open for discussion?"},
          {"QH", "QH"}

          // END OF MATERIAL TO LOCALIZE
        };
}

