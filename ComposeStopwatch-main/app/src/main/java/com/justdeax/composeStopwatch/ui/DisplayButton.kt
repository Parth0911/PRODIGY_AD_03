package com.justdeax.composeStopwatch.ui
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.justdeax.composeStopwatch.R
import com.justdeax.composeStopwatch.ui.theme.DarkColorScheme
import com.justdeax.composeStopwatch.util.StopwatchAction
import com.justdeax.composeStopwatch.util.commandService

@Composable
fun DisplayButton(
    modifier: Modifier,
    isStarted: Boolean,
    isRunning: Boolean,
    notificationEnabled: Boolean,
    isAdditionalShow: Boolean,
    showHideAdditional: () -> Unit,
    reset: () -> Unit,
    startResume: () -> Unit,
    pause: () -> Unit,
    addLap: () -> Unit
) {
    val context = LocalContext.current
    val startDrawable = painterResource(R.drawable.round_play_arrow_24)
    val pauseDrawable = painterResource(R.drawable.round_pause_24)
    val stopDrawable = painterResource(R.drawable.round_stop_24)
    val addLapsDrawable = painterResource(R.drawable.round_add_circle_24)
    val additionalDrawable = painterResource(R.drawable.round_grid_view_24)
    val startButtonSizeAnimation by animateIntAsState(
        targetValue = if (isStarted) 120 else 300,
        animationSpec = keyframes { durationMillis = 250 },
        label = ""
    )

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center
    ) {
        Box(contentAlignment = Alignment.Center) {
            androidx.compose.animation.AnimatedVisibility(
                visible = isStarted,
                enter = EnterTransition.None,
                exit = fadeOut(tween(500))
            ) {
                Row {
                    IconButton(
                        onClick = { showHideAdditional() },
                        painter = additionalDrawable,
                        contentDesc = context.getString(R.string.additional_action)
                    )
                    Spacer(Modifier.width(170.dp))
                    if (isRunning)
                        IconButton(
                            onClick = {
                                if (notificationEnabled)
                                    context.commandService(StopwatchAction.ADD_LAP)
                                else
                                    addLap()
                            },
                            painter = addLapsDrawable,
                            contentDesc = context.getString(R.string.add_lap)
                        )
                    else
                        IconButton(
                            onClick = {
                                if (isAdditionalShow) showHideAdditional()
                                if (notificationEnabled)
                                    context.commandService(StopwatchAction.RESET)
                                else
                                    reset()
                            },
                            painter = stopDrawable,
                            contentDesc = context.getString(R.string.stop)
                        )
                }
            }
            IconButton(
                width = startButtonSizeAnimation,
                onClick = {
                    if (isRunning) {
                        if (notificationEnabled) context.commandService(StopwatchAction.PAUSE)
                        else pause()
                    } else {
                        if (notificationEnabled) context.commandService(StopwatchAction.START_RESUME)
                        else startResume()
                    }
                },
                painter = if (isRunning) pauseDrawable else startDrawable,
                contentDesc =
                if (isRunning) context.getString(R.string.pause)
                else context.getString(R.string.resume)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DisplayButtonPreview() {
    MaterialTheme(colorScheme = DarkColorScheme) {
        DisplayButton(
            Modifier
                .fillMaxWidth()
                .padding(top = 20.dp, bottom = 50.dp),
            isStarted = true,
            isRunning = false,
            notificationEnabled = false,
            isAdditionalShow = true,
            showHideAdditional = { },
            reset = { },
            startResume = { },
            pause = { },
            addLap = { }
        )
    }
}