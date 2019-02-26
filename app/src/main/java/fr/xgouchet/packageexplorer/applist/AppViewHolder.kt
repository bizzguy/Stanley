package fr.xgouchet.packageexplorer.applist

import android.util.Log
import android.view.View
import android.widget.Toast
import fr.xgouchet.packageexplorer.ui.adapter.BaseViewHolder
import fr.xgouchet.packageexplorer.databinding.ItemAppBinding
import io.reactivex.functions.BiConsumer

class AppViewHolder(val binding: ItemAppBinding, listener: BiConsumer<AppViewModel, View?>?)
    : BaseViewHolder<AppViewModel>(listener, binding.root) {

    var selectedView: View? = null

    init {
        binding.root.setOnClickListener {
            Log.d("click", "clicked card")
            selectedView = it
            fireSelected() }
        binding.buttonSettings.setOnClickListener(View.OnClickListener {
            Log.d("click", "clicked button")
            selectedView = it
            fireSelected()
        })
    }

    override fun onBindItem(item: AppViewModel) {
        binding.app = item
        binding.executePendingBindings()
    }

    override fun getTransitionView(): View? {
        //return binding.iconApp
        return selectedView
    }
}