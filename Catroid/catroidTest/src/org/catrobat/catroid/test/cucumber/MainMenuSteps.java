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
import android.test.AndroidTestCase;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import com.robotium.solo.Solo;

import org.catrobat.catroid.ProjectManager;
import org.catrobat.catroid.R;
import org.catrobat.catroid.formulaeditor.UserVariable;
import org.catrobat.catroid.stage.StageActivity;
import org.catrobat.catroid.ui.MainMenuActivity;
import org.catrobat.catroid.ui.MyProjectsActivity;
import org.catrobat.catroid.ui.ProjectActivity;
import org.catrobat.catroid.ui.WebViewActivity;
import org.catrobat.catroid.uitest.util.UiTestUtils;

import java.util.ArrayList;
import java.util.List;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

// CHECKSTYLE DISABLE MethodNameCheck FOR 1000 LINES
public class MainMenuSteps extends AndroidTestCase {
	private String projectName = UiTestUtils.DEFAULT_TEST_PROJECT_NAME;
	private final Object programStartWaitLock = new Object();
	private boolean programHasStarted = false;
	private String testingproject = UiTestUtils.PROJECTNAME1;

	//@Deprecated
	@Given("^I am in the main menu$")
	public void I_am_in_the_main_menu() {
		Solo solo = (Solo) Cucumber.get(Cucumber.KEY_SOLO);
		assertEquals("I am not in the main menu.", MainMenuActivity.class, solo.getCurrentActivity().getClass());
	}

	//@Deprecated
	@When("^I press the (\\w+) button$")
	public void I_press_the_s_Button(String button) {
		// searchButton(String) apparently returns true even for
		// partial matches, but clickOnButton(String) doesn't work
		// that way. Thus we must always use clickOnText(String) because
		// the features may not contain the full text of the button.
		Solo solo = (Solo) Cucumber.get(Cucumber.KEY_SOLO);
		solo.clickOnText(button);
	}

	//@Deprecated
	@Then("^I should see the following buttons$")
	public void I_should_see_the_following_buttons(List<String> expectedButtons) {
		Solo solo = (Solo) Cucumber.get(Cucumber.KEY_SOLO);
		List<String> actualButtons = new ArrayList<String>();
		for (Button button : solo.getCurrentViews(Button.class)) {
			String text = button.getText().toString();
			if (!text.isEmpty()) {
				// Only use the first paragraph of a button text.
				int trimIndex = text.contains("\n") ? text.indexOf("\n") : text.length();
				actualButtons.add(text.substring(0, trimIndex));
			}
		}
		assertEquals("I do not see the expected buttons.", expectedButtons, actualButtons);
	}

	//@Deprecated
	@Then("^I should switch to the (\\w+) view$")
	public void I_should_switch_to_the_s_view(String view) {
		Class<? extends Activity> activityClass = null;

		if ("New".equals(view)) {
			activityClass = MainMenuActivity.class;
		} else if ("programs".equals(view)) {
			activityClass = MyProjectsActivity.class;

		} else if ("Help".equals(view)) {
			activityClass = WebViewActivity.class;

		} else if ("Explore".equals(view)) {
			activityClass = WebViewActivity.class;

		} else if ("Upload".equals(view)) {
			activityClass = MainMenuActivity.class;

		} else if ("Continue".equals(view)) {
			activityClass = MainMenuActivity.class;

		} else {
			fail(String.format("View '%s' does not exist.", view));
		}
		Solo solo = (Solo) Cucumber.get(Cucumber.KEY_SOLO);
		solo.waitForActivity(activityClass.getSimpleName(), 3000);
		assertEquals("I did not switch to the expected view.", activityClass, solo.getCurrentActivity().getClass());
		solo.sleep(2000); // give activity time to completely load
		solo.getCurrentActivity().finish();
	}

	@And("^I input program name in (\\w+) button$")
	public void I_input_program_name_in_New_button(String view) throws Throwable {
		Class<? extends Activity> activityClass = null;
		Solo solo = (Solo) Cucumber.get(Cucumber.KEY_SOLO);
		if ("New".equals(view)) {
			activityClass = MainMenuActivity.class;
			assertTrue("dialog not loaded in 5 seconds",
					solo.waitForText(solo.getString(R.string.new_project_dialog_title), 0, 500));
			EditText newProject = (EditText) solo.getView(R.id.project_name_edittext);
			solo.enterText(newProject, testingproject);
			String buttonOKText = solo.getString(org.catrobat.catroid.R.string.ok);
			solo.waitForText(buttonOKText);
			solo.clickOnText(buttonOKText);
			assertTrue("dialog not loaded in 5 seconds", solo.waitForText(solo.getString(org.catrobat.catroid.R.string.project_orientation_title), 0, 500));
			solo.clickOnButton(buttonOKText);
			solo.waitForActivity(ProjectActivity.class.getSimpleName());
			assertEquals("New Project is not testingproject!", UiTestUtils.PROJECTNAME1, ProjectManager.getInstance().getCurrentProject().getName());
		}
	}

	@Then("^I play the program$")
	public void I_play_the_program() throws Throwable {

		Solo solo = (Solo) Cucumber.get(Cucumber.KEY_SOLO);
		solo.clickOnView(solo.getView(org.catrobat.catroid.R.id.button_play));
		solo.waitForActivity(StageActivity.class.getSimpleName(), 3000);
		assertEquals("I am in the wrong Activity.", StageActivity.class, solo.getCurrentActivity().getClass());

		synchronized (programStartWaitLock) {
			if (!programHasStarted) {
				programStartWaitLock.wait(1000);
			}
		}
	}

	@And("^I test the brick categories existence$")
	public void I_check_the_brick_category_control() throws Throwable {
		Solo solo = (Solo) Cucumber.get(Cucumber.KEY_SOLO);

		UiTestUtils.clickOnBottomBar(solo, org.catrobat.catroid.R.id.button_add);
		solo.clickOnText(getContext().getString(org.catrobat.catroid.R.string.category_control));
		assertTrue("testing", true);
		solo.goBack();
		solo.clickOnText(getContext().getString(org.catrobat.catroid.R.string.category_motion));
		assertTrue("testing", true);
		solo.goBack();
		solo.clickOnText(getContext().getString(R.string.category_sound));
		assertTrue("testing", true);
		solo.goBack();
		solo.clickOnText(getContext().getString(R.string.category_looks));
		assertTrue("testing", true);
		solo.goBack();
		solo.clickOnText(getContext().getString(R.string.category_data));
		assertTrue("testing", true);
		solo.goBack();
		solo.clickOnText(getContext().getString(R.string.category_user_bricks));
		assertTrue("testing", true);
		solo.goBack();


	}

	@Then("^I test the Control bricks existence$")
	public void I_test_the_Control_bricks() throws Throwable {
		Solo solo = (Solo) Cucumber.get(Cucumber.KEY_SOLO);
		UiTestUtils.clickOnBottomBar(solo, R.id.button_add);
		String categoryControl = solo.getString(R.string.category_control);
		String whenstarted = solo.getString(R.string.brick_when_started);
		String whentouched = solo.getString(R.string.brick_when_touched);
		String brickbroadcast = solo.getString(R.string.brick_broadcast);
		String brickwait = solo.getString(R.string.brick_wait);
		solo.clickOnText(categoryControl);
		assertTrue("WhenStartedBrick was not renamed for background sprite", solo.searchText(whenstarted));
		solo.clickOnText(whenstarted);
		solo.sleep(500);
		UiTestUtils.dragFloatingBrickDownwards(solo);
		assertTrue("WhenStartedBrick was not renamed for background sprite", solo.searchText(whenstarted));
		UiTestUtils.clickOnBottomBar(solo, R.id.button_add);
		solo.clickOnText(categoryControl);
		assertTrue("WhenTouchedBrick was not renamed for background sprite", solo.searchText(whentouched));
		solo.clickOnText(whentouched);
		solo.sleep(500);
		UiTestUtils.dragFloatingBrickDownwards(solo);
		assertTrue("WhenTouchedBrick was not renamed for background sprite", solo.searchText(whentouched));
		UiTestUtils.clickOnBottomBar(solo, R.id.button_add);
		solo.clickOnText(categoryControl);
		assertTrue("BrickbroadCast was not renamed for background sprite", solo.searchText(brickbroadcast));
		solo.clickOnText(brickbroadcast);
		solo.sleep(500);
		UiTestUtils.dragFloatingBrickDownwards(solo);
		assertTrue("BrickbroadCast was not renamed for background sprite", solo.searchText(brickbroadcast));
		UiTestUtils.clickOnBottomBar(solo, R.id.button_add);
		solo.clickOnText(categoryControl);
		assertTrue("BrickWait was not renamed for background sprite", solo.searchText(brickwait));
		solo.clickOnText(brickwait);
		solo.sleep(500);
		UiTestUtils.dragFloatingBrickDownwards(solo);
		assertTrue("BrickWait was not renamed for background sprite", solo.searchText(brickwait));
	}

	@Then("^I test the change variable (\\d+) by (.+)$")
	public void I_test_the_change_variable_by_(int arg1, double arg2) throws Throwable {
		Solo solo = (Solo) Cucumber.get(Cucumber.KEY_SOLO);
		UiTestUtils.clickOnBottomBar(solo, R.id.button_add);
		String categorydata = solo.getString(R.string.category_data);
		String setvariable = solo.getString(R.string.brick_set_variable);
		String changetextvariable = solo.getString(R.string.brick_change_variable);
		solo.clickOnText(categorydata);
		assertTrue("SetVariable was not renamed for background sprite", solo.searchText(setvariable));
		solo.clickOnText(setvariable);
		solo.sleep(500);
		UiTestUtils.dragFloatingBrickDownwards(solo);
		solo.sleep(500);
		assertTrue("SetVariable was not renamed for background sprite", solo.searchText(setvariable));

		UiTestUtils.clickOnBottomBar(solo, R.id.button_add);
		solo.clickOnText(categorydata);
		assertTrue("ChangeVariable was not renamed for background sprite", solo.searchText(changetextvariable));
		solo.clickOnText(changetextvariable);
		solo.sleep(500);
		UiTestUtils.dragFloatingBrickDownwards(solo);
		assertTrue("ChangeVariable was not renamed for background sprite", solo.searchText(changetextvariable));
		solo.clickOnText("New");
		solo.enterText(0, "testProject");
		String buttonOKText = solo.getString(org.catrobat.catroid.R.string.ok);
		solo.waitForText(buttonOKText);
		solo.clickOnText(buttonOKText);
		solo.clickOnText("1");
		UiTestUtils.insertIntegerIntoEditText(solo, arg1);
		solo.clickOnView(solo.getView(R.id.formula_editor_keyboard_ok));
		solo.clickOnText("1");
		UiTestUtils.insertDoubleIntoEditText(solo, arg2);
		solo.clickOnView(solo.getView(R.id.formula_editor_keyboard_ok));
		solo.waitForView(solo.getView(R.id.button_play));
		UiTestUtils.clickOnBottomBar(solo, R.id.button_play);
		solo.waitForActivity(StageActivity.class.getSimpleName());
		solo.sleep(500);
		solo.goBack();
		solo.goBack();
		solo.clickOnText("0");
		solo.clickOnView(solo.getView(R.id.formula_editor_keyboard_data));
		solo.sleep(500);
		ListView listView = getVariableListView();
		UserVariable userVariable = (UserVariable) listView.getItemAtPosition(0);
		assertEquals("Value is not equal!", userVariable.getValue(), 42.0);
	}


	private ListView getVariableListView() {
		Solo solo = (Solo) Cucumber.get(Cucumber.KEY_SOLO);
		return solo.getCurrentViews(ListView.class).get(1);
	}



	@Then("^I test the set variable \"([^\"]*)\"$")
	public void I_test_the_set_variable(String text) throws Throwable {

		Solo solo = (Solo) Cucumber.get(Cucumber.KEY_SOLO);
		UiTestUtils.clickOnBottomBar(solo, R.id.button_add);
		String categorydata = solo.getString(R.string.category_data);
		String setvariable = solo.getString(R.string.brick_set_variable);
		String showtextvariable = solo.getString(R.string.brick_show_text_var);

		solo.clickOnText(categorydata);
		assertTrue("SetVariable was not renamed for background sprite", solo.searchText(setvariable));
		solo.clickOnText(setvariable);
		solo.sleep(500);
		UiTestUtils.dragFloatingBrickDownwards(solo);
		solo.sleep(500);
		assertTrue("SetVariable was not renamed for background sprite", solo.searchText(setvariable));
		UiTestUtils.clickOnBottomBar(solo, R.id.button_add);
		solo.clickOnText(categorydata);
		assertTrue("ShowTextVariable was not renamed for background sprite", solo.searchText(showtextvariable));
		solo.clickOnText(showtextvariable);
		solo.sleep(500);
		UiTestUtils.dragFloatingBrickDownwards(solo);
		assertTrue("ShowTextVariable was not renamed for background sprite", solo.searchText(showtextvariable));
		solo.clickOnText("New");
		Spinner setVariableSpinner = solo.getCurrentViews(Spinner.class).get(0);
		solo.clickOnView(setVariableSpinner);
		solo.enterText(0, projectName);
		String buttonOKText = solo.getString(org.catrobat.catroid.R.string.ok);
		solo.waitForText(buttonOKText);
		solo.clickOnText(buttonOKText);
		solo.clickOnText("1");
		solo.clickOnView(solo.getView(R.id.formula_editor_keyboard_string));
		solo.enterText(0, text);

		solo.clickOnButton(solo.getString(R.string.ok));
		solo.clickOnView(solo.getView(R.id.formula_editor_keyboard_ok));
		solo.waitForView(solo.getView(R.id.button_play));
		UiTestUtils.clickOnBottomBar(solo, R.id.button_play);
		solo.waitForActivity(StageActivity.class.getSimpleName());
		solo.sleep(500);
		solo.goBack();
		solo.goBack();
		solo.clickOnText("0");
		solo.clickOnView(solo.getView(R.id.formula_editor_keyboard_data));
		solo.sleep(500);
		solo.clickOnView(solo.getView(R.id.formula_editor_keyboard_ok));
		ListView listView = getVariableListView();
		UserVariable userVariable = (UserVariable) listView.getItemAtPosition(0);
		assertEquals("Value is not equal!", userVariable.getValue(), "きぼう");
	}

}