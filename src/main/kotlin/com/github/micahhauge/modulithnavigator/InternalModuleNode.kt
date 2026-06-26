package com.github.micahhauge.modulithnavigator

import com.intellij.ide.projectView.PresentationData
import com.intellij.ide.projectView.ViewSettings
import com.intellij.ide.projectView.impl.nodes.PsiDirectoryNode
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiDirectory
import com.intellij.ui.SimpleTextAttributes

class InternalModuleNode(project: Project, dir: PsiDirectory, settings: ViewSettings?) :
    PsiDirectoryNode(project, dir, settings) {

    override fun getWeight(): Int = -1

    override fun update(presentation: PresentationData) {
        super.update(presentation)
        if (!ModulithNavigatorSettings.getInstance().state.dimInternalSuffix) return
        val text = presentation.presentableText ?: return
        val dotIndex = text.lastIndexOf(".internal")
        if (dotIndex < 0) return
        presentation.clearText()
        presentation.addText(text.substring(0, dotIndex), SimpleTextAttributes.REGULAR_ATTRIBUTES)
        presentation.addText(" .internal", SimpleTextAttributes.GRAYED_SMALL_ATTRIBUTES)
    }
}
