package otus.gpb.homework.activities.receiver

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import otus.gpb.homework.activities.receiver.databinding.ActivityReceiverBinding

class ReceiverActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityReceiverBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val title = intent.extras?.getString("title")
        val year = intent.extras?.getString("year")
        val description = intent.extras?.getString("description")
        binding.yearTextView.setText(year)
        binding.titleTextView.setText(title)
        binding.descriptionTextView.setText(description)
        when(title){
            "Interstellar" -> {
                val drawable = ContextCompat.getDrawable(this, R.drawable.interstellar)
                binding.posterImageView.setImageDrawable(drawable)
            }
            "Nice guys" -> {
                val drawable = ContextCompat.getDrawable(this, R.drawable.niceguys)
                binding.posterImageView.setImageDrawable(drawable)
            }
        }
    }
}