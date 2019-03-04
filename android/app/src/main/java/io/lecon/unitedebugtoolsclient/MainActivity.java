package io.lecon.unitedebugtoolsclient;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import io.lecon.unitedebugtoolsclient.database.CarDBHelper;
import io.lecon.unitedebugtoolsclient.database.ContactDBHelper;
import io.lecon.unitedebugtoolsclient.database.ExtTestDBHelper;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions,
                                           int[] grantResults) {
        if (requestCode == 124) {
            if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                Toast.makeText(this, "授权成功", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "授权失败", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void addToDatabase(View view) {
        ContactDBHelper contactDBHelper = new ContactDBHelper(getApplicationContext());
        if (contactDBHelper.count() == 0) {
            for (int i = 0; i < 100; i++) {
                String name = "name_" + i;
                Integer phone = i;
                String email = "email_" + i;
                String street = "street_" + i;
                String place = "place_" + i;
                contactDBHelper.insertContact(name, phone, "[{\"name\":\"王小二\",\"age\":25.2,\"birthday\":\"1990-01-01\",\"school\":\"蓝翔\",\"major(技能)\":[\"理发\",\"挖掘机\"],\"has_girlfriend\":false,\"car\":null,\"house\":null,\"comment\":\"这是一个注释\"},{\"name\":\"王小二\",\"age\":25.2,\"birthday\":\"1990-01-01\",\"school\":\"蓝翔\",\"major(技能)\":[\"理发\",\"挖掘机\"],\"has_girlfriend\":false,\"car\":null,\"house\":null,\"comment\":\"这是一个注释\"},{\"name\":\"王小二\",\"age\":25.2,\"birthday\":\"1990-01-01\",\"school\":\"蓝翔\",\"major(技能)\":[\"理发\",\"挖掘机\"],\"has_girlfriend\":false,\"car\":null,\"house\":null,\"comment\":\"这是一个注释\"}]", street, null);
            }
        }

        CarDBHelper carDBHelper = new CarDBHelper(getApplicationContext());
        if (carDBHelper.count() == 0) {
            for (int i = 0; i < 50; i++) {
                String name = "name_" + i;
                String color = "RED";
                float mileage = i + 10.45f;
                carDBHelper.insertCar(name, color, mileage);
            }
        }

        ExtTestDBHelper extTestDBHelper = new ExtTestDBHelper(getApplicationContext());
        if (extTestDBHelper.count() == 0) {
            for (int i = 0; i < 20; i++) {
                String value = "value_" + i;
                extTestDBHelper.insertTest(value);
            }
        }
    }


    public void reqPermission(View view) {
        int permissionCheck1 = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        int permissionCheck2 = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionCheck1 != PackageManager.PERMISSION_GRANTED || permissionCheck2 != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE},
                    124);

        }
    }

    public void initThis(View view) {
        reqPermission(null);
        new Thread(new Runnable() {
            @Override
            public void run() {
                addToDatabase(null);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        TextView view1 = findViewById(R.id.tips);
                        view1.setText("初始化完成！\n" +
                                "1. 连接数据线到计算机 \n" +
                                "2. 打开adb命令行 adb forward tcp:8089 tcp:8089 \n" +
                                "3. 计算机浏览器打开 http://127.0.0.1:8089");
                    }
                });
            }
        }).start();
    }
}
