package com.gzz100.zbh.home.appointment.entity;

import com.gzz100.zbh.data.entity.Staff;

/**
 * Created by Lam on 2018/2/7.
 */

public class Agenda {
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
}
