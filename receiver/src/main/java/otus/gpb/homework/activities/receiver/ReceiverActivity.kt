package otus.gpb.homework.activities.receiver

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources

class ReceiverActivity : AppCompatActivity() {

    companion object{
        const val SENDER_TITLE = "SENDER_TITLE"
        const val SENDER_YEAR = "SENDER_YEAR"
        const val SENDER_DESCRIPTION = "SENDER_DESCRIPTION"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_receiver)

        val title = intent?.getStringExtra(SENDER_TITLE)
        findViewById<TextView>(R.id.titleTextView).text = title

        findViewById<TextView>(R.id.yearTextView).text =
            intent?.getStringExtra(SENDER_YEAR)

        findViewById<TextView>(R.id.descriptionTextView).text =
            intent?.getStringExtra(SENDER_DESCRIPTION)

        val image = when(title)
        {
            "Интерстеллар" -> AppCompatResources.getDrawable(this, R.drawable.interstellar)
            "Славные парни" -> AppCompatResources.getDrawable(this, R.drawable.niceguys)
            else -> null
        }

        if(image != null ) {
            findViewById<ImageView>(R.id.posterImageView).setImageDrawable(image)
        }

    }
}
