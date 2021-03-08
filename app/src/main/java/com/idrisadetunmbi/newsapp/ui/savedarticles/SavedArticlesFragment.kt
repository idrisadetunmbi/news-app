package com.idrisadetunmbi.newsapp.ui.savedarticles

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.idrisadetunmbi.newsapp.R
import com.idrisadetunmbi.newsapp.databinding.FragmentBrowseArticlesBinding
import com.idrisadetunmbi.newsapp.models.Article
import com.idrisadetunmbi.newsapp.ui.ArticlesAdapter
import com.idrisadetunmbi.newsapp.utils.navController
import com.idrisadetunmbi.newsapp.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import io.cabriole.decorator.LinearMarginDecoration
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class SavedArticlesFragment : Fragment(R.layout.fragment_browse_articles),
    ArticlesAdapter.ItemCallback {

    private val binding by viewBinding(FragmentBrowseArticlesBinding::bind)
    private val viewModel by viewModels<SavedArticlesViewModel>()
    private val articlesAdapter by lazy { ArticlesAdapter(this) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()

        viewModel.state
            .onEach { articlesAdapter.submitList(it.articles) }
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun initViews() {
        binding.toolbar.setupWithNavController(
            navController,
            AppBarConfiguration(setOf(R.id.browse_articles_nav, R.id.saved_articles_nav))
        )
        binding.swrContainer.isEnabled = false
        binding.rvArticles.apply {
            adapter = articlesAdapter
            addItemDecoration(
                LinearMarginDecoration.create(
                    margin = resources.getDimensionPixelOffset(R.dimen.spacing_two),
                    orientation = LinearLayoutManager.VERTICAL
                )
            )
        }
    }

    override fun onClickItem(article: Article) {
        val dir = SavedArticlesFragmentDirections
            .actionSavedArticlesNavToArticleDetailsFragment(article)
        findNavController().navigate(dir)
    }

    override fun onClickBookmarkItem(article: Article) {
        viewModel.handleBookmarkArticle(article)
    }
}
