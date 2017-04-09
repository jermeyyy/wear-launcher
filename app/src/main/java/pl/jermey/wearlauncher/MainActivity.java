package pl.jermey.wearlauncher;

import android.content.ComponentName;
import android.content.Intent;
import android.support.wearable.activity.WearableActivity;
import android.support.wearable.view.SwipeDismissFrameLayout;
import android.widget.FrameLayout;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import pl.jermey.wearlauncher.fragments.AppListFragment_;

@EActivity(R.layout.main_activity)
public class MainActivity extends WearableActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    @ViewById
    FrameLayout container;
    @ViewById
    SwipeDismissFrameLayout swipeDismiss;

    @AfterViews
    void afterViews() {
        getFragmentManager().beginTransaction().replace(R.id.container, AppListFragment_.builder().build()).commit();
        swipeDismiss.addCallback(new SwipeDismissFrameLayout.Callback() {
            @Override
            public void onDismissed(SwipeDismissFrameLayout layout) {
                super.onDismissed(layout);
                gotoClockActivity();
            }
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        if (LauncherApp.isMenuVisible) {
            gotoClockActivity();
            finish();
        } else {
            getFragmentManager().beginTransaction().replace(R.id.container, AppListFragment_.builder().build()).commit();
        }
    }

    private void gotoClockActivity() {
        Intent clockIntent = new Intent();
        clockIntent.setAction(Intent.ACTION_VIEW);
        clockIntent.setComponent(new ComponentName("com.google.android.wearable.app", "com.google.android.clockwork.home2.activity.HomeActivity2"));
        startActivity(clockIntent);
    }
}
