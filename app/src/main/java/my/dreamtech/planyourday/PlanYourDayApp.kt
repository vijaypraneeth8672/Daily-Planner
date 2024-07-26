package my.dreamtech.planyourday

import android.app.Application
import my.dreamtech.planyourday.data.Graph

class PlanYourDayApp : Application(){
    override fun onCreate() {
        super.onCreate()
        Graph.provide(this)
        createNotificationChannel(this)
    }
}