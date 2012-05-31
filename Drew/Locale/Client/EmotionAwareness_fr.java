/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Drew.Locale.Client;

/**
 *
 * @author chanel
 */

import java.util.*;


public class EmotionAwareness_fr extends ListResourceBundle{

	public Object[][] getContents() {
		return contents;
	}

	static final Object[][] contents = {
		// LOCALIZE THIS
		{"WindowTitle", "Outil d'awarness \u00e9motionel"},
                {"InputMessage", "N'oubliez d'entrer l'\u00e9motion que vous ressentez, faites le maintenant !"},
                {"NegButtonsTitle", "Emotions n\u00e9gatives"},
                {"PosButtonsTitle", "Emotions positives"},
                {"SelfEmotionsTitle", "Tes \u00e9motions"},
                {"OthersEmotionsTitle", "Emotions des autres"},
                {"RefreshButtonTitle", "Pas d'\u00e9motion"},
 		// END OF MATERIAL TO LOCALIZE
	};
}
