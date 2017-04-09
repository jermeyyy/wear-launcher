package pl.jermey.wearlauncher.items;

import android.support.v7.widget.RecyclerView;
import android.support.wearable.view.CircledImageView;
import android.view.View;
import android.widget.TextView;

import com.mikepenz.fastadapter.items.AbstractItem;

import java.util.List;

import pl.jermey.wearlauncher.R;
import pl.jermey.wearlauncher.model.AppDetail;

/**
 * Created by Jermey on 08.04.2017.
 */

public class AppItem extends AbstractItem<AppItem, AppItem.ViewHolder> {

    public AppDetail appDetail;

    public AppItem(AppDetail appDetail) {
        this.appDetail = appDetail;
    }

    @Override
    public int getType() {
        return R.id.app_item;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.app_item;
    }

    @Override
    public void bindView(ViewHolder holder, List<Object> payloads) {
        super.bindView(holder, payloads);
        if (appDetail == null) {
            holder.name.setText("");
            holder.icon.setImageDrawable(null);
            return;
        }
        holder.name.setText(appDetail.label);
        holder.icon.setImageDrawable(appDetail.icon);
    }

    @Override
    public ViewHolder getViewHolder(View v) {
        return new ViewHolder(v);
    }

    protected static class ViewHolder extends RecyclerView.ViewHolder {
        protected TextView name;
        protected CircledImageView icon;

        public ViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.name);
            icon = (CircledImageView) itemView.findViewById(R.id.icon);
        }
    }
}
