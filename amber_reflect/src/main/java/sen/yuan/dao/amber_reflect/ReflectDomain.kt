package sen.yuan.dao.amber_reflect

import android.util.Log
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.full.declaredMemberProperties

/**
 * Created by senyuanzi on 2018/1/8.
 */

class ReflectDomain<out T>(val pair: Pair<T, String>)

infix fun <T> T.reflect(propertyName: String)
        = ReflectDomain(this to propertyName)

infix fun <T : Any, D> ReflectDomain<T>.to(targetValue: D) {

    this.pair.first::class.declaredMemberProperties.firstOrNull {
        it.name == this.pair.second
    }?.apply {
        @Suppress("UNCHECKED_CAST")
        (this as KMutableProperty1<T, D>).set(this@to.pair.first, targetValue)
    } ?: Log.e("反射领域", this.pair.first::class.simpleName + "类没有名为" + this.pair.second + "的属性")

}