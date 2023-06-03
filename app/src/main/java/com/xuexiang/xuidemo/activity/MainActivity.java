package com.xuexiang.xuidemo.activity;

import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.tabs.TabLayout;
import com.umeng.analytics.MobclickAgent;
import com.xuexiang.xui.utils.ResUtils;
import com.xuexiang.xui.utils.ThemeUtils;
import com.xuexiang.xui.utils.ViewUtils;
import com.xuexiang.xui.utils.WidgetUtils;
import com.xuexiang.xui.utils.XToastUtils;
import com.xuexiang.xui.widget.dialog.DialogLoader;
import com.xuexiang.xuidemo.R;
import com.xuexiang.xuidemo.adapter.menu.DrawerAdapter;
import com.xuexiang.xuidemo.adapter.menu.DrawerItem;
import com.xuexiang.xuidemo.adapter.menu.SimpleItem;
import com.xuexiang.xuidemo.adapter.menu.SpaceItem;
import com.xuexiang.xuidemo.base.BaseActivity;
import com.xuexiang.xuidemo.fragment.components.layout.AlphaViewFragment;
import com.xuexiang.xuidemo.fragment.components.layout.LinkageScrollLayoutFragment;
import com.xuexiang.xuidemo.fragment.other.QRCodeFragment;
import com.xuexiang.xuidemo.fragment.other.SettingFragment;
import com.xuexiang.xuidemo.utils.SettingSPUtils;
import com.xuexiang.xuidemo.utils.TokenUtils;
import com.xuexiang.xuidemo.utils.Utils;
import com.xuexiang.xuidemo.widget.GuideTipsDialog;
import com.xuexiang.xutil.common.ClickUtils;
import com.xuexiang.xutil.system.DeviceUtils;
import com.yarolegovich.slidingrootnav.SlideGravity;
import com.yarolegovich.slidingrootnav.SlidingRootNav;
import com.yarolegovich.slidingrootnav.SlidingRootNavBuilder;
import com.yarolegovich.slidingrootnav.callback.DragStateListener;

import java.util.Arrays;

import butterknife.BindView;

/**
 * 项目主页面
 *
 * @author xuexiang
 * @since 2018/11/13 下午5:20
 */
public class MainActivity extends BaseActivity implements DrawerAdapter.OnItemSelectedListener, ClickUtils.OnClick2ExitListener {
    private static final int POS_COMPONENTS = 0;
    private static final int POS_UTILITIES = 1;
    private static final int POS_LOGOUT = 2;

    @BindView(R.id.tabs)
    TabLayout mTabLayout;

    private SlidingRootNav mSlidingRootNav;
    private LinearLayout mLLMenu;
    private String[] mMenuTitles;
    private Drawable[] mMenuIcons;
    private DrawerAdapter mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //登记一下
        MobclickAgent.onProfileSignIn(DeviceUtils.getAndroidID());

        initData();

        initViews();

        initSlidingMenu(savedInstanceState);

        initOthers();
    }
    private void initData() {
        mMenuTitles = ResUtils.getStringArray(this, R.array.menu_titles);
        mMenuIcons = ResUtils.getDrawableArray(this, R.array.menu_icons);
    }

    @Override
    protected boolean isSupportSlideBack() {
        return false;
    }

    private void initViews() {
        WidgetUtils.clearActivityBackground(this);
        initTab();
    }

    /**
     * 初始化Tab
     */
    private void initTab() {
        WidgetUtils.addTabWithoutRipple(mTabLayout, "教室", SettingSPUtils.getInstance().isUseCustomTheme() ? R.drawable.custom_selector_icon_tabbar_component : R.drawable.selector_icon_tabbar_component);
        WidgetUtils.addTabWithoutRipple(mTabLayout, "课堂", SettingSPUtils.getInstance().isUseCustomTheme() ? R.drawable.custom_selector_icon_tabbar_util : R.drawable.selector_icon_tabbar_util);
        WidgetUtils.setTabLayoutTextFont(mTabLayout);

        switchPage(LinkageScrollLayoutFragment.class);

        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mAdapter.setSelected(tab.getPosition());
                switch (tab.getPosition()) {
                    case POS_COMPONENTS:
                        switchPage(LinkageScrollLayoutFragment.class);
                        break;
                    case 1:
                        switchPage(AlphaViewFragment.class);
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }


    private void initOthers() {
        GuideTipsDialog.showTips(this);
        //静默检查版本更新
        Utils.checkUpdate(this, false);
    }


    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        initSlidingMenu(null);
    }

    public void openMenu() {
        if (mSlidingRootNav != null) {
            mSlidingRootNav.openMenu();
        }
    }

    public void closeMenu() {
        if (mSlidingRootNav != null) {
            mSlidingRootNav.closeMenu();
        }
    }

    public boolean isMenuOpen() {
        if (mSlidingRootNav != null) {
            return mSlidingRootNav.isMenuOpened();
        }
        return false;
    }

    private void initSlidingMenu(Bundle savedInstanceState) {
        mSlidingRootNav = new SlidingRootNavBuilder(this)
                .withGravity(ResUtils.isRtl(this) ? SlideGravity.RIGHT : SlideGravity.LEFT)
                .withMenuOpened(false)
                .withContentClickableWhenMenuOpened(false)
                .withSavedState(savedInstanceState)
                .withMenuLayout(R.layout.menu_left_drawer)
                .inject();

        mLLMenu = mSlidingRootNav.getLayout().findViewById(R.id.ll_menu);
        final AppCompatImageView ivQrcode = mSlidingRootNav.getLayout().findViewById(R.id.iv_qrcode);
        ivQrcode.setOnClickListener(v -> openNewPage(QRCodeFragment.class));

        final AppCompatImageView ivSetting = mSlidingRootNav.getLayout().findViewById(R.id.iv_setting);
        ivSetting.setOnClickListener(v -> openNewPage(SettingFragment.class));
        ViewUtils.setVisibility(mLLMenu, false);

        mAdapter = new DrawerAdapter(Arrays.asList(
                createItemFor(POS_COMPONENTS).setChecked(true),
                createItemFor(POS_UTILITIES),
                new SpaceItem(48),
                createItemFor(POS_LOGOUT)));
        mAdapter.setListener(this);

        RecyclerView list = findViewById(R.id.list);
        list.setNestedScrollingEnabled(false);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.setAdapter(mAdapter);

        mAdapter.setSelected(POS_COMPONENTS);
        mSlidingRootNav.setMenuLocked(false);
        mSlidingRootNav.getLayout().addDragStateListener(new DragStateListener() {
            @Override
            public void onDragStart() {
                ViewUtils.setVisibility(mLLMenu, true);
            }

            @Override
            public void onDragEnd(boolean isMenuOpened) {
                ViewUtils.setVisibility(mLLMenu, isMenuOpened);
            }
        });
    }

    @Override
    public void onItemSelected(int position) {
        switch (position) {
            case POS_COMPONENTS:
            case POS_UTILITIES:
                if (mTabLayout != null) {
                    TabLayout.Tab tab = mTabLayout.getTabAt(position);
                    if (tab != null) {
                        tab.select();
                    }
                }
                mSlidingRootNav.closeMenu();
                break;
            case POS_LOGOUT:
                DialogLoader.getInstance().showConfirmDialog(
                        this,
                        getString(R.string.lab_logout_confirm),
                        getString(R.string.lab_yes),
                        (dialog, which) -> {
                            dialog.dismiss();
                            TokenUtils.handleLogoutSuccess();
                            finish();
                        },
                        getString(R.string.lab_no),
                        (dialog, which) -> dialog.dismiss()
                );
                break;
            default:
                break;
        }
    }

    private DrawerItem createItemFor(int position) {
        return new SimpleItem(mMenuIcons[position], mMenuTitles[position])
                .withIconTint(ThemeUtils.resolveColor(this, R.attr.xui_config_color_content_text))
                .withTextTint(ThemeUtils.resolveColor(this, R.attr.xui_config_color_content_text))
                .withSelectedIconTint(ThemeUtils.getMainThemeColor(this))
                .withSelectedTextTint(ThemeUtils.getMainThemeColor(this));
    }

    /**
     * 菜单、返回键响应
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (isMenuOpen()) {
                closeMenu();
            } else {
                ClickUtils.exitBy2Click(2000, this);
            }
        }
        return true;
    }

    /**
     * 再点击一次
     */
    @Override
    public void onRetry() {
        XToastUtils.toast("再按一次退出程序");
    }

    /**
     * 退出
     */
    @Override
    public void onExit() {
        moveTaskToBack(true);
    }
}
