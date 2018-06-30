package fr.corenting.edcompanion.models;

public class Ranks extends BaseModel {

    public Rank Combat;
    public Rank Trade;
    public Rank Explore;
    public Rank Cqc;

    public Rank Federation;
    public Rank Empire;

    public Ranks() {
        Combat = new Rank();
        Trade = new Rank();
        Explore = new Rank();
        Cqc = new Rank();
        Federation = new Rank();
        Empire = new Rank();
    }

    public class Rank {
        public String name;
        public int value;
        public int progress;
    }
}
