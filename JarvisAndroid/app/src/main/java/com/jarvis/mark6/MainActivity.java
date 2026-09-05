package com.jarvis.mark6;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.speech.tts.TextToSpeech;
import android.speech.tts.Voice;

import java.util.Locale;

public class MainActivity extends Activity {

    private TextToSpeech tts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WebView webView = new WebView(this);

        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setAllowFileAccess(true);

        webView.setWebViewClient(new WebViewClient());

        tts = new TextToSpeech(this, status -> {
            if (status == TextToSpeech.SUCCESS) {
                tts.setLanguage(new Locale("es", "ES"));

                Voice voz = tts.getDefaultVoice();
                if (voz != null) {
                    tts.setVoice(voz);
                }
            }
        });

        webView.addJavascriptInterface(new AndroidTTS(), "AndroidTTS");

        webView.loadUrl("file:///android_asset/index.html");

        setContentView(webView);
    }

    public class AndroidTTS {

        @JavascriptInterface
        public void speak(String texto) {
            if (tts != null) {
                tts.stop();
                tts.setSpeechRate(0.88f);
                tts.setPitch(0.72f);
                tts.speak(texto, TextToSpeech.QUEUE_FLUSH, null, "JARVIS");
            }
        }
    }

    @Override
    protected void onDestroy() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }

        super.onDestroy();
    }
}
