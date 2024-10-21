package com.uds.foufoufood.view.client.components
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.uds.foufoufood.R
import com.uds.foufoufood.data_class.model.Menu
import com.uds.foufoufood.ui.component.TextLink


val menuTest = Menu(
    name = "Crème Brûlée",
    descriptor = "Rich custard base topped with a layer of caramelized sugar",
    price = 10.0,
    category = "Dessert",
    image = "https://images.unsplash.com/photo-1487004121828-9fa15a215a7a?q=80&w=2070&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
    restaurantId = "1"
)

@Preview
@Composable
fun MenuRestaurant(menu: Menu = menuTest) {
    LazyColumn {
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                shape = RoundedCornerShape(10.dp),
                elevation = CardDefaults.cardElevation(5.dp)
            ) {
                // Enlever la LazyColumn inutile ici
                Column {
                    Box(
                        modifier = Modifier
                            .height(250.dp)
                            .fillMaxWidth()
                    ) {
                        AsyncImage(
                            model = menu.image,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(16.dp)),
                            contentDescription = menu.descriptor,
                            contentScale = ContentScale.Crop,
                        )
                        Box(
                            modifier = Modifier.padding(15.dp),
                            contentAlignment = Alignment.TopStart
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                IconButton(
                                    onClick = { /* Action du bouton */ },
                                    modifier = Modifier
                                        .clip(CircleShape)
                                        .background(Color.White)
                                        .size(50.dp)
                                ) {
                                    Text(
                                        "<",
                                        style = TextStyle(fontSize = 30.sp),
                                        color = Color.Gray
                                    )
                                }
                                HeartIconButton() // Ton bouton en forme de cœur
                            }
                        }
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(5.dp)
                            .background(Color.Yellow),
                    ) {
                        Text(menu.name, style = TextStyle(color = Color.Black, fontSize = 35.sp))
                        Spacer(Modifier.height(10.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(0.5f),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.SpaceAround,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("⭐", style = TextStyle(fontSize = 15.sp))
                                Text("${menu.price}", style = TextStyle(fontSize = 18.sp))
                                Spacer(Modifier.width(20.dp))
                            }
                            TextLink(onClick = { /* Lien pour les avis */ }, label = "See Review")
                        }
                        Spacer(Modifier.height(10.dp))

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Text("$", style = TextStyle(fontSize = 15.sp))
                                Text("${menu.price}", style = TextStyle(fontSize = 20.sp))
                                Spacer(Modifier.width(20.dp))
                            }

                            // Counter
                            CounterProductBought()
                        }

                        Text(
                            modifier = Modifier.padding(8.dp),
                            text = menu.descriptor.trimIndent()
                        )
                    }
                }
            }
        }

        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth() // Occupe toute la largeur
                    .padding(vertical = 120.dp), // Ajout d'un padding vertical pour plus d'espace
                verticalArrangement = Arrangement.Center, // Centre verticalement le contenu
                horizontalAlignment = Alignment.CenterHorizontally // Centre horizontalement le bouton
            ) {
                AddToCartButton()
            }
        }
    }
}

@Composable
fun AddToCartButton() {
    Button(onClick = { /* Ajouter au panier action */ }) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(Color.White)
                    .padding(2.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.buy_cart_icon),
                    contentDescription = "Buy Cart Icon",
                    modifier = Modifier.size(24.dp)
                )
            }
            Spacer(Modifier.width(8.dp)) // Espacement entre l'icône et le texte
            Text(
                text = "ADD TO CART",
                fontSize = 16.sp,  // Taille du texte
                fontWeight = FontWeight.Bold, // Texte en gras
                color = Color.White // Couleur du texte
            )
        }
    }
}
