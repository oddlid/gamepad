package net.oddware.gamepad

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import net.oddware.gamepad.databinding.ActivityMainBinding
import timber.log.Timber

class MainActivity : AppCompatActivity() {
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

    private fun setupNav() {
        Timber.d("Finding nav controller...")
        val navCtl = findNavController(R.id.nav_host_fragment)
        Timber.d("Calling setupWithNavController...")
        binding.bottomNav.setupWithNavController(navCtl)
        Timber.d("Calling setupActionBarWithNavController...")
        setupActionBarWithNavController(navCtl, appBarConfig)
    }
}