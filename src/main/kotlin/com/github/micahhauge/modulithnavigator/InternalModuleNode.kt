package com.github.micahhauge.modulithnavigator

import com.intellij.ide.projectView.ViewSettings
import com.intellij.ide.projectView.impl.nodes.PsiDirectoryNode
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiDirectory

class InternalModuleNode(project: Project, dir: PsiDirectory, settings: ViewSettings?) :
    PsiDirectoryNode(project, dir, settings) {

    override fun getWeight(): Int = -1
}
