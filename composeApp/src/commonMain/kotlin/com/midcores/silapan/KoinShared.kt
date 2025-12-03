package com.midcores.silapan

import com.midcores.silapan.api.ConnectivityObserver
import com.midcores.silapan.api.service.AuthService
import com.midcores.silapan.api.service.GaleriService
import com.midcores.silapan.api.service.JadwalPelayananService
import com.midcores.silapan.api.service.PengaduanService
import com.midcores.silapan.api.service.ProfileService
import com.midcores.silapan.data.repository.AuthRepositoryImpl
import com.midcores.silapan.data.repository.GaleriRepositoryImpl
import com.midcores.silapan.data.repository.JadwalRepositoryImpl
import com.midcores.silapan.data.repository.PengaduanRepositoryImpl
import com.midcores.silapan.data.repository.ProfileRepositoryImpl
import com.midcores.silapan.database.DatabaseDataSource
import com.midcores.silapan.database.createDatabase
import com.midcores.silapan.database.createDriver
import com.midcores.silapan.domain.repository.AuthRepository
import com.midcores.silapan.domain.repository.GaleriRepository
import com.midcores.silapan.domain.repository.JadwalRepository
import com.midcores.silapan.domain.repository.PengaduanRepository
import com.midcores.silapan.domain.repository.ProfileRepository
import com.midcores.silapan.domain.usecase.auth.LoginUsecase
import com.midcores.silapan.domain.usecase.auth.LogoutUsecase
import com.midcores.silapan.domain.usecase.galeri.GetGaleriByCategoryUsecase
import com.midcores.silapan.domain.usecase.galeri.GetGaleriUsecase
import com.midcores.silapan.domain.usecase.jadwal_pelayanan.GetJadwalPelayananUsecase
import com.midcores.silapan.domain.usecase.pengaduan.CreatePengaduanUseCase
import com.midcores.silapan.domain.usecase.pengaduan.DeletePengaduanUseCase
import com.midcores.silapan.domain.usecase.pengaduan.GetPengaduanListUseCase
import com.midcores.silapan.domain.usecase.profile.GetSilapanProfileUsecase
import com.midcores.silapan.domain.usecase.profile.GetUserProfileUsecase
import com.midcores.silapan.presentation.ui.login.LoginViewModel
import com.midcores.silapan.presentation.viewmodel.LocationFormViewModel
import com.midcores.silapan.presentation.viewmodel.galeri.GalleryViewModel
import com.midcores.silapan.presentation.viewmodel.jadwal_pelayanan.JadwalPelayananViewModel
import com.midcores.silapan.presentation.viewmodel.pengaduan.PengaduanFormViewModel
import com.midcores.silapan.presentation.viewmodel.pengaduan.PengaduanViewModel
import com.midcores.silapan.presentation.viewmodel.profile.ProfileViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

@Suppress("unused")
fun initKoin() {
    startKoin {
        printLogger(Level.DEBUG)
        modules(appModule)
    }
}

val commonModule = module {
    singleOf(::createDriver)
    singleOf(::createDatabase)
    singleOf(::DatabaseDataSource)
    singleOf(::SettingsRepository)
    
    single {
        CoroutineScope(Dispatchers.Default + SupervisorJob())
    }

    single { ConnectivityObserver() }
}

val repositoryModule = module {
    // Services
    singleOf(::AuthService)
    singleOf(::PengaduanService)
    singleOf(::GaleriService)
    singleOf(::JadwalPelayananService)
    singleOf(::ProfileService)

    // Repositories
    single<AuthRepository> { AuthRepositoryImpl(get(), get()) }
    single<PengaduanRepository> { PengaduanRepositoryImpl(get(), get()) }
    single<GaleriRepository> { GaleriRepositoryImpl(get(), get()) }
    single<JadwalRepository> { JadwalRepositoryImpl(get(), get()) }
    single<ProfileRepository> { ProfileRepositoryImpl(get(), get()) }

    // Use cases
    factory { LoginUsecase(get()) }
    factory { LogoutUsecase(get()) }
    factory { GetPengaduanListUseCase(get()) }
    factory { CreatePengaduanUseCase(get()) }
    factory { DeletePengaduanUseCase(get()) }
    factory { GetGaleriUsecase(get()) }
    factory { GetGaleriByCategoryUsecase(get()) }
    factory { GetJadwalPelayananUsecase(get()) }
    factory { GetUserProfileUsecase(get()) }
    factory { GetSilapanProfileUsecase(get()) }
}

val viewModelModule = module {
    viewModelOf(::LoginViewModel)
    viewModelOf(::PengaduanFormViewModel)
    viewModelOf(::ProfileViewModel)
    viewModelOf(::JadwalPelayananViewModel)
    viewModelOf(::GalleryViewModel)
    singleOf(::PengaduanViewModel)
    singleOf(::LocationFormViewModel)
}

expect val platformModule: Module

val appModule = listOf(
    platformModule,
    commonModule,
    repositoryModule,
    viewModelModule
)
