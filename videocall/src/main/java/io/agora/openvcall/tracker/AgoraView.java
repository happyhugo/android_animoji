package io.agora.openvcall.tracker;

import android.content.Context;
import android.util.AttributeSet;

import com.agora.ui.AGControlView;

import io.agora.openvcall.R;


/**
 * Created by shijian on 2017/1/23.
 */

public class AgoraView extends AGControlView {
    public AgoraView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.agora_control_layout;
    }
}