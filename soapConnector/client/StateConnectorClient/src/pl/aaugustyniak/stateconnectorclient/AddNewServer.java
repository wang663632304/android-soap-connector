package pl.aaugustyniak.stateconnectorclient;

import pl.aaugustyniak.stateconnectorclient.ShowServer.ConnectorServerList;
import pl.aaugustyniak.stateconnectorclient.helpers.ViewHelper;
import pl.aaugustyniak.stateconnectorclient.model.Server;
import pl.aaugustyniak.stateconnectorclient.model.ServerDAO;
import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

public class AddNewServer implements OnClickListener {

	private ConnectorServerList context;

	AddNewServer(ConnectorServerList context) {
		this.context = context;
	}

	public void onClick(View view) {
		Server newServer = new Server();

		EditText displayName = (EditText) ViewHelper.findViewById((Activity) context,
				"display_name_field");
		newServer.setDisplayName(displayName.getText().toString());

		EditText url = (EditText) ViewHelper.findViewById((Activity) context, "url_field");
		newServer.setUrl(url.getText().toString());

		EditText apiKey = (EditText) ViewHelper.findViewById((Activity) context,
				"api_key_field");
		newServer.setApiKey(apiKey.getText().toString());

		ServerDAO.insert(newServer);
		
		context.loadServers();
	}
}