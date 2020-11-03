package edu.aku.hassannaqvi.uen_tmk_el.ui.sections;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
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
import edu.aku.hassannaqvi.uen_tmk_el.databinding.ActivitySectionCBinding;
import edu.aku.hassannaqvi.uen_tmk_el.ui.list_activity.FamilyMembersListActivity;
import edu.aku.hassannaqvi.uen_tmk_el.ui.other.EndingActivity;
import edu.aku.hassannaqvi.uen_tmk_el.utils.AppUtilsKt;
import edu.aku.hassannaqvi.uen_tmk_el.utils.JSONUtils;

import static edu.aku.hassannaqvi.uen_tmk_el.core.MainApp.userName;


public class SectionCActivity extends AppCompatActivity {

    ActivitySectionCBinding bi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bi = DataBindingUtil.setContentView(this, R.layout.activity_section_c);
        bi.setCallback(this);
        setupSkip();

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.intro_dialog, null);
        dialogBuilder.setView(dialogView);

        TextView textView = dialogView.findViewById(R.id.elc_title22);
        textView.setText(" منھنجو نالو " + userName + " آھي ");
        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

    }


    private void setupSkip() {
        bi.elc2.setOnCheckedChangeListener((radioGroup, i) -> Clear.clearAllFields(bi.llelec2));
    }


    public void BtnContinue() {
        if (!formValidation()) return;
        try {
            SaveDraft();
            if (UpdateDB()) {
                finish();
                startActivity(new Intent(this, bi.elc202.isChecked() ? EndingActivity.class : FamilyMembersListActivity.class).putExtra("complete", false));
            } else {
                Toast.makeText(this, "Sorry. You can't go further.\n Please contact IT Team (Failed to update DB)", Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private boolean UpdateDB() {
        DatabaseHelper db = MainApp.appInfo.getDbHelper();
        int updcount = db.updatesFormColumn(FormsContract.FormsTable.COLUMN_SC, MainApp.form.getsC());
        return updcount == 1;
    }


    private void SaveDraft() throws JSONException {

        JSONObject json = new JSONObject();

        json.put("elc1", bi.elc101.isChecked() ? "1"
                : bi.elc102.isChecked() ? "2"
                : "-1");

        json.put("elc2", bi.elc201.isChecked() ? "1"
                : bi.elc202.isChecked() ? "2"
                : "-1");

        json.put("elc3", bi.elc3.getText().toString());

        json.put("elc4", bi.elc401.isChecked() ? "1"
                : bi.elc402.isChecked() ? "2"
                : "-1");

        json.put("elc501", bi.elc501.getText().toString().trim().isEmpty() ? "-1" : bi.elc501.getText().toString());
        json.put("elc502", bi.elc502.getText().toString().trim().isEmpty() ? "-1" : bi.elc502.getText().toString());

        json.put("elc6", bi.elc601.isChecked() ? "1"
                : bi.elc602.isChecked() ? "2"
                : "-1");

        try {
            JSONObject json_merge = JSONUtils.mergeJSONObjects(new JSONObject(MainApp.form.getsC()), json);
            MainApp.form.setsC(String.valueOf(json_merge));

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