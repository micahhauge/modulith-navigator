package com.github.micahhauge.modulithnavigator

import com.intellij.ide.projectView.PresentationData
import com.intellij.ide.util.treeView.AbstractTreeNode
import com.intellij.openapi.project.Project
import com.intellij.ui.SimpleTextAttributes
import javax.swing.Icon

class SectionHeaderNode(project: Project, label: String, private val nodeWeight: Int, private val icon: Icon) :
    AbstractTreeNode<String>(project, label) {

    override fun getWeight(): Int = nodeWeight

    override fun isAlwaysLeaf(): Boolean = true

    override fun getChildren(): Collection<AbstractTreeNode<*>> = emptyList()

    override fun update(presentation: PresentationData) {
        if (ModulithNavigatorSettings.getInstance().state.showSectionIcons) {
            presentation.setIcon(icon)
        }
        presentation.addText(value ?: return, SimpleTextAttributes.GRAYED_ATTRIBUTES)
    }

    override fun canNavigate(): Boolean = false

    override fun canNavigateToSource(): Boolean = false
}
