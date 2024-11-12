package otus.gpb.homework.activities.sender

import android.annotation.SuppressLint
import android.content.Intent
import android.content.Intent.CATEGORY_DEFAULT
import android.content.Intent.EXTRA_TITLE
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import otus.gpb.homework.activities.receiver.R

class SenderActivity : AppCompatActivity() {

    companion object{
        const val SENDER_TITLE = "SENDER_TITLE"
        const val SENDER_YEAR = "SENDER_YEAR"
        const val SENDER_DESCRIPTION = "SENDER_DESCRIPTION"
    }

    @SuppressLint("QueryPermissionsNeeded")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_sender)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        findViewById<Button>(R.id.buttonToGoogleMaps).setOnClickListener{
            startActivity(
                Intent(Intent.ACTION_VIEW,
                    Uri.parse("geo:0,0?q=restaurants")))
        }

        findViewById<Button>(R.id.buttonSendEmail).setOnClickListener{
            val sendIntent = Intent.createChooser(Intent().apply {
                action = Intent.ACTION_SENDTO
                putExtra(Intent.EXTRA_EMAIL, "foo@foo.org")
                putExtra(Intent.EXTRA_SUBJECT, "Message subject")
                putExtra(Intent.EXTRA_TEXT, "Message text")
                data = Uri.parse("mailto:foo@foo.org")
            }, null)
            startActivity(sendIntent)
        }

        findViewById<Button>(R.id.buttonOpenReceiver).setOnClickListener {
            val sendIntent = Intent(Intent.ACTION_SEND).apply {
                addCategory(CATEGORY_DEFAULT)
                type = "text/plain"
                putExtra(SENDER_TITLE, "Славные парни")
                putExtra(SENDER_YEAR, "2016")
                putExtra(SENDER_DESCRIPTION, "Что бывает, когда напарником брутального костолома становится субтильный лопух? Наемный охранник Джексон Хили и частный детектив Холланд Марч вынуждены работать в паре, чтобы распутать плевое дело о пропавшей девушке, которое оборачивается преступлением века. Смогут ли парни разгадать сложный ребус, если у каждого из них – свои, весьма индивидуальные методы.")
            }
            val shareIntent = Intent.createChooser(sendIntent, null)
            startActivity(shareIntent)
        }
    }
}