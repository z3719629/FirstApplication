package com.crm.firstapplication.activity;

import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.crm.firstapplication.R;
import com.crm.firstapplication.RxBus;
import com.crm.firstapplication.adapter.CrmPagerAdapter;
import com.crm.firstapplication.adapter.RecyclerViewAdapter;
import com.crm.firstapplication.adapter.SimplePaddingDecoration;
import com.crm.firstapplication.contract.CrmMainContract;
import com.crm.firstapplication.databinding.ActivityCrmMainBinding;
import com.crm.firstapplication.events.Events;
import com.crm.firstapplication.model.CrmMain;
import com.ikimuhendis.ldrawer.ActionBarDrawerToggle;

import org.xutils.ex.DbException;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.annotations.NonNull;
import qiu.niorgai.StatusBarCompat;

public class CrmMainActivity extends BaseActivity<CrmMainContract.ICrmMainPresenter> implements CrmMainContract.ICrmMainView, AdapterView.OnItemClickListener {

    private ActivityCrmMainBinding mBinding;

    private String[] lvs = {"List Item 01", "List Item 02", "List Item 03", "List Item 04","List Item 01", "List Item 02", "List Item 03", "List Item 04","List Item 01", "List Item 02", "List Item 03", "List Item 04","List Item 01", "List Item 02", "List Item 03", "List Item 04","List Item 01", "List Item 02", "List Item 03", "List Item 04","List Item 01", "List Item 02", "List Item 03", "List Item 04","List Item 01", "List Item 02", "List Item 03", "List Item 04"};

    private List<String> mDatas;

    @Override
    protected void rxBusEventProcess(@NonNull Events<?> events) throws Exception {
        Toast.makeText(CrmMainActivity.this,"提交文本："+events.getContent().toString(),Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void initCreate(@Nullable Bundle savedInstanceState) throws DbException {
        //setContentView(R.layout.activity_crm_main);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_crm_main);

        CrmMain crmMain = new CrmMain();
        crmMain.name.set("name");

        // 改变状态栏颜色
        StatusBarCompat.setStatusBarColor(this, Color.parseColor("#99f5c0"), 0);

        this.initToolbar();

        this.initTabLayout();

    }

    private void initToolbar() {
        final Toolbar toolbar = mBinding.appBar.toolbar;
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
    }

    private void initTabLayout() {
        TabLayout tabLayout = mBinding.appBar.tabLayout;
        tabLayout.addTab(tabLayout.newTab().setText("Tab 1"));
        tabLayout.addTab(tabLayout.newTab().setText("Tab 2"));
        tabLayout.addTab(tabLayout.newTab().setText("Tab 3"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = mBinding.appBar.viewpager;
        CrmPagerAdapter adapter = new CrmPagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
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

        DrawerLayout mDrawerLayout = mBinding.drawerLayout;
        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.mipmap.ic_launcher, R.string.drawer_open, R.string.drawer_close) {

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
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, lvs);
        RecyclerView recyclerView = mBinding.lvLeftMenu;

        initData();

        List<String> l = new ArrayList();
        for(String s : this.lvs) {
            l.add(s);
        }
        RecyclerViewAdapter recycleAdapter = new RecyclerViewAdapter<String>(l) {

            @Override
            public int getLayoutId(int viewType) {
                return R.layout.recycle_view_item;
            }

            @Override
            public void convert(RecyclerViewAdapter.RecycleViewHolder holder, String data, int position) {
                holder.setText(R.id.id_num, data);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        View view = ((ViewGroup)v).getChildAt(0);
                        Toast.makeText(CrmMainActivity.this, ((TextView)view).getText(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        };

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        //设置布局管理器
        recyclerView.setLayoutManager(layoutManager);
        //设置为垂直布局，这也是默认的
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        //设置分隔线
        //recyclerView.addItemDecoration(new DividerGridItemDecoration(this));
        //设置增加或删除条目的动画
        recyclerView.setItemAnimator( new DefaultItemAnimator());

        recyclerView.setAdapter(recycleAdapter);
        //recyclerView.setOnItemClickListener(this);

        recyclerView.addItemDecoration(new SimplePaddingDecoration(this));

        //禁止手势滑动
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        //打开手势滑动
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);

        mDrawerLayout.openDrawer(GravityCompat.START);
    }

    @Override
    protected CrmMainContract.ICrmMainPresenter getP() {
        return null;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        RxBus.getInstance().send(Events.TAP, view.getId());
    }

    private void initData() {
        mDatas = new ArrayList<String>();
        for ( int i=0; i < 40; i++) {
            mDatas.add( "item"+i);
        }
    }
}
