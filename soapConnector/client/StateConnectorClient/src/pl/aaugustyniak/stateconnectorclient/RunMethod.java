package pl.aaugustyniak.stateconnectorclient;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParserException;

import pl.aaugustyniak.stateconnectorclient.helpers.SoapWrapper;
import pl.aaugustyniak.stateconnectorclient.model.Server;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

public class RunMethod implements OnItemClickListener {

	private static final String SOAP_OUTPUT = "soapOutput";
	private static final String SOAP_METHOD = "soapMethod";
	private static final String DATA_OBJECT = "server";

	private SoapWrapper connection;
	private ConnectorMethodList context;

	public interface ConnectorMethodList extends ConnectorList {
		public void loadMethods(SoapWrapper soap);

		public SoapWrapper getSoap();

		public Server getServer();

	}

	public RunMethod(SoapWrapper connection, ConnectorMethodList context) {
		this.connection = connection;
		this.context = context;
	}

	@Override
	public void onItemClick(AdapterView<?> adapter, View view, int position,
			long id) {

		final String method = connection.getMethodsList().get(position);

		Intent intent = new Intent((Context) context, MethodActivity.class);// .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		try {
			intent.putExtra(SOAP_OUTPUT, connection.callMethod(method)
					.getResponse().toString());
		} catch (XmlPullParserException e) {
			Toast.makeText((Context) context, "Web Service Error",
					Toast.LENGTH_LONG).show();
		} catch (IOException e) {
			Toast.makeText((Context) context, "IO Error", Toast.LENGTH_LONG)
					.show();
		}
		intent.putExtra(DATA_OBJECT, context.getServer());
		intent.putExtra(SOAP_METHOD, method);
		((Context) context).startActivity(intent);
	}
}
