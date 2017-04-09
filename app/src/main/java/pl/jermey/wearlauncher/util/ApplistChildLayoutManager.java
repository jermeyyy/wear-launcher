package pl.jermey.wearlauncher.util;

import android.content.Context;
import android.support.wearable.view.CurvedChildLayoutManager;
import android.support.wearable.view.WearableRecyclerView;
import android.view.View;

import pl.jermey.wearlauncher.R;

/**
 * Created by Jermey on 09.04.2017.
 */

public class ApplistChildLayoutManager extends CurvedChildLayoutManager {

    private static final float MAX_ICON_PROGRESS = 0.65f;

    public ApplistChildLayoutManager(Context context) {
        super(context);
    }

    @Override
    public void updateChild(View child, WearableRecyclerView parent) {
        if (child.getId() == R.id.voiceSearch) {
            child.setX(parent.getWidth() / 2 - child.getWidth() / 2);
            return;
        }
        super.updateChild(child, parent);
        // Figure out % progress from top to bottom
        float centerOffset = ((float) child.getHeight() / 2.0f) / (float) parent.getHeight();
        float yRelativeToCenterOffset = (child.getY() / parent.getHeight()) + centerOffset;

        // Normalize for center
        float mProgressToCenter = Math.abs(0.5f - yRelativeToCenterOffset);
        // Adjust to the maximum scale
        mProgressToCenter = Math.min(mProgressToCenter, MAX_ICON_PROGRESS);

        child.setScaleX(1 - mProgressToCenter);
        child.setScaleY(1 - mProgressToCenter);
        child.setTranslationX(-mProgressToCenter * parent.getWidth() / 2.5f);
    }
}