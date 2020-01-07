package mou.demo_kotlin

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.BottomSheetDialog
import android.support.design.widget.CoordinatorLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.*
import android.widget.LinearLayout
import android.widget.Toast
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.bottom_sheet.view.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.list_item.view.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import kotlinx.serialization.list
import mou.demo_kotlin.model.P
import mou.util.http_get
import mou.util.http_post
import net.steamcrafted.materialiconlib.MaterialMenuInflater
import org.json.JSONObject

class MainActivity : AppCompatActivity() {
    companion object {
        const val SZ_URL = "https://crt8b3p0n9.execute-api.ap-southeast-1.amazonaws.com/prod"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        rv_movie.layoutManager = LinearLayoutManager(this)

        http_get(sz_url = SZ_URL) {
            // parsing data back
            val obj = Json(JsonConfiguration.Stable).parse(P.serializer().list, it)
            runOnUiThread({
                rv_movie.adapter = Adapter_A(ArrayList(obj), this@MainActivity)
            })
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        MaterialMenuInflater
            .with(this)
            .setDefaultColor(Color.WHITE)
            .inflate(R.menu.menu_main, menu);
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> {

                val view = layoutInflater.inflate(R.layout.bottom_sheet, null)
                val dialog = BottomSheetDialog(this)
                view.btn_post.setOnClickListener(object : View.OnClickListener {
                    override fun onClick(v: View?) {
                        val json = JSONObject()
                        json.put("c", view.et_c.text)
                        json.put("t", view.et_t.text)
                        json.put("u", view.et_u.text)
                        json.put("i", view.et_i.text)
                        json.put("a", view.et_a.text)
                        http_post(SZ_URL, json.toString()) {
                            val obj = Json(JsonConfiguration.Stable).parse(P.serializer().list, it)
                            runOnUiThread({
                                rv_movie.adapter = Adapter_A(ArrayList(obj), this@MainActivity)
                                dialog.hide()
                                Toast.makeText(applicationContext, "Posted.", Toast.LENGTH_SHORT)
                                    .show()
                            })
                        }
                    }
                })
                dialog.setContentView(view)
                dialog.show()
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}

//------------------------------------------------------
//  RecyclerView UI Classes BEGAN
class Adapter_A(val items: ArrayList<P>, val context: Context) :
    RecyclerView.Adapter<ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.list_item, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) { // set value on update
        holder.tv_u?.text = items.get(position).u
        holder.tv_t?.text = items.get(position).t
        holder.tv_c?.text = items.get(position).c
        Picasso.get().load(items.get(position).i).into(holder.iv_i);
        Picasso.get().load(items.get(position).a).into(holder.iv_a);
    }

    override fun getItemCount(): Int { // return count of the list-item
        return items.size
    }
}

class ViewHolder(view: View) : RecyclerView.ViewHolder(view) { // Hold the TextView(s)
    val iv_a = view.iv_a
    val tv_u = view.tv_u
    val tv_t = view.tv_t
    val tv_c = view.tv_c
    val iv_i = view.iv_i
}
//  RecyclerView UI Classes ENDED
//------------------------------------------------------