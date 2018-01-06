package sen.yuan.dao.amber

import android.app.Application
import sen.yuan.dao.amber.sqlite_module.AmberConfig
import sen.yuan.dao.amber.sqlite_module.AmberDatabaseOpenHelper

/**
 * Created by senyuanzi on 2018/1/6.
 */
object Amber {
    var isDebug = false

    lateinit var database: AmberDatabaseOpenHelper

    fun init(baseApp: Application, config: AmberConfig) {

        database = AmberDatabaseOpenHelper.getInstance(baseApp, config)

    }
}