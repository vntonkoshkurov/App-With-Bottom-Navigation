package com.vaston.tester

import android.os.Bundle
import android.util.Log
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.vaston.tester.databinding.ActivityMainBinding
import com.vaston.tester.classes.StackController
import com.vaston.tester.ui.Cart
import com.vaston.tester.ui.Home
import com.vaston.tester.ui.Person
import com.vaston.tester.ui.Search

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var stackController: StackController
    private val listOfFragment = listOf<Fragment>(
        Home(),
        Search(),
        Cart(),
        Person()
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        stackController = StackController(supportFragmentManager, listOfFragment)

        val navView: BottomNavigationView = binding.navView
        navView.itemIconTintList = null

        if (stackController.initStatus) {
            //устанавливаем слушателя фрагментов
            navView.setOnItemSelectedListener {
                when (it.itemId) {
                    R.id.fragmentHome -> {
                        stackController.selectedTab(StackController.TAB_M1)
                        true
                    }

                    R.id.fragmentSearch -> {
                        stackController.selectedTab(StackController.TAB_M2)
                        true
                    }

                    R.id.fragmentCart -> {
                        stackController.selectedTab(StackController.TAB_M3)
                        true
                    }

                    R.id.fragmentPerson -> {
                        stackController.selectedTab(StackController.TAB_M4)
                        true
                    }

                    else -> false
                }
            }

            navView.selectedItemId = R.id.fragmentHome
        } else {
            Log.d("MainActivity message:", "Incorect StackController Initialisation")
        }
    }

    /**создаем собственную обработку нажатия на кнопку НАЗАД (перенаправление на собсвенный метод)*/
    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if (stackController.checkSizeStack()) {
            finish()
            return
        }
        stackController.popFragments()
    }
}