package ch.whatever

import coil3.PlatformContext
import me.tatarka.inject.annotations.Component
import me.tatarka.inject.annotations.Provides
import org.w3c.dom.Window
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.MergeComponent
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

@Component
@MergeComponent(AppScope::class)
@SingleIn(AppScope::class)
abstract class WasmJsApplicationComponent(@get:Provides protected val window: Window) :
    SharedApplicationComponent, WasmJsApplicationComponentMerged {

    abstract val coilBugApp: CoilBugApp

    override fun providePlatformContext(): PlatformContext = PlatformContext.INSTANCE

    companion object
}
