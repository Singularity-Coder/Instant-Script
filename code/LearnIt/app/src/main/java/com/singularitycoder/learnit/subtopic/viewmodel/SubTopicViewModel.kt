package com.singularitycoder.learnit.subtopic.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.singularitycoder.learnit.subject.model.Subject
import com.singularitycoder.learnit.subtopic.dao.SubTopicDao
import com.singularitycoder.learnit.subtopic.model.SubTopic
import com.singularitycoder.learnit.topic.model.Topic
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SubTopicViewModel @Inject constructor(
    private val subTopicDao: SubTopicDao,
) : ViewModel() {

    fun addSubTopicItem(subTopic: SubTopic) = viewModelScope.launch {
        subTopicDao.insert(subTopic)
    }

    fun updateAllSubTopics(list: List<SubTopic>) = viewModelScope.launch {
        subTopicDao.updateAll(list)
    }

    fun updateSubTopic(subTopic: SubTopic?) = viewModelScope.launch {
        subTopic ?: return@launch
        subTopicDao.update(subTopic)
    }

    fun getAllTopicByTopicIdItemsFlow(topicId: Long?): Flow<List<SubTopic>> {
        topicId ?: return emptyFlow()
        return subTopicDao.getAllItemsByTopicIdStateFlow(topicId)
    }

    fun getAllSubTopicItemsFlow() = subTopicDao.getAllItemsStateFlow()

    suspend fun getAllBookItems() = subTopicDao.getAll()

//    suspend fun hasBooks() = subTopicDao.hasItems()

    suspend fun getBookItemById(id: String) = subTopicDao.getItemById(id)

    suspend fun getBookDataItemById(id: String) = subTopicDao.getItemById(id)

//    suspend fun updateCompletedPageWithId(completedPage: Int, id: String) = bookDao.updateCompletedPageWithId(completedPage, id)

//    suspend fun getLast3BookItems() = bookDao.getLast3By()

    fun deleteSubTopic(subTopic: SubTopic?) = viewModelScope.launch {
        subTopicDao.delete(subTopic ?: return@launch)
    }

//    fun deleteBookDataItem(subject: Subject?) = viewModelScope.launch {
//        subTopicDao.deleteBy(subject?.id ?: return@launch)
//    }

    fun deleteAllBookItems() = viewModelScope.launch {
        subTopicDao.deleteAll()
    }

    fun deleteAllBookDataItems() = viewModelScope.launch {
        subTopicDao.deleteAll()
    }

    fun deleteAllSubTopicsBy(topicId: Long?) = viewModelScope.launch {
        topicId ?: return@launch
        subTopicDao.deleteAllByTopicId(topicId)
    }
}
