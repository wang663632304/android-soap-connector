package pl.aaugustyniak.stateconnectorclient.model;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public final class ServerDAO {

	private static SQLiteDatabase db = null;

	public static void initialize(Context context) {

		if (db == null) {
			db = context.openOrCreateDatabase(
					"pl.aaugustyniak.stateconnectorclient.servers.db",
					SQLiteDatabase.CREATE_IF_NECESSARY, null);

			createTable(db, "server");
			if (isTableEmpty("server")) {

				Server local = new Server();

				local.setDisplayName("Pierwszy");
				local.setUrl("http://home.aaugustyniak.pl/ewsie/soap/server2.php");
				local.setApiKey("qwerty");
				local.setReportable(0);

				insert(local);

			}
		}

	}

	private static void createTable(SQLiteDatabase database, String tableName) {
		try {
			database.beginTransaction();

			String tableSql = "CREATE TABLE IF NOT EXISTS " + tableName + " ("
					+ "id INTEGER PRIMARY KEY AUTOINCREMENT,"
					+ "display_name TEXT," + "url TEXT," + "api_key TEXT,"
					+ "reportable INTEGER" + ");";
			database.execSQL(tableSql);

			database.setTransactionSuccessful();
		} finally {
			database.endTransaction();
		}
	}

	private static boolean isTableEmpty(String table) {
		Cursor cursor = null;
		try {
			cursor = db.rawQuery("SELECT count(*) FROM " + table, null);

			int countIndex = cursor.getColumnIndex("count(*)");
			cursor.moveToFirst();
			int rowCount = cursor.getInt(countIndex);
			if (rowCount > 0) {
				return false;
			}

			return true;
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
	}

	public static void insert(Server server) {
		try {
			db.beginTransaction();

			String displayName = server.getDisplayName();
			int reportable = server.getReportable();
			String url = server.getUrl();
			String apiKey = server.getApiKey();

			String insert = "INSERT INTO server "
					+ " (display_name, url, api_key, reportable) VALUES (?,?,?,?);";
			db.execSQL(insert, new Object[] { displayName, url, apiKey,
					reportable });
			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
		}
	}

	public static void update(Server server) {
		try {
			db.beginTransaction();

			int id = server.getId();
			int reportable = server.getReportable();
			String displayName = server.getDisplayName();
			String url = server.getUrl();
			String apiKey = server.getApiKey();

			String update = "UPDATE server SET"
					+ " display_name=?, url=?, api_key=?, reportable=? WHERE id=?;";
			db.execSQL(update, new Object[] { displayName, url, apiKey,
					reportable, id });
			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
		}
	}

	public static List<Server> readAll() {
		Cursor cursor = null;
		try {
			List<Server> all = new ArrayList<Server>();

			cursor = db.rawQuery("SELECT * FROM server", null);
			if (cursor.getCount() > 0) {
				int idIndex = cursor.getColumnIndex("id");
				int displayNameIndex = cursor.getColumnIndex("display_name");
				int urlIndex = cursor.getColumnIndex("url");
				int apiKeyIndex = cursor.getColumnIndex("api_key");
				int reportableIndex = cursor.getColumnIndex("reportable");

				cursor.moveToFirst();
				do {
					int id = cursor.getInt(idIndex);
					String displayName = cursor.getString(displayNameIndex);
					String url = cursor.getString(urlIndex);
					String apiKey = cursor.getString(apiKeyIndex);
					int reportable = cursor.getInt(reportableIndex);

					Server server = new Server();
					server.setId(id);
					server.setDisplayName(displayName);
					server.setUrl(url);
					server.setApiKey(apiKey);
					server.setReportable(reportable);
					all.add(server);
					cursor.moveToNext();
				} while (!cursor.isAfterLast());
			}
			return all;
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
	}

	public static void delete(Server server) {
		try {
			db.beginTransaction();

			String delete = "DELETE FROM server WHERE id='" + server.getId()
					+ "'";
			db.execSQL(delete);

			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
		}
	}

	public static void close() {
		db.close();
	}

}
