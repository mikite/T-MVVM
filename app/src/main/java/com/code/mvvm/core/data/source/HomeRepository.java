package com.code.mvvm.core.data.source;


import com.code.mvvm.core.data.BaseRepository;
import com.code.mvvm.core.data.pojo.banner.BannerListVo;
import com.code.mvvm.core.data.pojo.home.HomeListVo;
import com.code.mvvm.core.data.pojo.home.HomeMergeVo;
import com.code.mvvm.network.rx.RxSubscriber;
import com.code.mvvm.util.StringUtil;
import com.mvvm.http.rx.RxSchedulers;
import com.mvvm.stateview.StateConstants;

import io.reactivex.Flowable;

/**
 * @author：tqzhang on 18/7/26 16:18
 */
public class HomeRepository extends BaseRepository {

    public static String EVENT_KEY_HOME = null;

    private Flowable<HomeListVo> mHomeListObservable;

    private Flowable<BannerListVo> mBannerObservable;

    private final HomeMergeVo homeMergeVo = new HomeMergeVo();

    public HomeRepository() {
        if (EVENT_KEY_HOME == null) {
            EVENT_KEY_HOME = StringUtil.getEventKey();
        }
    }

    //构造获取主页数据的Observable
    public void loadHomeData(String id) {
        mHomeListObservable = apiService.getHomeData(id);
    }

    //构造获取banner数据的Observable
    public void loadBannerData(String posType,
                               String fCatalogId,
                               String sCatalogId,
                               String tCatalogId,
                               String province) {
        mBannerObservable = apiService.getBannerData(posType, fCatalogId, sCatalogId, tCatalogId, province);
    }

    //组装home和banner的observable，添加Disposable
    public void loadHomeData() {
        addDisposable(Flowable.concat(mBannerObservable, mHomeListObservable)
                .compose(RxSchedulers.io_main())//支持背压的线程调度
                .subscribeWith(new RxSubscriber<Object>() {
                    @Override
                    protected void onNoNetWork() {
                        postState(StateConstants.NET_WORK_STATE);
                    }

                    @Override
                    public void onSuccess(Object object) {
                        if (object instanceof BannerListVo) {
                            homeMergeVo.bannerListVo = (BannerListVo) object;
                        } else if (object instanceof HomeListVo) {
                            homeMergeVo.homeListVo = (HomeListVo) object;
                            if (homeMergeVo != null) {
                                //发送数据到EVENT_KEY_HOME
                                postData(EVENT_KEY_HOME, homeMergeVo);
                                postState(StateConstants.SUCCESS_STATE);
                            } else {
                                postState(StateConstants.NOT_DATA_STATE);
                            }
                        }

                    }

                    @Override
                    public void onFailure(String msg, int code) {
                        postState(StateConstants.ERROR_STATE);
                    }
                }));

    }

}
