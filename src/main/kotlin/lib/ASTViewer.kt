package lib

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import cafe.adriel.bonsai.core.node.Branch
import cafe.adriel.bonsai.core.node.BranchNode
import cafe.adriel.bonsai.core.node.Leaf
import cafe.adriel.bonsai.core.tree.Tree
import lang.parser.ast.ASTChunk
import repository.LuaDocument

@Composable
fun ASTWindow(document: LuaDocument) {

	val chunk by document.chunk.collectAsState(null)

	Tree<String> {

		chunk?.statements?.forEach {
			Branch(it::class.java.simpleName) {

			}
		}
	}
}