package com.android.systemui.statusbar.powerwidget;

import com.android.systemui.R;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;

import java.util.ArrayList;
import java.util.List;

public class FlashlightButton extends PowerButton {

    private static final List<Uri> OBSERVED_URIS = new ArrayList<Uri>();
    static {
        OBSERVED_URIS.add(Settings.System.getUriFor(Settings.System.TORCH_STATE));
    }

    public FlashlightButton() { mType = BUTTON_FLASHLIGHT; }

    public static native String openFlash() ;
    public static native String closeFlash() ;
    public static native String setFlashOn() ;
    public static native String setFlashOff() ;

    // Load libflash once on app startup.
    static {
    	System.loadLibrary("jni_flashwidget");	
    }

    @Override
    protected void updateState() {
        boolean enabled = Settings.System.getInt(mView.getContext().getContentResolver(), Settings.System.TORCH_STATE, 0) == 1;
        if(enabled) {
            mIcon = R.drawable.stat_flashlight_on;
            mState = STATE_ENABLED;
        } else {
            mIcon = R.drawable.stat_flashlight_off;
            mState = STATE_DISABLED;
        }
    }

    @Override
    protected void toggleState() {
        Context context = mView.getContext();
	boolean enabled = Settings.System.getInt(context.getContentResolver(), Settings.System.TORCH_STATE, 0) == 1;
	if (!enabled) {
		openFlash();
		setFlashOn();
		Settings.System.putInt(context.getContentResolver(), Settings.System.TORCH_STATE, 1);
	} else {
		setFlashOff();
		closeFlash();
		Settings.System.putInt(context.getContentResolver(), Settings.System.TORCH_STATE, 0);
	}
    }

    @Override
    protected List<Uri> getObservedUris() {
        return OBSERVED_URIS;
    }
}
