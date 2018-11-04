package healthskillservicecontroller;

/**
 * Created by lujingyang on 4/11/18.
 */
import com.opencsv.bean.CsvBindByName;

public class Customer {
    @CsvBindByName(column = "id")
    private String id;

    @CsvBindByName(column = "postcode")
    private String postcode;

    @CsvBindByName(column = "age")
    private String age;

    @CsvBindByName(column = "gender")
    private String gender;

    @CsvBindByName(column = "has_provider")
    private String hasProvider;

    @CsvBindByName(column = "has_children")
    private String hasChildren;

    @CsvBindByName(column = "marital_status")
    private String maritalStatus;

    @CsvBindByName(column = "cover_type")
    private String coverType;

    public String getId() {
        return id;
    }

    public String getAge() {
        return age;
    }

    public String isHasChildren() {
        return hasChildren;
    }

    public String isHasProvider() {
        return hasProvider;
    }

    public String getCoverType() {
        return coverType;
    }

    public String getGender() {
        return gender;
    }

    public String getMaritalStatus() {
        return maritalStatus;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public void setCoverType(String coverType) {
        this.coverType = coverType;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setHasChildren(String hasChildren) {
        this.hasChildren = hasChildren;
    }

    public void setHasProvider(String hasProvider) {
        this.hasProvider = hasProvider;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setMaritalStatus(String maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public Customer(String id, String postcode, String age, String gender, String hasProvider, String hasChildren, String maritalStatus, String coverType){
        this.id = id;
        this.postcode = postcode;
        this.age = age;
        this.gender = gender;
        this.coverType = coverType;
        this.hasChildren = hasChildren;
        this.hasProvider = hasProvider;
        this.maritalStatus = maritalStatus;
    }

    @Override
    public String toString() {
        return "ID is: "+ this.id + " and age is: "+ this.age + " and gender is: "+ this.gender;
    }
}
