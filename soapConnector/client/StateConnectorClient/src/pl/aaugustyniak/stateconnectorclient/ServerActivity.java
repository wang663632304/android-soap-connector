package pl.aaugustyniak.stateconnectorclient;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;

import pl.aaugustyniak.stateconnectorclient.RunMethod.ConnectorMethodList;
import pl.aaugustyniak.stateconnectorclient.helpers.SoapWrapper;
import pl.aaugustyniak.stateconnectorclient.helpers.ViewHelper;
import pl.aaugustyniak.stateconnectorclient.model.Server;
import pl.aaugustyniak.stateconnectorclient.model.ServerDAO;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.OnItemClickListener;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class ServerActivity extends Activity implements ConnectorMethodList {

	private OnItemClickListener runMethod = null;
	private SoapWrapper soap = null;
	private Server server = null;

	/**
	 * @return the server
	 */
	public Server getServer() {
		return server;
	}

	/**
	 * @return the soap
	 */
	public SoapWrapper getSoap() {
		return soap;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void onResume() {
		try {
			super.onResume();
			Intent intent = getIntent();
			soap = (SoapWrapper) intent.getSerializableExtra("soapObject");
			server = (Server) intent.getSerializableExtra("server");
			this.loadMethods(soap);
		} catch (Exception e) {
			e.printStackTrace(System.out);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuItem editItem = menu.add(Menu.NONE, Menu.NONE, 0, "Edit Server");
		editItem.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			public boolean onMenuItemClick(MenuItem clickedItem) {
				editServer();
				return true;
			}
		});

		MenuItem delItem = menu.add(Menu.NONE, Menu.NONE, 0, "Delete Server");
		delItem.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			public boolean onMenuItemClick(MenuItem clickedItem) {
				delServer();
				return true;
			}
		});
		return true;
	}

	private void delServer() {

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		String displayName = server.getDisplayName();
		String apiKey = server.getApiKey();

		builder = builder.setMessage(apiKey);
		builder = builder.setCancelable(true);
		builder = builder.setTitle(displayName);

		builder.setPositiveButton("Delete",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.dismiss();
						ServerDAO.delete(server);
						MainActivity.instance.loadServers();
					}
				}).setNeutralButton("Close",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.dismiss();
					}
				});

		AlertDialog alert = builder.create();
		alert.show();

	}

	public void loadMethods(SoapWrapper soap) {
		try {
			String layoutClass = this.getPackageName() + ".R$layout";
			String main = "activity_server";
			@SuppressWarnings("rawtypes")
			Class clazz = Class.forName(layoutClass);
			Field field = clazz.getField(main);
			int screenId = field.getInt(clazz);
			this.setContentView(screenId);

			ListView list = (ListView) ViewHelper.findViewById(this,
					"methods_list");

			ArrayList<HashMap<String, String>> listData = new ArrayList<HashMap<String, String>>();

			int size = soap.getMethodsList().size();
			for (int i = 0; i < size; i++) {
				String method = soap.getMethodsList().get(i);
				HashMap<String, String> local = new HashMap<String, String>();
				local.put("name", method);
				local.put("method", method);
				listData.add(local);
			}

			int row = ViewHelper.findLayoutId(this, "method_row");
			int[] cells = new int[] { ViewHelper.findViewId(this, "name"),
					ViewHelper.findViewId(this, "method") };
			String[] header = new String[] { "name", "method" };

			SimpleAdapter listAdapter = new SimpleAdapter(this, listData, row,
					header, cells);
			list.setAdapter(listAdapter);

			runMethod = new RunMethod(soap, this);
			list.setOnItemClickListener(runMethod);

		} catch (Exception e) {
			e.printStackTrace(System.out);
		}
	}

	private void editServer() {
		try {
			String layoutClass = this.getPackageName() + ".R$layout";
			String new_server = "server_details";
			@SuppressWarnings("rawtypes")
			Class clazz = Class.forName(layoutClass);
			Field field = clazz.getField(new_server);
			int screenId = field.getInt(clazz);
			this.setContentView(screenId);

			EditText displayName = (EditText) ViewHelper.findViewById(this,
					"display_name_field");
			displayName.setText(server.getDisplayName());

			EditText url = (EditText) ViewHelper
					.findViewById(this, "url_field");
			url.setText(server.getUrl());

			EditText apiKey = (EditText) ViewHelper.findViewById(this,
					"api_key_field");
			apiKey.setText(server.getApiKey());

			Button save = (Button) ViewHelper.findViewById(this, "save");
			EditServer server = new EditServer(this);
			save.setOnClickListener(server);

			Button cancel = (Button) ViewHelper.findViewById(this, "cancel");
			cancel.setOnClickListener(new OnClickListener() {
				public void onClick(View button) {
					Toast.makeText(ServerActivity.this,
							"Server Edition was cancelled!!", Toast.LENGTH_LONG)
							.show();
					ServerActivity.this.loadMethods(soap);
				}
			});
		} catch (Exception e) {
			e.printStackTrace(System.out);
		}
	}

}
