package mou.demo_kotlin.model
import kotlinx.serialization.*
import kotlinx.serialization.json.*

@Serializable
data class P(val id:Int, val c:String, val t:String, val u:String, val i:String, val a:String)
