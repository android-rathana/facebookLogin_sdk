package com.example.ratha.facebooklogin_sdk.util.helper.accountkit

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.example.ratha.facebooklogin_sdk.util.FacebookDataCallback
import com.facebook.accountkit.*
import com.facebook.accountkit.ui.AccountKitActivity
import com.facebook.accountkit.ui.AccountKitConfiguration
import com.facebook.accountkit.ui.LoginType

class AccountKitHelper(var mActivity: AppCompatActivity,val mCallback: FacebookDataCallback<Account>) {
    val accessToken=AccountKit.getCurrentAccessToken()

    companion object {
        const val APP_REQUEST_CODE = 99
        val whiteList = arrayOf("KH","US","KR")
    }

    fun initial(){
        val intent= Intent(mActivity,AccountKitActivity::class.java)
        val configurationBuilder=AccountKitConfiguration.AccountKitConfigurationBuilder(
                LoginType.PHONE,
                AccountKitActivity.ResponseType.TOKEN
        )
        configurationBuilder.setSMSWhitelist(whiteList)
        configurationBuilder.setDefaultCountryCode("KH")
        configurationBuilder.setReadPhoneStateEnabled(true)
        configurationBuilder.setReceiveSMS(true)
        intent.putExtra(
                AccountKitActivity.ACCOUNT_KIT_ACTIVITY_CONFIGURATION,
                configurationBuilder.build()
        )
        mActivity.startActivityForResult(intent,
            APP_REQUEST_CODE
        )
    }

    fun onActivityResult(requestCode: Int , resultCode: Int, data: Intent){
        if(requestCode== APP_REQUEST_CODE){
            val loginResult=data.getParcelableExtra<AccountKitLoginResult>(AccountKitLoginResult.RESULT_KEY)
            if(loginResult.error!=null){
                Log.e(mActivity.javaClass.name,"error")
            }else if(loginResult.wasCancelled()){
                Log.e(mActivity.javaClass.name,"was canceled")
            }else{
                if(loginResult.accessToken!=null){
                    Log.e(mActivity.javaClass.name,"Success: ${loginResult?.accessToken?.accountId}")
                }else{
                    Log.e(mActivity.javaClass.name,"Success: ${loginResult?.authorizationCode?.substring(1,10)}")
                }

                //login suucess here
                getCurrentUser()
            }
        }
    }

    private fun getCurrentUser(){
       AccountKit.getCurrentAccount(object : AccountKitCallback<Account>{
           override fun onSuccess(account: Account?) {
               if(mCallback!=null){
                   mCallback.onSuccess(account!!)
               }
           }

           override fun onError(accountKitError: AccountKitError?) {
                mCallback?.onFail(accountKitError.toString())
           }
       })
    }

    fun logout(){
        AccountKit.logOut()
    }

    fun onDestroy(){
        mCallback==null
    }
}