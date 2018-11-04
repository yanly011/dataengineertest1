package healthskillservicecontroller;

/**
 * Created by lujingyang on 4/11/18.
 */
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class HealtheSkillServiceController {
    @RequestMapping(value="/upload", method= RequestMethod.GET)
    public String UploadFile(){
        return "upload";
    }
}
