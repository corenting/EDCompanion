package fr.corenting.edcompanion.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CommunityGoalReward(val contributors: String, val rewards: String,
                               val tier: String) : Parcelable