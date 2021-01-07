package pl.jutupe.home.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import pl.jutupe.base.SingleLiveData
import timber.log.Timber

class SearchViewModel : ViewModel() {

    private var searchJob: Job? = null

    val events = SingleLiveData<SearchViewEvent>()

    fun onSearchTextChanged(text: String?) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(500)

            if (text.isNullOrBlank()) {
                Timber.d("searching stopped (text is null or blank)")
                events.value = SearchViewEvent.SetBackdropRecentlySearchedTitle

                //todo clear list (display recents)
            } else {
                Timber.d("searching for media items ($text)")
                events.value = SearchViewEvent.SetBackdropSearchTitle(text)
                //todo show found items
            }
        }
    }
}