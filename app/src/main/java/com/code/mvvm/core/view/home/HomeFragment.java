package com.code.mvvm.core.view.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.adapter.adapter.DelegateAdapter;
import com.adapter.listener.OnItemClickListener;
import com.code.mvvm.R;
import com.code.mvvm.base.BaseListFragment;
import com.code.mvvm.config.Constants;
import com.code.mvvm.core.data.pojo.book.BookList;
import com.code.mvvm.core.data.pojo.common.TypeVo;
import com.code.mvvm.core.data.pojo.course.CourseInfoVo;
import com.code.mvvm.core.data.pojo.home.CategoryVo;
import com.code.mvvm.core.data.pojo.home.HomeMergeVo;
import com.code.mvvm.core.data.source.HomeRepository;
import com.code.mvvm.core.view.course.VideoDetailsActivity;
import com.code.mvvm.core.vm.HomeViewModel;
import com.code.mvvm.util.AdapterPool;


/**
 * @author：tqzhang on 18/5/2 18:46
 */
public class HomeFragment extends BaseListFragment<HomeViewModel> implements OnItemClickListener {

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public void initView(final Bundle state) {
        //--->创建viewModel->绑定注册订阅者生命周期->以便接收数据->监听状态
        super.initView(state);
        setTitle(getResources().getString(R.string.home_title_name));
        refreshHelper.setEnableLoadMore(false);
    }


    //绑定注册订阅者生命周期----> 接收添加数据
    @Override
    protected void dataObserver() {
        registerSubscriber(HomeRepository.EVENT_KEY_HOME, HomeMergeVo.class)
                .observe(this, homeMergeVo -> {
                    if (homeMergeVo != null) {
                        //请求完成后向页面添加数据
                        HomeFragment.this.addItems(homeMergeVo);
                    }
                });

    }

    //创建layoutManager
    @Override
    protected RecyclerView.LayoutManager createLayoutManager() {
        return new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
    }

    //创建页面适配器--绑定home页面的bean类和viewitem的布局文件
    @Override
    protected DelegateAdapter createAdapter() {
        return AdapterPool.newInstance().getHomeAdapter(getActivity())
                .setOnItemClickListener(this)
                .build();
    }
    //获取主页数据
    @Override
    protected void getRemoteData() {
        mViewModel.getHomeListData();
    }


    //组装获得到的数据
    private void addItems(HomeMergeVo homeMergeVo) {
        if (isRefresh) {
            mItems.clear();
        }
        mItems.add(homeMergeVo.bannerListVo);
        mItems.add(new CategoryVo("title"));
        mItems.add(new TypeVo(getResources().getString(R.string.recommend_live_type)));
        if (homeMergeVo.homeListVo.data.live_recommend.size() > 0) {
            mItems.addAll(homeMergeVo.homeListVo.data.live_recommend);
        }
        mItems.add(new TypeVo(getResources().getString(R.string.recommend_video_type)));
        if (homeMergeVo.homeListVo.data.course.size() > 0) {
            mItems.addAll(homeMergeVo.homeListVo.data.course);
        }
        mItems.add(new TypeVo(getResources().getString(R.string.recommend_book_type)));
        if (homeMergeVo.homeListVo.data.publishingbook.size() > 0) {
            mItems.add(new BookList(homeMergeVo.homeListVo.data.publishingbook));
        }
        mItems.add(new TypeVo(getResources().getString(R.string.special_tab_name)));
        if (homeMergeVo.homeListVo.data.matreialsubject.size() > 0) {
            mItems.addAll(homeMergeVo.homeListVo.data.matreialsubject);
        }
        //将数据设置到适配器
        setData();
    }

    @Override
    public void onItemClick(View view, int i, Object object) {
        if (object != null) {
            if (object instanceof CourseInfoVo) {
                Intent intent = new Intent(activity, VideoDetailsActivity.class);
                intent.putExtra(Constants.COURSE_ID, ((CourseInfoVo) object).courseid);
                activity.startActivity(intent);
            }
        }
    }
}
