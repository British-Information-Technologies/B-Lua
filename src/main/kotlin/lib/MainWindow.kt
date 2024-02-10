package lib

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.onEach
import lib.TokenListView
import repository.LuaDocument

@Composable
@Preview
fun MainWindow(
	document: LuaDocument
) {



	MaterialTheme {
		Scaffold(
			topBar = {
				TopAppBar(
					title = { Text(text = "Lua Parser") }
				)
			},
			modifier = Modifier.fillMaxSize()
		) {
			Box(
				modifier = Modifier.fillMaxSize(),
				contentAlignment = Alignment.Center
			) {

				val text by document.rawText.collectAsState("")
				val annotatedText by document.annotatedText.onEach { println(it.text) }
					.collectAsState(AnnotatedString(""))
				val tokens by document.tokens.collectAsState(listOf())

				Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
					OutlinedTextField(
						label = { Text(text = "Source") },
						modifier = Modifier.weight(1f).fillMaxHeight().padding(16.dp),
						value = text,
						onValueChange = {
							document.updateText(it)
						}
					)
					TokenListView(tokens = tokens)
					Text(
						modifier = Modifier.weight(1f).fillMaxHeight().padding(16.dp),
						fontFamily = FontFamily.Monospace,
						text = annotatedText
					)
				}
			}
		}
	}
}