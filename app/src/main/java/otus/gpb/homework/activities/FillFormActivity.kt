package otus.gpb.homework.activities

import android.R.id.message
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import otus.gpb.homework.activities.databinding.ActivityFillFormBinding


class FillFormActivity : AppCompatActivity() {

    private var name = ""
    private var surname = ""
    private var age = ""
    private val binding by lazy {
        ActivityFillFormBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val person = intent.extras?.getParcelable(ContractFillFormActivity.KEY_PERSON) as Person?
        if (person != null) {
            binding.editTextFirstName.setText(person.name)
            binding.editTextLastName.setText(person.surname)
            binding.editTextAge.setText(person.age)
        }
        binding.buttonApply.setOnClickListener(){
            name = binding.editTextFirstName.text.toString()
            surname = binding.editTextLastName.text.toString()
            age = binding.editTextAge.text.toString()
            val intent = EditProfileActivity.newIntent(this)
            intent.apply {
                putExtra(ContractFillFormActivity.KEY_NAME, name)
                putExtra(ContractFillFormActivity.KEY_SURNAME, surname)
                putExtra(ContractFillFormActivity.KEY_AGE, age)
            }
            setResult(RESULT_OK, intent)
            finish()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        setResult(RESULT_CANCELED)
    }

    companion object{
        fun newIntent(context: Context): Intent{
            return Intent(context, FillFormActivity::class.java)
        }
        const val EXTRA_NAME = "name"
        const val EXTRA_SURNAME = "surname"
        const val EXTRA_AGE = "age"
    }
}