package com.uds.foufoufood.ui.page

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.uds.foufoufood.R
import com.uds.foufoufood.data_class.model.User

@Composable
fun ProfileScreen(user: User) {
    // Extraire les données de l'utilisateur connecté
    val name = user.name
    val email = user.email
    val role = user.role
    val address = user.address
    val hasAddress = address != null

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.height(50.dp))

        // Afficher le profil (nom, type)
        ProfileImage()

        Spacer(modifier = Modifier.height(8.dp))

        ProfileName(name = name)

        Spacer(modifier = Modifier.height(16.dp))

        if (role != null) {
            ProfileType(role = role)
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Gestion de l'email
        ProfileEmailSection(
            email = email,
            isEditable = false,
            onEditClick = { /* TODO: Activer la modification de l'email */ },
            onSaveClick = { /* TODO: Sauvegarder l'email modifié */ }
        )

        Spacer(modifier = Modifier.height(24.dp))

        if (hasAddress) {
            ProfileExistingAddress( /* TODO : Afficher l'adresse existante */ )
        } else {
            NoAddressSection(onAddAddressClick = { /* TODO: Ajouter une adresse */ })
        }
    }
}

@Composable
fun ProfileImage() {
    Image(
        painter = painterResource(id = R.drawable.circle_background), // Remplacer avec votre image réelle
        contentDescription = null,
        modifier = Modifier
            .size(120.dp)
            .clip(CircleShape)
            .background(Color.Gray) // Exemple de couleur de fond
            .padding(16.dp),
        contentScale = ContentScale.Crop
    )
}

@Composable
fun ProfileName(name: String) {
    Text(
        text = name,
        fontSize = 22.sp,
        fontWeight = FontWeight.Bold,
        color = Color(0xFFFFA500) // Couleur orange
    )
}

@Composable
fun ProfileType(role: String) {
    Text(
        text = role,
        fontSize = 16.sp,
        color = Color(0xFF8B4513) // Couleur marron
    )
}


@Composable
fun ProfileEmailSection(
    email: String,
    isEditable: Boolean,
    onEditClick: () -> Unit,
    onSaveClick: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(id = R.string.email), // Libellé pour "Email"
                fontSize = 16.sp,
                color = Color.Gray,
                modifier = Modifier.padding(end = 10.dp)
            )

            if (!isEditable) {
                // Bouton pour activer l'édition de l'email
                IconButton(onClick = onEditClick) {
                    Icon(
                        painter = painterResource(id = R.drawable.pen_square_icon), // Icône pour modifier
                        contentDescription = "Edit Email",
                        modifier = Modifier.size(28.dp)
                    )
                }
            } else {
                // Bouton pour sauvegarder l'email modifié
                IconButton(onClick = onSaveClick) {
                    Icon(
                        painter = painterResource(id = R.drawable.save),
                        contentDescription = "Save Email",
                        modifier = Modifier.size(28.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Champ de texte pour afficher/modifier l'email
        TextField(
            value = email, // Email actuel de l'utilisateur
            onValueChange = { /* Logique pour modifier l'email */ },
            modifier = Modifier
                .fillMaxWidth()
                .height(65.dp)
                .background(Color.LightGray, shape = RoundedCornerShape(8.dp))
                .padding(start = 25.dp),
            enabled = isEditable, // Activer/désactiver l'édition
            placeholder = {
                Text(text = stringResource(id = R.string.email_example), color = Color.Gray)
            },
            textStyle = TextStyle(fontSize = 16.sp)
        )
    }
}

@Composable
fun ProfileExistingAddress() {
    Column {
        Row(modifier = Modifier.fillMaxWidth()) {
            // Appliquer Modifier.weight dans le Row ici
            AddressTextField(
                value = "123",
                placeholder = "Numéro de rue",
                onValueChange = { /* Gérer le changement */ },
                modifier = Modifier.weight(1f) // Appliquer le weight dans le Row
            )

            Spacer(modifier = Modifier.width(10.dp))

            AddressTextField(
                value = "Rue de la Paix",
                placeholder = "Nom de rue",
                onValueChange = { /* Gérer le changement */ },
                modifier = Modifier.weight(3f) // Appliquer un autre poids
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            AddressTextField(
                value = "75000",
                placeholder = "Code postal",
                onValueChange = { /* Gérer le changement */ },
                modifier = Modifier.weight(1.3f) // Appliquer le weight
            )

            Spacer(modifier = Modifier.width(10.dp))

            AddressTextField(
                value = "Paris",
                placeholder = "Ville",
                onValueChange = { /* Gérer le changement */ },
                modifier = Modifier.weight(1.7f) // Appliquer le weight ici aussi
            )
        }
    }
}

@Composable
fun AddressTextField(
    value: String,
    placeholder: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier // Accepter un Modifier externe pour personnalisation
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier
            .height(65.dp)
            .background(Color.LightGray, shape = RoundedCornerShape(8.dp))
            .padding(start = 25.dp),
        placeholder = {
            Text(text = placeholder, color = Color.Gray)
        },
        textStyle = TextStyle(fontSize = 16.sp)
    )
}

@Composable
fun NoAddressSection(onAddAddressClick: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = R.string.no_address),
            fontSize = 16.sp,
            color = Color.Gray,
            modifier = Modifier.padding(bottom = 20.dp)
        )

        Button(
            onClick = { onAddAddressClick() },
            modifier = Modifier
                .height(60.dp)
                .padding(horizontal = 50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.orange))
        ) {
            Text(
                text = stringResource(id = R.string.add_address),
                fontSize = 15.sp,
                color = Color.White
            )
        }
    }
}
