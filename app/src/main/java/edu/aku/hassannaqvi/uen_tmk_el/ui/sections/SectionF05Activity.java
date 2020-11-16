package edu.aku.hassannaqvi.uen_tmk_el.ui.sections;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
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
import org.threeten.bp.Instant;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import edu.aku.hassannaqvi.uen_tmk_el.CONSTANTS;
import edu.aku.hassannaqvi.uen_tmk_el.R;
import edu.aku.hassannaqvi.uen_tmk_el.contracts.DeathContract;
import edu.aku.hassannaqvi.uen_tmk_el.core.DatabaseHelper;
import edu.aku.hassannaqvi.uen_tmk_el.core.MainApp;
import edu.aku.hassannaqvi.uen_tmk_el.databinding.ActivitySectionF05Binding;
import edu.aku.hassannaqvi.uen_tmk_el.datecollection.AgeModel;
import edu.aku.hassannaqvi.uen_tmk_el.datecollection.DateRepository;
import edu.aku.hassannaqvi.uen_tmk_el.models.Death;
import edu.aku.hassannaqvi.uen_tmk_el.ui.list_activity.FamilyMembersListActivity;
import edu.aku.hassannaqvi.uen_tmk_el.utils.AppUtilsKt;
import kotlin.Pair;

import static edu.aku.hassannaqvi.uen_tmk_el.CONSTANTS.C_DEATH_COUNT;

public class SectionF05Activity extends AppCompatActivity {

    ActivitySectionF05Binding bi;
    int count, cCounter = 1;
    boolean imFlag = true;
    Instant dtInstant = null;
    private Death death;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bi = DataBindingUtil.setContentView(this, R.layout.activity_section_f05);
        bi.setCallback(this);
        setupContent();

        //for 5 years in CMF9H (Age at death) calendar
        /*String dt = DateUtils.getYearsBack("dd/MM/yyyy", -5);
        bi.cmf9h.setMinDate(dt);*/
    }


    private void setupContent() {
        count = Integer.parseInt(getIntent().getStringExtra(C_DEATH_COUNT));
        setupNextButtonText();

        //Set Spinner
        Pair<List<Integer>, List<String>> mList = FamilyMembersListActivity.mainVModel.getAllMarriedWomen();
        List<String> mother = new ArrayList<String>() {
            {
                add("....");
                add("N/A");
                addAll(mList.getSecond());
            }
        };
        bi.cmf9c.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, mother));

        bi.cmf9c.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) return;
                String serial = i == 1 ? "97" : String.valueOf(mList.getFirst().get(i - 2));
                bi.cmf9d.setText(serial);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        bi.cmf9hy.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                dtInstant = null;
//                if (!bi.im021.isChecked() || bi.im0497.isChecked()) return;
                String txt01, txt02, txt03;
                bi.cmf9hd.setEnabled(true);
                bi.cmf9hm.setEnabled(true);
                if (!TextUtils.isEmpty(bi.cmf9hd.getText()) && !TextUtils.isEmpty(bi.cmf9hm.getText()) && !TextUtils.isEmpty(bi.cmf9hy.getText())) {
                    txt01 = bi.cmf9hd.getText().toString();
                    txt02 = bi.cmf9hm.getText().toString();
                    txt03 = bi.cmf9hy.getText().toString();
                } else return;
                if ((!bi.cmf9hd.isRangeTextValidate()) ||
                        (!bi.cmf9hm.isRangeTextValidate()) ||
                        (!bi.cmf9hy.isRangeTextValidate()))
                    return;

                if (bi.cmf9hd.getText().toString().equals("98") && bi.cmf9hm.getText().toString().equals("98")) {
                    imFlag = true;
                    return;
                }

                int day = bi.cmf9hd.getText().toString().equals("98") ? 15 : Integer.parseInt(txt01);
                int month = Integer.parseInt(txt02);
                int year = Integer.parseInt(txt03);

                AgeModel age;
                if (MainApp.form.getLocalDate() != null)
                    age = DateRepository.Companion.getCalculatedAge(MainApp.form.getLocalDate(), year, month, day);
                else
                    age = DateRepository.Companion.getCalculatedAge(year, month, day);
                if (age == null) {
                    bi.cmf9hy.setError("Invalid date");
                    imFlag = false;
                } else {
                    imFlag = true;
                    bi.cmf9hd.setEnabled(false);
                    bi.cmf9hm.setEnabled(false);

                    //Setting Date
                    try {
                        dtInstant = Instant.parse(new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).format(new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH).parse(
                                day + "-" + month + "-" + year
                        )) + "T06:24:01Z");

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private boolean setupNextButtonText() {
        if (count > 0) {
            Clear.clearAllFields(bi.GrpName);
            bi.cmf9a.setText(String.valueOf(cCounter));
            bi.cmf9b.requestFocus();
            if (count > 1) {
                bi.btnContinue.setText("Next Death");
                return false;
            } else if (count == 1) {
                bi.btnContinue.setText("Next Section");
                return false;
            }
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
                    startActivity(new Intent(this, SectionF06Activity.class));
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
        long updcount = db.addDeath(death);
        death.set_ID(String.valueOf(updcount));
        if (updcount > 0) {
            death.set_UID(death.getDeviceID() + death.get_ID());
            db.updatesDeathColumn(DeathContract.DeathTable.COLUMN_UID, death.get_UID(), death.get_ID());
            return true;
        } else {
            Toast.makeText(this, "Sorry. You can't go further.\n Please contact IT Team (Failed to update DB)", Toast.LENGTH_SHORT).show();
            return false;
        }
    }


    private void SaveDraft() throws JSONException {

        death = new Death();
        death.setSysdate(MainApp.form.getSysdate());
        death.setUUID(MainApp.form.get_UID());
        death.setUsername(MainApp.userName);
        death.setDeviceID(MainApp.appInfo.getDeviceID());
        death.setDevicetagID(MainApp.appInfo.getTagName());
        death.setAppversion(MainApp.appInfo.getAppVersion());
        death.setElb1(MainApp.form.getElb1());
        death.setElb11(MainApp.form.getElb11());
        death.setType(CONSTANTS.CHILD_DEATH_TYPE);

        JSONObject json = new JSONObject();

        json.put("elb8a", MainApp.form.getElb8a());
        json.put("cmf9a", bi.cmf9a.getText().toString());
        json.put("cmf9b", bi.cmf9b.getText().toString());
        json.put("cmf9c", bi.cmf9c.getSelectedItem().toString());
        json.put("cmf9d", bi.cmf9d.getText().toString());

        json.put("cmf9e", bi.cmf9e01.isChecked() ? "1"
                : bi.cmf9e02.isChecked() ? "2"
                : "-1");

        json.put("cmf9fd", bi.cmf9fd.getText().toString().trim().isEmpty() ? "-1" : bi.cmf9fd.getText().toString());
        json.put("cmf9fm", bi.cmf9fm.getText().toString().trim().isEmpty() ? "-1" : bi.cmf9fm.getText().toString());
        json.put("cmf9fy", bi.cmf9fy.getText().toString().trim().isEmpty() ? "-1" : bi.cmf9fy.getText().toString());

        json.put("cmf9g", bi.cmf9g01.isChecked() ? "1"
                : bi.cmf9g02.isChecked() ? "2"
                : bi.cmf9g03.isChecked() ? "3"
                : bi.cmf9g04.isChecked() ? "4"
                : bi.cmf9g05.isChecked() ? "5"
                : "-1");

//        json.put("cmf9h", bi.cmf9h.getText().toString());

        json.put("cmf9hd", bi.cmf9hd.getText().toString().trim().isEmpty() ? "-1" : bi.cmf9hd.getText().toString());
        json.put("cmf9hm", bi.cmf9hm.getText().toString().trim().isEmpty() ? "-1" : bi.cmf9hm.getText().toString());
        json.put("cmf9hy", bi.cmf9hy.getText().toString().trim().isEmpty() ? "-1" : bi.cmf9hy.getText().toString());

        json.put("cmf9i", bi.cmf9i1.isChecked() ? "1"
                : bi.cmf9i2.isChecked() ? "2"
                : bi.cmf9i3.isChecked() ? "3"
                : bi.cmf9i4.isChecked() ? "4"
                : bi.cmf9i5.isChecked() ? "5"
                : bi.cmf9i6.isChecked() ? "6"
                : bi.cmf9i7.isChecked() ? "7"
                : bi.cmf9i8.isChecked() ? "8"
                : bi.cmf9i96.isChecked() ? "96"
                : "-1");
        json.put("cmf9i96x", bi.cmf9i96x.getText().toString());

        death.setsB(json.toString());

        count--;
        cCounter++;
    }


    private boolean formValidation() {
        if (!imFlag) {
            Toast.makeText(this, "Invalid date!", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!Validator.emptyCheckingContainer(this, bi.GrpName))
            return false;

        if (bi.cmf9fy.getText().toString().equals("0") && bi.cmf9fm.getText().toString().equals("0") && bi.cmf9fd.getText().toString().equals("0")) {
            Toast.makeText(this, "Day, Month & Year can't be zero", Toast.LENGTH_SHORT).show();
            bi.cmf9fy.requestFocus();
            return false;
        }

        return true;

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