package geekbrains.ru.translator

import android.provider.ContactsContract.Data
import com.nhaarman.mockito_kotlin.mock
import geekbrains.ru.translator.model.data.AppState
import geekbrains.ru.translator.model.data.DataModel
import geekbrains.ru.translator.model.data.Meanings
import geekbrains.ru.translator.model.data.Translation
import geekbrains.ru.translator.model.datasource.RetrofitImplementation
import geekbrains.ru.translator.model.repository.Repository
import geekbrains.ru.translator.model.repository.RepositoryImplementation
import geekbrains.ru.translator.model.repository.RepositoryImplementationLocal
import geekbrains.ru.translator.model.repository.RepositoryLocal
import geekbrains.ru.translator.view.main.MainInteractor
import geekbrains.ru.translator.viewmodel.Interactor
import kotlinx.coroutines.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import java.security.CodeSource
import kotlin.coroutines.CoroutineContext

class UnitTest_Translator {

    @Mock
    private lateinit var repository: Repository<List<DataModel>>

    @Mock
    private lateinit var repositoryLocal: RepositoryLocal<List<DataModel>>


    private lateinit var mainInteractor: MainInteractor

    val appStateSuccess: AppState = AppState.Success(
        listOf(
            DataModel(
                "Hello",
                listOf(Meanings(Translation("Привет"), ""))
            )

        )
    )

    val responseAppState = mock<AppState>()
    val someWord = "hello"
    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        val mainInteractor = MainInteractor(repository, repositoryLocal)
    }

    val job = CoroutineScope(
        Dispatchers.Main
                + SupervisorJob()
    )


    @Test
    fun repositoryRemote_getData_Test() {


        job.launch { mainInteractor.getData(someWord, true) }
        job.launch { Mockito.verify(repository, Mockito.times(1)).getData(someWord) }
        job.cancel()
    }

    @Test
    fun repositoryRemote_saveToDB(){
        val someWord = "hello"

        job.launch { mainInteractor.getData(someWord, true) }
        job.launch { Mockito.verify(repositoryLocal, Mockito.times(1)).saveToDB(appStateSuccess) }
        job.cancel()
    }

    @Test
    fun repositoryLocal_getData_ReturnDataModel() {


        val listData = listOf(
            DataModel(
                "Hello",
                listOf(Meanings(Translation("Привет"), ""))
            )

        )

        job.launch { mainInteractor.getData(someWord, true) }
        job.launch { Mockito.`when`(repositoryLocal.getData(someWord)).thenReturn(listData)}
        job.cancel()

    }

    @Test
    fun mainInteractor_returnAppState(){

        job.launch {  Mockito.`when`(mainInteractor.getData(someWord,true)).thenReturn(appStateSuccess)}
        job.launch {  mainInteractor.getData(someWord,true)}

        job.cancel()
    }

}