package net.quduo.pixel.interfaces.android.common;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MobileTextWatcher implements TextWatcher {

    private EditText mEditText;
    private TextView mCleanIcon;

    private int beforeLen = 0;
    private int afterLen = 0;

    public MobileTextWatcher(EditText editText, TextView cleanIcon) {
        this.mEditText = editText;
        this.mCleanIcon = cleanIcon;
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

        if(mEditText.getText().length() > 0) {
            mCleanIcon.setVisibility(View.VISIBLE);
        } else {
            mCleanIcon.setVisibility(View.GONE);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        beforeLen = s.length();
    }

    @Override
    public void afterTextChanged(Editable s) {
        String value = mEditText.getText().toString();
        afterLen = value.length();

        if (afterLen > beforeLen) {
            if (value.length() == 4 || value.length() == 9) {
                mEditText.setText(new StringBuffer(value).insert(value.length() - 1, " ").toString());
                mEditText.setSelection(mEditText.getText().length());
            }
        } else {
            if (value.startsWith(" ")) {
                mEditText.setText(new StringBuffer(value).delete(afterLen - 1, afterLen).toString());
                mEditText.setSelection(mEditText.getText().length());
            }
        }

    }
}
