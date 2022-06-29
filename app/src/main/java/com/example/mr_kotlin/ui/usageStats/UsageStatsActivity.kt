package com.example.mr_kotlin.ui.usageStats

import android.app.AppOpsManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Process.myUid
import android.provider.Settings
import android.widget.TextView
import com.example.mr_kotlin.R
import android.app.AppOpsManager.MODE_ALLOWED
import android.app.AppOpsManager.OPSTR_GET_USAGE_STATS
import android.app.usage.UsageStats
import android.app.usage.UsageStatsManager
import android.os.Process
import java.text.SimpleDateFormat
import java.util.*


class UsageStatsActivity : AppCompatActivity() {


    lateinit var UsageStats: TextView

    override fun onCreate(savedInstanceState: Bundle?) {

        //Eliminate the top name feature
        supportActionBar?.hide()

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_usage_stats)

        UsageStats = findViewById(R.id.UsageStats)

        if (CheckUsaseStatsPermision()){
            showUsageStats()
        }
        else{
            startActivity(Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS))
        }

    }

    private fun showUsageStats(){
        var usageStatsManager: UsageStatsManager = getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
        var cal: Calendar = Calendar.getInstance()
        cal.add(Calendar.DAY_OF_MONTH, -1)

        var queryUsageStats : List<UsageStats>? = usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY,cal.timeInMillis,
            System.currentTimeMillis())

        var stats_data : String = ""


        for (i in 0..queryUsageStats!!.size-1){
            stats_data = stats_data + "Package Name"+queryUsageStats.get(i).packageName +"\n"+
                                      "Last Time Used: "+ convertTime(queryUsageStats.get(i).lastTimeUsed)+"\n"+
                                      "Describe Contents: "+ queryUsageStats.get(i).describeContents()+"\n"+
                                      "First Time Stamp: "+ convertTime(queryUsageStats.get(i).firstTimeStamp)+"\n"+
                                      "Last Time Stamp: "+ convertTime(queryUsageStats.get(i).lastTimeStamp)+"\n"+
                                      "Total time in Foreground: "+ convertTime2(queryUsageStats.get(i).totalTimeInForeground)+"\n\n"
        }

        UsageStats.setText(stats_data)

    }

    private fun convertTime(Lastime: Long):String{
        var date: Date= Date(Lastime)
        var format: SimpleDateFormat = SimpleDateFormat("dd/MM/yyyy hh:mm a",Locale.ENGLISH)
        return format.format(date)
    }

    private fun convertTime2(lastTimeUsed: Long):String{
        var date: Date= Date(lastTimeUsed)
        var format: SimpleDateFormat = SimpleDateFormat("hh:mm",Locale.ENGLISH)
        return format.format(date)
    }

    private fun CheckUsaseStatsPermision():Boolean{
        var appOpsManager: AppOpsManager?= null
        var mode: Int=0
        appOpsManager = getSystemService(Context.APP_OPS_SERVICE)!! as AppOpsManager
        mode = appOpsManager.checkOpNoThrow(OPSTR_GET_USAGE_STATS,Process.myUid(),packageName)
        return mode == MODE_ALLOWED
    }
}