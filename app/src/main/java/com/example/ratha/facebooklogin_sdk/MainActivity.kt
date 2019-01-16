package com.example.ratha.facebooklogin_sdk

import android.content.Intent
import android.content.pm.PackageManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.View
import com.example.ratha.facebooklogin_sdk.util.FacebookDataCallback
import com.example.ratha.facebooklogin_sdk.util.helper.facebooklogin.FacebookLoginHelper
import com.example.ratha.facebooklogin_sdk.util.helper.accountkit.AccountKitHelper
import com.facebook.accountkit.Account
import org.khmeracademy.util.helper.facebook.FacebookUser
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

class MainActivity : AppCompatActivity() {

    lateinit var facebookLoginHelper: FacebookLoginHelper
    lateinit var accountKitHelper: AccountKitHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        getHashKey()
        //facebook login
        facebookLoginHelper = FacebookLoginHelper(this,
            object : FacebookDataCallback<FacebookUser> {
                override fun onFail(error: String) {

                }

                override fun onSuccess(user: FacebookUser) {
                    Log.e("Data", user.toString())
                }
            })


        //init accountkit login
        accountKitHelper= AccountKitHelper(this,
            object : FacebookDataCallback<Account> {
                override fun onFail(error: String) {
                    Log.e("AccountInfo", error)
                }

                override fun onSuccess(user: Account) {
                    Log.e("AccountInfo", user.toString())
                    val intent = Intent(this@MainActivity, HomeActivity::class.java)
                    startActivity(intent)
                }
            })
    }


    fun onLoginClicked(view : View){
        facebookLoginHelper?.initial()
    }

    fun onLoginWithAccountKit(view :View){
        accountKitHelper?.initial()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        facebookLoginHelper.onActivityResult(requestCode,resultCode,data!!)
        accountKitHelper.onActivityResult(requestCode,resultCode,data!!)
    }


    override fun onDestroy() {
        super.onDestroy()
        accountKitHelper.onDestroy()
    }
    fun getHashKey(){
        try {
            val info = packageManager.getPackageInfo(
                packageName,
                PackageManager.GET_SIGNATURES)
            for (signature in info.signatures) {
                val md = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                Log.e("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT))
            }
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        }
    }
}
