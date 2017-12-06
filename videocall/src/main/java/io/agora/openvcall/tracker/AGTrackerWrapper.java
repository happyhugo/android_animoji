package io.agora.openvcall.tracker;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.agora.tracker.AGFilterType;
import com.agora.tracker.AGTrackerManager;
import com.agora.tracker.AGTrackerSettings;
import com.agora.tracker.bean.AGFilter;
import com.agora.tracker.bean.AGRenderResult;
import com.agora.tracker.bean.AGYuvFrame;
import com.agora.tracker.bean.conf.StickerConfig;
import com.agora.tracker.common.Config;
import com.agora.ui.OnViewEventListener;
import com.agora.ui.helper.ResourceHelper;
import com.agora.ui.model.SharePreferenceMgr;

import static com.agora.ui.AGControlView.BEAUTY_BIG_EYE_TYPE;
import static com.agora.ui.AGControlView.BEAUTY_THIN_FACE_TYPE;
import static com.agora.ui.AGControlView.REMOVE_BLEMISHES;
import static com.agora.ui.AGControlView.SKIN_SHINNING_TENDERNESS;
import static com.agora.ui.AGControlView.SKIN_TONE_PERFECTION;
import static com.agora.ui.AGControlView.SKIN_TONE_SATURATION;

/**
 * Created by shijian on 2016/9/28.
 */

public class AGTrackerWrapper {
    public interface UIClickListener{
        void onTakeShutter() ;
        void onSwitchCamera();
    }

    private AGTrackerSettings mTrackerSetting;
    private AGTrackerManager mTrackerManager;

    public AGTrackerWrapper(Context context, int cameraFaceId) {

        SharePreferenceMgr instance = SharePreferenceMgr.getInstance();

        AGTrackerSettings.BeautySettings2 beautySettings2 = new AGTrackerSettings.BeautySettings2();
        beautySettings2.setWhiteProgress(instance.getSkinWhite());
        beautySettings2.setDermabrasionProgress(instance.getSkinRemoveBlemishes());
        beautySettings2.setSaturatedProgress(instance.getSkinSaturation());
        beautySettings2.setPinkProgress(instance.getSkinTenderness());

        AGTrackerSettings.BeautySettings beautySettings = new AGTrackerSettings.BeautySettings();
        beautySettings.setBigEyeScaleProgress(instance.getBigEye());
        beautySettings.setThinFaceScaleProgress(instance.getThinFace());

        mTrackerSetting = new AGTrackerSettings().
                setBeauty2Enabled(instance.isBeautyEnabled()).
                setBeautySettings2(beautySettings2).
                setBeautyFaceEnabled(instance.isLocalBeautyEnabled()).
                setBeautySettings(beautySettings).
                setCameraFaceId(cameraFaceId);

        mTrackerManager = new AGTrackerManager(context).
                setTrackerSetting(mTrackerSetting).
                build();


        //copy assets config/sticker/filter to sdcard
        ResourceHelper.copyResource2SD(context);
        //关闭日志打印
        Config.isDebug = false;
    }

    public void onCreate(Activity activity) {
        Log.i("tracker","onCreate");

        mTrackerManager.onCreate(activity);
        AGRender.init(this);
    }

    public void onResume(Activity activity) {
        Log.i("tracker","onResume");
        mTrackerManager.onResume(activity);
    }

    public void onPause(Activity activity) {
        Log.i("tracker","onPause");
        mTrackerManager.onPause(activity);
    }

    public void onDestroy(Activity activity) {
        Log.i("tracker","onDestroy");
        mTrackerManager.onDestory(activity);
        AGRender.destory();
    }

    /**
     * UI事件处理类
     * @param uiClickListener
     * @return
     */
    public OnViewEventListener initUIEventListener(final UIClickListener uiClickListener) {
        OnViewEventListener eventListener = new OnViewEventListener() {
            @Override
            public void onSwitchBeauty2(boolean enable) {
                getmTrackerManager().setBeauty2Enabled(enable);
            }

            @Override
            public void onTakeShutter() {
                if (uiClickListener != null)
                    uiClickListener.onTakeShutter();
            }

            @Override
            public void onSwitchCamera() {
                if (uiClickListener != null)
                    uiClickListener.onSwitchCamera();
            }

            @Override
            public void onSwitchFilter(AGFilter filter) {
                getmTrackerManager().switchFilter(filter);
            }

            @Override
            public void onStickerChanged(StickerConfig item) {
                getmTrackerManager().switchSticker(item);
            }

            @Override
            public void onSwitchBeauty(boolean enable) {
                getmTrackerManager().setBeautyEnabled(enable);
            }

            @Override
            public void onSwitchBeautyFace(boolean enable) {
                getmTrackerManager().setBeautyFaceEnabled(enable);
            }

            @Override
            public void onDistortionChanged(AGFilterType filterType) {
                getmTrackerManager().switchDistortion(filterType);

            }

            /**
             * set face beauty param
             * @param type type
             * @param param [0~100]
             */
            @Override
            public void onAdjustFaceBeauty(int type, float param) {
                switch (type) {
                    case BEAUTY_BIG_EYE_TYPE:
                        getmTrackerManager().setEyeMagnifying((int) param);
                        break;
                    case BEAUTY_THIN_FACE_TYPE:
                        getmTrackerManager().setChinSliming((int) param);
                        break;
                    case SKIN_SHINNING_TENDERNESS:
                        //粉嫩
                        getmTrackerManager().setSkinTenderness((int) param);
                        break;
                    case SKIN_TONE_SATURATION:
                        //饱和
                        getmTrackerManager().setSkinSaturation((int) param);
                        break;
                    case REMOVE_BLEMISHES:
                        //磨皮
                        getmTrackerManager().setSkinBlemishRemoval((int) param);
                        break;
                    case SKIN_TONE_PERFECTION:
                        //美白
                        getmTrackerManager().setSkinWhitening((int) param);
                        break;
                }

            }

            @Override
            public void onFaceBeautyLevel(float level) {
                getmTrackerManager().adjustBeauty(level);
            }


        };

        return eventListener;
    }

    public AGTrackerManager getmTrackerManager() {
        return mTrackerManager;
    }

    public boolean isNeedTrack() {
        return mTrackerSetting.isNeedTrack();
    }


    public AGRenderResult renderYuvFrame(AGYuvFrame yuvFrame) {
        return mTrackerManager.renderYuvFrame(yuvFrame);
    }

}
