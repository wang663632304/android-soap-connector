package pl.aaugustyniak.stateconnectorclient;

import java.io.IOException;
import java.util.List;

import org.xmlpull.v1.XmlPullParserException;

import pl.aaugustyniak.stateconnectorclient.helpers.SoapWrapper;
import pl.aaugustyniak.stateconnectorclient.model.Server;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

public class ShowServer implements OnItemClickListener {

	public interface ConnectorServerList extends ConnectorList {
		public void loadServers();
	}

	private static final String SOAP_MESSAGE = "soapObject";
	private static final String DATA_OBJECT = "server";

	private List<Server> servers;
	private ConnectorServerList context;

	public ShowServer(List<Server> servers, ConnectorServerList context) {
		this.servers = servers;
		this.context = context;
	}

	@Override
	public void onItemClick(AdapterView<?> adapter, View view, int position,
			long id) {

		final Server server = servers.get(position);
		String apiUrl = server.getUrl();
		String apiKey = server.getApiKey();
		Intent intent = new Intent((Context) context, ServerActivity.class);// .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		try {

			intent.putExtra(SOAP_MESSAGE, new SoapWrapper(apiUrl, apiKey));

		} catch (XmlPullParserException e) {
			Toast.makeText((Context) context, "Web Service Error",
					Toast.LENGTH_LONG).show();
		} catch (IOException e) {
			Toast.makeText((Context) context, "IO Error", Toast.LENGTH_LONG)
					.show();
		}
		intent.putExtra(DATA_OBJECT, server);
		((Context) context).startActivity(intent);

	}
}