package com.crabgore.pointapp.ui

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.crabgore.pointapp.R
import com.crabgore.pointapp.common.loadImage
import com.crabgore.pointapp.databinding.ActivityMainBinding
import com.crabgore.pointapp.databinding.NavHeaderMainBinding
import com.crabgore.pointapp.ui.login.LoginActivity
import com.facebook.AccessToken
import com.facebook.GraphRequest
import com.facebook.login.LoginManager
import com.google.android.material.navigation.NavigationView
import timber.log.Timber

class MainActivity : AppCompatActivity() {
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var headerBinding: NavHeaderMainBinding
    private val navController by lazy { findNavController(R.id.nav_host_fragment_content_main) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initUI()
        getAbdSerFBUserInfo(AccessToken.getCurrentAccessToken())
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_logout -> {
                logout()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    /**
     * Если честно, настройка navigation drawer для меня в новинку, так как всегда пользовалься только
     * bottom navigation view вкупе с navigation components
     */
    private fun initUI() {
        headerBinding = NavHeaderMainBinding.bind(binding.navView.getHeaderView(0))

        setSupportActionBar(binding.appBarMain.toolbar)
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    private fun getAbdSerFBUserInfo(accessToken: AccessToken) {
        Timber.d("facebook registering user")
        val request =
            GraphRequest.newMeRequest(
                accessToken
            ) { user, _ ->
                val email = user?.getString("email")
                val firstName = user?.getString("first_name")
                val lastName = user?.getString("last_name")

                headerBinding.userName.text = "$firstName $lastName"
                headerBinding.userLogin.text = email
                loadImage(
                    "https://graph.facebook.com/" + accessToken.userId + "/picture?type=large",
                    headerBinding.userIcon
                )
            }

        val parameters = Bundle()
        parameters.putString("fields", "first_name, last_name, email")
        request.apply {
            this.parameters = parameters
            executeAsync()
        }
    }

    private fun logout() {
        LoginManager.getInstance().logOut()
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}