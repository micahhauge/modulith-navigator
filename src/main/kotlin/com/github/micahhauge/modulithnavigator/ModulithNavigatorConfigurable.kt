package com.github.micahhauge.modulithnavigator

import com.intellij.openapi.options.Configurable
import com.intellij.ui.components.JBCheckBox
import com.intellij.util.ui.FormBuilder
import javax.swing.JComponent
import javax.swing.JPanel

class ModulithNavigatorConfigurable : Configurable {

    private var showIconsCheckbox: JBCheckBox? = null

    override fun getDisplayName(): String = "Modulith Navigator"

    override fun createComponent(): JComponent {
        showIconsCheckbox = JBCheckBox("Show icons on section headers (🔓 Open Modules / 🔒 Closed Modules)")
        return FormBuilder.createFormBuilder()
            .addComponent(showIconsCheckbox!!)
            .addComponentFillVertically(JPanel(), 0)
            .panel
    }

    override fun isModified(): Boolean =
        showIconsCheckbox?.isSelected != ModulithNavigatorSettings.getInstance().state.showSectionIcons

    override fun apply() {
        ModulithNavigatorSettings.getInstance().state.showSectionIcons =
            showIconsCheckbox?.isSelected ?: true
    }

    override fun reset() {
        showIconsCheckbox?.isSelected =
            ModulithNavigatorSettings.getInstance().state.showSectionIcons
    }

    override fun disposeUIResources() {
        showIconsCheckbox = null
    }
}
