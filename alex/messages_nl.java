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
* File: messages_nl.java
* Author:
* Description:
*
* $Id: messages_nl.java,v 1.8 2003/11/24 10:54:31 serpaggi Exp $
 */


package alex;

import java.util.*;

public class messages_nl extends ListResourceBundle {
  public Object[][] getContents() {
    return contents;
  }

  static final Object[][] contents = {
    // LOCALIZE THIS
    {"template_file", "TemplatesUTF8.txt.nl"},
    {"select_template", "1 Kies een zinsopener"},
    {"make_Argument", "2 Maak je argument                                                                    "},
    {"number_Prefix", ""},
    {"number_Postfix", " "},
    {"argue_Label", "Argument:"},
    {"opinion_Label", "Mening:"},
    {"explore_Label", "Onderzoeken:"},
    {"dialogue_Label", "Commentaar:"},
    {"submit_Label", "3 Toevoegen"},
    {"Advice1", "Controleer je argument en de verbanden voordat je toevoegt"},
    {"OKStatus", "Kies een zinsopener uit één van de lijsten"},
    {"invalid_Status", "Niet goed - je moet tekst invullen voordat je op toevoegen klikt"},
    {"QA", "Zou je je standpunt in het debat willen verduidelijken?"},
    {"QB", "Wil je naar een ander onderwerp overgaan?"},
    {"QC", "Zou je meer willen weten over het standpunt van je partner in het debat?"},
    {"QD", "Weet je zeker dat je alle issues onderzocht hebt die te maken hebben met je argument?"},
    {"QE", "Wil je een argument geven voor jouw standpunt in het debat?"},
    {"QF", "Welk van de zaken die te maken hebben met voorgaande argumenten zou je verder willen onderzoeken?"},
    {"QG", "Zou je een nieuw argument voor de discussie willen geven?"}
    // END OF MATERIAL TO LOCALIZE
  };
}