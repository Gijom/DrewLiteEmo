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
* File: messages_pt.java
* Author:
* Description:
*
* $Id: messages_pt.java,v 1.6 2003/11/24 10:54:31 serpaggi Exp $
 */


package alex;

import java.util.*;

public class messages_pt extends ListResourceBundle {
  public Object[][] getContents() {
    return contents;
  }

  static final Object[][] contents = {
    // LOCALIZE THIS

    {"template_file", "TemplatesUTF8.txt.pt"},
    {"select_template", "1 Seleccione um template"},
    {"make_Argument", "2 Construa o Seu Argumento                                                                 "},
    {"number_Prefix", ""},
    {"number_Postfix", " "},
    {"argue_Label", "Argumento:"},
    {"opinion_Label", "Opinião:"},
    {"explore_Label", "Explorar:"},
    {"dialogue_Label", "Comentar:"},
    {"submit_Label", "3 Submeter"},
    {"Advice1", "Verifique o seu argumento incluindo as ligações antes de o submeter"},
    {"OKStatus", "Escolha um template de uma das listas"},
    {"invalid_Status", "Inválido - deve escrever algum texto antes de submeter"},
    {"QA", "Gostaria de clarificar a sua posição no debate?"},
    {"QB", "Gostaria de passar para outro tópico?"},
    {"QC", "Gostaria de saber mais acerca da posição do seu par no debate?"},
    {"QD", "Tem a certeza que explorou todos os aspectos relacionados com o seu argumento?"},
    {"QE", "Gostaria de apresentar um argumento a favor da sua posição no debate?"},
    {"QF", "Qual dos aspectos relacionados com o argumento anterior gostaria de explorar e aprofundar?"},
    {"QG", "Gostaria de apresentar um novo argumento para discussão?"}

    // END OF MATERIAL TO LOCALIZE
  };
}