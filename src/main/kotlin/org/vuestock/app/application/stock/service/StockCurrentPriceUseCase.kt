package org.vuestock.app.application.stock.service

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.vuestock.app.adapter.stock.out.StockDistributionExternal
import org.vuestock.app.adapter.stock.out.command.KisApiHeader
import org.vuestock.app.application.stock.port.`in`.StockAuthorizationResolver
import org.vuestock.app.application.stock.port.`in`.StockCurrentPricePort
import org.vuestock.app.application.stock.port.`in`.dto.StockPrice
import org.vuestock.app.application.stock.type.StockSign
import java.time.LocalDateTime

@Service
class StockCurrentPriceUseCase(
    private val stockDistributionExternal: StockDistributionExternal,
    private val stockAuthorizationResolverImpl: StockAuthorizationResolver,
    @Value("\${kis.distribution-trid}")
    private val distributionTrId: String
) : StockCurrentPricePort {

    override fun getCurrentPrice(stockCode: String): StockPrice {
        val authInfo = stockAuthorizationResolverImpl.getAuthorizationInfo()
        val kisApiHeader = KisApiHeader.from(authInfo!!, distributionTrId)
        val bodyJson = stockDistributionExternal.requestDistributions(kisApiHeader, stockCode, LocalDateTime.now())
        if (!bodyJson.containsKey("output1")) {
            return StockPrice.empty()
        }
        val currentPriceInfo = bodyJson["output1"] as Map<*, *>
        val currentPrice = (currentPriceInfo["stck_prpr"] as String).toInt()
        val previousDayPriceDiff = (currentPriceInfo["prdy_vrss"] as String).toInt()
        val previousDayPriceDiffPercent = (currentPriceInfo["prdy_ctrt"] as String).toFloat()
        return StockPrice(
            currentPrice = currentPrice,
            previousDayPriceDiff = previousDayPriceDiff,
            previousDaySign = StockSign.findBySign(currentPriceInfo["prdy_vrss_sign"] as String),
            previousDayPriceDiffPercent = previousDayPriceDiffPercent,
            date = "",
            time = ""
        )
    }
}