package com.nebraskarod.wear.appandtile.tile

import androidx.wear.tiles.LayoutElementBuilders
import androidx.wear.tiles.LayoutElementBuilders.Column
import androidx.wear.tiles.LayoutElementBuilders.Text
import androidx.wear.tiles.RequestBuilders
import androidx.wear.tiles.ResourceBuilders
import androidx.wear.tiles.TileBuilders
import androidx.wear.tiles.TimelineBuilders
import com.nebraskarod.wear.appandtile.R
import com.google.common.util.concurrent.Futures
import com.google.common.util.concurrent.ListenableFuture
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import java.time.Duration
import androidx.core.content.ContextCompat
import androidx.wear.tiles.ActionBuilders
import androidx.wear.tiles.ColorBuilders.argb
import androidx.wear.tiles.DeviceParametersBuilders
import androidx.wear.tiles.DimensionBuilders
import androidx.wear.tiles.DimensionBuilders.dp
import androidx.wear.tiles.LayoutElementBuilders.Image
import androidx.wear.tiles.LayoutElementBuilders.Spacer
import androidx.wear.tiles.ModifiersBuilders
import androidx.wear.tiles.ModifiersBuilders.Background
import androidx.wear.tiles.ModifiersBuilders.Corner
import androidx.wear.tiles.ModifiersBuilders.Modifiers
import androidx.wear.tiles.ModifiersBuilders.Padding
import androidx.wear.tiles.TileService
import com.nebraskarod.wear.appandtile.MainActivity
import kotlinx.coroutines.guava.future
//import android.util.Log
//import androidx.core.content.ContentProviderCompat.requireContext
//import kotlinx.coroutines.delay


private const val RESOURCES_VERSION = "1"
private val REFRESH_INTERVAL = Duration.ofMinutes(1)
private const val ID_IMAGE_THE_BTN = "image_the_btn"

// dimensions
private val BUTTON_SIZE = dp(48f)
private val BUTTON_RADIUS = dp(24f)
private val BUTTON_PADDING = dp(0f) //4
private val VERTICAL_SPACING_HEIGHT = dp(8f)

var tileText = "Im a Tile"

class HelloTileService : TileService() {
    private val serviceScope = CoroutineScope(Dispatchers.IO)


    override fun onTileRequest(requestParams: RequestBuilders.TileRequest):
            ListenableFuture<TileBuilders.Tile> {

        val deviceParameters = requestParams.deviceParameters!!

        return Futures.immediateFuture(
            TileBuilders.Tile.Builder()
                .setResourcesVersion(RESOURCES_VERSION)
                .setFreshnessIntervalMillis(REFRESH_INTERVAL.toMillis())
                .setTimeline(
                    TimelineBuilders.Timeline.Builder()
                        .addTimelineEntry(
                        TimelineBuilders.TimelineEntry.Builder().setLayout(
                            LayoutElementBuilders.Layout.Builder().setRoot(
                                layout(deviceParameters)
                            ).build()
                        ).build()
                    ).build()
                ).build()
        )
    }

    private fun layout(deviceParameters: DeviceParametersBuilders.DeviceParameters) =
        LayoutElementBuilders.Box.Builder()
            .setWidth(DimensionBuilders.expand())
            .setHeight(DimensionBuilders.expand())
            .addContent(
                Column.Builder()
                    .addContent(theTestBtn(ID_IMAGE_THE_BTN))
                    .addContent(Spacer.Builder().setHeight((VERTICAL_SPACING_HEIGHT)
                    ).build())
                    //.addContent(resources.getString(R.string.helloTile)).build()
                    .addContent(
                        Text.Builder()
                            //.setText("Im a Tile")
                            .setText(tileText)
                            .build()
                    ).build()
                    ).build()

    override fun onResourcesRequest(requestParams: RequestBuilders.ResourcesRequest) = serviceScope.future {
        ResourceBuilders.Resources.Builder()
            .setVersion(RESOURCES_VERSION)
            .addIdToImageMapping(
                ID_IMAGE_THE_BTN,
                ResourceBuilders.ImageResource.Builder()
                    .setAndroidResourceByResId(
                        ResourceBuilders.AndroidImageResourceByResId.Builder()
                            .setResourceId(R.drawable.baseline_flatware_24)
                            .build()
                    )
                    .build()
            )
            .build()
    }

    private fun theTestBtn(theButtonID: String) =
        Image.Builder()
            .setWidth(BUTTON_SIZE)
            .setHeight(BUTTON_SIZE)
            .setResourceId(theButtonID)
            .setModifiers(
                Modifiers.Builder()
                    .setPadding(
                        Padding.Builder()
                            .setStart(BUTTON_PADDING)
                            .setEnd(BUTTON_PADDING)
                            .setTop(BUTTON_PADDING)
                            .setBottom(BUTTON_PADDING)
                            .build()
                    )
                    .setBackground(
                        Background.Builder()
                            .setCorner(Corner.Builder().setRadius(BUTTON_RADIUS).build())
                            .setColor(argb(ContextCompat.getColor(this, R.color.primaryDark)))
                            .build()
                    )

                    .setClickable(
                        ModifiersBuilders.Clickable.Builder()
                            .setId(theButtonID)
                            .setOnClick(
                                ActionBuilders.LaunchAction.Builder()
                                    .setAndroidActivity(
                                        ActionBuilders.AndroidActivity.Builder()
                                            .setClassName(MainActivity::class.qualifiedName ?: "")
                                            .setPackageName(this.packageName)
                                            .build()
                                    ).build()
                            ).build()
                    )

//                    .setClickable(
//                        Clickable.Builder()
//                            .setId(theButtonID)
//                            .setOnClick(ActionBuilders.LoadAction.Builder().build())
//                            .build()
//                    )
                    .build()
            )
            .build()

}
