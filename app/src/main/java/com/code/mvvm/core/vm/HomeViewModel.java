package com.code.mvvm.core.vm;

import android.app.Application;
import android.support.annotation.NonNull;

import com.code.mvvm.core.data.source.HomeRepository;
import com.mvvm.base.AbsViewModel;


/**
 * @author：tqzhang on 18/7/26 16:15
 */
public class HomeViewModel extends AbsViewModel<HomeRepository> {

    public HomeViewModel(@NonNull Application application) {
        super(application);
    }

    private void getHomeListData(String id) {
        mRepository.loadHomeData(id);
    }

    private void getBannerData(String posType,
                               String fCatalogId,
                               String sCatalogId,
                               String tCatalogId,
                               String province) {
        mRepository.loadBannerData(posType, fCatalogId, sCatalogId, tCatalogId, province);

    }

    //获取主页数据
    public void getHomeListData() {
        getBannerData("1", "4", "109", "", null);
        getHomeListData("0");
        //respository获取数据添加Disposable
        mRepository.loadHomeData();
    }

}
