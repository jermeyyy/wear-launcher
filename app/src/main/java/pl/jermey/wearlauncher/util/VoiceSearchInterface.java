package pl.jermey.wearlauncher.util;

import android.content.Intent;
import android.speech.SpeechRecognizer;

/**
 * Created by Jermey on 09.04.2017.
 */

public interface VoiceSearchInterface {
    void onSearch(String query);
    SpeechRecognizer getSpeechRecognizer();
    Intent getSpeechIntent();
}
