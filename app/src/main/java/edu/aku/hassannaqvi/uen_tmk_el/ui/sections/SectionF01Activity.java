package edu.aku.hassannaqvi.uen_tmk_el.ui.sections;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.validatorcrawler.aliazaz.Validator;

import org.json.JSONException;
import org.json.JSONObject;

import edu.aku.hassannaqvi.uen_tmk_el.R;
import edu.aku.hassannaqvi.uen_tmk_el.core.MainApp;
import edu.aku.hassannaqvi.uen_tmk_el.databinding.ActivitySectionF01Binding;
import edu.aku.hassannaqvi.uen_tmk_el.ui.other.MainActivity;
import edu.aku.hassannaqvi.uen_tmk_el.utils.AppUtilsKt;

public class SectionF01Activity extends AppCompatActivity {

    ActivitySectionF01Binding bi;
    int raf = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bi = DataBindingUtil.setContentView(this, R.layout.activity_section_f01);
        bi.setCallback(this);
        setupSkip();
    }


    private void setupSkip() {
    }


    public void BtnContinue() {
        if (!formValidation()) return;
        try {
            SaveDraft();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (UpdateDB()) {
            finish();
            startActivity(new Intent(this, MainActivity.class).putExtra("complete", true));
        } else {
            Toast.makeText(this, "Sorry. You can't go further.\n Please contact IT Team (Failed to update DB)", Toast.LENGTH_SHORT).show();
        }
    }


    private boolean UpdateDB() {

        /*DatabaseHelper db = MainApp.appInfo.getDbHelper();
        long updcount = db.addForm(form);
        form.set_ID(String.valueOf(updcount));
        if (updcount > 0) {
            form.set_UID(form.getDeviceID() + form.get_ID());
            db.updatesFormColumn(FormsContract.FormsTable.COLUMN_UID, form.get_UID());
            return true;
        } else {
            Toast.makeText(this, "Sorry. You can't go further.\n Please contact IT Team (Failed to update DB)", Toast.LENGTH_SHORT).show();
            return false;
        }*/
        return true;
    }


    private void SaveDraft() throws JSONException {

        JSONObject json = new JSONObject();

        json.put("f1a", bi.f1a.getText().toString());
        json.put("f1b", bi.f1b.getText().toString());

        json.put("raf1", bi.raf101.isChecked() ? "1"
                : bi.raf102.isChecked() ? "2"
                : "-1");

        json.put("raf2", bi.raf2.getText().toString());

        json.put("raf301", bi.raf301.getText().toString());
        json.put("raf302", bi.raf302.getText().toString());
        json.put("raf303", bi.raf303.getText().toString());
        json.put("raf304", bi.raf304.getText().toString());

        json.put("raf4", bi.raf401.isChecked() ? "1"
                : bi.raf402.isChecked() ? "2"
                : bi.raf498.isChecked() ? "98"
                : "-1");

        json.put("raf5", bi.raf5.getText().toString());

        MainApp.form.setsF(json.toString());

    }


    private boolean formValidation() {

        if (!bi.raf301.getText().toString().trim().isEmpty()
                && !bi.raf302.getText().toString().trim().isEmpty()
                && !bi.raf303.getText().toString().trim().isEmpty()
                && !bi.raf304.getText().toString().trim().isEmpty()) {

            if ((Integer.parseInt(bi.raf301.getText().toString())
                    + Integer.parseInt(bi.raf302.getText().toString())
                    + Integer.parseInt(bi.raf303.getText().toString())
                    + Integer.parseInt(bi.raf304.getText().toString())) > 15) {
                Toast.makeText(this, "Question RAF3 \nAll Pregnancies Can't be greater than 15!", Toast.LENGTH_SHORT).show();
                return false;
            }
        }

        return Validator.emptyCheckingContainer(this, bi.GrpName);
    }


    public void BtnEnd() {
        AppUtilsKt.openEndActivity(this);
    }


    public void showTooltipView(View view) {
        AppUtilsKt.showTooltip(this, view);
    }

}
