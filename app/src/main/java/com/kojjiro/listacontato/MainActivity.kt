package com.kojjiro.listacontato

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.content.edit
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.kojjiro.listacontato.ContactDetail.Companion.EXTRA_CONTACT
import java.util.zip.Inflater

class MainActivity : AppCompatActivity(),ClickItemContatcListener {
    private val rvList: RecyclerView by lazy {
        findViewById<RecyclerView>(R.id.rv_list)
    }
    private val adapter = ContactAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.drawer_menu)

        initDrawer()
        fetchListContatc()
        bindViews()
        //updateList()
    }

    private fun fetchListContatc(){
        val list = arrayOf(
            Contact(
                "Guilherme Medeiros",
                "(31)988073837",
                "img.png"
            ),
            Contact(
                "Ellen Carla Deiro",
                "(31)987654321",
                "img.png"
            )
        )
        getInstanceSharedPreferences().edit {
            val json =Gson().toJson(list)
            putString("contacts", json)
            commit()
        }
    }

    private fun getInstanceSharedPreferences(): SharedPreferences{
        return getSharedPreferences("com.kojjiro.listacontato.PREFERENCES",Context.MODE_PRIVATE)
    }

    private fun initDrawer(){
        val drawerLayout=findViewById<View>(R.id.drawer_layout) as DrawerLayout
        val toolbar=findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        val toggle=ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_drawer, R.string.close_drawer)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
    }

    private fun bindViews(){
        rvList.adapter=adapter
        rvList.layoutManager=LinearLayoutManager(this)
    }

    private fun getListContacts(): List<Contact>{
        val list=getInstanceSharedPreferences().getString("contacts","[]")
        val turnsType=object :TypeToken<List<Contact>>(){}.type
        return Gson().fromJson(list, turnsType)
    }

    private fun updateList(){
        val list=getListContacts()
        adapter.updateList(list)
    }

    private fun showToast(message:String){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater= menuInflater
        inflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId){
            R.id.item_menu_1 -> {
                showToast("Exibindo Item menu 1")
                true
            }
            R.id.item_menu_2 -> {
                showToast("Exibindo Item menu 2")
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun clickItemContatc(contact: Contact) {
        val intent=Intent(this,ContactDetail::class.java)
        intent.putExtra(EXTRA_CONTACT, contact)
        startActivity(intent)
    }

}