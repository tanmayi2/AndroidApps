package com.example.ingredientsapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import androidx.fragment.app.Fragment

class IngredientSlideFragment : Fragment() {
    var ingText : String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        var view : View = inflater.inflate(R.layout.ingredientlayout, container, false)
        var textView = view.findViewById<TextView>(R.id.ingredient_name)
        textView.text = ingText
        return view
    }
}