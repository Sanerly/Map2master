package com.vison.gmap;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

/**
 * @Author: Sanerly
 * @CreateDate: 2020/8/4 11:49
 * @Description: 类描述
 */
public class InfoWindowAdapter implements GoogleMap.InfoWindowAdapter{
    private Context mContext;

    public InfoWindowAdapter(Context context) {
        this.mContext = context;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return initView(marker);
    }


    @Override
    public View getInfoContents(Marker marker) {
        return initView(marker);
    }


    private View initView(Marker marker) {
       View view= LayoutInflater.from(mContext).inflate(R.layout.layout_info_window,null);
        TextView title=view.findViewById(R.id.tv_title);
        TextView message=view.findViewById(R.id.tv_message);
        if (TextUtils.isEmpty(marker.getTitle())){
            title.setVisibility(View.GONE);
        }
        title.setText(marker.getTitle());
        message.setText(marker.getSnippet());
        return view;
    }
}
