package com.icaller.callscreen.icalldialer;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.view.ViewCompat;


public class CustomLoadProgressDialogView extends Dialog {
    Context context;
    private String message;
    TextView messageTextView;
    RelativeLayout rel;

    public CustomLoadProgressDialogView(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        requestWindowFeature(1);
        setContentView(R.layout.custom_progress_dialog);
        this.rel = (RelativeLayout) findViewById(R.id.layout);
        this.messageTextView = (TextView) findViewById(R.id.messageTextView);
        Context context = this.context;
        if (context.getSharedPreferences(context.getPackageName(), 0).getBoolean("dark_theme", false)) {
            Log.e("TAG", "onCreate:**************** ");
            this.rel.setBackgroundResource(R.drawable.tab_bg_dark);
            this.messageTextView.setTextColor(-1);
            this.context.setTheme(R.style.AppThemeContactContact1);
        } else {
            this.rel.setBackgroundResource(R.drawable.gradiant_bg);
            this.messageTextView.setTextColor(ViewCompat.MEASURED_STATE_MASK);
            this.context.setTheme(R.style.AppThemeContactContact);
            Log.e("TAG", "onCreate:***** ");
        }
        getWindow().setBackgroundDrawable(new ColorDrawable(0));
        setCancelable(false);
    }
}
