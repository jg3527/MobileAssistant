
package com.example.textpart;

import com.baidu.voicerecognition.android.VoiceRecognitionConfig;
import com.baidu.voicerecognition.android.ui.BaiduASRDigitalDialog;

/**
 * 涓存椂淇濆瓨鍙傛暟淇℃伅锛孌emo婕旂ず浣跨敤銆傚紑鍙戣�涓嶉』鍏虫敞
 * 
 * @author yangliang02
 */
public class Config {

    /** 璇煶鎼滅储妯″紡 */
    public static int VOICE_TYPE = Config.VOICE_TYPE_SEARCH;

    /** 瀵硅瘽妗嗘牱寮�*/
    public static int DIALOG_THEME = BaiduASRDigitalDialog.THEME_BLUE_LIGHTBG;

    /** 璇煶鎼滅储妯″紡 */
    public static final int VOICE_TYPE_SEARCH = 0;

    /** 璇煶杈撳叆妯″紡 */
    public static final int VOICE_TYPE_INPUT = 1;

    /**
     * 褰撳墠璇嗗埆璇█
     */
    public static String CURRENT_LANGUAGE = VoiceRecognitionConfig.LANGUAGE_CHINESE;

    private static int CURRENT_LANGUAGE_INDEX = 0;

    public static String getCurrentLanguage() {
        return CURRENT_LANGUAGE;
    }

    public static int getCurrentLanguageIndex() {
        return CURRENT_LANGUAGE_INDEX;
    }

    public static void setCurrentLanguageIndex(int index) {
        switch (index) {
            case 1:
                CURRENT_LANGUAGE = VoiceRecognitionConfig.LANGUAGE_CANTONESE;
                break;
            case 2:
                CURRENT_LANGUAGE = VoiceRecognitionConfig.LANGUAGE_ENGLISH;
                break;

            default:
                CURRENT_LANGUAGE = VoiceRecognitionConfig.LANGUAGE_CHINESE;
                index = 0;
                break;
        }
        CURRENT_LANGUAGE_INDEX = index;
    }

    /**
     * 鎾斁寮�闊�     */
    public static boolean PLAY_START_SOUND = true;

    /**
     * 鎾斁缁撴潫闊�     */
    public static boolean PLAY_END_SOUND = true;

    /**
     * 鏄剧ず闊抽噺
     */
    public static boolean SHOW_VOL = true;

    /**
     * 鏄惁鍚敤璇箟瑙ｆ瀽
     */
    public static boolean enableNLU = false;

    /**
     * 浣跨敤楹﹀厠椋庡綍闊宠緭鍏ユ簮
     */
    public static boolean USE_DEFAULT_AUDIO_SOURCE = true;
}
