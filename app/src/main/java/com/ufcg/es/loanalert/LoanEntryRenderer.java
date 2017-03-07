package com.ufcg.es.loanalert;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.joanzapata.iconify.IconDrawable;
import com.pedrogomez.renderers.Renderer;

import butterknife.Bind;
import butterknife.ButterKnife;

class LoanEntryRenderer extends Renderer<LoanEntry> {

    private final Context context;

    @Bind(R.id.loan_entry_surface) LinearLayout loanEntrySurface;
    @Bind(R.id.loan_entry_title) TextView loanEntryTitle;
    @Bind(R.id.loan_entry_due_date) TextView loanEntryDueDate;
    @Bind(R.id.loan_entry_due_date_icon) ImageView loanEntryDueDateIcon;

    LoanEntryRenderer(Context context) {
        this.context = context;
    }

    @Override
    protected void setUpView(View view) {}

    @Override
    protected void hookListeners(View view) {}

    @Override
    protected View inflate(LayoutInflater inflater, ViewGroup parent) {
        View inflatedView = inflater.inflate(R.layout.loan_entry_item, parent, false);
        ButterKnife.bind(this, inflatedView);
        loanEntryDueDateIcon.setImageDrawable((new IconDrawable(context, "md-alarm")
            .actionBarSize().colorRes(R.color.colorAccent)));
        return inflatedView;
    }

    @Override
    public void render() {
        final LoanEntry loanEntry = getContent();
        loanEntryTitle.setText(loanEntry.getTitle());
        loanEntryDueDate.setText(Utils.formatDate(loanEntry.getDueDate()));
        loanEntrySurface.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, LoanEntryActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra(LoanEntryActivity.BUNDLE_KEY_TARGET_LOAN_ENTRY_ID,
                    Integer.toString(loanEntry.getLoanEntryId()));
                context.startActivity(intent);
            }

        });
    }

}
