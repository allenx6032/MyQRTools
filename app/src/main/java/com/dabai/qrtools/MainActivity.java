package com.dabai.qrtools;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.dabai.qrtools.activity.WIFIandroid;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * //  ┏┓　　　┏┓
 * //┏┛┻━━━┛┻┓
 * //┃　　　　　　　┃
 * //┃　　　━　　　┃
 * //┃　┳┛　┗┳　┃
 * //┃　　　　　　　┃
 * //┃　　　┻　　　┃
 * //┃　　　　　　　┃
 * //┗━┓　　　┏━┛
 * //    ┃　　　┃   神兽保佑
 * //    ┃　　　┃   代码无BUG！
 * //    ┃　　　┗━━━┓
 * //    ┃　　　　　　　┣┓
 * //    ┃　　　　　　　┏┛
 * //    ┗┓┓┏━┳┓┏┛
 * //      ┃┫┫　┃┫┫
 * //      ┗┻┛　┗┻┛
 **/


public class MainActivity extends AppCompatActivity {


    String wificonfig;

    ConstraintLayout cons;

    private Context context;
    TextView tips;
    //特效 duang
    boolean add = true;
    float alpha = 0;
    private String TAG = "dabaizzz";
    private int REQUEST_CODE_SCAN = 100;
    private boolean clip_monitor, screenshot_monitor;
    private Intent screenintent, clipintent;
    private RadioGroup rg;

    TextView btn_create,btn_scan;
    private String[] data1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //Ui美化
        SharedPreferences sp = this.getSharedPreferences("com.dabai.qrtools_preferences", 0);

        setContentView(R.layout.activity_main);
//        getSupportActionBar().setElevation(0);

        context = getApplicationContext();


        //是否阻止截图
        if (Control.is_sc) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }


        //dark
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        /**
         * 申请权限
         */
        int checkResult = getApplicationContext().checkCallingOrSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        //if(!=允许),抛出异常
        if (checkResult != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1); // 动态申请读取权限
            }
        } else {
        }

        /**
         * 申请权限
         */
        int checkResult1 = getApplicationContext().checkCallingOrSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        //if(!=允许),抛出异常
        if (checkResult1 != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1); // 动态申请读取权限
            }
        } else {
        }
        cons = findViewById(R.id.cons);


        init();


        btn_create = findViewById(R.id.btn_create);
        btn_scan = findViewById(R.id.btn_scan);

        btn_create.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                final View view3 = LayoutInflater.from(context).inflate(R.layout.dialog_wifihistory, null);
                final AlertDialog addddddd2 = new AlertDialog.Builder(MainActivity.this)
                        .setTitle("二维码生成 - 历史记录")
                        .setView(view3)
                        .setPositiveButton("关闭", null)
                        .setNeutralButton("清空", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                set_sharedString("QR_create","");
                                Toast.makeText(context, "清空历史记录完成!", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .show();


                Window window2 = addddddd2.getWindow();//对话框窗口
                window2.setGravity(Gravity.BOTTOM);//设置对话框显示在屏幕中间
                window2.setWindowAnimations(R.style.dialog_style_bottom);//添加动画

                ListView lv = view3.findViewById(R.id.lv);
                TextView tv = view3.findViewById(R.id.textView4);

                final String data = get_sharedString("QR_create","");

                data1 = data.split("@@@");
                String[] data2 = new String[data1.length];


                for (int i = 0;i<data1.length;i++){
                    String datatmp = data1[i];
                    if (datatmp.length() > 10){
                        data2[i] = datatmp.substring(0,5)+"..."+datatmp.substring(datatmp.length()-5);
                    }else {
                        data2[i] = datatmp;
                    }
                }


                data2 = reverseArray(data2);
                data1 = reverseArray(data1);

                if (data.equals("")){
                    tv.setVisibility(View.VISIBLE);
                    lv.setVisibility(View.GONE);
                }else {
                    lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                            ToRes(data1[i]);
                            addddddd2.dismiss();

                        }
                    });


                    lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                        @Override
                        public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                            Toast toast = Toast.makeText(getApplicationContext(), ""+data1[i], Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.TOP, 0, 0);
                            toast.show();

                            return true;
                        }
                    });

                    ArrayAdapter adapter = new ArrayAdapter(MainActivity.this, android.R.layout.simple_list_item_1, data2);
                    lv.setAdapter(adapter);
                }

                return true;
            }
        });


        btn_scan.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                final View view3 = LayoutInflater.from(context).inflate(R.layout.dialog_wifihistory, null);
                final AlertDialog addddddd2 = new AlertDialog.Builder(MainActivity.this)
                        .setTitle("二维码扫描 - 历史记录")
                        .setView(view3)
                        .setPositiveButton("关闭", null)
                        .setNeutralButton("清空", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                set_sharedString("QR_scan","");
                                Toast.makeText(context, "清空历史记录完成!", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .show();


                Window window2 = addddddd2.getWindow();//对话框窗口
                window2.setGravity(Gravity.BOTTOM);//设置对话框显示在屏幕中间
                window2.setWindowAnimations(R.style.dialog_style_bottom);//添加动画

                ListView lv = view3.findViewById(R.id.lv);
                TextView tv = view3.findViewById(R.id.textView4);

                final String data = get_sharedString("QR_scan","");

                data1 = data.split("@@@");
                String[] data2 = new String[data1.length];

                for (int i = 0;i<data1.length;i++){
                    String datatmp = data1[i];
                    if (datatmp.length() > 10){
                        data2[i] = datatmp.substring(0,5)+"..."+datatmp.substring(datatmp.length()-5);
                    }else {
                        data2[i] = datatmp;
                    }
                }

                data2 = reverseArray(data2);
                data1 = reverseArray(data1);

                if (data.equals("")){
                    tv.setVisibility(View.VISIBLE);
                    lv.setVisibility(View.GONE);
                }else {
                    lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                            ToResult(data1[i]);
                            addddddd2.dismiss();

                        }
                    });


                    lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                        @Override
                        public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                            Toast toast = Toast.makeText(getApplicationContext(), ""+data1[i], Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.TOP, 0, 0);
                            toast.show();

                            return true;
                        }
                    });


                    ArrayAdapter adapter = new ArrayAdapter(MainActivity.this, android.R.layout.simple_list_item_1, data2);
                    lv.setAdapter(adapter);
                }

                return true;
            }
        });
    }


    /**
     * 反转数组
     * @return
     */
    public static String[] reverseArray(String[] array){
        String [] newArray = new String[array.length];
        for(int i=0; i<newArray.length; i++){
            newArray[i] = array[array.length - i - 1];
        }
        return newArray;
    }



    void ToResult(String data) {

        if (data != null) {
            String result = data;
            Intent intent = new Intent(this, ScanResultActivity.class);
            intent.putExtra("result", result);
            startActivity(intent);
        }
    }

    /**
     * 提交与获取
     *
     * @param key
     * @param value
     */
    public void set_sharedString(String key, String value) {
        SharedPreferences sp = this.getSharedPreferences("data", 0);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public String get_sharedString(String key, String moren) {
        SharedPreferences sp = this.getSharedPreferences("data", 0);
        return sp.getString(key, moren);
    }


    /**
     * 初始化各种东西
     */
    private void init() {

        SharedPreferences sp = this.getSharedPreferences("com.dabai.qrtools_preferences", 0);

        //获取 设置 值  来设置是否启动监听

        clip_monitor = sp.getBoolean("clip_monitor", false);
        screenshot_monitor = sp.getBoolean("screenshot_monitor", false);
        //Log.d(TAG, "剪切板服务: " + clip_monitor);
        //Log.d(TAG, "截图服务: " + screenshot_monitor);


        //初始化剪切板监听
        clipintent = new Intent(this, ClipService.class);

        if (clip_monitor) {
            // Android 8.0使用startForegroundService在前台启动新服务
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(clipintent);
            } else {
                context.startService(clipintent);
            }
        }


        //初始化截图监听
        screenintent = new Intent(this, ScreenshotMonitorService.class);

        if (screenshot_monitor) {
            // Android 8.0使用startForegroundService在前台启动新服务
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(screenintent);
            } else {
                context.startService(screenintent);
            }
        }


/*
//检测Android版本隐藏功能
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P + 1) {
            CardView get_clip = findViewById(R.id.get_clip);
            get_clip.setVisibility(View.GONE);

        }
*/


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.setting:
                startActivity(new Intent(this, SettingActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void QR_create(View view) {

        startActivity(new Intent(this, TextQRActivity.class));

    }

    public void QR_scan(View view) {

        startActivity(new Intent(this, ScanToolActivity.class));

    }


    private boolean isAvilible(Context context, String packageName) {
        final PackageManager packageManager = context.getPackageManager();
        // 获取所有已安装程序的包信息
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        for (int i = 0; i < pinfo.size(); i++) {
            if (pinfo.get(i).packageName.equalsIgnoreCase(packageName))
                return true;
        }
        return false;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {


        if (requestCode == 6) {
            int checkResult16 = getApplicationContext().checkCallingOrSelfPermission(Manifest.permission.READ_CONTACTS);
            //if(!=允许),抛出异常
            if (checkResult16 == PackageManager.PERMISSION_GRANTED) {

                Uri uri = ContactsContract.Contacts.CONTENT_URI;
                Intent intent = new Intent(Intent.ACTION_PICK, uri);
                startActivityForResult(intent, 5);
            }
        }
        if (requestCode == 200) {
            int checkResult16 = getApplicationContext().checkCallingOrSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION);
            //if(!=允许),抛出异常
            if (checkResult16 == PackageManager.PERMISSION_GRANTED) {

                share_thiswifi();
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {

// 扫描二维码/条码回传
            if (requestCode == 5 && resultCode == RESULT_OK) {
                if (data != null) {

                    Uri uri = data.getData();
                    String[] contacts = getPhoneContacts(uri);
                    String content = "BEGIN:VCARD\n" + "VERSION:3.0\n" + "N:" + contacts[0] + "\n" + "TEL:" + contacts[1] + "\n" + "NOTE:QRTools Share\n" + "END:VCARD";
                    ToRes(content);

                }
            }
        } catch (Exception e) {
            Toast.makeText(context, "生成失败,请换一个联系人试试:" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }


    private String[] getPhoneContacts(Uri uri) {
        String[] contact = new String[2];
        //得到ContentResolver对象
        ContentResolver cr = getContentResolver();
        //取得电话本中开始一项的光标
        Cursor cursor = cr.query(uri, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            //取得联系人姓名
            int nameFieldColumnIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
            contact[0] = cursor.getString(nameFieldColumnIndex);
            //取得电话号码
            String ContactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
            Cursor phone = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + ContactId, null, null);
            if (phone != null) {

                phone.moveToFirst();
                contact[1] = phone.getString(phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

            }
            phone.close();
            cursor.close();
        } else {
            return null;
        }
        return contact;
    }

    public void vcf_create(View view) {

        /**
         * 申请权限
         */
        int checkResult = getApplicationContext().checkCallingOrSelfPermission(Manifest.permission.READ_CONTACTS);
        //if(!=允许),抛出异常
        if (checkResult != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, 6); // 动态申请读取权限
            }
        } else {
            Uri uri = ContactsContract.Contacts.CONTENT_URI;
            Intent intent = new Intent(Intent.ACTION_PICK, uri);
            startActivityForResult(intent, 5);
        }
    }

    //退出时的时间
    private long mExitTime;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (isServiceRunning(getApplicationContext(), "com.dabai.qrtools.ScreenshotMonitorService") || isServiceRunning(getApplicationContext(), "com.dabai.qrtools.ClipService")) {
                new AlertDialog.Builder(this).setTitle("警告").setMessage("你还有监听服务在后台运行,某些手机退出软件会造成服务停止,不推荐强制退出")

                        .setPositiveButton("最小化", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent();
                                // 为Intent设置Action、Category属性
                                intent.setAction(Intent.ACTION_MAIN);// "android.intent.action.MAIN"
                                intent.addCategory(Intent.CATEGORY_HOME); //"android.intent.category.HOME"
                                startActivity(intent);
                            }
                        }).show();
            } else {

                if ((System.currentTimeMillis() - mExitTime) > 2000) {
                    Toast.makeText(context, "再按一次退出", Toast.LENGTH_SHORT).show();
                    mExitTime = System.currentTimeMillis();
                } else {
                    finish();
                }
            }
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }


    /**
     * 判断服务是否开启
     *
     * @return
     */
    public static boolean isServiceRunning(Context context, String ServiceName) {
        if (("").equals(ServiceName) || ServiceName == null)
            return false;
        ActivityManager myManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        ArrayList<ActivityManager.RunningServiceInfo> runningService = (ArrayList<ActivityManager.RunningServiceInfo>) myManager
                .getRunningServices(30);
        for (int i = 0; i < runningService.size(); i++) {
            if (runningService.get(i).service.getClassName().toString()
                    .equals(ServiceName)) {
                return true;
            }
        }
        return false;
    }

    public void more_create(View view) {
        /**
         * 批量入口
         */

        startActivity(new Intent(this, MoreActivity.class));
    }

    public void gif_create(View view) {
        startActivity(new Intent(this, GifActivity.class));
    }

    boolean isroot;

    public void wifi_config(View view) {

        final AlertDialog ad = new AlertDialog.Builder(this).setTitle("分享WiFi")
                .setItems(new String[]{"手动生成分享码", "自动获取WiFi列表", "历史连接记录"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (i) {

                            case 0:

                                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                                        != PackageManager.PERMISSION_GRANTED) {//未开启定位权限
                                    Toast.makeText(MainActivity.this, "需要开启定位权限！", Toast.LENGTH_SHORT).show();
                                    //开启定位权限,200是标识码
                                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 200);
                                } else {
                                    share_thiswifi();
                                }

                                break;

                            case 1:

                                suthread();

                                break;
                            case 2:

                                final View view2 = LayoutInflater.from(context).inflate(R.layout.dialog_wifihistory, null);
                                final AlertDialog addddddd2 = new AlertDialog.Builder(MainActivity.this)
                                        .setTitle("选择一个历史连接")
                                        .setView(view2)
                                        .show();


                                Window window2 = addddddd2.getWindow();//对话框窗口
                                window2.setGravity(Gravity.BOTTOM);//设置对话框显示在屏幕中间
                                window2.setWindowAnimations(R.style.dialog_style_bottom);//添加动画

                                ListView lv = view2.findViewById(R.id.lv);
                                TextView tv = view2.findViewById(R.id.textView4);


                                try {
                                    final File dir = new File("/sdcard/QRTWifi/");
                                    final String[] data = dir.list();

                                    lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                            try {
                                                String text = new FileUtils().readText(new File(dir, data[i])).trim();
                                                Intent resultIntent = new Intent(MainActivity.this, TextQRActivity.class);
                                                resultIntent.putExtra("download", text);
                                                addddddd2.dismiss();
                                                startActivity(resultIntent);

                                            } catch (FileNotFoundException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    });
                                    ArrayAdapter adapter = new ArrayAdapter(MainActivity.this, android.R.layout.simple_list_item_1, data);
                                    lv.setAdapter(adapter);
                                } catch (Exception e) {
                                    tv.setVisibility(View.VISIBLE);
                                    lv.setVisibility(View.GONE);

                                }
                                break;
                        }
                    }
                }).show();


        Window window = ad.getWindow();//对话框窗口
        window.setGravity(Gravity.BOTTOM);//设置对话框显示在屏幕中间
        window.setWindowAnimations(R.style.dialog_style_bottom);//添加动画
    }

    private void share_thiswifi() {

        final View view = LayoutInflater.from(context).inflate(R.layout.dialog_wifishare, null);

        AlertDialog addddddd = new AlertDialog.Builder(MainActivity.this)
                .setTitle("请输入各项空值")
                .setView(view)
                .setNeutralButton("取消", null)
                .setPositiveButton("生成", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {


                        switch (rg.getCheckedRadioButtonId()) {


                            case R.id.radioButton:
                                EditText ed1 = view.findViewById(R.id.ed1);

                                String text = "WIFI:T:;P:;S:" + ed1.getText().toString() + ";";

                                ToRes(text);
                                break;

                            case R.id.radioButton2:
                                EditText ed11 = view.findViewById(R.id.ed1);
                                EditText ed22 = view.findViewById(R.id.ed2);

                                String text2 = "WIFI:T:WPA;P:" + ed22.getText().toString() + ";S:" + ed11.getText().toString() + ";";

                                ToRes(text2);

                                break;
                        }

                    }
                })
                .show();

        rg = view.findViewById(R.id.rg);
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            EditText ed2 = view.findViewById(R.id.ed2);

            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (radioGroup.getCheckedRadioButtonId()) {
                    case R.id.radioButton:

                        ed2.setEnabled(false);
                        break;

                    case R.id.radioButton2:

                        ed2.setEnabled(true);
                        break;
                }

            }
        });


        WifiManager wifiManager = (WifiManager) context.getSystemService(WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();

        EditText ed1 = view.findViewById(R.id.ed1);

        ed1.setText(wifiInfo.getSSID().replace("\"", ""));

        WifiManager mWifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = mWifiManager.getConnectionInfo();


        Window window = addddddd.getWindow();//对话框窗口
        window.setGravity(Gravity.BOTTOM);//设置对话框显示在屏幕中间
        window.setWindowAnimations(R.style.dialog_style_bottom);//添加动画

    }


    void ToRes(String text) {
        Intent resultIntent = new Intent(MainActivity.this, TextQRActivity.class);
        resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        resultIntent.putExtra("download", text);
        startActivity(resultIntent);

    }

    public void suthread() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                isroot = isRoot();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        if (!isroot) {
                            AlertDialog ad = new AlertDialog.Builder(MainActivity.this).setTitle("权限提示")
                                    .setMessage("ROOT权限获取失败,点击重新获取。")
                                    .setPositiveButton("尝试获取", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            try {
                                                Runtime.getRuntime().exec("su");
                                            } catch (IOException e) {
                                            }
                                            suthread();

                                        }
                                    }).show();

                            Window window = ad.getWindow();//对话框窗口
                            window.setGravity(Gravity.BOTTOM);//设置对话框显示在屏幕中间
                            window.setWindowAnimations(R.style.dialog_style_bottom);//添加动画

                        } else {
                            showWifi();
                        }
                    }
                });
            }
        }).start();


    }


    //显示wifi选择弹窗
    private void showWifi() {

        startActivity(new Intent(this, WIFIandroid.class));
    }


    private boolean isRoot() {
        try {
            Process process = Runtime.getRuntime().exec("su");
            process.getOutputStream().write("exit\n".getBytes());
            process.getOutputStream().flush();

            int i = process.waitFor();
            if (0 == i) {
                process = Runtime.getRuntime().exec("su");
                return true;
            }

        } catch (Exception e) {
            return false;
        }
        return false;

    }

    private String exec(String command) {
        try {
            java.lang.Process process = Runtime.getRuntime().exec(command);
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));
            int read;
            char[] buffer = new char[4096];
            StringBuffer output = new StringBuffer();
            while ((read = reader.read(buffer)) > 0) {
                output.append(buffer, 0, read);
            }
            reader.close();
            process.waitFor();
            return output.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public void QR_getclip(View view) {


        try {
            ClipboardManager clip = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ContentResolver cr = getContentResolver();
            ClipData clipdata = clip.getPrimaryClip();

            if (clip != null) {

                String text = null;

                //从剪贴板数据中获取第一项
                ClipData.Item item = clipdata.getItemAt(0);
                text = item.coerceToText(this).toString();
                ToRes(text);
            }

        } catch (Exception e) {
            Toast.makeText(context, "剪切板异常:)", Toast.LENGTH_SHORT).show();
        }


    }



}