package org.catrobat.catroid.test.cucumber;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

public class RaspberryPiConnection {

    private static final String TAG = RaspberryPiConnection.class.getSimpleName();

    public void robotarmSetToDefault()
    {
        try{

            URL url = new URL("http://192.168.0.12/do/set_to_default");
            url.openStream();
  }
        catch(Exception e){

            Log.e(TAG, "URL not reachable");
        }
    }

}
