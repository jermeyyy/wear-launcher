package pl.jermey.wearlauncher.items;

import android.os.Bundle;
import android.speech.SpeechRecognizer;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.zagum.speechrecognitionview.RecognitionProgressView;
import com.github.zagum.speechrecognitionview.adapters.RecognitionListenerAdapter;
import com.mikepenz.fastadapter.items.AbstractItem;

import java.util.ArrayList;
import java.util.List;

import pl.jermey.wearlauncher.R;
import pl.jermey.wearlauncher.util.AnimationListenerAdapter;
import pl.jermey.wearlauncher.util.ResizeAnimation;
import pl.jermey.wearlauncher.util.VoiceSearchInterface;


/**
 * Created by Jermey on 09.04.2017.
 */

public class SearchItem extends AbstractItem<SearchItem, SearchItem.ViewHolder> {

    private static final String TAG = SearchItem.class.getSimpleName();
    private VoiceSearchInterface searchInterface;
    private String query;

    public SearchItem(VoiceSearchInterface searchInterface) {
        this.searchInterface = searchInterface;
    }

    @Override
    public ViewHolder getViewHolder(View v) {
        return new ViewHolder(v);
    }

    @Override
    public int getType() {
        return R.id.search_item;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.search_item;
    }

    @Override
    public void bindView(ViewHolder holder, List<Object> payloads) {
        super.bindView(holder, payloads);
        int[] heights = {40, 56, 38, 60, 35};
        int[] colors = {
                ContextCompat.getColor(holder.itemView.getContext(), R.color.color1),
                ContextCompat.getColor(holder.itemView.getContext(), R.color.color2),
                ContextCompat.getColor(holder.itemView.getContext(), R.color.color3),
                ContextCompat.getColor(holder.itemView.getContext(), R.color.color4),
                ContextCompat.getColor(holder.itemView.getContext(), R.color.color5)
        };
        holder.recognitionView.setColors(colors);
        holder.recognitionView.setBarMaxHeightsInDp(heights);
        holder.recognitionView.setSpeechRecognizer(searchInterface.getSpeechRecognizer());
        holder.recognitionView.setRecognitionListener(new RecognitionListenerAdapter() {

            @Override
            public void onEndOfSpeech() {
                super.onEndOfSpeech();
                searchInterface.getSpeechRecognizer().stopListening();
                holder.recognitionView.stop();
                collapseSearchView(holder, null);
            }

            @Override
            public void onResults(Bundle results) {
                ArrayList<String> speechResults = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                if (speechResults != null && !speechResults.isEmpty()) {
                    searchInterface.getSpeechRecognizer().stopListening();
                    query = speechResults.get(0);
                    searchInterface.onSearch(query);
                    collapseSearchView(holder, query);
                } else {
                    searchInterface.getSpeechRecognizer().stopListening();
                    collapseSearchView(holder, null);
                }
            }
        });
        holder.recognitionView.play();
        holder.searchIcon.setOnClickListener(v -> {
            searchInterface.getSpeechRecognizer().startListening(searchInterface.getSpeechIntent());
            expandSearchView(holder);
        });
        holder.closeIcon.setOnClickListener(v -> {
            query = "";
            collapseSearchView(holder, null);
            searchInterface.onSearch(query);
        });
        holder.recognitionView.setOnClickListener(v -> {
            searchInterface.getSpeechRecognizer().stopListening();
            collapseSearchView(holder, null);
        });
    }

    private void expandSearchView(final ViewHolder holder) {
        int targetHeight = (int) (holder.itemView.getResources().getDisplayMetrics().scaledDensity * 96f);
        ResizeAnimation resizeAnimation = new ResizeAnimation(holder.itemView, targetHeight, holder.itemView.getHeight());
        resizeAnimation.setDuration(150);
        resizeAnimation.setAnimationListener(new AnimationListenerAdapter() {
            @Override
            public void onAnimationEnd(Animation animation) {
                holder.searchIcon.setVisibility(View.GONE);
                holder.recognitionView.setVisibility(View.VISIBLE);
                holder.queryText.setVisibility(View.VISIBLE);
                holder.queryText.setText("Search app...");
            }
        });
        holder.itemView.startAnimation(resizeAnimation);
    }

    private void collapseSearchView(final ViewHolder holder, String queryToShow) {
        holder.recognitionView.setVisibility(View.GONE);

        if (TextUtils.isEmpty(query)) {
            holder.queryText.setVisibility(View.GONE);
        } else {
            holder.queryText.setText(query);
        }

        int targetHeight = (int) (holder.itemView.getResources().getDisplayMetrics().scaledDensity * 56f);
        ResizeAnimation resizeAnimation = new ResizeAnimation(holder.itemView, targetHeight, holder.itemView.getHeight());
        resizeAnimation.setDuration(150);
        resizeAnimation.setAnimationListener(new AnimationListenerAdapter() {
            @Override
            public void onAnimationEnd(Animation animation) {
                holder.searchIcon.setVisibility(View.VISIBLE);
            }
        });
        holder.itemView.startAnimation(resizeAnimation);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView searchIcon;
        ImageView closeIcon;
        RecognitionProgressView recognitionView;
        TextView queryText;

        public ViewHolder(View itemView) {
            super(itemView);
            searchIcon = (ImageView) itemView.findViewById(R.id.searchIcon);
            closeIcon = (ImageView) itemView.findViewById(R.id.closeIcon);
            recognitionView = (RecognitionProgressView) itemView.findViewById(R.id.recognitionView);
            queryText = (TextView) itemView.findViewById(R.id.queryText);
        }
    }
}
