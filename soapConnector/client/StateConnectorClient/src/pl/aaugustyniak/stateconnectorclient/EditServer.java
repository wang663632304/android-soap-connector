package pl.aaugustyniak.stateconnectorclient;

import pl.aaugustyniak.stateconnectorclient.RunMethod.ConnectorMethodList;
import pl.aaugustyniak.stateconnectorclient.helpers.ViewHelper;
import pl.aaugustyniak.stateconnectorclient.model.Server;
import pl.aaugustyniak.stateconnectorclient.model.ServerDAO;
import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

public class EditServer implements OnClickListener {

	private ConnectorMethodList context;

	EditServer(ConnectorMethodList context) {
		this.context = context;
	}

	public void onClick(View view) {
		Server server = context.getServer();
		EditText displayName = (EditText) ViewHelper.findViewById((Activity) context, "display_name_field");
		server.setDisplayName(displayName.getText().toString());

		EditText url = (EditText) ViewHelper.findViewById((Activity) context, "url_field");
		server.setUrl(url.getText().toString());

		EditText apiKey = (EditText) ViewHelper.findViewById((Activity) context, "api_key_field");
		server.setApiKey(apiKey.getText().toString());

		ServerDAO.update(server);
		context.loadMethods(context.getSoap());
	}
}