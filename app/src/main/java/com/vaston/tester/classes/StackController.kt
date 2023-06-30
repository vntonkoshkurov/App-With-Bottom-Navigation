/**Vasily Tonkoshkurov vntonkoshkurov@yandex.ru
 * Telegramm @vaston_RU*/

package com.vaston.tester.classes

import android.annotation.SuppressLint
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.vaston.tester.R
import java.util.Stack

/** Класс для хранения стека фрагментов для нижней навигационной панели
 * Максимальное количество пунков в меню - 5 шт.
 * В класс необходимо передать:
 * - FragmentManager,
 * - Список с фрагментами, которые образуют пункты меню.*/
class StackController(supportFragmentManager: FragmentManager, listOfFragment: List<Fragment>) {

    companion object {
        const val TAB_M1 = "m1"
        const val TAB_M2 = "m2"
        const val TAB_M3 = "m3"
        const val TAB_M4 = "m4"
        const val TAB_M5 = "m5"
    }

    var mStack = HashMap<String, Stack<Fragment>>()
    private var mCurrentTab: String = ""
    private val supportFragmentManager: FragmentManager
    private var listMenu = listOf(
        TAB_M1,
        TAB_M2,
        TAB_M3,
        TAB_M4,
        TAB_M5
    )
    private val listOfFragment: List<Fragment>
    var initStatus: Boolean = false
    private val TAG = "StackController message:"

    init {
        this.listOfFragment = listOfFragment
        this.supportFragmentManager = supportFragmentManager
        /**создаем массив из стэков, согласно общему количеству пунков в меню
        В нашем случае у нас "*" пункта меню, соответственно будем хранить "*" стека с последовательностями
        дочерних фрагментов*/
        if (listOfFragment.size < listMenu.size) {
            for (i in 0..listOfFragment.size) {
                mStack[listMenu[i]] = Stack<Fragment>()
            }
            initStatus = true
        } else {
            Log.d(TAG, "Count of menu item must be no more then five")
        }
    }

    /**Метод, который определяет к какому из 4-х стеков фрагментов меню
    добавить новый фрагмент. Врагменты, вызываемые из под главных фрагментов, должны попасть в
    соответствующий стек родительского фрагмента
     */
    fun selectedTab(tabId: String) {
        mCurrentTab = tabId
        /*Если главный фрагмент отсутствует в массиве стеков, помещаем его туда.
         *Отказываемся от анимации.
         *Если главный фрагмент уже есть в массиве стеков, то помещаем последущие фрагменты,
         *Вызываемые из под него в его стэк*/
        if (initStatus && listOfFragment.size >= (listMenu.indexOf(tabId) + 1)) {
            if (mStack[tabId]!!.size == 0) {
                when (tabId) {
                    TAB_M1 -> pushFragments(tabId, listOfFragment[0], true)
                    TAB_M2 -> pushFragments(tabId, listOfFragment[1], true)
                    TAB_M3 -> pushFragments(tabId, listOfFragment[2], true)
                    TAB_M4 -> pushFragments(tabId, listOfFragment[3], true)
                    TAB_M5 -> pushFragments(tabId, listOfFragment[4], true)
                }
            } else {
                pushFragments(tabId, mStack[tabId]!!.lastElement(), false)
            }
        } else {
            Log.d(TAG, "Entered incorrect value of menu item")
        }
    }

    /**метод, который загружает новый фрагмент на экран, после его помещения в стэк
     * родительского фрагмента*/
    private fun pushFragments(tag: String, fragment: Fragment, shoulAdd: Boolean) {
        if (shoulAdd) mStack[tag]!!.push(fragment)
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.content, fragment)
        ft.commit()

    }

    @SuppressLint("CommitTransaction")
    fun popFragments() {
        if (initStatus) {
            val fragment = mStack[mCurrentTab]!!.elementAt(mStack[mCurrentTab]!!.size - 2)
            mStack[mCurrentTab]!!.pop()
            val ft = supportFragmentManager.beginTransaction()
            ft.replace(R.id.content, fragment)
            ft.commit()
        }
    }

    fun checkSizeStack(): Boolean {
        return (mStack[mCurrentTab]!!.size == 1)
    }
}