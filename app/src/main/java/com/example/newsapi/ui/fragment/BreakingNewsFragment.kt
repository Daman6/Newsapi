package com.example.newsapi.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsapi.NewsActivity
import com.example.newsapi.R
import com.example.newsapi.adapter.NewsAdapter
import com.example.newsapi.databinding.FragmentBreakingNewsBinding
import com.example.newsapi.ui.NewsViewmodel
import com.example.newsapi.util.Resource


class BreakingNewsFragment : Fragment() {


    private lateinit var viewmodel:NewsViewmodel
    private lateinit var binding: FragmentBreakingNewsBinding
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
        binding = FragmentBreakingNewsBinding.inflate(layoutInflater)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        setUpRecyclerView()

        newsAdapter.setOnItemClickListener {
            val bundle= Bundle().apply {
                putSerializable("article",it)
            }
            findNavController().navigate(R.id.action_breakingNewsFragment_to_articleFragment,bundle )
        }


        viewmodel.breakingNews.observe(viewLifecycleOwner, Observer {response->
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
                        Toast.makeText(requireContext(),"Error occured $message",Toast.LENGTH_LONG).show()
                    }
                }
                is Resource.Loading ->{
                    showProgessBar()
                }
            }

        })


    }

    private fun hideProgressBar() {
        binding.paginationProgressBar.visibility = View.INVISIBLE
        Toast.makeText(requireContext(),"Progessbar Hide",Toast.LENGTH_LONG).show()

    }
    private fun showProgessBar(){
        binding.paginationProgressBar.visibility = View.VISIBLE
        Toast.makeText(requireContext(),"Progessbar Shown",Toast.LENGTH_LONG).show()
    }

    private fun setUpRecyclerView(){
        newsAdapter = NewsAdapter()
        binding.rvBreakingNews.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

}