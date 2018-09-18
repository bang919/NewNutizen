package com.nutizen.nu.receiver;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothHeadset;
import android.bluetooth.BluetoothProfile;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;

/**
 * Created by Administrator on 2017/12/27.
 */

public class HeadsetReceiver extends BroadcastReceiver {

    private int currVolume = 0;
    private AudioManager audioManager;

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        if (audioManager == null) {

            audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
//            audioManager.setMode(AudioManager.ROUTE_SPEAKER);
            audioManager.setMode(AudioManager.MODE_IN_CALL);
            audioManager.setStreamVolume(AudioManager.STREAM_VOICE_CALL,
                    audioManager.getStreamVolume(AudioManager.STREAM_VOICE_CALL),
                    AudioManager.STREAM_VOICE_CALL);
        }

        currVolume = audioManager.getStreamVolume(AudioManager.STREAM_VOICE_CALL);
        if (BluetoothHeadset.ACTION_CONNECTION_STATE_CHANGED.equals(action)) {

            BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();

            audioManager.setSpeakerphoneOn(false);

            if (BluetoothProfile.STATE_DISCONNECTED == adapter.getProfileConnectionState(BluetoothProfile.HEADSET)) {
                //Bluetooth headset is now disconnected
                audioManager.setSpeakerphoneOn(true);
            }
        } else if ("android.intent.action.HEADSET_PLUG".equals(action)) {

            if (intent.hasExtra("state")) {

                audioManager.setSpeakerphoneOn(false);

                if (intent.getIntExtra("state", 0) == 0) {
                    audioManager.setSpeakerphoneOn(true);
                }
            }
        }
    }
}