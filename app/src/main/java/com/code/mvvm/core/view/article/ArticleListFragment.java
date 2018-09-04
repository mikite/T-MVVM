package com.code.mvvm.core.view.article;

import android.arch.lifecycle.Observer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import com.code.mvvm.base.BaseListFragment;
import com.code.mvvm.config.Constants;
import com.code.mvvm.core.data.pojo.article.ArticleVo;
import com.code.mvvm.core.vm.ArticleViewModel;
import com.code.mvvm.util.AdapterPool;
import com.trecyclerview.multitype.MultiTypeAdapter;

/**
 * @author：zhangtianqiu on 18/7/2 14:40
 */
public class ArticleListFragment extends BaseListFragment<ArticleViewModel> {

    public static ArticleListFragment newInstance() {
        return new ArticleListFragment();
    }

    @Override
    public void initView(Bundle state) {
        super.initView(state);
    }

    @Override
    protected void dataObserver() {
        mViewModel.getArticleList().observe(this, new Observer<ArticleVo>() {
            @Override
            public void onChanged(@Nullable ArticleVo articleVo) {
                if (articleVo != null) {
                    lastId = articleVo.data.list.get(articleVo.data.list.size() - 1).newsid;
                    setData(articleVo.data.list);
                }
            }
        });
    }

    @Override
    protected void onStateRefresh() {
        super.onStateRefresh();
        getRemoteData();
    }

    @Override
    protected RecyclerView.LayoutManager createLayoutManager() {
        return new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
    }

    @Override
    protected MultiTypeAdapter createAdapter() {
        return AdapterPool.newInstance().getArticleAdapter(getActivity());
    }

    @Override
    protected void lazyLoad() {
        super.lazyLoad();
        getRemoteData();
    }

    @Override
    public void onRefresh() {
        super.onRefresh();
        getRemoteData();
    }

    @Override
    protected void getRemoteData() {
        mViewModel.getArticleList(getArguments().getString("type_id"), lastId, Constants.PAGE_RN);
    }
}