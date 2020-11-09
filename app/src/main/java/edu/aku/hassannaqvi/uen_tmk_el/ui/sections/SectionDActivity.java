package edu.aku.hassannaqvi.uen_tmk_el.ui.sections;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.validatorcrawler.aliazaz.Clear;
import com.validatorcrawler.aliazaz.Validator;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import edu.aku.hassannaqvi.uen_tmk_el.R;
import edu.aku.hassannaqvi.uen_tmk_el.contracts.FamilyMembersContract;
import edu.aku.hassannaqvi.uen_tmk_el.core.DatabaseHelper;
import edu.aku.hassannaqvi.uen_tmk_el.core.MainApp;
import edu.aku.hassannaqvi.uen_tmk_el.databinding.ActivitySectionDBinding;
import edu.aku.hassannaqvi.uen_tmk_el.datecollection.AgeModel;
import edu.aku.hassannaqvi.uen_tmk_el.datecollection.DateRepository;
import edu.aku.hassannaqvi.uen_tmk_el.ui.list_activity.FamilyMembersListActivity;
import edu.aku.hassannaqvi.uen_tmk_el.utils.AppUtilsKt;
import edu.aku.hassannaqvi.uen_tmk_el.utils.EndSectionActivity;
import edu.aku.hassannaqvi.uen_tmk_el.viewmodel.MainVModel;
import kotlin.Pair;

import static edu.aku.hassannaqvi.uen_tmk_el.CONSTANTS.SERIAL_EXTRA;

public class SectionDActivity extends AppCompatActivity implements EndSectionActivity {

    ActivitySectionDBinding bi;
    private MainVModel mainVModel;
    private FamilyMembersContract fmc;
    private boolean fmcFLAG = false;
    private int serial = 0;
    private Pair<List<Integer>, List<String>> menSLst;
    private Pair<List<Integer>, List<String>> womenSLst;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        bi = DataBindingUtil.setContentView(this, R.layout.activity_section_d);
        bi.setCallback(this);

        setUIComponent();
        setListeners();
    }

    private void setUIComponent() {
        mainVModel = FamilyMembersListActivity.Companion.getMainVModel();
        serial = getIntent().getIntExtra(SERIAL_EXTRA, 0);
        bi.mmd1.setText(String.valueOf(serial));
        fmc = mainVModel.getMemberInfo(serial);
        fmcFLAG = fmc == null;

        if (fmcFLAG) {
            bi.fldGrpSectionD01.setVisibility(View.VISIBLE);
            bi.fldGrpSectionD02.setVisibility(View.GONE);
            fmc = new FamilyMembersContract();
        } else {
            bi.a202Name.setText(new StringBuilder(fmc.getName().toUpperCase()).append("\n")
                    .append(getResources().getString(R.string.mmd1))
                    .append(":")
                    .append(fmc.getSerialno()));
            bi.fldGrpSectionD01.setVisibility(View.GONE);
            bi.fldGrpSectionD02.setVisibility(View.VISIBLE);

            menSLst = mainVModel.getAllMenWomenName(1, Integer.parseInt(fmc.getSerialno()));
            womenSLst = mainVModel.getAllMenWomenName(2, Integer.parseInt(fmc.getSerialno()));

            List<String> menLst = new ArrayList<String>() {
                {
                    add("....");
                    add("NA");
                    if (menSLst != null) addAll(menSLst.getSecond());
                }
            };
            List<String> womenLst = new ArrayList<String>() {
                {
                    add("....");
                    add("NA");
                    if (womenSLst != null) addAll(womenSLst.getSecond());
                }
            };

            bi.mmd0801.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, menLst));
            bi.mmd901.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, womenLst));
        }

        if (serial == 1) {
            Clear.clearAllFields(bi.mmd3, false);
            bi.mmd301.setChecked(true);
            bi.mmd601.setMinvalue(18);
        }

    }

    public void BtnContinue() {
        if (!formValidation()) return;

        if (fmcFLAG) {
            try {
                SaveDraft();
                setResult(RESULT_OK, new Intent().putExtra(SERIAL_EXTRA, serial));
                finish();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            int calculatingAge = Integer.parseInt(bi.mmd601.getText().toString());
            if ((calculatingAge >= 5 && calculatingAge < 10) && bi.mmd901.getSelectedItemPosition() == 1)
                AppUtilsKt.openWarningActivity(this, "Are you confirming that mother is not available?");
            else savingDB();
        }

    }

    private boolean UpdateDB() {
        DatabaseHelper db = MainApp.appInfo.getDbHelper();
        long updcount = db.addFamilyMember(fmc);
        fmc.set_id(String.valueOf(updcount));
        if (updcount > 0) {
            fmc.setUid(MainApp.deviceId + fmc.get_id());
            db.updatesFamilyMemberColumn(FamilyMembersContract.MemberTable.COLUMN_UID, fmc.getUid(), fmc.get_id());
            return true;
        } else {
            Toast.makeText(this, "Sorry. You can't go further.\n Please contact IT Team (Failed to update DB)", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    private void SaveDraft() throws JSONException {

        if (fmcFLAG) {
            fmc.setUuid(MainApp.form.get_UID());
//            fmc.setLuid(MainApp.form.getLuid());
            fmc.setClusterno(MainApp.form.getElb1());
            fmc.setSubclusterno(MainApp.form.getElb8a());
            fmc.setHhno(MainApp.form.getElb11());
            fmc.setSerialno(bi.mmd1.getText().toString());
            fmc.setName(bi.mmd2.getText().toString());
            fmc.setRelHH(bi.mmd301.isChecked() ? "1" :
                    bi.mmd302.isChecked() ? "2" :
                            bi.mmd303.isChecked() ? "3" :
                                    bi.mmd304.isChecked() ? "4" :
                                            bi.mmd305.isChecked() ? "5" :
                                                    bi.mmd306.isChecked() ? "6" :
                                                            bi.mmd307.isChecked() ? "7" :
                                                                    bi.mmd308.isChecked() ? "8" :
                                                                            bi.mmd309.isChecked() ? "9" :
                                                                                    bi.mmd310.isChecked() ? "10" :
                                                                                            bi.mmd311.isChecked() ? "11" :
                                                                                                    bi.mmd312.isChecked() ? "12" :
                                                                                                            bi.mmd313.isChecked() ? "13" :
                                                                                                                    bi.mmd314.isChecked() ? "14" :
                                                                                                                            bi.mmd315.isChecked() ? "15" : "-1");
            fmc.setRelHHxx(bi.mmd315x.getText().toString());

            fmc.setGender(
                    bi.mmd401.isChecked() ? "1" :
                            bi.mmd402.isChecked() ? "2" :
                                    bi.mmd403.isChecked() ? "3" : "-1");

            if (serial == 1) FamilyMembersListActivity.Companion.setGenderFlag(fmc.getGender());

            fmc.setAge("-1");

            // Update in ViewModel
            mainVModel.setFamilyMembers(fmc);
            serial++;
            return;
        }

        fmc.setMarital(
                bi.mmd701.isChecked() ? "1" :
                        bi.mmd702.isChecked() ? "2" :
                                bi.mmd703.isChecked() ? "3" :
                                        bi.mmd704.isChecked() ? "4" : "-1");

        JSONObject sd = new JSONObject();

        sd.put("sysdate", MainApp.form.getSysdate());
        sd.put("username", MainApp.userName);
        sd.put("deviceid", MainApp.appInfo.getDeviceID());
        sd.put("tagid", MainApp.appInfo.getTagName());
        sd.put("appversion", MainApp.appInfo.getAppVersion());

        FamilyMembersContract motherFMC;
        sd.put("mmd0801", menSLst.getFirst().size() != 0 && bi.mmd0801.getSelectedItemPosition() != 1
                ? mainVModel.getMemberInfo(menSLst.getFirst().get(bi.mmd0801.getSelectedItemPosition() - 2)).getSerialno() : "97");
        fmc.setfName(bi.mmd0801.getSelectedItem().toString());

        motherFMC = womenSLst.getFirst().size() != 0 && bi.mmd901.getSelectedItemPosition() != 1
                ? mainVModel.getMemberInfo(womenSLst.getFirst().get(bi.mmd901.getSelectedItemPosition() - 2)) : null;
        String motherSerial = womenSLst.getFirst().size() != 0 && bi.mmd901.getSelectedItemPosition() != 1
                ? mainVModel.getMemberInfo(womenSLst.getFirst().get(bi.mmd901.getSelectedItemPosition() - 2)).getSerialno() : "97";
        fmc.setMother_name(bi.mmd901.getSelectedItem().toString());
        sd.put("mmd901", motherSerial);
        fmc.setMother_serial(motherSerial);

        sd.put("mmd501", bi.mmd501.getText().toString());
        sd.put("mmd502", bi.mmd502.getText().toString());
        sd.put("mmd503", bi.mmd503.getText().toString());
        sd.put("mmd601", bi.mmd601.getText().toString());
        sd.put("mmd602", bi.mmd602.getText().toString());
        fmc.setAge(bi.mmd601.getText().toString());
        fmc.setAgeMonths(Integer.parseInt(bi.mmd601.getText().toString()) * 12 + Integer.parseInt(bi.mmd602.getText().toString()));

        sd.put("mmd10", bi.mmd1001.isChecked() ? "1"
                : bi.mmd1002.isChecked() ? "2"
                : "-1");

        sd.put("mmd11", bi.mmd1100.isChecked() ? "0" :
                bi.mmd1101.isChecked() ? "1" :
                        bi.mmd1102.isChecked() ? "2" :
                                bi.mmd1103.isChecked() ? "3" :
                                        bi.mmd1104.isChecked() ? "4" :
                                                bi.mmd1105.isChecked() ? "5" :
                                                        bi.mmd1106.isChecked() ? "6" :
                                                                bi.mmd1107.isChecked() ? "7" :
                                                                        bi.mmd1108.isChecked() ? "8" :
                                                                                bi.mmd1109.isChecked() ? "9" :
                                                                                        bi.mmd1110.isChecked() ? "10" :
                                                                                                bi.mmd1111.isChecked() ? "11" :
                                                                                                        bi.mmd1112.isChecked() ? "12" :
                                                                                                                bi.mmd1113.isChecked() ? "13" :
                                                                                                                        bi.mmd1114.isChecked() ? "14" :
                                                                                                                                bi.mmd1115.isChecked() ? "15" :
                                                                                                                                        bi.mmd1116.isChecked() ? "16" :
                                                                                                                                                bi.mmd1117.isChecked() ? "17" :
                                                                                                                                                        bi.mmd1118.isChecked() ? "18" :
                                                                                                                                                                bi.mmd1119.isChecked() ? "19" :
                                                                                                                                                                        bi.mmd1120.isChecked() ? "20" :
                                                                                                                                                                                bi.mmd1198.isChecked() ? "98" :
                                                                                                                                                                                        bi.mmd1199.isChecked() ? "99" : "-1");
        sd.put("mmd12", bi.mmd1201.isChecked() ? "1" :
                bi.mmd1202.isChecked() ? "2" :
                        bi.mmd1203.isChecked() ? "3" :
                                bi.mmd1204.isChecked() ? "4" :
                                        bi.mmd1205.isChecked() ? "5" :
                                                bi.mmd1206.isChecked() ? "6" :
                                                        bi.mmd1207.isChecked() ? "6" :
                                                                bi.mmd1208.isChecked() ? "8" :
                                                                        bi.mmd1209.isChecked() ? "9" :
                                                                                bi.mmd1210.isChecked() ? "10" :
                                                                                        bi.mmd1211.isChecked() ? "11" :
                                                                                                bi.mmd1212.isChecked() ? "12" :
                                                                                                        bi.mmd1299.isChecked() ? "99" : "-1");

        sd.put("mmd13", bi.mmd1301.isChecked() ? "1" : bi.mmd1302.isChecked() ? "2" : "-1");

        sd.put("tagid", MainApp.form.getTagid());

        fmc.setAvailable(bi.mmd1301.isChecked() ? "1" : bi.mmd1302.isChecked() ? "2" : "-1");

        fmc.setsD(String.valueOf(sd));

        // Update in ViewModel
        mainVModel.updateFamilyMembers(fmc);

        if (Integer.parseInt(fmc.getAge()) >= 15 && Integer.parseInt(fmc.getAge()) < 50 && fmc.getGender().equals("2") && !bi.mmd702.isChecked())
            mainVModel.setMWRA(fmc);
        else if (Integer.parseInt(fmc.getAge()) < 5) {
            mainVModel.setChildU5(fmc);
            if (fmc.getAgeMonths() < 24) {
                if (motherFMC == null) return;
                if (Integer.parseInt(motherFMC.getAge()) >= 15 && Integer.parseInt(motherFMC.getAge()) < 50)
                    mainVModel.setMwraChildU2(motherFMC);
            }
        }

    }

    private boolean formValidation() {
        if (fmcFLAG) return Validator.emptyCheckingContainer(this, bi.fldGrpSectionD);
        else {
            if (!Validator.emptyCheckingContainer(this, bi.fldGrpSectionD))
                return false;
            if (!Validator.emptyEditTextPicker(this, bi.mmd601))
                return false;

            if (Integer.parseInt(bi.mmd601.getText().toString()) == 0 && Integer.parseInt(bi.mmd602.getText().toString()) == 0) {
                bi.mmd602.setError("Year and Month both cannot be 0");
                bi.mmd602.setFocusable(true);
                return false;
            }
            return true;
        }
    }

    public void BtnEnd() {
        finish();
    }

    private void setListeners() {
        EditText[] txtListener = new EditText[]{bi.mmd501, bi.mmd502};
        for (EditText txtItem : txtListener) {

            txtItem.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    bi.mmd601.setText(null);
                    bi.mmd602.setText(null);
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

        }

        bi.mmd503.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                bi.mmd601.setEnabled(false);
                bi.mmd602.setEnabled(false);
                bi.mmd601.setText(null);
                bi.mmd602.setText(null);
                if (bi.mmd501.getText().toString().isEmpty() || bi.mmd502.getText().toString().isEmpty() || bi.mmd503.getText().toString().isEmpty())
                    return;
                if (bi.mmd501.getText().toString().equals("98") && bi.mmd502.getText().toString().equals("98") && bi.mmd503.getText().toString().equals("9998")) {
                    bi.mmd601.setEnabled(true);
                    bi.mmd602.setEnabled(true);
                    return;
                }

                int day = bi.mmd501.getText().toString().equals("98") ? 15 : Integer.parseInt(bi.mmd501.getText().toString());
                int month = Integer.parseInt(bi.mmd502.getText().toString());
                int year = Integer.parseInt(bi.mmd503.getText().toString());

                AgeModel age = DateRepository.Companion.getCalculatedAge(year, month, day);
                if (age == null) return;
                bi.mmd601.setText(String.valueOf(age.getYear()));
                bi.mmd602.setText(String.valueOf(age.getMonth()));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        bi.mmd7.setOnCheckedChangeListener((group, checkedId) -> {
            if (fmc.getGender().equals("2") && !bi.mmd702.isChecked())
                bi.mmd1201.setEnabled(true);
            else {
                bi.mmd1201.setEnabled(false);
                bi.mmd1201.setChecked(false);
            }
        });

        bi.mmd601.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (bi.mmd601.getText().toString().isEmpty()) return;
                int calAge = Integer.parseInt(bi.mmd601.getText().toString());
                if (Integer.signum(calAge) == -1) return;
                personInfoFunctionality(calAge);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        bi.mmd10.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == bi.mmd1001.getId()) {
                bi.fldGrpCVmmd11.setVisibility(View.VISIBLE);
            } else {
                bi.mmd11.clearCheck();
                bi.fldGrpCVmmd11.setVisibility(View.GONE);
            }
        });

        bi.mmd3.setOnCheckedChangeListener((group, checkedID) -> {
            Clear.clearAllFields(bi.mmd4, true);
            if (serial == 2) {
                if (checkedID == bi.mmd302.getId()) {
                    String gender = FamilyMembersListActivity.Companion.getGenderFlag();
                    if (!gender.equals("3")) Clear.clearAllFields(bi.mmd4, false);
                    switch (gender) {
                        case "1":
                            bi.mmd402.setChecked(true);
                            break;
                        case "2":
                            bi.mmd401.setChecked(true);
                            break;
                    }
                }
            }
        });

    }

    private void personInfoFunctionality(int calAge) {

        bi.fldGrpCVmmd7.setVisibility(View.GONE);
        bi.fldGrpCVmmd10.setVisibility(View.GONE);
        bi.fldGrpCVmmd11.setVisibility(View.GONE);
        bi.fldGrpCVmmd12.setVisibility(View.GONE);

        if (calAge > 3 && calAge < 10) {
            bi.fldGrpCVmmd10.setVisibility(View.VISIBLE);
            bi.fldGrpCVmmd11.setVisibility(View.VISIBLE);
        } else if (calAge == 10) {
            bi.fldGrpCVmmd10.setVisibility(View.VISIBLE);
            bi.fldGrpCVmmd11.setVisibility(View.VISIBLE);
            bi.fldGrpCVmmd12.setVisibility(View.VISIBLE);
        } else if (calAge > 10) {
            bi.fldGrpCVmmd7.setVisibility(View.VISIBLE);
            bi.fldGrpCVmmd10.setVisibility(View.VISIBLE);
            bi.fldGrpCVmmd11.setVisibility(View.VISIBLE);
            bi.fldGrpCVmmd12.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public boolean onSupportNavigateUp() {
        setResult(RESULT_CANCELED);
        finish();
        return true;
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this, "Press top back button.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void endSecActivity(boolean flag) {
        savingDB();
    }

    private void savingDB() {
        try {
            SaveDraft();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (UpdateDB()) {
            setResult(RESULT_OK);
            finish();
        } else {
            Toast.makeText(this, "Failed to Update Database!", Toast.LENGTH_SHORT).show();
        }
    }
}
