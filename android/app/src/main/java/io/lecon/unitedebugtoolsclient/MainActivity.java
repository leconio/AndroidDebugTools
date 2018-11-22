package io.lecon.unitedebugtoolsclient;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.lecon.debugtools.DebugTools;
import io.lecon.debugtools.db.DefaultDatabaseHelper;
import io.lecon.unitedebugtoolsclient.database.CarDBHelper;
import io.lecon.unitedebugtoolsclient.database.ContactDBHelper;
import io.lecon.unitedebugtoolsclient.database.ExtTestDBHelper;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = DebugTools.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        startActivity(new Intent(this, SettingActivity.class));
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
                contactDBHelper.insertContact(name, phone, "{\"taskVo\":{\"id\":\"0001OnFE7tp7_acz0gpAe=lQOrZsyXIS000\",\"region\":\"886EA\",\"status\":\"COMPLETED\",\"subStatus\":\"\",\"dispatchStatus\":\"\",\"transferStatus\":\"\",\"operator\":\"708317\",\"owner\":\"708317\",\"bizCatg\":\"\",\"taskType\":\"P\",\"startTime\":1542769200000,\"completeTime\":1542783810000,\"limitTime\":1542770266000,\"remark\":\"\",\"createdTime\":1542766666000,\"taskListTag\":\"C\",\"modifyTime\":1542783811368,\"timeForPopup\":1542766666000,\"pickupDraftOpId\":\"708317\",\"markExceptionBefore54\":false},\"orderDetail\":{\"orderInfo\":{\"orderId\":\"CX1457832774033408\",\"orderCreateTm\":1542766665000,\"orderCityCode\":\"852\",\"orderDeptCode\":\"886EA\",\"scheduleBookTime\":\"\",\"scheduleLastTime\":\"\",\"deliveryTel\":\"52664647\",\"deliveryMobile\":\"52664647\",\"deliveryContact\":\"HK Sender\",\"deliveryAddr\":\"香港南區石澳伟业街161号德胜广场25-26FSLG ASIA TEST LABSSERVICELtd\",\"simpleDeliveryAddr\":\"香港南區石澳伟业街161号德胜广场25-26FSLG ASIA TEST LABSSERVICELtd\",\"deliveryCompany\":\"\",\"consigneeTel\":\"92466666\",\"consigneeMobile\":\"92466666\",\"consigneeContact\":\"Emma\",\"consigneeAddr\":\"New York NEW YORK NEW YORK The Statue of Liberty\",\"simpleConsigneeAddr\":\"New York NEW YORK NEW YORK The Statue of Liberty\",\"consigneeCompany\":\"guess it\",\"pkgName\":\"書籍\",\"pkgNumber\":107,\"pkgWeight\":0.5,\"realWeight\":0.5,\"pkgFreight\":1050,\"payMethod\":\"7\",\"originPayMethod\":\"1\",\"destCityCode\":\"001\",\"productType\":\"S5 Plus B类\",\"productTypeHHT\":\"C921\",\"productTypeName\":\"標快十(國際)- B類包裹\",\"waybillNo\":\"086358641240\",\"employeeTel\":\"15278885405\",\"isHhtWaybill\":\"1\",\"originId\":\"CX1457832774033408\",\"proName\":\"標快十(國際)- B類包裹\",\"gisResult\":{\"gisFlag\":\"1\"},\"selfSendFlg\":false,\"pickupType\":\"3\",\"orderSourceType\":\"1\",\"specialShaped\":false,\"selfPickFlag\":false,\"omsSelfPickFlag\":false,\"riskIndicator\":-1,\"interType\":\"2\",\"orderChannel\":\"11\",\"deliveryCustomerId\":\"45567\",\"consigneeCustomerId\":\"45567\",\"addresseePostCode\":\"45678\",\"declareCurrency\":\"HKD\",\"declareWeight\":0.5,\"declareType\":0,\"taxPaymethod\":2,\"hmtDeclareValue\":2138.93,\"hmtCargoList\":[{\"name\":\"書籍\",\"uom\":\"本\",\"quantity\":107,\"price\":\"19.99\",\"origin\":\"HK\",\"englishName\":\"book\",\"englishUom\":\"piece\"}],\"englishDeliveryContact\":\"HK Sender\",\"englishDeliveryAddr\":\"Hong Kong Hong Kong Southern District Shek O WeiYeJie161HaoDeShengGuangChang25-26FSLG ASIA TEST LABSSERVICELtd\",\"pickupTaskType\":3,\"picUrl\":[null],\"isStoreGoods\":false,\"printFlag\":false},\"serviceList\":[]},\"labels\":[{\"displayName\":\"印\",\"code\":\"DZD\",\"labelType\":\"D\"}],\"currentTime\":1542783817114,\"textView\":\"<html>\\n<body bgcolor=\\\"#efefef\\\" style=\\\"word-wrap:break-word\\\">\\n\\n<font color=\\\"#c07a1b\\\">原始訂單號:CX1457832774033408<\\/font>\\n\\n\\n\\n<table width=\\\"100%\\\">\\n<\\/table>\\n<\\/body>\\n<\\/html>\",\"version\":5,\"handoverToStore\":false,\"bagStatus\":0,\"urgeCount\":0,\"extend\":\"{\\\"a\\\":false,\\\"authentication\\\":false,\\\"customsClearanceBatch\\\":\\\"TPE0400JFK\\\",\\\"destinationCurrencyName\\\":\\\"USD\\\",\\\"exchangeRate\\\":0.033094,\\\"extend\\\":\\\"1\\\",\\\"hideExchange\\\":true,\\\"isElecSignFlag\\\":false,\\\"isReplace\\\":false,\\\"originCurrencyName\\\":\\\"NTD\\\",\\\"secret\\\":0,\\\"specialShaped\\\":false}\",\"specialWareHouseFlag\":false,\"previewOneKeyPickup\":true,\"deliveryChgOmsLst\":[],\"waybillDetail\":{\"waybill\":{\"waybillNo\":\"086358641240\",\"deliveryTel\":\"52664647\",\"deliveryMobile\":\"52664647\",\"deliveryContact\":\"HK Sender\",\"deliveryAddr\":\"香港南區石澳伟业街161号德胜广场25-26FSLG ASIA TEST LABSSERVICELtd\",\"simpleDeliveryAddr\":\"香港南區石澳伟业街161号德胜广场25-26FSLG ASIA TEST LABSSERVICELtd\",\"deliveryCompany\":\"\",\"consigneeTel\":\"92466666\",\"consigneeMobile\":\"92466666\",\"consigneeContact\":\"Emma\",\"consigneeAddr\":\"New York NEW YORK NEW YORK The Statue of Liberty\",\"simpleConsigneeAddr\":\"New York NEW YORK NEW YORK The Statue of Liberty\",\"consigneeCompany\":\"guess it\",\"addresseePostCode\":\"45678\",\"declaredValue\":0,\"identityAuthentication\":false},\"waybillFee\":[{\"feeTypeCode\":\"1\",\"feeAmt\":1050,\"paymentTypeCode\":\"7\",\"settlementTypeCode\":\"1\",\"waybillFeeName\":\"運費\"}],\"childList\":[]},\"examineCargo\":false}", street, null);
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
        Map<String, Object> condition = new HashMap<>();
        condition.put("phone", "phone_1");
        condition.put("name", "name_1");
        String contacts = ddh.queryData("Contact.db", "contacts", condition);
        Log.i(TAG, "queryTest: " + contacts);
    }

    public void updateTest(View view) {
        DefaultDatabaseHelper ddh = new DefaultDatabaseHelper(this);
        Map<String, Object> condition = new HashMap<>();
        condition.put("phone", "phone_1");
        condition.put("name", "name_1");

        Map<String, Object> newValue = new HashMap<>();
        newValue.put("phone", "phone_10000");
        newValue.put("name", "name_10000");
        ddh.updateData("Contact.db", "contacts", condition, newValue);


        Map<String, Object> queryCondition = new HashMap<>();
        queryCondition.put("phone", "phone_10000");
        queryCondition.put("name", "name_10000");
        String contacts = ddh.queryData("Contact.db", "contacts", queryCondition);
        Log.i(TAG, "updateTest: " + contacts);
    }

    public void insertTest(View view) {
        DefaultDatabaseHelper ddh = new DefaultDatabaseHelper(this);
        Map<String, Object> condition = new HashMap<>();
        condition.put("phone", "phone_9999");
        condition.put("name", "name_9999");
        condition.put("email", "email_9999");
        condition.put("street", "street_9999");
        ddh.insertData("Contact.db", "contacts", condition);


        Map<String, Object> queryCondition = new HashMap<>();
        queryCondition.put("phone", "phone_9999");
        queryCondition.put("name", "name_9999");
        String contacts = ddh.queryData("Contact.db", "contacts", queryCondition);
        Log.i(TAG, "insertTest: " + contacts);
    }

    public void deleteTest(View view) {
        DefaultDatabaseHelper ddh = new DefaultDatabaseHelper(this);
        Map<String, Object> condition = new HashMap<>();
        condition.put("phone", "phone_9999");
        condition.put("name", "name_9999");
        ddh.deleteData("Contact.db", "contacts", condition);


        Map<String, Object> queryCondition = new HashMap<>();
        queryCondition.put("phone", "phone_9999");
        queryCondition.put("name", "name_9999");
        String contacts = ddh.queryData("Contact.db", "contacts", queryCondition);
        Log.i(TAG, "insertTest: " + contacts);
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
}
