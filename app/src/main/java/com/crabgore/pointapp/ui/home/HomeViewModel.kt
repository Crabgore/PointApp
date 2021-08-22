package com.crabgore.pointapp.ui.home

import androidx.lifecycle.MutableLiveData
import com.crabgore.pointapp.data.SearchResponse
import com.crabgore.pointapp.domain.repositories.GitRepository
import com.crabgore.pointapp.ui.base.BaseViewModel
import com.crabgore.pointapp.ui.items.UserItem
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import java.util.concurrent.TimeUnit

class HomeViewModel(
    private val repository: GitRepository
) : BaseViewModel() {
    val searchedUsers: MutableLiveData<List<UserItem>> = MutableLiveData()
    val additionalUsers: MutableLiveData<List<UserItem>> = MutableLiveData()
    val failures: MutableLiveData<Boolean> = MutableLiveData()

    fun searchUser(q: String?) {
        val disposable = repository.searchUser(q, 1)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError(::onError)
            .delay(600, TimeUnit.MILLISECONDS)
            .subscribe(::parseSearchResponse, ::handleFailure)

        addDisposable(disposable)
    }

    fun additionalSearchUsers(q: String?, page: Int) {
        val disposable = repository.searchUser(q, page)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError(::onError)
            .subscribe(::parseAdditionalSearchResponse, ::handleFailure)

        addDisposable(disposable)
    }

    private fun onError(t: Throwable) {
        Timber.d("Произошла ошибка поиска ${t.localizedMessage}")
        t.localizedMessage?.let {
            if (it.contains("403")) failures.value = true
        }
    }

    private fun parseSearchResponse(response: SearchResponse) {
        Timber.d("Список пользователей $response")
        searchedUsers.postValue(makeItemsList(response))
    }

    private fun parseAdditionalSearchResponse(response: SearchResponse) {
        Timber.d("Дополнительный список пользователей $response")
        additionalUsers.postValue(makeItemsList(response))
    }

    private fun makeItemsList(response: SearchResponse): List<UserItem> {
        val list: MutableList<UserItem> = mutableListOf()
        response.items.forEach { user ->
            list.add(
                UserItem(
                    user.id,
                    user.login,
                    user.avatarURL
                )
            )
        }
        return list
    }
}