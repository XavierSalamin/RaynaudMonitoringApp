package fr.hes.raynaudmonitoring;

import android.content.Context;
import android.view.KeyEvent;
import android.widget.EditText;

public class CustomEditText extends EditText {
    public CustomEditText(Context context) {
        super(context);
    }
    private KeyImeChange keyImeChangeListener;

    public void setKeyImeChangeListener(KeyImeChange listener)
    {
        keyImeChangeListener = listener;
    }

    public interface KeyImeChange
    {
        public void onKeyIme(int keyCode, KeyEvent event);
    }

    @Override
    public boolean onKeyPreIme(int keyCode, KeyEvent event)
    {
        if (keyImeChangeListener != null)
        {
            keyImeChangeListener.onKeyIme(keyCode, event);
        }
        return false;
    }
}
