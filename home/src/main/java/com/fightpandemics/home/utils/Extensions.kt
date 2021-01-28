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
private const val ZERO = 0
private const val ONE = 1
private const val TWO = 2
private const val SEVEN = 7
private const val TWENTY_THREE = 23
private const val TWENTY_NINE = 29
private const val THIRTY = 30
private const val THREE_ONE = 31
private const val THREE_SIX_FOUR = 364
private const val THREE_SIX_FIVE = 365

fun userInitials(userName: String?): String {
    val splitName = userName!!.trim().split("\\s+".toRegex()).toMutableList()
    val firstInitials = splitName[0][0]

    return when {
        splitName.size > ONE -> {
            val secondInitials = splitName[ONE][0]
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
        day >= THREE_SIX_FIVE -> return "${day / THREE_SIX_FIVE} year"

        day in THREE_ONE..THREE_SIX_FOUR -> return "${day / THIRTY} months"
        day == THIRTY -> return "${day / THIRTY} month"

        day in SEVEN..TWENTY_NINE -> return "${day / SEVEN} weeks"
        day == SEVEN -> return "${day / SEVEN} week"

        day >= ONE -> return "$day days"
        day == ONE -> return "$day day"

        hr in TWO..TWENTY_THREE -> return "$hr hours"
        hr == ONE -> return "$hr hour"

        min in TWO..60 -> return "$min mins"
        min == ONE -> return "$min min"

        sec in ONE..60 -> return "$sec secs"
        sec == ZERO -> return "now"
    }
    return null
}
