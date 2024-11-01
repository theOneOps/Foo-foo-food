package com.uds.foufoufood.view.admin

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.uds.foufoufood.data_class.model.User
import com.uds.foufoufood.navigation.Screen
import com.uds.foufoufood.ui.component.DrawerScaffold
import com.uds.foufoufood.ui.component.SearchBar
import com.uds.foufoufood.ui.component.TitlePage
import com.uds.foufoufood.viewmodel.AdminUsersViewModel
import com.uds.foufoufood.viewmodel.UserViewModel

@Composable
fun LivreurPage(
    navController: NavHostController,
    adminUsersViewModel: AdminUsersViewModel,
    userViewModel: UserViewModel,
    onRoleChanged: (User, String) -> Unit
) {

    LaunchedEffect(Unit) {
        adminUsersViewModel.fetchUsers("livreur")
    }
    val livreurUsersState by adminUsersViewModel.filteredUsers.observeAsState()
    val livreurUsers = livreurUsersState!!

    DrawerScaffold(
        navController = navController,
        userViewModel = userViewModel,
        currentScreen = Screen.AdminLivreur.route
    ) {
        Column(
            modifier = Modifier
                .padding(top = 60.dp, start = 20.dp, end = 20.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(10.dp))

            TitlePage(label = "Les livreurs")
            SearchBar(
                searchText = adminUsersViewModel.searchText,
                onSearchTextChanged = adminUsersViewModel::onSearchQueryChanged,
            )

            LazyColumn(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxSize()
            ) {
                items(livreurUsers.size) { index ->
                    val user = livreurUsers[index]
                    UserItem(user = user, onClick = {
                        if (user.email.isNotEmpty()) {
                            navController.navigate("userProfile/${user.email}")
                        } else {
                            Log.e("LivreurPage", "User email is empty")
                        }
                    })
                }
            }
        }
    }
}