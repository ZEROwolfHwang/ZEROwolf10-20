package com.android.htc.greenroad.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.orhanobut.logger.Logger;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;
import com.android.htc.greenroad.R;
import com.android.htc.greenroad.activity.ShowActivity;
import com.android.htc.greenroad.bean.ScanInfoBean;
import com.android.htc.greenroad.litepalbean.SupportScan;
import com.android.htc.greenroad.tools.SPUtils;
import com.android.htc.greenroad.tools.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class ScanFragment extends Fragment implements CompoundButton.OnCheckedChangeListener {

    private static final int REQUEST_CODE_SCAN = 901;


    Unbinder unbinder;

    @BindView(R.id.scan_qr_code)
    TextView mScanQrCode;
    @BindView(R.id.btn_edit_able)
    ToggleButton mBtnEditAble;

    private static EditText mTextExportNumber;
    private static EditText mText_table_1;
    private static EditText mText_table_2;
    private static EditText mText_table_3;
    private static EditText mText_table_4;
    private static EditText mText_table_5;
    private static EditText mText_table_6;
    private static EditText mText_table_7;
    private static EditText mText_table_8;
    private static EditText mText_table_9;
    private static EditText mText_table_10;
    private static EditText mText_table_11;
    private static EditText mText_table_12;

    private static ScanFragment sFragment;
    private static SupportScan sSupportScan;
    private static String enterType;
    private EditText[] mEditTextsScan;
    private ToggleButton mToggleEditable;
    private int mThemeTag;

    public ScanFragment() {
        // Required empty public constructor
    }

    public static ScanFragment newInstance(String type) {
        if (sFragment == null) {
            sFragment = new ScanFragment();
        }
        enterType = type;
        return sFragment;
    }

    public static ScanFragment newInstance(String type, SupportScan scanInfoBean) {
        if (sFragment == null) {
            sFragment = new ScanFragment();
        }
        sSupportScan = scanInfoBean;
        enterType = type;
        return sFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mThemeTag = (int) SPUtils.get(getContext(), SPUtils.KEY_THEME_TAG, 1);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_scan, container, false);
        unbinder = ButterKnife.bind(this, view);


        initView(view);
        return view;
    }

    private void initView(View view) {

//        mTvChangeLaneConfig= (TextView) view.findViewById(R.id.tv_change_lane_config);
        mTextExportNumber = (EditText) view.findViewById(R.id.export_number);
        mText_table_1 = (EditText) view.findViewById(R.id.text_table_1);
        mText_table_2 = (EditText) view.findViewById(R.id.text_table_2);
        mText_table_3 = (EditText) view.findViewById(R.id.text_table_3);
        mText_table_4 = (EditText) view.findViewById(R.id.text_table_4);
        mText_table_5 = (EditText) view.findViewById(R.id.text_table_5);
        mText_table_6 = (EditText) view.findViewById(R.id.text_table_6);
        mText_table_7 = (EditText) view.findViewById(R.id.text_table_7);
        mText_table_8 = (EditText) view.findViewById(R.id.text_table_8);
        mText_table_9 = (EditText) view.findViewById(R.id.text_table_9);
        mText_table_10 = (EditText) view.findViewById(R.id.text_table_10);
        mText_table_11 = (EditText) view.findViewById(R.id.text_table_11);
        mText_table_12 = (EditText) view.findViewById(R.id.text_table_12);
        mToggleEditable = (ToggleButton) view.findViewById(R.id.btn_edit_able);

        mEditTextsScan = new EditText[]{mTextExportNumber, mText_table_1,
                mText_table_2, mText_table_3, mText_table_4, mText_table_5,
                mText_table_6, mText_table_7, mText_table_8, mText_table_9, mText_table_10,
                mText_table_11, mText_table_12
        };

        mToggleEditable.setOnCheckedChangeListener(this);

        //从草稿的详情页进入采集界面进行修改,会初始化扫描的内容
        if (ShowActivity.TYPE_DRAFT_ENTER_SHOW.equals(enterType)) {
            mTextExportNumber.setText(sSupportScan.getScan_code());
            mText_table_1.setText(sSupportScan.getScan_01Q());
            mText_table_2.setText(sSupportScan.getScan_02Q());
            mText_table_3.setText(sSupportScan.getScan_03Q());
            mText_table_4.setText(sSupportScan.getScan_04Q());
            mText_table_5.setText(sSupportScan.getScan_05Q());
            mText_table_6.setText(sSupportScan.getScan_06Q());
            mText_table_7.setText(sSupportScan.getScan_07Q());
            mText_table_8.setText(sSupportScan.getScan_08Q());
            mText_table_9.setText(sSupportScan.getScan_09Q());
            mText_table_10.setText(sSupportScan.getScan_10Q());
            mText_table_11.setText(sSupportScan.getScan_11Q());
            mText_table_12.setText(sSupportScan.getScan_12Q());
        }
    }


    @OnClick({R.id.scan_qr_code, R.id.btn_edit_able})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.scan_qr_code:
                Intent intent = new Intent(getActivity(), CaptureActivity.class);
                startActivityForResult(intent, REQUEST_CODE_SCAN);
                DetailsFragment.notifyTag(false);
                break;
            case R.id.btn_edit_able:
                if (mBtnEditAble.isChecked()) {
                    for (int i = 0; i < mEditTextsScan.length; i++) {
                        mEditTextsScan[i].setEnabled(true);
                        mEditTextsScan[i].setTextColor(Color.BLUE);
                    }
                } else {
                    for (int i = 0; i < mEditTextsScan.length; i++) {
                        if (mThemeTag == 1) {
                            mEditTextsScan[i].setTextColor(Color.DKGRAY);
                        } else {
                            mEditTextsScan[i].setTextColor(Color.WHITE);
                        }
                        mEditTextsScan[i].setEnabled(false);
                    }
                }
                break;
            default:
                break;
        }
    }

    /**
     * 对扫描二维码后的信息进行解析
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 处理二维码扫描结果
        if (requestCode == REQUEST_CODE_SCAN) {
            //处理扫描结果（在界面上显示）
            if (null != data) {
                Bundle bundle = data.getExtras();
                if (bundle == null) {
                    return;
                }
                if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                    String result = bundle.getString(CodeUtils.RESULT_STRING);
                    Logger.i("解析结果:" + result);

                    String[] result_QrCode = result.split(";");

                    mTextExportNumber.setText(result_QrCode[0]);

                    mText_table_1.post(() -> mText_table_1.setText(result_QrCode[1]));
                    mText_table_3.setText(result_QrCode[10]);
                    mText_table_5.setText(result_QrCode[14]);
                    //入口路段

                    mText_table_7.setText(result_QrCode[2]);
                    mText_table_9.setText(result_QrCode[3]);
                    mText_table_11.setText(result_QrCode[6]);

                    mText_table_2.setText(result_QrCode[11]);
                    mText_table_4.setText(result_QrCode[12]);
                    mText_table_6.setText(result_QrCode[13]);
                    //出口路段
                    mText_table_8.setText(result_QrCode[4]);
                    mText_table_10.setText(result_QrCode[5]);
                    mText_table_12.setText(result_QrCode[9]);


                } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                    ToastUtils.singleToast("解析二维码失败");
                }
            }
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    public static void setScanConnectListener(ScanBeanConnectListener listener) {

        String scanCode = mTextExportNumber.getText().toString().trim();

        if (scanCode != null) {
            String scan_01Q = mText_table_1.getText().toString().trim();
            String scan_02Q = mText_table_2.getText().toString().trim();
            String scan_03Q = mText_table_3.getText().toString().trim();
            String scan_04Q = mText_table_4.getText().toString().trim();
            String scan_05Q = mText_table_5.getText().toString().trim();
            String scan_06Q = mText_table_6.getText().toString().trim();
            String scan_07Q = mText_table_7.getText().toString().trim();
            String scan_08Q = mText_table_8.getText().toString().trim();
            String scan_09Q = mText_table_9.getText().toString().trim();
            String scan_10Q = mText_table_10.getText().toString().trim();
            String scan_11Q = mText_table_11.getText().toString().trim();
            String scan_12Q = mText_table_12.getText().toString().trim();

            ScanInfoBean bean = new ScanInfoBean();

            bean.setScan_code(scanCode);
            bean.setScan_01Q(scan_01Q);
            bean.setScan_02Q(scan_02Q);
            bean.setScan_03Q(scan_03Q);
            bean.setScan_04Q(scan_04Q);
            bean.setScan_05Q(scan_05Q);
            bean.setScan_06Q(scan_06Q);
            bean.setScan_07Q(scan_07Q);
            bean.setScan_08Q(scan_08Q);
            bean.setScan_09Q(scan_09Q);
            bean.setScan_10Q(scan_10Q);
            bean.setScan_11Q(scan_11Q);
            bean.setScan_12Q(scan_12Q);
            Logger.i("!!!!!!!!!!!!!!!!!" + bean.toString());
            listener.beanConnect(bean);
        } else {
            ToastUtils.singleToast("请扫描二维码");
            return;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

    }

    public interface ScanBeanConnectListener {
        void beanConnect(ScanInfoBean bean);
    }
}
