package shift.scheduler.backend.payload;

import shift.scheduler.backend.model.Company;
import shift.scheduler.backend.model.Role;

public class RegistrationRequest {

    private Role role;

    private String username;
    private String name;
    private String password;

    private Company company;

    // Employee-specific fields
    private Short minHoursPerDay;
    private Short maxHoursPerDay;
    private Short minHoursPerWeek;
    private Short maxHoursPerWeek;

    public RegistrationRequest() {}

    public RegistrationRequest(Role role, String username, String name, String password) {
        this.role = role;
        this.username = username;
        this.name = name;
        this.password = password;
    }

    public RegistrationRequest(Role role, String username, String name, String password, Short minHoursPerDay, Short maxHoursPerDay, Short minHoursPerWeek, Short maxHoursPerWeek) {
        this.role = role;
        this.username = username;
        this.name = name;
        this.password = password;
        this.minHoursPerDay = minHoursPerDay;
        this.maxHoursPerDay = maxHoursPerDay;
        this.minHoursPerWeek = minHoursPerWeek;
        this.maxHoursPerWeek = maxHoursPerWeek;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public Short getMinHoursPerDay() {
        return minHoursPerDay;
    }

    public void setMinHoursPerDay(Short minHoursPerDay) {
        this.minHoursPerDay = minHoursPerDay;
    }

    public Short getMaxHoursPerDay() {
        return maxHoursPerDay;
    }

    public void setMaxHoursPerDay(Short maxHoursPerDay) {
        this.maxHoursPerDay = maxHoursPerDay;
    }

    public Short getMinHoursPerWeek() {
        return minHoursPerWeek;
    }

    public void setMinHoursPerWeek(Short minHoursPerWeek) {
        this.minHoursPerWeek = minHoursPerWeek;
    }

    public Short getMaxHoursPerWeek() {
        return maxHoursPerWeek;
    }

    public void setMaxHoursPerWeek(Short maxHoursPerWeek) {
        this.maxHoursPerWeek = maxHoursPerWeek;
    }
}
