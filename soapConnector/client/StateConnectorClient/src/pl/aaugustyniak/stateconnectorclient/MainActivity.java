package pl.aaugustyniak.stateconnectorclient;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import pl.aaugustyniak.stateconnectorclient.ShowServer.ConnectorServerList;
import pl.aaugustyniak.stateconnectorclient.helpers.ViewHelper;
import pl.aaugustyniak.stateconnectorclient.model.Server;
import pl.aaugustyniak.stateconnectorclient.model.ServerDAO;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class MainActivity extends Activity implements ConnectorServerList {

	private OnItemClickListener showServer = null;
	private AddNewServer newServer = null;
	public static MainActivity instance;
	private List<Server> servers = null;
	private static final String PROMPTER_SERVICE = "pl.aaugustyniak.stateconnectorclient.services.PrompterService";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		instance = this;
		ServerDAO.initialize(this);
		Intent start = new Intent(PROMPTER_SERVICE);
		MainActivity.this.startService(start);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Intent stop = new Intent(PROMPTER_SERVICE);
		MainActivity.this.stopService(stop);
		ServerDAO.close();
	}

	@Override
	protected void onResume() {
		super.onResume();
		try {
			servers = ServerDAO.readAll();
			this.loadServers();

		} catch (Exception e) {
			e.printStackTrace(System.out);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuItem newServer = menu.add(Menu.NONE, Menu.NONE, 0, "New Server");
		newServer.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			public boolean onMenuItemClick(MenuItem clickedItem) {
				MainActivity.this.loadNewServer();
				return true;
			}
		});
		return true;
	}

	public void loadServers() {
		 servers = ServerDAO.readAll();
		try {
			String layoutClass = this.getPackageName() + ".R$layout";
			String main = "activity_main";
			@SuppressWarnings("rawtypes")
			Class clazz = Class.forName(layoutClass);
			Field field = clazz.getField(main);
			int screenId = field.getInt(clazz);
			this.setContentView(screenId);

			ListView list = (ListView) ViewHelper.findViewById(this, "list");
			ArrayList<HashMap<String, String>> listData = new ArrayList<HashMap<String, String>>();

			int size = servers.size();
			for (int i = 0; i < size; i++) {
				Server server = servers.get(i);
				HashMap<String, String> local = new HashMap<String, String>();
				local.put("name", server.getDisplayName());
				local.put("url", server.getUrl());
				listData.add(local);
			}

			int row = ViewHelper.findLayoutId(this, "server_row");
			int[] cells = new int[] { ViewHelper.findViewId(this, "name"),
					ViewHelper.findViewId(this, "url") };
			String[] header = new String[] { "name", "url" };

			SimpleAdapter listAdapter = new SimpleAdapter(this, listData, row,
					header, cells);
			list.setAdapter(listAdapter);

			showServer = new ShowServer(servers, this);
			list.setOnItemClickListener(showServer);

		} catch (Exception e) {
			e.printStackTrace(System.out);
		}
	}

	private void loadNewServer() {

		try {
			String layoutClass = this.getPackageName() + ".R$layout";
			String new_server = "server_details";
			@SuppressWarnings("rawtypes")
			Class clazz = Class.forName(layoutClass);
			Field field = clazz.getField(new_server);
			int screenId = field.getInt(clazz);
			this.setContentView(screenId);

			Button save = (Button) ViewHelper.findViewById(this, "save");
			newServer = new AddNewServer(this);
			save.setOnClickListener(newServer);

			Button cancel = (Button) ViewHelper.findViewById(this, "cancel");
			cancel.setOnClickListener(new OnClickListener() {
				public void onClick(View button) {
					Toast.makeText(MainActivity.this,
							"Server Creation was cancelled!!",
							Toast.LENGTH_LONG).show();
					MainActivity.this.loadServers();
				}
			});
		} catch (Exception e) {
			e.printStackTrace(System.out);
		}
	}
}
