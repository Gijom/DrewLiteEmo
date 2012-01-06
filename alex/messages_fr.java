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
* File: messages_fr.java
* Author:
* Description:
*
* $Id: messages_fr.java,v 1.12 2003/11/24 10:54:31 serpaggi Exp $
 */


package alex;

import java.util.*;

public class messages_fr extends ListResourceBundle {
  public Object[][] getContents() {
    return contents;
  }

  static final Object[][] contents = {
    // LOCALIZE THIS
    {"template_file", "TemplatesUTF8.txt.fr"},
    {"select_template", "1 Selectionner une phrase-type"},
    {"make_Argument", "2 Construire votre argument                                                                "},
    {"number_Prefix", ""},
    {"number_Postfix", ""},
    {"argue_Label", "Argumenter"},
    {"opinion_Label", "Opinion"},
    {"explore_Label", "Explorer"},
    {"dialogue_Label", "Commenter"},
    {"submit_Label", "3 Soumettre"},
    {"Advice1", "Vérifier votre argument avec les liens avant de le soumettre"},
    {"OKStatus", "Choisir l'une des phrases-type de la liste "},
    {"invalid_Status", "Attention - vous devez écrire un texte avant de le soumettre"},
    {"QA", "Souhaitez-vous clarifier votre position dans le débat ?"},
    {"QB", "Souhaitez-vous changer de sujet de discussion ?"},
    {"QC", "Souhaitez-vous en savoir plus sur le point de vue de votre partenaire dans le débat ?"},
    {"QD", "Êtes-vous sûr que vous avez exploré tous les points de vue avec votre argument ?"},
    {"QE", "Souhaitez-vous proposer un argument en faveur de votre point de vue dans le débat ?. "},
    {"QF", "Quels arguments ou questions déjà évoqués voulez-vous approfondir ? "},
    {"QG", "Souhaitez-vous proposer un nouvel argument à discuter ?"}
    // END OF MATERIAL TO LOCALIZE
  };
}


