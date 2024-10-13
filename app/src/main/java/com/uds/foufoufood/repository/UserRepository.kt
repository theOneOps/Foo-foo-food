import android.util.Log
import com.uds.foufoufood.model.User
import com.uds.foufoufood.network.RoleUpdateRequest
import com.uds.foufoufood.network.UserApi
import com.uds.foufoufood.request.EmailRequest
import com.uds.foufoufood.request.LoginRequest
import com.uds.foufoufood.request.ProfileRequest
import com.uds.foufoufood.request.RegistrationRequest
import com.uds.foufoufood.request.VerificationRequest
import com.uds.foufoufood.response.ApiResponse
import com.uds.foufoufood.response.AuthResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserRepository(private val userApi: UserApi) {

    suspend fun login(email: String, password: String): AuthResponse? = withContext(Dispatchers.IO) {
        try {
            val request = LoginRequest(email, password)
            val response = userApi.login(request)  // Appel API direct
            if (response.isSuccessful) {
                response.body()  // Retourner les données d'authentification si tout va bien
            } else {
                Log.e("UserRepository", "Erreur de connexion: ${response.code()}")
                null
            }
        } catch (e: Exception) {
            Log.e("UserRepository", "Erreur réseau: ${e.message}")
            null
        }
    }

    // Récupérer le profil utilisateur
    suspend fun getUserProfile(token: String): AuthResponse? = withContext(Dispatchers.IO) {
        try {
            val response = userApi.getUserProfile(token)  // Appel API direct
            if (response.isSuccessful) {
                response.body()
            } else {
                Log.e("UserRepository", "Erreur lors de la récupération du profil: ${response.code()}")
                null
            }
        } catch (e: Exception) {
            Log.e("UserRepository", "Erreur réseau: ${e.message}")
            null
        }
    }

    // Inscription - Étape 1
    suspend fun initiateRegistration(name: String, email: String, password: String): Boolean = withContext(Dispatchers.IO) {
        return@withContext try {
            val request = RegistrationRequest(name, email, password)
            val response = userApi.initiateRegistration(request)
            response.isSuccessful && response.body()?.success == true  // Retourne true si succès
        } catch (e: Exception) {
            Log.e("UserRepository", "Erreur d'inscription: ${e.message}")
            false
        }
    }

    // Vérification du code - Étape 2
    suspend fun verifyCode(email: String, code: String): Boolean = withContext(Dispatchers.IO) {
        return@withContext try {
            val request = VerificationRequest(email, code)
            val response = userApi.verifyCode(request)
            response.isSuccessful && response.body()?.success == true
        } catch (e: Exception) {
            Log.e("UserRepository", "Erreur de vérification: ${e.message}")
            false
        }
    }

    // Inscription complète - Étape 3
    suspend fun completeRegistration(email: String, profileType: String): Boolean = withContext(Dispatchers.IO) {
        return@withContext try {
            val request = ProfileRequest(email, profileType)
            val response = userApi.completeRegistration(request)
            response.isSuccessful && response.body()?.success == true
        } catch (e: Exception) {
            Log.e("UserRepository", "Erreur lors de la finalisation de l'inscription: ${e.message}")
            false
        }
    }

    // Renvoi du code de vérification
    suspend fun resendVerificationCode(email: String): Boolean = withContext(Dispatchers.IO) {
        return@withContext try {
            val request = EmailRequest(email)
            val response = userApi.resendVerificationCode(request)
            response.isSuccessful && response.body()?.success == true
        } catch (e: Exception) {
            Log.e("UserRepository", "Erreur lors de l'envoi du code: ${e.message}")
            false
        }
    }



}
