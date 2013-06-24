package pl.aaugustyniak.stateconnectorclient;

import pl.aaugustyniak.stateconnectorclient.helpers.ViewHelper;
import pl.aaugustyniak.stateconnectorclient.model.Server;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

public class MethodActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_method);

		Intent intent = getIntent();
		String soapOutput = (String) intent.getSerializableExtra("soapOutput");
		String soapMethod = (String) intent.getSerializableExtra("soapMethod");
		Server server = (Server) intent.getSerializableExtra("server");

		TextView methodSignature = (TextView) ViewHelper.findViewById(this,
				"method_signature");
		
		methodSignature.setText(soapMethod + "@" + server.getUrl());

		EditText methodOutput = (EditText) ViewHelper.findViewById(this,
				"method_output");
		methodOutput.setText(soapOutput);
	}

}
