package com.wen.hugo.randomMatch;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.avos.avoscloud.AVCloud;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FunctionCallback;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMCmdMessageBody;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.EaseConstant;
import com.wen.hugo.R;
import com.wen.hugo.chatPage.ChatActivity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hugo on 11/29/17.
 */

public class MatchActivity extends Activity {
    ProgressDialog mProgressDialog;
    EMMessageListener msgListener;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.match_activity);
        setLoading();
        match();
    }


    public void setLoading(){
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setMessage("正在匹配......");
        mProgressDialog.setCancelable(false);
        mProgressDialog.setButton(DialogInterface.BUTTON_NEUTRAL, "取消匹配",
            new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
//                    mProgressDialog.dismiss();
                      cancelMatch();
                      MatchActivity.this.finish();

                }});
        mProgressDialog.show();
    }

    public void match(){
        Map<String, String> dicParameters = new HashMap<String, String>();
        dicParameters.put("name", AVUser.getCurrentUser().getUsername());
        AVCloud.callFunctionInBackground("match", dicParameters, new FunctionCallback() {
            public void done(Object object, AVException e) {
                if (e == null) {
                    addListener();
                    if(object.toString().equalsIgnoreCase("notmatch")){

                    }else{
                        MatchToSomeOne(object.toString());
                    }
                } else {
                    e.printStackTrace();
                }
            }
        });
    }

    public void cancelMatch(){
        Map<String, String> dicParameters = new HashMap<String, String>();
        dicParameters.put("name", AVUser.getCurrentUser().getUsername());
        AVCloud.callFunctionInBackground("cancelMatch", dicParameters, new FunctionCallback() {
            public void done(Object object, AVException e) {
                if (e != null) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void MatchToSomeOne(String username) {
        EMMessage cmdMsg = EMMessage.createSendMessage(EMMessage.Type.CMD);
        String action="action1";
        EMCmdMessageBody cmdBody=new EMCmdMessageBody(action);
        // 设置消息body
        cmdMsg.addBody(cmdBody);
        // 设置要发给谁，用户username或者群聊groupid
        cmdMsg.setTo(username);
        EMClient.getInstance().chatManager().sendMessage(cmdMsg);
    }

    public void SendToSomeOne(String username){
        EMMessage cmdMsg = EMMessage.createSendMessage(EMMessage.Type.CMD);
        String action="action2";
        EMCmdMessageBody cmdBody=new EMCmdMessageBody(action);
        // 设置消息body
        cmdMsg.addBody(cmdBody);
        // 设置要发给谁，用户username或者群聊groupid
        cmdMsg.setTo(username);
        EMClient.getInstance().chatManager().sendMessage(cmdMsg);
    }

    public void matchOk(String username){
        mProgressDialog.dismiss();
        MatchActivity.this.finish();
        Intent intent = new Intent(MatchActivity.this, ChatActivity.class);
        intent.putExtra(EaseConstant.EXTRA_USER_ID, username);
        startActivity(intent);
    }

    public void addListener(){
        msgListener = new EMMessageListener() {

            @Override
            public void onMessageReceived(List<EMMessage> messages) {
                //收到消息
            }

            @Override
            public void onCmdMessageReceived(List<EMMessage> messages) {
                //收到透传消息
                for (EMMessage message : messages) {
                    EMCmdMessageBody cmdMsgBody = (EMCmdMessageBody) message.getBody();
                    final String action = cmdMsgBody.action();//获取自定义action
                    //获取扩展属性 此处省略
                    //maybe you need get extension of your message
                    //message.getStringAttribute("");
                    if(action.equalsIgnoreCase("action1")){
                        SendToSomeOne(message.getFrom());
                        matchOk(message.getFrom());
                    }else if(action.equalsIgnoreCase("action2")){
                        matchOk(message.getFrom());
                    }
                }
            }

            @Override
            public void onMessageRead(List<EMMessage> messages) {
                //收到已读回执
            }

            @Override
            public void onMessageDelivered(List<EMMessage> message) {
                //收到已送达回执
            }
            @Override
            public void onMessageRecalled(List<EMMessage> messages) {
                //消息被撤回
            }

            @Override
            public void onMessageChanged(EMMessage message, Object change) {
                //消息状态变动
            }
        };
        EMClient.getInstance().chatManager().addMessageListener(msgListener);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        EMClient.getInstance().chatManager().removeMessageListener(msgListener);
    }
}

