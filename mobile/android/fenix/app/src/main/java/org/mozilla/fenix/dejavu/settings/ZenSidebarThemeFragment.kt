/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.mozilla.fenix.dejavu.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import org.mozilla.fenix.R
import org.mozilla.fenix.compose.list.RadioButtonListItem
import org.mozilla.fenix.sidebar.ZenSidebarResources
import org.mozilla.fenix.sidebar.ZenSidebarTheme

class ZenSidebarThemeFragment : DejavuComposeFragment(ZenSidebarResources.SIDEBAR_THEME_TITLE) {
    @Composable
    override fun DejavuScreen() {
        val settings = remember { dejavuSettings() }
        val currentThemeId by settings.sidebarThemeId.collectAsState()

        Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
            SettingsHint(stringResource(ZenSidebarResources.SIDEBAR_THEME_PREFERENCE_SUMMARY))
            ZenSidebarTheme.builtins.forEachIndexed { index, theme ->
                RadioButtonListItem(
                    label = theme.displayName,
                    selected = theme.id == currentThemeId,
                    modifier = Modifier.settingsCard(index, ZenSidebarTheme.builtins.size),
                    onClick = { settings.setSidebarThemeId(theme.id) },
                )
            }
        }
    }
}
