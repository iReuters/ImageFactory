/* 
 * @ProjectName VideoGo
 * @Copyright null
 * 
 * @FileName RealPlayActivity.java
 * @Description 这里对文件进行描述
 * 
 * @author chenxingyf1
 * @data 2014-6-11
 * 
 * @note 这里写本文件的详细功能描述和注释
 * @note 历史记录
 * 
 * @warning 这里写本文件的相关警告
 */
package com.videogo.ui.realplay;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.speech.tts.TextToSpeech;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.arcsoft.arcfacedemo.common.Constants;
import com.arcsoft.face.AgeInfo;
import com.arcsoft.face.FaceEngine;
import com.arcsoft.face.FaceFeature;
import com.arcsoft.face.FaceInfo;
import com.arcsoft.face.FaceSimilar;
import com.arcsoft.face.GenderInfo;
import com.arcsoft.face.enums.DetectFaceOrientPriority;
import com.arcsoft.face.enums.DetectMode;
import com.arcsoft.face.util.ImageUtils;
import com.arcsoft.imageutil.ArcSoftImageFormat;
import com.arcsoft.imageutil.ArcSoftImageUtil;
import com.arcsoft.imageutil.ArcSoftImageUtilError;
import com.baidu.aip.bodyanalysis.AipBodyAnalysis;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.videogo.EzvizApplication;
import com.videogo.facedetection.GetPersonNameAlertDialog;
import com.videogo.facedetection.PersonInfo;
import com.videogo.facedetection.PersonInfoAdapter;
import com.videogo.RootActivity;
import com.videogo.constant.Config;
import com.videogo.constant.Constant;
import com.videogo.constant.IntentConsts;
import com.videogo.errorlayer.ErrorInfo;
import com.videogo.exception.BaseException;
import com.videogo.exception.ErrorCode;
import com.videogo.exception.InnerException;
import com.videogo.facedetection.UnRegisteredPersonInfo;
import com.videogo.openapi.EZConstants;
import com.videogo.openapi.EZConstants.EZPTZAction;
import com.videogo.openapi.EZConstants.EZPTZCommand;
import com.videogo.openapi.EZConstants.EZRealPlayConstants;
import com.videogo.openapi.EZConstants.EZVideoLevel;
import com.videogo.openapi.EZOpenSDKListener;
import com.videogo.openapi.EZPlayer;
import com.videogo.openapi.bean.EZCameraInfo;
import com.videogo.openapi.bean.EZDeviceInfo;
import com.videogo.openapi.bean.EZVideoQualityInfo;
import com.videogo.realplay.RealPlayStatus;
import com.videogo.ui.cameralist.EZCameraListActivity;
import com.videogo.ui.common.ScreenOrientationHelper;
import com.videogo.ui.util.ActivityUtils;
import com.videogo.ui.util.AudioPlayUtil;
import com.videogo.ui.util.DataManager;
import com.videogo.ui.util.EZUtils;
import com.videogo.ui.util.VerifyCodeInput;
import com.videogo.util.ConnectionDetector;
import com.videogo.util.LocalInfo;
import com.videogo.util.LogUtil;
import com.videogo.util.MediaScanner;
import com.videogo.util.RotateViewUtil;
import com.videogo.util.SDCardUtil;
import com.videogo.util.Utils;
import com.videogo.widget.CheckTextButton;
import com.videogo.widget.CustomRect;
import com.videogo.widget.CustomTouchListener;
import com.videogo.widget.RingView;
import com.videogo.widget.TitleBar;
import com.videogo.widget.WaitDialog;
import com.videogo.widget.loading.LoadingTextView;


import org.MediaPlayer.PlayM4.Player;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import ezviz.ezopensdk.debug.VideoFileUtil;
import ezviz.ezopensdk.demo.DemoConfig;
import ezviz.ezopensdk.R;
import io.github.lijunguan.imgselector.ImageSelector;
import openpose.Classifier;
import openpose.Common;
import openpose.TensorFlowPoseDetector;
import openpose.env.Logger;


import static com.videogo.openapi.EZConstants.MSG_GOT_STREAM_TYPE;
import static com.videogo.openapi.EZConstants.MSG_VIDEO_SIZE_CHANGED;

public class EZRealPlayActivity extends RootActivity implements OnClickListener, SurfaceHolder.Callback,
        Handler.Callback, OnTouchListener, VerifyCodeInput.VerifyCodeInputListener, TextToSpeech.OnInitListener {
    private static final String TAG = EZRealPlayActivity.class.getSimpleName();

    private static final int ANIMATION_DURING_TIME = 500;

    // UI消息
    public static final int MSG_PLAY_UI_UPDATE = 200;

    public static final int MSG_AUTO_START_PLAY = 202;

    public static final int MSG_CLOSE_PTZ_PROMPT = 203;

    public static final int MSG_HIDE_PTZ_DIRECTION = 204;

    public static final int MSG_HIDE_PAGE_ANIM = 205;

    public static final int MSG_PLAY_UI_REFRESH = 206;

    public static final int MSG_PREVIEW_START_PLAY = 207;

    public static final int MSG_SET_VEDIOMODE_SUCCESS = 105;

    public static final int MSG_SET_VEDIOMODE_FAIL = 106;

    private static final Logger LOGGER = new Logger();

    private static final int MP_INPUT_SIZE = 368;
    private static final String MP_INPUT_NAME = "image";
    private static final String MP_OUTPUT_L1 = "Openpose/MConv_Stage6_L1_5_pointwise/BatchNorm/FusedBatchNorm";
    private static final String MP_OUTPUT_L2 = "Openpose/MConv_Stage6_L2_5_pointwise/BatchNorm/FusedBatchNorm";
    private static final String MP_MODEL_FILE = "file:///android_asset/frozen_person_model.pb";

    private String mRtspUrl = null;
    private RealPlaySquareInfo mRealPlaySquareInfo = null;

    private AudioPlayUtil mAudioPlayUtil = null;
    private LocalInfo mLocalInfo = null;
    private Handler mHandler = null;

    private int dispWidth;
    private int dispHeight;
    private float mRealRatio = Constant.LIVE_VIEW_RATIO;
    private int mStatus = RealPlayStatus.STATUS_INIT;
    private boolean mIsOnStop = false;
    private int mOrientation = Configuration.ORIENTATION_PORTRAIT;
    private int mForceOrientation = 0;
    private Rect mRealPlayRect = null;

    private LinearLayout mRealPlayPageLy = null;
    private TitleBar mPortraitTitleBar = null;
    private TitleBar mLandscapeTitleBar = null;
    private Button mTiletRightBtn = null;
    private RelativeLayout mRealPlayPlayRl = null;

    private SurfaceView mRealPlaySv = null;
    private SurfaceHolder mRealPlaySh = null;
    private CustomTouchListener mRealPlayTouchListener = null;


    private RelativeLayout mRealPlayLoadingRl;
    private TextView mRealPlayTipTv;
    private ImageView mRealPlayPlayIv;
    private ImageView imageView;
    private ImageView faceRect;
    private LoadingTextView mRealPlayPlayLoading;
    private LinearLayout mRealPlayPlayPrivacyLy;
    private ImageView mPageAnimIv = null;
    private AnimationDrawable mPageAnimDrawable = null;

    private LinearLayout mRealPlayControlRl = null;
    private ImageButton mRealPlayBtn = null;
    private ImageButton mRealPlaySoundBtn = null;
    private TextView mRealPlayFlowTv = null;
    private int mControlDisplaySec = 0;

    // 播放比例 Play ratio
    private float mPlayScale = 1;

    private RelativeLayout mRealPlayCaptureRl = null;
    private LayoutParams mRealPlayCaptureRlLp = null;
    private ImageView mRealPlayCaptureIv = null;
    private ImageView mRealPlayCaptureWatermarkIv = null;
    private int mCaptureDisplaySec = 0;
    private LinearLayout mRealPlayRecordLy = null;
    private ImageView mRealPlayRecordIv = null;
    private TextView mRealPlayRecordTv = null;

    private boolean isRecording = false;
    private String mRecordTime = null;
    private int mRecordSecond = 0;

    private HorizontalScrollView mRealPlayOperateBar = null;

    private LinearLayout mRealPlayPtzBtnLy = null;
    private LinearLayout mRealPlayTalkBtnLy = null;
    private LinearLayout mRealPlaySslBtnLy = null;
    //    private LinearLayout mRealPlayPrivacyBtnLy = null;
    private LinearLayout mRealPlayCaptureBtnLy = null;
    private LinearLayout mRealPlayRecordContainerLy = null;

    private ImageButton mRealPlayPtzBtn = null;
    private ImageButton mRealPlayTalkBtn = null;
    private Button mRealPlaySslBtn = null;
    private ImageButton mRealPlayPrivacyBtn = null;
    private ImageButton mRealPlayCaptureBtn = null;
    private ImageButton mRealPlayIntelligentDetectionBtn = null;
    private View mRealPlayRecordContainer = null;

    private ImageButton mRealPlayRecordBtn = null;
    private ImageButton mRealPlayRecordStartBtn = null;
    private RotateViewUtil mRecordRotateViewUtil = null;

    private Button mRealPlayQualityBtn = null;

    private RelativeLayout mRealPlayFullOperateBar = null;
    private ImageButton mRealPlayFullPlayBtn = null;
    private ImageButton mRealPlayFullSoundBtn = null;
    private ImageButton mRealPlayFullTalkBtn = null;
    private ImageButton mRealPlayFullCaptureBtn = null;
    private ImageButton mRealPlayFullPtzBtn = null;
    private ImageButton mRealPlayFullRecordBtn = null;
    private ImageButton mRealPlayFullRecordStartBtn = null;
    private View mRealPlayFullRecordContainer = null;
    private LinearLayout mRealPlayFullFlowLy = null;
    private TextView mRealPlayFullRateTv = null;
    private TextView mRealPlayFullFlowTv = null;
    private TextView mRealPlayRatioTv = null;
    private ImageButton mRealPlayFullIntelligentDetectionBtn = null;

    private ImageButton mRealPlayFullPtzAnimBtn = null;
    private ImageView mRealPlayFullPtzPromptIv = null;
    private boolean mIsOnPtz = false;
    private ImageView mRealPlayPtzDirectionIv = null;
    private ImageButton mRealPlayFullAnimBtn = null;
    private int[] mStartXy = new int[2];
    private int[] mEndXy = new int[2];

    private PopupWindow mQualityPopupWindow = null;
    private PopupWindow mPtzPopupWindow = null;
    private PopupWindow mFaceDetectionWindow = null;
    private LinearLayout mPtzControlLy = null;
    private PopupWindow mTalkPopupWindow = null;
    private RingView mTalkRingView = null;
    private Button mTalkBackControlBtn = null;
    private Switch faceDetect;
    private Switch faceCompare;
    private Switch faceAlarm;
    private Switch unsafeActs;
    private Switch skeletonDetect1;
    private Switch unsafeActsAlarm;
    private boolean faceDetectState = false;
    private boolean isDetectUnsafeActs = false;
    private boolean isDetectSkeleton = false;
    private boolean isUnsafeActsAlarmOpen = false;
    private WaitDialog mWaitDialog = null;

    private RealPlayBroadcastReceiver mBroadcastReceiver = null;
    private Timer mUpdateTimer = null;
    private TimerTask mUpdateTimerTask = null;

    // 全屏按钮 Full screen button
    private CheckTextButton mFullscreenButton;
    private CheckTextButton mFullscreenFullButton;
    private ScreenOrientationHelper mScreenOrientationHelper;
    private Button mFaceDetect;

    // 弱提示预览信息  Weak prompt preview information
    private long mStartTime = 0;
    private long mStopTime = 0;

    // 云台控制状态  PTZ control status
    private float mZoomScale = 0;
    private int mCommand = -1;

    // 横屏对讲 Cross screen intercom
    private ImageButton mRealPlayFullTalkAnimBtn;
    // 对讲模式 Talkback mode
    private boolean mIsOnTalk = false;

    private boolean mIsOnFaceDetection;

    // 直播预告 Live announcements
    private TextView mRealPlayPreviewTv = null;

    private EZPlayer mEZPlayer = null;
    //    private StubPlayer mStub = new StubPlayer();
    private CheckTextButton mFullScreenTitleBarBackBtn;
    private EZVideoLevel mCurrentQulityMode = EZVideoLevel.VIDEO_LEVEL_HD;
    private EZDeviceInfo mDeviceInfo = null;
    private EZCameraInfo mCameraInfo = null;
    private String mVerifyCode;

    private long mStreamFlow = 0;
    private Timer timer;

    private Bitmap bitmap;
    private int code = 1;
    private String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/0_OpenSDK/Face_Detection";
    private MyThread thrd;
    private MyThread facedetect;
    private boolean isFaceCompare = false;
    private Canvas canvas;
    private Paint paint;

    private ArrayList<PersonInfo> registeredPersonList = new ArrayList<>();
    private FaceEngine faceEngine = new FaceEngine();
    private ImageView faceRegListNull;
    private DrawerLayout drawerLayout;
    private RecyclerView recyclerView;
    private LinearLayout linearLayout;
    private boolean isRegListUpdate = false;
    PersonInfoAdapter personInfoAdapter;

    private ImageButton faceRegEdit;
    private boolean isEdit;
    private List<UnRegisteredPersonInfo> unRegisteredPersonInfoList = new ArrayList<>();
    private boolean isOpenAlarm = false;
    private TextToSpeech mTTS;
    private Classifier detector;


    private int succeedReg;
    private int mRealFlow = 0;

    private Button skeletonDetect;

    //    private GoogleApiClient client;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activeEngine();
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        faceEngine.init(this, DetectMode.ASF_DETECT_MODE_IMAGE, DetectFaceOrientPriority.ASF_OP_0_ONLY, 32, 20,
                FaceEngine.ASF_FACE_DETECT | FaceEngine.ASF_FACE_RECOGNITION | FaceEngine.ASF_GENDER | FaceEngine.ASF_AGE);

        initData();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
//        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
            initView();
            //initFaceDetector();

    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: ");
        stopFaceDetect();
        if (mTTS != null) {
            mTTS.stop();
            mTTS.shutdown();
        }
    }

    @Override
    protected void onResume() {


        super.onResume();

        if (mOrientation == Configuration.ORIENTATION_LANDSCAPE) {
            return;
        }
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                if (mRealPlaySv != null) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(mRealPlaySv.getWindowToken(), 0);
                }
            }
        }, 200);

        initUI();
        //mRealPlaySv.setVisibility(View.VISIBLE);

        LogUtil.infoLog(TAG, "onResume real play status:" + mStatus);
        if (mCameraInfo != null && mDeviceInfo != null &&  mDeviceInfo.getStatus() != 1) {
            if (mStatus != RealPlayStatus.STATUS_STOP) {
                stopRealPlay();
            }
            setRealPlayFailUI(getString(R.string.realplay_fail_device_not_exist));
        } else {
            if (mStatus == RealPlayStatus.STATUS_PAUSE
                    || mStatus == RealPlayStatus.STATUS_DECRYPT) {
                // 开始播放
                startRealPlay();
            }
        }
        mIsOnStop = false;
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mTTS != null) {
            mTTS.stop();
            mTTS.shutdown();
        }
        if (mScreenOrientationHelper != null) {
            mScreenOrientationHelper.postOnStop();
        }

        mHandler.removeMessages(MSG_AUTO_START_PLAY);
        hidePageAnim();

        if (mCameraInfo == null && mRtspUrl == null) {
            return;
        }

        closePtzPopupWindow();
        closeTalkPopupWindow(true, false);
        if (mStatus != RealPlayStatus.STATUS_STOP) {

            mIsOnStop = true;
            stopRealPlay();
            mStatus = RealPlayStatus.STATUS_PAUSE;
            setRealPlayStopUI();
        } else {
            setStopLoading();
        }
        //mRealPlaySv.setVisibility(View.INVISIBLE);

    }

    /*
     * (non-Javadoc)
     * @see android.app.Activity#onDestroy()
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        registeredPersonList = null;
        stopFaceDetect();
        if (mTTS != null) {
            mTTS.stop();
            mTTS.shutdown();
        }
        if (mEZPlayer != null) {
            mEZPlayer.release();
        }
        mHandler.removeMessages(MSG_AUTO_START_PLAY);
        mHandler.removeMessages(MSG_HIDE_PTZ_DIRECTION);
        mHandler.removeMessages(MSG_CLOSE_PTZ_PROMPT);
        mHandler.removeMessages(MSG_HIDE_PAGE_ANIM);
        mHandler = null;

        if (mBroadcastReceiver != null) {
            // 取消锁屏广播的注册 Cancel the registration of the lock screen broadcast
            unregisterReceiver(mBroadcastReceiver);
            mBroadcastReceiver = null;
        }
        mScreenOrientationHelper = null;

        unInitEngine();
    }

   private void  exit(){
       closePtzPopupWindow();
       closeTalkPopupWindow(true, false);
       if (mStatus != RealPlayStatus.STATUS_STOP) {
           stopRealPlay();
           setRealPlayStopUI();
       }
       mHandler.removeMessages(MSG_AUTO_START_PLAY);
       mHandler.removeMessages(MSG_HIDE_PTZ_DIRECTION);
       mHandler.removeMessages(MSG_CLOSE_PTZ_PROMPT);
       mHandler.removeMessages(MSG_HIDE_PAGE_ANIM);
       if (mBroadcastReceiver != null) {
           // Cancel the registration of the lock screen broadcast
           unregisterReceiver(mBroadcastReceiver);
           mBroadcastReceiver = null;
       }
       finish();
    }

    @Override
    public void finish() {
        if (mCameraInfo != null){
            Intent intent = new Intent();
            intent.putExtra(IntentConsts.EXTRA_DEVICE_ID,mCameraInfo.getDeviceSerial());
            intent.putExtra(IntentConsts.EXTRA_CAMERA_NO,mCameraInfo.getCameraNo());
            intent.putExtra("video_level",mCameraInfo.getVideoLevel().getVideoLevel());
            setResult(EZCameraListActivity.RESULT_CODE, intent);
        }
        super.finish();
    }

    @Override
    public void onBackPressed() {
        if (this.getResources().getConfiguration().orientation != Configuration.ORIENTATION_PORTRAIT) {
            mScreenOrientationHelper.portrait();
            return;
        }
        exit();
    }


    private void initData() {
        Application application = (Application) getApplication();
        mAudioPlayUtil = AudioPlayUtil.getInstance(application);
        mLocalInfo = LocalInfo.getInstance();
        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        mLocalInfo.setScreenWidthHeight(metric.widthPixels, metric.heightPixels);
        mLocalInfo.setNavigationBarHeight((int) Math.ceil(25 * getResources().getDisplayMetrics().density));

        mHandler = new Handler(this);
        mRecordRotateViewUtil = new RotateViewUtil();

        mBroadcastReceiver = new RealPlayBroadcastReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_USER_PRESENT);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        registerReceiver(mBroadcastReceiver, filter);

        mRealPlaySquareInfo = new RealPlaySquareInfo();
        Intent intent = getIntent();
        if (intent != null) {
            mCameraInfo = intent.getParcelableExtra(IntentConsts.EXTRA_CAMERA_INFO);
            mDeviceInfo = intent.getParcelableExtra(IntentConsts.EXTRA_DEVICE_INFO);
            mRtspUrl = intent.getStringExtra(IntentConsts.EXTRA_RTSP_URL);
            Log.d(TAG, "initData: " + mRtspUrl);
            if (mCameraInfo != null) {
                mCurrentQulityMode = (mCameraInfo.getVideoLevel());
            }
            LogUtil.debugLog(TAG, "rtspUrl:" + mRtspUrl);

            getRealPlaySquareInfo();
        }
        if (mDeviceInfo != null && mDeviceInfo.getIsEncrypt() == 1) {
            mVerifyCode = DataManager.getInstance().getDeviceSerialVerifyCode(mCameraInfo.getDeviceSerial());
        }
    }

    private void getRealPlaySquareInfo() {
        if (TextUtils.isEmpty(mRtspUrl)) {
            return;
        }
        Uri uri = Uri.parse(mRtspUrl.replaceFirst("&", "?"));
        try {
            mRealPlaySquareInfo.mSquareId = Integer.parseInt(uri.getQueryParameter("squareid"));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        try {
            mRealPlaySquareInfo.mChannelNo = Integer.parseInt(Utils.getUrlValue(mRtspUrl, "channelno=", "&"));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        mRealPlaySquareInfo.mCameraName = uri.getQueryParameter("cameraname");
        try {
            mRealPlaySquareInfo.mSoundType = Integer.parseInt(uri.getQueryParameter("soundtype"));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        mRealPlaySquareInfo.mCoverUrl = uri.getQueryParameter("md5Serial");
        if (!TextUtils.isEmpty(mRealPlaySquareInfo.mCoverUrl)) {
            mRealPlaySquareInfo.mCoverUrl = mLocalInfo.getServAddr() + mRealPlaySquareInfo.mCoverUrl + "_mobile.jpeg";
        }
    }

    private boolean isHandset = false;
    public void onClickSwitchBetweenSpeakerAndHandset(View view) {
        Button switchButton = (Button) view;
        if (isHandset){
            if (mEZPlayer != null){
                mEZPlayer.setSpeakerphoneOn(true);
            }
            switchButton.setText(getResources().getString(R.string.switch_to_handset));
            isHandset = false;
        }else{
            if (mEZPlayer != null){
                mEZPlayer.setSpeakerphoneOn(false);
            }
            switchButton.setText(getResources().getString(R.string.switch_to_speaker));
            isHandset = true;
        }
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            int result = mTTS.setLanguage(Locale.CHINA);
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.d(TAG, "onInit: 初始化失败");
            }
        }
        if (mTTS != null) {
            mTTS.setPitch(1.0f);
            mTTS.setSpeechRate(0.7f);
        }
    }


    private class RealPlayBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (Intent.ACTION_SCREEN_OFF.equals(intent.getAction())) {
                closePtzPopupWindow();
                closeTalkPopupWindow(true, false);
                if (mStatus != RealPlayStatus.STATUS_STOP) {
                    stopRealPlay();
                    mStatus = RealPlayStatus.STATUS_PAUSE;
                    setRealPlayStopUI();
                }
            }
        }
    }

    private void initTitleBar() {
        mPortraitTitleBar = (TitleBar) findViewById(R.id.title_bar_portrait);
        mPortraitTitleBar.addBackButton(new OnClickListener() {

            @Override
            public void onClick(View v) {
                closePtzPopupWindow();
                closeTalkPopupWindow(true, false);
                if (mStatus != RealPlayStatus.STATUS_STOP) {
                    stopRealPlay();
                    setRealPlayStopUI();
                }
                finish();
            }
        });
        if (mRtspUrl == null) {
        } else {
            //mPortraitTitleBar.setBackgroundColor(getResources().getColor(R.color.black_bg));
        }
        mLandscapeTitleBar = (TitleBar) findViewById(R.id.title_bar_landscape);
        mLandscapeTitleBar.setStyle(Color.rgb(0xff, 0xff, 0xff), getResources().getDrawable(R.color.dark_bg_70p),
                getResources().getDrawable(R.drawable.message_back_selector));
        mLandscapeTitleBar.setOnTouchListener(this);
        mFullScreenTitleBarBackBtn = new CheckTextButton(this);
        mFullScreenTitleBarBackBtn.setBackground(getResources().getDrawable(R.drawable.common_title_back_selector));
        mLandscapeTitleBar.addLeftView(mFullScreenTitleBarBackBtn);
    }

    private void initRealPlayPageLy() {
        mRealPlayPageLy = (LinearLayout) findViewById(R.id.realplay_page_ly);
        /** 测量状态栏高度 Measure the status bar height**/
        ViewTreeObserver viewTreeObserver = mRealPlayPageLy.getViewTreeObserver();
        viewTreeObserver.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (mRealPlayRect == null) {
                    // 获取状况栏高度
                    mRealPlayRect = new Rect();
                    getWindow().getDecorView().getWindowVisibleDisplayFrame(mRealPlayRect);
                }
            }
        });
    }

    private void initView() {
        setContentView(R.layout.ez_realplay_page);
        //保持屏幕常亮 Keep the screen on
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        initTitleBar();
        initRealPlayPageLy();
        initLoadingUI();
        faceRect = findViewById(R.id.face_rect);
        findViewById(R.id.reg_person).setOnClickListener(this);
        recyclerView = findViewById(R.id.realplay_page_rv);
        faceRegListNull = findViewById(R.id.face_reg_list_null);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);



        faceRegEdit = findViewById(R.id.realplay_page_face_reg);
        faceRegEdit.setOnClickListener(this);
        mRealPlayPlayRl = (RelativeLayout) findViewById(R.id.realplay_play_rl);
        drawerLayout = findViewById(R.id.realplay_page_dl);
        linearLayout = findViewById(R.id.realplay_page_ll);
        mRealPlaySv = (SurfaceView) findViewById(R.id.realplay_sv);
        mRealPlaySh = mRealPlaySv.getHolder();
        mRealPlaySh.addCallback(this);
        drawerLayout.setScrimColor(Color.TRANSPARENT);
        drawerLayout.setBackgroundColor(0xe0e0e0);
        ViewGroup.LayoutParams params = linearLayout.getLayoutParams();
        params.width = (int) (getWindowManager().getDefaultDisplay().getWidth() * 0.6);
        linearLayout.setLayoutParams(params);

        drawerLayout.setDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });

        mRealPlayTouchListener = new CustomTouchListener() {
            @Override
            public boolean canZoom(float scale) {
                if (mStatus == RealPlayStatus.STATUS_PLAY) {
                    return true;
                } else {
                    return false;
                }
            }

            @Override
            public boolean canDrag(int direction) {
                if (mStatus != RealPlayStatus.STATUS_PLAY) {
                    return false;
                }
                if (mEZPlayer != null && mDeviceInfo != null) {
                    // 出界判断 Out of bounds
                    if (DRAG_LEFT == direction || DRAG_RIGHT == direction) {
                        // 左移/右移出界判断 Left / right out of bounds
                        if (mDeviceInfo.isSupportPTZ()) {
                            return true;
                        }
                    } else if (DRAG_UP == direction || DRAG_DOWN == direction) {
                        // 上移/下移出界判断  Move up / down to judge
                        if (mDeviceInfo.isSupportPTZ()) {
                            return true;
                        }
                    }
                }
                return false;
            }

            @Override
            public void onSingleClick() {
                onRealPlaySvClick();
            }

            @Override
            public void onDoubleClick(MotionEvent e) {
            }

            @Override
            public void onZoom(float scale) {
                LogUtil.debugLog(TAG, "onZoom:" + scale);
                if (mEZPlayer != null && mDeviceInfo != null &&  mDeviceInfo.isSupportZoom()) {
                    startZoom(scale);
                }
            }

            @Override
            public void onDrag(int direction, float distance, float rate) {
                LogUtil.debugLog(TAG, "onDrag:" + direction);
                if (mEZPlayer != null) {
                    startDrag(direction, distance, rate);
                }
            }

            @Override
            public void onEnd(int mode) {
                LogUtil.debugLog(TAG, "onEnd:" + mode);
                if (mEZPlayer != null) {
                    stopDrag(false);
                }
                if (mEZPlayer != null && mDeviceInfo != null && mDeviceInfo.isSupportZoom()) {
                    stopZoom();
                }
            }

            @Override
            public void onZoomChange(float scale, CustomRect oRect, CustomRect curRect) {
                LogUtil.debugLog(TAG, "onZoomChange:" + scale);
                if (mEZPlayer != null && mDeviceInfo != null && mDeviceInfo.isSupportZoom()) {
                    //采用云台调焦 Using PTZ focus
                    return;
                }
                if (mStatus == RealPlayStatus.STATUS_PLAY) {
                    if (scale > 1.0f && scale < 1.1f) {
                        scale = 1.1f;
                    }
                    setPlayScaleUI(scale, oRect, curRect);
                }
            }
        };
        mRealPlaySv.setOnTouchListener(mRealPlayTouchListener);

        mRealPlayPtzDirectionIv = (ImageView) findViewById(R.id.realplay_ptz_direction_iv);

        mRealPlayControlRl = (LinearLayout) findViewById(R.id.realplay_control_rl);
        mRealPlayBtn = (ImageButton) findViewById(R.id.realplay_play_btn);
        mRealPlaySoundBtn = (ImageButton) findViewById(R.id.realplay_sound_btn);
        mLocalInfo.setSoundOpen(false);
        mRealPlayFlowTv = (TextView) findViewById(R.id.realplay_flow_tv);
        mRealPlayFlowTv.setText("0k/s");

        mRealPlayCaptureRl = (RelativeLayout) findViewById(R.id.realplay_capture_rl);
        mRealPlayCaptureRlLp = (LayoutParams) mRealPlayCaptureRl.getLayoutParams();
        mRealPlayCaptureIv = (ImageView) findViewById(R.id.realplay_capture_iv);
        mRealPlayCaptureWatermarkIv = (ImageView) findViewById(R.id.realplay_capture_watermark_iv);
        mRealPlayRecordLy = (LinearLayout) findViewById(R.id.realplay_record_ly);
        mRealPlayRecordIv = (ImageView) findViewById(R.id.realplay_record_iv);
        mRealPlayRecordTv = (TextView) findViewById(R.id.realplay_record_tv);

        mRealPlayQualityBtn = (Button) findViewById(R.id.realplay_quality_btn);

        mRealPlayFullFlowLy = (LinearLayout) findViewById(R.id.realplay_full_flow_ly);
        mRealPlayFullRateTv = (TextView) findViewById(R.id.realplay_full_rate_tv);
        mRealPlayFullFlowTv = (TextView) findViewById(R.id.realplay_full_flow_tv);
        mRealPlayRatioTv = (TextView) findViewById(R.id.realplay_ratio_tv);
        mRealPlayFullRateTv.setText("0k/s");
        mRealPlayFullFlowTv.setText("0MB");

        mFullscreenButton = (CheckTextButton) findViewById(R.id.fullscreen_button);
        mFullscreenFullButton = (CheckTextButton) findViewById(R.id.fullscreen_full_button);

        if (mRtspUrl == null) {
            initOperateBarUI(false);

            initFullOperateBarUI();
            mRealPlayOperateBar.setVisibility(View.VISIBLE);
        } else {
            //mRealPlayPageLy.setBackgroundColor(getResources().getColor(R.color.black_bg));
            LinearLayout.LayoutParams realPlayPlayRlLp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
                    LayoutParams.WRAP_CONTENT);
            realPlayPlayRlLp.gravity = Gravity.CENTER;
            //mj 2015/11/01 realPlayPlayRlLp.weight = 1;
            mRealPlayPlayRl.setLayoutParams(realPlayPlayRlLp);
            mRealPlayPlayRl.setBackgroundColor(getResources().getColor(R.color.common_bg));
        }

        setRealPlaySvLayout();
        initCaptureUI();
        mScreenOrientationHelper = new ScreenOrientationHelper(this, mFullscreenButton, /*mFullscreenFullButton*/mFullScreenTitleBarBackBtn);

        mWaitDialog = new WaitDialog(this, android.R.style.Theme_Translucent_NoTitleBar);
        mWaitDialog.setCancelable(false);
    }

    public void startDrag(int direction, float distance, float rate) {
    }

    public void stopDrag(boolean control) {
    }

    private void startZoom(float scale) {
        if (mEZPlayer == null) {
            return;
        }

        hideControlRlAndFullOperateBar(false);
        boolean preZoomIn = mZoomScale > 1.01 ? true : false;
        boolean zoomIn = scale > 1.01 ? true : false;
        if (mZoomScale != 0 && preZoomIn != zoomIn) {
            LogUtil.debugLog(TAG, "startZoom stop:" + mZoomScale);
            //            mEZOpenSDK.controlPTZ(mZoomScale > 1.01 ? RealPlayStatus.PTZ_ZOOMIN
            //                    : RealPlayStatus.PTZ_ZOOMOUT, RealPlayStatus.PTZ_SPEED_DEFAULT, EZPlayer.PTZ_COMMAND_STOP);
            mZoomScale = 0;
        }
        if (scale != 0 && (mZoomScale == 0 || preZoomIn != zoomIn)) {
            mZoomScale = scale;
            LogUtil.debugLog(TAG, "startZoom start:" + mZoomScale);
            //            mEZOpenSDK.controlPTZ(mZoomScale > 1.01 ? RealPlayStatus.PTZ_ZOOMIN
            //                    : RealPlayStatus.PTZ_ZOOMOUT, RealPlayStatus.PTZ_SPEED_DEFAULT, EZPlayer.PTZ_COMMAND_START);
        }
    }

    private void stopZoom() {
        if (mEZPlayer == null) {
            return;
        }
        if (mZoomScale != 0) {
            LogUtil.debugLog(TAG, "stopZoom stop:" + mZoomScale);
            //            mEZOpenSDK.controlPTZ(mZoomScale > 1.01 ? RealPlayStatus.PTZ_ZOOMIN
            //                    : RealPlayStatus.PTZ_ZOOMOUT, RealPlayStatus.PTZ_SPEED_DEFAULT, EZPlayer.PTZ_COMMAND_STOP);
            mZoomScale = 0;
        }
    }

    private void setPtzDirectionIv(int command) {
        setPtzDirectionIv(command, 0);
    }

    private void setPtzDirectionIv(int command, int errorCode) {
        if (command != -1 && errorCode == 0) {
            LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT);
            switch (command) {
                case RealPlayStatus.PTZ_LEFT:
                    mRealPlayPtzDirectionIv.setBackgroundResource(R.drawable.left_twinkle);
                    params.addRule(RelativeLayout.CENTER_VERTICAL);
                    params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                    mRealPlayPtzDirectionIv.setLayoutParams(params);
                    break;
                case RealPlayStatus.PTZ_RIGHT:
                    mRealPlayPtzDirectionIv.setBackgroundResource(R.drawable.right_twinkle);
                    params.addRule(RelativeLayout.CENTER_VERTICAL);
                    params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                    mRealPlayPtzDirectionIv.setLayoutParams(params);
                    break;
                case RealPlayStatus.PTZ_UP:
                    mRealPlayPtzDirectionIv.setBackgroundResource(R.drawable.up_twinkle);
                    params.addRule(RelativeLayout.CENTER_HORIZONTAL);
                    params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                    mRealPlayPtzDirectionIv.setLayoutParams(params);
                    break;
                case RealPlayStatus.PTZ_DOWN:
                    mRealPlayPtzDirectionIv.setBackgroundResource(R.drawable.down_twinkle);
                    params.addRule(RelativeLayout.CENTER_HORIZONTAL);
                    params.addRule(RelativeLayout.ALIGN_BOTTOM, R.id.realplay_sv);
                    mRealPlayPtzDirectionIv.setLayoutParams(params);
                    break;
                default:
                    break;
            }
            mRealPlayPtzDirectionIv.setVisibility(View.VISIBLE);
            mHandler.removeMessages(MSG_HIDE_PTZ_DIRECTION);
            Message msg = new Message();
            msg.what = MSG_HIDE_PTZ_DIRECTION;
            msg.arg1 = 1;
            mHandler.sendMessageDelayed(msg, 500);
        } else if (errorCode != 0) {
            LayoutParams svParams = (LayoutParams) mRealPlaySv.getLayoutParams();
            LayoutParams params = null;
            switch (errorCode) {
                case ErrorCode.ERROR_CAS_PTZ_ROTATION_LEFT_LIMIT_FAILED:
                    params = new LayoutParams(LayoutParams.WRAP_CONTENT, svParams.height);
                    mRealPlayPtzDirectionIv.setBackgroundResource(R.drawable.ptz_left_limit);
                    params.addRule(RelativeLayout.CENTER_VERTICAL);
                    params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                    mRealPlayPtzDirectionIv.setLayoutParams(params);
                    break;
                case ErrorCode.ERROR_CAS_PTZ_ROTATION_RIGHT_LIMIT_FAILED:
                    params = new LayoutParams(LayoutParams.WRAP_CONTENT, svParams.height);
                    mRealPlayPtzDirectionIv.setBackgroundResource(R.drawable.ptz_right_limit);
                    params.addRule(RelativeLayout.CENTER_VERTICAL);
                    params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                    mRealPlayPtzDirectionIv.setLayoutParams(params);
                    break;
                case ErrorCode.ERROR_CAS_PTZ_ROTATION_UP_LIMIT_FAILED:
                    params = new LayoutParams(svParams.width, LayoutParams.WRAP_CONTENT);
                    mRealPlayPtzDirectionIv.setBackgroundResource(R.drawable.ptz_top_limit);
                    params.addRule(RelativeLayout.CENTER_HORIZONTAL);
                    params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                    mRealPlayPtzDirectionIv.setLayoutParams(params);
                    break;
                case ErrorCode.ERROR_CAS_PTZ_ROTATION_DOWN_LIMIT_FAILED:
                    params = new LayoutParams(svParams.width, LayoutParams.WRAP_CONTENT);
                    mRealPlayPtzDirectionIv.setBackgroundResource(R.drawable.ptz_bottom_limit);
                    params.addRule(RelativeLayout.CENTER_HORIZONTAL);
                    params.addRule(RelativeLayout.ALIGN_BOTTOM, R.id.realplay_sv);
                    mRealPlayPtzDirectionIv.setLayoutParams(params);
                    break;
                default:
                    break;
            }
            mRealPlayPtzDirectionIv.setVisibility(View.VISIBLE);
            mHandler.removeMessages(MSG_HIDE_PTZ_DIRECTION);
            Message msg = new Message();
            msg.what = MSG_HIDE_PTZ_DIRECTION;
            msg.arg1 = 1;
            mHandler.sendMessageDelayed(msg, 500);
        } else {
            mRealPlayPtzDirectionIv.setVisibility(View.GONE);
            mHandler.removeMessages(MSG_HIDE_PTZ_DIRECTION);
        }
    }

    private int getSupportPtz() {
        if (mEZPlayer == null || mDeviceInfo == null) {
            return 0;
        }
        if (mDeviceInfo.isSupportPTZ() || mDeviceInfo.isSupportZoom()) {
            return 1;
        } else {
            return 0;
        }
    }

    @SuppressWarnings("deprecation")
    private void initUI() {
        mPageAnimDrawable = null;
        mRealPlaySoundBtn.setVisibility(View.VISIBLE);
        if (mCameraInfo != null) {
            mPortraitTitleBar.setTitle(mCameraInfo.getCameraName());
            mLandscapeTitleBar.setTitle(mCameraInfo.getCameraName());
            setCameraInfoTiletRightBtn();
            if (mLocalInfo.isSoundOpen()) {
                mRealPlaySoundBtn.setBackgroundResource(R.drawable.ezopen_vertical_preview_sound_selector);
                mRealPlayFullSoundBtn.setBackgroundResource(R.drawable.play_full_soundon_btn_selector);
            } else {
                mRealPlaySoundBtn.setBackgroundResource(R.drawable.ezopen_vertical_preview_sound_off_selector);
                mRealPlayFullSoundBtn.setBackgroundResource(R.drawable.play_full_soundoff_btn_selector);
            }
            mRealPlayCaptureBtnLy.setVisibility(View.VISIBLE);
            mRealPlayFullCaptureBtn.setVisibility(View.VISIBLE);
            mRealPlayRecordContainerLy.setVisibility(View.VISIBLE);
            mRealPlayFullRecordContainer.setVisibility(View.VISIBLE);
            mRealPlayQualityBtn.setVisibility(View.VISIBLE);
            mRealPlayFullSoundBtn.setVisibility(View.VISIBLE);
            mRealPlayFullPtzAnimBtn.setVisibility(View.GONE);
            mRealPlayFullPtzPromptIv.setVisibility(View.GONE);
            updateUI();
        } else if (mRtspUrl != null) {
            if (!TextUtils.isEmpty(mRealPlaySquareInfo.mCameraName)) {
                mPortraitTitleBar.setTitle(mRealPlaySquareInfo.mCameraName);
                mLandscapeTitleBar.setTitle(mRealPlaySquareInfo.mCameraName);
            }
            mRealPlaySoundBtn.setVisibility(View.GONE);
            mRealPlayQualityBtn.setVisibility(View.GONE);
        }

        if (mOrientation == Configuration.ORIENTATION_LANDSCAPE) {
            updateOperatorUI();
        }
    }

    private void setCameraInfoTiletRightBtn() {
        if (mTiletRightBtn != null && mDeviceInfo != null) {
            if (mDeviceInfo.getStatus() == 1) {
                mTiletRightBtn.setVisibility(View.VISIBLE);
            } else {
                mTiletRightBtn.setVisibility(View.GONE);
            }
        }
    }

    private void initOperateBarUI(boolean bigScreen) {
        bigScreen = false;
        if (mRealPlayOperateBar != null) {
            mRealPlayOperateBar.setVisibility(View.GONE);
            mRealPlayOperateBar = null;
        }
        if (bigScreen) {
            mRealPlayOperateBar = (HorizontalScrollView) findViewById(R.id.ezopen_realplay_operate_bar2);
            findViewById(R.id.ezopen_realplay_operate_bar).setVisibility(View.GONE);

            mRealPlayPtzBtnLy = (LinearLayout) findViewById(R.id.realplay_ptz_btn_ly2);
            mRealPlayTalkBtnLy = (LinearLayout) findViewById(R.id.realplay_talk_btn_ly2);
            mRealPlaySslBtnLy = (LinearLayout) findViewById(R.id.realplay_ssl_btn_ly2);
            mRealPlayCaptureBtnLy = (LinearLayout) findViewById(R.id.realplay_previously_btn_ly2);
            mRealPlayRecordContainerLy = (LinearLayout) findViewById(R.id.realplay_video_container_ly2);

            mRealPlayTalkBtn = (ImageButton) findViewById(R.id.realplay_talk_btn2);
            mRealPlaySslBtn = (Button) findViewById(R.id.realplay_ssl_btn2);
            mRealPlayPrivacyBtn = (ImageButton) findViewById(R.id.realplay_privacy_btn2);
            mRealPlayCaptureBtn = (ImageButton) findViewById(R.id.realplay_previously_btn2);
            mRealPlayRecordContainer = findViewById(R.id.realplay_video_container2);
            mRealPlayRecordBtn = (ImageButton) findViewById(R.id.realplay_video_btn2);
            mRealPlayRecordStartBtn = (ImageButton) findViewById(R.id.realplay_video_start_btn2);
            mRealPlayPtzBtn = (ImageButton) findViewById(R.id.realplay_ptz_btn2);
        } else {
            mRealPlayOperateBar = (HorizontalScrollView) findViewById(R.id.ezopen_realplay_operate_bar);
            findViewById(R.id.ezopen_realplay_operate_bar2).setVisibility(View.GONE);

            mRealPlayPtzBtnLy = (LinearLayout) findViewById(R.id.realplay_ptz_btn_ly);
            mRealPlayTalkBtnLy = (LinearLayout) findViewById(R.id.realplay_talk_btn_ly);
            mRealPlaySslBtnLy = (LinearLayout) findViewById(R.id.realplay_ssl_btn_ly);
            mRealPlayCaptureBtnLy = (LinearLayout) findViewById(R.id.realplay_previously_btn_ly);
            mRealPlayRecordContainerLy = (LinearLayout) findViewById(R.id.realplay_video_container_ly);

            mRealPlayTalkBtn = (ImageButton) findViewById(R.id.realplay_talk_btn);
            mRealPlaySslBtn = (Button) findViewById(R.id.realplay_ssl_btn);
            mRealPlayPrivacyBtn = (ImageButton) findViewById(R.id.realplay_privacy_btn);
            mRealPlayCaptureBtn = (ImageButton) findViewById(R.id.realplay_previously_btn);
            mRealPlayRecordContainer = findViewById(R.id.realplay_video_container);
            mRealPlayRecordBtn = (ImageButton) findViewById(R.id.realplay_video_btn);
            mRealPlayRecordStartBtn = (ImageButton) findViewById(R.id.realplay_video_start_btn);
            mRealPlayPtzBtn = (ImageButton) findViewById(R.id.realplay_ptz_btn);
            mRealPlayIntelligentDetectionBtn = findViewById(R.id.intelligent_detection);
        }
        mRealPlayTalkBtn.setEnabled(false);
        mRealPlayOperateBar.setVisibility(View.VISIBLE);
    }

    private void setBigScreenOperateBtnLayout() {
    }

    private void initFullOperateBarUI() {
        mRealPlayFullOperateBar = (RelativeLayout) findViewById(R.id.realplay_full_operate_bar);
        mRealPlayFullPlayBtn = (ImageButton) findViewById(R.id.realplay_full_play_btn);
        mRealPlayFullSoundBtn = (ImageButton) findViewById(R.id.realplay_full_sound_btn);
        mRealPlayFullTalkBtn = (ImageButton) findViewById(R.id.realplay_full_talk_btn);
        mRealPlayFullCaptureBtn = (ImageButton) findViewById(R.id.realplay_full_previously_btn);
        mRealPlayFullPtzBtn = (ImageButton) findViewById(R.id.realplay_full_ptz_btn);
        mRealPlayFullRecordContainer = findViewById(R.id.realplay_full_video_container);
        mRealPlayFullRecordBtn = (ImageButton) findViewById(R.id.realplay_full_video_btn);
        mRealPlayFullRecordStartBtn = (ImageButton) findViewById(R.id.realplay_full_video_start_btn);
        mRealPlayFullOperateBar.setOnTouchListener(this);

        mRealPlayFullPtzAnimBtn = (ImageButton) findViewById(R.id.realplay_full_ptz_anim_btn);
        mRealPlayFullPtzPromptIv = (ImageView) findViewById(R.id.realplay_full_ptz_prompt_iv);

        mRealPlayFullTalkAnimBtn = (ImageButton) findViewById(R.id.realplay_full_talk_anim_btn);

        mRealPlayFullAnimBtn = (ImageButton) findViewById(R.id.realplay_full_anim_btn);
    }

    private void startFullBtnAnim(final View animView, final int[] startXy, final int[] endXy,
                                  final AnimationListener animationListener) {
        animView.setVisibility(View.VISIBLE);
        TranslateAnimation anim = new TranslateAnimation(startXy[0], endXy[0], startXy[1], endXy[1]);
        anim.setAnimationListener(animationListener);
        anim.setDuration(ANIMATION_DURING_TIME);
        animView.startAnimation(anim);
    }

    private void setVideoLevel() {
        if (mCameraInfo == null || mEZPlayer == null || mDeviceInfo == null) {
            return;
        }

        if (mDeviceInfo.getStatus() == 1) {
            mRealPlayQualityBtn.setEnabled(true);
        } else {
            mRealPlayQualityBtn.setEnabled(false);
        }

        /**************
         * 本地数据保存 需要更新之前获取到的设备列表信息，开发者自己设置
         *
         * Local data saved need to be updated before the obtained device list information, the developer's own settings
         * *********************/
        mCameraInfo.setVideoLevel(mCurrentQulityMode.getVideoLevel());

        //
        /**
         *
         * 视频质量，2-高清，1-标清，0-流畅
         * Video quality, 2-HD, 1-standard, 0- smooth
         *
         */
        if (mCurrentQulityMode.getVideoLevel() == EZVideoLevel.VIDEO_LEVEL_FLUNET.getVideoLevel()) {
            mRealPlayQualityBtn.setText(R.string.quality_flunet);
        } else if (mCurrentQulityMode.getVideoLevel() == EZVideoLevel.VIDEO_LEVEL_BALANCED.getVideoLevel()) {
            mRealPlayQualityBtn.setText(R.string.quality_balanced);
        } else if (mCurrentQulityMode.getVideoLevel() == EZVideoLevel.VIDEO_LEVEL_HD.getVideoLevel()) {
            mRealPlayQualityBtn.setText(R.string.quality_hd);
        }else if (mCurrentQulityMode.getVideoLevel() == EZVideoLevel.VIDEO_LEVEL_SUPERCLEAR.getVideoLevel()){
            mRealPlayQualityBtn.setText(R.string.quality_super_hd);
        }else{
            mRealPlayQualityBtn.setText("unknown");
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        mOrientation = newConfig.orientation;

        onOrientationChanged();
        super.onConfigurationChanged(newConfig);
    }

    private void updateOrientation() {
        if (mIsOnTalk) {
            if (mEZPlayer != null && mDeviceInfo != null && mDeviceInfo.isSupportTalk() != EZConstants.EZTalkbackCapability.EZTalkbackNoSupport) {
                setOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
            } else {
                setForceOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            }
        } else {
            if (mStatus == RealPlayStatus.STATUS_PLAY) {
                setOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
            } else {
                if (mOrientation == Configuration.ORIENTATION_PORTRAIT) {
                    setOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                } else {
                    setOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
                }
            }
        }
    }

    private void updateOperatorUI() {
        if (mOrientation == Configuration.ORIENTATION_PORTRAIT) {
            fullScreen(false);
            updateOrientation();
            mPortraitTitleBar.setVisibility(View.VISIBLE);
            mLandscapeTitleBar.setVisibility(View.GONE);
            mRealPlayControlRl.setVisibility(View.VISIBLE);
            if (mRtspUrl == null) {
                mRealPlayPageLy.setBackgroundColor(getResources().getColor(R.color.common_bg));
                mRealPlayOperateBar.setVisibility(View.VISIBLE);
                mRealPlayFullOperateBar.setVisibility(View.GONE);
                mFullscreenFullButton.setVisibility(View.GONE);
                if (isRecording) {
                    mRealPlayRecordBtn.setVisibility(View.GONE);
                    mRealPlayRecordStartBtn.setVisibility(View.VISIBLE);
                } else {
                    mRealPlayRecordBtn.setVisibility(View.VISIBLE);
                    mRealPlayRecordStartBtn.setVisibility(View.GONE);
                }
            }
        } else {
            fullScreen(true);
            mPortraitTitleBar.setVisibility(View.GONE);
            // hide the
            mRealPlayControlRl.setVisibility(View.GONE);
            if (!mIsOnTalk && !mIsOnPtz) {
                mLandscapeTitleBar.setVisibility(View.VISIBLE);
            }
            if (mRtspUrl == null) {
                mRealPlayOperateBar.setVisibility(View.GONE);
                mRealPlayPageLy.setBackgroundColor(getResources().getColor(R.color.black_bg));
                mRealPlayFullOperateBar.setVisibility(View.GONE);
                if (!mIsOnTalk && !mIsOnPtz) {
                    mFullscreenFullButton.setVisibility(View.GONE);
                }
                if (isRecording) {
                    mRealPlayFullRecordBtn.setVisibility(View.GONE);
                    mRealPlayFullRecordStartBtn.setVisibility(View.VISIBLE);
                } else {
                    mRealPlayFullRecordBtn.setVisibility(View.VISIBLE);
                    mRealPlayFullRecordStartBtn.setVisibility(View.GONE);
                }
            }
        }

        //        mRealPlayControlRl.setVisibility(View.GONE);
        closeQualityPopupWindow();
        if (mStatus == RealPlayStatus.STATUS_START) {
            showControlRlAndFullOperateBar();
        }
    }

    private void updatePtzUI() {
        if (!mIsOnPtz) {
            return;
        }
        if (mOrientation == Configuration.ORIENTATION_PORTRAIT) {
            setFullPtzStopUI(false);
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    openPtzPopupWindow(mRealPlayPlayRl);
                }
            });
        } else {
            closePtzPopupWindow();
            setFullPtzStartUI(false);
        }
    }

    private void updateTalkUI() {
        if (!mIsOnTalk) {
            return;
        }
        if (mOrientation == Configuration.ORIENTATION_PORTRAIT) {
            if (mRealPlayFullTalkAnimBtn != null) {
                mRealPlayFullTalkAnimBtn.setVisibility(View.GONE);
                mFullscreenFullButton.setVisibility(View.GONE);
            }
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    openTalkPopupWindow(false);
                }
            });
        } else {
            if (mRealPlayFullTalkAnimBtn != null) {
                mRealPlayFullOperateBar.setVisibility(View.VISIBLE);
                mRealPlayFullOperateBar.post(new Runnable() {

                    @Override
                    public void run() {
                        mRealPlayFullTalkBtn.getLocationInWindow(mStartXy);
                        mEndXy[0] = Utils.dip2px(EZRealPlayActivity.this, 20);
                        mEndXy[1] = mStartXy[1];

                        mRealPlayFullOperateBar.setVisibility(View.GONE);
                        mRealPlayFullTalkAnimBtn.setVisibility(View.VISIBLE);
                        //                        mFullscreenFullButton.setVisibility(View.VISIBLE);
                        ((AnimationDrawable) mRealPlayFullTalkAnimBtn.getBackground()).start();
                    }

                });
            }
            closeTalkPopupWindow(false, false);
        }
    }

    private void fullScreen(boolean enable) {
        if (enable) {
            WindowManager.LayoutParams lp = getWindow().getAttributes();
            lp.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
            getWindow().setAttributes(lp);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        } else {
            WindowManager.LayoutParams attr = getWindow().getAttributes();
            attr.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getWindow().setAttributes(attr);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
    }

    private void onOrientationChanged() {
        setRealPlaySvLayout();

        updateOperatorUI();
        updateCaptureUI();
        updateTalkUI();
        updatePtzUI();
    }

    /*
     * (non-Javadoc)
     * @see android.view.SurfaceHolder.Callback#surfaceChanged(android.view.SurfaceHolder , int,
     * int, int)
     */
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        if (mEZPlayer != null) {
            mEZPlayer.setSurfaceHold(holder);
        }
        mRealPlaySh = holder;
    }

    /*
     * (non-Javadoc)
     * @see android.view.SurfaceHolder.Callback#surfaceCreated(android.view.SurfaceHolder )
     */
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (mEZPlayer != null) {
            mEZPlayer.setSurfaceHold(holder);
        }
        mRealPlaySh = holder;
        if (mStatus == RealPlayStatus.STATUS_INIT) {
            // 开始播放
            startRealPlay();
        }
    }

    /*
     * (non-Javadoc)
     * @see android.view.SurfaceHolder.Callback#surfaceDestroyed(android.view. SurfaceHolder)
     */
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        if (mEZPlayer != null) {
            mEZPlayer.setSurfaceHold(null);
        }
        mRealPlaySh = null;
    }

    private int num = 0;
    /*
     * (non-Javadoc)
     * @see android.view.View.OnClickListener#onClick(android.view.View)
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.skeleton_detect:
                String mPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/skeleton.jpeg";
                AipBodyAnalysis client = new AipBodyAnalysis("19386573", "05yklmuUy8KM9GCu54CeoN8w", "VpY30LarrdxlnsqtaOzo3XDmh0DVG99D");
                client.setConnectionTimeoutInMillis(2000);
                client.setSocketTimeoutInMillis(60000);
                HashMap<String, String> options = new HashMap<String, String>();

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        JSONObject res= client.bodyAnalysis(mPath, options);
                        try {
                            Log.d(TAG, "onClick: " + res.toString(2));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();

                break;
            case R.id.face_detect:
                faceDetectState = !faceDetectState;
                if (faceDetectState) {
                    faceCompare.setVisibility(View.VISIBLE);
                    faceDetect.setChecked(true);
                    Toast.makeText(this, "开始识别", Toast.LENGTH_SHORT).show();
                    try {
                        faceDetectCapture();
                        Thread.sleep(200);
                        drawFaceRect(faceRect, dispWidth, dispHeight);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    faceCompare.setVisibility(View.GONE);
                    faceAlarm.setVisibility(View.GONE);
                    faceDetect.setChecked(false);
                    isFaceCompare = false;
                    isOpenAlarm = false;
                    stopFaceDetect();
                    bitmap = Bitmap.createBitmap(dispWidth, dispHeight, Bitmap.Config.ARGB_8888);
                    faceRect.setImageBitmap(bitmap);
                }

                break;
            case R.id.face_compare:
                isFaceCompare = !isFaceCompare;
                if (isFaceCompare) {
                    faceCompare.setChecked(true);
                    faceAlarm.setVisibility(View.VISIBLE);
                    if (isOpenAlarm) {
                        faceAlarm.setChecked(true);
                    } else {
                        faceAlarm.setChecked(false);
                    }
                } else {
                    faceCompare.setChecked(false);
                    faceAlarm.setVisibility(View.GONE);
                    isOpenAlarm = false;
                }
                break;
            case R.id.face_alarm:
                isOpenAlarm = !isOpenAlarm;
                mTTS = new TextToSpeech(getApplicationContext(), this);
                if (isOpenAlarm) {
                    faceAlarm.setChecked(true);
                } else {
                    unRegisteredPersonInfoList.clear();
                    faceAlarm.setChecked(false);
                    if (mTTS != null){
                        mTTS.stop();
                        mTTS.shutdown();
                    }

                }
                break;
            case R.id.realplay_page_face_reg:
                if (isFaceCompare) {
                    Toast.makeText(this, "开启人脸对比时无法使用此功能", Toast.LENGTH_SHORT).show();
                } else {
                    faceRegListEdit();
                }

                break;
            case R.id.face_reg:
                openFaceRegPopupWindow();
                break;
            case R.id.reg_person:
                registeredPersonList = getPersonData();
                if (registeredPersonList == null || registeredPersonList.size() == 0) {
                    faceRegListNull.setVisibility(View.VISIBLE);
                    faceRegEdit.setVisibility(View.INVISIBLE);
                    drawerLayout.openDrawer(GravityCompat.END);
                    return;
                } else {
                    faceRegEdit.setVisibility(View.VISIBLE);
                    faceRegListNull.setVisibility(View.GONE);
                    isEdit = false;
                    faceRegEdit.setBackground(getResources().getDrawable(R.drawable.face_reg_edit, null));
                    for (PersonInfo personInfo : registeredPersonList) {
                        Log.d(TAG, "onClick: " + personInfo.getId() + personInfo.getAge() + personInfo.getGender());
                    }

                    personInfoAdapter = new PersonInfoAdapter(registeredPersonList, isEdit);

                    recyclerView.setAdapter(personInfoAdapter);
                    drawerLayout.openDrawer(GravityCompat.END);
                }
                break;
            case R.id.unsafe_acts:
                isDetectUnsafeActs = !isDetectUnsafeActs;
                if (isDetectUnsafeActs) {
                    faceDetectState = false;
                    isFaceCompare = false;
                    isOpenAlarm = false;
                    faceDetect.setChecked(false);
                    faceAlarm.setVisibility(View.GONE);
                    faceCompare.setVisibility(View.GONE);
                    if (mTTS != null) {
                        mTTS = null;
                    }
                    skeletonDetect1.setVisibility(View.VISIBLE);
                    // Configure the detector
                    detector = TensorFlowPoseDetector.create(
                            getAssets(),
                            MP_MODEL_FILE,
                            MP_INPUT_SIZE,
                            MP_INPUT_NAME,
                            new String[]{MP_OUTPUT_L1, MP_OUTPUT_L2}
                    );
                    /*try {
                        faceDetectCapture();
                        Thread.sleep(200);
                        drawFaceRect(faceRect, dispWidth, dispHeight);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }*/
                    skeletonDetect();

                } else {
                    skeletonDetect1.setVisibility(View.GONE);
                    unsafeActsAlarm.setVisibility(View.GONE);
                    unsafeActs.setChecked(false);
                    isDetectSkeleton = false;
                    isUnsafeActsAlarmOpen= false;
                    //
                    bitmap = Bitmap.createBitmap(dispWidth, dispHeight, Bitmap.Config.ARGB_8888);
                    faceRect.setImageBitmap(bitmap);
                }
                break;
            case R.id.skeleton_detect1:
                break;
            case R.id.unsafe_acts_alarm:
                break;
            case R.id.realplay_play_btn:
            case R.id.realplay_full_play_btn:
            case R.id.realplay_play_iv:
                if (mStatus != RealPlayStatus.STATUS_STOP) {
                    stopRealPlay();
                    setRealPlayStopUI();
                } else {
                    startRealPlay();
                }
                break;

            case R.id.realplay_previously_btn:
            case R.id.realplay_previously_btn2:
            case R.id.realplay_full_previously_btn:
                onCapturePicBtnClick();
                break;
            case R.id.realplay_capture_rl:
                onCaptureRlClick();
                break;
            case R.id.realplay_video_btn:
            case R.id.realplay_video_start_btn:
            case R.id.realplay_video_btn2:
            case R.id.realplay_video_start_btn2:
            case R.id.realplay_full_video_btn:
            case R.id.realplay_full_video_start_btn:
                onRecordBtnClick();
                break;
            case R.id.realplay_talk_btn:
            case R.id.realplay_talk_btn2:
            case R.id.realplay_full_talk_btn:
                startVoiceTalk();
                break;

            case R.id.realplay_quality_btn:
                openQualityPopupWindow(mRealPlayQualityBtn);
                break;
            case R.id.realplay_ptz_btn:
            case R.id.realplay_ptz_btn2:
                openPtzPopupWindow(mRealPlayPlayRl);
                break;
            case R.id.intelligent_detection:
                openFaceDetectionPopupWindow(mRealPlayPlayRl);
                break;
            case R.id.face_close_btn:
                closeFaceDetectionPopupWindow();
                break;
            case R.id.realplay_full_ptz_btn:
                setFullPtzStartUI(true);
                break;
            case R.id.realplay_full_ptz_anim_btn:
                setFullPtzStopUI(true);
                break;
            case R.id.realplay_sound_btn:
            case R.id.realplay_full_sound_btn:
                onSoundBtnClick();
                break;
            case R.id.realplay_full_talk_anim_btn:
                closeTalkPopupWindow(true, true);
                break;
            default:
                break;
        }
    }

    private void setFullPtzStartUI(boolean startAnim) {
        mIsOnPtz = true;
        setPlayScaleUI(1, null, null);
        if (mLocalInfo.getPtzPromptCount() < 3) {
            mRealPlayFullPtzPromptIv.setBackgroundResource(R.drawable.ptz_prompt);
            mRealPlayFullPtzPromptIv.setVisibility(View.VISIBLE);
            mLocalInfo.setPtzPromptCount(mLocalInfo.getPtzPromptCount() + 1);
            mHandler.removeMessages(MSG_CLOSE_PTZ_PROMPT);
            mHandler.sendEmptyMessageDelayed(MSG_CLOSE_PTZ_PROMPT, 2000);
        }
        if (startAnim) {
            mRealPlayFullAnimBtn.setBackgroundResource(R.drawable.yuntai_pressed);
            mRealPlayFullPtzBtn.getLocationInWindow(mStartXy);
            mEndXy[0] = Utils.dip2px(this, 20);
            mEndXy[1] = mStartXy[1];
            startFullBtnAnim(mRealPlayFullAnimBtn, mStartXy, mEndXy, new AnimationListener() {

                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    mRealPlayFullPtzAnimBtn.setVisibility(View.VISIBLE);
                    mRealPlayFullAnimBtn.setVisibility(View.GONE);
                    onRealPlaySvClick();
                    //                    mFullscreenFullButton.setVisibility(View.VISIBLE);
                }
            });
        } else {
            mRealPlayFullOperateBar.setVisibility(View.VISIBLE);
            mRealPlayFullOperateBar.post(new Runnable() {

                @Override
                public void run() {
                    mRealPlayFullPtzBtn.getLocationInWindow(mStartXy);
                    mEndXy[0] = Utils.dip2px(EZRealPlayActivity.this, 20);
                    mEndXy[1] = mStartXy[1];

                    mRealPlayFullOperateBar.setVisibility(View.GONE);
                    mRealPlayFullPtzAnimBtn.setVisibility(View.VISIBLE);
                    //                    mFullscreenFullButton.setVisibility(View.VISIBLE);
                }

            });
        }
    }

    private void setFullPtzStopUI(boolean startAnim) {
        mIsOnPtz = false;
        if (startAnim) {
            mRealPlayFullPtzAnimBtn.setVisibility(View.GONE);
            mFullscreenFullButton.setVisibility(View.GONE);
            mRealPlayFullAnimBtn.setBackgroundResource(R.drawable.yuntai_pressed);
            startFullBtnAnim(mRealPlayFullAnimBtn, mEndXy, mStartXy, new AnimationListener() {

                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    mRealPlayFullAnimBtn.setVisibility(View.GONE);
                    onRealPlaySvClick();
                }
            });
        } else {
            mRealPlayFullPtzAnimBtn.setVisibility(View.GONE);
            mFullscreenFullButton.setVisibility(View.GONE);
        }
        mRealPlayFullPtzPromptIv.setVisibility(View.GONE);
        mHandler.removeMessages(MSG_CLOSE_PTZ_PROMPT);
    }

    private void onSoundBtnClick() {
        if (mLocalInfo.isSoundOpen()) {
            mLocalInfo.setSoundOpen(false);
            mRealPlaySoundBtn.setBackgroundResource(R.drawable.ezopen_vertical_preview_sound_off_selector);
            if (mRealPlayFullSoundBtn != null) {
                mRealPlayFullSoundBtn.setBackgroundResource(R.drawable.play_full_soundoff_btn_selector);
            }
        } else {
            mLocalInfo.setSoundOpen(true);
            mRealPlaySoundBtn.setBackgroundResource(R.drawable.ezopen_vertical_preview_sound_selector);
            if (mRealPlayFullSoundBtn != null) {
                mRealPlayFullSoundBtn.setBackgroundResource(R.drawable.play_full_soundon_btn_selector);
            }
        }

        setRealPlaySound();
    }

    private void setRealPlaySound() {
        if (mEZPlayer != null) {
            if (mRtspUrl == null) {
                if (mLocalInfo.isSoundOpen()) {
                    mEZPlayer.openSound();
                } else {
                    mEZPlayer.closeSound();
                }
            } else {
                if (mRealPlaySquareInfo.mSoundType == 0) {
                    mEZPlayer.closeSound();
                } else {
                    mEZPlayer.openSound();
                }
            }
        }
    }

    private void startVoiceTalk() {
        LogUtil.debugLog(TAG, "startVoiceTalk");
        if (mEZPlayer == null) {
            LogUtil.debugLog(TAG, "EZPlaer is null");
            return;
        }
        if (mCameraInfo == null) {
            return;
        }
        mIsOnTalk = true;

        updateOrientation();

        Utils.showToast(this, R.string.start_voice_talk);
        mRealPlayTalkBtn.setEnabled(false);
        mRealPlayFullTalkBtn.setEnabled(false);
        mRealPlayFullTalkAnimBtn.setEnabled(false);
        if (mOrientation == Configuration.ORIENTATION_LANDSCAPE) {
            mRealPlayFullAnimBtn.setBackgroundResource(R.drawable.speech_1);
            mRealPlayFullTalkBtn.getLocationInWindow(mStartXy);
            mEndXy[0] = Utils.dip2px(this, 20);
            mEndXy[1] = mStartXy[1];
            startFullBtnAnim(mRealPlayFullAnimBtn, mStartXy, mEndXy, new AnimationListener() {

                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    Utils.showToast(EZRealPlayActivity.this, R.string.realplay_full_talk_start_tip);
                    mRealPlayFullTalkAnimBtn.setVisibility(View.VISIBLE);
                    mRealPlayFullAnimBtn.setVisibility(View.GONE);
                    onRealPlaySvClick();
                    //                    mFullscreenFullButton.setVisibility(View.VISIBLE);
                }
            });
        }

        if (mEZPlayer != null) {
            mEZPlayer.closeSound();
        }
        mEZPlayer.startVoiceTalk();
    }

    private void stopVoiceTalk(boolean startAnim) {
        if (mCameraInfo == null || mEZPlayer == null) {
            return;
        }
        LogUtil.debugLog(TAG, "stopVoiceTalk");

        mEZPlayer.stopVoiceTalk();
        handleVoiceTalkStoped(startAnim);
    }

    private OnClickListener mOnPopWndClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.quality_super_hd_btn:
                    setQualityMode(EZVideoLevel.VIDEO_LEVEL_SUPERCLEAR);
                    break;
                case R.id.quality_hd_btn:
                    setQualityMode(EZVideoLevel.VIDEO_LEVEL_HD);
                    break;
                case R.id.quality_balanced_btn:
                    setQualityMode(EZVideoLevel.VIDEO_LEVEL_BALANCED);
                    break;
                case R.id.quality_flunet_btn:
                    setQualityMode(EZVideoLevel.VIDEO_LEVEL_FLUNET);
                    break;
                case R.id.ptz_close_btn:
                    closePtzPopupWindow();
                    break;
                case R.id.ptz_flip_btn:
                    //                    setPtzFlip();
                    break;
                case R.id.talkback_close_btn:
                    closeTalkPopupWindow(true, false);
                    break;
                default:
                    break;
            }
        }
    };

    private void ptzOption(final EZPTZCommand command, final EZPTZAction action) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean ptz_result = false;
                try {
                    ptz_result = EzvizApplication.getOpenSDK().controlPTZ(mCameraInfo.getDeviceSerial(), mCameraInfo.getCameraNo(), command,
                            action, EZConstants.PTZ_SPEED_DEFAULT);
                } catch (BaseException e) {
                    e.printStackTrace();
                }
                LogUtil.i(TAG, "controlPTZ ptzCtrl result: " + ptz_result);
            }
        }).start();
    }

    private OnTouchListener mOnTouchListener = new OnTouchListener() {

        @Override
        public boolean onTouch(View view, MotionEvent motionevent) {
            boolean ptz_result = false;
            int action = motionevent.getAction();
            final int speed = EZConstants.PTZ_SPEED_DEFAULT;
            switch (action) {
                case MotionEvent.ACTION_DOWN:
                    switch (view.getId()) {
                        case R.id.talkback_control_btn:
                            mTalkRingView.setVisibility(View.VISIBLE);
                            mEZPlayer.setVoiceTalkStatus(true);
                            break;
                        case R.id.ptz_top_btn:
                            mPtzControlLy.setBackgroundResource(R.drawable.ptz_up_sel);
                            setPtzDirectionIv(RealPlayStatus.PTZ_UP);
                            ptzOption(EZPTZCommand.EZPTZCommandUp, EZPTZAction.EZPTZActionSTART);
                            break;
                        case R.id.ptz_bottom_btn:
                            mPtzControlLy.setBackgroundResource(R.drawable.ptz_bottom_sel);
                            setPtzDirectionIv(RealPlayStatus.PTZ_DOWN);
                            ptzOption(EZPTZCommand.EZPTZCommandDown, EZPTZAction.EZPTZActionSTART);
                            break;
                        case R.id.ptz_left_btn:
                            mPtzControlLy.setBackgroundResource(R.drawable.ptz_left_sel);
                            setPtzDirectionIv(RealPlayStatus.PTZ_LEFT);
                            ptzOption(EZPTZCommand.EZPTZCommandLeft, EZPTZAction.EZPTZActionSTART);
                            break;
                        case R.id.ptz_right_btn:
                            mPtzControlLy.setBackgroundResource(R.drawable.ptz_right_sel);
                            setPtzDirectionIv(RealPlayStatus.PTZ_RIGHT);
                            ptzOption(EZPTZCommand.EZPTZCommandRight, EZPTZAction.EZPTZActionSTART);
                            break;
                        default:
                            break;
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    switch (view.getId()) {
                        case R.id.talkback_control_btn:
                            mEZPlayer.setVoiceTalkStatus(false);
                            mTalkRingView.setVisibility(View.GONE);
                            break;
                        case R.id.ptz_top_btn:
                            mPtzControlLy.setBackgroundResource(R.drawable.ptz_bg);
                            ptzOption(EZPTZCommand.EZPTZCommandUp, EZPTZAction.EZPTZActionSTOP);
                            break;
                        case R.id.ptz_bottom_btn:
                            mPtzControlLy.setBackgroundResource(R.drawable.ptz_bg);
                            ptzOption(EZPTZCommand.EZPTZCommandDown, EZPTZAction.EZPTZActionSTOP);
                            break;
                        case R.id.ptz_left_btn:
                            mPtzControlLy.setBackgroundResource(R.drawable.ptz_bg);
                            ptzOption(EZPTZCommand.EZPTZCommandLeft, EZPTZAction.EZPTZActionSTOP);
                            break;
                        case R.id.ptz_right_btn:
                            mPtzControlLy.setBackgroundResource(R.drawable.ptz_bg);
                            ptzOption(EZPTZCommand.EZPTZCommandRight, EZPTZAction.EZPTZActionSTOP);
                            break;
                        default:
                            break;
                    }
                    break;
                default:
                    break;
            }
            return false;
        }
    };

    public void setSoundLocalization(int status) {
    }

    private void setQualityMode(final EZVideoLevel mode) {
        // 检查网络是否可用 Check if the network is available
        if (!ConnectionDetector.isNetworkAvailable(EZRealPlayActivity.this)) {
            // 提示没有连接网络 Prompt not to connect to the network
            Utils.showToast(EZRealPlayActivity.this, R.string.realplay_set_fail_network);
            return;
        }

        if (mEZPlayer != null) {
            mWaitDialog.setWaitText(this.getString(R.string.setting_video_level));
            mWaitDialog.show();

            Thread thr = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        // need to modify by yudan at 08-11
                        EzvizApplication.getOpenSDK().setVideoLevel(mCameraInfo.getDeviceSerial(), mCameraInfo.getCameraNo(), mode.getVideoLevel());
                        mCurrentQulityMode = mode;
                        Message msg = Message.obtain();
                        msg.what = MSG_SET_VEDIOMODE_SUCCESS;
                        mHandler.sendMessage(msg);
                        LogUtil.i(TAG, "setQualityMode success");
                    } catch (BaseException e) {
                        mCurrentQulityMode = EZVideoLevel.VIDEO_LEVEL_FLUNET;
                        e.printStackTrace();
                        Message msg = Message.obtain();
                        msg.what = MSG_SET_VEDIOMODE_FAIL;
                        mHandler.sendMessage(msg);
                        LogUtil.i(TAG, "setQualityMode fail");
                    }

                }
            }) {
            };
            thr.start();
        }
    }

    private void openTalkPopupWindow(boolean showAnimation) {
        if (mEZPlayer == null && mDeviceInfo == null) {
            return;
        }
        closeTalkPopupWindow(false, false);
        LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewGroup layoutView = (ViewGroup) layoutInflater.inflate(R.layout.realplay_talkback_wnd, null, true);
        layoutView.setFocusable(true);
        layoutView.setFocusableInTouchMode(true);
        layoutView.setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(View arg0, int arg1, KeyEvent arg2) {
                if (arg1 == KeyEvent.KEYCODE_BACK) {
                    LogUtil.infoLog(TAG, "KEYCODE_BACK DOWN");
                    closeTalkPopupWindow(true, false);
                }
                return false;
            }
        });

        ImageButton talkbackCloseBtn = (ImageButton) layoutView.findViewById(R.id.talkback_close_btn);
        talkbackCloseBtn.setOnClickListener(mOnPopWndClickListener);
        mTalkRingView = (RingView) layoutView.findViewById(R.id.talkback_rv);
        mTalkBackControlBtn = (Button) layoutView.findViewById(R.id.talkback_control_btn);
        mTalkBackControlBtn.setOnTouchListener(mOnTouchListener);

        if (mDeviceInfo.isSupportTalk() == EZConstants.EZTalkbackCapability.EZTalkbackFullDuplex) {
            mTalkRingView.setVisibility(View.VISIBLE);
            mTalkBackControlBtn.setEnabled(false);
            mTalkBackControlBtn.setText(R.string.talking);
        }

        int height = mLocalInfo.getScreenHeight() - mPortraitTitleBar.getHeight() - mRealPlayPlayRl.getHeight()
                - (mRealPlayRect != null ? mRealPlayRect.top : mLocalInfo.getNavigationBarHeight());
        mTalkPopupWindow = new PopupWindow(layoutView, LayoutParams.MATCH_PARENT, height, true);
        // mTalkPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        if (showAnimation) {
            mTalkPopupWindow.setAnimationStyle(R.style.popwindowUpAnim);
        }
        mTalkPopupWindow.setFocusable(false);
        mTalkPopupWindow.setOutsideTouchable(false);
        mTalkPopupWindow.showAsDropDown(mRealPlayPlayRl);
        // mTalkPopupWindow.setOnDismissListener(new OnDismissListener() {
        //
        // @Override
        // public void onDismiss() {
        // LogUtil.infoLog(TAG, "KEYCODE_BACK DOWN");
        // mTalkPopupWindow = null;
        // closeTalkPopupWindow();
        // }
        // });
        mTalkPopupWindow.update();
        mTalkRingView.post(new Runnable() {
            @Override
            public void run() {
                if (mTalkRingView != null) {
                    mTalkRingView.setMinRadiusAndDistance(mTalkBackControlBtn.getHeight() / 2f,
                            Utils.dip2px(EZRealPlayActivity.this, 22));
                }
            }
        });
    }

    private void closeTalkPopupWindow(boolean stopTalk, boolean startAnim) {
        if (mTalkPopupWindow != null) {
            LogUtil.infoLog(TAG, "closeTalkPopupWindow");
            dismissPopWindow(mTalkPopupWindow);
            mTalkPopupWindow = null;
        }
        mTalkRingView = null;
        if (stopTalk)
            stopVoiceTalk(startAnim);
    }

    private void openPtzPopupWindow(View parent) {
        closePtzPopupWindow();
        mIsOnPtz = true;
        setPlayScaleUI(1, null, null);

        LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewGroup layoutView = (ViewGroup) layoutInflater.inflate(R.layout.realplay_ptz_wnd, null, true);

        mPtzControlLy = (LinearLayout) layoutView.findViewById(R.id.ptz_control_ly);
        ImageButton ptzCloseBtn = (ImageButton) layoutView.findViewById(R.id.ptz_close_btn);
        ptzCloseBtn.setOnClickListener(mOnPopWndClickListener);
        ImageButton ptzTopBtn = (ImageButton) layoutView.findViewById(R.id.ptz_top_btn);
        ptzTopBtn.setOnTouchListener(mOnTouchListener);
        ImageButton ptzBottomBtn = (ImageButton) layoutView.findViewById(R.id.ptz_bottom_btn);
        ptzBottomBtn.setOnTouchListener(mOnTouchListener);
        ImageButton ptzLeftBtn = (ImageButton) layoutView.findViewById(R.id.ptz_left_btn);
        ptzLeftBtn.setOnTouchListener(mOnTouchListener);
        ImageButton ptzRightBtn = (ImageButton) layoutView.findViewById(R.id.ptz_right_btn);
        ptzRightBtn.setOnTouchListener(mOnTouchListener);
        ImageButton ptzFlipBtn = (ImageButton) layoutView.findViewById(R.id.ptz_flip_btn);
        ptzFlipBtn.setOnClickListener(mOnPopWndClickListener);

        int height = mLocalInfo.getScreenHeight() - mPortraitTitleBar.getHeight() - mRealPlayPlayRl.getHeight()
                - (mRealPlayRect != null ? mRealPlayRect.top : mLocalInfo.getNavigationBarHeight());
        mPtzPopupWindow = new PopupWindow(layoutView, LayoutParams.MATCH_PARENT, height, true);
        mPtzPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        mPtzPopupWindow.setAnimationStyle(R.style.popwindowUpAnim);
        mPtzPopupWindow.setFocusable(true);
        mPtzPopupWindow.setOutsideTouchable(true);
        mPtzPopupWindow.showAsDropDown(parent);
        mPtzPopupWindow.setOnDismissListener(new OnDismissListener() {

            @Override
            public void onDismiss() {
                LogUtil.infoLog(TAG, "KEYCODE_BACK DOWN");
                mPtzPopupWindow = null;
                mPtzControlLy = null;
                closePtzPopupWindow();
            }
        });
        mPtzPopupWindow.update();
    }

    private void closePtzPopupWindow() {
        mIsOnPtz = false;
        if (mPtzPopupWindow != null) {
            dismissPopWindow(mPtzPopupWindow);
            mPtzPopupWindow = null;
            mPtzControlLy = null;
            setForceOrientation(0);
        }
    }

    private void openFaceDetectionPopupWindow(View parent) {
        closeFaceDetectionPopupWindow();

        View layoutView = View.inflate(this, R.layout.realplay_facedetection_wnd, null);
        if (faceDetect == null || faceCompare == null || faceAlarm == null) {
            faceDetect = layoutView.findViewById(R.id.face_detect);
            faceDetect.setOnClickListener(this);
            faceCompare = layoutView.findViewById(R.id.face_compare);
            faceCompare.setOnClickListener(this);
            faceAlarm = layoutView.findViewById(R.id.face_alarm);
            faceAlarm.setOnClickListener(this);
            if (!faceDetectState) {
                faceCompare.setVisibility(View.GONE);
                faceAlarm.setVisibility(View.GONE);
            }
        } else {
            faceDetect = layoutView.findViewById(R.id.face_detect);
            faceDetect.setOnClickListener(this);
            faceCompare = layoutView.findViewById(R.id.face_compare);
            faceCompare.setOnClickListener(this);
            faceAlarm = layoutView.findViewById(R.id.face_alarm);
            faceAlarm.setOnClickListener(this);
            if (faceDetectState) {
                faceDetect.setChecked(true);
                faceCompare.setVisibility(View.VISIBLE);
                if (isFaceCompare) {
                    faceCompare.setChecked(true);
                    if (isOpenAlarm) {
                        faceAlarm.setChecked(true);
                    } else {
                        faceAlarm.setChecked(false);
                    }
                } else {
                    faceCompare.setChecked(false);
                }
            } else {
                faceDetect.setChecked(false);
                faceCompare.setVisibility(View.GONE);
                faceAlarm.setVisibility(View.GONE);
            }
        }

        if (unsafeActs == null || skeletonDetect1 == null || unsafeActsAlarm == null) {
            unsafeActs = layoutView.findViewById(R.id.unsafe_acts);
            unsafeActs.setOnClickListener(this);
            skeletonDetect1 = layoutView.findViewById(R.id.skeleton_detect1);
            skeletonDetect1.setOnClickListener(this);
            unsafeActsAlarm = layoutView.findViewById(R.id.unsafe_acts_alarm);
            unsafeActsAlarm.setOnClickListener(this);
            if (!isDetectUnsafeActs){
                skeletonDetect1.setVisibility(View.GONE);
                unsafeActsAlarm.setVisibility(View.GONE);
            }
        }else {
            unsafeActs = layoutView.findViewById(R.id.unsafe_acts);
            unsafeActs.setOnClickListener(this);
            skeletonDetect1 = layoutView.findViewById(R.id.skeleton_detect1);
            skeletonDetect1.setOnClickListener(this);
            unsafeActsAlarm = layoutView.findViewById(R.id.unsafe_acts_alarm);
            unsafeActsAlarm.setOnClickListener(this);
            if (isDetectUnsafeActs) {
                skeletonDetect1.setVisibility(View.VISIBLE);
                unsafeActs.setChecked(true);
                if (isDetectSkeleton) {
                    unsafeActsAlarm.setVisibility(View.VISIBLE);
                    skeletonDetect1.setChecked(true);
                    if (isUnsafeActsAlarmOpen) {
                        unsafeActsAlarm.setChecked(true);
                    } else {
                        unsafeActsAlarm.setChecked(false);
                    }
                } else {
                    skeletonDetect1.setChecked(false);
                }
            }else {
                unsafeActs.setChecked(false);
                skeletonDetect1.setVisibility(View.GONE);
                unsafeActsAlarm.setVisibility(View.GONE);
            }
        }
        layoutView.findViewById(R.id.face_reg).setOnClickListener(this);
        layoutView.findViewById(R.id.face_close_btn).setOnClickListener(this);
        layoutView.findViewById(R.id.skeleton_detect).setOnClickListener(this);
            int height = mLocalInfo.getScreenHeight() - mPortraitTitleBar.getHeight() - mRealPlayPlayRl.getHeight()
                    - (mRealPlayRect != null ? mRealPlayRect.top : mLocalInfo.getNavigationBarHeight());
            mFaceDetectionWindow = new PopupWindow(layoutView, LayoutParams.MATCH_PARENT, height, true);
            mFaceDetectionWindow.setAnimationStyle(R.style.popwindowUpAnim);
            mFaceDetectionWindow.setOutsideTouchable(true);
            mFaceDetectionWindow.showAsDropDown(parent);
            mFaceDetectionWindow.setOnDismissListener(new OnDismissListener() {
                @Override
                public void onDismiss() {
                    mFaceDetectionWindow = null;
                    closeFaceDetectionPopupWindow();
                }
            });
            mFaceDetectionWindow.update();

    }

    private void closeFaceDetectionPopupWindow() {
        if (mFaceDetectionWindow != null) {
            dismissPopWindow(mFaceDetectionWindow);
            mFaceDetectionWindow = null;
        }
    }

    private void openFaceRegPopupWindow() {
        if ((registeredPersonList = getPersonData()) == null) {
            registeredPersonList = new ArrayList<>();
        }

        View view = View.inflate(this, R.layout.realplay_facereg_wnd, null);
        Button album = view.findViewById(R.id.btn_pop_album);
        Button camrea = view.findViewById(R.id.btn_pop_camera);
        Button cancel = view.findViewById(R.id.btn_pop_cancel);

        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels / 3;
        PopupWindow popupWindow = new PopupWindow(view, width, LayoutParams.WRAP_CONTENT);
        popupWindow.setAnimationStyle(R.style.popwindowUpAnim);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);

        album.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageSelector.getInstance()
                        .setSelectModel(ImageSelector.MULTI_MODE)
                        .setMaxCount(5)
                        .setGridColumns(3)
                        .setShowCamera(false)
                        .startSelect(EZRealPlayActivity.this);
            }
        });
        camrea.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                //intent.putExtra("android.intent.extras.CAMERA_FACING", 1); // 调用前置摄像头

                startActivityForResult(intent, ImageSelector.REQUEST_OPEN_CAMERA);
            }
        });
        cancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
            }
        });

        popupWindow.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
                layoutParams.alpha = 1.0f;
                getWindow().setAttributes(layoutParams);
            }
        });
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.alpha = 0.4f;
        getWindow().setAttributes(layoutParams);
        popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
    }



    private void openQualityPopupWindow(View anchor) {
        if (mEZPlayer == null) {
            return;
        }
        closeQualityPopupWindow();
        LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewGroup layoutView = (ViewGroup) layoutInflater.inflate(R.layout.realplay_quality_items, null, true);

        Button qualitySuperHdBtn = (Button) layoutView.findViewById(R.id.quality_super_hd_btn);
        qualitySuperHdBtn.setOnClickListener(mOnPopWndClickListener);
        Button qualityHdBtn = (Button) layoutView.findViewById(R.id.quality_hd_btn);
        qualityHdBtn.setOnClickListener(mOnPopWndClickListener);
        Button qualityBalancedBtn = (Button) layoutView.findViewById(R.id.quality_balanced_btn);
        qualityBalancedBtn.setOnClickListener(mOnPopWndClickListener);
        Button qualityFlunetBtn = (Button) layoutView.findViewById(R.id.quality_flunet_btn);
        qualityFlunetBtn.setOnClickListener(mOnPopWndClickListener);

        qualityFlunetBtn.setVisibility(View.GONE);
        qualityBalancedBtn.setVisibility(View.GONE);
        qualityHdBtn.setVisibility(View.GONE);
        qualitySuperHdBtn.setVisibility(View.GONE);
        // 清晰度 0-流畅，1-均衡，2-高清，3-超清
        for (EZVideoQualityInfo qualityInfo: mCameraInfo.getVideoQualityInfos()){
            if (mCameraInfo.getVideoLevel().getVideoLevel() == qualityInfo.getVideoLevel()){
                // 当前清晰度不添加到可切换清晰度列表中
                continue;
            }
            switch (qualityInfo.getVideoLevel()){
                case 0:
                    qualityFlunetBtn.setVisibility(View.VISIBLE);
                    break;
                case 1:
                    qualityBalancedBtn.setVisibility(View.VISIBLE);
                    break;
                case 2:
                    qualityHdBtn.setVisibility(View.VISIBLE);
                    break;
                case 3:
                    qualitySuperHdBtn.setVisibility(View.VISIBLE);
                    break;
                default:break;
            }
        }

        mQualityPopupWindow = new PopupWindow(layoutView, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, true);
        mQualityPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        mQualityPopupWindow.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                LogUtil.infoLog(TAG, "KEYCODE_BACK DOWN");
                mQualityPopupWindow = null;
                closeQualityPopupWindow();
            }
        });
        try {
            int widthMode = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
            int heightMode = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
            mQualityPopupWindow.getContentView().measure(widthMode, heightMode);
            int yOffset = -(anchor.getHeight() + mQualityPopupWindow.getContentView().getMeasuredHeight());
            mQualityPopupWindow.showAsDropDown(anchor, 0, yOffset);
        } catch (Exception e) {
            e.printStackTrace();
            closeQualityPopupWindow();
        }
    }

    private void closeQualityPopupWindow() {
        if (mQualityPopupWindow != null) {
            dismissPopWindow(mQualityPopupWindow);
            mQualityPopupWindow = null;
        }
    }

    private String mCurrentRecordPath = null;
    private void onRecordBtnClick() {
        mControlDisplaySec = 0;
        if (isRecording) {
            stopRealPlayRecord();
            return;
        }

        if (!SDCardUtil.isSDCardUseable()) {
            // 提示SD卡不可用
            //Prompt SD card is not available
            Utils.showToast(EZRealPlayActivity.this, R.string.remoteplayback_SDCard_disable_use);
            return;
        }

        if (SDCardUtil.getSDCardRemainSize() < SDCardUtil.PIC_MIN_MEM_SPACE) {
            // 提示内存不足
            //Prompt for insufficient memory
            Utils.showToast(EZRealPlayActivity.this, R.string.remoteplayback_record_fail_for_memory);
            return;
        }

        if (mEZPlayer != null) {
            final String strRecordFile = DemoConfig.getRecordsFolder() +"/" + System.currentTimeMillis() + ".mp4";
            LogUtil.i(TAG, "recorded video file path is " + strRecordFile);
            mEZPlayer.setStreamDownloadCallback(new EZOpenSDKListener.EZStreamDownloadCallback() {
                @Override
                public void onSuccess(String filepath) {
                    LogUtil.i(TAG, "EZStreamDownloadCallback onSuccess " + filepath);
                    dialog("Record result", "saved to " + mCurrentRecordPath);
                }

                @Override
                public void onError(EZOpenSDKListener.EZStreamDownloadError code) {
                    LogUtil.e(TAG, "EZStreamDownloadCallback onError " + code.name());
                }
            });
            if(mEZPlayer.startLocalRecordWithFile(strRecordFile)){
                isRecording = true;
                mCurrentRecordPath = strRecordFile;
                mCaptureDisplaySec = 4;
                updateCaptureUI();
                mAudioPlayUtil.playAudioFile(AudioPlayUtil.RECORD_SOUND);
                handleRecordSuccess(strRecordFile);
            }else{
                handleRecordFail();
            }
        }
    }

    private void stopRealPlayRecord() {
        if (mEZPlayer == null || !isRecording) {
            return;
        }
        // 设置录像按钮为check状态
        //Set the recording button to the check status
        if (mOrientation == Configuration.ORIENTATION_PORTRAIT) {
            if (!mIsOnStop) {
                mRecordRotateViewUtil.applyRotation(mRealPlayRecordContainer, mRealPlayRecordStartBtn,
                        mRealPlayRecordBtn, 0, 90);
            } else {
                mRealPlayRecordStartBtn.setVisibility(View.GONE);
                mRealPlayRecordBtn.setVisibility(View.VISIBLE);
            }
            mRealPlayFullRecordStartBtn.setVisibility(View.GONE);
            mRealPlayFullRecordBtn.setVisibility(View.VISIBLE);
        } else {
            if (!mIsOnStop) {
                mRecordRotateViewUtil.applyRotation(mRealPlayFullRecordContainer, mRealPlayFullRecordStartBtn,
                        mRealPlayFullRecordBtn, 0, 90);
            } else {
                mRealPlayFullRecordStartBtn.setVisibility(View.GONE);
                mRealPlayFullRecordBtn.setVisibility(View.VISIBLE);

            }
            mRealPlayRecordStartBtn.setVisibility(View.GONE);
            mRealPlayRecordBtn.setVisibility(View.VISIBLE);
        }
        mAudioPlayUtil.playAudioFile(AudioPlayUtil.RECORD_SOUND);
        mEZPlayer.stopLocalRecord();

        // 计时按钮不可见
        //The timed button is not visible
        mRealPlayRecordLy.setVisibility(View.GONE);
        mCaptureDisplaySec = 0;
        updateCaptureUI();
        isRecording = false;
    }

    private void onCaptureRlClick() {
    }

    private void onCapturePicBtnClick() {

        mControlDisplaySec = 0;
        if (!SDCardUtil.isSDCardUseable()) {
            // 提示SD卡不可用
            //Prompt SD card is not available
            Utils.showToast(EZRealPlayActivity.this, R.string.remoteplayback_SDCard_disable_use);
            return;
        }

        if (SDCardUtil.getSDCardRemainSize() < SDCardUtil.PIC_MIN_MEM_SPACE) {
            // 提示内存不足
            //Prompt for insufficient memory
            Utils.showToast(EZRealPlayActivity.this, R.string.remoteplayback_capture_fail_for_memory);
            return;
        }

        if (mEZPlayer != null) {
            mCaptureDisplaySec = 4;
            updateCaptureUI();

            Thread thr = new Thread() {
                @Override
                public void run() {
                    Bitmap bmp = mEZPlayer.capturePicture();
                    if (bmp != null) {
                        try {
                            mAudioPlayUtil.playAudioFile(AudioPlayUtil.CAPTURE_SOUND);

                            final String strCaptureFile = DemoConfig.getCapturesFolder() + "/" + System.currentTimeMillis() + ".jpg";
                            LogUtil.e(TAG, "captured picture file path is " + strCaptureFile);

                            if (TextUtils.isEmpty(strCaptureFile)) {
                                bmp.recycle();
                                bmp = null;
                                return;
                            }
                            EZUtils.saveCapturePictrue(strCaptureFile, bmp);


                            MediaScanner mMediaScanner = new MediaScanner(EZRealPlayActivity.this);
                            mMediaScanner.scanFile(strCaptureFile, "jpg");
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(EZRealPlayActivity.this, getResources().getString(R.string.already_saved_to_volume)+strCaptureFile, Toast.LENGTH_SHORT).show();
                                }
                            });
                        } catch (InnerException e) {
                            e.printStackTrace();
                        } finally {
                            if (bmp != null) {
                                bmp.recycle();
                                bmp = null;
                                return;
                            }
                        }
                    }else{
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), "抓图失败, 检查是否开启了硬件解码",
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    super.run();
                }
            };
            thr.start();
        }
    }

    private void onRealPlaySvClick() {
        if (mCameraInfo != null && mEZPlayer != null && mDeviceInfo != null) {
            if (mDeviceInfo.getStatus() != 1) {
                return;
            }
            if (mOrientation == Configuration.ORIENTATION_PORTRAIT) {
                setRealPlayControlRlVisibility();
            } else {
                setRealPlayFullOperateBarVisibility();
            }
        } else if (mRtspUrl != null) {
            setRealPlayControlRlVisibility();
        }
    }

    private void setRealPlayControlRlVisibility() {
        if (mLandscapeTitleBar.getVisibility() == View.VISIBLE || mRealPlayControlRl.getVisibility() == View.VISIBLE) {
            //            mRealPlayControlRl.setVisibility(View.GONE);
            mLandscapeTitleBar.setVisibility(View.GONE);
            closeQualityPopupWindow();
        } else {
            mRealPlayControlRl.setVisibility(View.VISIBLE);
            if (mOrientation == Configuration.ORIENTATION_LANDSCAPE) {
                if (!mIsOnTalk && !mIsOnPtz) {
                    mLandscapeTitleBar.setVisibility(View.VISIBLE);
                }
            } else {
                mLandscapeTitleBar.setVisibility(View.GONE);
            }
            mControlDisplaySec = 0;
        }
    }

    private void setRealPlayFullOperateBarVisibility() {
        if (mLandscapeTitleBar.getVisibility() == View.VISIBLE) {
            mRealPlayFullOperateBar.setVisibility(View.GONE);
            if (!mIsOnTalk && !mIsOnPtz) {
                mFullscreenFullButton.setVisibility(View.GONE);
            }
            mLandscapeTitleBar.setVisibility(View.GONE);
        } else {
            if (!mIsOnTalk && !mIsOnPtz) {
                //mj mRealPlayFullOperateBar.setVisibility(View.VISIBLE);
                //                mFullscreenFullButton.setVisibility(View.VISIBLE);
                mLandscapeTitleBar.setVisibility(View.VISIBLE);
            }
            mControlDisplaySec = 0;
        }
    }

    private void startRealPlay() {
        // 增加手机客户端操作信息记录
        //Increase the mobile client operation information record
        LogUtil.debugLog(TAG, "startRealPlay");

        if (mStatus == RealPlayStatus.STATUS_START || mStatus == RealPlayStatus.STATUS_PLAY) {
            return;
        }

        // 检查网络是否可用
        //Check if the network is available
        if (!ConnectionDetector.isNetworkAvailable(this)) {
            // 提示没有连接网络
            //Prompt not to connect to the network
            setRealPlayFailUI(getString(R.string.realplay_play_fail_becauseof_network));
            return;
        }

        mStatus = RealPlayStatus.STATUS_START;
        setRealPlayLoadingUI();

        if (mCameraInfo != null) {
                mEZPlayer = EzvizApplication.getOpenSDK().createPlayer(mCameraInfo.getDeviceSerial(), mCameraInfo.getCameraNo());
            if (mEZPlayer == null)
                return;
            if (mDeviceInfo == null) {
                return;
            }

            mEZPlayer.setPlayVerifyCode(DataManager.getInstance().getDeviceSerialVerifyCode(mCameraInfo.getDeviceSerial()));
//            if (mDeviceInfo.getIsEncrypt() == 1) {
//                mEZPlayer.setPlayVerifyCode(DataManager.getInstance().getDeviceSerialVerifyCode(mCameraInfo.getDeviceSerial()));
//            }

            mEZPlayer.setHandler(mHandler);
            mEZPlayer.setSurfaceHold(mRealPlaySh);

            // 不建议使用，会导致抓图功能失效
//            mEZPlayer.setHardDecode(true);

            startRecordOriginVideo();

            mEZPlayer.startRealPlay();
        } else if (mRtspUrl != null) {
            mEZPlayer = EzvizApplication.getOpenSDK().createPlayerWithUrl(mRtspUrl);
            if (mEZPlayer == null)
                return;
            mEZPlayer.setHandler(mHandler);
            mEZPlayer.setSurfaceHold(mRealPlaySh);

            // 不建议使用，会导致抓图功能失效
//            mEZPlayer.setHardDecode(true);

            startRecordOriginVideo();

            mEZPlayer.startRealPlay();
        }
        updateLoadingProgress(0);
    }

    private void startRecordOriginVideo(){
        String fileName = LocalInfo.getInstance().getFilePath() + "/origin_video_real_play.ps";
        VideoFileUtil.startRecordOriginVideo(mEZPlayer,fileName);
    }

    private void stopRealPlay() {
        LogUtil.debugLog(TAG, "stopRealPlay");
        mStatus = RealPlayStatus.STATUS_STOP;

        stopUpdateTimer();
        if (mEZPlayer != null) {
            stopRealPlayRecord();
            mEZPlayer.stopRealPlay();
        }
        mStreamFlow = 0;
    }
   //未加载好时按钮为灰色
    private void setRealPlayLoadingUI() {
        mStartTime = System.currentTimeMillis();
        setStartloading();
        mRealPlayBtn.setBackgroundResource(R.drawable.play_stop_selector);

        if (mCameraInfo != null  && mDeviceInfo != null) {
            mRealPlayCaptureBtn.setEnabled(false);
            mRealPlayRecordBtn.setEnabled(false);
            mRealPlayIntelligentDetectionBtn.setEnabled(false);
            //mRealPlayIntelligentDetectionBtn.setEnabled(false);
            if (mDeviceInfo.getStatus() == 1) {
                mRealPlayQualityBtn.setEnabled(true);
            } else {
                mRealPlayQualityBtn.setEnabled(false);
            }
            mRealPlayPtzBtn.setEnabled(false);

            mRealPlayFullPlayBtn.setBackgroundResource(R.drawable.play_full_stop_selector);
            mRealPlayFullCaptureBtn.setEnabled(false);
            mRealPlayFullRecordBtn.setEnabled(false);
            mRealPlayFullFlowLy.setVisibility(View.GONE);
            mRealPlayFullPtzBtn.setEnabled(false);
           // mRealPlayFullIntelligentDetectionBtn.setEnabled(false);
        }

        showControlRlAndFullOperateBar();
    }

    private void showControlRlAndFullOperateBar() {
        if (mRtspUrl != null || mOrientation == Configuration.ORIENTATION_PORTRAIT) {
            mRealPlayControlRl.setVisibility(View.VISIBLE);
            if (mOrientation == Configuration.ORIENTATION_LANDSCAPE) {
                if (!mIsOnTalk && !mIsOnPtz) {
                    mLandscapeTitleBar.setVisibility(View.VISIBLE);
                }
            } else {
                mLandscapeTitleBar.setVisibility(View.GONE);
            }
            mControlDisplaySec = 0;
        } else {
            if (!mIsOnTalk && !mIsOnPtz) {
                mRealPlayFullOperateBar.setVisibility(View.VISIBLE);
                //                mFullscreenFullButton.setVisibility(View.VISIBLE);
                mLandscapeTitleBar.setVisibility(View.VISIBLE);
            }
            mControlDisplaySec = 0;
        }
    }

    private void setRealPlayStopUI() {
        stopUpdateTimer();
        updateOrientation();
        setRealPlaySvLayout();
        setStopLoading();
        hideControlRlAndFullOperateBar(true);
        mRealPlayBtn.setBackgroundResource(R.drawable.play_play_selector);
        if (mCameraInfo != null && mDeviceInfo != null) {
            closePtzPopupWindow();
            setFullPtzStopUI(false);
            mRealPlayCaptureBtn.setEnabled(false);
            mRealPlayRecordBtn.setEnabled(false);
           // mRealPlayIntelligentDetectionBtn.setEnabled(false);
            if (mDeviceInfo.getStatus() == 1) {
                mRealPlayQualityBtn.setEnabled(true);
            } else {
                mRealPlayQualityBtn.setEnabled(false);
            }
            mRealPlayFullPtzBtn.setEnabled(false);
            if (mDeviceInfo.getStatus() == 1) {
                mRealPlayPrivacyBtn.setEnabled(true);
                mRealPlaySslBtn.setEnabled(true);
            } else {
                mRealPlayPrivacyBtn.setEnabled(false);
                mRealPlaySslBtn.setEnabled(false);
            }

            mRealPlayFullPlayBtn.setBackgroundResource(R.drawable.play_full_play_selector);
            mRealPlayFullCaptureBtn.setEnabled(false);
            mRealPlayFullRecordBtn.setEnabled(false);
            mRealPlayPtzBtn.setEnabled(false);
            //mRealPlayFullIntelligentDetectionBtn.setEnabled(false);
        }
    }

    private void setRealPlayFailUI(String errorStr) {
        mStopTime = System.currentTimeMillis();
        showType();

        stopUpdateTimer();
        updateOrientation();

        {
            setLoadingFail(errorStr);
        }
        mRealPlayFullFlowLy.setVisibility(View.GONE);
        mRealPlayBtn.setBackgroundResource(R.drawable.play_play_selector);

        hideControlRlAndFullOperateBar(true);

        if (mCameraInfo != null && mDeviceInfo != null) {
            closePtzPopupWindow();
            setFullPtzStopUI(false);

            mRealPlayCaptureBtn.setEnabled(false);
            mRealPlayRecordBtn.setEnabled(false);
            //mRealPlayIntelligentDetectionBtn.setEnabled(false);
            if (mDeviceInfo.getStatus() == 1 && (mEZPlayer == null)) {
                mRealPlayQualityBtn.setEnabled(true);
            } else {
                mRealPlayQualityBtn.setEnabled(false);
            }
            mRealPlayPtzBtn.setEnabled(false);
            if (mDeviceInfo.getStatus() == 1) {
                mRealPlayPrivacyBtn.setEnabled(true);
                mRealPlaySslBtn.setEnabled(true);
            } else {
                mRealPlayPrivacyBtn.setEnabled(false);
                mRealPlaySslBtn.setEnabled(false);
            }

            mRealPlayFullPlayBtn.setBackgroundResource(R.drawable.play_full_play_selector);
            mRealPlayFullCaptureBtn.setEnabled(false);
            mRealPlayFullRecordBtn.setEnabled(false);
            mRealPlayFullPtzBtn.setEnabled(false);
           // mRealPlayFullIntelligentDetectionBtn.setEnabled(false);
        }
    }

    private void setRealPlaySuccessUI() {
        mStopTime = System.currentTimeMillis();
        showType();

        updateOrientation();
        setLoadingSuccess();
        mRealPlayFlowTv.setVisibility(View.VISIBLE);
        mRealPlayFullFlowLy.setVisibility(View.VISIBLE);
        mRealPlayBtn.setBackgroundResource(R.drawable.play_stop_selector);

        if (mCameraInfo != null && mDeviceInfo != null) {
            mRealPlayCaptureBtn.setEnabled(true);
            mRealPlayRecordBtn.setEnabled(true);
            mRealPlayIntelligentDetectionBtn.setEnabled(true);
            if (mDeviceInfo.getStatus() == 1) {
                mRealPlayQualityBtn.setEnabled(true);
            } else {
                mRealPlayQualityBtn.setEnabled(false);
            }
            if (getSupportPtz() == 1) {
                mRealPlayPtzBtn.setEnabled(true);
            }

            mRealPlayFullPlayBtn.setBackgroundResource(R.drawable.play_full_stop_selector);
            mRealPlayFullCaptureBtn.setEnabled(true);
            mRealPlayFullRecordBtn.setEnabled(true);
            mRealPlayFullPtzBtn.setEnabled(true);
          //  mRealPlayFullIntelligentDetectionBtn.setEnabled(true);
        }

//        setRealPlaySound();

        startUpdateTimer();
    }

    private void checkRealPlayFlow() {
        if ((mEZPlayer != null && mRealPlayFlowTv.getVisibility() == View.VISIBLE)) {
            // 更新流量数据
            //Update traffic data
            long streamFlow = mEZPlayer.getStreamFlow();
            updateRealPlayFlowTv(streamFlow);
        }
    }

    private void updateRealPlayFlowTv(long streamFlow) {
        long streamFlowUnit = streamFlow - mStreamFlow;
        if (streamFlowUnit < 0)
            streamFlowUnit = 0;
        float fKBUnit = (float) streamFlowUnit / (float) Constant.KB;
        String descUnit = String.format("%.2f k/s ", fKBUnit);
        mRealPlayFlowTv.setText(descUnit);
        mStreamFlow = streamFlow;
    }


    private void setOrientation(int sensor) {
        if (mForceOrientation != 0) {
            LogUtil.debugLog(TAG, "setOrientation mForceOrientation:" + mForceOrientation);
            return;
        }

        if (sensor == ActivityInfo.SCREEN_ORIENTATION_SENSOR)
            mScreenOrientationHelper.enableSensorOrientation();
        else
            mScreenOrientationHelper.disableSensorOrientation();
    }

    public void setForceOrientation(int orientation) {
        if (mForceOrientation == orientation) {
            LogUtil.debugLog(TAG, "setForceOrientation no change");
            return;
        }
        mForceOrientation = orientation;
        if (mForceOrientation != 0) {
            if (mForceOrientation != mOrientation) {
                if (mForceOrientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
                    mScreenOrientationHelper.portrait();
                } else {
                    mScreenOrientationHelper.landscape();
                }
            }
            mScreenOrientationHelper.disableSensorOrientation();
        } else {
            updateOrientation();
        }
    }

    /*
     * (non-Javadoc)
     * @see android.os.Handler.Callback#handleMessage(android.os.Message)
     */
    @SuppressLint("NewApi")
    @Override
    public boolean handleMessage(Message msg) {
        if (this.isFinishing()) {
            return false;
        }
         LogUtil.infoLog(TAG, "handleMessage:" + msg.what);
        switch (msg.what) {
            case MSG_VIDEO_SIZE_CHANGED:
                LogUtil.d(TAG, "MSG_VIDEO_SIZE_CHANGED");
                try {
                    String temp = (String) msg.obj;
                    String[] strings = temp.split(":");
                    int mVideoWidth = Integer.parseInt(strings[0]);
                    int mVideoHeight = Integer.parseInt(strings[1]);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case EZRealPlayConstants.MSG_GET_CAMERA_INFO_SUCCESS:
                updateLoadingProgress(20);
                handleGetCameraInfoSuccess();
                break;
            case EZRealPlayConstants.MSG_REALPLAY_PLAY_START:
                updateLoadingProgress(40);
                break;
            case EZRealPlayConstants.MSG_REALPLAY_CONNECTION_START:
                updateLoadingProgress(60);
                break;
            case EZRealPlayConstants.MSG_REALPLAY_CONNECTION_SUCCESS:
                updateLoadingProgress(80);
                break;
            case EZRealPlayConstants.MSG_REALPLAY_PLAY_SUCCESS:
                ViewGroup playInfoVg = (ViewGroup) findViewById(R.id.vg_play_info);
                if (playInfoVg != null){
                    playInfoVg.setVisibility(View.VISIBLE);
                }
                showDecodeType();
                handlePlaySuccess(msg);
                if (faceDetectState) {
                    faceCompare.setVisibility(View.VISIBLE);
                    faceDetect.setChecked(true);
                    if (isFaceCompare) {
                        faceCompare.setChecked(true);
                        faceAlarm.setVisibility(View.VISIBLE);
                        if (isOpenAlarm) {
                            faceAlarm.setChecked(true);
                        }
                    }
                    try {
                        faceDetectCapture();
                        Thread.sleep(200);
                        drawFaceRect(faceRect, dispWidth, dispHeight);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                if (isDetectUnsafeActs) {
                    skeletonDetect1.setVisibility(View.VISIBLE);
                    unsafeActs.setChecked(true);
                    if (isDetectSkeleton) {
                        skeletonDetect1.setChecked(true);
                        unsafeActsAlarm.setVisibility(View.VISIBLE);
                        if (isUnsafeActsAlarmOpen) {
                            unsafeActsAlarm.setChecked(true);
                        }
                    }
                    /*try {
                        faceDetectCapture();
                        Thread.sleep(200);
                        drawFaceRect(faceRect, dispWidth, dispHeight);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }*/
                }
                break;
            case EZRealPlayConstants.MSG_REALPLAY_PLAY_FAIL:
                handlePlayFail(msg.obj);
                break;
            case EZRealPlayConstants.MSG_SET_VEDIOMODE_SUCCESS:
                handleSetVedioModeSuccess();
                break;
            case EZRealPlayConstants.MSG_SET_VEDIOMODE_FAIL:
                handleSetVedioModeFail(msg.arg1);
                break;
            case EZRealPlayConstants.MSG_PTZ_SET_FAIL:
                handlePtzControlFail(msg);
                break;
            case EZRealPlayConstants.MSG_REALPLAY_VOICETALK_SUCCESS:
                handleVoiceTalkSucceed();
                break;
            case EZRealPlayConstants.MSG_REALPLAY_VOICETALK_STOP:
                handleVoiceTalkStoped(false);
                break;
            case EZRealPlayConstants.MSG_REALPLAY_VOICETALK_FAIL:
                ErrorInfo errorInfo = (ErrorInfo) msg.obj;
                handleVoiceTalkFailed(errorInfo);
                break;
            case MSG_PLAY_UI_UPDATE:
                updateRealPlayUI();
                break;
            case MSG_AUTO_START_PLAY:
                startRealPlay();
                break;
            case MSG_CLOSE_PTZ_PROMPT:
                mRealPlayFullPtzPromptIv.setVisibility(View.GONE);
                break;
            case MSG_HIDE_PTZ_DIRECTION:
                handleHidePtzDirection(msg);
                break;
            case MSG_HIDE_PAGE_ANIM:
                hidePageAnim();
                break;
            case MSG_PLAY_UI_REFRESH:
                initUI();
                break;
            case MSG_PREVIEW_START_PLAY:
                mPageAnimIv.setVisibility(View.GONE);
                mRealPlayPreviewTv.setVisibility(View.GONE);
                mStatus = RealPlayStatus.STATUS_INIT;
                startRealPlay();
                break;
            case MSG_GOT_STREAM_TYPE:
                showStreamType(msg.arg1);
                break;
            default:
                // do nothing
                break;
        }
        return false;
    }

    private void showDecodeType(){
        if(mEZPlayer != null && mEZPlayer.getPlayPort() >= 0){
            int intDecodeType = Player.getInstance().getDecoderType(mEZPlayer.getPlayPort());
            String strDecodeType;
            if (intDecodeType == 1){
                strDecodeType = "hard";
            }else{
                strDecodeType = "soft";
            }
            String streamTypeMsg = "decode type: " + strDecodeType;
            TextView streamTypeTv = findViewById(R.id.tv_decode_type);
            if (streamTypeTv != null){
                streamTypeTv.setText(streamTypeMsg);
                streamTypeTv.setVisibility(View.GONE);
            }
        }
    }

    private void showStreamType(int streamType){
        String streamTypeMsg = getApplicationContext().getString(R.string.stream_type) + changeIntTypeToStringType(streamType);
        TextView streamTypeTv = findViewById(R.id.tv_stream_type);
        if (streamTypeTv != null){
            streamTypeTv.setText(streamTypeMsg);
            streamTypeTv.setVisibility(View.GONE);
        }
    }

    private String changeIntTypeToStringType(int streamType) {
        String strStreamType;
        switch (streamType){
            /*
              取流方式切换到私有流媒体转发模式
             */
            case 0:
                strStreamType = "private_stream";
                break;
            /*
              取流方式切换到P2P模式
             */
            case 1:
                strStreamType = "p2p";
                break;
            /*
              取流方式切换到内网直连模式
             */
            case 2:
                strStreamType = "direct_inner";
                break;
            /*
              取流方式切换到外网直连模式
             */
            case 3:
                strStreamType = "direct_outer";
                break;
            /*
              取流方式切换到云存储回放
             */
            case 4:
                strStreamType = "cloud_playback";
                break;
            /*
              取流方式切换到云存储留言
             */
            case 5:
                strStreamType = "cloud_leave_msg";
                break;
            /*
              取流方式切换到反向直连模式
             */
            case 6:
                strStreamType = "direct_reverse";
                break;
            /*
              取流方式切换到HCNETSDK
             */
            case 7:
                strStreamType = "hcnetsdk";
                break;
            default:
                strStreamType = "unknown(" + streamType + ")";
                break;
        }
        return strStreamType;
    }

    private void handleHidePtzDirection(Message msg) {
        if (mHandler == null) {
            return;
        }
        mHandler.removeMessages(MSG_HIDE_PTZ_DIRECTION);
        if (msg.arg1 > 2) {
            mRealPlayPtzDirectionIv.setVisibility(View.GONE);
        } else {
            mRealPlayPtzDirectionIv.setVisibility(msg.arg1 == 1 ? View.GONE : View.VISIBLE);
            Message message = new Message();
            message.what = MSG_HIDE_PTZ_DIRECTION;
            message.arg1 = msg.arg1 + 1;
            mHandler.sendMessageDelayed(message, 500);
        }
    }

    private void handlePtzControlFail(Message msg) {
        LogUtil.debugLog(TAG, "handlePtzControlFail:" + msg.arg1);
        switch (msg.arg1) {
            case ErrorCode.ERROR_CAS_PTZ_CONTROL_CALLING_PRESET_FAILED:
                // 正在调用预置点，键控动作无效
                //Calling preset point, name action is invalid
                Utils.showToast(EZRealPlayActivity.this, R.string.camera_lens_too_busy, msg.arg1);
                break;
            case ErrorCode.ERROR_CAS_PTZ_PRESET_PRESETING_FAILE:// 当前正在调用预置点
                Utils.showToast(EZRealPlayActivity.this, R.string.ptz_is_preseting, msg.arg1);
                break;
            case ErrorCode.ERROR_CAS_PTZ_CONTROL_TIMEOUT_SOUND_LACALIZATION_FAILED:
                // 当前正在声源定位
                //Is currently locating at sound source
                break;
            case ErrorCode.ERROR_CAS_PTZ_CONTROL_TIMEOUT_CRUISE_TRACK_FAILED:
                // 键控动作超时(当前正在轨迹巡航)
                //Key action timeout (currently tracing)
                Utils.showToast(EZRealPlayActivity.this, R.string.ptz_control_timeout_cruise_track_failed, msg.arg1);
                break;
            case ErrorCode.ERROR_CAS_PTZ_PRESET_INVALID_POSITION_FAILED:
                // 当前预置点信息无效
                //The current preset information is invalid
                Utils.showToast(EZRealPlayActivity.this, R.string.ptz_preset_invalid_position_failed, msg.arg1);
                break;
            case ErrorCode.ERROR_CAS_PTZ_PRESET_CURRENT_POSITION_FAILED:
                // 该预置点已是当前位置
                //The preset point is the current position
                Utils.showToast(EZRealPlayActivity.this, R.string.ptz_preset_current_position_failed, msg.arg1);
                break;
            case ErrorCode.ERROR_CAS_PTZ_PRESET_SOUND_LOCALIZATION_FAILED:
                // 设备正在响应本次声源定位
                //The device is responding to this sound source location
                Utils.showToast(EZRealPlayActivity.this, R.string.ptz_preset_sound_localization_failed, msg.arg1);
                break;
            case ErrorCode.ERROR_CAS_PTZ_OPENING_PRIVACY_FAILED:// 当前正在开启隐私遮蔽 Is currently opening privacy masking
            case ErrorCode.ERROR_CAS_PTZ_CLOSING_PRIVACY_FAILED:// 当前正在关闭隐私遮蔽   The privacy mask is currently being turned off
            case ErrorCode.ERROR_CAS_PTZ_MIRRORING_FAILED:// 设备正在镜像操作（设备镜像要几秒钟，防止频繁镜像操作）The device is mirroring (the device mirroring takes a few seconds to prevent frequent mirroring)
                Utils.showToast(EZRealPlayActivity.this, R.string.ptz_operation_too_frequently, msg.arg1);
                break;
            case ErrorCode.ERROR_CAS_PTZ_CONTROLING_FAILED:// 设备正在键控动作（上下左右）(一个客户端在上下左右控制，另外一个在开其它东西) The device is keying action (up and down left and right) (a client in the upper and lower left and right control, the other one in the open other things)
                break;
            case ErrorCode.ERROR_CAS_PTZ_FAILED:// 云台当前操作失败 PTZ current operation failed
                break;
            case ErrorCode.ERROR_CAS_PTZ_PRESET_EXCEED_MAXNUM_FAILED:// 当前预置点超过最大个数 The current preset exceeds the maximum number
                Utils.showToast(EZRealPlayActivity.this, R.string.ptz_preset_exceed_maxnum_failed, msg.arg1);
                break;
            case ErrorCode.ERROR_CAS_PTZ_PRIVACYING_FAILED:// 设备处于隐私遮蔽状态（关闭了镜头，再去操作云台相关）The device is in a privacy state (close the lens, and then operate the PTZ related)
                Utils.showToast(EZRealPlayActivity.this, R.string.ptz_privacying_failed, msg.arg1);
                break;
            case ErrorCode.ERROR_CAS_PTZ_TTSING_FAILED:// 设备处于语音对讲状态(区别以前的语音对讲错误码，云台单独列一个）Equipment in the voice intercom state (the difference between the previous voice intercom error code, PTZ separate one)
                Utils.showToast(EZRealPlayActivity.this, R.string.ptz_mirroring_failed, msg.arg1);
                break;
            case ErrorCode.ERROR_CAS_PTZ_ROTATION_UP_LIMIT_FAILED:// 设备云台旋转到达上限位 The PTZ rotation reaches the upper limit
            case ErrorCode.ERROR_CAS_PTZ_ROTATION_DOWN_LIMIT_FAILED:// 设备云台旋转到达下限位 The PTZ rotation reaches the lower limit
            case ErrorCode.ERROR_CAS_PTZ_ROTATION_LEFT_LIMIT_FAILED:// 设备云台旋转到达左限位  The PTZ rotation reaches the left limit
            case ErrorCode.ERROR_CAS_PTZ_ROTATION_RIGHT_LIMIT_FAILED:// 设备云台旋转到达右限位 The PTZ rotation reaches the right limit
                setPtzDirectionIv(-1, msg.arg1);
                break;
            default:
                Utils.showToast(EZRealPlayActivity.this, R.string.ptz_operation_failed, msg.arg1);
                break;
        }
    }

    private void hidePageAnim() {
        mHandler.removeMessages(MSG_HIDE_PAGE_ANIM);
        if (mPageAnimDrawable != null) {
            if (mPageAnimDrawable.isRunning()) {
                mPageAnimDrawable.stop();
            }
            mPageAnimDrawable = null;
            mPageAnimIv.setBackgroundDrawable(null);
            mPageAnimIv.setVisibility(View.GONE);
        }
        if (mPageAnimIv != null) {
            mPageAnimIv.setBackgroundDrawable(null);
            mPageAnimIv.setVisibility(View.GONE);
        }
    }

    private void setRealPlayTalkUI() {
        if (mEZPlayer != null && mDeviceInfo != null && (mDeviceInfo.isSupportTalk() != EZConstants.EZTalkbackCapability.EZTalkbackNoSupport)) {
            mRealPlayTalkBtnLy.setVisibility(View.VISIBLE);
            if (mCameraInfo != null && mDeviceInfo.getStatus() == 1) {
                mRealPlayTalkBtn.setEnabled(true);
            } else {
                mRealPlayTalkBtn.setEnabled(false);
            }
            if (mDeviceInfo.isSupportTalk() != EZConstants.EZTalkbackCapability.EZTalkbackNoSupport) {
                mRealPlayFullTalkBtn.setVisibility(View.VISIBLE);
            } else {
                mRealPlayFullTalkBtn.setVisibility(View.GONE);
            }
        } else {
            mRealPlayTalkBtnLy.setVisibility(View.GONE);
            mRealPlayFullTalkBtn.setVisibility(View.GONE);
        }
        mRealPlayTalkBtnLy.setVisibility(View.VISIBLE);
        //mRealPlayTalkBtn.setEnabled(false);
    }

    private void updatePermissionUI() {
        mRealPlayTalkBtnLy.setVisibility(View.VISIBLE);
    }

    private void updateUI() {
        setRealPlayTalkUI();

        setVideoLevel();
        
/*
        if (mRealPlayMgr != null && mRealPlayMgr.getSupportPtzPrivacy() == 1) {
            mRealPlayPrivacyBtnLy.setVisibility(View.VISIBLE);
            if (mCameraInfo.getOnlineStatus() == 1 && mRealPlayMgr.getPrivacyStatus() != 1) {
                mRealPlayQualityBtn.setEnabled(true);
            } else {
                mRealPlayQualityBtn.setEnabled(false);
            }
        } else {
            mRealPlayPrivacyBtnLy.setVisibility(View.GONE);
        }
        */
        //        mRealPlayPrivacyBtnLy.setVisibility(View.GONE);
        //        setSoundLocalizationUI();

        {
            mRealPlaySslBtnLy.setVisibility(View.GONE);
        }

        if (getSupportPtz() == 1) {
            mRealPlayPtzBtnLy.setVisibility(View.VISIBLE);
            mRealPlayFullPtzBtn.setVisibility(View.VISIBLE);
        } else {
            //mRealPlayPtzBtnLy.setVisibility(View.GONE);
            //mRealPlayFullPtzBtn.setVisibility(View.GONE);
            mRealPlayPtzBtnLy.setEnabled(false);
            mRealPlayFullPtzBtn.setEnabled(false);
        }

        updatePermissionUI();
    }

    private void handleGetCameraInfoSuccess() {
        LogUtil.infoLog(TAG, "handleGetCameraInfoSuccess");

        updateUI();

    }

    private void handleVoiceTalkSucceed() {
        if (mOrientation == Configuration.ORIENTATION_PORTRAIT) {
            openTalkPopupWindow(true);
        } else {
            mRealPlayFullTalkAnimBtn.setVisibility(View.VISIBLE);
            //            mFullscreenFullButton.setVisibility(View.VISIBLE);
            ((AnimationDrawable) mRealPlayFullTalkAnimBtn.getBackground()).start();
        }

        mRealPlayTalkBtn.setEnabled(true);
        mRealPlayFullTalkBtn.setEnabled(true);
        mRealPlayFullTalkAnimBtn.setEnabled(true);
    }

    private void handleVoiceTalkFailed(ErrorInfo errorInfo) {
        LogUtil.debugLog(TAG, "Talkback failed. " + errorInfo.toString());

        closeTalkPopupWindow(true, false);

        switch (errorInfo.errorCode) {
            case ErrorCode.ERROR_TRANSF_DEVICE_TALKING:
                Utils.showToast(EZRealPlayActivity.this, R.string.realplay_play_talkback_fail_ison);
                break;
            case ErrorCode.ERROR_TRANSF_DEVICE_PRIVACYON:
                Utils.showToast(EZRealPlayActivity.this, R.string.realplay_play_talkback_fail_privacy);
                break;
            case ErrorCode.ERROR_TRANSF_DEVICE_OFFLINE:
                Utils.showToast(EZRealPlayActivity.this, R.string.realplay_fail_device_not_exist);
                break;
            case ErrorCode.ERROR_TTS_MSG_REQ_TIMEOUT:
            case ErrorCode.ERROR_TTS_MSG_SVR_HANDLE_TIMEOUT:
            case ErrorCode.ERROR_TTS_WAIT_TIMEOUT:
            case ErrorCode.ERROR_TTS_HNADLE_TIMEOUT:
                Utils.showToast(EZRealPlayActivity.this, R.string.realplay_play_talkback_request_timeout, errorInfo.errorCode);
                break;
            case ErrorCode.ERROR_CAS_AUDIO_SOCKET_ERROR:
            case ErrorCode.ERROR_CAS_AUDIO_RECV_ERROR:
            case ErrorCode.ERROR_CAS_AUDIO_SEND_ERROR:
                Utils.showToast(EZRealPlayActivity.this, R.string.realplay_play_talkback_network_exception, errorInfo.errorCode);
                break;
            default:
                Utils.showToast(EZRealPlayActivity.this, R.string.realplay_play_talkback_fail, errorInfo.errorCode);
                break;
        }
    }

    private void handleVoiceTalkStoped(boolean startAnim) {
        if (mIsOnTalk) {
            mIsOnTalk = false;
            setForceOrientation(0);
        }
        if (mOrientation == Configuration.ORIENTATION_LANDSCAPE) {
            if (startAnim) {
                mRealPlayFullTalkAnimBtn.setVisibility(View.GONE);
                mFullscreenFullButton.setVisibility(View.GONE);
                mRealPlayFullAnimBtn.setBackgroundResource(R.drawable.speech_1);
                startFullBtnAnim(mRealPlayFullAnimBtn, mEndXy, mStartXy, new AnimationListener() {

                    @Override
                    public void onAnimationStart(Animation animation) {
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        mRealPlayFullAnimBtn.setVisibility(View.GONE);
                        onRealPlaySvClick();
                    }
                });
            } else {
                mRealPlayFullTalkAnimBtn.setVisibility(View.GONE);
                mFullscreenFullButton.setVisibility(View.GONE);
            }
        }

        mRealPlayTalkBtn.setEnabled(true);
        mRealPlayFullTalkBtn.setEnabled(true);
        mRealPlayFullTalkAnimBtn.setEnabled(true);

        if (mStatus == RealPlayStatus.STATUS_PLAY) {
            if (mEZPlayer != null) {
                if (mLocalInfo.isSoundOpen()) {
                    mEZPlayer.openSound();
                } else {
                    mEZPlayer.closeSound();
                }
            }
        }
    }

    private void handleSetVedioModeSuccess() {
        closeQualityPopupWindow();
        setVideoLevel();
        try {
            mWaitDialog.setWaitText(null);
            mWaitDialog.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (mStatus == RealPlayStatus.STATUS_PLAY) {
            // 停止播放 Stop play
            stopRealPlay();
            SystemClock.sleep(500);
            // 开始播放 start play
            startRealPlay();
        }
    }

    private void handleSetVedioModeFail(int errorCode) {
        closeQualityPopupWindow();
        setVideoLevel();
        try {
            mWaitDialog.setWaitText(null);
            mWaitDialog.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Utils.showToast(EZRealPlayActivity.this, R.string.realplay_set_vediomode_fail, errorCode);
    }

    private void handleRecordSuccess(String recordFilePath) {
        if (mCameraInfo == null) {
            return;
        }

        if (mOrientation == Configuration.ORIENTATION_PORTRAIT) {
            if (!mIsOnStop) {
                mRecordRotateViewUtil.applyRotation(mRealPlayRecordContainer, mRealPlayRecordBtn,
                        mRealPlayRecordStartBtn, 0, 90);
            } else {
                mRealPlayRecordBtn.setVisibility(View.GONE);
                mRealPlayRecordStartBtn.setVisibility(View.VISIBLE);
            }
            mRealPlayFullRecordBtn.setVisibility(View.GONE);
            mRealPlayFullRecordStartBtn.setVisibility(View.VISIBLE);
        } else {
            if (!mIsOnStop) {
                mRecordRotateViewUtil.applyRotation(mRealPlayFullRecordContainer, mRealPlayFullRecordBtn,
                        mRealPlayFullRecordStartBtn, 0, 90);
            } else {
                mRealPlayFullRecordBtn.setVisibility(View.GONE);
                mRealPlayFullRecordStartBtn.setVisibility(View.VISIBLE);
            }
            mRealPlayRecordBtn.setVisibility(View.GONE);
            mRealPlayRecordStartBtn.setVisibility(View.VISIBLE);
        }
        isRecording = true;
        mRealPlayRecordLy.setVisibility(View.VISIBLE);
        mRealPlayRecordTv.setText("00:00");
        mRecordSecond = 0;
    }

    private void handleRecordFail() {
        Utils.showToast(EZRealPlayActivity.this, R.string.remoteplayback_record_fail);
        if (isRecording) {
            stopRealPlayRecord();
        }
    }

    private void hideControlRlAndFullOperateBar(boolean excludeLandscapeTitle) {
        //        mRealPlayControlRl.setVisibility(View.GONE);
        closeQualityPopupWindow();
        if (mRealPlayFullOperateBar != null) {
            mRealPlayFullOperateBar.setVisibility(View.GONE);
            if (mOrientation == Configuration.ORIENTATION_PORTRAIT) {
                mFullscreenFullButton.setVisibility(View.GONE);
            } else {
                if (!mIsOnTalk && !mIsOnPtz) {
                    mFullscreenFullButton.setVisibility(View.GONE);
                }
            }
        }
        if (excludeLandscapeTitle && mOrientation == Configuration.ORIENTATION_LANDSCAPE) {
            if (!mIsOnTalk && !mIsOnPtz) {
                mLandscapeTitleBar.setVisibility(View.VISIBLE);
            }
        } else {
            mLandscapeTitleBar.setVisibility(View.GONE);
        }
    }

    private void updateRealPlayUI() {
        if (mControlDisplaySec == 5) {
            mControlDisplaySec = 0;
            hideControlRlAndFullOperateBar(false);
        }
        checkRealPlayFlow();
        updateCaptureUI();

        if (isRecording) {
            updateRecordTime();
        }
    }

    private void initCaptureUI() {
        mCaptureDisplaySec = 0;
        mRealPlayCaptureRl.setVisibility(View.GONE);
        mRealPlayCaptureIv.setImageURI(null);
        mRealPlayCaptureWatermarkIv.setTag(null);
        mRealPlayCaptureWatermarkIv.setVisibility(View.GONE);
    }

    // 更新抓图/录像显示UI
    //Update the capture / video display UI
    private void updateCaptureUI() {
        if (mRealPlayCaptureRl.getVisibility() == View.VISIBLE) {
            if (mOrientation == Configuration.ORIENTATION_PORTRAIT) {
                if (mRealPlayControlRl.getVisibility() == View.VISIBLE) {
                    mRealPlayCaptureRlLp.setMargins(0, 0, 0, Utils.dip2px(this, 40));
                } else {
                    mRealPlayCaptureRlLp.setMargins(0, 0, 0, 0);
                }
                mRealPlayCaptureRl.setLayoutParams(mRealPlayCaptureRlLp);
            } else {
                LayoutParams realPlayCaptureRlLp = new LayoutParams(
                        Utils.dip2px(this, 65), Utils.dip2px(this, 45));
                realPlayCaptureRlLp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                realPlayCaptureRlLp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                mRealPlayCaptureRl.setLayoutParams(realPlayCaptureRlLp);
            }
            if (mRealPlayCaptureWatermarkIv.getTag() != null) {
                mRealPlayCaptureWatermarkIv.setVisibility(View.VISIBLE);
                mRealPlayCaptureWatermarkIv.setTag(null);
            }
        }
        if (mCaptureDisplaySec >= 4) {
            initCaptureUI();
        }
    }

    private void updateRecordTime() {
        if (mRealPlayRecordIv.getVisibility() == View.VISIBLE) {
            mRealPlayRecordIv.setVisibility(View.INVISIBLE);
        } else {
            mRealPlayRecordIv.setVisibility(View.VISIBLE);
        }

        int leftSecond = mRecordSecond % 3600;
        int minitue = leftSecond / 60;
        int second = leftSecond % 60;

        String recordTime = String.format("%02d:%02d", minitue, second);
        mRealPlayRecordTv.setText(recordTime);
    }

    // 处理密码错误
    //Processing password is wrong
    private void handlePasswordError(int title_resid, int msg1_resid, int msg2_resid) {
        stopRealPlay();
        setRealPlayStopUI();
        LogUtil.debugLog(TAG, "startRealPlay");

        if (mCameraInfo == null || mStatus == RealPlayStatus.STATUS_START || mStatus == RealPlayStatus.STATUS_PLAY) {
            return;
        }

        //  Check if the network is available
        if (!ConnectionDetector.isNetworkAvailable(this)) {
            // 提示没有连接网络 Prompt not to connect to the network
            setRealPlayFailUI(getString(R.string.realplay_play_fail_becauseof_network));
            return;
        }

        mStatus = RealPlayStatus.STATUS_START;
        setRealPlayLoadingUI();

        updateLoadingProgress(0);
    }

    private void handlePlaySuccess(Message msg) {
        LogUtil.d(TAG,"handlePlaySuccess");
        mStatus = RealPlayStatus.STATUS_PLAY;

        // 声音处理  Sound processing
        setRealPlaySound();

        // temp solution for OPENSDK-92
        // Android 预览3Q10的时候切到流畅之后 视频播放窗口变大了 
        //        if (description.arg1 != 0) {
        //            mRealRatio = (float) description.arg2 / description.arg1;
        //        } else {
        //            mRealRatio = Constant.LIVE_VIEW_RATIO;
        //        }
        mRealRatio = Constant.LIVE_VIEW_RATIO;

        boolean bSupport = true;//(float) mLocalInfo.getScreenHeight() / mLocalInfo.getScreenWidth() >= BIG_SCREEN_RATIO;
        if (bSupport) {
            initOperateBarUI(mRealRatio <= Constant.LIVE_VIEW_RATIO);
            initUI();
            if (mRealRatio <= Constant.LIVE_VIEW_RATIO) {
                setBigScreenOperateBtnLayout();
            }
        }
        setRealPlaySvLayout();
        setRealPlaySuccessUI();
        updatePtzUI();
        //        startPrivacyAnim();
        updateTalkUI();
        if (mDeviceInfo != null && mDeviceInfo.isSupportTalk() != EZConstants.EZTalkbackCapability.EZTalkbackNoSupport) {
            mRealPlayTalkBtn.setEnabled(true);
        }else{
            mRealPlayTalkBtn.setEnabled(false);
        }
        if (mEZPlayer != null) {
            mStreamFlow = mEZPlayer.getStreamFlow();
        }
    }

    private void setRealPlaySvLayout() {
        final int screenWidth = mLocalInfo.getScreenWidth();
        final int screenHeight = (mOrientation == Configuration.ORIENTATION_PORTRAIT) ? (mLocalInfo.getScreenHeight() - mLocalInfo
                .getNavigationBarHeight()) : mLocalInfo.getScreenHeight();
        final LayoutParams realPlaySvlp = Utils.getPlayViewLp(mRealRatio, mOrientation,
                mLocalInfo.getScreenWidth(), (int) (mLocalInfo.getScreenWidth() * Constant.LIVE_VIEW_RATIO),
                screenWidth, screenHeight);
        LayoutParams svLp = new LayoutParams(realPlaySvlp.width, realPlaySvlp.height);
        ViewGroup playWindowVg = (ViewGroup) findViewById(R.id.vg_play_window);
        playWindowVg.setLayoutParams(svLp);

        if (mRtspUrl == null) {
            // do nothing
        } else {
            LinearLayout.LayoutParams realPlayPlayRlLp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
                    LayoutParams.MATCH_PARENT);
            realPlayPlayRlLp.gravity = Gravity.CENTER;
            mRealPlayPlayRl.setLayoutParams(realPlayPlayRlLp);
        }
        mRealPlayTouchListener.setSacaleRect(Constant.MAX_SCALE, 0, 0, realPlaySvlp.width, realPlaySvlp.height);
        setPlayScaleUI(1, null, null);
    }

    private void handlePlayFail(Object obj) {
        int errorCode = 0;
        if (obj != null) {
            ErrorInfo errorInfo = (ErrorInfo) obj;
            errorCode = errorInfo.errorCode;
            LogUtil.debugLog(TAG, "handlePlayFail:" + errorInfo.errorCode);
        }


        hidePageAnim();

        stopRealPlay();

        updateRealPlayFailUI(errorCode);
    }

    private void updateRealPlayFailUI(int errorCode) {
        String txt = null;
        LogUtil.i(TAG, "updateRealPlayFailUI: errorCode:" + errorCode);
        // 判断返回的错误码
        switch (errorCode) {
            case ErrorCode.ERROR_TRANSF_ACCESSTOKEN_ERROR:
                ActivityUtils.goToLoginAgain(EZRealPlayActivity.this);
                return;
            case ErrorCode.ERROR_CAS_MSG_PU_NO_RESOURCE:
                txt = getString(R.string.remoteplayback_over_link);
                break;
            case ErrorCode.ERROR_TRANSF_DEVICE_OFFLINE:
                if (mCameraInfo != null) {
                    mCameraInfo.setIsShared(0);
                }
                txt = getString(R.string.realplay_fail_device_not_exist);
                break;
            case ErrorCode.ERROR_INNER_STREAM_TIMEOUT:
                txt = getString(R.string.realplay_fail_connect_device);
                break;
            case ErrorCode.ERROR_WEB_CODE_ERROR:
                //VerifySmsCodeUtil.openSmsVerifyDialog(Constant.SMS_VERIFY_LOGIN, this, this);
                //txt = Utils.getErrorTip(this, R.string.check_feature_code_fail, errorCode);
                break;
            case ErrorCode.ERROR_WEB_HARDWARE_SIGNATURE_OP_ERROR:
                //VerifySmsCodeUtil.openSmsVerifyDialog(Constant.SMS_VERIFY_HARDWARE, this, null);
//                SecureValidate.secureValidateDialog(this, this);
                //txt = Utils.getErrorTip(this, R.string.check_feature_code_fail, errorCode);
                break;
            case ErrorCode.ERROR_TRANSF_TERMINAL_BINDING:
                txt = "请在萤石客户端关闭终端绑定 "
                    + "Please close the terminal binding on the fluorite client";
                break;
            // 收到这两个错误码，可以弹出对话框，让用户输入密码后，重新取流预览
            case ErrorCode.ERROR_INNER_VERIFYCODE_NEED:
            case ErrorCode.ERROR_INNER_VERIFYCODE_ERROR: {
                DataManager.getInstance().setDeviceSerialVerifyCode(mCameraInfo.getDeviceSerial(), null);
                VerifyCodeInput.VerifyCodeInputDialog(this, this).show();
            }
            break;
            case ErrorCode.ERROR_EXTRA_SQUARE_NO_SHARING:
            default:
                txt = Utils.getErrorTip(this, R.string.realplay_play_fail, errorCode);
                break;
        }

        if (!TextUtils.isEmpty(txt)) {
            setRealPlayFailUI(txt);
        } else {
            setRealPlayStopUI();
        }
    }



    private void startUpdateTimer() {
        stopUpdateTimer();
        mUpdateTimer = new Timer();
        mUpdateTimerTask = new TimerTask() {
            @Override
            public void run() {
                if (mLandscapeTitleBar != null && mRealPlayControlRl != null
                        && (mLandscapeTitleBar.getVisibility() == View.VISIBLE || mRealPlayControlRl.getVisibility() == View.VISIBLE)
                        && mControlDisplaySec < 5) {
                    mControlDisplaySec++;
                }
                if (mRealPlayCaptureRl != null && mRealPlayCaptureRl.getVisibility() == View.VISIBLE
                        && mCaptureDisplaySec < 4) {
                    mCaptureDisplaySec++;
                }

                if (mEZPlayer != null && isRecording) {



                    Calendar OSDTime = mEZPlayer.getOSDTime();
                    if (OSDTime != null) {
                        String playtime = Utils.OSD2Time(OSDTime);
                        if (!TextUtils.equals(playtime, mRecordTime)) {
                            mRecordSecond++;
                            mRecordTime = playtime;
                        }
                    }
                }
                if (mHandler != null) {
                    mHandler.sendEmptyMessage(MSG_PLAY_UI_UPDATE);
                }
            }
        };
        mUpdateTimer.schedule(mUpdateTimerTask, 0, 1000);
    }

    private void stopUpdateTimer() {
        mCaptureDisplaySec = 4;
        updateCaptureUI();
        mHandler.removeMessages(MSG_PLAY_UI_UPDATE);
        if (mUpdateTimer != null) {
            mUpdateTimer.cancel();
            mUpdateTimer = null;
        }

        if (mUpdateTimerTask != null) {
            mUpdateTimerTask.cancel();
            mUpdateTimerTask = null;
        }
    }

    private void dismissPopWindow(PopupWindow popupWindow) {
        if (popupWindow != null && !isFinishing()) {
            try {
                popupWindow.dismiss();
            } catch (Exception e) {
                // TODO: handle exception
            }
        }
    }

    private void setPlayScaleUI(float scale, CustomRect oRect, CustomRect curRect) {
        if (scale == 1) {
            if (mPlayScale == scale) {
                return;
            }
            mRealPlayRatioTv.setVisibility(View.GONE);
            try {
                if (mEZPlayer != null) {
                    mEZPlayer.setDisplayRegion(false, null, null);
                }
            } catch (BaseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else {
            if (mPlayScale == scale) {
                try {
                    if (mEZPlayer != null) {
                        mEZPlayer.setDisplayRegion(true, oRect, curRect);
                    }
                } catch (BaseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                return;
            }
            LayoutParams realPlayRatioTvLp = (LayoutParams) mRealPlayRatioTv
                    .getLayoutParams();
            if (mOrientation == Configuration.ORIENTATION_PORTRAIT) {
                realPlayRatioTvLp.setMargins(Utils.dip2px(this, 10), Utils.dip2px(this, 10), 0, 0);
            } else {
                realPlayRatioTvLp.setMargins(Utils.dip2px(this, 70), Utils.dip2px(this, 20), 0, 0);
            }
            mRealPlayRatioTv.setLayoutParams(realPlayRatioTvLp);
            String sacleStr = String.valueOf(scale);
            mRealPlayRatioTv.setText(sacleStr.subSequence(0, Math.min(3, sacleStr.length())) + "X");
            //mj mRealPlayRatioTv.setVisibility(View.VISIBLE);
            mRealPlayRatioTv.setVisibility(View.GONE);
            hideControlRlAndFullOperateBar(false);
            try {
                if (mEZPlayer != null) {
                    mEZPlayer.setDisplayRegion(true, oRect, curRect);
                }
            } catch (BaseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        mPlayScale = scale;
    }

    /*
     * (non-Javadoc)
     * @see android.view.View.OnTouchListener#onTouch(android.view.View, android.view.MotionEvent)
     */
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (v.getId()) {
            case R.id.realplay_pages_gallery:
                mRealPlayTouchListener.touch(event);
                break;
            case R.id.realplay_full_operate_bar:
                return true;
            default:
                break;
        }
        return false;
    }



    private void showType() {
        if (Config.LOGGING && mEZPlayer != null) {
            //Utils.showLog(EZRealPlayActivity.this, "getType " + ",time：" + (mStopTime - mStartTime));
        }
    }

    private void initLoadingUI() {
        mRealPlayLoadingRl = (RelativeLayout) findViewById(R.id.realplay_loading_rl);
        mRealPlayTipTv = (TextView) findViewById(R.id.realplay_tip_tv);
        mRealPlayPlayIv = (ImageView) findViewById(R.id.realplay_play_iv);
        mRealPlayPlayLoading = (LoadingTextView) findViewById(R.id.realplay_loading);
        mRealPlayPlayPrivacyLy = (LinearLayout) findViewById(R.id.realplay_privacy_ly);

        mRealPlayPlayIv.setOnClickListener(this);

        mPageAnimIv = (ImageView) findViewById(R.id.realplay_page_anim_iv);
    }

    private void updateLoadingProgress(final int progress) {
        mRealPlayPlayLoading.setTag(Integer.valueOf(progress));
        mRealPlayPlayLoading.setText(progress + "%");
        mHandler.postDelayed(new Runnable() {

            @Override
            public void run() {
                if (mRealPlayPlayLoading != null) {
                    Integer tag = (Integer) mRealPlayPlayLoading.getTag();
                    if (tag != null && tag.intValue() == progress) {
                        Random r = new Random();
                        mRealPlayPlayLoading.setText((progress + r.nextInt(20)) + "%");
                    }
                }
            }

        }, 500);
    }

    private void setStartloading() {
        mRealPlayLoadingRl.setVisibility(View.VISIBLE);
        mRealPlayTipTv.setVisibility(View.GONE);
        mRealPlayPlayLoading.setVisibility(View.VISIBLE);
        mRealPlayPlayIv.setVisibility(View.GONE);
        mRealPlayPlayPrivacyLy.setVisibility(View.GONE);
    }

    public void setStopLoading() {
        mRealPlayLoadingRl.setVisibility(View.VISIBLE);
        mRealPlayTipTv.setVisibility(View.GONE);
        mRealPlayPlayLoading.setVisibility(View.GONE);
        mRealPlayPlayIv.setVisibility(View.VISIBLE);
        mRealPlayPlayPrivacyLy.setVisibility(View.GONE);
    }

    public void setLoadingFail(String errorStr) {
        mRealPlayLoadingRl.setVisibility(View.VISIBLE);
        mRealPlayTipTv.setVisibility(View.VISIBLE);
        mRealPlayTipTv.setText(errorStr);
        mRealPlayPlayLoading.setVisibility(View.GONE);
        mRealPlayPlayIv.setVisibility(View.GONE);
        mRealPlayPlayPrivacyLy.setVisibility(View.GONE);
    }

    private void setPrivacy() {
        mRealPlayLoadingRl.setVisibility(View.VISIBLE);
        mRealPlayTipTv.setVisibility(View.GONE);
        mRealPlayPlayLoading.setVisibility(View.GONE);
        mRealPlayPlayIv.setVisibility(View.GONE);
        mRealPlayPlayPrivacyLy.setVisibility(View.VISIBLE);
    }

    private void setLoadingSuccess() {
        mRealPlayLoadingRl.setVisibility(View.INVISIBLE);
        mRealPlayTipTv.setVisibility(View.GONE);
        mRealPlayPlayLoading.setVisibility(View.GONE);
        mRealPlayPlayIv.setVisibility(View.GONE);
    }

    @Override
    public void onInputVerifyCode(final String verifyCode) {
        LogUtil.debugLog(TAG, "verify code is " + verifyCode);
        DataManager.getInstance().setDeviceSerialVerifyCode(mCameraInfo.getDeviceSerial(), verifyCode);
        if (mEZPlayer != null) {
            startRealPlay();
        }
    }

    FileOutputStream mOs;

    private EZOpenSDKListener.EZStandardFlowCallback mLocalRecordCb = new EZOpenSDKListener.EZStandardFlowCallback() {
        @Override
        public void onStandardFlowCallback(int type, byte[] data, int dataLen) {
            LogUtil.verboseLog(TAG, "standard flow. type is " + type + ". dataLen is " + dataLen + ". data0 is " + data[0]);

            if (mOs == null) {
                File f = new File("/sdcard/videogo.mp4");
                try {
                    mOs = new FileOutputStream(f);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    LogUtil.errorLog(TAG, "new record file failed");

                    return;
                }
            }
            try {
                mOs.write(data, 0, dataLen);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {

            }
        }
    };

    private void faceDetectCapture() {

        mControlDisplaySec = 0;
        if (!SDCardUtil.isSDCardUseable()) {
            // 提示SD卡不可用
            //Prompt SD card is not available
            Utils.showToast(EZRealPlayActivity.this, R.string.remoteplayback_SDCard_disable_use);
            return;
        }

        if (SDCardUtil.getSDCardRemainSize() < SDCardUtil.PIC_MIN_MEM_SPACE) {
            // 提示内存不足
            //Prompt for insufficient memory
            Utils.showToast(EZRealPlayActivity.this, R.string.remoteplayback_capture_fail_for_memory);
            return;
        }

        if (mEZPlayer != null) {
            mCaptureDisplaySec = 4;
            updateCaptureUI();


            thrd = new MyThread() {
                @Override
                public void run() {
                    while (!thrd.exit) {
                        Log.d(TAG, "run: " + "开始截图");

                        Bitmap bmp = mEZPlayer.capturePicture();

                        if (bmp != null) {
                            try {

                                String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/0_OpenSDK/Face_Detection";

                                File file = new File(path);

                                if (!file.exists()) {
                                    file.mkdir();
                                }
                                final String strCaptureFile = path + "/" + code + ".jpg";
                                if (TextUtils.isEmpty(strCaptureFile)) {
                                    bmp.recycle();
                                    bmp = null;
                                    return;
                                }
                                EZUtils.saveCapturePictrue(strCaptureFile, bmp);


                                MediaScanner mMediaScanner = new MediaScanner(EZRealPlayActivity.this);
                                mMediaScanner.scanFile(strCaptureFile, "jpg");

                            } catch (InnerException e) {
                                e.printStackTrace();
                            } finally {
                                if (bmp != null) {
                                    bmp.recycle();
                                    bmp = null;

                                }
                            }
                        } else {
                            return;
                        }
                        super.run();
                        try {
                            Thread.sleep(50);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        Log.d(TAG, "run: " + "截图" + code);
                        code += 1;
                        if (code > 5) {
                            code = 1;
                        }
                    }
                }

            };
            thrd.start();
        }
    }
    private void skeletonDetect() {

        mControlDisplaySec = 0;

        if (mEZPlayer != null) {
            mCaptureDisplaySec = 4;
            updateCaptureUI();


            thrd = new MyThread() {
                @Override
                public void run() {
                    while (!thrd.exit) {
                        Log.d(TAG, "run: " + "开始截图");

                        Bitmap bmp = mEZPlayer.capturePicture();

                        if (bmp != null) {
                            try {

                                drawSkeleton(bmp, faceRect, dispWidth, dispHeight);

                            } catch (Exception e) {
                                e.printStackTrace();
                            } finally {
                                if (bmp != null) {
                                    bmp.recycle();
                                    bmp = null;

                                }
                            }
                        } else {
                            return;
                        }
                        super.run();
                    }
                }

            };
            thrd.start();
        }
    }

    private void drawSkeleton(Bitmap bmp, final ImageView faceRect, int dispWidth, int dispHeight) {

        bitmap = Bitmap.createBitmap(dispWidth, dispHeight, Bitmap.Config.ARGB_8888);


        if (isDetectUnsafeActs) {
            tensorflowPoseDetect(bmp);
        }

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                faceRect.setImageBitmap(bitmap);
            }
        });


    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            dispWidth = faceRect.getWidth();
            dispHeight = faceRect.getHeight();
           Log.d(TAG, "onWindowFocusChanged: " + dispHeight + " " + dispWidth);
            bitmap = Bitmap.createBitmap(dispWidth, dispHeight, Bitmap.Config.ARGB_8888);
            faceRect.setImageBitmap(bitmap);
        }
    }

    private void drawFaceRect(final ImageView faceRect, int dispWidth, int dispHeight) {

        bitmap = Bitmap.createBitmap(dispWidth, dispHeight, Bitmap.Config.ARGB_8888);

        final String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/0_OpenSDK/Face_Detection";
        final File fileList = new File(path);

        facedetect = new MyThread() {
            @Override
            public void run() {

                    Log.d(TAG, "run: " + "开始" + code);
                    int code = 1;
                    File file = new File(path + "/" + code + ".jpg");
                    Log.d(TAG, "run: " + file.getAbsolutePath() + file.exists() + fileList.list().length);
                    while (!facedetect.exit) {
                        bitmap = Bitmap.createBitmap(dispWidth, dispHeight, Bitmap.Config.ARGB_8888);

                        /*runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                faceRect.setImageBitmap(bitmap);
                            }
                        });*/
                        Log.d(TAG, "run: " + "进去循环" + code);
                        if (!file.exists()) {
                            if (code > 5) {
                                code = 1;
                                continue;
                            } else {
                                code += 1;
                                file = new File(path + "/" + code + ".jpg");
                                continue;
                            }

                        }

                        if (file.length() == 0) {
                            try {
                                Thread.sleep(500);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            Log.d(TAG, "run: " + "文件大小为0");
                            continue;
                        }
                        if (faceDetectState) {
                            faceDetect(file.getAbsolutePath());
                        } else if (isDetectUnsafeActs) {
                            //tensorflowPoseDetect(file.getAbsolutePath());
                        }

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                faceRect.setImageBitmap(bitmap);
                            }
                        });

                        code += 1;
                        file.delete();
                        file = new File(path + "/" + code + ".jpg");

                        Log.d(TAG, "run: " + "进入下一个循环");

                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    Log.d(TAG, "run: " + "跳出循环");
                }

        };

           facedetect.start();

    }
    //动作识别
    private void tensorflowPoseDetect(Bitmap bitmap1) {
        Log.d(TAG, "tensorflowPoseDetect: ");
        //Bitmap bitmap1 = BitmapFactory.decodeFile(filePath);
        bitmap1 = getNewBitmap(bitmap1, MP_INPUT_SIZE, MP_INPUT_SIZE);
        long start = SystemClock.uptimeMillis();
        List<Classifier.Recognition> mResults = detector.recognizeImage(bitmap1);
        long end = SystemClock.uptimeMillis() - start;
        LOGGER.i("处理时间: " + end + "毫秒");
        bitmap = Bitmap.createBitmap(dispWidth, dispHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas1 = new Canvas(bitmap);

        draw_humans(canvas1, mResults.get(0).humans);

        /*for (TensorFlowPoseDetector.Human human : mResults.get(0).humans) {
            int cp = Common.CocoPart.values().length;
            Set<Integer> part_idxs = human.parts.keySet();
            for (Common.CocoPart i : Common.CocoPart.values()) {
                //if i not in part_idxs:
                if (!part_idxs.contains(i.index)) {
                    LOGGER.w("COORD %s, NULL, NULL", i.toString());
                    continue;
                }
                TensorFlowPoseDetector.Coord part_coord = human.parts.get(i.index);
                Log.d("sadsa", "my time: " + (part_coord.x * bitmap.getWidth() + 0.5f) + " " + (part_coord.y * bitmap.getHeight() + 0.5f));

            }
        }*/
    }

    //开始识别并绘制矩形框
    private void faceDetect(String filePath) {
        Bitmap originalBitmap = BitmapFactory.decodeFile(filePath);
        Bitmap alignedBitmap = ArcSoftImageUtil.getAlignedBitmap(originalBitmap, true);
        if (alignedBitmap == null) {
            return;
        }
        byte[] bgr24 = ArcSoftImageUtil.createImageData(alignedBitmap.getWidth(), alignedBitmap.getHeight(), ArcSoftImageFormat.BGR24);
        int transformCode = ArcSoftImageUtil.bitmapToImageData(alignedBitmap, bgr24, ArcSoftImageFormat.BGR24);
        if (transformCode != ArcSoftImageUtilError.CODE_SUCCESS) {
            Log.d(TAG, "onClick: " + transformCode);
            return;
        }
        int processMask = FaceEngine.ASF_AGE | FaceEngine.ASF_GENDER;
        List<FaceInfo> faceInfoList = new ArrayList<>();
        List<AgeInfo> myAgeInfo = new ArrayList<>();
        List<GenderInfo> myGenderInfo = new ArrayList<>();
        int errCode = faceEngine.detectFaces(bgr24, alignedBitmap.getWidth(), alignedBitmap.getHeight(), FaceEngine.CP_PAF_BGR24, faceInfoList);
        int myErrCode1 = faceEngine.process(bgr24, alignedBitmap.getWidth(), alignedBitmap.getHeight(), FaceEngine.CP_PAF_BGR24, faceInfoList, processMask);
        int myErrCode2 = faceEngine.getAge(myAgeInfo);
        int myErrCode3 = faceEngine.getGender(myGenderInfo);


        canvas = new Canvas(bitmap);
        paint = new Paint();
        paint.setAntiAlias(true);

        if (errCode == com.arcsoft.face.ErrorInfo.MOK && myErrCode1 == com.arcsoft.face.ErrorInfo.MOK && myErrCode2 == com.arcsoft.face.ErrorInfo.MOK && myErrCode3 == com.arcsoft.face.ErrorInfo.MOK && faceInfoList.size() > 0) {
                if (isFaceCompare) {
                    faceCompare(bgr24, alignedBitmap.getWidth(), alignedBitmap.getHeight(), faceInfoList, myAgeInfo, myGenderInfo);
                } else {
                    drawFaceRect(canvas, paint, faceInfoList, myAgeInfo, myGenderInfo);
                }


        } else {
            Log.d(TAG, "onClick: " + "detect failed! " + errCode);
        }

    }

    //开启人脸对比和矩形绘制
    private void faceCompare(byte[] data, int width, int height, List<FaceInfo> faceInfoList, List<AgeInfo> ageInfoList, List<GenderInfo> genderInfoList) {
        if (faceInfoList.size() == 0) {
            return;
        }

        if (registeredPersonList.size() == 0) {
            registeredPersonList = getPersonData();
            /*if (registeredPersonList.size() == 0) {
                return;
            }*/
        } else if (isRegListUpdate) {
            registeredPersonList = getPersonData();
            isRegListUpdate = false;
            if (registeredPersonList.size() == 0) {
                return;
            }
        }

         for (int i = 0; i < faceInfoList.size(); i++) {
             FaceFeature myFaceFeature = new FaceFeature();
             boolean isDetected = false;
             int myErrCode = faceEngine.extractFaceFeature(data, width, height, FaceEngine.CP_PAF_BGR24, faceInfoList.get(i), myFaceFeature);
             if (myErrCode != com.arcsoft.face.ErrorInfo.MOK) {
                 continue;
             }
             if (registeredPersonList.size() == 0) {
                 drawFaceRect1(canvas, paint, null, faceInfoList.get(i), ageInfoList.get(i), genderInfoList.get(i), false);
                 continue;
             }

             for (int j = 0; j < registeredPersonList.size(); j++) {
                 FaceSimilar faceSimilar = new FaceSimilar();
                 faceEngine.compareFaceFeature(myFaceFeature, registeredPersonList.get(j).getFaceFeature(), faceSimilar);
                 //Log.d(TAG, "faceCompare: " + faceSimilar.getScore());
                 isDetected = faceSimilar.getScore() >= 0.8;
                 if (isDetected) {
                    drawFaceRect1(canvas, paint, registeredPersonList.get(j).getName(), faceInfoList.get(i), ageInfoList.get(i), genderInfoList.get(i), isDetected);
                        if (isOpenAlarm && unRegisteredPersonInfoList.size() != 0) {
                            for (int k = 0; k < unRegisteredPersonInfoList.size(); k++){
                                FaceSimilar faceSimilar1 = new FaceSimilar();
                                faceEngine.compareFaceFeature(myFaceFeature, unRegisteredPersonInfoList.get(k).getFaceFeature(), faceSimilar1);
                                if (faceSimilar1.getScore() >= 0.8) {
                                    unRegisteredPersonInfoList.get(k).count = 0;
                                    unRegisteredPersonInfoList.get(k).noOperationCount = 0;
                                    break;
                                }
                            }
                        }
                     break;
                 }
             }
             if (!isDetected) {
                 drawFaceRect1(canvas, paint, null, faceInfoList.get(i), ageInfoList.get(i), genderInfoList.get(i), isDetected);
                 if (isOpenAlarm) {
                     if (unRegisteredPersonInfoList.size() != 0) {
                         boolean isEverDetected = false;
                         for (int k = 0; k < unRegisteredPersonInfoList.size(); k++){
                             FaceSimilar faceSimilar = new FaceSimilar();
                             faceEngine.compareFaceFeature(myFaceFeature, unRegisteredPersonInfoList.get(k).getFaceFeature(), faceSimilar);
                             if (faceSimilar.getScore() >= 0.8) {
                                 unRegisteredPersonInfoList.get(k).count++;
                                 unRegisteredPersonInfoList.get(k).isModified = true;
                                 unRegisteredPersonInfoList.get(k).noOperationCount = 0;
                                 isEverDetected = true;
                                 break;
                             }
                         }
                         if (!isEverDetected) {
                             UnRegisteredPersonInfo personInfo = new UnRegisteredPersonInfo();
                             personInfo.setFaceFeature(myFaceFeature);
                             unRegisteredPersonInfoList.add(personInfo);
                         }

                     } else {
                         UnRegisteredPersonInfo personInfo = new UnRegisteredPersonInfo();
                         personInfo.setFaceFeature(myFaceFeature);
                         unRegisteredPersonInfoList.add(personInfo);
                     }
                 }

             }

         }
         //标记未操作的数据，达到一定次数从列表中删除，并检测可疑用户数量
        if (isOpenAlarm && unRegisteredPersonInfoList.size() > 0) {
            int unRegisteredPersonNum = 0;
            int index = 0;
            Iterator<UnRegisteredPersonInfo> iterator = unRegisteredPersonInfoList.iterator();
            while (iterator.hasNext()) {
                if (index == unRegisteredPersonInfoList.size())
                {   //防止下标越界
                    break;
                }
                Log.d(TAG, "faceCompare: " + index);
                UnRegisteredPersonInfo info = iterator.next();
                if (unRegisteredPersonInfoList.get(index).count >= 7) {
                    unRegisteredPersonNum++;
                }
                if (unRegisteredPersonInfoList.get(index).isModified) {
                    unRegisteredPersonInfoList.get(index).isModified = false;
                } else {
                    unRegisteredPersonInfoList.get(index).noOperationCount++;
                }
                if (unRegisteredPersonInfoList.get(index).noOperationCount >= 4) {
                    iterator.remove();
                }
                index++;
            }
            if (unRegisteredPersonNum != 0) {
                if (mTTS != null && !mTTS.isSpeaking()) {
                    mTTS.speak("检测到" + unRegisteredPersonNum + "个可疑人员！", TextToSpeech.QUEUE_FLUSH, null, "" + unRegisteredPersonNum);
                }

            }

        }

    }

    private void drawFaceRect(Canvas canvas, Paint paint, List<FaceInfo> faceInfoList, List<AgeInfo> ageInfoList, List<GenderInfo> genderInfoList) {
        for (int i = 0; i < faceInfoList.size(); i++) {
            paint.setStrokeWidth(6);
            paint.setStyle(Paint.Style.STROKE);
            paint.setColor(Color.GREEN);
            canvas.drawRect((float) faceInfoList.get(i).getRect().left / 1920 * dispWidth, (float) faceInfoList.get(i).getRect().top / 1080 * dispHeight,
                    (float) faceInfoList.get(i).getRect().right / 1920 * dispWidth, (float) faceInfoList.get(i).getRect().bottom / 1080 * dispHeight, paint);
            // Log.d(TAG, "onClick: 矩形坐标 " + face.getRect().left + " " + face.getRect().top + " " + face.getRect().right + " " + face.getRect().bottom);
            String str = (genderInfoList.get(i).getGender() == 0 ? "男" : genderInfoList.get(i).getGender() == 1 ? "女" : "未知") + "，" + ageInfoList.get(i).getAge();
            paint.setStrokeWidth(1);
            paint.setStyle(Paint.Style.FILL_AND_STROKE);
            paint.setTextSize(faceInfoList.get(i).getRect().width() >> 3);
            canvas.drawText(str, (float) faceInfoList.get(i).getRect().left / 1920 * dispWidth, (float) faceInfoList.get(i).getRect().top / 1080 * dispHeight - 13, paint);
        }
    }

    private void drawFaceRect1(Canvas canvas, Paint paint, String name, FaceInfo faceInfo, AgeInfo ageInfo, GenderInfo genderInfo, boolean isDetected) {
        paint.setStrokeWidth(6);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(isDetected? Color.GREEN : Color.RED);
        canvas.drawRect((float) faceInfo.getRect().left / 1920 * dispWidth, (float) faceInfo.getRect().top / 1080 * dispHeight,
                (float) faceInfo.getRect().right / 1920 * dispWidth, (float) faceInfo.getRect().bottom / 1080 * dispHeight, paint);
        // Log.d(TAG, "onClick: 矩形坐标 " + face.getRect().left + " " + face.getRect().top + " " + face.getRect().right + " " + face.getRect().bottom);
        String str = (isDetected? (name == null? "未命名" : name) : "未注册") + "，" + (genderInfo.getGender() == 0 ? "男" : genderInfo.getGender() == 1 ? "女" : "未知") + "，" + ageInfo.getAge();
        paint.setStrokeWidth(1);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setTextSize(faceInfo.getRect().width() >> 3);
        canvas.drawText(str, (float) faceInfo.getRect().left / 1920 * dispWidth, (float) faceInfo.getRect().top / 1080 * dispHeight - 13, paint);
    }

    private int faceRegister(Bitmap bitmap) {
        int succeedReg = 0;
        boolean isNew =true;
        Bitmap alignedBitmap = ArcSoftImageUtil.getAlignedBitmap(bitmap, true);
        if (alignedBitmap == null) {
            return -1;
        }
        byte[] bgr24 = ArcSoftImageUtil.createImageData(alignedBitmap.getWidth(), alignedBitmap.getHeight(), ArcSoftImageFormat.BGR24);
        int transformCode = ArcSoftImageUtil.bitmapToImageData(alignedBitmap, bgr24, ArcSoftImageFormat.BGR24);
        if (transformCode != ArcSoftImageUtilError.CODE_SUCCESS) {
            Log.d(TAG, "onClick: " + transformCode);
            return -1;
        }
        //图片中检测到的人脸
        int processMask = FaceEngine.ASF_AGE | FaceEngine.ASF_GENDER;
        List<FaceInfo> myFaceInfo = new ArrayList<>();

        List<AgeInfo> myAgeInfo = new ArrayList<>();
        List<GenderInfo> myGenderInfo = new ArrayList<>();
        List<Bitmap> myBitmaps = new ArrayList<>();


        int myErrCode = faceEngine.detectFaces(bgr24, alignedBitmap.getWidth(), alignedBitmap.getHeight(), FaceEngine.CP_PAF_BGR24, myFaceInfo);
        int myErrCode1 = faceEngine.process(bgr24, alignedBitmap.getWidth(), alignedBitmap.getHeight(), FaceEngine.CP_PAF_BGR24, myFaceInfo, processMask);
        int myErrCode2 = faceEngine.getAge(myAgeInfo);
        int myErrCode3 = faceEngine.getGender(myGenderInfo);
        for (FaceInfo faceInfo : myFaceInfo) {
                if (faceInfo.getRect().bottom < 0) {
                    faceInfo.getRect().bottom = 0;
                } else if (faceInfo.getRect().right < 0) {
                    faceInfo.getRect().right = 0;
                } else if (faceInfo.getRect().top < 0) {
                    faceInfo.getRect().top = 0;
                } else if (faceInfo.getRect().left < 0) {
                    faceInfo.getRect().left = 0;
                }

            myBitmaps.add(ImageUtils.cropImage(bitmap, faceInfo.getRect()));
        }
        if (myBitmaps.size() != myFaceInfo.size()) {
            Log.d(TAG, "faceRegister: " + "获取裁剪后的图像失败3！");
            return -1;
        }


        if (myFaceInfo.size() == 0 | myAgeInfo.size() == 0 | myGenderInfo.size() == 0) {
            return -1;
        }
        FaceSimilar faceSimilar = new FaceSimilar();
        for (int i = 0; i < myFaceInfo.size(); i++) {
            //每次都要重新生成FaceFeature对象，否则会出错
            FaceFeature faceFeature = new FaceFeature();
            int myErrCode4 = faceEngine.extractFaceFeature(bgr24, alignedBitmap.getWidth(), alignedBitmap.getHeight(), FaceEngine.CP_PAF_BGR24, myFaceInfo.get(i), faceFeature);
            if (myErrCode == com.arcsoft.face.ErrorInfo.MOK && myErrCode1 == com.arcsoft.face.ErrorInfo.MOK && myErrCode2 == com.arcsoft.face.ErrorInfo.MOK && myErrCode3 == com.arcsoft.face.ErrorInfo.MOK && myErrCode4 == com.arcsoft.face.ErrorInfo.MOK) {
                if (registeredPersonList.size() == 0) {
                    String fileName = UUID.randomUUID().toString();
                    PersonInfo personInfo = new PersonInfo();
                    personInfo.setId(fileName);
                    personInfo.setGender(myGenderInfo.get(i).getGender());
                    personInfo.setAge(myAgeInfo.get(i).getAge());
                    personInfo.setFaceInfo(myFaceInfo.get(i));
                    personInfo.setFaceFeature(faceFeature);
                    registeredPersonList.add(personInfo);
                    savePersonPortrait(myBitmaps.get(i), fileName);
                    succeedReg += 1;
                } else {
                    for (int j = 0; j < registeredPersonList.size(); j++) {
                        FaceFeature savedFaceFeature = registeredPersonList.get(j).getFaceFeature();
                        faceEngine.compareFaceFeature(faceFeature, savedFaceFeature, faceSimilar);
                        if (faceSimilar.getScore() >= 0.8) {
                            isNew = false;
                            break;
                        }
                    }
                    if (isNew) {
                        String fileName = UUID.randomUUID().toString();
                        PersonInfo personInfo = new PersonInfo();
                        personInfo.setId(fileName);
                        personInfo.setGender(myGenderInfo.get(i).getGender());
                        personInfo.setAge(myAgeInfo.get(i).getAge());
                        personInfo.setFaceInfo(myFaceInfo.get(i));
                        personInfo.setFaceFeature(faceFeature);
                        registeredPersonList.add(personInfo);
                        savePersonPortrait(myBitmaps.get(i), fileName);
                        succeedReg += 1;
                        isNew = true;
                    }
                }

            }

        }
        return succeedReg;
    }

    //通过返回的data参数进行人脸注册，同时剔除重复样本
    private void imageReg(Intent data) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                int faceNum1 = registeredPersonList.size();

                ArrayList<String> imagesPath = data.getStringArrayListExtra(ImageSelector.SELECTED_RESULT);
                if(imagesPath != null){
                    //TODO  do something...
                    for (String path : imagesPath) {
                        Bitmap bitmap = BitmapFactory.decodeFile(path);
                        succeedReg = faceRegister(bitmap);
                    }
                }
                int faceNum2 = registeredPersonList.size();
                if (faceNum2 > faceNum1) {
                    savePersonData(registeredPersonList);
                    isRegListUpdate = true;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //人脸信息数据存储
                            Toast.makeText(EZRealPlayActivity.this, "注册成功！新增" + succeedReg + "个样本", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(EZRealPlayActivity.this, "注册失败，未检测到人脸或重复注册！", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).start();
    }

    private void camreaReg(Intent data) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (data.hasExtra("data")) {
                    int faceNum1 = registeredPersonList.size();
                    Bitmap photo = data.getParcelableExtra("data");

                    succeedReg = faceRegister(photo);
                    int faceNum2 = registeredPersonList.size();
                    if (faceNum2 > faceNum1) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                savePersonData(registeredPersonList);
                                isRegListUpdate = true;
                                Toast.makeText(EZRealPlayActivity.this, "注册成功！新增" + succeedReg + "个样本", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                Toast.makeText(EZRealPlayActivity.this, "注册失败，未检测到人脸或重复注册！", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(EZRealPlayActivity.this, "获取图片失败！", Toast.LENGTH_SHORT).show();
                        }
                    });

                }

            }
        }).start();
    }

    private void savePersonData(ArrayList<PersonInfo> personInfoList) {

        SharedPreferences.Editor editor = getSharedPreferences("person_data", MODE_PRIVATE).edit();
        Gson gson = new Gson();
        String json = gson.toJson(personInfoList);
        editor.putString("personInfoList", json);
        editor.apply();
    }

    //从SD卡读取保存的人脸数据
    private ArrayList<PersonInfo> getPersonData() {
        SharedPreferences sharedPreferences = getSharedPreferences("person_data", MODE_PRIVATE);
        ArrayList<PersonInfo> personInfos = null;
        Gson gson = new Gson();
        String json = sharedPreferences.getString("personInfoList", "");
        Type type = new TypeToken<ArrayList<PersonInfo>>() {}.getType();
        personInfos = gson.fromJson(json, type);
        return personInfos;
    }

    private void savePersonPortrait(Bitmap bitmap, String fileName) {
        String filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/0_OpenSDK/Portraits";
        File file = new File(filePath);

        if (!file.exists()) {
            file.mkdir();
        }

        String imageFile = filePath + "/" + fileName + ".jpg";
        try {
            EZUtils.saveCapturePictrue(imageFile, bitmap);
        } catch (InnerException e) {
            e.printStackTrace();
        }


    }

    public void deletePersonData(String id) {
        String filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/0_OpenSDK/Portraits";
        File file = new File(filePath);
        File[] files = file.listFiles();
        String fileName = id + ".jpg";
        if (!file.exists()) {
            file.mkdir();
        }
        for (File mFile : files) {
            if (fileName.equals(mFile.getName())) {
                mFile.delete();
            }
        }
        if (registeredPersonList != null) {
            for (int i = 0; i < registeredPersonList.size(); i++) {
                if (registeredPersonList.get(i).getId().equals(id)) {
                    registeredPersonList.remove(i);
                    break;
                }
            }
        }
        ArrayList<PersonInfo> arrayList = new ArrayList<>();
        if ((arrayList = getPersonData()) != null) {
            for (int i = 0; i < arrayList.size(); i++) {
                if (arrayList.get(i).getId().equals(id)) {
                    arrayList.remove(i);
                    break;
                }
            }
        }
        savePersonData(arrayList);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(EZRealPlayActivity.this, "删除成功！", Toast.LENGTH_SHORT).show();
            }
        });

    }

    //修改和设置用户名

    private void setUserName(String id, String name) {
        if (registeredPersonList != null) {
            for (int i = 0; i < registeredPersonList.size(); i++) {
                if (registeredPersonList.get(i).getId().equals(id)) {
                    registeredPersonList.get(i).setName(name);
                    break;
                }
            }
        }
        ArrayList<PersonInfo> arrayList = new ArrayList<>();
        if ((arrayList = getPersonData()) != null) {
            for (int i = 0; i < arrayList.size(); i++) {
                if (arrayList.get(i).getId().equals(id)) {
                    arrayList.get(i).setName(name);
                    break;
                }
            }
        }
        savePersonData(arrayList);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(EZRealPlayActivity.this, "修改成功！", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //已注册用户的信息删除和用户名修改
    private void faceRegListEdit() {
        isEdit = !isEdit;
        if (isEdit) {
            faceRegEdit.setBackground(getResources().getDrawable(R.drawable.face_reg_edit_cancel, null));
        } else {
            faceRegEdit.setBackground(getResources().getDrawable(R.drawable.face_reg_edit, null));
        }
        if (personInfoAdapter != null) {
            personInfoAdapter = new PersonInfoAdapter(registeredPersonList, isEdit);
            recyclerView.setAdapter(personInfoAdapter);
            PersonInfoAdapter.OnItemClickListener listener = new PersonInfoAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, String id, int position, boolean isSetName) {
                    if (isSetName) {

                        GetPersonNameAlertDialog dialog = new GetPersonNameAlertDialog(EZRealPlayActivity.this, new GetPersonNameAlertDialog.OnEditInputFinishedListener() {
                            @Override
                            public void editInputFinished(String userName) {
                                if (userName != null) {
                                    setUserName(id, userName);
                                    personInfoAdapter.notifyDataSetChanged();
                                    isRegListUpdate = true;
                                } else {
                                    Toast.makeText(EZRealPlayActivity.this, "用户名不能为空", Toast.LENGTH_SHORT);
                                }
                            }
                        });
                        dialog.setView(new EditText(EZRealPlayActivity.this));
                        dialog.show();

                    } else {
                        deletePersonData(id);
                        if (registeredPersonList.size() == 0) {
                            faceRegEdit.setVisibility(View.INVISIBLE);
                            faceRegListNull.setVisibility(View.VISIBLE);
                        }
                        Log.d(TAG, "监听2: " + position + " " + registeredPersonList.size());

                        personInfoAdapter.notifyItemRemoved(position);
                        if (position != registeredPersonList.size()) {
                            personInfoAdapter.notifyItemRangeChanged(position, registeredPersonList.size() - position);
                            isRegListUpdate = true;
                        }

                    }
                }
            };
            personInfoAdapter.setOnItemClickListener(listener);

        }

    }

    //首次使用虹软需要激活引擎
    private void activeEngine() {
        int code = FaceEngine.activeOnline(this, Constants.APP_ID, Constants.SDK_KEY);
        if(code == com.arcsoft.face.ErrorInfo.MOK){
            Log.i(TAG, "activeOnline success");
        }else if(code == com.arcsoft.face.ErrorInfo.MERR_ASF_ALREADY_ACTIVATED){
            Log.i(TAG, "already activated");
        }else{
            Log.i(TAG, "activeOnline failed, code is : " + code);
        }
    }

    public Bitmap getNewBitmap(Bitmap bitmap, int newWidth ,int newHeight){
        // 获得图片的宽高.
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        // 计算缩放比例.
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 取得想要缩放的matrix参数.
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // 得到新的图片.
        Bitmap newBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
        return newBitmap;
    }
    private Integer HUMAN_RADIUS = 10;
    //描出人体骨骼
    private void draw_humans(Canvas canvas, List<TensorFlowPoseDetector.Human> human_list) {
        //def draw_humans(img, human_list):
        // image_h, image_w = img_copied.shape[:2]
        int cp = Common.CocoPart.values().length;
        int image_w = canvas.getWidth();
        int image_h = canvas.getHeight();

        //    for human in human_list:
        for (TensorFlowPoseDetector.Human human : human_list) {
            Point[] centers = new Point[cp];
            //part_idxs = human.keys()
            Set<Integer> part_idxs = human.parts.keySet();

            LOGGER.i("COORD =====================================");
            //# draw point
            //for i in range(CocoPart.Background.value):
            for (Common.CocoPart i : Common.CocoPart.values()) {
                //if i not in part_idxs:
                if (!part_idxs.contains(i.index)) {
                    LOGGER.w("COORD %s, NULL, NULL", i.toString());
                    continue;
                }
                //part_coord = human[i][1]
                TensorFlowPoseDetector.Coord part_coord = human.parts.get(i.index);
                //center = (int(part_coord[0] * image_w + 0.5), int(part_coord[1] * image_h + 0.5))
                Point center = new Point((int) (part_coord.x * image_w + 0.5f), (int) (part_coord.y * image_h + 0.5f));
                //centers[i] = center
                centers[i.index] = center;

                //cv2.circle(img_copied, center, 3, CocoColors[i], thickness=3, lineType=8, shift=0)
                Paint paint = new Paint();
                paint.setColor(Color.rgb(Common.CocoColors[i.index][0], Common.CocoColors[i.index][1], Common.CocoColors[i.index][2]));
                paint.setStyle(Paint.Style.FILL);
                canvas.drawCircle(center.x, center.y, HUMAN_RADIUS, paint);

                LOGGER.i("COORD %s, %f, %f", i.toString(), part_coord.x, part_coord.y);
            }

            //# draw line
            //for pair_order, pair in enumerate(CocoPairsRender):
            for (int pair_order = 0; pair_order < Common.CocoPairsRender.length; pair_order++) {
                int[] pair = Common.CocoPairsRender[pair_order];
                //if pair[0] not in part_idxs or pair[1] not in part_idxs:
                if (!part_idxs.contains(pair[0]) || !part_idxs.contains(pair[1])) {
                    continue;
                }

                //img_copied = cv2.line(img_copied, centers[pair[0]], centers[pair[1]], CocoColors[pair_order], 3)
                Paint paint = new Paint();
                paint.setAntiAlias(true);
                paint.setColor(Color.rgb(Common.CocoColors[pair_order][0], Common.CocoColors[pair_order][1], Common.CocoColors[pair_order][2]));
                paint.setStrokeWidth(HUMAN_RADIUS);
                paint.setStyle(Paint.Style.STROKE);

                canvas.drawLine(centers[pair[0]].x, centers[pair[0]].y, centers[pair[1]].x, centers[pair[1]].y, paint);
            }
        }
        //    return img_copied
    }

    private void unInitEngine() {
        if (faceEngine != null) {
            int faceEngineCode = faceEngine.unInit();
            Log.i(TAG, "unInitEngine: " + faceEngineCode);
        }
    }

    private void stopFaceDetect() {
        File file = new File(path);
        File[] files = file.listFiles();
        if (thrd != null && facedetect != null){
            thrd.exit = true;
            Log.d(TAG, "onDestroy: " + "停止截图");
            facedetect.exit = true;
        }
        for (File file1 : files) {
            if (file1.isFile()) {
                file1.delete();
            }
        }
    }

    public static byte[] toByteArray(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, baos);
        return baos.toByteArray();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == ImageSelector.REQUEST_SELECT_IMAGE && resultCode == RESULT_OK) {
            imageReg(data);


        } else if (requestCode == ImageSelector.REQUEST_OPEN_CAMERA && resultCode == RESULT_OK && data != null) {
            camreaReg(data);

        } else if (data == null | resultCode != RESULT_OK) {
            Toast.makeText(EZRealPlayActivity.this, "获取图片失败！", Toast.LENGTH_SHORT).show();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}

class MyThread extends Thread {
    public volatile boolean exit = false;
}
