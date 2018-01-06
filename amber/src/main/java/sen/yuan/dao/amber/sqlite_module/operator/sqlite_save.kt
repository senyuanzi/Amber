package sen.yuan.dao.amber.sqlite_module.operator

import android.database.sqlite.SQLiteDatabase
import com.google.gson.Gson
import org.jetbrains.anko.db.*
import sen.yuan.dao.amber.Amber
import sen.yuan.dao.amber.e
import sen.yuan.dao.amber.sqlite_module.annotation.PrimaryKey
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.full.starProjectedType

/**
 * Created by senyuanzi on 2016/11/28.
 */
fun <T : Any> SQLiteDatabase.tryCreateTable(data: T) {
    val mClass = data.javaClass.kotlin
    val name = "${mClass.simpleName}"
    val parameters = mClass.primaryConstructor!!.parameters


    val tablePairs = parameters.associate {
        val pair = it.name!! to it.type.let {
            when (it) {
                Boolean::class.starProjectedType, String::class.starProjectedType -> TEXT
                Int::class.starProjectedType -> INTEGER
                Float::class.starProjectedType, Double::class.starProjectedType -> REAL
                else -> TEXT
            }
        }
        if (it.annotations.map { it.annotationClass }.contains(PrimaryKey::class)) pair.copy(second = pair.second + PRIMARY_KEY + UNIQUE).apply { e("reflect_columns", "$first:$second") }
        else pair.apply { e("reflect_columns", "$first:$second") }
    }.toList().toTypedArray()

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

