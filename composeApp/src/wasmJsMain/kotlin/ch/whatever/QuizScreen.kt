package ch.whatever

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import co.touchlab.kermit.Logger
import coil3.compose.AsyncImage
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.screen.Screen
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.AppScope

data object QuizScreen : Screen {
    data class State(
        val eventSink: (Event) -> Unit
    ) : CircuitUiState

    sealed interface Event : CircuitUiEvent {
    }
}

@CircuitInject(QuizScreen::class, AppScope::class)
@Inject
class QuizPresenter(
    @Assisted private val navigator: Navigator,
) : Presenter<QuizScreen.State> {
    private val log = Logger.withTag("QuizScreen")

    @Composable
    override fun present(): QuizScreen.State {
        return QuizScreen.State { }
    }
}

@CircuitInject(QuizScreen::class, AppScope::class)
@Composable
fun QuizView(state: QuizScreen.State, modifier: Modifier = Modifier) {
    // TODO AsyncImage in QuizView relevant
    //Text("quiz")
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
}

