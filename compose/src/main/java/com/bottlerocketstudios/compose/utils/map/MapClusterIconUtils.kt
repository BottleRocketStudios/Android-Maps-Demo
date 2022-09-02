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

// This is used to determine which bucket each cluster would go in.
private val buckets = intArrayOf(10, 20, 50, 100, 200, 500, 1000)
private const val MIN_BUCKET_SIZE = 0
private val icons = SparseArray<BitmapDescriptor>()
private var circleBackground: ShapeDrawable? = null
private const val DEFAULT_SATURATION = 1f
private const val DEFAULT_VALUE = .6f
private const val HUE_RANGE = 220f
private const val SIZE_RANGE = 300f

fun clusteringIconGenerator(context: Context, cluster: Cluster<MapClusterItem>): BitmapDescriptor {
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
    if(descriptor == null) {
        circleBackground?.paint?.color = getColor(bucket)
        descriptor = BitmapDescriptorFactory.fromBitmap(
            iconGenerator.makeIcon(
                getClusterText(bucket)
            ))
        icons.put(bucket, descriptor)
    }
    return descriptor

}

private fun makeSquaredTextView(context: Context): ClusterMarkerTextView {
    val density = context.resources.displayMetrics.density
    val squareTextView = ClusterMarkerTextView(context)
    val layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    squareTextView.apply {
        this.layoutParams = layoutParams
        id = R.id.amu_text
    }
    val twelveDpi: Int = (12 * density).toInt()
    squareTextView.setPadding(twelveDpi, twelveDpi, twelveDpi, twelveDpi)
    return squareTextView
}

private fun makeMapClusterBackground(context: Context): LayerDrawable {
    val density = context.resources.displayMetrics.density
    circleBackground = ShapeDrawable(OvalShape())
    val outline = ShapeDrawable(OvalShape())
    outline.paint.color = -0x7f000001 // Transparent white.
    val background = LayerDrawable(arrayOf<Drawable>(outline, circleBackground!!))
    val strokeWidth: Int = (density * 3).toInt()
    background.setLayerInset(1, strokeWidth, strokeWidth, strokeWidth, strokeWidth)
    return background
}

private fun getColor(clusterSize: Int): Int {
    val size = clusterSize.toFloat().coerceAtMost(SIZE_RANGE)
    val hue = (SIZE_RANGE - size) * (SIZE_RANGE - size) / (SIZE_RANGE * SIZE_RANGE) * HUE_RANGE
    return Color.HSVToColor(
        floatArrayOf(
            hue, DEFAULT_SATURATION, DEFAULT_VALUE
        )
    )
}

fun getClusterText(bucket: Int): String {
    return if (bucket < buckets[MIN_BUCKET_SIZE]) {
        bucket.toString()
    } else "$bucket+"
}


fun getBucket(cluster: Cluster<MapClusterItem>): Int {
    val size = cluster.size
    if (size <= buckets[MIN_BUCKET_SIZE]) {
        return size
    }
    for (index in 0 until buckets.size - 1) {
        if (size < buckets[index + 1]) {
            return buckets[index]
        }
    }
    return buckets[buckets.size - 1]
}
