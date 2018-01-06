package sen.yuan.dao.amber.sqlite_module.operator

import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException

/**
 * Created by senyuanzi on 2016/11/26.
 */
inline fun SQLiteDatabase.tryDo(functor: SQLiteDatabase.() -> Any?): Any? {
    return try {
        functor()
    } catch (ex: SQLiteException) {
        if (ex.message?.contains("no such table") == true) "no such table"
        else throw ex
    }
}



