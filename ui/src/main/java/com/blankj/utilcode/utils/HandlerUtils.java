//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.blankj.utilcode.utils;

import android.os.Handler;
import android.os.Message;
import java.lang.ref.WeakReference;

public class HandlerUtils {
    private HandlerUtils() {
        throw new UnsupportedOperationException("u can\'t instantiate me...");
    }

    public interface OnReceiveMessageListener {
        void handlerMessage(Message var1);
    }

    public static class HandlerHolder extends Handler {
        WeakReference<HandlerUtils.OnReceiveMessageListener> mListenerWeakReference;

        public HandlerHolder(HandlerUtils.OnReceiveMessageListener listener) {
            this.mListenerWeakReference = new WeakReference(listener);
        }

        public void handleMessage(Message msg) {
            if(this.mListenerWeakReference != null && this.mListenerWeakReference.get() != null) {
                ((HandlerUtils.OnReceiveMessageListener)this.mListenerWeakReference.get()).handlerMessage(msg);
            }

        }
    }
}
