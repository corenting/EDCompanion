package fr.corenting.edcompanion.activities

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.LinearLayoutManager
import fr.corenting.edcompanion.adapters.CommunityGoalsAdapter
import fr.corenting.edcompanion.adapters.NewsAdapter
import fr.corenting.edcompanion.databinding.ActivityDetailsBinding
import fr.corenting.edcompanion.models.CommunityGoal
import fr.corenting.edcompanion.models.NewsArticle
import fr.corenting.edcompanion.utils.ThemeUtils

class DetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(ThemeUtils.getThemeToUse(this))
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // Set toolbar
        setSupportActionBar(binding.includeAppBar.toolbar)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Common recycler view setup
        val linearLayoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = linearLayoutManager

        // Get the goal or the article
        if (intent.extras != null) {
            val communityGoal = intent.extras?.getParcelable<CommunityGoal>("goal")
            val article = intent.extras?.getParcelable<NewsArticle>("article")
            if (communityGoal == null && article != null) {
                galnetArticleSetup(article)
            } else if (communityGoal != null) {
                communityGoalSetup(communityGoal)
            }
        }

        binding.recyclerView.smoothScrollToPosition(-10) // because recycler view may not start on top
    }

    private fun communityGoalSetup(communityGoal: CommunityGoal) {
        supportActionBar?.title = communityGoal.title

        // Adapter setup
        val adapter = CommunityGoalsAdapter(this, binding.recyclerView, true)
        binding.recyclerView.adapter = adapter
        adapter.submitList(listOf(communityGoal))
    }

    private fun galnetArticleSetup(article: NewsArticle) {
        supportActionBar?.title = article.title

        // Adapter view setup
        val adapter = NewsAdapter(this, binding.recyclerView, true)
        binding.recyclerView.adapter = adapter
        adapter.submitList(listOf(article))
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}
