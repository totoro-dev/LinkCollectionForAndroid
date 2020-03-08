package top.totoro.linkcollection.android.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

import entry.CollectionInfo;
import entry.SearchInfo;
import search.Search;
import top.totoro.linkcollection.android.R;
import top.totoro.linkcollection.android.adapter.ShowPagerAdapter;
import top.totoro.linkcollection.android.base.BaseActivity;
import top.totoro.linkcollection.android.dialog.AboutDialog;
import top.totoro.linkcollection.android.dialog.CollectDialog;
import top.totoro.linkcollection.android.dialog.LoadingDialog;
import top.totoro.linkcollection.android.dialog.MyInformationDialog;
import top.totoro.linkcollection.android.fragment.ChooseLovesFragment;
import top.totoro.linkcollection.android.fragment.MeFragment;
import top.totoro.linkcollection.android.fragment.PushFragment;
import top.totoro.linkcollection.android.fragment.ServiceFragment;
import top.totoro.linkcollection.android.util.FindView;
import top.totoro.linkcollection.android.util.Logger;

/**
 * Create by HLM on 2020-02-14
 * 链接收藏的主要显示页面
 */
public class ShowActivity extends BaseActivity implements ViewPager.OnPageChangeListener, View.OnClickListener {

    private static String localSearchKey = ""; // 本地搜索的关键词，用于恢复搜索框的内容
    private static String serviceSearchKey = ""; // 全网搜索的关键词，用于恢复搜索框的内容
    private static int currPosition = 0; // 当前ViewPager显示的是哪个fragment
    private List<Fragment> fragments = new LinkedList<>();
    private List<TextView> tables = new LinkedList<>(); // 底部栏标题，其实也可以用数组
    private Toolbar tbToolBar; // 顶部栏
    private SearchView svSearch; // 搜索框
    private ViewPager vpContainer;
    private TextView tvMe;
    private TextView tvPush;
    private TextView tvService;
    private RelativeLayout rvShowMainLayout;
    private FrameLayout flShowChooseLoveLayout;
    private FindView find;
    private ShowPagerAdapter showPagerAdapter;
    private MeFragment meFragment;
    private PushFragment pushFragment;
    private ServiceFragment serviceFragment;
    private ChooseLovesFragment chooseLovesFragment = new ChooseLovesFragment();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Logger.d(this, "onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);
        find = new FindView(findViewById(R.id.show_layout));
        tbToolBar = find.View(Toolbar.class, R.id.show_toolbar);
        setSupportActionBar(tbToolBar);
    }

    @Override
    protected void onStart() {
        Logger.d(this, "onStart()");
        super.onStart();
        vpContainer = find.View(ViewPager.class, R.id.show_container);
        tvMe = find.TextView(R.id.table_me);
        tvPush = find.TextView(R.id.table_push);
        tvService = find.TextView(R.id.table_service);
        rvShowMainLayout = find.RelativeLayout(R.id.show_main_layout);
        flShowChooseLoveLayout = find.View(FrameLayout.class, R.id.show_choose_love_layout);
        fragments.clear();
        tables.clear();
        meFragment = MeFragment.getInstance();
        pushFragment = PushFragment.getInstance();
        serviceFragment = ServiceFragment.getInstance();
        showPagerAdapter = new ShowPagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.POSITION_UNCHANGED, fragments);
        fragments.add(meFragment);
        fragments.add(pushFragment);
        fragments.add(serviceFragment);
        tables.add(tvMe);
        tables.add(tvPush);
        tables.add(tvService);
        vpContainer.setAdapter(showPagerAdapter);
        tvMe.setOnClickListener(this);
        tvPush.setOnClickListener(this);
        tvService.setOnClickListener(this);
        vpContainer.addOnPageChangeListener(this);
        vpContainer.setOffscreenPageLimit(tables.size()); // 设置ViewPager切换时，不刷新页面内容
    }

    @Override
    protected void onResume() {
        Logger.d(this, "onResume()");
        super.onResume();
        Logger.d(this, "fragment " + currPosition + " isAdded");
        tables.get(currPosition).setTextColor(getResources().getColor(R.color.colorPrimary));
        resetToolBarTitle();
        vpContainer.setCurrentItem(currPosition, true);
        /***** 接收自收藏通知的页面显示 *****/
        Intent intent = getIntent();
        if (intent == null || intent.getStringExtra("link") == null) return;
        String link = intent.getStringExtra("link");
        String title = intent.getStringExtra("title");
        String l1 = intent.getStringExtra("l1");
        String l2 = intent.getStringExtra("l2");
        String l3 = intent.getStringExtra("l3");
        Logger.d(this, link, title, l1, l2, l3);
        CollectDialog.newInstance(null, title, link, l1, l2, l3).show(getSupportFragmentManager(), "collect");
    }

    @Override
    public void onBackPressed() {
        if (currPosition == 0 && meFragment.showingLabels) {
            Logger.d(this, "onBackPressed()");
            meFragment.showCollect();
            return;
        }
        // 如果是显示选择关注的界面就回到主页面
        if (rvShowMainLayout.getVisibility() == View.GONE) {
            finishChooseLoveFragment();
        }
//        super.onBackPressed(); // 无法通过返回键直接退出程序
    }

    /**
     * 进入选择关注的页面
     */
    public void toChooseLoveFragment() {
        rvShowMainLayout.setVisibility(View.GONE);
        flShowChooseLoveLayout.setVisibility(View.VISIBLE);
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        if (chooseLovesFragment.isAdded()) {
            transaction.show(chooseLovesFragment).commit();
        } else {
            transaction.add(R.id.show_choose_love_layout, chooseLovesFragment).commit();
        }
    }

    /**
     * 取消显示选择关注的页面
     */
    public void finishChooseLoveFragment() {
        rvShowMainLayout.setVisibility(View.VISIBLE);
        flShowChooseLoveLayout.setVisibility(View.GONE);
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.hide(chooseLovesFragment).commit();
    }

    /**
     * 显示全部收藏的链接
     */
    public void showAllCollectLinks() {
        if (meFragment.isAdded() && currPosition != 0) {
            vpContainer.setCurrentItem(0, true);
        }
        meFragment.showCollect();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.show_links:
                showAllCollectLinks();
                break;
            case R.id.show_labels:
                meFragment.showLabels();
                break;
//            case R.id.add_item:
//                break;
            case R.id.information:
                MyInformationDialog.getInstance().show(this);
                break;
            case R.id.about:
                AboutDialog.getInstance().show(getSupportFragmentManager(), "about");
                break;
        }
        return super.onOptionsItemSelected(item);
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
                    LinkedList<CollectionInfo> data = Search.searchInLocal(s);
                    if (data.size() <= 0) {
                        Toast.makeText(instance, R.string.search_no_data, Toast.LENGTH_LONG).show();
                    } else {
                        closeSearchView();
                        meFragment.refreshCollectData(data);
                        meFragment.showCollect();
                    }
                } else if (currPosition == 2) {
                    new Thread(() -> { // 开启线程搜索数据
                        LoadingDialog.getInstance().show(getSupportFragmentManager());
                        LinkedList<SearchInfo> data = Search.searchInService(s);
                        runOnUiThread(() -> {
                            if (data.size() <= 0) {
                                Toast.makeText(instance, R.string.search_no_data, Toast.LENGTH_LONG).show();
                            } else {
                                closeSearchView();
                                serviceFragment.refreshData(data);
                            }
                        });
                        LoadingDialog.getInstance().dismiss();
                    }).start();
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

    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        Logger.d(this, "onMenuOpened()");
        if (menu != null) {
            // 让菜单同时显示图标和文字
            if (menu.getClass().getSimpleName().equalsIgnoreCase("MenuBuilder")) {
                ((MenuBuilder) menu).setOptionalIconsVisible(true);
            }
        }
        return super.onMenuOpened(featureId, menu);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        Logger.d(this, "onPageSelected() position = " + position);
        resetBottomBar(position);
        setSearchViewHint(); // 恢复当前页面搜索框的内容
        resetToolBarTitle();
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
    }

    /**
     * 重置底部栏选中状态
     *
     * @param position 当前显示的页面位置
     */
    private void resetBottomBar(int position) {
        tables.get(currPosition).setTextColor(getResources().getColor(R.color.colorBottomTableTextColor));
        tables.get(position).setTextColor(getResources().getColor(R.color.colorPrimary));
        if (currPosition != position) closeSearchView();
        currPosition = position;
    }

    private void resetToolBarTitle() {
        if (currPosition == 0) {
            tbToolBar.setTitle(R.string.tb_local_title);
        } else if (currPosition == 1) {
            tbToolBar.setTitle(R.string.tb_push_title);
            pushFragment.checkLoveInfo();
        } else if (currPosition == 2) {
            tbToolBar.setTitle(R.string.tb_service_title);
        }
    }

    // 设置搜索框的显示内容
    private void setSearchViewHint() {
        if (svSearch == null) return;
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
            if (svSearch == null) return;
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
