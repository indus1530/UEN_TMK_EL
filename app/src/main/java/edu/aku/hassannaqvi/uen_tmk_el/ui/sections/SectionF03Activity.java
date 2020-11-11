package edu.aku.hassannaqvi.uen_tmk_el.ui.sections;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
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
import java.util.Locale;

import edu.aku.hassannaqvi.uen_tmk_el.CONSTANTS;
import edu.aku.hassannaqvi.uen_tmk_el.R;
import edu.aku.hassannaqvi.uen_tmk_el.contracts.DeathContract;
import edu.aku.hassannaqvi.uen_tmk_el.core.DatabaseHelper;
import edu.aku.hassannaqvi.uen_tmk_el.core.MainApp;
import edu.aku.hassannaqvi.uen_tmk_el.databinding.ActivitySectionF03Binding;
import edu.aku.hassannaqvi.uen_tmk_el.datecollection.AgeModel;
import edu.aku.hassannaqvi.uen_tmk_el.datecollection.DateRepository;
import edu.aku.hassannaqvi.uen_tmk_el.models.Death;
import edu.aku.hassannaqvi.uen_tmk_el.utils.AppUtilsKt;

import static edu.aku.hassannaqvi.uen_tmk_el.CONSTANTS.DEATH_COUNT;


public class SectionF03Activity extends AppCompatActivity {

    ActivitySectionF03Binding bi;
    private Death death;
    int count, mCounter = 1;
    boolean imFlag = true;
    Instant dtInstant = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bi = DataBindingUtil.setContentView(this, R.layout.activity_section_f03);
        bi.setCallback(this);
        setupContent();

        //for 5 years in RAF73 (Age at death) calendar
        /*String dt = DateUtils.getYearsBack("dd/MM/yyyy", -5);
        bi.raf7e.setMinDate(dt);*/
    }


    private void setupContent() {
        count = Integer.parseInt(getIntent().getStringExtra(DEATH_COUNT));
        setupNextButtonText();

        bi.raf7ey.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                dtInstant = null;
//                if (!bi.im021.isChecked() || bi.im0497.isChecked()) return;
                String txt01, txt02, txt03;
                bi.raf7ed.setEnabled(true);
                bi.raf7em.setEnabled(true);
                if (!TextUtils.isEmpty(bi.raf7ed.getText()) && !TextUtils.isEmpty(bi.raf7em.getText()) && !TextUtils.isEmpty(bi.raf7ey.getText())) {
                    txt01 = bi.raf7ed.getText().toString();
                    txt02 = bi.raf7em.getText().toString();
                    txt03 = bi.raf7ey.getText().toString();
                } else return;
                if ((!bi.raf7ed.isRangeTextValidate()) ||
                        (!bi.raf7em.isRangeTextValidate()) ||
                        (!bi.raf7ey.isRangeTextValidate()))
                    return;

                if (bi.raf7ed.getText().toString().equals("98") && bi.raf7em.getText().toString().equals("98")) {
                    imFlag = true;
                    return;
                }
                int day = bi.raf7ed.getText().toString().equals("98") ? 15 : Integer.parseInt(txt01);
                int month = Integer.parseInt(txt02);
                int year = Integer.parseInt(txt03);

                AgeModel age;
                if (MainApp.form.getLocalDate() != null)
                    age = DateRepository.Companion.getCalculatedAge(MainApp.form.getLocalDate(), year, month, day);
                else
                    age = DateRepository.Companion.getCalculatedAge(year, month, day);
                if (age == null) {
                    bi.raf7ey.setError("Invalid date");
                    imFlag = false;
                } else {
                    imFlag = true;
                    bi.raf7ed.setEnabled(false);
                    bi.raf7em.setEnabled(false);

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
            bi.raf7a.setText(String.valueOf(mCounter));
            bi.raf7b.requestFocus();
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
                    startActivity(new Intent(this, SectionF04Activity.class));
                }
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
        death.setType(CONSTANTS.MOTHER_DEATH_TYPE);

        JSONObject json = new JSONObject();

        json.put("elb8a", MainApp.form.getElb8a());
        json.put("raf7a", bi.raf7a.getText().toString());
        json.put("raf7b", bi.raf7b.getText().toString());
        json.put("raf7cd", bi.raf7cd.getText().toString());
        json.put("raf7cm", bi.raf7cm.getText().toString());
        json.put("raf7cy", bi.raf7cy.getText().toString());

        json.put("raf7d", bi.raf7d01.isChecked() ? "1"
                : bi.raf7d02.isChecked() ? "2"
                : bi.raf7d03.isChecked() ? "3"
                : bi.raf7d04.isChecked() ? "4"
                : bi.raf7d05.isChecked() ? "5"
                : "-1");

//        json.put("raf7e", bi.raf7e.getText().toString().trim().isEmpty() ? "-1" : bi.raf7e.getText().toString());

        json.put("raf7cd", bi.raf7ed.getText().toString().trim().isEmpty() ? "-1" : bi.raf7ed.getText().toString());
        json.put("raf7cm", bi.raf7em.getText().toString().trim().isEmpty() ? "-1" : bi.raf7em.getText().toString());
        json.put("raf7cy", bi.raf7ey.getText().toString().trim().isEmpty() ? "-1" : bi.raf7ey.getText().toString());

        json.put("raf7f", bi.raf7f01.isChecked() ? "1"
                : bi.raf7f02.isChecked() ? "2"
                : bi.raf7f03.isChecked() ? "3"
                : bi.raf7f04.isChecked() ? "4"
                : bi.raf7f05.isChecked() ? "5"
                : bi.raf7f06.isChecked() ? "6"
                : bi.raf7f96.isChecked() ? "96"
                : "-1");

        json.put("raf7f96x", bi.raf7f96x.getText().toString());

        death.setsB(json.toString());

        count--;
        mCounter++;

    }


    private boolean formValidation() {
        if (!imFlag) {
            Toast.makeText(this, "Invalid date!", Toast.LENGTH_SHORT).show();
            return false;
        }
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