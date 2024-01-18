package com.example.simplearrayadapter.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.core.view.descendants
import androidx.viewbinding.ViewBinding
import kotlin.reflect.KClass

abstract class SimpleMultiItemArrayAdapter<ItemT : Any>(
	context: Context,
	itemBindings: List<BindingData<ItemT>>,
) : ArrayAdapter<ItemT>(context, 0) {
	
	abstract val bindings: List<BindingData<ItemT>>
	
	
	private val itemClassToViewType = itemBindings.mapIndexed { index, data -> data.itemClass to index }.toMap()

	override fun getItem(position: Int): ItemT = super.getItem(position)!!
	
	fun getItemOrNull(position: Int): ItemT? = super.getItem(position)
	
	override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
		
		val inflater = LayoutInflater.from(parent.context)
		
		val item = getItem(position)
		
		val bindingData = bindings.first { it.itemClass.isInstance(item) }
		
		val binding = convertView?.tag as? ViewBinding
			?: bindingData.inflateView(inflater, parent, false).apply { root.tag = this }
		
		bindingData.onBindView(this, binding, item, position)
		
		return binding.root
	}
	
	override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
		
		val inflater = LayoutInflater.from(parent.context)
		
		val item = getItem(position)
		
		val bindingData = bindings.first { it.itemClass.isInstance(item) }
		
		val binding = convertView?.tag as? ViewBinding
			?: bindingData.inflateDropDownView?.invoke(inflater, parent, false)?.apply { root.tag = this }
			?: bindingData.inflateView(inflater, parent, false).apply { root.tag = this }
		
		bindingData.onBindDropDownView?.invoke(this, binding, item, position)
			?: bindingData.onBindView(this, binding, item, position)
		
		return binding.root
	}
}


@Suppress("UNCHECKED_CAST")
inline fun <VB : ViewBinding, reified ItemT : Any> bindingOf(
	noinline inflateView: (LayoutInflater, ViewGroup, Boolean) -> VB,
	noinline onBindView: ArrayAdapter<ItemT>.(
		binding: VB,
		item: ItemT,
		position: Int,
	) -> Unit,
): BindingData<ItemT> {
	return BindingData(
		itemClass = ItemT::class,
		inflateView = inflateView,
		onBindView = onBindView as ArrayAdapter<ItemT>.(
			binding: ViewBinding,
			item: ItemT,
			position: Int,
		) -> Unit,
	)
}

@Suppress("UNCHECKED_CAST")
inline fun <VB : ViewBinding, reified ItemT : Any> bindingOf(
	noinline inflateView: (LayoutInflater, ViewGroup, Boolean) -> VB,
	noinline onBindView: ArrayAdapter<ItemT>.(
		binding: VB,
		item: ItemT,
		position: Int,
	) -> Unit,
	noinline onBindDropDownView: ArrayAdapter<ItemT>.(
		binding: VB,
		item: ItemT,
		position: Int,
	) -> Unit,
): BindingData<ItemT> {
	return BindingData(
		itemClass = ItemT::class,
		inflateView = inflateView,
		onBindView = onBindView as ArrayAdapter<ItemT>.(
			binding: ViewBinding,
			item: ItemT,
			position: Int,
		) -> Unit,
		onBindDropDownView = onBindDropDownView as ArrayAdapter<ItemT>.(
			binding: ViewBinding,
			item: ItemT,
			position: Int,
		) -> Unit,
	)
}

@Suppress("UNCHECKED_CAST")
inline fun <ViewVB : ViewBinding, DropDownViewVB : ViewBinding, reified ItemT : Any> bindingOf(
	noinline inflateView: (LayoutInflater, ViewGroup, Boolean) -> ViewVB,
	noinline onBindView: ArrayAdapter<ItemT>.(
		binding: ViewVB,
		item: ItemT,
		position: Int,
	) -> Unit,
	noinline inflateDropDownView: ((LayoutInflater, ViewGroup, Boolean) -> DropDownViewVB),
	noinline onBindDropDownView: (ArrayAdapter<ItemT>.(
		binding: DropDownViewVB,
		item: ItemT,
		position: Int,
	) -> Unit)? = null,
): BindingData<ItemT> {
	return BindingData(
		itemClass = ItemT::class,
		inflateView = inflateView,
		onBindView = onBindView as ArrayAdapter<ItemT>.(
			binding: ViewBinding,
			item: ItemT,
			position: Int,
		) -> Unit,
		inflateDropDownView = inflateDropDownView,
		onBindDropDownView = onBindDropDownView as ArrayAdapter<ItemT>.(
			binding: ViewBinding,
			item: ItemT,
			position: Int,
		) -> Unit,
	)
}


class BindingData<out ItemT : Any>(
	val itemClass: KClass<@UnsafeVariance ItemT>,
	val inflateView: (LayoutInflater, ViewGroup, Boolean) -> ViewBinding,
	val onBindView: ArrayAdapter<@UnsafeVariance ItemT>.(
		binding: ViewBinding,
		item: @UnsafeVariance ItemT,
		position: Int,
	) -> Unit = { binding, item, _ ->
		defaultArrayAdapterBind(binding, item)
	},
	val inflateDropDownView: ((LayoutInflater, ViewGroup, Boolean) -> ViewBinding)? = null,
	val onBindDropDownView: (ArrayAdapter<@UnsafeVariance ItemT>.(
		binding: ViewBinding,
		item: @UnsafeVariance ItemT,
		position: Int,
	) -> Unit)? = null,
)


private fun <VB : ViewBinding, ItemT : Any> defaultArrayAdapterBind(binding: VB, item: ItemT) {

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