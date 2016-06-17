package com.yunguo.ygmjconfig.Util;

import android.R;
import android.app.Activity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

public class EditChangedListener implements TextWatcher{
	
	private EditText edite;
	private View view;
	
	
	public EditChangedListener(View view,EditText edite){
		this.edite = edite;
		this.view = view;
	}

	@Override
	public void afterTextChanged(Editable s) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		switch (edite.getId()) {
		case 00:
			break;

		default:
			break;
		}
	}

}
