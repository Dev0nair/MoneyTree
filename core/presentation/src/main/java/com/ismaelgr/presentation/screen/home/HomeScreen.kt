package com.ismaelgr.presentation.screen.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ismaelgr.presentation.model.HomeOption
import com.ismaelgr.presentation.screen.loading.LoadingScreen

@Composable
fun HomeScreen(
    navigateTo: (String) -> Unit
) {
    val viewModel: HomeViewModel = hiltViewModel()
    val loading by viewModel.loading.collectAsState()
    
    if (loading) {
        LoadingScreen(loadingMsg = "Descargando datos")
    } else {
        val options = listOf(
            HomeOption("Lista de días") { navigateTo("daylist") },
            HomeOption("Estudio resultados") { navigateTo("daylist") },
            HomeOption("Estudio ganancias") { navigateTo("profitstudio") },
            HomeOption("Resultados") { navigateTo("daylist") },
            HomeOption("Panel de control") { navigateTo("controlpanel") },
            HomeOption("Report") { navigateTo("report") },
        )
        
        View(options)
    }
}

@Composable
private fun View(
    options: List<HomeOption>,
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {
        LazyVerticalGrid(
            modifier = Modifier.padding(10.dp),
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(options) { option ->
                OptionView(option = option)
            }
        }
    }
}


@Composable
private fun OptionView(option: HomeOption) {
    Box(modifier = Modifier
        .fillMaxSize()
        .aspectRatio(2 / 1f)
        .background(color = MaterialTheme.colorScheme.primaryContainer, shape = RoundedCornerShape(24.dp))
        .clickable { option.onClick() }) {
        Text(modifier = Modifier.align(Alignment.Center), text = option.name)
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    View(
        options = listOf(
            HomeOption(name = "Opcion 1", onClick = { }),
            HomeOption(name = "Opcion 2", onClick = { }),
            HomeOption(name = "Opcion 3", onClick = { })
        )
    )
    
}