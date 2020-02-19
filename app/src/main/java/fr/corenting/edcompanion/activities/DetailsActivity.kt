package fr.corenting.edcompanion.activities

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import butterknife.ButterKnife
import fr.corenting.edcompanion.R
import fr.corenting.edcompanion.adapters.CommunityGoalsAdapter
import fr.corenting.edcompanion.adapters.NewsAdapter
import fr.corenting.edcompanion.models.CommunityGoal
import fr.corenting.edcompanion.models.NewsArticle
import fr.corenting.edcompanion.utils.ThemeUtils
import kotlinx.android.synthetic.main.activity_details.*

class DetailsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        ThemeUtils.setTheme(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
        ButterKnife.bind(this)

        // Set toolbar
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Common recycler view setup
        val linearLayoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = linearLayoutManager

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

        recyclerView.smoothScrollToPosition(-10) // because recycler view may not start on top
    }

    private fun communityGoalSetup(communityGoal: CommunityGoal) {
        supportActionBar?.title = communityGoal.title

        // Adapter setup
        val adapter = CommunityGoalsAdapter(this, recyclerView, true)
        recyclerView.adapter = adapter
        adapter.submitList(listOf(communityGoal))
    }

    private fun galnetArticleSetup(article: NewsArticle) {
        supportActionBar?.title = article.title

        // Adapter view setup
        val adapter = NewsAdapter(this, recyclerView, true)
        recyclerView.adapter = adapter
        adapter.submitList(listOf(article))
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}
