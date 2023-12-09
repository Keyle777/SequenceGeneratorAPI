package keyle.fun.SequenceGeneratorAPI.controller;

import keyle.fun.SequenceGeneratorAPI.service.Sequence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/api/sequence")
public class SequenceNumberController {

    @Qualifier("databaseSequenceServiceImpl")
    @Resource
    private Sequence sequence1;

    @Qualifier("redisSequenceServiceImpl")
    @Resource
    private Sequence sequence2;

    @Qualifier("snowflakeSequenceServiceImpl")
    @Resource
    private Sequence sequence3;

    @GetMapping("/databaseSequenceServiceImpl")
    public String getNextSequenceNumber1() {
        return sequence1.nextNo();
    }

    @GetMapping("/redisSequenceServiceImpl")
    public String getNextSequenceNumber2() {
        return sequence2.nextNo();
    }

    @GetMapping("/snowflakeSequenceServiceImpl")
    public String getNextSequenceNumber3() {
        return sequence3.nextNo();
    }
}
