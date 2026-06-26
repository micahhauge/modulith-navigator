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
        if (parent !is PsiDirectoryNode) return children

        // Only consider directories whose names look like Java packages (lowercase).
        // This excludes IntelliJ synthetic nodes like "JavaWebApplication".
        val packageDirs = children.filterIsInstance<PsiDirectoryNode>().filter { isJavaPackageDir(it) }
        if (packageDirs.size < 2) return children

        val (open, internal) = packageDirs.partition { !isInternalModule(it) }
        if (open.isEmpty() || internal.isEmpty()) return children

        val project = open.firstOrNull()?.project ?: internal.firstOrNull()?.project

        val wrappedInternal = internal.map { node ->
            node.value?.let { dir ->
                node.project?.let { InternalModuleNode(it, dir, settings) }
            } ?: node
        }

        val openHeader = project?.let { SectionHeaderNode(it, "Open Modules", 9, AllIcons.Nodes.Public) }
        val internalHeader = project?.let { SectionHeaderNode(it, "Closed Modules", 99, AllIcons.Nodes.Padlock) }

        // Non-package children (non-dirs and synthetic dirs like JavaWebApplication) go at the bottom
        val otherChildren = children.filterNot { child -> child is PsiDirectoryNode && isJavaPackageDir(child) }
        return listOfNotNull(openHeader) + open +
               listOfNotNull(internalHeader) + wrappedInternal +
               otherChildren
    }

    private fun isJavaPackageDir(node: PsiDirectoryNode): Boolean {
        val name = node.virtualFile?.name ?: return false
        return name.matches(Regex("[a-z][a-z0-9]*"))
    }

    private fun isInternalModule(node: PsiDirectoryNode): Boolean {
        return node.virtualFile?.name == "internal"
    }
}
