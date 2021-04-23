package android.dte.listefemerides;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.PendingIntent.CanceledException;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.dte.listefemerides.R;
import android.net.Uri;
import android.util.Log;
import android.widget.RemoteViews;

//widget en forma de lista
public class TestListWidgetProvider extends AppWidgetProvider {
	AppWidgetManager miWM;
	int[] appWIds;
	private static final String tag = "TestListWidgetProvider";
	public static final String ACTION_LIST_CLICK = "com.androidbook.homewidgets.listclick";
	public static final String EXTRA_LIST_ITEM_TEXT = "com.androidbook.homewidgets.list_item_text";

	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {

		// ---recorrer los widget y actualizarlos
		final int N = appWidgetIds.length;
		for (int i = 0; i < N; i++) {
			int appWidgetId = appWidgetIds[i];
			updateAppWidget(context, appWidgetManager, appWidgetId);
		}

		miWM = appWidgetManager;
		super.onUpdate(context, appWidgetManager, appWidgetIds);
		appWIds = appWidgetIds;
	}

	public void onDeleted(Context context, int[] appWidgetIds) {
		super.onDeleted(context, appWidgetIds);
	}

	public void onEnabled(Context context) {
		super.onEnabled(context);
		DBHelper entry = new DBHelper(context);
		entry.open();
	}

	public void onDisabled(Context context) {
		super.onDisabled(context);
	}

	private void updateAppWidget(Context context,
			AppWidgetManager appWidgetManager, int appWidgetId) {

		// actualizacion del widget
		appWidgetManager.updateAppWidget(appWidgetId, null);

		//=====Intent para recibir el click sobre un item (RElacionado con TestRemoteView)
		// setup a list view callback.
		// you need a pending intent that is unique
		// for this widget id. Send a message to
		// ourselves which you will catch in OnReceive.
		Intent onListClickIntent = new Intent(context,
				TestListWidgetProvider.class);

		// set an action so that this receiver can distinguish it
		// from other widget related actions
		onListClickIntent.setAction(TestListWidgetProvider.ACTION_LIST_CLICK);
		// because this receiver serves all instances
		// of this app widget. You need to know which
		// specific instance this message is targeted for.
		onListClickIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
				appWidgetId);

		// Make this intent unique as you are getting ready
		// to create a pending intent with it.
		// The toUri method loads the extras as
		// part of the uri string.
		// The data of this intent is not used at all except
		// to establish this intent as a unique pending intent.
		// See intent.filterEquals() method to see
		// how intents are compared to see if they are unique.
		onListClickIntent.setData(Uri.parse(onListClickIntent
				.toUri(Intent.URI_INTENT_SCHEME)));

		// you need to deliver this intent later when
		// the remote view is clicked as a broadcast intent
		// to this same receiver.
		final PendingIntent onListClickPendingIntent = PendingIntent
				.getBroadcast(context, 0, onListClickIntent,
						PendingIntent.FLAG_UPDATE_CURRENT);

		
		// ---Creacion de la vista remota
		RemoteViews rv = new RemoteViews(context.getPackageName(),
				R.layout.test_list_widget_layout);
		// rv = null;
		// rv = new RemoteViews(context.getPackageName(),
		// R.layout.test_list_widget_layout);
		// rv.setEmptyView(R.id.listwidget_list_view_id,
		// -1);

		rv.setEmptyView(R.id.listwidget_list_view_id,
				R.id.listwidget_empty_view_id);
		// Specify the service to provide data for the
		// collection widget.

		final Intent intent = new Intent(context, TestRemoteViewsService.class); //Intento que llama al servicio
		// This is purely for debugging. Unnecessary otherwise
		intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
		rv.setRemoteAdapter(appWidgetId, R.id.listwidget_list_view_id, intent);

		// Set this pending intent as a template for
		// the list item view.
		// Each view in the list will then need to specify
		// a set of additional extras to be appended
		// to this template and then broadcast the
		// final template.
		// See how the remoteviewsfactory() sets up
		// the each item in the list remoteview.
		// See also docs for RemoteViews.setFillIntent()
		rv.setPendingIntentTemplate(R.id.listwidget_list_view_id,
				onListClickPendingIntent); //=====finalmente luego de INTENT1 recibir el INTENT2 es llamado

		
		// ---Intent para recibir el click en el buscar
		Intent intent2 = new Intent(context, BuscarActivityList.class);
		intent2.setAction("BUSCAR");
		intent2.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
		PendingIntent pendingIntent2 = PendingIntent.getActivity(context,
				appWidgetId, intent2, PendingIntent.FLAG_UPDATE_CURRENT);
		rv.setOnClickPendingIntent(R.id.listwidget_footer_textview_id,
				pendingIntent2);

		// ---Intent para recibir cuando es las 12 am (cambio de dia)
		Intent inten = new Intent(context, TestListWidgetProvider.class);
		inten.setAction("ALARM");
		PendingIntent pIntent = PendingIntent.getBroadcast(context, 1, inten,
				PendingIntent.FLAG_CANCEL_CURRENT);

		AlarmManager manager = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		// int interval = 1000 * 60 * 20;

		/* Set the alarm to start at 10:30 AM */
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis());
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 60);

		manager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
				pIntent);
		/* Repeating on every 20 minutes interval */
		// manager.setRepeating(AlarmManager.RTC_WAKEUP,
		// calendar.getTimeInMillis(),
		// 1000 * 60 * 20, pIntent);

		appWidgetManager.updateAppWidget(appWidgetId, rv);
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent.getAction().equals(TestListWidgetProvider.ACTION_LIST_CLICK)) {
			// this action is not one of usual widget actions
			// such as onDeleted, onEnabled etc.
			// Instead this is a specific action that is directed here
			// by the intents loaded into the list view items
			dealWithListAction(context, intent);
			return;
		}

		// Obtenemos el widget manager de nuestro contexto
		AppWidgetManager widgetManager = AppWidgetManager.getInstance(context);
		// obtenemos el arreglo de widget
		appWIds = widgetManager.getAppWidgetIds(new ComponentName(context,
				TestListWidgetProvider.class));
		// Actualizamos el widget
		onUpdate(context, widgetManager, appWIds);

		super.onReceive(context, intent);
	}

	public void dealWithListAction(Context context, Intent intent) {
		String clickedItemText = intent
				.getStringExtra(TestListWidgetProvider.EXTRA_LIST_ITEM_TEXT);

		if (clickedItemText == null) {
			clickedItemText = "Error";
		}
		String ind = clickedItemText;
		clickedItemText = clickedItemText + "Clicked on item text:"
				+ clickedItemText;
		// Toast t = Toast.makeText(context, clickedItemText,
		// Toast.LENGTH_LONG);
		// t.show();

		String indice = ind.split(":")[1]; // obtencion del indice del item
		Global.ahora = Integer.parseInt(indice);

		// ---Llamado a la clase ver para ver la efemeride
		try {
			Intent intent1 = new Intent(context, VerActivityList.class);
			PendingIntent pendingIntent1 = PendingIntent.getActivity(context,
					0, intent1, 0);
			pendingIntent1.send();
		} catch (CanceledException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}// eof-class