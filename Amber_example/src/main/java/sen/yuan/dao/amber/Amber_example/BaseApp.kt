package sen.yuan.dao.amber.Amber_example

import android.app.Application
import sen.yuan.dao.amber.Amber
import sen.yuan.dao.amber.sqlite_module.AmberConfig

/**
 * Created by senyuanzi on 2018/1/6.
 */


class BaseApp : Application() {
    override fun onCreate() {
        super.onCreate()
        Amber.init(this, AmberConfig("amber_example", null, 1))
    }
}