package com.nl.develop.mvp;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.nl.develop.widgets.LoadingDialog;

/**
 * Created by NiuLei on 2018/1/29.
 * MVP activity基类
 */

public class MvpActivity<P extends MvpContract.IPresenter> extends AppCompatActivity implements MvpContract.IView<P> {
    protected P presenter;
    private LoadingDialog loadingDialog;
    private boolean isFirst;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (presenter != null) {
            presenter.onCreate(savedInstanceState);
        }

    }

    /**
     * loading取消事件
     */
    DialogInterface.OnCancelListener onCancelListener = new DialogInterface.OnCancelListener() {
        @Override
        public void onCancel(DialogInterface dialog) {
            if (presenter != null) {
                presenter.cancelRequest();
            }
        }
    };

    @Override
    protected void onDestroy() {
        if (loadingDialog != null) {
            loadingDialog.setOnCancelListener(null);
            loadingDialog.dismiss();
        }

        super.onDestroy();
        if (presenter != null) {
            presenter.onDestroy();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (presenter != null) {
            presenter.onStart();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (presenter != null) {
            presenter.onStop();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (presenter != null) {
            presenter.onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (presenter != null) {
            presenter.onPause();
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus && !isFirst) {
            isFirst = true;
            if (presenter != null) {
                presenter.onUiVisible();
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (presenter == null) {
            super.onBackPressed();
        } else {
            if (presenter.onBackPressed()) {
                super.onBackPressed();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (presenter != null) {
            presenter.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (presenter != null) {
            presenter.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public P getPresenter() {
        return presenter;
    }

    @Override
    public void setPresenter(P presenter) {
        this.presenter = presenter;
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public void showToast(int resId) {
        showToast(getString(resId));
    }

    @Override
    public void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void startProgress(int resId) {
        if (loadingDialog == null) {
            loadingDialog = new LoadingDialog(this);
            loadingDialog.setOnCancelListener(onCancelListener);
        }

        if (loadingDialog != null && !isFinishing() && !loadingDialog.isShowing()) {
            loadingDialog.show(getString(resId));
        }
    }


    @Override
    public void stopProgress() {
        if (loadingDialog != null && !isFinishing()) {
            loadingDialog.dismiss();
        }
    }
}
