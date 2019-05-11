package net.blakelee.calllog

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_call_details.view.*

class CallLogAdapter : RecyclerView.Adapter<CallLogAdapter.CallLogViewHolder>() {

    private val items = mutableListOf<CallDetails>()

    private fun getItemViewId() = R.layout.item_call_details

    override fun getItemCount(): Int = items.count()

    override fun onBindViewHolder(holder: CallLogViewHolder, position: Int) {
        holder.onBind(items[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CallLogViewHolder =
        CallLogViewHolder(LayoutInflater.from(parent.context).inflate(getItemViewId(), parent, false))


    fun setItems(items: List<CallDetails>) {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    inner class CallLogViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val phoneNumberText: TextView = view.phoneNumber
        private val dateText: TextView = view.date
        private val directionText: TextView = view.direction

        fun onBind(item: CallDetails) {
            with(item) {
                phoneNumberText.text = phoneNumber
                dateText.text = date
                directionText.text = direction
            }
        }
    }
}