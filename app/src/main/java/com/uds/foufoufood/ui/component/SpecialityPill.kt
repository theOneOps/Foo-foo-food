package com.uds.foufoufood.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import com.uds.foufoufood.R
import com.uds.foufoufood.data_class.model.Speciality

@Composable
fun SpecialityPills(
    specialities: List<Speciality>,
    selectedSpeciality: Speciality?,
    onSpecialitySelected: (Speciality?) -> Unit
) {
    LazyRow {
        items(specialities) { category ->
            SpecialityPill(
                speciality = category,
                isSelected = selectedSpeciality == category,
                onClick = {
                    if (selectedSpeciality == category) {
                        onSpecialitySelected(null)
                    } else {
                        onSpecialitySelected(category)
                    }
                }
            )
        }
    }
}

@Composable
fun SpecialityPill(speciality: Speciality, isSelected: Boolean, onClick: () -> Unit) {
    Surface(
        color = if (isSelected) colorResource(id = R.color.orange_pale)
        else colorResource(id = R.color.white), // Use colors from resources
        shape = RoundedCornerShape(24.dp), // Rounded pill shape
        modifier = Modifier
            .padding(9.dp)
            .clickable(onClick = onClick)
            .shadow(4.dp, RoundedCornerShape(48.dp)), // Elevation with rounded shape
        tonalElevation = 8.dp
    ) {
        Column(
            modifier = Modifier
                .padding(4.dp)
                .width(55.dp)
                .height(95.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Round Icon
            Image(
                painter = painterResource(id = speciality.iconResId),
                contentDescription = null,
                modifier = Modifier
                    .size(52.dp) // Icon size
                    .clip(CircleShape), // Make the icon round
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Category name
            Text(
                fontFamily = FontFamily(Font(R.font.sofiapro_medium)),
                text = speciality.name,
                color = if (isSelected) colorResource(id = R.color.white)
                else colorResource(id = R.color.black), // Text color based on selection
                style = MaterialTheme.typography.bodySmall)
        }
    }
}
