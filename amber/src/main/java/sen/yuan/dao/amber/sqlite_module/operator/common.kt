package sen.yuan.dao.amber.sqlite_module.operator

import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import org.jetbrains.anko.db.INTEGER
import org.jetbrains.anko.db.REAL
import org.jetbrains.anko.db.TEXT
import kotlin.reflect.KProperty
import kotlin.reflect.KType
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.starProjectedType

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


inline fun <reified T : KProperty<*>> T.sqliteType() = when (this.returnType) {
    Boolean::class.starProjectedType, String::class.starProjectedType -> TEXT
    Long::class.starProjectedType, Int::class.starProjectedType -> INTEGER
    Float::class.starProjectedType, Double::class.starProjectedType -> REAL
    else -> TEXT
}
