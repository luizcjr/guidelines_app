package com.luiz.guidelines.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.luiz.guidelines.R
import com.luiz.guidelines.databinding.ItemsGuidelineBinding
import com.luiz.guidelines.models.Guidelines
import com.luiz.guidelines.ui.interfaces.GuidelinesListener

class GuidelinesAdapter(
    private val context: Context,
    private val guidelines: ArrayList<Guidelines>,
    private val listener: GuidelinesListener
) : RecyclerView.Adapter<GuidelinesAdapter.GuidelinesViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GuidelinesViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = DataBindingUtil.inflate<ItemsGuidelineBinding>(
            inflater,
            R.layout.items_guideline,
            parent,
            false
        )
        return GuidelinesViewHolder(view)
    }

    override fun getItemCount() = guidelines.size

    override fun onBindViewHolder(holder: GuidelinesViewHolder, position: Int) {
        val itemAtual = guidelines[position]
        holder.view.guidelines = itemAtual

        holder.view.clGuideline.setOnClickListener {
            val expaned = itemAtual.expanded

            itemAtual.expanded = !expaned

            notifyItemChanged(position)

            for (i in 0 until guidelines.size) {
                val otherSchedule: Guidelines = guidelines[i]
                if (otherSchedule.id != itemAtual.id) {
                    otherSchedule.expanded = false
                    notifyItemChanged(i)
                }
            }
        }

        holder.view.clCollapse.visibility =
            if (itemAtual.expanded) View.VISIBLE else View.GONE

        holder.view.btnAction.setOnClickListener {
            listener.action(position)
        }

        if (itemAtual.isOpen) {
            holder.view.btnAction.setBackgroundResource(R.drawable.bcg_btn_red)
            holder.view.btnAction.text = "Finalizar"
        } else {
            holder.view.btnAction.setBackgroundResource(R.drawable.bcg_btn_primary)
            holder.view.btnAction.text = "Reabrir"
        }
    }

    class GuidelinesViewHolder(var view: ItemsGuidelineBinding) : RecyclerView.ViewHolder(view.root)
}