package test;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * Created by hedingwei on 16/10/2017.
 */
@Controller
public class CommandController {


    @RequestMapping(value = "/helloworld",method = RequestMethod.POST)
    @ResponseBody
    public String cmd(@RequestBody String body) throws Exception {
        return "hello world:\n"+body;
    }

    @RequestMapping(value = "/helloworld/{type}/{name}",method = RequestMethod.GET)
    @ResponseBody
    public String cmdget(@PathVariable("type")String type,@PathVariable("name")String name) throws Exception {
        return "hello world:\n"+type+"/"+name;
    }


}
