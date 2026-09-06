package com.example.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull

class LearningRepository(private val learningDao: LearningDao) {

    val userStatsFlow: Flow<UserStats?> = learningDao.getUserStatsFlow()
    val chatMessagesFlow: Flow<List<ChatMessage>> = learningDao.getChatMessages()

    suspend fun getOrCreateUserStats(): UserStats {
        val existing = learningDao.getUserStats()
        if (existing != null) {
            return existing
        } else {
            val defaultStats = UserStats()
            learningDao.insertUserStats(defaultStats)
            return defaultStats
        }
    }

    suspend fun completeLesson(lessonId: String, xpReward: Int = 20) {
        val stats = getOrCreateUserStats()
        val completedList = stats.completedLessons.split(",").filter { it.isNotEmpty() }.toMutableSet()
        
        if (!completedList.contains(lessonId)) {
            completedList.add(lessonId)
            val newCompletedLessons = completedList.joinToString(",")
            val newXp = stats.xp + xpReward
            val newLevel = (newXp / 100) + 1
            
            val updated = stats.copy(
                xp = newXp,
                level = newLevel,
                completedLessons = newCompletedLessons,
                lastActiveTimestamp = System.currentTimeMillis()
            )
            learningDao.insertUserStats(updated)
        }
    }

    suspend fun addQuizResult(isCorrect: Boolean, xpReward: Int = 10) {
        val stats = getOrCreateUserStats()
        val incrementXp = if (isCorrect) xpReward else 0
        val newXp = stats.xp + incrementXp
        val newLevel = (newXp / 100) + 1
        
        val updated = stats.copy(
            xp = newXp,
            level = newLevel,
            quizTotal = stats.quizTotal + 1,
            quizCorrect = stats.quizCorrect + if (isCorrect) 1 else 0,
            lastActiveTimestamp = System.currentTimeMillis()
        )
        learningDao.insertUserStats(updated)
    }

    suspend fun recordStreak() {
        val stats = getOrCreateUserStats()
        val currentMillis = System.currentTimeMillis()
        val divUnit = 24 * 60 * 60 * 1000L // milliseconds in a day
        val lastDay = stats.lastActiveTimestamp / divUnit
        val today = currentMillis / divUnit
        
        val newStreak = when {
            today == lastDay -> stats.streak // Already active today
            today == lastDay + 1 -> stats.streak + 1 // Consecutive day
            else -> 1 // Streak broken or first time
        }
        
        val updated = stats.copy(
            streak = if (newStreak == 0) 1 else newStreak,
            lastActiveTimestamp = currentMillis
        )
        learningDao.insertUserStats(updated)
    }

    suspend fun addXp(amount: Int) {
        val stats = getOrCreateUserStats()
        val newXp = stats.xp + amount
        val newLevel = (newXp / 100) + 1
        val updated = stats.copy(
            xp = newXp,
            level = newLevel,
            lastActiveTimestamp = System.currentTimeMillis()
        )
        learningDao.insertUserStats(updated)
    }

    suspend fun insertChatMessage(sender: String, text: String) {
        val msg = ChatMessage(sender = sender, message = text)
        learningDao.insertChatMessage(msg)
    }

    suspend fun clearChat() {
        learningDao.clearChatMessages()
    }

    suspend fun resetAll() {
        learningDao.clearChatMessages()
        val freshStats = UserStats(
            id = 1,
            level = 1,
            xp = 0,
            completedLessons = "",
            streak = 1,
            lastActiveTimestamp = System.currentTimeMillis(),
            quizTotal = 0,
            quizCorrect = 0
        )
        learningDao.insertUserStats(freshStats)
    }
}
