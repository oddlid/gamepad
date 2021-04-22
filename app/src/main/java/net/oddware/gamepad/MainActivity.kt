package net.oddware.gamepad

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import net.oddware.gamepad.databinding.ActivityMainBinding
import timber.log.Timber

class MainActivity : AppCompatActivity() {
    companion object {
        fun hideKeyboard(ctx: Context, view: View) {
            val imm = ctx.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    private lateinit var binding: ActivityMainBinding
    private val appBarConfig = AppBarConfiguration(
        topLevelDestinationIds = setOf(
            R.id.gameSelectionFragment,
            R.id.archiveFragment
        )
    )

    /*
    // By accidentally overriding this method instead of the same with only one parameter, I spent days
    // in frustration not understanding why I got and empty activity and not even a fucking log statement.
    // I'll let this stay here commented as a warning and reminder to myself...
    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        Timber.d("Entering MainActivity onCreate()...")
        super.onCreate(savedInstanceState, persistentState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        Timber.d("Setting content view...")
        setContentView(view)
        Timber.d("Calling setupNav()...")
        //setupNav()
    }
     */

    override fun onCreate(savedInstanceState: Bundle?) {
        Timber.d("Entering MainActivity onCreate()...")
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        Timber.d("Setting content view...")
        setContentView(view)
        Timber.d("Calling setupNav()...")
        setupNav()
    }

    override fun onSupportNavigateUp(): Boolean {
        val navCtl = findNavController(R.id.nav_host_fragment)
        return navCtl.navigateUp() || super.onSupportNavigateUp()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            Timber.d("Home button pressed")
            onBackPressedDispatcher.onBackPressed()
            // If we want normal handling of the button, we must return false here.
            // If we return true, the event is consumed, and processing stops, which in practice
            // means we would need to duplicate handling of home/back, which in this case is not
            // how we want it.
            return false
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        Timber.d("Back button pressed")
    }

    private fun setupNav() {
        Timber.d("Finding nav controller...")
        val navCtl = findNavController(R.id.nav_host_fragment)
        Timber.d("Calling setupWithNavController...")
        binding.bottomNav.setupWithNavController(navCtl)
        Timber.d("Calling setupActionBarWithNavController...")
        setupActionBarWithNavController(navCtl, appBarConfig)
    }
}