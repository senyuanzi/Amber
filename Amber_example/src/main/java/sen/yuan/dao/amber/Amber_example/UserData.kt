package sen.yuan.dao.amber.Amber_example

import sen.yuan.dao.amber.sqlite_module.annotation.PrimaryKey


/**
 * Created by senyuanzi on 2016/11/19.
 */


data class UserData(
        @PrimaryKey
        var id: Int = -1,
        var name: String = "",
        var age: Int = -1,
        var isChild: Boolean = false
)