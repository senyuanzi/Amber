package sen.yuan.dao.amber_reflect

import kotlin.reflect.KClass
import kotlin.reflect.KProperty
import kotlin.reflect.full.declaredMemberProperties

/**
 * Created by senyuanzi on 2018/1/7.
 */
object AmberReflect {


}


inline fun <reified T : Any> T.members()
        = this.javaClass.kotlin.declaredMemberProperties
        .associate {
            it.name to it.get(this@members)
        }

inline fun <reified T : Any> T.membersTypes()
        = this.javaClass.kotlin.declaredMemberProperties
        .associate {
            it.name to it.returnType
        }

inline fun <reified T : Any> T.membersAnnotations()
        = this.javaClass.kotlin.declaredMemberProperties
        .associate {
            it.name to it.annotations
        }

inline fun <reified T : KProperty<*>> T.containsAnnotation(annotationClass: KClass<out Annotation>)
        = this.annotations
        .map {
            it.annotationClass
        }.contains(annotationClass)

inline fun <reified T : KProperty<*>> T.whenContainsAnnotation(annotationClass: KClass<out Annotation>, block: T.() -> T)
        = if (this.containsAnnotation(annotationClass)) block() else this


inline fun <T> Iterable<T>.changeWhile(predicate: (T) -> Boolean, block: (T) -> T): ArrayList<T> {
    val list = arrayListOf<T>()
    for (item in this)
        if (predicate(item))
            list.add(block(item))
        else list.add(item)
    return list
}


