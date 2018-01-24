package cn.liucl.debugtools;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * SDK设置页面
 * Created by spawn on 2018/1/24.
 */

public class SettingActivity extends Activity {

    public static boolean CONNECT_STATE = false;

    public static final String UNITE_PAN = "unite_pan";
    public static final String UNITE_PAN_IP = "unite_pan_ip";
    public static final String UNITE_PAN_PORT = "unite_pan_port";

    private SharedPreferences sharedPreferences;

    private Pattern r = Pattern.compile("^(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[1-9])\\.(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\.(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\.(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)$");

    private Button mConnectBtn;
    private EditText mEditIpView;
    private EditText mEditPortView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        sharedPreferences = getSharedPreferences(UNITE_PAN, Context.MODE_PRIVATE);
        assignsView();
        initEvent();
    }

    private void initEvent() {
        mConnectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ipText = mEditIpView.getText().toString();
                String port = mEditPortView.getText().toString();
                if (checkIp(ipText) && checkPort(port)) {
                    saveToSp(ipText, port);
                    processConnect(ipText);
                } else {
                    Toast.makeText(SettingActivity.this, "格式错误", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean checkPort(String portText) {
        try {
            int port = Integer.parseInt(portText);
            if (port > 0 && port < 65536) {
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    private boolean checkIp(String ip) {
        Matcher matcher = r.matcher(ip);
        return matcher.matches();
    }

    private void saveToSp(String ipText, String portText) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(UNITE_PAN_IP, ipText);
        editor.putString(UNITE_PAN_PORT, portText);
        editor.apply();
    }

    /**
     * 开始连接
     *
     * @param ipText
     */
    private void processConnect(String ipText) {

    }

    private void assignsView() {
        mConnectBtn = findViewById(R.id.connect);
        mEditIpView = findViewById(R.id.edit_ip);
        mEditPortView = findViewById(R.id.edit_port);
    }


}
