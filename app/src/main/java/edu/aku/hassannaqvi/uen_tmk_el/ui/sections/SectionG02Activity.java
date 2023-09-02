package edu.aku.hassannaqvi.uen_tmk_el.ui.sections;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.validatorcrawler.aliazaz.Clear;
import com.validatorcrawler.aliazaz.Validator;

import org.json.JSONException;
import org.json.JSONObject;

import edu.aku.hassannaqvi.uen_tmk_el.R;
import edu.aku.hassannaqvi.uen_tmk_el.contracts.FormsContract;
import edu.aku.hassannaqvi.uen_tmk_el.core.DatabaseHelper;
import edu.aku.hassannaqvi.uen_tmk_el.core.MainApp;
import edu.aku.hassannaqvi.uen_tmk_el.databinding.ActivitySectionG02Binding;
import edu.aku.hassannaqvi.uen_tmk_el.utils.AppUtilsKt;
import edu.aku.hassannaqvi.uen_tmk_el.utils.JSONUtils;

public class SectionG02Activity extends AppCompatActivity {

    ActivitySectionG02Binding bi;
    public static boolean chg101, chg106, chg9, chg10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bi = DataBindingUtil.setContentView(this, R.layout.activity_section_g02);
        bi.setCallback(this);

        setupSkip();
    }

    private void setupSkip() {

        chg101 = getIntent().getBooleanExtra("chg1", true);
        chg106 = getIntent().getBooleanExtra("chg6", true);
        chg9 = getIntent().getBooleanExtra("chg9", true);
        chg10 = getIntent().getBooleanExtra("chg10", true);

        if (chg101) {
            bi.fldGrpSecG07.setVisibility(View.GONE);
            Clear.clearAllFields(bi.fldGrpSecG07);
        } else
            bi.fldGrpSecG07.setVisibility(View.VISIBLE);

        if (chg106) {
            bi.fldGrpSecG07.setVisibility(View.GONE);
            Clear.clearAllFields(bi.fldGrpSecG07);
        } else
            bi.fldGrpSecG07.setVisibility(View.VISIBLE);

        if (chg9 && chg10) {
            bi.fldGrpSecG09.setVisibility(View.GONE);
            Clear.clearAllFields(bi.fldGrpSecG09);
        } else
            bi.fldGrpSecG09.setVisibility(View.VISIBLE);

        bi.chg27.setOnCheckedChangeListener((radioGroup, i) -> Clear.clearAllFields(bi.fldGrpCVchg29));


        bi.chg32.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == bi.chg3202.getId()) {
                Clear.clearAllFields(bi.fldGrpSecG06);
                bi.fldGrpSecG06.setVisibility(View.GONE);
            } else {
                bi.fldGrpSecG06.setVisibility(View.VISIBLE);
            }
        });

        bi.chg33.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == bi.chg3302.getId()) {
                Clear.clearAllFields(bi.fldGrpCVchg34);
                bi.fldGrpCVchg34.setVisibility(View.GONE);
            } else {
                bi.fldGrpCVchg34.setVisibility(View.VISIBLE);
            }
        });

    }


    public void BtnContinue() {
        if (!formValidation()) return;
        try {
            SaveDraft();
            if (UpdateDB()) {
                finish();
                startActivity(new Intent(this, SectionH01Activity.class));
            } else {
                Toast.makeText(this, "Sorry. You can't go further.\n Please contact IT Team (Failed to update DB)", Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private boolean UpdateDB() {
        DatabaseHelper db = MainApp.appInfo.getDbHelper();
        int updcount = db.updatesFormColumn(FormsContract.FormsTable.COLUMN_SG, MainApp.form.getsG());
        return updcount == 1;
    }


    private void SaveDraft() throws JSONException {

        JSONObject json = new JSONObject();

        json.put("chg26", bi.chg2601.isChecked() ? "1"
                : bi.chg2602.isChecked() ? "2"
                : "-1");
        json.put("chg2601x", bi.chg2601x.getText().toString());

        json.put("chg27", bi.chg2701.isChecked() ? "1"
                : bi.chg2702.isChecked() ? "2"
                : bi.chg2703.isChecked() ? "3"
                : bi.chg2704.isChecked() ? "4"
                : bi.chg2705.isChecked() ? "5"
                : bi.chg2706.isChecked() ? "6"
                : bi.chg2707.isChecked() ? "7"
                : bi.chg2708.isChecked() ? "8"
                : bi.chg2709.isChecked() ? "9"
                : bi.chg2796.isChecked() ? "96"
                : "-1");
        json.put("chg2796x", bi.chg2796x.getText().toString());

        json.put("chg28", bi.chg2801.isChecked() ? "1"
                : bi.chg2802.isChecked() ? "98"
                : "-1");
        json.put("chg2801x", bi.chg2801x.getText().toString());

        json.put("chg29", bi.chg2901.isChecked() ? "1"
                : bi.chg2902.isChecked() ? "98"
                : "-1");
        json.put("chg2901x", bi.chg2901x.getText().toString());

        json.put("chg30", bi.chg3001.isChecked() ? "1"
                : bi.chg3002.isChecked() ? "98"
                : "-1");
        json.put("chg3001x", bi.chg3001x.getText().toString());

        json.put("chg31", bi.chg3101.isChecked() ? "1"
                : bi.chg3102.isChecked() ? "2"
                : bi.chg3103.isChecked() ? "3"
                : bi.chg3104.isChecked() ? "4"
                : bi.chg3196.isChecked() ? "96"
                : "-1");
        json.put("chg3196x", bi.chg3196x.getText().toString());

        json.put("chg32", bi.chg3201.isChecked() ? "1"
                : bi.chg3202.isChecked() ? "2"
                : "-1");

        json.put("chg33", bi.chg3301.isChecked() ? "1"
                : bi.chg3302.isChecked() ? "2"
                : "-1");

        json.put("chg3401", bi.chg3401.isChecked() ? "1" : "-1");
        json.put("chg3402", bi.chg3402.isChecked() ? "2" : "-1");
        json.put("chg3403", bi.chg3403.isChecked() ? "3" : "-1");
        json.put("chg3404", bi.chg3404.isChecked() ? "4" : "-1");
        json.put("chg3405", bi.chg3405.isChecked() ? "5" : "-1");
        json.put("chg3406", bi.chg3406.isChecked() ? "6" : "-1");
        json.put("chg3407", bi.chg3407.isChecked() ? "7" : "-1");
        json.put("chg3408", bi.chg3408.isChecked() ? "8" : "-1");
        json.put("chg3409", bi.chg3409.isChecked() ? "9" : "-1");
        json.put("chg3410", bi.chg3410.isChecked() ? "10" : "-1");
        json.put("chg3411", bi.chg3411.isChecked() ? "11" : "-1");
        json.put("chg3412", bi.chg3412.isChecked() ? "12" : "-1");
        json.put("chg3496", bi.chg3496.isChecked() ? "96" : "-1");
        json.put("chg3496x", bi.chg3496x.getText().toString().trim().isEmpty() ? "-1" : bi.chg3496x.getText().toString());

        try {
            JSONObject json_merge = JSONUtils.mergeJSONObjects(new JSONObject(MainApp.form.getsG()), json);

            MainApp.form.setsG(String.valueOf(json_merge));

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }


    private boolean formValidation() {
        return Validator.emptyCheckingContainer(this, bi.GrpName);
    }


    public void BtnEnd() {
        AppUtilsKt.openEndActivity(this);
    }


    @Override
    public void onBackPressed() {
        Toast.makeText(this, "You Can't go back", Toast.LENGTH_LONG).show();
    }
}