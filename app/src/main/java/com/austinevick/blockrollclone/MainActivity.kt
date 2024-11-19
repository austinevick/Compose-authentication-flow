package com.austinevick.blockrollclone

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import androidx.navigation.compose.rememberNavController
import com.austinevick.blockrollclone.navigation.NavigationGraph
import com.austinevick.blockrollclone.theme.BlockrollCloneTheme
import com.austinevick.blockrollclone.view.auth.PasscodeLoginScreen
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.absoluteValue

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BlockrollCloneTheme {
                 NavigationGraph(navController = rememberNavController())
            }
        }
    }
}

@Composable
fun MyApp() {
    val pagerState = rememberPagerState(pageCount = { 5 })


    LaunchedEffect(pagerState) {
        // Collect from the a snapshotFlow reading the currentPage
        snapshotFlow { pagerState.currentPage }.collect { page ->
            // Do something with each page change, for example:
            // viewModel.sendPageSelectedEvent(page)
            Log.d("Page change", "Page changed to $page")
        }
    }


    Scaffold { innerPadding ->

        HorizontalPager(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            state = pagerState
        ) { i ->
            OutlinedCard(modifier = Modifier
                .padding(vertical = 60.dp, horizontal = 16.dp)
                .fillMaxSize()
                .size(200.dp)
                .graphicsLayer {
                    val pageOffset = (
                            (pagerState.currentPage - i) + pagerState
                                .currentPageOffsetFraction
                            ).absoluteValue
                    alpha = lerp(
                        start = 0.8f,
                        stop = 1f,
                        fraction = 1f - pageOffset.coerceIn(0f, 1f)
                    )
                }

            ) {
                Text("Item $i", modifier = Modifier
                    .align(Alignment.CenterHorizontally))
            }
        }
    }
}
