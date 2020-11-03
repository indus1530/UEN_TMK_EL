package edu.aku.hassannaqvi.uen_tmk_el.ui.sections;


import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.validatorcrawler.aliazaz.Clear;
import com.validatorcrawler.aliazaz.Validator;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.aku.hassannaqvi.uen_tmk_el.CONSTANTS;
import edu.aku.hassannaqvi.uen_tmk_el.R;
import edu.aku.hassannaqvi.uen_tmk_el.contracts.FamilyMembersContract;
import edu.aku.hassannaqvi.uen_tmk_el.contracts.Mwra_ChildrenContract;
import edu.aku.hassannaqvi.uen_tmk_el.core.DatabaseHelper;
import edu.aku.hassannaqvi.uen_tmk_el.core.MainApp;
import edu.aku.hassannaqvi.uen_tmk_el.databinding.ActivitySectionF01Binding;
import edu.aku.hassannaqvi.uen_tmk_el.models.MWRA_CHILD;
import edu.aku.hassannaqvi.uen_tmk_el.ui.list_activity.FamilyMembersListActivity;
import edu.aku.hassannaqvi.uen_tmk_el.utils.AppUtilsKt;

public class SectionF01Activity extends AppCompatActivity {

    ActivitySectionF01Binding bi;
    ArrayAdapter<String> adapter;
    List<String> mwraNames;
    Map<String, FamilyMembersContract> mwraMAP;
    FamilyMembersContract selectedMWRA;
    MWRA_CHILD mwraChild;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bi = DataBindingUtil.setContentView(this, R.layout.activity_section_f01);
        bi.setCallback(this);
        setupContent();
        setupListeners();
    }


    private void setupListeners() {
        bi.f1a.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) return;
                selectedMWRA = mwraMAP.get(bi.f1a.getSelectedItem().toString());
                bi.f1b.setText(selectedMWRA.getSerialno());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        bi.raf1.setOnCheckedChangeListener((group, id) -> Clear.clearAllFields(bi.llraf102));


        bi.raf2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (bi.raf2.getText().toString().isEmpty()) return;
                bi.raf302.setText("");
                bi.raf303.setText("");
                bi.raf304.setText("");
                bi.raf301.setEnabled(true);
                bi.raf301.setMaxvalue(Float.parseFloat(bi.raf2.getText().toString()));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        bi.raf301.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (bi.raf301.getText().toString().isEmpty()) return;
                bi.raf302.setText("");
                bi.raf303.setText("");
                bi.raf304.setText("");
                bi.raf302.setEnabled(true);
                bi.raf302.setMaxvalue(Float.parseFloat(bi.raf2.getText().toString()) - Float.parseFloat(bi.raf301.getText().toString()));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        bi.raf302.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (bi.raf302.getText().toString().isEmpty()) return;
                bi.raf303.setText("");
                bi.raf304.setText("");
                bi.raf303.setEnabled(true);
                bi.raf303.setMaxvalue(Float.parseFloat(bi.raf2.getText().toString()) - (Float.parseFloat(bi.raf301.getText().toString()) + Float.parseFloat(bi.raf302.getText().toString())));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        bi.raf303.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (bi.raf303.getText().toString().isEmpty()) return;
                bi.raf304.setText("");
                bi.raf304.setEnabled(true);
                bi.raf304.setMaxvalue(Float.parseFloat(bi.raf2.getText().toString())
                        - (Float.parseFloat(bi.raf301.getText().toString())
                        + Float.parseFloat(bi.raf302.getText().toString())
                        + Float.parseFloat(bi.raf303.getText().toString())));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }


    private void setupContent() {
        List<FamilyMembersContract> mwra = FamilyMembersListActivity.mainVModel.getMwraLst().getValue();
        mwraMAP = new HashMap<>();
        mwraNames = new ArrayList<>();
        mwraNames.add("....");
        for (FamilyMembersContract item : mwra) {
            mwraNames.add(item.getName());
            mwraMAP.put(item.getName(), item);
        }
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, mwraNames);
        bi.f1a.setAdapter(adapter);

        setupNextButtonText();
    }


    private boolean setupNextButtonText() {
        if (mwraNames.size() > 2) {
            Clear.clearAllFields(bi.fldGrpSecF01);
            bi.btnContinue.setText("Next MWRA");
            bi.f1b.setFocusable(true);
            return false;
        } else if (mwraNames.size() == 2) {
            bi.btnContinue.setText("Next Section");
            return false;
        }
        return true;
    }


    public void BtnContinue() {
        if (!formValidation()) return;
        try {
            SaveDraft();
            if (UpdateDB()) {
                if (setupNextButtonText()) {
                    finish();
                    startActivity(new Intent(this, SectionF02Activity.class));
                }
            } else {
                Toast.makeText(this, "Sorry. You can't go further.\n Please contact IT Team (Failed to update DB)", Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private boolean UpdateDB() {

        DatabaseHelper db = MainApp.appInfo.getDbHelper();
        long updcount = db.addMWRACHILD(mwraChild);
        mwraChild.set_ID(String.valueOf(updcount));
        if (updcount > 0) {
            mwraChild.set_UID(mwraChild.getDeviceID() + mwraChild.get_ID());
            db.updatesMWRAChildColumn(Mwra_ChildrenContract.MWRAChildTable.COLUMN_UID, mwraChild.get_UID(), mwraChild.get_ID());
            return true;
        } else {
            Toast.makeText(this, "Sorry. You can't go further.\n Please contact IT Team (Failed to update DB)", Toast.LENGTH_SHORT).show();
            return false;
        }
    }


    private void SaveDraft() throws JSONException {

        mwraChild = new MWRA_CHILD();
        mwraChild.setSysdate(MainApp.form.getSysdate());
        mwraChild.setUsername(MainApp.userName);
        mwraChild.setDeviceID(MainApp.appInfo.getDeviceID());
        mwraChild.setDevicetagID(MainApp.appInfo.getTagName());
        mwraChild.setAppversion(MainApp.appInfo.getAppVersion());
        mwraChild.setUUID(MainApp.form.get_UID());
        mwraChild.setElb1(MainApp.form.getElb1());
        mwraChild.setElb11(MainApp.form.getElb11());
        mwraChild.setFmuid(selectedMWRA.getUid());
        mwraChild.setType(CONSTANTS.MWRA_TYPE);

        JSONObject json = new JSONObject();

        json.put("elb8a", MainApp.form.getElb8a());
        json.put("f1a", bi.f1a.getSelectedItem().toString());
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

        mwraChild.setsB(json.toString());

        mwraNames.remove(bi.f1a.getSelectedItem().toString());
        adapter.notifyDataSetChanged();
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


    @Override
    public void onBackPressed() {
        Toast.makeText(this, "You Can't go back", Toast.LENGTH_LONG).show();
    }

}
