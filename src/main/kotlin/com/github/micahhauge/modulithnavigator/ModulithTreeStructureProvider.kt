package com.github.micahhauge.modulithnavigator

import com.intellij.ide.projectView.TreeStructureProvider
import com.intellij.ide.projectView.ViewSettings
import com.intellij.ide.projectView.impl.nodes.PsiDirectoryNode
import com.intellij.ide.util.treeView.AbstractTreeNode

class ModulithTreeStructureProvider : TreeStructureProvider {

    override fun modify(
        parent: AbstractTreeNode<*>,
        children: Collection<AbstractTreeNode<*>>,
        settings: ViewSettings?,
    ): Collection<AbstractTreeNode<*>> {
        val dirs = children.filterIsInstance<PsiDirectoryNode>()
        if (dirs.size < 2) return children

        val (open, internal) = dirs.partition { !isInternalModule(it) }
        if (open.isEmpty() || internal.isEmpty()) return children

        val wrappedInternal = internal.map { node ->
            node.value?.let { dir ->
                node.project?.let { project ->
                    InternalModuleNode(project, dir, settings)
                }
            } ?: node
        }

        val nonDirs = children.filter { it !is PsiDirectoryNode }
        return open + wrappedInternal + nonDirs
    }

    private fun isInternalModule(node: PsiDirectoryNode): Boolean {
        return node.virtualFile?.name == "internal"
    }
}
