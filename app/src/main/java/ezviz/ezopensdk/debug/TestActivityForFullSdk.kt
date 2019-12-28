package ezviz.ezopensdk.debug

import android.os.Bundle
import android.view.View
import com.alibaba.android.arouter.facade.Postcard
import com.alibaba.android.arouter.facade.callback.NavCallback
import com.alibaba.android.arouter.launcher.ARouter
import com.ezviz.sdk.configwifi.common.ConfigWifiTaskManager
import com.ezviz.sdk.configwifi.finder.DeviceFindCallback
import com.videogo.RootActivity
import com.videogo.openapi.EZConstants
import com.videogo.openapi.EZOpenSDK
import com.videogo.openapi.EzvizAPI
import com.videogo.util.LogUtil
import com.videogo.wificonfig.APWifiConfig
import ezviz.ezopensdk.R
import ezviz.ezopensdkcommon.common.IntentConstants
import ezviz.ezopensdkcommon.common.RouteNavigator
import kotlin.concurrent.thread

class TestActivityForFullSdk : RootActivity() {

    val TAG = "@@zhuwen"

    val routerWifiName = "adcd"
    val routerWifiPwd = "12345687"
    val deviceSerial = "C54348757"
    val deviceVerifyCode = "FTFPKL"
    val configWifiPrefix = "EZVIZ_"
    val deviceHotspotSsid = configWifiPrefix + deviceSerial
    val deviceHotspotPwd = configWifiPrefix + deviceVerifyCode

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_for_sdk)
    }

    fun onClickTest(view: View) {
        EZOpenSDK.getInstance().stopAPConfigWifiWithSsid()
        EZOpenSDK.getInstance().startAPConfigWifiWithSsid(routerWifiName, routerWifiPwd, deviceSerial, deviceVerifyCode,
                /*deviceHotspotSsid, deviceHotspotPwd, true,*/ object : APWifiConfig.APConfigCallback() {
            override fun onSuccess() {
                //
            }

            override fun OnError(code: Int) {
                //
            }
        })
    }

    fun onClickStop(view: View) {
        ConfigWifiTaskManager.unInit()
    }

    fun onClickStart(view: View) {
//        ConfigWifiTaskManager.init()

//        ConfigWifiExecutingActivityPresenter.changePresenter(ApConfigPresenter())
//        val intent = Intent(this, ConfigWifiExecutingActivity::class.java)
//        intent.putExtra(IntentConstants.ROUTER_WIFI_SSID, routerWifiName)
//        intent.putExtra(IntentConstants.ROUTER_WIFI_PASSWORD, routerWifiPwd)
//        intent.putExtra(IntentConstants.DEVICE_SERIAL, deviceSerial)
//        intent.putExtra(IntentConstants.DEVICE_VERIFY_CODE, deviceVerifyCode)
//        intent.putExtra(IntentConstants.DEVICE_HOTSPOT_SSID, deviceHotspotSsid)
//        intent.putExtra(IntentConstants.DEVICE_HOTSPOT_PWD, deviceHotspotPwd)
//        startActivity(intent)

//        val finder = DeviceFinderFromPlatform.getInstance()
//        finder.setCallback(mCallback)
//        val param = DeviceFindParam()
//        param.serial = deviceSerial
//        param.verifyCode = deviceVerifyCode
//        finder.start(param)

        ARouter.getInstance().build(RouteNavigator.CONFIG_WIFI_MAIN_PAGE)
                .withString(IntentConstants.DEVICE_SERIAL, deviceSerial)
                .withString(IntentConstants.DEVICE_VERIFY_CODE, deviceVerifyCode)
                .navigation(this, object : NavCallback() {
                    override fun onArrival(postcard: Postcard?) {
                        LogUtil.d(TAG, "jump success")
                    }
                    override fun onLost(postcard: Postcard?) {
                        LogUtil.d(TAG, "jump fail")
                    }
                })
    }

    private val mCallback = object : DeviceFindCallback() {
        override fun onFind(deviceSerial: String?) {
            LogUtil.e(TAG, "found $deviceSerial from platform")
        }

        override fun onError(code: Int, msg: String?) {
            LogUtil.e(TAG, "occurred error while querying from platform, error code is $code")
        }
    }

}
