package com.cjq.yicaijiaoyu.utils;

import android.content.Context;
import android.widget.ImageView;

import com.cjq.yicaijiaoyu.R;
import com.squareup.picasso.Picasso;

import java.io.File;

/**
 * Created by android on 2015/6/24.
 */
public class ImageUtil {
    public static void LoadImage(Context context,String url,ImageView imageView){
        Picasso.with(context).load(url).placeholder(R.drawable.loading).into(imageView);
    }

    public static void LoadImage(Context context,int id,ImageView imageView){
        Picasso.with(context).load(id).placeholder(R.drawable.loading).into(imageView);
    }

    public static void LoadImage(Context context,File file,ImageView imageView){
        Picasso.with(context).load(file).placeholder(R.drawable.loading).into(imageView);
    }
}
