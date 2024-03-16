package com.amm.valleytraildam.ui.view.adminview.viewholders

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.amm.valleytraildam.R
import com.amm.valleytraildam.databinding.UserItemBinding
import com.amm.valleytraildam.model.User

class AdminUsersViewHolder(private val binding: UserItemBinding,
                           private val listener: OnUserClickListener
) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {

    private var user: User? = null

    init {
        binding.root.setOnClickListener(this)
    }

    fun bind(user: User) {
        this.user = user
        binding.tvUserEmail.text = user.email
        binding.tvUserName.text = user.name
        binding.tvUserPhone.text = binding.root.context.getString (R.string.tel, user.phone)
        binding.tvUserNif.text = binding.root.context.getString(R.string.dni, user.nif)
    }

    override fun onClick(v: View?) {
        user?.let {
            listener.onUserClick(it)
        }
    }

    interface OnUserClickListener {
        fun onUserClick(user: User)
    }

    companion object {
        fun create(parent: ViewGroup, listener: OnUserClickListener): AdminUsersViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = UserItemBinding.inflate(inflater, parent, false)
            return AdminUsersViewHolder(binding, listener)
        }
    }
}