package top.pythong.noteblog.app.login.model

import java.util.*


/**
 *
 * @author ChangJiahong
 * @date 2019/8/23
 */
data class LoggedInUser(
    val uid: Int,
    val username: String,
    val email: String,
    val imgUrl: String,
    /**
     * 性别（1男/0女）
     */
    var sex: Boolean,
    /**
     * 年龄
     */
    var age: Int,
    /**
     * 创建时间
     */
    var created: String,
    /**
     * 角色集合
     */
    var roles: List<String>
)