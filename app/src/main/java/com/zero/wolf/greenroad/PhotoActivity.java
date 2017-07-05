package com.zero.wolf.greenroad;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.makeramen.roundedimageview.RoundedImageView;
import com.orhanobut.logger.Logger;
import com.zero.wolf.greenroad.activity.SureGoodsActivity;
import com.zero.wolf.greenroad.tools.ActionBarTool;
import com.zero.wolf.greenroad.tools.ImageProcessor;
import com.zero.wolf.greenroad.tools.SPUtils;

import java.io.File;

public class PhotoActivity extends BaseActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    public static final int TYPE_NUMBER = 101;
    public static final int TYPE_BODY = 102;
    public static final int TYPE_GOODS = 103;

    public static final int NONE = 0;
    public static final int PHOTOHRAPH = 1;// 拍照
    public static final int PHOTOZOOM = 2; // 缩放
    public static final int PHOTORESOULT = 3;// 结果

    public static final String IMAGE_UNSPECIFIED = "image/*";

    private static final String TAG = "PhotoActivity";
    private static final int PHOTOHRAPH_NUMBER = 111;
    private static final int PHOTOHRAPH_BODY = 112;
    private static final int PHOTOHRAPH_GOODS = 113;
    private static final int PHOTORESOULT_1 = 221;
    private static final int PHOTORESOULT_2 = 222;
    private static final int PHOTORESOULT_3 = 223;
    private static final int COLOR_GREEN = 331;
    private static final int COLOR_YELLOW = 332;
    private static final int SHUT_CAMERA = 441;
    private static final int SHUT_RECORDING = 442;

    private static final int enter_sure_person = 551;
    private static final int enter_sure_smart = 552;

    private static int currentColor = COLOR_GREEN;
    private static int currentShut = SHUT_CAMERA;

    private int LicensePlateColor;

    private AppCompatActivity mActivity;

    //private RoundedImageView mShow_3_1_car_number;
    private RoundedImageView mShow_3_1_car_number;
    private RoundedImageView mShow_3_2_car_body;
    private RoundedImageView mShow_3_3_car_goods;

    private RoundedImageView mIv_car_number;
    private RoundedImageView mIv_car_body;
    private RoundedImageView mIv_car_goods;
    private Button mBt_ok_send;


    private Uri mPhotoUri;
    private String mFilePath;
    private ToggleButton mToggleButton_color;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        mActivity = this;
        mFilePath = Environment.getExternalStorageState().toString();


        initView();

    }

    private void initView() {

        initToolbar();

        //车牌颜色的按钮
        mToggleButton_color = (ToggleButton) findViewById(R.id.toggleButton_color);

//        拍照或者摄影的按钮
//        mToggleButton_shut = (ToggleButton) findViewById(R.id.toggleButton_shut);

        //点击的

        mIv_car_number = (RoundedImageView) findViewById(R.id.iv_car_number);
        mIv_car_body = (RoundedImageView) findViewById(R.id.iv_car_body);
        mIv_car_goods = (RoundedImageView) findViewById(R.id.iv_car_goods);
        //展示的
        //mShow_3_1_car_number = (RoundedImageView) findViewById(R.id.show_3_1_car_number);
        mShow_3_1_car_number = (RoundedImageView) findViewById(R.id.show_3_1_car_number);
        mShow_3_2_car_body = (RoundedImageView) findViewById(R.id.show_3_2_car_body);
        mShow_3_3_car_goods = (RoundedImageView) findViewById(R.id.show_3_3_car_goods);

        mBt_ok_send = (Button) findViewById(R.id.bt_ok_send);

        mToggleButton_color.setOnCheckedChangeListener(this);
//        mToggleButton_shut.setOnCheckedChangeListener(this);
        mIv_car_number.setOnClickListener(this);
        mIv_car_body.setOnClickListener(this);
        mIv_car_goods.setOnClickListener(this);
        mBt_ok_send.setOnClickListener(this);

        //初始化黄色车牌
        mToggleButton_color.setPadding(0, 0, 70, 0);
    }

    private void initToolbar() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_photo);
        setSupportActionBar(toolbar);

        TextView title_text_view = ActionBarTool.getInstance(mActivity).getTitle_text_view();
        title_text_view.setText(getString(R.string.shut_camera));

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//显示返回上级的箭头
        //getSupportActionBar().setDisplayShowTitleEnabled(false);//将actionbar原有的标题去掉（这句一般是用在xml方法一实现）
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_photo, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.menu_bar_person_definite) {
            Toast.makeText(PhotoActivity.this, "点击了人工识别", Toast.LENGTH_SHORT).show();
            personDefinite();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void personDefinite() {
        enterSureGoodsActivity();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_car_number:
                Log.i(TAG, "onClick: " + "点击了拍车牌的照片");
                shutPhoto(TYPE_NUMBER);
                break;
            case R.id.iv_car_body:
                shutPhoto(TYPE_BODY);
                break;

            case R.id.iv_car_goods:
                shutPhoto(TYPE_GOODS);
                break;
            case R.id.bt_ok_send:
                enterSureGoodsActivity();
                break;

            default:
                break;

        }
    }


    private void enterSureGoodsActivity() {

        carNumberCount();

        Intent intent = new Intent(PhotoActivity.this, SureGoodsActivity.class);
        startActivity(intent);

    }

    /**
     * 对已拍摄车辆以及未上传车辆进行计数
     */
    private void carNumberCount() {
        SPUtils.add_one(mActivity, SPUtils.CAR_COUNT);
        int car_count = (int) SPUtils.get(mActivity, SPUtils.CAR_COUNT, 0);
        Logger.i("car_count------------" + car_count);

        SPUtils.add_one(mActivity, SPUtils.CAR_NOT_COUNT);
        int car_not_count = (int) SPUtils.get(mActivity, SPUtils.CAR_NOT_COUNT, 0);
        Logger.i("car_not_count------------" + car_not_count);
    }

    private void shutPhoto(int type_int) {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (mFilePath != null) {
            mFilePath = null;
            mFilePath = Environment.getExternalStorageDirectory().getAbsolutePath();

        }
        if (type_int == TYPE_NUMBER) {
            mFilePath = mFilePath + "/" + System.currentTimeMillis()
                    + "number.jpg";
            Logger.i(mFilePath);
        } else if (type_int == TYPE_BODY) {
            mFilePath = mFilePath + "/" + System.currentTimeMillis()
                    + "body.jpg";
        } else if (type_int == TYPE_GOODS) {
            mFilePath = mFilePath + "/" + System.currentTimeMillis()
                    + "goods.jpg";

        }

        mPhotoUri = Uri.fromFile(new File(mFilePath));

        intent.putExtra(MediaStore.EXTRA_OUTPUT, mPhotoUri);

        verifyStoragePermissions(mActivity);
        if (type_int == TYPE_NUMBER) {
            startActivityForResult(intent, TYPE_NUMBER);
        } else if (type_int == TYPE_BODY) {
            startActivityForResult(intent, TYPE_BODY);
        } else if (type_int == TYPE_GOODS) {
            startActivityForResult(intent, TYPE_GOODS);

        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
            if (requestCode == TYPE_NUMBER) {
                Bitmap bitmap = BitmapFactory.decodeFile(mFilePath);
                ImageProcessor processor = new ImageProcessor(bitmap);
                Bitmap bitmap1 = processor.scale((float) 0.2);
                mShow_3_1_car_number.setImageBitmap(bitmap1);
            } else if (requestCode == TYPE_BODY) {
                Bitmap bitmap = BitmapFactory.decodeFile(mFilePath);
                ImageProcessor processor = new ImageProcessor(bitmap);
                Bitmap bitmap1 = processor.scale((float) 0.2);
                mShow_3_2_car_body.setImageBitmap(bitmap1);
            } else {
                Bitmap bitmap = BitmapFactory.decodeFile(mFilePath);
                ImageProcessor processor = new ImageProcessor(bitmap);
                Bitmap bitmap1 = processor.scale((float) 0.2);
                mShow_3_3_car_goods.setImageBitmap(bitmap1);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.toggleButton_color:
                analyzeCarColor(buttonView, isChecked);
                break;
            default:
                break;
        }
    }

    private void analyzeCarColor(CompoundButton buttonView, boolean isChecked) {

        int paddingLeft = buttonView.getPaddingLeft();
        if (isChecked) {
            paddingLeft = 0;
            currentColor = COLOR_YELLOW;
            buttonView.setPadding(0, 0, 70, 0);
            Logger.i("当前是黄牌");

        } else {
            paddingLeft = 0;
            currentColor = COLOR_GREEN;
            buttonView.setPadding(80, 0, 0, 0);
            Logger.i("当前是蓝牌");
        }

    }



    /**android API23过后需要动态的申请权限
     * @param activity
     */
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }
}
