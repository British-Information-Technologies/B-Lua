import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import lib.ASTWindow
import lib.MainWindow
import repository.LuaDocument

fun main() = application {

	val document = LuaDocument()

	Window(onCloseRequest = ::exitApplication) {
		MainWindow(document = document)
	}

	Window(onCloseRequest = ::exitApplication) {
		ASTWindow(document = document)
	}
}
