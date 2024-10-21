package com.uds.foufoufood.data_class.model
import coil.compose.AsyncImagePainter

data class Restaurant(
    val name:String,
    val address: Address,
    val speciality:String,
    val phone:String,
    val openingHours:String,
    val items:List<Menu>,
    val rating: Double,
    val reviews:List<String>,
    val imageUrl: String,
)

/*

const restaurantSchema = new Schema({
    name: { type: String, required: true, lowercase: true , unique:true},
    address: addressSchema,
    speciality: {
        type: String,
        default: "no speciality",
        lowercase: true,
    },
    phone: {
        type: String,
        validate: {
        validator: isValidPhone,
        message: (props) => `${props.value} is not a valid phone number!`,
    },
    },
    openinghours: {
        type: String,
        validate: { validator: isValidHours, message: "invalid hours" },
    },
    items: [{ type: Schema.Types.ObjectId , ref:"Menus"}],
    rating: { type: Number },
    reviews: { type: [Reviews.schema] },

    // Référence à l'utilisateur propriétaire du restaurant
    userId: {
        type: Schema.Types.ObjectId,
        ref: "Users", // Le modèle "User" auquel ce champ fait référence
    },
});*/
