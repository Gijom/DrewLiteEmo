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


public class EmotionAwareness extends ListResourceBundle{

	public Object[][] getContents() {
		return contents;
	}

	static final Object[][] contents = {
		// LOCALIZE THIS
		{"WindowTitle", "Emotion Awareness Tool"},
                {"InputMessage", "Do not forget to input the emotion you are feeling, please do it now !"},
                {"NegButtonsTitle", "Negative emotions"},
                {"PosButtonsTitle", "Positive emotions"},
                {"SelfEmotionsTitle", "Your emotions"},
                {"OthersEmotionsTitle", "Others' emotions"},
                {"RefreshButtonTitle", "Clear emotion"},

		// END OF MATERIAL TO LOCALIZE
	};
}
