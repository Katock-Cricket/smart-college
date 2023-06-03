/*
 * Copyright (C) 2019 xuexiangjys(xuexiangjys@163.com)
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

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xpage.enums.CoreAnim;
import com.xuexiang.xpage.model.PageInfo;
import com.xuexiang.xui.utils.WidgetUtils;
import com.xuexiang.xui.utils.XToastUtils;
import com.xuexiang.xui.widget.alpha.XUIAlphaButton;
import com.xuexiang.xui.widget.dialog.DialogLoader;
import com.xuexiang.xuidemo.DemoDataProvider;
import com.xuexiang.xuidemo.R;
import com.xuexiang.xuidemo.adapter.CommonGridAdapter;
import com.xuexiang.xuidemo.base.BaseFragment;
import com.xuexiang.xuidemo.base.BaseHomeFragment;

import java.util.List;

import butterknife.BindView;

/**
 * @author xuexiang
 * @since 2018/12/19 下午1:33
 */
@Page(name = "智慧课堂", anim = CoreAnim.fade)
public class AlphaViewFragment extends BaseFragment implements View.OnClickListener {
    @BindView(R.id.class_ops)
    RecyclerView recyclerHead;
    private CommonGridAdapter mGridAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_alpha_view;
    }

    @Override
    protected void initViews() {
        WidgetUtils.initGridRecyclerView(recyclerHead, 4, 0);
        recyclerHead.setAdapter(mGridAdapter = new CommonGridAdapter(true));
        mGridAdapter.refresh(DemoDataProvider.getGridItems(getContext()));
    }

    @Override
    protected void initListeners() {
        super.initListeners();
        mGridAdapter.setOnItemClickListener((itemView, item, position) -> XToastUtils.toast("点击了：" + item.getTitle()));
        View view = findViewById(R.id.btn_sign);
        view.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view instanceof XUIAlphaButton){
            System.out.println("click");
            DialogLoader.getInstance().showTipDialog(
                    getContext(),
                    getString(R.string.tip_warning),
                    getString(R.string.content_fail_confirm_dialog),
                    getString(R.string.lab_submit));
        }
    }
}
