package android.dte.listefemerides;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.RemoteViews;

public class WidgetEfemerides extends AppWidgetProvider {
	private static final String ACCION_PULSAR = "PULSAR";
	private static final String ACCION_VER = "VER";
	private static final String ACCION_BUSCAR = "BUSCAR";

	@Override
	public void onEnabled(Context context) {

		DBHelper entry = new DBHelper(context);
		entry.open();

		SharedPreferences miPrefer = context.getSharedPreferences(
				"WidgetPrefs", Context.MODE_PRIVATE);

		// --- Creacion del editor donde se salvara los checkBoxs
		SharedPreferences.Editor editor = miPrefer.edit();

		// Otencion del numero de la efemeride guardada
		int num = miPrefer.getInt("efemeride", 1);
		editor.putInt("efemeride", 1);
		editor.commit();
	}

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		// Iteramos la lista de widgets en ejecución
		for (int i = 0; i < appWidgetIds.length; i++) {
			// ID del widget actual
			int widgetId = appWidgetIds[i];
			// Actualizamos el widget actual
			actualizar(context, appWidgetManager, widgetId);
		}
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent.getAction().equals(ACCION_PULSAR)) {
			// Obtenemos el ID del widget a actualizar
			int widgetId = intent.getIntExtra(
					AppWidgetManager.EXTRA_APPWIDGET_ID,
					AppWidgetManager.INVALID_APPWIDGET_ID);
			// Obtenemos el widget manager de nuestro contexto
			AppWidgetManager widgetManager = AppWidgetManager
					.getInstance(context);
			// Actualizamos el widget
			if (widgetId != AppWidgetManager.INVALID_APPWIDGET_ID) {
				actualizar(context, widgetManager, widgetId);
			}
		} else if (intent.getAction().equals(ACCION_VER)) {
			// context.startActivity(new Intent(context, VerActivity.class));
			// context.overridePendingTransition(R.anim.zoom_forward_in,
			// R.anim.zoom_forward_out);
			Global.mes = 0;
		} else {
			// Obtencion del widgetManager
			AppWidgetManager widgetManager = AppWidgetManager
					.getInstance(context);
			// Obtencion del arreglo de id de widget
			int[] appWIds;
			appWIds = widgetManager.getAppWidgetIds(new ComponentName(context,
					WidgetEfemerides.class));
			// Actualizamos el widget
			onUpdate(context, widgetManager, appWIds);
		}
		super.onReceive(context, intent);
	}

	public static void actualizar(Context context,
			AppWidgetManager widgetManager, int widgetId) {
		RemoteViews componentes = new RemoteViews(context.getPackageName(),
				R.layout.widget_efemerides);

		// ---Intent para pasar a la proxima efemride del dia
		final Intent onClickIntent = new Intent(context, WidgetEfemerides.class);
		onClickIntent.setAction(WidgetEfemerides.ACCION_PULSAR);
		onClickIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);
		final PendingIntent onClickPendingIntent = PendingIntent.getBroadcast(
				context, widgetId, onClickIntent,
				PendingIntent.FLAG_UPDATE_CURRENT);
		componentes.setOnClickPendingIntent(R.id.text_titulo,
				onClickPendingIntent);

		// ---Intent para mostrar las efemerides
		Intent intent2 = new Intent(context, VerActivitySimple.class);
		intent2.setAction(WidgetEfemerides.ACCION_VER);
		intent2.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);
		PendingIntent pendingIntent2 = PendingIntent.getActivity(context,
				widgetId, intent2, PendingIntent.FLAG_UPDATE_CURRENT);
		componentes.setOnClickPendingIntent(R.id.btn_ver, pendingIntent2);

		// ---Intent para mostrar la pantalla buscar
		Intent intent3 = new Intent(context, BuscarActivityList.class);
		intent3.setAction(WidgetEfemerides.ACCION_VER);
		intent3.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);
		PendingIntent pendingIntent3 = PendingIntent.getActivity(context,
				widgetId, intent3, PendingIntent.FLAG_UPDATE_CURRENT);

		componentes.setOnClickPendingIntent(R.id.btn_buscar, pendingIntent3);

		// ---Intent para el cambio de dia a las 12 am
		Intent inten = new Intent(context, WidgetEfemerides.class);
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

		// ---Mostrarndo el contenido del widget
		SharedPreferences miPrefer = context.getSharedPreferences(
				"WidgetPrefs", Context.MODE_PRIVATE);

		DBHelper entry = new DBHelper(context);
		entry.open();

		int num;
		num = (miPrefer.getInt("efemeride", 1));// obteniendo el numero de la
												// efemeride

		// Chequeando que no este fuera de rango
		if (num >= entry.getTotal() + 1) {
			num = 1;
		}

		// obteneiendo una efemeride determinada
		Efemeride e = entry.getEfemerides(num);

		//poniendo el año
		componentes.setTextViewText(R.id.text_anno,
				e.getDia() + "-" + e.getMes() + "-" + e.getAnno());
		//poniendo el titulo
		componentes.setTextViewText(R.id.text_titulo, e.getHecho());
		//poniendo el numero de la efemire del total
		componentes.setTextViewText(R.id.text_numero,
				num + " de " + entry.getTotal());

		widgetManager.updateAppWidget(widgetId, componentes);

		//pasando de efemeride
		num++;

		//Guardando el indice de la efemeride proxima
		SharedPreferences.Editor editor = miPrefer.edit();
		editor.putInt("efemeride", num);
		editor.commit();
	}
}
