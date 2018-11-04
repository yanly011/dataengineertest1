package healthskillservicecontroller;

/**
 * Created by lujingyang on 4/11/18.
 */
public class HealthSkillMap {
        private String customerId;
        private String skill;

        public HealthSkillMap(String id, String skill) {
            this.customerId = id;
            this.skill = skill;
        }

    public String getCustomerId() {
        return customerId;
    }

    public String getSkill() {
        return skill;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public void setSkill(String skill) {
        this.skill = skill;
    }
}
