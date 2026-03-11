package com.example.mycaffe.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.mycaffe.databinding.ActivitySplashBinding

/**
 * SplashActivity serves as the entry screen of the application.
 *
 * <p>This activity displays a splash screen layout and allows the user
 * to navigate to the main application screen by pressing a start button.</p>
 */
class SplashActivity : AppCompatActivity() {
    /**
     * ViewBinding instance used to access views from {@code activity_splash.xml}.
     */
    private lateinit var binding: ActivitySplashBinding

    /**
     * Called when the activity is first created.
     *
     * <p>Initializes the activity by:</p>
     * <ul>
     *     <li>Enabling edge-to-edge layout for a modern UI appearance</li>
     *     <li>Inflating the layout using ViewBinding</li>
     *     <li>Setting up click listener on the start button to launch {@link MainActivity}</li>
     * </ul>
     *
     * @param savedInstanceState previous state of the activity, if it existed
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Enable modern edge-to-edge layout
        enableEdgeToEdge()

        // Inflate the layout using ViewBinding
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Handle click on the start button to navigate to MainActivity
        binding.startBtn.setOnClickListener {
          startActivity(Intent(this, MainActivity::class.java))
        }
    }
}