package com.example.simplearrayadapter.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.annotation.ArrayRes
import androidx.viewbinding.ViewBinding

@Suppress("FunctionName")
fun <VB : ViewBinding> TextArrayAdapter(
	context: Context,
	inflateView: (LayoutInflater, ViewGroup, Boolean) -> VB,
): ArrayAdapter<CharSequence> {
	return object : SimpleSingleItemArrayAdapter<VB, CharSequence>(
		context = context,
		inflateView = inflateView,
	) {}
}

@Suppress("FunctionName")
fun <VB : ViewBinding> TextArrayAdapter(
	context: Context,
	@ArrayRes arrayRes: Int,
	inflateView: (LayoutInflater, ViewGroup, Boolean) -> VB,
): ArrayAdapter<CharSequence> {
	return object : SimpleSingleItemArrayAdapter<VB, CharSequence>(
		context = context,
		inflateView = inflateView,
	) {
		init {
			addAll(context.resources.getTextArray(arrayRes).toList())
		}
	}
}

@Suppress("FunctionName")
fun <VB : ViewBinding, ItemT : Any> SimpleArrayAdapter(
	context: Context,
	inflateView: (LayoutInflater, ViewGroup, Boolean) -> VB,
): ArrayAdapter<ItemT> {
	return object : SimpleSingleItemArrayAdapter<VB, ItemT>(
		context = context,
		inflateView = inflateView,
	) {}
}

@Suppress("FunctionName")
inline fun <VB : ViewBinding, ItemT : Any> SimpleArrayAdapter(
	context: Context,
	noinline inflateView: (LayoutInflater, ViewGroup, Boolean) -> VB,
	crossinline onBindView: ArrayAdapter<ItemT>.(
		binding: VB,
		item: ItemT,
		position: Int,
	) -> Unit,
): ArrayAdapter<ItemT> {
	return object : SimpleSingleItemArrayAdapter<VB, ItemT>(
		context = context,
		inflateView = inflateView,
	) {
		override fun onBindView(binding: VB, position: Int) {
			onBindView(binding, getItem(position), position)
		}
	}
}

@Suppress("FunctionName")
inline fun <VB : ViewBinding, ItemT : Any> SimpleArrayAdapter(
	context: Context,
	noinline inflateView: (LayoutInflater, ViewGroup, Boolean) -> VB,
	crossinline onBindDropDownView: ArrayAdapter<ItemT>.(
		binding: VB,
		item: ItemT,
		position: Int,
	) -> Unit,
	crossinline onBindView: ArrayAdapter<ItemT>.(
		binding: VB,
		item: ItemT,
		position: Int,
	) -> Unit,
): ArrayAdapter<ItemT> {
	return object : SimpleSingleItemArrayAdapter<VB, ItemT>(
		context = context,
		inflateView = inflateView,
	) {
		override fun onBindView(binding: VB, position: Int) {
			onBindView(binding, getItem(position), position)
		}

		override fun onBindDropDownView(binding: VB, position: Int) {
			onBindDropDownView(binding, getItem(position), position)
		}
	}
}

@Suppress("FunctionName")
inline fun <VB : ViewBinding, ItemT : Any, DropDownVB : ViewBinding> SimpleArrayAdapter(
	context: Context,
	noinline inflateView: (LayoutInflater, ViewGroup, Boolean) -> VB,
	noinline inflateDropDownView: (LayoutInflater, ViewGroup, Boolean) -> DropDownVB,
	crossinline onBindView: ArrayAdapter<ItemT>.(
		binding: VB,
		item: ItemT,
		position: Int,
	) -> Unit,
	crossinline onBindDropDownView: ArrayAdapter<ItemT>.(
		binding: DropDownVB,
		item: ItemT,
		position: Int,
	) -> Unit,
): ArrayAdapter<ItemT> {
	return object : SingleItemArrayAdapter<VB, DropDownVB, ItemT>(
		context = context,
		inflateView = inflateView,
		inflateDropDownView = inflateDropDownView,
	) {
		override fun onBindView(binding: VB, position: Int) {
			onBindView(binding, getItem(position), position)
		}

		override fun onBindDropDownView(binding: DropDownVB, position: Int) {
			onBindDropDownView(binding, getItem(position), position)
		}
	}
}



@Suppress("FunctionName")
fun <VB : ViewBinding> textBinding(
	inflateView: (LayoutInflater, ViewGroup, Boolean) -> VB,
): BindingData<String> {
	return BindingData(
		itemClass = String::class,
		inflateView = inflateView,
	)
}
