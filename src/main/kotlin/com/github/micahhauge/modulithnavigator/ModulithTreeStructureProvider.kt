package com.github.micahhauge.modulithnavigator

import com.intellij.ide.projectView.TreeStructureProvider
import com.intellij.ide.projectView.ViewSettings
import com.intellij.ide.projectView.impl.nodes.PsiDirectoryNode
import com.intellij.ide.util.treeView.AbstractTreeNode
import com.intellij.icons.AllIcons

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

        val project = open.firstOrNull()?.project ?: internal.firstOrNull()?.project

        val wrappedInternal = internal.map { node ->
            node.value?.let { dir ->
                node.project?.let { InternalModuleNode(it, dir, settings) }
            } ?: node
        }

        val openHeader = project?.let { SectionHeaderNode(it, "Open Modules", 9, AllIcons.Nodes.Public) }
        val internalHeader = project?.let { SectionHeaderNode(it, "Closed Modules", 99, AllIcons.Nodes.Padlock) }

        val nonDirs = children.filter { it !is PsiDirectoryNode }
        return listOfNotNull(openHeader) + open +
               listOfNotNull(internalHeader) + wrappedInternal +
               nonDirs
    }

    private fun isInternalModule(node: PsiDirectoryNode): Boolean {
        return node.virtualFile?.name == "internal"
    }
}
