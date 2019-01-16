package com.example.ratha.facebooklogin_sdk.util

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.facebook.*
import com.facebook.internal.ServerProtocol
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.gson.GsonBuilder
import org.json.JSONException
import org.json.JSONObject
import org.khmeracademy.util.helper.facebook.FacebookUser
import org.khmeracademy.util.helper.facebook.FacebookUserInfo


class FacebookLoginHelper{

    var mActivity: AppCompatActivity
    var mCallback: FacebookDataCallback<FacebookUser>
    var mCallbackManager: CallbackManager
    val mPermissions = mutableListOf("public_profile", "email", "user_gender")
    val mFields = "id,name,first_name,last_name,email,picture.type(large),gender,link,birthday"

    constructor( mActivity: AppCompatActivity,  callback: FacebookDataCallback<FacebookUser>){
        this.mActivity=mActivity
        this.mCallback=callback
        FacebookSdk.sdkInitialize(mActivity.applicationContext)
        mCallbackManager= CallbackManager.Factory.create()
    }
    fun initial(){
        LoginManager.getInstance().logOut()
        LoginManager.getInstance().registerCallback(mCallbackManager,object :FacebookCallback<LoginResult>{
            override fun onSuccess(result: LoginResult?) {
                val graphRequest =GraphRequest.newMeRequest(result?.accessToken,object : GraphRequest.GraphJSONObjectCallback{
                    override fun onCompleted(obj: JSONObject?, response: GraphResponse?) {

                        val gson=GsonBuilder().create()
                        val facebookUserInfo=gson.fromJson(obj.toString(),FacebookUserInfo::class.java)
                        var profileImage=""
                        try {
                            val profile =response?.jsonObject?.getJSONObject("picture")?.getJSONObject("data")
                            profileImage= profile!!.getString("url")
                        }catch (e : JSONException){
                            e.printStackTrace()
                        }

                        val user=FacebookUser(
                                userId = facebookUserInfo.id.toString(),
                                firstName = facebookUserInfo.first_name,
                                lastName = facebookUserInfo.last_name,
                                email = facebookUserInfo.email,
                                gender = facebookUserInfo.gender,
                                link =facebookUserInfo.link,
                                dateOfBirth = facebookUserInfo.birthday!!,
                                pictureProfile = profileImage,
                                phone = "",
                                accessToken = result?.accessToken!!.token
                        )

                        mCallback.onSuccess(user)
                    }
                })

                val b=Bundle();
                b.putString("fields",mFields)
                graphRequest.parameters=b
                graphRequest.version=ServerProtocol.getDefaultAPIVersion()
                graphRequest.executeAsync()
            }

            override fun onCancel() {
                Log.e(mActivity.javaClass.name,"cancel facebook login")
            }

            override fun onError(error: FacebookException?) {
                mCallback.onFail(error = error!!.message!!)
            }
        })

        LoginManager.getInstance().logInWithReadPermissions(mActivity,mPermissions)
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        if (mCallbackManager != null) {
            mCallbackManager.onActivityResult(requestCode, resultCode, data)
        }
    }


}