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
                    <link rel="stylesheet" type="text/css" href="./css/$skin"/>

                    <link rel="stylesheet" href="./styles/default.css">
                    <script src="./js/highlight.pack.js"></script>
                    <script>hljs.initHighlightingOnLoad();</script>

                </head>
                <body class="markdown-body">
                    $mdSource
                </body>
                <script type="text/javascript">

                    imgClick();

                    codeClick();

                    function imgClick(){
                        var imgs = document.getElementsByTagName("img");
                        for (var i=0;i<imgs.length;i++) {
                            imgs[0].onclick = function(){
                                listener.openImage(this.src);
                            }
                        }

                    }
                    function codeClick(){
                        var codes = document.getElementsByTagName("code");
                        for (var i=0;i<codes.length;i++) {
                            codes[i].onclick = function(){
                                listener.openCode(this.innerHTML, this.className);
                            }
                        }
                    }

                </script>
            </html>
        """
    }

    fun generateCodeHtml(codeSource: String, codeClass: String): String {
        val skin = "prettify"
        return generateCodeHtml(codeSource, codeClass, skin)
    }

    private fun generateCodeHtml(codeSource: String, codeClass: String, skin: String): String {
        return """
            <html>
                <head>
                    <meta charset="utf-8" />
                    <title>Code View</title>
                    <meta name="viewport" content="width=device-width; initial-scale=1.0; "/>
                    <script src="./core/run_prettify.js?autoload=true&amp;skin=$skin&amp;lang=$codeClass&amp;" defer></script>

                    <style>
                        pre {
                            word-wrap: normal;
                            white-space: no-wrap;
                        }
                    </style>

                </head>
                <body class="markdown-body">
                    <?prettify lang=$codeClass linenums=true?>
                    <pre class="prettyprint"><code>$codeSource</code>
                    </pre>
                </body>
            </html>

        """
    }


//    <link rel="stylesheet" href="./styles/default.css">
//    <script src="./js/highlight.pack.js"></script>
//    <script>hljs.initHighlightingOnLoad();</script>
}