package com.vj.navigationcomponent.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.RecyclerView
import com.vj.navigationcomponent.databinding.LayoutItemBinding
import com.vj.navigationcomponent.model.RepoResponse
import com.vj.navigationcomponent.viewmodel.RepoViewModel
import dagger.hilt.android.AndroidEntryPoint

class RepoAdapter(private val repoItemClickListener: RepoItemClickListener) :
    RecyclerView.Adapter<RepoAdapter.DataViewHolder>() {

    private val gitUsers: ArrayList<RepoResponse> = arrayListOf()

    inner class DataViewHolder(itemView: LayoutItemBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        var layoutItemBinding: LayoutItemBinding = itemView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder =
        DataViewHolder(
            LayoutItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun getItemCount(): Int = gitUsers.size

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        holder.layoutItemBinding.repoList = gitUsers[position]
        holder.layoutItemBinding.repoItemClickListener = repoItemClickListener
        holder.layoutItemBinding.executePendingBindings()
    }

    fun addGitUsers(gitUsers: ArrayList<RepoResponse>) {
        this.gitUsers.apply {
            clear()
            addAll(gitUsers)
        }
    }


    interface RepoItemClickListener {
        fun onRepoItemClicked(name: String, avatar: String)
    }
}

