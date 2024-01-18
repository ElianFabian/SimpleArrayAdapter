package com.example.simplearrayadapter.derived

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding

@Suppress("FunctionName")
fun <VB : ViewBinding> TextHintArrayAdapter(
	context: Context,
	inflateView: (LayoutInflater, ViewGroup, Boolean) -> VB,
	hint: CharSequence,
): SimpleHintArrayAdapter<VB, CharSequence> {
	return object : SimpleHintArrayAdapter<VB, CharSequence>(
		context = context,
		inflateView = inflateView,
		hint = hint,
	) {}
}

@Suppress("FunctionName")
fun <VB : ViewBinding, ItemT : Any> SimpleHintArrayAdapter(
	context: Context,
	inflateView: (LayoutInflater, ViewGroup, Boolean) -> VB,
	hint: ItemT,
	onBindView: SimpleHintArrayAdapter<VB, ItemT>.(
		binding: VB,
		item: ItemT,
		position: Int,
	) -> Unit,
): SimpleHintArrayAdapter<VB, ItemT> {
	return object : SimpleHintArrayAdapter<VB, ItemT>(
		context = context,
		inflateView = inflateView,
		hint = hint,
	) {
		override fun onBindView(binding: VB, position: Int) {
			this.onBindView(binding, getItemAt(position), position)
		}
	}
}