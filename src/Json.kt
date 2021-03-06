import com.google.gson.*
import java.io.*
import com.google.gson.JsonObject

import com.google.gson.JsonElement
import com.google.gson.JsonArray
import com.google.gson.JsonParser
import java.io.FileInputStream
import org.python.bouncycastle.asn1.x500.style.RFC4519Style.title
import org.python.bouncycastle.asn1.x500.style.RFC4519Style.title
import org.python.bouncycastle.asn1.x500.style.RFC4519Style.title
import org.python.bouncycastle.asn1.x500.style.RFC4519Style.title
import java.io.OutputStreamWriter
import java.io.FileOutputStream
import java.io.File
import org.python.bouncycastle.asn1.x500.style.RFC4519Style.title
import org.python.bouncycastle.asn1.x500.style.RFC4519Style.title
import java.io.IOException
import java.io.FileNotFoundException
class Json{
    val PLUGINSJSONPATH =System.getProperty("user.dir")+"\\Setting.json"
    fun getType(title:String): String? {
        val JsonFile = FileInputStream(PLUGINSJSONPATH)
        val JsonReadCoding = InputStreamReader(JsonFile,"UTF-8")
        val Parser = JsonParser()
        val Object = Parser.parse(BufferedReader(JsonReadCoding)) as JsonObject
        var type:String?? = null
        val Plugins = Object.getAsJsonArray("Plugins")
        Plugins
                .asSequence()
                .map { it.asJsonObject }
                .filter { it.get("title").asString.equals(title, ignoreCase=true) }
                .forEach { type=it.get("type").asString }
        return type
    }
    fun getDialog(title:String):String?{
        val JsonFile = FileInputStream(PLUGINSJSONPATH)
        val JsonReadCoding = InputStreamReader(JsonFile,"UTF-8")
        val Parser = JsonParser()
        val Object = Parser.parse(BufferedReader(JsonReadCoding)) as JsonObject
        var Dialog:String?? = null
        val Plugins = Object.getAsJsonArray("Plugins")
        Plugins
                .asSequence()
                .map { it.asJsonObject }
                .filter { it.get("title").asString.equals(title, ignoreCase=true) }
                .forEach { Dialog=it.get("dialog").asString }
        return Dialog
    }
    fun isDialog(title: String):Boolean{
        val JsonFile = FileInputStream(PLUGINSJSONPATH)
        val JsonReadCoding = InputStreamReader(JsonFile,"UTF-8")
        val Parser = JsonParser()
        val Object = Parser.parse(BufferedReader(JsonReadCoding)) as JsonObject
        val Plugins = Object.getAsJsonArray("Plugins")
        return Plugins
                .asSequence()
                .map { it.asJsonObject }
                .any { it.get("title").asString.equals(title, ignoreCase=true) && !it.get("dialog").isJsonNull }
    }
    fun getAuthor(title:String):String?{
        val JsonFile = FileInputStream(PLUGINSJSONPATH)
        val JsonReadCoding = InputStreamReader(JsonFile,"UTF-8")
        val Parser = JsonParser()
        val Object = Parser.parse(BufferedReader(JsonReadCoding)) as JsonObject
        val Plugins = Object.getAsJsonArray("Plugins")
        var author:String? = null
        Plugins
                .asSequence()
                .map { it.asJsonObject }
                .filter { it.get("title").asString.equals(title, ignoreCase=true) }
                .forEach { author=it.get("author").asString }
        return author
    }
    fun getDetail(title:String):String?{
        val jsonfile=FileInputStream(PLUGINSJSONPATH)
        val jsonreadcoding=InputStreamReader(jsonfile, "UTF-8")
        val parser=JsonParser()
        val Object=parser.parse(BufferedReader(jsonreadcoding)) as JsonObject
        var detailStr: String?=null
        val Plugins=Object.getAsJsonArray("Plugins")
        Plugins
                .asSequence()
                .map { it.asJsonObject }
                .filter { it.get("title").asString.equals(title, ignoreCase=true) }
                .forEach {
                    detailStr=if(!it.get("dialog").isJsonNull) {
                        ("Title:"+it.get("title").asString+"\n\n"
                                +"Author："+it.get("author").asString+"\n\n"
                                +"Type："+it.get("type").asString+"\n\n"
                                +"Detail："+it.get("detail").asString+"\n\n"
                                +"Dialog："+it.get("dialog").asString+"\n\n"
                                +"Path："+it.get("path").asString)
                    } else {
                        ("Title："+it.get("title").asString+"\n\n"
                                +"Author："+it.get("author").asString+"\n\n"
                                +"Type："+it.get("type").asString+"\n\n"
                                +"Detail："+it.get("detail").asString+"\n\n"
                                +"Path："+it.get("path").asString)
                    }
                }
        return detailStr
    }
    fun createJSON(path:String):String{
        lateinit var title: String
        var type: String?=null
        var author: String?=null
        var detail: String?=null
        var dialog: String?=null
        try {
            val pypath=FileInputStream(path)
            val readpy=InputStreamReader(pypath, "UTF-8")
            val pyPluginread=BufferedReader(readpy)
            var lineText: String?=null
            while (true){
                lineText=pyPluginread.readLine()
                if(lineText!=null){
                    if(lineText!!.toLowerCase().contains("title:")) {
                        title=lineText.substring(lineText.toLowerCase().indexOf("title:")+6, lineText.length)
                    } else if(lineText.toLowerCase().contains("type:")) {
                        type=lineText.substring(lineText.toLowerCase().indexOf("type:")+5, lineText.length)
                    } else if(lineText.toLowerCase().contains("author:")) {
                        author=lineText.substring(lineText.toLowerCase().indexOf("author:")+7, lineText.length)
                    } else if(lineText.toLowerCase().contains("detail:")) {
                        detail=lineText.substring(lineText.toLowerCase().indexOf("detail:")+7, lineText.length)
                    } else if(lineText.toLowerCase().contains("dialog:")) {
                        dialog=lineText.substring(lineText.toLowerCase().indexOf("dialog:")+7, lineText.length)
                    } else if(lineText.contains("}")) {
                        break
                    }
                }
            }
        } catch (e1: Exception) {
            e1.printStackTrace()
        }

        val jsonfile=FileInputStream(path)
        var Object: JsonObject?=null
        var Plugins: JsonArray?=null
        if(File(PLUGINSJSONPATH).isFile() && File(PLUGINSJSONPATH).exists()) {
            val jsonreadcoding=InputStreamReader(jsonfile, "UTF-8")
            val jsonread=BufferedReader(jsonreadcoding)
            var jsonText: String?=jsonread.readLine()
            if(jsonText!=null) {
                //爬一下原有的json数据 以免被重写
                val parser=JsonParser()
                Object=parser.parse(InputStreamReader(FileInputStream(PLUGINSJSONPATH), "UTF-8")) as JsonObject
                Plugins=Object.getAsJsonArray("Plugins")
            } else {
                Object=JsonObject()
                Plugins=JsonArray()
            }
            jsonreadcoding.close()
        } else {
            File(PLUGINSJSONPATH).createNewFile()
            Object=JsonObject()
            Plugins=JsonArray()
        }
        val Plugin=JsonObject()
        val tool=JsonFormatTool()
        Plugin.addProperty("title", title)
        Plugin.addProperty("type", type)
        Plugin.addProperty("author", author)
        Plugin.addProperty("detail", detail)
        Plugin.addProperty("dialog", dialog)
        Plugin.addProperty("path", path)
        Plugins!!.add(Plugin)               // 将json对象添加到数组
        Object.add("Plugins", Plugins)   // 将数组添加到json对象
        val jsonStr=Object.toString()   // 将json对象转化成json字符串
        val outfile=FileOutputStream(PLUGINSJSONPATH)
        val outprint=OutputStreamWriter(outfile, "UTF-8")
        outprint.write(tool.formatJson(jsonStr))
        outprint.flush()
        outprint.close()
        return title
    }
    fun rmPlugin(title:String){
        val jsonfile=FileInputStream(PLUGINSJSONPATH)
        var Object: JsonObject?=null
        var Plugins: JsonArray?=null
        if(File(PLUGINSJSONPATH).isFile() && File(PLUGINSJSONPATH).exists()) {
            val jsonreadcoding=InputStreamReader(jsonfile, "UTF-8")
            val jsonread=BufferedReader(jsonreadcoding)
            var jsonText: String?=jsonread.readLine()
            if(jsonText!=null) {
                //爬一下原有的json数据 以免被重写
                val parser=JsonParser()
                Object=parser.parse(InputStreamReader(FileInputStream(PLUGINSJSONPATH), "UTF-8")) as JsonObject
                Plugins=Object.getAsJsonArray("Plugins")
            }
            jsonreadcoding.close()
        }
        //
        for (jsonElement in Plugins!!) {
            val Plugin=jsonElement.asJsonObject
            if(Plugin.get("title").asString.equals(title, ignoreCase=true)) {
                Plugins.remove(Plugin)
                break
            }
        }
        val tool=JsonFormatTool()
        Object!!.add("Plugins", Plugins)   // 将数组添加到json对象
        val jsonStr=Object.toString()   // 将json对象转化成json字符串
        val outfile=FileOutputStream(PLUGINSJSONPATH)
        val outprint=OutputStreamWriter(outfile, "UTF-8")
        outprint.write(tool.formatJson(jsonStr))
        outprint.flush()
        outprint.close()
    }
    fun getPath(title:String):String?{
        var path: String?=null
        val jsonfile=FileInputStream(PLUGINSJSONPATH)
        val jsonreadcoding=InputStreamReader(jsonfile, "UTF-8")
        val parser=JsonParser()
        val Object=parser.parse(BufferedReader(jsonreadcoding)) as JsonObject
        val Plugins=Object.getAsJsonArray("Plugins")
        Plugins
                .asSequence()
                .map { it.asJsonObject }
                .filter { it.get("title").asString.equals(title, ignoreCase=true) }
                .forEach { path=it.get("path").asString }
        return path
    }
    fun isJSON():Boolean{
        val jsonfile=File(PLUGINSJSONPATH)
        var readjson: InputStreamReader?=null
        try {
            readjson=InputStreamReader(FileInputStream(jsonfile))
        } catch (e1: FileNotFoundException) {
            // TODO 自动生成的 catch 块
            e1.printStackTrace()
        }

        val jsonread=BufferedReader(readjson)
        val jsonText: String?=null
        var isjson=true
        try {
            isjson=jsonread.readLine()!=null
        } catch (e: IOException) {
            // TODO 自动生成的 catch 块
            e.printStackTrace()
        }

        return isjson
    }
}