package com.fightpandemics.createpost.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.fightpandemics.createpost.databinding.ItemMyOrganizationListBinding

class OrganizationAdapter(private val organizationArray: Array<Organization>) : RecyclerView.Adapter<OrganizationViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrganizationViewHolder {
        val binding = ItemMyOrganizationListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OrganizationViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return organizationArray.size
    }

    override fun onBindViewHolder(holder: OrganizationViewHolder, position: Int) {
        val organization = organizationArray[position]
        holder.bind(organization)
    }
}

class OrganizationViewHolder(val binding: ItemMyOrganizationListBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(organization: Organization) {
        val img = ResourcesCompat.getDrawable(binding.root.resources, organization.image, null)
        binding.organisationAvatar.setImageDrawable(img)
        binding.organisationName.text = organization.name
    }

}

data class Organization(var image: Int, var name: String)
