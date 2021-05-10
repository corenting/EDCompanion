package fr.corenting.edcompanion.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CommunityGoalReward(val contributors: String, val rewards: String,
                               val tier: String) : Parcelable