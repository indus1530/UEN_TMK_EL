package edu.aku.hassannaqvi.uen_tmk_el.ui.sections;


import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.validatorcrawler.aliazaz.Clear;
import com.validatorcrawler.aliazaz.Validator;

import org.json.JSONException;
import org.json.JSONObject;

import edu.aku.hassannaqvi.uen_tmk_el.R;
import edu.aku.hassannaqvi.uen_tmk_el.databinding.ActivitySectionFBinding;
import edu.aku.hassannaqvi.uen_tmk_el.ui.other.EndingActivity;
import edu.aku.hassannaqvi.uen_tmk_el.utils.AppUtilsKt;

public class SectionFActivity extends AppCompatActivity {

    ActivitySectionFBinding bi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bi = DataBindingUtil.setContentView(this, R.layout.activity_section_f);
        bi.setCallback(this);
        setupSkip();

    }

    private void setupSkip() {
        bi.raf402.setOnCheckedChangeListener((radioGroup, i) -> Clear.clearAllFields(bi.fldGrpCVraf5));
        bi.raf602.setOnCheckedChangeListener((radioGroup, i) -> Clear.clearAllFields(bi.ly2));
        bi.cmf802.setOnCheckedChangeListener((radioGroup, i) -> Clear.clearAllFields(bi.ly1));
        bi.cmf10.setOnCheckedChangeListener((radioGroup, i) -> Clear.clearAllFields(bi.ly3));
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
            startActivity(new Intent(this, EndingActivity.class).putExtra("complete", true));
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

        json.put("raf6", bi.raf601.isChecked() ? "1"
                : bi.raf602.isChecked() ? "2"
                : "-1");

        json.put("raf7", bi.raf7.getText().toString());

        json.put("rasno", bi.rasno.getText().toString());

        json.put("raa", bi.raa.getText().toString());

        json.put("rab01", bi.rab01.getText().toString());

        json.put("rab02", bi.rab02.getText().toString());

        json.put("rab03", bi.rab03.getText().toString());

        json.put("rac", "-1");

        json.put("rac01", "-1");

        json.put("rac02", "-1");

        json.put("rac03", "-1");

        json.put("rac04", "-1");

        json.put("rac05", "-1");

        json.put("rad01", bi.rad01.getText().toString());

        json.put("rad02", bi.rad02.getText().toString());

        json.put("rad03", bi.rad03.getText().toString());

        json.put("rae", bi.rae01.isChecked() ? "1"
                : bi.rae02.isChecked() ? "2"
                : bi.rae03.isChecked() ? "3"
                : bi.rae04.isChecked() ? "4"
                : bi.rae05.isChecked() ? "5"
                : bi.rae06.isChecked() ? "6"
                : bi.rae96.isChecked() ? "96"
                : "-1");

        json.put("rae96x", bi.rae96x.getText().toString());
        json.put("cmf8", bi.cmf801.isChecked() ? "1"
                : bi.cmf802.isChecked() ? "2"
                : "-1");

        json.put("cmf9", bi.cmf9.getText().toString());

        json.put("cmsr", bi.cmsr.getText().toString());

        json.put("cma", bi.cma.getText().toString());

        json.put("cmb", bi.cmb.getText().toString());

        json.put("cmc", "-1");

        json.put("cmc01", "-1");

        json.put("cmc02", "-1");

        json.put("cmd01", bi.cmd01.getText().toString());

        json.put("cmd02", bi.cmd02.getText().toString());

        json.put("cmd03", bi.cmd03.getText().toString());

        json.put("cme", "-1");

        json.put("cme01", "-1");

        json.put("cme02", "-1");

        json.put("cme03", "-1");

        json.put("cme04", "-1");

        json.put("cme05", "-1");

        json.put("cmf01", bi.cmf01.getText().toString());

        json.put("cmf02", bi.cmf02.getText().toString());

        json.put("cmf03", bi.cmf03.getText().toString());

        json.put("cmg", "-1");

        json.put("cmg01", "-1");

        json.put("cmg02", "-1");

        json.put("cmg03", "-1");

        json.put("cmg04", "-1");

        json.put("cmg05", "-1");

        json.put("cmg06", "-1");

        json.put("cmg07", "-1");

        json.put("cmg08", "-1");

        json.put("cmg96", bi.cmg96.getText().toString());

        json.put("cmf10", bi.cmf1001.isChecked() ? "1"
                : bi.cmf1002.isChecked() ? "2"
                : bi.cmf1003.isChecked() ? "666"
                : bi.cmf1098.isChecked() ? "98"
                : "-1");

        //     json.put("cmf1098x", bi.cmf1098x.getText().toString());
        json.put("cmf11", bi.cmf1101.isChecked() ? "1"
                : bi.cmf1102.isChecked() ? "2"
                : bi.cmf1103.isChecked() ? "3"
                : bi.cmf1104.isChecked() ? "4"
                : bi.cmf1196.isChecked() ? "96"
                : "-1");

        json.put("cmf1196x", bi.cmf1196x.getText().toString());
        json.put("cmf1201", bi.cmf1201.isChecked() ? "1" : "-1");

        json.put("cmf1202", bi.cmf1202.isChecked() ? "2" : "-1");

        json.put("cmf1203", bi.cmf1203.isChecked() ? "3" : "-1");

        json.put("cmf1204", bi.cmf1204.isChecked() ? "4" : "-1");

        json.put("cmf1205", bi.cmf1205.isChecked() ? "5" : "-1");

        json.put("cmf1206", bi.cmf1206.isChecked() ? "6" : "-1");


    }


    private boolean formValidation() {
        return Validator.emptyCheckingContainer(this, bi.GrpName);
    }


    public void BtnEnd() {
        AppUtilsKt.openEndActivity(this);
    }

}
