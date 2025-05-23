package com.app.dolt.ui

import android.annotation.SuppressLint
import android.app.ActivityOptions
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.content.Context
import android.content.Intent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.view.menu.MenuItemImpl
import androidx.databinding.DataBindingUtil
import com.app.dolt.R
import com.app.dolt.databinding.ActivityMenuBinding
import com.app.dolt.ui.challenge.FeedCActivity
import com.app.dolt.ui.post.FeedPActivity
import com.app.dolt.ui.profile.ProfileActivity
import com.app.dolt.ui.search.SearchActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.core.view.size
import androidx.core.view.get
import androidx.lifecycle.lifecycleScope
import com.app.dolt.repository.UserStatsRepository
import com.app.dolt.utils.StatsHandler
import kotlinx.coroutines.launch
import timber.log.Timber

/**
 * Actividad base que contiene y gestiona la barra de navegación inferior.
 * Todas las actividades que la extienden heredan la funcionalidad de navegación.
 */
open class MenuActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMenuBinding

    /**
     * Método llamado al crear la actividad.
     * Configura la barra de navegación y sus eventos de selección.
     *
     * @param savedInstanceState : Estado anterior guardado (si existe).
     */
    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //enableEdgeToEdge()

        binding = DataBindingUtil.setContentView(this, R.layout.activity_menu)

        // Configura la barra de navegación inferior
        val bottomNavigation : BottomNavigationView = binding.bottomNavigation
        val menu = bottomNavigation.menu

        // Ajusta el estado checkable de los elementos del menú
        for(i in 0 until menu.size){
            (menu[i] as? MenuItemImpl)?.let{
                it.isExclusiveCheckable = false
                it.isChecked = false
                it.isExclusiveCheckable = true
            }
        }


        // Actualiza periódicamente los elementos coins y vote_times
        val userStatsRepository = UserStatsRepository() // Asegúrate de tener acceso al repositorio
        StatsHandler.startRepeatingTask {
            lifecycleScope.launch {
                userStatsRepository.refreshUserStats()
                val userStats = userStatsRepository.getLocalUserStats()
                binding.coins.text = "\uD83E\uDE99" + userStats?.coins.toString()
                binding.voteTimes.text = userStats?.vote_times.toString()
            }
        }




        // Configura los eventos al seleccionar un elemento de la barra de navegación
        bottomNavigation.setOnItemSelectedListener  { item ->
            when (item.itemId) {
                R.id.navigation_challenges -> {
                    if (this !is FeedCActivity) {
                        val intent = Intent(this, FeedCActivity::class.java)
                        val options = ActivityOptions.makeCustomAnimation(this, 0, 0)
                        startActivity(intent, options.toBundle())
                    }
                    true
                }
                R.id.navigation_feed -> {
                    if (this !is FeedPActivity) {
                        val intent = Intent(this, FeedPActivity::class.java)
                        val options = ActivityOptions.makeCustomAnimation(this, 0, 0)
                        startActivity(intent, options.toBundle())
                    }
                    true
                }
                R.id.navigation_profile -> {
                    if (this !is ProfileActivity || intent.getStringExtra("MYSELF") != "true") {
                        val sharedPreferences = getSharedPreferences("MY_APP_PREFS", Context.MODE_PRIVATE)
                        val username = sharedPreferences.getString("USERNAME", null)
                        val intent = Intent(this, ProfileActivity::class.java)
                        intent.putExtra("USERNAME", username)
                        intent.putExtra("MYSELF", "true")
                        val options = ActivityOptions.makeCustomAnimation(this, 0, 0)
                        startActivity(intent, options.toBundle())
                    }
                    true
                }
                R.id.navigation_search -> {
                    if (this !is SearchActivity) {
                        val intent = Intent(this, SearchActivity::class.java)
                        val options = ActivityOptions.makeCustomAnimation(this, 0, 0)
                        startActivity(intent, options.toBundle())
                    }
                    true
                }
                else -> false
            }
        }
    }
}