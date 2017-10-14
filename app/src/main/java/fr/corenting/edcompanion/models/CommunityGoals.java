package fr.corenting.edcompanion.models;

import java.util.List;

public class CommunityGoals extends BaseModel {
    public List<CommunityGoal> GoalsList;

    public CommunityGoals(boolean success, List<CommunityGoal> goals)
    {
        this.Success = success;
        this.GoalsList = goals;
    }
}
