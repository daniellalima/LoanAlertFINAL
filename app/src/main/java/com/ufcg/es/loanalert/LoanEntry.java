package com.ufcg.es.loanalert;

import android.os.Parcel;
import android.os.Parcelable;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import org.joda.time.DateTime;

import java.util.Random;

@SuppressWarnings("WeakerAccess")
@Table(name = "LoanEntry")
class LoanEntry extends Model implements Parcelable {

    @Column(unique = true, onUniqueConflict = Column.ConflictAction.REPLACE) private int loanEntryId;
    @Column private String title;
    @Column private String observations;
    @Column private Long creationDate;
    @Column private Long dueDate;

    public LoanEntry() {
        loanEntryId = new Random().nextInt(Integer.MAX_VALUE);
    }

    protected LoanEntry(Parcel in) {
        loanEntryId = in.readInt();
        title = in.readString();
        observations = in.readString();
        creationDate = in.readLong();
        dueDate = in.readLong();
    }

    public static final Creator<LoanEntry> CREATOR = new Creator<LoanEntry>() {
        @Override
        public LoanEntry createFromParcel(Parcel in) {
            return new LoanEntry(in);
        }

        @Override
        public LoanEntry[] newArray(int size) {
            return new LoanEntry[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(loanEntryId);
        parcel.writeString(title);
        parcel.writeString(observations);
        parcel.writeLong(creationDate);
        parcel.writeLong(dueDate);
    }

    int getLoanEntryId() {
        return loanEntryId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    String getObservations() {
        return observations;
    }

    void setObservations(String observations) {
        this.observations = observations;
    }

    DateTime getCreationDate() {
        return new DateTime(creationDate);
    }

    void setCreationDate(DateTime creationDate) {
        this.creationDate = creationDate.getMillis();
    }

    DateTime getDueDate() {
        return new DateTime(dueDate);
    }

    void setDueDate(DateTime dueDate) {
        this.dueDate = dueDate.getMillis();
    }

}
