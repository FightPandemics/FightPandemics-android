package com.fightpandemics.search.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fightpandemics.core.dagger.scope.FeatureScope
import timber.log.Timber
import javax.inject.Inject

@FeatureScope
class SearchViewModel @Inject constructor() : ViewModel(){

    private val _filteredPosts = MutableLiveData<MutableList<String>>()
    val filteredPosts: LiveData<MutableList<String>>
        get() = _filteredPosts

    private val _allPosts = MutableLiveData<MutableList<String>>()
    val allPosts: LiveData<MutableList<String>>
        get() = _allPosts

    init {
        _filteredPosts.value = mutableListOf()
        _allPosts.value = mutableListOf()
    }

    fun filterPosts(query: String){
        Timber.i("Debug: query: $query filteredposts: ${_filteredPosts.value}, full: ${_allPosts.value}")
        if (query.isBlank()){
            _filteredPosts.value = _allPosts.value!!.toMutableList()
        } else{
            val tempList = mutableListOf<String>()
            _allPosts.value?.let {
                for (post in it){
                    if (query in post){
                        tempList.add(post)
                    }
                }
                Timber.i("Debug: templist: $tempList,  filteredposts: ${_filteredPosts.value}, full: ${_allPosts.value}")
                _filteredPosts.value!!.clear()
                _filteredPosts.value = tempList
                Timber.i("Debug after clear: templist: $tempList,  filteredposts: ${_filteredPosts.value}, full: ${_allPosts.value}")
            }
        }
    }

    fun loadAllPosts(){
        // todo this does API call and updates the live data
        val postsData = mutableListOf<String>()
        for(i in 1..10){
            postsData.add("Post Number: $i")
        }
        _allPosts.value = postsData.toMutableList()
        _filteredPosts.value = postsData.toMutableList()
    }

}
