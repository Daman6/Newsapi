package com.example.newsapi.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsapi.NewsActivity
import com.example.newsapi.R
import com.example.newsapi.adapter.NewsAdapter
import com.example.newsapi.databinding.FragmentBreakingNewsBinding
import com.example.newsapi.databinding.FragmentSearchNewsBinding
import com.example.newsapi.ui.NewsViewmodel
import com.example.newsapi.util.Resource
import kotlinx.coroutines.*


class SearchNewsFragment : Fragment() {


    private lateinit var viewmodel: NewsViewmodel
    private lateinit var binding: FragmentSearchNewsBinding
    private lateinit var newsAdapter: NewsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSearchNewsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewmodel = (activity as NewsActivity).viewmodel

        var job: Job?=null
        binding.etSearch.addTextChangedListener {editable->
            job?.cancel()
            job = MainScope().launch {
                delay(500L)
                editable?.let {
                    if (editable.toString().isNotEmpty()){
                        viewmodel.searchNews(editable.toString())
                    }
                }
            }

        }

        setUpRecyclerView()
        viewmodel.searchNews.observe(viewLifecycleOwner, Observer {response->
            when(response){
                is Resource.Success ->{
                    hideProgressBar()
                    response.data?.let { newsResponse ->
                        newsAdapter.differ.submitList(newsResponse.articles)
                    }
                }
                is Resource.Error ->{
                    hideProgressBar()
                    response.message.let { message ->
                        Toast.makeText(requireContext(),"Error occured $message", Toast.LENGTH_LONG).show()
                    }
                }
                is Resource.Loading ->{
                    showProgessBar()
                }
            }

        })
    }

    private fun hideProgressBar() {
        Toast.makeText(requireContext(),"Progessbar Hide",Toast.LENGTH_LONG).show()

    }
    private fun showProgessBar(){
        Toast.makeText(requireContext(),"Progessbar Shown",Toast.LENGTH_LONG).show()
    }

    private fun setUpRecyclerView(){
        newsAdapter = NewsAdapter()
        binding.rvSearchNews.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }


}