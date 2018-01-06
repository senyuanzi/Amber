package sen.yuan.dao.amber.sqlite_module.operator

import org.jetbrains.anko.db.MapRowParser
import org.jetbrains.anko.db.SelectQueryBuilder
import org.jetbrains.anko.db.select
import sen.yuan.dao.amber.Amber

/**
 * Created by senyuanzi on 2016/11/19.
 */


inline fun <reified T : Any> T.columns(vararg fields: String, crossinline functor: SelectQueryBuilder.() -> Unit): List<Map<String, Any?>> {

    val name = "${this.javaClass.kotlin.simpleName}"
    return Amber.database.use {
        tryDo {
            select(name).columns(*fields).apply {
                functor()
            }.parseList(object : MapRowParser<Map<String, Any?>> {
                override fun parseRow(columns: Map<String, Any?>): Map<String, Any?> {
                    return columns
                }
            })
        }.let {
            when (it) {
                null, "no such table" -> emptyList<Map<String, Any?>>()
                is List<*> -> it as List<Map<String, Any?>>
                else -> emptyList<Map<String, Any?>>()
            }
        }
    }
}


