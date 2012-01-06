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
 * File: Rejoueur_pt.java
 * Author:
 * Description:
 *
 * $Id: Rejoueur_pt.java,v 1.8 2004/03/01 10:23:38 jaillon Exp $
 */

package Drew.Locale.Client;

import java.util.*;

public class Rejoueur_pt extends ListResourceBundle {
	public Object[][] getContents() {
		return contents;
	}

	static final Object[][] contents = {
		// LOCALIZE THIS
		{"WindowTitle", "Repetir painel de controlo"},
		{"ButtonApplet","Repetir"},

		{"LabelList", "Lista de participantes"},
		{"LabelEcouter", "Ouvir"},
		{"LabelEtat", "ESTADO DA LIGA��O:"},
		{"LabelMsg", "MENSAGENS DE INFORMA��O:"},
		{"LabelConnecter", "Por favor, ligue-se."},
		{"LabelLigne", "Linha actual"},
		{"LabelSignification", "Significado da presente linha."},
		{"ButtonConnect", "Ligar"},
		{"ButtonDeconnect", "Desligar"},
		{"ButtonQuit", "Sair"},
		{"ButtonPLAY", "CORRER"},
		{"ButtonSTEP", "AVAN�AR"},
		{"ButtonSTOP", "PARAR"},
		{"ButtonRWD", "REBOBINAR"},

		{"panel.filedialog", "Selecionar ficheiro de rastreio"},
		{"panel.check", "Verificar este ficheiro"},
		{"panel.modules", "M�dulos"},
		{"panel.locale", "L�ngua da Interface"},
		{"panel.layout", "Layout do M�dulo"},
		{"panel.all", "Tudo em um"},
		{"panel.horizontal", "Duplicar-Horizontalmente"},
		{"panel.vertical", "Duplicar-Verticalmente"},
		{"panel.separate", "Separar"},
		{"panel.print.scale", "Escala de Impress�o do Gr�fico"},
		{"panel.replay", "Replay"},
		{"panel.rooms", "Salas"},
		{"panel.participants", "Participantes nas salas"},

		{"msg000", ""},
		{"msg001", "a desligar..."},
		{"msg002", "ligado"},
		{"msg003", "por favor, aguarde."},
		{"msg004", "a obter ou a processar informa��o"},
		{"msg005", "Mensagem do servidor: "},
		{"msg006", "{0} junte-se a n�s"},
		{"msg007", "{0} sair"},
		{"msg008", "mensagem do servidor: "},
		{"msg009", "{0} sair da sala {1} para {2}"},
		{"msg010", " para "},
		{"msg011", "{0} pedir lista de participantes"},
		{"msg012", "{0} utilisar m�dulo {1}"},
		{"msg013", "esta mensagem n�o � para esta sala"},
		{"msg014", "nenhuma tradu��o dispon�vel"},
		{"msg015", "desligado"},
		{"msg016", "por favor, ligue-se."},
		{"msg017", "liga��o terminada pelo servidor."}
		// END OF MATERIAL TO LOCALIZE
	};
}
