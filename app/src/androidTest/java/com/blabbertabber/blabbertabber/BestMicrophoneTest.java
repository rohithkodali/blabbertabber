package com.blabbertabber.blabbertabber;

import android.media.MediaRecorder;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by cunnie on 10/6/15.
 * <p/>
 * Test that devices are using the correct microphones.
 */

@RunWith(AndroidJUnit4.class)
public class BestMicrophoneTest {
    private static final String TAG = "BestMicrophoneTest";
    private BuildMODEL mBuildModel = mock(BuildMODEL.class);

    @Test
    public void testDefaultDeviceHasMic() {
        when(mBuildModel.model()).thenReturn("fake model");
        assertEquals("Default should use MediaRecorder.AudioSource.DEFAULT", MediaRecorder.AudioSource.DEFAULT, BestMicrophone.getBestMicrophone(mBuildModel));
    }

    @Test
    public void testNexus5HasMic() {
        Log.i(TAG, "mBuildModel == " + mBuildModel);
        when(mBuildModel.model()).thenReturn("Nexus 5");
        assertEquals("Nexus 5 should use MediaRecorder.AudioSource.MIC", MediaRecorder.AudioSource.MIC, BestMicrophone.getBestMicrophone(mBuildModel));
    }

    @Test
    public void testNexus6HasVoiceRecognition() {
        when(mBuildModel.model()).thenReturn("Nexus 6");
        assertEquals("Nexus 6 should use MediaRecorder.AudioSource.VOICE_RECOGNITION", MediaRecorder.AudioSource.VOICE_RECOGNITION, BestMicrophone.getBestMicrophone(mBuildModel));
    }
}

