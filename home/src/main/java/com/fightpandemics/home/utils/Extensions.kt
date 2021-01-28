package com.fightpandemics.home.utils

import android.content.Intent
import android.os.Build
import com.fightpandemics.core.utils.Constants
import com.fightpandemics.home.R
import org.threeten.bp.Duration
import org.threeten.bp.Instant
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneId
import org.threeten.bp.ZoneOffset
import org.threeten.bp.format.DateTimeFormatter
import java.util.Locale

val TAB_TITLES = arrayOf(
    R.string.tab_all,
    R.string.tab_offers,
    R.string.tab_requests
)
private const val zero = 0
private const val one = 1
private const val two = 2
private const val seven = 7
private const val twentyThree = 23
private const val twentyNine = 29
private const val thirty = 30
private const val threeOne = 31
private const val threeSixFour = 364
private const val threeSixFive = 365

fun userInitials(userName: String?): String {
    val splitName = userName!!.trim().split("\\s+".toRegex()).toMutableList()
    val firstInitials = splitName[0][0]

    return when {
        splitName.size > one -> {
            val secondInitials = splitName[one][0]
            "$firstInitials$secondInitials".toUpperCase(Locale.ROOT)
        }
        else -> "$firstInitials".toUpperCase(Locale.ROOT)
    }
}

fun sharePost(postTitle: String?, postId: String): Intent {
    val postURL = Constants.FIGHTPANDEMICS_POST_URL.plus(postId)
    val sendIntent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_SUBJECT, R.string.share)
        putExtra(Intent.EXTRA_TITLE, postTitle)
        putExtra(Intent.EXTRA_TEXT, postURL)
    }
    return Intent.createChooser(sendIntent, "Share link to")
}

fun getPostCreated(createdAt: String?): String? {

    // FOR API >= 26
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val readDateTimeFormatter = DateTimeFormatter
            .ISO_OFFSET_DATE_TIME
            .withZone(ZoneOffset.UTC)
        val utcLocalDateTime = LocalDateTime.parse(createdAt, readDateTimeFormatter)
        val localZonedDateTime = utcLocalDateTime
            .atOffset(ZoneOffset.UTC)
            .atZoneSameInstant(ZoneId.systemDefault())
        val created = localZonedDateTime.toInstant()

        val now = Instant.now()
        val elapsedDuration: Duration = Duration.between(created, now)

        // Timber.e(elapsedDuration.seconds.toString())

        val elapsed = calculateTime(elapsedDuration)
        return elapsed
    } else {
        return ""
    }
}
fun calculateTime(elapsedDuration: Duration): String? {
    val day = elapsedDuration.toDays().toInt()
    val hr = elapsedDuration.toHours().toInt()
    val min = elapsedDuration.toMinutes().toInt()
    val sec = elapsedDuration.seconds.toInt()

    when {
        day >= threeSixFive -> return "${day / threeSixFive} year"

        day in threeOne..threeSixFour -> return "${day / thirty} months"
        day == thirty -> return "${day / thirty} month"

        day in seven..twentyNine -> return "${day / seven} weeks"
        day == seven -> return "${day / seven} week"

        day >= one -> return "$day days"
        day == one -> return "$day day"

        hr in two..twentyThree -> return "$hr hours"
        hr == one -> return "$hr hour"

        min in two..60 -> return "$min mins"
        min == one -> return "$min min"

        sec in one..60 -> return "$sec secs"
        sec == zero -> return "now"
    }
    return null
}
