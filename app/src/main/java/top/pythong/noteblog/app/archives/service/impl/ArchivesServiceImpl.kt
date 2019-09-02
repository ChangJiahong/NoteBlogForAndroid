package top.pythong.noteblog.app.archives.service.impl

import android.content.Context
import top.pythong.noteblog.app.archives.dao.IArchivesDataSource
import top.pythong.noteblog.app.archives.model.Archive
import top.pythong.noteblog.app.archives.service.IArchivesService
import top.pythong.noteblog.app.article.service.IArticleService
import top.pythong.noteblog.app.home.model.PageInfo
import top.pythong.noteblog.data.Result

/**
 *
 * @author ChangJiahong
 * @date 2019/9/2
 */
class ArchivesServiceImpl(private val context: Context, private val archivesDataSource: IArchivesDataSource) :
    IArchivesService {

    override fun getArchives(page: Int, size: Int): Result<PageInfo<Archive>> {
        val restResponse = archivesDataSource.getArchives(page.toString(), size.toString())
        if (restResponse.isOk()) {

            val pageInfo = restResponse.data!!

            return Result.ok(pageInfo)
        }

        return Result.fail(restResponse)
    }
}