package cn.liucl.unitedebugtoolsclient;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import java.util.HashMap;
import java.util.Map;

import cn.liucl.debugtools.db.DefaultDatabaseHelper;
import cn.liucl.unitedebugtoolsclient.database.CarDBHelper;
import cn.liucl.unitedebugtoolsclient.database.ContactDBHelper;
import cn.liucl.unitedebugtoolsclient.database.ExtTestDBHelper;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
//        HashMap<String, File> map = ddh.listAllDatabase();
//        List<String> list = ddh.listAllTables("Car.db");

        Map<String, String> condition = new HashMap<>();
        condition.put("phone", "phone_0");
        condition.put("name", "name_1");
        String contacts = ddh.queryData("Contact.db", "contacts", condition);
        Log.i(TAG, "queryTableName: " + contacts);
    }
}
