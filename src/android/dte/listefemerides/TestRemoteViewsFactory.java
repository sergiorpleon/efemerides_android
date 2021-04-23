package android.dte.listefemerides;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.dte.listefemerides.R;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService.RemoteViewsFactory;


class TestRemoteViewsFactory implements RemoteViewsFactory {
	private Context mContext;
	private int mAppWidgetId;
	private static String tag = "TRVF";
	String[] elmes = { "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
			"Julio", "Agosto", "Septiembre", "Octubre", "Noviembre",
			"Diciembre" };

	public TestRemoteViewsFactory(Context context, Intent intent) {
		mContext = context;
		// Purely for debugging. Unnecessary otherwise.
		mAppWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
				AppWidgetManager.INVALID_APPWIDGET_ID);
		Log.d(tag, "factory created");
	}

	// Called when your factory is first constructed.
	// The same factory may be shared across multiple
	// RemoteViewAdapters depending on the intent passed.
	public void onCreate() {
		Log.d(tag, "onCreate called for widget id:" + mAppWidgetId);
	}

	// Called when the last RemoteViewsAdapter that is
	// associated with this factory is unbound.
	public void onDestroy() {
		Log.d(tag, "destroy called for widget id:" + mAppWidgetId);
	}

	// The total number of items
	// in this list
	public int getCount() {
		DBHelper entry = new DBHelper(mContext);
		entry.open();
		return entry.getTotal();
		// return 20;

	}

	public RemoteViews getViewAt(int position) {
		// Abriendo la Base Datos
		DBHelper entry = new DBHelper(mContext);
		entry.open();
		
		int num;
		num = position + 1;

		//obtencion de la una efemeride determinada
		Efemeride e = entry.getEfemerides(num);

		// Creacion del la vista remota
		RemoteViews rv = new RemoteViews(this.mContext.getPackageName(),
				R.layout.list_item_layout);

		//---llenando remote vista
		rv.setTextViewText(
				R.id.text_anno,
				e.getDia() + " de " + elmes[e.getMes() - 1] + " de "
						+ e.getAnno());
		rv.setTextViewText(R.id.text_titulo, e.getHecho());
		
		this.loadItemOnClickExtras(rv, position);
		return rv;
	}

	private void loadItemOnClickExtras(RemoteViews rv, int position) {
		//INTENT2
		Intent ei = new Intent();
		ei.putExtra(TestListWidgetProvider.EXTRA_LIST_ITEM_TEXT,
				"Position of the item Clicked:" + position);
		rv.setOnClickFillInIntent(R.id.text_titulo, ei);  //=====este intento esta relaciono con INTENT1
	}

	// This allows for the use of a custom loading view
	// which appears between the time that getViewAt(int)
	// is called and returns. If null is returned,
	// a default loading view will be used.
	public RemoteViews getLoadingView() {
		return null;
	}

	// How many different types of views
	// are there in this list.
	public int getViewTypeCount() {
		return 1;
	}

	// The internal id of the item
	// at this position
	public long getItemId(int position) {
		return position;
	}

	// True if the same id
	// always refers to the same object.
	public boolean hasStableIds() {
		return true;
	}

	// Called when notifyDataSetChanged() is triggered
	// on the remote adapter. This allows a RemoteViewsFactory
	// to respond to data changes by updating
	// any internal references.
	// Note: expensive tasks can be safely performed
	// synchronously within this method.
	// In the interim, the old data will be displayed
	// within the widget.
	public void onDataSetChanged() {
	}
}
