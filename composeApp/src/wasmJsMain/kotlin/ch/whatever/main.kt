package ch.whatever

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import com.slack.circuit.retained.rememberRetained
import kotlinx.browser.document
import kotlinx.browser.window

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    ComposeViewport(document.body!!) {
        val component = rememberRetained { WasmJsApplicationComponent.create(window) }

        component.coilBugApp()
    }
}

fun getPath(): String = window.location.hash
