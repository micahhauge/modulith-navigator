package com.github.micahhauge.modulithnavigator

import com.intellij.ide.projectView.ProjectView
import com.intellij.openapi.options.Configurable
import com.intellij.openapi.project.ProjectManager
import com.intellij.ui.components.JBCheckBox
import com.intellij.util.ui.FormBuilder
import javax.swing.JComponent
import javax.swing.JPanel

class ModulithNavigatorConfigurable : Configurable {

    private var showOpenHeaderCheckbox: JBCheckBox? = null
    private var showClosedHeaderCheckbox: JBCheckBox? = null
    private var showOtherHeaderCheckbox: JBCheckBox? = null
    private var showIconsCheckbox: JBCheckBox? = null

    override fun getDisplayName(): String = "Modulith Navigator"

    override fun createComponent(): JComponent {
        showOpenHeaderCheckbox = JBCheckBox("Show \"Open Modules\" header")
        showClosedHeaderCheckbox = JBCheckBox("Show \"Closed Modules\" header")
        showOtherHeaderCheckbox = JBCheckBox("Show \"Other Files\" header")
        showIconsCheckbox = JBCheckBox("Show icons on headers")
        return FormBuilder.createFormBuilder()
            .addComponent(showOpenHeaderCheckbox!!)
            .addComponent(showClosedHeaderCheckbox!!)
            .addComponent(showOtherHeaderCheckbox!!)
            .addComponent(showIconsCheckbox!!)
            .addComponentFillVertically(JPanel(), 0)
            .panel
    }

    override fun isModified(): Boolean {
        val s = ModulithNavigatorSettings.getInstance().state
        return showOpenHeaderCheckbox?.isSelected != s.showOpenHeader ||
               showClosedHeaderCheckbox?.isSelected != s.showClosedHeader ||
               showOtherHeaderCheckbox?.isSelected != s.showOtherHeader ||
               showIconsCheckbox?.isSelected != s.showSectionIcons
    }

    override fun apply() {
        ModulithNavigatorSettings.getInstance().state.apply {
            showOpenHeader = showOpenHeaderCheckbox?.isSelected ?: true
            showClosedHeader = showClosedHeaderCheckbox?.isSelected ?: true
            showOtherHeader = showOtherHeaderCheckbox?.isSelected ?: true
            showSectionIcons = showIconsCheckbox?.isSelected ?: true
        }
        ProjectManager.getInstance().openProjects.forEach { project ->
            ProjectView.getInstance(project).refresh()
        }
    }

    override fun reset() {
        val s = ModulithNavigatorSettings.getInstance().state
        showOpenHeaderCheckbox?.isSelected = s.showOpenHeader
        showClosedHeaderCheckbox?.isSelected = s.showClosedHeader
        showOtherHeaderCheckbox?.isSelected = s.showOtherHeader
        showIconsCheckbox?.isSelected = s.showSectionIcons
    }

    override fun disposeUIResources() {
        showOpenHeaderCheckbox = null
        showClosedHeaderCheckbox = null
        showOtherHeaderCheckbox = null
        showIconsCheckbox = null
    }
}
