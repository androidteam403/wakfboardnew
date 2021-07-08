package com.thresholdsoft.wakfboard.ui.mainactivity.fragments.surveylistfrag.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class FarmersRequest implements Serializable {
    @SerializedName("rows")
    public List<Rows> rows = new ArrayList<Rows>();

    public class Rows {
        @SerializedName("age")
        public String age;
        @SerializedName("email")
        public String email;
        @SerializedName("email_verified")
        public EmailVerified email_verified;
        @SerializedName("farmer_type")
        public FarmerType farmer_type;
        @SerializedName("gender")
        public Gender gender;
        @SerializedName("marital_status")
        public MaritalStatus marital_status;
        @SerializedName("mobile")
        public String mobile;
        @SerializedName("mobile_verifies")
        public MobileVerifies mobile_verifies;
        @SerializedName("name")
        public String name;
        @SerializedName("nri")
        public Nri nri;
        @SerializedName("nri_address")
        public String nri_address;
        @SerializedName("nri_city")
        public String nri_city;
        @SerializedName("nri_contactno")
        public String nri_contactno;
        @SerializedName("nri_country")
        public String nri_country;
        @SerializedName("nri_email")
        public String nri_email;
        @SerializedName("nri_state")
        public String nri_state;
        @SerializedName("nri_zip")
        public String nri_zip;
        @SerializedName("pic")
        public String pic;
        @SerializedName("uid")
        public String uid;

        public String getAge() {
            return age;
        }

        public void setAge(String age) {
            this.age = age;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public EmailVerified getEmail_verified() {
            return email_verified;
        }

        public void setEmail_verified(EmailVerified email_verified) {
            this.email_verified = email_verified;
        }

        public FarmerType getFarmer_type() {
            return farmer_type;
        }

        public void setFarmer_type(FarmerType farmer_type) {
            this.farmer_type = farmer_type;
        }

        public Gender getGender() {
            return gender;
        }

        public void setGender(Gender gender) {
            this.gender = gender;
        }

        public MaritalStatus getMarital_status() {
            return marital_status;
        }

        public void setMarital_status(MaritalStatus marital_status) {
            this.marital_status = marital_status;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public MobileVerifies getMobile_verifies() {
            return mobile_verifies;
        }

        public void setMobile_verifies(MobileVerifies mobile_verifies) {
            this.mobile_verifies = mobile_verifies;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Nri getNri() {
            return nri;
        }

        public void setNri(Nri nri) {
            this.nri = nri;
        }

        public String getNri_address() {
            return nri_address;
        }

        public void setNri_address(String nri_address) {
            this.nri_address = nri_address;
        }

        public String getNri_city() {
            return nri_city;
        }

        public void setNri_city(String nri_city) {
            this.nri_city = nri_city;
        }

        public String getNri_contactno() {
            return nri_contactno;
        }

        public void setNri_contactno(String nri_contactno) {
            this.nri_contactno = nri_contactno;
        }

        public String getNri_country() {
            return nri_country;
        }

        public void setNri_country(String nri_country) {
            this.nri_country = nri_country;
        }

        public String getNri_email() {
            return nri_email;
        }

        public void setNri_email(String nri_email) {
            this.nri_email = nri_email;
        }

        public String getNri_state() {
            return nri_state;
        }

        public void setNri_state(String nri_state) {
            this.nri_state = nri_state;
        }

        public String getNri_zip() {
            return nri_zip;
        }

        public void setNri_zip(String nri_zip) {
            this.nri_zip = nri_zip;
        }

        public String getPic() {
            return pic;
        }

        public void setPic(String pic) {
            this.pic = pic;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public class EmailVerified {
            @SerializedName("name")
            public String name;
            @SerializedName("uid")
            public String uid;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getUid() {
                return uid;
            }

            public void setUid(String uid) {
                this.uid = uid;
            }
        }

        public class FarmerType {
            @SerializedName("name")
            public String name;
            @SerializedName("uid")
            public String uid;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getUid() {
                return uid;
            }

            public void setUid(String uid) {
                this.uid = uid;
            }
        }

        public class Gender {
            @SerializedName("name")
            public String name;
            @SerializedName("uid")
            public String uid;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getUid() {
                return uid;
            }

            public void setUid(String uid) {
                this.uid = uid;
            }
        }

        public class MaritalStatus {
            @SerializedName("name")
            public String name;
            @SerializedName("uid")
            public String uid;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getUid() {
                return uid;
            }

            public void setUid(String uid) {
                this.uid = uid;
            }
        }

        public class MobileVerifies {
            @SerializedName("name")
            public String name;
            @SerializedName("uid")
            public String uid;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getUid() {
                return uid;
            }

            public void setUid(String uid) {
                this.uid = uid;
            }
        }

        public class Nri {
            @SerializedName("name")
            public String name;
            @SerializedName("uid")
            public String uid;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getUid() {
                return uid;
            }

            public void setUid(String uid) {
                this.uid = uid;
            }
        }
    }
}