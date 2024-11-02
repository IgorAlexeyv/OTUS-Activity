package otus.gpb.homework.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class ActivityC : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i("Tag","$localClassName ${hashCode()} ${Throwable().stackTrace[0].methodName}")
        enableEdgeToEdge()
        setContentView(R.layout.activity_c)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val buttonOpenActivityA = findViewById<Button>(R.id.button_OpenActivityA)
        buttonOpenActivityA.setOnClickListener {
            val intent = Intent(this, ActivityA::class.java)
            startActivity(intent)
        }

        val buttonOpenActivityD = findViewById<Button>(R.id.button_OpenActivityD)
        buttonOpenActivityD.setOnClickListener {
            val intent = Intent(this, ActivityD::class.java)
            startActivity(intent)
            finishAffinity()
        }

        val buttonCloseActivityC = findViewById<Button>(R.id.button_CloseActivityC)
        buttonCloseActivityC.setOnClickListener {
            finish()
        }

        val buttonCloseStack = findViewById<Button>(R.id.button_CloseStack)
        buttonCloseStack.setOnClickListener {
            finishAffinity()
            val intent = Intent(this, ActivityB::class.java)
            intent.putExtra("CloseStack", true)
            startActivity(intent)
        }
    }

    override fun onStart() {
        super.onStart()
        Log.i("Tag","$localClassName ${hashCode()} ${Throwable().stackTrace[0].methodName}")
    }

    override fun onResume() {
        super.onResume()
        Log.i("Tag","$localClassName ${hashCode()} ${Throwable().stackTrace[0].methodName}")
    }

    override fun onPause() {
        super.onPause()
        Log.i("Tag","$localClassName ${hashCode()} ${Throwable().stackTrace[0].methodName}")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i("Tag","$localClassName ${hashCode()} ${Throwable().stackTrace[0].methodName}")
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        Log.i("Tag","$localClassName ${hashCode()} ${Throwable().stackTrace[0].methodName}")
    }
}