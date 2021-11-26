/*
 * Copyright (C) 2021 Patrick Goldinger
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package dev.patrickgold.florisboard.app.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.patrickgold.florisboard.app.prefs.AppPrefs
import dev.patrickgold.florisboard.app.prefs.florisPreferenceModel
import dev.patrickgold.jetpref.ui.compose.PreferenceLayout
import dev.patrickgold.jetpref.ui.compose.PreferenceUiContent

@Deprecated("Deprecated in favor of FlorisScreen DSL.")
@Composable
fun FlorisScreen(
    title: String,
    backArrowVisible: Boolean = true,
    scrollable: Boolean = true,
    iconSpaceReserved: Boolean = true,
    actions: @Composable RowScope.() -> Unit = { },
    bottomBar: @Composable () -> Unit = { },
    floatingActionButton: @Composable () -> Unit = { },
    content: PreferenceUiContent<AppPrefs>,
) {
    Scaffold(
        topBar = { FlorisAppBar(title, backArrowVisible, actions) },
        bottomBar = bottomBar,
        floatingActionButton = floatingActionButton,
    ) { innerPadding ->
        val modifier = if (scrollable) {
            Modifier.florisVerticalScroll()
        } else {
            Modifier
        }
        Box(modifier = modifier.padding(innerPadding)) {
            PreferenceLayout(
                florisPreferenceModel(),
                scrollable = false,
                iconSpaceReserved = iconSpaceReserved,
            ) {
                content(this)
            }
        }
    }
}

@Composable
fun FlorisScreen(builder: @Composable FlorisScreenScope.() -> Unit) {
    val scope = FlorisScreenScopeImpl()
    builder(scope)
    scope.Render()
}

typealias FlorisScreenActions = @Composable RowScope.() -> Unit
typealias FlorisScreenBottomBar = @Composable () -> Unit
typealias FlorisScreenContent = PreferenceUiContent<AppPrefs>
typealias FlorisScreenFab = @Composable () -> Unit

interface FlorisScreenScope {
    var title: String

    var backArrowVisible: Boolean

    var scrollable: Boolean

    var iconSpaceReserved: Boolean

    fun actions(actions: FlorisScreenActions)

    fun bottomBar(bottomBar: FlorisScreenBottomBar)

    fun floatingActionButton(fab: FlorisScreenFab)

    fun content(content: FlorisScreenContent)
}

private class FlorisScreenScopeImpl : FlorisScreenScope {
    override var title: String = ""
    override var backArrowVisible: Boolean = true
    override var scrollable: Boolean = true
    override var iconSpaceReserved: Boolean = true

    private var actions: FlorisScreenActions = { }
    private var bottomBar: FlorisScreenBottomBar = { }
    private var content: FlorisScreenContent = { }
    private var fab: FlorisScreenFab = { }

    override fun actions(actions: FlorisScreenActions) {
        this.actions = actions
    }

    override fun bottomBar(bottomBar: FlorisScreenBottomBar) {
        this.bottomBar = bottomBar
    }

    override fun content(content: FlorisScreenContent) {
        this.content = content
    }

    override fun floatingActionButton(fab: FlorisScreenFab) {
        this.fab = fab
    }

    @Composable
    fun Render() {
        Scaffold(
            topBar = { FlorisAppBar(title, backArrowVisible, actions) },
            bottomBar = bottomBar,
            floatingActionButton = fab,
        ) { innerPadding ->
            val modifier = if (scrollable) {
                Modifier.florisVerticalScroll()
            } else {
                Modifier
            }
            Box(modifier = modifier.padding(innerPadding)) {
                PreferenceLayout(
                    florisPreferenceModel(),
                    scrollable = false,
                    iconSpaceReserved = iconSpaceReserved,
                ) {
                    content(this)
                }
            }
        }
    }
}
