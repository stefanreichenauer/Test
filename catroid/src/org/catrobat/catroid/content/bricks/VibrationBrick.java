/*
 * Catroid: An on-device visual programming system for Android devices
 * Copyright (C) 2010-2016 The Catrobat Team
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
package org.catrobat.catroid.content.bricks;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;

import org.catrobat.catroid.ProjectManager;
import org.catrobat.catroid.R;
import org.catrobat.catroid.common.BrickValues;
import org.catrobat.catroid.content.Project;
import org.catrobat.catroid.content.Sprite;
import org.catrobat.catroid.formulaeditor.Formula;
import org.catrobat.catroid.formulaeditor.InterpretationException;
import org.catrobat.catroid.ui.fragment.FormulaEditorFragment;
import org.catrobat.catroid.utils.Utils;

import java.util.List;

public class VibrationBrick extends FormulaBrick {
	private static final long serialVersionUID = 1L;

	private transient View prototypeView;

	public VibrationBrick() {
		addAllowedBrickField(BrickField.VIBRATE_DURATION_IN_SECONDS);
	}

	public VibrationBrick(float vibrateDurationInSeconds) {
		initializeBrickFields(new Formula(vibrateDurationInSeconds));
	}

	public VibrationBrick(Formula vibrateDurationInSecondsFormula) {
		initializeBrickFields(vibrateDurationInSecondsFormula);
	}

	private void initializeBrickFields(Formula vibrateDurationInSecondsFormula) {
		addAllowedBrickField(BrickField.VIBRATE_DURATION_IN_SECONDS);
		setFormulaWithBrickField(BrickField.VIBRATE_DURATION_IN_SECONDS, vibrateDurationInSecondsFormula);
	}

	@Override
	public int getRequiredResources() {
		return VIBRATOR;
	}

	@Override
	public View getView(Context context, int brickId, BaseAdapter baseAdapter) {
		if (animationState) {
			return view;
		}

		view = View.inflate(context, R.layout.brick_vibration, null);
		view = getViewWithAlpha(alphaValue);

		setCheckboxView(R.id.brick_vibration_checkbox);

		final Brick brickInstance = this;
		checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				checked = isChecked;
				adapter.handleCheck(brickInstance, isChecked);
			}
		});

		TextView textVibrate = (TextView) view.findViewById(R.id.brick_vibration_prototype_text_view);
		TextView editVibrate = (TextView) view.findViewById(R.id.brick_vibration_edit_text);
		TextView secondTextVibrate = (TextView) view.findViewById(R.id.brick_vibration_second_label);

		getFormulaWithBrickField(BrickField.VIBRATE_DURATION_IN_SECONDS)
				.setTextFieldId(R.id.brick_vibration_edit_text);
		getFormulaWithBrickField(BrickField.VIBRATE_DURATION_IN_SECONDS).refreshTextField(view);

		if (getFormulaWithBrickField(BrickField.VIBRATE_DURATION_IN_SECONDS).isSingleNumberFormula()) {
			try {
				secondTextVibrate.setText(view.getResources().getQuantityString(R.plurals.second_plural,
						Utils.convertDoubleToPluralInteger(getFormulaWithBrickField(BrickField.VIBRATE_DURATION_IN_SECONDS)
								.interpretDouble(ProjectManager.getInstance().getCurrentSprite()))));
			} catch (InterpretationException interpretationException) {
				Log.d(getClass().getSimpleName(), "Formula interpretation for this specific Brick failed.", interpretationException);
			}
		} else {
			secondTextVibrate.setText(view.getResources().getQuantityString(R.plurals.second_plural,
					Utils.TRANSLATION_PLURAL_OTHER_INTEGER));
		}

		textVibrate.setVisibility(View.GONE);
		editVibrate.setVisibility(View.VISIBLE);
		editVibrate.setOnClickListener(this);

		return view;
	}

	@Override
	public View getPrototypeView(Context context) {
		prototypeView = View.inflate(context, R.layout.brick_vibration, null);
		TextView textVibrate = (TextView) prototypeView.findViewById(R.id.brick_vibration_prototype_text_view);
		textVibrate.setText(Utils.getNumberStringForBricks(BrickValues.VIBRATE_SECONDS));
		TextView secondTextVibrate = (TextView) prototypeView.findViewById(R.id.brick_vibration_second_label);
		secondTextVibrate.setText(context.getResources().getQuantityString(R.plurals.second_plural,
				Utils.convertDoubleToPluralInteger(BrickValues.VIBRATE_SECONDS)));
		return prototypeView;
	}

	@Override
	public View getViewWithAlpha(int alphaValue) {
		if (view != null) {
			View layout = view.findViewById(R.id.brick_vibration_layout);
			Drawable background = layout.getBackground();
			background.setAlpha(alphaValue);

			TextView textVibrationLabel = (TextView) view.findViewById(R.id.brick_vibration_label);
			TextView textVibrationSeconds = (TextView) view.findViewById(R.id.brick_vibration_prototype_text_view);
			TextView editSeconds = (TextView) view.findViewById(R.id.brick_vibration_edit_text);

			textVibrationLabel.setTextColor(textVibrationLabel.getTextColors().withAlpha(alphaValue));
			textVibrationSeconds.setTextColor(textVibrationSeconds.getTextColors().withAlpha(alphaValue));

			editSeconds.setTextColor(editSeconds.getTextColors().withAlpha(alphaValue));
			editSeconds.getBackground().setAlpha(alphaValue);

			this.alphaValue = alphaValue;
		}
		return view;
	}

	@Override
	public List<SequenceAction> addActionToSequence(Sprite sprite, SequenceAction sequence) {
		sequence.addAction(sprite.getActionFactory().createVibrateAction(sprite,
				getFormulaWithBrickField(BrickField.VIBRATE_DURATION_IN_SECONDS)));
		return null;
	}

	@Override
	public void showFormulaEditorToEditFormula(View view) {
		FormulaEditorFragment.showFragment(view, this, BrickField.VIBRATE_DURATION_IN_SECONDS);
	}

	@Override
	public void updateReferenceAfterMerge(Project into, Project from) {
	}
}
