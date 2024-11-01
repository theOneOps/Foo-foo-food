package com.uds.foufoufood.view.admin

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.uds.foufoufood.data_class.model.Restaurant
import com.uds.foufoufood.viewmodel.AdminRestaurantsViewModel
import com.uds.foufoufood.viewmodel.AdminUsersViewModel

@Composable
fun LinkRestorerRestoAndRestoPage(
    navController: NavController,
    adminRestaurantsViewModel: AdminRestaurantsViewModel,
    adminUsersViewModel: AdminUsersViewModel,
    restaurant: Restaurant
) {
    val context = LocalContext.current
    var expanded by remember { mutableStateOf(false) }
    var selectedRestorerName by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        adminUsersViewModel.fetchUsers("restaurateur")
    }
    val anyFreeRestorers by adminUsersViewModel.filteredUsers.observeAsState()
    val freeRestorers =
        anyFreeRestorers!!.filter { it.restaurantId == null || it.restaurantId == "" }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Text(
                text = restaurant.name,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 24.dp)
            )
        }

        if (freeRestorers.isEmpty()) {
            item {
                Text("il n'y a pas de restaurateur à lier à un restaurant")
            }
        } else {
            item {
                Box(modifier = Modifier.fillMaxWidth()) {
                    OutlinedButton(
                        onClick = { expanded = true }, modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = selectedRestorerName.ifEmpty { "Sélectionnez un restaurateur" })
                    }

                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        freeRestorers.forEach { restorer ->
                            DropdownMenuItem(text = {
                                Text(text = restorer.name)
                            }, onClick = {
                                selectedRestorerName = restorer.name
                                expanded = false
                            })
                        }
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }

        item {
            Button(
                onClick = {
                    if (selectedRestorerName.isNotEmpty()) {
                        val restorerSelectedId =
                            freeRestorers.filter { it.name == selectedRestorerName }
                        restorerSelectedId[0].let {
                            adminRestaurantsViewModel.linkARestorer(
                                it._id, restaurant._id
                            )
                        }
                        Toast.makeText(context, "Restaurateur lié avec succès", Toast.LENGTH_SHORT)
                            .show()
                        navController.popBackStack()
                    }
                }, modifier = Modifier.fillMaxWidth(), enabled = selectedRestorerName.isNotEmpty()
            ) {
                Text("Lier le restaurateur")
            }
        }
    }
}
