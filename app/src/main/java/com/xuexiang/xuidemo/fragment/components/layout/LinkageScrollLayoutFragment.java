/*
 * Copyright (C) 2020 xuexiangjys(xuexiangjys@163.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.xuexiang.xuidemo.fragment.components.layout;

import android.annotation.SuppressLint;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xpage.config.AppPageConfig;
import com.xuexiang.xpage.enums.CoreAnim;
import com.xuexiang.xpage.model.PageInfo;
import com.xuexiang.xui.utils.WidgetUtils;
import com.xuexiang.xui.widget.layout.linkage.view.LinkageRecyclerView;
import com.xuexiang.xuidemo.DemoDataProvider;
import com.xuexiang.xuidemo.R;
import com.xuexiang.xuidemo.adapter.DeviceAdapter;
import com.xuexiang.xuidemo.base.BaseHomeFragment;

import org.json.JSONObject;

import okhttp3.*;

import java.io.IOException;
import java.util.List;

import butterknife.BindView;

/**
 * @author xuexiang
 * @since 2020/3/11 7:32 PM
 */
@Page(name = "智慧教室", anim= CoreAnim.fade)
public class LinkageScrollLayoutFragment extends BaseHomeFragment {

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.recyclerView)
    LinkageRecyclerView recyclerView;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;

    private DeviceAdapter deviceAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_linkage_scroll_layout;
    }

    @Override
    protected void initViews() {

        WidgetUtils.initRecyclerView(recyclerView, 0);
        recyclerView.setAdapter(deviceAdapter = new DeviceAdapter());

    }

    @Override
    protected List<PageInfo> getPageContents() {
        return AppPageConfig.getInstance().getComponents();
    }

    @Override
    protected void initListeners() {
        //下拉刷新
        refreshLayout.setOnRefreshListener(refreshLayout -> refreshLayout.getLayout().postDelayed(() -> {
            deviceAdapter.refresh(DemoDataProvider.getDeviceData());
            refreshLayout.finishRefresh();
        }, 500));


        refreshLayout.autoRefresh();

        deviceAdapter.setOnItemClickListener((itemView, item, position) -> {
            //click
        });
    }
}
