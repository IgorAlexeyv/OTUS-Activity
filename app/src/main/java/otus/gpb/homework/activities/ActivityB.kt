package otus.gpb.homework.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class ActivityB : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i("Tag", "$localClassName ${hashCode()} ${Throwable().stackTrace[0].methodName}")
        enableEdgeToEdge()
        setContentView(R.layout.activity_b)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val buttonOpenActivityC = findViewById<Button>(R.id.button_OpenActivityC)
        buttonOpenActivityC.setOnClickListener {
            val intent = Intent(this, ActivityC::class.java)
            startActivity(intent)
        }
    }

    override fun onStart() {
        super.onStart()
        Log.i("Tag", "$localClassName ${hashCode()} ${Throwable().stackTrace[0].methodName}")
        if( intent.getBooleanExtra("CloseStack", false) ){
            finishAndRemoveTask()
        }
    }

    override fun onResume() {
        super.onResume()
        Log.i("Tag", "$localClassName ${hashCode()} ${Throwable().stackTrace[0].methodName}")
    }

    override fun onPause() {
        super.onPause()
        Log.i("Tag", "$localClassName ${hashCode()} ${Throwable().stackTrace[0].methodName}")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i("Tag", "$localClassName ${hashCode()} ${Throwable().stackTrace[0].methodName}")
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        Log.i("Tag", "$localClassName ${hashCode()} ${Throwable().stackTrace[0].methodName}")
    }
}