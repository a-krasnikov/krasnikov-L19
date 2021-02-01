package krasnikov.project.animationapp.ui.country

import android.graphics.drawable.PictureDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.transition.TransitionInflater
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import krasnikov.project.animationapp.CountriesViewModel
import krasnikov.project.animationapp.R
import krasnikov.project.animationapp.databinding.FragmentCountryInfoBinding
import krasnikov.project.animationapp.utils.GlideApp

class CountryInfoFragment : Fragment() {

    private lateinit var binding: FragmentCountryInfoBinding

    private val viewModel by activityViewModels<CountriesViewModel>()

    private val requestBuilder: RequestBuilder<PictureDrawable> by lazy {
        GlideApp.with(requireContext()).`as`(PictureDrawable::class.java)
            .error(R.drawable.ic_error)
            .dontAnimate()
    }

    private val countryName by lazy { requireArguments().getString(ARG_NAME) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Avoid a postponeEnterTransition on orientation change, and postpone only of first creation.
        if (savedInstanceState == null) {
            postponeEnterTransition();
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCountryInfoBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prepareSharedElementTransition()
        setData()
    }

    private fun prepareSharedElementTransition() {
        binding.ivFlag.transitionName = getString(R.string.transition_icon, countryName)
        binding.tvName.transitionName = getString(R.string.transition_text, countryName)

        sharedElementEnterTransition = TransitionInflater.from(requireContext())
            .inflateTransition(android.R.transition.move)
    }

    private fun setData() {
        countryName?.let { viewModel.observeCountry(it) }?.observe(viewLifecycleOwner) {
            loadImage(it.flag)
            binding.tvName.text = it.name
            binding.tvArea.text = getString(R.string.text_area, it.area)
            binding.tvPopulation.text = getString(R.string.text_population, it.population)
            binding.tvPopulationDensity.text =
                getString(R.string.text_population_density, it.populationDensity)
        }
    }

    private fun loadImage(url: String) {
        requestBuilder.load(url).listener(object : RequestListener<PictureDrawable> {
            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<PictureDrawable>?,
                isFirstResource: Boolean
            ): Boolean {
                startPostponedEnterTransition()
                return false
            }

            override fun onResourceReady(
                resource: PictureDrawable?,
                model: Any?,
                target: Target<PictureDrawable>?,
                dataSource: DataSource?,
                isFirstResource: Boolean
            ): Boolean {
                startPostponedEnterTransition()
                return false
            }
        }).into(binding.ivFlag)
    }

    companion object {
        private const val ARG_NAME = "name"

        @JvmStatic
        fun newInstance(nameCountry: String) = CountryInfoFragment().apply {
            arguments = Bundle().apply {
                putString(ARG_NAME, nameCountry)
            }
        }
    }
}