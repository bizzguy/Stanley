package fr.xgouchet.packageexplorer.applist

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.pm.Signature
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import timber.log.Timber
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import javax.security.cert.CertificateException
import javax.security.cert.X509Certificate


/**
 * @author Xavier Gouchet
 */
data class AppViewModel(val packageName: String = "",
                        val title: String = "",
                        val icon: Drawable = ColorDrawable(Color.TRANSPARENT),
                        val installTime: Long = 0,
                        val updateTime: Long = 0,
                        val flags: Int = 0,
                        val certificates: List<X509Certificate> = emptyList(),
                        val version: String? = "") {

    val installTimeStr = DATE_FORMAT.format(Date(installTime))
    var updateTimeStr = DATE_TIME_FORMAT.format(Date(updateTime))
    val isSystemApp = (flags and ApplicationInfo.FLAG_SYSTEM != 0)
    val isDebuggable = (flags and ApplicationInfo.FLAG_DEBUGGABLE != 0)
    val isLargeHeap = (flags and ApplicationInfo.FLAG_LARGE_HEAP != 0)

    init {
        updateTimeStr  = DATE_FORMAT.format(Date(updateTime)) + " at " + DATE_TIME_FORMAT_SHORT.format(Date(updateTime))
    }

    companion object {

        fun fromPackageName(context: Context, packageName: String): AppViewModel? {
            val pm = context.packageManager
            try {
                val pi = pm.getPackageInfo(packageName, 0)
                val ai = pm.getApplicationInfo(packageName, 0)
                return fromAppInfo(pm, pi, ai)
            } catch (e: PackageManager.NameNotFoundException) {
                Timber.e(e, "Error getting app info")
                return null
            }

        }

        fun fromAppInfo(pm: PackageManager, pi: PackageInfo, ai: ApplicationInfo): AppViewModel {
            val signatures: Array<Signature> = pi.signatures ?: emptyArray()
            val certificates = signatures
                    .map {
                        try {
                            X509Certificate.getInstance(it.toByteArray())
                        } catch (e: CertificateException) {
                            null
                        }
                    }
                    .filter { it != null }
                    .toTypedArray()

            val updateTime = pi.lastUpdateTime.toString() + "CST"

            // if date is today then show "Today at hh:mm"

            return AppViewModel(packageName = ai.packageName,
                    title = pm.getApplicationLabel(ai).toString(),
                    icon = pm.getApplicationIcon(ai),
                    installTime = pi.firstInstallTime,
                    updateTime = pi.lastUpdateTime,
                    flags = ai.flags,
                    certificates = listOfNotNull(*certificates),
                    version = pi.versionName + "  (" + pi.versionCode + ")"
            )
        }

        val DATE_FORMAT: DateFormat = SimpleDateFormat.getDateInstance(DateFormat.LONG, Locale.US)
        val DATE_TIME_FORMAT: DateFormat = SimpleDateFormat("yyyy-MM-dd  h:mm", Locale.US)
        val DATE_TIME_FORMAT_SHORT: DateFormat = SimpleDateFormat("h:mm a", Locale.US)

    }
}
