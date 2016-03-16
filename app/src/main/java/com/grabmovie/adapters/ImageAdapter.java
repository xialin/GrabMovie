package com.grabmovie.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.grabmovie.R;
import com.grabmovie.apis.GmAPIs;
import com.grabmovie.apis.Movie;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * Created by xialin on 16/3/16.
 * <p/>
 * http://developer.android.com/guide/topics/ui/layout/gridview.html
 */
public class ImageAdapter extends BaseAdapter {

    private ArrayList<Movie> data = new ArrayList<>();

    private LayoutInflater inflater;

    private DisplayImageOptions options;

    public ImageAdapter(Context context) {
        inflater = LayoutInflater.from(context);

        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.item_empty)
                .showImageForEmptyUri(R.drawable.item_empty)
                .showImageOnFail(R.drawable.error)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        View view = convertView;
        if (view == null) {
            view = inflater.inflate(R.layout.item_grid_image, parent, false);
            assert view != null;
            holder = new ViewHolder();
            holder.imageView = (ImageView) view.findViewById(R.id.image);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        // TODO:
        Movie movie = (Movie)getItem(position);
        String url = GmAPIs.getPosterUrl(movie.getPosterPath());
        ImageLoader.getInstance().displayImage(url, holder.imageView, options);

        return view;
    }

    public void pushData(ArrayList<Movie> movies){
        data.addAll(movies);
        notifyDataSetChanged();
    }

    static class ViewHolder {
        ImageView imageView;
    }
}
