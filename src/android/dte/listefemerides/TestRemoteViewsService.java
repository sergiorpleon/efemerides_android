package android.dte.listefemerides;

import android.content.Intent;
import android.widget.RemoteViewsService;

public class TestRemoteViewsService extends RemoteViewsService {
	@Override
	public RemoteViewsFactory onGetViewFactory(Intent intent) {
		return new TestRemoteViewsFactory(this.getApplicationContext(), intent);
	}
}
