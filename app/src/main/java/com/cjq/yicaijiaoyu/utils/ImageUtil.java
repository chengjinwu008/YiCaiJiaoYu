package com.cjq.yicaijiaoyu.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Environment;
import android.util.LruCache;
import android.widget.ImageView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.cjq.yicaijiaoyu.R;
import com.jakewharton.disklrucache.DiskLruCache;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by android on 2015/6/24.
 */
public class ImageUtil {
    public static ImageLoader loader;

    public static void LoadImage(Context context,String url,ImageView imageView){
        if(url!=null)
        try {
            getImageLoader(context).get(url,getImageListener(imageView,R.drawable.guanggao,R.drawable.guanggao));
        } catch (IOException e) {
            e.printStackTrace();
        }
        else
            imageView.setImageResource(R.drawable.guanggao);
//        Picasso.with(context).load(url).placeholder(R.drawable.guanggao).into(imageView);
    }

    public static void LoadImage(Context context,int id,ImageView imageView){
        Picasso.with(context).load(id).placeholder(R.drawable.guanggao).into(imageView);
    }

    public static void LoadImage(Context context,File file,ImageView imageView){
        Picasso.with(context).load(file).placeholder(R.drawable.guanggao).into(imageView);
    }

    public static ImageLoader getImageLoader(Context context) throws IOException {
        if(loader==null){
            loader = new ImageLoader(Volley.newRequestQueue(context),new BitmapCache(context));
        }
        return loader;
    }

    public static ImageLoader.ImageListener getImageListener(ImageView view,int default_id,int fail_id){
        return ImageLoader.getImageListener(view,default_id,fail_id);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
    private static class BitmapCache implements ImageLoader.ImageCache{

        private LruCache<String,Bitmap> mCache;
        private DiskLruCache mDiskCache;

        public BitmapCache(Context context) throws IOException {
            int maxSize = 10*1024*1024;//采用运行内存的四分之一作为缓存
            mCache = new LruCache<String,Bitmap>(maxSize){
                @Override
                protected int sizeOf(String key, Bitmap value) {
                    return value.getRowBytes()*value.getHeight(); //bitmap的大小算法
                }
            };
            if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()))
            mDiskCache=DiskLruCache.open(new File(Environment.getExternalStorageDirectory().getPath()+"/Android/data/"+context.getPackageName()+"/cache"),VersionUtil.getVersionCode(context.getPackageManager(), context.getPackageName()),1,10*1024*1024);
        }

        @Override
        public Bitmap getBitmap(String s) {
            s = MessageDiagestUtil.MD5(s).toLowerCase();
            Bitmap res = mCache.get(s);
            if(res!=null){
                return res;
            }else{
                try {
                    DiskLruCache.Snapshot snapshot = mDiskCache.get(s);
                    if(snapshot==null)
                        return null;
                    InputStream in = snapshot.getInputStream(0);
                    Bitmap bitmap = BitmapFactory.decodeStream(in);
                    addToLru(s, bitmap);
                    return bitmap;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        private void addToLru(String s, Bitmap bitmap){
            s=MessageDiagestUtil.MD5(s).toLowerCase();
            mCache.put(s,bitmap);
        }

        private void addToDisk(String s,Bitmap bitmap) throws IOException {
            ByteArrayOutputStream bao = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG,100,bao);
            byte[] buff = bao.toByteArray();

            DiskLruCache.Editor editor = mDiskCache.edit(MessageDiagestUtil.MD5(s).toLowerCase());
            OutputStream out = editor.newOutputStream(0);
            out.write(buff);
            out.flush();
            out.close();
        }

        @Override
        public void putBitmap(final String s, final Bitmap bitmap) {
            new Thread(){
                @Override
                public void run() {
                    addToLru(s, bitmap);
                    try {
                        addToDisk(s, bitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }.start();
        }
    }
}
