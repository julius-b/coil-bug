package ch.whatever

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import co.touchlab.kermit.Logger
import coil3.compose.AsyncImage
import coil3.compose.AsyncImagePainter
import coilbug.composeapp.generated.resources.Res
import coilbug.composeapp.generated.resources.compose_multiplatform
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.screen.Screen
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject
import org.jetbrains.compose.resources.painterResource
import software.amazon.lastmile.kotlin.inject.anvil.AppScope

data object MainScreen : Screen {
    data class State(
        val eventSink: (Event) -> Unit
    ) : CircuitUiState

    sealed interface Event : CircuitUiEvent {
        data object QuizClicked : Event
    }
}

@CircuitInject(MainScreen::class, AppScope::class)
@Inject
class MainPresenter(
    @Assisted private val navigator: Navigator
) : Presenter<MainScreen.State> {
    private val log = Logger.withTag("MainScreen")

    @Composable
    override fun present(): MainScreen.State {
        return MainScreen.State { event ->
            when (event) {
                is MainScreen.Event.QuizClicked -> {
                    navigator.goTo(QuizScreen)
                }
            }
        }
    }
}

@CircuitInject(MainScreen::class, AppScope::class)
@Composable
fun MainView(state: MainScreen.State, modifier: Modifier = Modifier) {
    var showContent by remember { mutableStateOf(false) }
    Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        repeat(20) {
            AsyncImage(
                modifier = Modifier,
                //model = "https://http.cat/4${it.toString().padStart(2, '0')}",
                model = "http://localhost:8080/4${it.toString().padStart(2, '0')}.jpg",
                contentDescription = null,
                contentScale = ContentScale.Crop,
                onState = { state -> Logger.withTag("MainView").logImageState(state) }
            )
        }

        Button(onClick = { showContent = !showContent }) {
            Text("Click me!")
        }
        AnimatedVisibility(showContent) {
            val greeting = remember { state.eventSink(MainScreen.Event.QuizClicked) }
            Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                Image(painterResource(Res.drawable.compose_multiplatform), null)
                Text("Compose: $greeting")
            }
        }
    }
}

fun Logger.logImageState(state: AsyncImagePainter.State, model: String? = null) {
    when (state) {
        is AsyncImagePainter.State.Loading -> d { "image loading" }
        is AsyncImagePainter.State.Error -> e(state.result.throwable) { "failed to load image" }
        is AsyncImagePainter.State.Success -> d { "done loading image" }
        is AsyncImagePainter.State.Empty -> Unit
    }
}
