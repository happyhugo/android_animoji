package io.agora.openvcall.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.media.AudioManager;
import android.media.Image;
import android.media.ImageReader;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewStub;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ai.deepar.ar.MyCustomizedCameraRenderer;
import io.agora.openvcall.R;
import io.agora.openvcall.model.AGEventHandler;
import io.agora.openvcall.model.ConstantApp;
import io.agora.openvcall.model.Message;
import io.agora.openvcall.model.Subject;
import io.agora.openvcall.model.User;
import io.agora.propeller.Constant;
import io.agora.propeller.UserStatusData;
import io.agora.propeller.VideoInfoData;
import io.agora.propeller.preprocessing.VideoPreProcessing;
import io.agora.propeller.ui.RtlLinearLayoutManager;
import io.agora.rtc.IRtcEngineEventHandler;
import io.agora.rtc.RtcEngine;
import io.agora.rtc.video.AgoraVideoFrame;
import io.agora.rtc.video.VideoCanvas;

public class ChatActivity extends BaseActivity implements AGEventHandler {

    private final static Logger log = LoggerFactory.getLogger(ChatActivity.class);

    private GridVideoViewContainer mGridVideoViewContainer;

    private RelativeLayout mSmallVideoViewDock;

    // should only be modified under UI thread
    private final HashMap<Integer, SurfaceView> mUidsList = new HashMap<>(); // uid = 0 || uid == EngineConfig.mUid

    private volatile boolean mVideoMuted = false;

    private volatile boolean mAudioMuted = false;

    private volatile int mAudioRouting = -1; // Default
    private String timu[];
//   private String timu[] = new String[]{
//                                    "1. 给你一个任意的机会，你会选择和谁共进晚餐？(所有的问题男生先回答，两个人都回答完毕点击下一题)",
//                                    "2. 你想要成名吗？以什么方式？",
//                                    "3. 你心中一个完美的日子是怎样的",
//                                    "4. 如果你可以活到 90 岁，而且身心都保持在 30 岁的状态，那这 60 年你想要怎么度过？",
//                                    "5. 关于怎么死去，你有没有过神秘的预感？",
//                                    "6. 举出你和你对面这位的 3 个共同点。",
//                                    "7. 你只有 4 分钟时间，但请在这 4 分钟内尽量详细地讲述你的人生故事。",
//                                    "8. 如果你明天醒来时能得到一种新的能力或品质，你想要的是什么？",
//                                    "9. 如果水晶球可以预测你的未来以及一切，你想要知道什么？",
//                                    "10. 有没有什么是你梦寐以求的？但为什么没有做？",
//                                    "11. 你人生中最大的成就是什么？",
//                                    "12. 你最糟糕的记忆是什么？",
//                                    "13. 如果你知道你只有一年可以活了，你会改变你的生活方式吗？为什么？",
//                                    "14. 轮流分享你认为恋人应该具有的好品质。总共分享 5 个。",
//                                    "15. 你的家庭亲密、温暖吗？你觉得你的童年是不是比其他人更幸福一些？",
//                                    "16. 你与母亲的关系怎样？",
//                                    "17. 要和对面那位成为好朋友，他或者她最应该知道的事情是什么？请与他或者她分享。",
//                                    "18. 告诉你对面那位，你喜欢他（她）什么？必须非常诚实，说一些你可能不会和第一次见面的人说的话。",
//                                    "19. 分享一件你人生中的囧事",
//                                    "20. 你上一次哭是什么时候？当着他人的面还是独自一人？",
//                                    "21. 告诉对面那位，你已经开始喜欢他身上的一些东西。",
//                                    "22. 如果你今夜就会死去，而且没有机会和任何人说，你最遗憾的没有说出口的话是？为什么你还没有告诉他们？",
//                                    "36. 分享一个你的私人困扰，并向你对面那位请求解决建议，请他（她）以自己的方式来解决。然后，再询问他（她）对于这个问题的个人感受。"};
    private int index = 0;
    private boolean heisnext = false;
    private boolean woisnext = false;
    private TextView next = null;
    private String temp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return false;
    }

    @Override
    protected void initUIandEvent() {
        initWorkerThread();
        event().addEventHandler(this);

        Intent i = getIntent();

        String channelName = i.getStringExtra(ConstantApp.ACTION_KEY_CHANNEL_NAME);

        final String encryptionKey = getIntent().getStringExtra(ConstantApp.ACTION_KEY_ENCRYPTION_KEY);

        final String encryptionMode = getIntent().getStringExtra(ConstantApp.ACTION_KEY_ENCRYPTION_MODE);

        doConfigEngine(encryptionKey, encryptionMode);

        mGridVideoViewContainer = (GridVideoViewContainer) findViewById(R.id.grid_video_view_container);
        mGridVideoViewContainer.setItemEventHandler(new VideoViewEventListener() {
            @Override
            public void onItemDoubleClick(View v, Object item) {
//                log.debug("onItemDoubleClick " + v + " " + item + " " + mLayoutType);
//
//                if (mUidsList.size() < 2) {
//                    return;
//                }
//
//                UserStatusData user = (UserStatusData) item;
//                int uid = (user.mUid == 0) ? config().mUid : user.mUid;
//
//                if (mLayoutType == LAYOUT_TYPE_DEFAULT && mUidsList.size() != 1) {
//                    switchToSmallVideoView(uid);
//                } else {
//                    switchToDefaultVideoView();
//                }
            }
        });

        SurfaceView surfaceV = setupLocalVideo(getApplicationContext());
        mUidsList.put(0, surfaceV); // get first surface view
        mGridVideoViewContainer.initViewContainer(this, 0, mUidsList); // first is now full view

        next = (TextView)findViewById(R.id.next);

        TextView textChannelName = (TextView) findViewById(R.id.channel_name);
        textChannelName.setText(channelName);

        optional();

        LinearLayout bottomContainer = (LinearLayout) findViewById(R.id.bottom_container);
        FrameLayout.MarginLayoutParams fmp = (FrameLayout.MarginLayoutParams) bottomContainer.getLayoutParams();
        fmp.bottomMargin = virtualKeyHeight() + 16;

        initMessageList();

        RtcEngine rtcEngine = rtcEngine();
        rtcEngine.muteLocalAudioStream(true);

        List<String> subject = Subject.getSubjects().get(0).getContent();
        timu = subject.toArray(new String[subject.size()]);
    }

    byte[] pixelData = new byte[480 * 640 * 4];
    // Tutorial Step 3
    private SurfaceView setupLocalVideo(Context ctx) {
        MyCustomizedCameraRenderer myCustomizedCameraRenderer = new MyCustomizedCameraRenderer(ctx);
        myCustomizedCameraRenderer.addAilistener(new MyCustomizedCameraRenderer.AiListener() {
            @Override
            public void onAIReady() {
                Log.d("ChatActivity", "onAIReady join channel");

                worker().joinChannel(getIntent().getStringExtra(ConstantApp.ACTION_KEY_CHANNEL_NAME), config().mUid);
            }
        });
        myCustomizedCameraRenderer.addFrameAvailableListener(new ImageReader.OnImageAvailableListener() {
            @Override
            public void onImageAvailable(ImageReader imageReader) {
                Image image = imageReader.acquireNextImage();//获取下一个
                Image.Plane[] planes = image.getPlanes();
                int width = image.getWidth();//设置的宽
                int height = image.getHeight();//设置的高
                int pixelStride = planes[0].getPixelStride();//像素个数，RGBA为4
                int rowStride = planes[0].getRowStride();//这里除pixelStride就是真实宽度
                int rowPadding = rowStride/pixelStride -  width;//计算多余宽度
                ByteBuffer buffer = planes[0].getBuffer();//获得buffer
//                int offset = 0;
//                for (int i = 0; i < height; ++i) {
//                    buffer.get(pixelData,offset,width*4);
//                    offset += width*4;
//                    buffer.position(buffer.position()+rowPadding*4);
//                }

                int offset = 0;
                int position = 0;
                int mark = (width-1)*pixelStride;
                for(int i=width;i>0;i--){
                    position = mark;
                    for(int j=0;j<height;j++){
                        pixelData[offset++] = buffer.get(position++);
                        pixelData[offset++] = buffer.get(position++);
                        pixelData[offset++] = buffer.get(position++);
                        pixelData[offset++] = buffer.get(position++);
                        position = position+rowStride-pixelStride;
                    }
                    mark -= pixelStride;
                }


//                Bitmap b = Bitmap.createBitmap(640,480,Bitmap.Config.ARGB_8888);
//                b.copyPixelsFromBuffer(ByteBuffer.wrap(pixelData));
//                File f = new File(Environment.getExternalStorageDirectory(), System.currentTimeMillis()+".jpg");
//
//                try {
//                    FileOutputStream out = new FileOutputStream(f);
//                    b.compress(Bitmap.CompressFormat.JPEG, 90, out);
//                    out.flush();
//                    out.close();
//                } catch (FileNotFoundException e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                } catch (IOException e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                }



                AgoraVideoFrame vf = new AgoraVideoFrame();
                vf.buf = pixelData;
                vf.format = AgoraVideoFrame.FORMAT_RGBA;
                vf.timeStamp = System.currentTimeMillis();
                vf.stride = 640;
                vf.height = 480;
                vf.rotation = 90;
                boolean result = worker().getRtcEngine().pushExternalVideoFrame(vf);
                Log.i("ChatActivity","........"+offset);
                image.close();//用完需要关闭
            }
        });
        return myCustomizedCameraRenderer;
    }

    public void onClickHideIME(View view) {
        log.debug("onClickHideIME " + view);

        closeIME(findViewById(R.id.msg_content));

        findViewById(R.id.msg_input_container).setVisibility(View.GONE);
        findViewById(R.id.bottom_action_end_call).setVisibility(View.GONE);
//      findViewById(R.id.bottom_action_container).setVisibility(View.GONE);
    }

    private InChannelMessageListAdapter mMsgAdapter;

    private ArrayList<Message> mMsgList;

    private void initMessageList() {
        mMsgList = new ArrayList<>();
        RecyclerView msgListView = (RecyclerView) findViewById(R.id.msg_list);

        mMsgAdapter = new InChannelMessageListAdapter(this, mMsgList);
        mMsgAdapter.setHasStableIds(true);
        msgListView.setAdapter(mMsgAdapter);
        msgListView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false));
        msgListView.addItemDecoration(new MessageListDecoration());
    }

    private void notifyMessageChanged(Message msg) {
        mMsgList.add(msg);

        int MAX_MESSAGE_COUNT = 1;

        if (mMsgList.size() > MAX_MESSAGE_COUNT) {
            int toRemove = mMsgList.size() - MAX_MESSAGE_COUNT;
            for (int i = 0; i < toRemove; i++) {
                mMsgList.remove(i);
            }
        }

        mMsgAdapter.notifyDataSetChanged();
    }

    private int mDataStreamId;

    private void sendChannelMsg(String msgStr) {
        RtcEngine rtcEngine = rtcEngine();
        if (mDataStreamId <= 0) {
            mDataStreamId = rtcEngine.createDataStream(true, true); // boolean reliable, boolean ordered
        }

        if (mDataStreamId < 0) {
            String errorMsg = "Create data stream error happened " + mDataStreamId;
            log.warn(errorMsg);
            showLongToast(errorMsg);
            return;
        }

        byte[] encodedMsg;
        try {
            encodedMsg = msgStr.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            encodedMsg = msgStr.getBytes();
        }

        rtcEngine.sendStreamMessage(mDataStreamId, encodedMsg);
    }

    private void optional() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        setVolumeControlStream(AudioManager.STREAM_VOICE_CALL);
    }

    private void optionalDestroy() {
    }

    private int getVideoProfileIndex() {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        int profileIndex = pref.getInt(ConstantApp.PrefManager.PREF_PROPERTY_PROFILE_IDX, ConstantApp.DEFAULT_PROFILE_IDX);
        if (profileIndex > ConstantApp.VIDEO_PROFILES.length - 1) {
            profileIndex = ConstantApp.DEFAULT_PROFILE_IDX;

            // save the new value
            SharedPreferences.Editor editor = pref.edit();
            editor.putInt(ConstantApp.PrefManager.PREF_PROPERTY_PROFILE_IDX, profileIndex);
            editor.apply();
        }
        return profileIndex;
    }

    private void doConfigEngine(String encryptionKey, String encryptionMode) {
        int vProfile = ConstantApp.VIDEO_PROFILES[getVideoProfileIndex()];
        worker().configEngine(vProfile, encryptionKey, encryptionMode);
        worker().getRtcEngine().setExternalVideoSource(true,false,true);
    }

    public void onBtn0Clicked(View view) {
        log.info("onBtn0Clicked " + view + " " + mVideoMuted + " " + mAudioMuted);
        showMessageEditContainer();
    }

    private void showMessageEditContainer() {
        findViewById(R.id.bottom_action_container).setVisibility(View.GONE);
        findViewById(R.id.bottom_action_end_call).setVisibility(View.GONE);
        findViewById(R.id.msg_input_container).setVisibility(View.VISIBLE);

        EditText edit = (EditText) findViewById(R.id.msg_content);

        edit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND
                        || (event != null && event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    String msgStr = v.getText().toString();
                    if (TextUtils.isEmpty(msgStr)) {
                        return false;
                    }
                    sendChannelMsg(msgStr);

                    v.setText("");

                    Message msg = new Message(Message.MSG_TYPE_TEXT,
                            new User(config().mUid, String.valueOf(config().mUid)), msgStr);
                    notifyMessageChanged(msg);

                    return true;
                }
                return false;
            }
        });

        openIME(edit);
    }

    public void onCustomizedFunctionClicked(View view) {
        log.info("onCustomizedFunctionClicked " + view + " " + mVideoMuted + " " + mAudioMuted + " " + mAudioRouting);
        if (mVideoMuted) {
            onSwitchSpeakerClicked();
        } else {
            onSwitchCameraClicked();
        }
    }

    private void onSwitchCameraClicked() {
//        RtcEngine rtcEngine = rtcEngine();
//        rtcEngine.switchCamera();
        //实现自己的
    }

    private void onSwitchSpeakerClicked() {
        RtcEngine rtcEngine = rtcEngine();
        rtcEngine.setEnableSpeakerphone(mAudioRouting != 3);
    }

    @Override
    protected void deInitUIandEvent() {
        optionalDestroy();

        doLeaveChannel();
        event().removeEventHandler(this);
        MyCustomizedCameraRenderer local= (MyCustomizedCameraRenderer)getLocalView();
        if (local != null) {
            local.onDestroy();
        }
        mUidsList.clear();
    }

    private void doLeaveChannel() {
        worker().leaveChannel(config().mChannel);
    }

    public void onEndCallClicked(View view) {
        log.info("onEndCallClicked " + view);
        dialog();
//      finish();
    }

    protected void dialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("确认退出吗？");
        builder.setTitle("提示");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                ChatActivity.this.finish();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    private VideoPreProcessing mVideoPreProcessing;

    public void onBtnNClicked(View view) {
        if (mVideoPreProcessing == null) {
            mVideoPreProcessing = new VideoPreProcessing();
        }

        ImageView iv = (ImageView) view;
        Object showing = view.getTag();
        if (showing != null && (Boolean) showing) {
            mVideoPreProcessing.enablePreAudioProcessing(false);
            iv.setTag(null);
            iv.clearColorFilter();
        } else {
            mVideoPreProcessing.enablePreAudioProcessing(true);
            iv.setTag(true);
            iv.setColorFilter(getResources().getColor(R.color.agora_blue), PorterDuff.Mode.MULTIPLY);
        }
    }

    public void onVoiceChatClicked(View view) {
        log.info("onVoiceChatClicked " + view + " " + mUidsList.size() + " video_status: " + mVideoMuted + " audio_status: " + mAudioMuted);
        if (mUidsList.size() == 0) {
            return;
        }

        SurfaceView surfaceV = getLocalView();
        if (surfaceV == null ) {
            log.warn("onVoiceChatClicked " + view + " " + surfaceV);
            return;
        }

        RtcEngine rtcEngine = rtcEngine();
        mVideoMuted = !mVideoMuted;

        if (mVideoMuted) {
            rtcEngine.disableVideo();
        } else {
            rtcEngine.enableVideo();
        }

        ImageView iv = (ImageView) view;

        iv.setImageResource(mVideoMuted ? R.drawable.btn_video : R.drawable.btn_voice);

        hideLocalView(mVideoMuted);

        if (mVideoMuted) {
            resetToVideoDisabledUI();
        } else {
            resetToVideoEnabledUI();
        }
    }

    private SurfaceView getLocalView() {
        for (HashMap.Entry<Integer, SurfaceView> entry : mUidsList.entrySet()) {
            if (entry.getKey() == 0 || entry.getKey() == config().mUid) {
                return entry.getValue();
            }
        }

        return null;
    }

    private void hideLocalView(boolean hide) {
        int uid = config().mUid;
        doHideTargetView(uid, hide);
    }

    private void doHideTargetView(int targetUid, boolean hide) {
        HashMap<Integer, Integer> status = new HashMap<>();
        status.put(targetUid, hide ? UserStatusData.VIDEO_MUTED : UserStatusData.DEFAULT_STATUS);
        if (mLayoutType == LAYOUT_TYPE_DEFAULT) {
            mGridVideoViewContainer.notifyUiChanged(mUidsList, targetUid, status, null);
        } else if (mLayoutType == LAYOUT_TYPE_SMALL) {
            UserStatusData bigBgUser = mGridVideoViewContainer.getItem(0);
            if (bigBgUser.mUid == targetUid) { // big background is target view
                mGridVideoViewContainer.notifyUiChanged(mUidsList, targetUid, status, null);
            } else { // find target view in small video view list
                log.warn("SmallVideoViewAdapter call notifyUiChanged " + mUidsList + " " + (bigBgUser.mUid & 0xFFFFFFFFL) + " target: " + (targetUid & 0xFFFFFFFFL) + "==" + targetUid + " " + status);
                mSmallVideoViewAdapter.notifyUiChanged(mUidsList, bigBgUser.mUid, status, null);
            }
        }
    }

    private void resetToVideoEnabledUI() {
        ImageView iv = (ImageView) findViewById(R.id.customized_function_id);
        iv.setImageResource(R.drawable.btn_switch_camera);
        iv.clearColorFilter();

        notifyHeadsetPlugged(mAudioRouting);
    }

    private void resetToVideoDisabledUI() {
        ImageView iv = (ImageView) findViewById(R.id.customized_function_id);
        iv.setImageResource(R.drawable.btn_speaker);
        iv.clearColorFilter();

        notifyHeadsetPlugged(mAudioRouting);
    }

    public void onVoiceMuteClicked(View view) {
        log.info("onVoiceMuteClicked " + view + " " + mUidsList.size() + " video_status: " + mVideoMuted + " audio_status: " + mAudioMuted);
        if (mUidsList.size() == 0) {
            return;
        }

        RtcEngine rtcEngine = rtcEngine();
        rtcEngine.muteLocalAudioStream(mAudioMuted = !mAudioMuted);

        ImageView iv = (ImageView) view;

        if (mAudioMuted) {
            iv.setColorFilter(getResources().getColor(R.color.agora_blue), PorterDuff.Mode.MULTIPLY);
        } else {
            iv.clearColorFilter();
        }
    }

    @Override
    public void onFirstRemoteVideoDecoded(int uid, int width, int height, int elapsed) {
        doRenderRemoteUi(uid);
//        if(!heisnext&&!woisnext) {
            new Handler(ChatActivity.this.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    next(null);
                }
            }, 2000);
//        }
    }

    private void doRenderRemoteUi(final int uid) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (isFinishing()) {
                    return;
                }

                if (mUidsList.containsKey(uid)) {
                    return;
                }

                SurfaceView surfaceV = RtcEngine.CreateRendererView(getApplicationContext());
                mUidsList.put(uid, surfaceV);

                boolean useDefaultLayout = mLayoutType == LAYOUT_TYPE_DEFAULT && mUidsList.size() != 2;

                surfaceV.setZOrderOnTop(!useDefaultLayout);
                surfaceV.setZOrderMediaOverlay(!useDefaultLayout);

                rtcEngine().setupRemoteVideo(new VideoCanvas(surfaceV, VideoCanvas.RENDER_MODE_FIT, uid));

                if (useDefaultLayout) {
                    log.debug("doRenderRemoteUi LAYOUT_TYPE_DEFAULT " + (uid & 0xFFFFFFFFL));
                    switchToDefaultVideoView();
                } else {
                    int bigBgUid = mSmallVideoViewAdapter == null ? uid : mSmallVideoViewAdapter.getExceptedUid();
                    log.debug("doRenderRemoteUi LAYOUT_TYPE_SMALL " + (uid & 0xFFFFFFFFL) + " " + (bigBgUid & 0xFFFFFFFFL));
                    switchToSmallVideoView(bigBgUid);
                }
            }
        });
    }

    @Override
    public void onJoinChannelSuccess(String channel, final int uid, int elapsed) {
        log.debug("onJoinChannelSuccess " + channel + " " + (uid & 0xFFFFFFFFL) + " " + elapsed);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (isFinishing()) {
                    return;
                }

                SurfaceView local = mUidsList.remove(0);

                if (local == null) {
                    return;
                }

                mUidsList.put(uid, local);
            }
        });
    }

    @Override
    public void onUserOffline(int uid, int reason) {
//      doRemoveRemoteUi(uid);
        this.finish();
    }

    @Override
    public void onExtraCallback(final int type, final Object... data) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (isFinishing()) {
                    return;
                }

                doHandleExtraCallback(type, data);
            }
        });
    }

    private void doHandleExtraCallback(int type, Object... data) {
        int peerUid;
        boolean muted;

        switch (type) {
            case AGEventHandler.EVENT_TYPE_ON_USER_AUDIO_MUTED:
                peerUid = (Integer) data[0];
                muted = (boolean) data[1];

                if (mLayoutType == LAYOUT_TYPE_DEFAULT) {
                    HashMap<Integer, Integer> status = new HashMap<>();
                    status.put(peerUid, muted ? UserStatusData.AUDIO_MUTED : UserStatusData.DEFAULT_STATUS);
                    mGridVideoViewContainer.notifyUiChanged(mUidsList, config().mUid, status, null);
                }

                break;

            case AGEventHandler.EVENT_TYPE_ON_USER_VIDEO_MUTED:
                peerUid = (Integer) data[0];
                muted = (boolean) data[1];

                doHideTargetView(peerUid, muted);

                break;

            case AGEventHandler.EVENT_TYPE_ON_USER_VIDEO_STATS:
                IRtcEngineEventHandler.RemoteVideoStats stats = (IRtcEngineEventHandler.RemoteVideoStats) data[0];

                if (Constant.SHOW_VIDEO_INFO) {
                    if (mLayoutType == LAYOUT_TYPE_DEFAULT) {
                        mGridVideoViewContainer.addVideoInfo(stats.uid, new VideoInfoData(stats.width, stats.height, stats.delay, stats.receivedFrameRate, stats.receivedBitrate));
                        int uid = config().mUid;
                        int profileIndex = getVideoProfileIndex();
                        String resolution = getResources().getStringArray(R.array.string_array_resolutions)[profileIndex];
                        String fps = getResources().getStringArray(R.array.string_array_frame_rate)[profileIndex];
                        String bitrate = getResources().getStringArray(R.array.string_array_bit_rate)[profileIndex];

                        String[] rwh = resolution.split("x");
                        int width = Integer.valueOf(rwh[0]);
                        int height = Integer.valueOf(rwh[1]);

                        mGridVideoViewContainer.addVideoInfo(uid, new VideoInfoData(width > height ? width : height,
                                width > height ? height : width,
                                0, Integer.valueOf(fps), Integer.valueOf(bitrate)));
                    }
                } else {
                    mGridVideoViewContainer.cleanVideoInfo();
                }

                break;

            case AGEventHandler.EVENT_TYPE_ON_SPEAKER_STATS:
                IRtcEngineEventHandler.AudioVolumeInfo[] infos = (IRtcEngineEventHandler.AudioVolumeInfo[]) data[0];

                if (infos.length == 1 && infos[0].uid == 0) { // local guy, ignore it
                    break;
                }

                if (mLayoutType == LAYOUT_TYPE_DEFAULT) {
                    HashMap<Integer, Integer> volume = new HashMap<>();

                    for (IRtcEngineEventHandler.AudioVolumeInfo each : infos) {
                        peerUid = each.uid;
                        int peerVolume = each.volume;

                        if (peerUid == 0) {
                            continue;
                        }
                        volume.put(peerUid, peerVolume);
                    }
                    mGridVideoViewContainer.notifyUiChanged(mUidsList, config().mUid, null, volume);
                }

                break;

            case AGEventHandler.EVENT_TYPE_ON_APP_ERROR:
                int subType = (int) data[0];

                if (subType == ConstantApp.AppError.NO_NETWORK_CONNECTION) {
                    showLongToast(getString(R.string.msg_no_network_connection));
                }

                break;

            case AGEventHandler.EVENT_TYPE_ON_DATA_CHANNEL_MSG:
                synchronized (this) {
//                  peerUid = (Integer) data[0];
                    final byte[] content = (byte[]) data[1];
//                  notifyMessageChanged(new Message(new User(peerUid, String.valueOf(peerUid)), new String(content)));
                    if (new String(content).contains("ready"+index+":")) {
                        temp = new String(content).split(":")[1];
                        heisnext = true;
                        if (woisnext) {
                            woisnext = false;
                            heisnext = false;
                            nextQuestion();
                        }
                    }
                }
                break;

            case AGEventHandler.EVENT_TYPE_ON_AGORA_MEDIA_ERROR: {
                int error = (int) data[0];
                String description = (String) data[1];

                notifyMessageChanged(new Message(new User(0, null), error + " " + description));

                break;
            }

            case AGEventHandler.EVENT_TYPE_ON_AUDIO_ROUTE_CHANGED:
                 notifyHeadsetPlugged((int) data[0]);

                 break;

        }
    }

    private void sendImready() {
        if(index<timu.length) {
            sendChannelMsg("ready" + index + ":" + timu[index]);
        }
    }

    private void nextQuestion() {
        if(index<timu.length) {
            next.setVisibility(View.VISIBLE);
            next.setText("下一题");
            next.setBackgroundColor(R.color.dark_gray);
            Message msg = new Message(Message.MSG_TYPE_TEXT,
                    new User(config().mUid, String.valueOf(config().mUid)), temp);
            index++;
            notifyMessageChanged(msg);
        }else{
            mMsgList.clear();
            mMsgAdapter.notifyDataSetChanged();
            findViewById(R.id.msg_input_container).setVisibility(View.GONE);
            findViewById(R.id.bottom_action_container).setVisibility(View.VISIBLE);
            next.setVisibility(View.GONE);
            Toast.makeText(this,"聊天在10s后结束，感谢你的参与",Toast.LENGTH_LONG).show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    ChatActivity.this.finish();
                }
            },10000);
        }
    }

    private void requestRemoteStreamType(final int currentHostCount) {
        log.debug("requestRemoteStreamType " + currentHostCount);
    }

    private void doRemoveRemoteUi(final int uid) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (isFinishing()) {
                    return;
                }

                Object target = mUidsList.remove(uid);
                if (target == null) {
                    return;
                }

                int bigBgUid = -1;
                if (mSmallVideoViewAdapter != null) {
                    bigBgUid = mSmallVideoViewAdapter.getExceptedUid();
                }

                log.debug("doRemoveRemoteUi " + (uid & 0xFFFFFFFFL) + " " + (bigBgUid & 0xFFFFFFFFL) + " " + mLayoutType);

                if (mLayoutType == LAYOUT_TYPE_DEFAULT || uid == bigBgUid) {
                    switchToDefaultVideoView();
                } else {
                    switchToSmallVideoView(bigBgUid);
                }
            }
        });
    }

    private SmallVideoViewAdapter mSmallVideoViewAdapter;

    private void switchToDefaultVideoView() {
        if (mSmallVideoViewDock != null) {
            mSmallVideoViewDock.setVisibility(View.GONE);
        }
        mGridVideoViewContainer.initViewContainer(this, config().mUid, mUidsList);

        mLayoutType = LAYOUT_TYPE_DEFAULT;
    }

    private void switchToSmallVideoView(int bigBgUid) {
        HashMap<Integer, SurfaceView> slice = new HashMap<>(1);
        slice.put(bigBgUid, mUidsList.get(bigBgUid));
        mGridVideoViewContainer.initViewContainer(this, bigBgUid, slice);

        bindToSmallVideoView(bigBgUid);

        mLayoutType = LAYOUT_TYPE_SMALL;

        requestRemoteStreamType(mUidsList.size());
    }

    public int mLayoutType = LAYOUT_TYPE_DEFAULT;

    public static final int LAYOUT_TYPE_DEFAULT = 0;

    public static final int LAYOUT_TYPE_SMALL = 1;

    int id = 0;
    private void bindToSmallVideoView(final int exceptUid) {
        if (mSmallVideoViewDock == null) {
            ViewStub stub = (ViewStub) findViewById(R.id.small_video_view_dock);
            mSmallVideoViewDock = (RelativeLayout) stub.inflate();
        }

        boolean twoWayVideoCall = mUidsList.size() == 2;

        RecyclerView recycler = (RecyclerView) findViewById(R.id.small_video_view_container);

        boolean create = false;

        if (mSmallVideoViewAdapter == null) {
            create = true;
            mSmallVideoViewAdapter = new SmallVideoViewAdapter(this, config().mUid, exceptUid, mUidsList, new VideoViewEventListener() {
                @Override
                public void onItemDoubleClick(View v, Object item) {
//                   switchToDefaultVideoView();
                    if(id==0){
                        switchToSmallVideoView(config().mUid);
                        id = config().mUid;
                    }else{
                        if(id == config().mUid){
                            switchToSmallVideoView(exceptUid);
                            id = exceptUid;
                        }else{
                            switchToSmallVideoView(config().mUid);
                            id = config().mUid;
                        }
                    }
                }
            });
            mSmallVideoViewAdapter.setHasStableIds(true);
        }
        recycler.setHasFixedSize(true);

        log.debug("bindToSmallVideoView " + twoWayVideoCall + " " + (exceptUid & 0xFFFFFFFFL));

        if (twoWayVideoCall) {
            recycler.setLayoutManager(new RtlLinearLayoutManager(getApplicationContext(), RtlLinearLayoutManager.HORIZONTAL, false));
        } else {
            recycler.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
        }
        recycler.addItemDecoration(new SmallVideoViewDecoration());
        recycler.setAdapter(mSmallVideoViewAdapter);

        recycler.setDrawingCacheEnabled(true);
        recycler.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_AUTO);

        if (!create) {
            mSmallVideoViewAdapter.setLocalUid(config().mUid);
            mSmallVideoViewAdapter.notifyUiChanged(mUidsList, exceptUid, null, null);
        }
        recycler.setVisibility(View.VISIBLE);
        mSmallVideoViewDock.setVisibility(View.VISIBLE);
    }

    public void next(View view){
        woisnext = true;
        sendImready();
        if(heisnext&&woisnext){
            heisnext = false;
            woisnext = false;
            nextQuestion();
        }else{
            next.setText("等待对方");
            next.setBackgroundColor(R.color.agora_blue);
        }
    }

    public void notifyHeadsetPlugged(final int routing) {
        log.info("notifyHeadsetPlugged " + routing + " " + mVideoMuted);

        mAudioRouting = routing;

        if (!mVideoMuted) {
            return;
        }

        ImageView iv = (ImageView) findViewById(R.id.customized_function_id);
        if (mAudioRouting == 3) { // Speakerphone
            iv.setColorFilter(getResources().getColor(R.color.agora_blue), PorterDuff.Mode.MULTIPLY);
        } else {
            iv.clearColorFilter();
        }
    }

    public void left(View view) {
        ((MyCustomizedCameraRenderer)getLocalView()).gotoPrevious();
    }

    public void right(View view) {
        ((MyCustomizedCameraRenderer)getLocalView()).gotoNext();
    }
}
