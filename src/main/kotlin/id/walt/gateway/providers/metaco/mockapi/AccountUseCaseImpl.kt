package id.walt.gateway.providers.metaco.mockapi

import id.walt.gateway.Common
import id.walt.gateway.dto.*
import id.walt.gateway.usecases.AccountUseCase
import java.time.LocalDateTime
import java.util.*

class AccountUseCaseImpl : AccountUseCase {
    val tickerUseCase = TickerUseCaseImpl()

    override fun profile(parameter: AccountParameter) = Result.success((0..1).map { getProfile(parameter.accountId) })

    override fun balance(parameter: AccountParameter) = Result.success(AccountBalance((1..2).map { getBalance() }))
    override fun balance(parameter: BalanceParameter) = Result.success(getBalance())

    override fun transactions(parameter: AccountParameter) =
        Result.success((1..24).map { getTransaction(UUID.randomUUID().toString()) })

    override fun transaction(parameter: TransactionParameter) = Result.success(getTransactionTransferData())

    private fun getProfile(id: String) = ProfileData(
        id = UUID.randomUUID().toString(),
        alias = Common.getRandomString(7, 1),
        addresses = listOf("0x${Common.getRandomString(40, 2)}"),
        ticker = listOf("tGOLD", "eth")[Common.getRandomInt(from = 0, to = 2)]
    )

    private fun getBalance() = BalanceData(
        amount = Common.getRandomString(Common.getRandomInt(from = 3, to = 16), 0).removePrefix("0"),
        ticker = tickerUseCase.get(TickerParameter("")).getOrThrow(),
    )

    private fun getTransaction(id: String) = tickerUseCase.get(TickerParameter("")).getOrThrow().let {
        TransactionData(
            id = id,
            relatedAccount = "0x${Common.getRandomString(40, 2)}",
            amount = Common.getRandomString(3, 0),
            ticker = it,
            price = it.price,
            type = listOf("Transfer", "Sell", "Buy", "Receive")[Common.getRandomInt(to = 4)],
            status = getTransactionStatus(),
            date = getDate(),
        )
    }

    private fun getTokenTriple() = let {
        val name = listOf("tGOLD", "Stable Coin")[Common.getRandomInt(to = 2)]
        val kind = if (name == "tGOLD") "Contract" else "Native"
        val symbol = if (name == "tGOLD") "tGOLD" else "eth"
        Triple(name, kind, symbol)
    }

    private fun getTransfers() = (1..2).map {
        TransferData(
            amount = Common.getRandomLong(to = 10000).toString(),
            type = listOf("Transfer", "Fee")[it % 2],
            address = listOf("0x${Common.getRandomString(40, 2)}", null)[it % 2]
        )
    }

    private fun getTransactionTransferData() = TransactionTransferData(
        status = getTransactionStatus(),
        date = getDate(),
        total = getAmountWithValue(),
        transfers = getTransfers()
    )

    private fun getTransactionStatus() = listOf("Detected", "Confirmed", "Expired")[Common.getRandomInt(to = 3)]

    private fun getDate() = LocalDateTime.now().plusDays(Common.getRandomLong(from = -10, to = 0)).toString()

    private fun getAmountWithValue() = AmountWithValue(
        amount = Common.getRandomLong(to = 10000).toString(),
        ticker = tickerUseCase.get(TickerParameter("")).getOrThrow()
    )
}