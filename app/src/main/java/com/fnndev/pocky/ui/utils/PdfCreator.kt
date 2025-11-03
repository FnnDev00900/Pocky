package com.fnndev.pocky.ui.utils

import android.content.Context
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.net.Uri
import com.fnndev.pocky.data.local.models.Transaction
import com.fnndev.pocky.data.local.models.TransactionType
import java.io.OutputStream
import java.text.NumberFormat

class PdfCreator {

    fun generatePdf(
        context: Context,
        listTransaction: List<Transaction>,
        uri: Uri
    ) {
        try {
            context.contentResolver.openOutputStream(uri)?.use { outputStream ->
                generatePdf(listTransaction, outputStream)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun generatePdf(listTransaction: List<Transaction>, outputStream: OutputStream) {

        if (listTransaction.isEmpty()) return

        val pdfDocument = PdfDocument()

        // A4 Size
        val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create()
        var page = pdfDocument.startPage(pageInfo)
        var canvas = page.canvas

        val paint = Paint()
        paint.textSize = 14f

        var y = 50f
        canvas.drawText("گزارش تراکنش ها", 200f, y, paint)
        y += 40f

        for (transaction in listTransaction) {
            val transactionType =
                if (transaction.type == TransactionType.INCOME) "دریافت" else "پرداخت"
            val transactionAmount = NumberFormat.getInstance().format(transaction.amount) + " ريال"
            val line =
                "${transaction.date} | $transactionType | مبلغ: $transactionAmount | توضیح: ${transaction.description}"
            canvas.drawText(line, 30f, y, paint)
            y += 25f
            if (y > 800f) {
                pdfDocument.finishPage(page)
                page = pdfDocument.startPage(pageInfo)
                canvas = page.canvas
                y = 50f
            }
        }

        pdfDocument.finishPage(page)

        pdfDocument.writeTo(outputStream)
        pdfDocument.close()
    }
}
