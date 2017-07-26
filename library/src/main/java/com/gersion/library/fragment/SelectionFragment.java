package com.gersion.library.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gersion.library.R;
import com.gersion.library.adapter.SelectIconRvAdapter;
import com.gersion.library.adapter.ShowMultiAdapter;
import com.gersion.library.listener.Filter;
import com.gersion.library.listener.OnItemClickListener;
import com.gersion.library.multitype.ItemViewBinder;
import com.gersion.library.smartrecycleview.SmartRecycleView;
import com.gersion.library.utils.ScreenUtils;
import com.gersion.library.utils.SizeUtils;
import com.gersion.library.viewholder.BaseViewHolder;

import java.util.List;

/**
 * Created by gersy on 2017/7/25.
 */

public abstract class SelectionFragment extends Fragment {
    private Activity activity;
    private View mView;
    private LinearLayout mContainer;
    private RecyclerView mIconRecyclerView;
    private EditText mEtSearch;
    private SmartRecycleView mSmartRecycleView;
    private LinearLayout mLlSelectAll;
    private ImageView mIvSelectAll;
    private SelectIconRvAdapter mIconListRvAdapter;
    private LinearLayoutManager mLinearLayoutManager;
    private List<Filter> mSelectList;
    private TextView mTvConfirm;
    private int mMaxWidth;
    private ShowMultiAdapter mAdapter;
    private List<Filter> mData;
    private ImageView mFloatImg;
    private int mStartX;
    private int mStartY;
    private FrameLayout mFlContainer;
    private View mSourceView;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.activity = (Activity) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_selection, container, false);
        initView();
        initListener();
        return mView;
    }

    protected <T extends View> T findView(int id) {
        return (T) mView.findViewById(id);
    }

    private void initView() {
        mContainer = findView(R.id.container);
        mIconRecyclerView = findView(R.id.icon_recyclerView);
        mEtSearch = findView(R.id.et_search);
        mSmartRecycleView = findView(R.id.smartRecycleView);
        mLlSelectAll = findView(R.id.ll_select_all);
        mIvSelectAll = findView(R.id.iv_select_all);
        mTvConfirm = findView(R.id.tv_confirm);
        mFloatImg = findView(R.id.float_img);
        mFlContainer = findView(R.id.fl_container);

//        int[] location = new int[2];
//        mContainer.getLocationOnScreen(location);
//        mStartX = location[0];
//        mStartY = location[1];

        int screenWidth = ScreenUtils.getScreenWidth(activity);
        mMaxWidth = screenWidth * 2 / 3;
        initRecyclerView();
    }

    private void initRecyclerView() {
        mIconListRvAdapter = new SelectIconRvAdapter();
        mLinearLayoutManager = new LinearLayoutManager(activity);
        mLinearLayoutManager.setAutoMeasureEnabled(true);

        mLinearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mIconRecyclerView.setLayoutManager(mLinearLayoutManager);
        mIconRecyclerView.setAdapter(mIconListRvAdapter);

        mSelectList = mIconListRvAdapter.getList();

        mAdapter = new ShowMultiAdapter();
        mAdapter.register(getBeanClass(), getBinder());
        mSmartRecycleView.setFirstPage(1)
                .setAutoRefresh(false)
                .setPageSize(20)
                .setAdapter(mAdapter)
                .loadMoreEnable(false)
                .refreshEnable(false)
                .setLayoutManger(SmartRecycleView.LayoutManagerType.LINEAR_LAYOUT);

        mSmartRecycleView.handleData(mData);

    }

    private void getParentPoint() {
        int[] locations = new int[2];
        mIconRecyclerView.getLocationOnScreen(locations);
        mStartX = locations[0];
        mStartY = locations[1];
    }

    public void getSourcePoint() {
        getParentPoint();
        mFloatImg.setVisibility(View.VISIBLE);

        int[] sourceLocation = new int[2];
        mSourceView.getLocationOnScreen(sourceLocation);
        int startX = sourceLocation[0];
        int startY = sourceLocation[1];

        int[] tagetLocation = new int[2];
        mIconRecyclerView.getLocationOnScreen(tagetLocation);
        int endX = tagetLocation[0]+mIconRecyclerView.getWidth()+SizeUtils.dp2px(activity,5);
        int endY = tagetLocation[1]+SizeUtils.dp2px(activity,5)*2;
        if (endX>=mMaxWidth){
            endX = mMaxWidth-SizeUtils.dp2px(activity,40);
        }

        ObjectAnimator animatorX = ObjectAnimator.ofFloat(mFloatImg, "translationX", startX-mStartX, endX-mStartX);
        ObjectAnimator animatorY = ObjectAnimator.ofFloat(mFloatImg, "translationY", startY - mStartY, endY - mStartY);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(animatorX, animatorY);
        animatorSet.setDuration(calcDuration(startX-endX,startY-endY));
        animatorSet.setInterpolator(new OvershootInterpolator(1.1f));
        animatorSet.start();
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mFloatImg.setVisibility(View.GONE);
            }
        });
    }

    private void animate(RecyclerView sourceRecycler, RecyclerView targetRecycler, int position) {
        View view = sourceRecycler.getLayoutManager().findViewByPosition(position);
        if (view == null) {
            return;
        }
        getParentPoint();

        mFloatImg.setPivotX(mFloatImg.getWidth() / 2);
        mFloatImg.setPivotY(mFloatImg.getHeight() / 2);
        mFloatImg.setVisibility(View.VISIBLE);
        int[] initial = new int[2];
        view.getLocationOnScreen(initial);

        sourceRecycler.getLayoutManager().removeViewAt(position);

        ShowMultiAdapter sourceRecyclerAdapter = (ShowMultiAdapter) sourceRecycler.getAdapter();
        Filter removedItem = sourceRecyclerAdapter.getItem(position);

        int width = view.getWidth();
//        view.removeFromParent();
//        parent.addView(view)
//        view.layoutParams = view.layoutParams.apply {
//            this.width = width
//        }
        int[] container = new int[2];
        sourceRecycler.getLocationOnScreen(container);

        view.setTranslationX(initial[0] + 0.5f);
        view.setTranslationY((initial[1] - container[1]) + 0.5f);

//        @Suppress("UNCHECKED_CAST")
        SelectIconRvAdapter adapter = (SelectIconRvAdapter) targetRecycler.getAdapter();
        int newPos = adapter.add(removedItem);
        int[] targetCoordinates = getTarget(targetRecycler, newPos);


        float targetX = (targetCoordinates[0] - initial[0]) + 0.5f;
        float targetY = (targetCoordinates[1] - initial[1]) + 0.5f;
        long duration = calcDuration(targetX, targetY);

        animateTranslation(mFloatImg,targetX,targetY,duration);

//        ObjectAnimator animatorX = ObjectAnimator.ofFloat(mFloatImg, "translationX", initial[0], targetCoordinates[0]);
//        ObjectAnimator animatorY = ObjectAnimator.ofFloat(mFloatImg, "translationY", initial[1] - mStartY, targetCoordinates[1] - mStartY);
//        AnimatorSet animatorSet = new AnimatorSet();
//        animatorSet.playTogether(animatorX, animatorY);
//        animatorSet.setDuration(500);
//        animatorSet.start();

//        animateAlpha(removedItem, targetRecycler, view, duration)
//        animateTranslation(view, deltaX = targetX, deltaY = targetY, duration = duration)
    }

    private void animateTranslation(View view, float deltaX,float deltaY,Long duration ) {
        view.animate().setDuration(duration)
                .setInterpolator(new OvershootInterpolator(1.1f))
                .translationXBy(deltaX)
                .translationYBy(deltaY)
                .start();
    }

    private long calcDuration(float targetX, float targetY) {
        return (long) (Math.sqrt((targetX * targetX + targetY * targetY)) * 0.7);
    }

    private int[] getTarget(RecyclerView targetRecycler, int index) {
        int prev = Math.max(0, index - 0);
        RecyclerView.ViewHolder viewHolderForAdapterPosition = targetRecycler.findViewHolderForAdapterPosition(prev);
        View targetView = viewHolderForAdapterPosition==null?null:viewHolderForAdapterPosition.itemView;
        if (targetView == null) {
            viewHolderForAdapterPosition = targetRecycler.findViewHolderForAdapterPosition(prev-1);
            targetView = viewHolderForAdapterPosition==null?null:viewHolderForAdapterPosition.itemView;
            if (targetView != null) {
                int[] targetCoordinates = new int[2];
                targetView.getLocationOnScreen(targetCoordinates);
                targetCoordinates[1] += targetView.getHeight();
                return targetCoordinates;
            }
        }

        if (targetView == null) {
            int[] targetCoordinates = new int[2];
            targetRecycler.getLocationOnScreen(targetCoordinates);
            if (targetRecycler.getChildCount() != 0) {
                targetCoordinates[1] += targetRecycler.getHeight();
            }
            return targetCoordinates;
        }

        return new int[]{0,0};
    }


    public void handleData(List data) {
        mData = data;
        if (mSmartRecycleView == null) {
            return;
        }
        mSmartRecycleView.handleData(data);
    }

    public abstract ItemViewBinder<Filter, BaseViewHolder> getBinder();

    public abstract Class<Filter> getBeanClass();

    private void initListener() {
//        mIconListRvAdapter.setOnLastItemLoadListenr(new OnLastItemLoadListener() {
//            @Override
//            public void onLastLoaded(View v) {
//                getSourcePoint(v);
//            }
//        });
        mLlSelectAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mIvSelectAll.isSelected()) {
                    mIconListRvAdapter.addAllData(mAdapter.getSelectionList());
                    mIconRecyclerView.smoothScrollToPosition(mAdapter.getSelectionList().size());
                } else {
                    mIconListRvAdapter.clear();
                }
                mAdapter.changeAllDataStatus(!mIvSelectAll.isSelected());
                refreshLayout(!mIvSelectAll.isSelected());
            }
        });

        getBinder().setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View v, Filter item, boolean isSelected) {
                if (isSelected) {
                    mIconListRvAdapter.add(item);
                    mIconRecyclerView.smoothScrollToPosition(mIconListRvAdapter.getItemCount());
                    mSourceView = v.findViewById(R.id.iv_icon);
//                    mSourceView.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            animate(mSmartRecycleView.getRecyclerView(), mIconRecyclerView, 2);
//                        }
//                    },200);
                    getSourcePoint();

                } else {
                    mIconListRvAdapter.remove(item);
                }
                refreshLayout(false);
            }
        });

        mIconListRvAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View v, Filter item, boolean isSelected) {
                mAdapter.updateItem(item);
                mIconListRvAdapter.remove(item);
                refreshLayout(false);
            }
        });
    }

    private void refreshLayout(boolean isSelected) {
        mIvSelectAll.setSelected(isSelected);

        if (mAdapter.getSelectionList().size() == mIconListRvAdapter.getItemCount()) {
            mIvSelectAll.setSelected(true);
        }

        int size = mSelectList.size();
        mTvConfirm.setText(size == 0 ? "确定" : "确定(" + size + ")");
        if (size == 0) {
            mTvConfirm.setEnabled(false);
        } else {
            mTvConfirm.setEnabled(true);
        }
        int width = SizeUtils.dp2px(activity, 45) * size;
        if (width > mMaxWidth) {
            width = mMaxWidth;
        }
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, LinearLayout.LayoutParams.WRAP_CONTENT);
        mIconRecyclerView.setLayoutParams(params);
        mContainer.requestLayout();
    }

}
