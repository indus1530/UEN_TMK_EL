package edu.aku.hassannaqvi.uen_tmk_el.ui.list_activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.leinardi.android.speeddial.SpeedDialActionItem
import com.leinardi.android.speeddial.SpeedDialView
import edu.aku.hassannaqvi.uen_tmk_el.CONSTANTS
import edu.aku.hassannaqvi.uen_tmk_el.CONSTANTS.Companion.SERIAL_EXTRA
import edu.aku.hassannaqvi.uen_tmk_el.R
import edu.aku.hassannaqvi.uen_tmk_el.adapter.FamilyMemberListAdapter
import edu.aku.hassannaqvi.uen_tmk_el.contracts.FamilyMembersContract
import edu.aku.hassannaqvi.uen_tmk_el.contracts.FormsContract
import edu.aku.hassannaqvi.uen_tmk_el.core.DatabaseHelper
import edu.aku.hassannaqvi.uen_tmk_el.core.MainApp
import edu.aku.hassannaqvi.uen_tmk_el.core.MainApp.*
import edu.aku.hassannaqvi.uen_tmk_el.databinding.ActivityFamilyMembersListBinding
import edu.aku.hassannaqvi.uen_tmk_el.ui.sections.SectionDActivity
import edu.aku.hassannaqvi.uen_tmk_el.ui.sections.SectionE01Activity
import edu.aku.hassannaqvi.uen_tmk_el.utils.JSONUtils
import edu.aku.hassannaqvi.uen_tmk_el.utils.KishGrid
import edu.aku.hassannaqvi.uen_tmk_el.utils.openEndActivity
import edu.aku.hassannaqvi.uen_tmk_el.viewmodel.MainVModel
import kotlinx.android.synthetic.main.activity_family_members_list.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONException
import org.json.JSONObject

class FamilyMembersListActivity : AppCompatActivity() {

    private var serial = 1
    private var memSelectedCounter = 0
    private lateinit var adapter: FamilyMemberListAdapter
    private lateinit var bi: ActivityFamilyMembersListBinding
    private var currentFM: FamilyMembersContract? = null
    private lateinit var clickLst: MutableList<FamilyMembersContract>
    private lateinit var db: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_family_members_list)
        setSupportActionBar(toolbar)

        bi = DataBindingUtil.setContentView(this, R.layout.activity_family_members_list)
        bi.callback = this
        db = appInfo.dbHelper

        settingValue()
        settingMenu()

        clickLst = mutableListOf()
    }

    private fun settingMenu() {

        val actionItems = mutableListOf<SpeedDialActionItem>()
        actionItems.add(SpeedDialActionItem.Builder(R.id.sd_main_fab, R.drawable.ic_add_person).setLabel("Add Member").create())
        actionItems.add(SpeedDialActionItem.Builder(R.id.arih1, R.drawable.ic_finish).setLabel("Next Section").create())
        actionItems.add(SpeedDialActionItem.Builder(R.id.sd_fab, R.drawable.ic_exit).setLabel("Force exit").create())
        bi.fabMenu.addAllActionItems(actionItems)
        bi.fabMenu.setOnActionSelectedListener(SpeedDialView.OnActionSelectedListener { actionItem ->
            when (actionItem.getLabel(this)) {
                "Add Member" -> {
                    startActivityForResult(Intent(this, SectionDActivity::class.java).putExtra(SERIAL_EXTRA, serial), CONSTANTS.MEMBER_ITEM)
                }
                "Next Section" -> {
                    if (memSelectedCounter == 0) return@OnActionSelectedListener false

                    if (memSelectedCounter != serial - 1) return@OnActionSelectedListener false

                    indexKishMWRA = null
                    indexKishMWRAChild = null

                    lifecycleScope.launch {
                        if (mainVModel.getAllUnder2().isNotEmpty() && !mainVModel.mwraChildU2Lst.value.isNullOrEmpty()) {
                            indexKishMWRA = withContext(Dispatchers.Main) {
                                mainVModel.mwraChildU2Lst.value?.get(
                                        kishSelectedMWRA(intent.getIntExtra("sno", 0),
                                                mainVModel.getAllUnder2().size) - 1)
                            }

                            val childLst = mainVModel.getAllU2ChildrenOfSelMWRA(indexKishMWRA.serialno.toInt())
                            indexKishMWRAChild = withContext(Dispatchers.Main) {
                                childLst?.let {
                                    childLst[kishSelectedMWRA(intent.getIntExtra("sno", 0),
                                            childLst.size) - 1]
                                }
                            }

                            updateKishMember(indexKishMWRA, 1)

                        }
                        updateCounters()
                        finish()
                        startActivity(Intent(this@FamilyMembersListActivity, SectionE01Activity::class.java))
                    }

                }
                "Force exit" -> {
                    openEndActivity(this)
                    return@OnActionSelectedListener true
                }
            }
            false
        })
        bi.toolbarLayout.title = "Household FamilyMembers"
        bi.toolbarLayout.setExpandedTitleTextAppearance(R.style.expandCollapse)
    }

    private fun settingValue() {
        mainVModel = this.run {
            ViewModelProvider(this)[MainVModel::class.java]
        }
        mainVModel.childLstU5.observe(this, { item -> bi.contentScroll.under5.text = String.format("%02d", item.size) })
        mainVModel.mwraLst.observe(this, { item -> bi.contentScroll.mwra.text = String.format("%02d", item.size) })
        mainVModel.familyMemLst.observe(this, { item ->
            bi.contentScroll.total.text = String.format("%02d", item.size)
            adapter.setMList(item)
        })
        setupRecyclerView(mutableListOf())
    }

    private fun setupRecyclerView(membersLst: MutableList<FamilyMembersContract>) {
        adapter = FamilyMemberListAdapter(this, membersLst, mainVModel)
        bi.contentScroll.recyclerView.layoutManager = LinearLayoutManager(this)
        bi.contentScroll.recyclerView.adapter = adapter
        adapter.setItemClicked { item, position ->
            openDialog(this, item)
            setItemClick {
                currentFM = item
                startActivityForResult(Intent(this, SectionDActivity::class.java)
                        .putExtra(SERIAL_EXTRA, item.serialno.toInt()), CONSTANTS.MEMBER_ITEM)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == CONSTANTS.MEMBER_ITEM) {
            when (resultCode) {
                Activity.RESULT_OK -> {
                    data?.let { serial = data.getIntExtra(SERIAL_EXTRA, 0) } ?: handlingHolder()
                }
                Activity.RESULT_CANCELED -> {
                }
            }
        }
    }

    private fun handlingHolder() {
        memSelectedCounter++
        currentFM?.let {
            mainVModel.setCheckedItemValues(currentFM!!.serialno.toInt())
        }

    }

    private fun kishSelectedMWRA(sno: Int, size: Int): Int {
        return KishGrid.kishGridProcess(sno, size)
    }

    companion object {
        lateinit var mainVModel: MainVModel
        var genderFlag: String = "0"
    }

    override fun onBackPressed() {
        Toast.makeText(this, "You Can't go back", Toast.LENGTH_LONG).show()
    }

    private suspend fun updateKishMember(fmc: FamilyMembersContract, int: Int) =
            withContext(Dispatchers.IO) {
                db.updatesFamilyMemberColumn(FamilyMembersContract.MemberTable.COLUMN_KISH_SELECTED, int.toString(), fmc)
            }

    private suspend fun updateCounters() =
            withContext(Dispatchers.IO) {
                try {
                    val json = JSONObject()
                    json.put("d14", bi.contentScroll.total.text.toString())
                    json.put("d15", bi.contentScroll.mwra.text.toString())
                    json.put("d16", bi.contentScroll.under5.text.toString())
                    val json_merge = JSONUtils.mergeJSONObjects(JSONObject(form.getsC()), json)
                    form.setsC(json_merge.toString())
                } catch (e: JSONException) {
                    e.printStackTrace()
                }

                db.updatesFormColumn(FormsContract.FormsTable.COLUMN_SC, MainApp.form.getsC())
            }


}
