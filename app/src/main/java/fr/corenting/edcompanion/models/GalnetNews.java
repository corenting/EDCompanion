package fr.corenting.edcompanion.models;


import java.util.List;

public class GalnetNews extends BaseModel {
    public List<GalnetArticle> Articles;

    public GalnetNews(boolean success, List<GalnetArticle> articles)
    {
        this.Success = success;
        this.Articles = articles;
    }
}
