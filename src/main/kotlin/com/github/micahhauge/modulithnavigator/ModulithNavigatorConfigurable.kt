package com.github.micahhauge.modulithnavigator

import com.intellij.icons.AllIcons
import com.intellij.ide.projectView.ProjectView
import com.intellij.openapi.options.Configurable
import com.intellij.openapi.project.ProjectManager
import com.intellij.ui.IdeBorderFactory
import com.intellij.ui.SimpleColoredComponent
import com.intellij.ui.SimpleTextAttributes
import com.intellij.ui.components.JBCheckBox
import com.intellij.util.ui.FormBuilder
import com.intellij.util.ui.JBUI
import com.intellij.util.ui.UIUtil
import java.awt.BorderLayout
import java.awt.Component
import java.awt.event.ItemListener
import javax.swing.BoxLayout
import javax.swing.Icon
import javax.swing.JComponent
import javax.swing.JPanel

class ModulithNavigatorConfigurable : Configurable {

    private var showOpenHeaderCheckbox: JBCheckBox? = null
    private var showClosedHeaderCheckbox: JBCheckBox? = null
    private var showOtherHeaderCheckbox: JBCheckBox? = null
    private var showIconsCheckbox: JBCheckBox? = null
    private var dimInternalSuffixCheckbox: JBCheckBox? = null
    private var previewPanel: JPanel? = null

    override fun getDisplayName(): String = "Modulith Navigator"

    override fun createComponent(): JComponent {
        showOpenHeaderCheckbox = JBCheckBox("Show \"Open Modules\" header")
        showClosedHeaderCheckbox = JBCheckBox("Show \"Closed Modules\" header")
        showOtherHeaderCheckbox = JBCheckBox("Show \"Other Files\" header")
        showIconsCheckbox = JBCheckBox("Show icons on headers")
        dimInternalSuffixCheckbox = JBCheckBox("Dim .internal suffix on closed modules")

        val onChange = ItemListener { refreshPreview() }
        showOpenHeaderCheckbox!!.addItemListener(onChange)
        showClosedHeaderCheckbox!!.addItemListener(onChange)
        showOtherHeaderCheckbox!!.addItemListener(onChange)
        showIconsCheckbox!!.addItemListener(onChange)
        dimInternalSuffixCheckbox!!.addItemListener(onChange)

        return FormBuilder.createFormBuilder()
            .addComponent(showOpenHeaderCheckbox!!)
            .addComponent(showClosedHeaderCheckbox!!)
            .addComponent(showOtherHeaderCheckbox!!)
            .addComponent(showIconsCheckbox!!)
            .addSeparator()
            .addComponent(dimInternalSuffixCheckbox!!)
            .addSeparator()
            .addComponent(buildPreviewContainer())
            .addComponentFillVertically(JPanel(), 0)
            .panel
    }

    private fun buildPreviewContainer(): JPanel {
        val inner = JPanel().apply {
            layout = BoxLayout(this, BoxLayout.Y_AXIS)
            background = UIUtil.getTreeBackground()
            border = JBUI.Borders.empty(6)
        }
        previewPanel = inner
        refreshPreview()
        return JPanel(BorderLayout()).apply {
            border = IdeBorderFactory.createTitledBorder("Preview")
            add(inner, BorderLayout.CENTER)
        }
    }

    private fun refreshPreview() {
        val panel = previewPanel ?: return
        panel.removeAll()

        val showOpen = showOpenHeaderCheckbox?.isSelected ?: true
        val showClosed = showClosedHeaderCheckbox?.isSelected ?: true
        val showOther = showOtherHeaderCheckbox?.isSelected ?: true
        val showIcons = showIconsCheckbox?.isSelected ?: true
        val dimInternal = dimInternalSuffixCheckbox?.isSelected ?: true

        if (showOpen) panel.add(headerRow("Open Modules", if (showIcons) AllIcons.Nodes.Public else null))
        panel.add(moduleRow("client"))
        panel.add(moduleRow("onboarding"))
        if (showClosed) panel.add(headerRow("Closed Modules", if (showIcons) AllIcons.Nodes.Padlock else null))
        panel.add(internalRow("questions", dimInternal))
        panel.add(internalRow("mixpanel", dimInternal))
        if (showOther) panel.add(headerRow("Other Files", null))
        panel.add(otherRow("JavaWebApplication"))

        panel.revalidate()
        panel.repaint()
    }

    private fun headerRow(label: String, icon: Icon?): SimpleColoredComponent =
        SimpleColoredComponent().apply {
            if (icon != null) setIcon(icon)
            append(label, SimpleTextAttributes.GRAYED_ATTRIBUTES)
            alignmentX = Component.LEFT_ALIGNMENT
        }

    private fun moduleRow(name: String): SimpleColoredComponent =
        SimpleColoredComponent().apply {
            setIcon(AllIcons.Nodes.Package)
            ipad = JBUI.insets(0, 16, 0, 0)
            append(name, SimpleTextAttributes.REGULAR_ATTRIBUTES)
            alignmentX = Component.LEFT_ALIGNMENT
        }

    private fun internalRow(name: String, dim: Boolean): SimpleColoredComponent =
        SimpleColoredComponent().apply {
            setIcon(AllIcons.Nodes.Package)
            ipad = JBUI.insets(0, 16, 0, 0)
            append(name, SimpleTextAttributes.REGULAR_ATTRIBUTES)
            if (dim) {
                append(" .internal", SimpleTextAttributes.GRAYED_SMALL_ATTRIBUTES)
            } else {
                append(".internal", SimpleTextAttributes.REGULAR_ATTRIBUTES)
            }
            alignmentX = Component.LEFT_ALIGNMENT
        }

    private fun otherRow(name: String): SimpleColoredComponent =
        SimpleColoredComponent().apply {
            setIcon(AllIcons.Nodes.Folder)
            ipad = JBUI.insets(0, 16, 0, 0)
            append(name, SimpleTextAttributes.REGULAR_ATTRIBUTES)
            alignmentX = Component.LEFT_ALIGNMENT
        }

    override fun isModified(): Boolean {
        val s = ModulithNavigatorSettings.getInstance().state
        return showOpenHeaderCheckbox?.isSelected != s.showOpenHeader ||
               showClosedHeaderCheckbox?.isSelected != s.showClosedHeader ||
               showOtherHeaderCheckbox?.isSelected != s.showOtherHeader ||
               showIconsCheckbox?.isSelected != s.showSectionIcons ||
               dimInternalSuffixCheckbox?.isSelected != s.dimInternalSuffix
    }

    override fun apply() {
        ModulithNavigatorSettings.getInstance().state.apply {
            showOpenHeader = showOpenHeaderCheckbox?.isSelected ?: true
            showClosedHeader = showClosedHeaderCheckbox?.isSelected ?: true
            showOtherHeader = showOtherHeaderCheckbox?.isSelected ?: true
            showSectionIcons = showIconsCheckbox?.isSelected ?: true
            dimInternalSuffix = dimInternalSuffixCheckbox?.isSelected ?: true
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
        dimInternalSuffixCheckbox?.isSelected = s.dimInternalSuffix
        refreshPreview()
    }

    override fun disposeUIResources() {
        showOpenHeaderCheckbox = null
        showClosedHeaderCheckbox = null
        showOtherHeaderCheckbox = null
        showIconsCheckbox = null
        dimInternalSuffixCheckbox = null
        previewPanel = null
    }
}
