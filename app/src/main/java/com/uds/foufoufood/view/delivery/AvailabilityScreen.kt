package com.uds.foufoufood.view.delivery

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.uds.foufoufood.viewmodel.DeliveryViewModel


@Composable
fun AvailabilityScreen(
    navController: NavHostController,
    deliveryViewModel: DeliveryViewModel
) {
    val isAvailable by deliveryViewModel.isAvailable.collectAsState()

    // Utilisation de la Box pour centrer les éléments
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Texte pour afficher l'état actuel de disponibilité
            Text(
                text = if (isAvailable) "Disponible pour livrer" else "Indisponible",
                style = MaterialTheme.typography.headlineSmall,
                color = if (isAvailable) Color.Green else Color.Red
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Switch pour changer l'état de disponibilité
            Switch(
                checked = isAvailable,
                onCheckedChange = { deliveryViewModel.setAvailability(it) },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.Green,
                    uncheckedThumbColor = Color.Red
                )
            )
        }
    }
}
