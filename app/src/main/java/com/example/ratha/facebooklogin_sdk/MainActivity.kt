package com.example.ratha.facebooklogin_sdk

import android.content.Intent
import android.content.pm.PackageManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.View
import com.example.ratha.facebooklogin_sdk.util.FacebookDataCallback
import com.example.ratha.facebooklogin_sdk.util.FacebookLoginHelper
import org.khmeracademy.util.helper.facebook.FacebookUser
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

class MainActivity : AppCompatActivity() {

    lateinit var facebookLoginHelper: FacebookLoginHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        getHashKey()

        facebookLoginHelper = FacebookLoginHelper(this,object : FacebookDataCallback<FacebookUser>{
            override fun onFail(error: String) {

            }

            override fun onSuccess(user: FacebookUser) {
                Log.e("Data",user.toString())
            }
        })
    }


    fun onLoginClicked(view : View){
        facebookLoginHelper?.initial()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        facebookLoginHelper.onActivityResult(requestCode,resultCode,data!!)
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
