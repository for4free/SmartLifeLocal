package com.tobey.bean_huanxin;

import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.easemob.EMCallBack;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMGroupManager;
import com.google.gson.Gson;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.tobey.easyLife.R;

public class MainActivity extends Activity implements View.OnClickListener{
    private static final OkHttpClient mOkHttpClient=new OkHttpClient();
    private static final Gson gson=new Gson();
    private EditText username,password,to;
    private Button register,login,logout,video,voice;
    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case REGISTER:
                    RegisterModel bean= (RegisterModel) msg.obj;
                    if (bean.getStatus()==200){
                        Toast.makeText(getApplicationContext(),"ע��ɹ���",Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(getApplicationContext(),"ע��ʧ�ܣ�"+bean.getMessage(),Toast.LENGTH_LONG).show();
                    }
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };
    private static final int REGISTER=0x01;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_huanxin);

        initView();
    }

    private void initView() {
        username= (EditText) findViewById(R.id.username);
        password= (EditText) findViewById(R.id.password);
        to= (EditText) findViewById(R.id.to);
        register= (Button) findViewById(R.id.register);
        login= (Button) findViewById(R.id.login);
        logout= (Button) findViewById(R.id.logout);
        video= (Button) findViewById(R.id.video);
        voice= (Button) findViewById(R.id.voice);
        register.setOnClickListener(this);
        login.setOnClickListener(this);
        logout.setOnClickListener(this);
        video.setOnClickListener(this);
        voice.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.register:
                register();
                break;
            case R.id.login:
                login();
                break;
            case R.id.logout:
                logout();
                break;
            case R.id.voice:
                voice();
                break;
            case R.id.video:
                video();
                break;
            default:
                break;
        }
    }


    private void register() {
        String u=username.getText().toString();
        String p=password.getText().toString();
        if (TextUtils.isEmpty(u)||TextUtils.isEmpty(p)){
            Toast.makeText(getApplicationContext(),"�˺Ż����벻��Ϊ�գ�",Toast.LENGTH_LONG).show();
            return ;
        }

        RequestBody requestBody= new FormEncodingBuilder()
                .add("username",u)
                .add("password",p)
                .build();
        String url="http://123.206.78.18/api/videoApi/index.php";
        Request request=new Request.Builder().url(url).post(requestBody).build();
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Log.e("TAG","Error,register failure.");
            }
            @Override
            public void onResponse(Response response) throws IOException {
                String result=response.body().string();
                RegisterModel bean=gson.fromJson(result,RegisterModel.class);
                Message message=Message.obtain();
                message.obj=bean;
                message.what=REGISTER;
                mHandler.sendMessage(message);
            }
        });
    }

    private void login() {
        final String u=username.getText().toString();
        final String p=password.getText().toString();
        if (TextUtils.isEmpty(u)||TextUtils.isEmpty(p)){
            Toast.makeText(getApplicationContext(),"�˺Ż����벻��Ϊ�գ�",Toast.LENGTH_LONG).show();
            return ;
        }
        //�����Ƚ����Լ��������ĵ�¼����
        //�Լ���������¼�ɹ�����ִ�л��ŷ������ĵ�¼����
        EMChatManager.getInstance().login(u, p, new EMCallBack() {//�ص�
            @Override
            public void onSuccess() {
                runOnUiThread(new Runnable() {
                    public void run() {
                        EMGroupManager.getInstance().loadAllGroups();
                        EMChatManager.getInstance().loadAllConversations();
                        Toast.makeText(MainActivity.this, "��½����������ɹ�", Toast.LENGTH_SHORT).show();
                        Log.e("TAG", "��½����������ɹ���");
                    }
                });
            }

            @Override
            public void onProgress(int progress, String status) {
                Log.e("TAG", "��½����������� " + "progress:" + progress + " status:" + status);
            }

            @Override
            public void onError(int code, String message) {
                Log.e("TAG", "��½���������ʧ�ܣ�"+code+"=="+message+"�û���"+u+"���룺"+p);
                //Toast.makeText(MainActivity.this, "��½���������ʧ��"+code+"=="+message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void logout() {
        //�����Ƚ����Լ����������˳�����
        //�Լ���������¼�ɹ�����ִ�л��ŷ��������˳�����

        //�˷���Ϊ�첽����
        EMChatManager.getInstance().logout(new EMCallBack() {
            @Override
            public void onSuccess() {
                Log.e("TAG", "�˳�����������ɹ���");
                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(MainActivity.this, "�˳�����������ɹ�", Toast.LENGTH_SHORT).show();
                        Log.e("TAG", "�˳�����������ɹ���");
                    }
                });
            }

            @Override
            public void onProgress(int progress, String status) {
                Log.e("TAG", "�˳������������ " + " progress:" + progress + " status:" + status);

            }

            @Override
            public void onError(int code, String message) {
                Log.e("TAG", "�˳����������ʧ�ܣ�" + " code:" + code + " message:" + message);

            }
        });
    }


    private void voice() {
        if (!EMChatManager.getInstance().isConnected())
            Toast.makeText(this, "δ���ӵ�������", Toast.LENGTH_SHORT).show();
        else{
            String toUser=to.getText().toString();
            if (TextUtils.isEmpty(toUser)){
                Toast.makeText(MainActivity.this, "����д���ܷ��˺�", Toast.LENGTH_SHORT).show();
                return ;
            }
            Intent intent = new Intent(MainActivity.this, VoiceCallActivity.class);
            intent.putExtra("username", toUser);
            intent.putExtra("isComingCall", false);
            startActivity(intent);
        }

    }

    private void video() {
        if (!EMChatManager.getInstance().isConnected()) {
            Toast.makeText(MainActivity.this, "δ���ӵ�������", Toast.LENGTH_SHORT).show();
        }
        else {
            String toUser=to.getText().toString();
            if (TextUtils.isEmpty(toUser)){
                Toast.makeText(MainActivity.this, "����д���ܷ��˺�", Toast.LENGTH_SHORT).show();
                return ;
            }
            Intent intent = new Intent(MainActivity.this, VideoCallActivity.class);
            intent.putExtra("username", toUser);
            intent.putExtra("isComingCall", false);
            startActivity(intent);
        }
    }

}
