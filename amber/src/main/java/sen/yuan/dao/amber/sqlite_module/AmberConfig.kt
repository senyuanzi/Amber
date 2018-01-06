package sen.yuan.dao.amber.sqlite_module

import android.database.sqlite.SQLiteDatabase

/**
 * Created by senyuanzi on 2018/1/6.
 */

data class AmberConfig(
        val name: String = "Amber",
        val factory: SQLiteDatabase.CursorFactory? = null,
        val version: Int = 1
)