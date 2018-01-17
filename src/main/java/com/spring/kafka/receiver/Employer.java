package com.spring.kafka.receiver;

/**
 * Created by sureshpasupuleti on 1/14/18.
 */
public class Employer {
    private String key;
    private String firstName;
    private String LastName;
    private String phoneNumber;
    private String employeeEmailAddress;
    private String groupEmailAddress;
    private String location;
    private String signedOnHarrassementPolicy;
    private String signedOnEmployeeAgreement;
    private String enrolledForMedicalInsurance;
    private String enrolledForBenefits;


    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }



    public String getSignedOnHarrassementPolicy() {
        return signedOnHarrassementPolicy;
    }

    public void setSignedOnHarrassementPolicy(String signedOnHarrassementPolicy) {
        this.signedOnHarrassementPolicy = signedOnHarrassementPolicy;
    }

    public String getSignedOnEmployeeAgreement() {
        return signedOnEmployeeAgreement;
    }

    public void setSignedOnEmployeeAgreement(String signedOnEmployeeAgreement) {
        this.signedOnEmployeeAgreement = signedOnEmployeeAgreement;
    }

    public String getEnrolledForMedicalInsurance() {
        return enrolledForMedicalInsurance;
    }

    public void setEnrolledForMedicalInsurance(String enrolledForMedicalInsurance) {
        this.enrolledForMedicalInsurance = enrolledForMedicalInsurance;
    }

    public String getEnrolledForBenefits() {
        return enrolledForBenefits;
    }

    public void setEnrolledForBenefits(String enrolledForBenefits) {
        this.enrolledForBenefits = enrolledForBenefits;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getEmployeeEmailAddress() {
        return employeeEmailAddress;
    }

    public void setEmployeeEmailAddress(String employeeEmailAddress) {
        this.employeeEmailAddress = employeeEmailAddress;
    }

    public String getGroupEmailAddress() {
        return groupEmailAddress;
    }

    public void setGroupEmailAddress(String groupEmailAddress) {
        this.groupEmailAddress = groupEmailAddress;
    }
}
