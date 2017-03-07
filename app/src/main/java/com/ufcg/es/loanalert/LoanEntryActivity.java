package com.ufcg.es.loanalert;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;

import com.activeandroid.query.Select;
import com.github.guilhermesgb.marqueeto.LabelledMarqueeEditText;
import com.joanzapata.iconify.IconDrawable;

import org.joda.time.DateTime;

import butterknife.Bind;
import butterknife.ButterKnife;

public class LoanEntryActivity extends AppCompatActivity {

    public static final String BUNDLE_KEY_TARGET_LOAN_ENTRY_ID = "BUNDLE_KEY_TARGET_LOAN_ENTRY_ID";
    public static final String BUNDLE_KEY_RAW_LOAN_ENTRY_CREATION_DATE = "BUNDLE_KEY_RAW_LOAN_ENTRY_CREATION_DATE";
    public static final String BUNDLE_KEY_RAW_LOAN_ENTRY_DUE_DATE = "BUNDLE_KEY_RAW_LOAN_ENTRY_DUE_DATE";

    @Bind(R.id.loan_entry_title) LabelledMarqueeEditText loanEntryTitle;
    @Bind(R.id.loan_entry_observations) LabelledMarqueeEditText loanEntryObservations;
    @Bind(R.id.loan_entry_creation_date) LabelledMarqueeEditText loanEntryCreationDate;
    @Bind(R.id.loan_entry_creation_date_surface) View loanEntryCreationDateSurface;
    @Bind(R.id.loan_entry_due_date) LabelledMarqueeEditText loanEntryDueDate;
    @Bind(R.id.loan_entry_due_date_surface) View loanEntryDueDateSurface;
    @Bind(R.id.loan_entry_save_button) Button loanEntrySaveButton;

    private String targetLoanEntryId;
    private String rawLoanEntryCreationDate;
    private String rawLoanEntryDueDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loan_entry);
        ButterKnife.bind(this);
        checkActivityMode(savedInstanceState);
        if (targetLoanEntryId == null) {
            setTitle(getString(R.string.label_register_actiity));
            DateTime dateTime = new DateTime();
            rawLoanEntryCreationDate = dateTime.toString();
            loanEntryCreationDate.setText(Utils.formatDate(dateTime));
        } else {
            setTitle(getString(R.string.label_edit_loan_entry_activity));
            refreshLoanEntryInformation();
        }
        loanEntryCreationDate.setEnabled(false);
        loanEntryCreationDateSurface.setOnClickListener(null);
        loanEntryTitle.setTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable editable) {
                validateSaveButton();
            }

        });
        loanEntryObservations.setTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable editable) {
                validateSaveButton();
            }

        });
        loanEntryDueDate.setTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable editable) {
                validateSaveButton();
            }

        });
        loanEntryDueDateSurface.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                DateTime initial = new DateTime();
                if (!TextUtils.isEmpty(loanEntryDueDate.getText())) {
                    initial = DateTime.parse(rawLoanEntryDueDate);
                }
                DatePickerDialog dialog = new DatePickerDialog(LoanEntryActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                        DateTime dateTime = new DateTime();
                        dateTime = dateTime.withYear(year);
                        dateTime = dateTime.withMonthOfYear(monthOfYear + 1);
                        dateTime = dateTime.withDayOfMonth(dayOfMonth);
                        rawLoanEntryDueDate = dateTime.toString();
                        loanEntryDueDate.setText(Utils.formatDate(dateTime));
                    }

                }, initial.getYear(), initial.getMonthOfYear() - 1, initial.getDayOfMonth());
                dialog.show();
            }

        });
        loanEntrySaveButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                LoanEntry loanEntry;
                if (targetLoanEntryId == null) {
                    loanEntry = new LoanEntry();
                } else {
                    loanEntry = new Select().from(LoanEntry.class)
                        .where("loanEntryId = ?", targetLoanEntryId).executeSingle();
                }
                loanEntry.setTitle(loanEntryTitle.getText());
                loanEntry.setObservations(loanEntryObservations.getText());
                loanEntry.setCreationDate(DateTime.parse(rawLoanEntryCreationDate));
                loanEntry.setDueDate(DateTime.parse(rawLoanEntryDueDate));
                loanEntry.save();
                finish();
            }

        });
    }

    private void validateSaveButton() {
        if (TextUtils.isEmpty(loanEntryTitle.getText())
                || TextUtils.isEmpty(loanEntryObservations.getText())
                || TextUtils.isEmpty(loanEntryDueDate.getText())) {
            loanEntrySaveButton.setEnabled(false);
        } else {
            loanEntrySaveButton.setEnabled(true);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(BUNDLE_KEY_TARGET_LOAN_ENTRY_ID, targetLoanEntryId);
        outState.putString(BUNDLE_KEY_RAW_LOAN_ENTRY_CREATION_DATE, rawLoanEntryCreationDate);
        outState.putString(BUNDLE_KEY_RAW_LOAN_ENTRY_DUE_DATE, rawLoanEntryDueDate);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        checkActivityMode(null);
        if (targetLoanEntryId != null) {
            getMenuInflater().inflate(R.menu.loan_entry, menu);
            menu.getItem(0).setIcon(new IconDrawable(this, "md-refresh")
                .actionBarSize().colorRes(android.R.color.white));
            menu.getItem(1).setIcon(new IconDrawable(this, "md-delete")
                .actionBarSize().colorRes(android.R.color.white));
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        checkActivityMode(null);
        if (targetLoanEntryId == null) {
            return true;
        }
        int id = item.getItemId();
        if (id == R.id.action_loan_entry_reset) {
            refreshLoanEntryInformation();
            loanEntryCreationDate.setEnabled(false);
            loanEntryCreationDateSurface.setOnClickListener(null);
            return true;
        } else if (id == R.id.action_loan_entry_remove) {
            LoanEntry loanEntry = new Select().from(LoanEntry.class)
                .where("loanEntryId = ?", targetLoanEntryId).executeSingle();
            loanEntry.delete();
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void checkActivityMode(Bundle savedInstanceState) {
        final Bundle bundle;
        if (getIntent() != null) {
            bundle = getIntent().getExtras();
        } else if (savedInstanceState != null) {
            bundle = savedInstanceState;
        } else {
            bundle = null;
        }
        if (bundle != null) {
            targetLoanEntryId = bundle.getString(BUNDLE_KEY_TARGET_LOAN_ENTRY_ID);
        }
    }

    private void refreshLoanEntryInformation() {
        LoanEntry loanEntry = new Select().from(LoanEntry.class)
            .where("loanEntryId = ?", targetLoanEntryId).executeSingle();
        Log.wtf("TAG", "loanEntryTitle: " + loanEntryTitle);
        loanEntryTitle.setText(loanEntry.getTitle());
        loanEntryObservations.setText(loanEntry.getObservations());
        rawLoanEntryCreationDate = loanEntry.getCreationDate().toString();
        loanEntryCreationDate.setText(Utils.formatDate(loanEntry.getCreationDate()));
        rawLoanEntryDueDate = loanEntry.getDueDate().toString();
        loanEntryDueDate.setText(Utils.formatDate(loanEntry.getDueDate()));
    }

}
