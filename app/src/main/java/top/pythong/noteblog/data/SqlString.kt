package top.pythong.noteblog.data

/**
 *
 * @author ChangJiahong
 * @date 2019/9/10
 */
enum class SqlString(val sql: String) {

    /**
     * 下载记录表
     */
    TasksTable(
        """
            CREATE TABLE
            IF
                NOT EXISTS TasksTable (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    name CHAR ( 255 ) NOT NULL,
                    fileId CHAR ( 255 ) NOT NULL,
                    toPath VARCHAR ( 255 ) NOT NULL,
                    type VARCHAR( 100 ) NOT NULL,
                    state INTEGER NOT NULL,
                    contentLen INTEGER,
                    downloadLen INTEGER
                )
    """.trimIndent()
    )

    ,

    /**
     * 搜索历史表
     */
    SearchHistory(
        """
            CREATE TABLE
            IF
                NOT EXISTS SearchHistory (
                    _id INTEGER PRIMARY KEY AUTOINCREMENT,
                    name CHAR ( 255 ) NOT NULL UNIQUE,
                    created INTEGER NOT NULL
                )
    """.trimIndent()
    )

    ;

}