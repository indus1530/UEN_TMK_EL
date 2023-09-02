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
import edu.aku.hassannaqvi.uen_tmk_el.contracts.FormsContract;
import edu.aku.hassannaqvi.uen_tmk_el.core.DatabaseHelper;
import edu.aku.hassannaqvi.uen_tmk_el.core.MainApp;
import edu.aku.hassannaqvi.uen_tmk_el.databinding.ActivitySectionKBinding;
import edu.aku.hassannaqvi.uen_tmk_el.ui.list_activity.FamilyMembersListActivity;
import edu.aku.hassannaqvi.uen_tmk_el.ui.other.EndingActivity;
import edu.aku.hassannaqvi.uen_tmk_el.utils.AppUtilsKt;

public class SectionKActivity extends AppCompatActivity {

    ActivitySectionKBinding bi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bi = DataBindingUtil.setContentView(this, R.layout.activity_section_k);
        bi.setCallback(this);
        setupSkip();

    }


    private void setupSkip() {
        bi.wsk3.setOnCheckedChangeListener((radioGroup, i) -> {
            Clear.clearAllFields(bi.fldGrpCVwsk4);
            Clear.clearAllFields(bi.fldGrpCVwsk5);
        });
        bi.wsk8.setOnCheckedChangeListener((radioGroup, i) -> Clear.clearAllFields(bi.wsk9cv));
        bi.wsk11.setOnCheckedChangeListener((radioGroup, i) -> Clear.clearAllFields(bi.fldGrpk11));
        bi.wsk12.setOnCheckedChangeListener((radioGroup, i) -> Clear.clearAllFields(bi.fldGrpk12));

    }


    public void BtnContinue() {
        if (!formValidation()) return;
        try {
            SaveDraft();
            if (UpdateDB()) {
                finish();
                boolean routeFlag = FamilyMembersListActivity.mainVModel.getChildLstU5().getValue() != null && FamilyMembersListActivity.mainVModel.getChildLstU5().getValue().size() > 0;
                startActivity(new Intent(this, routeFlag ? SectionLActivity.class : MainApp.indexKishMWRA != null && MainApp.indexKishMWRA.getAvailable().equals("1") ? SectionN01Activity.class : EndingActivity.class).putExtra("complete", true));
            } else {
                Toast.makeText(this, "Sorry. You can't go further.\n Please contact IT Team (Failed to update DB)", Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private boolean UpdateDB() {
        DatabaseHelper db = MainApp.appInfo.getDbHelper();
        int updcount = db.updatesFormColumn(FormsContract.FormsTable.COLUMN_SK, MainApp.form.getsK());
        return updcount == 1;
    }

    private void SaveDraft() throws JSONException {

        JSONObject json = new JSONObject();

        json.put("wsk1", bi.wsk101.isChecked() ? "1"
                : bi.wsk102.isChecked() ? "2"
                : bi.wsk103.isChecked() ? "3"
                : bi.wsk104.isChecked() ? "4"
                : bi.wsk105.isChecked() ? "5"
                : bi.wsk106.isChecked() ? "6"
                : bi.wsk107.isChecked() ? "7"
                : bi.wsk108.isChecked() ? "8"
                : bi.wsk109.isChecked() ? "9"
                : bi.wsk110.isChecked() ? "10"
                : bi.wsk111.isChecked() ? "11"
                : bi.wsk112.isChecked() ? "12"
                : bi.wsk113.isChecked() ? "13"
                : bi.wsk114.isChecked() ? "14"
                : bi.wsk115.isChecked() ? "15"
                : bi.wsk196.isChecked() ? "96"
                : "-1");

        json.put("wsk196x", bi.wsk196x.getText().toString());
        json.put("wsk2", bi.wsk201.isChecked() ? "1"
                : bi.wsk202.isChecked() ? "2"
                : bi.wsk203.isChecked() ? "3"
                : bi.wsk204.isChecked() ? "4"
                : bi.wsk205.isChecked() ? "5"
                : bi.wsk206.isChecked() ? "6"
                : bi.wsk207.isChecked() ? "7"
                : bi.wsk208.isChecked() ? "8"
                : bi.wsk209.isChecked() ? "9"
                : bi.wsk210.isChecked() ? "10"
                : bi.wsk211.isChecked() ? "11"
                : bi.wsk212.isChecked() ? "12"
                : bi.wsk213.isChecked() ? "13"
                : bi.wsk214.isChecked() ? "14"
                : bi.wsk215.isChecked() ? "15"
                : bi.wsk296.isChecked() ? "96"
                : "-1");

        json.put("wsk296x", bi.wsk296x.getText().toString());
        json.put("wsk3", bi.wsk301.isChecked() ? "1"
                : bi.wsk302.isChecked() ? "2"
                : bi.wsk303.isChecked() ? "3"
                : "-1");

        json.put("wsk15", bi.wsk1501.isChecked() ? "1"
                : bi.wsk1502.isChecked() ? "2"
                : bi.wsk1503.isChecked() ? "3"
                : bi.wsk1504.isChecked() ? "4"
                : bi.wsk1596.isChecked() ? "96"
                : "-1");

        json.put("wsk1596x", bi.wsk1596x.getText().toString());
        json.put("wsk4", bi.wsk401.isChecked() ? ""
                : bi.wsk498.isChecked() ? "98"
                : "-1");

        json.put("wsk401x", bi.wsk401x.getText().toString());
        json.put("wsk5", bi.wsk501.isChecked() ? "1"
                : bi.wsk502.isChecked() ? "2"
                : bi.wsk503.isChecked() ? "3"
                : bi.wsk504.isChecked() ? "4"
                : bi.wsk505.isChecked() ? "5"
                : "-1");

        json.put("wsk6", bi.wsk601.isChecked() ? "1"
                : bi.wsk602.isChecked() ? "2"
                : "-1");

        json.put("wsk7", bi.wsk701.isChecked() ? "1"
                : bi.wsk702.isChecked() ? "2"
                : bi.wsk798.isChecked() ? "98"
                : "-1");

        json.put("wsk8", bi.wsk801.isChecked() ? "1"
                : bi.wsk802.isChecked() ? "2"
                : bi.wsk898.isChecked() ? "98"
                : "-1");

        json.put("wsk9", bi.wsk901.isChecked() ? "1"
                : bi.wsk902.isChecked() ? "2"
                : bi.wsk903.isChecked() ? "3"
                : bi.wsk904.isChecked() ? "4"
                : bi.wsk905.isChecked() ? "5"
                : bi.wsk906.isChecked() ? "6"
                : bi.wsk907.isChecked() ? "7"
                : bi.wsk996.isChecked() ? "96"
                : "-1");

        json.put("wsk996x", bi.wsk996x.getText().toString());
        json.put("wsk10", bi.wsk1001.isChecked() ? "1"
                : bi.wsk1002.isChecked() ? "2"
                : bi.wsk1003.isChecked() ? "3"
                : bi.wsk1004.isChecked() ? "4"
                : bi.wsk1005.isChecked() ? "5"
                : bi.wsk1006.isChecked() ? "6"
                : bi.wsk1096.isChecked() ? "96"
                : "-1");

        json.put("wsk1096x", bi.wsk1096x.getText().toString());
        json.put("wsk11", bi.wsk1101.isChecked() ? "1"
                : bi.wsk1102.isChecked() ? "2"
                : bi.wsk1103.isChecked() ? "3"
                : bi.wsk1104.isChecked() ? "4"
                : bi.wsk1105.isChecked() ? "5"
                : bi.wsk1106.isChecked() ? "6"
                : bi.wsk1107.isChecked() ? "7"
                : bi.wsk1108.isChecked() ? "8"
                : bi.wsk1109.isChecked() ? "9"
                : bi.wsk1110.isChecked() ? "10"
                : bi.wsk1111.isChecked() ? "11"
                : "-1");

        json.put("wsk12", bi.wsk1201.isChecked() ? "1"
                : bi.wsk1202.isChecked() ? "2"
                : bi.wsk1298.isChecked() ? "98"
                : "-1");

        json.put("wsk13", bi.wsk1301.isChecked() ? "1"
                : bi.wsk1302.isChecked() ? "2"
                : "-1");

        json.put("wsk14", bi.wsk1401.isChecked() ? "1"
                : bi.wsk1402.isChecked() ? "2"
                : bi.wsk1498.isChecked() ? "98"
                : "-1");

        json.put("wsk16", bi.wsk1601.isChecked() ? "1"
                : bi.wsk1602.isChecked() ? "2"
                : bi.wsk1603.isChecked() ? "3"
                : bi.wsk1604.isChecked() ? "4"
                : bi.wsk1605.isChecked() ? "96"
                : "-1");

        json.put("wsk1605x", bi.wsk1605x.getText().toString());

        MainApp.form.setsK(json.toString());
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