package krasnikov.project.animationapp.data

import androidx.recyclerview.widget.DiffUtil
import krasnikov.project.animationapp.data.model.Country

class CountryDiffUtilCallback(
    private val oldList: List<Country>,
    private val newList: List<Country>
) :
    DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldCountry = oldList[oldItemPosition]
        val newCountry = newList[newItemPosition]

        return oldCountry.name == newCountry.name
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldCountry = oldList[oldItemPosition]
        val newCountry = newList[newItemPosition]

        return oldCountry.area == newCountry.area
                && oldCountry.population == newCountry.population
                && oldCountry.flag == newCountry.flag
    }
}