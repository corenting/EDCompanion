package fr.corenting.edcompanion.models;

public class Ranks extends BaseModel {

    public Rank combat;
    public Rank trade;
    public Rank explore;
    public Rank cqc;

    public Rank federation;
    public Rank empire;

    public Ranks() {
        combat = new Rank();
        trade = new Rank();
        explore = new Rank();
        cqc = new Rank();
        federation = new Rank();
        empire = new Rank();
    }

    public class Rank {
        public String name;
        public int value;
        public int progress;
    }
}
