package sen.yuan.dao.amber.sqlite_module.operator

import android.database.sqlite.SQLiteDatabase
import com.google.gson.Gson
import org.jetbrains.anko.db.PRIMARY_KEY
import org.jetbrains.anko.db.UNIQUE
import org.jetbrains.anko.db.createTable
import org.jetbrains.anko.db.insert
import sen.yuan.dao.amber.Amber
import sen.yuan.dao.amber.e
import sen.yuan.dao.amber.sqlite_module.annotation.Ignore
import sen.yuan.dao.amber.sqlite_module.annotation.PrimaryKey
import sen.yuan.dao.amber_reflect.containsAnnotation
import kotlin.reflect.full.declaredMemberProperties

/**
 * Created by senyuanzi on 2016/11/28.
 */
fun <T : Any> SQLiteDatabase.tryCreateTable(data: T) {
    val mClass = data.javaClass.kotlin
    val name = "${mClass.simpleName}"
    val properties = mClass.declaredMemberProperties

    val tablePairs = properties
            .filter { !it.containsAnnotation(Ignore::class) }
            .map {
                it.name to if (it.containsAnnotation(PrimaryKey::class)) {
                    it.sqliteType() + PRIMARY_KEY + UNIQUE
                } else it.sqliteType()
            }.toTypedArray()

    this.createTable(name, true, *tablePairs)
}


fun <T : Any> T.save(): Long {
    val mClass = this.javaClass.kotlin
    val name = "${mClass.simpleName}"
    val properties = mClass.declaredMemberProperties

    val valuePairs = properties.associate {
        e("reflect_values", "${it.name}:${it.get(this).toString()}")
        it.name to it.get(this).let {
            when (it) {
                true -> "true"
                false -> "false"
                is String, is Int, is Float, is Double -> it
                else -> Gson().toJson(it)
            }
        }
    }.toList().toTypedArray()


    return Amber.database.use {
        tryCreateTable(this@save)
        insert(name, *valuePairs)
    }
}

