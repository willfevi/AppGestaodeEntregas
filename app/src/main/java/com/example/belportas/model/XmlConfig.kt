package com.example.belportas.model

import android.util.Xml
import com.example.belportas.model.data.Task
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import java.io.IOException
import java.io.StringReader
import java.text.SimpleDateFormat


class XmlConfig {

    fun parseXml(xml: String): Task? {
        try {
            val parser = Xml.newPullParser()
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false)
            parser.setInput(StringReader(xml))
            parser.nextTag()
            parser.require(XmlPullParser.START_TAG, null, "nfeProc")
            while (parser.next() != XmlPullParser.END_TAG) {
                if (parser.eventType != XmlPullParser.START_TAG) {
                    continue
                }
                if (parser.name == "NFe") {
                    return readNFe(parser)
                } else {
                    skip(parser)
                }
            }
        } catch (e: XmlPullParserException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }

    @Throws(XmlPullParserException::class, IOException::class)
    private fun readNFe(parser: XmlPullParser): Task {
        var noteNumber = ""
        var value = ""
        var address = ""
        var city = ""
        var deliveryStatus = ""
        var date = ""
        var clientName = ""

        parser.require(XmlPullParser.START_TAG, null, "NFe")
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.eventType != XmlPullParser.START_TAG) {
                continue
            }
            when (parser.name) {
                "infNFe" -> {
                    parser.require(XmlPullParser.START_TAG, null, "infNFe")
                    while (parser.next() != XmlPullParser.END_TAG) {
                        if (parser.eventType != XmlPullParser.START_TAG) {
                            continue
                        }
                        when (parser.name) {
                            "ide" -> {
                                parser.require(XmlPullParser.START_TAG, null, "ide")
                                while (parser.next() != XmlPullParser.END_TAG) {
                                    if (parser.eventType != XmlPullParser.START_TAG) {
                                        continue
                                    }
                                    when (parser.name) {
                                        "nNF" -> noteNumber = readValue(parser, "nNF")
                                        "dhEmi" -> {
                                            val rawDate = readValue(parser, "dhEmi")
                                            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX")
                                            val outputFormat = SimpleDateFormat("dd/MM/yyyy ")
                                            date = outputFormat.format(inputFormat.parse(rawDate))
                                        }
                                        else -> skip(parser)
                                    }
                                }
                            }
                            "dest" -> {
                                parser.require(XmlPullParser.START_TAG, null, "dest")
                                while (parser.next() != XmlPullParser.END_TAG) {
                                    if (parser.eventType != XmlPullParser.START_TAG) {
                                        continue
                                    }
                                    when (parser.name) {
                                        "xNome" -> clientName = readValue(parser, "xNome")
                                        "enderDest" -> {
                                            parser.require(XmlPullParser.START_TAG, null, "enderDest")
                                            while (parser.next() != XmlPullParser.END_TAG) {
                                                if (parser.eventType != XmlPullParser.START_TAG) {
                                                    continue
                                                }
                                                when (parser.name) {
                                                    "xMun" -> {
                                                        city = readValue(parser, "xMun") + " "
                                                    }
                                                    "UF" -> {
                                                        city += readValue(parser, "UF") + " "
                                                    }
                                                    "xBairro" -> {
                                                        address += readValue(parser, "xBairro") + " "
                                                    }
                                                    "xLgr" -> {
                                                        address += readValue(parser, "xLgr") + " "
                                                    }
                                                    "nro" -> {
                                                        address += readValue(parser, "nro") + " "
                                                    }
                                                    "xCpl" -> {
                                                        address += readValue(parser, "xCpl")
                                                    }
                                                    else -> {
                                                        skip(parser)
                                                    }
                                                }
                                            }
                                        }
                                        else -> skip(parser)
                                    }
                                }
                            }
                            "total" -> {
                                parser.require(XmlPullParser.START_TAG, null, "total")
                                while (parser.next() != XmlPullParser.END_TAG) {
                                    if (parser.eventType != XmlPullParser.START_TAG) {
                                        continue
                                    }
                                    if (parser.name == "ICMSTot") {
                                        parser.require(XmlPullParser.START_TAG, null, "ICMSTot")
                                        while (parser.next() != XmlPullParser.END_TAG) {
                                            if (parser.eventType != XmlPullParser.START_TAG) {
                                                continue
                                            }
                                            if (parser.name == "vNF") {
                                                value = readValue(parser, "vNF")
                                            } else {
                                                skip(parser)
                                            }
                                        }
                                    } else {
                                        skip(parser)
                                    }
                                }
                            }
                            "pag" -> {
                                parser.require(XmlPullParser.START_TAG, null, "pag")
                                while (parser.next() != XmlPullParser.END_TAG) {
                                    if (parser.eventType != XmlPullParser.START_TAG) {
                                        continue
                                    }
                                    if (parser.name == "detPag") {
                                        parser.require(XmlPullParser.START_TAG, null, "detPag")
                                        while (parser.next() != XmlPullParser.END_TAG) {
                                            if (parser.eventType != XmlPullParser.START_TAG) {
                                                continue
                                            }
                                            if (parser.name == "vPag") {
                                                value = readValue(parser, "vPag")
                                            } else {
                                                skip(parser)
                                            }
                                        }
                                    } else {
                                        skip(parser)
                                    }
                                }
                            }
                            else -> skip(parser)
                        }
                    }
                }
                else -> skip(parser)
            }
        }
        return Task(0, noteNumber, value, address, city, deliveryStatus, date, clientName)
    }

    @Throws(IOException::class, XmlPullParserException::class)
    private fun readValue(parser: XmlPullParser, tag: String): String {
        parser.require(XmlPullParser.START_TAG, null, tag)
        val value = readText(parser)
        parser.require(XmlPullParser.END_TAG, null, tag)
        return value
    }

    @Throws(IOException::class, XmlPullParserException::class)
    private fun readText(parser: XmlPullParser): String {
        var result = ""
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.text
            parser.nextTag()
        }
        return result
    }

    @Throws(XmlPullParserException::class, IOException::class)
    private fun skip(parser: XmlPullParser) {
        if (parser.eventType != XmlPullParser.START_TAG) {
            throw IllegalStateException()
        }
        var depth = 1
        while (depth != 0) {
            when (parser.next()) {
                XmlPullParser.END_TAG -> depth--
                XmlPullParser.START_TAG -> depth++
            }
        }
    }
}
