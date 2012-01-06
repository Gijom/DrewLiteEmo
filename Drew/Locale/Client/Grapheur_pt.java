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
 * File: Grapheur_pt.java
 * Author:
 * Description:
 *
 * $Id: Grapheur_pt.java,v 1.9 2004/03/01 10:15:52 jaillon Exp $
 */

package Drew.Locale.Client;

import java.util.*;

public class Grapheur_pt extends ListResourceBundle {
	public Object[][] getContents() {
		return contents;
	}

	static final Object[][] contents = {
		// LOCALIZE THIS
		{"WindowTitle", "Espaço Gráfico para Argumentação"},
		{"LabelList", "Participantes ligados:"},
		{"getActive", "Participar"},
		{"getPassive", "Somente Ouvir"},
		{"ButtonNewBox", "Nova Caixa"},
		{"ButtonNewRel", "Nova Relação"},
		{"ButtonDelete", "Apagar"},
		{"ButtonMore", "Mais informação..."},
		{"LabelPro", "A favor"},
		{"LabelContra", "Contra"},
		{"ButtonQuit", "Sair"},
		{"ButtonPrint", "Copia"},
		{"ButtonAutoLayout", "Automatic Layout"},
		//EDITUSER DIALOG 
		{"EUWindowTitle", "Editar Participantes Presentes"},
		{"ButtonNew","Novo"},
		{"EULabelList", "Participantes disponíveis:"},
		{"LabelEngaged", "Ocupado"},
		{"EUButtonEdit", "Editar"},
		{"ButtonClose", "Fechar"},
		{"ButtonSave", "Guardar"},
		{"ButtonCancel", "Cancelar"},
		{"ButtonEditUsers", "Editar lista de utilizadores"},
		//MORE INFO DIALOG
		{"MILabelBox","Conteúdo"},
		{"MILabelRelation","Tipo de Relação:"},
		{"MILabelComment","Comentário"},
		{"MILabelPro","Participantes a Favor"},
		{"MILabelContra","Participantes Contra"},
		{"LabelRelation0","Indefinido(a) [?]"},
		{"LabelRelation1","Apoiar [+++]"},
		{"LabelRelation2","Apoiar [++]"},
		{"LabelRelation3","Apoiar [+]"},
		{"LabelRelation4","Indifferente [=]"},
		{"LabelRelation5","Atacar [-]"},
		{"LabelRelation6","Atacar [--]"},
		{"LabelRelation7","Atacar [---]"},
		// ARGUMENT
		{"CommentPrompt","Pode dizer algo mais acerca disto?"},
		{"NewArgument", "Nova Caixa"},
		// PRINT
		{"print.argument.name", "Nome: {0}" },
		{"print.argument.comment", "Comentário : {0}" },
		{"print.boite.typebox", "Tipo: Caixa" },
		{"print.fleche.type", "Tipo: Relação entre {0} e {1}." },
		{"print.fleche.nothing", "nada" },
		// END OF MATERIAL TO LOCALIZE
	};
}
