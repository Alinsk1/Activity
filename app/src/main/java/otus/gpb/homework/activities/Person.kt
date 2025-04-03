package otus.gpb.homework.activities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class Person(
    val name: String,
    val surname: String,
    val age: String
    ): Parcelable