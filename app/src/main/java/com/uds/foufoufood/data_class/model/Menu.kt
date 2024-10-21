package com.uds.foufoufood.data_class.model

data class Menu(
    val name:String,
    val descriptor: String,
    val price: Double,
    val category: String,
    val image:String,
    val restaurantId:String
)


/*
const menuSchema = new Schema({
    name: { type: String, required: true, lowercase: true },
    description: { type: String, maxLength: 140, lowercase: true, default:"" },
    price: { type: Number, default: 0, required: true },
    category: { type: String, default: "", lowercase: true, required:true },
    image: { type: String, required:true, unique:true },
    restaurantId: {
        type: Schema.Types.ObjectId,
        required: true,
        ref: "Restaurants",
    },
});*/
