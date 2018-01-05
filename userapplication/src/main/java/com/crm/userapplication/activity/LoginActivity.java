package com.crm.userapplication.activity;

import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.design.widget.TabLayout.Tab;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.crm.userapplication.R;
import com.crm.userapplication.activity.base.BaseActivity;
import com.crm.userapplication.contract.LoginContract;
import com.crm.userapplication.databinding.ActivityLoginBinding;
import com.crm.userapplication.fragment.LoginTabOneFragment;
import com.crm.userapplication.presenter.LoginPresenter;
import com.crm.userapplication.rxbus.Events;

import org.xutils.ex.DbException;

import io.reactivex.annotations.NonNull;
import qiu.niorgai.StatusBarCompat;

public class LoginActivity extends BaseActivity<LoginContract.ILoginPresenter> implements LoginContract.ILoginView {

    private ActivityLoginBinding mBinding;

    @Override
    protected void rxBusEventProcess(@NonNull Events<?> events) throws Exception {

    }

    @Override
    protected void initCreate(@Nullable Bundle savedInstanceState) throws DbException {
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        // 改变状态栏颜色
        StatusBarCompat.setStatusBarColor(this, Color.parseColor("#000000"), 0);

        initToolBar(mBinding.toolbar);

        initViews(mBinding.contentLogin.tabLayout, mBinding.contentLogin.viewPager);
    }

    /**
     * 该方法是用来加载菜单布局的
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //加载菜单文件
        getMenuInflater().inflate(R.menu.menu_login, menu);

        // search
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                Toast.makeText(LoginActivity.this,"提交文本："+s,Toast.LENGTH_SHORT).show();
                return false;
            }
            @Override
            public boolean onQueryTextChange(String s) {
                Toast.makeText(LoginActivity.this,"当前文本："+s,Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        MenuItemCompat.setOnActionExpandListener(menu.findItem(R.id.action_search),new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                Toast.makeText(LoginActivity.this,"actionView展开了！",Toast.LENGTH_SHORT).show();
                return true;
            }
            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                Toast.makeText(LoginActivity.this,"actionView关闭了！",Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected LoginContract.ILoginPresenter getP() {
        return new LoginPresenter(this);
    }

    private void initToolBar(final Toolbar toolbar) {

        //title
        toolbar.setTitle("CRM");
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

    private void initViews(TabLayout mTablayout, ViewPager mViewPager) {

        mViewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {

            private String[] mTitles = new String[]{"菜单一", "菜单二", "菜单三", "菜单四"};

            @Override
            public Fragment getItem(int position) {
                if (position == 1) {
                    return new LoginTabOneFragment();
                } else if (position == 2) {
                    return new LoginTabOneFragment();
                }else if (position==3){
                    return new LoginTabOneFragment();
                }
                return new LoginTabOneFragment();
            }

            @Override
            public int getCount() {
                return mTitles.length;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return mTitles[position];
            }

        });

        mTablayout.setupWithViewPager(mViewPager);

        Tab one = mTablayout.getTabAt(0);
        Tab two = mTablayout.getTabAt(1);
        Tab three = mTablayout.getTabAt(2);
        Tab four = mTablayout.getTabAt(3);

        if (one != null) {
            one.setIcon(getResources().getDrawable(R.mipmap.ic_launcher));
        }
        if (two != null) {
            two.setIcon(getResources().getDrawable(R.mipmap.ic_launcher));
        }
        if (three != null) {
            three.setIcon(getResources().getDrawable(R.mipmap.ic_launcher));
        }
        if (four != null) {
            four.setIcon(getResources().getDrawable(R.mipmap.ic_launcher));
        }

    }
}
