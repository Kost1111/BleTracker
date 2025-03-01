package com.bletracker.presentation.settings

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.bletracker.R
import java.util.Locale

@Composable
fun SettingsScreen() {
    val context = LocalContext.current
    val configuration = LocalConfiguration.current
    val currentLocale = configuration.locales[0]

    val languageOptions = listOf("Русский", "English")
    var selectedLanguage by remember { mutableStateOf(languageOptions.indexOf(currentLocale.displayName).takeIf { it >= 0 } ?: 0) }
    var isDarkTheme by remember { mutableStateOf(false) }

    fun onLanguageChange(language: String) {
        val locale = when (language) {
            "English" -> Locale("en")
            else -> Locale("ru")
        }
        val configuration = context.resources.configuration
        configuration.setLocale(locale)
        context.createConfigurationContext(configuration)
        selectedLanguage = languageOptions.indexOf(language).takeIf { it >= 0 } ?: 0
    }

    fun onThemeToggle(isChecked: Boolean) {
        isDarkTheme = isChecked
    }

    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = stringResource(R.string.settings), style = MaterialTheme.typography.titleMedium)

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = stringResource(id = R.string.language),
            style = MaterialTheme.typography.titleMedium
        )
        DropdownMenuLocal(
            items = languageOptions,
            selectedIndex = selectedLanguage,
            onItemSelected = { language -> onLanguageChange(language) }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(id = R.string.notifications),
            style = MaterialTheme.typography.bodyMedium
        )
        Switch(
            checked = true,
            onCheckedChange = {
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(id = R.string.theme),
            style = MaterialTheme.typography.bodyMedium
        )
        Switch(
            checked = isDarkTheme,
            onCheckedChange = { onThemeToggle(it) }
        )

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(onClick = {
        }) {
            Text(text = stringResource(id = R.string.clear_data))
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(onClick = {
        }) {
            Text(text = stringResource(id = R.string.feedback))
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(onClick = {
        }) {
            Text(text = stringResource(id = R.string.about_app))
        }
    }
}

@Composable
fun DropdownMenuLocal(
    items: List<String>,
    selectedIndex: Int,
    onItemSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        TextButton(onClick = { expanded = true }) {
            Text(text = items.getOrNull(selectedIndex) ?: "")
        }

        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            items.forEachIndexed { index, item ->
                DropdownMenuItem(onClick = {
                    onItemSelected(item)
                    expanded = false
                }) {
                    Text(text = item)
                }
            }
        }
    }
}

