package pl.aaugustyniak.stateconnectorclient.services;

import java.io.IOException;
import java.util.List;

import org.xmlpull.v1.XmlPullParserException;

import pl.aaugustyniak.stateconnectorclient.MainActivity;
import pl.aaugustyniak.stateconnectorclient.helpers.SoapWrapper;
import pl.aaugustyniak.stateconnectorclient.helpers.ViewHelper;
import pl.aaugustyniak.stateconnectorclient.model.Server;
import pl.aaugustyniak.stateconnectorclient.model.ServerDAO;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

public class PrompterService extends Service {

	private static final int PROMPTER_TIMEOUT = 5000;
	private static final String PROMPTER_FEED_METHOD = "getMessageFromStack";

	private List<Server> servers = null;
	private boolean isRunning = true;

	@Override
	public IBinder onBind(Intent intent) {
		// Not implemented
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		//ServerDAO.initialize(MainActivity.instance);
		servers = ServerDAO.readAll();
		
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		super.onStartCommand(intent, flags, startId);

		Toast.makeText(this, "Starting the Prompter Service",
				Toast.LENGTH_SHORT).show();

		isRunning = true;
		Thread backgroundThread = new Thread(new BackgroundThread());
		backgroundThread.start();
		return START_STICKY;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		isRunning = false;

		Toast.makeText(this, "Stopping the Prompter Service",
				Toast.LENGTH_SHORT).show();
	}

	private class BackgroundThread implements Runnable {

		public void run() {
			try {
				while (isRunning) {
					SoapWrapper soap = null;
					for (Server s : servers) {
						try {
							soap = new SoapWrapper(s.getUrl(), s.getApiKey());
							soap.callMethod(PROMPTER_FEED_METHOD);
							String res = soap.getResponse().toString();

							if (res != null && res.length() != 0)
								makeNotification(s.getDisplayName(),
										"Event occured "+s.getDisplayName() , res);

						} catch (XmlPullParserException e) {
							System.out.println("Web service err");
						} catch (IOException e) {
							System.out.println("IO err");
						} finally {
							System.out.println("Soap error");
						}

						Thread.currentThread();
						Thread.sleep(PROMPTER_TIMEOUT);
					}

				}
				// System.out.println("Background Thread is finished.........");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		private void makeNotification(String title, String header, String text) {

			NotificationManager notifier = (NotificationManager) MainActivity.instance
					.getSystemService(Context.NOTIFICATION_SERVICE);

			int icon = ViewHelper.findDrawableId(MainActivity.instance,
					"ic_launcher");
			Notification notification = new Notification(icon, title,
					System.currentTimeMillis());

			Intent toLaunch = new Intent(MainActivity.instance,
					MainActivity.class);
			PendingIntent contentIntent = PendingIntent.getActivity(
					MainActivity.instance, 0, toLaunch, 0);

			notification.setLatestEventInfo(MainActivity.instance, header,
					text, contentIntent);

			notification.flags |= Notification.FLAG_AUTO_CANCEL;
			notification.flags |= Notification.DEFAULT_SOUND;

			notifier.notify(0x007, notification);

		}

	}
}
