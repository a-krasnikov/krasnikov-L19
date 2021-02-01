package krasnikov.project.animationapp.ui.countries

import android.graphics.drawable.PictureDrawable
import android.os.Bundle
import android.util.Pair
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import androidx.recyclerview.widget.DiffUtil
import androidx.transition.TransitionInflater
import com.bumptech.glide.ListPreloader.PreloadModelProvider
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.integration.recyclerview.RecyclerViewPreloader
import com.bumptech.glide.util.ViewPreloadSizeProvider
import krasnikov.project.animationapp.App
import krasnikov.project.animationapp.CountriesViewModel
import krasnikov.project.animationapp.R
import krasnikov.project.animationapp.data.CountryDiffUtilCallback
import krasnikov.project.animationapp.data.model.Country
import krasnikov.project.animationapp.databinding.FragmentCountryListBinding
import krasnikov.project.animationapp.ui.country.CountryInfoFragment
import krasnikov.project.animationapp.utils.GlideApp

class CountryListFragment : Fragment(), CountryAdapter.CountryItemClickListener {

    private lateinit var binding: FragmentCountryListBinding

    private val viewModel by activityViewModels<CountriesViewModel> {
        CountriesViewModel.CountriesViewModelFactory(
            (requireActivity().application as App).countryRepository
        )
    }

    private val requestBuilder: RequestBuilder<PictureDrawable> by lazy {
        GlideApp.with(requireContext()).`as`(PictureDrawable::class.java)
            .error(R.drawable.ic_error)
    }

    private val adapter by lazy { CountryAdapter(requestBuilder) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCountryListBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupAdapter()
        prepareTransitions()
    }

    override fun onCountryItemClick(
        country: Country,
        pairImage: Pair<View, String>,
        pairTitle: Pair<View, String>
    ) {
        navigateToCountryInfo(country, pairImage, pairTitle)
    }

    private fun setupAdapter() {
        val sizeProvider = ViewPreloadSizeProvider<Country>()
        val modelProvider: PreloadModelProvider<Country> = adapter
        val preloader = RecyclerViewPreloader(
            GlideApp.with(this), modelProvider, sizeProvider, 10
        )
        binding.rvMain.addOnScrollListener(preloader)

        viewModel.observeCountries.observe(viewLifecycleOwner) {
            val diffUtilCallback = CountryDiffUtilCallback(adapter.items, it)
            val diffResult = DiffUtil.calculateDiff(diffUtilCallback)

            adapter.items = it
            diffResult.dispatchUpdatesTo(adapter)
        }

        adapter.listener = this
        binding.rvMain.adapter = adapter
    }

    private fun prepareTransitions() {
        exitTransition = TransitionInflater.from(context)
            .inflateTransition(android.R.transition.move)
    }

    private fun navigateToCountryInfo(
        country: Country,
        pairImage: Pair<View, String>,
        pairTitle: Pair<View, String>
    ) {
        parentFragmentManager.commit {
            val fragment = CountryInfoFragment.newInstance(country.name)
            addSharedElement(pairImage.first, pairImage.second)
            addSharedElement(pairTitle.first, pairTitle.second)
            setCustomAnimations(
                R.anim.slide_in_bottom,
                R.anim.slide_out_top,
                R.anim.slide_in_bottom,
                R.anim.slide_out_top
            )
            addToBackStack("CountryListFragment")
            replace(R.id.fragment_container, fragment)
            setReorderingAllowed(true)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = CountryListFragment()
    }
}