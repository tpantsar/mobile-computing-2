package com.codemave.core.data.datasource.payment

import com.codemave.core.database.dao.PaymentDao
import com.codemave.core.database.entity.PaymentEntity
import com.codemave.core.domain.entity.Category
import com.codemave.core.domain.entity.Payment
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PaymentDataSourceImpl @Inject constructor(
    private val paymentDao: PaymentDao
) : PaymentDataSource {
    override suspend fun addPayment(payment: Payment) {
        paymentDao.insertOrUpdate(payment.toEntity())
    }

    override suspend fun updatePayment(payment: Payment) {
        paymentDao.insertOrUpdate(payment.toEntity())
    }

    /* TODO */
    /*override suspend fun getPayment(payment: Payment) {
        paymentDao.findOne(paymentId = Long)
    }*/

    override suspend fun deletePayment(payment: Payment) {
        paymentDao.delete(payment.toEntity())
    }

    override suspend fun loadPaymentsFor(category: Category): Flow<List<Payment>> {
        return paymentDao.findPaymentsByCategory(category.categoryId).map { list ->
            list.map {
                it.fromEntity()
            }
        }
    }

    override suspend fun loadAllPayments(): List<Payment> {
        return paymentDao.findAll().map {
            it.fromEntity()
        }
    }

    private fun Payment.toEntity() = PaymentEntity(
        paymentId = this.paymentId,
        categoryId = this.categoryId,
        title = this.title,
        priority = this.priority,
        date = this.date
    )

    private fun PaymentEntity.fromEntity() = Payment(
        paymentId = this.paymentId,
        categoryId = this.categoryId,
        priority = this.priority,
        date = this.date,
        title = this.title
    )
}