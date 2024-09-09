package kishk.ahmedmohamed.drugbank.ui.activities

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.Navigation
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import dagger.hilt.android.AndroidEntryPoint
import kishk.ahmedmohamed.drugbank.databinding.ActivityMainBinding

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private val binding: ActivityMainBinding
        get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        _binding = ActivityMainBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        binding.navHostFrame.post {
            _binding?.let {
                val navController = Navigation.findNavController(it.navHostFrame)
                val config = AppBarConfiguration(navController.graph)
                setupActionBarWithNavController(navController, config)
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return Navigation.findNavController(binding.navHostFrame).navigateUp()
                || super.onSupportNavigateUp()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}