package fr.xgouchet.packageexplorer.applist

import android.app.Activity
import fr.xgouchet.packageexplorer.core.utils.getMainActivities
import fr.xgouchet.packageexplorer.core.utils.getResolvedIntent
import fr.xgouchet.packageexplorer.details.app.AppDetailsActivity
import fr.xgouchet.packageexplorer.ui.mvp.Navigator
import kotlin.properties.Delegates.notNull

/**
 * @author Xavier F. Gouchet
 */
class AppListNavigator : Navigator<AppViewModel> {

    override var currentActivity: Activity by notNull()

    override fun goToItemDetails(item: AppViewModel) {
        AppDetailsActivity.startWithApp(currentActivity, item)
    }

    override fun goToItemEdition(item: AppViewModel) {
        val resolvedInfos = getMainActivities(currentActivity, item.packageName)

        val filteredInfos = resolvedInfos.filter { !it.activityInfo.name.contains("leakcanary") }

        if (filteredInfos.isEmpty()) {
            return
        } else if (filteredInfos.size == 1) {
            val intent = getResolvedIntent(filteredInfos[0])
            currentActivity.startActivity(intent)
        }
    }

    override fun goToItemCreation() {
    }
    
    override fun goBack() {
        currentActivity.finish()
    }

}
