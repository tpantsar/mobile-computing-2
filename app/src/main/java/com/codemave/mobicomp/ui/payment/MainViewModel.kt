package com.codemave.mobicomp.ui.payment

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat.from
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codemave.core.domain.entity.Category
import com.codemave.core.domain.entity.Payment
import com.codemave.core.domain.repository.CategoryRepository
import com.codemave.core.domain.repository.PaymentRepository
import com.codemave.mobicomp.Graph
import com.codemave.mobicomp.R
import com.codemave.mobicomp.ui.category.CategoryViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDateTime
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@HiltViewModel
class MainViewModel @Inject constructor(
    private val paymentRepository: PaymentRepository,
    private val categoryRepository: CategoryRepository
): ViewModel() {

    private val _paymentViewState = MutableStateFlow<PaymentViewState>(PaymentViewState.Loading)
    val paymentState: StateFlow<PaymentViewState> = _paymentViewState

    private val _categoryList: MutableStateFlow<List<Category>> = MutableStateFlow(mutableListOf())
    val categories: StateFlow<List<Category>> =_categoryList

    private val _categoryViewState = MutableStateFlow<CategoryViewState>(CategoryViewState.Loading)
    val categoryState: StateFlow<CategoryViewState> = _categoryViewState

    private val _selectedCategory = MutableStateFlow<Category?>(null)

    fun savePayment(payment: Payment) {
        viewModelScope.launch {
            paymentRepository.addPayment(payment)
            notifyUserOfPayment(payment)
        }
    }

    /* TODO */
    fun deleteReminder(payment: Payment) {
        viewModelScope.launch {
            paymentRepository.deletePayment(payment)
        }
    }

    /* TODO */
    fun updateCategory(category: Category) {
        viewModelScope.launch {
            categoryRepository.updateCategory(category)
        }
    }

    /* TODO */
    fun updatePayment(payment: Payment) {
        viewModelScope.launch {
            paymentRepository.updatePayment(payment)
        }
    }

    /* TODO */
    fun getPayment(id: Int) {
        viewModelScope.launch {
            val payment = paymentRepository.getPayment(id)
        }
    }

    fun onCategorySelected(category: Category) {
        _selectedCategory.value = category
    }

    private fun notifyUserOfPayment(payment: Payment) {
        val notificationId = 10
        val builder = NotificationCompat.Builder(
            Graph.appContext,
            "channel_id"
        )
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("New reminder set")
            .setContentText("Reminder '${payment.title}' on ${payment.date}")
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        with(from(Graph.appContext)) {
            if (ActivityCompat.checkSelfPermission(
                    Graph.appContext,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {

                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return
            }
            notify(notificationId, builder.build())
        }
    }

    private fun createNotificationChannel() {
        val name = "NotificationChannel"
        val descriptionText = "NotificationChannelDescription"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel("channel_id", name, importance).apply {
            description = descriptionText
        }
        val notificationManager = Graph.appContext
            .getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    fun loadPaymentsFor(category: Category?) {
        if (category != null) {
            viewModelScope.launch {
                val payments = paymentRepository.loadAllPayments()
                _paymentViewState.value =
                    PaymentViewState.Success(
                        payments.filter {
                            it.categoryId == category.categoryId }
                    )
            }
        }
    }

    private suspend fun loadCategories() {
        combine(
            categoryRepository.loadCategories()
                .onEach { categories ->
                    if (categories.isNotEmpty() && _selectedCategory.value == null) {
                        _selectedCategory.value = categories.first()
                    }
                },
            _selectedCategory
        ) { categories, selectedCategory ->
            _categoryViewState.value = CategoryViewState.Success(selectedCategory, categories)
            _categoryList.value = categories
        }
            .catch { error -> CategoryViewState.Error(error) }
            .launchIn(viewModelScope)
    }

    private fun fakeData() = listOf(
        Category(name = "Personal"),
        Category(name = "Study"),
        Category(name = "Work"),
        Category(name = "Other")
    )

    private fun dummyData() : List<Payment> {
        return listOf(
            Payment(
                title = "Buy groceries",
                categoryId = 1,
                priority = 2,
                date = LocalDateTime.now()
            ),
            Payment(
                title = "Make food",
                categoryId = 1,
                priority = 3,
                date = LocalDateTime.now()
            ),
            Payment(
                title = "Prepare for exam",
                categoryId = 2,
                priority = 1,
                date = LocalDateTime.now()
            ),
            Payment(
                title = "Finish homework",
                categoryId = 2,
                priority = 1,
                date = LocalDateTime.now()
            )
        )
    }

    init {
        createNotificationChannel()

        fakeData().forEach {
            viewModelScope.launch {
                categoryRepository.addCategory(it)
            }
        }
        dummyData().forEach {
            viewModelScope.launch {
                savePayment(it)
            }
        }
        viewModelScope.launch {
            loadCategories()
        }
    }
}

