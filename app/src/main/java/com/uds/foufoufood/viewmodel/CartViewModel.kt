package com.uds.foufoufood.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uds.foufoufood.data_class.model.CartItem
import com.uds.foufoufood.data_class.model.OrderItem
import com.uds.foufoufood.data_class.request.OrderRequest
import com.uds.foufoufood.repository.OrderRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class CartViewModel(
    private val orderRepository: OrderRepository, private val userViewModel: UserViewModel
) : ViewModel() {

    private val _cartItems = MutableLiveData<List<CartItem>>(mutableListOf())
    val cartItems: LiveData<List<CartItem>> get() = _cartItems

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    private val _orderSuccessMessage = MutableLiveData<String?>()
    val orderSuccessMessage: LiveData<String?> get() = _orderSuccessMessage

    private var currentRestaurantId: String? = null

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

    fun incrementQuantity(item: CartItem) {
        val updatedList = (_cartItems.value ?: emptyList()).map {
            if (it.menu._id == item.menu._id) {
                it.copy(quantity = it.quantity + 1)
            } else {
                it
            }
        }
        _cartItems.value = updatedList
    }

    fun decrementQuantity(item: CartItem) {
        val updatedList = (_cartItems.value ?: emptyList()).mapNotNull {
            when {
                it.menu._id == item.menu._id && it.quantity > 1 -> it.copy(quantity = it.quantity - 1)
                it.menu._id == item.menu._id && it.quantity == 1 -> null
                else -> it
            }
        }
        _cartItems.value = updatedList
    }

    fun removeItem(item: CartItem) {
        val updatedList =
            (_cartItems.value ?: emptyList()).filterNot { it.menu._id == item.menu._id }
        _cartItems.value = updatedList
    }

    fun clearCart() {
        _cartItems.value = emptyList()
        currentRestaurantId = null
    }

    fun checkout() {
        viewModelScope.launch {
            val token = userViewModel.token.value
            val clientEmail = userViewModel.user.value?.email ?: ""
            val clientName = userViewModel.user.value?.name ?: ""
            val restaurantId = currentRestaurantId ?: return@launch
            val deliveryAddress = userViewModel.user.value?.address
            val items = cartItems.value ?: emptyList()

            if (token != null && deliveryAddress != null) {
                val hasActiveOrder = orderRepository.hasActiveOrder(token, clientEmail)
                if (hasActiveOrder) {
                    _errorMessage.value = "Vous avez déjà une commande en cours."
                    return@launch
                }

                val orderRequest = OrderRequest(clientEmail = clientEmail,
                    clientName = clientName,
                    restaurantId = restaurantId,
                    deliveryAddress = deliveryAddress,
                    dishes = items.map {
                        OrderItem(
                            menu = it.menu, quantity = it.quantity
                        )
                    })

                val orderResponse = orderRepository.createOrder(token, orderRequest)

                if (orderResponse != null) {
                    clearCart()
                    _orderSuccessMessage.value = "Commande passée avec succès !"

                    delay(100)

                    val assignResponse =
                        orderRepository.assignOrderToDelivery(orderResponse.orderId)
                    if (assignResponse != null) {
                        _orderSuccessMessage.value = "Commande assignée à un livreur !"
                    } else {
                        _errorMessage.value = "En attende d'un livreur..."
                    }
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