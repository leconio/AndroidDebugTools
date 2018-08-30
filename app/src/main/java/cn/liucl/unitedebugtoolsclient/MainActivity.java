package cn.liucl.unitedebugtoolsclient;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.liucl.debugtools.DebugTools;
import cn.liucl.debugtools.SettingActivity;
import cn.liucl.debugtools.db.DefaultDatabaseHelper;
import cn.liucl.unitedebugtoolsclient.database.CarDBHelper;
import cn.liucl.unitedebugtoolsclient.database.ContactDBHelper;
import cn.liucl.unitedebugtoolsclient.database.ExtTestDBHelper;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = DebugTools.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startActivity(new Intent(this, SettingActivity.class));

    }

    public void addToDatabase(View view) {
        ContactDBHelper contactDBHelper = new ContactDBHelper(getApplicationContext());
        if (contactDBHelper.count() == 0) {
            for (int i = 0; i < 100; i++) {
                String name = "name_" + i;
                String phone = "phone_" + i;
                String email = "email_" + i;
                String street = "street_" + i;
                String place = "place_" + i;
                contactDBHelper.insertContact(name, phone, email, street, null);
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

    public void queryTableName(View view) {
        DefaultDatabaseHelper ddh = new DefaultDatabaseHelper(this);
        List<String> list = ddh.listAllTables("Car.db");
        Log.i(TAG, "queryTableName: " + list);
    }

    public void queryDatabase(View view) {
        DefaultDatabaseHelper ddh = new DefaultDatabaseHelper(this);
        HashMap<String, File> map = ddh.listAllDatabase();
        Log.i(TAG, "queryDatabase: " + map);
    }

    public void queryTest(View view) {
        DefaultDatabaseHelper ddh = new DefaultDatabaseHelper(this);
        Map<String, String> condition = new HashMap<>();
        condition.put("phone", "phone_1");
        condition.put("name", "name_1");
        String contacts = ddh.queryData("Contact.db", "contacts", condition);
        Log.i(TAG, "queryTest: " + contacts);
    }

    public void updateTest(View view) {
        DefaultDatabaseHelper ddh = new DefaultDatabaseHelper(this);
        Map<String, String> condition = new HashMap<>();
        condition.put("phone", "phone_1");
        condition.put("name", "name_1");

        Map<String, String> newValue = new HashMap<>();
        newValue.put("phone", "phone_10000");
        newValue.put("name", "name_10000");
        ddh.updateData("Contact.db", "contacts", condition, newValue);


        Map<String, String> queryCondition = new HashMap<>();
        queryCondition.put("phone", "phone_10000");
        queryCondition.put("name", "name_10000");
        String contacts = ddh.queryData("Contact.db", "contacts", queryCondition);
        Log.i(TAG, "updateTest: " + contacts);
    }

    public void insertTest(View view) {
        DefaultDatabaseHelper ddh = new DefaultDatabaseHelper(this);
        Map<String, String> condition = new HashMap<>();
        condition.put("phone", "phone_9999");
        condition.put("name", "name_9999");
        condition.put("email", "email_9999");
        condition.put("street", "street_9999");
        ddh.insertData("Contact.db", "contacts", condition);


        Map<String, String> queryCondition = new HashMap<>();
        queryCondition.put("phone", "phone_9999");
        queryCondition.put("name", "name_9999");
        String contacts = ddh.queryData("Contact.db", "contacts", queryCondition);
        Log.i(TAG, "insertTest: " + contacts);
    }

    public void deleteTest(View view) {
        DefaultDatabaseHelper ddh = new DefaultDatabaseHelper(this);
        Map<String, String> condition = new HashMap<>();
        condition.put("phone", "phone_9999");
        condition.put("name", "name_9999");
        ddh.deleteData("Contact.db", "contacts", condition);


        Map<String, String> queryCondition = new HashMap<>();
        queryCondition.put("phone", "phone_9999");
        queryCondition.put("name", "name_9999");
        String contacts = ddh.queryData("Contact.db", "contacts", queryCondition);
        Log.i(TAG, "insertTest: " + contacts);
    }
}
