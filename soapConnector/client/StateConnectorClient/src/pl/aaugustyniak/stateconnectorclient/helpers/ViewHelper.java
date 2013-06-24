package pl.aaugustyniak.stateconnectorclient.helpers;

import java.lang.reflect.Field;

import pl.aaugustyniak.stateconnectorclient.ConnectorList;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;

public class ViewHelper {
	public static View findViewById(Activity activity, String viewId) {
		try {
			String idClass = activity.getPackageName() + ".R$id";
			@SuppressWarnings("rawtypes")
			Class clazz = Class.forName(idClass);
			Field field = clazz.getField(viewId);

			return activity.findViewById(field.getInt(clazz));
		} catch (Exception e) {
			return null;
		}
	}

	public static int findViewId(Activity activity, String variable) {
		try {
			String idClass = activity.getPackageName() + ".R$id";
			@SuppressWarnings("rawtypes")
			Class clazz = Class.forName(idClass);
			Field field = clazz.getField(variable);

			return field.getInt(clazz);
		} catch (Exception e) {
			return -1;
		}
	}

	public static int findLayoutId(Activity activity, String variable) {
		try {
			String idClass = activity.getPackageName() + ".R$layout";
			@SuppressWarnings("rawtypes")
			Class clazz = Class.forName(idClass);
			Field field = clazz.getField(variable);

			return field.getInt(clazz);
		} catch (Exception e) {
			return -1;
		}
	}

	public static AlertDialog getOkModal(ConnectorList currentActivity,
			String title, String message) {
		AlertDialog okModal = null;

		okModal = new AlertDialog.Builder((Activity) currentActivity)
				.setTitle(title).setMessage(message).setCancelable(false)
				.create();
		okModal.setButton(DialogInterface.BUTTON_POSITIVE, "OK",
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int status) {
						dialog.dismiss();
					}
				});

		return okModal;
	}

	public static int findDrawableId(Activity activity, String variable) {
		try {
			String idClass = activity.getPackageName() + ".R$drawable";
			@SuppressWarnings("rawtypes")
			Class clazz = Class.forName(idClass);
			Field field = clazz.getField(variable);

			return field.getInt(clazz);
		} catch (Exception e) {
			return -1;
		}
	}

}