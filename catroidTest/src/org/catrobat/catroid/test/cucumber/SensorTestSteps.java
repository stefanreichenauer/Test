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

import android.test.AndroidTestCase;
import android.util.Log;

import com.robotium.solo.Solo;

import org.catrobat.catroid.formulaeditor.SensorHandler;
import org.catrobat.catroid.formulaeditor.Sensors;
import org.catrobat.catroid.uitest.util.UiTestUtils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

// CHECKSTYLE DISABLE MethodNameCheck FOR 1000 LINES
public class SensorTestSteps extends AndroidTestCase {
	private String projectName = UiTestUtils.DEFAULT_TEST_PROJECT_NAME;
	private final Object programStartWaitLock = new Object();
	private boolean programHasStarted = false;
	private String testingproject = UiTestUtils.PROJECTNAME1;
    private String robotArmURL = "http://192.168.1.195:5000/";

    private Double delta = 20d;

    private static final String TAG = SensorTestSteps.class.getSimpleName();

    Double xAcceleration = 0d;
    Double yAcceleration = 0d;
    Double zAcceleration = 0d;
    Double xInclination = 0d;
    Double yInclination = 0d;
    Double compassDirection = 0d;


    @Given("^Robotarm should be in default position$")
    public void robotarm_should_be_in_default_position() throws Throwable {

        sendCommandToRobotarm("do/set_to_default");
    }

    @Given("^I check the sensor values$")
    public void i_check_the_sensor_values() throws Throwable {
// Write code here that turns the phrase above into concrete actions

        SensorHandler.startSensorListener(getContext());

        xAcceleration = SensorHandler.getSensorValue(Sensors.X_ACCELERATION);
        yAcceleration = SensorHandler.getSensorValue(Sensors.Y_ACCELERATION);
        zAcceleration = SensorHandler.getSensorValue(Sensors.Z_ACCELERATION);
        xInclination = SensorHandler.getSensorValue(Sensors.X_INCLINATION);
        yInclination = SensorHandler.getSensorValue(Sensors.Y_INCLINATION);
        compassDirection = SensorHandler.getSensorValue(Sensors.COMPASS_DIRECTION);

        Log.e(TAG + " check", "xAcceleration: " + xAcceleration);
        Log.e(TAG + " check", "yAcceleration: " + yAcceleration);
        Log.e(TAG + " check", "zAcceleration: " + zAcceleration);
        Log.e(TAG + " check", "xInclination: " + xInclination);
        Log.e(TAG + " check", "yInclination: " + yInclination);
        Log.e(TAG + " check", "compassDirection: " + compassDirection);
    }

    @When("^The Robotarm didn't move$")
    public void the_Robotarm_didn_t_move() throws Throwable {
// Write code here that turns the phrase above into concrete actions

    }

    @When("^DUT didn't move$")
    public void dut_didn_t_move() throws Throwable {
// Write code here that turns the phrase above into concrete actions

    }

    @When("^The Robotarm turns in '(\\w+ \\w+)' axis by (-?\\d+) degree$")
    public void the_Robotarm_turns_in_X_axis_by_degree(String axis, int degrees) throws Throwable {
// Write code here that turns the phrase above into concrete actions

        switch (axis){
            case "X inclination":
                sendCommandToRobotarm("turn/X?angle=" + -degrees);
                break;
            case "Y inclination":
                sendCommandToRobotarm("turn/Y?angle=" + degrees);
                break;
            case "compass direction":
                sendCommandToRobotarm("turn/Z?angle=" + degrees);
        }
    }


    @When("^The Robotarm prepares the test for the '(\\w+)' accelerometer$")
    public void the_Robotarm_prepares_the_test_for_the_X_accelerometer$(String axis) throws Throwable {
// Write code here that turns the phrase above into concrete actions

        switch (axis){
            case "X":
                sendCommandToRobotarm("turn/Y?angle=90");
                sendCommandToRobotarm("turn/Z?angle=-90");
                break;
            case "Y":
                sendCommandToRobotarm("turn/X?angle=90");
                sendCommandToRobotarm("turn/Z?angle=-90");
                break;
            case "Z":
                sendCommandToRobotarm("servo/shoulder?dutycycle=21");
                sendCommandToRobotarm("turn/Y?angle=-90");
        }
    }

    @Then("^The '(\\w+)' accelerometer test values should be checked$")
    public void the_X_accelerometer_test_values_should_be_checked$(String axis) throws Throwable {
// Write code here that turns the phrase above into concrete actions

        switch (axis){
            case "X":
                sendAsynchronousCommandToRobotarm("turn/Z?angle=90", 'X');
                break;
            case "Y":
                sendAsynchronousCommandToRobotarm("turn/Z?angle=90", 'Y');
                break;
            case "Z":
                sendAsynchronousCommandToRobotarm("servo/shoulder?dutycycle=17", 'Z');
        }
    }


    @Then("^'(\\w+ \\w+)' should not be changed$")
    public void sensor_value_should_not_be_changed(String sensorValue) throws Throwable {
// Write code here that turns the phrase above into concrete actions

        Double value = 0d;

        switch(sensorValue){
            case "X acceleration":
                Log.e(TAG + " change", "xAcceleration: " + xAcceleration);
                value = SensorHandler.getSensorValue(Sensors.X_ACCELERATION);
                assertEquals("Devise moved", xAcceleration, value, 4d);
                break;
            case "Y acceleration":
                Log.e(TAG + " change", "yAcceleration: " + yAcceleration);
                value = SensorHandler.getSensorValue(Sensors.Y_ACCELERATION);
                assertEquals("Devise moved", yAcceleration, value, 4d);
                break;
            case "Z acceleration":
                Log.e(TAG + " change", "zAcceleration: " + zAcceleration);
                value = SensorHandler.getSensorValue(Sensors.Z_ACCELERATION);
                assertEquals("Devise moved", zAcceleration, value, 4d);
                break;
            case "X inclination":
                Log.e(TAG + " change", "xInclination: " + xInclination);
                value = SensorHandler.getSensorValue(Sensors.X_INCLINATION);
                assertEquals("Devise moved", xInclination, value, 4d);
                break;
            case "Y inclination":
                Log.e(TAG + " change", "yInclination: " + yInclination);
                value = SensorHandler.getSensorValue(Sensors.Y_INCLINATION);
                assertEquals("Devise moved", yInclination, value, 4d);
                break;
            case "compass direction":
                Log.e(TAG + " change", "compassDirection: " + compassDirection);
                value = SensorHandler.getSensorValue(Sensors.COMPASS_DIRECTION);
                assertEquals("Devise moved", compassDirection, value, 4d);
                break;
            default:
                fail("Wrong sensor value");
        }
    }


    @Then("^'(\\w+ \\w+)' should be changed by (-?\\d+) degree$")
    public void axis_inclination_should_be_changed_by_degree(String axis, int degrees) throws Throwable {
// Write code here that turns the phrase above into concrete actions

        Double inclination = 0d;
        Solo solo = (Solo) Cucumber.get(Cucumber.KEY_SOLO);
        solo.sleep(1000);
        switch(axis){
            case "X inclination":
                inclination = SensorHandler.getSensorValue(Sensors.X_INCLINATION);
                Log.e(TAG + " change", "xInclination: " + inclination);
                assertEquals("Devise did not move", degrees, inclination, delta);
                break;
            case "Y inclination":
                inclination = SensorHandler.getSensorValue(Sensors.Y_INCLINATION);
                Log.e(TAG + " change", "yInclination: " + inclination);
                assertEquals("Devise did not move", degrees, inclination, delta);
                break;
            case "compass direction":
                Double compass = compassDirection + degrees;
                if((degrees > 0) && (compass > 180)){
                    compass = -360 + compass;
                }
                inclination = SensorHandler.getSensorValue(Sensors.COMPASS_DIRECTION);
                Log.e(TAG + " change", "compassDirection: " + inclination);
                assertEquals("Devise did not move", compass, inclination, delta);
                break;
            default:
                fail("Wrong value for axis");
        }
       controlNotMovedAxis(axis);
    }

    public void controlNotMovedAxis(String axis)
    {
        Double value = 0d;

        if(!axis.equals("X inclination")) {
            Log.e(TAG + " change", "xInclination: " + xInclination);
            value = SensorHandler.getSensorValue(Sensors.X_INCLINATION);
            assertEquals("X inclination off", 0d, value, delta);
        }
        if(!axis.equals("Y inclination")) {
            Log.e(TAG + " change", "yInclination: " + yInclination);
            value = SensorHandler.getSensorValue(Sensors.Y_INCLINATION);
            assertEquals("Y inclination off", 0d, value, delta);
        }
        if(!axis.equals("compass direction")){
                Log.e(TAG + " change", "compassDirection: " + compassDirection);
                value = SensorHandler.getSensorValue(Sensors.COMPASS_DIRECTION);
                assertEquals("Compass direction off", compassDirection, value, delta);
        }
    }

    public void sendCommandToRobotarm(final String command){

            try{
                URL myURL = new URL(robotArmURL + command);
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

    public void sendAsynchronousCommandToRobotarm(final String command, char axis){

        Thread thread = new Thread(new Runnable(){
            @Override
            public void run(){
                try{
                    URL myURL = new URL(robotArmURL + command);
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
        });
        thread.start();

        Solo solo = (Solo) Cucumber.get(Cucumber.KEY_SOLO);
        List<Double> acceleration =  new ArrayList<>();

        Sensors sensor = null;

        switch (axis){
            case 'X':
                sensor = Sensors.X_ACCELERATION;
                break;
            case 'Y':
                sensor = Sensors.Y_ACCELERATION;
                break;
            case 'Z':
                sensor = Sensors.Z_ACCELERATION;
        }

        while (thread.isAlive()) {
            acceleration.add(SensorHandler.getSensorValue(sensor));
            solo.sleep(5);
        }

        Log.e(TAG + " acc", axis  +" Acceleration: " + Collections.max(acceleration));
        assertTrue("Accelerometer test failed", Collections.max(acceleration) > 5d);
    }
}