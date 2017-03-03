package com.skr.simple.base;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.skr.simple.App;
import com.skr.simple.R;
import com.skr.simple.common.Constants;
import com.skr.simple.utils.ActivityCollector;
import com.skr.simple.utils.MyUtils;
import com.socks.library.KLog;
import com.squareup.leakcanary.RefWatcher;

import butterknife.ButterKnife;
import rx.Subscription;

/**
 * @author hyw
 * @since 2016/11/24
 */
public abstract class BaseActivity extends AppCompatActivity {
    protected boolean mIsNoTitle = true;
    protected boolean mIsShowBackBtn = true;
    protected boolean mIsShowSettingsBtn = true;

    /**
     * Provide your layout ID
     */
    public abstract int getLayoutId();

    /**
     * Initialize your layout components here
     */
    public abstract void initViews();

    protected Subscription mSubscription;

    protected ProgressDialog mProgressDialog;

    protected void showProgressDialog() {
        if (mProgressDialog == null) {
            initProgressDialog();
        }
        mProgressDialog.show();
    }

    protected void dismissProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }


    private void initProgressDialog() {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage(getString(R.string.loading));
        mProgressDialog.setCancelable(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        KLog.i(getClass().getSimpleName());
        ActivityCollector.addActivity(this);
        setContentView(getLayoutId());
        ButterKnife.bind(this);
        initViews();
        initToolBar();
    }

    private void initToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            if (mIsNoTitle) {
                toolbar.setTitle("");
            }
            if (mIsShowBackBtn) {
                toolbar.setNavigationIcon(R.mipmap.ic_back);
            }
            setSupportActionBar(toolbar);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);

        RefWatcher refWatcher = App.getRefWatcher(this);
        refWatcher.watch(this);

        MyUtils.cancelSubscription(mSubscription);
        MyUtils.fixInputMethodManagerLeak(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (mIsShowSettingsBtn) {
            getMenuInflater().inflate(R.menu.settings, menu);
            return true;
        } else {
            return super.onCreateOptionsMenu(menu);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    finishAfterTransition();
                } else {
                    finish();
                }
                break;
            case R.id.settings:
//                goToActivity(SettingsActivity.class);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    protected void goToActivity(Class targetActivity) {
        Intent intent = new Intent(this, targetActivity);
        startActivity(intent);
    }

    protected void goToActivity(Class targetActivity, Bundle bundle) {
        Intent intent = new Intent(this, targetActivity);
        intent.putExtra(Constants.BUNDLE, bundle);
        startActivity(intent);
    }

    protected void goToActivityForResult(Class targetActivity, Bundle bundle, int requestCode) {
        Intent intent = new Intent(this, targetActivity);
        intent.putExtra(Constants.BUNDLE, bundle);
        startActivityForResult(intent, requestCode);
    }
}

