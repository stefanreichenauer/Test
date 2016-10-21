/*
 * Catroid: An on-device visual programming system for Android devices
 * Copyright (C) 2010-2015 The Catrobat Team
 * (<http://developer.catrobat.org/credits>)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * An additional term exception under section 7 of the GNU Affero
 * General Public License, version 3, is available at
 * http://developer.catrobat.org/license_additional_term
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.catrobat.catroid.test.cucumber;

import android.app.Activity;
import android.hardware.Sensor;
import android.test.AndroidTestCase;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import com.robotium.solo.Solo;

import org.catrobat.catroid.ProjectManager;
import org.catrobat.catroid.R;
import org.catrobat.catroid.formulaeditor.SensorHandler;
import org.catrobat.catroid.formulaeditor.Sensors;
import org.catrobat.catroid.formulaeditor.UserVariable;
import org.catrobat.catroid.stage.StageActivity;
import org.catrobat.catroid.ui.MainMenuActivity;
import org.catrobat.catroid.ui.MyProjectsActivity;
import org.catrobat.catroid.ui.ProjectActivity;
import org.catrobat.catroid.ui.WebViewActivity;
import org.catrobat.catroid.uitest.util.UiTestUtils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

// CHECKSTYLE DISABLE MethodNameCheck FOR 1000 LINES
public class SensorTestSteps extends AndroidTestCase {
	private String projectName = UiTestUtils.DEFAULT_TEST_PROJECT_NAME;
	private final Object programStartWaitLock = new Object();
	private boolean programHasStarted = false;
	private String testingproject = UiTestUtils.PROJECTNAME1;

    @Given("^Robotarm should be in default position$")
    public void robotarm_should_be_in_default_position() throws Throwable {
        try{
            URL myURL = new URL("http://192.168.0.12:5000/do/set_to_default");
            URLConnection myURLConnection = myURL.openConnection();

            HttpURLConnection httpConn = (HttpURLConnection) myURLConnection;
            httpConn.connect();

            if (httpConn.getResponseCode() != HttpURLConnection.HTTP_OK) {
                fail("URL responded with error code " + httpConn.getResponseCode());
            }
        }
        catch(MalformedURLException e){
            fail("URL malformed. " + e.getMessage());
        }
        catch(IOException e){
            fail("Connection failed. " + e.getMessage());
        }
    }

    @Then("^X acceleration value should be zero$")
    public void x_acceleration_value_should_be_zero() throws Throwable {
// Write code here that turns the phrase above into concrete actions
        SensorHandler.startSensorListener(getContext());
        assertEquals("Devise is moving", 0d, Math.abs(SensorHandler.getSensorValue(Sensors.X_ACCELERATION)), 2d);
    }

    @Then("^Y acceleration value should be zero$")
    public void y_acceleration_value_should_be_zero() throws Throwable {
// Write code here that turns the phrase above into concrete actions
        SensorHandler.startSensorListener(getContext());
        assertEquals("Devise is moving", 0d, Math.abs(SensorHandler.getSensorValue(Sensors.Y_ACCELERATION)), 2d);
    }

    @Then("^Z acceleration value should be zero$")
    public void z_acceleration_value_should_be_zero() throws Throwable {
// Write code here that turns the phrase above into concrete actions
        SensorHandler.startSensorListener(getContext());
        assertEquals("Devise is moving", 0d, Math.abs(SensorHandler.getSensorValue(Sensors.Z_ACCELERATION)), 2d);
    }

    @Then("^X inclination value should be zero$")
    public void x_inclination_value_should_be_zero() throws Throwable {
// Write code here that turns the phrase above into concrete actions
        SensorHandler.startSensorListener(getContext());
        assertEquals("Devise is moving", 0d, Math.abs(SensorHandler.getSensorValue(Sensors.X_INCLINATION)), 4d);
    }

    @Then("^Y inclination value should be zero$")
    public void y_inclination_value_should_be_zero() throws Throwable {
// Write code here that turns the phrase above into concrete actions
        SensorHandler.startSensorListener(getContext());
        assertEquals("Devise is moving", 0d, Math.abs(SensorHandler.getSensorValue(Sensors.Y_INCLINATION)), 4d);
    }

    @Then("^compass direction value should not change$")
    public void compass_direction_value_should_not_change() throws Throwable {
// Write code here that turns the phrase above into concrete actions
        Solo solo = (Solo) Cucumber.get(Cucumber.KEY_SOLO);
        SensorHandler.startSensorListener(getContext());
        Double compass =  Math.abs(SensorHandler.getSensorValue(Sensors.COMPASS_DIRECTION));
        solo.sleep(1000);
        assertEquals("Devise is moving", compass, Math.abs(SensorHandler.getSensorValue(Sensors.COMPASS_DIRECTION)), 4d);
    }

}