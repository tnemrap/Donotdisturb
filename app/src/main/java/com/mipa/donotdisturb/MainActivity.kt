package com.mipa.donotdisturb

import android.app.NotificationManager
import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import android.widget.Toast
import android.content.Intent
import android.os.Build
import android.provider.Settings


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // get the notification manager system service
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        // Click listener for button widget
        button_on.setOnClickListener {
            if (checkNotificationPolicyAccess(notificationManager)){
                notificationManager.onDOD()
                toast("Do Not Disturb turned on.")
            }
        }

        // Click listener for button widget
        button_off.setOnClickListener{
            if (checkNotificationPolicyAccess(notificationManager)){
                notificationManager.offDOD()
                toast("Do Not Disturb turned off")
            }
        }

        // Click listener for button widget
        button_on_alarm.setOnClickListener{
            if (checkNotificationPolicyAccess(notificationManager)){
                notificationManager.onAlarmDOD()
                toast("Do Not Disturb Alarm Only.")
            }
        }
    }


    // Method to check notification policy access status
    private fun checkNotificationPolicyAccess(notificationManager:NotificationManager):Boolean{
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (notificationManager.isNotificationPolicyAccessGranted){
                //toast("Notification policy access granted.")
                return true
            }else{
                toast("You need to grant notification policy access.")
                // If notification policy access not granted for this package
                val intent = Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS)
                startActivity(intent)
            }
        }else{
            toast("Device does not support this feature.")
        }
        return false
    }
}


/*
    *** reference source developer.android.com ***

    INTERRUPTION_FILTER_NONE
        Interruption filter constant - No interruptions filter - all notifications are suppressed
        and all audio streams (except those used for phone calls) and vibrations are muted.

    INTERRUPTION_FILTER_ALL
        Interruption filter constant - Normal interruption filter - no notifications are suppressed.

    INTERRUPTION_FILTER_ALARMS
        Interruption filter constant - Alarms only interruption filter - all notifications except
        those of category CATEGORY_ALARM are suppressed.

 INTERRUPTION_FILTER_PRIORITY
        Interruption filter constant - Priority interruption filter - all notifications are
        suppressed except those that match the priority criteria.
*/


// Extension function to turn on do not disturb
fun NotificationManager.onDOD(){
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        this.setInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_NONE)
    }
}


// Extension function to turn off do not disturb
fun NotificationManager.offDOD(){
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        this.setInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_ALL)
    }
}


// Extension function to set alarms only interruption filter
fun NotificationManager.onAlarmDOD(){
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        this.setInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_ALARMS)
    }
}



// Extension function to show toast message
fun Context.toast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}