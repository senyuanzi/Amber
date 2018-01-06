package sen.yuan.dao.amber.sqlite_module.operator

import org.jetbrains.anko.db.SelectQueryBuilder
import org.jetbrains.anko.db.select
import sen.yuan.dao.amber.Amber
import sen.yuan.dao.amber.sqlite_module.annotation.PrimaryKey
import sen.yuan.dao.amber.sqlite_module.condition
import sen.yuan.dao.amber.sqlite_module.parser.classParser
import kotlin.reflect.full.declaredMemberProperties

/**
 * Created by senyuanzi on 2016/11/28.
 */


/**
 * 根据 主键 查询数据
 * */
inline fun <reified T : Any> T.findOneByKey(primaryKey: Any): T? {
    val mClass = this.javaClass.kotlin
    val name = "${mClass.simpleName}"
    var propertyName: String? = null
    mClass.declaredMemberProperties.forEach {
        if (it.annotations.map {
            it.annotationClass
        }.contains(PrimaryKey::class)) propertyName = it.name
    }
    if (propertyName == null) throw Exception("$name 类型没有设置PrimaryKey")
    return Amber.database.use {
        tryDo {
            select(name).apply {
                limit(1)
                condition { propertyName!! equalsData primaryKey }
            }.parseOpt(sen.yuan.dao.amber.sqlite_module.parser.classParser<T>())
        }.let {
            when (it) {
                null, "no such table" -> null
                is T -> it
                else -> null
            }
        }
    }
}


inline fun <reified T : Any> T.findOne(crossinline functor: SelectQueryBuilder.() -> Unit): T? {
    val name = "${this.javaClass.kotlin.simpleName}"

    return Amber.database.use {
        tryDo {
            select(name).apply {
                limit(1)
                functor()
            }.parseOpt(classParser<T>())
        }.let {
            when (it) {
                null, "no such table" -> null
                is T -> it
                else -> null
            }
        }

    }
}


inline fun <reified T : Any> T.findAll(crossinline functor: SelectQueryBuilder.() -> Unit): List<T> {
    val name = "${this.javaClass.kotlin.simpleName}"

    return Amber.database.use {
        tryDo {
            select(name).apply {
                functor()
            }.parseList(classParser<T>())
        }.let {
            when (it) {
                null, "no such table" -> emptyList()
                is List<*> -> it as List<T>
                else -> emptyList()
            }
        }
    }
}
