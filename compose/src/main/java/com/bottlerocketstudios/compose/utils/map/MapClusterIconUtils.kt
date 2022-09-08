package com.bottlerocketstudios.compose.utils.map

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.OvalShape
import android.util.SparseArray
import android.view.ViewGroup
import androidx.core.util.isNotEmpty
import com.bottlerocketstudios.compose.R
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.maps.android.clustering.Cluster
import com.google.maps.android.ui.IconGenerator
import kotlin.math.pow

// This is used to determine which bucket each cluster would go in.
private val buckets = intArrayOf(10, 20, 50, 100, 200, 500, 1000)
private val icons = SparseArray<BitmapDescriptor>()
private var circleBackground: ShapeDrawable? = null
private const val DEFAULT_SATURATION = 1f
private const val DEFAULT_VALUE = .6f
private const val HUE_RANGE = 220f
private const val SIZE_RANGE = 300f
private const val PADDING = 12
private const val INDEX_ONE = 1
private const val TRANSPARENT_WHITE = -0x7f000001
private const val STROKE_MODIFIER = 3
fun clusterIconGenerator(context: Context, cluster: Cluster<MapClusterItem>): BitmapDescriptor {
    val iconGenerator = IconGenerator(context)
    iconGenerator.apply {
        setContentView(makeSquaredTextView(context = context))
        setTextAppearance(com.google.maps.android.R.style.amu_ClusterIcon_TextAppearance)
        setBackground(makeMapClusterBackground(context))
    }
    val bucket = getBucket(cluster)
    var descriptor: BitmapDescriptor? = if (icons.isNotEmpty()) {
        icons.get(bucket)
    } else {
        null
    }
    if (descriptor == null) {
        circleBackground?.paint?.color = getColor(bucket)
        descriptor = BitmapDescriptorFactory.fromBitmap(
            iconGenerator.makeIcon(
                getClusterText(bucket)
            )
        )
        icons.put(bucket, descriptor)
    }
    return descriptor
}

// TextView is used to display the number in the cluster.
private fun makeSquaredTextView(context: Context): ClusterMarkerTextView {
    val density = context.resources.displayMetrics.density
    val squareTextView = ClusterMarkerTextView(context)
    val layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    squareTextView.apply {
        this.layoutParams = layoutParams
        id = R.id.amu_text
    }
    val padding: Int = (PADDING * density).toInt()
    squareTextView.setPadding(padding, padding, padding, padding)
    return squareTextView
}

// Makes the
private fun makeMapClusterBackground(context: Context): LayerDrawable {
    val density = context.resources.displayMetrics.density
    circleBackground = ShapeDrawable(OvalShape())
    val outline = ShapeDrawable(OvalShape())
    outline.paint.color = TRANSPARENT_WHITE // Transparent white.
    val background = LayerDrawable(arrayOf<Drawable>(outline, circleBackground!!))
    val strokeWidth: Int = (density * STROKE_MODIFIER).toInt()
    background.setLayerInset(INDEX_ONE, strokeWidth, strokeWidth, strokeWidth, strokeWidth)
    return background
}

// Changes to the different hue and saturation based on the size of the cluster.
private fun getColor(clusterSize: Int): Int = Color.HSVToColor(
    floatArrayOf(
        (SIZE_RANGE - clusterSize).coerceAtLeast(0f).pow(2) / SIZE_RANGE.pow(2) * HUE_RANGE,
        DEFAULT_SATURATION,
        DEFAULT_VALUE
    )
)

fun getClusterText(size: Int) = "$size" + if (size >= buckets.first()) "+" else ""

// Determine where the cluster will go in which bucket. Buckets are used for the String that will be displayed to the user.
fun getBucket(cluster: Cluster<MapClusterItem>) = buckets.lastOrNull { cluster.size > it } ?: cluster.size
