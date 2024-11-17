package otus.gpb.homework.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class FillFormActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_fill_form)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.textViewFirstName)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        findViewById<Button>(R.id.buttonApplay).setOnClickListener{
            setResult(RESULT_OK, Intent().apply{
                putExtra(KEY_FIRST_NAME, findViewById<EditText>(R.id.editTextFirstName).text.toString())
                putExtra(KEY_SECOND_NAME, findViewById<EditText>(R.id.editTextSecondName).text.toString())
                putExtra(KEY_AGE, findViewById<EditText>(R.id.editTextAge).text.toString())
            })
            finish()
        }
    }
}