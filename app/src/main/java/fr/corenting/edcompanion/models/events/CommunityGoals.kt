package fr.corenting.edcompanion.models.events

import fr.corenting.edcompanion.models.CommunityGoal

data class CommunityGoals(val success: Boolean, val goalsList: List<CommunityGoal>)
