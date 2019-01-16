package org.khmeracademy.util.helper.facebook

data class FacebookUserInfo (
        val locale :String ="",
        val id: Long =0,
        val gender : String="",
        val first_name: String="",
        val last_name: String="",
        val name :String="",
        val link: String="",
        val email: String="",
        val birthday : String="",
        val update_time: String=""
)