package pl.jutupe.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import pl.jutupe.home.databinding.ItemWrapperBinding

class WrapperAdapter(
    private val adapter: RecyclerView.Adapter<*>,
    private val layoutManager: RecyclerView.LayoutManager,
    private val header: WrapperHeader? = null,
): RecyclerView.Adapter<WrapperAdapter.WrapperViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WrapperViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemWrapperBinding.inflate(inflater, parent, false)

        return WrapperViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WrapperViewHolder, position: Int) {
        holder.bind(layoutManager, adapter, header)
    }

    override fun getItemCount(): Int = 1

    class WrapperViewHolder(
        private val binding: ItemWrapperBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(
            manager: RecyclerView.LayoutManager,
            adapter: RecyclerView.Adapter<*>,
            header: WrapperHeader?
        ) {
            binding.manager = manager
            binding.adapter = adapter
            binding.header = header
            binding.executePendingBindings()
        }
    }

    data class WrapperHeader(val text: String)
}

fun RecyclerView.Adapter<*>.wrap(
    manager: RecyclerView.LayoutManager,
    header: WrapperAdapter.WrapperHeader? = null,
) = WrapperAdapter(this, manager, header)