package com.crm.firstapplication.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;

import com.crm.firstapplication.ContentFragment;
import com.crm.firstapplication.fragment.MainFragment;
import com.crm.firstapplication.R;
import com.crm.firstapplication.RxBus;
import com.crm.firstapplication.events.Events;
import com.crm.firstapplication.presenter.BasePresenter;
import com.crm.firstapplication.presenter.IBasePresenter;
import com.ikimuhendis.ldrawer.ActionBarDrawerToggle;

import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.xutils.ex.DbException;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.io.File;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import qiu.niorgai.StatusBarCompat;

@ContentView(R.layout.activity_main_2)
public class MainActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    @ViewInject(R.id.toolbar)
    private Toolbar toolbar;

    @ViewInject(R.id.tab_layout)
    private TabLayout tabLayout;

    @ViewInject(R.id.viewpager)
    private ViewPager viewPager;

    private PagerAdapter adapter;

    @ViewInject(R.id.dl_left)
    private DrawerLayout mDrawerLayout;

    private ActionBarDrawerToggle mDrawerToggle;

    @ViewInject(R.id.lv_left_menu)
    private ListView lvLeftMenu;
    private String[] lvs = {"List Item 01", "List Item 02", "List Item 03", "List Item 04"};
    private ArrayAdapter arrayAdapter;

    private ShareActionProvider mShareActionProvider;

    @Override
    protected void rxBusEventProcess(@NonNull Events events) throws Exception {
        Toast.makeText(MainActivity.this,"提交文本："+events.getContent().toString(),Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void initCreate(@Nullable Bundle savedInstanceState) throws DbException {

        // 允许标题栏完全出屏幕
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        // 改变状态栏颜色
        StatusBarCompat.setStatusBarColor(this, Color.parseColor("#99f5c0"), 0);

        //app logo
        toolbar.setLogo(R.mipmap.ic_launcher);
        //title
        toolbar.setTitle("  Material Design ToolBar");
        //sub title
        toolbar.setSubtitle("  ToolBar subtitle");
        //以上3个属性必须在setSupportActionBar(toolbar)之前调用
        setSupportActionBar(toolbar);
        //设置导航Icon，必须在setSupportActionBar(toolbar)之后设置
        toolbar.setNavigationIcon(R.mipmap.ic_launcher);
        //添加菜单点击事件
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(toolbar, "Click setNavigationIcon", Snackbar.LENGTH_SHORT).show();
            }
        });

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()){
//                case R.id.action_search://因为使用android.support.v7.widget.SearchView类，可以在
//                    onCreateOptionsMenu(Menu menu)中直接设置监听事件
//                    Snackbar.make(toolbar,"Click Search",Snackbar.LENGTH_SHORT).show();
//                    break;
                    case R.id.action_share:
                        Snackbar.make(toolbar,"Click Share",Snackbar.LENGTH_SHORT).show();
                        break;
                    case R.id.action_more:
                        Snackbar.make(toolbar,"Click More",Snackbar.LENGTH_SHORT).show();
                        break;
                }
                return true;
            }
        });

        tabLayout.addTab(tabLayout.newTab().setText("Tab 1"));
        tabLayout.addTab(tabLayout.newTab().setText("Tab 2"));
        tabLayout.addTab(tabLayout.newTab().setText("Tab 3"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        adapter = new PagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        // 侧滑菜单
        //toolbar.setTitle("Toolbar");//设置Toolbar标题
        //toolbar.setTitleTextColor(Color.parseColor("#ffffff")); //设置标题颜色
        //setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.mipmap.ic_launcher, R.string.drawer_open, R.string.drawer_close) {

            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
            }
        };

        mDrawerToggle.syncState();
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        //设置菜单列表
        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, lvs);
        lvLeftMenu.setAdapter(arrayAdapter);
        lvLeftMenu.setOnItemClickListener(this);

        //禁止手势滑动
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        //打开手势滑动
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);

        mDrawerLayout.openDrawer(GravityCompat.END);
    }

    @Override
    protected IBasePresenter getP() {
        return new BasePresenter<>(this);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        //将ActionBarDrawerToggle与DrawerLayout同步起来
        //将ActionBarDrawerToggle中的Drawer图标设置为ActionBar里的Home_Button的图标
        mDrawerToggle.syncState();

        super.onPostCreate(savedInstanceState);
    }

    //这个方法用来监测手机状态的变化，比如横屏竖屏的切换
    @Override
    public void onConfigurationChanged(Configuration newConfig) {

        mDrawerToggle.onConfigurationChanged(newConfig);
        super.onConfigurationChanged(newConfig);
    }

    /**
     * 该方法是用来加载菜单布局的
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //加载菜单文件
        getMenuInflater().inflate(R.menu.menu_main, menu);

        // search
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                Toast.makeText(MainActivity.this,"提交文本："+s,Toast.LENGTH_SHORT).show();
                return false;
            }
            @Override
            public boolean onQueryTextChange(String s) {
                Toast.makeText(MainActivity.this,"当前文本："+s,Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        MenuItemCompat.setOnActionExpandListener(menu.findItem(R.id.action_search),new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                Toast.makeText(MainActivity.this,"actionView展开了！",Toast.LENGTH_SHORT).show();
                return true;
            }
            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                Toast.makeText(MainActivity.this,"actionView关闭了！",Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        // Locate MenuItem with ShareActionProvider
        MenuItem item = menu.findItem(R.id.action_share);


        // Fetch and store ShareActionProvider
        // mShareActionProvider = (ShareActionProvider) item.getActionProvider();
        mShareActionProvider = (ShareActionProvider)MenuItemCompat.getActionProvider(item);
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        File es = Environment.getExternalStorageDirectory();
        String path = es.getPath();
        sendIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(path+"/Pictures/145.jpg"));
        sendIntent.setType("image/*");

        if(mShareActionProvider!=null)
        {
            mShareActionProvider.setShareIntent(sendIntent);
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//        String msg = "";
//        switch (id) {
//            case R.id.action_search:
//                msg = "action_search";
//                break;
////            case R.id.action_intent:
////                msg = "action_intent";
////                break;
////            case R.id.action_settings:
////                msg = "action_settings";
////                break;
//            default:
//                break;
//        }
//        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        //将ActionBar上的图标与Drawer结合起来
        if(mDrawerToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 单击事件
     * type默认View.OnClickListener.class，故此处可以简化不写，@Event(R.id.bt_main)
     */
    @Event(type = View.OnClickListener.class,value = R.id.tab_layout)
    private void testInjectOnClick(View v){
        Snackbar.make(v,"OnClickListener",Snackbar.LENGTH_SHORT).show();
    }

    /**
     * 长按事件
     */
    @Event(type = View.OnLongClickListener.class,value = R.id.tab_layout)
    private boolean testOnLongClickListener(View v){
        Snackbar.make(v,"testOnLongClickListener",Snackbar.LENGTH_SHORT).show();
        return true;
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                            long arg3) {
        // 动态插入一个Fragment到FrameLayout当中
        ContentFragment contentFragment = new ContentFragment();
        Bundle args = new Bundle();
        args.putString("text", lvs[position]);
        contentFragment.setArguments(args);

//        android.app.FragmentManager fm = getFragmentManager();
//        fm.beginTransaction().replace(R.id.iv_layout_main, contentFragment)
//                .commit();

        mDrawerLayout.closeDrawer(lvLeftMenu);
    }

    class PagerAdapter extends FragmentStatePagerAdapter {
        int numOfTabs;
        public PagerAdapter(FragmentManager fm, int numOfTabs) {
            super(fm);
            this.numOfTabs = numOfTabs;
        }
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new MainFragment();
                case 1:
                    return new MainFragment();
                case 2:
                    return new Fragment();
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return numOfTabs;
        }
    }

}
