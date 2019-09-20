package top.pythong.noteblog.data

/**
 *
 * @author ChangJiahong
 * @date 2019/9/10
 */
object SqlString {

    val TasksTable = """
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
}