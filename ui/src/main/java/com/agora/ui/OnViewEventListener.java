package com.agora.ui;

import com.agora.tracker.AGFilterType;
import com.agora.tracker.bean.AGFilter;
import com.agora.tracker.bean.conf.StickerConfig;

/**
 * Created by shijian on 08/12/2016.
 */

public interface OnViewEventListener {
    void onTakeShutter();

    void onSwitchCamera();

    void onStickerChanged(StickerConfig stickerConfig);

    void onSwitchBeauty(boolean enable);

    void onSwitchBeautyFace(boolean enable);

    void onDistortionChanged(AGFilterType filterType);

    void onAdjustFaceBeauty(int type, float param);

    void onFaceBeautyLevel(float level);

    void onSwitchBeauty2(boolean enable);

    void onSwitchFilter(AGFilter filter);
}