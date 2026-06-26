package com.github.micahhauge.modulithnavigator

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage

@State(name = "ModulithNavigatorSettings", storages = [Storage("modulith-navigator.xml")])
class ModulithNavigatorSettings : PersistentStateComponent<ModulithNavigatorSettings.SettingsState> {

    data class SettingsState(var showSectionIcons: Boolean = true)

    private var state = SettingsState()

    override fun getState(): SettingsState = state

    override fun loadState(state: SettingsState) {
        this.state = state
    }

    companion object {
        fun getInstance(): ModulithNavigatorSettings =
            ApplicationManager.getApplication().getService(ModulithNavigatorSettings::class.java)
    }
}
