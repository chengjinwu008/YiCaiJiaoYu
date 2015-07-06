package com.cjq.yicaijiaoyu.activities;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
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
import com.cjq.yicaijiaoyu.adapter.ChapterAdapter;
import com.cjq.yicaijiaoyu.adapter.CommentsAdapter;
import com.cjq.yicaijiaoyu.adapter.PagerAdapter;
import com.cjq.yicaijiaoyu.entities.AuthorityRequestEntity;
import com.cjq.yicaijiaoyu.entities.ChapterEntity;
import com.cjq.yicaijiaoyu.entities.ChapterRequestEvent;
import com.cjq.yicaijiaoyu.entities.ChapterResultEvent;
import com.cjq.yicaijiaoyu.entities.CommentsEntity;
import com.cjq.yicaijiaoyu.entities.CommentsRequestEvent;
import com.cjq.yicaijiaoyu.entities.CommentsResultEvent;
import com.cjq.yicaijiaoyu.entities.CourseEntity;
import com.cjq.yicaijiaoyu.entities.FavoriteEntity;
import com.cjq.yicaijiaoyu.entities.NeedVideoInfoEvent;
import com.cjq.yicaijiaoyu.entities.UserEntity;
import com.cjq.yicaijiaoyu.entities.VideoInfoEvent;
import com.cjq.yicaijiaoyu.entities.VideoInfoRequestEntity;
import com.cjq.yicaijiaoyu.fragments.ChapterFragment;
import com.cjq.yicaijiaoyu.fragments.CommentsFragment;
import com.cjq.yicaijiaoyu.fragments.CourseInfoFragment;
import com.cjq.yicaijiaoyu.utils.AccountUtil;
import com.cjq.yicaijiaoyu.utils.DialogUtil;
import com.cjq.yicaijiaoyu.utils.NetStateUtil;
import com.cjq.yicaijiaoyu.videoPlayer.DBservice;
import com.cjq.yicaijiaoyu.videoPlayer.DownloadInfo;
import com.cjq.yicaijiaoyu.videoPlayer.MediaController;
import com.easefun.polyvsdk.SDKUtil;
import com.easefun.polyvsdk.ijk.IjkVideoView;
import com.ypy.eventbus.EventBus;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayActivity extends BaseActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {

    public static final String URL = "url";
    public static final String TITLE = "title";
    public static final String PROGRESS = "progress";
    public static final String LEC_NAME = "lectureName";
    public static final String LEC_INTRO = "lectureIntro";
    public static final String LEC_PORT = "lecturePortrait";
    public static final String INTRO = "intro";
    public static final String ID = "id";
    public static final String FREE = "free";
    private MediaController mediaController;
    private static int fragment_chosen = 2;
    private TextView detail;
    private TextView pinlun;
    private TextView chapter;
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
    private static int stopPosition=0;
    private static boolean isLandscape=false;
    private View rr;
    private View bottomBar;

    //详情请求回调
    public void onEventBackgroundThread(NeedVideoInfoEvent e) {
        //接到请求视频信息的请求
        sendInfo();
    }

    private void sendInfo() {
        VideoInfoEvent event = new VideoInfoEvent(lec_portrait, lec_name, lec_info, video_info);

        EventBus.getDefault().post(event);
    }

    //评论请求回调
    public void onEventBackgroundThread(CommentsRequestEvent e) {
        //todo 发起对视频评论的请求
        CommentsEntity commentsEntity1 = new CommentsEntity(new Date().getTime(), new UserEntity("https://www.baidu.com/img/bd_logo1.png", "陈昶", "12"), "总之这个就是评论了，咋地吧");
        CommentsEntity commentsEntity2 = new CommentsEntity(new Date().getTime(), new UserEntity("https://www.baidu.com/img/bd_logo1.png", "陈昶", "12"), "总之这个就是评论了，咋地吧");
        List<CommentsEntity> commentsEntities = new ArrayList<>();
        commentsEntities.add(commentsEntity1);
        commentsEntities.add(commentsEntity2);

        CommentsAdapter adapter = new CommentsAdapter(commentsEntities, this);
        CommentsResultEvent event = new CommentsResultEvent(adapter);

        EventBus.getDefault().post(event);
    }

    //章节请求回调
    public void onEventBackgroundThread(ChapterRequestEvent e) {
        //todo 发起章节请求
        ChapterEntity chapter1 = new ChapterEntity("第一张，我就呵呵了");
        ChapterEntity chapter2 = new ChapterEntity("第二章，没有下文");
        ChapterEntity chapter3 = new ChapterEntity("第三章，都说了没下文了……");

        List<CourseEntity> courses = new ArrayList<>();
        courses.add(new CourseEntity("sl8da4jjbx5d715bc3a8ce8f8194afab_s", "再见今天", "3:30"));
        courses.add(new CourseEntity("sl8da4jjbx5d715bc3a8ce8f8194afab_s", "再见明天", "3:30"));
        courses.add(new CourseEntity("sl8da4jjbx5d715bc3a8ce8f8194afab_s", "再见后天", "3:30"));

        chapter1.setVideos(courses);

        List<ChapterEntity> chapters = new ArrayList<>();
        chapters.add(chapter1);
        chapters.add(chapter2);
        chapters.add(chapter3);

        //构建adapter
        ChapterAdapter adapter = new ChapterAdapter(chapters, this);

        //发送adapter
        EventBus.getDefault().post(new ChapterResultEvent(adapter));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(isLandscape){
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }

        setContentView(R.layout.activity_play);

        initialRR();
        EventBus.getDefault().register(this);

        bottomBar = findViewById(R.id.bottom_bar);
        pager = (ViewPager) findViewById(R.id.content);
        rr=findViewById(R.id.rr);
        wm = this.getWindowManager();
        w = wm.getDefaultDisplay().getWidth();
        h = wm.getDefaultDisplay().getHeight();
        rl = (RelativeLayout) findViewById(R.id.rl);


        if(isLandscape){
            RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(w,h);
            rl.setLayoutParams(p);
            bottomBar.setVisibility(View.GONE);
            pager.setVisibility(View.GONE);
            rr.setVisibility(View.GONE);
        }
//        botlayout = (RelativeLayout)findViewById(R.id.botlayout);

        service=new DBservice(this);

        //注册back键
        findViewById(R.id.back).setOnClickListener(this);
        //注册关注键
        findViewById(R.id.add_to_favor).setOnClickListener(this);
        //注册评论键
        findViewById(R.id.write_comments).setOnClickListener(this);

        final Intent intent = getIntent();
        id = intent.getStringExtra(ID);
        final boolean free = intent.getBooleanExtra(FREE, false);
        CommonDataObject.nowPlayingId = id;

        //判断网络
        NetStateUtil.checkNetwork(new NetStateUtil.NetWorkStateListener() {
            @Override
            public void doWithNetWork() {
                if (free) {
                    //免费的不用判断
                    getCourseInfo();
                } else {
                    //判断登录
                    if (AccountUtil.isLoggedIn(PlayActivity.this)) {
                        //登录了
                        checkAuthority();
                    } else {
                        //没登录
                        DialogUtil.showLoginAlert(PlayActivity.this);
                    }
                }
            }

            @Override
            public void doWithoutNetWork() {
                Toast.makeText(PlayActivity.this, R.string.no_network, Toast.LENGTH_SHORT).show();
                DialogUtil.showNetWorkDialog(PlayActivity.this, new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                });
            }
        });

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

    }

    private void checkAuthority() {
        // TODO: 2015/7/3 检查权限
        StringRequest request = new StringRequest(Request.Method.POST, CommonDataObject.AUTHORITY_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject object = new JSONObject(s);
                    if ("0000".equals(object.getString("code"))) {
                        boolean authority;
                        if (object.getJSONObject("data").has("dataBack"))
                            authority = object.getJSONObject("data").getInt("dataBack") == 1;
                        else
                            authority = object.getJSONObject("data").getInt("head_seek") == 1;
                        if (authority) {
                            //有权限
                            getCourseInfo();
                        } else {
                            //没有权限
                            DialogUtil.showPayDialog(PlayActivity.this, new Runnable() {
                                @Override
                                public void run() {
                                    finish();
                                }
                            });
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                AuthorityRequestEntity.Data data = new AuthorityRequestEntity.Data(id, AccountUtil.getUserId(PlayActivity.this));
                AuthorityRequestEntity entity = new AuthorityRequestEntity(CommonDataObject.AUTHORITY_REQUEST_CODE, data);
                Map<String, String> params = new HashMap<String, String>();
                params.put("opjson", CommonDataObject.GSON.toJson(entity));
                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(PlayActivity.this);
        queue.add(request);
        queue.start();
    }

    private void getCourseInfo() {
        // TODO: 2015/7/3 进行请求并回显
        StringRequest request = new StringRequest(Request.Method.POST, CommonDataObject.VIDEO_INFO_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject o = new JSONObject(s);
                    if ("0000".equals(o.getString("code"))) {
                        JSONObject data = o.getJSONObject("data").getJSONObject("data");
                        lec_portrait = CommonDataObject.MAIN_URL + data.getString("tea_img");
                        lec_name = data.getString("tea_name");
                        lec_info = data.getString("tea_desc");
                        video_info = data.getString("goods_decs");

                        vid = data.getString("goods_vid");
                        playVideo();
                        sendInfo();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                VideoInfoRequestEntity.Data data = new VideoInfoRequestEntity.Data(id, AccountUtil.getUserId(PlayActivity.this));
                VideoInfoRequestEntity entity = new VideoInfoRequestEntity(CommonDataObject.VIDEO_INFO_REQUEST_CODE, data);

                Map<String, String> params = new HashMap<>();
                params.put("opjson", CommonDataObject.GSON.toJson(entity));

                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(PlayActivity.this);
        queue.add(request);
        queue.start();
    }

    private void playVideo() {
        // TODO: 2015/7/3 播放
//        String path = getPath(vid);

        videoView = (IjkVideoView) findViewById(R.id.videoview);
        progressBar = (ProgressBar) findViewById(R.id.loadingprogress);
        videoView.setMediaBufferingIndicator(progressBar);
        mediaController = new MediaController(PlayActivity.this, false);
        mediaController.setAnchorView(videoView);
        videoView.setMediaController(mediaController);
//        if(path != null && path.length() > 0){
//            progressBar.setVisibility(View.GONE);
//            videoView.setVideoPath(path);
//
//        }else{
        if(isLandscape){
            videoView.setVid("ea307b2422ebe43ec2618ae1c8fa6c76_e",2);
        }else{
            videoView.setVid("ea307b2422ebe43ec2618ae1c8fa6c76_e",1);
        }

            videoView.seekTo(stopPosition);
//        }

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

    class VideoInfo extends AsyncTask<String,String,String> {
        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            DownloadInfo downloadInfo=null;
            JSONArray jsonArray = SDKUtil.loadVideoInfo(params[0]);
            String vid = null;
            String duration = null;
            int filesize = 0;
            try {
                JSONObject jsonObject =jsonArray.getJSONObject(0);
                vid = jsonObject.getString("vid");
                duration = jsonObject.getString("duration");
                filesize = jsonObject.getInt("filesize1");
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            downloadInfo = new DownloadInfo(vid, duration, filesize);
            if(service!=null&&!service.isAdd(downloadInfo)){
                service.addDownloadFile(downloadInfo);
            }else{
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        Toast.makeText(PlayActivity.this, "this video has been added !!", Toast.LENGTH_SHORT).show();
                    }
                });

            }
            return null;
        }
    }

    @Override
    protected void onDestroy() {
        CommonDataObject.nowPlayingId = null;
        EventBus.getDefault().unregister(this);
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
                if (AccountUtil.isLoggedIn(PlayActivity.this)) {
                    StringRequest request = new StringRequest(Request.Method.POST, CommonDataObject.FAVORATE_URL, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String s) {
                            // TODO: 2015/7/2 根据回调改变关注图标的颜色
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {

                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            FavoriteEntity.Data data = new FavoriteEntity.Data(AccountUtil.getUserId(PlayActivity.this), id);
                            // TODO: 2015/7/2 根据关注状态来设置请求码
                            FavoriteEntity entity = new FavoriteEntity(CommonDataObject.ADD_FAVORATE_REQUEST_CODE, data);

                            Map<String, String> params = new HashMap<>();
                            params.put("opjson", CommonDataObject.GSON.toJson(entity));
                            return super.getParams();
                        }
                    };

                    RequestQueue queue = Volley.newRequestQueue(this);
                    queue.add(request);
                    queue.start();
                }
                break;
            case R.id.write_comments:
                // TODO: 2015/7/2 撰写评论
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
    public void changeToLandscape(){
        stopPosition = videoView.getCurrentPosition();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        isLandscape = !isLandscape;
    }

    //	切换到竖屏
    public void changeToPortrait(){
        RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(w, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,200,getResources().getDisplayMetrics()));
        rl.setLayoutParams(p);
        stopPosition = videoView.getCurrentPosition();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        isLandscape = !isLandscape;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case 0:
                if(RESULT_OK==resultCode){
                    stopPosition = data.getIntExtra("stopPosition",0);
                    videoView.seekTo(stopPosition);
                    videoView.start();
                }
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            if(isLandscape){
                changeToPortrait();
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
