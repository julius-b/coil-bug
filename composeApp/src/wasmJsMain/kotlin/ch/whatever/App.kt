package ch.whatever

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import co.touchlab.kermit.Logger
import com.slack.circuit.backstack.rememberSaveableBackStack
import com.slack.circuit.foundation.Circuit
import com.slack.circuit.foundation.CircuitCompositionLocals
import com.slack.circuit.foundation.NavigableCircuitContent
import com.slack.circuit.foundation.rememberCircuitNavigator
import com.slack.circuit.retained.rememberRetained
import me.tatarka.inject.annotations.Inject
import org.jetbrains.compose.ui.tooling.preview.Preview

const val DO_BUG = true

typealias CoilBugApp = @Composable () -> Unit

@Inject
@Composable
@Preview
fun CoilBugApp(circuit: Circuit) {
    MaterialTheme {
        val backStack = rememberSaveableBackStack(MainScreen)
        val navigator = rememberCircuitNavigator(backStack) { }

        val path = rememberRetained { getPath().substringAfter('#').lowercase() }
        if (DO_BUG) {
            LaunchedEffect(Unit) {
                Logger.withTag("CoilBugApp").i { "path: $path" }
                if (path == "") return@LaunchedEffect
                when (path) {
                    "quiz" -> navigator.goTo(QuizScreen)
                }
            }
        } else {
            rememberRetained(Unit) {
                Logger.withTag("CoilBugApp").i { "path: $path" }
                when (path) {
                    "quiz" -> navigator.goTo(QuizScreen)
                }
                Unit
            }
        }

        CircuitCompositionLocals(circuit) {
            NavigableCircuitContent(
                navigator = navigator,
                backStack = backStack,
            )
        }
    }
}