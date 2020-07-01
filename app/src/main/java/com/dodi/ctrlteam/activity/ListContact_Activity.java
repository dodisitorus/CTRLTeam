package com.dodi.ctrlteam.activity;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.dodi.ctrlteam.R;
import com.dodi.ctrlteam.activity.fragment.AllContactFragment;
import com.dodi.ctrlteam.activity.fragment.InviteReceiveFragment;
import com.dodi.ctrlteam.activity.fragment.InviteSentFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ListContact_Activity extends AppCompatActivity {

    @BindView(R.id.viewpager)
    ViewPager viewPager;
    @BindView(R.id.tabs)
    TabLayout tabLayout;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.floatingActionButton)
    FloatingActionButton floatingActionCreateNew;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_contact_);

        ButterKnife.bind(this);

        // set viewPager
        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);

        // toolbar init
        onSetToolbar();

        // floating button action
        floatingActionCreateNew.setOnClickListener(view1 -> onInviteContact());
    }

    private void onSetToolbar() {
        toolbar.setTitle(R.string.list_contact);
        toolbar.setNavigationOnClickListener(v -> finish());

        // set option on room chat
        toolbar.inflateMenu(R.menu.main_menu_contact);
        toolbar.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.form_invite_user:

                    return true;
                default:
                    return false;
            }
        });

        Menu menu = toolbar.getMenu();
        MenuItem mSearch = menu.findItem(R.id.action_search);
        SearchView mSearchView = (SearchView) mSearch.getActionView();
        mSearchView.setQueryHint("Search");
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                return false;
            }
        });
    }

    private void setupViewPager(ViewPager viewPager) {
        ListContact_Activity.ViewPagerAdapter adapter = new ListContact_Activity.ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new AllContactFragment(), "My Network");
        adapter.addFragment(new InviteReceiveFragment(), "Receive Invitation");
        adapter.addFragment(new InviteSentFragment(), "Sent Invitation");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    private void onInviteContact() {
        Intent intent = new Intent(ListContact_Activity.this, SearchActivity.class);
        intent.putExtra("invite", true);
        startActivity(intent);
    }
}
