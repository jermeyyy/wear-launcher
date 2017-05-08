package pl.jermey.wearlauncher.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.wearable.view.WearableRecyclerView;
import android.util.Log;

import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import pl.jermey.wearlauncher.LauncherApp;
import pl.jermey.wearlauncher.R;
import pl.jermey.wearlauncher.items.AppItem;
import pl.jermey.wearlauncher.items.SearchItem;
import pl.jermey.wearlauncher.model.AppDetail;
import pl.jermey.wearlauncher.util.ApplistChildLayoutManager;
import pl.jermey.wearlauncher.util.VoiceSearchInterface;

/**
 * Created by Jermey on 09.04.2017.
 */

@SuppressWarnings("unchecked")
@EFragment(R.layout.app_list_fragment)
public class AppListFragment extends Fragment implements VoiceSearchInterface {

    private static final String TAG = AppListFragment.class.getSimpleName();

    @ViewById
    WearableRecyclerView appList;

    private FastItemAdapter adapter;
    private SpeechRecognizer speechRecognizer;

    @AfterViews
    void afterViews() {
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(getActivity());
        setupAppList();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (speechRecognizer != null) {
            speechRecognizer.destroy();
        }
    }

    @Override
    public void onSearch(String query) {
        Log.d(TAG, "onSearch: " + query);
        adapter.getItemFilter().filter(query, count -> {
            if (count == 2) {
                openApp(((AppItem) adapter.getAdapterItem(1)).appDetail);
            }
        });
        appList.getLayoutManager().scrollToPosition(0);
    }

    @Override
    public SpeechRecognizer getSpeechRecognizer() {
        return speechRecognizer;
    }

    @Override
    public Intent getSpeechIntent() {
        Intent speechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getActivity().getPackageName());
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_COMPLETE_SILENCE_LENGTH_MILLIS, 1200);
        return speechRecognizerIntent;
    }

    void openApp(AppDetail appDetail) {
        Intent i = getContext().getPackageManager().getLaunchIntentForPackage(appDetail.name.toString());
        startActivity(i);
    }

    void setupAppList() {
        appList.setLayoutManager(new ApplistChildLayoutManager(getActivity()));
        adapter = new FastItemAdapter<>();
        adapter.withPositionBasedStateManagement(false);
        adapter.withFilterPredicate((item, constraint) -> {
            if (item instanceof AppItem)
                if (((AppItem) item).appDetail == null)
                    return true;
                else
                    return !constraint.toString().toLowerCase().contains(((AppItem) item).appDetail.label.toString().toLowerCase());
            else if (item instanceof SearchItem)
                return false;
            else
                return true;
        });
        adapter.withOnClickListener((v, adapter1, item, position) -> {
            if (item instanceof AppItem) {
                AppDetail appDetail = ((AppItem) item).appDetail;
                if (appDetail == null)
                    return false;
                openApp(appDetail);
                return true;
            } else {
                return false;
            }
        });
        adapter.add(new SearchItem(this));
        for (AppDetail appDetail : LauncherApp.appList) {
            adapter.add(new AppItem(appDetail));
        }
        adapter.add(new AppItem(null));
        appList.setAdapter(adapter);
    }
}
