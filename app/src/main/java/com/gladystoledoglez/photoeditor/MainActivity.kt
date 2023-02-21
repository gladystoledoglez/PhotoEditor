package com.gladystoledoglez.photoeditor

import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import com.gladystoledoglez.photoeditor.databinding.ActivityMainBinding
import com.gladystoledoglez.photoeditor.extensions.hideAllMenuItems
import com.gladystoledoglez.photoeditor.extensions.toLiteModelCartoonedFp161
import com.gladystoledoglez.photoeditor.ml.LiteModelCartoonganFp161

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private var cartoonedFilterModel: LiteModelCartoonganFp161? = null
    private var cartoonedImageModel: LiteModelCartoonganFp161? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navController =
            (supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment)
                .navController
        setupActionBarWithNavController(navController)
    }

    override fun onSupportNavigateUp() = navController.navigateUp() || super.onSupportNavigateUp()

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        menu?.hideAllMenuItems()
        return super.onCreateOptionsMenu(menu)
    }

    override fun onDestroy() {
        cartoonedFilterModel?.close()
        cartoonedImageModel?.close()
        super.onDestroy()
    }

    fun initCartoonedModels() {
        cartoonedFilterModel = toLiteModelCartoonedFp161()
        cartoonedImageModel = toLiteModelCartoonedFp161()
    }

    fun getCartoonedFilterModel() = cartoonedFilterModel

    fun getCartoonedImageModel() = cartoonedImageModel
}