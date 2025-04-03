package otus.gpb.homework.activities

import android.Manifest
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import otus.gpb.homework.activities.databinding.ActivityEditProfileBinding


class EditProfileActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityEditProfileBinding.inflate(layoutInflater)
    }
    private val arrayString = arrayOf("Сделать фото", "Выбрать фото")
    private var item = 0
    private var count = 0
    private lateinit var imageView: ImageView
    private var imageUri = ""
    private val getContent = registerForActivityResult(ActivityResultContracts.GetContent()){ uri: Uri? ->
        if (uri != null){
            imageUri = uri.toString()
            populateImage(uri)
        }
    }
    private val launcher = registerForActivityResult(ContractFillFormActivity()){ person ->
        Log.d("Main", person.toString())
        if (person != null){
            binding.textviewName.text = person.name
            binding.textviewSurname.text = person.surname
            binding.textviewAge.text = person.age
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
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
        binding.button4.setOnClickListener(){
            launcher.launch(getPerson())
        }
        binding.imageviewPhoto.setOnClickListener(){
            val builder = AlertDialog.Builder(this)
            builder.apply {
                setTitle("Редактирование фото")
                setSingleChoiceItems(arrayString, -1, object : DialogInterface.OnClickListener{
                    override fun onClick(p0: DialogInterface?, p1: Int) {
                        item = p1
                    }
                })
                setPositiveButton("Да", object : DialogInterface.OnClickListener {
                    override fun onClick(p0: DialogInterface?, p1: Int) {
                        when(arrayString[item]){
                            "Сделать фото" -> {
                                if (ContextCompat.checkSelfPermission(this@EditProfileActivity, Manifest.permission.CAMERA)
                                    == PackageManager.PERMISSION_GRANTED){
                                    binding.imageviewPhoto.setImageResource(R.drawable.cat)
                                } else {
                                    when(count){
                                        0 -> {
                                            ActivityCompat.requestPermissions(
                                                this@EditProfileActivity, arrayOf(
                                                    Manifest.permission.CAMERA,
                                                ), 1)
                                        }
                                        1 -> createAlertDialogWhyCamera()
                                    }
                                }
                            }
                            "Выбрать фото" -> {
                                getContent.launch("image/*")
                            }
                        }
                    }
                })
                setNegativeButton("Отменить", null)
                create()
                show()
            }
        }
    }

    private fun getPerson(): Person{
        return Person(
        binding.textviewName.text.toString(),
        binding.textviewSurname.text.toString(),
        binding.textviewAge.text.toString()
        )
    }

    private fun createAlertDialogWhyCamera(){
        val builder = AlertDialog.Builder(this)
        builder.apply {
            setTitle("Зачем нужна камера")
            setMessage("Камера нужна вам для того, чтобы можно было загрузить фотографию котика, которая будет использоваться для установки аватарки профиля")
            setPositiveButton("Дать доступ", object : DialogInterface.OnClickListener{
                override fun onClick(p0: DialogInterface?, p1: Int) {
                    ActivityCompat.requestPermissions(
                        this@EditProfileActivity, arrayOf(
                            Manifest.permission.CAMERA,
                        ), 1)
                }
            })
            setNegativeButton("Отмена", null)
            create()
            show()
        }
    }

    private fun createAlertDialogOpenSettings(){
        val builder = AlertDialog.Builder(this)
        builder.apply {
            setTitle("Открытие настроек")
            setPositiveButton("Открыть настройки", object : DialogInterface.OnClickListener{
                override fun onClick(p0: DialogInterface?, p1: Int) {
                    val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                    startActivity(intent)
                }
            })
            create()
            show()
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            binding.imageviewPhoto.setImageResource(R.drawable.cat)
        } else {
            count++
            if (count == 2){
                createAlertDialogOpenSettings()
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
        val intent = Intent(Intent.ACTION_SEND)
        intent.setPackage("org.telegram.messenger")
        intent.setType("*/*")
        intent.putExtra(Intent.EXTRA_STREAM, imageUri)
        intent.putExtra(Intent.EXTRA_TEXT, "Name: ${binding.textviewName.text}\n" +
                "Surname: ${binding.textviewSurname.text}\n +" +
                "Age: ${binding.textviewAge.text}")
        startActivity(intent)
    }

    companion object {
        fun newIntent(context: Context): Intent{
            return Intent(context, EditProfileActivity::class.java)
        }
    }
}