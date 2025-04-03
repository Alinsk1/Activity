package otus.gpb.homework.activities

import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract
import androidx.appcompat.app.AppCompatActivity

class ContractFillFormActivity: ActivityResultContract<Person, Person?>(){
    override fun createIntent(context: Context, input: Person): Intent {
        val intent = FillFormActivity.newIntent(context)
        intent.putExtra(KEY_PERSON, input)
        return intent
    }

    override fun parseResult(resultCode: Int, intent: Intent?): Person? {
        when {
            resultCode == AppCompatActivity.RESULT_CANCELED -> return null
            intent == null -> return null
        }
        val person = Person(
            intent?.extras?.getString(KEY_NAME).toString(),
            intent?.extras?.getString(KEY_SURNAME).toString(),
            intent?.extras?.getString(KEY_AGE).toString(),
        )
        return person
    }

    companion object {
        const val KEY_PERSON = "person"
        const val KEY_NAME = "name"
        const val KEY_SURNAME = "surname"
        const val KEY_AGE = "age"
    }
}