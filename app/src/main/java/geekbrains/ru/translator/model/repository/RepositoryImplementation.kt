package geekbrains.ru.translator.model.repository

import geekbrains.ru.translator.model.data.DataModel
import geekbrains.ru.translator.model.datasource.DataSource

class RepositoryImplementation(private val dataSource: DataSource<List<DataModel>>) :
    Repository<List<DataModel>> {

    override suspend fun getData(word: String): List<DataModel> {
        return dataSource.getData(word)
    }
}
