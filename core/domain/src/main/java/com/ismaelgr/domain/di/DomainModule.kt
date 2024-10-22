package com.ismaelgr.domain.di

import com.ismaelgr.domain.IRepository
import com.ismaelgr.domain.manager.PunctuationManager
import com.ismaelgr.domain.manager.StatisticManager
import com.ismaelgr.domain.usecase.CalculateSetsToWinUseCase
import com.ismaelgr.domain.usecase.CleanEstimationsUseCase
import com.ismaelgr.domain.usecase.DownloadDataUseCase
import com.ismaelgr.domain.usecase.GenerateEstimationOfDateUseCase
import com.ismaelgr.domain.usecase.GenerateStatisticOfDateUseCase
import com.ismaelgr.domain.usecase.GetNextResultDateUseCase
import com.ismaelgr.domain.usecase.GetResultsUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class)
object DomainModule {
    
    // section Manager
    @Provides
    fun getPunctuationsManager(iRepository: IRepository): PunctuationManager =
        PunctuationManager(iRepository)
    
    @Provides
    fun getStatisticsManager(iRepository: IRepository): StatisticManager = StatisticManager(iRepository)
    // end Section
    // section UseCase
    @Provides
    fun getDownLoadDataUseCase(iRepository: IRepository): DownloadDataUseCase = DownloadDataUseCase(iRepository)
    
    @Provides
    fun getResultsUseCase(iRepository: IRepository): GetResultsUseCase = GetResultsUseCase(iRepository)
    
    @Provides
    fun getGenerateStatisticOfDateUseCase(iRepository: IRepository, punctuationManager: PunctuationManager): GenerateStatisticOfDateUseCase = GenerateStatisticOfDateUseCase(iRepository, punctuationManager)
    
    @Provides
    fun getNextResultDateUseCase(iRepository: IRepository): GetNextResultDateUseCase = GetNextResultDateUseCase(iRepository)
    
    @Provides
    fun getGenerateEstimationsOfDateUseCase(iRepository: IRepository, punctuationManager: PunctuationManager, statisticManager: StatisticManager): GenerateEstimationOfDateUseCase = GenerateEstimationOfDateUseCase(iRepository, punctuationManager, statisticManager)
    
    @Provides
    fun getCleanEstimationsUseCase(iRepository: IRepository): CleanEstimationsUseCase = CleanEstimationsUseCase(iRepository)
    
    @Provides
    fun getCalculateSetsToWinUseCase(iRepository: IRepository, punctuationManager: PunctuationManager, statisticManager: StatisticManager): CalculateSetsToWinUseCase = CalculateSetsToWinUseCase(iRepository, punctuationManager, statisticManager)
    // end Section
}