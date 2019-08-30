package top.pythong.noteblog.utils

import org.apache.commons.lang3.StringUtils

/**
 *
 * @author ChangJiahong
 * @date 2019/8/29
 */
object HtmlHelper {

    fun generateArticleContentHtml(mdSource: String): String {

        val skin = "markdown_.css"

        return generateArticleContentHtml(mdSource, skin)
    }

    private fun generateArticleContentHtml(mdSource: String, skin: String) = run {
        """
            <html>
                <head>
                    <meta charset="utf-8" />
                    <title>MD View</title>
                    <meta name="viewport" content="width=device-width; initial-scale=1.0; maximum-scale=1.0; user-scalable=0;"/>
                    <link rel="stylesheet" type="text/css" href="./$skin"/>
                    <style>

                    </style>
                </head>
                <body class="markdown-body">
                    $mdSource
                </body>
            </html>
        """
    }

}