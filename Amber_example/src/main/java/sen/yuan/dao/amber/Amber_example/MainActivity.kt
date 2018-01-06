package sen.yuan.dao.amber.Amber_example

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.db.SqlOrderDirection
import sen.yuan.dao.amber.sqlite_module.condition
import sen.yuan.dao.amber.sqlite_module.operator.*

class MainActivity : AppCompatActivity() {

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                message.setText(R.string.title_home)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dashboard -> {
                message.setText(R.string.title_dashboard)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_notifications -> {
                message.setText(R.string.title_notifications)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        test()
    }


    private fun test() {

        //save
        val user = UserData()
        user.id = 666
        user.name = "sen"
        user.age = 23
        user.isChild = false
        user.save()

        //or

        UserData(2, "john", 12, true).save()
        UserData(7, "lucy", 8, true).save()
        UserData(4, "su", 80, false).save()


        //find
        val user1 = UserData().findOneByKey(666)
        e { user1?.name ?: "no user witch id is 666" } // "sen"

        val user2 = UserData().findOne {
            condition {
                "age" between 7..16
                "isChild" Not false
            }
            orderBy("age", SqlOrderDirection.ASC)
        }
        e { user2?.name ?: "no user witch age is between 7..16 and isChild is not false" } // "lucy"

        val userList = UserData().findAll {
            condition {
                "age" moreThan 11
                "name" like "s%"
            }
        }
        e { userList.toString() } // 2


        //update
        var user3 = UserData().findOne { condition { "name" equalsData "lucy" } }!!
        user3.age += 1
        user3.update()
        user3 = UserData().findOne { condition { "name" equalsData "lucy" } }!!
        e { user3.age.toString() } // 9

        //or
        var user4 = UserData(7, "lucy", 10, true)//user4.id == user3.id · 注意primary key相同
        user4.update()//user4 will overwrite the old data · 将会覆盖掉旧数据
        user4 = UserData().findOneByKey(7)!!
        e { user4.age.toString() } // 10

        var user5 = UserData(90, "white", 77, false)// 90 is a new primary key
        user5.updateOrSave()// Gluttony will save a new data
        user5 = UserData().findOneByKey(90)!!
        e { user5.name } // "white"

        UserData().updateByKey(90) { -> arrayOf("name" to "black", "age" to 80) }
        val user6 = UserData().findOneByKey(90)!!
        e { user6.name + ":" + user6.age } //white:77->black:80

        UserData().updateByKey(90, "name" to "green", "age" to 82)
        val user7 = UserData().findOneByKey(90)!!
        e { user7.name + ":" + user7.age } //black:80->green:82

        UserData().update("name" to "red", "age" to 99) {
            condition {
                "name" equalsData "green"
            }
        }
        val user8 = UserData().findOneByKey(90)!!
        e { user8.name + ":" + user8.age } //green:82->red:99

        //deleteAll
        var user9: UserData?
        user9 = UserData(90)//only need primary key
        user9.delete()
        user9 = UserData().findOneByKey(90)
        e { user9?.name ?: "no user" }//no user

        UserData().deleteByKey(666)//deleteAll sen
        val user10 = UserData().findOneByKey(666)
        e { user10?.name ?: "no user" }//no user

        UserData().deleteAll {
            condition {
                "isChild" equalsData true
            }
        }//john and lucy will be deleted
        val children = UserData().findAll { condition { "isChild" equalsData true } }
        e { children.size.toString() }//0

        e { UserData().findAll { }.size.toString() }//1
        UserData().clear()
        e { UserData().findAll { }.size.toString() }//0

    }

}
