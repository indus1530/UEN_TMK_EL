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
import edu.aku.hassannaqvi.uen_tmk_el.databinding.ActivitySectionH02Binding;
import edu.aku.hassannaqvi.uen_tmk_el.utils.AppUtilsKt;
import edu.aku.hassannaqvi.uen_tmk_el.utils.JSONUtils;

public class SectionH02Activity extends AppCompatActivity {

    ActivitySectionH02Binding bi;
    public static boolean arih10, arih11;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bi = DataBindingUtil.setContentView(this, R.layout.activity_section_h02);
        bi.setCallback(this);

        setupSkip();
    }

    private void setupSkip() {
        arih10 = getIntent().getBooleanExtra("arih10", true);
        arih11 = getIntent().getBooleanExtra("arih11", true);

        if (arih10 && arih11) {
            bi.fldGrpSecH10.setVisibility(View.GONE);
            Clear.clearAllFields(bi.fldGrpSecH10);
        } else
            bi.fldGrpSecH10.setVisibility(View.VISIBLE);

        bi.arih29.setOnCheckedChangeListener((radioGroup, i) -> Clear.clearAllFields(bi.fldGrpCVarih30));

       /* bi.arih1.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId != bi.arih101.getId()) {
                Clear.clearAllFields(bi.fldGrpCVarih2);
            }
        });

        bi.arih3.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == bi.arih301.getId()) {
                Clear.clearAllFields(bi.fldGrpSecH01);
            }
        });

        bi.arih7.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == bi.arih701.getId()) {
                Clear.clearAllFields(bi.fldGrpCVarih8);
            }
        });

        bi.arih14.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == bi.arih1402.getId()) {
                Clear.clearAllFields(bi.fldGrpSecH02);
            }
        });

        bi.arih16.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == bi.arih1602.getId()) {
                Clear.clearAllFields(bi.fldGrpSecH03);
            }
        });

        bi.arih21.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == bi.arih2103.getId()) {
                Clear.clearAllFields(bi.fldGrpSecH04);
                Clear.clearAllFields(bi.fldGrpCVarih26);
            }
        });

        bi.arih22.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == bi.arih2202.getId()) {
                Clear.clearAllFields(bi.fldGrpSecH05);
            }
        });

        bi.arih24.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == bi.arih2401.getId()) {
                Clear.clearAllFields(bi.fldGrpCVarih25);
            }
        });*/

    }


    public void BtnContinue() {
        if (!formValidation()) return;
        try {
            SaveDraft();
            if (UpdateDB()) {
                finish();
                startActivity(new Intent(this, MainApp.indexKishMWRA != null ? SectionI01Activity.class : SectionKActivity.class));
            } else {
                Toast.makeText(this, "Sorry. You can't go further.\n Please contact IT Team (Failed to update DB)", Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private boolean UpdateDB() {
        DatabaseHelper db = MainApp.appInfo.getDbHelper();
        int updcount = db.updatesFormColumn(FormsContract.FormsTable.COLUMN_SH, MainApp.form.getsH());
        return updcount == 1;
    }


    private void SaveDraft() throws JSONException {

        JSONObject json = new JSONObject();

        json.put("arih27", bi.arih2701.isChecked() ? "1"
                : bi.arih2702.isChecked() ? "98"
                : "-1");
        json.put("arih2701x", bi.arih2701x.getText().toString().trim().isEmpty() ? "-1" : bi.arih2701x.getText().toString());

        json.put("arih28", bi.arih2801.isChecked() ? "1"
                : bi.arih2802.isChecked() ? "98"
                : "-1");
        json.put("arih2801x", bi.arih2801x.getText().toString().trim().isEmpty() ? "-1" : bi.arih2801x.getText().toString());


        json.put("arih29", bi.arih2901.isChecked() ? "1"
                : bi.arih2902.isChecked() ? "2"
                : bi.arih2903.isChecked() ? "3"
                : bi.arih2904.isChecked() ? "4"
                : bi.arih2905.isChecked() ? "5"
                : bi.arih2906.isChecked() ? "6"
                : bi.arih2907.isChecked() ? "7"
                : bi.arih2908.isChecked() ? "8"
                : bi.arih2909.isChecked() ? "9"
                : bi.arih2996.isChecked() ? "96"
                : "-1");
        json.put("arih2996x", bi.arih2996x.getText().toString().trim().isEmpty() ? "-1" : bi.arih2996x.getText().toString());

        json.put("arih30", bi.arih3001.isChecked() ? ""
                : bi.arih3002.isChecked() ? "98"
                : "-1");
        json.put("arih3001x", bi.arih3001x.getText().toString().trim().isEmpty() ? "-1" : bi.arih3001x.getText().toString());

        json.put("arih31", bi.arih3101.isChecked() ? "1"
                : bi.arih3102.isChecked() ? "2"
                : bi.arih3198.isChecked() ? "98"
                : "-1");

        json.put("arih32", bi.arih3201.isChecked() ? "1"
                : bi.arih3202.isChecked() ? "2"
                : bi.arih3298.isChecked() ? "98"
                : "-1");

        json.put("arih33", bi.arih3301.isChecked() ? "1"
                : bi.arih3302.isChecked() ? "2"
                : bi.arih3398.isChecked() ? "98"
                : "-1");

        json.put("arih34", bi.arih3401.isChecked() ? ""
                : bi.arih3402.isChecked() ? "98"
                : "-1");
        json.put("arih3401x", bi.arih3401x.getText().toString().trim().isEmpty() ? "-1" : bi.arih3401x.getText().toString());

        json.put("arih35", bi.arih3501.isChecked() ? "1"
                : bi.arih3502.isChecked() ? "2"
                : bi.arih3503.isChecked() ? "3"
                : bi.arih3504.isChecked() ? "4"
                : bi.arih3596.isChecked() ? "96"
                : "-1");
        json.put("arih3596x", bi.arih3596x.getText().toString().trim().isEmpty() ? "-1" : bi.arih3596x.getText().toString());

        json.put("arih3601", bi.arih3601.isChecked() ? "1" : "-1");
        json.put("arih3602", bi.arih3602.isChecked() ? "2" : "-1");
        json.put("arih3603", bi.arih3603.isChecked() ? "3" : "-1");
        json.put("arih3604", bi.arih3604.isChecked() ? "4" : "-1");
        json.put("arih3605", bi.arih3605.isChecked() ? "5" : "-1");
        json.put("arih3606", bi.arih3606.isChecked() ? "6" : "-1");
        json.put("arih3607", bi.arih3607.isChecked() ? "7" : "-1");
        json.put("arih3608", bi.arih3608.isChecked() ? "8" : "-1");
        json.put("arih3609", bi.arih3609.isChecked() ? "9" : "-1");
        json.put("arih3610", bi.arih3610.isChecked() ? "10" : "-1");
        json.put("arih3611", bi.arih3611.isChecked() ? "11" : "-1");
        json.put("arih3696", bi.arih3696.isChecked() ? "96" : "-1");
        json.put("arih3696x", bi.arih3696x.getText().toString().trim().isEmpty() ? "-1" : bi.arih3696x.getText().toString());

        try {
            JSONObject json_merge = JSONUtils.mergeJSONObjects(new JSONObject(MainApp.form.getsH()), json);

            MainApp.form.setsH(String.valueOf(json_merge));

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