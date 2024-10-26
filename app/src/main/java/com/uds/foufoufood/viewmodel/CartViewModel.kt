package com.uds.foufoufood.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uds.foufoufood.data_class.model.CartItem
import com.uds.foufoufood.data_class.model.OrderItem
import com.uds.foufoufood.data_class.request.OrderRequest
import com.uds.foufoufood.repository.OrderRepository
import kotlinx.coroutines.launch

class CartViewModel(
    private val orderRepository: OrderRepository,
    private val userViewModel: UserViewModel // Assuming user info is retrieved from here
) : ViewModel() {

    private val _cartItems = MutableLiveData<List<CartItem>>(mutableListOf())
    val cartItems: LiveData<List<CartItem>> get() = _cartItems

    private val _errorMessage = MutableLiveData<String?>() // To capture error messages
    val errorMessage: LiveData<String?> get() = _errorMessage

    private val _orderSuccessMessage = MutableLiveData<String?>() // To capture success messages
    val orderSuccessMessage: LiveData<String?> get() = _orderSuccessMessage

    private var currentRestaurantId: String? = null // Track the current restaurant ID

    fun addItem(item: CartItem, restaurantId: String) {
        if (currentRestaurantId == null || currentRestaurantId == restaurantId) {
            currentRestaurantId = restaurantId
            val updatedList = _cartItems.value.orEmpty().toMutableList()
            val existingItem = updatedList.find { it.menu._id == item.menu._id }
            if (existingItem != null) {
                existingItem.quantity += item.quantity
            } else {
                updatedList.add(item)
            }
            _cartItems.value = updatedList
        } else {
            clearCart()
            currentRestaurantId = restaurantId
            _cartItems.value = mutableListOf(item)
        }
    }

    fun removeItem(item: CartItem) {
        val updatedList = _cartItems.value.orEmpty().toMutableList()
        val currentItem = updatedList.find { it.menu._id == item.menu._id }

        if (currentItem != null) {
            if (currentItem.quantity > 1) {
                // Remove the item and add back an updated one to avoid direct mutation
                updatedList.remove(currentItem)
                updatedList.add(currentItem.copy(quantity = currentItem.quantity - 1))
            } else {
                // Remove the item completely if quantity is 1
                updatedList.remove(currentItem)
            }
            // Update _cartItems with a new list to trigger recomposition
            _cartItems.value = updatedList.toList()  // Ensuring a new reference is created
        }
    }

    fun clearCart() {
        _cartItems.value = emptyList() // Update LiveData with an empty list
        currentRestaurantId = null // Reset the restaurant ID
    }

    fun checkout() {
        viewModelScope.launch {
            val token = userViewModel.token.value
            val clientEmail = userViewModel.user.value?.email ?: ""
            val clientName = userViewModel.user.value?.name ?: ""
            val restaurantId = currentRestaurantId ?: return@launch
            val deliveryAddress = userViewModel.user.value?.address
            val items = cartItems.value ?: emptyList() // Get the actual list from LiveData

            // Check for an active order before proceeding with checkout
            if (token != null && deliveryAddress != null) {
                val hasActiveOrder = orderRepository.hasActiveOrder(token, clientEmail)
                if (hasActiveOrder) {
                    _errorMessage.value = "Vous avez déjà une commande en cours."
                    return@launch
                }

                val orderRequest = OrderRequest(
                    clientEmail = clientEmail,
                    clientName = clientName,
                    restaurantId = restaurantId,
                    deliveryAddress = deliveryAddress,
                    dishes = items.map { OrderItem(menu = it.menu, quantity = it.quantity) } // Map items to OrderItem list
                )

                val orderResponse = orderRepository.createOrder(token, orderRequest)
                if (orderResponse != null) {
                    clearCart()
                    _orderSuccessMessage.value = "Commande passée avec succès !" // Set success message
                } else {
                    _errorMessage.value = "Échec de la création de la commande. Veuillez réessayer."
                }
            } else {
                _errorMessage.value = "Informations utilisateur ou adresse manquantes."
            }
        }
    }


    fun clearErrorMessage() {
        _errorMessage.value = null
    }

    fun clearSuccessMessage() {
        _orderSuccessMessage.value = null
    }
}