package com.tobey.bean_huanxin;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.SoundPool;
import android.os.Bundle;

import com.easemob.chat.EMCallStateChangeListener;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMMessage;
import com.easemob.chat.TextMessageBody;
import com.tobey.easyLife.R;



public class CallActivity extends Activity {
    public static final String MESSAGE_ATTR_IS_VOICE_CALL = "is_voice_call";
    public static final String MESSAGE_ATTR_IS_VIDEO_CALL = "is_video_call";
    protected boolean isInComingCall;
    protected String username;
    protected CallingState callingState = CallingState.CANCED;
    protected String callDruationText;
    protected String msgid;
    protected AudioManager audioManager;
    protected SoundPool soundPool;
    protected Ringtone ringtone;
    protected int outgoing;
    protected EMCallStateChangeListener callStateListener;
    
    
    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        audioManager = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (soundPool != null)
            soundPool.release();
        if (ringtone != null && ringtone.isPlaying())
            ringtone.stop();
        audioManager.setMode(AudioManager.MODE_NORMAL);
        audioManager.setMicrophoneMute(false);
        
        if(callStateListener != null)
            EMChatManager.getInstance().removeCallStateChangeListener(callStateListener);
            
    }
    
    /**
     * ���Ų�������
     * 
     */
    protected int playMakeCallSounds() {
        try {
            // �������
            float audioMaxVolumn = audioManager.getStreamMaxVolume(AudioManager.STREAM_RING);
            // ��ǰ����
            float audioCurrentVolumn = audioManager.getStreamVolume(AudioManager.STREAM_RING);
            float volumnRatio = audioCurrentVolumn / audioMaxVolumn;

            audioManager.setMode(AudioManager.MODE_RINGTONE);
            audioManager.setSpeakerphoneOn(false);

            // ����
            int id = soundPool.play(outgoing, // ������Դ
                    0.3f, // ������
                    0.3f, // ������
                    1, // ���ȼ���0���
                    -1, // ѭ��������0�ǲ�ѭ����-1����Զѭ��
                    1); // �ط��ٶȣ�0.5-2.0֮�䡣1Ϊ�����ٶ�
            return id;
        } catch (Exception e) {
            return -1;
        }
    }
    
    // ��������
    protected void openSpeakerOn() {
        try {
            if (!audioManager.isSpeakerphoneOn())
                audioManager.setSpeakerphoneOn(true);
            audioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // �ر�������
    protected void closeSpeakerOn() {

        try {
            if (audioManager != null) {
                // int curVolume =
                // audioManager.getStreamVolume(AudioManager.STREAM_VOICE_CALL);
                if (audioManager.isSpeakerphoneOn())
                    audioManager.setSpeakerphoneOn(false);
                audioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
                // audioManager.setStreamVolume(AudioManager.STREAM_VOICE_CALL,
                // curVolume, AudioManager.STREAM_VOICE_CALL);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * ����ͨ����Ϣ��¼
     * @param type 0����Ƶ��1����Ƶ
     */
    protected void saveCallRecord(int type) {
        EMMessage message = null;
        TextMessageBody txtBody = null;
        if (!isInComingCall) { // ���ȥ��ͨ��
            message = EMMessage.createSendMessage(EMMessage.Type.TXT);
            message.setReceipt(username);
        } else {
            message = EMMessage.createReceiveMessage(EMMessage.Type.TXT);
            message.setFrom(username);
        }

        String st1 = getResources().getString(R.string.call_duration);
        String st2 = getResources().getString(R.string.refused);
        String st3 = getResources().getString(R.string.the_other_party_has_refused_to);
        String st4 = getResources().getString(R.string.the_other_is_not_online);
        String st5 = getResources().getString(R.string.the_other_is_on_the_phone);
        String st6 = getResources().getString(R.string.the_other_party_did_not_answer);
        String st7 = getResources().getString(R.string.did_not_answer);
        String st8 = getResources().getString(R.string.has_been_cancelled);
        switch (callingState) {
        case NORMAL:
            txtBody = new TextMessageBody(st1 + callDruationText);
            break;
        case REFUESD:
            txtBody = new TextMessageBody(st2);
            break;
        case BEREFUESD:
            txtBody = new TextMessageBody(st3);
            break;
        case OFFLINE:
            txtBody = new TextMessageBody(st4);
            break;
        case BUSY:
            txtBody = new TextMessageBody(st5);
            break;
        case NORESPONSE:
            txtBody = new TextMessageBody(st6);
            break;
        case UNANSWERED:
            txtBody = new TextMessageBody(st7);
            break;
        default:
            txtBody = new TextMessageBody(st8);
            break;
        }
        // ������չ����
        if(type == 0)
            message.setAttribute(MESSAGE_ATTR_IS_VOICE_CALL, true);
        else
            message.setAttribute(MESSAGE_ATTR_IS_VIDEO_CALL, true);

        // ������Ϣbody
        message.addBody(txtBody);
        message.setMsgId(msgid);

        // ����
        EMChatManager.getInstance().saveMessage(message, false);
    }

    enum CallingState {
        CANCED, NORMAL, REFUESD, BEREFUESD, UNANSWERED, OFFLINE, NORESPONSE, BUSY
    }
}
