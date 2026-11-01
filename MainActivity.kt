package com.example.fortnitescreenobserver

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast

class MainActivity : Activity() {
    private lateinit var preview: ImageView
    private val pickImage = 42

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER_HORIZONTAL
            setPadding(24, 32, 24, 24)
        }

        val title = TextView(this).apply {
            text = "Screen Observer"
            textSize = 26f
        }
        val note = TextView(this).apply {
            text = "Prototype d’analyse manuelle de captures. Cette application ne contrôle pas Fortnite et n’automatise pas les parties."
            textSize = 16f
            setPadding(0, 16, 0, 16)
        }
        val choose = Button(this).apply {
            text = "Choisir une capture d’écran"
            setOnClickListener {
                val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                    addCategory(Intent.CATEGORY_OPENABLE)
                    type = "image/*"
                }
                startActivityForResult(intent, pickImage)
            }
        }
        preview = ImageView(this).apply {
            adjustViewBounds = true
            scaleType = ImageView.ScaleType.FIT_CENTER
            contentDescription = "Aperçu de la capture choisie"
        }

        root.addView(title)
        root.addView(note)
        root.addView(choose)
        root.addView(preview, LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, 0, 1f
        ))
        setContentView(root)
    }

    @Deprecated("Deprecated in Android API, kept for broad compatibility")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == pickImage && resultCode == RESULT_OK) {
            val uri: Uri = data?.data ?: return
            try {
                contentResolver.openInputStream(uri)?.use { stream ->
                    preview.setImageBitmap(BitmapFactory.decodeStream(stream))
                }
            } catch (_: Exception) {
                Toast.makeText(this, "Impossible d’ouvrir cette image.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
