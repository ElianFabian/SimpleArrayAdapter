package com.example.genericarrayadapter.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.viewbinding.ViewBinding
import kotlin.reflect.KClass

class GenericMultiItemArrayAdapter<out ItemT : Any>(
	context: Context,
	itemBindings: List<BindingData<ItemT>>,
) : ArrayAdapter<@UnsafeVariance ItemT>(context, 0)
{
	private val bindings = itemBindings.distinct()
	private val itemClassToViewType = itemBindings.mapIndexed { index, data -> data.itemClass to index }.toMap()


	override fun getView(position: Int, convertView: View?, parent: ViewGroup): View
	{
		val layoutInflater = LayoutInflater.from(parent.context)

		val viewType = getItemViewType(position)

		val itemBindingData = bindings[viewType]

		val binding = convertView?.tag as? ViewBinding ?: itemBindingData.inflateView(layoutInflater, parent, false)

		val item = getItem(position)!!

		itemBindingData.onBindView(this, item, binding, position)

		return binding.root
	}

	override fun getItemViewType(position: Int): Int
	{
		val item = getItem(position)!!

		return itemClassToViewType[item::class]!!
	}

	override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View
	{
		val layoutInflater = LayoutInflater.from(parent.context)

		val item = getItem(position)!!

		val viewType = getItemViewType(position)

		val itemBindingData = bindings[viewType]

		val binding = if (itemBindingData.inflateDropDownView == null)
		{
			if (itemBindingData.onBindDropDownView != null)
			{
				val binding = convertView?.tag as? ViewBinding ?: itemBindingData.inflateView(layoutInflater, parent, false).apply { root.tag = this }

				itemBindingData.onBindDropDownView.invoke(this, item, binding, position)

				binding
			}
			else
			{
				val binding = convertView?.tag as? ViewBinding ?: itemBindingData.inflateView(layoutInflater, parent, false).apply { root.tag = this }

				itemBindingData.onBindView(this, item, binding, position)

				binding
			}
		}
		else
		{
			if (itemBindingData.onBindDropDownView != null)
			{
				val binding = convertView?.tag as? ViewBinding ?: itemBindingData.inflateDropDownView.invoke(layoutInflater, parent, false).apply { root.tag = this }

				itemBindingData.onBindDropDownView.invoke(this, item, binding, position)

				binding
			}
			else
			{
				val binding = convertView?.tag as? ViewBinding ?: itemBindingData.inflateView.invoke(layoutInflater, parent, false).apply { root.tag = this }

				itemBindingData.onBindView(this, item, binding, position)

				binding
			}
		}

		return binding.root
	}
}


@Suppress("FunctionName")
fun <ItemT : Any> GenericArrayAdapter(
	context: Context,
	itemBindings: List<BindingData<ItemT>>,
): ArrayAdapter<ItemT> = GenericMultiItemArrayAdapter(
	context = context,
	itemBindings = itemBindings,
)


@Suppress("FunctionName")
inline fun <reified ItemT : Any, VB : ViewBinding> Binding(
	noinline inflateView: (LayoutInflater, ViewGroup, Boolean) -> VB,
): BindingData<ItemT>
{
	return BindingData(
		itemClass = ItemT::class,
		inflateView = inflateView,
	)
}

@Suppress("FunctionName")
inline fun <reified ItemT : Any, VB : ViewBinding> Binding(
	noinline inflateView: (LayoutInflater, ViewGroup, Boolean) -> VB,
	noinline onBindView: ArrayAdapter<ItemT>.(
		item: ItemT,
		binding: VB,
	) -> Unit,
): BindingData<ItemT>
{
	return Binding(
		inflateView = inflateView,
		onBindView = { item, binding, _ ->
			onBindView(item, binding)
		},
	)
}

@Suppress("FunctionName", "UNCHECKED_CAST")
inline fun <reified ItemT : Any, VB : ViewBinding> Binding(
	noinline inflateView: (LayoutInflater, ViewGroup, Boolean) -> VB,
	noinline onBindView: ArrayAdapter<ItemT>.(
		item: ItemT,
		binding: VB,
		position: Int,
	) -> Unit,
): BindingData<ItemT>
{
	return BindingData(
		itemClass = ItemT::class,
		inflateView = inflateView,
		onBindView = onBindView as ArrayAdapter<ItemT>.(
			item: ItemT,
			binding: ViewBinding,
			position: Int,
		) -> Unit,
	)
}

@Suppress("FunctionName", "UNCHECKED_CAST")
inline fun <reified ItemT : Any, VB : ViewBinding> Binding(
	noinline inflateView: (LayoutInflater, ViewGroup, Boolean) -> VB,
	noinline onBindView: ArrayAdapter<ItemT>.(
		item: ItemT,
		binding: VB,
		position: Int,
	) -> Unit,
	noinline onBindDropDownView: ArrayAdapter<ItemT>.(
		item: ItemT,
		binding: VB,
		position: Int,
	) -> Unit,
): BindingData<ItemT>
{
	return BindingData(
		itemClass = ItemT::class,
		inflateView = inflateView,
		onBindView = onBindView as ArrayAdapter<ItemT>.(
			item: ItemT,
			binding: ViewBinding,
			position: Int,
		) -> Unit,
		onBindDropDownView = onBindDropDownView as ArrayAdapter<ItemT>.(
			item: ItemT,
			binding: ViewBinding,
			position: Int,
		) -> Unit,
	)
}

@Suppress("FunctionName", "UNCHECKED_CAST")
inline fun <reified ItemT : Any, VB : ViewBinding> Binding(
	noinline inflateView: (LayoutInflater, ViewGroup, Boolean) -> VB,
	noinline onBindView: ArrayAdapter<ItemT>.(
		item: ItemT,
		binding: VB,
	) -> Unit,
	noinline onBindDropDownView: ArrayAdapter<ItemT>.(
		item: ItemT,
		binding: VB,
	) -> Unit,
): BindingData<ItemT>
{
	return BindingData(
		itemClass = ItemT::class,
		inflateView = inflateView,
		onBindView = { item, binding, _ ->
			onBindView(item, binding as VB)
		},
		onBindDropDownView = { item, binding, _ ->
			onBindDropDownView(item, binding as VB)
		},
	)
}

@Suppress("FunctionName", "UNCHECKED_CAST")
inline fun <reified ItemT : Any, VB : ViewBinding> Binding(
	noinline inflateView: (LayoutInflater, ViewGroup, Boolean) -> VB,
	noinline onBindView: ArrayAdapter<ItemT>.(
		item: ItemT,
		binding: VB,
		position: Int,
	) -> Unit,
	noinline onBindDropDownView: ArrayAdapter<ItemT>.(
		item: ItemT,
		binding: VB,
	) -> Unit,
): BindingData<ItemT>
{
	return BindingData(
		itemClass = ItemT::class,
		inflateView = inflateView,
		onBindView = onBindView as ArrayAdapter<ItemT>.(
			item: ItemT,
			binding: ViewBinding,
			position: Int,
		) -> Unit,
		onBindDropDownView = { item, binding, _ ->
			onBindDropDownView(item, binding as VB)
		},
	)
}

@Suppress("FunctionName", "UNCHECKED_CAST")
inline fun <reified ItemT : Any, VB : ViewBinding> Binding(
	noinline inflateView: (LayoutInflater, ViewGroup, Boolean) -> VB,
	noinline onBindView: ArrayAdapter<ItemT>.(
		item: ItemT,
		binding: VB,
	) -> Unit,
	noinline onBindDropDownView: ArrayAdapter<ItemT>.(
		item: ItemT,
		binding: VB,
		position: Int,
	) -> Unit,
): BindingData<ItemT>
{
	return BindingData(
		itemClass = ItemT::class,
		inflateView = inflateView,
		onBindView = { item, binding, _ ->
			onBindView(item, binding as VB)
		},
		onBindDropDownView = onBindDropDownView as ArrayAdapter<ItemT>.(
			item: ItemT,
			binding: ViewBinding,
			position: Int,
		) -> Unit,
	)
}

@Suppress("FunctionName", "UNCHECKED_CAST")
inline fun <reified ItemT : Any, ViewVB : ViewBinding, DropDownViewVB : ViewBinding> Binding(
	noinline inflateView: (LayoutInflater, ViewGroup, Boolean) -> ViewVB,
	noinline onBindView: ArrayAdapter<ItemT>.(
		item: ItemT,
		binding: ViewVB,
		position: Int,
	) -> Unit,
	noinline inflateDropDownView: ((LayoutInflater, ViewGroup, Boolean) -> ViewBinding),
	noinline onBindDropDownView: (ArrayAdapter<ItemT>.(
		item: ItemT,
		binding: DropDownViewVB,
		position: Int,
	) -> Unit)? = null,
): BindingData<ItemT>
{
	return BindingData(
		itemClass = ItemT::class,
		inflateView = inflateView,
		onBindView = onBindView as ArrayAdapter<ItemT>.(
			item: ItemT,
			binding: ViewBinding,
			position: Int,
		) -> Unit,
		inflateDropDownView = inflateDropDownView,
		onBindDropDownView = onBindDropDownView as ArrayAdapter<ItemT>.(
			item: ItemT,
			binding: ViewBinding,
			position: Int,
		) -> Unit,
	)
}

@Suppress("FunctionName", "UNCHECKED_CAST")
inline fun <reified ItemT : Any, ViewVB : ViewBinding, DropDownViewVB : ViewBinding> Binding(
	noinline inflateView: (LayoutInflater, ViewGroup, Boolean) -> ViewVB,
	noinline onBindView: ArrayAdapter<ItemT>.(
		item: ItemT,
		binding: ViewVB,
	) -> Unit,
	noinline inflateDropDownView: ((LayoutInflater, ViewGroup, Boolean) -> ViewBinding),
	noinline onBindDropDownView: (ArrayAdapter<ItemT>.(
		item: ItemT,
		binding: DropDownViewVB,
	) -> Unit)? = null,
): BindingData<ItemT>
{
	return BindingData(
		itemClass = ItemT::class,
		inflateView = inflateView,
		onBindView = { item, binding, _ ->
			onBindView(item, binding as ViewVB)
		},
		inflateDropDownView = inflateDropDownView,
		onBindDropDownView = { item, binding, _ ->
			onBindDropDownView?.invoke(this, item, binding as DropDownViewVB)
		},
	)
}

@Suppress("FunctionName", "UNCHECKED_CAST")
inline fun <reified ItemT : Any, ViewVB : ViewBinding, DropDownViewVB : ViewBinding> Binding(
	noinline inflateView: (LayoutInflater, ViewGroup, Boolean) -> ViewVB,
	noinline onBindView: ArrayAdapter<ItemT>.(
		item: ItemT,
		binding: ViewVB,
		position: Int,
	) -> Unit,
	noinline inflateDropDownView: ((LayoutInflater, ViewGroup, Boolean) -> ViewBinding),
	noinline onBindDropDownView: (ArrayAdapter<ItemT>.(
		item: ItemT,
		binding: DropDownViewVB,
	) -> Unit)? = null,
): BindingData<ItemT>
{
	return BindingData(
		itemClass = ItemT::class,
		inflateView = inflateView,
		onBindView = onBindView as ArrayAdapter<ItemT>.(
			item: ItemT,
			binding: ViewBinding,
			position: Int,
		) -> Unit,
		inflateDropDownView = inflateDropDownView,
		onBindDropDownView = { item, binding, _ ->
			onBindDropDownView?.invoke(this, item, binding as DropDownViewVB)
		},
	)
}

@Suppress("FunctionName", "UNCHECKED_CAST")
inline fun <reified ItemT : Any, ViewVB : ViewBinding, DropDownViewVB : ViewBinding> Binding(
	noinline inflateView: (LayoutInflater, ViewGroup, Boolean) -> ViewVB,
	noinline onBindView: ArrayAdapter<ItemT>.(
		item: ItemT,
		binding: ViewVB,
	) -> Unit,
	noinline inflateDropDownView: ((LayoutInflater, ViewGroup, Boolean) -> ViewBinding),
	noinline onBindDropDownView: (ArrayAdapter<ItemT>.(
		item: ItemT,
		binding: DropDownViewVB,
		position: Int,
	) -> Unit)? = null,
): BindingData<ItemT>
{
	return BindingData(
		itemClass = ItemT::class,
		inflateView = inflateView,
		onBindView = { item, binding, _ ->
			onBindView(item, binding as ViewVB)
		},
		inflateDropDownView = inflateDropDownView,
		onBindDropDownView = onBindDropDownView as ArrayAdapter<ItemT>.(
			item: ItemT,
			binding: ViewBinding,
			position: Int,
		) -> Unit,
	)
}

data class BindingData<out ItemT : Any>(
	val itemClass: KClass<@UnsafeVariance ItemT>,
	val inflateView: (LayoutInflater, ViewGroup, Boolean) -> ViewBinding,
	val onBindView: ArrayAdapter<@UnsafeVariance ItemT>.(
		item: @UnsafeVariance ItemT,
		binding: ViewBinding,
		position: Int,
	) -> Unit = { item, binding, _ -> defaultArrayAdapterViewBinding(item, binding) },
	val inflateDropDownView: ((LayoutInflater, ViewGroup, Boolean) -> ViewBinding)? = null,
	val onBindDropDownView: (ArrayAdapter<@UnsafeVariance ItemT>.(
		item: @UnsafeVariance ItemT,
		binding: ViewBinding,
		position: Int,
	) -> Unit)? = null,
)