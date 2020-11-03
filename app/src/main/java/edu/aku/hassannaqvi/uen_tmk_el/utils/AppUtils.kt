package edu.aku.hassannaqvi.uen_tmk_el.utils

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import edu.aku.hassannaqvi.uen_tmk_el.R
import edu.aku.hassannaqvi.uen_tmk_el.databinding.ChildEndDialogBinding
import edu.aku.hassannaqvi.uen_tmk_el.databinding.GeneralEndDialogBinding
import edu.aku.hassannaqvi.uen_tmk_el.ui.other.EndingActivity
import java.util.*


private fun checkPermission(context: Context): IntArray {
    return intArrayOf(ContextCompat.checkSelfPermission(context,
            Manifest.permission.READ_CONTACTS), ContextCompat.checkSelfPermission(context,
            Manifest.permission.GET_ACCOUNTS), ContextCompat.checkSelfPermission(context,
            Manifest.permission.READ_PHONE_STATE), ContextCompat.checkSelfPermission(context,
            Manifest.permission.ACCESS_FINE_LOCATION), ContextCompat.checkSelfPermission(context,
            Manifest.permission.ACCESS_COARSE_LOCATION), ContextCompat.checkSelfPermission(context,
            Manifest.permission.WRITE_EXTERNAL_STORAGE), ContextCompat.checkSelfPermission(context,
            Manifest.permission.CAMERA))
}

fun getPermissionsList(context: Context): List<String> {
    val permissions = arrayOf(Manifest.permission.READ_CONTACTS, Manifest.permission.GET_ACCOUNTS,
            Manifest.permission.READ_PHONE_STATE, Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA)
    val listPermissionsNeeded: MutableList<String> = ArrayList()
    for (i in checkPermission(context).indices) {
        if (checkPermission(context)[i] != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(permissions[i])
        }
    }
    return listPermissionsNeeded
}

fun openEndActivity(activity: Activity) {
    val dialog = Dialog(activity)
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
    dialog.setContentView(R.layout.general_end_dialog)
    dialog.setCancelable(false)
    val params = WindowManager.LayoutParams()
    params.copyFrom(dialog.window!!.attributes)
    params.width = WindowManager.LayoutParams.WRAP_CONTENT
    params.height = WindowManager.LayoutParams.WRAP_CONTENT
    dialog.show()
    dialog.window!!.attributes = params
    dialog.findViewById<View>(R.id.btnOk).setOnClickListener { view: View? ->
        activity.finish()
        activity.startActivity(Intent(activity, EndingActivity::class.java).putExtra("complete", false))
//                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
    }
    dialog.findViewById<View>(R.id.btnNo).setOnClickListener { view: View? -> dialog.dismiss() }
}

fun showDialogActivity(activity: Activity, message: String) {
    val dialog = Dialog(activity)
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
    val bi: GeneralEndDialogBinding = DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.general_end_dialog, null, false)
    dialog.setContentView(bi.root)
    bi.content.text = message
    dialog.setCancelable(false)
    val params = WindowManager.LayoutParams()
    params.copyFrom(dialog.window!!.attributes)
    params.width = WindowManager.LayoutParams.WRAP_CONTENT
    params.height = WindowManager.LayoutParams.WRAP_CONTENT
    dialog.window!!.attributes = params
    dialog.show()
    bi.btnOk.visibility = View.GONE
    bi.btnNo.setOnClickListener {
        dialog.dismiss()
    }
}

fun showDialogActivity(activity: Activity, message: String, defaultFlag: Boolean = true) {
    val dialog = Dialog(activity)
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
    val bi: GeneralEndDialogBinding = DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.general_end_dialog, null, false)
    dialog.setContentView(bi.root)
    bi.content.text = message
    dialog.setCancelable(false)
    val params = WindowManager.LayoutParams()
    params.copyFrom(dialog.window!!.attributes)
    params.width = WindowManager.LayoutParams.WRAP_CONTENT
    params.height = WindowManager.LayoutParams.WRAP_CONTENT
    dialog.window!!.attributes = params
    dialog.show()
    bi.btnOk.setOnClickListener {
        dialog.dismiss()
        val endSecAActivity = activity as EndSectionActivity
        endSecAActivity.endSecActivity(defaultFlag)
    }
    bi.btnNo.visibility = View.GONE
}

@JvmOverloads
fun openWarningActivity(activity: Activity, message: String, defaultFlag: Boolean = true) {
    val dialog = Dialog(activity)
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
    val bi: ChildEndDialogBinding = DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.child_end_dialog, null, false)
    dialog.setContentView(bi.root)
    bi.content.text = message
    dialog.setCancelable(false)
    val params = WindowManager.LayoutParams()
    params.copyFrom(dialog.window!!.attributes)
    params.width = WindowManager.LayoutParams.WRAP_CONTENT
    params.height = WindowManager.LayoutParams.WRAP_CONTENT
    dialog.window!!.attributes = params
    dialog.show()
    bi.btnOk.setOnClickListener {
        val endSecAActivity = activity as EndSectionActivity
        endSecAActivity.endSecActivity(defaultFlag)
    }
    bi.btnNo.setOnClickListener {
        dialog.dismiss()
    }
}

@JvmOverloads
fun contextEndActivity(activity: Activity, defaultFlag: Boolean = true) {
    val dialog = Dialog(activity)
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
    dialog.setContentView(R.layout.general_end_dialog)
    dialog.setCancelable(false)
    val params = WindowManager.LayoutParams()
    params.copyFrom(dialog.window!!.attributes)
    params.width = WindowManager.LayoutParams.WRAP_CONTENT
    params.height = WindowManager.LayoutParams.WRAP_CONTENT
    dialog.show()
    dialog.window!!.attributes = params
    val endSecAActivity = activity as EndSectionActivity
    dialog.findViewById<View>(R.id.btnOk).setOnClickListener { endSecAActivity.endSecActivity(defaultFlag) }
    dialog.findViewById<View>(R.id.btnNo).setOnClickListener { dialog.dismiss() }
}

fun contextBackActivity(activity: Activity) {
    val dialog = Dialog(activity)
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
    dialog.setContentView(R.layout.back_dialog)
    dialog.setCancelable(false)
    val params = WindowManager.LayoutParams()
    params.copyFrom(dialog.window!!.attributes)
    params.width = WindowManager.LayoutParams.WRAP_CONTENT
    params.height = WindowManager.LayoutParams.WRAP_CONTENT
    dialog.show()
    dialog.window!!.attributes = params
    dialog.findViewById<View>(R.id.btnOk).setOnClickListener {
        activity.finish()
    }
    dialog.findViewById<View>(R.id.btnNo).setOnClickListener { dialog.dismiss() }
}

@JvmOverloads
fun openWarningActivity(activity: Activity, title: String, message: String, btnYesTxt: String = "YES", btnNoTxt: String = "NO") {
    val dialog = Dialog(activity)
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
    val bi: ChildEndDialogBinding = DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.child_end_dialog, null, false)
    dialog.setContentView(bi.root)
    bi.alertTitle.text = title
    bi.alertTitle.setTextColor(ContextCompat.getColor(activity, R.color.green))
    bi.content.text = message
    bi.btnOk.text = btnYesTxt
    bi.btnOk.setBackgroundColor(ContextCompat.getColor(activity, R.color.green))
    bi.btnNo.text = btnNoTxt
    bi.btnNo.setBackgroundColor(ContextCompat.getColor(activity, R.color.gray))
    dialog.setCancelable(false)
    val params = WindowManager.LayoutParams()
    params.copyFrom(dialog.window!!.attributes)
    params.width = WindowManager.LayoutParams.WRAP_CONTENT
    params.height = WindowManager.LayoutParams.WRAP_CONTENT
    dialog.window!!.attributes = params
    dialog.show()
    bi.btnOk.setOnClickListener {
        val warningActivity = activity as WarningActivityInterface
        warningActivity.callWarningActivity()
    }
    bi.btnNo.setOnClickListener {
        dialog.dismiss()
    }
}

interface EndSectionActivity {
    fun endSecActivity(flag: Boolean)
}

interface WarningActivityInterface {
    fun callWarningActivity()
}

fun showTooltip(context: Context, view: View) {
    if (view.id != View.NO_ID) {
        val package_name: String = context.applicationContext.packageName
        // Question Number Textview ID must be prefixed with q_ e.g.: 'q_aa12a'
        val infoid = view.resources.getResourceName(view.id).replace("$package_name:id/q_", "")
        // Question info text must be suffixed with _info e.g.: aa12a_info
        val stringRes: Int = context.resources.getIdentifier(infoid + "_info", "string", package_name)
        // Fetch info text from strings.xml
        //String infoText = (String) getResources().getText(stringRes);
        // Check if string resource exists to avoid crash on missing info string
        if (stringRes != 0) {
            // Fetch info text from strings.xml
            val infoText = context.resources.getText(stringRes) as String
            AlertDialog.Builder(context)
                    .setTitle("Info: " + infoid.toUpperCase(Locale.ROOT))
                    .setMessage(infoText)
                    .setIcon(android.R.drawable.ic_dialog_info)
                    .show()
        } else {
            Toast.makeText(context, "No information available on this question.", Toast.LENGTH_SHORT).show()
        }
    } else {
        Toast.makeText(context, "No ID Associated with this question.", Toast.LENGTH_SHORT).show()
    }
}

fun getMemberIcon(gender: Int, age: String): Int? {
    return when (age.toInt()) {
        -1 -> R.drawable.mem_icon
        in 1..10 -> when (gender) {
            1 -> R.drawable.ctr_childboy
            2 -> R.drawable.ctr_childgirl
            else -> R.drawable.mem_icon
        }
        else -> when (gender) {
            1 -> R.drawable.ctr_male
            2 -> R.drawable.ctr_female
            else -> R.drawable.mem_icon
        }
    }
}
