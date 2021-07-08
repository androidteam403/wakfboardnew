package com.thresholdsoft.wakfboard.ui.mainactivity.fragments.surveylistfrag.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class FarmersResponse implements Serializable {
    @SerializedName("message")
    public String message;
    @SerializedName("success")
    public boolean success;
    @SerializedName("zcServerIp")
    public String zcServerIp;
    @SerializedName("zcServerHost")
    public String zcServerHost;
    @SerializedName("zcServerDateTime")
    public String zcServerDateTime;
    @SerializedName("data")
    public Data data;

    public String getMessage() {
        return message;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getZcServerIp() {
        return zcServerIp;
    }

    public String getZcServerHost() {
        return zcServerHost;
    }

    public String getZcServerDateTime() {
        return zcServerDateTime;
    }

    public Data getData() {
        return data;
    }

    public class Data implements Serializable{
        @SerializedName("listData")
        public ListData listData;

        public class ListData implements Serializable{
            @SerializedName("zc_extra")
            public String zc_extra;
            @SerializedName("total")
            public int total;
            @SerializedName("size")
            public int size;
            @SerializedName("select")
            public boolean select;
            @SerializedName("rows")
            public List<Rows> rows = new ArrayList<Rows>();
            @SerializedName("records")
            public String records;
            @SerializedName("pivotData")
            public String pivotData;
            @SerializedName("page")
            public int page;
            @SerializedName("aggregation")
            public String aggregation;

            public String getZc_extra() {
                return zc_extra;
            }

            public int getTotal() {
                return total;
            }

            public int getSize() {
                return size;
            }

            public boolean isSelect() {
                return select;
            }

            public List<Rows> getRows() {
                return rows;
            }

            public String getRecords() {
                return records;
            }

            public String getPivotData() {
                return pivotData;
            }

            public int getPage() {
                return page;
            }

            public String getAggregation() {
                return aggregation;
            }

            public class Rows implements Serializable{
                @SerializedName("uid")
                public String uid;
                @SerializedName("pic")
                public List<Pic> pic = new ArrayList<Pic>();
                @SerializedName("nri_zip")
                public String nri_zip;
                @SerializedName("nri_state")
                public String nri_state;
                @SerializedName("nri_email")
                public String nri_email;
                @SerializedName("nri_country")
                public String nri_country;
                @SerializedName("nri_contactno")
                public String nri_contactno;
                @SerializedName("nri_city")
                public String nri_city;
                @SerializedName("nri_address")
                public String nri_address;
                @SerializedName("nri")
                public Nri nri;
                @SerializedName("name")
                public String name;
                @SerializedName("mobile_verifies")
                public MobileVerifies mobile_verifies;
                @SerializedName("mobile")
                public long mobile;
                @SerializedName("marital_status")
                public MaritalStatus maritalStatus;
                @SerializedName("gender")
                public Gender gender;
                @SerializedName("farmer_type")
                public FarmerType farmer_type;
                @SerializedName("email_verified")
                public EmailVerified email_verified;
                @SerializedName("email")
                public String email;
                @SerializedName("age")
                public int age;
                private int surveyType;

                public int getSurveyType() {
                    return surveyType;
                }

                public void setSurveyType(int surveyType) {
                    this.surveyType = surveyType;
                }

                public String getUid() {
                    return uid;
                }

                public List<Pic> getPic() {
                    return pic;
                }

                public String getNri_zip() {
                    return nri_zip;
                }

                public String getNri_state() {
                    return nri_state;
                }

                public String getNri_email() {
                    return nri_email;
                }

                public String getNri_country() {
                    return nri_country;
                }

                public String getNri_contactno() {
                    return nri_contactno;
                }

                public String getNri_city() {
                    return nri_city;
                }

                public String getNri_address() {
                    return nri_address;
                }

                public Nri getNri() {
                    return nri;
                }

                public String getName() {
                    return name;
                }

                public MobileVerifies getMobile_verifies() {
                    return mobile_verifies;
                }

                public long getMobile() {
                    return mobile;
                }

                public MaritalStatus getMaritalStatus() {
                    return maritalStatus;
                }

                public Gender getGender() {
                    return gender;
                }

                public FarmerType getFarmer_type() {
                    return farmer_type;
                }

                public EmailVerified getEmail_verified() {
                    return email_verified;
                }

                public String getEmail() {
                    return email;
                }

                public int getAge() {
                    return age;
                }

                public class Pic {

                }

                public class Nri implements Serializable{
                    @SerializedName("uid")
                    public String uid;
                    @SerializedName("other")
                    public Other other;
                    @SerializedName("name")
                    public String name;
                    @SerializedName("icon")
                    public String icon;

                    public String getUid() {
                        return uid;
                    }

                    public Other getOther() {
                        return other;
                    }

                    public String getName() {
                        return name;
                    }

                    public String getIcon() {
                        return icon;
                    }

                    public class Other implements Serializable{
                        @SerializedName("color")
                        public String color;

                        public String getColor() {
                            return color;
                        }
                    }
                }

                public class MobileVerifies implements Serializable{
                    @SerializedName("uid")
                    public String uid;
                    @SerializedName("other")
                    public Other other;
                    @SerializedName("name")
                    public String name;
                    @SerializedName("icon")
                    public String icon;

                    public String getUid() {
                        return uid;
                    }

                    public Other getOther() {
                        return other;
                    }

                    public String getName() {
                        return name;
                    }

                    public String getIcon() {
                        return icon;
                    }

                    public class Other implements Serializable{
                        @SerializedName("color")
                        public String color;

                        public String getColor() {
                            return color;
                        }
                    }
                }

                public class MaritalStatus implements Serializable{
                    @SerializedName("uid")
                    public String uid;
                    @SerializedName("other")
                    public Other other;
                    @SerializedName("name")
                    public String name;
                    @SerializedName("icon")
                    public String icon;

                    public String getUid() {
                        return uid;
                    }

                    public Other getOther() {
                        return other;
                    }

                    public String getName() {
                        return name;
                    }

                    public String getIcon() {
                        return icon;
                    }

                    public class Other implements Serializable{
                        @SerializedName("color")
                        public String color;

                        public String getColor() {
                            return color;
                        }
                    }
                }

                public class Gender implements Serializable{
                    @SerializedName("uid")
                    public String uid;
                    @SerializedName("other")
                    public Other other;
                    @SerializedName("name")
                    public String name;
                    @SerializedName("icon")
                    public String icon;

                    public String getUid() {
                        return uid;
                    }

                    public Other getOther() {
                        return other;
                    }

                    public String getName() {
                        return name;
                    }

                    public String getIcon() {
                        return icon;
                    }

                    public class Other implements Serializable{
                        @SerializedName("color")
                        public String color;

                        public String getColor() {
                            return color;
                        }
                    }
                }

                public class FarmerType implements Serializable{
                    @SerializedName("uid")
                    public String uid;
                    @SerializedName("other")
                    public Other other;
                    @SerializedName("name")
                    public String name;
                    @SerializedName("icon")
                    public String icon;

                    public String getUid() {
                        return uid;
                    }

                    public Other getOther() {
                        return other;
                    }

                    public String getName() {
                        return name;
                    }

                    public String getIcon() {
                        return icon;
                    }

                    public class Other implements Serializable{
                        @SerializedName("color")
                        public String color;

                        public String getColor() {
                            return color;
                        }
                    }
                }

                public class EmailVerified implements Serializable{
                    @SerializedName("uid")
                    public String uid;
                    @SerializedName("other")
                    public Other other;
                    @SerializedName("name")
                    public String name;
                    @SerializedName("icon")
                    public String icon;

                    public String getUid() {
                        return uid;
                    }

                    public Other getOther() {
                        return other;
                    }

                    public String getName() {
                        return name;
                    }

                    public String getIcon() {
                        return icon;
                    }

                    public class Other implements Serializable{
                        @SerializedName("color")
                        public String color;

                        public String getColor() {
                            return color;
                        }
                    }
                }
            }
        }

        @SerializedName("zcDebugLogs")
        public ZcDebugLogs zcDebugLogs;

        public class ZcDebugLogs implements Serializable{
            @SerializedName("1")
            public List<LogsData> logsData = new ArrayList<LogsData>();

            public List<LogsData> getLogsData() {
                return logsData;
            }

            public class LogsData implements Serializable{
                @SerializedName("time")
                public String time;
                @SerializedName("place")
                public String place;
                @SerializedName("log")
                public String log;
                @SerializedName("context")
                public String context;

                public String getTime() {
                    return time;
                }

                public String getPlace() {
                    return place;
                }

                public String getLog() {
                    return log;
                }

                public String getContext() {
                    return context;
                }
            }
        }

        public ListData getListData() {
            return listData;
        }

        public ZcDebugLogs getZcDebugLogs() {
            return zcDebugLogs;
        }
    }
}
