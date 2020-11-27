package com.fightpandemics.home.utils

import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import com.fightpandemics.core.utils.Constants
import com.fightpandemics.home.R
import org.threeten.bp.*
import org.threeten.bp.format.DateTimeFormatter
import timber.log.Timber
import java.util.*

val TAB_TITLES = arrayOf(
    com.fightpandemics.home.R.string.tab_all,
    com.fightpandemics.home.R.string.tab_offers,
    com.fightpandemics.home.R.string.tab_requests
)

fun userInitials(userName: String?): String {
    val splitName = userName!!.trim().split("\\s+".toRegex()).toMutableList()
    val firstInitials = splitName[0][0]

    return when {
        splitName.size > 1 -> {
            val secondInitials = splitName[1][0]
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

// TODO 4 -
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

        //Timber.e(elapsedDuration.seconds.toString())

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
        day >= 365 -> return "${day / 365} year"

        day in 31..364 -> return "${day / 30} months"
        day == 30 -> return "${day / 30} month"

        day in 7..29 -> return "${day / 7} weeks"
        day == 7 -> return "${day / 7} week"

        day >= 1 -> return "${day} days"
        day == 1 -> return "${day} day"

        hr in 2..23 -> return "${hr} hours"
        hr == 1 -> return "${hr} hour"

        min in 2..60 -> return "${min} mins"
        min == 1 -> return "${min} min"

        sec in 1..60 -> return "${sec} secs"
        sec == 0 -> return "now"
    }
    return null
}
