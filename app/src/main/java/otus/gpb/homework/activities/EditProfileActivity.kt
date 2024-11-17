package otus.gpb.homework.activities

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.FileProvider
import androidx.core.graphics.drawable.toBitmap
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

const val KEY_FIRST_NAME = "FIRST_NAME"
const val KEY_SECOND_NAME = "SECOND_NAME"
const val KEY_AGE = "AGE"

class EditProfileActivity : AppCompatActivity() {

    private enum class AlertsDlg {DLG_PHOTO, DLG_SECOND_CAMERA_PERMISSION_REQ, DLG_OPEN_SETTINGS}
    private enum class Menu(val value: Int) { TAKE_PHOTO(0), SELECT_PHOTO(1)}
    private lateinit var imageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)
        imageView = findViewById(R.id.imageview_photo)

        findViewById<Toolbar>(R.id.toolbar).apply {
            inflateMenu(R.menu.menu)
            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.send_item -> {
                        openSenderApp()
                        true
                    }
                    else -> false
                }
            }
        }

        findViewById<ImageView>(R.id.imageview_photo).setOnClickListener {
            AlertDialog.Builder(this).apply {
                setTitle("Фото")
                setIcon(android.R.drawable.ic_dialog_info)
                setItems(arrayOf("Сделать фото", "Выбрать фото"), AlertsDlgClickListener(AlertsDlg.DLG_PHOTO))
                setNegativeButton("Отмена", null)
                show()
            }
        }

        findViewById<Button>(R.id.button4).setOnClickListener{
            fillFormActivityLauncher.launch(Intent(this, FillFormActivity::class.java))
        }
    }

    //Первая попытка запросить разрешение на использование камеры
    private val permissionCameraFirst = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
        granted ->
        if (granted) findViewById<ImageView>(R.id.imageview_photo).setImageResource(R.drawable.cat)
    }

    //Вторая попытка запросить разрешение на использование камеры
    private val permissionCameraSecond = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
        granted ->
        if (!granted){
            AlertDialog.Builder(this@EditProfileActivity).apply {
                setIcon(android.R.drawable.ic_dialog_info)
                setMessage("В настройках необходимо дать разрешение на использование камеры.")
                setPositiveButton("Открыть настройки", AlertsDlgClickListener(AlertsDlg.DLG_OPEN_SETTINGS))
                show()
            }
        }
    }

    private val pickPhoto = registerForActivityResult(ActivityResultContracts.GetContent()) {
        uri -> if( uri != null) populateImage(uri)
    }

    private val fillFormActivityLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()) {
        result ->
        val data = result.data
        val code = result.resultCode

        if( code == RESULT_OK && data != null){
            val str = data.getStringExtra(KEY_FIRST_NAME)
            findViewById<TextView>(R.id.textview_name).text = data.getStringExtra(KEY_FIRST_NAME)
            findViewById<TextView>(R.id.textview_surname).text = data.getStringExtra(KEY_SECOND_NAME)
            findViewById<TextView>(R.id.textview_age).text = data.getStringExtra(KEY_AGE)
        }
    }

    private inner class AlertsDlgClickListener(val dialod: AlertsDlg) : DialogInterface.OnClickListener {
        override fun onClick(dialog: DialogInterface, which: Int) {
            when(dialod) {
                AlertsDlg.DLG_PHOTO -> {
                    when (which) {
                        Menu.TAKE_PHOTO.value -> {
                            when
                            {
                                checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED -> {
                                    findViewById<ImageView>(R.id.imageview_photo).setImageResource(R.drawable.cat)
                                }

                                shouldShowRequestPermissionRationale(Manifest.permission.CAMERA) -> {
                                    AlertDialog.Builder(this@EditProfileActivity).apply {
                                        setTitle("Ранее Вы запретили к камере доступ")
                                        setMessage("Чтобы сделать фото необходимо разрешение на использование камеры.")
                                        setIcon(android.R.drawable.ic_dialog_info)
                                        setPositiveButton("Дать доступ", AlertsDlgClickListener(AlertsDlg.DLG_SECOND_CAMERA_PERMISSION_REQ))
                                        setNegativeButton("Отмена", null)
                                        show()
                                    }
                                }

                                else -> permissionCameraFirst.launch(Manifest.permission.CAMERA)
                            }
                        }

                        Menu.SELECT_PHOTO.value -> pickPhoto.launch("image/*")
                    }
                }

                AlertsDlg.DLG_SECOND_CAMERA_PERMISSION_REQ ->
                    permissionCameraSecond.launch(Manifest.permission.CAMERA)

                AlertsDlg.DLG_OPEN_SETTINGS ->
                    startActivity( Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).setData(Uri.fromParts("package", packageName, null)))
            }

        }
    }

    /**
     * Используйте этот метод чтобы отобразить картинку полученную из медиатеки в ImageView
     */
    private fun populateImage(uri: Uri) {
        val bitmap = BitmapFactory.decodeStream(contentResolver.openInputStream(uri))
        imageView.setImageBitmap(bitmap)
    }

    private fun openSenderApp() {
        var text = "Имя:" + findViewById<TextView>(R.id.textview_name).text.toString() + "\n"
        text += "Фамилия:" + findViewById<TextView>(R.id.textview_surname).text.toString() + "\n"
        text += "Возраст:" + findViewById<TextView>(R.id.textview_age).text.toString()

        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, text)
            putExtra(Intent.EXTRA_STREAM, getBitmapUriFromBitmap(this@EditProfileActivity, imageView.drawable.toBitmap()))
            setPackage("org.telegram.messenger");
            type = "*/*"
        }

        val shareIntent = Intent.createChooser(sendIntent, "Отправить через...")
        startActivity(shareIntent)
    }

    private fun getBitmapUriFromBitmap(context: Context, bitmap: Bitmap): Uri? {
        var bmpUri: Uri? = null
        try {
            val file = File(
                context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                "share_image_${System.currentTimeMillis()}.png"
            )
            val out = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, out)
            out.close()
            bmpUri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID, file)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return bmpUri
    }
}