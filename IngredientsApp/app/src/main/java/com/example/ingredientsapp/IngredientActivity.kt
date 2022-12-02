package com.example.ingredientsapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2

class IngredientActivity : FragmentActivity()  {

    private var numPages : Int? = null
    private lateinit var viewPager: ViewPager2
    var ingredientList : ArrayList<String> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ingredient)

        val text = intent?.extras?.getString("TextString") as String
        var ingredient : String = ""

        if (text != null) {
            for (i in 0..text.length-1) {
                if(text[i] == ',' || text[i] == '[' || text[i] == ']' || text[i] == '(' || text[i] == ')' || i == text.length-1) {
                    ingredient = ingredient.trim()
                    if(ingredient != "") {
                        ingredientList.add(ingredient)
                        //showToast(ingredient)
                        Log.d(TAG, ingredient)
                    }
                    ingredient = ""
                } else {
                   ingredient += text[i]
                }
            }
            numPages = ingredientList.size
        }

        // Instantiate a ViewPager2 and a PagerAdapter.
        viewPager = findViewById<ViewPager2>(R.id.pager)

        // The pager adapter, which provides the pages to the view pager widget.
        val pagerAdapter = ScreenSlidePagerAdapter(this)
        pagerAdapter.numberOfScreens = numPages!!
        viewPager.adapter = pagerAdapter

        //TODO: format the ViewPager2 slider system, remove unnecessary words from ingredients, add photo cropping between taking the photo and scanning it

    }

    private fun showToast(message: String) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show()
    }

    /**
     * A simple pager adapter that represents 5 ScreenSlidePageFragment objects, in
     * sequence.
     */
    private inner class ScreenSlidePagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {
        var numberOfScreens : Int = 0
        var arrayList : ArrayList<String> = ingredientList
        override fun getItemCount(): Int = numberOfScreens

        override fun createFragment(position: Int): Fragment {
            var frag : IngredientSlideFragment = IngredientSlideFragment()
            frag.ingText = ingredientList[position]
            return frag
        }
    }

    companion object {
        private const val TAG = "IngredientActivity"
    }
}