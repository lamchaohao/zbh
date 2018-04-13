package com.gzz100.zbh.home.appointment.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.gzz100.zbh.data.entity.Staff;

/**
 * Created by Lam on 2018/2/7.
 */

public class Agenda implements Parcelable {
    private String agendaName;
    private Staff staff;

    public String getAgendaName() {
        return agendaName;
    }

    public void setAgendaName(String agendaName) {
        this.agendaName = agendaName;
    }

    public Staff getStaff() {
        return staff;
    }

    public void setStaff(Staff staff) {
        this.staff = staff;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.agendaName);
        dest.writeParcelable(this.staff, flags);
    }

    public Agenda() {
    }

    protected Agenda(Parcel in) {
        this.agendaName = in.readString();
        this.staff = in.readParcelable(Staff.class.getClassLoader());
    }

    public static final Parcelable.Creator<Agenda> CREATOR = new Parcelable.Creator<Agenda>() {
        @Override
        public Agenda createFromParcel(Parcel source) {
            return new Agenda(source);
        }

        @Override
        public Agenda[] newArray(int size) {
            return new Agenda[size];
        }
    };
}
