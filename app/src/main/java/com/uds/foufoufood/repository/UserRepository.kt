import android.util.Log
import com.uds.foufoufood.network.UserService
import com.uds.foufoufood.response.AuthResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UserRepository(private val userService: UserService) {
    suspend fun login(email: String, password: String): AuthResponse? = withContext(Dispatchers.IO){
        Log.d("UserRepository", "login")
        return@withContext userService.login(email, password)
    }

    suspend fun getUserProfile(token: String): AuthResponse? = withContext(Dispatchers.IO){
        Log.d("UserRepository", "getUserProfile")
        return@withContext userService.getUserProfile(token)
    }

    // Inscription - Étape 1 : Collecter email, nom, mot de passe et envoyer un email de vérification
    suspend fun initiateRegistration(name: String, email: String, password: String): Boolean {
        return try {
            val response = userService.initiateRegistration(name, email, password)
            Log.d("UserRepository", response.toString())
            response // True si réussi
        } catch (e: Exception) {
            false // En cas d'erreur
        }
    }

    // Vérification du code - Étape 2 : Confirmer le code reçu
    suspend fun verifyCode(email: String, code: String): Boolean {
        return try {
            val response = userService.verifyCode(email, code)
            response
        } catch (e: Exception) {
            false // En cas d'erreur
        }
    }

    // Inscription complète - Étape 3 : Choisir le type de profil
    suspend fun completeRegistration(email: String, profileType: String): Boolean {
        return try {
            val response = userService.completeRegistration(email, profileType)
            response
        } catch (e: Exception) {
            false // En cas d'erreur
        }
    }

    // Renvoi du code de vérification
    suspend fun resendVerificationCode(email: String) = withContext(Dispatchers.IO){
        return@withContext userService.resendVerificationCode(email)
    }
}