package com.zero.wolf.greenroad.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.zero.wolf.greenroad.R;
import com.zero.wolf.greenroad.activity.SureGoodsActivity;
import com.zero.wolf.greenroad.adapter.DetailsRecyclerAdapter;
import com.zero.wolf.greenroad.bean.DetailInfoBean;
import com.zero.wolf.greenroad.bean.SerializableMain2Sure;
import com.zero.wolf.greenroad.manager.CarColorManager;
import com.zero.wolf.greenroad.manager.GlobalManager;
import com.zero.wolf.greenroad.tools.ToastUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


public class DetailsFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    Unbinder unbinder;
    @BindView(R.id.recycler_view_shoot_photo)
    RecyclerView mRecyclerViewShootPhoto;
    private static RadioGroup mRadioGroupColor;
    private static TextView mTvChangeNumberDetail;
    private static TextView mTvChangeGoodsDetail;



    private static String mCurrent_color;


    private String mCarNumber;
    private String mCarGoods;

    private static ArrayList<MyBitmap> mMyBitmaps;
    private DetailsRecyclerAdapter mAdapter;

    public DetailsFragment() {
        // Required empty public constructor
    }


    public static DetailsFragment newInstance() {
        DetailsFragment fragment = new DetailsFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
     /*   if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }*/
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_details, container, false);
        unbinder = ButterKnife.bind(this, view);
        initView(view);
        initRadioColor();
        initRecyclerView();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        CarNumberFragment.setTextChangedFragment((edittext -> {
            if (edittext.length() == 7) {
                mCarNumber = edittext;
            } else {
                mCarNumber = "车牌号格式不正确";
            }
        }));

        GoodsFragment.setTextChangedFragment(edittext -> {
            mCarGoods = edittext;
        });

        PhotoFragment.setBitmapListListener(bitmaps -> {
            mMyBitmaps = bitmaps;
        });



        Logger.i(mCarNumber + "]]]]]]]]]");
        mTvChangeNumberDetail.setText(mCarNumber);
        mTvChangeGoodsDetail.setText(mCarGoods);

        if (mMyBitmaps != null && mMyBitmaps.size() != 0) {
            mAdapter.updateListView(mMyBitmaps);
        }
    }


    private void initRecyclerView() {
        LinearLayoutManager manager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        mRecyclerViewShootPhoto.setLayoutManager(manager);
        mAdapter = new DetailsRecyclerAdapter(getContext(), mMyBitmaps, () -> {
            enterSureActivity(GlobalManager.ENTERTYPE_PHOTO);
        });
        mRecyclerViewShootPhoto.setAdapter(mAdapter);
    }

    private void initRadioColor() {
        mRadioGroupColor.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.license_yellow:
                    mCurrent_color = CarColorManager.COLOR_YELLOW;
                    Logger.i(mCurrent_color);
                    break;
                case R.id.license_blue:
                    mCurrent_color = CarColorManager.COLOR_BLUE;
                    Logger.i(mCurrent_color);
                    break;
                case R.id.license_black:
                    mCurrent_color = CarColorManager.COLOR_BLACK;
                    Logger.i(mCurrent_color);
                    break;
                case R.id.license_green:
                    mCurrent_color = CarColorManager.COLOR_GREEN;
                    Logger.i(mCurrent_color);
                    break;
                case R.id.license_white:
                    Logger.i(mCurrent_color);
                    mCurrent_color = CarColorManager.COLOR_WHITE;
                    break;

                default:
                    break;
            }
        });
    }

    private void initView(View view) {

        mTvChangeNumberDetail = (TextView) view.findViewById(R.id.tv_change_number_detail);
        mTvChangeGoodsDetail = (TextView) view.findViewById(R.id.tv_change_goods_detail);

        mRadioGroupColor = (RadioGroup) view.findViewById(R.id.radio_group_color);

    }

    @OnClick({R.id.tv_change_number_detail, R.id.tv_change_goods_detail})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_change_number_detail:
                enterSureActivity(GlobalManager.ENTERTYPE_NUMBER);
                ToastUtils.singleToast("进入车牌号的选择业");
                break;
            case R.id.tv_change_goods_detail:
                enterSureActivity(GlobalManager.ENTERTYPE_GOODS);
                ToastUtils.singleToast("进入货物的选择业");
                break;
            default:
                break;
        }
    }

    private void enterSureActivity(String type) {
        String carNumber = mTvChangeNumberDetail.getText().toString();
        String goods = mTvChangeGoodsDetail.getText().toString();

        SerializableMain2Sure main2Sure = new SerializableMain2Sure();
        main2Sure.setCarNumber_I(carNumber);
        main2Sure.setGoods_I(goods);

        SureGoodsActivity.actionStart(getActivity(), main2Sure, type);

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    /**
     * 将数据再传递进子fragment的回调操作
     * @param listener
     */
    public static void setBitmapListListener(BitmapListListener listener) {
        if (mMyBitmaps != null) {
            listener.BitmapListener(mMyBitmaps);
        }
    }


    public interface BitmapListListener {
        void BitmapListener(ArrayList<MyBitmap> bitmaps);
    }

    public void setDetailsConnectListener(DetailsBeanConnectListener listener) {

        ArrayList<String> bitmap_path = new ArrayList<>();
        if (mMyBitmaps != null) {
            for (int i = 0; i < mMyBitmaps.size(); i++) {
                bitmap_path.add(mMyBitmaps.get(i).getPath());
            }
        }

        String number = mTvChangeNumberDetail.getText().toString().trim();
        String goods = mTvChangeGoodsDetail.getText().toString().trim();

        DetailInfoBean bean = new DetailInfoBean();
        bean.setColor(mCurrent_color);
        bean.setNumber(number);
        bean.setGoods(goods);

        bean.setBitmapPaths(bitmap_path);

        listener.beanConnect(bean);
    }

    public interface DetailsBeanConnectListener {

        void beanConnect(DetailInfoBean bean);
    }
}
