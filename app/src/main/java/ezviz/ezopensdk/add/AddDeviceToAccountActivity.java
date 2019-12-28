package ezviz.ezopensdk.add;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.videogo.RootActivity;
import com.videogo.exception.BaseException;
import com.videogo.exception.ErrorCode;
import com.videogo.openapi.EZOpenSDK;
import com.videogo.ui.util.ActivityUtils;

import ezviz.ezopensdk.R;
import ezviz.ezopensdkcommon.common.IntentConstants;
import ezviz.ezopensdkcommon.common.RouteNavigator;

@Route(path = RouteNavigator.ADD_DEVICE_PAGE)
public class AddDeviceToAccountActivity extends RootActivity {

    private View mAddUi;
    private View mFailUi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_device_to_acount);
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        initUi();
        tryToAddDevice();
        showAddUi();
    }

    public void onClickRetryAddDevice(View view) {
        tryToAddDevice();
        showAddUi();
    }

    public void onClickGiveUpAddDevice(View view) {
        finishAdd(false);
    }

    private void initUi() {
        mAddUi = findViewById(R.id.app_progress_bar_add_device);
        mFailUi = findViewById(R.id.app_vg_failed_to_add_device);
    }

    private void tryToAddDevice(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String serial = getIntent().getStringExtra(IntentConstants.DEVICE_SERIAL);
                String verifyCode = getIntent().getStringExtra(IntentConstants.DEVICE_VERIFY_CODE);
                boolean isAddSuc = false;
                try {
                    EZOpenSDK.getInstance().addDevice(serial, verifyCode);
                    isAddSuc = true;
                } catch (BaseException e) {
                    e.printStackTrace();
                    if (e.getErrorCode() == ErrorCode.ERROR_WEB_DEVICE_ADD_RESULT_ADDED_BY_CURRENT_ACCOUNT){
                        isAddSuc = true;
                    }
                }
                if (isAddSuc){
                    finishAdd(true);
                }else{
                    showFailUi();
                }
            }
        }).start();
    }

    private void showAddUi(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mAddUi.setVisibility(View.VISIBLE);
                mFailUi.setVisibility(View.GONE);
            }
        });
    }

    private void finishAdd(boolean isSucToAdd){
        if (isSucToAdd){
            showToast(getString(R.string.app_add_device_success));
        }
        ActivityUtils.goToMainPage(this);
        finish();
    }

    private void showFailUi(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mAddUi.setVisibility(View.GONE);
                mFailUi.setVisibility(View.VISIBLE);
            }
        });
    }

}
