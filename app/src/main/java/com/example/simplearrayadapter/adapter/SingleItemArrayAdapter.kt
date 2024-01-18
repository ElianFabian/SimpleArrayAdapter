package com.example.simplearrayadapter.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.core.view.descendants
import androidx.viewbinding.ViewBinding

abstract class SingleItemArrayAdapter
<
	VB : ViewBinding,
	DropDownVB : ViewBinding,
	out ItemT : Any,
	>(
	context: Context,
	val inflateView: (LayoutInflater, ViewGroup, Boolean) -> VB,
	val inflateDropDownView: (LayoutInflater, ViewGroup, Boolean) -> DropDownVB,
) : ArrayAdapter<@UnsafeVariance ItemT>(context, 0) {

	open fun onBindView(binding: VB, position: Int) {
		defaultArrayAdapterBind(binding, getItem(position))
	}

	open fun onBindDropDownView(binding: DropDownVB, position: Int) {
		defaultArrayAdapterBind(binding, getItem(position))
	}

	override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

		val inflater = LayoutInflater.from(parent.context)

		@Suppress("UNCHECKED_CAST")
		val binding = convertView?.tag as? VB
			?: inflateView(inflater, parent, false).apply { root.tag = this }

		onBindView(binding, position)

		return binding.root
	}

	override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {

		val inflater = LayoutInflater.from(parent.context)

		@Suppress("UNCHECKED_CAST")
		val binding = convertView?.tag as? DropDownVB
			?: inflateDropDownView(inflater, parent, false).apply { root.tag = this }

		onBindDropDownView(binding, position)

		return binding.root
	}

	override fun getItem(position: Int): ItemT = super.getItem(position)!!

	open fun getItemOrNull(position: Int): ItemT? = when (position) {
		in 0 until count -> super.getItem(position)
		else             -> null
	}


	protected companion object {
		fun <VB : ViewBinding, ItemT : Any> defaultArrayAdapterBind(binding: VB, item: ItemT) {

			val textView: TextView? = when (val root = binding.root) {
				is TextView  -> root
				is ViewGroup -> {
					val descendants = root.descendants

					val textViewWithId = descendants.firstOrNull { view ->
						view is TextView && view.id != -1
					} as? TextView

					textViewWithId ?: descendants.firstOrNull { view ->
						view is TextView
					} as? TextView
				}
				else         -> null
			}

			textView?.text = when (item) {
				is CharSequence -> item
				else            -> "$item"
			}
		}
	}
}

abstract class SimpleSingleItemArrayAdapter<VB : ViewBinding, ItemT : Any>(
	context: Context,
	inflateView: (LayoutInflater, ViewGroup, Boolean) -> VB,
) : SingleItemArrayAdapter<VB, VB, ItemT>(
	context = context,
	inflateView = inflateView,
	inflateDropDownView = inflateView,
) {
	override fun onBindView(binding: VB, position: Int) {
		defaultArrayAdapterBind(binding, getItem(position))
	}

	override fun onBindDropDownView(binding: VB, position: Int) {
		onBindView(binding, position)
	}
}