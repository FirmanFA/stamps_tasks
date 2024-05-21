import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import java.net.HttpURLConnection
import java.net.URL
import java.text.SimpleDateFormat
import java.util.Date
import kotlin.math.sqrt

fun main(args: Array<String>) {

    task1()
    println()
    println()
    println()
    task2()

}

fun task2() {
    val url =
        URL("https://api.openweathermap.org/data/2.5/forecast?units=metric&lon=112.6239367&lat=-7.9354367&appid=073ad9e22e0b29bed8693631a41dc840")
    val connection = url.openConnection() as HttpURLConnection

    try {
        connection.requestMethod = "GET"
        connection.connectTimeout = 5000
        connection.readTimeout = 5000

        val responseCode = connection.responseCode
        if (responseCode == HttpURLConnection.HTTP_OK) {
            val response = connection.inputStream.bufferedReader().use { it.readText() }
            val jsonResponse = Json.decodeFromString<JsonObject>(response)
            val weatherList = jsonResponse["list"]?.jsonArray

            println("Weather Forecast:")
            weatherList?.distinctBy {
                val weatherData = it.jsonObject;
                val dateFormatter = SimpleDateFormat("EEE, dd MMM yyyy")
                val dateFromRawResponse = dateFormatter.format(Date((weatherData["dt"].toString().toLong() * 1000L)))

                dateFromRawResponse
            }?.forEach {

                val weatherData = it.jsonObject;
                val dateFormatter = SimpleDateFormat("EEE, dd MMM yyyy")
                val dateFromRawResponse = dateFormatter.format(Date((weatherData["dt"].toString().toLong() * 1000L)))

                println("${dateFromRawResponse}: ${weatherData["main"]?.jsonObject?.get("temp")}\u00B0C")
            }

        } else {
            println("Error: $responseCode")
        }
    } catch (e: Exception) {
        e.printStackTrace()
    } finally {
        connection.disconnect()
    }
}

fun task1() {
    for (i in 100 downTo 1) {

        if (isPrime(i)) {
            continue
        } else {

            when {
                (isDivisibleBy3(i) && isDivisibleBy5(i)) -> print("FooBar, ")
                (isDivisibleBy3(i)) -> print("Foo, ")
                (isDivisibleBy5(i)) -> print("Bar, ")
                else -> print("$i, ")
            }
        }
    }
}

fun isDivisibleBy3(number: Int): Boolean {
    return number % 3 == 0
}

fun isDivisibleBy5(number: Int): Boolean {
    return number % 5 == 0
}

fun isPrime(number: Int): Boolean {
    if (number <= 1) return false
    if (number == 2) return true
    if (number % 2 == 0) return false

    for (i in 3..sqrt(number.toDouble()).toInt() step 2) {
        if (number % i == 0) return false
    }

    return true
}