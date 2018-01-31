package com.crm.onenetcontroller.activity;

import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.chinamobile.iot.onenet.OneNetApi;
import com.chinamobile.iot.onenet.OneNetApiCallback;
import com.crm.mylibrary.activity.base.BaseActivity;
import com.crm.mylibrary.rxbus.Events;
import com.crm.mylibrary.view.ChangeColorIconWithTextView;
import com.crm.onenetcontroller.R;
import com.crm.onenetcontroller.contract.MainContract;
import com.crm.onenetcontroller.databinding.ActivityMainBinding;
import com.crm.onenetcontroller.fragment.MainTabOneFragment;
import com.crm.onenetcontroller.listener.TabItemOnTouchListener;
import com.crm.onenetcontroller.presenter.MainPresenter;

import org.xutils.ex.DbException;

import qiu.niorgai.StatusBarCompat;

public class MainActivity extends BaseActivity<MainContract.IMainPresenter> implements MainContract.IMainView {

    ActivityMainBinding mBinding;

    @Override
    protected void rxBusEventProcess(@io.reactivex.annotations.NonNull Events<?> events) throws Exception {
        if(events.getCode() == Events.EVENT_TAP) {
            Toast.makeText(MainActivity.this, events.getContent().toString(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void initCreate(@Nullable Bundle savedInstanceState) throws DbException {
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        // 改变状态栏颜色
        StatusBarCompat.setStatusBarColor(this, Color.parseColor("#2292e7"), 0);

        initToolBar(mBinding.toolbar);

        initViews(mBinding.contentMain.tabLayout, mBinding.contentMain.viewPager);
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

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected MainContract.IMainPresenter getP() {
        return new MainPresenter(this);
    }

    private void initToolBar(final Toolbar toolbar) {

        //title
        toolbar.setTitle("ONENET");
        //以上3个属性必须在setSupportActionBar(toolbar)之前调用
        setSupportActionBar(toolbar);
        //设置导航Icon，必须在setSupportActionBar(toolbar)之后设置
        //toolbar.setNavigationIcon(R.mipmap.ic_launcher);
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
                    case R.id.action_more:
                        Snackbar.make(toolbar,"Click More",Snackbar.LENGTH_SHORT).show();
                        break;
                }
                return true;
            }
        });
    }

    private void initViews(final TabLayout mTablayout, final ViewPager mViewPager) {

        FragmentPagerAdapter fpa = new FragmentPagerAdapter(getSupportFragmentManager()) {

            private String[] mTitles = new String[]{"菜单一", "菜单一", "菜单一", "菜单一"};

            @Override
            public Fragment getItem(int position) {
                if (position == 1) {
                    return new MainTabOneFragment();
                } else if (position == 2) {
                    return new MainTabOneFragment();
                }else if (position==3){
                    return new MainTabOneFragment();
                }
                return new MainTabOneFragment();
            }

            @Override
            public int getCount() {
                return mTitles.length;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return mTitles[position];
            }

        };
        mViewPager.setAdapter(fpa);

        mViewPager.setCurrentItem(0);
        mViewPager.setOffscreenPageLimit(fpa.getCount());

        mTablayout.setupWithViewPager(mViewPager);


        TabLayout.Tab one = mTablayout.getTabAt(0);
        TabLayout.Tab two = mTablayout.getTabAt(1);
        TabLayout.Tab three = mTablayout.getTabAt(2);
        TabLayout.Tab four = mTablayout.getTabAt(3);

        if (one != null) {
            //one.setIcon(util.getDrawable(R.mipmap.ic_launcher));
            View view= LayoutInflater.from(this).inflate(R.layout.tab_one, null);
            ChangeColorIconWithTextView tv = (ChangeColorIconWithTextView) view.findViewById(R.id.id_indicator_four);
            //tv.setBackgroundResource(R.drawable.tab_select);
            one.setCustomView(tv);
            tv.setIconAlpha(1.0f);

            tv.setOnTouchListener(new TabItemOnTouchListener(this, mViewPager, one));
        }
        if (two != null) {
            View view= LayoutInflater.from(this).inflate(R.layout.tab_one, null);
            ChangeColorIconWithTextView tv = (ChangeColorIconWithTextView) view.findViewById(R.id.id_indicator_four);
            //tv.setBackgroundResource(R.drawable.tab_select);
            two.setCustomView(tv);
            tv.setIconAlpha(0.0f);

            tv.setOnTouchListener(new TabItemOnTouchListener(this, mViewPager, two));
        }
        if (three != null) {
            View view= LayoutInflater.from(this).inflate(R.layout.tab_one, null);
            ChangeColorIconWithTextView tv = (ChangeColorIconWithTextView) view.findViewById(R.id.id_indicator_four);
            //tv.setBackgroundResource(R.drawable.tab_select);
            three.setCustomView(tv);
            tv.setIconAlpha(0.0f);

            tv.setOnTouchListener(new TabItemOnTouchListener(this, mViewPager, three));
        }
        if (four != null) {
            View view= LayoutInflater.from(this).inflate(R.layout.tab_one, null);
            ChangeColorIconWithTextView tv = (ChangeColorIconWithTextView) view.findViewById(R.id.id_indicator_four);
            //tv.setBackgroundResource(R.drawable.tab_select);
            four.setCustomView(tv);
            tv.setIconAlpha(0.0f);

            tv.setOnTouchListener(new TabItemOnTouchListener(this, mViewPager, four));
        }

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (positionOffset > 0)
                {
                    ChangeColorIconWithTextView left = (ChangeColorIconWithTextView)mTablayout.getTabAt(position).getCustomView();
                    ChangeColorIconWithTextView right = (ChangeColorIconWithTextView)mTablayout.getTabAt(position+1).getCustomView();

                    //left.setIconAlpha(1 - positionOffset);
                    //right.setIconAlpha(positionOffset);
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mTablayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                //tab.getCustomView().findViewById(R.id.id_indicator_four).setSelected(true);
                ((ChangeColorIconWithTextView)tab.getCustomView().findViewById(R.id.id_indicator_four)).setIconAlpha(1.0f);
                mViewPager.setCurrentItem(tab.getPosition(), false);
                Log.i("TTTTTTTTTTTTTTTT", "onTabSelected " + tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                //tab.getCustomView().findViewById(R.id.id_indicator_four).setSelected(false);
                ((ChangeColorIconWithTextView)tab.getCustomView().findViewById(R.id.id_indicator_four)).setIconAlpha(0.0f);
                Log.i("TTTTTTTTTTTTTTTT", "onTabUnselected " + tab.getPosition());
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition(), false);
            }
        });

    }
}
