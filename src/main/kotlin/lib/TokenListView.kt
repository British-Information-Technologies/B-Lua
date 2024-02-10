package lib

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import lang.lexer.Token

@Composable
fun RowScope.TokenListView(
	tokens: List<Token>
) {
	LazyColumn(
		modifier = Modifier.weight(0.75f).fillMaxSize(),
		contentPadding = PaddingValues(16.dp),
		verticalArrangement = Arrangement.spacedBy(4.dp),
	) {
		items(items = tokens) { token ->
			Card(
				modifier = Modifier.fillMaxWidth(),
				elevation = 16.dp,
			) {
				Column {
					Text(
						modifier = Modifier.padding(8.dp),
						text = "type: ${token::class.java.simpleName}"
					)
					Text(
						modifier = Modifier.padding(8.dp),
						text = "${token.start}:${token.end}"
					)
					Text(
						modifier = Modifier.padding(8.dp),
						text = token.text.replace("\n", "\\n").replace("\t", "\\t")
					)
				}
			}
		}
	}
}