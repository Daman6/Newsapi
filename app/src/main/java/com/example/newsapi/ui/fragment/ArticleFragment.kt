package com.example.newsapi.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.navigation.fragment.navArgs
import com.example.newsapi.NewsActivity
import com.example.newsapi.R
import com.example.newsapi.databinding.FragmentArticleBinding
import com.example.newsapi.model.Article
import com.example.newsapi.ui.NewsViewmodel
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.combine


class ArticleFragment : Fragment() {

    lateinit var viewmodel: NewsViewmodel
    val args: ArticleFragmentArgs by navArgs()
    lateinit var binding: FragmentArticleBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentArticleBinding.inflate(layoutInflater)
       return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewmodel = (activity as NewsActivity).viewmodel
        val article=args.article
        Toast.makeText(requireContext(), article.url+"fff", Toast.LENGTH_SHORT).show()
        binding.webView.apply {
            webViewClient = WebViewClient()
            loadUrl(article.url!!)
        }
        binding.fab.setOnClickListener{
            viewmodel.saveArticle(article)
            Snackbar.make(it,"SAVED",Snackbar.LENGTH_LONG).show()
        }
    }

}