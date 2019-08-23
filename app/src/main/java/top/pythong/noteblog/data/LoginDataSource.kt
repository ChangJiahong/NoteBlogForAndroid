package top.pythong.noteblog.data

import android.util.Log
import top.pythong.noteblog.data.model.LoggedInUser
import java.io.IOException



/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class LoginDataSource {

    fun login(username: String, password: String): Result<LoggedInUser> {

//        val restTemplate = RestTemplate()
//
//        var headers = HttpHeaders()
//        val httpEntity = HttpEntity<MultiValueMap<String, Any>>( headers)
//
//        var restResponse = restTemplate.postForEntity("http://172.16.113.2/login",
//            httpEntity,
//            RestResponse::class.java,
//            username, password)

//        Log.d("响应msg",restResponse.body.msg)
        return Result.Success(LoggedInUser("C", "JH"))
    }

    fun logout() {
        // TODO: revoke authentication
    }
}

