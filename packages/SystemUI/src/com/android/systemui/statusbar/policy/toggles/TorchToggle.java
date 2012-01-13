/*
 * Copyright (C) 2010 The Android Open Source Project
 * Copyright 2011 Colin McDonough
 * 
 * Modified for AOKP by Mike Wilson (Zaphod-Beeblebrox)
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.systemui.statusbar.policy.toggles;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import com.android.systemui.R;

/**
 * TODO: Fix the WakeLock
 */
public class TorchToggle extends Toggle {

    private static final String TAG = "TorchToggle";

    private boolean mIsTorchOn;
    private Context mContext;
    private Torch torch;

    public TorchToggle(Context context) {
        super(context);
        setLabel(R.string.toggle_torch);
        setIcon(R.drawable.toggle_torch);
        mContext = context;

        IntentFilter intentFilter = new IntentFilter(Torch.ACTION_UPDATE);
        mContext.registerReceiver(new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                updateState();
            }
        }, intentFilter);

        updateState();
    }

    @Override
    protected void updateInternalToggleState() {
        torch = Torch.getTorch();
        if (torch != null)
            mToggle.setChecked(torch.isOn());
        else
            mToggle.setChecked(false);
    }

    @Override
    protected void onCheckChanged(boolean isChecked) {
        if (isChecked) {
            Log.d(TAG, "Instantiating Torch");
            if (Torch.getTorch() == null) {
                Log.d(TAG, "Starting Intent");
                Intent intent = new Intent(mContext, Torch.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            }
            torch = Torch.getTorch();
        } else {
            Log.d(TAG, "Destroying Torch");
            torch = Torch.getTorch();
            if (torch != null) {
                torch.finish();
            }
        }
    }

}