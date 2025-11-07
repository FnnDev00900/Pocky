package com.fnndev.pocky.ui.utils

import android.content.Context
import android.graphics.*
import android.graphics.pdf.PdfDocument
import android.net.Uri
import androidx.core.content.res.ResourcesCompat
import com.fnndev.pocky.R
import com.fnndev.pocky.data.local.models.Transaction
import com.fnndev.pocky.data.local.models.TransactionType
import java.io.OutputStream
import java.text.NumberFormat

class PdfCreator {

    fun generatePdf(context: Context, listTransaction: List<Transaction>, uri: Uri) {
        try {
            context.contentResolver.openOutputStream(uri)?.use { outputStream ->
                generatePdf(context, listTransaction, outputStream)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun generatePdf(context: Context, listTransaction: List<Transaction>, outputStream: OutputStream) {
        if (listTransaction.isEmpty()) return

        val pdfDocument = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create()

        var typeface = ResourcesCompat.getFont(context, R.font.nazanin)

        val paint = Paint().apply {
            textSize = 14f
            color = Color.BLACK
            textAlign = Paint.Align.RIGHT
            this.typeface = typeface
        }

        val titlePaint = Paint(paint).apply {
            textSize = 18f
            typeface = Typeface.create(typeface, Typeface.BOLD)
        }

        val linePaint = Paint().apply {
            color = Color.GRAY
            strokeWidth = 1f
        }

        val boldPaint = Paint(paint).apply {
            typeface = Typeface.create(typeface, Typeface.BOLD)
        }

        val columnWidths = floatArrayOf(80f, 80f, 120f, 200f)
        val headers = arrayOf("تاریخ", "نوع", "مبلغ (ریال)", "توضیح")

        var totalIncome = 0L
        var totalExpense = 0L

        var pageNumber = 1
        var page = pdfDocument.startPage(pageInfo)
        var canvas = page.canvas
        var y = 60f

        fun drawHeader() {
            canvas.drawText("گزارش تراکنش‌ها (صفحه $pageNumber)", 550f, 60f, titlePaint)
            var x = 550f
            var headerY = 100f
            for (i in headers.indices) {
                canvas.drawText(headers[i], x, headerY, paint)
                x -= columnWidths[i]
            }
            headerY += 10f
            canvas.drawLine(40f, headerY, 550f, headerY, linePaint)
            y = headerY + 20f
        }

        drawHeader()

        for (transaction in listTransaction) {
            val transactionType =
                if (transaction.type == TransactionType.INCOME) "دریافت" else "پرداخت"
            val amountStr = NumberFormat.getInstance().format(transaction.amount)
            val values = arrayOf(transaction.date, transactionType, amountStr, transaction.description)

            var x = 550f
            for (i in values.indices) {
                canvas.drawText(values[i], x, y, paint)
                x -= columnWidths[i]
            }

            y += 20f
            canvas.drawLine(40f, y, 550f, y, linePaint)
            y += 10f

            if (transaction.type == TransactionType.INCOME)
                totalIncome += transaction.amount
            else
                totalExpense += transaction.amount

            if (y > 780f) {
                pdfDocument.finishPage(page)
                pageNumber++
                page = pdfDocument.startPage(pageInfo)
                canvas = page.canvas
                drawHeader()
            }
        }

        y += 30f
        canvas.drawText(
            "جمع دریافتی‌ها: ${NumberFormat.getInstance().format(totalIncome)} ریال",
            550f,
            y,
            boldPaint
        )
        y += 25f
        canvas.drawText(
            "جمع پرداختی‌ها: ${NumberFormat.getInstance().format(totalExpense)} ریال",
            550f,
            y,
            boldPaint
        )
        y += 25f
        canvas.drawText(
            "مانده: ${NumberFormat.getInstance().format(totalIncome - totalExpense)} ریال",
            550f,
            y,
            boldPaint
        )

        pdfDocument.finishPage(page)
        pdfDocument.writeTo(outputStream)
        pdfDocument.close()
    }
}
