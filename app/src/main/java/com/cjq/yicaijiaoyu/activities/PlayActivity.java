package com.cjq.yicaijiaoyu.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StatFs;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cjq.yicaijiaoyu.CommonDataObject;
import com.cjq.yicaijiaoyu.R;
import com.cjq.yicaijiaoyu.adapter.PagerAdapter;
import com.cjq.yicaijiaoyu.dao.Comments;
import com.cjq.yicaijiaoyu.dao.Course;
import com.cjq.yicaijiaoyu.entities.AuthorityRequestEntity;
import com.cjq.yicaijiaoyu.entities.ChapterEntity;
import com.cjq.yicaijiaoyu.entities.ChapterRequestEntity;
import com.cjq.yicaijiaoyu.entities.ChapterRequestEvent;
import com.cjq.yicaijiaoyu.entities.CommentsRequestEntity;
import com.cjq.yicaijiaoyu.entities.CommentsRequestEvent;
import com.cjq.yicaijiaoyu.entities.CommentsResultEvent;
import com.cjq.yicaijiaoyu.entities.CourseInfoRequestEntity;
import com.cjq.yicaijiaoyu.entities.NeedVideoInfoEvent;
import com.cjq.yicaijiaoyu.entities.VideoEntity;
import com.cjq.yicaijiaoyu.entities.VideoInfoEvent;
import com.cjq.yicaijiaoyu.fragments.ChapterFragment;
import com.cjq.yicaijiaoyu.fragments.CommentsFragment;
import com.cjq.yicaijiaoyu.fragments.CourseInfoFragment;
import com.cjq.yicaijiaoyu.utils.AccountUtil;
import com.cjq.yicaijiaoyu.utils.CommentsUtil;
import com.cjq.yicaijiaoyu.utils.CourseHistoryUtil;
import com.cjq.yicaijiaoyu.utils.DialogUtil;
import com.cjq.yicaijiaoyu.utils.NetStateUtil;
import com.cjq.yicaijiaoyu.videoPlayer.DBservice;
import com.cjq.yicaijiaoyu.videoPlayer.DownloadInfo;
import com.cjq.yicaijiaoyu.videoPlayer.MediaController;
import com.easefun.polyvsdk.PolyvDownloadProgressListener;
import com.easefun.polyvsdk.PolyvDownloader;
import com.easefun.polyvsdk.PolyvSDKClient;
import com.easefun.polyvsdk.SDKUtil;
import com.easefun.polyvsdk.ijk.IjkVideoView;
import com.ypy.eventbus.EventBus;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayActivity extends BaseActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {
    public static final String ID = "id";
    private MediaController mediaController;
    private static int fragment_chosen = 2;
    private TextView detail;
    private TextView pinlun;
    private TextView chapter;
    private int nowCommentsPage=0;
    private View soap;
    private String lec_portrait;
    private String lec_name;
    private String lec_info;
    private String video_info;
    private FragmentManager manager;
    private ViewPager pager;
    private String id;
    private String vid;
    private IjkVideoView videoView;
    private ProgressBar progressBar;
    private DBservice service;
    private WindowManager wm;
    private int w;
    private int h;
    private RelativeLayout rl;
    //    private RelativeLayout botlayout;
    private static int stopPosition = 0;
    private static boolean isLandscape = false;
    private View rr;
    private View bottomBar;
    private String length;
    private BroadcastReceiver connectionReceiver;
    private PolyvDownloader downloader;
    private Handler mHandler=new Handler();
    private Course course;

    //详情请求回调
    public void onEventBackgroundThread(NeedVideoInfoEvent e) {
        //接到请求视频信息的请求
        EventBus.getDefault().post(new VideoInfoEvent(course));
    }

    //评论请求回调
    public void onEventBackgroundThread(CommentsRequestEvent e) {
        //1.加载评论缓存
        EventBus.getDefault().post(new CommentsResultEvent(course));
    }

    //章节请求回调
    public void onEventBackgroundThread(ChapterRequestEvent e) {
        //todo 发起章节请求

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (isLandscape) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        setContentView(R.layout.activity_play);

        initialRR();
        EventBus.getDefault().register(this);

        bottomBar = findViewById(R.id.bottom_bar);
        pager = (ViewPager) findViewById(R.id.content);
        rr = findViewById(R.id.rr);
        wm = this.getWindowManager();
        w = wm.getDefaultDisplay().getWidth();
        h = wm.getDefaultDisplay().getHeight();
        rl = (RelativeLayout) findViewById(R.id.rl);


        if (isLandscape) {
            RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(w, h);
            rl.setLayoutParams(p);
            bottomBar.setVisibility(View.GONE);
            pager.setVisibility(View.GONE);
            rr.setVisibility(View.GONE);
        }

        service = new DBservice(this);

        //注册back键
        findViewById(R.id.back).setOnClickListener(this);
        //注册关注键
        findViewById(R.id.add_to_favor).setOnClickListener(this);
        //注册评论键
        findViewById(R.id.write_comments).setOnClickListener(this);

        final Intent intent = getIntent();
        //获取goods_id
        id = intent.getStringExtra(ID);
//        首先可以获取到课程实体
        course = CourseHistoryUtil.getCourse(this,id);

        //开始请求详细信息
        StringRequest stringRequest  =  new StringRequest(Request.Method.POST, CommonDataObject.COURSE_INFO_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                System.out.println(s);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("opjson",CommonDataObject.GSON.toJson(new CourseInfoRequestEntity(CommonDataObject.COURSE_INFO_REQUEST_CODE,new CourseInfoRequestEntity.Data(course.getGoods_id(),course.getUserId()))));
                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(PlayActivity.this);
        queue.add(stringRequest);
        queue.start();

        lec_portrait = null;// intent.getStringExtra(LEC_PORT);
        lec_name = null;// intent.getStringExtra(LEC_NAME);
        lec_info = null;// intent.getStringExtra(LEC_INTRO);
        video_info = null;// intent.getStringExtra(INTRO);

        manager = getSupportFragmentManager();

        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new CourseInfoFragment());
        fragments.add(new CommentsFragment());
        fragments.add(new ChapterFragment());
        PagerAdapter adapter = new PagerAdapter(manager, fragments);
        pager.setAdapter(adapter);

        pager.addOnPageChangeListener(PlayActivity.this);

        //注册网络监听
        registerReceiver();

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                changeColor();
                changeFragment();
            }
        },500);
    }

    private void playVideo(String vid) {
        if(vid==null || "".equals(vid)){
            return;
        }
        if(videoView==null){
            videoView = (IjkVideoView) findViewById(R.id.videoview);
            progressBar = (ProgressBar) findViewById(R.id.loadingprogress);
            videoView.setMediaBufferingIndicator(progressBar);
            mediaController = new MediaController(PlayActivity.this, false);
            mediaController.setAnchorView(videoView);
            videoView.setMediaController(mediaController);
        }
        if (!PolyvDownloader.isVideoExists(vid, 1))
            DialogUtil.showDownloadDialog(PlayActivity.this, new Runnable() {
                @Override
                public void run() {
                    download();
                }
            });
        videoView.setVid(vid, 1);
        videoView.seekTo(stopPosition);

        //设置切屏事件
        mediaController.setOnBoardChangeListener(new MediaController.OnBoardChangeListener() {

            @Override
            public void onPortrait() {
                changeToLandscape();
            }

            @Override
            public void onLandscape() {
                changeToPortrait();
            }
        });

        mediaController.setOnVideoChangeListener(new MediaController.OnVideoChangeListener() {

            @Override
            public void onVideoChange(int layout) {
                videoView.setVideoLayout(layout);
                switch (layout) {
                    case IjkVideoView.VIDEO_LAYOUT_ORIGIN:
                        Toast.makeText(PlayActivity.this, "VIDEO_LAYOUT_ORIGIN", Toast.LENGTH_SHORT).show();
                        break;
                    case IjkVideoView.VIDEO_LAYOUT_SCALE:
                        Toast.makeText(PlayActivity.this, "VIDEO_LAYOUT_SCALE", Toast.LENGTH_SHORT).show();
                        break;
                    case IjkVideoView.VIDEO_LAYOUT_STRETCH:
                        Toast.makeText(PlayActivity.this, "VIDEO_LAYOUT_STRETCH", Toast.LENGTH_SHORT).show();
                        break;
                    case IjkVideoView.VIDEO_LAYOUT_ZOOM:
                        Toast.makeText(PlayActivity.this, "VIDEO_LAYOUT_ZOOM", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });

//        findViewById(R.id.AddTodownload).setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View arg0) {
//                // TODO Auto-generated method stub
//                String currentvid= videoview.getCurrentVideoId();
//                new VideoInfo().execute(currentvid);
//            }
//        });
//            videoView.setVid(vid);
//        videoView.setVid("ea307b2422ebe43ec2618ae1c8fa6c76_e");
    }

    private String getPath(String vid) {
        int range = vid.indexOf("_");
        String vpid = vid.substring(0, range);
        File mp4File = new File(PolyvSDKClient.getInstance().getDownloadDir(),
                vpid + "_" + 1 + ".mp4");
        if (mp4File.exists())
            return mp4File.getPath();
        else
            return null;
    }

    class VideoInfo extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            DownloadInfo downloadInfo = null;
            JSONArray jsonArray = SDKUtil.loadVideoInfo(params[0]);
            String vid = null;
            String duration = null;
            int filesize = 0;
            try {
                JSONObject jsonObject = jsonArray.getJSONObject(0);
                vid = jsonObject.getString("vid");
                duration = jsonObject.getString("duration");
                filesize = jsonObject.getInt("filesize1");

            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            //判断大小
            File dir = PolyvSDKClient.getInstance().getDownloadDir();
            if (!dir.exists())
                dir.mkdirs();
            StatFs statFs = new StatFs(Environment.getExternalStorageDirectory().getPath());
            long blocksize=statFs.getBlockSize();
//            long totalblocks=statFs.getBlockCount();
            long availableblocks=statFs.getAvailableBlocks();
            //判断容量
            long size = availableblocks*blocksize;

            if(filesize>size){
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(PlayActivity.this,getString(R.string.no_rom),Toast.LENGTH_SHORT).show();
                    }
                });
                return null;
                //容量不够，不进行下载，提示
            }
            downloader = new PolyvDownloader(vid, 1);
            downloader.start();

            downloader.setPolyvDownloadProressListener(new PolyvDownloadProgressListener() {
                @Override
                public void onDownloadSuccess() {
                    // TODO 保存
//                Log.i("aaa", "下载完成");
                }

                @Override
                public void onDownload(long current, long total) {
//                / TODO 显示进度
//                Message msg = new Message();
//                msg.what = DOWNLOAD;
//                Bundle bundle/ = new Bundle();
//                bundle.putLong("current", current);
//                bundle.putLong("total", total);
//                msg.setData(bundle);
//                handler.sendMessage(msg);
                }
                @Override
                public void onDownloadFail(String error) {
//                // TODO 清除下载缓存
//                Log.i("aaa", "下载失败 ："+error);
                }
            });

//            downloadInfo = new DownloadInfo(vid, duration, filesize);
//            if (service != null && !service.isAdd(downloadInfo)) {
//                service.addDownloadFile(downloadInfo);
//            } else {
//                runOnUiThread(new Runnable() {
//
//                    @Override
//                    public void run() {
//                        // TODO Auto-generated method stub
//                        Toast.makeText(PlayActivity.this, "this video has been added !!", Toast.LENGTH_SHORT).show();
//                    }
//                });
//            }
            return null;
        }
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        unRegisterReceiver();
        super.onDestroy();
    }

    private void initialRR() {
        soap = findViewById(R.id.soap);
        //初始化肥皂的长宽
        detail = (TextView) findViewById(R.id.detail);
        pinlun = (TextView) findViewById(R.id.pinlun);
        chapter = (TextView) findViewById(R.id.chapter);

        LinearLayout rr = (LinearLayout) findViewById(R.id.rrs);
        int count = rr.getChildCount();

        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) soap.getLayoutParams();

        int width = getResources().getDisplayMetrics().widthPixels;

        params.width = width / count;
        soap.setLayoutParams(params);

        //初始化三按钮
        detail.setTextColor(getResources().getColor(R.color.main_titlebar_background));

        detail.setOnClickListener(this);
        pinlun.setOnClickListener(this);
        chapter.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_play, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.detail:
                fragment_chosen = 0;
                changeColor();
                changeFragment();
                break;
            case R.id.pinlun:
                fragment_chosen = 1;
                changeColor();
                changeFragment();
                break;
            case R.id.chapter:
                fragment_chosen = 2;
                changeColor();
                changeFragment();
                break;
            case R.id.back:
                finish();
                break;
            case R.id.add_to_favor:
                // TODO: 2015/7/2 添加关注 应该先判断用户是否登录，并且登录了要判断用户是否关注该课程
//                if (AccountUtil.isLoggedIn(PlayActivity.this)) {
//                    StringRequest request = new StringRequest(Request.Method.POST, CommonDataObject.FAVORATE_URL, new Response.Listener<String>() {
//                        @Override
//                        public void onResponse(String s) {
//                            // TODO: 2015/7/2 根据回调改变关注图标的颜色
//                        }
//                    }, new Response.ErrorListener() {
//                        @Override
//                        public void onErrorResponse(VolleyError volleyError) {
//
//                        }
//                    }) {
//                        @Override
//                        protected Map<String, String> getParams() throws AuthFailureError {
//                            FavoriteEntity.Data data = new FavoriteEntity.Data(AccountUtil.getUserId(PlayActivity.this), id);
//                            // TODO: 2015/7/2 根据关注状态来设置请求码
//                            FavoriteEntity entity = new FavoriteEntity(CommonDataObject.ADD_FAVORATE_REQUEST_CODE, data);
//
//                            Map<String, String> params = new HashMap<>();
//                            params.put("opjson", CommonDataObject.GSON.toJson(entity));
//                            return super.getParams();
//                        }
//                    };
//
//                    RequestQueue queue = Volley.newRequestQueue(this);
//                    queue.add(request);
//                    queue.start();
//                }
                break;
            case R.id.write_comments:
                CommentsUtil.showCommentsActivity(this, id, AccountUtil.getUserId(this));
                break;
        }
    }

    /**
     * 改变头颜色的方法
     */
    private void changeColor() {
        //首先改变颜色值
        RelativeLayout.LayoutParams params;
        int left;
        switch (fragment_chosen) {
            case 0:
                //改变颜色值
                detail.setTextColor(getResources().getColor(R.color.main_titlebar_background));
                pinlun.setTextColor(getResources().getColor(R.color.menu_text_color));
                chapter.setTextColor(getResources().getColor(R.color.menu_text_color));
                //移动滑块
                params = (RelativeLayout.LayoutParams) soap.getLayoutParams();
                params.leftMargin = detail.getLeft();
                soap.setLayoutParams(params);
                break;
            case 1:
//改变颜色值
                pinlun.setTextColor(getResources().getColor(R.color.main_titlebar_background));
                detail.setTextColor(getResources().getColor(R.color.menu_text_color));
                chapter.setTextColor(getResources().getColor(R.color.menu_text_color));
                //移动滑块
                params = (RelativeLayout.LayoutParams) soap.getLayoutParams();
                params.leftMargin = pinlun.getLeft();

                soap.setLayoutParams(params);
                break;
            case 2:
//改变颜色值
                chapter.setTextColor(getResources().getColor(R.color.main_titlebar_background));
                pinlun.setTextColor(getResources().getColor(R.color.menu_text_color));
                detail.setTextColor(getResources().getColor(R.color.menu_text_color));
                //移动滑块
                params = (RelativeLayout.LayoutParams) soap.getLayoutParams();
                params.leftMargin = chapter.getLeft();

                soap.setLayoutParams(params);
                break;
        }
    }

    /**
     * 切换到fragment
     */
    private void changeFragment() {
        pager.setCurrentItem(fragment_chosen);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        fragment_chosen = position;
        changeColor();
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    //	   切换到横屏
    public void changeToLandscape() {
        stopPosition = videoView.getCurrentPosition();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        isLandscape = !isLandscape;
    }

    //	切换到竖屏
    public void changeToPortrait() {
        RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(w, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 200, getResources().getDisplayMetrics()));
        rl.setLayoutParams(p);
        stopPosition = videoView.getCurrentPosition();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        isLandscape = !isLandscape;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 0:
                if (RESULT_OK == resultCode) {
                    stopPosition = data.getIntExtra("stopPosition", 0);
                    videoView.seekTo(stopPosition);
                    videoView.start();
                }
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (isLandscape) {
                changeToPortrait();
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    public void download() {
        if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())){
            //判断大小
            File dir = PolyvSDKClient.getInstance().getDownloadDir();
            if (!dir.exists())
                dir.mkdirs();
            StatFs statFs = new StatFs(Environment.getExternalStorageDirectory().getPath());
            long blocksize=statFs.getBlockSize();
//            long totalblocks=statFs.getBlockCount();
            long availableblocks=statFs.getAvailableBlocks();
            //判断容量
            long size = availableblocks*blocksize;

            //todo 得到视频大小
//            if(filesize>size){
//                mHandler.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        Toast.makeText(PlayActivity.this,getString(R.string.no_rom),Toast.LENGTH_SHORT).show();
//                    }
//                });
//                return;
//                //容量不够，不进行下载，提示
//            }
            downloader = new PolyvDownloader(vid, 1);
            downloader.start();

            downloader.setPolyvDownloadProressListener(new PolyvDownloadProgressListener() {
                @Override
                public void onDownloadSuccess() {
                    // TODO 保存
//                Log.i("aaa", "下载完成");
                }

                @Override
                public void onDownload(long current, long total) {
//                / TODO 显示进度
//                Message msg = new Message();
//                msg.what = DOWNLOAD;
//                Bundle bundle/ = new Bundle();
//                bundle.putLong("current", current);
//                bundle.putLong("total", total);
//                msg.setData(bundle);
//                handler.sendMessage(msg);
                }
                @Override
                public void onDownloadFail(String error) {
//                // TODO 清除下载缓存
//                Log.i("aaa", "下载失败 ："+error);
                }
            });
        }
        // TODO: 2015/7/7 移植该方法到设置里面去 给评论加下拉刷新
        //downloader.deleteVideo(videoId, 1);
//删除下载目录
        //downloader.cleanDownloadDir();
    }

    private  void registerReceiver(){
        IntentFilter filter=new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        connectionReceiver= new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                //网络状态改变，发送网络状态改变的函数
                ConnectivityManager connectivityManager=(ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo mobNetInfo=connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
                NetworkInfo  wifiNetInfo=connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

                if(wifiNetInfo.isConnected()){
                    // TODO: 2015/7/8 wifi连接
                }else
                if(mobNetInfo.isConnected()){
                    // TODO: 2015/7/8 wifi 没连接，移动网络连接
                    videoView.pause();
                    if(downloader!=null)
                        downloader.stop();
                }else{
                    // TODO: 2015/7/8 都不连接
                }
            }
        };
        this.registerReceiver(connectionReceiver, filter);
    }

    private void unRegisterReceiver(){
        unregisterReceiver(connectionReceiver);
    }
}
