package org.vuestock.app.adapter.stock.out

import org.springframework.data.jpa.repository.JpaRepository
import org.vuestock.app.domain.stock.Stock

interface StockRepository : JpaRepository<Stock, String> {
}