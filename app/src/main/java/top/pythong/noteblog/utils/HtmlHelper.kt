package top.pythong.noteblog.utils

/**
 *
 * @author ChangJiahong
 * @date 2019/8/29
 */
object HtmlHelper {

    fun generateArticleContentHtml(htmlSource: String): String {

        val skin = "markdown_.css"

        return generateArticleContentHtml(htmlSource, skin)
    }

    private fun generateArticleContentHtml(htmlSource: String, skin: String) = run {
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
                    $htmlSource
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

    fun generateImageHtml(url: String, backgroundColor: String): String {
        return """
            <html>
                <head>
                    <style>
                        img{height: auto; width: 100%;}
                        body{background: $backgroundColor;}
                    </style>
                </head>
                <body><img src="$url"/></body>
            </html>"""
    }


    fun generateMdContentHtml(mdSource: String): String {

        val skin = "markdown_.css"

        return generateMdContentHtml(mdSource, skin)
    }


    /**
     * markdown格式
     */
    private fun generateMdContentHtml(mdSource: String, skin: String) = run {
        """
            <html>
                <head>
                    <meta charset="utf-8" />
                    <title>MD View</title>
                    <meta name="viewport" content="width=device-width; initial-scale=1.0; maximum-scale=1.0; user-scalable=0;"/>
                    <link rel="stylesheet" type="text/css" href="./css/$skin"/>

                    <link rel="stylesheet" href="./styles/default.css">
                    <script src="./js/marked.js"></script>
                    <script src="./js/highlight.pack.js"></script>
                    <script>hljs.initHighlightingOnLoad();</script>

                </head>
                <body class="markdown-body">
                    <div id="content">$mdSource</div>
                </body>
                <script type="text/javascript">

                    var body = document.getElementById("content");
                    var mdSource = body.innerHTML;
                    var htm = marked(mdSource);
                    body.innerHTML = htm;

                </script>
            </html>
        """.trimIndent()
    }

    //    <link rel="stylesheet" href="./styles/default.css">
//    <script src="./js/highlight.pack.js"></script>
//    <script>hljs.initHighlightingOnLoad();</script>
}