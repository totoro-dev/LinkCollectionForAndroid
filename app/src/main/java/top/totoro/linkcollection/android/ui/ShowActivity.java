package top.totoro.linkcollection.android.ui;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

import search.Search;
import top.totoro.linkcollection.android.R;
import top.totoro.linkcollection.android.adapter.ShowPagerAdapter;
import top.totoro.linkcollection.android.base.BaseActivity;
import top.totoro.linkcollection.android.fragment.MeFragment;
import top.totoro.linkcollection.android.fragment.PushFragment;
import top.totoro.linkcollection.android.fragment.ServiceFragment;
import top.totoro.linkcollection.android.util.FindView;
import top.totoro.linkcollection.android.util.Logger;

/**
 * Create by HLM on 2020-02-14
 */
public class ShowActivity extends BaseActivity implements ViewPager.OnPageChangeListener, View.OnClickListener {

    private int currPosition = 0;

    private List<Fragment> fragments = new LinkedList<>();
    private List<TextView> tables = new LinkedList<>();
    private SearchView svSearch;
    private ViewPager vpContainer;
    private TextView tvMe;
    private TextView tvPush;
    private TextView tvService;
    private FindView find;

    private MeFragment meFragment;
    private PushFragment pushFragment;
    private ServiceFragment serviceFragment;
    private ShowPagerAdapter showPagerAdapter;

    private static String localSearchKey = "";
    private static String serviceSearchKey = "";

    @Override
    protected void onStart() {
        Logger.d(this, "onStart()");
        super.onStart();
        vpContainer = find.View(ViewPager.class, R.id.show_container);
        tvMe = find.TextView(R.id.table_me);
        tvPush = find.TextView(R.id.table_push);
        tvService = find.TextView(R.id.table_service);
        fragments.clear();
        tables.clear();
        meFragment = new MeFragment();
        pushFragment = new PushFragment();
        serviceFragment = new ServiceFragment();
        showPagerAdapter = new ShowPagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.POSITION_UNCHANGED, fragments);
        fragments.add(meFragment);
        fragments.add(pushFragment);
        fragments.add(serviceFragment);
        tables.add(tvMe);
        tables.add(tvPush);
        tables.add(tvService);
        tvMe.setOnClickListener(this);
        tvPush.setOnClickListener(this);
        tvService.setOnClickListener(this);
        vpContainer.setAdapter(showPagerAdapter);
        vpContainer.addOnPageChangeListener(this);
        vpContainer.setOffscreenPageLimit(3); // 设置ViewPager切换时，不刷新页面内容
    }

    @Override
    protected void onResume() {
        Logger.d(this, "onResume()");
        super.onResume();
        tables.get(currPosition).setTextColor(getResources().getColor(R.color.colorPrimary));
        vpContainer.setCurrentItem(currPosition, true);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Logger.d(this, "onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);
        find = new FindView(findViewById(R.id.show_layout));
        setSupportActionBar(find.View(Toolbar.class, R.id.show_toolbar));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Logger.d(this, "onCreateOptionsMenu()");
        getMenuInflater().inflate(R.menu.menu_tool_bar, menu);
        MenuItem search = menu.findItem(R.id.search);
        svSearch = (SearchView) MenuItemCompat.getActionView(search);
        setSearchViewHint();
        //搜索框监听
        svSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                Logger.d(ShowActivity.this, "TextSubmit : " + s);
                if (currPosition == 0) {
                    meFragment.refreshData(Search.searchInLocal(s));
                } else if (currPosition == 2) {
                    serviceFragment.refreshData(Search.searchInService(s));
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if (currPosition == 0) {
                    localSearchKey = s;
                } else if (currPosition == 2) {
                    serviceSearchKey = s;
                }
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    // 让菜单同时显示图标和文字
    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        Logger.d(this, "onMenuOpened()");
        if (menu != null) {
            if (menu.getClass().getSimpleName().equalsIgnoreCase("MenuBuilder")) {
                try {
                    Method method = menu.getClass().getDeclaredMethod("setOptionalIconsVisible", Boolean.TYPE);
                    method.setAccessible(true);
                    method.invoke(menu, true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return super.onMenuOpened(featureId, menu);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        Logger.d(this, "onPageSelected()");
        tables.get(currPosition).setTextColor(getResources().getColor(R.color.colorBottomTableTextColor));
        tables.get(position).setTextColor(getResources().getColor(R.color.colorPrimary));
        if (currPosition != position) closeSearchView();
        currPosition = position;
        setSearchViewHint(); // 恢复当前页面搜索框的内容
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.table_me:
                vpContainer.setCurrentItem(0, true);
                break;
            case R.id.table_push:
                vpContainer.setCurrentItem(1, true);
                break;
            case R.id.table_service:
                vpContainer.setCurrentItem(2, true);
                break;
        }
        Logger.d(this, "current page item : " + currPosition);
    }

    // 设置搜索框的显示内容
    private void setSearchViewHint() {
        if (currPosition == 0) {
            if (localSearchKey.equals("")) {
                svSearch.setQueryHint(getResources().getString(R.string.search_local));
            } else {
                svSearch.setQuery(localSearchKey, false);
            }
        } else if (currPosition == 1) {
            svSearch.setQuery("", false); // 清空搜索框内容，并显示标示语
            svSearch.setQueryHint(getResources().getString(R.string.search_unable));
        } else if (currPosition == 2) {
            if (serviceSearchKey.equals("")) {
                svSearch.setQueryHint(getResources().getString(R.string.search_service));
            } else {
                svSearch.setQuery(serviceSearchKey, false);
            }
        }
    }

    // 关闭SearchView搜索框
    private void closeSearchView() {
        try {
            String query = svSearch.getQuery().toString();
            svSearch.setQuery("", false); // 需要在搜索框无内容的情况下调用一次才会关闭
            Method method = svSearch.getClass().getDeclaredMethod("onCloseClicked");
            method.setAccessible(true);
            method.invoke(svSearch);
            svSearch.setQuery(query, false); // 恢复搜索框里的内容
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
